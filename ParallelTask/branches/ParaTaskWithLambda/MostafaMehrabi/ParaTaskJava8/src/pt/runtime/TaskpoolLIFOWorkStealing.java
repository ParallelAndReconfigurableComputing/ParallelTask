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
 *	There is no global queue, all tasks are placed on private queues in a round-robin fashion. Local queues are ordered such that 
 *  when a thread enqueues a task into its own local queue it enqueues the task to the head of the queue. However, if a thread
 *  enqueues the task to another thread's local queue, it enqueues the task to the tail of the queue.
 *  <br><br>
 *  If workers start to run out of work, then they steal (randomly) from another worker. [this is similar to JCilk].
 *  This could be enhanced by favoring to steal from a worker that we last succeeded to steal from. local queues are ordered such
 *  that when a thread takes a task from its owned queue it takes it from the tail (LIFO), but when it steals a task from another
 *  thread's queue, it takes it from the head (FIFO).
 *  
 *  @author Mostafa Mehrabi
 *  @since  9/9/2014
 */
public class TaskpoolLIFOWorkStealing extends AbstractTaskPool {
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
		boolean taskEnqueued = false;
		
		//if the task cannot be executed by arbitrary threads.
		if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK){
			privateQueues.get(taskID.getExecuteOnThread()).add(taskID);
			taskEnqueued = true;
		}
		
		//so the task can be executed by arbitrary threads. Now if the task is a TaskIDGroup...
		else if (taskID instanceof TaskIDGroup){
			globalMultiTaskQueue.add(taskID);
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
	 * After all these processes, if there are still no tasks found, the <b><i>victim thread</i></b> will be set to <code>null</code>
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
					next = privateQueues.get(workerID).poll();
				}
			}
			
			//if there were no tasks found in the privateQueue, then look into the globalMultiTask queue
			while ((next = globalMultiTaskQueue.poll()) != null) {
				// expand multi task
				int count = next.getCount();
				int currentMultiTaskThreadPool = ThreadPool.getMultiTaskThreadPoolSize();
				TaskInfo taskinfo = next.getTaskInfo();
				
				taskinfo.setSubTask(true);
				//aren't we repetitively operating on the same taskInfo?
				for (int i = 0; i < count; i++) {
					TaskID taskID = new TaskID(taskinfo);
					taskID.setRelativeID(i);
					taskID.setExecuteOnThread(i%currentMultiTaskThreadPool);
					taskID.setSubTask(true);
					taskID.setPartOfGroup(((TaskIDGroup)next));
					((TaskIDGroup)next).addInnerTask(taskID);
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
		
		globalMultiTaskQueue = new PriorityBlockingQueue<TaskID<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				AbstractTaskPool.LIFO_TaskID_Comparator);
			
		privateQueues = new ArrayList<AbstractQueue<TaskID<?>>>();
		localOneoffTaskQueues = new ConcurrentHashMap<Integer, LinkedBlockingDeque<TaskID<?>>>();
		
		initialiseWorkerThreads();
	}
}
