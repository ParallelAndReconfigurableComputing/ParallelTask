package code;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pt.runtime.TaskThread;
import pu.loopScheduler.AbstractLoopScheduler.LoopCondition;
import pu.loopScheduler.LoopRange;
import pu.loopScheduler.LoopScheduler;
import pu.loopScheduler.LoopSchedulerFactory;
import pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType;
import apt.annotations.Future;
import apt.annotations.InitParaTask;
import apt.annotations.TaskInfoType;

public class ReductionTest {
	
	public int foo(int x){
		return x*10;
	}
	
	public int multiTask(LoopScheduler scheduler){
		TaskThread taskThread = (TaskThread) Thread.currentThread();
		LoopRange range = scheduler.getChunk(taskThread.getThreadID());
		Random rand = new Random();
		int result = 0;
		for(int i = range.loopStart; i < range.loopEnd; i += range.localStride){
			int randomNo = 0;
			if(i != 0)
				randomNo = rand.nextInt(i);
			result += foo(randomNo);
		}
		return result;
	}
	
	public Map<String, Integer> mapMaker(String str){
		Map<String, Integer> newMap = new HashMap<>();
		newMap.put(str, 20);
		return newMap;
	}
	
	@InitParaTask()
	public void process(int range){
		
		LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, range, 1, Runtime.getRuntime().availableProcessors(),
				LoopCondition.LessThan, LoopSchedulingType.Static);
		
		@Future(taskType=TaskInfoType.MULTI, reduction="sum")
		int task = multiTask(scheduler);
		
		System.out.println("The result of task is: " + task);
		
		@Future(taskType=TaskInfoType.MULTI, reduction="union(min)")
		Map<String, Integer> task2 = mapMaker("HI");
		
		System.out.println("The result for task2 is: " + task2.get("HI"));
	}
	
	public static void main(String[] args){
	  	ReductionTest test = new ReductionTest();
	  	test.process(20);
	}
}
