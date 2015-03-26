/*
 * 	3/2/2015
 * 
 * 	Basically a copy of TaskpoolLIFOWorkStealing.
 * 	However, before enqueuing, will check the current registering thread and
 * 	will determine if the queue that the thread will be assigned to is capable of continuing with enqueuing.
 * 	If the queue is too full, then it will automatically stop enqueuing until it hits a certain threshold
 * 	(Similiar to the global counter for each queue instead). Note that it will only affect the queue that is
 * 	currently full.
 */

package pt.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TaskpoolLIFOWorkFirst extends TaskpoolLIFOWorkStealing {
	
	/**
	 * 	Constants for determining whether the local task queue has reached its threshold.
	 */
	
	
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
		//System.out.println("HELP ME");
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
			
			/**
			 * 	Instead of just enqueuing onto the local task queue, the population of the task queue will determine
			 * 	whether enqueuing will occur as usual, or if the task queue is too full, will be forced to
			 * 	manually execute the thread.
			 */
			
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
					
					//If size exceeds the threshold, then will process sequentially
					if(localOneoffTaskQueues.get(tid).size() >= threshold) {
						TaskInfo taskInfo = taskID.getTaskInfo();
						Method m = taskInfo.getMethod();
						try {
							/*
							 * 	Use of a non-generic TaskID to allow the use of a setReturnResult() without
							 * 	using objects for parameterisation.
							 */
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
					} else {
						//localQueues[tid].addFirst(taskID);
						//localQueues.get(tid).addFirst(taskID);
						localOneoffTaskQueues.get(tid).addFirst(taskID);
					}
				}else {
					//-- just add it to a random worker thread's queue.. (Add it at the end, since it's not hot in that thread's queue)
					
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
					
					
					//If size exceeds the threshold, then will process sequentially
					if(localOneoffTaskQueues.get(randThread).size() >= threshold) {
						TaskInfo taskInfo = taskID.getTaskInfo();
						Method m = taskInfo.getMethod();
						try {
							/*
							 * 	Use of a non-generic TaskID to allow the use of a setReturnResult() without
							 * 	using objects for parameterisation.
							 */
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
					} else {
						//localQueues[randThread].addLast(taskID);
						//localQueues.get(randThread).addLast(taskID);
						localOneoffTaskQueues.get(randThread).addLast(taskID);
					}
				}
			} else {
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
				
				
				//If size exceeds the threshold, then will process sequentially
				if(localOneoffTaskQueues.get(randThread).size() >= threshold) {
					TaskInfo taskInfo = taskID.getTaskInfo();
					Method m = taskInfo.getMethod();
					try {
						/*
						 * 	Use of a non-generic TaskID to allow the use of a setReturnResult() without
						 * 	using objects for parameterisation.
						 */
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
				} else {
					//System.out.println("oneoffTaskThreadPoolSize is " + oneoffTaskThreadPoolSize + " randThread is " + randThread);
					
					//localQueues[randThread].addLast(taskID);
					//localQueues.get(randThread).addLast(taskID);
					localOneoffTaskQueues.get(randThread).addLast(taskID);
				}
			}
		}
	}
}
