package pt.runtime;


import pt.functionalInterfaces.FunctorOneArgNoReturn;
import pt.functionalInterfaces.FunctorOneArgWithReturn;
import pt.runtime.ParaTask.TaskType;
import pt.runtime.TaskInfo;

public class TaskInfoOneArg<R, T1> extends TaskInfo<R> {
	
	private T1 arg1;
	
	private FunctorOneArgNoReturn<T1> functorNoReturn = null;
	private FunctorOneArgWithReturn<R, T1> functorWithReturn = null;
	
	TaskInfoOneArg(FunctorOneArgNoReturn<T1> functorNoReturn, TaskType taskType, int taskCount){
		this.hasNoReturn = true;
		this.functorNoReturn = functorNoReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoOneArg(FunctorOneArgNoReturn<T1> functorNoReturn, TaskType taskType){
		this(functorNoReturn, taskType, ParaTask.STAR);
	}
	
	TaskInfoOneArg(FunctorOneArgWithReturn<R, T1> functorWithReturn, TaskType taskType, int taskCount){
		this.hasNoReturn = false;
		this.functorWithReturn = functorWithReturn;
		this.rudimentarySetup(taskType, taskCount);
	}
	
	TaskInfoOneArg(FunctorOneArgWithReturn<R, T1> functorWithReturn, TaskType taskType){
		this(functorWithReturn, taskType, ParaTask.STAR);
	}
	
	public TaskID<R> start(T1 arg1) {
		try{
			this.arg1 = arg1;
			
			if(this.taskCount == 1)
				return TaskpoolFactory.getTaskpool().enqueue(this);
			else{
				TaskIDGroup<R> taskGroup = TaskpoolFactory.getTaskpool().enqueueMulti(this);
				return taskGroup;
			}
		}catch(IllegalArgumentException e){
			System.out.println("An exception occurred in TaskInfoOneArg::start method!");
			System.out.println("The error might have been caused by passing unexpected parameters!");
			e.printStackTrace();
			return null;
		}
	}
	
	R execute(){
		if (functorWithReturn!=null)
			return functorWithReturn.exec(arg1);
		functorNoReturn.exec(arg1);
		return null;
	}
}
