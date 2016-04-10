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
/**
 * This class extends <code>AbstractTaskPool</code> which is an implementation of <code>TaskPool</code>
 * and uses <code>Work Sharing</code> policy for scheduling tasks. In <code>Work Sharing</code> all tasks
 * are queued to a shared global queue, and are executed by threads in a FIFO (first in first out) policy.
 * 
 *@author Mostafa Mehrabi
 *@since  9/9/2014
 * */
public class TaskpoolFIFOWorkSharing extends AbstractTaskPool {
	/**
	 * Enqueues a task that is ready to be executed to one of the following task pools.<br>
	 * 1- If the task can be executed by any thread, and is multi-task, it is enqueued to
	 *    the <code>globalMultiTask</code> task pool.<br>
	 * 2- If the task can be executed by any thread, but is not multi-task it is enqueued to
	 *    the <code>globalOneOffTask</code> task pool.<br>
	 * 3- If the task cannot be executed by any thread, it is enqueued to the local 
	 *    <code>privateQueue</code> of the thread in charge of executing the task.<br>
	 * 
	 * @author Mostafa Mehrabi
	 * @since  9/9/2014
	 * */
	@Override
	protected void enqueueReadyTask(TaskID<?> taskID) {
		//Multi-tasks are added here first because this scheduling is fully FIFO according to the enqueuing timestamp
		//I think we should also check here if the TaskIDGroup is multi task, if not, then expand all its tasks into
		//global OneOff TaskQueue, don't just put it into globalMultiTaskQueue
		if (taskID.getExecuteOnThread() == ParaTask.ANY_THREAD_TASK){
			if (taskID instanceof TaskIDGroup){
				globalMultiTaskQueue.add(taskID);
			}
			else{
				globalOneOffTaskQueue.add(taskID);
			}
		}
		
		else{
			privateTaskQueues.get(taskID.getExecuteOnThread()).add(taskID);
		}
			
	}
	
	/**
	 * This method is called by a worker thread in order to get another task to execute. This method is schedule specific
	 * and its implementations are different for different scheduling types. For work sharing, the method tries to poll 
	 * a task from the worker thread's <code>privateQueue</code>. If the polling is successful, it attempts to execute the task. If 
	 * the attempt is successful the task will be passed to the worker thread to execute; otherwise this method will call
	 * for enqueueing the slots of that task, becuase unsuccessful execution attempts mean that the task is either cancelled or 
	 * completed.
	 * <br><br>
	 * If there are no tasks found in the thread's private queue, the method will then check the <code>globalMultiTask</code> queue, 
	 * this is because one off tasks are not enqueued to private queues, and they are submitted to <code>gloabOneOffTask</code> queues
	 * from the beginning.<br>
	 * While there are multi-tasks in the <code>globalMultiTask</code> queue, they are polled and expanded and enqueued 
	 * as ready-to-execute tasks.<br>
	 * If no tasks were found in the <code>gloablMultiTask</code> queue, the method will check the <code>globalOneOffTask</code>
	 * queue, and if still nothing is found, it will return <code>null</code>. It should be mentioned that <code>polling</code> 
	 * is used to fetch the elements inserted earlier to fulfill the <code>FIFO</code> scheduling policy.   
	 * 
	 * @return  The <code>TaskID</code> appropriate for the current worker, otherwise <code>null</code> if there wasn't one at this time
	 * 
	 * @author Mostafa Mehrabi
	 * @since   9/9/2014
	 */
	@Override
	public TaskID<?> workerPollNextTask() {
		
		WorkerThread workerThread = (WorkerThread) Thread.currentThread();
		int workerID = workerThread.getThreadID();
		
		TaskID<?> nextTaskID = null;
		//shouldn't this be !wt.isMultiTaskWorker()
		if (workerThread.isMultiTaskWorker()) {
			
			while ((nextTaskID = privateTaskQueues.get(workerID).poll()) != null) {
				
				//attempt to execute this task
				if (nextTaskID.executeAttempt()) {
					//no cancel attempt was successful so far, therefore may execute this task
					return nextTaskID;
				} 
				
				//if the task cannot be started, it means the task was successfully cancelled beforehand, 
				//therefore, execute slots and grab another task
				nextTaskID.enqueueSlots(true);
			}
			
			//Thread could not find a task from its private queue, now try the global multi task queue.
			while ((nextTaskID = globalMultiTaskQueue.poll()) != null) {
				// expand multi task
				int count = ((TaskIDGroup<?>)nextTaskID).getGroupSize();
				int multiTaskThreadPoolSize = ThreadPool.getMultiTaskThreadPoolSize();
				TaskInfo<?> taskInfo = nextTaskID.getTaskInfo();

				// indicate this is a task info of multi-task
				taskInfo.setTaskInfoOfMultiTask(true);
				
				for (int i = 0; i < count; i++) {
					TaskID<?> taskID = new TaskID(taskInfo);
					
					taskID.setRelativeID(i);
					taskID.setExecuteOnThread(i%multiTaskThreadPoolSize);
					
					taskID.setSubTask(true);
					taskID.setPartOfGroup(((TaskIDGroup<?>)nextTaskID));
					((TaskIDGroup<?>)nextTaskID).addInnerTask(taskID);
					enqueueReadyTask(taskID);
					
				}
				// After a multi task worker thread expand a mult task, set the expansion flag.
				((TaskIDGroup<?>)nextTaskID).setExpanded(true);
			}
			//return null;
		} else {
			while ((nextTaskID = globalOneOffTaskQueue.poll()) != null) {
				
				if (nextTaskID.executeAttempt()) {
					return nextTaskID;
				} else {
					//task is considered complete, so execute slots
					nextTaskID.enqueueSlots(true);	
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
		
		globalMultiTaskQueue = new PriorityBlockingQueue<TaskID<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				AbstractTaskPool.FIFO_TaskID_Comparator);
		
		privateTaskQueues = new ArrayList<AbstractQueue<TaskID<?>>>();
		
		
		globalOneOffTaskQueue = new PriorityBlockingQueue<TaskID<?>>(
				AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
				AbstractTaskPool.FIFO_TaskID_Comparator);
		
		
		initialiseWorkerThreads();
	}
}
