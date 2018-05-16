package pt.runtime;

import java.util.HashMap;
import java.util.Map;

public class CloudTaskThread extends TaskThread {
	
	private Map<TaskID<?>, CloudTask<?>> results = null;

	public CloudTaskThread(Taskpool taskpool) {
		super(taskpool, false);
		results = new HashMap<>();
	}
	
	/** 
	 * Continuously checks for a task in the cloud task queue, and if no task is found, then it checks for the results of the
	   previous tasks.
	 **/				
	@Override
	public void run() {
		while(true) {
			TaskID<?> taskID = this.taskpool.getNextCloudTask();
			if(taskID != null)
				executeTask(taskID);
		}
	}	
	
	@Override
	protected <T> boolean executeTask(TaskID<T> taskID) {
		TaskInfo<T> taskInfo = taskID.getTaskInfo();
		if(taskInfo instanceof CloudTaskNoArgs){
			
		}
		return false;
	}
}
