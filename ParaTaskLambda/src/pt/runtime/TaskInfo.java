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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.runtime.ParaTask.TaskType;



/**
 * 
 * This class is used to store information regarding the task, which is not related 
 * to its execution time. That is, the information regarding the task before it is
 * invoked. Thus, the information regarding the execution period (i.e. after the task is
 * invoked) will be stored in the TaskID.<br>
 * In general this class stores information regarding<br> 
 * 1- Dependences of a specific task<br>
 * 2- The handlers (i.e. slots) to notify during the execution and after execution<br>
 * 3- It identifies if a task is a sub task of another task<br>
 * 4- It keeps a reference to the thread which registers the task<br>
 * 5- It asynchronously collects the exceptions that may occur during execution by their<br>
 *    types (i.e. classes) and their corresponding exception handlers<br>
 * <br>   
 * This class keeps a reference to the thread which registers the task. If the registering 
 * thread is EDT (either android or java), it obtains the reference from ParaTask.getEDT(), otherwise
 * the reference to the <code>CurrentThread</code> will be made.<br>
 * <br>
 * This class also provides a method which returns the appropriate exception handler for an 
 * exception class that it receives as argument. Moreover, this class provides a method that
 * returns an instance of this class for a functor that it receives as argument.<br>
 * <br>
 * @author Mostafa Mehrabi
 * @since  7/9/2014
 */
public abstract class TaskInfo<R> {
	

	// for ParaTask in Java 8
	//private Functor<?> lambda;
	
	protected int taskCount = 1;

	protected Thread registeringThread;
	protected List<Slot<R>> slotsToNotify;
	protected List<Slot<R>> interSlotsToNotify;
	protected List<TaskID<?>> dependences;

	// for implicit results/dequeuing
	protected int[] taskIdArgIndexes = new int[] {};
	protected int[] queueArgIndexes = new int[] {};

	protected boolean hasAnySlots = false;
	protected boolean isMultiTask = false;
	protected boolean isInteractive = false;
	protected boolean registeredByGuiThread = false;
	protected boolean hasNoReturn = false;

	protected Map<Class<? extends Throwable>, Slot<? extends Throwable>> asyncExceptions = new HashMap<>();
	
	/* 
	 * Used to identify if it is a sub task for a multi task
	 */
	protected boolean isTaskInfoOfMultiTask = false;
	
	protected TaskInfo(){}
	
	/*
	 * Sets the number of tasks in a multiple-task.
	 * 
	 */
	//some methods are returning the current instance of Task<T> which seems to be pointless
	protected void setCount(int count) {
		if(count < 0) {
			throw new IllegalArgumentException("the value for task count must be greater than 0 or equal to STAR (0)");
		}
		this.taskCount = count;
	}
	
	boolean hasBeenRegisteredByGuiThread(){
		return this.registeredByGuiThread;
	}

	public int[] getTaskIdArgIndexes() {
		return taskIdArgIndexes;
	}

	public int[] getQueueArgIndexes() {
		return queueArgIndexes;
	}
	
	//the three dots in arguments means, zero or more, or an array of integers
	//may be passed as parameter/s.
	public void setTaskIdArgIndexes(int... indexes) {
		this.taskIdArgIndexes = indexes;
	}

	public void setQueueArgIndexes(int... indexes) {
		this.queueArgIndexes = indexes;
	}

	/**
	 * Returns the registering thread of this task, iff the
	 * thread is still alive, otherwise it returns <code>null</code>.
	 * 
	 * @param Thread The registering thread that is returned by this method.
	 * */
	public Thread getRegisteringThread() {
		if (hasBeenRegisteredByGuiThread())
			return GuiThread.getEventDispatchThread();
		if (registeringThread.isAlive())
			return registeringThread;
		return null;
	}

	//Why do we have to check if the GUI thread is the EDT?
	public Thread setRegisteringThread() {
		try {
			if (GuiThread.currentThreadIsEventDispatchThread()){//if the current thread is an event dispatch thread
				registeringThread = ParaTask.getEDT();
				this.registeredByGuiThread = true;
			}
			else
				registeringThread = Thread.currentThread();
		} catch (Exception e) {
			registeringThread = Thread.currentThread();
		}
		return registeringThread;
	}

	//make sure these steps are automated.. Should it be automated?
	void setTaskIDForSlotsAndHandlers(TaskID<R> taskID) {
		if (slotsToNotify != null) {
			for (Slot<R> slot : slotsToNotify) {
				slot.setTaskID(taskID);
			}
		}
		if (interSlotsToNotify != null) {
			for (Slot<R> slot : interSlotsToNotify){
				slot.setTaskID(taskID);
			}
		}
		
		// We don't set TaskID for the exception handlers, instead, at the time
		// of invoking the exception handler, the stored instance of exception
		// must be sent to the corresponding handler. 
	}

	<E extends Throwable> void setAsyncCatch(Class<E> exceptionClass, Slot<E> handler) {
		
		if (exceptionClass == null)
			throw new IllegalArgumentException("There is no exception class specified!");
		else if (handler == null)
			throw new IllegalArgumentException("There is no exception handler specified for this exception");
	
		asyncExceptions.put(exceptionClass, handler);
		hasAnySlots = true;
	}

	/**
	 * Returns the handler associated to the specified exception. 
	 */
	@SuppressWarnings("unchecked")
	<E extends Throwable> Slot<E> getExceptionHandler(Class<E> occuredException) {
		
		if (asyncExceptions.isEmpty())
			return null;
		
		if (asyncExceptions.containsKey(occuredException))
			return (Slot<E>) asyncExceptions.get(occuredException);
			
		return null; 
	}
	
	public boolean hasAnySlots() {
		return hasAnySlots;
	}
	
	public List<Slot<R>> getInterSlotsToNotify() {
		return interSlotsToNotify;
	}

	public List<Slot<R>> getSlotsToNotify() {
		return slotsToNotify;
	}

	public List<TaskID<?>> getDependences() {
		return dependences;
	}

	public boolean isInteractive() {
		return isInteractive;
	}
	
	public boolean hasNoReturn(){
		return hasNoReturn;
	}
	
	protected void setInteractive(boolean isInteractive) {
		this.isInteractive = isInteractive;
	}
	
	protected void setMultiTask(boolean isMultiTask){
		this.isMultiTask = isMultiTask;
	}
	
	public boolean isMultiTask(){
		return this.isMultiTask;
	}

	protected boolean hasRegisteredHandlers() {
		return !asyncExceptions.isEmpty();
	}

	protected boolean isTaskInfoOfMultiTask() {
		return isTaskInfoOfMultiTask;
	}

	protected void setTaskInfoOfMultiTask(boolean isSubTask) {
		this.isTaskInfoOfMultiTask = isSubTask;
	}

	//This method is used by child classes to setup their task attributes
	protected void rudimentarySetup(TaskType taskType, int taskCount){
		
		switch(taskType){
		case MULTI:
			setMultiTask(true);
			setCount(taskCount);
			break;
		case MULTI_IO:
			setMultiTask(true);
			setInteractive(true);
			setCount(taskCount);
			break;
		case INTERACTIVE:
			setInteractive(true);
			setCount(1);
			break;
		case ONEOFF:
			setCount(1);
			break;
    	}		
	}
	
	void notify(Slot<R> handler) {
		if (slotsToNotify == null)
			slotsToNotify = new ArrayList<Slot<R>>();
		this.slotsToNotify.add(handler);
		hasAnySlots = true;
	}

	
	void dependsOn(TaskID<?>... taskIDs) {
		this.dependences = Arrays.asList(taskIDs);
	}

	abstract R execute();
}
