package pt.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskpoolLIFOWorkFirst extends TaskpoolLIFOWorkStealing {
	
	/*
	 * 	Used for the Work-First implementation.
	 * 	Boolean used to check if the Work-First is currently being used.
	 * 	The Counter is used to check if the threshold for the number of tasks have been met.
	 */
	protected boolean isWorkFirst = true; //TEMP - MAKE IT TOGGLABLE
	protected AtomicInteger workFirstCounter = new AtomicInteger(0);

	private int workFirstUpperThreshold = 40;//1400;
	private int workFirstLowerThreshold = 20;//700;
	private boolean isWorkFirstInPlace = false;
	
	
	/**
	 * 	@Override
	 * Creates a TaskID for the specified task (whose details are contained in the TaskInfo). The enqueuing process is dependent
	 * on the conditions of the Work-First thresholds. If the number of tasks queued exceeds the workFirstUpperThreshold,
	 * then tasks are no longer queued but executed sequentially - Otherwise tasks will be queued.
	 * It then returns the TaskID after the task has been queued or executed via Work-First conditions. 
	 * This method is Work-First schedule-specific. 
	 */
	public TaskID enqueue(TaskInfo taskinfo) {
		
		/*
		 * 	Configures thresholds based on upper and lower bounds.
		 */
		if(isWorkFirst) {
			//System.out.println(workFirstCounter.get());
			if(workFirstCounter.get() >= workFirstUpperThreshold)
				isWorkFirstInPlace = true;
			else if(workFirstCounter.get() <= workFirstLowerThreshold)
				isWorkFirstInPlace = false;
		}
		
		TaskID taskID = new TaskID(taskinfo);
		Thread rt = taskinfo.setRegisteringThread();
		
		/**
		 * 	When the Work-First is used, it will consider the work-first threshold and will stop enqueuing when
		 * 	the threshold has been reached.
		 * 	Instead of enqueuing, tasks will be sequentially processed instead.
		 */
				/**
				 * 	Steps:
				 * 	Get registering thread
				 * 	Check if it is a worker thread?
				 * 	If there is a current task:
				 * 		Set task to started or enqueued or something
				 * 		Add this task to the worker's local task queue - At the back
				 * 	Otherwise execute new tasks sequentially.
				 */


			/*
			 * 	Attempts to see if there's a task that this thread's currently working on.
			 * 	If so, then it attempts to enqueue this task to someone else instead.
			 */
		if(isWorkFirstInPlace) {
		
		
				//Thread registering stuff was here
				if (rt instanceof TaskThread) {
					TaskID currentTask = ((TaskThread)rt).currentExecutingTask();
	
					if(!currentTask.hasCompleted()) {
						//if(currentTask.hasCompleted())
						taskID.setEnclosingTask(currentTask); //Necessary??
						
						TaskInfo currentTaskInfo = currentTask.getTaskInfo();
						
						
						ArrayList<TaskID> allDependences = null;
						if (currentTaskInfo.getDependences() != null)
							allDependences = ParaTask.allTasksInList(currentTaskInfo.getDependences());				
									
						if (currentTaskInfo.hasAnySlots())
							currentTaskInfo.setTaskIDForSlotsAndHandlers(currentTask);
						
						if (currentTask.isPipeline()) {
							//-- pipeline threads don't need to wait for dependencies
							startPipelineTask(currentTask);
						} else if (allDependences == null) {
							if (currentTask.isInteractive())
								startInteractiveTask(currentTask);
							else
								enqueueReadyTask(currentTask);
						} else {
							enqueueWaitingTask(currentTask, allDependences);
						}
					}
					
					
					/*
					 * 	Attempts to execute task directly
					 */
					
					ArrayList<TaskID> allDependences = null;
					if (taskinfo.getDependences() != null)
						allDependences = ParaTask.allTasksInList(taskinfo.getDependences());
					
					if (rt instanceof TaskThread)
						taskID.setEnclosingTask(((TaskThread)rt).currentExecutingTask());
					
					if (taskinfo.hasAnySlots())
						taskinfo.setTaskIDForSlotsAndHandlers(taskID);
				
					//((TaskThread) rt).executeTask(taskID);
					try {
						
						Method m = taskinfo.getMethod();
						taskID.setReturnResult(m.invoke(taskinfo.getInstance(), taskinfo.getParameters()));
						
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
				}
		} else {
			ArrayList<TaskID> allDependences = null;
			if (taskinfo.getDependences() != null)
				allDependences = ParaTask.allTasksInList(taskinfo.getDependences());
			
			if (rt instanceof TaskThread)
				taskID.setEnclosingTask(((TaskThread)rt).currentExecutingTask());
			
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
					workFirstCounter.incrementAndGet();
					localOneoffTaskQueues.get(tid).addFirst(taskID);
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

					//localQueues[randThread].addLast(taskID);
					//localQueues.get(randThread).addLast(taskID);
					workFirstCounter.incrementAndGet();
					localOneoffTaskQueues.get(randThread).addLast(taskID);
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
				
				//System.out.println("oneoffTaskThreadPoolSize is " + oneoffTaskThreadPoolSize + " randThread is " + randThread);
				
				//localQueues[randThread].addLast(taskID);
				//localQueues.get(randThread).addLast(taskID);
				workFirstCounter.incrementAndGet();
				localOneoffTaskQueues.get(randThread).addLast(taskID);
			}
		}
	}
	
	
	/**
	 * Only worker threads can call this method.
	 * This method first checks if the worker thread is a multi-task worker thread. In that case, it will check the thread's
	 * <code>privateQueue</code>, and if a task is found, and the attempt for executing it is successful, the task will be 
	 * passed to the thread to execute.
	 * <br><br>
	 * However, if the thread's <code>privateQueue</code> is empty, the method searches through the <code>globalMultiTask</code>
	 * queue, expands each multi-task into its sub-tasks and enqueues them as <code>ready-to-execute</code> tasks. However, the
	 * thread will temporarily return without having anything to execute this time.
	 * <br><br>
	 * If the worker thread is not a multi-task worker thread, it is first attempted to poll a task from the head of that
	 * thread's <code>localOneOffTask</code> queue. If a task is found and the preliminary attempt for executing it is 
	 * successful, that task will be passed to the thread to execute.
	 * <br><br>
	 * However, if there are no tasks in the thread's <code>localOneOffTask</code> queue, the thread will try to steal a task 
	 * from the tail of another thread's <code>localOneOffTask</code> queue. Preferably, if there is a thread from which a
	 * task has been stolen already (AKA <b><i>victim thread</i></b>), we would like to steal from the same victim's 
	 * <code>localOneOffTask</code> queue. 
	 * <br><br>
	 * But if there isn't any previous victims for task stealing, starting from a random thread's <code>localOneOffTask</code>
	 * queue, we proceed through every thread's <code>localOneOffTask</code> queue (except for the current thread's own queue)
	 * and we look for a task to steal from the tail of that local queue. Once a task is found, and the preliminary attempt 
	 * for executing it is successful, that task will be passed to the thread, and that <code>localOneOffTask</code> queue's 
	 * corresponding thread will be remembered as the <b><i>victim thread</i></b>.
	 * <br><br>
	 * After all these processes, if there are still no tasks found, the <b><i>victim thread</i></b> will be set to <cod>null</code>
	 * and the method returns <code>null</code> indicating an unsuccessful attempt for polling a task.
	 * 
	 *  @author Mostafa Mehrabi
	 *  @since  14/9/2014
	 * */
	@Override
	public TaskID workerPollNextTask() {
		
		WorkerThread wt = (WorkerThread) Thread.currentThread();
		TaskID next = null;
		
		if (wt.isMultiTaskWorker()) {
			int workerID = wt.getThreadLocalID();
			
			next= privateQueues.get(workerID).poll();
			
			while (next != null) {
				if (next.executeAttempt()) {
					return next;
				} else {
					next.enqueueSlots(true);
					//-- task was successfully cancelled beforehand, therefore grab another task
					next = privateQueues.get(workerID).poll();
				}
			}
		}
			
		//if there were no tasks found in the privateQueue, then look into the globalMultiTask queue
		if (wt.isMultiTaskWorker()) {
			while ((next = globalMultiTaskqueue.poll()) != null) {
				// expand multi task
				int count = next.getCount();
				int currentMultiTaskThreadPool = ThreadPool.getMultiTaskThreadPoolSize();
				TaskInfo taskinfo = next.getTaskInfo();
				
				taskinfo.setSubTask(true);
				
				for (int i = 0; i < count; i++) {
					TaskID taskID = new TaskID(taskinfo);
					
					taskID.setRelativeID(i);
					taskID.setExecuteOnThread(i%currentMultiTaskThreadPool);
					
					//Change since 23/05/2013, see the constructor of TaskID.
					//taskID.setGlobalID(next.globalID());
					
					taskID.setSubTask(true);
					/**
					 * 
					 * @author Kingsley
					 * @since 08/05/2013
					 * 
					 * Add newly created taskID(sub task) back into the original group 
					 * 
					 * @since 10/05/2013
					 * Set part of group before add a task id into a group.
					 */
					taskID.setPartOfGroup(((TaskIDGroup)next));
					((TaskIDGroup)next).add(taskID);
					enqueueReadyTask(taskID);
				}
				/**
				 * 
				 * @author Kingsley
				 * @since 08/05/2013
				 * 
				 * After a multi task worker thread expand a mult task, set the expansion flag.
				 */
				((TaskIDGroup)next).setExpanded(true);
				
				//Another option on how to implement Later Expansion
				/*TaskIDGroup group = (TaskIDGroup) next;
				int size = group.groupSize();
				int currentMultiTaskThreadPool = ThreadPool.getMultiTaskThreadPoolSize();
				int i = -1;
				for (Iterator iterator = group.groupMembers(); iterator.hasNext();) {
					TaskID taskID = (TaskID) iterator.next();
					taskID.setRelativeID(++i);
					taskID.setExecuteOnThread(i%currentMultiTaskThreadPool);
					enqueueReadyTask(taskID);
				}*/
				
				
			}
			
			
			
			/*
			*//**
			 * 
			 * @Author : Kingsley
			 * @since : 26/04/2013
			 * 
			 * Get workthread for multi task worker
			 * 
			 * *//*
			int workerID = wt.getMultiTaskThreadID();
			
			//next = localQueues[workerID].pollFirst();
			next = localMultiTaskQueues.get(workerID).pollFirst();
			
			
			while (next != null) {
				
				//-- attempt to have permission to execute this task
				if (next.executeAttempt()) {
					return next;
				} else {
					next.enqueueSlots(true);
					
					*//**
					 * 
					 * @Author : Kingsley
					 * @since : 25/04/2013
					 * The data structure is changed from array to list, therefore the corresponding way to
					 * get the data has to be changed.
					 * 
					 * *//*
					//next = localQueues[workerID].pollFirst();
					next = localMultiTaskQueues.get(workerID).pollFirst();
				}
			}
			
			//-- prefer to steal from the same victim last stolen from (if any)
			int prevVictim = lastStolenFrom.get();
			if (prevVictim != NOT_STOLEN) {
				
				*//**
				 * 
				 * @Author : Kingsley
				 * @since : 25/04/2013
				 * The data structure is changed from array to list, therefore the corresponding way to
				 * get the data has to be changed.
				 * 
				 * *//*
				//Deque<TaskID<?>> victimQueue = localQueues[prevVictim];
				Deque<TaskID<?>> victimQueue = localMultiTaskQueues.get(prevVictim);
				
				next = victimQueue.pollLast();
				while (next != null) {
					int reservedFor = next.getExecuteOnThread();
					if (reservedFor != ParaTaskHelper.ANY_THREAD_TASK && reservedFor != workerID) {
						//-- this is a multi-task that belongs to another thread, place it on that thread's private queue
						
						*//**
						 * 
						 * @Author : Kingsley
						 * @since : 25/04/2013
						 * The data structure is changed from array to list, therefore the corresponding way to
						 * get the data has to be changed.
						 * 
						 * *//*
						//privateQueues[reservedFor].add(next);
						privateQueues.get(reservedFor).add(next);
						
					} else if (next.executeAttempt()) {
						//-- otherwise, it is safe to attempt to execute this task
						return next;
					} else {
						//-- task has been canceled
						next.enqueueSlots(true);
					}
					next = victimQueue.pollLast();	
				}
			}
			
			//-- try to steal from a random thread.. if unsuccessful, try the next thread (until all threads were tried).
			int startVictim = (int) (Math.random()*numMultiTaskThreads); 
			
			for (int v = 0; v < numMultiTaskThreads; v++) {
				int nextVictim = (startVictim+v)%numMultiTaskThreads;
				
				//-- No point in trying to steal from self..
				if (nextVictim != workerID) {
					
					*//**
					 * 
					 * @Author : Kingsley
					 * @since : 25/04/2013
					 * The data structure is changed from array to list, therefore the corresponding way to
					 * get the data has to be changed.
					 * 
					 * *//*
					//Deque<TaskID<?>> victimQueue = localQueues[nextVictim];
					Deque<TaskID<?>> victimQueue = localMultiTaskQueues.get(nextVictim);
					
					next = victimQueue.pollLast();
					while (next != null) {
						
						int reservedFor = next.getExecuteOnThread();
						
						if (reservedFor != ParaTaskHelper.ANY_THREAD_TASK && reservedFor != workerID) {
							//-- this is a multi-task that belongs to another thread, place it on that thread's private queue
							
							*//**
							 * 
							 * @Author : Kingsley
							 * @since : 25/04/2013
							 * The data structure is changed from array to list, therefore the corresponding way to
							 * get the data has to be changed.
							 * 
							 * *//*
							//privateQueues[reservedFor].add(next);
							privateQueues.get(reservedFor).add(next);
							
						} else if (next.executeAttempt()) {
							//-- otherwise, it is safe to attempt to execute this task
							lastStolenFrom.set(nextVictim);
							return next;
						} else {
							//-- task has been canceled
							next.enqueueSlots(true);
						}
						next = victimQueue.pollLast();	
					}
				}
			}
			lastStolenFrom.set(NOT_STOLEN);
		*/}else {
			/**
			 * 
			 * @Author : Kingsley
			 * @since : 26/04/2013
			 * 
			 * Get worker thread id for one-off task worker
			 * 
			 * @since : 02/05/2013
			 * One-off task threads do not need local thread ID. Still use
			 * global id here.
			 * */
			//int workerID = wt.getOneoffTaskThreadID();
			int workerID = wt.getThreadID();
			
			//next = localQueues[workerID].pollFirst();
			next = localOneoffTaskQueues.get(workerID).pollFirst();
			
			
			while (next != null) {
				//System.out.println(((WorkerThread) Thread.currentThread()).getThreadID() + " get from it self");

				//-- attempt to have permission to execute this task
				if (next.executeAttempt()) {
					workFirstCounter.decrementAndGet();
					return next;
				} else {
					next.enqueueSlots(true);
					
					/**
					 * 
					 * @Author : Kingsley
					 * @since : 25/04/2013
					 * The data structure is changed from array to list, therefore the corresponding way to
					 * get the data has to be changed.
					 * 
					 * */
					//next = localQueues[workerID].pollFirst();
					next = localOneoffTaskQueues.get(workerID).pollFirst();
				}
			}
			
			//-- prefer to steal from the same victim last stolen from (if any)
			int prevVictim = lastStolenFrom.get();
			if (prevVictim != NOT_STOLEN) {
				
				/**
				 * 
				 * @Author : Kingsley
				 * @since : 25/04/2013
				 * The data structure is changed from array to list, therefore the corresponding way to
				 * get the data has to be changed.
				 * 
				 * 
				 * @since 18/05/2013
				 * When trying to steal from a victim, check if the victim is null first.
				 * */
				//Deque<TaskID<?>> victimQueue = localQueues[prevVictim];
				Deque<TaskID<?>> victimQueue = localOneoffTaskQueues.get(prevVictim);
				
				if (null != victimQueue) {
					next = victimQueue.pollLast();
				}
				
				while (next != null) {
					//System.out.println(((WorkerThread) Thread.currentThread()).getThreadID() + " steal from " + prevVictim);

					/**
					 * 
					 * @Author : Kingsley
					 * @since : 04/05/2013
					 * 
					 * This is the situation that one-off task worker thread is stealing tasks 
					 * from local one-off task queue, could simply try to execute the task.
					 * 
					 * 
					 * */
					
					if (next.executeAttempt()) {
						//-- otherwise, it is safe to attempt to execute this task
						workFirstCounter.decrementAndGet();
						return next;
					} else {
						//-- task has been canceled
						next.enqueueSlots(true);
					}
					
					/*int reservedFor = next.getExecuteOnThread();
					if (reservedFor != ParaTaskHelper.ANY_THREAD_TASK && reservedFor != workerID) {
						//-- this is a multi-task that belongs to another thread, place it on that thread's private queue
						
						*//**
						 * 
						 * @Author : Kingsley
						 * @since : 25/04/2013
						 * The data structure is changed from array to list, therefore the corresponding way to
						 * get the data has to be changed.
						 * 
						 * *//*
						//privateQueues[reservedFor].add(next);
						privateQueues.get(reservedFor).add(next);
						
					} else if (next.executeAttempt()) {
						//-- otherwise, it is safe to attempt to execute this task
						return next;
					} else {
						//-- task has been canceled
						next.enqueueSlots(true);
					}*/
					next = victimQueue.pollLast();	
				}
			}

			/**
			 * 
			 * @Author : Kingsley
			 * @since : 25/04/2013
			 * 
			 * Get a real worker ID array
			 * 
			 * @since : 18/05/2013
			 * The variable of "numOneoffTaskThreads" is cancelled, whenever want the size
			 * of how many one-off task worker threads, call thread pool directly.
			 * 
			 * @since 23/05/2013
			 * Re-structure the code
			 * When trying to steal from other workers, instead of getting the size of One-off Task Thread Pool,
			 * Using One-off Task Queues size. 
			 * */
			
			//-- try to steal from a random thread.. if unsuccessful, try the next thread (until all threads were tried).
			//int oneoffTaskThreadPoolSize = ThreadPool.getOneoffTaskThreadPoolSize();
			int oneoffTaskQueuesSize = localOneoffTaskQueues.size();
			
			//int startVictim = (int) (Math.random()*oneoffTaskThreadPoolSize); 
			int startVictim = (int) (Math.random()*oneoffTaskQueuesSize); 
			
			//Integer[] workIDs = localOneoffTaskQueues.keySet().toArray(new Integer[oneoffTaskThreadPoolSize]);
			Integer[] workIDs = localOneoffTaskQueues.keySet().toArray(new Integer[oneoffTaskQueuesSize]);
			
			//for (int v = 0; v < oneoffTaskThreadPoolSize; v++) {
			for (int v = 0; v < oneoffTaskQueuesSize; v++) {
				/**
				 * 
				 * @Author : Kingsley
				 * @since : 25/04/2013
				 * 
				 * Get a real worker ID
				 * 
				 * @since : 18/05/2013
				 * The variable of "numOneoffTaskThreads" is cancelled, whenever want the size
				 * of how many one-off task worker threads, call thread pool directly.
				 * 
				 * */
				//int nextVictim = (startVictim+v)%numOneoffTaskThreads;
				
				//int nextVictim = workIDs[(startVictim+v)%oneoffTaskThreadPoolSize];
				int nextVictim = workIDs[(startVictim+v)%oneoffTaskQueuesSize];
				
				//-- No point in trying to steal from self..
				if (nextVictim != workerID) {
					
					/**
					 * 
					 * @Author : Kingsley
					 * @since : 25/04/2013
					 * The data structure is changed from array to list, therefore the corresponding way to
					 * get the data has to be changed.
					 * 
					 * @since 18/05/2013
					 * When trying to steal from a victim, check if the victim is null first.
					 * */
					//Deque<TaskID<?>> victimQueue = localQueues[nextVictim];
					Deque<TaskID<?>> victimQueue = localOneoffTaskQueues.get(nextVictim);
				
					if (null != victimQueue) {
						next = victimQueue.pollLast();
					}

					while (next != null) {
						//System.out.println(((WorkerThread) Thread.currentThread()).getThreadID() + " steal from " + nextVictim);

						/**
						 * 
						 * @Author : Kingsley
						 * @since : 04/05/2013
						 * 
						 * This is the situation that one-off task worker thread is stealing tasks 
						 * from local one-off task queue, could simply try to execute the task.
						 * 
						 * 
						 * */
						if (next.executeAttempt()) {
							//-- otherwise, it is safe to attempt to execute this task
							lastStolenFrom.set(nextVictim);
							workFirstCounter.decrementAndGet();
							return next;
						} else {
							//-- task has been canceled
							next.enqueueSlots(true);
						}
						
						/*int reservedFor = next.getExecuteOnThread();
						
						if (reservedFor != ParaTaskHelper.ANY_THREAD_TASK && reservedFor != workerID) {
							//-- this is a multi-task that belongs to another thread, place it on that thread's private queue
							
							*//**
							 * 
							 * @Author : Kingsley
							 * @since : 25/04/2013
							 * The data structure is changed from array to list, therefore the corresponding way to
							 * get the data has to be changed.
							 * 
							 * *//*
							//privateQueues[reservedFor].add(next);
							privateQueues.get(reservedFor).add(next);
							
						} else if (next.executeAttempt()) {
							//-- otherwise, it is safe to attempt to execute this task
							lastStolenFrom.set(nextVictim);
							return next;
						} else {
							//-- task has been canceled
							next.enqueueSlots(true);
						}*/
						next = victimQueue.pollLast();	
					}
				}
			}
			lastStolenFrom.set(NOT_STOLEN);
		}
		
		
		
		
		//-- nothing found
		return null;
	}
}
