package paratask.runtime;

import android.os.Handler;
import android.os.Looper;

public class GuiThreadAndroid implements GuiThreadProxy {

	private GuiThreadAndroid() {
	}

	private static GuiThreadAndroid instance;

	public static GuiThreadAndroid getInstance() {
		if (instance == null)
			instance = new GuiThreadAndroid();
		return instance;
	}

	private static Handler handler;
	private static Thread mainThread;

	public void init() {
		Looper mainLooper = Looper.getMainLooper();
		handler = new Handler(mainLooper);
		mainThread = mainLooper.getThread();
	}
	
	public Thread getEventDispatchThread() {
		return mainThread;
	}

	public boolean isEventDispatchThread() {
		if (Looper.myLooper() == null)
			return false;
		return Looper.myLooper() == Looper.getMainLooper();
	}

	public void invokeLater(Runnable r) {
		handler.post(r);
	}
}