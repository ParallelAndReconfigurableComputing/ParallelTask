package pTAnnotation;

import pt.runtime.ParaTask;
import pt.runtime.ParaTask.PTSchedulingPolicy;
import pt.runtime.WorkerThread;
import pu.loopScheduler.AbstractLoopScheduler.LoopCondition;
import pu.loopScheduler.LoopRange;
import pu.loopScheduler.LoopScheduler;
import pu.loopScheduler.LoopSchedulerFactory;
import apt.annotations.Future;
import apt.annotations.TaskInfoType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class PTAnnotationLusearchImplementationAnnotated{
	
	int numOfThreads;
	int repeatFactor;
	int counter;
	@Future
	Void[] futureGroup;
	public PTAnnotationLusearchImplementationAnnotated(int type, int numOfThreads, int repeatFactor) {
		this.numOfThreads = numOfThreads;
		this.repeatFactor = repeatFactor;
		futureGroup = new Void[repeatFactor];
		counter = 0;
		ParaTask.PTSchedulingPolicy scheduleType = PTSchedulingPolicy.MixedSchedule;
		switch(type){
		case 1:
			scheduleType = PTSchedulingPolicy.WorkStealing;
			break;
		case 2:
			scheduleType = PTSchedulingPolicy.WorkSharing;
			break;
		}
		ParaTask.init(scheduleType, numOfThreads);
	}

	
	public void search() {
		
		LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, 10, 1, numOfThreads, LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
		
		@Future(taskType=TaskInfoType.MULTI)
		Void multiTask = searchTask(scheduler);
		
		futureGroup[counter++] = multiTask;
	}

	public Void searchTask(LoopScheduler scheduler) {
		
		return null;
	}
	
	
	public void waitTillFinished(){
		Long start = System.currentTimeMillis();
		Void temp = futureGroup[0];
		Long end = System.currentTimeMillis();
		System.out.println("barrier took: " + (end - start) + " milliseconds.");
	}
}
