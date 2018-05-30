package pt.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

public class CloudTaskThread extends TaskThread {
	
	private Map<TaskID<?>, AbstractCloudTask<?>> results = null;

	public CloudTaskThread(int globalID, Taskpool taskpool) {
		super(taskpool, false);
		results = new HashMap<>();
	}
	
	/** 
	 * Continuously checks for a task in the cloud task queue, and if no task is found, then it checks for the results of the
	   previous tasks.
	 **/				
	@Override
	public void run() {
		while(!ParaTask.terminateParaTask()) {
			TaskID<?> taskID = this.taskpool.getNextCloudTask();
			if(taskID != null)
				executeTask(taskID);
			else
				checkForResults();
		}
	}	
	
	@Override
	protected <T> boolean executeTask(TaskID<T> taskID) {
		TaskInfo<T> taskInfo = taskID.getTaskInfo();
		if(taskInfo instanceof CloudTaskNoArgs){
			CloudTaskNoArgs<T> cloudTask = (CloudTaskNoArgs<T>) taskInfo;
			cloudTask.executeCloudTask();
			results.put(taskID, cloudTask);
		}
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void checkForResults() {
		for(Entry<TaskID<?>, AbstractCloudTask<?>> entry : results.entrySet()) {
			
			AbstractCloudTask cloudTask = entry.getValue();
			TaskID taskID = entry.getKey();
			
			if(cloudTask.resultIsReady() && !cloudTask.hasException()) {
				try {
					taskID.setReturnResult(cloudTask.getResult());
					taskID.enqueueSlots(false);
					results.remove(taskID);
				} catch (InterruptedException | ExecutionException e) {
					cloudTask.setException(e);
				}
			}
			
			if(cloudTask.hasException()) {
				taskID.setException(cloudTask.getException());
				taskID.enqueueSlots(false);
				results.remove(taskID);
			}
		}
	}
}
