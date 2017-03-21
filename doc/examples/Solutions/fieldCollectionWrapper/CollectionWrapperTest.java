

package fieldCollectionWrapper;


public class CollectionWrapperTest {
    static volatile pt.hybridWrappers.PtHybridList<java.lang.Integer> myList;

    static volatile java.util.List<java.lang.Integer> __myListPtTask__;

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

    @apt.annotations.Task
    public static int foo(int arg) {
        return arg;
    }

    public static int foox(int arg) {
        return arg;
    }

    public static void main(java.lang.String[] args) {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        pt.runtime.TaskInfoNoArgs<java.lang.Integer> __specialNumPtTask__ = ((pt.runtime.TaskInfoNoArgs<java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorNoArgsWithReturn<java.lang.Integer>) (() -> fieldCollectionWrapper.CollectionWrapperTest.simulateWork((-2)))))));
        pt.runtime.TaskID<java.lang.Integer> __specialNumPtTaskID__ = __specialNumPtTask__.start();
        for (int i = 0; i < 20; i++) {
            pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer> __numPtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, java.lang.Integer>) (( __iPtNonLambdaArg__) -> fieldCollectionWrapper.CollectionWrapperTest.simulateWork(__iPtNonLambdaArg__))))));
            pt.runtime.TaskID<java.lang.Integer> __numPtTaskID__ = __numPtTask__.start(i);
            if ((i % 3) == 0)
                fieldCollectionWrapper.CollectionWrapperTest.myList.add(i);
            else
                fieldCollectionWrapper.CollectionWrapperTest.myList.add(__numPtTaskID__);
            
        }
        fieldCollectionWrapper.CollectionWrapperTest.myList.add(__specialNumPtTaskID__);
        fieldCollectionWrapper.CollectionWrapperTest.myList.add(((__specialNumPtTaskID__.getReturnResult()) + 2));
        for (int counter = 0; counter < (fieldCollectionWrapper.CollectionWrapperTest.myList.size()); counter++) {
            pt.runtime.TaskInfoNoArgs<java.lang.Integer> __foo_2PtTask__ = ((pt.runtime.TaskInfoNoArgs<java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorNoArgsWithReturn<java.lang.Integer>) (() -> fieldCollectionWrapper.CollectionWrapperTest.foo(0))))));
            pt.runtime.TaskID<java.lang.Integer> __foo_2PtTaskID__ = __foo_2PtTask__.start();
            int num = fieldCollectionWrapper.CollectionWrapperTest.myList.get(((fieldCollectionWrapper.CollectionWrapperTest.foox(counter)) + (__foo_2PtTaskID__.getReturnResult())));
            pt.runtime.TaskInfoNoArgs<java.lang.Integer> __foo_3PtTask__ = ((pt.runtime.TaskInfoNoArgs<java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorNoArgsWithReturn<java.lang.Integer>) (() -> fieldCollectionWrapper.CollectionWrapperTest.foo(12))))));
            pt.runtime.TaskID<java.lang.Integer> __foo_3PtTaskID__ = __foo_3PtTask__.start();
            int num2 = fieldCollectionWrapper.CollectionWrapperTest.myList.get(__foo_3PtTaskID__);
            pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer> __foo_1PtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, java.lang.Integer>) (( __counterPtNonLambdaArg__) -> fieldCollectionWrapper.CollectionWrapperTest.foo(__counterPtNonLambdaArg__))))));
            pt.runtime.TaskID<java.lang.Integer> __foo_1PtTaskID__ = __foo_1PtTask__.start(counter);
            java.lang.System.out.println(((("get(Index): " + (fieldCollectionWrapper.CollectionWrapperTest.myList.get(__foo_1PtTaskID__))) + ", and num: ") + (num + num2)));
        }
    }

    static {
        pt.runtime.ParaTask.processingInParallel(true);
        fieldCollectionWrapper.CollectionWrapperTest.__myListPtTask__ = pt.runtime.ParaTask.getPtHybridWrapper(new java.util.ArrayList<java.lang.Integer>());
        fieldCollectionWrapper.CollectionWrapperTest.myList = ((pt.hybridWrappers.PtHybridList<java.lang.Integer>) (fieldCollectionWrapper.CollectionWrapperTest.__myListPtTask__));
    }
}

