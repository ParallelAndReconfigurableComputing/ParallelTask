/**
 * 
 */
package pt.runtime;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Kingsley
 * @since 24/05/2013
 * 
 * Use a linked blocking queue to hold poison pills
 *
 */
public class PoisonPillBox {
	private static LinkedBlockingQueue<PoisonPill> pillqueue = new LinkedBlockingQueue<PoisonPill>();
	
	protected static void addPill(PoisonPill pill){
		pillqueue.add(pill);
	}
	
	protected static PoisonPill getPill(){
		return pillqueue.poll();
	}
}
