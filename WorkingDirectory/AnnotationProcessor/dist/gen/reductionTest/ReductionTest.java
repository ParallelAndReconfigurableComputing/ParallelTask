

package reductionTest;


public class ReductionTest {
    pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>> __myArrayPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>();

    pu.RedLib.IntegerSum intSum = new pu.RedLib.IntegerSum();

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>[] myArray = new java.util.HashMap[5];

    public int function(pu.loopScheduler.LoopScheduler scheduler) throws java.lang.InterruptedException {
        pu.loopScheduler.LoopRange range = scheduler.getChunk(((int) (java.lang.Thread.currentThread().getId())));
        int result = 0;
        for (int number = range.loopStart; number < (range.loopEnd); number += range.localStride) {
            java.lang.Thread.sleep((number * 1000));
            java.util.Random rand = new java.util.Random();
            int randNo = rand.nextInt(number);
            result += randNo;
        }
        return result;
    }

    public void method() throws java.lang.InterruptedException {
        pu.loopScheduler.LoopScheduler scheduler = pu.loopScheduler.LoopSchedulerFactory.createLoopScheduler(0, 24, 1, java.lang.Runtime.getRuntime().availableProcessors(), pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
        pt.runtime.TaskInfoOneArg<java.lang.Integer, pu.loopScheduler.LoopScheduler> __numPtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, pu.loopScheduler.LoopScheduler>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, pu.loopScheduler.LoopScheduler>) (( __schedulerPtNonLambdaArg__) -> {
            try {
                return function(__schedulerPtNonLambdaArg__);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
                return null;
            }
        })))));
        pt.runtime.TaskIDGroup<java.lang.Integer> __numPtTaskID__ = ((pt.runtime.TaskIDGroup<java.lang.Integer>) (__numPtTask__.start(scheduler)));
        pt.runtime.ParaTask.setReductionOperationForTaskIDGroup(__numPtTaskID__, intSum);
        java.lang.System.out.println(("The result for num is: " + (__numPtTaskID__.getReturnResult())));
    }

    {
        pu.RedLib.MapUnion<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> __myArrayPtTaskReductionObject__ = new pu.RedLib.MapUnion<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>(new reductionTest.MapSwap<java.lang.String, java.lang.Integer>());
        pt.runtime.ParaTask.setReductionOperationForTaskIDGroup(__myArrayPtTaskIDGroup__, __myArrayPtTaskReductionObject__);
    }
}

