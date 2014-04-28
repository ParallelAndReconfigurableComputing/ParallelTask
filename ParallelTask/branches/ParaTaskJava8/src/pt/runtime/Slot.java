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

public class Slot {	
	final public static Slot quit = new Slot();

	private ConcurrentLinkedQueue<Object> interResults = null;
	private Class<?> interResultType = null;
	
	private boolean isIntermediateResultSlot;
	private boolean isASetCompleteSlot;
	
	private Future<?> taskID = null; 	// this is the task for which this slot is attached to (who should assign it?)
				// cannot be assigned at the time the slot is created (since the TaskID wasn't created just yet)
	
	private Functor<?> handler;
	private FunctorVoidWithOneArg<Future<?>> handlerWithArg;
	private FunctorVoid voidHandler;
	private FunctionInterExceptionHandler exceptionHanlder;
	private FunctorVoidWithTwoArgs<Future<?>, Object> interimHandler;
	
	private Slot() {
	}
	
	public Slot(Functor<?> handler) {
		this.handler = handler;
	}
	
	public Slot(FunctorVoid handler) {
		this.voidHandler = handler;
	}
	
	public Slot(FunctorVoidWithOneArg<Future<?>> handlerWithArg) {
		this.handlerWithArg = handlerWithArg;
	}
	
	public Slot(FunctionInterExceptionHandler handler) {
		this.exceptionHanlder = handler;
	}
	
	public Slot(FunctorVoidWithTwoArgs<Future<?>, Object> interimHandler) {
		this.interimHandler = interimHandler;
		this.isIntermediateResultSlot = true;
	}
	
	public Slot setIsSetCompleteSlot(boolean setComplete) {
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
		interResults.add(value);
	}
	
	private Object getNextIntermediateResultValue() {
		return interResults.poll();
	}
	
	public Class<?> getIntermediateResultType() {
		return interResultType;
	}

	public Future<?> getTaskID() {
		return taskID;
	}

	public void setTaskID(Future<?> taskID) {
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
