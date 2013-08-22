/**
 * 
 */
package paratask.test;

import paratask.runtime.CurrentTask;

/**
 * @author hp
 * 
 */
public class _TestCancel {
	public void compute() {
		for (int i = 0; i < Integer.MAX_VALUE && !CurrentTask.cancelRequested(); i++) {
			System.out.println(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
