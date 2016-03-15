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

import java.util.concurrent.ExecutionException;

//Separate interactive threads are created for independent interactive tasks.
public class InteractiveThread extends TaskThread {

	private TaskID<?> taskID = null;
	
	InteractiveThread(Taskpool taskpool, TaskID<?> taskID) {
		/* interactive threads don't need access to the taskpool */
		super(taskpool);
		this.taskID = taskID;
	}

	@Override
	public void run() {	
		
		boolean success = executeTask(taskID);
		if (success) {
			if (!taskID.hasBeenCancelled())
				taskID.getReturnResult();
		} else {
//			if (task.hasUserError()) {
//				System.err.print(" --- This failure is user-error ( " + task.getException().getMessage() + " )  --- " );
//			}
//			System.err.println("FAILED!! InteractiveThread " + threadID + " ( " + Thread.currentThread().getId()+ ")"+ " wanted to execute task: " + task.getGlobalID() + " of method: " 
//					+ task.getTaskInfo().getMethod().getName());
		}
		taskpool.interactiveTaskCompleted(taskID);
		
		// TODO make this more efficient by recycling interactive threads
	}
}
