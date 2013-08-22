/*
 *  Copyright (C) 2010 Nasser Giacaman, Oliver Sinnen
 *
 *  This file is part of Parallel Task.
 *
 *  Parallel Task is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or (at 
 *  your option) any later version.
 *
 *  Parallel Task is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along 
 *  with Parallel Task. If not, see <http://www.gnu.org/licenses/>.
 */

package paratask.runtime;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Thread-specific event loop. Any user-defined thread that makes use of the ParaTask clauses 
 * <code>notify</code>, <code>asyncCatch</code>, or <code>notifyInterim</code> must make use of 
 * this <code>EventLoop</code>. This also includes the main thread (the thread that invokes the 
 * application's <code>main</code> method). This requirement is necessary because such threads
 * do not have an event loop of their own, yet the above ParaTask clauses involve event-based callbacks.  
 * <br><br>
 * ParaTask threads (i.e. any code executed inside a ParaTask task), and Java's Event Dispatch Thread (EDT) 
 * (i.e. any code executed inside handlers such <code>actionPerformed</code>, etc.) must not use
 * this <code>EventLoop</code> since they already have an event loop of their own. However, in order to 
 * initialise these event loops, the programmer is required to call {@link ParaTask#init()} at the 
 * start of the <code>main</code> method of their application.
 * <br><br>
 * Note that if any thread has nothing to compute, then that thread will terminate. In particular, this means that if
 * the main thread (the thread that executes the <code>main</code> method) returns from the <code>main</code> method, then essentially the 
 * application ends: any other running threads will also be terminated. For example, if the the main thread uses a 
 * <code>notifyGUI</code> clause, then the EDT might not have a chance to execute the clause and the also the worker 
 * threads themselves might not be able to complete the tasks since the application terminates when the main thread
 * terminates. To avoid this, programmers should invoke {@link ParaTask#init()} early in the <code>main</code> method, and then 
 * invoke {@link EventLoop#exec()} at the end of the <code>main</code> method so that the application remains active. 
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 *
 */
public class EventLoop {

	static private ConcurrentHashMap<Thread, TaskListener> listeners = new ConcurrentHashMap<Thread, TaskListener>();
	
	static Thread EDT = null;		// a reference to the EDT
	
	static private SlotHandlingThread sht = null; 
	static private TaskListener slotHandlingThreadTaskLister = null;
	
	EventLoop() {
		
	}
	
	/*
	 * This will register the EDT and creates a special TaskListener for it.
	 * Users don't need to call this, since ParaTask.init() will do this. 
	 */
	static void init() {
		
		//-- Create the task pool
		TaskpoolFactory.getTaskpool();
		
		//-- initial the slot handling thread
		sht = new SlotHandlingThread();
		register(sht);
		sht.setDaemon(true);
		sht.start();
		slotHandlingThreadTaskLister = getTaskListener(sht);
		
		//-- initialise the EDT
		EDT = GuiThread.getEventDispatchThread();
		// create a special task listener for the EDT
		listeners.putIfAbsent(EDT, new EDTTaskListener());
	}
	
	/**
	 * Register a user-defined thread (or the <code>main</code> thread) with ParaTask. See class 
	 * description above for more details. The thread should call this method <u>before</u> it 
	 * invokes any tasks with ParaTask clauses, and before it enters the event loop.
	 * 
	 * @see #exec()
	 * @return	<code>true</code> if this thread is not registered already, otherwise <code>false</code> if already registered.
	 * 
	 * @throws UnsupportedOperationException	If attempting to register a ParaTask thread or the EDT
	 */
	static public boolean register() {
		Thread curThread = Thread.currentThread();
		if (ParaTaskHelper.isSubClassOf(curThread.getClass(), TaskThread.class)) {
			throw new UnsupportedOperationException("Attempt to call EventLoop.register() from a ParaTask TaskThread, but these" +
					" threads already have their own event loop!");
		} else if (GuiThread.isEventDispatchThread()) {
			throw new UnsupportedOperationException("Attempt to call EventLoop.quit() from a Java's EDT but this" +
					" thread already has its own event loop!");
		} else {
			return register(curThread);			
		}
	}
	
	static private boolean register(Thread thread) {
		if (listeners.containsKey(thread))
			return false;
		
		TaskListener ta = new TaskListener();
		
		// only succeeds if didn't have a TaskListener before
		TaskListener currentListener = listeners.putIfAbsent(thread, ta);
		return currentListener==null;
	}
	
	/*
	 * Returns the TaskListener that is registered for the argument thread (NOT the current thread)
	 * 
	 * @param thread	The Thread for which to get the TaskListener for
	 * @return			The TaskListener for the argument thread, or null if the specified Thread is null or not registered
	 */
	
	static TaskListener getTaskListener(Thread thread) {
		TaskListener listener = listeners.get(thread);
		if (listener == null && !(thread instanceof TaskThread))
			throw new ParaTaskRuntimeException("\n\tOne of 2 solutions: \n\t(i)  Please ensure the main() method of your application calls ParaTask.init() near the start. " +
					"\n\t(ii) In addition, any other thread (that you have defined) that uses any of the ParaTask notify clauses must " +
					"\n\tregister with the ParaTask runtime by calling EventLoop.register(). If this is the case, the thread that " +
					"needs \n\tregistering is \""+thread+"\", whose ID is "+thread.getId()+".\n");
		return listener;
	}
	
	static TaskListener getEDTTaskListener() {
		if (EDT == null) {
			throw new RuntimeException("Please call ParaTask.init() early in the main method of your application!");
//			ParaTask.init();
		}
		return getTaskListener(EDT);
	}
	
	static TaskListener getCurrentThreadTaskListener() {
		//-- Sometimes the EDT sometimes changes to another thread instance. Therefore use the one initialised at the beginning
		if (GuiThread.isEventDispatchThread())
			return getTaskListener(EDT);
		return getTaskListener(Thread.currentThread());
	}
	
	/**
	 * The calling thread enters the event loop. The thread should be registered before calling this method.
	 * 
	 * The calling thread stays listening to tasks until the <code>quit()</code> method is called.
	 * 
	 * @return	Returns 0 if no error occurs. 
	 * @see		#register()
	 * @see		#quit()
	 * @throws UnsupportedOperationException	If executed by a ParaTask thread or the EDT
	 */
	static public int exec() {
		// TODO error codes not implemented yet
		
		if (ParaTaskHelper.isSubClassOf(Thread.currentThread().getClass(), TaskThread.class)) {
			throw new UnsupportedOperationException("Attempt to call EventLoop.exec() from a ParaTask TaskThread, but these" +
					" threads already have their own event loop!");
		} else if (GuiThread.isEventDispatchThread()) {
			throw new UnsupportedOperationException("Attempt to call EventLoop.exec() from a Java's EDT but this" +
					" thread already has its own event loop!");
		} else {
			return getCurrentThreadTaskListener().exec();		
		}
	}
	
	/**
	 * Stops the event loop for the calling thread.
	 * @see		#exec()
	 * @see		#register()
	 * @throws UnsupportedOperationException	If executed by a ParaTask thread or the EDT
	 */
	static public void quit() {
		if (ParaTaskHelper.isSubClassOf(Thread.currentThread().getClass(), TaskThread.class)) {
			throw new UnsupportedOperationException("Attempt to call EventLoop.quit() from a ParaTask TaskThread! If the enclosing " +
					"method is used in a notify or asyncCatch clause, ensure that the registering thread is not a ParaTask TaskThread when " +
					"using EventLoop.quit().");
		} else if (GuiThread.isEventDispatchThread()) {
			throw new UnsupportedOperationException("Attempt to call EventLoop.quit() from a Java's EDT! If the enclosing " +
					"method is used in a notify or asyncCatch clause, ensure that the registering thread is not the EDT when " +
					"using EventLoop.quit().");
		} else {
			getCurrentThreadTaskListener().executeSlots(Slot.quit);			
		}
		
	}

	static void executeOnSlotHandlingThread(Slot slot) {
		slotHandlingThreadTaskLister.executeSlots(slot);
	}
}
