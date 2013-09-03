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
	
	/**
	 * 
	 * @Author Kingsley
	 * @since 25/04/2013
	 * 
	 * When creating a new worker, indicate it is dedicated for multi task or dedicated for one-off task
	 * 
	 * @since 16/05/2013
	 * Add a variable to check if the worker thread should go out of the loop.
	 * 
	 * @since 23/05/2013
	 * Add a variable to indicate if the worker thread is waiting for tasks completion
	 * 
	 * @since 24/05/2013
	 * Create a static variable used to tell if some threads need to be cancelled or not.
	 * 
	 * @since 25/05/2013
	 * Change "isKilled" to "isPoisoned"
	 * Think it is un-necessary to distinguish nested and non-nested situation. Cancel "isWaiting".
	 * 
	 * */
	private boolean isMultiTaskWorker;
	
	//private boolean isKilled = false;
	private boolean isPoisoned = false;
	
	//private boolean isWaiting = false;
	
	private boolean isCancelRequired = false; 
	
	public WorkerThread(int globalID, int localID, Taskpool taskpool, boolean isMultiTaskWorker) {
		super(taskpool, isMultiTaskWorker);
		
		// this ensures that WorkerThread IDs start from 0
		
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 26/04/2013
		 * 
		 * Still remain the threadID check.
		 * 
		 * Thread id check has to be separated.
		 * 
		 * @since : 02/05/2013
		 * One-off task threads do not need local thread ID.
		 * 
		 * */
		
		if (threadID != globalID)
			throw new IllegalArgumentException("WorkerID does not match - should create WorkerThreads first");
	
		
		if (isMultiTaskWorker) {
			if (threadLocalID != localID)
				throw new IllegalArgumentException("WorkerID does not match - should create WorkerThreads first");
		}/*else {
			if (oneoffTaskThreadID != localID)
				throw new IllegalArgumentException("WorkerID does not match - should create WorkerThreads first");
			
		}*/

		this.isMultiTaskWorker = isMultiTaskWorker;
	}
	
	/** 
	 * This method is called to tell the worker to execute ONE other task from the taskpool (if it finds one), 
	 * otherwise it will sleep for the specified time delay
	 * 
	 * returns true if it did execute another task.. otherwise false if it ended up sleeping instead
	 * 
	 * 
	 * @author Kingsley
	 * @since 23/05/2013
	 * If worker thread find a poison pill from within this "executeAnotherTaskOrSleep()" method, which means
	 * it is waiting for some other tasks finish first. 
	 * Set "isWaiting" to true, in order to examine this variable inside of the poison pill.
	 * 
	 * @since 25/05/2013
	 * Think it is un-necessary to distinguish nested and non-nested situation. Cancel "isWaiting".
	 */
	public boolean executeAnotherTaskOrSleep() {

		TaskID task = taskpool.workerPollNextTask();
		if (task != null) {
			//isWaiting = true;
			
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
	
	/**
	 * @author Kingsley
	 * @since 23/05/2013
	 * 
	 * If worker thread find a poison pill from within this "run()" method, which means
	 * it is not waiting for some other tasks finish first. 
	 * Set "isWaiting" to false, in order to examine this variable inside of the poison pill.
	 * 
	 * @since 25/05/2013
	 * Think it is un-necessary to distinguish nested and non-nested situation. Cancel "isWaiting".
	 * */
	
	
	@Override
	public void run() {
		while (true) {
			TaskID task = taskpool.workerTakeNextTask();
			//System.out.println(Thread.currentThread().getId());
			//- execute the task
			
			//isWaiting = false;
			
			boolean success = executeTask(task);
			
			/**
			 * @author Kingsley
			 * @since 16/05/2013
			 * 
			 * After the worker thread found a fake task, it may get a chance to go out of the loop.
			 * 
			 * @since 24/05/2013
			 * If worker thread can pick up task from within this method, which means it is not 
			 * under a nested situation.
			 * 
			 * And only "isCancelRequired" = true, can break the loop
			 * 
			 * @since 25/05/2013
			 * If "isCancelRequired" = true, which means current thread has a chance to take a 
			 * poison pill but, before that, it may have been poisoned.
			 * 
			 * @since 31/05/2013
			 * Simplify the implementation of reducing thread number.
			 * Do not need to access the "PoisonPillBox" to get a "pill"
			 * Instead, using the class of "LottoBox"
			 * 
			 * */
			//System.out.println("outside " + threadID + " " + isCancelRequired + " " + isPoisoned);

			if (isCancelRequired) {
				if (isPoisoned) {
					break;
				}else {
					/*PoisonPill pill = PoisonPillBox.getPill();
					if (null != pill) {
						pill.tryKill();
					}else {
						//Test only branch
						//System.out.println(threadID + " - null - ? ");
					}*/
					
					LottoBox.tryLuck();
					
					if (/*isKilled*/ isPoisoned) {
						break;
					}
				}
			}
			
			
			
			
			
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

	protected boolean isMultiTaskWorker() {
		return isMultiTaskWorker;
	}

	protected void setMultiTaskWorker(boolean isMultiTaskWorker) {
		this.isMultiTaskWorker = isMultiTaskWorker;
	}

	/*protected void setKilled(boolean isKilled) {
		this.isKilled = isKilled;
	}*/
	protected void setPoisoned(boolean isPoisoned) {
		this.isPoisoned = isPoisoned;
	}

	protected boolean isPoisoned() {
		return isPoisoned;
	}

	/**
	 * 
	 * @author Kingsley
	 * @since 25/05/2013
	 * Think it is un-necessary to distinguish nested and non-nested situation. Cancel "isWaiting".
	 * 
	 * */
	
	/*protected boolean isWaiting() {
		return isWaiting;
	}*/

	

	/**
	 * 
	 * @author Kingsley
	 * @since 24/05/2013
	 * @param boolean cancelRequired
	 * 
	 * Used by thread pool to tell worker threads that some threads need to be killed.
	 * 
	 * 
	 * */
	protected void requireCancel(boolean cancelRequired){
		this.isCancelRequired = cancelRequired;
	}

	protected boolean isCancelRequired() {
		return isCancelRequired;
	}
}
