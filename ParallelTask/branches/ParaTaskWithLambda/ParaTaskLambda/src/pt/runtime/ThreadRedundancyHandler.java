/**
 * 
 */
package pt.runtime;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadRedundancyHandler {
	/*
	 * <code>numberOfRedundantThreads</code> stands for the number of <b>poisoned</b> threads that a 
	 * specific <code>ThreadPool</code> (especially the <code>OneOff-task</code> thread pool)
	 * knows about. A poisoned thread is a thread that is requested to <i>cancel</i> <b>but is not </b>
	 * <i>cancelled</i> yet! A thread is normally poisoned as a result of changes in the size of 
	 * <code>ThreadPool</code>!
	 * 
	 * @author Mostafa Mehrabi
	 * @since  16/9/2014
	 * */
	private static AtomicInteger numberOfRedundantThreads = new AtomicInteger(0);
	
	private final static ReentrantLock reentrantLock = new ReentrantLock();
	
	/*
	 * This method informs the thread pool that it can NOW remove a redundant thread from its 
	 * corresponding pool, as the thread has finished its current task.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  15/9/2014
	 * */
	protected static void informThreadPool(){
		while (true) {
			if (reentrantLock.tryLock()) {
				WorkerThread workerThread = (WorkerThread) Thread.currentThread();
				if (numberOfRedundantThreads.get() > 0) {
					//Inform Thread Pool
					ThreadPool.removeThreadFromPool(workerThread.isMultiTaskWorker(), workerThread.getThreadID());
					
					//Count down the latch
					numberOfRedundantThreads.decrementAndGet();
				
					//Set it as a cancelled thread
					workerThread.setCancelled(true);
				}else {
					//No chance to die
					workerThread.requestCancel(false);
				}
				reentrantLock.unlock();
				return;
			}else {
				try {
					Thread.sleep(ParaTask.WORKER_SLEEP_DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * This method is normally called by a <code>Thread Pool</code> (especially <code>OneOff-task</code> thread pools), as
	 * a result of the size of that <code>Thread Pool</code> shrinking, which will consequently cause some of the redundant
	 * threads to be <u>poisoned</u>. This method adds the number of redundant threads to <code>delta</code>
	 * 
	 * @see #numberOfRedundantThreads
	 * @author Mostafa Mehrabi
	 * @since  16/9/2014
	 * */
	protected static void setNumberOfRedundantThreads(int delta){
		numberOfRedundantThreads.addAndGet(delta);
	}
}