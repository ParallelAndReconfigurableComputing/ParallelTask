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

import java.util.concurrent.locks.ReentrantLock;

import pi.RedLib.Reduction;
import pt.functionalInterfaces.FunctorEightArgsNoReturn;
import pt.functionalInterfaces.FunctorEightArgsWithReturn;
import pt.functionalInterfaces.FunctorElevenArgsNoReturn;
import pt.functionalInterfaces.FunctorElevenArgsWithReturn;
import pt.functionalInterfaces.FunctorFiveArgsNoReturn;
import pt.functionalInterfaces.FunctorFiveArgsWithReturn;
import pt.functionalInterfaces.FunctorFourArgsNoReturn;
import pt.functionalInterfaces.FunctorFourArgsWithReturn;
import pt.functionalInterfaces.FunctorNineArgsNoReturn;
import pt.functionalInterfaces.FunctorNineArgsWithReturn;
import pt.functionalInterfaces.FunctorNoArgsNoReturn;
import pt.functionalInterfaces.FunctorNoArgsWithReturn;
import pt.functionalInterfaces.FunctorOneArgNoReturn;
import pt.functionalInterfaces.FunctorOneArgWithReturn;
import pt.functionalInterfaces.FunctorSevenArgsNoReturn;
import pt.functionalInterfaces.FunctorSevenArgsWithReturn;
import pt.functionalInterfaces.FunctorSixArgsNoReturn;
import pt.functionalInterfaces.FunctorSixArgsWithReturn;
import pt.functionalInterfaces.FunctorTenArgsNoReturn;
import pt.functionalInterfaces.FunctorTenArgsWithReturn;
import pt.functionalInterfaces.FunctorThreeArgsNoReturn;
import pt.functionalInterfaces.FunctorThreeArgsWithReturn;
import pt.functionalInterfaces.FunctorTwelveArgsNoReturn;
import pt.functionalInterfaces.FunctorTwelveArgsWithReturn;
import pt.functionalInterfaces.FunctorTwoArgsNoReturn;
import pt.functionalInterfaces.FunctorTwoArgsWithReturn;
import pt.runtime.TaskInfo.TaskType;

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
//	static {
//		ParaTask.init();
//	}
			
	//private static int threadPoolSize = Runtime.getRuntime().availableProcessors();
	private static ScheduleType scheduleType = null;
	private static boolean isInitialized = false;
	private static boolean hasStartedWorking = false;

	private static Thread EDT = null;		// a reference to the EDT
	private static AbstractTaskListener listener;	// the EDT task listener
	private static ReentrantLock lock;
	
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
		MixedSchedule
	};
		
		
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
	
	static void paraTaskStarted(boolean started){
		ParaTask.hasStartedWorking = started;
	}
	
	static boolean hasParaTaskStarted(){
		return ParaTask.hasStartedWorking;
	}
		

    /**
     * Set the size of the thread pool. To have any effect, this must be executed very early before 
     * ParaTask creates the runtime. This method throws {@link IllegalArgumentException} if the parameter
     * passed to it is smaller than one. This method returns <code>false</code> if it fails to adjust thread pool. This
     * happens when ParaTask has already started working (i.e., TaskInfos are enqueued and TaskIDs are
     * created), otherwise it returns <code>true</code>
     * 
     * @param size
     * @return boolean <code>true</code> if scheduling type is changed successfully, otherwise <code>false</code>.
     */
    public static boolean setThreadPoolSize(ThreadPoolType threadPoolType, int size) {
    	if (size < 1)
			throw new IllegalArgumentException("Trying to create a Taskpool with " + size + " threads");
    	if (hasParaTaskStarted())
    		return false;
    	if (!isInitialized())
    		ParaTask.init();
		ThreadPool.setPoolSize(threadPoolType,size);
		return true;
    }
    
        
    /**
     * Set the scheduling scheme. This only has an effect if no tasks have been executed yet 
     * (i.e. must be called before ParaTask is initialized). This method throws
     * {@link IllegalAccessExecption} if ParaTask is initialized earlier.
     * This method returns <code>false</code> if it fails to adjust thread pool. This
     * happens when ParaTask has already started working (i.e., TaskInfos are enqueued and TaskIDs are
     * created), otherwise it returns <code>true</code>
     * 
     * @param type The schedule to use.
     * @throws IllegalAccessException 
     * @return boolean <code>true</code> if scheduling type is changed successfully, otherwise <code>false</code>.
     */
    public static boolean setSchedulingType(ScheduleType type) {
       if (ParaTask.hasParaTaskStarted())
    		return false;
    	scheduleType = type;
    	return true;
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
			return init(ScheduleType.MixedSchedule);
		else
			return init(scheduleType);
	}
	
	public static <R> void executeInterimHandler(Slot<R> slot){
		lock.lock();
		ParaTask.getEDTTaskListener().executeSlot(slot);
		lock.unlock();
	}
	
	public static <T> void setReductionOperation(TaskIDGroup<T> taskGroup, Reduction<T> reduction){
		taskGroup.setReduction(reduction);
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
				lock = new ReentrantLock();
				isInitialized = true;
			}catch(Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return true;
	}
	
	static AbstractTaskListener getEDTTaskListener() {
		if (EDT == null) {
			throw new RuntimeException("ParaTask must be initialized early in the main method of application!");
		}
		return listener;
	}	
	
	public static <R> void registerSlotToNotify(TaskInfo<R> taskInfo, FunctorNoArgsNoReturn functor){
		taskInfo.notify(new Slot<R>(functor));
	}
	
	public static <R> void registerSlotToNotify(TaskInfo<R> taskInfo, FunctorOneArgNoReturn<R> functor, TaskID<R> taskID){
		taskInfo.notify(new Slot<R>(functor, taskID));
	}
	//****************************************************************************************TASK GENERATORS******************************************************************
	//****************************************************************************************ONE-OFF TASKS********************************************************************
	public static TaskInfo<Void> asTask(FunctorNoArgsNoReturn functor){
		return new TaskInfoNoArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R> TaskInfo<R> asTask(FunctorNoArgsWithReturn<R> functor){
		return new TaskInfoNoArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1> TaskInfo<Void> asTask(FunctorOneArgNoReturn<T1> functor){
		return new TaskInfoOneArg<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1> TaskInfo<R> asTask(FunctorOneArgWithReturn<R, T1> functor){
		return new TaskInfoOneArg<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2> TaskInfo<Void> asTask(FunctorTwoArgsNoReturn<T1, T2> functor){
		return new TaskInfoTwoArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2> TaskInfo<R> asTask(FunctorTwoArgsWithReturn<R, T1, T2> functor){
		return new TaskInfoTwoArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3> TaskInfo<Void> asTask(FunctorThreeArgsNoReturn<T1, T2, T3> functor){
		return new TaskInfoThreeArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3> TaskInfo<R> asTask(FunctorThreeArgsWithReturn<R, T1, T2, T3> functor){
		return new TaskInfoThreeArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3, T4> TaskInfo<Void> asTask(FunctorFourArgsNoReturn<T1, T2, T3, T4> functor){
		return new TaskInfoFourArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4> TaskInfo<R> asTask(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functor){
		return new TaskInfoFourArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3, T4, T5> TaskInfo<Void> asTask(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functor){
		return new TaskInfoFiveArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4, T5> TaskInfo<R> asTask(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functor){
		return new TaskInfoFiveArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3, T4, T5, T6> TaskInfo<Void> asTask(FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functor){
		return new TaskInfoSixArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6> TaskInfo<R> asTask(FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functor){
		return new TaskInfoSixArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7> TaskInfo<Void> asTask(FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functor){
		return new TaskInfoSevenArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7> TaskInfo<R> asTask(FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functor){
		return new TaskInfoSevenArgs<>(functor, TaskType.ONEOFF); 
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8> TaskInfo<Void> asTask(FunctorEightArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8> functor){
		return new TaskInfoEightArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8> TaskInfo<R> asTask(FunctorEightArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8> functor){
		return new TaskInfoEightArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> TaskInfo<Void> asTask(FunctorNineArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9> functor){
		return new TaskInfoNineArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9> TaskInfo<R> asTask(FunctorNineArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> functor){
		return new TaskInfoNineArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> TaskInfo<Void> asTask(FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functor){
		return new TaskInfoTenArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> TaskInfo<R> asTask(FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functor){
		return new TaskInfoTenArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> TaskInfo<Void> asTask(FunctorElevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functor){
		return new TaskInfoElevenArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> TaskInfo<R> asTask(FunctorElevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functor){
		return new TaskInfoElevenArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> TaskInfo<Void> asTask(FunctorTwelveArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> functor){
		return new TaskInfoTwelveArgs<>(functor, TaskType.ONEOFF);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> TaskInfo<R> asTask(FunctorTwelveArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> functor){
		return new TaskInfoTwelveArgs<>(functor, TaskType.ONEOFF);
	}
	
	//***********************************************************************MULTI TASKS**************************************************************************************************
	public static TaskInfo<Void> asMultiTask(FunctorNoArgsNoReturn functor){
		return new TaskInfoNoArgs<>(functor, TaskType.MULTI);
	}
	
	public static TaskInfo<Void> asMultiTask(FunctorNoArgsNoReturn functor, int taskCoun){
		return new TaskInfoNoArgs<>(functor, TaskType.MULTI, taskCoun);
	}
	
	public static <R> TaskInfo<R> asMultiTask(FunctorNoArgsWithReturn<R> functor){
		return new TaskInfoNoArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R> TaskInfo<R> asMultiTask(FunctorNoArgsWithReturn<R> functor, int taskCount){
		return new TaskInfoNoArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1> TaskInfo<Void> asMultiTask(FunctorOneArgNoReturn<T1> functor){
		return new TaskInfoOneArg<>(functor, TaskType.MULTI);
	}
	
	public static <T1> TaskInfo<Void> asMultiTask(FunctorOneArgNoReturn<T1> functor, int taskCount){
		return new TaskInfoOneArg<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1> TaskInfo<R> asMultiTask(FunctorOneArgWithReturn<R, T1> functor){
		return new TaskInfoOneArg<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1> TaskInfo<R> asMultiTask(FunctorOneArgWithReturn<R, T1> functor, int taskCount){
		return new TaskInfoOneArg<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2> TaskInfo<Void> asMultiTask(FunctorTwoArgsNoReturn<T1, T2> functor){
		return new TaskInfoTwoArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2> TaskInfo<Void> asMultiTask(FunctorTwoArgsNoReturn<T1, T2> functor, int taskCount){
		return new TaskInfoTwoArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2> TaskInfo<R> asMultiTask(FunctorTwoArgsWithReturn<R, T1, T2> functor){
		return new TaskInfoTwoArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2> TaskInfo<R> asMultiTask(FunctorTwoArgsWithReturn<R, T1, T2> functor, int taskCount){
		return new TaskInfoTwoArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3> TaskInfo<Void> asMultiTask(FunctorThreeArgsNoReturn<T1, T2, T3> functor){
		return new TaskInfoThreeArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3> TaskInfo<Void> asMultiTask(FunctorThreeArgsNoReturn<T1, T2, T3> functor, int  taskCount){
		return new TaskInfoThreeArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3> TaskInfo<R> asMultiTask(FunctorThreeArgsWithReturn<R, T1, T2, T3> functor){
		return new TaskInfoThreeArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3> TaskInfo<R> asMultiTask(FunctorThreeArgsWithReturn<R, T1, T2, T3> functor, int taskCount){
		return new TaskInfoThreeArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3, T4> TaskInfo<Void> asMultiTask(FunctorFourArgsNoReturn<T1, T2, T3, T4> functor){
		return new TaskInfoFourArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4> TaskInfo<Void> asMultiTask(FunctorFourArgsNoReturn<T1, T2, T3, T4> functor, int taskCount){
		return new TaskInfoFourArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4> TaskInfo<R> asMultiTask(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functor){
		return new TaskInfoFourArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3, T4> TaskInfo<R> asMultiTask(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functor, int taskCount){
		return new TaskInfoFourArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3, T4, T5> TaskInfo<Void> asMultiTask(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functor){
		return new TaskInfoFiveArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4, T5> TaskInfo<Void> asMultiTask(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functor, int taskCount){
		return new TaskInfoFiveArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4, T5> TaskInfo<R> asMultiTask(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functor){
		return new TaskInfoFiveArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3, T4, T5> TaskInfo<R> asMultiTask(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functor, int taskCount){
		return new TaskInfoFiveArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3, T4, T5, T6> TaskInfo<Void> asMultiTask(FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functor){
		return new TaskInfoSixArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4, T5, T6> TaskInfo<Void> asMultiTask(FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functor, int taskCount){
		return new TaskInfoSixArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6> TaskInfo<R> asMultiTask(FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functor){
		return new TaskInfoSixArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6> TaskInfo<R> asMultiTask(FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functor, int taskCount){
		return new TaskInfoSixArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7> TaskInfo<Void> asMultiTask(FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functor){
		return new TaskInfoSevenArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7> TaskInfo<Void> asMultiTask(FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functor, int taskCount){
		return new TaskInfoSevenArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7> TaskInfo<R> asMultiTask(FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functor){
		return new TaskInfoSevenArgs<>(functor, TaskType.MULTI); 
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7> TaskInfo<R> asMultiTask(FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functor, int taskCount){
		return new TaskInfoSevenArgs<>(functor, TaskType.MULTI, taskCount); 
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8> TaskInfo<Void> asMultiTask(FunctorEightArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8> functor){
		return new TaskInfoEightArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8> TaskInfo<Void> asMultiTask(FunctorEightArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8> functor, int taskCount){
		return new TaskInfoEightArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8> TaskInfo<R> asMultiTask(FunctorEightArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8> functor){
		return new TaskInfoEightArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8> TaskInfo<R> asMultiTask(FunctorEightArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8> functor, int taskCount){
		return new TaskInfoEightArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> TaskInfo<Void> asMultiTask(FunctorNineArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9> functor){
		return new TaskInfoNineArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> TaskInfo<Void> asMultiTask(FunctorNineArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9> functor, int taskCount){
		return new TaskInfoNineArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9> TaskInfo<R> asMultiTask(FunctorNineArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> functor){
		return new TaskInfoNineArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9> TaskInfo<R> asMultiTask(FunctorNineArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> functor, int taskCount){
		return new TaskInfoNineArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> TaskInfo<Void> asMultiTask(FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functor){
		return new TaskInfoTenArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> TaskInfo<Void> asMultiTask(FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functor, int taskCount){
		return new TaskInfoTenArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> TaskInfo<R> asMultiTask(FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functor){
		return new TaskInfoTenArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> TaskInfo<R> asMultiTask(FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functor, int taskCount){
		return new TaskInfoTenArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> TaskInfo<Void> asMultiTask(FunctorElevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functor){
		return new TaskInfoElevenArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> TaskInfo<Void> asMultiTask(FunctorElevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functor, int taskCount){
		return new TaskInfoElevenArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> TaskInfo<R> asMultiTask(FunctorElevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functor){
		return new TaskInfoElevenArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> TaskInfo<R> asMultiTask(FunctorElevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functor, int taskCount){
		return new TaskInfoElevenArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> TaskInfo<Void> asMultiTask(FunctorTwelveArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> functor){
		return new TaskInfoTwelveArgs<>(functor, TaskType.MULTI);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> TaskInfo<Void> asMultiTask(FunctorTwelveArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> functor, int taskCount){
		return new TaskInfoTwelveArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> TaskInfo<R> asMultiTask(FunctorTwelveArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> functor){
		return new TaskInfoTwelveArgs<>(functor, TaskType.MULTI);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> TaskInfo<R> asMultiTask(FunctorTwelveArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> functor, int taskCount){
		return new TaskInfoTwelveArgs<>(functor, TaskType.MULTI, taskCount);
	}
	
//******************************************************************************************INTERACTIVE TASKS************************************************************************************
	
	public static TaskInfo<Void> asIOTask(FunctorNoArgsNoReturn functor){
		return new TaskInfoNoArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R> TaskInfo<R> asIOTask(FunctorNoArgsWithReturn<R> functor){
		return new TaskInfoNoArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1> TaskInfo<Void> asIOTask(FunctorOneArgNoReturn<T1> functor){
		return new TaskInfoOneArg<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1> TaskInfo<R> asIOTask(FunctorOneArgWithReturn<R, T1> functor){
		return new TaskInfoOneArg<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2> TaskInfo<Void> asIOTask(FunctorTwoArgsNoReturn<T1, T2> functor){
		return new TaskInfoTwoArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2> TaskInfo<R> asIOTask(FunctorTwoArgsWithReturn<R, T1, T2> functor){
		return new TaskInfoTwoArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3> TaskInfo<Void> asIOTask(FunctorThreeArgsNoReturn<T1, T2, T3> functor){
		return new TaskInfoThreeArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3> TaskInfo<R> asIOTask(FunctorThreeArgsWithReturn<R, T1, T2, T3> functor){
		return new TaskInfoThreeArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3, T4> TaskInfo<Void> asIOTask(FunctorFourArgsNoReturn<T1, T2, T3, T4> functor){
		return new TaskInfoFourArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4> TaskInfo<R> asIOTask(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functor){
		return new TaskInfoFourArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3, T4, T5> TaskInfo<Void> asIOTask(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functor){
		return new TaskInfoFiveArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4, T5> TaskInfo<R> asIOTask(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functor){
		return new TaskInfoFiveArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3, T4, T5, T6> TaskInfo<Void> asIOTask(FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functor){
		return new TaskInfoSixArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6> TaskInfo<R> asIOTask(FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functor){
		return new TaskInfoSixArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7> TaskInfo<Void> asIOTask(FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functor){
		return new TaskInfoSevenArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7> TaskInfo<R> asIOTask(FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functor){
		return new TaskInfoSevenArgs<>(functor, TaskType.INTERACTIVE); 
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8> TaskInfo<Void> asIOTask(FunctorEightArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8> functor){
		return new TaskInfoEightArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8> TaskInfo<R> asIOTask(FunctorEightArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8> functor){
		return new TaskInfoEightArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> TaskInfo<Void> asIOTask(FunctorNineArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9> functor){
		return new TaskInfoNineArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9> TaskInfo<R> asIOTask(FunctorNineArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> functor){
		return new TaskInfoNineArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> TaskInfo<Void> asIOTask(FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functor){
		return new TaskInfoTenArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> TaskInfo<R> asIOTask(FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functor){
		return new TaskInfoTenArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> TaskInfo<Void> asIOTask(FunctorElevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functor){
		return new TaskInfoElevenArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> TaskInfo<R> asIOTask(FunctorElevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functor){
		return new TaskInfoElevenArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> TaskInfo<Void> asIOTask(FunctorTwelveArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> functor){
		return new TaskInfoTwelveArgs<>(functor, TaskType.INTERACTIVE);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> TaskInfo<R> asIOTask(FunctorTwelveArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> functor){
		return new TaskInfoTwelveArgs<>(functor, TaskType.INTERACTIVE);
	}	
}
