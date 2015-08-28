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

/**
 * This class which extends the <code>java.lang.Thread</code> class facilitates creating tasks threads
 * with their own <code>IDs</code>. Moreover, <code>multi task</code> threads also have a <code>localID</code>
 * in order to be identified in their own thread-pool. 
 * <br><br>
 * This class allows assigning instances of <code>TaskPool</code> to the instances of this class at the creation
 * time via the constructor. The customized <code>executeTask</code> method in this class executes the <b>task</b> that 
 * is assigned to a specific instance of this class, and stores the final result in the <code>TaskID</code> instance
 * that is associated to the <b>task</b>.
 * 
 * @author Mostafa Mehrabi
 * @since  15/9/2014
 * */
public abstract class TaskThread extends Thread {

	//-- TaskThreads could potentially have a stack of currently-processing tasks 
	//(e.g. if it blocks on a TaskID that hasn't completed)
	protected Stack<TaskID<?>> currentTaskStack = new Stack<TaskID<?>>();
	
	
	/**
	 * 
	 * ThreadID is used to identify the position of a worker thread
	 * in the entire system.
	 * <br><br>
	 * For each <i>newly created</i> task thread the <code><b>nextThreadID</b></code> is increased, 
	 * which is then associated to that thread's <code>threadID</code> to identify the position of 
	 * that worker thread in its own dedicated thread pool.
	 * Moreover, for <i>multi task</i> threads the <code><b>nextThreadLocalID</b></code> is increased,
	 * and then associated to that thread's <code>threadLocalID</code>.
	 * 
	 * @author Kingsley
	 * @since  26/04/2013
	 *
	 * @author Mostafa Mehrabi
	 * @since  15/9/2014
	 * 
	 *  */
	protected int threadID = -1;
	
	protected int threadLocalID = -1;
	
	
	protected Taskpool taskpool = null;
	
	// thread-safe in case interactive threads need to be created from multiple threads
	private static AtomicInteger nextThreadID = new AtomicInteger(-1); 	
	
	private static AtomicInteger nextThreadLocalID = new AtomicInteger(-1); 
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * 
	 * This constructor is used for "Interactive Thread", "Pipeline Thread" and "Slot Handling Thread"
	 * Give no thread ID to these thread types
	 * 
	 * */
	
	public TaskThread(Taskpool taskpool) {
		//this.threadID = nextThreadID.incrementAndGet();
		this.taskpool = taskpool;
	}
	
	/**
	 * 
	 * @author : Kingsley
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
		} 		
		this.taskpool = taskpool;
	}
	
	/**
	 * Executes the task given to this thread, and stores the result in the TaskID. This method
	 * pushes the task being executed in the <code>currentTaskStack</code> while the task is being
	 * executed, and pops the task out when the execution has either finished, or failed. 
	 * @return <code>true</code> if task executed successfully, <code>false</code> otherwise
	 * 
	 * @author Mostafa Mehrabi
	 * @since  15/9/2014
	 */
	protected boolean executeTask(TaskID task){
		currentTaskStack.push(task);
		
		TaskInfo info = task.getTaskInfo();
		Object result = null;
		
		try {
			result = info.execute();
			
			task.setReturnResult(result);
			task.enqueueSlots(false);
			
			currentTaskStack.pop();
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		currentTaskStack.pop();
		return false; 
	}
	
	/**
	 * Return the TaskID that is being currently executed by this TaskThread 
	 * @return	The current TaskID, or null if not working on a task
	 */
	public TaskID<?> currentExecutingTask() {
		return currentTaskStack.peek();
	}
	
	public int getThreadID() {
		return threadID;
	}

	

	/**
	 * 
	 * @author : Kingsley
	 * @since : 26/04/2013
	 * 
	 * Call to find the local threadID. This is specially used to identify the
	 * position of local queue in the local queue list. 
	 * 
	 * @since : 02/05/2013
	 * One-off task threads do not need local thread ID.
	 * 
	 * @since : 10/05/2013
	 * Change the function of getMultiTaskThreadID() to getThreadLocalID
	 * 
	 * */
	
	protected int getThreadLocalID() {
		return threadLocalID;
	}
	
	
}
