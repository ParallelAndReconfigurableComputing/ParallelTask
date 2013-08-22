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
import java.util.concurrent.PriorityBlockingQueue;

import paratask.queues.FifoLifoQueue;

public class TaskpoolMixedScheduling extends AbstractTaskPool {
	
	@Override
	protected void enqueueReadyTask(TaskID<?> taskID) {
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013
		 * Instead of simplely putting task into mixed queue, putting task to 
		 * different mixed queue by checking if the task is multi task or not.
		 * 
		 * @since : 15/05/2013
		 * Check if (taskID instanceof TaskIDGroup)
		 * if true, taskID is a un-expanded multi task;
		 * if false, taskID is a one-off task.
		 * 
		 * Check if(taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK) 
		 * if true, taskID is a expanded multi sub task.
		 * 
		 * For one-off task, when none-worker threads is queuing a task, this task
		 * goes to the mixed one-off task queue by calling mixedqueue.addGlobal(taskID);
		 * while worker threads is queuing a task, this task goes to the mixed one-off 
		 * task queue by calling mixedqueue.addLocal(taskID).
		 * 
		 * For multi task, only the global part of mixed queue is required.
		 * using
		 * if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK)
		 * to check if this task has been expanded or not. 
		 * if expanded this goes to the private multi task queue; 
		 * if not, this task goes to the mixed one-off task queue 
		 * by calling mixedqueue.addGlobal(taskID). 
		 * 
		 * */
		
		/*if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK) {
			if (taskID.getTaskInfo().getRegisteringThread() instanceof TaskThread)
				mixedMultiTaskqueue.addLocal(taskID);
			else
				mixedMultiTaskqueue.addGlobal(taskID);
		}else {
			if (taskID.getTaskInfo().getRegisteringThread() instanceof TaskThread)
				mixedOneoffTaskqueue.addLocal(taskID);
			else
				mixedOneoffTaskqueue.addGlobal(taskID);
		}*/
		
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
