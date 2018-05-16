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

import java.util.concurrent.atomic.AtomicBoolean;

//Separate interactive threads are created for independent interactive tasks.
public class InteractiveThread extends TaskThread {

	private TaskID<?> taskID = null;
	private AtomicBoolean alive = new AtomicBoolean();
	private AtomicBoolean newTaskReceived = new AtomicBoolean();
	
	InteractiveThread(Taskpool taskpool, TaskID<?> taskID) {
		super(taskpool);
		alive.set(true);
		newTaskReceived.set(false);
		this.taskID = taskID;
	}
	
	boolean isInactive(){
		return(taskID == null && alive.get());
	}
	
	void setTaskID(TaskID<?> taskID){
		this.taskID = taskID;
		this.newTaskReceived.set(true);
		interrupt();
	}
	
	private void resetThread(){
		taskpool.interactiveTaskCompleted(taskID);
		this.taskID = null;
		this.newTaskReceived.set(false);
	}

	@Override
	public void run() {	
		while(taskID != null){
			boolean success = executeTask(taskID);
			if (success) {
				if (!taskID.hasBeenCancelled()){
					taskID.getReturnResult();
					taskpool.interactiveTaskCompleted(taskID);	
				}
			} 
			
			resetThread();
			try{
				Thread.sleep(ParaTask.INTERACTIVE_SLEEP_DELAY);
			}catch(InterruptedException e){
				if(!newTaskReceived.get())//if the interrupt exception is not due to a new task, then kill the thread.
					break;
				Thread.interrupted();//for clearing the flag!
			}
		}
		
		/*without this flag, sometimes a taskID is added exactly before
		 * run() method ends, and therefore taskID won't be executed.*/
		alive.set(false);
	}
}
