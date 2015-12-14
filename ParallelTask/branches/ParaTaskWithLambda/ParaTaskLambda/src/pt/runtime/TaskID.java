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

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A future object representing a task invocation. As well as containing the return result for non-<code>void</code> tasks, 
 * the <code>TaskID</code> may also be used with <code>dependsOn</code>, cancel attempts, and other various functions. 
 * <br><br>
 * Every aspect that deals with a task during its execution will go through this class <code>'TaskID'</code>. This class 
 * keeps the information regarding<br>
 * 1- A task's global ID and its relative ID (relativeID is used within a multi-task group).<br>
 * 2- The enclosing task of a specific instance (for finding asynchronous exceptions).<br>
 * 3- The initial information of this instance (i.e. taskInfo which is an instance of Task).<br>
 * 4- Indicates if a task can be executed by any arbitrary thread.<br>
 * 5- Records and returns the final result of a task.<br>
 * 6- Indicates if an instance of task has completed its execution.<br>
 * 7- Indicates if an instance of task has been requested to cancel.<br>
 * 8- Indicates if an instance of task has been successfully canceled (first cancel request, then practically cancel).<br>
 * 9- Keeps track of the progress of an instance of the task. <br>
 * 10-Returns the corresponding exception handler for a specific exception class by communicating with taskInfo<br> 
 * 11-Each instance has two count down latches. One for the registering thread, and one for other threads. Because<br>
 *    the registering thread is allowed to process through the slots, and we want to unblock the threads that are <br>
 *    waiting for the task to finish, before it starts proceeding through the slots (handlers)
 *<br><br>
 *Each task can have three states, <code>CREATED, CANCELLED</code> and <code>STARTED</code>. By default a task's status is 
 *set to CREATED, when its constructor is called. For a task to be executed or complete the execution it needs to be
 *on STARTED status. Whenever a task is created the count down latches will be set to one to block the threads that depend 
 *on this task, and when that task is completed, or cancelled the count down latches are set back to zero to unblock the 
 *threads.<br><br>
 *All sub-tasks of a multi-task share the same globalID, at the time of creation. A taskID holds the information about
 *whether a task is interactive, and whether it has slots (handlers) to notify from the task.<br><br>
 *Moreover, a taskID enables requesting for cancellation of the instance of task, allows recording the tasks that are
 *waiting for the instance of task to finish, returns the final result of a task and also provides a mechanism through 
 *which other threads can wait for the instance of task to finish (or do another task meanwhile they wait for the instance
 *of task to finish). For a task in order to complete, all the slots stored in the task need to be executed; therefore the 
 *method 'enqueueSlots' must be called.      
 * 
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 * @author Mostafa Mehrabi
 * 
 * @since  8/9/2014
 *
 * @param <T> The task's return type
 */
//a TaskID's return type (i.e., T) is possibly equivalent to the return type of 
//a functor (i.e., R).
public class TaskID<T> {
	static {
		if (!ParaTask.isInitialized()){
			ParaTask.init();
		}
		if (!ParaTask.hasParaTaskStarted()){
			ParaTask.paraTaskStarted(true);
		}
	}
	//private int count = 0;
	
	/**
	 * 
	 * @author  Kingsley
	 * @since 21/05/2013
	 * 
	 * When a multi task is expanded, set this field to <u><b>true</u></b> for its every single sub tasks.
	 * 
	 * */
	//isSubTask and isInteractive also exist in Task, which one to use?
	private boolean isSubTask = false;

	
	static protected AtomicInteger nextGlobalID = new AtomicInteger(-1);
	
	protected int globalID = -1;
	protected int relativeID = 0;
	
	// This attribute is used in case we need to find an asynchronous exception handler
	// of a task, within the exception handlers of its enclosing tasks.
	protected TaskID<?> enclosingTask = null;	
	
	private int executeOnThread = ParaTask.ANY_THREAD_TASK;
	
	protected TaskInfo<T> taskInfo = null;
	private T returnResult = null;
	
	private int progress = 0;

	//protected AtomicBoolean hasCompleted = null;
	//protected boolean cancelled = false;
	protected AtomicBoolean cancelRequested = new AtomicBoolean(false);
	
    //-- the registering thread has its own latch, since it is allowed to progress inside slots of this TaskID
    private CountDownLatch completedLatchForRegisteringThread= null;
    
    //-- all the other threads (non-registering threads) must wait at this latch, until slots complete
    private CountDownLatch completedLatchForNonRegisteringThreads = null;
    
	private ReentrantLock changeStatusLock = null;
	
	protected AtomicBoolean hasUserError = new AtomicBoolean(false);
	private Throwable exception = null;
	
	private boolean isInteractive = false;
	/**
	 * TaskIDs waiting for this task to finish.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  4/8/2015
	 * */
	private ConcurrentLinkedQueue<TaskID<?>> waitingTasks = new ConcurrentLinkedQueue<>();
	
	/**
	 * TaskIDs that this task is waiting for to finish.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  4/8/2015
	 * */
	private ConcurrentLinkedQueue<TaskID<?>> remainingDependences = new ConcurrentLinkedQueue<>();
	//private ConcurrentHashMap<TaskID<?>, Object> remainingDependences = new ConcurrentHashMap<>();	//-- TaskIDs this task is waiting for
	
	/**
	 * The group of tasks that this task belongs to, if it is a <code>subtask</code> of 
	 * a <code>multi-task</code>. Otherwise, this attribute remains null for a <code>one-off</code>
	 * or an <code>I/O task</code>.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  4/8/2015
	 * */
	protected TaskIDGroup<?> group = null;
	
	protected boolean hasSlots = false;

	static final protected int CREATED = 0;
	static final protected int CANCELLED = 1;
	static final protected int STARTED = 2;
	static final protected int COMPLETED = 3;
	protected AtomicInteger status = null; 
	
	/*
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
		completedLatchForNonRegisteringThreads = new CountDownLatch(1);
		//hasCompleted = new AtomicBoolean(false);
		status = new AtomicInteger(CREATED);
		changeStatusLock = new ReentrantLock();
	}
	

	/**
	 * This constructor receives information about, whether a task is interactive
	 * as well as it sets the count down latch to one. 
	 * */
	public TaskID(TaskInfo<T> taskInfo) {
		this();		
		globalID = nextGlobalID.incrementAndGet();
		completedLatchForRegisteringThread = new CountDownLatch(1);
		this.taskInfo = taskInfo;
		isInteractive = taskInfo.isInteractive();
		if (taskInfo != null) {
			hasSlots = taskInfo.getSlotsToNotify() != null;
		}
	}
	
	
	
	public TaskID(boolean alreadyCompleted) {
		if (alreadyCompleted) {
			globalID = nextGlobalID.incrementAndGet();
			completedLatchForRegisteringThread = new CountDownLatch(0);
			completedLatchForNonRegisteringThreads = new CountDownLatch(0);
			//hasCompleted = new AtomicBoolean(true);
			status = new AtomicInteger(COMPLETED);
		} else {
			throw new UnsupportedOperationException("Don't call this constructor if passing in 'false'!");
		}
	}
	
	protected boolean isSubTask() {
		return isSubTask;
	}

	protected void setSubTask(boolean isSubTask) {
		this.isSubTask = isSubTask;
	}
	
	/**
	 * Checks to see if this task is an interactive task.
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
	 * Returns the group that this task is part of (assuming it is a multi-task).  
	 * @return	Returns the group associated with this task. If not part of a multi-task, then
	 * returns <code>null</code>.
	 */
	public TaskIDGroup<?> getGroup() {
		return group;
	}
	
	/**
	 * Returns the task's globally-unique ID.
	 * @return	The task's unique ID.
	 * @see CurrentTask#globalID()
	 * @see CurrentTask#relativeID()
	 * @see #getRelativeID()
	 */
	public int getGlobalID() {
		return globalID;
	}
	
	/**
	 * Relative IDs are used to arrange the sub-tasks of a multi-task. This method returns the 
	 * sub-task's relative ID in the multi-task.
	 * @return The position, starting from 0, of this sub-task compared to it's sibling subtasks.
	 * @see CurrentTask#globalID()
	 * @see CurrentTask#relativeID()
	 * @see #getGlobalID()  
	 */
	public int getRelativeID() {
		return relativeID;
	}
	
	void setRelativeID(int relativeID) {
		this.relativeID = relativeID;
	}
	
	TaskInfo<T> getTaskInfo() {
		return taskInfo;
	}
	
	/**
	 * Tells if a task could start being executed. That means the task has been <code>CREATED</code>, and
	 * is not <code>CANCELLED</code>. Returns <code>true</code> if the task can start,
	 * and returns <code>false</code> otherwise.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  9/9/2014
	 * */
	boolean executeAttempt() {
		synchronized (this) {
			if (status.get() != CREATED){
				//should the status be set to CANCELLED? Don't think so,
				//the task could be started already when this method is
				//called on it.
				//status.set(CANCELLED);
				return false;		
			}		
			status.set(STARTED);
			return true;
		}		
	}
	
	
	void setEnclosingTask(TaskID<?> enclosingTask) {
		this.enclosingTask = enclosingTask;
	}
	
	
	TaskID<?> getEnclosingTask() {
		return enclosingTask;
	}
	
	
	/**
	 * Checks to see if this task has been requested to cancel.
	 * @return <code>true</code> if it has been requested to cancel, <code>false</code> otherwise.
	 * @see CurrentTask#cancelRequested()
	 * @see #cancelAttempt()
	 * @see #hasBeenCancelled()
	 */
	public boolean cancelRequested() {
		return cancelRequested.get();
	}	

	/**
	 * Checks to see if this task has successfully cancelled.
	 * @return <code>true</code> if it has cancelled successfully, <code>false</code> otherwise. 
	 */
	public boolean hasBeenCancelled() {
		return status.get() == CANCELLED;
	}
	
	/**
	 * Checks to see whether the task has completed.
	 * @return	<code>true</code> if it has completed, <code>false</code> otherwise
	 * @see #getProgress()
	 * @see CurrentTask#getProgress()
	 */
	public boolean hasCompleted() {
		return status.get() == COMPLETED;
	}
	
	
	/**
	 * Attempts to cancel the task. It first changes the state of the task to <code>CANCELLED</code>, and then
	 * checks if the previous status of the task was <code>CREATED</code> or if the task is already cancelled. 
	 * In that case the cancellation attempt will be successful, and the method will return <code>true</code>
	 * <br><br>
	 * If cancelled successfully, the task will not be enqueued. A failed cancel 
	 * will still allow the task to continue executing. To stop the task, the task should check
	 * to see if a cancel request has been made. 
	 * @return <code>true</code> if it has cancelled successfully, <code>false</code> otherwise.
	 *
	 * @author Mostafa Mehrabi
	 * @author Kingsley
	 * 
	 * @see #cancelRequested() 
	 * 
	 * @see CurrentTask#cancelRequested()
	 * @see #hasBeenCancelled()
	 * 
	 */
	public boolean cancelAttempt() {
		synchronized (this) {
			cancelRequested.set(true);
			
			if (status.get() != CREATED){
				//should the status be set to CANCELLED?
				//status.set(CANCELLED);
				return false;
			}
			
			status.set(CANCELLED);
			//cancelled = true;
			return true;
		}		
	}
	
	
	/**
	 *  
	 * This method is called when a task is complete. In order to set a task as "complete", this
	 * method goes through the list of tasks that have been waiting for this task to finish, and
	 * removes the task from their lists of dependences. Then it unblocks all the waiting threads,
	 * even the registering thread (in case there are slots to execute), and sets the flag 'hasComplete' 
	 * to <code>true</code>.
	 * If this task is registered as a group of tasks, the method will go through all tasks that have 
	 * been waiting for this group to finish, and then removes this group from their lists of dependences.
	 * If any of those waiting tasks have their list of dependences emptied by removing this group, it will
	 * be introduced to the task pool as a ready-to-execute task. 
	 * 
	 * @author Mostafa Mehrabi
	 * @since 9/9/2014
	 * */
	void setComplete() {
		
		changeStatusLock.lock();
				
		TaskID<?> waiter = null;
		while ((waiter = waitingTasks.poll())!=null){
				waiter.dependenceFinished(this);
		}
		
		completedLatchForRegisteringThread.countDown();	
		completedLatchForNonRegisteringThreads.countDown();
		status.set(COMPLETED);
		changeStatusLock.unlock();
		
		if (group != null) 
			group.oneMoreInnerTaskCompleted();			
	}

	
	
	/**
	 * A <code>waiter</code> is another task that is waiting for the instance of task to finish. Once a 
	 * request for adding a waiter for this task is received, the instance will check if it is already 
	 * completed. If that is the case, the instance will remove itself from the list of dependences of 
	 * the <code>waiter</code>, otherwise the <code>waiter</code> will be added to the list of waiters.
	 * (i.e. list of other tasks which are waiting for this instance to finish).
	 * 
	 * @author Mostafa Mehrabi
	 * @since  9/9/2014
	 * */
	void addWaiter(TaskID<?> waiter) {
		if (hasCompleted()) {
			waiter.dependenceFinished(this);
		} else {
			synchronized(this){
				if (!hasCompleted()) {
					waitingTasks.add(waiter);
				} else {
					waiter.dependenceFinished(this);
				}
			}
		}
	}
	
	/** One of the other Tasks (that this task dependsOn) has finished, and 
	 * will be removed from the list of dependences of this task. If that was 
	 * the last Task in the list of dependences, the task pool (which is in
	 * charge of scheduling) will be informed that this instance of TaskID is 
	 * ready to be executed!
	 * 
	 *  @author Mostafa Mehrabi
	 *  @since  9/9/2014
	 * */
	void dependenceFinished(TaskID<?> otherTask) {
		remainingDependences.remove(otherTask);
		if (remainingDependences.isEmpty()) {
			TaskpoolFactory.getTaskpool().nowReady(this);
		}
	}
	
	//maybe remianingDependences can be a concurrent queue instead of hashMap?
	void setRemainingDependences(List<TaskID<?>> dependences) {
		for (TaskID<?> dependency : dependences){
			if (!remainingDependences.contains(dependency))
				remainingDependences.add(dependency);
		}
	}
	
	
	/**
	 * Returns the result of the task. In order to return the final result of a task we have to
	 * wait until that task is finished. If the task has not finished yet, the current thread blocks
	 * (i.e., the thread which wants the result from this task blocks). 
	 * ParaTask worker threads will not block, instead they execute other ready tasks until
	 * this task completes.
	 * @return	The result of the task.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public T getReturnResult() throws ExecutionException, InterruptedException {
		waitTillFinished();
		if (hasBeenCancelled())
			throw new ParaTaskRuntimeException("Attempting to get the result of a cancelled Task!");
		return returnResult;
	}
	
	/**
	 * Helps the current thread with waiting for the task to finish. This method first
	 * checks if the task is already completed. If not, the method starts dealing with
	 * the current thread. Interactive threads or user threads can block and wait until
	 * the task is finished. However, if the blocking thread is a ParaTask worker thread, 
	 * it can execute some other tasks until this task completes. 
	 * <br><br>
	 * Before a worker tries to find another task and executes it, this thread should 
	 * check if there is a cancel request for the thread. If a worker thread is poisoned
	 * (i.e. requested to cancel, but not cancelled yet), even if there are some unfinished
	 * children tasks, it will not get a chance to execute them, but it will have a chance
	 * to inform the task pool and ask the task pool to remove it (i.e. the current worker 
	 * thread) from its (task pool's) list of worker threads.
	 * <br><br>
	 * If the current thread is a worker thread and is not poisoned, neither it is cancelled 
	 * already, it will be allowed to execute some other tasks (depending on the scheduling 
	 * scheme) or it can go to sleep. If the thread is logically cancelled already, it will 
	 * sleep until it is shut down by the virtual machine.
	 * <br><br>
	 * If the current thread is not a worker thread, and is the thread that has registered this
	 * task, it has to wait on its own count down latch, other wise the thread can wait on the 
	 * normal count down latch.
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * 
	 * @author Mostafa Mehrabi
	 * @since  9/9/2014
	 * @author Kingsley
	 * @since 25/05/2013
	 * */
	
	public void waitTillFinished() throws ExecutionException, InterruptedException {		
		if (!(hasCompleted() || hasBeenCancelled())) { 
			//get the thread which is trying to finish the task.
			Thread thisThread = Thread.currentThread();
			
			// Only WorkerThreads can execute a new TaskID.. all other threads belong to the user, or 
			// are InteractiveThreads (therefore it is OK for them to block) 
			if (thisThread instanceof WorkerThread) {
				WorkerThread thisWorkerThread = (WorkerThread) thisThread;
				
				while (!hasCompleted()) {
					//is worker thread poisoned? Required to be cancelled, but not cancelled yet?
					if (thisWorkerThread.isCancelRequired() && !thisWorkerThread.isCancelled()) {
						ThreadRedundancyHandler.informThreadPool();
					}
					
					if (!thisWorkerThread.isCancelled()) {
						// causes the worker to either execute a task or sleep
						thisWorkerThread.executeAnotherTaskOrSleep();
					} else {
						//if the worker thread is cancelled, then put it to sleep
						//until it is killed by JVM or Dalvik
						try {
							//200 mili-seconds of sleeping for the thread before it polls again
							Thread.sleep(ParaTask.WORKER_SLEEP_DELAY);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			} else {
				if (isCurrentThreadTheRegisteringThread()) {
					completedLatchForRegisteringThread.await();
				} else {
					completedLatchForNonRegisteringThreads.await();
				}
			}
		}
		
		//This is checked at the end because we have to give a task a chance to 
		//be executed. If the task has any errors, it will finish, and then we can check
		//if there is any errors.
		if (hasUserError.get()) {
			throw new ExecutionException(exception);
		}
	}
	
	/**
	 * This method checks if the current thread is the one that has registered the task
	 * associated to this instance of TaskID. For this purpose, it first checks if the
	 * registering thread that was initially recorded by ParaTask framework, has registered
	 * the task as well, and then checks if the current thread is the event dispatch thread
	 * (i.e., GUI) and returns <code>true</code>, even though the current event dispatch thread
	 * might be a different thread than the one registering the task. Otherwise, it simply
	 * checks if the current thread is the one that registered thread. 
	 * 
	 * @author Mostafa Mehrabi
	 * @since  18/8/2015
	 * */
	protected boolean isCurrentThreadTheRegisteringThread() {
		Thread registeredThread = taskInfo.getRegisteringThread();
		if (registeredThread == null)
			return false;
		/*This approach is still error prone, since the thread that was registered by ParaTask
		 * initially, may not be the one registering the task, and yet, that registering thread
		 * is killed and replaced by another GUI thread! Instead, it is better to use a boolean
		 * flag for each task to indicate if it has been registered by GUI thread.*/
		if (taskInfo.hasBeenRegisteredByGuiThread() && GuiThread.currentThreadIsEventDispatchThread())
			return true;
		else 
			return registeredThread == Thread.currentThread();
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
	 * Checks to see whether the task had any errors. 
	 * @return	<code>true</code> if there was an error, <code>false</code> otherwise
	 */
	public boolean hasUserError() {
		return hasUserError.get();
	}
	
	
	
	/**
	 * In order to make sure a task is completed, the possible handlers (slots) that are queued by the task need 
	 * to be invoked. This method invokes slots and the exception handlers to be executed by the registered thread. 
	 * <br><br>
	 * Note that this TaskID is NOT considered completed, until all these slots are finished (even though the 
	 * actual task logic has been executed, we need to wait for the slots before handling dependences, etc).
	 * Therefore, the registering thread will later set the status of this task as complete. 
	 */
	void enqueueSlots(boolean onlyEnqueueFinishedSlot) {
		
		if (isMultiTask()) {
			//Part of a multi-task, will only enqueue the slots of the group when the last TaskID in the group completes
			group.oneMoreInnerTaskCompleted();
			// This was never here before -- was deadlocking without it... 20/3/2010 
			//This needs further investigation, whether it should be actually here? 
			setComplete();	
			
		} else {
			//Even if this TaskID is within a group, it is a separate entity since not a multi-task
			
			//If it has been cancelled, then probably don't want to execute the handlers... (except the setCompleteMethod())
			if (hasUserError() || hasSlots) {
				
				completedLatchForRegisteringThread.countDown(); //-- so that registering thread will not block in slots 
				completedLatchForNonRegisteringThreads.countDown();  // ADDED THIS?? -- I think this should be added after the slots and handlers are executed.
				
				if (hasUserError.get())
					executeExceptionHandler();
				if (hasSlots){
					executeAllTaskSlots();
				}
				//Since slots are executed in the order they are enqueued, then this will be the last slot!
				//We want to ensure that 'setComplete' is called after all slots are executed, so we enqueue
				//the method as another slot at the end.
				Slot<Void> slot = new Slot<>(this::setComplete);
				slot.setIsSetCompleteSlot(true);
				executeOneTaskSlot(slot);
				
			} else {
				setComplete();
			}
			
		}
	}
	
	/**
	 * Returns the appropriate exception hanlder for a specific class of exception,
	 * by receiving that exception class as argument.
	 * 
	 * @author Mostafa Mehrabi
	 * @since 9/9/2014
	 * */
	protected Slot<?> getExceptionHandler(Class<?> occurredException) {
		
		//-- first, try to get handler defined immediately for this task
		Slot<?> handler = taskInfo.getExceptionHandler(occurredException);
		
		TaskID<?> currentTask = this;
		//-- while we have not found a handler, and while there are other enclosing tasks
		while (handler == null && currentTask.getEnclosingTask() != null) {
			currentTask = currentTask.getEnclosingTask();
			handler = currentTask.getTaskInfo().getExceptionHandler(occurredException);
		}
		
		return handler;
	}
	
	//-- returns the number of handlers that it will execute for this TaskID
	private boolean executeExceptionHandler() {
		Slot<?> handler = getExceptionHandler(exception.getClass());
		
		if (handler != null) {
			executeOneTaskSlot(handler);
			return true;
		} else {
			System.err.println("No asynchronous exception handler (i.e. asyncCatch clause) was specified when " +
					"invoking:\n" +
							"\n\tThe globalID of this task is "+ getGlobalID() + ", and the encountered exception: ");
			exception.printStackTrace();
			return false;
		}
	}
	
	//This doesn't enqueue interim slots, whenever interim handlers are called from
	//within a TaskInfo, this method is called on the corresponding TaskID.
	void executeInterimSlot(Slot<?> slot){
		executeOneTaskSlot(slot);
	}
	
	void executeOneTaskSlot(Slot<?> slot) {
		ParaTask.getEDTTaskListener().executeSlot(slot);
	}
	
	protected int executeIntermediateTaskSlots() {
		return 0;
	}
	
	protected int executeAllTaskSlots() {
		List<Slot<T>> slotsToNotify = taskInfo.getSlotsToNotify();
		for (Slot<T> slotToNotify : slotsToNotify)
			executeOneTaskSlot(slotToNotify);
		return taskInfo.getSlotsToNotify().size();
	}
	
	void setPartOfGroup(TaskIDGroup<?> group) {
		this.group = group;
	}
	
	/**
	 * Checks to see if this task is part of a multi-task group.
	 * @return <code>true</code> if this task is part of a multi-task, <code>false</code> otherwise 
	 */
    //	shouldn't this be changed to isSubtask?
	public boolean isMultiTask() {
		return (group != null && group.isMultiTask());
	}
	
	/**
	 * Returns the size of the multi-task this task is part of. I think this should be moved to 
	 * TaskIDGroup, instead of TaskID.
	 * @return	The multi-task size, otherwise returns 1 if this task is not part of a multi-task.
	 */
	int multiTaskSize() {
		if (!isMultiTask())
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
	 * @author : Kingsley
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
