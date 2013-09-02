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

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

/**
 *	
 *	There is no global queue, all tasks are placed on private queues in a round-robin fashion. If workers start to run out of work, 
 * 	then they steal (randomly) from another worker. [this is similar to JCilk].
 * 
 *   	(Maybe this could be enhanced by favoring to steal from a worker that we last succeeded to steal from). 
 *	
 *	Private queues are ordered such that workers get the head, while thiefs get the tail.  
 */
public class TaskpoolLIFOWorkStealing extends AbstractTaskPool {
	
	@Override
	protected void enqueueReadyTask(TaskID<?> taskID) {
		
		if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK) {
			//-- this is a multi-task, so place it on that thread's local queue (if it is wrongly stolen, it then gets placed on private queue)
			localQueues[taskID.getExecuteOnThread()].add(taskID);
		} else {
			//-- this is a normal task. 
			Thread regThread = taskID.getTaskInfo().getRegisteringThread();
			
			if (regThread instanceof WorkerThread) {
				//-- Add task to this thread's worker queue, at the beginning since it is the "hottest" task.
				
				WorkerThread workerThread = (WorkerThread) regThread;
				int tid = workerThread.getThreadID();
				localQueues[tid].addFirst(taskID);
				
			} else {
				//-- just add it to a random worker thread's queue.. (Add it at the end, since it's not hot in that thread's queue)
				
				int randThread = (int)(Math.random()*numThreads);
				localQueues[randThread].addLast(taskID);
			}
		}
	}
	
	@Override
	public TaskID workerPollNextTask() {
		
		WorkerThread wt = (WorkerThread) Thread.currentThread();
		int workerID = wt.getThreadID();
		
		//-- first check current worker's private queue and find a multi-task that has not been cancelled
		TaskID next = privateQueues[workerID].poll();
		while (next != null) {
			
			//-- attempt to execute this task
			if (next.executeAttempt()) {
				//-- no cancel attempt was successful so far, therefore may execute this task
				//-- this (multi-)task is here because another thread accidentally stole it from the local queue 
				return next;
			} else {
				//-- task was successfully cancelled beforehand, therefore grab another task
				next.enqueueSlots(true);
				next = privateQueues[workerID].poll();
			}
		}
		
		//-- check for a task from this worker thread's local queue.
		next = localQueues[workerID].pollFirst();
		while (next != null) {
			
			//-- attempt to have permission to execute this task
			if (next.executeAttempt()) {
				return next;
			} else {
				next.enqueueSlots(true);
				next = localQueues[workerID].pollFirst();
			}
		}
		
		//-- prefer to steal from the same victim last stolen from (if any)
		int prevVictim = lastStolenFrom.get();
		if (prevVictim != NOT_STOLEN) {
			Deque<TaskID<?>> victimQueue = localQueues[prevVictim];
			
			next = victimQueue.pollLast();
			while (next != null) {
				int reservedFor = next.getExecuteOnThread();
				if (reservedFor != ParaTaskHelper.ANY_THREAD_TASK && reservedFor != workerID) {
					//-- this is a multi-task that belongs to another thread, place it on that thread's private queue
					privateQueues[reservedFor].add(next);
					
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
		int startVictim = (int) (Math.random()*numThreads); 
		
		for (int v = 0; v < numThreads; v++) {
			int nextVictim = (startVictim+v)%numThreads;
			
			//-- No point in trying to steal from self..
			if (nextVictim != workerID) {
				Deque<TaskID<?>> victimQueue = localQueues[nextVictim];
				
				next = victimQueue.pollLast();
				while (next != null) {
					
					int reservedFor = next.getExecuteOnThread();
					
					if (reservedFor != ParaTaskHelper.ANY_THREAD_TASK && reservedFor != workerID) {
						//-- this is a multi-task that belongs to another thread, place it on that thread's private queue
						privateQueues[reservedFor].add(next);
						
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
		
		//-- nothing found
		return null;
	}
	
	@Override
	protected void initialise() {
		lastStolenFrom = new ThreadLocal<Integer>() {
				@Override protected Integer initialValue() 
				{ 
					return NOT_STOLEN; 
				}};
		
		privateQueues = new PriorityBlockingQueue[numThreads];	//
		localQueues = new LinkedBlockingDeque[numThreads];			// LIFO for owner, but FIFO for thieves 
		for (int i = 0; i < numThreads; i++) {
			privateQueues[i] = new PriorityBlockingQueue<TaskID<?>>(INITIAL_QUEUE_CAPACITY, FIFO_TaskID_Comparator);
			localQueues[i] = new LinkedBlockingDeque<TaskID<?>>();
		}
		initialiseWorkerThreads();
	}
}
