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
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TaskThread extends Thread {

	//-- TaskThreads could potentially have a stack of currently-processing tasks (e.g. if it blocks on a TaskID that hasn't completed)
	protected Stack<TaskID> currentTaskStack = new Stack<TaskID>();
	
	protected int threadID = -1;
	protected Taskpool taskpool = null;
	
	private HashMap<String, Object> threadPrivates = new HashMap<String, Object>();
	private Object threadPrivate;
	
	// thread-safe in case interactive threads need to be created from multiple threads
	private static AtomicInteger nextThreadID = new AtomicInteger(-1); 	
	
	public TaskThread(Taskpool taskpool) {
		this.threadID = nextThreadID.incrementAndGet();
		this.taskpool = taskpool;
	}
	
	/**
	 * Executes the current task for this thread, and stores the result in the TaskID
	 * @return	true if task executed successfully, false otherwise
	 */
	protected boolean executeTask(TaskID task) {
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
	
	public void setTP(Object tp){
		threadPrivate = tp;
	}
	
	public Object getTP(){
		return threadPrivate;
	}
	
	public void setTP(Object tp, String key){
		threadPrivates.put(key, tp);
	}
	
	public Object getTP(String key){
		return threadPrivates.get(key);
	}
}
