

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                myList.add(__numPtTaskID__.getReturnResult());
            
        }
        myList.add(__specialNumPtTaskID__);
        for (int counter = 0 ; counter < (myList.size()) ; counter++) {
            int num = myList.get(counter);
            System.out.println(((("get(Index): " + (myList.get(counter))) + ", and num: ") + num));
        }
    }
    
}

