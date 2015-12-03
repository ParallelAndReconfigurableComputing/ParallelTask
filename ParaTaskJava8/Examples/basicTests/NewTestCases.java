package benchmarks.basicTests;

import java.util.concurrent.atomic.AtomicInteger;

public class NewTestCases {
	static int count = 5;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//int count = 5;
		count++;
		
		
		InterfaceUser< Void, Void> user = new InterfaceUser<Void, Void>(()->foo());
		count++;
		//System.out.println(user.execute());
		user.execute();
		//@Future(notify="ddklfs()",dependsOn=id3, notifyInterim="updateCount()")
		//int i = foo(count); --> TaskID __i__
		//int b = foo1(i); --> TaskID __b__.dependsOn(__i__);
		System.out.println(count);

//		AtomicInteger count = new AtomicInteger(5);
//		count.incrementAndGet();
//		InterfaceUser< Integer, AtomicInteger> user = new InterfaceUser<Integer, AtomicInteger>((AtomicInteger x)->x.incrementAndGet());
//		System.out.println(user.execute(count));
//		System.out.println(count);
	}
	
	//@Task
	public static void foo(){
		
		/*I want count to be updated on GUI*/
		for (int i = 0; i<5; i++) {
			count++;
			//@publish
			updateCount();  // --> ParaTask.publish(CurrentTask.getCurrentTask(), "updateCount()");
			
			/*
			 * Runnable runnable = new Runnable() {
			 * 		 public void run() {
			 * 			updateCount();
			 * 		}
			 * }
			 * SU.invokeLater(runnable);
			 * 
			 */
			
		}
	}
	
	//process function
	public static void updateCount(){
		//GUIThread.invokeLater(); -- not
	}

}
