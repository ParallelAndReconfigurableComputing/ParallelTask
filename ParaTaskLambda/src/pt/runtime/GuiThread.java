package pt.runtime;

/**
 * This class instantiates an instance of a GUI thread proxy,
 * based on the underlying virtual machine. It is through this
 * class that ParaTask framework retrieves information about
 * the current GUI thread. 
 * 
 * @author Mostafa Mehrabi
 * @since  18/8/2015
 * */
public class GuiThread {

	private static GuiThreadProxy proxy;
	
	private static boolean initialized = false;
	
	public static void init() {
		if (initialized)
			return;
		
		String vmName = System.getProperty("java.vm.name");
		if (vmName.equals("Dalvik")) {
			proxy = GuiThreadAndroidDynamic.getInstance();
		} else {
			proxy = GuiThreadSwing.getInstance();
		}
	
		initialized = true;
	}
	
	/**
	 * Returns the current GUI (i.e., EventDispatch) thread of the system.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  18/8/2015
	 * */
	public static Thread getEventDispatchThread() {
		return proxy.getEventDispatchThread();
	}

	/**
	 * This method indicates if the <b>current thread</b> is the 
	 * <code>EventDispatchThread</code>, and therefore it is 
	 * the GUI thread.
	 * 
	 * @return <code>true</code> if the current thread is the <code>EventDispatchThread</code>
	 * , otherwise it returns <code>false</code>.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  18/8/2015
	 * */
	public static boolean currentThreadIsEventDispatchThread() {
		return proxy.isEventDispatchThread();
	}

	/**
	 * Enqueues a handler method/task to be executed by the <code>EventDispatchThread</code>,
	 * and allows the enqueueing thread to carry on doing other jobs. 
	 * 
	 * @param r Runnable object that represents the method/task.  
	 * 
	 * @author Mostafa Mehrabi
	 * @since  19/8/2015
	 * 
	 * */
	public static void invokeLater(Runnable r) {
		proxy.invokeLater(r);
	}
}
