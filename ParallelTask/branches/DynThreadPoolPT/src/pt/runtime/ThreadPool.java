/**
 * 
 */
package pt.runtime;

import java.lang.reflect.Method;
import java.util.AbstractQueue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import pt.runtime.ParaTask;
import pt.runtime.TaskID;
import pt.runtime.Taskpool;
import pt.runtime.WorkerThread;
import pt.runtime.ParaTask.ThreadPoolType;


/**
 * @author Kingsley
 * 
 * @since 01/10/2013
 * Remove all the unnecessary code before merging.
 */
public class ThreadPool {

	private static int poolSize = -1;
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * 
	 * This two variables are not really necessary.
	 * 
	 * @since 27/05/2013
	 * Re use the field of "oneoffTaskThreadPoolSize" to trace how many one-off task
	 * worker threads should EXACTLY exist in the one-off task thread pool
	 * Re use the field of "multiTaskThreadPoolSize" to trace how many one-off task
	 * worker threads should EXACTLY exist in the one-off task thread pool
	 * 
	 * @since 31/05/2013
	 * Need a lock to make sure that all the operations on the thread pool collection 
	 * are atomic.
	 * 
	 * */
	private static int multiTaskThreadPoolSize;
	
	private static int oneoffTaskThreadPoolSize;

	private final static ReentrantLock reentrantLock = new ReentrantLock();
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * 
	 * Used to indicate different thread pool type
	 * 
	 * @since 27/05/2013
	 * Move this enumeration to the class of ParaTask, since it is open to user.
	 * 
	 * */
	 /* private static enum ThreadPoolType{
	    	ALL, ONEOFF, MULTI
	 }*/
	
    /**
     * 
     * @author Kingsley
	 * @since 16/05/2013
	 * Change the pool size from LinkedList to TreeMap, the key is the thread id.
	 * The benifit is TreeMap is already sorted, so we can get the worker thread
	 * who owns the highest thread id, or get the worker thread just simply using 
	 * its thread id as a index.
	 * 
	 * @since 16/06/2013
	 * One-off task worker thread does not need to be sorted and synchronized.
	 * Using HashMap instead.
	 * 
	 */
	private static Map<Integer, WorkerThread> oneoffTaskWorkers = new HashMap<Integer, WorkerThread>();

	private static SortedMap<Integer, WorkerThread> multiTaskWorkers = Collections.synchronizedSortedMap(new TreeMap<Integer, WorkerThread>());
	
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * 
	 * Used to maintain the global id
	 * 
	 * */
	private static int globalID = 0;
	
	/**
	 * 
	 * @author Kingsley
	 * @since 11/05/2013
	 * 
	 * This is used to enqueue fake task
	 * The fake task is used to kill threads.
	 * */
	private static Taskpool taskpool;

	static {
		if (poolSize < 0) {
			// Initially, the pool size is equal to the nuber of processors
			poolSize = Runtime.getRuntime().availableProcessors();
		}
	}

	/**
	 * 
	 * @Author Kingsley
	 * @since 25/04/2013
	 * Initialize worker threads here
	 * 
	 * @since 11/05/2013
	 * Set taskpool first
	 * Add a timer to test thread killing, this code will probably be removed later 
	 * 
	 * */
	
	protected static void initialize(Taskpool taskpool) {
		ThreadPool.taskpool = taskpool;
		initializeWorkerThreads(taskpool);
	}
	
	private static void initializeWorkerThreads(Taskpool taskpool) {
		/**
		 * 
		 * @Author Kingsley
		 * @since 26/04/2013
		 * It must to create different worker thread separately, since local queue is not shared,
		 * the index of local queue for both worker thread type starts from 0;
		 * 
		 * @since 02/05/2013
		 * One-off task threads do not need local thread ID.
		 * 
		 * 
		 * */
		int multiTaskWorkerID = 0;
		
		int multiTaskThreadPoolSize = poolSize/2;
		
		for (int i = 0; i < poolSize; i++, globalID++) {
			if (multiTaskWorkerID < multiTaskThreadPoolSize) {
				WorkerThread workers = new WorkerThread(globalID, multiTaskWorkerID, taskpool, true);
				workers.setPriority(Thread.MAX_PRIORITY);
				workers.setDaemon(true);

				multiTaskWorkers.put(globalID, workers);
				
				multiTaskWorkerID++;
				
				/**
				 * 
				 * @author Kingsley
				 * @since : 18/05/2013
				 * No matter what scheduling policy is, private task queues
				 * are always required. Before putting something inside, the collection
				 * of the queues must has already been initialized. 
				 * 
				 * */
				List<AbstractQueue<TaskID<?>>> privateTaskQueues = taskpool.getPrivateTaskQueues();
				privateTaskQueues.add(new PriorityBlockingQueue<TaskID<?>>(
						AbstractTaskPool.INITIAL_QUEUE_CAPACITY,
						AbstractTaskPool.FIFO_TaskID_Comparator));
				
				workers.start();
			}else {
			
		
				/**
				 * 
				 * @Auther : Kingsley
				 * @since : 02/05/2013
				 * One-off task threads do not need local thread ID. 
				 * Simply give its local id the same value as its global id.
				 * 
				 * */
				WorkerThread workers = new WorkerThread(globalID, globalID, taskpool, false);
				workers.setPriority(Thread.MAX_PRIORITY);
				workers.setDaemon(true);
				oneoffTaskWorkers.put(globalID, workers);
				
				/**
				 * 
				 * @Auther : Kingsley
				 * @since : 02/05/2013
				 * 
				 * Create local one-off task queue for each worker here since the HashMap
				 * requires worker's global id
				 * 
				 * @since : 15/05/2013
				 * According to different scheduling policies, local one-off task queues
				 * maybe not required. Before putting something inside, check if the collection
				 * of the queues has already been initialized. 
				 * 
				 * */
				Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneoffTaskQueues = taskpool.getLocalOneoffTaskQueues();
				if (null != localOneoffTaskQueues) {
					localOneoffTaskQueues.put(globalID, new LinkedBlockingDeque<TaskID<?>>());
				}

				workers.start();
			}
		}
		multiTaskThreadPoolSize = multiTaskWorkers.size();
		oneoffTaskThreadPoolSize = oneoffTaskWorkers.size();
	}
	
	//? How should I return the pool size ?
	//Maybe multi task thread pool size + one-off task thread pool size
	protected static int getPoolSize(ThreadPoolType threadPoolType) {
		switch (threadPoolType) {
		case ONEOFF:
			return oneoffTaskThreadPoolSize;
		case MULTI:
			return multiTaskThreadPoolSize;
		default:
			return multiTaskThreadPoolSize + oneoffTaskThreadPoolSize;
		}
	}

	/**
	 * 
	 * @Author Kingsley
	 * @since 26/04/2013
	 * 
	 * Set total pool size and adjust the size of each pool.
	 * 
	 * @since 02/05/2013
	 * When call this method, set the size for each pool.
	 * 
	 * @since 27/05/2013
	 * Add another argument to indicate the thread pool type
	 * 
	 * */
	
	protected static void setPoolSize(ThreadPoolType threadPoolType, int poolSize) {
		/**
		 * 
		 * @author Kingsley
		 * @since 18/05/2013
		 * 
		 * This two variables are not really necessary. 
		 * When user call setPoolSize(int poolSize), add or remove workers
		 * from the thread pool directly.
		 * 
		 * 
		 * */
		ThreadPool.poolSize = poolSize;
		adjustThreadPool(threadPoolType, poolSize);
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
		return oneoffTaskWorkers.size();
	}

	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * 
	 * Adjust the pool size according to the parameter.
	 * Only called after user called setPoolSize(int poolSize);
	 * setMultiTaskThreadPoolSize(int multiTaskThreadPoolSize) or 
	 * setOneoffTaskThreadPoolSize(int oneoffTaskThreadPoolSize)
	 * 
	 * @param ThreadPoolType Indicate different thread pool 
	 * @param newSize
	 * @return 
	 * */
	private static void adjustThreadPool(ThreadPoolType type, int newSize) {
		switch (type) {
		case ALL:
			adjustMultiTaskThreadPool(newSize);
			adjustOneoffThreadPool(newSize);
			break;
		case ONEOFF:
			adjustOneoffThreadPool(newSize);
			break;
		case MULTI:
			adjustMultiTaskThreadPool(newSize);
			break;
		default:
			break;
		}
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
		
	}
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * @param newSize
	 * @return 
	 * 
	 * Adjust the pool size for one-off task thread pool
	 * 
	 * @since 23/05/2013
	 * Slightly modify the method for WorkStealing 
	 * 
	 * @since 27/05/2013
	 * Fix the bug of continues calling setPoolSize();
	 * 
	 * @since 31/05/2013
	 * Simplify the implementation of reducing thread number.
	 * Acquire a lock before loop the one-off task worker thread pool
	 * */
	private static void adjustOneoffThreadPool(int newSize){
		if (oneoffTaskThreadPoolSize == newSize) {
			return;
		}else if (oneoffTaskThreadPoolSize > newSize) {
			int diff = oneoffTaskThreadPoolSize - newSize;

			LottoBox.setLotto(diff);
			
			reentrantLock.lock();
			
			for (Iterator<WorkerThread> iterator = oneoffTaskWorkers.values().iterator(); iterator.hasNext();) {
				WorkerThread workerThread = (WorkerThread) iterator.next();
				workerThread.requireCancel(true);
			}
			
			reentrantLock.unlock();
			
			oneoffTaskThreadPoolSize = oneoffTaskThreadPoolSize - diff;
		}else {
			int diff = newSize - oneoffTaskThreadPoolSize;
			
			for (int i = 0; i < diff; i++, globalID++) {
				WorkerThread workers = new WorkerThread(globalID, globalID, taskpool, false);
				workers.setPriority(Thread.MAX_PRIORITY);
				workers.setDaemon(true);

				oneoffTaskWorkers.put(globalID, workers);
				Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneoffTaskQueues = taskpool.getLocalOneoffTaskQueues();
				if (null != localOneoffTaskQueues) {
					localOneoffTaskQueues.put(globalID, new LinkedBlockingDeque<TaskID<?>>());
				}
				workers.start();
			}
			
			oneoffTaskThreadPoolSize = oneoffTaskWorkers.size();
		}
	}
	
	/**
	 * 
	 * @author Kingsley
	 * @since 16/05/2013
	 * 
	 * Before worker thread really stop working, give it a chance to tell the threadpool
	 * its thread type and its thread id. Then the threadpool can remove it from the 
	 * corresponding thread pool
	 * 
	 * @since 23/05/2013
	 * Do not re-allocate tasks AND let other works keep stealing
	 * Acquire a lock before accessing the one-off task worker thread pool
	 * */
	protected static void lastWords(boolean isMultiTaskWorker, int threadID){
		if (isMultiTaskWorker) {
			
		}else {
			reentrantLock.lock();
			
			oneoffTaskWorkers.remove(threadID);
			
			reentrantLock.unlock();
		}
	}

	/**
	 * 
	 * @Author Kingsley
	 * @since 18/06/2013
	 * @return int
	 * 
	 * Returns the approximate number of threads that are actively executing tasks.
	 * 
	 * */
	public static int getActiveCount(ThreadPoolType type) {
		switch (type) {
		case ONEOFF:
			return oneoffTaskWorkers.size();
		case MULTI:
			return multiTaskWorkers.size();
		default:
			return oneoffTaskWorkers.size()+multiTaskWorkers.size();
		}
	}
 }


