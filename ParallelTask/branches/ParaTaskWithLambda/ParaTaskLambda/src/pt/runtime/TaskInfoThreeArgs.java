package pt.runtime;

import pt.functionalInterfaces.FunctorThreeArgsNoReturn;
import pt.functionalInterfaces.FunctorThreeArgsWithReturn;
import pt.runtime.ParaTask.TaskType;

public class TaskInfoThreeArgs<R, T1, T2, T3> extends TaskInfo<R> {

	private FunctorThreeArgsNoReturn<T1, T2, T3> functorNoReturn = null;
	private FunctorThreeArgsWithReturn<R, T1, T2, T3> functorWithReturn = null;
	
	private T1 arg1; private T2 arg2; private T3 arg3;
	
	TaskInfoThreeArgs(FunctorThreeArgsNoReturn<T1, T2, T3> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoThreeArgs(FunctorThreeArgsNoReturn<T1, T2, T3> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, ParaTask.STAR);
	}
	
	TaskInfoThreeArgs(FunctorThreeArgsWithReturn<R, T1, T2, T3> functorWithReturn, TaskType taskType, int taskCount){
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	public TaskInfoThreeArgs(FunctorThreeArgsWithReturn<R, T1, T2, T3> functorWithReturn, TaskType taskTyp) {
		this(functorWithReturn, taskTyp, ParaTask.STAR);
	}
	
	public TaskID<R> start(T1 arg1, T2 arg2, T3 arg3) {
		try{
			this.arg1 = arg1; this.arg2 = arg2; this.arg3 = arg3; 
					
			if(this.taskCount == 1)
				return TaskpoolFactory.getTaskpool().enqueue(this);
			else{
				TaskIDGroup<R> taskGroup = TaskpoolFactory.getTaskpool().enqueueMulti(this);
				return taskGroup;
			}
		}catch(IllegalArgumentException e){
			System.out.println("An exception occurred in TaskInfoThreeArgs::start method!");
			System.out.println("The error might have been caused by passing unexpected parameters!");
			e.printStackTrace();
			return null;
		}
	}
	
	R execute(){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3);
		this.functorNoReturn.exec(arg1, arg2, arg3);
		return null;
	}
}
