/**
 * 
 */
package pt.test;

import pt.runtime.ParaTask;

/**
 * @author hp
 *
 */
public class TestMainMethod {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (null == args || args.length != 4) {
			try {
				throw new Exception("Wrong arguemnts setting");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//ParaTask.setThreadPoolSize(Integer.valueOf(args[3]));
		
		new TestCase(args[0], args[1], args[2]).startTest();
	}

}
