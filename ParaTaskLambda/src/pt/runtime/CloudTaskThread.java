package pt.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CloudTaskThread extends TaskThread {
	
	//private Map<TaskID<?>, AbstractCloudTask<?>> pendingResults = null;
	private List<TaskID<?>> pendingResults = null;
	private AtomicBoolean threadKilled = null;

	public CloudTaskThread(int globalID, Taskpool taskpool) {
		super(taskpool, false);
		threadKilled = new AtomicBoolean(false);
		pendingResults = new ArrayList<>();
	}
	
	/** 
	 * Continuously checks for a task in the cloud task queue, and if no task is found, then it checks for the results of the
	   previous tasks.
	 **/				
	@Override
	public void run() {
		while(!(ParaTask.terminateParaTask()||threadKilled.get())) {
			TaskID<?> taskID = this.taskpool.getNextCloudTask();
			if(taskID != null)
				executeTask(taskID);
			else
				checkForResults();
		}
		processTerminationPhase();
	}	
	
	@Override
	protected <T> boolean executeTask(TaskID<T> taskID) {
		TaskInfo<T> taskInfo = taskID.getTaskInfo();
		if(taskInfo instanceof AbstractCloudTask){
			AbstractCloudTask<T> cloudTask = (AbstractCloudTask<T>) taskInfo;
			cloudTask.executeCloudTask();
			pendingResults.add(taskID);
		}
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void checkForResults() {
			
		Iterator<TaskID<?>> iterator = pendingResults.iterator();
		while(iterator.hasNext()) {
			
			TaskID taskID = iterator.next();
			AbstractCloudTask cloudTask = (AbstractCloudTask) taskID.getTaskInfo();
			if(cloudTask.resultIsReady() && !cloudTask.hasException()) {
				try {
					taskID.setReturnResult(cloudTask.getResult());
					taskID.enqueueSlots(false);
					iterator.remove();
				} catch (InterruptedException | ExecutionException e) {
					cloudTask.setException(e);
				}
			}
			
			//this is a separate if statement, needs to be checked regardless of a 
			//cloud task being done or not. 
			if(cloudTask.hasException()) {
				System.out.println("cloud task is finished with exception");
				taskID.setException(cloudTask.getException());
				taskID.enqueueSlots(false);
				iterator.remove();
			}
		}
	}
	
	/* 
	* In this phase that either ParaTask or the cloud engine has been shut down, the 
	* cloud thread/threads process all of the tasks that currently exist in ParaTask
	* cloud task queue, and wait until all of the results are back, then the thread 
	* terminates itself. 
	*/
	private void processTerminationPhase() {
		TaskID<?> taskID = this.taskpool.getNextCloudTask();
		while(taskID != null) {
			executeTask(taskID);
			taskID = this.taskpool.getNextCloudTask();
		}
		
		while(!pendingResults.isEmpty()) {
			checkForResults();
		}	
	}
	
	public void killThread() {
		this.threadKilled.set(true);
	}
}
