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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class Task<T> {
	static {
		ParaTask.init();
	}
	
	static final int STAR = 0;

	// for ParaTask in Java 8
	private Functor<?> lambda;
	private FunctorVoid lambdaVoid;
	private StandardFunctor<?, ?> standardFunctor;
	
	private int taskCount = 1;

	private Thread registeringThread;
	private List<TaskSlot> slotsToNotify;
	private List<TaskSlot> interSlotsToNotify;
	private List<TaskID<?>> dependences;

	// for implicit results/dequeuing
	private int[] taskIdArgIndexes = new int[] {};
	private int[] queueArgIndexes = new int[] {};

	private boolean hasAnySlots = false;

	// -- Should always ensure that the registered exceptions are kept lined up
	// with the handlers
	// Maybe using a Map was better,
	//private List<Class<?>> exceptions = null;//keeps the records of the exceptions occurred 
	//private List<Slot> exceptionHandlers = null;//keeps the records of the handlers corresponding to those exceptions
	private Map<Class<?>, TaskSlot> asyncExceptions = new HashMap<Class<?>, TaskSlot>();

	private boolean isInteractive = false;

	/**
	 * 
	 * @author Kingsley
	 * @since 10/05/2013
	 * 
	 *        Used to identify if it is a sub task for a multi task
	 * 
	 * */
	private boolean isSubTask = false;

	//These constructors can at least automate the process of creating TaskID.
	private Task(Functor<?> lambda) {
		this.lambda = lambda;
	}

	private Task(FunctorVoid lambda) {
		this.lambdaVoid = lambda;
	}
	
	private <R, P> Task(StandardFunctor<R, P> standardFunctor){
		this.standardFunctor = standardFunctor;
	}

	/**
	 * Sets the number of tasks in a multiple-task.
	 * 
	 * @param count The number of multiple-tasks
	 * 
	 */
	//some methods are returning the current instance of Task<T> which seems to be pointless
	private Task<T> setCount(int count) {
		if(count < 0) {
			throw new IllegalArgumentException("the value for task count must be greater than 0 or equal to STAR (0)");
		}
		this.taskCount = count;
		return this;
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
		if (registeringThread.isAlive())
			return registeringThread;
		return null;
	}

	//Why do we have to check if the GUI thread is the EDT?
	public Thread setRegisteringThread() {
		try {
			if (GuiThread.isEventDispatchThread())//if the current thread is an event dispatch thread
				registeringThread = ParaTask.getEDT();
			else
				registeringThread = Thread.currentThread();
		} catch (Exception e) {
			registeringThread = Thread.currentThread();
		}
		return registeringThread;
	}

	//make sure these steps are automated.. Should it be automated?
	public void setTaskIDForSlotsAndHandlers(TaskID<T> taskID) {
		if (slotsToNotify != null) {
			for (Iterator<TaskSlot> it = slotsToNotify.iterator(); it.hasNext();) {
				it.next().setTaskID(taskID);
			}
		}
		if (interSlotsToNotify != null) {
			for (Iterator<TaskSlot> it = interSlotsToNotify.iterator(); it.hasNext();) {
				it.next().setTaskID(taskID);
			}
		}
		
		if (!asyncExceptions.isEmpty()){
			Set<Class<?>> exceptionClasses = asyncExceptions.keySet();
			for (Class<?> exception : exceptionClasses){
				TaskSlot handler = asyncExceptions.get(exception);
				handler.setTaskID(taskID);
			}
		}
	}

	/**
	 * This method should be used in the order that the exception handlers are
	 * to be considered later on
	 * 
	 * @param exceptionClass
	 * @param handler
	 */
	public Task<T> asyncCatch(Class<?> exceptionClass, FunctionInterExceptionHandler handler) {
		
		if (exceptionClass == null)
			throw new IllegalArgumentException("There is no exception class specified!");
		else if (handler == null)
			throw new IllegalArgumentException("There is no exception handler specified for this exception");
	
		asyncExceptions.put(exceptionClass, (TaskSlot)handler);
		hasAnySlots = true;
		return this;
	}

	/**
	 * Returns the handler associated to the specified exception. 
	 */
	public TaskSlot getExceptionHandler(Class<?> occuredException) {
		
		if (asyncExceptions.isEmpty())
			return null;
		
		else if (!asyncExceptions.containsKey(occuredException))
			return null;
			
		else 
			return asyncExceptions.get(occuredException);
	}
	
	public boolean hasAnySlots() {
		return hasAnySlots;
	}
	
	public List<TaskSlot> getInterSlotsToNotify() {
		return interSlotsToNotify;
	}

	public List<TaskSlot> getSlotsToNotify() {
		return slotsToNotify;
	}

	public List<TaskID<?>> getDependences() {
		return dependences;
	}

	public boolean isInteractive() {
		return isInteractive;
	}

	private Task<T> setInteractive(boolean isInteractive) {
		this.isInteractive = isInteractive;
		return this;
	}

	public boolean hasRegisteredHandlers() {
		return !asyncExceptions.isEmpty();
	}

	/**
	 * 
	 * @author Kingsley
	 * @since 10/05/2013
	 * 
	 *        Getter and Setter for isSubTask
	 * 
	 * */
	protected boolean isSubTask() {
		return isSubTask;
	}

	protected void setSubTask(boolean isSubTask) {
		this.isSubTask = isSubTask;
	}

	//returns a functor as a task
	public static <T> Task<T> asTask(Functor<T> fun) {
		return new Task<T>(fun);
	}
	
	public static Task<Void> asTask(FunctorVoid fun) {
		return new Task<Void>(fun);
	}
	
	public static <R, P, T> Task<T> asTask(StandardFunctor<R, P> standardFunctor){
		return new Task<T>(standardFunctor);
	}
	
	//Once the final policy for which "Functor" to use is decide, make sure that 
	//asTask, asMultiple and asIOTask methods are modified accordingly.
	public static <T> Task<T> asMultiTask(Functor<T> fun, int count) {
		return new Task<T>(fun).setCount(count);
	}
	
	public static <T> Task<T> asMultiTask(Functor<T> fun) {
		return new Task<T>(fun).setCount(STAR);
	}
	
	public static Task<Void> asMultiTask(FunctorVoid fun, int count) {
		return new Task<Void>(fun).setCount(count);
	}
	
	public static Task<Void> asMultiTask(FunctorVoid fun) {
		return new Task<Void>(fun).setCount(STAR);
	}

	public static <T> Task<T> asIOTask(Functor<T> fun) {
		return new Task<T>(fun).setInteractive(true);
	}

	public static Task<Void> asIOTask(FunctorVoid fun) {
		return new Task<Void>(fun).setInteractive(true);
	}

	public <T2> Task<T> withHandler(Functor<T2> handler) {
		if (slotsToNotify == null)
			slotsToNotify = new ArrayList<TaskSlot>();
		this.slotsToNotify.add(new TaskSlot(handler));
		hasAnySlots = true;
		return this;
	}

	public Task<T> withHandler(FunctorVoidWithOneArg<TaskID<?>> handler) {
		if (slotsToNotify == null)
			slotsToNotify = new ArrayList<TaskSlot>();
		this.slotsToNotify.add(new TaskSlot(handler));
		hasAnySlots = true;
		return this;
	}
	
	public Task<T> withHandler(FunctorVoid handler) {
		if (slotsToNotify == null)
			slotsToNotify = new ArrayList<TaskSlot>();
		this.slotsToNotify.add(new TaskSlot(handler));
		hasAnySlots = true;
		return this;
	}
	
	//with interim handlers could be pointless and unnecessary	
	public Task<T> withInterimHandler(FunctorVoidWithTwoArgs<TaskID<?>, Object> handler) {
		if (interSlotsToNotify == null)
			interSlotsToNotify = new ArrayList<TaskSlot>();
		interSlotsToNotify.add(new TaskSlot(handler));
		return this;
	}

	public Task<T> dependsOn(TaskID<?>... taskIDs) {
		this.dependences = Arrays.asList(taskIDs);
		return this;
	}

	//This is where a Task returns its corresponding TaskID
	public TaskID<T> start() {
		if(this.taskCount == 1)
			return TaskpoolFactory.getTaskpool().enqueue(this);
		else
			return TaskpoolFactory.getTaskpool().enqueueMulti(this, this.taskCount);
	}

	Object execute() {
		if (this.lambda != null)
			return this.lambda.exec();
		else {
			this.lambdaVoid.exec();
			return null;
		}
	}
}
