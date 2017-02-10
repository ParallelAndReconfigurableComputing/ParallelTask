

package smallProject;


public class FutureArray {
    int __myArrayPtTaskIDGroupSize__;

    int[] myArray;

    pu.RedLib.IntegerSum newSum = new pu.RedLib.IntegerSum();

    java.lang.Integer newInt = new java.lang.Integer(0);

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
            myArray[i] = task((i + 3));
        }
        int[] bigArray = new int[2];
        bigArray[myArray[0]] = task(1);
        bigArray[myArray[1]] = task(2);
        java.lang.Void dd = finishTask(myArray, 7);
    }

    public void processInnerTasks() {
        int[] newArray = new int[7];
        pt.runtime.TaskIDGroup<Integer> __newArrayPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(7);
        for (int i = 0; i < (newArray.length); i++) {
            pt.runtime.TaskInfoOneArg<Integer, Integer> ____newArray_1__PtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> task(__iPtNonLambdaArg__))));
            pt.runtime.TaskID<Integer> ____newArray_1__PtTaskID__ = ____newArray_1__PtTask__.start(i);
            __newArrayPtTaskIDGroup__.setInnerTask(i, ____newArray_1__PtTaskID__);
        }
        try {
            __newArrayPtTaskIDGroup__.waitTillFinished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int __newArrayLoopIndex1__ = 0; __newArrayLoopIndex1__ < 7; __newArrayLoopIndex1__++)
            newArray[__newArrayLoopIndex1__] = __newArrayPtTaskIDGroup__.getInnerTaskResult(__newArrayLoopIndex1__);
        
        java.lang.System.out.println((((newArray[0]) + ", ") + (newArray[2])));
        newArray[4] = task(100);
        newArray[6] = task(200);
    }

    private java.lang.Void finishTask(int[] array, int index) {
        for (int i = 0; i < (range); i++) {
            java.lang.System.out.println(((("The result of " + i) + "th task: ") + (array[i])));
        }
        return null;
    }
}

