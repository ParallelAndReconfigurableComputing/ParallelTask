/**
 * 
 */
package paratask.runtime;

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

import paratask.runtime.ParaTask;
import paratask.runtime.ParaTask.ThreadPoolType;
import paratask.runtime.TaskID;
import paratask.runtime.Taskpool;
import paratask.runtime.WorkerThread;


/**
 * @author Kingsley
 * 
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
	//private static LinkedList<WorkerThread> oneoffTaskWorkers = new LinkedList<WorkerThread>();
	//private static SortedMap<Integer, WorkerThread> oneoffTaskWorkers = Collections.synchronizedSortedMap(new TreeMap<Integer, WorkerThread>());
	private static Map<Integer, WorkerThread> oneoffTaskWorkers = new HashMap<Integer, WorkerThread>();

	//private static LinkedList<WorkerThread> multiTaskWorkers = new LinkedList<WorkerThread>();
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
			
			/**
			 * 
			 * @author Kingsley
			 * @since 18/05/2013
			 * 
			 * This two variables are not really necessary.
			 * 
			 * 
			 * */
			//initMultiTaskThreadPoolSize = poolSize/2;
			//initOneoffTaskThreadPoolSize = poolSize-initMultiTaskThreadPoolSize;
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
		
		//Used for test killing
		//new Timer().schedule(new Executor(), 1000 * 3);
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
		//int oneoffTaskWorkerID = 0;
		
		int multiTaskThreadPoolSize = poolSize/2;
		
		for (int i = 0; i < poolSize; i++, globalID++) {
			if (multiTaskWorkerID < multiTaskThreadPoolSize) {
				WorkerThread workers = new WorkerThread(globalID, multiTaskWorkerID, taskpool, true);
				workers.setPriority(Thread.MAX_PRIORITY);
				workers.setDaemon(true);
				
				//multiTaskWorkers.addLast(workers);
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
				
				//oneoffTaskWorkers.addLast(workers);
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
				
				//oneoffTaskWorkerID++;
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
		//calEachPoolSize(poolSize);
		
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
		
		//ThreadPool.multiTaskThreadPoolSize = poolSize;
		//ThreadPool.oneoffTaskThreadPoolSize = poolSize;
		//adjustThreadPool(ThreadPoolType.ALL, poolSize);
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
	 * When user call setMultiTaskThreadPoolSize(int multiTaskThreadPoolSize) or 
	 * setOneoffTaskThreadPoolSize(int oneoffTaskThreadPoolSize). Add or remove workers
	 * from the corresponding thread pool directly.
	 * 
	 * @since 27/05/2013
	 * It is not necessary to implement these two method.
	 * 
	 * */
	
	/*protected static void setMultiTaskThreadPoolSize(int multiTaskThreadPoolSize) {
		//ThreadPool.multiTaskThreadPoolSize = multiTaskThreadPoolSize;
		adjustThreadPool(ThreadPoolType.MULTI, multiTaskThreadPoolSize);
	}*/

	/*protected static void setOneoffTaskThreadPoolSize(int oneoffTaskThreadPoolSize) {
		//ThreadPool.oneoffTaskThreadPoolSize = oneoffTaskThreadPoolSize;
		adjustThreadPool(ThreadPoolType.ONEOFF, oneoffTaskThreadPoolSize);
	}*/

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
		//int oldSize = oneoffTaskWorkers.size();
		if (oneoffTaskThreadPoolSize == newSize) {
			return;
		}else if (oneoffTaskThreadPoolSize > newSize) {
			int diff = oneoffTaskThreadPoolSize - newSize;

			//since 31/05/2013
			//Do not create poison pills like this way, using new class of "LottoBox"
			/*PoisonPillCountDownLatch pDownLatch = new PoisonPillCountDownLatch(new CountDownLatch(diff));
			PoisonPillGenerator.createPoisonPills(oneoffTaskWorkers.size(), pDownLatch);*/

			LottoBox.setLotto(diff);
			
			reentrantLock.lock();
			
			for (Iterator<WorkerThread> iterator = oneoffTaskWorkers.values().iterator(); iterator.hasNext();) {
				WorkerThread workerThread = (WorkerThread) iterator.next();
				workerThread.requireCancel(true);
			}
			
			reentrantLock.unlock();
			
			oneoffTaskThreadPoolSize = oneoffTaskThreadPoolSize - diff;
			
			/*switch (ParaTask.getScheduleType()) {
			case WorkSharing:
			case MixedSchedule:
				SortedMap<Integer, WorkerThread> subWorkers = oneoffTaskWorkers;
				for (int i = 0; i < diff; i++) {
					subWorkers.get(subWorkers.lastKey()).setKilled(true);
					subWorkers = subWorkers.subMap(subWorkers.firstKey(), subWorkers.lastKey());
				}
				break;
			case WorkStealing:
				Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneoffTaskMap = ThreadPool.taskpool.getLocalOneoffTaskQueues();		

				TaskID<?> poisonPill = null;
				
				PoisonPillCountDownLatch pDownLatch = new PoisonPillCountDownLatch(new CountDownLatch(diff));
				
				for (int i = 0; i < diff; i++) {
					poisonPill = FakeTaskGenerator.createPoisonPill(pDownLatch);
					
					for (Iterator<LinkedBlockingDeque<TaskID<?>>> iterator = localOneoffTaskMap.values().iterator(); iterator.hasNext();) {
						LinkedBlockingDeque<TaskID<?>> localOneoffTaskDeque = (LinkedBlockingDeque<TaskID<?>>) iterator.next();
						localOneoffTaskDeque.addLast(poisonPill);
					}
				}
				TaskID<?> poisonPill = FakeTaskGenerator.createPoisonPills(diff);
				for (Iterator<LinkedBlockingDeque<TaskID<?>>> iterator = localOneoffTaskMap.values().iterator(); iterator.hasNext();) {
					LinkedBlockingDeque<TaskID<?>> localOneoffTaskDeque = (LinkedBlockingDeque<TaskID<?>>) iterator.next();
					localOneoffTaskDeque.addLast(poisonPill);
				}
				break;		
			default:
				break;
			}*/
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
	 * @Author : Kingsley
	 * @since : 26/04/2013
	 * 
	 * Pool size for multi task worker thread pool and pool size for one-off task
	 * worker thread pool could be re-calculated several times 
	 * 
	 * @since : 18/05/2013
	 * Originally this method is only used for initialization
	 * Cancel this method, whenever the pool size is wanted, get it directly from 
	 * the collection size.
	 * 
	 * */
	
	/*private static void calEachPoolSize(int poolsize){
		multiTaskThreadPoolSize = poolSize/2;
		oneoffTaskThreadPoolSize = poolSize-multiTaskThreadPoolSize;
	}*/
	
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
			//taskReallocation(threadID);
			
			reentrantLock.unlock();
		}
	}
	
	/**
	 * 
	 * @author Kingsley
	 * @since 18/05/2013
	 * 
	 * After remove a worker out from thread pool, re-allocate its un-finished jobs if necessary. 
	 * */
	
	@SuppressWarnings("unused")
	private static void taskReallocation(int threadID) {/*
		Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneoffTaskMap = ThreadPool.taskpool.getLocalOneoffTaskQueues();		

		LinkedBlockingDeque<TaskID<?>> queue = localOneoffTaskMap.get(threadID);

		if (queue.isEmpty()) {
			return;
		}else {
			LinkedBlockingDeque<TaskID<?>>[] aliveQueues = localOneoffTaskMap.values().toArray(new LinkedBlockingDeque[localOneoffTaskMap.size()]);
			int index = 0;
			for (Iterator iterator = queue.iterator(); iterator.hasNext(); index++) {
				TaskID<?> taskID = (TaskID<?>) iterator.next();
				if (index == aliveQueues.length) {
					index = 0;
				}
				aliveQueues[index].addFirst(taskID);
			}
		}
	*/}

	/**
	 * 
	 * @Author Kingsley
	 * @since 11/05/2013
	 * 
	 * Help to create a fake task
	 * 
	 * @since 23/05/2013
	 * Change from "createPoisonPill()" to "createPoisonPills(PoisonPillCountDownLatch count)"
	 * 
	 * @return TaskID<?>
	 * */
	final static class FakeTaskGenerator{/*
		private static TaskID<?> createPoisonPill(PoisonPillCountDownLatch count) {
			TaskInfo taskInfo = new TaskInfo();
			
			PoisonPill pill = new PoisonPill(count);
			Method tryWait = null;
			try {
				tryWait = pill.getClass().getDeclaredMethod("tryWait", new Class[]{});
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			
			taskInfo.setParameters();
			taskInfo.setInstance(pill);
			taskInfo.setMethod(tryWait);
			//set as subtask so it will not be allocated a global id
			taskInfo.setSubTask(true);
			
			return new TaskID(taskInfo);
		}
	*/}
	
	
	/**
	 * 
	 * @Author Kingsley
	 * @since 24/05/2013
	 * @return PoisonPill
	 * 
	 * Help to create a poison pill
	 * 
	 * */
	final static class PoisonPillGenerator{/*
		private static void createPoisonPills(int number, PoisonPillCountDownLatch count) {
			for (int i = 0; i < number; i++) {
				PoisonPillBox.addPill(new PoisonPill(count));
			}
		}
	*/}

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
	
	/**
	 * 
	 * @Author Kingsley
	 * @since 11/05/2013
	 * 
	 * The class is used to simulate killing threads.
	 * At current stage, only one-off task worker threads can be killed.
	 * The idea is creating a fake task, the first one-off task worker threads will stop working.
	 * 
	 * For workstealing policy, insert the poison pill to every local queues.
	 * The first worker thread whose local queue goes to empty will get the poison pill, then
	 * stop working.
	 * 
	 * @since 16/05/2013
	 * For worksharing and mixedschedule policy, do not really need poison pill. 
	 * First find the worker thread who owns the highest thread id, then ask it to stop working.
	 * 
	 * @since 23/05/2013
	 * Instead of adding fake task to the end of the local queue, adding it to the front of the queue.
	 * Then worker thread can find it ASAP.
	 * 
	 * @since 24/05/2013
	 * The new idea is create a flag for worker threads when trying to kill some.
	 * Meanwhile, create poison pills which share a common count down latch for each thread.
	 * 
	 * We create poison pills but do not create fake tasks because
	 * 1, we do not want to use task queues
	 * 2, we want to treat different scheduling types equally
	 * 3, if for some reasons, user call setThreadPoolSize() twice like this:
	 *           {
	 * 				......
	 * 				setThreadPoolSize(2);
	 * 				......
	 * 				setThreadPoolSize(1);
	 * 				......
	 * 
	 * 			 }
	 * 	  we can create pills twice which share different count down latches as an approach
	 * 
	 * @since 31/05/2013
	 * Simplify the implementation of reducing thread number.
	 * */
	
	/*final static class Executor extends TimerTask {
		@Override
		public void run() {
			switch (ParaTask.getScheduleType()) {
			case WorkSharing:
			case MixedSchedule:
				oneoffTaskWorkers.get(oneoffTaskWorkers.lastKey()).setKilled(true);
				break;
			case WorkStealing:
				PoisonPillCountDownLatch pDownLatch = new PoisonPillCountDownLatch(new CountDownLatch(1));
				
				TaskID<?> poisonPill = FakeTaskGenerator.createPoisonPill(pDownLatch);
				
				Map<Integer, LinkedBlockingDeque<TaskID<?>>> localOneoffTaskMap = ThreadPool.taskpool.getLocalOneoffTaskQueues();		

				for (Iterator<LinkedBlockingDeque<TaskID<?>>> iterator = localOneoffTaskMap.values().iterator(); iterator.hasNext();) {
					LinkedBlockingDeque<TaskID<?>> localOneoffTaskDeque = (LinkedBlockingDeque<TaskID<?>>) iterator.next();
					//localOneoffTaskDeque.addLast(poisonPill);
					localOneoffTaskDeque.addFirst(poisonPill);
				}
				
				break;
			
			default:
				break;
			}
		
			
			//since 31/05/2013
			//Do not create poison pills like this way, using new class of "LottoBox"
			PoisonPillCountDownLatch pDownLatch = new PoisonPillCountDownLatch(new CountDownLatch(1));
			PoisonPillGenerator.createPoisonPills(oneoffTaskWorkers.size(), pDownLatch);
			LottoBox.setLotto(1);
			
			for (Iterator<WorkerThread> iterator = oneoffTaskWorkers.values().iterator(); iterator.hasNext();) {
				WorkerThread workerThread = (WorkerThread) iterator.next();
				workerThread.requireCancel(true);
				
			}
		}
	}
*/
 }


