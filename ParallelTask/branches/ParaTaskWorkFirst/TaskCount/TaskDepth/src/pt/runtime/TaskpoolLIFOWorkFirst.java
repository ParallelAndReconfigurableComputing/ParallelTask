package pt.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 
 * @author Weng
 * 
 * 	This WorkFirst implementation will be based on the depth of the task hierarchy.
 * 	Each task will be assigned a number which will correspond to the depth of the task itself,
 * 	representing a tree-like structure.
 * 	When a certain depth has been reached, then the WorkFirst system will stop all enqueuing.
 *
 */

public class TaskpoolLIFOWorkFirst extends TaskpoolLIFOWorkStealing {
	
	//Task Depth threshold
	//private int taskDepthThreshold = 8;
	
	
	
	
	/*
	 * Creates a TaskID for the specified task (whose details are contained in the TaskInfo). It then returns the TaskID after 
	 * the task has been queued. If the depth of a task exceeds the threshold, tasks are no longer queued and are executed
	 * sequentially. 
	 * This method is generic and schedule-specific to Work-First. 
	 */
	public TaskID enqueue(TaskInfo taskinfo) {
		
		TaskID taskID = new TaskID(taskinfo);
		
		ArrayList<TaskID> allDependences = null;
		
		//-- determine if this task is being enqueued from within another task.. if so, set the enclosing task (needed to 
		//--		propogate exceptions to outer tasks (in case they have a suitable handler))
		Thread rt = taskinfo.setRegisteringThread();
		
		if (rt instanceof TaskThread) {
			TaskID parentTask = ((TaskThread)rt).currentExecutingTask();
			taskID.setEnclosingTask(parentTask);
			taskID.setTaskDepth(parentTask.getTaskDepth()+1);
		}
		
		if(ParaTask.getScheduleType() == ParaTask.ScheduleType.WorkFirst && taskID.getTaskDepth() >= taskDepthThreshold) {
			/*
			 * 	Directly extracts the method of the task to operate on the class sequentially.
			 * 	Also while invoking the sequential method of the task, the return result has also been set.
			 */
			try {
				//System.out.println("Sequential");
				Method m = taskinfo.getMethod();
				taskID.setReturnResult(m.invoke(taskinfo.getInstance(), taskinfo.getParameters()));
				m = null;
				
				/*
				 * 	Once successfully invoked, clean up the rest of the TaskID info.
				 */
				taskID.setComplete();
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else { 
			
			taskCounter.getAndIncrement();
			
			//System.out.println("Enqueue");
			if (taskinfo.getDependences() != null)
				allDependences = ParaTask.allTasksInList(taskinfo.getDependences());
		
			if (taskinfo.hasAnySlots())
				taskinfo.setTaskIDForSlotsAndHandlers(taskID);
			
			if (taskID.isPipeline()) {
				//-- pipeline threads don't need to wait for dependencies
				startPipelineTask(taskID);
			} else if (allDependences == null) {
				if (taskID.isInteractive())
					startInteractiveTask(taskID);
				else
					enqueueReadyTask(taskID);
			} else {
				enqueueWaitingTask(taskID, allDependences);
			}
		}
		
		return taskID;
	}
	
	
	/**
	* When enqueuing a task in the <code>WorkStealing</code> policy, if the task is not able to be executed on any arbitrary thread,
	 * regardless of the type of enqueuing thread it will be enqueued to the <code>privateQueue</code> of the thread in charge of 
	 * executing that task. However, if a task <b>can be executed by arbitrary threads</b>, then if the task is a <code>TaskIDGroup</code> 
	 * it will be enqueued to the <code>globalMultiTask</code> queue, otherwise if the enqueuing thread is a <code>Worker Thread</code> 
	 * and <b>is not</b> a <code>MultiTaskWorker</code> thread, it will enqueue the task to the head of its local queue. For other
	 * cases where the enqueuing thread <b>is</b> a <code>MultiTaskWorker</code> thread or <b>is not</b> a <code>Worker Thread</code>, 
	 * the task will be enqueued to the tail of a random thread's <code>localQueue</code>.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  10/9/2014
	 * */
	@Override
	protected void enqueueReadyTask(TaskID<?> taskID) {
		if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK || taskID instanceof TaskIDGroup) {
			//-- this is a multi-task, so place it on that thread's local queue (if it is wrongly stolen, it then gets placed on private queue)
			
			/**
			 * 
			 * @Author : Kingsley
			 * @since : 25/04/2013
			 * The data structure is changed from array to list, therefore the corresponding way to
			 * get the data has to be changed.
			 * 
			 * This is a multi task, so insert it into local Multi Task queue.
			 * 
			 * @since 04/05/2013
			 * There are no local queues for multi task any more.
			 * 
			 * 
			 * */
			//localQueues[taskID.getExecuteOnThread()].add(taskID);
			//localQueues.get(taskID.getExecuteOnThread()).add(taskID);
			
			//localMultiTaskQueues.get(taskID.getExecuteOnThread()).add(taskID);
			if (taskID.getExecuteOnThread() == ParaTaskHelper.ANY_THREAD_TASK) {
				globalMultiTaskqueue.add(taskID);
			} else {
				privateQueues.get(taskID.getExecuteOnThread()).add(taskID);
			}
	
		} else {
			//-- this is a normal task. 
			
			//Get current queuing thread(Could be worker thread or non-worker thread)
			Thread regThread = taskID.getTaskInfo().getRegisteringThread();
			
			/**
			 * 
			 * @Author : Kingsley
			 * @since : 25/04/2013
			 * The data structure is changed from array to list, therefore the corresponding way to
			 * get the data has to be changed.
			 * 
			 * This is a one-off task, so insert it into local One-off Task queue if current
			 * worker thread is dedicated for One-off task.
			 * 
			 * */
			
			if (regThread instanceof WorkerThread) {
				//-- Add task to this thread's worker queue, at the beginning since it is the "hottest" task.
				
				WorkerThread workerThread = (WorkerThread) regThread;
				
				if (!workerThread.isMultiTaskWorker()) {
					
					
					/*if(taskID.getTaskDepth() >= taskDepthThreshold) {
						TaskInfo taskInfo = taskID.getTaskInfo();
						Method m = taskInfo.getMethod();
						try {
							
							 * 	Use of a non-generic TaskID to allow the use of a setReturnResult() without
							 * 	using objects for parameterisation.
							 
							TaskID taskID2 = taskID;
							taskID2.setReturnResult(m.invoke(taskInfo.getInstance(), taskInfo.getParameters()));
							taskID2.setComplete();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else */{
					
						/**
						 * 
						 * @Author : Kingsley
						 * @since : 26/04/2013
						 * 
						 * The worker thread here is not multi task worker, should use 
						 * getOneoffTaskThreadID() instead.
						 * 
						 * @since : 02/05/2013
						 * One-off task threads do not need local thread ID. Still use
						 * global id here.
						 * 
						 * */
						//int tid = workerThread.getThreadID();
						//int tid = workerThread.getOneoffTaskThreadID();
						int tid = workerThread.getThreadID();
	
						//localQueues[tid].addFirst(taskID);
						//localQueues.get(tid).addFirst(taskID);
						localOneoffTaskQueues.get(tid).addFirst(taskID);
					}
				}else {
					//-- just add it to a random worker thread's queue.. (Add it at the end, since it's not hot in that thread's queue)
					
					
					/*if(taskID.getTaskDepth() >= taskDepthThreshold) {
						TaskInfo taskInfo = taskID.getTaskInfo();
						Method m = taskInfo.getMethod();
						try {
							
							 * 	Use of a non-generic TaskID to allow the use of a setReturnResult() without
							 * 	using objects for parameterisation.
							 
							TaskID taskID2 = taskID;
							taskID2.setReturnResult(m.invoke(taskInfo.getInstance(), taskInfo.getParameters()));
							taskID2.setComplete();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else*/ {
					
					
						/**
						 * 
						 * @Author : Kingsley
						 * @since : 25/04/2013
						 * 
						 * Get a real worker ID array
						 * Get a real worker ID as well
						 * 
						 * @since 18/05/2013
						 * The variable of "numOneoffTaskThreads" is cancelled, whenever want the size
						 * of how many one-off task worker threads, call thread pool directly.
						 * 
						 * When queuing tasks, access thread pool, get the number of alive worker thread first,
						 * only give these threads tasks
						 * 
						 * @since 23/05/2013
						 * Re-structure the code
						 * */
						int oneoffTaskThreadPoolSize = ThreadPool.getOneoffTaskThreadPoolSize();
						Integer[] workIDs = localOneoffTaskQueues.keySet().toArray(new Integer[oneoffTaskThreadPoolSize]);
	
						//int randThread = (int)(Math.random()*numOneoffTaskThreads);
						int randThread = workIDs[(int)(Math.random()*oneoffTaskThreadPoolSize)];
	
						//localQueues[randThread].addLast(taskID);
						//localQueues.get(randThread).addLast(taskID);
						localOneoffTaskQueues.get(randThread).addLast(taskID);
						
					}
				}
			} else {
				
				
				/*if(taskID.getTaskDepth() >= taskDepthThreshold) {
					TaskInfo taskInfo = taskID.getTaskInfo();
					Method m = taskInfo.getMethod();
					try {
						
						 * 	Use of a non-generic TaskID to allow the use of a setReturnResult() without
						 * 	using objects for parameterisation.
						 
						TaskID taskID2 = taskID;
						taskID2.setReturnResult(m.invoke(taskInfo.getInstance(), taskInfo.getParameters()));
						taskID2.setComplete();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else*/ {
				
				
					//-- just add it to a random worker thread's queue.. (Add it at the end, since it's not hot in that thread's queue)
					/**
					 * 
					 * @Author Kingsley
					 * @since 25/04/2013
					 * 
					 * Get a real worker ID array
					 * Get a real worker ID as well
					 * 
					 * @since 18/05/2013
					 * The variable of "numOneoffTaskThreads" is cancelled, whenever want the size
					 * of how many one-off task worker threads, call thread pool directly.
					 * 
					 * When queuing tasks, access thread pool, get the number of alive worker thread first,
					 * only give these threads tasks
					 * 
					 * @since 23/05/2013
					 * Re-structure the code
					 * 
					 * */
					int oneoffTaskThreadPoolSize = ThreadPool.getOneoffTaskThreadPoolSize();
					
					Integer[] workIDs = localOneoffTaskQueues.keySet().toArray(new Integer[oneoffTaskThreadPoolSize]);
					
					//int randThread = (int)(Math.random()*numOneoffTaskThreads);
					int randThread = workIDs[(int)(Math.random()*oneoffTaskThreadPoolSize)];
					
					//System.out.println("oneoffTaskThreadPoolSize is " + oneoffTaskThreadPoolSize + " randThread is " + randThread);
					
					//localQueues[randThread].addLast(taskID);
					//localQueues.get(randThread).addLast(taskID);
					localOneoffTaskQueues.get(randThread).addLast(taskID);
				}
			}
		}
	}
}
