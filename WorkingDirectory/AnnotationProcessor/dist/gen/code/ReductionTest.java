

package code;


public class ReductionTest {
    public int foo(int x) {
        return x * 10;
    }

    public int multiTask(pu.loopScheduler.LoopScheduler scheduler) {
        pt.runtime.TaskThread taskThread = ((pt.runtime.TaskThread) (java.lang.Thread.currentThread()));
        pu.loopScheduler.LoopRange range = scheduler.getChunk(taskThread.getThreadID());
        java.util.Random rand = new java.util.Random();
        int result = 0;
        for (int i = range.loopStart; i < (range.loopEnd); i += range.localStride) {
            int randomNo = 0;
            if (i != 0)
                randomNo = rand.nextInt(i);
            
            result += foo(randomNo);
        }
        return result;
    }

    public java.util.Map<java.lang.String, java.lang.Integer> mapMaker(java.lang.String str) {
        java.util.Map<java.lang.String, java.lang.Integer> newMap = new java.util.HashMap<>();
        newMap.put(str, 20);
        return newMap;
    }

    public void process(int range) {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        pu.loopScheduler.LoopScheduler scheduler = pu.loopScheduler.LoopSchedulerFactory.createLoopScheduler(0, range, 1, java.lang.Runtime.getRuntime().availableProcessors(), pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
        pt.runtime.TaskInfoOneArg<Integer, pu.loopScheduler.LoopScheduler> __taskPtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, pu.loopScheduler.LoopScheduler>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, pu.loopScheduler.LoopScheduler>)(__schedulerPtNonLambdaArg__) -> multiTask(__schedulerPtNonLambdaArg__))));
        pt.runtime.TaskIDGroup<Integer> __taskPtTaskID__ = (pt.runtime.TaskIDGroup<Integer>)__taskPtTask__.start(scheduler);
        pu.RedLib.IntegerSum __taskPtTaskReductionObject__ = new pu.RedLib.IntegerSum();
        pt.runtime.ParaTask.registerReduction(__taskPtTaskID__, __taskPtTaskReductionObject__);
        java.lang.System.out.println(("The result of task is: " + __taskPtTaskID__.getReturnResult()));
        pt.runtime.TaskInfoNoArgs<java.util.Map<java.lang.String, java.lang.Integer>> __task2PtTask__ = ((pt.runtime.TaskInfoNoArgs<java.util.Map<java.lang.String, java.lang.Integer>>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, 
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<java.util.Map<java.lang.String, java.lang.Integer>>)() -> mapMaker("HI"))));
        pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.lang.Integer>> __task2PtTaskID__ = (pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.lang.Integer>>)__task2PtTask__.start();
        pu.RedLib.MapUnion<java.lang.String, java.lang.Integer> __task2PtTaskReductionObject__ = new pu.RedLib.MapUnion<java.lang.String, java.lang.Integer>(new pu.RedLib.IntegerMinimum());
        pt.runtime.ParaTask.registerReduction(__task2PtTaskID__, __task2PtTaskReductionObject__);
        java.lang.System.out.println(("The result for task2 is: " + (__task2PtTaskID__.getReturnResult().get("HI"))));
    }

    public static void main(java.lang.String[] args) {
        code.ReductionTest test = new code.ReductionTest();
        test.process(20);
    }
}

