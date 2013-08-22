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

package paratask.runtime;

import java.util.concurrent.ExecutionException;

public class InteractiveThread extends TaskThread {

	private TaskID task = null;
	
	public InteractiveThread(Taskpool taskpool, TaskID task) {
		/* interactive threads don't need access to the taskpool */
		super(taskpool);
		this.task = task;
	}

	@Override
	public void run() {	
		
		boolean success = executeTask(task);
		if (success) {
			try {
				if (!task.cancelledSuccessfully())
					task.getReturnResult();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
//			if (task.hasUserError()) {
//				System.err.print(" --- This failure is user-error ( " + task.getException().getMessage() + " )  --- " );
//			}
//			System.err.println("FAILED!! InteractiveThread " + threadID + " ( " + Thread.currentThread().getId()+ ")"+ " wanted to execute task: " + task.getGlobalID() + " of method: " 
//					+ task.getTaskInfo().getMethod().getName());
		}
		taskpool.interactiveTaskCompleted(task);
		
		// TODO make this more efficient by recycling interactive threads
	}
}
