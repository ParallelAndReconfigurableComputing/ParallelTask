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
import pt.functionalInterfaces.FunctorNoArgsNoReturn;
import pt.functionalInterfaces.FunctorOneArgNoReturn;

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
public class Slot<T>{	
	//final public static Slot<R> quit = new Slot<R>();

	protected ConcurrentLinkedQueue<Object> interResults = null;
	protected Class<?> interResultType = null;
		
	protected boolean isIntermediateResultSlot;
	protected boolean isASetCompleteSlot;
	
	private FunctorNoArgsNoReturn functorNoArg = null;
	private FunctorOneArgNoReturn<T> functorOneArg = null;
	
	// incurredException must be used for Slot objects that
	// are used as exception handlers. 
	private T incurredException = null;
	// The TaskID of a slot is added to it when enqueueing a taskInfo, by 
	// calling the setTaskIDForSlots( ) method on that taskInfo object. 
	// This method is called by the workPool instance, when enqueueing a
	// on-off or a multi-task.
	protected TaskID<T> taskID = null; 		
	
	Slot(){}
	
	Slot(FunctorOneArgNoReturn<T> functor){
		this.functorOneArg = functor;
	}		
	
	Slot(FunctorNoArgsNoReturn functor){
		this.functorNoArg = functor;
	}
	
	public Slot<T> setIsSetCompleteSlot(boolean setComplete) {
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
	
	Object getNextIntermediateResultValue() {
		return interResults.poll();
	}
	
	Class<?> getIntermediateResultType() {
		return interResultType;
	}

	TaskID<T> getTaskID() {
		return taskID;
	}
	
	void setTaskID(TaskID<T> taskID) {
		this.taskID = taskID;
	}

	boolean isIntermediateResultSlot() {
		return isIntermediateResultSlot;
	}
		
	//T is used as a generic in order to allow "Slot" work for a wider range
	//of functionalities, but for exception handlers it must be a Throwable!
	void setExceptionObject(T exception){
		this.incurredException = exception;
	}
	
	T getArgument(){
		if(incurredException != null){
			return incurredException;
		}
		else
			return taskID.getReturnResult();
	}

	void execute(){
		if (this.functorOneArg!=null){
			functorOneArg.exec(getArgument());
			return;
		}
		functorNoArg.exec();
	}
}
	
	
		
