

package smallProject;


public class FutureArray {
    private pt.runtime.TaskIDGroup<Integer> __myArrayPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>();

    private volatile boolean __myArrayPtTaskIDGroup__Synchronized = false;

    private volatile java.util.concurrent.locks.Lock __myArrayPtTaskIDGroup__Lock = new java.util.concurrent.locks.ReentrantLock();

    int range = 5;

    private int[] myArray;

    pu.RedLib.IntegerSum newSum = new pu.RedLib.IntegerSum();

    java.lang.Integer newInt = new java.lang.Integer(0);

    public FutureArray(int num) {
        myArray = new int[num];
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
            if (!__myArrayPtTaskIDGroup__Synchronized) {
                pt.runtime.TaskInfoOneArg<Integer, Integer> ____myArray_1__PtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> task((__iPtNonLambdaArg__ + 3)))));
                pt.runtime.TaskID<Integer> ____myArray_1__PtTaskID__ = ____myArray_1__PtTask__.start(i);
                __myArrayPtTaskIDGroup__.setInnerTask(i, ____myArray_1__PtTaskID__);
            }else {
                myArray[i] = task((i + 3));
            }
        }
        int[] bigArray = new int[2];
        if (!__myArrayPtTaskIDGroup__Synchronized) {
            __myArrayPtTaskIDGroup__Lock.lock();
            if (!__myArrayPtTaskIDGroup__Synchronized) {
                try {
                    __myArrayPtTaskIDGroup__.waitTillFinished();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int __myArrayLoopIndex1__ = 0; __myArrayLoopIndex1__ < myArray.length; __myArrayLoopIndex1__++)
                    myArray[__myArrayLoopIndex1__] = __myArrayPtTaskIDGroup__.getInnerTaskResult(__myArrayLoopIndex1__);
                
                __myArrayPtTaskIDGroup__Synchronized = true;
            }
            __myArrayPtTaskIDGroup__Lock.unlock();
        }
        for (int i = 0; i < 2; i++) {
            bigArray[myArray[i]] = task((i + 1));
        }
        java.lang.Void dd = finishTask(myArray, 7);
    }

    public void processInnerTasks() {
        boolean setArray = true;
        int[] newArray;
        pt.runtime.TaskIDGroup<Integer> __newArrayPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>();
        if (setArray) {
            newArray = new int[7];
        }else
            newArray = new int[8];
        
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
        for (int __newArrayLoopIndex1__ = 0; __newArrayLoopIndex1__ < newArray.length; __newArrayLoopIndex1__++)
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

    {
        pt.runtime.ParaTask.registerReduction(__myArrayPtTaskIDGroup__, newSum);
    }
}

