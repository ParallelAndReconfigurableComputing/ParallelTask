package pt.runtime;

import pt.functionalInterfaces.FunctorSixArgsNoReturn;
import pt.functionalInterfaces.FunctorSixArgsWithReturn;
import pt.runtime.ParaTask.TaskType;

public class TaskInfoSixArgs<R, T1, T2, T3, T4, T5, T6> extends TaskInfo<R> {

	private FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functorNoReturn = null;
	private FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functorWithReturn = null;
	
	private T1 arg1; private T2 arg2; private T3 arg3; private T4 arg4; 
	private T5 arg5; private T6 arg6;
	
	TaskInfoSixArgs(FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoSixArgs(FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoSixArgs(FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functorWithReturn, TaskType taskType, int taskCount){
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoSixArgs(FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functorWithReturn, TaskType taskType) {
		this(functorWithReturn, taskType, STAR);
	}
	
	public TaskID<R> start(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6) {
		try{
			this.arg1 = arg1; this.arg2 = arg2; this.arg3 = arg3; this.arg4 = arg4;
			this.arg5 = arg5; this.arg6 = arg6;  
			
			if(this.taskCount == 1)
				return TaskpoolFactory.getTaskpool().enqueue(this);
			else{
				TaskIDGroup<R> taskGroup = TaskpoolFactory.getTaskpool().enqueueMulti(this);
				return taskGroup;
			}
		}catch(IllegalArgumentException e){
			System.out.println("An exception occurred in TaskInfoSixArgs::start method!");
			System.out.println("The error might have been caused by passing unexpected parameters!");
			e.printStackTrace();
			return null;
		}
	}
	
	R execute(){
		if(this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6);
		return null;  
	}
}
