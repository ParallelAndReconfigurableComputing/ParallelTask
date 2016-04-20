/*
 *  Copyright (C) 2013 Nasser Giacaman, Oliver Sinnen
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

public abstract class AbstractTaskListener implements Runnable {

	abstract public void executeSlot(Slot<?> slot);

	/**
	 * Executes the specified slot. If an exception occurs while running the
	 * slot, this is stored in the slot.
	 * 
	 */
	protected void doExecuteSlot(Slot<?> slot) {
		try{
			slot.execute();
		}catch(Exception e){
			System.out.println("PARATASK RUNTIME: EXCEPTION OCCURRED WHEN EXECUTING A HANDLER TASK-SLOT!");
			e.printStackTrace();
		}
	}
}
