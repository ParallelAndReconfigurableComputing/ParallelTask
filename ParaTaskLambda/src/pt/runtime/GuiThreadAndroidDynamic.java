package pt.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * This class provides a proxy to Android GUI thread,
 * through which ParaTask framework retrieves information
 * about the Android GUI thread.
 * 
 * @author Mostafa Mehrabi
 * @since  18/8/2015
 * */
public class GuiThreadAndroidDynamic implements GuiThreadProxy {

	private GuiThreadAndroidDynamic() {
		this.init();
	}

	private static GuiThreadAndroidDynamic instance;

	public static GuiThreadAndroidDynamic getInstance() {
		if (instance == null)
			instance = new GuiThreadAndroidDynamic();
		return instance;
	}

	private static Class<?> handlerClass;
	private static Object handler;
	private static Method handlerPostMethod;

	private static Thread mainThread;

	public void init() {
		if (isEventDispatchThread()){
			System.out.println("ERROR: PARATASK CANNOT BE INITIALIZED BY THE EVENT DISPATCH THREAD! \n"
					+ "TRY USING \"ParaTask.init()\" AT THE BEGINNING OF THE \"main( )\" METHOD!");
			System.out.println("------------------------------------- TERMINATING THE PROGRAM -------------------------------------");
			System.exit(0);
		}
		
		try {			
				//gets a method object that reflects "android.os.Looper.getMainLooper()"
				Class<?> looperClass = Class.forName("android.os.Looper");
				Method getMainLooperMethod = looperClass.getMethod("getMainLooper");
			
				//invokes method "looperClass.getMainLooperMethod();"
				Object mainLooper = getMainLooperMethod.invoke(looperClass);
			
				handlerClass = Class.forName("android.os.Handler");
				Constructor<?> handlerCtor = handlerClass.getDeclaredConstructor(looperClass);
				
				
				//gets a handler (i.e., and instance of android Handler class) by effectively calling
				//handler = new Handler(LooperClass.getMainLooperMethod());
				handler = handlerCtor.newInstance(mainLooper);

				Method getThreadMethod = looperClass.getMethod("getThread");
				mainThread = (Thread)getThreadMethod.invoke(mainLooper);
		} catch (Exception e) {
			// fatal error
			throw new RuntimeException(e);
		}
	}
	
	public Thread getEventDispatchThread() {
		return mainThread;
	}

	public boolean isEventDispatchThread() {
		return Thread.currentThread().equals(mainThread);
	}

	public void invokeLater(Runnable r) {
		try {
			if (handlerPostMethod == null) {
				handlerPostMethod = handlerClass.getMethod("post", Runnable.class);
			}
			handlerPostMethod.invoke(handler, r);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}