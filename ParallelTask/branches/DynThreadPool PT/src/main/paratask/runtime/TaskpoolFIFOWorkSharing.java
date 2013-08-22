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

package paratask.runtime;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskpoolFIFOWorkSharing extends AbstractTaskPool {
	
	@Override
	protected void enqueueReadyTask(TaskID taskID) {
		//-- multi-tasks are added here first because this scheduling is fully FIFO according to the enqueuing timestamp
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013
		 * Instead of simplely putting task into global queue, putting task to 
		 * different global queue by checking if the task is multi task or not.
		 * 
		 * @since : 04/05/2013
		 * Check if (taskID instanceof TaskIDGroup)
		 * if true, taskID is a un-expanded multi task;
		 * if false, taskID is a one-off task.
		 * 
		 * Check if(taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK) 
		 * if true, taskID is a expanded multi sub task.
		 * 
		 * For one-off task, when none-worker threads is queuing a task, this task
		 * goes to the global one-off task queue; while worker threads is queuing 
		 * a task, this task goes to the its local one-off task queue.
		 * 
		 * For multi task,  using
		 * if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK)
		 * to check if this task has been expanded or not. 
		 * if expanded this goes to the private multi task queue; 
		 * if not, this task goes to the global multi task queue.
		 * 
		 * @since : 15/05/2013
		 * For one-off task, there should be only one-off task global queue.
		 * All tasks go to there right now.
		 * */
		//globalTaskqueue.add(taskID);		/* this will wake up any blocking worker threads if necessary */
	
		if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK || taskID instanceof TaskIDGroup) {
			if (taskID.getExecuteOnThread() == ParaTaskHelper.ANY_THREAD_TASK) {
				globalMultiTaskqueue.add(taskID);
			} else {
				privateQueues.get(taskID.getExecuteOnThread()).add(taskID);
			}
		}else {
			
			/*Thread t = Thread.currentThread();
			if (t instanceof TaskThread) {
				WorkerThread workerThread = (WorkerThread) t;
				localOneoffTaskQueues.get(workerThread.getThreadID()).add(taskID);
			}else {
				globalOne0ffTaskqueue.add(taskID);
			}*/
			
			globalOne0ffTaskqueue.add(taskID);
			
		}
	}
	
	/**
	 * Tries to poll for a task for the current worker.. first checks private queue, then global queue
	 * 
	 * If it finds a task on the global queue saved for another worker, then it is put on the private queue for thar worker thread 
	 * 
	 * This is non-blocking - therefore it returns null if it didnt find a task
	 * 
	 * If a canceled task is found, then it is discarded (but labeled "complete")
	 * 
	 * @return  the task appropriate for the current worker, otherwise null if there wasnt one at this time
	 * 
	 */
	@Override
	public TaskID workerPollNextTask() {
		
		WorkerThread wt = (WorkerThread) Thread.currentThread();
		int workerID = wt.getThreadID();
		
		// first check current worker's private queue and find a task that has not been cancelled
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013
		 * The data structure is changed from array to list, therefore the corresponding way to
		 * get the data has to be changed.
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
				 * The data structure is changed from array to list, therefore the corresponding way to
				 * get the data has to be changed.
				 * 
				 * */
				//next = privateQueues[workerID].poll();
				next = privateQueues.get(workerID).poll();
			}
		}
		
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 04/05/2013
		 * check local queue for one-off task before examining the global queue
		 * 
		 * 
		 * @since : 15/05/2013
		 * Only one-off task global queue exists, therefore, do not check local queues any more.
		 * 
		 * */
		
		/*if (!wt.isMultiTaskWorker()) {
			next = localOneoffTaskQueues.get(workerID).poll();
			
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

				next = localOneoffTaskQueues.get(workerID).poll();
			}
		}*/
		
		
		
		
		// check global queue.. if no suitable task found, return null
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013
		 * 
		 * check global queue according to the worker thread type.
		 * If no suitable task found, return null
		 * 
		 * @since 04/05/2013
		 * when chcking global queue, be careful that the multi task still not be expanded.
		 * 
		 * @since 10/05/2013
		 * Add a new variable to identify if current task is sub task
		 * Set it true when expand a multi task.
		 * 
		 * Since the sub task will not be given a global id, set the global id equal to its
		 * parent global id.
		 * 
		 * @since 21/05/2013
		 * When expand a multi task, set the field of "isSubTask" to true for its every single sub tasks.
		 * 
		 * */
		
		if (wt.isMultiTaskWorker()) {
			while ((next = globalMultiTaskqueue.poll()) != null) {
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
				
				
				/*int savedFor = next.getExecuteOnThread();
				
				// check if this task is saved for another worker thread
				if (savedFor == ParaTaskHelper.ANY_THREAD_TASK || savedFor == workerID) {
					
					//-- attempt to execute this task
					if (next.executeAttempt()) {
						//-- no cancel attempt was successful so far, therefore may execute this task
						
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
					 * The data structure is changed from array to list, therefore the corresponding way to
					 * get the data has to be changed.
					 * 
					 * *//*
					
		  			//privateQueues[savedFor].add(next);
					privateQueues.get(savedFor).add(next);
				}*/
			}
		} else {
			while ((next = globalOne0ffTaskqueue.poll()) != null) {
				/**
				 * 
				 * @Author : Kingsley
				 * @since : 04/05/2013
				 * 
				 * This is the situation that one-off task worker thread is checking 
				 * global one-off task queue, could simply try to execute the task.
				 * 
				 * 
				 * */
				if (next.executeAttempt()) {
					//-- no cancel attempt was successful so far, therefore may execute this task
					
					return next;
				} else {
					//-- task was successfully cancelled beforehand, therefore grab another task
					next.enqueueSlots(true);	//-- task is considered complete, so execute slots
					//-- TODO maybe should not execute slots for cancelled tasks, just the completedSlot() ?? 
				}
				
				/*int savedFor = next.getExecuteOnThread();
				
				// check if this task is saved for another worker thread
				if (savedFor == ParaTaskHelper.ANY_THREAD_TASK || savedFor == workerID) {
					
					//-- attempt to execute this task
					if (next.executeAttempt()) {
						//-- no cancel attempt was successful so far, therefore may execute this task
						
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
					 * The data structure is changed from array to list, therefore the corresponding way to
					 * get the data has to be changed.
					 * 
					 * 
					 * Actually this branch will never be executed since the task is picked from
					 * global one-off task queue, such that every one-off task dedicated worker
					 * thread can execute it.
					 * 
					 * *//*
					
		  			//privateQueues[savedFor].add(next);
					privateQueues.get(savedFor).add(next);
				}*/
			}
		}
		
		return null;
	}
	
	@Override
	public boolean executeSynchronously(int cutoff) {
		return false;		// TODO not yet implemented
	}
	
	@Override
	protected void initialise() {
		
		//-- The private queues and the global queue all have FIFO ordering, since tasks are executed in the same order as the enqueue.
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013
		 * The data structure is changed from array to list, therefore the corresponding way to
		 * initialize the data has to be changed.
		 * 
		 * Initialize private queue for only multi task worker threads.
		 * 
		 * Initialize both global multi task queue and global one-off task queue.
		 * 
		 * @since : 04/05/2013
		 * For multi task, global multi task queue and private queues should be initialized here
		 * 
		 * For one-off task, global one-off task queue and local one-off task queues should be 
		 * initialized here
		 * 
		 * @since : 15/05/2013
		 * For one-off task, only global multi task queue is required.
		 * 
		 * @since : 18/05/2013
		 * Thread pool should not take responsibility of creating local queues or private queues
		 * for individual working thread, even before those threads are created.
		 * Keep creating all shared queues, such as global queue and mixed queue here, create
		 * only the collection for unshared queues here, after worker threads are created later 
		 * along with their individual queues, those queues could be added into the collection.    
		 * 
		 * */
		
		
		/*privateQueues = new PriorityBlockingQueue[numThreads];
		for (int i = 0; i < numThreads; i++) {
			privateQueues[i] = new PriorityBlockingQueue<TaskID<?>>(INITIAL_QUEUE_CAPACITY, FIFO_TaskID_Comparator);
		}
		globalTaskqueue = new PriorityBlockingQueue<TaskID<?>>(INITIAL_QUEUE_CAPACITY, FIFO_TaskID_Comparator);*/
		
		//For multi task used
		globalMultiTaskqueue = new PriorityBlockingQueue<TaskID<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				AbstractTaskPool.FIFO_TaskID_Comparator);
		
		privateQueues = new ArrayList<AbstractQueue<TaskID<?>>>();
		
		//Put this create procedure into thread pool 
		/*for (int i = 0; i < numMultiTaskThreads; i++) {
			privateQueues.add(new PriorityBlockingQueue<TaskID<?>>(
					AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
					AbstractTaskPool.FIFO_TaskID_Comparator));
		}*/
		
		//For one-off task used
		globalOne0ffTaskqueue = new PriorityBlockingQueue<TaskID<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				AbstractTaskPool.FIFO_TaskID_Comparator);
		
		//localOneoffTaskQueues = new HashMap<Integer, LinkedBlockingDeque<TaskID<?>>>();
		
		initialiseWorkerThreads();
	}
}
