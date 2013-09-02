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

package pt.runtime;

import java.lang.reflect.Method;

public class ParaTaskHelper {

	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
	public static final String PT_PREFIX = "__pt__";
	

	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
	public static long WORKER_SLEEP_DELAY = 10;

	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
	public static int ANY_THREAD_TASK = -1;
	
	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
	public static int EXCEPTION_IN_SLOT = -1;
	
    /* ParaTask dummy variables, never used, just for Java compiler purposes */

	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
    public static TaskIDGroup dummyTaskID = null;
	
    
	/*
	 * a wrapper function, since setComplete is not public inside TaskID
	 */
	public static void setComplete(TaskID id) {
		id.setComplete();
	}
	
	public static void executeAnotherTask() {
		WorkerThread thread = (WorkerThread) Thread.currentThread();
		thread.executeAnotherTaskOrSleep();
	}

	/*
	 *	Created here so that it is not queried eveytime it is needed (since it is relatively costly)  
	 */

	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
	
	public static Method setCompleteSlot = null;
	
	/*
	 * Non-recursive start
	 * 
	 * TODO  refactor the code so that methods are not gotten unless really needed 
	 * 
	 */
	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
	static public Method getDeclaredMethod(Class start, String name, Class[] parameterTypes) throws SecurityException, NoSuchMethodException {
		if (start.getEnclosingClass() == null) {
			//-- there is no enclosing class, therefore method must be in here or the super class
			return getDeclaredMethodCheckSuperClasses(start, name, parameterTypes);
		} else {
			try {
				//-- first check super classes. If fail, then check enclosing classes.
				return getDeclaredMethodCheckSuperClasses(start, name, parameterTypes);
			} catch (NoSuchMethodException e) {
				//-- method must be in the enclosing classes
				return getDeclaredMethodCheckEnclosingClasses(start.getEnclosingClass(), name, parameterTypes);
			}
		}
	}
	
	/*  ParaTask helper method to determine Class from inside a static method */

	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
    static public class ClassGetter extends SecurityManager {
        public Class getCurrentClass() {
        	Class[] stack = getClassContext();
        	
        	if (stack == null) { // android 4.0 returns null
        		StackTraceElement[] classNames = Thread.currentThread().getStackTrace();
        		try {
        			return Class.forName(classNames[3].getClassName());
        		} catch (ClassNotFoundException e) {
        			throw new RuntimeException("Could not get name of current class.");
        		}
        	} else {
        		return stack[1];
        	}
        }
    }
    

	/**
	 * [This is only intended for internal use of the ParaTask compiler and runtime]
	 */
	static public boolean isSubClassOf(Class child, Class potentialParent) {
		try {
			child.asSubclass(potentialParent);
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	/*
	 * 	Recursively checks the enclosing classes to find the declared method
	 */
	static private Method getDeclaredMethodCheckEnclosingClasses(Class start, String name, Class[] parameterTypes) throws SecurityException, NoSuchMethodException{
		if (start.getEnclosingClass() == null ) {
			return start.getDeclaredMethod(name, parameterTypes);
		} else {
			try {
				return start.getDeclaredMethod(name, parameterTypes);
			} catch (NoSuchMethodException e) {
				//-- must now check the superclass
				return getDeclaredMethodCheckEnclosingClasses(start.getEnclosingClass(), name, parameterTypes); 
			}
		}
	}
	
	/*
	 * 	Recursively checks the super classes to find the declared method
	 */
	static private Method getDeclaredMethodCheckSuperClasses(Class start, String name, Class[] parameterTypes) throws SecurityException, NoSuchMethodException{
		if (start.getSuperclass() == null) {
			//-- there is no super class, so method should be declared in here 
			return start.getDeclaredMethod(name, parameterTypes);
		} else {
			try {
				//-- try to get the Method from the current class
				return start.getDeclaredMethod(name, parameterTypes);
			} catch (NoSuchMethodException e) {
				//-- must now check the superclass
				return getDeclaredMethodCheckSuperClasses(start.getSuperclass(), name, parameterTypes); 
			}
		}
	}
	
}
