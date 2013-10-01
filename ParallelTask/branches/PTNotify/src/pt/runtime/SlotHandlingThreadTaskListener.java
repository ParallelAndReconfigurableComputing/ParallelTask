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

import java.util.concurrent.LinkedBlockingQueue;


public class SlotHandlingThreadTaskListener extends AbstractTaskListener {

	private LinkedBlockingQueue<Slot> notifyQueue = new LinkedBlockingQueue<Slot>();
	
	private boolean alreadyInsideEventLoop = false;
	
	public void quit() {
		this.executeSlot(Slot.quit);
	}
	
	public void run() {
		if (alreadyInsideEventLoop)
			return;
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
					return;
				}
				
				System.out.println("SlotHandlingThreadTaskListener will execute Slot: " + slot);
				doExecuteSlot(slot);	// gets executed on current thread
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				//TODO re-throw exception or not? 
			}
		}
	}

	@Override
	public void executeSlot(Slot slot) {
		notifyQueue.add(slot);
	}
}
