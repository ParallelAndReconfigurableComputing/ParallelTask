package test;


public class CollectionWrapperTest {
    public static int simulateWork(int i) {
        java.util.Random rand = new java.util.Random();
        int random = rand.nextInt(10);
        try {
            java.lang.System.out.println((((("Thread " + (java.lang.Thread.currentThread().getId())) + " is going to sleep for ") + random) + " seconds."));
            java.lang.Thread.sleep((random * 1000));
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        java.lang.System.out.println(((("Thread " + (java.lang.Thread.currentThread().getId())) + " is returning ") + (i * random)));
        return i * random;
    }

    @sp.annotations.Task
    public static int foo(int arg) {
        return arg;
    }

    public static int foox(int arg) {
        return arg;
    }

    public static void main(java.lang.String[] args) {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingType.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        pt.runtime.TaskInfoNoArgs<Integer> __specialNumPtTask__ = ((pt.runtime.TaskInfoNoArgs<Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorNoArgsWithReturn<Integer>)() -> test.CollectionWrapperTest.simulateWork((-2)))));
        pt.runtime.TaskID<Integer> __specialNumPtTaskID__ = __specialNumPtTask__.start();
        java.util.List<java.lang.Integer> myList = pt.runtime.ParaTask.getPtWrapper(new java.util.ArrayList<java.lang.Integer>());
        for (int i = 0 ; i < 20 ; i++) {
            pt.runtime.TaskInfoOneArg<Integer, Integer> __numPtTask__ = ((pt.runtime.TaskInfoOneArg<Integer, Integer>)(pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, 
			(pt.functionalInterfaces.FunctorOneArgWithReturn<Integer, Integer>)(__iPtNonLambdaArg__) -> test.CollectionWrapperTest.simulateWork(__iPtNonLambdaArg__))));
            pt.runtime.TaskID<Integer> __numPtTaskID__ = __numPtTask__.start(i);
            if ((i % 3) == 0)
                myList.add(i);
            else
                myList.add(__numPtTaskID__);
            
        }
        myList.add(__specialNumPtTaskID__);
        myList.add((__specialNumPtTaskID__.getReturnResult() + 2));
        for (int counter = 0 ; counter < (myList.size()) ; counter++) {
            int num = myList.get(((test.CollectionWrapperTest.foox(counter)) + (test.CollectionWrapperTest.foo(0))));
            java.lang.System.out.println(((("get(Index): " + (myList.get(test.CollectionWrapperTest.foo(counter)))) + ", and num: ") + num));
        }
    }
}

