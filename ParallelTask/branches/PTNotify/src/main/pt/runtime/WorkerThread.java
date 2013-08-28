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


public class WorkerThread extends TaskThread {
	
	public WorkerThread(int id, Taskpool taskpool) {
		super(taskpool);
		
		// this ensures that WorkerThread IDs start from 0
		if (threadID != id)
			throw new IllegalArgumentException("WorkerID does not match - should create WorkerThreads first");
	}
	
	/** 
	 * This method is called to tell the worker to execute ONE other task from the taskpool (if it finds one), 
	 * otherwise it will sleep for the specified time delay
	 * 
	 * returns true if it did execute another task.. otherwise false if it ended up sleeping instead
	 * 
	 */
	public boolean executeAnotherTaskOrSleep() {
		TaskID task = taskpool.workerPollNextTask();
		if (task != null) {
			executeTask(task);
			return true;
		} else {
			try {
				Thread.sleep(ParaTaskHelper.WORKER_SLEEP_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	@Override
	public void run() {
		while (true) {
			TaskID task = taskpool.workerTakeNextTask();
			
			//- execute the task
			boolean success = executeTask(task);
			
			//-- Note: at this point, although the task has executed, it is not considered "complete" until the slots (if any) are executed by the registered thread.
			
			//-- The following is for testing purposes only, may be removed in final code
//			if (!success) {
////				System.err.print("FAILED!! WorkerThread " + threadID + " ( " + Thread.currentThread().getId()+ ")"+ " wanted to execute TaskID " + task.getGlobalID() + " of method: " 
////						+ task.getTaskInfo().getMethod().getName());
//				if (task.hasUserError()) {
////					System.err.println(" --- This is a user-error ( " + task.getException() + " )  --- " );
//				} else {
//					System.err.println("ParaTask faced a problem!! WorkerThread " + threadID + " ( " + Thread.currentThread().getId()+ ")"+ " wanted to execute TaskID " + task.getGlobalID() + " of method: " 
//							+ task.getTaskInfo().getMethod().getName());
//				}
//				System.err.flush();
//			}
		}
	}
}
