package pt.runtime;

import pt.functionalInterfaces.FunctorFiveArgsNoReturn;
import pt.functionalInterfaces.FunctorFiveArgsWithReturn;
import pt.runtime.ParaTask.TaskType;

public class TaskInfoFiveArgs<R, T1, T2, T3, T4, T5> extends TaskInfo<R> {
	
	private FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functorNoReturn = null;
	private FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functorWithReturn = null;
	
	private T1 arg1; private T2 arg2; private T3 arg3; private T4 arg4; private T5 arg5;

	TaskInfoFiveArgs(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoFiveArgs(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functorNoReturn, TaskType taskType) {
		this(functorNoReturn, taskType, ParaTask.STAR);
	}
	
	TaskInfoFiveArgs(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functorWithReturn, TaskType taskType, int taskCount) {
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoFiveArgs(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functorWithReturn, TaskType taskType) {
		this(functorWithReturn, taskType, ParaTask.STAR);
	}
	
	public TaskID<R> start(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5) {
		try{
			this.arg1 = arg1; this.arg2 = arg2; this.arg3 = arg3; this.arg4 = arg4;
			this.arg5 = arg5; 
			
			if(this.taskCount == 1)
				return TaskpoolFactory.getTaskpool().enqueue(this);
			else{
				TaskIDGroup<R> taskGroup = TaskpoolFactory.getTaskpool().enqueueMulti(this);
				return taskGroup;
			}
		}catch(IllegalArgumentException e){
			System.out.println("An exception occurred in TaskInfoFiveArgs::start method!");
			System.out.println("The error might have been caused by passing unexpected parameters!");
			e.printStackTrace();
			return null;
		}
	}
	
	R execute(){
		if(this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5);
		return null;
	}
}
