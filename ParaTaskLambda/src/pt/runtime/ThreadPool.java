/**
 * 
 */
package pt.runtime;

import java.util.AbstractQueue;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import pt.runtime.TaskID;
import pt.runtime.Taskpool;
import pt.runtime.WorkerThread;
import pt.runtime.ParaTask.ThreadPoolType;



public class ThreadPool {

	/*
	 * Indicates the size of the thread pool, and is automatically determined according to the 
	 * number of <code>CPU cores</code> at runtime. 
	 */
	private static int totalNumberOfThreads = -1;
	
	/* 
	 * Use the field of "oneoffTaskThreadPoolSize" to trace how many one-off task
	 * worker threads should EXACTLY exist in the one-off task thread pool
	 * Use the field of "multiTaskThreadPoolSize" to trace how many one-off task
	 * worker threads should EXACTLY exist in the one-off task thread pool
	 */
	private static int multiTaskThreadPoolSize = 0;
	
	private static int oneOffTaskThreadPoolSize = 0;

	private final static ReentrantLock reentrantLock = new ReentrantLock();

	
	/*
	 * The pools are <code>TreeMaps</code>, the key is the thread id.
	 * The benefit with <code>TreeMaps</code> is that they are already sorted, so 
	 * we can get the worker thread who owns the highest <code>threadID</code>, or get the worker thread just simply using 
	 * its <code>threadID</code> as an index.
	 * <br><br>
	 * One-off task worker thread does not need to be sorted and synchronized.
	 * Using HashMap instead.
	 */
	private static SortedMap<Integer, WorkerThread> multiTaskWorkers = Collections.synchronizedSortedMap(new TreeMap<Integer, WorkerThread>());
	
	private static Map<Integer, WorkerThread> oneOffTaskWorkers = new HashMap<Integer, WorkerThread>();

	
	
	private static int globalID = 0;
	
	private static Taskpool taskPool;

	protected static void initialize(Taskpool taskpool) {
		ThreadPool.taskPool = taskpool;
		TaskThread.resetTaskThreads();
		initializeWorkerThreads(taskpool);
	}
	
	public static void resetThreadPool(){
		globalID = 0;
	}
	
	/*
	 * This method initializes the <code>Worker Threads</code> that are going to execute tasks
	 * in this instance of <code>Thread Pool</code>. In the first step, this method creates as 
	 * many <code>Multi-task Threads</code> as half of the <code>CPU cores</code> at runtime 
	 * (i.e. half of <code>poolSize</code>). 
	 * <br><br>
	 *Then the <code>Mutli-task Threads</code> are granted <b>maximum</b> priority, and are allowed
	 *to be active as <b>daemon</b> threads if required. Then the <code>Mutli-task Threads</code> are 
	 *added to a <code><i>sorted tree map</i></code>, where their <code>globalID</code> is used as 
	 *the key! Finally there is a <code>privateQueue</code> created and associated to each <code>Mutli-task Thread</code>.
	 *The <code>privateQueues</code> are collected in a <b>list</b> of <code>privateQueues</code>, where the order of
	 *queues is same as the order of threads created. 
	 *<br><br> 
	 *Threads are given <code>globalIDs</code> which indicates their position in the thread pool. <code>Multi-task Threads</code>
	 *are also given a <code>localID</code> or <code>multiTaskWorkerID</code> which indicates the position of 
	 *<code>Multi-task Threads</code> with respect to each other.
	 *<br><br>
	 *After the <code>Multi-task Threads</code> are created, the other half of <code>poolSize</code> (i.e. No. of CPU-cores) is
	 *specified to <code>OneOff-task</code> threads. There will be <code>globalID</code>s associated to <code>OneOff-task</code>
	 *threads as both <u>local</u> and <u>global</u> IDs, because <code>OneOff-tasks</code> threads don't really need a
	 *local IDs. Same as <code>Multi-task</code> threads, <code>OneOff-task</code> threads are given <b>maximum</b> priority, 
	 *and are allowed to be active as <b>daemon</b> threads if required. <code>OneOff-task</code> threads are then added to
	 *a <i>Map</i> collection which maps a worker thread to its <code>globalID</code>. Moreover, there is one 
	 *<code>localOneOffTask</code> queue associated to each <code>OneOff-task</code> thread. The indices of local queues for both 
	 *worker thread types starts from 0.
	 * 
	 */
	private static void initializeWorkerThreads(Taskpool taskpool) {
		
		if(multiTaskThreadPoolSize == 0){
			multiTaskThreadPoolSize = Runtime.getRuntime().availableProcessors();
		}
		if(oneOffTaskThreadPoolSize == 0){
			oneOffTaskThreadPoolSize = Runtime.getRuntime().availableProcessors();
		}
		
		totalNumberOfThreads = oneOffTaskThreadPoolSize + multiTaskThreadPoolSize;
		
		int multiTaskWorkerID = 0;
		List<AbstractQueue<TaskID<?>>> privateTaskQueues = taskpool.getPrivateTaskQueues();
		Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneoffTaskQueues = taskpool.getLocalOneoffTaskQueues();
				
		for (int i = 0; i < totalNumberOfThreads; i++, globalID++) {
			if (multiTaskWorkerID < multiTaskThreadPoolSize) {
				WorkerThread workerThread = new WorkerThread(globalID, multiTaskWorkerID, taskpool, true);
				workerThread.setPriority(Thread.MAX_PRIORITY);
				workerThread.setDaemon(true);

				multiTaskWorkers.put(globalID, workerThread);
				
				multiTaskWorkerID++;
				
				/* No matter what scheduling policy is, private task queues
				 * are always required. Before putting something inside, the collection
				 * of the queues must have already been initialized. */
				privateTaskQueues.add(new PriorityBlockingQueue<TaskID<?>>(
						AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
						AbstractTaskPool.FIFO_TaskID_Comparator));
				
				workerThread.start();
			}else {
			
				// One-off task threads do not need local thread ID (multiTaskID). 
				WorkerThread workerThread = new WorkerThread(globalID, globalID, taskpool, false);
				workerThread.setPriority(Thread.MAX_PRIORITY);
				workerThread.setDaemon(true);
				oneOffTaskWorkers.put(globalID, workerThread);
				if (localOneoffTaskQueues != null) {
					localOneoffTaskQueues.put(globalID, new LinkedBlockingDeque<TaskID<?>>());
				}

				workerThread.start();
			}
		}
	}
	
	
	//? How should I return the pool size ?
	//Maybe multi task thread pool size + one-off task thread pool size
	protected static int getPoolSize(ThreadPoolType threadPoolType) {
		switch (threadPoolType) {
		case ONEOFF:
			return oneOffTaskThreadPoolSize;
		case MULTI:
			return multiTaskThreadPoolSize;
		default:
			return totalNumberOfThreads;
		}
	}

	/**
	 * 
	 * This method sets the size of the <code>Thread Pool</code> and depending on the type
	 * of the <code>Thread Pool</code> it takes further required actions as a result of 
	 * the change. The further actions take place in {@link #adjustThreadPoolCapacity(ThreadPoolType, int)}.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  15/9/2014
	 * */
	
	protected static void setPoolSize(ThreadPoolType threadPoolType, int poolSize) {
		adjustThreadPoolCapacity(threadPoolType, poolSize);
	}
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * 
	 * Change the getter methods, get the pool size directly from returning the collection size. 
	 * */
	
	protected static int getMultiTaskThreadPoolSize() {
		return multiTaskWorkers.size();
	}
	
	protected static int getOneoffTaskThreadPoolSize() {
		return oneOffTaskWorkers.size();
	}

	/* 
	 * Adjust the pool size according to the parameter.
	 * Only called after user called setPoolSize(int poolSize);
	 * setMultiTaskThreadPoolSize(int multiTaskThreadPoolSize) or 
	 * setOneoffTaskThreadPoolSize(int oneoffTaskThreadPoolSize)*/
	private static void adjustThreadPoolCapacity(ThreadPoolType type, int newSize) {
		switch (type) {
		case ALL:
			adjustMultiTaskThreadPool(newSize);
			adjustOneOffThreadPool(newSize);
			break;
		case ONEOFF:
			adjustOneOffThreadPool(newSize);
			break;
		case MULTI:
			adjustMultiTaskThreadPool(newSize);
			break;
		default:
			break;
		}
		
		totalNumberOfThreads = oneOffTaskThreadPoolSize + multiTaskThreadPoolSize;
	}
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * @param newSize
	 * @return 
	 * 
	 * Adjust the pool size for multi task thread pool
	 * Work on this part later.
	 * */
	private static void adjustMultiTaskThreadPool(int newSize){
		if(ParaTask.isInitialized())
			dynamicAdjustMultiTaskThreadPool(newSize);
		else
			multiTaskThreadPoolSize = newSize;
	}
	
	private static void adjustOneOffThreadPool(int newSize){
		if(ParaTask.isInitialized())
			dynamicAdjustOneOffThreadPool(newSize);
		else 
			oneOffTaskThreadPoolSize = newSize;
	}
	
	/**
	 * Adjust the pool size for one-off task thread pool. If the new size is smaller than 
	 * the original size of <code>OneOff-task</code> thread pool, then all threads in <code>OneOff-task</code>
	 * thread pool will get poisoned. However, because <code>lottoNum</code> is set to the number of 
	 * <i>redundant threads</i>, only the redundant threads will be removed. 
	 * <br><br>
	 * If the new size is bigger than the original size of <code>OneOff-task</code> thread pool, then there will
	 * be as many <code>OneOff-task</code> worker threads added to the <code>OneOff-task</code> thread pool as it
	 * is required to fill the new capacity. Threads will be granted <b>maximum</b> priority, and will be allowed 
	 * to behave as <b>daemon</b> threads if required. The threads will be added to the <code>OneOff-task</code>
	 * thread pool, and their corresponding <code>localOneOffTask</code> queue will be added to the <code>Map</code>
	 * collection. 
	 *
	 *@author Mostafa Mehrabi
	 *@since 16/9/2014
	 * 
	 * */
	private static void dynamicAdjustOneOffThreadPool(int newSize){
		if (oneOffTaskThreadPoolSize == newSize) {
			return;
		}else if (oneOffTaskThreadPoolSize > newSize) {
			int diff = oneOffTaskThreadPoolSize - newSize;

			ThreadRedundancyHandler.setNumberOfRedundantThreads(diff);
			
			reentrantLock.lock();
			
			//in the "waitTillFinished" method in TaskID, if a thread is poisoned, it cannot
			//execute any tasks! So, the "informThreadPool" method sets cancelRequest back to "false"
			//if enough threads have been killed. 
			for (WorkerThread workerThread : oneOffTaskWorkers.values()){
				workerThread.requestCancel(true);
			}
			
			reentrantLock.unlock();
			
		}else {
			int diff = newSize - oneOffTaskThreadPoolSize;
			
			for (int i = 0; i < diff; i++, globalID++) {
				WorkerThread workerThread = new WorkerThread(globalID, globalID, taskPool, false);
				workerThread.setPriority(Thread.MAX_PRIORITY);
				workerThread.setDaemon(true);

				oneOffTaskWorkers.put(globalID, workerThread);
				Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneoffTaskQueues = taskPool.getLocalOneoffTaskQueues();
				if (null != localOneoffTaskQueues) {
					localOneoffTaskQueues.put(globalID, new LinkedBlockingDeque<TaskID<?>>());
				}
				workerThread.start();
			}
			
		}
		
		oneOffTaskThreadPoolSize = newSize;
	}
	
	private static void dynamicAdjustMultiTaskThreadPool(int newSize){
		//Dynamic resizing of thread-pools, especially the multi-thread pool is not a good idea!
		return;
	}
	
	
	/*
	 * Before worker thread really stop working, give it a chance to tell the threadpool
	 * its thread type and its thread id. Then the threadpool can remove it from the 
	 * corresponding thread pool
	 * */
	protected static void removeThreadFromPool(boolean isMultiTaskWorker, int threadID){
		if (!isMultiTaskWorker) {
		    reentrantLock.lock();
			//shouldn't we remove their taskqueues as well?
		   	oneOffTaskWorkers.remove(threadID);
									
			reentrantLock.unlock();
		}
	}

	/**
	 * 
	 * @author Kingsley
	 * @since 18/06/2013
	 * @return int
	 * 
	 * Returns the approximate number of threads that are actively executing tasks.
	 * 
	 * */
	public static int getNumberOfActiveThreads(ThreadPoolType type) {
		switch (type) {
		case ONEOFF:
			return oneOffTaskWorkers.size();
		case MULTI:
			return multiTaskWorkers.size();
		default:
			return oneOffTaskWorkers.size()+multiTaskWorkers.size();
		}
	}
	
	/*
	 * Returns the worker thread corresponding to the ID that is sent to the method. Returns null if there
	 * are no threads registered for that ID in the pool. 
	 * 
	 * @param ID The integer parameter that stands of the ID of the thread that is to be retrieved from the pool.
	 * @return WorkerThread 
	 * 
	 * @author Mostafa Mehrabi
	 * @since  7/9/2015
	 */
	WorkerThread getWorkerThreadForID(int ID){
		if (multiTaskWorkers.containsKey(ID))
			return multiTaskWorkers.get(ID);
		else if (oneOffTaskWorkers.containsKey(ID))
			return oneOffTaskWorkers.get(ID);
		return null;
	}
 }


