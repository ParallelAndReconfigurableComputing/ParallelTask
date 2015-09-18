package pt.runtime;

import pt.functionalInterfaces.FunctorSixArgsNoReturn;
import pt.functionalInterfaces.FunctorSixArgsWithReturn;

class TaskInfoSixArgs<R, T1, T2, T3, T4, T5, T6> extends TaskInfo<R> {

	private FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functorNoReturn = null;
	private FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functorWithReturn = null;
	
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
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6){
		if(this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6);
		return null;  
	}
}
