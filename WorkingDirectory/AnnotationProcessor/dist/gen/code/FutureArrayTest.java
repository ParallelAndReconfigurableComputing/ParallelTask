package code;


public class FutureArrayTest {
    @sp.annotations.Future
    java.util.List<java.lang.Integer> myHybridList = pt.runtime.ParaTask.getPtWrapper(new java.util.ArrayList<java.lang.Integer>());

    public static int foo(int i) {
        return i * 10;
    }

    public static void main(java.lang.String[] args) {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingType.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        code.ObjectTest tester = new code.ObjectTest(3);
        pt.runtime.TaskInfoNoArgs<Integer> __bbPtTask__ = ((pt.runtime.TaskInfoNoArgs<Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 4, 
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<Integer>)() -> code.FutureArrayTest.foo(34))));
        pt.runtime.TaskID<Integer> __bbPtTaskID__ = __bbPtTask__.start();
        int n = 10;
        int[] array = new int[n];
        pt.runtime.TaskIDGroup<Integer> __arrayPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(n);
        for (int i = 0 ; i < 5 ; i++) {
            pt.runtime.TaskInfoOneArg<Integer, Integer> __aPtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 2, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> code.FutureArrayTest.foo(__iPtNonLambdaArg__))));
            pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, java.lang.InterruptedException.class, ()->{try{foo(1);}catch(Exception e){e.printStackTrace();}});
            pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, java.io.IOException.class, ()->{try{foo(2);}catch(Exception e){e.printStackTrace();}});
            pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, java.lang.RuntimeException.class, ()->{try{foo(3);}catch(Exception e){e.printStackTrace();}});
            pt.runtime.TaskID<Integer> __aPtTaskID__ = __aPtTask__.start(i);
            array[i] = __aPtTaskID__.getReturnResult();
        }
        array[5] = tester.get();
        array[6] = __bbPtTaskID__.getReturnResult();
        for (int i = 7 ; i < 10 ; i++) {
            array[i] = code.FutureArrayTest.foo((i * 15));
        }
        int[] myarray = new int[2];
        myarray[0] = tester.get();
        java.lang.System.out.println(array.length);
        for (int i = 0 ; i < (array.length) ; i++) {
            java.lang.System.out.print(array[i]);
            if (i != ((array.length) - 1))
                java.lang.System.out.print(", ");
            
        }
        try {
            __arrayPtTaskIDGroup__.waitTillFinished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int __ptLoopIndex1__ = 0 ; __ptLoopIndex1__ < n ; __ptLoopIndex1__++)
            array[__ptLoopIndex1__] = __arrayPtTaskIDGroup__.getInnerTaskResult(__ptLoopIndex1__);
        for (int i = 0 ; i < (array.length) ; i++) {
            code.FutureArrayTest.foo(array[i]);
        }
    }
}

