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

import java.util.concurrent.PriorityBlockingQueue;

public class TaskpoolFIFOWorkSharing extends AbstractTaskPool {
	
	@Override
	protected void enqueueReadyTask(TaskID taskID) {
		//-- multi-tasks are added here first because this scheduling is fully FIFO according to the enqueuing timestamp
		globalTaskqueue.add(taskID);		/* this will wake up any blocking worker threads if necessary */
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
		TaskID next = privateQueues[workerID].poll();
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
			next = privateQueues[workerID].poll();
		}
		
		// check global queue.. if no suitable task found, return null
		while ((next = globalTaskqueue.poll()) != null) {
			int savedFor = next.getExecuteOnThread();
			
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
	  			privateQueues[savedFor].add(next);
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
		privateQueues = new PriorityBlockingQueue[numThreads];
		for (int i = 0; i < numThreads; i++) {
			privateQueues[i] = new PriorityBlockingQueue<TaskID<?>>(INITIAL_QUEUE_CAPACITY, FIFO_TaskID_Comparator);
		}
		globalTaskqueue = new PriorityBlockingQueue<TaskID<?>>(INITIAL_QUEUE_CAPACITY, FIFO_TaskID_Comparator);
		
		initialiseWorkerThreads();
	}
}
