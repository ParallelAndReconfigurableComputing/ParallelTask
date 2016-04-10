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

import pt.queues.FifoLifoQueue;

/**
 * The mixed scheduling policy is a combination of <code>Work Stealing</code> and <code>Work Sharing</code> policies,
 * such that when enqueuing a task, if the enqueuing thread is a <i>worker thread</i>, the <code><b>Work Stealing</b></code>
 * policy is used, and if the enqueuing thread is a <i>non-worker thread</i>, the <code><b>Work Sharing</b></code> policy is
 * used!
 * <br><br>
 * A worker thread potentially prefers to execute tasks from its own <code>localOneOffTask</code> queue before helping with 
 * the global shared queue!
 * 
 * @author Mostafa Mehrabi
 * @since  14/9/2014
 * */
public class TaskpoolMixedScheduling extends AbstractTaskPool {
	
	/**
	 * When enqueuing tasks under the <code>Mixed Scheduling</code> policy, if a task <b>cannot</b> be executed 
	 * by arbitrary threads, it will be enqueued to the <code>privateQueue</code> of the thread in charge of
	 * executing it. 
	 * <br><br>
	 * However, if the task can be executed by arbitrary threads it will be enqueued to the <code>mixedMultiTask</code>
	 * queue if it is a <code>TaskIDGroup</code>, otherwise it will be enqueued to the <code>mixedOneOffTask</code> queue.
	 * 
	 * @author Mostafa Mehrabi
	 * @since   14/9/2014
	 * */
	@Override
	protected void enqueueReadyTask(TaskID<?> taskID) {
		//if the task cannot be executed on arbitrary threads, regardless of being
		//a TaskIDGroup or a OneOff task, it will be enqueued to a private queue
		if (taskID.getExecuteOnThread() != ParaTask.ANY_THREAD_TASK){
			privateTaskQueues.get(taskID.getExecuteOnThread()).add(taskID);
		}
		
		//if the task can be executed on arbitrary threads, and is a group task
		else if (taskID instanceof TaskIDGroup){
			mixedMultiTaskQueue.addGlobal(taskID);
		}
		
		//if the task can be executed on arbitrary threads, and is a one off task
		else{
			mixedOneOffTaskQueue.addGlobal(taskID);
		}
		
	}
	
	/**
	 * This method polls a new task for the current thread that is requesting for a new task under the 
	 * <code>Mixed Scheduling</code> policy. This method should only be called by <code>Worker Threads</code>. 
	 * <br><br>
	 * Once a worker thread requests for a new task, this method checks if the thread is a <code>Multi Task</code>
	 * thread. If so, then the <code>privateQueue</code> of that thread will be checked. In case there is a 
	 * task found which passes the preliminary execution attempt successfully, the task is passed to the thread to execute.
	 * <br><br>
	 * However, if the private queue of the <code>Multi Task</code> worker thread does not have any executable tasks, then 
	 * the method will check the <code>mixedMultiTask</code> queue. Every multi-task in this queue will be expanded into its
	 * sub-tasks, and the sub-tasks will be enqueue as <code>ready-to-execute</code> tasks, but the thread will temporarily 
	 * return <b>without</b> a task for this time, waiting for later chances. 
	 * <br><br>
	 * If the current worker thread is not a <code>Multi Task</code> worker thread, then the method checks the 
	 * <code>mixedOneOffTask</code> queue. For a given task inside this queue, if the thread is allowed to execute it, the 
	 * task will be checked with preliminary execution attempt. If the attempt is successful, the task will be sent to thread
	 * to execute. However, if the thread is not allowed to execute the task, the task will be enqueued to the <code>privateQueue</code>
	 * of the thread in charge of executing it.This process will continue until either a task is found for the thread, or all
	 * tasks inside the <code>mixedOneOffTask</code> queue are polled and enqueued into <code>privateQueues</code>.
	 * 
	 *@author Mostafa Mehrabi
	 *@since  14/9/2014
	 * */
	@Override
	public TaskID<?> workerPollNextTask() {
		
		WorkerThread workerThread = (WorkerThread) Thread.currentThread();
		int workerID = workerThread.getThreadID();
		
		TaskID<?> nextTaskID = null;
		
		if (workerThread.isMultiTaskWorker()) {
			
			while ((nextTaskID = privateTaskQueues.get(workerID).poll()) != null) {
				
				//-- attempt to execute this task
				if (nextTaskID.executeAttempt()) {
					//-- no cancel attempt was successful so far, therefore may execute this task
					return nextTaskID;
				} else {
					//-- task was successfully cancelled beforehand, therefore grab another task
					nextTaskID.enqueueSlots(true);	//-- task is considered complete, so execute slots
				}
			}
			
			int currentMultiTaskThreadPool = ThreadPool.getMultiTaskThreadPoolSize();
			int count = 0;
			// If the private queue of this multi-task does not have an executable task...
			// get task from mixed-schedule queue.. if no suitable task found, return null
			while ((nextTaskID = mixedMultiTaskQueue.poll()) != null) {
				
				// expand multi task
				count = ((TaskIDGroup<?>)nextTaskID).getGroupSize();
				
				TaskInfo<?> taskInfo = nextTaskID.getTaskInfo();

				// indicate this is a task info for multi-task
				taskInfo.setTaskInfoOfMultiTask(true);
				
				for (int i = 0; i < count; i++) {
					TaskID<?> taskID = new TaskID(taskInfo);
					
					taskID.setRelativeID(i);
					taskID.setExecuteOnThread(i%currentMultiTaskThreadPool);
					
					taskID.setSubTask(true);
					
					taskID.setPartOfGroup(((TaskIDGroup<?>)nextTaskID));
					((TaskIDGroup<?>)nextTaskID).addInnerTask(taskID);
					enqueueReadyTask(taskID);
					
				}
				((TaskIDGroup<?>)nextTaskID).setExpanded(true);
			}
			//return null;
		}else {
			// If the worker thread is not a multi-task thread look into the one off mixed queue...
			// get task from mixed-schedule queue.. if no suitable task found, return null
			while ((nextTaskID = mixedOneOffTaskQueue.poll()) != null) {
				int savedFor = nextTaskID.getExecuteOnThread();
				
				// check if this task is saved for another worker thread
				if (savedFor == ParaTask.ANY_THREAD_TASK || savedFor == workerID) {
					if (nextTaskID.executeAttempt()) {
						return nextTaskID;
					} else {
						// task was successfully cancelled beforehand, so execute slots
						nextTaskID.enqueueSlots(true);	
					}
				} else {
					privateTaskQueues.get(savedFor).add(nextTaskID);
				}
			}
		}
		
		
		return null;
	}

	
	@Override
	protected void initialise() {
	
		//For multi task
		mixedMultiTaskQueue = new FifoLifoQueue<TaskID<?>>();
		
		privateTaskQueues = new ArrayList<AbstractQueue<TaskID<?>>>();
		
		//For one-off task
		mixedOneOffTaskQueue = new FifoLifoQueue<TaskID<?>>();
		
		initialiseWorkerThreads();
	}
}
