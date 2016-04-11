package pt.runtime;

import pt.functionalInterfaces.FunctorTwoArgsNoReturn;
import pt.functionalInterfaces.FunctorTwoArgsWithReturn;
import pt.runtime.ParaTask.TaskType;

public class TaskInfoTwoArgs<R, T1, T2> extends TaskInfo<R> {
	
	private FunctorTwoArgsNoReturn<T1, T2> functorNoReturn = null;
	private FunctorTwoArgsWithReturn<R, T1, T2> functorWithReturn = null;
	
	private T1 arg1; private T2 arg2;
	
	TaskInfoTwoArgs(FunctorTwoArgsNoReturn<T1, T2> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoTwoArgs(FunctorTwoArgsNoReturn<T1, T2> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoTwoArgs(FunctorTwoArgsWithReturn<R, T1, T2> functorWithReturn, TaskType taskType, int taskCount){
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoTwoArgs(FunctorTwoArgsWithReturn<R, T1, T2> functorWithReturn, TaskType taskType){
		this(functorWithReturn, taskType, STAR);
	}
	
	public TaskID<R> start(T1 arg1, T2 arg2) {
		try{
			this.arg1 = arg1; this.arg2 = arg2; 
					
			if(this.taskCount == 1)
				return TaskpoolFactory.getTaskpool().enqueue(this);
			else{
				TaskIDGroup<R> taskGroup = TaskpoolFactory.getTaskpool().enqueueMulti(this);
				return taskGroup;
			}
		}catch(IllegalArgumentException e){
			System.out.println("An exception occurred in TaskInfoTwoArgs::start method!");
			System.out.println("The error might have been caused by passing unexpected parameters!");
			e.printStackTrace();
			return null;
		}
	}
	
	R execute(){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2);
		this.functorNoReturn.exec(arg1, arg2);
		return null;
	}
}
