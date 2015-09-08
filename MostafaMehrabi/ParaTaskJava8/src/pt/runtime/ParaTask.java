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

/**
 * @author Mostafa Mehrabi
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 * 
 * <br><br>
 * Helper methods for the ParaTask runtime. This class contains various functions to set up the ParaTask runtime. 
 * Importantly the type of scheduling, thread pool type, thread pool size, the EDT and the task listener are set 
 * and retrieved via this class.
 * <br><br>
 * All applications making use of the ParaTask features should invoke {@link ParaTask#init()} early in the <code>main</code>
 * method. This will initialise various aspects of the ParaTask runtime.
 */
public class ParaTask {
	
		
	//private static int threadPoolSize = Runtime.getRuntime().availableProcessors();
	private static ScheduleType scheduleType = null;
	private static boolean isInitialized = false;

	private static Thread EDT = null;		// a reference to the EDT
	private static AbstractTaskListener listener;	// the EDT task listener
	
	//for internal use only
	static final String PT_PREFIX = "__pt__";
	static long WORKER_SLEEP_DELAY = 200;
	static int ANY_THREAD_TASK = -1;
	static int EXCEPTION_IN_SLOT = -1;
	
		
	/**
	 * 
	 * Enum representing the possible schedules that ParaTask supports.
	 * 
	 * @author Nasser Giacaman
	 * @author Oliver Sinnen
	 */
	public static enum ScheduleType { 
		/**
		 * Tasks are queued to a shared global queue and are executed using a first in first out policy. 
		 */
		WorkSharing, 
		
		/**
		 * Tasks are queued to the queue local to the enqueing thread (or to a random thread's queue if the
		 * enqueing thread is not a worker thread). Tasks are executed using a last in first out policy when 
		 * taken from the thread's own queue. Otherwise, tasks are stolen from another thread's queue using
		 * a first in first out policy.
		 */
		WorkStealing,
		
		/**
		 * A combination of work-stealing and work-sharing. When a task is enqueued by a worker thread, this 
		 * behaves as work-stealing. When a task is enqueued by a non-worker thread (e.g. main thread or event 
		 * dispatch thread), this behaves as work-sharing. A worker thread always favours to execute tasks 
		 * from its local queue before helping with the global shared queue.    
		 */
		MixedSchedule };
		
		
   /**
	* 
	* Enum representing the possible thread pool types that ParaTask supports.
	* 
	* @author Kingsley
 	* @since 27/05/2013
  	*/
	public static enum ThreadPoolType{
	    	ALL, ONEOFF, MULTI
	 }	
	
	
		
	ParaTask(){
		
	}
	
	public static boolean isInitialized(){
		return isInitialized;
	}
	
	public static Thread getEDT() {
		return EDT;
	}
		

    /**
     * Set the size of the thread pool. To have any effect, this must be executed very early before 
     * ParaTask creates the runtime. This method throws {@link IllegalArgumentException} if the parameter
     * passed to it is smaller than one.
     * 
     * @param size
     */
    public static void setThreadPoolSize(ThreadPoolType threadPoolType, int size) {
    	if (size < 1)
			throw new IllegalArgumentException("Trying to create a Taskpool with " + size + " threads");
		ThreadPool.setPoolSize(threadPoolType,size);
    }
    
        
    /**
     * Set the scheduling scheme. This only has an effect if no tasks have been executed yet 
     * (i.e. must be called before ParaTask is initialized). This method throws
     * {@link IllegalAccessExecption} if ParaTask is initialized earlier.
     * 
     * @param type The schedule to use.
     * @throws IllegalAccessException 
     */
    public static void setSchedulingType(ScheduleType type) throws IllegalAccessException {
       if (isInitialized())
    		throw new IllegalAccessException("ParaTask has been initialized already!\n"
    				+ " The scheduling policy must be declared prior to the initialization stage!");
    	scheduleType = type;
    }
    
    /**
     * Returns the schedule being used in the runtime.  
     * @return		The schedule being used.
     */
    public static ScheduleType getScheduleType() {
    	return scheduleType;
    }
    
    /**
     * Returns the size of the thread pool.
     * 
     * @return	The thread pool size.
     */
    public static int getThreadPoolSize(ThreadPoolType threadPoolType) {
    	return ThreadPool.getPoolSize(threadPoolType);
    }
    
    public static int getNumberOfActiveThreads(ThreadPoolType threadPoolType){
    	return ThreadPool.getNumberOfActiveThreads(threadPoolType);
    }
	
	/**
	 * Returns a count of the number of active interactive tasks. Useful if need to decide whether a task 
	 * should be invoked interactively or not (e.g. to limit thread count). 
	 * @return	The number of active interactive tasks.
	 */
	public static int activeInteractiveTaskCount() {
		return TaskpoolFactory.getTaskpool().getActiveInteractiveTaskCount();
	}
	
	/**
	 * To be executed by the main thread (i.e. inside the <code>main</code> method). Registers the main thread and event 
	 * dispatch thread with ParaTask, and instantiates a task pool with the default <code>WorkStealing</code> scheduling
	 * policy, iff the scheduling policy is not set by the used beforehand. Otherwise, it initialized the task pool using
	 * the user-specified scheduling policy.
	 * 
	 * @return boolean If ParaTask has not been initialized before, this method returns <code>true</code> once the setup
	 * is done. Otherwise (i.e., ParaTask has been initialized before) it returns <code>false</code>.
	 * 
	 * @author Mostafa Mehrabi
	 * @since  2015
	 */
	public static boolean init(){
		if (scheduleType == null)
			return init(ScheduleType.WorkStealing);
		else
			return init(scheduleType);
	}
	
	/**
	 * To be executed by the main thread (i.e. inside the <code>main</code> method). Registers the main thread and event 
	 * dispatch thread with ParaTask, and instantiates a task pool with the user-specified scheduling policy.
	 * 
	 * @return boolean If ParaTask has not been initialized before, this method returns <code>true</code> once the setup
	 * is done. Otherwise (i.e., ParaTask has been initialized before) it returns <code>false</code>.
	 * 
	 * @author Mostafa Mehrabi
	 * @throws IllegalAccessException 
	 * @since  2015
	 */
	public static boolean init(ScheduleType scheduleType){
		if (isInitialized())
			return false;
		while(!isInitialized){
			try{
				GuiThread.init();
				setSchedulingType(scheduleType);
				//Create the task pool
				TaskpoolFactory.getTaskpool();
				
				//Initialize the EDT
				EDT = GuiThread.getEventDispatchThread();
				listener = new GuiEdtTaskListener();
				isInitialized = true;
			}catch(IllegalAccessException e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return true;
	}
	
	static AbstractTaskListener getEDTTaskListener() {
		if (EDT == null) {
			throw new RuntimeException("Please call ParaTask.init() early in the main method of your application!");
		}
		return listener;
	}	
	
	
}
