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
import java.util.concurrent.PriorityBlockingQueue;

import pt.queues.FifoLifoQueue;

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
	 * @sice   14/9/2014
	 * */
	@Override
	protected void enqueueReadyTask(TaskID<?> taskID) {
		//if the task cannot be executed on arbitrary threads, regardless of being
		//a TaskIDGroup or a OneOff task, it will be enqueued to a private queue
		if (taskID.getExecuteOnThread() != ParaTaskHelper.ANY_THREAD_TASK){
			privateQueues.get(taskID.getExecuteOnThread()).add(taskID);
		}
		
		//if the task can be executed on arbitrary threads, and is a group task
		else if (taskID instanceof TaskIDGroup){
			mixedMultiTaskqueue.addGlobal(taskID);
		}
		
		//if the task can be executed on arbitrary threads, and is a one off task
		else{
			mixedOneoffTaskqueue.addGlobal(taskID);
		}
		
	}
	
	@Override
	public TaskID workerPollNextTask() {
		
		WorkerThread wt = (WorkerThread) Thread.currentThread();
		int workerID = wt.getThreadID();
		
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
				
				next = privateQueues.get(workerID).poll();
			}
			
			// If the private queue of this multi-task does not have an executable task...
			// get task from mixed-schedule queue.. if no suitable task found, return null
			while ((next = mixedMultiTaskqueue.poll()) != null) {
				
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
					
					taskID.setPartOfGroup(((TaskIDGroup)next));
					((TaskIDGroup)next).add(taskID);
					enqueueReadyTask(taskID);
					
				}
				((TaskIDGroup)next).setExpanded(true);
			}
		}else {
			// If the worker thread is not a multi-task thread look into the one off mixed queue...
			// get task from mixed-schedule queue.. if no suitable task found, return null
			while ((next = mixedOneoffTaskqueue.poll()) != null) {
				int savedFor = next.getExecuteOnThread();
				
				// check if this task is saved for another worker thread
				if (savedFor == ParaTaskHelper.ANY_THREAD_TASK || savedFor == workerID) {
					if (next.executeAttempt()) {
						return next;
					} else {
						//-- task was successfully cancelled beforehand, therefore grab another task
						next.enqueueSlots(true);	//-- task is considered complete, so execute slots
						//-- TODO maybe should not execute slots for cancelled tasks, just the completedSlot() ?? 
					}
				} else {
					
					
		  			//privateQueues[savedFor].add(next);
					privateQueues.get(savedFor).add(next);
				}
			}
		}
		
		
		return null;
	}

	
	@Override
	protected void initialise() {
	
		//For multi task
		mixedMultiTaskqueue = new FifoLifoQueue<TaskID<?>>();
		
		privateQueues = new ArrayList<AbstractQueue<TaskID<?>>>();
		
		//For one-off task
		mixedOneoffTaskqueue = new FifoLifoQueue<TaskID<?>>();
		
		initialiseWorkerThreads();
	}
}
