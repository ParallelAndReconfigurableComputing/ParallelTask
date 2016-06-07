


public class FutureArrayTest {
    public static int foo(int i) {
        return i * 10;
    }
    
    public static void main(String[] args) {
        pt.runtime.TaskInfoNoArgs<Integer> __bbPtTask__ = ((pt.runtime.TaskInfoNoArgs<Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF ,4 ,
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<Integer>)() -> FutureArrayTest.foo(34))));
        pt.runtime.TaskID<Integer> __bbPtTaskID__ = __bbPtTask__.start();
        int n = 10;
        int[] array = new int[n];
        pt.runtime.TaskIDGroup<Integer> __arrayPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(n);
        for (int i = 0 ; i < 5 ; i++) {
            pt.runtime.TaskInfoOneArg<Integer, Integer> __aPtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF ,2 ,
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> FutureArrayTest.foo(__iPtNonLambdaArg__))));
            pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, IllegalArgumentException.class, ()->{try{foo1(1, 2);}catch(Exception e){e.printStackTrace();}});
            pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, InterruptedException.class, ()->{try{foo(1);}catch(Exception e){e.printStackTrace();}});
            pt.runtime.TaskID<Integer> __aPtTaskID__ = __aPtTask__.start(i);
            __arrayPtTaskIDGroup__.setInnerTask(i, __aPtTaskID__);
        }
        array[5] = FutureArrayTest.foo(300);
        array[6] = FutureArrayTest.foo(125);
        for (int i = 7 ; i < 10 ; i++) {
            array[i] = FutureArrayTest.foo((i * 15));
        }
        int[] myarray = new int[2];
        myarray[0] = 1;
        System.out.println(array.length);
        try {
            __arrayPtTaskIDGroup__.waitTillFinished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0 ; i < (array.length) ; i++) {
            System.out.print(array[i]);
            if (i != ((array.length) - 1))
                System.out.print(", ");
            
        }
        for (int i = 0 ; i < (array.length) ; i++) {
            FutureArrayTest.foo(array[i]);
        }
    }
    
}

