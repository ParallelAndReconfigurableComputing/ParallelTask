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

import pu.RedLib.Reduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An extension of <code>TaskID</code> to contain multiple tasks. In particular, a <code>TaskIDGroup</code> is returned for 
 * all multi-task invocations. Users may also instantiate a <code>TaskIDGroup</code> and populate it with multiple 
 * <code>TaskID</code>s (and <code>TaskIDGroup</code>s). This would be useful, for example, when invoking many different 
 * tasks, allowing a collective approach to synchronise on those tasks (i.e. wait on the <code>TaskIDGroup</code> rather
 * than the on each individual <code>TaskID</code>. 
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 *
 * @param <T>
 */
public class TaskIDGroup<T> extends TaskID<T> {
	
	private ArrayList<TaskID<T>> innerTasks = new ArrayList<>();
	
	private AtomicInteger numTaskCompleted = new AtomicInteger(0);
	
	private AtomicInteger barrier = new AtomicInteger(0);
	
	//-- Reductions are only performed once at most, by a single thread
	private boolean performedReduction = false;
	private ReentrantLock reductionLock = new ReentrantLock();
	private T reducedResult;
	
	/*Indicates how many sub tasks should be expanded, and its value can
	only be set from {@link AbstractTaskPool#enqueueMulti()}*/
	private int groupSize = 0;
	private boolean futureGroup = false;
	private boolean isElastic = false;
	private Reduction<T> reductionOperation = null; 
	
	//Only a multi-task if the group was created by ParaTask, this attribute 
	//distinguishes it from user-created groups
	private boolean isMultiTask = false;   
	
	private ParaTaskExceptionGroup exceptionGroup = null;
	private CopyOnWriteArrayList<Throwable> exceptionList = new CopyOnWriteArrayList<Throwable>();

	/**
	 * 
	 * @author Kingsley
	 * @since 08/05/2013
	 * 
	 * This is used to indicate if the multi task has been expanded or not
	 */
	private boolean isExpanded = false;
	
	public TaskIDGroup(){
		futureGroup = true;
		isElastic = true;
	}
	
	/**
	 * This public constructor is actually used to group a bunch of tasks, which
	 * may include one-off task or multi task, should not give any id to this 
	 * group.
	 * */
	public TaskIDGroup(int groupSize) {
		isElastic = false;
		futureGroup = true;
		this.setGroupSize(groupSize);
	}
	
	/**this is only used to create a multi-task (the size is known before adding the inner tasks)*/
	TaskIDGroup(int groupSize, TaskInfo<T> taskInfo) {
		super(taskInfo);
		isElastic = false;
		this.isMultiTask = taskInfo.isMultiTask();
		this.setGroupSize(groupSize);
		this.reductionOperation = null;
	}
	
	TaskIDGroup(int groupSize, TaskInfo<T> taskInfo, boolean isMultiTask){
		super(taskInfo);
		isElastic = false;
		this.isMultiTask = isMultiTask;
		this.setGroupSize(groupSize);
		this.reductionOperation = null;
	}
	
	TaskIDGroup(int groupSize, TaskInfo<T> taskInfo, Reduction<T> reduction) {
		super(taskInfo);
		isElastic = false;
		this.isMultiTask = taskInfo.isMultiTask();
		this.setGroupSize(groupSize);
		this.reductionOperation = reduction;
	}
	
	TaskIDGroup(int groupSize, TaskInfo<T> taskInfo, boolean isMultiTask, Reduction<T> reduction) {
		super(taskInfo);
		isElastic = false;
		this.isMultiTask = isMultiTask;
		this.setGroupSize(groupSize);
		this.reductionOperation = reduction;
	}
	
	/**
	 * Checks whether this TaskIDGroup represents a multi-task. This is because users may
	 * use <code>TaskIDGroup</code> to group a set of <code>TaskID</code>s, but those <code>TaskID</code>s might not necessarily
	 * be part of a multi-task.
	 * @return	<code>true</code> if this <code>TaskIDGroup</code> represents an actual multi-task, <code>false</code> otherwise
	 */
	boolean isMultiTask() {
		return isMultiTask;
	}
	
	int getGroupSize() {
		return groupSize;
	}

	void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
	
	/**
	 * @author Kingsley
	 * @since 10/05/2013
	 * 
	 * Do not set relative id here. Instead, before a TaskID is added into a group, a relative id
	 * should be given first.
	 * 
	 * However if do not set relative id here, when trying to add a TaskID into a user defined
	 * group, this TaskID become untraceable through relative id. It still can be traced through
	 * its global id. 
	 * 
	 * Because of the same reason, do not set part of group here. Instead, set part of group when
	 * a multi task is expanded just like set relative id there.
	 * 
	 * Maybe a good idea that only allow to call this method paratask runtime internally, which means
	 * it is used for multi task group only but not user defined group.
	 * */
	public void addInnerTask(TaskID<T> taskID) {
		if(!isElastic && innerTasks.size() == groupSize)
			throw new RuntimeException("\nTHE NUMBER OF INNER TASKS IS NOW THE SAME AS THE SIZE THAT WAS SET FOR THIS GROUP! NO MORE TASKS CAN BE ADDED\n"
					+ "FOR DYNAMIC GROUP SIZE USE DEAFULT CONSTRUCTOR OF TaskIDGroup!\n");
		
		taskID.setSubTask(true);
		taskID.setPartOfGroup(this);
		innerTasks.add(taskID);
		
		if(isElastic)
			groupSize = innerTasks.size();		
	}
	
	public void setInnerTask(int relativeID, TaskID<T> taskID){
		if(relativeID < innerTasks.size()){
			taskID.setSubTask(true);
			taskID.setPartOfGroup(this);
			innerTasks.set(relativeID, taskID);
		}
		else
			addInnerTask(taskID);
	}
	
		
	void setReduction(Reduction<T> reduction){
		reductionLock.lock();
		this.reductionOperation = reduction;
		reductionLock.unlock();
	}
	
	/*
	 * Perform a reduction on the set of results. A reduction is only to be performed once. 
	 * If this is called a second time then the pre-calculated answer is returned.
	 * @param red	The reduction to perform
	 * @return The result of performing the reduction on the set of <code>TaskID</code>s contained in this group.
	 */
	private void reduceResults() {
		if (groupSize == 0)			
			return;
		reductionLock.lock();
		if (performedReduction) {
			reductionLock.unlock();
			return;
		}
		reducedResult = getInnerTaskResult(0);
		for (int i = 1; i < groupSize; i++) {
			reducedResult = reductionOperation.reduce(reducedResult, getInnerTaskResult(i));
		}
		performedReduction = true;
		reductionLock.unlock();
	}
	
	private T reduceResults(Reduction<T> reductionOperation){
		if (groupSize == 0)			
			return null;
		reductionLock.lock();
		T result = getInnerTaskResult(0);
		for (int i = 1; i < groupSize; i++) {
			result = reductionOperation.reduce(result, getInnerTaskResult(i));
		}
		reductionLock.unlock();
		return result;
	}
	
	/**
	 * Returns the result of a particular task.
	 * @param relativeID The relative ID of the task whose result is wanted.
	 * @see CurrentTask#relativeID()
	 * @see TaskID#getRelativeID()
	 * @return The result for that task.
	 */
	public T getInnerTaskResult(int relativeID) {
		if(relativeID < 0 || relativeID >= innerTasks.size())
			throw new IndexOutOfBoundsException("INVALID INDEX REQUEST: " + relativeID + "! VALID RANGE: [" + 0 + "," + innerTasks.size() + ")");
		return innerTasks.get(relativeID).getReturnResult();
	}
	
	/**
	 * Return an iterator for the set of <code>TaskID</code>s contained in this group.
	 * @return	An iterator for this group of TaskIDs.
	 */
	Iterator<TaskID<T>> getGroupIterator() {
		return innerTasks.iterator();
	}
	
	/**
	 * Increments the number of inner tasks that have finished executing. Then checks if all inner-tasks
	 * are completed. If that is the case, then checks if there are any exceptions asynchronously recorded
	 * for any of the inner-tasks, and calls their handlers. Moreover, it checks for slots to notify   
	 * and executes them. Then it sets the task as "complete".
	 * 
	 * @author Mostafa Mehrabi
	 * @since  9/9/2014
	 */
	void oneMoreInnerTaskCompleted() { 
		int numCompleted = numTaskCompleted.incrementAndGet();
		
		if (numCompleted == groupSize) {
			
			//-- this is the last task in the multi-task group, therefore need to invoke slots/handlers
			boolean nothingToQueue = true;
						
			if (hasUserError()) {
				for (TaskID<?> taskID : innerTasks ) {
					if(taskID instanceof TaskIDGroup<?>){
						TaskIDGroup<?> taskIDGroup = (TaskIDGroup<?>) taskID;
						ParaTaskExceptionGroup groupException = (ParaTaskExceptionGroup) taskIDGroup.getException();
						if(groupException != null){
							nothingToQueue = false;
							try {
								taskIDGroup.handleGroupExceptions();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
					else{
						Throwable exception = taskID.getException(); 
						if (exception != null) {
							Slot<? extends Throwable> handler = taskID.getExceptionHandler(exception);
							
							if (handler != null) {
								executeOneTaskSlot(handler);
								nothingToQueue = false;
							} else {
								System.err.println("No asynchronous exception handler found in Task " 
																+ taskID.getGlobalID() + " for the following exception: ");
								exception.printStackTrace();
							}
						}
					}
				}
			}			
			
			//-- executeSlots
			if (hasSlots()) {				
				executeAllTaskSlots();
				nothingToQueue = false;
			} 

			if (nothingToQueue) {
				setComplete();
			} else {
				Slot<Void> slot = new Slot<Void>(this::setComplete);
				slot.setIsSetCompleteSlot(true);
				executeOneTaskSlot(slot);
			}
		}
	}

	@Override
	protected <E extends Throwable> Slot<E> getExceptionHandler(E occurredException){
		if(isMultiTask()){
			//multi-tasks have the same taskInfo with different data, so if there is a
			//handler it should be found in the first taskInfo!
			TaskID<?> taskID = innerTasks.get(0);
			Slot<E> handler = taskID.getExceptionHandler(occurredException);
			if (handler != null)
				return handler;
		}
		else{
			for(TaskID<?> taskID : innerTasks){
				Slot<E> handler = taskID.getExceptionHandler(occurredException);
				if(handler != null)
					return handler;
			}
		}
		return null;
	}
	
	@Override
	Throwable getException() {
		String reason = "Exception(s) occured inside multi-task execution (GlobalID of "+globalID+"). Individual exceptions are accessed via getExceptionSet()";
		exceptionGroup = new ParaTaskExceptionGroup(reason, exceptionList.toArray(new Throwable[0]));
		return exceptionGroup;
	}

	/**
	 * Get the result of a group of tasks. To use this method, an instance of a reduction operation
	 * must be passed to the TaskID constructor, otherwise use {@link #getReturnResult(Reduction)}. 
	 * 
	 * @see #getReturnResult(Reduction)
	 * @see #getInnerTaskResult(int)
	 * @throws UnsupportedOperationException
	 */
	@Override
	public T getReturnResult() {
		if(performedReduction)
			return reducedResult;
		if(!noReturn){
			if (this.reductionOperation == null)
				throw new UnsupportedOperationException("NO REDUCTION OBJECT HAS BEEN SPECIFIED FOR THE GROUP. EITHER A REDUCTION OBJECT MUST BE SPECIFIED,\n"
						+ "OR THE SUB-RESULTS MUST BE RETRIEVED INDIVIDUALLY!");
		}
		AtomicBoolean complain = new AtomicBoolean(false);
		try {
			waitTillFinished();
			if(hasUserError()){
				complain.set(true);
				throw new RuntimeException("THE TASK REACHED FOR ITS RETURN RESULT HAS ENCOUNTERED EXCEPTIONAL ERROR, \n"
						+ "THEREFORE, THE RESULT CANNOT BE VALID! FOR SUPPRESSING THIS ERROR, HANDLE EXCEPTIONS INTERNALLY IN THE TASKS!");
			}
			if(hasBeenCancelled() || noReturn)
				return null;
		} catch (Exception e) {
			if(e instanceof RuntimeException && complain.get())
				throw (RuntimeException)e;
			e.printStackTrace();
			return null;
		}
		if(noReturn)
			return null;
		//Another thread might/process might have performed reduction while this one was waiting.
		if(!performedReduction)
			reduceResults();
		return reducedResult;
	}
	
	/**
	 * This method is specifically useful for retrieving the results of the sub-tasks of a future
	 * group. Individual tasks may depend on a future group, and require the entire array of
	 * that future group, in order to perform their operation. For example, an asynchronous
	 * task that reduces the result of a future group when the future group is finished. 
	 * 
	 * @return T[] an array that represents the results of the sub-tasks of this future group.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2017
	 */
	public T[] getResultsAsArray(T[] array){
		if(array.length != innerTasks.size()){
			array = Arrays.copyOf(array, innerTasks.size());
		}
		
		AtomicBoolean complain = new AtomicBoolean(false);
		try {
			waitTillFinished();
			if(hasUserError()){
				complain.set(true);
				throw new RuntimeException("THE TASK-GROUP REACHED FOR ITS ARRAY OF RESULTS HAS ENCOUNTERED EXCEPTIONAL ERROR, \n"
						+ "THEREFORE, THE ARRAY CANNOT BE VALID! FOR SUPPRESSING THIS ERROR, HANDLE EXCEPTIONS INTERNALLY IN THE TASKS!");
			}
		} catch (Exception e) {
			if(e instanceof RuntimeException && complain.get())
				throw (RuntimeException)e;
			e.printStackTrace();
			return null;
		} 
		
		for(int index = 0; index < innerTasks.size(); index++){
			array[index] = innerTasks.get(index).getReturnResult();
		}
		return array;
	}

	/**
	 * Performs customized reductions on the results contained in the group.
	 * This is a one-off operation, and the specified reduction is not memorized as the group's 
	 * reduction operation.  
	 * @param reductionOperation	The reduction to perform on the results of the contained <code>TaskIDs</code>
	 * @return		The result after performing the specified reduction. 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public T getReturnResult(Reduction<T> reductionOperation){
		if (reductionOperation == null)
			throw new NullPointerException("THE REDUCTION OBJECT SPECIFIED IS NOT VALID!");
		
		AtomicBoolean complain = new AtomicBoolean(false);
		try {
			waitTillFinished();
			if(hasUserError()){
				complain.set(true);
				throw new RuntimeException("THE TASK REACHED FOR ITS RETURN RESULT HAS ENCOUNTERED EXCEPTIONAL ERROR, \n"
						+ "THEREFORE, THE RESULT CANNOT BE VALID! FOR SUPPRESSING THIS ERROR, HANDLE EXCEPTIONS INTERNALLY IN THE TASKS!");
			}
			if(hasBeenCancelled() || noReturn)
				return null;
		} catch (Exception e) {
			if(e instanceof RuntimeException && complain.get())
				throw (RuntimeException)e;
			e.printStackTrace();
			return null;
		}
		return reduceResults(reductionOperation);
	}

	@Override
	public TaskInfo<T> getTaskInfo() {
		return taskInfo;
	}


	/**
	 * This method is called by one of the inner tasks when it incurs an
	 * exception. Therefore, the group knows that there is an exception 
	 * within the group that needs handling. 
	 * 
	 * @param exception The exception instance that has been encountered by
	 * an inner task.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  19/8/2015
	 * */
	@Override
	public void setException(Throwable exception) {
		exceptionList.add(exception);
		hasUserError.set(true);
	}

	
	@Override
	public void setReturnResult(Object returnResult) {
		throw new ParaTaskRuntimeException("Cannot set the return result for a TaskIDGroup");
	}
	
	boolean isFutureGroup(){
		return futureGroup;
	}

	/**
	 * Canceling a group is currently not supported. Users must cancel each inner task individually.
	 * @throws UnsupportedOperationException	
	 */
	@Override
	public boolean cancelAttempt() {
		throw new UnsupportedOperationException("Cancelling a group is currently not supported, must cancel inner tasks individually.");
	}
	
	/**
	 * Waits for all the contained inner tasks to complete. It goes through all inner-tasks and
	 * if an inner-task is a TaskIDGroup, which means there is another multi-task inside the group
	 * waits until that inner multi-task is expanded and all its task are finished. Then, it proceeds 
	 * to finishe other inner-tasks.
	 * If an inner-task is a TaskID, which means it is a normal task, wait until that task is completed.
	 *
	 * @author Kingsley
	 * @author Mostafa Mehrabi
     * @since 08/05/2013
     * @since 9/9/2014
	 * */
	@Override
	public void waitTillFinished() throws ExecutionException, InterruptedException {
		if(hasCompleted())
			return;
		int size = 0;
		if(isMultiTask()){//because it may be a task group, which is not a multi-task, and is already expanded. 
		                  //without this condition, a task-group will wait indefinitely. 
			while(!this.isExpanded()){
				Thread.sleep(ParaTask.WORKER_SLEEP_DELAY);
			}
		}
		size = getGroupSize();
		for (int i = size-1; i >= 0; i--) {// wait for them in reverse order (LIFO)
			try {
				TaskID<?> taskID = innerTasks.get(i);
				if (taskID instanceof TaskIDGroup<?>) {
					TaskIDGroup<?> taskIDGroup = (TaskIDGroup<?>) taskID;
					//if a taskIDGroup is not a multi-task it will never be marked as "expanded"
					if(taskIDGroup.isMultiTask()){
						while (!taskIDGroup.isExpanded()) {
							//shall the thread try picking another task? if it is a worker thread!
							Thread.sleep(ParaTask.WORKER_SLEEP_DELAY);
						}
					}
					taskIDGroup.waitTillFinished();
				} else {
					taskID.waitTillFinished();					
				}
			} catch (ExecutionException e) {
				/* ignore the exception, all inner exceptions will be thrown below */
			}			
		}
		this.status.set(COMPLETED);
	}
	
	
	
	/* This method sets a checkpoint at which threads that arrive earlier wait until all threads arrive. 
	 * This is mostly done in situations where we want to make sure that at a specific stage
	 * all threads have reached a specific point in the program. 
	 * <br>
	 * Therefore, while none of the threads have called <code>barrier()</code>, all threads carry on doing their
	 * ordinary tasks. Once <code>barrier()</code> is called by a thread, that thread has to wait (either doing 
	 * some other tasks, or going to sleep) until all other threads arrive to that check point (i.e. call 
	 * <code>barrier()</code>). When all threads have called <code>barrier()</code>, barrier's counter will
	 * be set back to <b>zero</b>, and threads can carry on doing their tasks.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  9/9/2014
	 * */
	void barrier() throws InterruptedException, BrokenBarrierException {
		int pos = barrier.incrementAndGet();
		WorkerThread currentWorker = (WorkerThread) Thread.currentThread();
		
		if (pos != groupSize) {
			while (barrier.get() != groupSize && barrier.get() != 0) {
				//keep executing other tasks until all the threads have reached the barrier
				currentWorker.executeAnotherTaskOrSleep();
			}
		} else {
			//this is the last thread to arrive.. reset the barrier
			barrier.set(0);
		}
	}

	/*
	 * A recursive convenience function that digs into the TaskIDGroup and returns all the individual TaskIDs.
	 *
	 * @return List<TaskID<?>> the TaskIDs inside <code>group</code> placed inside a new ArrayList
	 * */
	List<TaskID<?>> allTasksInGroup() {
		ArrayList<TaskID<?>> listOfInnerTasks = new ArrayList<>();
		
		for (TaskID<?> innerTask : innerTasks){
			if (innerTask instanceof TaskIDGroup<?>)
				listOfInnerTasks.addAll(((TaskIDGroup<?>) innerTask).allTasksInGroup());
			else
				listOfInnerTasks.add(innerTask);
		}
		
		return listOfInnerTasks;
	}

	/**
	 * 
	 * @author Kingsley
	 * @since 08/05/2013
	 * 
	 * After a multi task worker thread expand a multi-task, call this method to set a "true" value.
	 */
	protected void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	
	protected boolean isExpanded(){
		return isExpanded;
	}
	
	void addHandlerSlot(Slot<T> slot){
		if(!futureGroup)
			throw new IllegalArgumentException("HANDLER SLOTS CAN BE ADDED TO TASKIDGROUPS ONLY IF IT IS A FUTURE GROUPS");
		if(hasCompleted())
			throw new IllegalAccessError("THE FUTURE GROUP HAS BEEN SYNCHRONIZED! NO MORE HANDLER METHODS CAN BE ADDED TO THE GROUP!");
		slot.setTaskID(this);
		slotsToNotify.add(slot);
	}	
	
	void handleGroupExceptions() throws Throwable{
		if(exceptionAlreadyHandled)
			return;
		ParaTaskExceptionGroup group = (ParaTaskExceptionGroup) getException();
		exceptionAlreadyHandled = true;
		if(group != null){
			Throwable[] throwables = group.getExceptionSet();
			for(Throwable throwable : throwables){
				Slot<? extends Throwable> handler = getExceptionHandler(throwable);
				if(handler != null){
					executeOneTaskSlot(handler);
				} else {
					System.err.println("No asynchronous exception handler found in Task " 
												+ getGlobalID() + " for throwable: " + throwable.toString());
					throw throwable;
				}
			}
		}
	}	
}
