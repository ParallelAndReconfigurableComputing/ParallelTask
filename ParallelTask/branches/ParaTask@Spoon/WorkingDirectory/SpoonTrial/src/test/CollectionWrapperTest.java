package test;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import sp.annotations.Task;

public class CollectionWrapperTest {
    public static int simulateWork(int i) {
        Random rand = new Random();
        int random = rand.nextInt(10);
        try {
            System.out.println((((("Thread " + (Thread.currentThread().getId())) + " is going to sleep for ") + random) + " seconds."));
            Thread.sleep((random * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(((("Thread " + (Thread.currentThread().getId())) + " is returning ") + (i * random)));
        return i * random;
    }
    
    @Task
    public static int foo(int arg) {
        return arg;
    }
    
    public static int foox(int arg) {
        return arg;
    }
    
    public static void main(String[] args) {
        pt.runtime.TaskInfoNoArgs<Integer> __specialNumPtTask__ = ((pt.runtime.TaskInfoNoArgs<Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF ,
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<Integer>)() -> CollectionWrapperTest.simulateWork(-2))));
        pt.runtime.TaskID<Integer> __specialNumPtTaskID__ = __specialNumPtTask__.start();
        pt.runtime.ParaTask.processingInParallel(true);
        List<java.lang.Integer>  __myListPtTask__ = pt.runtime.ParaTask.getPtWrapper(new ArrayList<java.lang.Integer> ());
        pt.wrappers.PtListWrapper<java.lang.Integer> myList = ((pt.wrappers.PtListWrapper<java.lang.Integer>)(__myListPtTask__));
        for (int i = 0 ; i < 20 ; i++) {
            pt.runtime.TaskInfoOneArg<Integer, Integer> __numPtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF ,
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> CollectionWrapperTest.simulateWork(__iPtNonLambdaArg__))));
            pt.runtime.TaskID<Integer> __numPtTaskID__ = __numPtTask__.start(i);
            if ((i % 3) == 0)
                myList.add(i);
            else
                myList.add(__numPtTaskID__);
            
        }
        myList.add(__specialNumPtTaskID__);
        for (int counter = 0 ; counter < (myList.size()) ; counter++) {
            pt.runtime.TaskInfoNoArgs<Integer> __foo_2PtTask__ = ((pt.runtime.TaskInfoNoArgs<Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF ,
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<Integer>)() -> (CollectionWrapperTest.foo(0)))));
            pt.runtime.TaskID<Integer> __foo_2PtTaskID__ = __foo_2PtTask__.start();
            int num = myList.get(((CollectionWrapperTest.foox(counter)) + __foo_2PtTaskID__.getReturnResult()));
            pt.runtime.TaskInfoOneArg<Integer, Integer> __foo_1PtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF ,
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__counterPtNonLambdaArg__) -> CollectionWrapperTest.foo(__counterPtNonLambdaArg__))));
            pt.runtime.TaskID<Integer> __foo_1PtTaskID__ = __foo_1PtTask__.start(counter);
            System.out.println(((("get(Index): " + (myList.get(__foo_1PtTaskID__))) + ", and num: ") + num));
        }
    }
    
}

