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
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
public class TaskpoolFIFOWorkSharing extends AbstractTaskPool {
	@Override
	protected void enqueueReadyTask(TaskID taskID) {
		//-- multi-tasks are added here first because this scheduling is fully FIFO according to the enqueuing timestamp
		
		if (taskID.getExecuteOnThread() == ParaTaskHelper.ANY_THREAD_TASK){
			if (taskID instanceof TaskIDGroup){
				globalMultiTaskqueue.add(taskID);
			}
			else{
				globalOne0ffTaskqueue.add(taskID);
			}
		}
		
		else{
			privateQueues.get(taskID.getExecuteOnThread()).add(taskID);
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
		//shouldn't this be !wt.isMultiTaskWorker()
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
		
		//why checking the same condition again?
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
			//Thread could not find a task from its private queue, now try the global multi task queue.
			while ((next = globalMultiTaskqueue.poll()) != null) {
				// expand multi task
				int count = next.getCount();
				int currentMultiTaskThreadPool = ThreadPool.getMultiTaskThreadPoolSize();
				Task taskinfo = next.getTaskInfo();

				// indicate this is a sub task
				taskinfo.setSubTask(true);
				
				for (int i = 0; i < count; i++) {
					TaskID taskID = new TaskID(taskinfo);
					
					taskID.setRelativeID(i);
					taskID.setExecuteOnThread(i%currentMultiTaskThreadPool);
					
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
		
		globalMultiTaskqueue = new PriorityBlockingQueue<TaskID<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				AbstractTaskPool.FIFO_TaskID_Comparator);
		
		privateQueues = new ArrayList<AbstractQueue<TaskID<?>>>();
		
		
		globalOne0ffTaskqueue = new PriorityBlockingQueue<TaskID<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				AbstractTaskPool.FIFO_TaskID_Comparator);
		
		
		initialiseWorkerThreads();
	}
}
