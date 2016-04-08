package pt.runtime;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class GuiThreadSwing implements GuiThreadProxy {

	private GuiThreadSwing() {
		this.init();
	}

	private static GuiThreadSwing instance;

	public static GuiThreadSwing getInstance() {
		if (instance == null)
			instance = new GuiThreadSwing();
		return instance;
	}
	
	private Thread edt;
	
	public void init() {
		if (isEventDispatchThread()){
			System.out.println("ERROR: PARATASK CANNOT BE INITIALIZED BY THE EVENT DISPATCH THREAD! \n"
					+ "TRY USING \"ParaTask.init()\" AT THE BEGINNING OF THE \"main( )\" METHOD!");
			System.out.println("------------------------------------- TERMINATING THE PROGRAM -------------------------------------");
			System.exit(0);
		}
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					//inside invoke and wait, to make sure EDT will implement it
					edt = Thread.currentThread();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Thread getEventDispatchThread() {
		return edt;
	}

	public boolean isEventDispatchThread() {
		return SwingUtilities.isEventDispatchThread();
	}

	public void invokeLater(Runnable r) {
		SwingUtilities.invokeLater(r);
	}
}
