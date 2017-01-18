package reductionTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pu.RedLib.IntegerSum;
import pu.loopScheduler.AbstractLoopScheduler.LoopCondition;
import pu.loopScheduler.LoopScheduler;
import pu.loopScheduler.LoopSchedulerFactory;
import pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType;
import pu.loopScheduler.LoopRange;
import sp.annotations.Future;
import sp.annotations.TaskInfoType;

public class ReductionTest {
	
	IntegerSum intSum = new IntegerSum();
	
	@Future(reduction="union(reductionTest.MapSwap)")
	Map<String, Map<String, Integer>>[] myArray = new HashMap[5];
	
	public int function(LoopScheduler scheduler) throws InterruptedException{
		LoopRange range = scheduler.getChunk((int) Thread.currentThread().getId());
		int result = 0;
		for (int number = range.loopStart; number < range.loopEnd; number += range.localStride){
			Thread.sleep(number*1000);
			Random rand = new Random();
			int randNo = rand.nextInt(number);
			result += randNo;
		}
		return result;
	}
	
	public void method() throws InterruptedException{
		LoopScheduler scheduler = LoopSchedulerFactory.createLoopScheduler(0, 24, 1, Runtime.getRuntime().availableProcessors(), LoopCondition.LessThan, LoopSchedulingType.Static);
		@Future(taskType=TaskInfoType.MULTI, reduction="intSum")
		int num = function(scheduler);
		System.out.println("The result for num is: " + num);
	}
}
