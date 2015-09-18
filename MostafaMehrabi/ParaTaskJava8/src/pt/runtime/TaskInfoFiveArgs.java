package pt.runtime;

import pt.functionalInterfaces.FunctorFiveArgsNoReturn;
import pt.functionalInterfaces.FunctorFiveArgsWithReturn;

class TaskInfoFiveArgs<R, T1, T2, T3, T4, T5> extends TaskInfo<R> {

	private FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functorNoReturn = null;
	private FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functorWithReturn = null;
	
	TaskInfoFiveArgs(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoFiveArgs(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functorNoReturn, TaskType taskType) {
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoFiveArgs(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functorWithReturn, TaskType taskType, int taskCount) {
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoFiveArgs(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functorWithReturn, TaskType taskType) {
		this(functorWithReturn, taskType, STAR);
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5){
		if(this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5);
		return null;
	}
}
