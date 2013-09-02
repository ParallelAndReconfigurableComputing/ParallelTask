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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
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
 * @param <E>
 */
public class TaskIDGroup<E> extends TaskID<E> {
	
	private ArrayList<TaskID<E>> innerTasks = new ArrayList<TaskID<E>>();
	
	private AtomicInteger numTaskCompleted = new AtomicInteger(0);
	
	private AtomicInteger barrier = new AtomicInteger(0);
	
	//-- Reductions are only performed once at most, by a single thread
	private boolean performedReduction = false;
	private ReentrantLock reductionLock = new ReentrantLock();
	private E reductionAnswer;
	
	private int nextRelativeID = 0;
	
	private int groupSize = 0;
	
	private boolean isMultiTask = false;   //-- only a multi-task if the group was created by ParaTask in a TASK(*) declaration
											//-- this distinguishes it from user-created groups
	
	private ParaTaskExceptionGroup exceptionGroup = null;
	private CopyOnWriteArrayList<Throwable> exceptionList = new CopyOnWriteArrayList<Throwable>();
	
	/**
	 * Construct a new group that will contain <code>groupSize</code> tasks.
	 * @param groupSize		The number of tasks that will be added to the group
	 */
	public TaskIDGroup(int groupSize) {
		this.groupSize = groupSize;
	}
	
	//-- this is only used to create a multi-task (the size is known before adding the inner tasks)
	TaskIDGroup(int groupSize, TaskInfo taskInfo) {
		super(taskInfo);
		this.isMultiTask = true;
		this.groupSize = groupSize;
		this.taskInfo = taskInfo;
	}
	
	/**
	 * Checks whether this TaskIDGroup represents a multi-task. This is because users may
	 * use <code>TaskIDGroup</code> to group a set of <code>TaskID</code>s, but those <code>TaskID</code>s might not necessarily
	 * be part of a multi-task.
	 * @return	<code>true</code> if this <code>TaskIDGroup</code> represents an actual multi-task, <code>false</code> otherwise
	 */
	public boolean isMultiTask() {
		return isMultiTask;
	}
	
	/**
	 * Add a task to this group. 
	 * @param id	The <code>TaskID</code> to add
	 */
	public void add(TaskID<E> id) {
		innerTasks.add(id);
		id.setPartOfGroup(this);
		id.setRelativeID(nextRelativeID++);
	}
	
	/**
	 * Returns the group size.
	 * @return	The group size.
	 */
	public int groupSize() {
		return groupSize;
	}
	
	/**
	 * Perform a reduction on the set of results. A reduction is only to be performed once. 
	 * If this is called a second time then the pre-calculated answer is returned.
	 * @param red	The reduction to perform
	 * @return The result of performing the reduction on the set of <code>TaskID</code>s contained in this group.
	 */
	public E reduce(Reduction<E> red) throws ExecutionException, InterruptedException {
		waitTillFinished();
		
		// TODO want to make this like the Parallel Iterator's reduction.. i.e. checks initial value, etc.. 
		
		if (groupSize == 0)
			return null;
		
		reductionLock.lock();
		if (performedReduction) {
			reductionLock.unlock();
			return reductionAnswer;
		}
		reductionAnswer = getInnerTaskResult(0);
		for (int i = 1; i < groupSize; i++) {
			reductionAnswer = red.combine(reductionAnswer, getInnerTaskResult(i));
		}
		performedReduction = true;
		reductionLock.unlock();
		return reductionAnswer;
	}
	
	/**
	 * Returns the result of a particular task.
	 * @param relativeID The relative ID of the task whose result is wanted.
	 * @see CurrentTask#relativeID()
	 * @see TaskID#relativeID()
	 * @return The result for that task.
	 */
	public E getInnerTaskResult(int relativeID) throws ExecutionException, InterruptedException {
		return (E) innerTasks.get(relativeID).getReturnResult();
	}
	
	/**
	 * Return an iterator for the set of <code>TaskID</code>s contained in this group.
	 * @return	An iterator for this group of TaskIDs.
	 */
	public Iterator<TaskID<E>> groupMembers() {
		return innerTasks.iterator();
	}
	
	/*
	 * increments the number of inner tasks that have finished executing.
	 * 
	 * used only for multi-tasks?
	 */
	void oneMoreInnerTaskCompleted() { 
		int numCompleted = numTaskCompleted.incrementAndGet();
		
		if (groupSize == numCompleted) {
			//-- this is the last task in the multi-task group, therefore need to invoke slots/handlers
			boolean nothingToQueue = true;
			
			if (hasUserError()) {
				
				//-- loop through all the TaskIDs and if it had an exception, except the handler..
				
				// TODO at the moment, the handler uses the group's TaskID rather than the one for the specific task.. needs to be fixed!!

				for (Iterator<TaskID<E>> it = groupMembers(); it.hasNext(); ) {
					TaskID<E> task = it.next();
					Throwable ex = task.getException();
					if (ex != null) {
						Slot handler = getExceptionHandler(ex.getClass());
						
						if (handler != null) {
							callTaskListener(handler);
							nothingToQueue = false;
						} else {
							System.err.println("No asynchronous exception handler found in Task " + task.globalID() + " for the following exception: ");
							ex.printStackTrace();
						}
					}
				}
			}
			
			//-- executeSlots
			if (hasSlots) {
				executeSlots();
				nothingToQueue = false;
			} else {
			}

			if (nothingToQueue) {
				setComplete();
			} else {
				callTaskListener(new Slot(ParaTaskHelper.setCompleteSlot, this, false, false, true));
			}
		} else {
//			System.out.println("Group size of "+ groupSize+ " not finished. Number of tasks completed so far: "+numCompleted);
		}
	}

	/*
	 *  TaskIDGroup does not need to have access to the fields of TaskID. It just stores all the TaskID objects, and makes
	 *  invocation on the inner TaskIDs in the group (to simplify implementation). 
	 */
	@Override
	void dependenceFinished(TaskID otherTask) {
		throw new UnsupportedOperationException("TODO: Not implemented!");
	}

	@Override
	public Throwable getException() {
		return exceptionGroup;
	}

	/**
	 * Get the result of a group of tasks. This is not a supported method for <code>TaskIDGroup</code>. To get the results,
	 * must either perform a reduction or get results of the individual tasks. 
	 * 
	 * @see #getReturnResult(Reduction)
	 * @see #getInnerTaskResult(int)
	 * @throws UnsupportedOperationException
	 */
	@Override
	public E getReturnResult() throws ExecutionException, InterruptedException {
		throw new UnsupportedOperationException("This is a TaskIDGroup, you must either specify a Reduction or get individual results from the inner TaskID members.");
	}

	/**
	 * Perform a reduction on the results contained in the group.  
	 * @param red	The reduction to perform on the results of the contained <code>TaskIDs</code>
	 * @return		The result after performing the specified reduction. 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public E getReturnResult(Reduction<E> red) throws ExecutionException, InterruptedException {
		return reduce(red);
	}

	@Override
	TaskInfo getTaskInfo() {
		throw new UnsupportedOperationException("TODO: Not implemented! Does a TaskIDGroup need to be able to use this method?");
	}

	/**
	 * Checks to see whether all the inner tasks have completed.
	 */
	@Override
	public boolean hasCompleted() {
		return super.hasCompleted();
	}
	
	/**
	 * Checks to see whether any of the inner tasks contained an error. 
	 */
	@Override
	public boolean hasUserError() {
		return super.hasUserError();
	}

	@Override
	void setComplete() {
		hasCompleted.set(true);
	}

	@Override
	void enqueueSlots(boolean onlyEnqueueFinishedSlot) {
	}

	@Override
	void setException(Throwable exception) {
		exceptionList.add(exception);
		hasUserError.set(true);
	}

	@Override
	void setRemainingDependences(ArrayList<TaskID> deps) {
		throw new UnsupportedOperationException("TODO: Not yet implemented!");
	}

	@Override
	void setReturnResult(Object returnResult) {
		throw new ParaTaskRuntimeException("Cannot set the return result for a TaskIDGroup");
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
	 * Waits for all the contained inner tasks to complete.
	 */
	@Override
	public void waitTillFinished() throws ExecutionException, InterruptedException {
		int size = innerTasks.size();
		
		for (int i = size-1; i >= 0; i--) {		// wait for them in reverse order (LIFO)
			try {
				innerTasks.get(i).waitTillFinished();
			} catch (ExecutionException e) {
				/* ignore the exception, all inner exceptions will be thrown below */
			}
		}
		if (hasUserError.get()) {
			String reason = "Exception(s) occured inside multi-task execution (GlobalID of "+globalID+"). Individual exceptions are accessed via getExceptionSet()";
			exceptionGroup = new ParaTaskExceptionGroup(reason, exceptionList.toArray(new Throwable[0]));
			throw exceptionGroup;
		}
	}
	
	void barrier() throws InterruptedException, BrokenBarrierException {
		int pos = barrier.incrementAndGet();
		WorkerThread currentWorker = (WorkerThread) Thread.currentThread();
		
		if (pos != groupSize) {
			while (barrier.get() != groupSize && barrier.get() != 0) {
				//-- keep executing other tasks until all the threads have reached the barrier
				currentWorker.executeAnotherTaskOrSleep();
			}
		} else {
			//-- this is the last thread to arrive.. reset the barrier
			barrier.set(0);
		}
	}
}
