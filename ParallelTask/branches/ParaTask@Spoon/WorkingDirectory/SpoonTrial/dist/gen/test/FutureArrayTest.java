package test;


public class FutureArrayTest {
    public static int foo(int i) {
        return i * 10;
    }

    public static void main(java.lang.String[] args) {
        test.ObjectTest tester = new test.ObjectTest(3);
        pt.runtime.TaskInfoNoArgs<Integer> __bbPtTask__ = ((pt.runtime.TaskInfoNoArgs<Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 4, 
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<Integer, >)() -> test.FutureArrayTest.foo(34))));
        pt.runtime.TaskID<Integer> __bbPtTaskID__ = __bbPtTask__.start();
        int n = 10;
        int[] array = new int[n];
        pt.runtime.TaskIDGroup<Integer> __arrayPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(n);
        for (int i = 0 ; i < 5 ; i++) {
            pt.runtime.TaskInfoOneArg<Integer, Integer> __aPtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 2, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> test.FutureArrayTest.foo(__iPtNonLambdaArg__))));
            pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, InterruptedException.class, ()->{try{foo(1);}catch(Exception e){e.printStackTrace();}});
            pt.runtime.TaskID<Integer> __aPtTaskID__ = __aPtTask__.start(i);
            __arrayPtTaskIDGroup__.setInnerTask(i, __aPtTaskID__);
        }
        pt.runtime.TaskInfoNoArgs<Integer> ____array_1__PtTask__ = ((pt.runtime.TaskInfoNoArgs<Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<Integer, >)() -> tester.get())));
        pt.runtime.TaskID<Integer> ____array_1__PtTaskID__ = ____array_1__PtTask__.start();
        __arrayPtTaskIDGroup__.setInnerTask(5, ____array_1__PtTaskID__);
        __arrayPtTaskIDGroup__.setInnerTask(6, __bbPtTaskID__);
        for (int i = 7 ; i < 10 ; i++) {
            pt.runtime.TaskInfoOneArg<Integer, Integer> ____array_2__PtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> test.FutureArrayTest.foo((__iPtNonLambdaArg__ * 15)))));
            pt.runtime.TaskID<Integer> ____array_2__PtTaskID__ = ____array_2__PtTask__.start(i);
            __arrayPtTaskIDGroup__.setInnerTask(i, ____array_2__PtTaskID__);
        }
        int[] myarray = new int[2];
        myarray[0] = tester.get();
        java.lang.System.out.println(array.length);
        try {
            __arrayPtTaskIDGroup__.waitTillFinished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int __ptLoopIndex1__ = 0 ; __ptLoopIndex1__ < n ; __ptLoopIndex1__++)
            array[__ptLoopIndex1__] = __arrayPtTaskIDGroup__.getInnerTaskResult(__ptLoopIndex1__);
        for (int i = 0 ; i < (array.length) ; i++) {
            java.lang.System.out.print(array[i]);
            if (i != ((array.length) - 1))
                java.lang.System.out.print(", ");
            
        }
        for (int i = 0 ; i < (array.length) ; i++) {
            test.FutureArrayTest.foo(array[i]);
        }
    }
}

