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
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
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
		boolean taskEnqueued = false;
		
		//if the task cannot be executed by arbitrary threads.
		if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK){
			privateQueues.get(taskID.getExecuteOnThread()).add(taskID);
			taskEnqueued = true;
		}
		
		//so the task can be executed by arbitrary threads. Now if the task is a TaskIDGroup...
		else if (taskID instanceof TaskIDGroup){
			globalMultiTaskqueue.add(taskID);
			taskEnqueued = true;
		}
		
		//so the task can be executed by arbitrary threads, but is not a TaskIDGroup.. 
		else{
			//then if the enqueuing thread is a worker but not multi-task worker...
			Thread registeringThread = taskID.getTaskInfo().getRegisteringThread();
			if (registeringThread instanceof WorkerThread){
				WorkerThread enqueuingWorkerThread = (WorkerThread) registeringThread;
				if (!enqueuingWorkerThread.isMultiTaskWorker()){
					int tID = enqueuingWorkerThread.getThreadID();
					localOneoffTaskQueues.get(tID).addFirst(taskID);
					taskEnqueued = true;
				}
			}
		}
		
		//if the task is still not enqueued, then the registering thread is either a multi-task worker thread, 
		//or is not a worker thread!
		if (!taskEnqueued){
			int oneOffTaskThreadPoolSize = ThreadPool.getOneoffTaskThreadPoolSize();
			Integer[] workerIDs = localOneoffTaskQueues.keySet().toArray(new Integer[oneOffTaskThreadPoolSize]);
			int randomThreadID = workerIDs[(int)(Math.random()*oneOffTaskThreadPoolSize)];
			localOneoffTaskQueues.get(randomThreadID).addLast(taskID);
			taskEnqueued = true;
		}
			
	}
	
	@Override
	public TaskID workerPollNextTask() {
		
		WorkerThread wt = (WorkerThread) Thread.currentThread();
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 26/04/2013
		 * 
		 * The workerID should be found according to different worker threads.
		 * 
		 * */
		//int workerID = wt.getThreadID();
		
		//-- first check current worker's private queue and find a multi-task that has not been cancelled
		
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
			/**
			 * 
			 * @Author : Kingsley
			 * @since : 26/04/2013
			 * 
			 * Get workthread for multi task worker
			 * 
			 * */
			int workerID = wt.getThreadLocalID();
			
			next= privateQueues.get(workerID).poll();
			
			while (next != null) {
				
				if (next.executeAttempt()) {
					//-- no cancel attempt was successful so far, therefore may execute this task
					//-- this (multi-)task is here because another thread accidentally stole it from the local queue 
					return next;
				} else {
					//-- task was successfully cancelled beforehand, therefore grab another task
					next.enqueueSlots(true);
					next = privateQueues.get(workerID).poll();
				}
			}
			
			//if there were no tasks found in the privateQueue, then look into the globalMultiTask queue
			while ((next = globalMultiTaskqueue.poll()) != null) {
				// expand multi task
				int count = next.getCount();
				int currentMultiTaskThreadPool = ThreadPool.getMultiTaskThreadPoolSize();
				Task taskinfo = next.getTaskInfo();
				
				taskinfo.setSubTask(true);
				//aren't we repetitively operating on the same taskInfo?
				for (int i = 0; i < count; i++) {
					TaskID taskID = new TaskID(taskinfo);
					taskID.setRelativeID(i);
					taskID.setExecuteOnThread(i%currentMultiTaskThreadPool);
					taskID.setSubTask(true);
					taskID.setPartOfGroup(((TaskIDGroup)next));
					((TaskIDGroup)next).add(taskID);
					enqueueReadyTask(taskID);
				}
			}
			
		}
		// if the worker thread is not a multi-task thread then check the local one-off task queues of that thread.
		else {
			int workerID = wt.getThreadID();
			next = localOneoffTaskQueues.get(workerID).pollFirst();
			
			
			while (next != null) {
				if (next.executeAttempt()) {
					return next;
				} else {
					next.enqueueSlots(true);
					next = localOneoffTaskQueues.get(workerID).pollFirst();
				}
			}
			
			//If there are no tasks in the thread's localOneOffTask queue, the thread will steal a task!
			//We prefer to steal from the same victim last stolen from (if any)
			int prevVictim = lastStolenFrom.get();
			if (prevVictim != NOT_STOLEN) {
				//if there is already a thread from which we have stolen a task before, then...
				Deque<TaskID<?>> victimQueue = localOneoffTaskQueues.get(prevVictim);
				
				if (null != victimQueue) {
					next = victimQueue.pollLast();
				}
				
				while (next != null) {
				if (next.executeAttempt()) {
						//-- otherwise, it is safe to attempt to execute this task
						return next;
					} else {
						//-- task has been canceled
						next.enqueueSlots(true);
					}
					
					next = victimQueue.pollLast();	
				}
			}

			//if we have not stolen any task from a thread before, or if victim's queue is empty...
			int oneoffTaskQueuesSize = localOneoffTaskQueues.size();
			
			//then chose a new victim  
			int startVictimIndex = (int) (Math.random()*oneoffTaskQueuesSize); 
			
			//get the array of the worker threads indices
			Integer[] workIDs = localOneoffTaskQueues.keySet().toArray(new Integer[oneoffTaskQueuesSize]);
			
			//start from a random thread's local oneOffTaskQueue..
			for (int v = 0; v < oneoffTaskQueuesSize; v++) {
				int nextVictimID = workIDs[(startVictimIndex+v)%oneoffTaskQueuesSize];
				
				//-- No point in trying to steal from self..
				if (nextVictimID != workerID) {
					
					Deque<TaskID<?>> victimQueue = localOneoffTaskQueues.get(nextVictimID);
				 //this part of the code is duplicated repeatedly, maybe it is better to implement it in a method
					if (null != victimQueue) {
						next = victimQueue.pollLast();
					}

					while (next != null) {
						if (next.executeAttempt()) {
							//-- otherwise, it is safe to attempt to execute this task
							lastStolenFrom.set(nextVictimID);
							return next;
						} else {
							//-- task has been canceled
							next.enqueueSlots(true);
						}
						
						next = victimQueue.pollLast();	
					}
				}
			}
			//couldn't steal from any of threads' task lists
			lastStolenFrom.set(NOT_STOLEN);
		}
		
		return null;
	}
	
	@Override
	protected void initialise() {
		lastStolenFrom = new ThreadLocal<Integer>() {
			@Override
			protected Integer initialValue() {
				return NOT_STOLEN;
			}};
		
		globalMultiTaskqueue = new PriorityBlockingQueue<TaskID<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				AbstractTaskPool.LIFO_TaskID_Comparator);
			
		privateQueues = new ArrayList<AbstractQueue<TaskID<?>>>();
		localOneoffTaskQueues = new ConcurrentHashMap<Integer, LinkedBlockingDeque<TaskID<?>>>();
		
		initialiseWorkerThreads();
	}
}
