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

import java.lang.ref.WeakReference;
import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import pt.queues.FifoLifoQueue;

public abstract class AbstractTaskPool implements Taskpool {
	
		
	protected final static int INITIAL_QUEUE_CAPACITY = 11;  
	
	protected final static Comparator<TaskID<?>> FIFO_TaskID_Comparator = new Comparator<TaskID<?>>() {
		@Override
		public int compare(TaskID<?> o1, TaskID<?> o2) {
			return o1.globalID - o2.globalID;
		}
	};
	
	protected final static Comparator<TaskID<?>> LIFO_TaskID_Comparator = new Comparator<TaskID<?>>() {
		@Override
		public int compare(TaskID<?> o1, TaskID<?> o2) {
			return o2.globalID - o1.globalID;
		}
	};
	
	/**
	 * 
	 */
	protected ConcurrentLinkedQueue<WeakReference<InteractiveThread>> cachedInteractiveThreadPool = new ConcurrentLinkedQueue<>();
	
	/**
	 * This queue holds the cloud tasks, from which the CloudTaskThread picks its tasks.
	 */
	protected ConcurrentLinkedQueue<TaskID<?>> cloudTaskQueue = new ConcurrentLinkedQueue<>();
	/** 
	 * Tasks that are not ready to be executed yet, still waiting for dependences to be done! 
	 * */
	protected ConcurrentHashMap<TaskID<?>, Object> waitingTasks = new ConcurrentHashMap<TaskID<?>, Object>();
	
	/**
	 * A thread-safe queue for global multi-tasks that are ready to be executed.
	 * */
	protected PriorityBlockingQueue<TaskID<?>> globalMultiTaskQueue = null;
	
	/**
	 * A thread-safe queue for global one-off tasks that are ready to be executed.
	 * */
	protected PriorityBlockingQueue<TaskID<?>> globalOneOffTaskQueue = null;
	
	/**
	 * A queue for ready-to-execute multi-tasks in a mixed-scheduling implementation.
	 */
	protected FifoLifoQueue<TaskID<?>> mixedMultiTaskQueue = null;
	
	/**
	 * A queue for ready-to-execute one-off tasks in a mixed-scheduling implementation.  
	 */
	protected FifoLifoQueue<TaskID<?>> mixedOneOffTaskQueue = null;
	
	/**
	 * A list of Abstract Queues that hold the private ready-to-execute tasks of worker threads. That is, for each worker
	 * thread, an Abstract Queue is added to the list. No stealing occurs within these queues. 
	 * @see  AbstractQueue
	 */
	protected List<AbstractQueue<TaskID<?>>> privateTaskQueues;
	
	/**
	 * A Map that associates the ID of a worker thread to a thread-safe queue that holds its 
	 * local ready-to-execute one-off tasks. Once the local one-off task queue of a worker 
	 * thread is empty, that worker thread can steal tasks from other thread's local one-off
	 * task queues. 
	 */
	protected Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneOffTaskQueues = null;
	
	/**
	 * Represents the thread for which the task was stolen the last time.
	 */
	protected ThreadLocal<Integer> lastStolenFrom = null;	
	protected static final int NOT_STOLEN = -1;			
	
	/** Stores the current count of active interactive tasks. This is private, 
	 * there is no need for sub-classes to refine how interactive tasks are handled */
	private AtomicInteger interactiveTaskCount = new AtomicInteger(0);
	
	protected AbstractTaskPool() {
		initialise();
	}
	
	
	/**
	 * This method is in charge of schedule-specific enqueueing of a ready task (public <code>enqueue()</code> and <code>enqueueMulti()</code>
	 * methods, are generic and will eventually use this method). This method will not be called for interactive tasks. This method is also not 
	 * necessarily executed by the actual original enqueueing thread (since it might be called later, as a task could wait for other tasks to complete).
	 *  
	 */
	protected abstract void enqueueReadyTask(TaskID<?> taskID); 
	
	/**
	 * (schedule-specific) 
	 * The worker thread pools for a task to execute. If there currently isn't one, then it returns null.
	 */
	public abstract TaskID<?> workerPollNextTask();	
	
	/**
	 * (schedule-specific)
	 * Performs initialization depending on the scheduling policy. 
	 */
	protected abstract void initialise();
	
	/**
	 * Creates a TaskID for the specified task (whose details are contained in the TaskInfo). It then returns the TaskID after 
	 * the task has been queued. This method is generic and not schedule-specific. 
	 * <br>
	 * During the process of enqueueing a task, the framework checks if the enqueueing thread is a task-thread (i.e., the thread
	 * is currently executing another task; therefore this task is being enqueued from within another task). If the enqueueing 
	 * thread is a task-thread, then its corresponding task is recorded as the enclosing task. 
	 */
	public <T> TaskID<T> enqueue(TaskInfo<T> taskInfo) {
		List<TaskID<?>> allDependees = taskInfo.getDependees();
		TaskID<T> taskID = new TaskID<T>(taskInfo);
		
		//determine if this task is being enqueued from within another task. If so, set the enclosing task (needed to 
		//propagate exceptions to outer tasks (in case they have a suitable handler)).
		Thread registeringThread = taskInfo.setRegisteringThread();
		
		if (registeringThread instanceof TaskThread)
			taskID.setEnclosingTask(((TaskThread)registeringThread).currentExecutingTask());
		
		if (taskInfo.hasAnySlots())
			taskInfo.setTaskIDForSlotsAndHandlers(taskID);
		
		if (allDependees == null) {
			if (taskID.isCloudTask()) {
				enqueueCloudTask(taskID);
			}else if (taskID.isInteractive()) {
				startInteractiveTask(taskID);
			}else {
				enqueueReadyTask(taskID);
			}
		} else {
			enqueueWaitingTask(taskID, allDependees);
		}
		
		return taskID;
	}	
	
	@Override
	public <T> TaskIDGroup<T> enqueueMulti(TaskInfo<T> taskInfo){
		int count = taskInfo.taskCount;
		if (count == ParaTask.STAR)
			count = ThreadPool.getMultiTaskThreadPoolSize();
		
		//currently there is no mechanism for TaskIDGroups where different tasks are scheduled within a group!
		TaskIDGroup<T> group = new TaskIDGroup<T>(count, taskInfo);
		
		List<TaskID<?>> allDependees = taskInfo.getDependees();
		Thread registeringThread = taskInfo.setRegisteringThread();
		
		if (registeringThread instanceof TaskThread)
			group.setEnclosingTask(((TaskThread)registeringThread).currentExecutingTask());
		
		if (taskInfo.hasAnySlots())
			taskInfo.setTaskIDForSlotsAndHandlers(group);
		
		if (allDependees == null)
			if (group.isInteractive()){ 
				startInteractiveTask(group);
			}
			else{
				enqueueReadyTask(group);
			}
		else// addDependences != null
			enqueueWaitingTask(group, allDependees);
		
		return group;
	}
	
	/**
	 * The worker thread blocks until it gets a task to execute. This method only returns when it finds an appropriate task 
	 * for the calling worker (therefore appears as blocking). If keeps polling for a task (this polling is schedule-specific). 
	 * If it does not find anything from the poll, then it sleeps before trying again.
	 * 
	 */
	@Override
	public TaskID<?> workerTakeNextTask() {
		while (true) {
			TaskID<?> nextTaskID = workerPollNextTask();
			
			if (nextTaskID != null) 
				return nextTaskID;
			
			try {
				Thread.sleep(ParaTask.WORKER_SLEEP_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the count of currently active interactive tasks. This is usually to know how many threads there are.
	 */
	public int getActiveInteractiveTaskCount() {
		return interactiveTaskCount.get();
	}
	
	/**
	 * Used to decrement the count of interactive tasks, given that the taskID belongs to an interactive task.
	 */
	public boolean interactiveTaskCompleted(TaskID<?> taskID) {
		if (taskID != null && taskID.isInteractive()){
			interactiveTaskCount.decrementAndGet();
			return true;
		}
		return false;
	}
	 /**
	  * Increases the number of interactive tasks by one, and assigns the interactive task to a
	  * new interactive thread, and starts the thread. 
	  * @param taskID The TaskID<?> object that represents the interactive task.
	  */
	protected <T> void startInteractiveTask(TaskID<T> taskID) {
		if (!taskID.isInteractive() || taskID == null)
			return;
		
		else if (taskID instanceof TaskIDGroup<?>){
			TaskIDGroup<T> taskIDGroup = (TaskIDGroup<T>) taskID;
			int taskCount = taskIDGroup.getGroupSize();
			TaskInfo<T> taskInfo = taskIDGroup.getTaskInfo();
			taskInfo.setTaskInfoOfMultiTask(true);
			for (int taskIndex = 0; taskIndex < taskCount; taskIndex++){
				TaskID<T> subTaskID = new TaskID<T>(taskInfo);
				subTaskID.setRelativeID(taskIndex);				
				taskIDGroup.addInnerTask(subTaskID);
				startInteractiveTask(subTaskID);
			}
			taskIDGroup.setExpanded(true);
		}
		
		else{
			interactiveTaskCount.incrementAndGet();
			for (WeakReference<InteractiveThread> interactiveRef : cachedInteractiveThreadPool){
				InteractiveThread interactiveThread = interactiveRef.get();
				if(interactiveThread != null && interactiveThread.isInactive()){
					interactiveThread.setTaskID(taskID);
					return;
				}
			}			
			InteractiveThread newInteractiveThread = new InteractiveThread(this, taskID);
			newInteractiveThread.start();
			cachedInteractiveThreadPool.add(new WeakReference<InteractiveThread>(newInteractiveThread));
		}
	}
	
	protected <T> void enqueueCloudTask(TaskID<T> cloudTask) {
		//new cloud tasks are only added to the cloud task pool if the cloud engine is active. 
		if(ParaTask.isCloudModeOn()) {
			this.cloudTaskQueue.add(cloudTask);
		}else {
			throw new RuntimeException("ONE OR MORE CLOUD TASKS COULD NOT BE ENQUEUED, DUE TO CLOUD ENGINE SHUTDOWN!");
		}
	}
	
	/*
	 * Adds a task to the queue of waiting-tasks, which are waiting on their dependencies to finish.  
	 * There is just one waiting queue, therefore adding to the waiting queue is not schedule-specific.
	 * If a waiting task has dependencies, those dependencies will be set for the TaskID as well.
	 * and They get the waiting task as a waiter for the dependencies.  
	 */
	protected void enqueueWaitingTask(TaskID<?> taskID, List<TaskID<?>> allDependees) {

		if (allDependees.size() > 0) {
			waitingTasks.put(taskID, "");
			taskID.setRemainingDependees(allDependees);
			
			for (int dependeeIndex = 0; dependeeIndex < allDependees.size(); dependeeIndex++) {
				allDependees.get(dependeeIndex).addWaiter(taskID);
			}
		} else {
			enqueueReadyTask(taskID);
		}
	}
	

	/**
	 * Removes the specified task off the waiting queue and moves it onto the ready-queue. 
	 */
	public void nowReady(TaskID<?> waiter) {
		//ensures that it is only enqueued once (so that enqueuing it a second time will fail)
		Object obj = waitingTasks.remove(waiter);
		if (obj != null) {
			if (waiter.isCloudTask())
				enqueueCloudTask(waiter);
			if (waiter.isInteractive())
				startInteractiveTask(waiter);
			else
				enqueueReadyTask(waiter);
		}
	}
	
	/**
	 * Creates the worker threads, but none of the queues. Must be called from the 
	 * initialize() method of the task pools that extend {@link #AbstractTaskPool()}. 
	 */
	 
	protected void initialiseWorkerThreads() {
		ThreadPool.initialize(this);
	}
	
	public boolean executeSynchronously(int cutoff) {
		return false;
	}
	
	public Map<Integer, LinkedBlockingDeque<TaskID<?>>> getLocalOneoffTaskQueues() {
		return localOneOffTaskQueues;
	}
	
	public List<AbstractQueue<TaskID<?>>> getPrivateTaskQueues() {
		return privateTaskQueues;
	}
	
	public synchronized TaskID<?> getNextCloudTask(){
		TaskID<?> taskID = cloudTaskQueue.poll();
		if(taskID != null) {
			cloudTaskQueue.remove(taskID);
			return taskID;
		}
		return null;
	}
	
	@Override
	public int cloudTaskPoolSize() {
		return cloudTaskQueue.size();
	}
}
