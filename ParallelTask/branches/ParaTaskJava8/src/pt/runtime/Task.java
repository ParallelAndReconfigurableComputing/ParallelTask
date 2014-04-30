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
import java.util.Iterator;
import java.util.List;

/**
 * 
 * Used to store certain information in order to invoke the task.. (i.e. before
 * invoking the task.. therefore information after the task has been invoked (eg
 * return value) are stored in the TaskID
 *
 */
public class Task<T> {
	static {
		ParaTask.init();
	}
	
	static final int STAR = 0;

	// for ParaTask in Java 8
	private Functor<?> lambda;
	private FunctorVoid lambdaVoid;
	
	private int taskCount = 1;

	private Thread registeringThread;
	private List<Slot> slotsToNotify;
	private List<Slot> interSlotsToNotify;
	private List<TaskID<?>> dependences;

	// for implicit results/dequeuing
	private int[] taskIdArgIndexes = new int[] {};
	private int[] queueArgIndexes = new int[] {};

	private boolean hasAnySlots = false;
	private boolean isPipeline = false;

	// -- Should always ensure that the registered exceptions are kept lined up
	// with the handlers
	private List<Class<?>> exceptions;
	private List<Slot> exceptionHandlers;

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

	private Task(Functor<?> lambda) {
		this.lambda = lambda;
	}

	private Task(FunctorVoid lambda) {
		this.lambdaVoid = lambda;
	}

	public boolean hasAnySlots() {
		return hasAnySlots;
	}
	
	private Task<T> setCount(int count) {
		if(count < 0) {
			throw new IllegalArgumentException("the value for task count must be greater than 0 or equal to STAR (0)");
		}
		this.taskCount = count;
		return this;
	}

	public boolean isPipeline() {
		return isPipeline;
	}

	public int[] getTaskIdArgIndexes() {
		return taskIdArgIndexes;
	}

	public int[] getQueueArgIndexes() {
		return queueArgIndexes;
	}

	public void setIsPipeline(boolean isPipeline) {
		this.isPipeline = isPipeline;
	}

	public void setTaskIdArgIndexes(int... indexes) {
		this.taskIdArgIndexes = indexes;
	}

	public void setQueueArgIndexes(int... indexes) {
		this.queueArgIndexes = indexes;
	}

	public Thread getRegisteringThread() {
		return registeringThread;
	}

	public Thread setRegisteringThread() {
		try {
			if (GuiThread.isEventDispatchThread())
				registeringThread = ParaTask.getEDT();
			else
				registeringThread = Thread.currentThread();
		} catch (Exception e) {
			registeringThread = Thread.currentThread();
		}
		return registeringThread;
	}

	public void setTaskIDForSlotsAndHandlers(TaskID<T> taskID) {
		if (slotsToNotify != null) {
			for (Iterator<Slot> it = slotsToNotify.iterator(); it.hasNext();) {
				it.next().setTaskID(taskID);
			}
		}
		if (interSlotsToNotify != null) {
			for (Iterator<Slot> it = interSlotsToNotify.iterator(); it
					.hasNext();) {
				it.next().setTaskID(taskID);
			}
		}
		if (exceptionHandlers != null) {
			for (Iterator<Slot> it = exceptionHandlers.iterator(); it
					.hasNext();) {
				it.next().setTaskID(taskID);
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
	public Task<T> asyncCatch(Class<?> exceptionClass,
			FunctionInterExceptionHandler handler) {
		if (this.exceptionHandlers == null) {
			exceptions = new ArrayList<>();
			exceptionHandlers = new ArrayList<>();
		}
		exceptions.add(exceptionClass);
		exceptionHandlers.add(new Slot(handler));
		hasAnySlots = true;
		return this;
	}

	/*
	 * This method returns the first suitable handler (if any is found) for the
	 * specified exception. It considers the correct inheritance structure, and
	 * the order of handlers considered is the same order as the programmer
	 * listed in the trycatch
	 */
	public Slot getExceptionHandler(Class<?> occuredException) {
		if (exceptions == null)
			return null;
		for (int i = 0; i < exceptions.size(); i++) {
			if (ParaTaskHelper.isSubClassOf(occuredException,
					exceptions.get(i)))
				return exceptionHandlers.get(i);
		}
		return null;
	}

	public List<Slot> getInterSlotsToNotify() {
		return interSlotsToNotify;
	}

	public List<Slot> getSlotsToNotify() {
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
		return exceptions != null
				&& !exceptions.isEmpty();
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

	public static <T> Task<T> asTask(Functor<T> fun) {
		return new Task<T>(fun);
	}
	
	public static Task<Void> asTask(FunctorVoid fun) {
		return new Task<Void>(fun);
	}
	
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
			slotsToNotify = new ArrayList<Slot>();
		this.slotsToNotify.add(new Slot(handler));
		hasAnySlots = true;
		return this;
	}

	public Task<T> withHandler(FunctorVoidWithOneArg<TaskID<?>> handler) {
		if (slotsToNotify == null)
			slotsToNotify = new ArrayList<Slot>();
		this.slotsToNotify.add(new Slot(handler));
		hasAnySlots = true;
		return this;
	}
	
	public Task<T> withHandler(FunctorVoid handler) {
		if (slotsToNotify == null)
			slotsToNotify = new ArrayList<Slot>();
		this.slotsToNotify.add(new Slot(handler));
		hasAnySlots = true;
		return this;
	}
	
	public Task<T> withInterimHandler(FunctorVoidWithTwoArgs<TaskID<?>, Object> handler) {
		if (interSlotsToNotify == null)
			interSlotsToNotify = new ArrayList<Slot>();
		interSlotsToNotify.add(new Slot(handler));
		return this;
	}

	public Task<T> dependsOn(TaskID<?>... taskIDs) {
		this.dependences = Arrays.asList(taskIDs);

		for (TaskID<?> t : taskIDs) {
			if (t.isPipeline()) {
				isPipeline = true;
				break;
			}
		}

		return this;
	}

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
