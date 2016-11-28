

package smallProject;


public class FutureArray {
    pt.runtime.TaskIDGroup<Integer> __myArrayPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>();

    int __myArrayPtTaskIDGroupSize__;

    int[] myArray;

    int range = 5;

    public FutureArray(int num) {
        myArray = new int[num];
        __myArrayPtTaskIDGroupSize__ = num;
        range = num;
    }

    private int task(int i) {
        try {
            java.util.Random rand = new java.util.Random();
            int sleepTime = rand.nextInt(10);
            int multiplier = rand.nextInt(i);
            java.lang.Thread.sleep((sleepTime * 1000));
            java.lang.System.out.println((((("Thread " + (java.lang.Thread.currentThread().getId())) + " slept for ") + sleepTime) + " seconds"));
            return i * multiplier;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void processTasks() {
        for (int i = 0; i < (range); i++) {
            pt.runtime.TaskInfoOneArg<Integer, Integer> ____myArray_1__PtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> task((__iPtNonLambdaArg__ + 3)))));
            pt.runtime.TaskID<Integer> ____myArray_1__PtTaskID__ = ____myArray_1__PtTask__.start(i);
            __myArrayPtTaskIDGroup__.setInnerTask(i, ____myArray_1__PtTaskID__);
        }
        finishTask();
    }

    private void finishTask() {
        try {
            __myArrayPtTaskIDGroup__.waitTillFinished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int __ptLoopIndex1__ = 0; __ptLoopIndex1__ < __myArrayPtTaskIDGroupSize__; __ptLoopIndex1__++)
            myArray[__ptLoopIndex1__] = __myArrayPtTaskIDGroup__.getInnerTaskResult(__ptLoopIndex1__);
        
        for (int i = 0; i < (range); i++) {
            java.lang.System.out.println(((("The result of " + i) + "th task: ") + (myArray[i])));
        }
    }

    {
        pu.RedLib.IntegerSum __myArrayPtTaskReductionObject__ = new pu.RedLib.IntegerSum();
        pt.runtime.ParaTask.setReductionOperationForTaskIDGroup(__myArrayPtTaskIDGroup__, __myArrayPtTaskReductionObject__);
    }
}

