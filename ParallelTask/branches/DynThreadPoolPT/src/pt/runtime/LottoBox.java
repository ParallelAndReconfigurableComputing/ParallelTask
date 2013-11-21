/**
 * 
 */
package pt.runtime;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Kingsley
 * @since 31/05/2013
 * 
 * @since 01/10/2013
 * Remove all the unnecessary code before merging.
 *
 */
public class LottoBox {
	protected static AtomicInteger lottoNum = new AtomicInteger(0);
	
	private final static ReentrantLock reentrantLock = new ReentrantLock();
	
	protected static void tryLuck(){
		while (true) {
			if (reentrantLock.tryLock()) {
				WorkerThread workerThread = (WorkerThread) Thread.currentThread();
				if (lottoNum.get() > 0) {
					//Inform Thread Pool
					ThreadPool.lastWords(workerThread.isMultiTaskWorker(), workerThread.getThreadID());
					
					//Count down the latch
					lottoNum.decrementAndGet();
				
					//Set it as a cancelled thread
					workerThread.setCancelled(true);
				}else {
					//No chance to die
					workerThread.requireCancel(false);
				}
				reentrantLock.unlock();
				break;
			}else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected static void setLotto(int delta){
		lottoNum.addAndGet(delta);
	}
}