/*
 *  Copyright (C) 2010 Nasser Giacaman, Oliver Sinnen
 *
 *  This file is part of Parallel Task.
 *
 *  Parallel Task is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or (at 
 *  your option) any later version.
 *
 *  Parallel Task is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along 
 *  with Parallel Task. If not, see <http://www.gnu.org/licenses/>.
 */

package pt.runtime;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.PriorityBlockingQueue;

import pt.queues.FifoLifoQueue;

/**
 * The mixed scheduling policy is a combination of <code>Work Stealing</code> and <code>Work Sharing</code> policies,
 * such that when enqueuing a task, if the enqueuing thread is a <i>worker thread</i>, the <code><b>Work Stealing</b></code>
 * policy is used, and if the enqueuing thread is a <i>non-worker thread</i>, the <code><b>Work Sharing</b></code> policy is
 * used!
 * <br><br>
 * A worker thread potentially prefers to execute tasks from its own <code>localOneOffTask</code> queue before helping with 
 * the global shared queue!
 * 
 * @author Mostafa Mehrabi
 * @since  14/9/2014
 * */
public class TaskpoolMixedScheduling extends AbstractTaskPool {
   /**
	 * When enqueuing tasks under the <code>Mixed Scheduling</code> policy, if a task <b>cannot</b> be executed 
	 * by arbitrary threads, it will be enqueued to the <code>privateQueue</code> of the thread in charge of
	 * executing it. 
	 * <br><br>
	 * However, if the task can be executed by arbitrary threads it will be enqueued to the <code>mixedMultiTask</code>
	 * queue if it is a <code>TaskIDGroup</code>, otherwise it will be enqueued to the <code>mixedOneOffTask</code> queue.
	 * 
	 * @author Mostafa Mehrabi
	 * @sice   14/9/2014
	 * */
	@Override
	protected void enqueueReadyTask(TaskID<?> taskID) {
			
		if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK || taskID instanceof TaskIDGroup) {
			if (taskID.getExecuteOnThread() == ParaTaskHelper.ANY_THREAD_TASK) {
				mixedMultiTaskqueue.addGlobal(taskID);
			} else {
				privateQueues.get(taskID.getExecuteOnThread()).add(taskID);
			}
		}else {
			if (Thread.currentThread() instanceof TaskThread) {
				mixedOneoffTaskqueue.addLocal(taskID);
			}else {
				mixedOneoffTaskqueue.addGlobal(taskID);
			}
		}
	}
	
	/**
	 * This method polls a new task for the current thread that is requesting for a new task under the 
	 * <code>Mixed Scheduling</code> policy. This method should only be called by <code>Worker Threads</code>. 
	 * <br><br>
	 * Once a worker thread requests for a new task, this method checks if the thread is a <code>Multi Task</code>
	 * thread. If so, then the <code>privateQueue</code> of that thread will be checked. In case there is a 
	 * task found which passes the preliminary execution attempt successfully, the task is passed to the thread to execute.
	 * <br><br>
	 * However, if the private queue of the <code>Multi Task</code> worker thread does not have any executable tasks, then 
	 * the method will check the <code>mixedMultiTask</code> queue. Every multi-task in this queue will be expanded into its
	 * sub-tasks, and the sub-tasks will be enqueue as <code>ready-to-execute</code> tasks, but the thread will temporarily 
	 * return <b>without</b> a task for this time, waiting for later chances. 
	 * <br><br>
	 * If the current worker thread is not a <code>Multi Task</code> worker thread, then the method checks the 
	 * <code>mixedOneOffTask</code> queue. For a given task inside this queue, if the thread is allowed to execute it, the 
	 * task will be checked with preliminary execution attempt. If the attempt is successful, the task will be sent to thread
	 * to execute. However, if the thread is not allowed to execute the task, the task will be enqueued to the <code>privateQueue</code>
	 * of the thread in charge of executing it.This process will continue until either a task is found for the thread, or all
	 * tasks inside the <code>mixedOneOffTask</code> queue are polled and enqueued into <code>privateQueues</code>.
	 * 
	 *@author Mostafa Mehrabi
	 *@since  14/9/2014
	 * */
	@Override
	public TaskID workerPollNextTask() {
		
		WorkerThread wt = (WorkerThread) Thread.currentThread();
		int workerID = wt.getThreadID();
		
		// first check current worker's private queue and find a task that has not been cancelled
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013 
		 * The data structure is changed from array to list, therefore the 
		 * corresponding way to initialize the data has to be changed.
		 * 
		 * One-off Task Worker will never examine its private queue.
		 * 
		 * */
		//TaskID next = privateQueues[workerID].poll();
		TaskID next = null;
		
		if (wt.isMultiTaskWorker()) {
			next = privateQueues.get(workerID).poll();
			
			while (next != null) {
				
				//-- attempt to execute this task
				if (next.executeAttempt()) {
					//-- no cancel attempt was successful so far, therefore may execute this task
					return next;
				} else {
					//-- task was successfully cancelled beforehand, therefore grab another task
					next.enqueueSlots(true);	//-- task is considered complete, so execute slots
					//-- TODO maybe should not execute slots for cancelled tasks, just the completedSlot() ?? 
				}
				
				/**
				 * 
				 * @Author : Kingsley
				 * @since : 25/04/2013 
				 * The data structure is changed from array to list, therefore the 
				 * corresponding way to initialize the data has to be changed.
				 * 
				 * */
				
				//next = privateQueues[workerID].poll();
				next = privateQueues.get(workerID).poll();
			}
		}
		
		/**
		 * 
		 * @Author Kingsley
		 * @since 25/04/2013
		 * 
		 * check global queue according to the worker thread type.
		 * If no suitable task found, return null
		 * 
		 * @since 15/05/2013
		 * Multi task expansion.
		 * 
		 * @since 21/05/2013
		 * When expand a multi task, set the field of "isSubTask" to true for its every single sub tasks.
		 * 
		 * */
		
		if (wt.isMultiTaskWorker()) {
			// get task from mixed-schedule queue.. if no suitable task found, return null
			while ((next = mixedMultiTaskqueue.poll()) != null) {
				
				// expand multi task
				int count = next.getCount();
				int currentMultiTaskThreadPool = ThreadPool.getMultiTaskThreadPoolSize();
				TaskInfo taskinfo = next.getTaskInfo();

				// indicate this is a sub task
				taskinfo.setSubTask(true);
				
				for (int i = 0; i < count; i++) {
					TaskID taskID = new TaskID(taskinfo);
					
					taskID.setRelativeID(i);
					taskID.setExecuteOnThread(i%currentMultiTaskThreadPool);
					
					//Change since 23/05/2013, see the constructor of TaskID.
					//taskID.setGlobalID(next.globalID());
					
					taskID.setSubTask(true);
					
					taskID.setPartOfGroup(((TaskIDGroup)next));
					((TaskIDGroup)next).add(taskID);
					enqueueReadyTask(taskID);
					
				}
				((TaskIDGroup)next).setExpanded(true);
				
				/*int savedFor = next.getExecuteOnThread();
				
				// check if this task is saved for another worker thread
				if (savedFor == ParaTaskHelper.ANY_THREAD_TASK || savedFor == workerID) {
					
					//-- attempt to execute this task
					if (next.executeAttempt()) {
						//-- no cancel attempt was successful so far, therefore may execute this task
						
						//numTasksExecuted[workerID]++;	// for testing...
						
						return next;
					} else {
						//-- task was successfully cancelled beforehand, therefore grab another task
						next.enqueueSlots(true);	//-- task is considered complete, so execute slots
						//-- TODO maybe should not execute slots for cancelled tasks, just the completedSlot() ?? 
					}
				} else {
					
					*//**
					 * 
					 * @Author : Kingsley
					 * @since : 25/04/2013 
					 * The data structure is changed from array to list, therefore the 
					 * corresponding way to initialize the data has to be changed.
					 * 
					 * *//*
					
		  			//privateQueues[savedFor].add(next);
					privateQueues.get(savedFor).add(next);
				}*/
			}
		}else {
			// get task from mixed-schedule queue.. if no suitable task found, return null
			while ((next = mixedOneoffTaskqueue.poll()) != null) {
				int savedFor = next.getExecuteOnThread();
				
				// check if this task is saved for another worker thread
				if (savedFor == ParaTaskHelper.ANY_THREAD_TASK || savedFor == workerID) {
					
					//-- attempt to execute this task
					if (next.executeAttempt()) {
						//-- no cancel attempt was successful so far, therefore may execute this task
						
//						numTasksExecuted[workerID]++;	// for testing...
						
						return next;
					} else {
						//-- task was successfully cancelled beforehand, therefore grab another task
						next.enqueueSlots(true);	//-- task is considered complete, so execute slots
						//-- TODO maybe should not execute slots for cancelled tasks, just the completedSlot() ?? 
					}
				} else {
					
					/**
					 * 
					 * @Author : Kingsley
					 * @since : 25/04/2013 
					 * The data structure is changed from array to list, therefore the 
					 * corresponding way to initialize the data has to be changed.
					 * 
					 * Actually this branch will never be executed since the task is picked from
					 * mixed one-off task queue, such that every one-off task dedicated worker
					 * thread can execute it.
					 * 
					 * */
					
		  			//privateQueues[savedFor].add(next);
					privateQueues.get(savedFor).add(next);
				}
			}
		}
		
		
		return null;
	}

	
	@Override
	protected void initialise() {
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013 
		 * The data structure is changed from array to list, therefore the 
		 * corresponding way to initialize the data has to be changed.
		 * 
		 * Initialize private queue for only multi task worker threads.
		 * 
		 * Initialize both mixed multi task queue and mixed one-off task queue.
		 * 
		 * 
		 * @since : 15/05/2013
		 * For multi task, just like worksharing and working stealing, there is 
		 * no stealing here as well. Thus, multi task mixed queue and private queues
		 * should be initialized here.
		 * 
		 * For one-off task, one-off task mixed queue should be initialized here.
		 * 
		 * 
		 * @since : 18/05/2013
		 * Thread pool should not take responsibility of creating local queues or private queues
		 * for individual working thread, even before those threads are created.
		 * Keep creating all shared queues, such as global queue and mixed queue here, create
		 * only the collection for unshared queues here, after worker threads are created later 
		 * along with their individual queues, those queues could be added into the collection. 
		 * */
		
		/*mixedQueue = new FifoLifoQueue<TaskID<?>>();
		privateQueues = new PriorityBlockingQueue[numThreads];	
		
		for (int i = 0; i < numThreads; i++) {
			privateQueues[i] = new PriorityBlockingQueue<TaskID<?>>(INITIAL_QUEUE_CAPACITY, FIFO_TaskID_Comparator);
		}*/
		
		//For multi task
		mixedMultiTaskqueue = new FifoLifoQueue<TaskID<?>>();
		
		privateQueues = new ArrayList<AbstractQueue<TaskID<?>>>();
		
		//Put this create procedure into thread pool 
		/*for (int i = 0; i < numMultiTaskThreads; i++) {
			privateQueues.add(new PriorityBlockingQueue<TaskID<?>>(
					AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
					AbstractTaskPool.FIFO_TaskID_Comparator));
		}*/
		
		//For one-off task
		mixedOneoffTaskqueue = new FifoLifoQueue<TaskID<?>>();
		
		initialiseWorkerThreads();
	}
}
