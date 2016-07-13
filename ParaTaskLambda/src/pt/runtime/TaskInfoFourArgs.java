package pt.runtime;

import pt.functionalInterfaces.FunctorFourArgsNoReturn;
import pt.functionalInterfaces.FunctorFourArgsWithReturn;
import pt.runtime.ParaTask.TaskType;

public class TaskInfoFourArgs<R, T1, T2, T3, T4> extends TaskInfo<R> {
	
	private FunctorFourArgsNoReturn<T1, T2, T3, T4> functorNoReturn = null;
	private FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functorWithReturn = null;
	
	private T1 arg1; private T2 arg2; private T3 arg3; private T4 arg4;

	TaskInfoFourArgs(FunctorFourArgsNoReturn<T1, T2, T3, T4> functorNoReturn, TaskType taskType, int taskCount){
		this.hasNoReturn = true;
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoFourArgs(FunctorFourArgsNoReturn<T1, T2, T3, T4> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, ParaTask.STAR);
	}
	
	TaskInfoFourArgs(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functorWithReturn, TaskType taskType, int taskCount) {
		this.hasNoReturn = false;
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoFourArgs(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functorWithReturn, TaskType taskType) {
		this(functorWithReturn, taskType, ParaTask.STAR);
	}
	
	public TaskID<R> start(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
		try{
			this.arg1 = arg1; this.arg2 = arg2; this.arg3 = arg3; this.arg4 = arg4;
					
			if(this.taskCount == 1)
				return TaskpoolFactory.getTaskpool().enqueue(this);
			else{
				TaskIDGroup<R> taskGroup = TaskpoolFactory.getTaskpool().enqueueMulti(this);
				return taskGroup;
			}
		}catch(IllegalArgumentException e){
			System.out.println("An exception occurred in TaskInfoFourArgs::start method!");
			System.out.println("The error might have been caused by passing unexpected parameters!");
			e.printStackTrace();
			return null;
		}
	}
	
	R execute(){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4);
		return null;
	}
}
