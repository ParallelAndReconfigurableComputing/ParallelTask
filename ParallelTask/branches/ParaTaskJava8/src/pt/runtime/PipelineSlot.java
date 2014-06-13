/**
 * 
 */
package pt.runtime;

import java.util.concurrent.ExecutionException;

public class PipelineSlot<T> extends Slot {
	private FunctorVoidWithOneArg<T> pipelineHandler;
	
	public PipelineSlot(FunctorVoidWithOneArg<T> pipelineHandler) {
		this.pipelineHandler = pipelineHandler;
	}
	
	@Override
	void execute() {
		if(pipelineHandler != null) {
			try {
				this.pipelineHandler.exec((T)this.taskID.getReturnResult());
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
