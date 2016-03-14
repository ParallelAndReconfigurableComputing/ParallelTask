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
import java.util.concurrent.ExecutionException;

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
public class Slot<R>{	
	//final public static Slot<R> quit = new Slot<R>();

	protected ConcurrentLinkedQueue<Object> interResults = null;
	protected Class<?> interResultType = null;
		
	protected boolean isIntermediateResultSlot;
	protected boolean isASetCompleteSlot;
	
	private FunctorNoArgsNoReturn functorNoArg = null;
	private FunctorOneArgNoReturn<R> functorOneArg = null;
	private R taskResult = null;
	// this is the task for which this slot is attached (who should assign it?)
	// cannot be assigned at the time the slot is created (since the TaskID wasn't created just yet)
	protected TaskID<R> taskID = null; 		
	
	Slot(){}
	
	Slot(FunctorOneArgNoReturn<R> functor, TaskID<R> taskID){
		this.taskID = taskID;
		this.functorOneArg = functor;
	}
	
	Slot(FunctorOneArgNoReturn<R> functor){
		this.functorOneArg = functor;
	}
	
	Slot(FunctorNoArgsNoReturn functor){
		this.functorNoArg = functor;
	}
	
	public Slot<R> setIsSetCompleteSlot(boolean setComplete) {
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
	
	public Class<?> getIntermediateResultType() {
		return interResultType;
	}

	public TaskID<R> getTaskID() {
		return taskID;
	}
	
	public void setTaskID(TaskID<R> taskID) {
		this.taskID = taskID;
	}

	public boolean isIntermediateResultSlot() {
		return isIntermediateResultSlot;
	}
	
	private R getTaskResult(){
		try {
			return taskID.getReturnResult();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	void execute(){
		if (this.functorOneArg!=null){
			functorOneArg.exec(this.getTaskResult());
			return;
		}
		functorNoArg.exec();
	}
}
	
	
		
