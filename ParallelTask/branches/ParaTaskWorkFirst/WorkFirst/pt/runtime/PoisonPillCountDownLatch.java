/**
 * 
 */
package pt.runtime;

import java.util.concurrent.CountDownLatch;

/**
 * @author Kingsley
 * @since 23/05/2013
 * 
 * Used for counting how many times a poison pill has been used.
 *
 */
public class PoisonPillCountDownLatch {
	private CountDownLatch countDownLatch;

	public PoisonPillCountDownLatch(CountDownLatch countDownLatch) {
		super();
		this.countDownLatch = countDownLatch;
	}

	protected CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

}
