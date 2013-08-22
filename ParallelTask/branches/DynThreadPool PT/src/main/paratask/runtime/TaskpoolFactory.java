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

import java.util.concurrent.locks.ReentrantLock;

public class TaskpoolFactory {
	
	private static Taskpool taskpool = null;
	private static ReentrantLock lock = new ReentrantLock();
	
	public static Taskpool getTaskpool() {
		if (taskpool == null) {
			lock.lock();
			if (taskpool == null) {
				switch (ParaTask.getScheduleType()) {
				case MixedSchedule:
					taskpool = new TaskpoolMixedScheduling();
					break;
				case WorkSharing:
					taskpool = new TaskpoolFIFOWorkSharing();
					break;
				case WorkStealing:
					taskpool = new TaskpoolLIFOWorkStealing();
					break;
				default:
					break;
				}
			}
			lock.unlock(); 
		}
		return taskpool;
	}
}
