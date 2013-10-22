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

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 
 * Helper methods for the ParaTask runtime. This class contains various functions to set up the ParaTask runtime, etc.
 * <br><br>
 * All applications making use of the ParaTask features should invoke {@link ParaTask#init()} early in the <code>main</code>
 * method. This will initialise various aspects of the ParaTask runtime.
 * 
 * @author Nasser Giacaman
 * @author Oliver Sinnen
 *
 */
public class ParaTask {
	
	private static int threadPoolSize = Runtime.getRuntime().availableProcessors();
	private static ScheduleType scheduleType = ScheduleType.MixedSchedule;
	static boolean isInitialized = false;

	static Thread EDT = null;		// a reference to the EDT
	static AbstractTaskListener listener;	// the EDT task listener
		
	ParaTask(){
		
	}
	
	public static Thread getEDT() {
		return EDT;
	}
	
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
     * Set the size of the thread pool. To have any effect, this must be executed very early before 
     * ParaTask creates the runtime. 
     * @param size
     */
    public static void setThreadPoolSize(int size) {
		if (size < 1)
			throw new IllegalArgumentException("Trying to create a Taskpool with " + size + " threads");
    	threadPoolSize = size;
    }
    
    /**
     * Set the scheduling scheme. This only has an effect if no tasks have been executed yet 
     * (i.e. must be called before the taskpool is created).
     * 
     * @param type The schedule to use.
     */
    public static void setScheduling(ScheduleType type) {
        // TODO modify so that it throws an exception if taskpool has already been created (i.e. too late to change schedule)
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
    public static int getThreadPoolSize() {
    	return threadPoolSize;
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
	 * dispatch thread with ParaTask, as well as other ParaTask runtime settings.
	 */
	public static void init() {
		
		while(!isInitialized){
			
			GuiThread.init();
		
			try {
				ParaTaskHelper.setCompleteSlot = 
					ParaTaskHelper.class.getDeclaredMethod("setComplete", new Class[] {TaskID.class});
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			//-- Create the task pool
			TaskpoolFactory.getTaskpool();
			
			//-- initialize the EDT
			EDT = GuiThread.getEventDispatchThread();
			listener = new GuiEdtTaskListener();
			/*
			 * The ParaTask keywords notifyGUI and notifyInterimGUI has been removed.
			 * All slots will be handled by the GUI EDT thread, and this feature has been
			 * tested on both Java SE platform and Android platform, for both with GUI
			 * and without GUI situations.
			 * 
			 * The SlotHandlingThread.java and SlotHandlingThreadTaskListener.java are 
			 * also removed.
			 * 
			 * If there are other situations where we cannot depend on the GUI EDT thread
			 * to handle slots, or you do want to have a separate thread act as the slot 
			 * handling thread instead of the GUI EDT thread, please add these two Java 
			 * files back, and initialize ParaTask.EDT and ParaTask.listener with their 
			 * instances.
			 * 
			 * Here are the svn revision and URLs of these two Java files before
			 * they are deleted:
			 * 
			 * revision 3717
			 * 
			 * https://svn.ece.auckland.ac.nz/svn/taschto/ParallelTask/branches/PTNotify/src/pt/runtime/SlotHandlingThread.java
			 * https://svn.ece.auckland.ac.nz/svn/taschto/ParallelTask/branches/PTNotify/src/pt/runtime/SlotHandlingThreadTaskListener.java
			 */
			
			isInitialized = true;
			
			System.out.println("ParaTask.init EDT id: " + EDT.getId() + " EDT name: " + EDT.getName());
		}
	}
	
	static AbstractTaskListener getEDTTaskListener() {
		if (EDT == null) {
			throw new RuntimeException("Please call ParaTask.init() early in the main method of your application!");
		}
		return listener;
	}	
	
	/**
	 * Flattens a list of TaskIDs. Only has an effect if some of the TaskIDs were actually TaskIDGroups.
	 * @param list	Input list of TaskIDs (with potentially some TaskIDGroups)
	 * @return	A list containing only TaskIDs (i.e. expanding the TaskIDGroups)
	 * @see #allTasksInGroup(TaskIDGroup)
	 */
	public static ArrayList<TaskID> allTasksInList(ArrayList<TaskID> list) {
		ArrayList<TaskID> result = new ArrayList<TaskID>();
		
		Iterator<TaskID> it = list.iterator();
		while (it.hasNext()) {
			TaskID id = it.next();
			if (id instanceof TaskIDGroup) {
				result.addAll(allTasksInGroup((TaskIDGroup)id));
			} else {
				result.add(id);
			}
		}
		return result;
	}
	
	/**
	 * A recursive convenience function that digs into the TaskIDGroup and returns all the individual TaskIDs.
	 * @see #allTasksInList(ArrayList) 
	 * @return the TaskIDs inside <code>group</code> placed inside a new ArrayList
	 * */
	public static ArrayList<TaskID> allTasksInGroup(TaskIDGroup group) {
		ArrayList<TaskID> list = new ArrayList<TaskID>();
		 
		Iterator<TaskID> it = group.groupMembers();
		while (it.hasNext()) {
			TaskID id = it.next();
			if (id instanceof TaskIDGroup) {
				list.addAll(allTasksInGroup((TaskIDGroup)id));
			} else {
				list.add(id);
			}
		}
		return list;
	}
}
