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

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
	 * @Author : Kingsley
	 * @since : 25/04/2013
	 * The number of multi task worker threads and the number of one-off task worker threads 
	 * 
	 * @since : 04/05/2013
	 * These numbers will only be used for initialization since they could be changed during
	 * the runtime, everytime they are required, re-query the thread pool.
	 * 
	 * @since : 18/05/2013
	 * These numbers should be only accessed during the runtime by calling the thread pool,
	 * in order to avoid accessing them during the initialization, cancel these two variables. 
	 * So as to numThreads, cancel it as well.
	 * */
	
	/* The number of worker threads */
	//protected int numThreads = -1;
	//protected int numMultiTaskThreads = -1;
	//protected int numOneoffTaskThreads = -1;
	
	
	/* The pool of worker threads */
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 25/04/2013
	 * Task pool does not need to know anything about worker, a thread pool class
	 * is used to manage the work threads.
	 * 
	 * */
	//protected WorkerThread[] workers;
	
	/* Tasks that are not ready to be executed yet, still waiting for dependences to be met */
	protected ConcurrentHashMap<TaskID<?>, Object> waitingTasks = new ConcurrentHashMap<TaskID<?>, Object>();
	
	/* Ready tasks, in a shared global queue (for work-sharing implementations) */
	/**
	 * 
	 * @Author Kingsley
	 * @since 25/04/2013
	 * Separate Multi Task and One-off Task. Create two different global queues
	 * 
	 * */
	//protected PriorityBlockingQueue<TaskID<?>> globalTaskqueue = null;
	protected PriorityBlockingQueue<TaskID<?>> globalMultiTaskqueue = null;
	protected PriorityBlockingQueue<TaskID<?>> globalOne0ffTaskqueue = null;
	
	
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 25/04/2013
	 * Separate Multi Task and One-off Task. Create two different mixed queues
	 * 
	 * */
	/* Ready tasks (for mixed-scheduling implementation) */
	//protected FifoLifoQueue<TaskID<?>> mixedQueue = null;
	protected FifoLifoQueue<TaskID<?>> mixedMultiTaskqueue = null;
	protected FifoLifoQueue<TaskID<?>> mixedOneoffTaskqueue = null;
	
	/* Ready tasks, in private queues (stores multi-tasks) - no stealing occurs within these queues */
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 25/04/2013
	 * Since new worker threads could be added into the thread pool, or old worker threads could
	 * be removed from the thread pool, the data structure of array could not be used anymore(array 
	 * has to be initialized with a certain length, and can not be changed). Instead a dynamic 
	 * data structure should be used here.
	 * 
	 * 
	 * */
	//protected AbstractQueue<TaskID<?>>[] privateQueues;
	protected List<AbstractQueue<TaskID<?>>> privateQueues;
	
	/* Ready tasks, for each worker thread. If empty, workers steal from another worker within these queues (for work-stealing implementations) */ 
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 25/04/2013
	 * Since new worker threads could be added into the thread pool, or old worker threads could
	 * be removed from the thread pool, the data structure of array could not be used anymore(array 
	 * has to be initialized with a certain length, and can not be changed). Instead a dynamic 
	 * data structure should be used here.
	 * 
	 * Generic Type will be erased at the compile time, so LinkedBlockingDeque has to be used rather
	 * than the interface Deque.
	 * 
	 * Separate Multi Task and One-off Task. Create two different global queues
	 * 
	 * @since : 02/05/2013
	 * One-off task threads do not need local thread ID, which means a List can not be used to group
	 * local one-off task queues here. HashMap will be used here as a replacement.
	 * 
	 * @since : 04/05/2013
	 * There are no local multi task queues any more.
	 * 
	 * */	
	//protected Deque<TaskID<?>>[] localQueues = null;		 
	//protected List<LinkedBlockingDeque<TaskID<?>>> localQueues = null;
	//protected List<LinkedBlockingDeque<TaskID<?>>> localMultiTaskQueues = null;
	//protected List<LinkedBlockingDeque<TaskID<?>>> localOneoffTaskQueues = null;
	protected Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneoffTaskQueues = null;
	
	
	protected ThreadLocal<Integer> lastStolenFrom = null;	
	protected static final int NOT_STOLEN = -1;			
	
	/* Stores the current count of active interactive tasks. This is private, 
	 * there no need for sub-classes to refine how interactive tasks are handled */
	private AtomicInteger interactiveTaskCount = new AtomicInteger(0);
	
	protected AbstractTaskPool() {
		/**
		 * 
		 * @Author : Kingsley
		 * @since : 25/04/2013
		 * 
		 * Enquire threads number from thread pool rather than from ParaTask
		 * 
		 * 
		 *  @since : 18/05/2013
		 * These numbers should be only accessed during the runtime by calling the thread pool,
		 * It does not make sense to get those values before the completion of initialization,
		 * */
		//numThreads = ParaTask.getThreadPoolSize();
		
		//numMultiTaskThreads = ThreadPool.getMultiTaskThreadPoolSize();
		//numOneoffTaskThreads = ThreadPool.getOneoffTaskThreadPoolSize();
		//numThreads = ThreadPool.getPoolSize();
		initialise();
	}
	
	/*
	 * (schedule-specific) 
	 * The schedule-specific enqueuing of a ready task is defined here (not in the public enqueue() and enqueueMulti() methods, 
	 * since those are generic and will eventually use this method). 
	 * This method will not be called for interactive tasks, since the enqueue(), etc will check this beforehand.
	 * 
	 * This method is also not necessarily executed by the actual original enqueueing thread (since this might be called later since
	 * the task was waiting for other tasks to complete).
	 *  
	 */
	protected abstract void enqueueReadyTask(TaskID<?> taskID/* , boolean wasWaiting  */); // this last parameter could be used to fine-tune performance
	
	/*
	 * (schedule-specific) 
	 * The worker thread polls for a task to execute. If there currently isn't one, then it returns null.
	 */
	public abstract TaskID workerPollNextTask();	
	
	/*
	 * (schedule-specific)
	 * Performs initialisation specific to the schedule. 
	 */
	protected abstract void initialise();
	
	/*
	 * Creates a TaskID for the specified task (whose details are contained in the TaskInfo). It then returns the TaskID after 
	 * the task has been queued. This method is generic and not schedule-specific. 
	 */
	public TaskID enqueue(TaskInfo taskinfo) {
		ArrayList<TaskID> allDependences = null;
		if (taskinfo.getDependences() != null)
			allDependences = ParaTask.allTasksInList(taskinfo.getDependences());
		
		TaskID taskID = new TaskID(taskinfo);
		
		//-- determine if this task is being enqueued from within another task.. if so, set the enclosing task (needed to 
		//--		propogate exceptions to outer tasks (in case they have a suitable handler))
		Thread rt = taskinfo.setRegisteringThread();
		
		if (rt instanceof TaskThread)
			taskID.setEnclosingTask(((TaskThread)rt).currentExecutingTask());
		
		if (taskinfo.hasAnySlots())
			taskinfo.setTaskIDForSlotsAndHandlers(taskID);
		
		if (taskID.isPipeline()) {
			//-- pipeline threads don't need to wait for dependencies
			startPipelineTask(taskID);
		} else if (allDependences == null) {
			if (taskID.isInteractive())
				startInteractiveTask(taskID);
			else
				enqueueReadyTask(taskID);
		} else {
			enqueueWaitingTask(taskID, allDependences);
		}
		
		return taskID;
	}
	
	/*
	 * Creates a TaskIDGroup after enqueuing the specified multi-task. This is not schedule-specific, since it uses underlying methods
	 * to perform the schedule-specific engueue.
	 */
	
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 04/05/2013
	 * 
	 * Later Expansion
	 * Rather than using an TaskIDGroup here, using a simple TaskID instead.
	 * 
	 *
	 * */
	
	/*public TaskIDGroup enqueueMulti(TaskInfo taskinfo, int count) {
		if (count <= 0)
			count = numMultiTaskThreads;
		
		TaskIDGroup group = new TaskIDGroup(count, taskinfo);
		
		ArrayList<TaskID> allDependences = null;
		if (taskinfo.getDependences() != null)
			allDependences = ParaTask.allTasksInList(taskinfo.getDependences());
		
		Thread rt = taskinfo.setRegisteringThread();
		boolean insideAnotherTask = rt instanceof TaskThread;
		
		TaskID encTask = null;
		if (insideAnotherTask)
			encTask = ((TaskThread)rt).currentExecutingTask();
		
		for (int i = 0; i < count; i++) {
			TaskID id = new TaskID(taskinfo);
			
			if (insideAnotherTask)
				id.setEnclosingTask(encTask);
			
			id.setRelativeID(i);
			id.setExecuteOnThread(i%numMultiTaskThreads);
			group.add(id);
		}
		
		if (taskinfo.hasAnySlots())
			taskinfo.setTaskIDForSlotsAndHandlers(group);
		
		//-- Don't want to queue the tasks until the group has been created
		for (Iterator<TaskID> it = group.groupMembers(); it.hasNext();) {
			TaskID taskID = it.next();
			
			if (allDependences == null)
				if (taskID.isInteractive()) 
					startInteractiveTask(taskID);
				else
					enqueueReadyTask(taskID);
			else
				enqueueWaitingTask(taskID, allDependences);
		}
		
		return group;
	}*/
	
	
	public TaskIDGroup enqueueMulti(TaskInfo taskinfo, int count){
		
		if (count <= 0)
			count = ThreadPool.getMultiTaskThreadPoolSize();
		
		TaskIDGroup group = new TaskIDGroup(count, taskinfo);
		group.setCount(count);
		
		ArrayList<TaskID> allDependences = null;
		if (taskinfo.getDependences() != null)
			allDependences = ParaTask.allTasksInList(taskinfo.getDependences());
			
		Thread rt = taskinfo.setRegisteringThread();
		
		if (rt instanceof TaskThread)
			group.setEnclosingTask(((TaskThread)rt).currentExecutingTask());
		
		if (taskinfo.hasAnySlots())
			taskinfo.setTaskIDForSlotsAndHandlers(group);
		
		if (allDependences == null)
			if (group.isInteractive()) 
				startInteractiveTask(group);
			else
				enqueueReadyTask(group);
		else
			enqueueWaitingTask(group, allDependences);
		
		return group;
	}
	
	//Another option on how to implement Later Expansion
	/*public TaskIDGroup enqueueMulti(TaskInfo taskinfo, int count){
		if (count <= 0)
			count = ThreadPool.getMultiTaskThreadPoolSize();;
		
		TaskIDGroup group = new TaskIDGroup(count, taskinfo);
		
		ArrayList<TaskID> allDependences = null;
		if (taskinfo.getDependences() != null)
			allDependences = ParaTask.allTasksInList(taskinfo.getDependences());
		
		Thread rt = taskinfo.setRegisteringThread();
		boolean insideAnotherTask = rt instanceof TaskThread;
		
		TaskID encTask = null;
		if (insideAnotherTask)
			encTask = ((TaskThread)rt).currentExecutingTask();
		
		for (int i = 0; i < count; i++) {
			TaskID id = new TaskID(taskinfo);
			
			if (insideAnotherTask)
				id.setEnclosingTask(encTask);

			group.add(id);
		}
		
		if (taskinfo.hasAnySlots())
			taskinfo.setTaskIDForSlotsAndHandlers(group);
		
		if (allDependences == null)
			if (group.isInteractive()) 
				startInteractiveTask(group);
			else
				enqueueReadyTask(group);
		else
			enqueueWaitingTask(group, allDependences);
		
		return group;
	}*/
	
	
	
	
	/*
	 * The worker thread blocks until it gets a task to execute.   
	 * 
	 * This method only returns when it finds an appropriate task for the calling worker (therefore appears as blocking).
	 * 
	 * If keeps polling for a task (this polling is schedule-specific). If it did not find anything from the poll, then it 
	 * sleeps before trying again, and again.
	 * 
	 */
	public TaskID workerTakeNextTask() {
		while (true) {
			TaskID next = workerPollNextTask();
			
			if (next != null) 
				return next;
			
			try {
				Thread.sleep(ParaTaskHelper.WORKER_SLEEP_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Returns the count of currently active interactive tasks. This is usually to know how many threads there are.
	 */
	public int getActiveInteractiveTaskCount() {
		return interactiveTaskCount.get();
	}
	
	/*
	 * Used to decrement the count of interactive tasks
	 */
	public void interactiveTaskCompleted(TaskID<?> taskID) {
		interactiveTaskCount.decrementAndGet();
	}
	
	protected void startInteractiveTask(TaskID taskID) {
		InteractiveThread it = new InteractiveThread(this, taskID);
		interactiveTaskCount.incrementAndGet();
		it.start();
	}
	
	protected void startPipelineTask(TaskID taskID) {
		PipelineThread pt = new PipelineThread(this, taskID);
		taskID.setPipelineThread(pt);
		pt.start();
	}
	
	/*
	 * There is just one waiting queue, therefore adding to the waiting queue is not schedule-specific.
	 */
	protected void enqueueWaitingTask(TaskID taskID, ArrayList<TaskID> allDependences) {

		if (allDependences.size() > 0) {
			waitingTasks.put(taskID, "");
			taskID.setRemainingDependences(allDependences);
			
			for (int d = 0; d < allDependences.size(); d++) {
				allDependences.get(d).addWaiter(taskID);
			}
		} else {
			enqueueReadyTask(taskID);
		}
	}
	

	/*
	 * Removes the specified task off the waiting queue and onto the ready-queue. 
	 */
	public void nowReady(TaskID<?> waiter) {
		/*
		 * remove 'waiter' from the waiting collection, and put it onto the ready queue
		 * ensures that it is only enqueued once (so that enqueuing it a second time will fail)
		 * */
		Object obj = waitingTasks.remove(waiter);
		if (obj != null) {
			if (waiter.isInteractive())
				startInteractiveTask(waiter);
			else
				enqueueReadyTask(waiter);
		}
	}
	
	/*
	 * This initialisation creates the worker threads, but none of the queues. Should be called from the 
	 * initialise() method of task pools that implement this class. 
	 */
	 
	 /**
	  * 
	  * @Author : Kingsley
	  * @since : 25/04/2013
	  * Task pool does not need to know anything about worker, a thread pool class
	  * is used to manage the work threads.
	  * 
	  * */
	protected void initialiseWorkerThreads() {
		/*workers = new WorkerThread[numThreads];
		for (int i = 0; i < numThreads; i++) {
			workers[i] = new WorkerThread(i, this);
			workers[i].setPriority(Thread.MAX_PRIORITY);
			workers[i].setDaemon(true);
			workers[i].start();
		}*/
		ThreadPool.initialize(this);
	}
	
	public boolean executeSynchronously(int cutoff) {
		return false;
	}
	
	public void printDebugInfo() {
		System.out.println("Debug info for TaskPool...");
		
		System.out.println(" ----------------  currently all debug info removed ");
//		
//		for (int i = 0; i < numThreads; i++) {
//			System.out.println("WT "+i+" completed "+numTasksExecuted[i]+" tasks ("+numTasksStolen[i]+" stolen)");
//		}
//		System.out.println("  -> Total number of tasks completed: " + totalNumTasksExecuted());
	}
	
	public int totalNumTasksExecuted() {
		int total = 0;
//		for (int i = 0; i < numThreads; i++) {
//			total+=numTasksExecuted[i];
//		}
		return total;
	}

	
	/**
	 * 
	 * @Author : Kingsley
	 * @since : 02/05/2013 
	 * Used to access local one-off task queues by thread pool when initialization.
	 * 
	 * @since : 18/05/2013 
	 * Used to access private task queues by thread pool when initialization.
	 *  
	 * */
	public Map<Integer, LinkedBlockingDeque<TaskID<?>>> getLocalOneoffTaskQueues() {
		return localOneoffTaskQueues;
	}
	
	public List<AbstractQueue<TaskID<?>>> getPrivateTaskQueues() {
		return privateQueues;
	}
}
