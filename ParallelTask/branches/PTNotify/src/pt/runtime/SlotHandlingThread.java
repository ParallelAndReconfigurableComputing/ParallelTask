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

/*
 * All that this thread does is execute slots (from notify or trycatch clauses). These are the slots that
 * would have come from clauses used inside a TASK (therefore the registering thread would be some form of 
 * a TaskThread (e.g. worker thread or interactive thread).
 * 
 * Therefore, the registering thread should execute those tasks. However, we do not want to execute those 
 * slots on those worker threads for the following reasons: 
 * 	- 	The idea behind slots (on the notify or trycatch clauses) is that they are not necessarily thread-safe.
 * 		This is why the registering thread executes them, as if they have sequential semantics (as opposed
 * 		to TASKs that are thread-safe).
 * 	-	By executing all such slots on a single thread (i.e. this thread), then the semantics is saved since
 * 		all these slots are being executed only by one thread, one at a time. Therefore, such slots do not need
 * 		to be coded thread-safe (staying consistent). The only exception is if the programmer maintained their 
 * 		own threads and those are using same data as these slots. This can easily be overcome by only using 
 * 		ParaTask threads (in which case the programmer need not worry about threads). Programmer just need to 
 * 		be careful that the EDT is not accessing the same data as these slots.
 *  -  	In addition to this, if the tasks were executed by a task thread, then from the programmer's point
 *  	of view it should not matter which (worker) thread executes the slot since the programmer should
 *  	not be thinking in terms of threads (e.g. using thread-local data).  
 * 
 */
public class SlotHandlingThread extends TaskThread {
	private Runnable runnable;
	
	public SlotHandlingThread(Runnable runnable) {
		super(null);
		this.runnable = runnable;
	}

	public void run() {
		this.runnable.run();
	}
}
