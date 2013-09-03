/**
 * 
 */
package pt.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Kingsley
 * @since 11/05/2013
 *
 * This class represents a fake task, which is used to kill thread
 * 
 * @since 16/05/2013
 * Do not use wait() anymore when the thread get the lock,
 * Set the variable "isKilled" to true, the thread will not pick up another task.
 * 
 * Add a condition here to check if the worker thread who get the lock, its 
 * local queue is empty. 
 * If not empty, re-insert this fake task back to the queue.
 * If is empty, stop working
 * 
 * @since 23/05/2013
 * Change the constructor from default to "PoisonPill(int num)"
 * Change the field of "counter" from the type of AtomicInteger to PoisonPillCountDownLatch
 * Add a new constructor
 * Add a new HashMap in order to record which worker thread actually take the fake task.
 */
public class PoisonPill {
	
	private final ReentrantLock reentrantLock = new ReentrantLock();
	
	//private static AtomicInteger counter = new AtomicInteger(1);
	
	private PoisonPillCountDownLatch count;

	public PoisonPill(PoisonPillCountDownLatch count) {
		this.count = count;
	}
	
	private static Map<Integer, WorkerThread> grave = new HashMap<Integer, WorkerThread>();

	/**
	 * @author Kingsley
	 * @since 11/05/2013
	 * Fake task with the real work
	 * 
	 * @author Kingsley
	 * @since 23/05/2013
	 * 
	 * The worker thread has already get the lock, count down the latch anyway.
	 * 
	 * Examine this worker thread is waiting other tasks or not. 
	 * 
	 * If it is, force it goes to sleep for a while, 
	 *           re-insert the poison back to the front of the queue,
	 *           inform main thread that this worker thread will be killed anyway, do not give it more tasks,
	 *           count down the latch,
	 *           AND add it into the killing list in order to prevent it from counting down latch more times.
	 * 
	 * If it is not, check it this thread exists in the killing list
	 * 		If it is, set it as a killed thread.
	 * 		It it is not, inform main thread that this worker thread will be killed now, do not give it more tasks,
	 *                    count down the latch,
	 *                    set it as a killed thread.
	 *               
	 * If the worker thread can not get the lock, check the count value first.
	 * 
	 * 		If the count > 0,  re-insert the poison back to the front of the queue
	 * 		If the count = 0,  check if the thread is in the killing list
	 * 				
	 * 				If it is in the killing list, re-insert the poison back to the front of the queue
	 * 				If it is not, drop the task.
	 * 
	 * 
	 * @since 24/05/2013
	 * @deprecated
	 * 
	 * Cannot be used anymore. Should be cancelled later at the release version.
	 * 
	 * @since 25/05/2013
	 * "workerThread.isWaiting()" is cancelled. Change to make the file could be correctly compiled.
	 * */
	
	protected void tryWait(){
		System.out.println(((WorkerThread) Thread.currentThread()).getThreadID() + " get fake task");
		
		if (reentrantLock.tryLock()) {
			
			WorkerThread workerThread = (WorkerThread) Thread.currentThread();
			
			System.out.println(workerThread.getThreadID() + " get lock");
			
			LinkedBlockingDeque<TaskID<?>> localDeque = workerThread.taskpool.getLocalOneoffTaskQueues().get(workerThread.getThreadID());

			/*if (workerThread.isWaiting()) {
				//Force to sleep
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//Re-insert fake task back
				localDeque.addFirst(workerThread.currentExecutingTask());
				
				//Check killing map first
				if (!grave.containsKey(workerThread.getThreadID())) {
					//Inform Thread Pool
					ThreadPool.lastWords(workerThread.isMultiTaskWorker(), workerThread.getThreadID());
					//Count down the latch
					count.getCountDownLatch().countDown();
					//Add to the killing list
					grave.put(workerThread.getThreadID(), workerThread);
				}
			} else {
				if (!grave.containsKey(workerThread.getThreadID())) {
					//Inform Thread Pool
					ThreadPool.lastWords(workerThread.isMultiTaskWorker(), workerThread.getThreadID());
					//Count down the latch
					count.getCountDownLatch().countDown();	
				}
				//Set it as a killed thread
				//workerThread.setKilled(true);
				workerThread.setPoisoned(true);
				
				System.out.println(workerThread.getThreadID() + " set killed");
			}*/
			
			
			
			/*if (workerThread.currentTaskStack.size() != 1) {
				localDeque.addLast(workerThread.currentExecutingTask());
			}else {
				if (counter.get() != 1) {
					return ;
				}
				
				synchronized (this) {
					try {
						counter.set(0);
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				workerThread.setKilled(true);
				
				counter.set(0);
				
				ThreadPool.lastWords(workerThread.isMultiTaskWorker(), workerThread.getThreadID());
			}*/
		}else{
			/*if (counter.get() == 1) {
				WorkerThread workerThread = (WorkerThread) Thread.currentThread();
				LinkedBlockingDeque<TaskID<?>> localDeque = workerThread.taskpool.getLocalOneoffTaskQueues().get(workerThread.getThreadID());
				localDeque.addLast(workerThread.currentExecutingTask());
			}*/
			
			WorkerThread workerThread = (WorkerThread) Thread.currentThread();
			LinkedBlockingDeque<TaskID<?>> localDeque = workerThread.taskpool.getLocalOneoffTaskQueues().get(workerThread.getThreadID());
			
			if (count.getCountDownLatch().getCount() > 0 || grave.containsKey(workerThread.getThreadID())) {
				localDeque.addFirst(workerThread.currentExecutingTask());
			}
			
			System.out.println(workerThread.getThreadID() + " get no lock");
		}
	}
	
	/**
	 * 
	 * @author Kingsley
	 * @since 25/05/2013
	 * 
	 * Though it is not necessary to distinguish non-nested and nested situation.
	 * 
	 * For non-nested situation, this method will set "isKilled" flag, in order to kill the worker thread permanently.
	 * 
	 * When trying execute this method, the procedure should be 
	 * 1. must get the lock, if not, wait for a while.
	 * 
	 * */
	
	protected void tryKill(){
		while (true) {
			if (reentrantLock.tryLock()) {
				WorkerThread workerThread = (WorkerThread) Thread.currentThread();
				
				if (count.getCountDownLatch().getCount() > 0) {
					//Inform Thread Pool
					ThreadPool.lastWords(workerThread.isMultiTaskWorker(), workerThread.getThreadID());
					
					//Count down the latch
					count.getCountDownLatch().countDown();	
				
					//Set it as a killed thread
					//workerThread.setKilled(true);
					workerThread.setPoisoned(true);
					//System.out.println(workerThread.threadID + "get poison pill");
				}else {
					//No chance to die
					//Think about how to set requireCancel(false);
					//workerThread.requireCancel(false);
				}
				
				break;
			}else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
