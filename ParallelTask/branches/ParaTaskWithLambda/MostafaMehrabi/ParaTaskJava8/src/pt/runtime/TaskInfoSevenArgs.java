package pt.runtime;

import pt.functionalInterfaces.FunctorSevenArgsNoReturn;
import pt.functionalInterfaces.FunctorSevenArgsWithReturn;

class TaskInfoSevenArgs<R, T1, T2, T3, T4, T5, T6, T7> extends TaskInfo<R> {

	private FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functorNoReturn = null;
	private FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functorWithReturn = null;
	
	TaskInfoSevenArgs(FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functorNoReturn, TaskType taskType, int taskCount){		
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoSevenArgs(FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoSevenArgs(FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functorWithReturn, TaskType taskType, int taskCount){
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoSevenArgs(FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functorWithReturn, TaskType taskType){
		this(functorWithReturn, taskType, STAR);
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return null;
	}
	
}
