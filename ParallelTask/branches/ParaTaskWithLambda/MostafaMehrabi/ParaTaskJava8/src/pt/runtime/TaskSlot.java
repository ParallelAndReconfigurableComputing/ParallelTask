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

import java.util.concurrent.ConcurrentLinkedQueue;
/**
 *This class allows the user to define a handler from one of the Functor types (functional interfaces)
 * which could be done by using lambda expressions as well. The user-defined handler will be associated
 * to the corresponding Functor instance in this class. Once the <code>execute</code> method of a <code>slot</code>
 * is called, the method executes the <code>execute</code> method of the corresponding handler which is 
 * an instance of one of the Fucntors. 
 * <br><br>
 * This class also allows storing and retrieving the intermediate results of a taskID.
 * 
 * @author Mostafa Mehrabi
 * @since  4/9/2014
 * 
 * */
//Add the explanation for 'setComplete' later!
public class TaskSlot {	
	final public static TaskSlot quit = new TaskSlot();

	private ConcurrentLinkedQueue<Object> interResults = null;
	private Class<?> interResultType = null;
	
	private boolean isIntermediateResultSlot;
	private boolean isASetCompleteSlot;
	
	private TaskID<?> taskID = null; 	// this is the task for which this slot is attached to (who should assign it?)
				// cannot be assigned at the time the slot is created (since the TaskID wasn't created just yet)
	
	private Functor<?> handler;
	private FunctorVoidWithOneArg<TaskID<?>> handlerWithArg;
	private FunctorVoid voidHandler;
	private FunctionInterExceptionHandler exceptionHanlder;
	private FunctorVoidWithTwoArgs<TaskID<?>, Object> interimHandler;
	
	private TaskSlot() {
	}
	
	public TaskSlot(Functor<?> handler) {
		this.handler = handler;
	}
	
	public TaskSlot(FunctorVoid handler) {
		this.voidHandler = handler;
	}
	
	public TaskSlot(FunctorVoidWithOneArg<TaskID<?>> handlerWithArg) {
		this.handlerWithArg = handlerWithArg;
	}
	
	public TaskSlot(FunctionInterExceptionHandler handler) {
		this.exceptionHanlder = handler;
	}
	
	public TaskSlot(FunctorVoidWithTwoArgs<TaskID<?>, Object> interimHandler) {
		this.interimHandler = interimHandler;
		this.isIntermediateResultSlot = true;
	}
	
	public TaskSlot setIsSetCompleteSlot(boolean setComplete) {
		this.isASetCompleteSlot = setComplete;
		return this;
	}	
	
	public boolean isASetCompleteSlot() {
		return this.isASetCompleteSlot;
	}
	
	public void addIntermediateResult(Class<?> type, Object value) {
		if (interResults == null) {
			interResults = new ConcurrentLinkedQueue<Object>();
			interResultType = type;
		}
		
		//else if (interResultType.equals(type))
		interResults.add(value);
	}
	
	private Object getNextIntermediateResultValue() {
		return interResults.poll();
	}
	
	public Class<?> getIntermediateResultType() {
		return interResultType;
	}

	public TaskID<?> getTaskID() {
		return taskID;
	}
	//how is a slot going to use a taskID?
	public void setTaskID(TaskID<?> taskID) {
		this.taskID = taskID;
	}

	public boolean isIntermediateResultSlot() {
		return isIntermediateResultSlot;
	}
	
	void execute() {
		if(this.handler != null)
			this.handler.exec();
		
		if(this.handlerWithArg != null)
			this.handlerWithArg.exec(this.taskID);
		
		if(this.voidHandler != null)
			this.voidHandler.exec();
		
		if(this.interimHandler != null)
			this.interimHandler.exec(this.taskID, getNextIntermediateResultValue());
		
		if(this.exceptionHanlder != null)
			this.exceptionHanlder.doWork(this.taskID.getException());
	}
}
