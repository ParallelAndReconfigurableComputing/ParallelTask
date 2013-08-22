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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class TaskListener {
	
	private LinkedBlockingQueue<Slot> notifyQueue = new LinkedBlockingQueue<Slot>();
	
	private boolean alreadyInsideEventLoop = false;
	
	public int exec() {
		if (alreadyInsideEventLoop)
			return -1;
		alreadyInsideEventLoop = true;
		
//		System.err.println("----- Thread "+Thread.currentThread().getId()+
//				" ("+Thread.currentThread()+") will now enter EventLoop ");
		while (true) {
			try {
				Slot slot = notifyQueue.take();
				
				// this Slot is requesting to end the event loop
				if (slot == Slot.quit) {
//					System.err.println("**** Thread "+Thread.currentThread().getId()+
//							" ("+Thread.currentThread()+") will now exit the ParaTask EventLoop ");
					return 0;
				}
				
				if (slot.executeOnEDT()) 
					executeSlotOnEDT(slot);		// gets executed on EDT using invokeLater(), success is set within this call
				else
					executeSlot(slot);	// gets executed on current thread
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				return -1;
			}
		}
	}
	
	/**
	 * Executes the specified slot. If an exception occurs while running the slot, this is stored in the slot.
	 * 
	 * @return	true if task executed successfully, false otherwise
	 */
	private void executeSlot(Slot slot) {
		Method method = slot.getMethod();
		int numArgs = method.getParameterTypes().length;
		TaskID<?> taskID = slot.getTaskID();
		Object interResult = null;
		if (slot.isIntermediateResultSlot())
			interResult = slot.getNextIntermediateResultValue();
		
		Object instance = slot.getInstance();
		
		try {
			
			// first check if this slot can be accessed from here...

			if (Modifier.isPublic(method.getModifiers())) {
				
				if (slot.isASetCompleteSlot()) {
					//-- invoke ParaTaskHelper.setComplete(instance).. where 'instance' is 
					//--   the actual TaskID whose (non-public) setComplete() we want to execute  
					method.invoke(null, instance);
				} else {
				
					// the only argument a slot can have (if any) is a single TaskID that represents the task that completed
					if (numArgs == 2)
						method.invoke(instance, taskID, interResult);
					else if (numArgs == 1)
						method.invoke(instance, taskID);
					else
						method.invoke(instance);
					
				}
			} else {
				try {
//					System.out.println("me = "+method);

//					System.err.println(slot.getInstance());
//					System.err.println("--");
//					System.err.println(slot.getInstance().getClass());
//					System.err.println("--");
//					System.err.println(slot.getMethod());
//					System.err.println("--");
//					System.err.println(slot.getMethod().getName());
//					System.err.println("--");
//					System.err.println(slot.getTaskID());
//					System.err.println("**");
					
					Method opener = method.getDeclaringClass().getMethod("__pt__accessPrivateSlot", new Class[] { Method.class, Object.class, TaskID.class, Object.class });
					
//					System.err.println("opener="+opener);
//					System.err.println("method="+method);
//					System.err.println("instance="+instance);
//					System.err.println("taskID="+taskID);
//					System.err.println("interResult="+interResult);
//					System.err.println("interResult.getClass()="+interResult.getClass());
					
					if (instance == null && Modifier.isStatic(method.getModifiers()))
						throw new RuntimeException("Cannot use private static methods in clause: "+method);
					
					/* invoke as static or on the instance??   invoking static opener 
					 * invoking as static, incase of private static methods..
					 * 
					 * currently cannot use static opener (for nested classes)
					 * cannot use private static slots too (since it still requires an instance of the 
					 * enclosing class to call that static method)
					 * */
					opener.invoke( instance, method, instance, taskID, interResult);

				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.err.println("ParaTask found an unexpected exception while executing a notify or trycatch method: ");
			e.getCause().printStackTrace();
			//-- do not want to end the program just because of one exception.. just print the stack trace and move on.
			//-- this behaviour is similar to the EDT - a new EDT is created to handle other events.
		}
	}
	
	protected void executeSlotOnEDT(final Slot slot) {
		GuiThread.invokeLater(new Runnable() {
			@Override
			public void run() {
				executeSlot(slot);
			}
		});
	}
	
	public void executeSlots(Slot slot) {
		notifyQueue.add(slot);
	}
	
	public void executeSlots(ArrayList<Slot> slots) {
		notifyQueue.addAll(slots);
	}
}
