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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TaskThread extends Thread {

	//-- TaskThreads could potentially have a stack of currently-processing tasks (e.g. if it blocks on a TaskID that hasn't completed)
	protected Stack<TaskID> currentTaskStack = new Stack<TaskID>();
	
	
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 26/04/2013
	 * Still keep threadID which is used to identify the position of a worker thread
	 * in the entire system.
	 * 
	 * Newly increase two threadID, multiTaskThreadID and oneoffTaskThreadID, which are
	 * used to indentify the position of a worker thread in its own dedicated thread pool.
	 * 
	 * @since : 02/05/2013
	 * One-off task threads do not need local thread ID.
	 * 
	 * @since £º10/05/2013
	 * Change the name of multiTaskThreadID to threadLocalID
	 * Change the name of nextMultiTaskThreadID to nextThreadLocalID
	 * 
	 * */
	protected int threadID = -1;
	
	//protected int multiTaskThreadID = -1;
	//protected int oneoffTaskThreadID = -1;
	protected int threadLocalID = -1;
	
	
	protected Taskpool taskpool = null;
	
	// thread-safe in case interactive threads need to be created from multiple threads
	private static AtomicInteger nextThreadID = new AtomicInteger(-1); 	
	
	//private static AtomicInteger nextMultiTaskThreadID = new AtomicInteger(-1); 	
	//private static AtomicInteger nextOneoffTaskThreadID = new AtomicInteger(-1); 
	private static AtomicInteger nextThreadLocalID = new AtomicInteger(-1); 
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * 
	 * This constructor is used for "Interactive Thread", "Pipeline Thread" and "Slot Handling Thread"
	 * Give no thread ID to these thread type
	 * 
	 * */
	
	public TaskThread(Taskpool taskpool) {
		//this.threadID = nextThreadID.incrementAndGet();
		this.taskpool = taskpool;
	}
	
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 26/04/2013
	 * 
	 * A new constructor. If modify the original constructor, the creation of other threads
	 * maybe impacted.
	 * 
	 * */
	
	public TaskThread(Taskpool taskpool, boolean isMultiTaskWorker) {
		this.threadID = nextThreadID.incrementAndGet();
		if (isMultiTaskWorker) {
			this.threadLocalID = nextThreadLocalID.incrementAndGet();
		} /*else {
			this.oneoffTaskThreadID = nextOneoffTaskThreadID.incrementAndGet();
		}*/
		
		this.taskpool = taskpool;
	}
	
	/**
	 * Executes the current task for this thread, and stores the result in the TaskID
	 * @return	true if task executed successfully, false otherwise
	 * @throws PoisonPillException 
	 */
	protected boolean executeTask(TaskID task){
		currentTaskStack.push(task);
		
		TaskInfo info = task.getTaskInfo();
		Method method = info.getMethod();
		Object instance = info.getInstance();
		Object[] args = info.getParameters();
		Object result = null;
		
		// retrieve results from implicit taskids
		int[] taskIdArgIndexes = info.getTaskIdArgIndexes();
		for (int index : taskIdArgIndexes) {
			try {
				args[index] = ((TaskID)args[index]).getReturnResult();
			} catch (InterruptedException e) {
				// can't happen because task is guaranteed to have finished
			} catch (ExecutionException e) {
				// can't happen, handled somewhere else (eventually will be anyway)
			}
		}
		
		try {
			result = method.invoke(instance, args);
			
			task.setReturnResult(result);
			task.enqueueSlots(false);
			
			currentTaskStack.pop();
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			task.setException(e.getTargetException());
			task.enqueueSlots(false);			
		}
		currentTaskStack.pop();
		return false; 
	}
	
	/**
	 * Return the currently executing TaskID by this TaskThread 
	 * @return	The current TaskID, or null if not working on a task
	 */
	public TaskID currentExecutingTask() {
		return currentTaskStack.peek();
	}
	
	public int getThreadID() {
		return threadID;
	}

	

	/**
	 * 
	 * @Author : Kingsley
	 * @since : 26/04/2013
	 * 
	 * Call to find the local threadID. This is specially used for identify the
	 * position of local queue in the local queue list. 
	 * 
	 * @since : 02/05/2013
	 * One-off task threads do not need local thread ID.
	 * 
	 * @since : 10/05/2013
	 * Change the function of getMultiTaskThreadID() to getThreadLocalID
	 * 
	 * */
	
	/*public int getMultiTaskThreadID() {
		return multiTaskThreadID;
	}

	public int getOneoffTaskThreadID() {
		return oneoffTaskThreadID;
	}*/
	
	protected int getThreadLocalID() {
		return threadLocalID;
	}
	
	
}
