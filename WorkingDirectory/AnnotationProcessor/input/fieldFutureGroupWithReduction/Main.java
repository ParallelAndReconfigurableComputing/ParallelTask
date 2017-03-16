package fieldFutureGroupWithReduction;

import apt.annotations.InitParaTask;

public class Main {

	@InitParaTask
	public static void main(String[] args){
		FieldFutureGroupClass object = new FieldFutureGroupClass(5);
		object.startTasks();
	}
}
