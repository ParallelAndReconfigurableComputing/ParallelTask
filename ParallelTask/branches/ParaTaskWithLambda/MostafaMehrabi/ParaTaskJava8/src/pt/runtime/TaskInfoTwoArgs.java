package pt.runtime;

import pt.functionalInterfaces.FunctorTwoArgsNoReturn;
import pt.functionalInterfaces.FunctorTwoArgsWithReturn;

class TaskInfoTwoArgs<R, T1, T2> extends TaskInfo<R> {
	
	private FunctorTwoArgsNoReturn<T1, T2> functorNoReturn = null;
	private FunctorTwoArgsWithReturn<R, T1, T2> functorWithReturn = null;
	
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
	
	R execute(T1 arg1, T2 arg2){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2);
		this.functorNoReturn.exec(arg1, arg2);
		return null;
	}
}
