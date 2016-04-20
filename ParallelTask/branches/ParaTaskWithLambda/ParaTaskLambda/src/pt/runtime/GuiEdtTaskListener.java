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
 * A task listener that execute event handlers on EDT. The 
 * implemented <i>abstract</i> method passes a slot to the 
 * <code>{@link #executeSlotOnEDT(Slot)}</code>. 
 * The method thereof executes the slot in a runnable on
 * GUI thread.
 * 
 * @author Mostafa Mehrabi
 * @since  21/9/2015
 * 
 */
public class GuiEdtTaskListener extends AbstractTaskListener {
	

	public void executeSlot(Slot<?> slot) {
		GuiThread.invokeLater(()->doExecuteSlot(slot));
	}

	@Override
	public void run() {
		//nothing to do since GUI EDT will execute slots in its own loop
	}
}

