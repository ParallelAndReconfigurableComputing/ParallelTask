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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A future object representing a task invocation. As well as containing the return result for non-<code>void</code> tasks, 
 * the <code>TaskID</code> may also be used with <code>dependsOn</code>, cancel attempts, and other various functions. 
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 *
 * @param <T> The task's return type
 */
public class TaskID<T> {
	
	static protected AtomicInteger nextGlobalID = new AtomicInteger(-1);
	
	protected int globalID = -1;
	protected int relativeID = 0;
	
	protected TaskID<?> enclosingTask = null;	// this is used in case we need to find an asynchrous exception handler
	
	private int executeOnThread = ParaTaskHelper.ANY_THREAD_TASK;
	
	protected Task<T> taskInfo = null;
	private T returnResult = null;
	
	private int progress = 0;

	protected AtomicBoolean hasCompleted = null;
	protected boolean cancelled = false;
	protected AtomicBoolean cancelRequested = new AtomicBoolean(false);
	
    //-- the registering thread has its own latch, since it is allowed to progress inside slots of this TaskID
    private CountDownLatch completedLatchForRegisteringThread= null;
    
    //-- all the other threads (non-registering threads) must wait at this latch, until slots complete
    private CountDownLatch completedLatch = null;
    
	private ReentrantLock changeStatusLock = null;
	
	protected AtomicBoolean hasUserError = new AtomicBoolean(false);
	private Throwable exception = null;
	
	private boolean isInteractive = false;
	
	private ConcurrentLinkedQueue<TaskID<?>> waitingTasks = null;	//-- TaskIDs waiting for this task 
	private ConcurrentHashMap<TaskID<?>, Object> remainingDependences = null;	//-- TaskIDs this task is waiting for
	
	protected TaskIDGroup<?> group = null;
	
	protected boolean hasSlots = false;

	static final protected int CREATED = 0;
	static final protected int CANCELLED = 1;
	static final protected int STARTED = 2;
	protected AtomicInteger status = new AtomicInteger(CREATED);
	
	/**
	 * 
	 * @Author  Kingsley
	 * @since 04/05/2013
	 * 
	 * Later Expansion
	 * Use this to indicate how many sub tasks should be expanded.
	 * Can only be set the value from {@link AbstractTaskPool#enqueueMulti()}
	 * 
	 * */
	private int count = 0;
	
	protected int getCount() {
		return count;
	}

	protected void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * 
	 * @Author  Kingsley
	 * @since 21/05/2013
	 * 
	 * When a multi task is expanded, set this field to true for its every single sub tasks.
	 * 
	 * */
	private boolean isSubTask = false;

	protected boolean isSubTask() {
		return isSubTask;
	}

	protected void setSubTask(boolean isSubTask) {
		this.isSubTask = isSubTask;
	}

	/**
	 * Checks to see if this task has successfully cancelled.
	 * @return <code>true</code> if it has cancelled successfully, <code>false</code> otherwise. 
	 */
	public boolean cancelledSuccessfully() {
		return cancelled;
	}
	
	/**
	 * Checks to see if this task is an interactibe task.
	 * 
	 * @return <code>true</code> if this is an interactive task, <code>false</code> otherwise.
	 */
	public boolean isInteractive() {
		return isInteractive;
	}
	
	/**
	 * Sets the progress of the task.
	 * @param progress	The progress to set for the task
	 * @see CurrentTask#getProgress()
	 * @see CurrentTask#setProgress(int)
	 * @see #getProgress()
	 */
	void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * Returns the current progress of this task
	 * @return	The task's current progress.
	 * @see CurrentTask#getProgress()
	 * @see CurrentTask#setProgress(int)
	 * @see #setProgress(int)
	 * 
	 */
	public int getProgress() {
		return progress;
	}
	
	/**
	 * Checks to see if this task has been requested to cancel.
	 * @return <code>true</code> if it has been requested to cancel, <code>false</code> otherwise.
	 * @see CurrentTask#cancelRequested()
	 * @see #cancelAttempt()
	 * @see #cancelledSuccessfully()
	 */
	public boolean cancelRequested() {
		return cancelRequested.get();
	}
	
	TaskID(boolean alreadyCompleted) {
		if (alreadyCompleted) {
			globalID = nextGlobalID.incrementAndGet();
			completedLatch = new CountDownLatch(0);
			completedLatch = new CountDownLatch(0);
			hasCompleted = new AtomicBoolean(true);
			status = new AtomicInteger(STARTED);
		} else {
			throw new UnsupportedOperationException("Don't call this constructor if passing in 'false'!");
		}
	}
	
	/**
	 * 
	 * @author Kingsley
	 * @since 10/05/2013
	 * 
	 * Move the globalID allocation from the constructor of TaskID() 
	 * to the constructor of TaskID(TaskInfo taskInfo)
	 * 
	 * The idea is all subtasks of a multi task should share a global id,
	 * rather than give them a new one when they are created.
	 * 
	 * 
	 * @since 23/05/2013
	 * All subtasks of a multi task share a global id, this idea is good for understanding
	 * All subtasks have different global id, this idea is good for software engineering.
	 * If we treat each subtask the same as one-off task, which means we should give each
	 * subtask a unique global id.
	 * 
	 * */
	
	TaskID() {
		//globalID = nextGlobalID.incrementAndGet();
		completedLatch = new CountDownLatch(1);
		hasCompleted = new AtomicBoolean(false);
		status = new AtomicInteger(CREATED);
		changeStatusLock = new ReentrantLock();
	}
	
	TaskID(Task<T> taskInfo) {
		this();
		
		/*if(!taskInfo.isSubTask()){
			globalID = nextGlobalID.incrementAndGet();
		}*/
		
		globalID = nextGlobalID.incrementAndGet();
		
		completedLatchForRegisteringThread = new CountDownLatch(1);
		this.taskInfo = taskInfo;
		isInteractive = taskInfo.isInteractive();
		if (taskInfo != null) {
			hasSlots = taskInfo.getSlotsToNotify() != null;
//			hasHandlers = taskInfo.hasRegisteredHandlers();
		}
	}
	
	//-- return true if successfully canceled
	//-- TODO at the moment this is not a perfect implementation, since a 2nd (and 3rd, etc) call to this method might return false, 
	// even if it has successfully cancelled.. (this can be correctly by locking - but don't want to introcuce the overhead of locking every 
	// time a task is started..)
	/**
	 * Attempts to cancel the task. If cancelled successfully, the task will not be enqueued. A failed cancel 
	 * will still allow the task to continue executing. To stop the task, the task should check
	 * to see if a cancel request has been made. 
	 * @return <code>true</code> if it has cancelled successfully, <code>false</code> otherwise.
	 * @see #cancelRequested() 
	 * @see CurrentTask#cancelRequested()
	 * @see #cancelledSuccessfully()
	 */
	public boolean cancelAttempt() {
		cancelRequested.set(true);
		
		int prevStatus = status.getAndSet(CANCELLED);
		
		// TODO currently this only returns true if this is the first time attempting to cancel.. should be 
		// fixed so that it also returns true if it was cancelled before..(but this requires locking etc).
		
		if (prevStatus == CREATED || cancelled) {
			cancelled = true;
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the group that this task is part of (assuming it is a multi-task).  
	 * @return	Returns the group associated with this task. If not part of a multi-task, then
	 * returns <code>null</code>.
	 */
	public TaskIDGroup<?> getGroup() {
		return group;
	}
	
	boolean executeAttempt() {
		int prevStatus = status.getAndSet(STARTED);
		
		//-- TODO if the status was not CREATED before this, then should probably set status to CANCELLED 
		return prevStatus == CREATED;
	}
	
	void setEnclosingTask(TaskID<?> enclosingTask) {
		this.enclosingTask = enclosingTask;
	}
	
	TaskID<?> getEnclosingTask() {
		return enclosingTask;
	}
	
	void addWaiter(TaskID<?> waiter) {
		if (hasCompleted.get()) {
			waiter.dependenceFinished(this);
		} else {
			changeStatusLock.lock();
			
			if (!hasCompleted.get()) {
				if (waitingTasks == null)
					waitingTasks = new ConcurrentLinkedQueue<>();
				
				waitingTasks.add(waiter);
			} else {
				waiter.dependenceFinished(this);
			}
			changeStatusLock.unlock();
		}
	}
	
	/* one of the other Tasks (that this task dependsOn) has finished 
	 * 
	 * if that was the last Task we were waiting for, inform the taskpool
	 * 
	 * */
	void dependenceFinished(TaskID<?> otherTask) {
		remainingDependences.remove(otherTask);
		if (remainingDependences.isEmpty()) {
			TaskpoolFactory.getTaskpool().nowReady(this);
		}
	}
	
	void setRemainingDependences(List<TaskID<?>> deps) {
		remainingDependences = new ConcurrentHashMap<>();
		Iterator<TaskID<?>> it = deps.iterator();
		while (it.hasNext()) {
			remainingDependences.put(it.next(), "");
		}
	}
	
	/**
	 * Returns the task's globally-unique ID.
	 * @return	The task's unique ID.
	 * @see CurrentTask#globalID()
	 * @see CurrentTask#relativeID()
	 * @see #relativeID()
	 */
	public int globalID() {
		return globalID;
	}
	
	/**
	 * Returns the sub-task's relative ID in the multi-task. 
	 * @return	The position, starting from 0, of this sub-task compared to it's sibling subtasks.
	 * @see CurrentTask#globalID()
	 * @see CurrentTask#relativeID()
	 * @see #globalID()  
	 */
	public int relativeID() {
		return relativeID;
	}
	
	void setRelativeID(int relativeID) {
		this.relativeID = relativeID;
	}
	
	Task<T> getTaskInfo() {
		return taskInfo;
	}
	
	/**
	 * Returns the result of the task. If the task has not finished yet, the current thread blocks. 
	 * ParaTask worker threads will not block, instead they execute other ready tasks until
	 * this task completes.
	 * @return	The result of the task.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public T getReturnResult() throws ExecutionException, InterruptedException {
		waitTillFinished();
		if (cancelledSuccessfully())
			throw new ParaTaskRuntimeException("Attempting to get the result of a cancelled Task!");
		return returnResult;
	}
	
	/**
	 * Blocks the current thread until the task finishes. If the blocking thread is a 
	 * ParaTask worker thread, then other tasks are executed until this task completes. 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * 
	 * 
	 * @author Kingsley
	 * @since 25/05/2013
	 * 
	 * Before a worker tries to find another task and executes it, this thread should 
	 * check if there is cancel request.
	 * 
	 * If a worker thread is poisoned, even there are some unfinished children tasks,
	 * it will not get a chance to execute them, what it can do is check if they are 
	 * finished or not(executed by other thread)
	 * 
	 * @since 31/05/2013
	 * Simplify the implementation of reducing thread number.
	 * Do not need to access the "PoisonPillBox" to get a "pill"
	 * Instead, using the class of "LottoBox"
	 */
	public void waitTillFinished() throws ExecutionException, InterruptedException {		
		if (!hasCompleted.get()) {
			Thread t = Thread.currentThread();
			
			/* Only WorkerThreads should start a new TaskID.. all other threads belong to the user, or 
			 * are InteractiveThreads (therefore it is OK for them to block) */
			if (t instanceof WorkerThread) {
				WorkerThread currentWorker = (WorkerThread) t;
				
				// if still not completed then start a substitute thread
				
				/**
				 * 
				 * @author Kingsley
				 * Add a new check condition here in order to check if it is poisoned
				 * 
				 * */
				
				while (!hasCompleted.get()) {
					//System.out.println("inside " + currentWorker.threadID + " " + currentWorker.isCancelRequired() + " " + currentWorker.isPoisoned());
					if (currentWorker.isCancelRequired() && !currentWorker.isCancelled()) {
						LottoBox.tryLuck();
					}
					
					if (!currentWorker.isCancelled()) {
						// causes the worker to either execute a task or sleep
						currentWorker.executeAnotherTaskOrSleep();
					} else {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			} else {
				if (currentThreadIsTheRegisteredThread()) {
//					System.out.println("current thread is the registering thread.. will await");
					completedLatchForRegisteringThread.await();
//					System.out.println("finished await");
				} else {
					completedLatch.await();
				}
			}
		}
		
		//-- task has completed.. was there a user error?
		if (hasUserError.get()) {
			throw new ExecutionException(exception);
		}
	}
	
	protected boolean currentThreadIsTheRegisteredThread() {
		Thread registered = taskInfo.getRegisteringThread();
		if (registered == null)
			return false;
		if (registered == ParaTask.getEDT() && GuiThread.isEventDispatchThread())
			return true;
		else 
			return registered == Thread.currentThread();
	}
	
	void setReturnResult(T returnResult) {
		this.returnResult = returnResult;
	}
	
	/**
	 * Returns the exception that occurred while this task executed. 
	 * @return	The exception that occurred.
	 */
	public Throwable getException() {
		return exception;
	}
	
	void setException(Throwable exception) {
		this.exception = exception;
		hasUserError.set(true);
		if (group != null) {
			group.setException(exception);
		}
	}
	
	/**
	 * Checks to see whether the task has completed.
	 * @return	<code>true</code> if it has completed, <code>false</code> otherwise
	 * @see #getProgress()
	 * @see CurrentTask#getProgress()
	 */
	public boolean hasCompleted() {
		return hasCompleted.get();
	}

	/**
	 * Checks to see whether the task had any errors. 
	 * @return	<code>true</code> if there was an error, <code>false</code> otherwise
	 */
	public boolean hasUserError() {
		return hasUserError.get();
	}
	
	/**
	 * @author Kingsley
	 * @date 2014/04/08
	 * 
	 * Invoke all the dependent subtasks for group
	 * */
	void setComplete() {
		
//		System.out.println("SET COMPLETE...");
		
		/* dependences (gotta be atomic) */
		changeStatusLock.lock();
		TaskID<?> waiter = null;
		
		if (waitingTasks != null) {
			while ((waiter = waitingTasks.poll()) != null) {
				// removes the waiter from the queue
				waiter.dependenceFinished(this);
			}
		}
		
		completedLatchForRegisteringThread.countDown();	//-- in case there were no slots
		completedLatch.countDown();
		hasCompleted.set(true);
		changeStatusLock.unlock();
		
		if (group != null) {
			
			TaskID<?> groupWaiter = null;
			
			if (((TaskID<?>)group).waitingTasks != null) {
				ConcurrentLinkedQueue<TaskID<?>> groupWaitingTaskIDs = ((TaskID<?>)group).waitingTasks;
				
				while ((groupWaiter = groupWaitingTaskIDs.poll()) != null) {
					// removes the waiter from the queue
					//groupWaiter.dependenceFinished(group);
					
					ConcurrentHashMap<TaskID<?>, Object> groupRemainingDependences = groupWaiter.remainingDependences;
									
					groupRemainingDependences.remove(group);
					if (groupRemainingDependences.isEmpty()) {
						TaskpoolFactory.getTaskpool().nowReady(groupWaiter);
					}
					
				}
			}
			
			group.setComplete();
		}
	}
	
	/*
	 *	This will enqueue the notify slots and the exception handlers to be executed by the registered thread. 
	 *	Note that this TaskID is NOT considered completed, until all these slots are finished (even though the 
	 *	actual task logic has been executed, we need to wait for the slots before handling dependences, etc).
	 *
	 * 	Therefore, the registering thread will later set the status of this task as complete. 
	 */
	void enqueueSlots(boolean onlyEnqueueFinishedSlot) {
		
		// TODO make use of the boolean passed in (and for multi-tasks) - i.e. don't execute slots of cancelled tasks?
		
		if (group != null && group.isMultiTask()) {
			//-- part of a multi-task, will only enqueue the slots of the group when the last TaskID in the group completes
			group.oneMoreInnerTaskCompleted();
			setComplete();	// TODO  this was never here before -- was deadlocking without it... 20/3/2010 
			
		} else {
			//-- even if this TaskID is within a group, it is a separate entity since not a multi-task
			
			// TODO   if it has been cancelled, then probably don't want to execute the handlers... (except the setCompleteMethod())
			if (hasUserError() || hasSlots) {
				
//				System.out.println("HAS SLOTS..");
				completedLatchForRegisteringThread.countDown(); //-- so that registering thread will not block in slots 
				completedLatch.countDown();  // ADDED THIS??
				
				if (hasUserError.get())
					executeHandlers();
				if (hasSlots)
					executeSlots();
				
				//-- 		since slots are executed in the order they are enqueued, then this will be the last slot! :-)
				callTaskListener(new Slot(this::setComplete).setIsSetCompleteSlot(true));
				
			} else {
				setComplete();
			}
		}
	}
	
	protected Slot getExceptionHandler(Class<?> occurredException) {
		
		//-- first, try to get handler defined immediately for this task
		Slot handler = taskInfo.getExceptionHandler(occurredException);
		
		TaskID<?> curTask = this;
		//-- while we have not found a handler, and while there are other enclosing tasks
		while (handler == null && curTask.getEnclosingTask() != null) {
			curTask = curTask.getEnclosingTask();
			handler = curTask.getTaskInfo().getExceptionHandler(occurredException);
		}
		
		return handler;
	}
	
	//-- returns the number of handlers that it will execute for this TaskID
	private int executeHandlers() {
		Slot handler = getExceptionHandler(exception.getClass());
		
		if (handler != null) {
			callTaskListener(handler);
			return 1;
		} else {
			System.err.println("No asynchronous exception handler (i.e. asyncCatch clause) was specified when " +
					"invoking:\n" +
							"\n\tThe globalID of this task is "+ globalID() + ", and the encountered exception: ");
			exception.printStackTrace();
			return 0;
		}
	}
	
	void callTaskListener(Slot slot) {
//		System.out.println("want to execute slot: "+slot.getMethod().getName());
		ParaTask.getEDTTaskListener().executeSlot(slot);
//		System.out.println("executed slot: "+slot.getMethod().getName());
	}
	
	protected int executeIntermediateSlots() {
		return 0;
	}
	
	protected int executeSlots() {
		for (Iterator<Slot> it = taskInfo.getSlotsToNotify().iterator(); it.hasNext(); ) 
			callTaskListener(it.next());
		return taskInfo.getSlotsToNotify().size();
	}
	
	void setPartOfGroup(TaskIDGroup<?> group) {
		this.group = group;
	}
	
	/**
	 * Checks to see if this task is part of a multi-task group.
	 * @return <code>true</code> if this task is part of a multi-task, <code>false</code> otherwise 
	 */
	public boolean isMultiTask() {
		return group != null;
	}
	
	/**
	 * Returns the size of the multi-task this task is part of. 
	 * @return	The multi-task size, otherwise returns 1 if this task is not part of a multi-task.
	 */
	public int multiTaskSize() {
		if (group == null)
			return 1;
		return group.groupSize();
	}
	
	int getExecuteOnThread() {
		return executeOnThread;
	}
	
	void setExecuteOnThread(int executeOnThread) {
		this.executeOnThread = executeOnThread;
	}

	
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 10/05/2013
	 * 
	 * When multi task is expanded, call this method, and set global id for its sub tasks.
	 * 
	 *
	 * */
	protected void setGlobalID(int globalID) {
		this.globalID = globalID;
	}
	
	
}
