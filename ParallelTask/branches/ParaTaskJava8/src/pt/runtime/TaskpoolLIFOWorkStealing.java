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
	protected void enqueueReadyTask(Future<?> taskID) {
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 04/05/2013
		 * Check if (taskID instanceof TaskIDGroup)
		 * if true, taskID is a un-expanded multi task;
		 * if false, taskID is a one-off task.
		 * 
		 * Check if(taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK) 
		 * if true, taskID is a expanded multi sub task.
		 * 
		 * For one-off task, when ono-worker threads is queuing a task, this task
		 * goes to a random local one-off task queue; while worker threads is queuing 
		 * a task, this task goes to the its local one-off task queue.
		 * 
		 * 
		 * For multi task,  using
		 * if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK)
		 * to check if this task has been expanded or not. 
		 * if expanded this goes to the private multi task queue; 
		 * if not, this task goes to the global multi task queue.
		 * 
		 * 
		 * */
		
		if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK || taskID instanceof FutureGroup) {
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
				localOneoffTaskQueues.get(randThread).addLast(taskID);
			}
		}
	}
	
	@Override
	public Future workerPollNextTask() {
		
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
		Future next = null;
		
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
				
				//-- attempt to execute this task
				if (next.executeAttempt()) {
					//-- no cancel attempt was successful so far, therefore may execute this task
					//-- this (multi-)task is here because another thread accidentally stole it from the local queue 
					return next;
				} else {
					//-- task was successfully cancelled beforehand, therefore grab another task
					next.enqueueSlots(true);
					
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
		}
		
	
		//-- check for a task from this worker thread's local queue.
		/**
		 * 
		 * @Author Kingsley
		 * @since 25/04/2013
		 * The data structure is changed from array to list, therefore the corresponding way to
		 * get the data has to be changed.
		 * 
		 * check local queue according to the worker thread type.
		 * If no suitable task found, return null
		 * 
		 * @since 04/05/2013
		 * There are no local queues for multi task any more.
		 * And there is no work-stealing for multi task any more 
		 * Here, the code will do the same thing just like what happened in work-sharing 
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
				Task taskinfo = next.getTaskInfo();
				
				taskinfo.setSubTask(true);
				
				for (int i = 0; i < count; i++) {
					Future taskID = new Future(taskinfo);
					
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
					taskID.setPartOfGroup(((FutureGroup)next));
					((FutureGroup)next).add(taskID);
					enqueueReadyTask(taskID);
				}
				/**
				 * 
				 * @author Kingsley
				 * @since 08/05/2013
				 * 
				 * After a multi task worker thread expand a mult task, set the expansion flag.
				 */
				((FutureGroup)next).setExpanded(true);
				
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
				Deque<Future<?>> victimQueue = localOneoffTaskQueues.get(prevVictim);
				
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
					Deque<Future<?>> victimQueue = localOneoffTaskQueues.get(nextVictim);
				
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
	
	@Override
	protected void initialise() {
		lastStolenFrom = new ThreadLocal<Integer>() {
			@Override
			protected Integer initialValue() {
				return NOT_STOLEN;
			}};
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013 
		 * The data structure is changed from array to list, therefore the 
		 * corresponding way to initialize the data has to be changed.
		 * 
		 * 
		 * Initialize both private queue and local queue for multi task worker threads.
		 * 
		 * Initialize local queue for one-off task worker threads.
		 * 
		 * 
		 * @since : 02/05/2013
		 * One-off task threads do not need local thread ID, which means a List can not be used to group
		 * local one-off task queues here. HashMap will be used here as a replacement. However, HashMap
		 * requires worker's global id as the key, in this method, the global id is not available, therefore
		 * keep localOneoffTaskQueues empty, create each local one-off task queue when create one-off task
		 * worker thread.
		 * 
		 * @since : 04/05/2013
		 * For multi task, global multi task queue and private queues should be initialized here
		 * Local multi task queues is not necessary.
		 * 
		 * For one-off task, only local one-off task queues should be 
		 * initialized here
		 * 
		 * @since : 18/05/2013
		 * Thread pool should not take responsibility of creating local queues or private queues
		 * for individual working thread, even before those threads are created.
		 * Keep creating all shared queues, such as global queue and mixed queue here, create
		 * only the collection for unshared queues here, after worker threads are created later 
		 * along with their individual queues, those queues could be added into the collection.    
		 * 
		 * @since 25/04/2013
		 * For "globalMultiTaskqueue", if user choose the schedule policy as "WorkStealing",
		 * multi tasks should follow this policy as well, which means the last multi task that
		 * put into the global MultiTask queue should be polled out first, then the hotest
		 * multi task can always be expanded frist. Thus change the comparator of "globalMultiTaskqueue"
		 * from "AbstractTaskPool.FIFO_TaskID_Comparator" to "AbstractTaskPool.LIFO_TaskID_Comparator".
		 * 
		 * */
				
		/*privateQueues = new PriorityBlockingQueue[numThreads];	//
		localQueues = new LinkedBlockingDeque[numThreads];  // LIFO for owner, but FIFO for thieves 
		for (int i = 0; i < numThreads; i++) {
			privateQueues[i] = new PriorityBlockingQueue<TaskID<?>>(INITIAL_QUEUE_CAPACITY, FIFO_TaskID_Comparator);
			localQueues[i] = new LinkedBlockingDeque<TaskID<?>>();
		}*/
			
			
		//For multi task used
			
		globalMultiTaskqueue = new PriorityBlockingQueue<Future<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				/*AbstractTaskPool.FIFO_TaskID_Comparator*/
				AbstractTaskPool.LIFO_TaskID_Comparator);
			
		privateQueues = new ArrayList<AbstractQueue<Future<?>>>();
		//localMultiTaskQueues = new ArrayList<LinkedBlockingDeque<TaskID<?>>>();
		
		//Put this create procedure into thread pool 
		/*for (int i = 0; i < numMultiTaskThreads; i++) {
			privateQueues.add(new PriorityBlockingQueue<TaskID<?>>(
					AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
					AbstractTaskPool.FIFO_TaskID_Comparator));
			//localMultiTaskQueues.add(new LinkedBlockingDeque<TaskID<?>>());
		}*/
		
		/*localOneoffTaskQueues = new ArrayList<LinkedBlockingDeque<TaskID<?>>>();
		for (int i = 0; i < numOneoffTaskThreads; i++) {
			localOneoffTaskQueues.add(new LinkedBlockingDeque<TaskID<?>>());
		}*/
		
		//For one-off task used
		
		localOneoffTaskQueues = new ConcurrentHashMap<Integer, LinkedBlockingDeque<Future<?>>>();
		
		initialiseWorkerThreads();
	}
}
