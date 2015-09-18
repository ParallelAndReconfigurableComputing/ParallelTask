package pt.runtime;


import pt.functionalInterfaces.FunctorOneArgNoReturn;
import pt.functionalInterfaces.FunctorOneArgWithReturn;
import pt.runtime.TaskInfo;

class TaskInfoOneArg<R, T1> extends TaskInfo<R> {
	
	private FunctorOneArgNoReturn<T1> functorNoReturn = null;
	private FunctorOneArgWithReturn<R, T1> functorWithReturn = null;
	
	TaskInfoOneArg(FunctorOneArgNoReturn<T1> functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoOneArg(FunctorOneArgNoReturn<T1> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoOneArg(FunctorOneArgWithReturn<R, T1> functorWithReturn, TaskType taskType, int taskCount){
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoOneArg(FunctorOneArgWithReturn<R, T1> functorWithReturn, TaskType taskType){
		this(functorWithReturn, taskType, STAR);
	}
	
	R execute(T1 arg1){
		if (functorWithReturn!=null)
			return functorWithReturn.exec(arg1);
		functorNoReturn.exec(arg1);
		return null;
	}
}
