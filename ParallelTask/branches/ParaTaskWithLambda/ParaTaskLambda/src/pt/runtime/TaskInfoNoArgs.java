package pt.runtime;

import pt.functionalInterfaces.FunctorNoArgsNoReturn;
import pt.functionalInterfaces.FunctorNoArgsWithReturn;
import pt.runtime.ParaTask.TaskType;

public class TaskInfoNoArgs<R> extends TaskInfo<R> {
	
	private FunctorNoArgsNoReturn functorNoReturn = null;
	private FunctorNoArgsWithReturn<R> functorWithReturn = null;
	
	TaskInfoNoArgs(FunctorNoArgsNoReturn functorNoReturn, TaskType taskType, int taskCount){
		this.hasNoReturn = true;
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	/*taskCount is only considered when a task is Multi, otherwise it is always 1.
	However, if this rule changes, then this implementation needs to change as well.*/			
	TaskInfoNoArgs(FunctorNoArgsNoReturn functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, ParaTask.STAR);
	}
	
	TaskInfoNoArgs(FunctorNoArgsWithReturn<R> functorWithReturn, TaskType taskType, int taskCount){
		this.hasNoReturn = false;
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoNoArgs(FunctorNoArgsWithReturn<R> functorWithReturn, TaskType taskType){
		this(functorWithReturn, taskType, ParaTask.STAR);
	}
	
	public TaskID<R> start(){
		try{
			if(this.taskCount == 1)
				return TaskpoolFactory.getTaskpool().enqueue(this);					
			else{
				TaskIDGroup<R> taskGroup = TaskpoolFactory.getTaskpool().enqueueMulti(this);
				return taskGroup;
			}
		}catch(IllegalArgumentException e){
			System.out.println("An exception occurred in TaskInfoNoArgs::start method!");
			System.out.println("The error might have been caused by passing unexpected parameters!");
			e.printStackTrace();
			return null;
		}
	}
	
	R execute(){
		if (this.functorWithReturn != null)
			return this.functorWithReturn.exec();
		this.functorNoReturn.exec();
		return null;
	}
}
