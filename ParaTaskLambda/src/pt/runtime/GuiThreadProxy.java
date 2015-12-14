package pt.runtime;

/**
 * An interface that defines the services that the proxies of
 * different virtual machine GUI threads should provide for
 * the ParaTask framework.
 * 
 * @author Mostafa Mehrabi
 * @since  18/8/2015
 * */
public interface GuiThreadProxy {
	void init();
	Thread getEventDispatchThread();
	boolean isEventDispatchThread();
	void invokeLater(Runnable r);
}
