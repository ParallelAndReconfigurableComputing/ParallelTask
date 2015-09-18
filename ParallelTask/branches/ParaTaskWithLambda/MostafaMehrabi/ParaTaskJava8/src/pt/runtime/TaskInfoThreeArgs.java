package pt.runtime;

import pt.functionalInterfaces.FunctorThreeArgsNoReturn;
import pt.functionalInterfaces.FunctorThreeArgsWithReturn;

class TaskInfoThreeArgs<R, T1, T2, T3> extends TaskInfo<R> {

	private FunctorThreeArgsNoReturn<T1, T2, T3> functorNoReturn = null;
	private FunctorThreeArgsWithReturn<R, T1, T2, T3> functorWithReturn = null;
	
	TaskInfoThreeArgs(FunctorThreeArgsNoReturn<T1, T2, T3> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoThreeArgs(FunctorThreeArgsNoReturn<T1, T2, T3> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoThreeArgs(FunctorThreeArgsWithReturn<R, T1, T2, T3> functorWithReturn, TaskType taskType, int taskCount){
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	public TaskInfoThreeArgs(FunctorThreeArgsWithReturn<R, T1, T2, T3> functorWithReturn, TaskType taskTyp) {
		this(functorWithReturn, taskTyp, STAR);
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3);
		this.functorNoReturn.exec(arg1, arg2, arg3);
		return null;
	}
}
