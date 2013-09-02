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

import pt.queues.FifoLifoQueue;

public class TaskpoolMixedScheduling extends AbstractTaskPool {
	
	@Override
	protected void enqueueReadyTask(TaskID<?> taskID) {
		if (taskID.getTaskInfo().getRegisteringThread() instanceof TaskThread)
			mixedQueue.addLocal(taskID);
		else
			mixedQueue.addGlobal(taskID);
	}
	
	@Override
	protected void initialise() {
		mixedQueue = new FifoLifoQueue<TaskID<?>>();
		privateQueues = new PriorityBlockingQueue[numThreads];	
		
		for (int i = 0; i < numThreads; i++) {
			privateQueues[i] = new PriorityBlockingQueue<TaskID<?>>(INITIAL_QUEUE_CAPACITY, FIFO_TaskID_Comparator);
		}
		initialiseWorkerThreads();
	}
	
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
		
		// get task from mixed-schedule queue.. if no suitable task found, return null
		while ((next = mixedQueue.poll()) != null) {
			int savedFor = next.getExecuteOnThread();
			
			// check if this task is saved for another worker thread
			if (savedFor == ParaTaskHelper.ANY_THREAD_TASK || savedFor == workerID) {
				
				//-- attempt to execute this task
				if (next.executeAttempt()) {
					//-- no cancel attempt was successful so far, therefore may execute this task
					
//					numTasksExecuted[workerID]++;	// for testing...
					
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

}
