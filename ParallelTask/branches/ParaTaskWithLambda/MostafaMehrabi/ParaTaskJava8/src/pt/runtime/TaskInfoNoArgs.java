package pt.runtime;

import pt.functionalInterfaces.FunctorNoArgsNoReturn;
import pt.functionalInterfaces.FunctorNoArgsWithReturn;

class TaskInfoNoArgs<R> extends TaskInfo<R> {
	
	private FunctorNoArgsNoReturn functorNoReturn = null;
	private FunctorNoArgsWithReturn<R> functorWithReturn = null;
	
	TaskInfoNoArgs(FunctorNoArgsNoReturn functorNoReturn, TaskType taskType, int taskCount){
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	/*taskCount is only considered when a task is Multi, otherwise it is always 1.
	However, if this rule changes, then this implementation needs to change as well.*/			
	TaskInfoNoArgs(FunctorNoArgsNoReturn functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, STAR);
	}
	
	TaskInfoNoArgs(FunctorNoArgsWithReturn<R> functorWithReturn, TaskType taskType, int taskCount){
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoNoArgs(FunctorNoArgsWithReturn<R> functorWithReturn, TaskType taskType){
		this(functorWithReturn, taskType, STAR);
	}
	
	R execute(){
		if (this.functorWithReturn != null)
			return this.functorWithReturn.exec();
		this.functorNoReturn.exec();
		return null;
	}
}
