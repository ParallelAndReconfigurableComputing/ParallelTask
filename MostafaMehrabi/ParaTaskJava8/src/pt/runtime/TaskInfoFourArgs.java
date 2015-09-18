package pt.runtime;

import pt.functionalInterfaces.FunctorFourArgsNoReturn;
import pt.functionalInterfaces.FunctorFourArgsWithReturn;

class TaskInfoFourArgs<R, T1, T2, T3, T4> extends TaskInfo<R> {

	private FunctorFourArgsNoReturn<T1, T2, T3, T4> functorNoReturn = null;
	private FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functorWithReturn = null;
	
	TaskInfoFourArgs(FunctorFourArgsNoReturn<T1, T2, T3, T4> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoFourArgs(FunctorFourArgsNoReturn<T1, T2, T3, T4> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoFourArgs(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functorWithReturn, TaskType taskType, int taskCount) {
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoFourArgs(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functorWithReturn, TaskType taskType) {
		this(functorWithReturn, taskType, STAR);
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4);
		return null;
	}
}
