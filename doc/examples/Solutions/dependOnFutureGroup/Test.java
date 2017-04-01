

package dependOnFutureGroup;


public class Test {
    private int IOTask(int num) throws java.lang.InterruptedException {
        int randNo = 0;
        java.util.Random rand = new java.util.Random();
        if (num == 0)
            randNo = 1;
        else
            randNo = 6 * (rand.nextInt(num));
        
        java.lang.System.out.println(((((((num + 1) + "- thread ") + (java.lang.Thread.currentThread().getId())) + " is going to sleep for ") + randNo) + " seconds."));
        java.lang.Thread.sleep((randNo * 1000));
        java.lang.System.out.println(((((((num + 1) + "-> thread ") + (java.lang.Thread.currentThread().getId())) + " slept for ") + randNo) + " seconds"));
        return randNo;
    }

    private java.util.Map<java.lang.String, java.lang.Integer> wordCountTask(java.util.concurrent.ConcurrentLinkedDeque<java.lang.String> wordList) {
        java.util.Map<java.lang.String, java.lang.Integer> result = new java.util.HashMap<>();
        for (java.lang.String word : wordList) {
            result.putAll(wordToInt(word));
        }
        return result;
    }

    private java.util.Map<java.lang.String, java.lang.Integer> wordToInt(java.lang.String word) {
        int multiplier = ((int) (java.lang.Thread.currentThread().getId()));
        int wordLetters = (word.length()) * multiplier;
        word = (word + ", ") + multiplier;
        java.util.Map<java.lang.String, java.lang.Integer> wordLength = new java.util.HashMap<>();
        wordLength.put(word, wordLetters);
        return wordLength;
    }

    private java.lang.Void printAllMaps(java.util.Map<java.lang.String, java.lang.Integer>[] maps) {
        int counter = 1;
        for (java.util.Map<java.lang.String, java.lang.Integer> map : maps) {
            java.lang.System.out.println(("For map number " + (counter++)));
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : map.entrySet()) {
                java.lang.System.out.println(((((entry.getKey()) + ", ") + (entry.getValue())) + "\n"));
            }
        }
        return null;
    }

    private java.lang.Void updateGui() {
        java.lang.System.out.println(("GUI updated by thread " + (java.lang.Thread.currentThread().getId())));
        return null;
    }

    private java.lang.Void updateDB() {
        java.lang.System.out.println(("DataBase updated by thread " + (java.lang.Thread.currentThread().getId())));
        return null;
    }

    private java.lang.Void reportResult(int sum) {
        java.lang.System.out.println(("Sum of the results is: " + sum));
        return null;
    }

    public static void main(java.lang.String[] args) {
        dependOnFutureGroup.Test tester = new dependOnFutureGroup.Test();
        tester.testFutureGroup(10);
        tester.testParallelFuture();
    }

    public void testFutureGroup(int num) {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        int[] group = new int[num];
        pt.runtime.TaskIDGroup<java.lang.Integer> __groupPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(num);
        pt.runtime.ParaTask.executeInterimHandler(()->updateGui());
        pt.runtime.ParaTask.executeInterimHandler(()->updateDB());
        try {
            for (int i = 0; i < num; i++) {
                pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer> ____group_1__PtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, java.lang.Integer>) (( __iPtNonLambdaArg__) -> IOTask(__iPtNonLambdaArg__))))));
                pt.runtime.ParaTask.registerAsyncCatch(____group_1__PtTask__, java.lang.InterruptedException.class, ( e) -> {
                    e.printStackTrace();
                });
                pt.runtime.TaskID<java.lang.Integer> ____group_1__PtTaskID__ = ____group_1__PtTask__.start(i);
                __groupPtTaskIDGroup__.setInnerTask(i, ____group_1__PtTaskID__);
            }
            pt.runtime.TaskInfoOneArg<java.lang.Integer, pt.runtime.TaskIDGroup<java.lang.Integer>> __reducePtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, pt.runtime.TaskIDGroup<java.lang.Integer>>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, pt.runtime.TaskIDGroup<java.lang.Integer>>) (( __groupPtLambdaArg__) -> pu.RedLib.Reducer.reduce(__groupPtLambdaArg__.getResultsAsArray(new java.lang.Integer[1]), pu.RedLib.Reducer.OPERATION.SUM))))));
            pt.runtime.ParaTask.registerSlotToNotify(__reducePtTask__, (__reducePtNonLambdaArg__)->reportResult(__reducePtNonLambdaArg__));
            pt.runtime.ParaTask.registerDependences(__reducePtTask__, __groupPtTaskIDGroup__);
            pt.runtime.TaskID<java.lang.Integer> __reducePtTaskID__ = __reducePtTask__.start(__groupPtTaskIDGroup__);
            java.lang.System.out.println("Enqueueing thread is processing here!");
            java.lang.System.out.println(("result of reduce is: " + (__reducePtTaskID__.getReturnResult())));
        } finally {
            java.lang.System.out.println("finished");
        }
    }

    public void testParallelFuture() {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        java.util.concurrent.ConcurrentLinkedDeque<java.lang.String> myList = new java.util.concurrent.ConcurrentLinkedDeque<>();
        myList.add("hello");
        myList.add("forget");
        myList.add("love");
        myList.add("saint");
        pt.runtime.TaskInfoOneArg<java.util.Map<java.lang.String, java.lang.Integer>, java.util.concurrent.ConcurrentLinkedDeque<java.lang.String>> __parallelFuturePtTask__ = ((pt.runtime.TaskInfoOneArg<java.util.Map<java.lang.String, java.lang.Integer>, java.util.concurrent.ConcurrentLinkedDeque<java.lang.String>>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.util.Map<java.lang.String, java.lang.Integer>, java.util.concurrent.ConcurrentLinkedDeque<java.lang.String>>) (( __myListPtNonLambdaArg__) -> wordCountTask(__myListPtNonLambdaArg__))))));
        pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.lang.Integer>> __parallelFuturePtTaskID__ = ((pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.lang.Integer>>) (__parallelFuturePtTask__.start(myList)));
        pu.RedLib.MapUnion<java.lang.String, java.lang.Integer> __parallelFuturePtTaskReductionObject__ = new pu.RedLib.MapUnion<java.lang.String, java.lang.Integer>(new pu.RedLib.IntegerSum());
        pt.runtime.ParaTask.registerReduction(__parallelFuturePtTaskID__, __parallelFuturePtTaskReductionObject__);
        int counter = 0;
        java.util.Map<java.lang.String, java.lang.Integer>[] wordMaps = new java.util.HashMap[myList.size()];
        pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.lang.Integer>> __wordMapsPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(myList.size());
        for (java.lang.String word : myList) {
            pt.runtime.TaskInfoOneArg<java.util.Map<java.lang.String, java.lang.Integer>, java.lang.String> ____wordMaps_1__PtTask__ = ((pt.runtime.TaskInfoOneArg<java.util.Map<java.lang.String, java.lang.Integer>, java.lang.String>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.util.Map<java.lang.String, java.lang.Integer>, java.lang.String>) (( __wordPtNonLambdaArg__) -> wordToInt(__wordPtNonLambdaArg__))))));
            pt.runtime.TaskID<java.util.Map<java.lang.String, java.lang.Integer>> ____wordMaps_1__PtTaskID__ = ____wordMaps_1__PtTask__.start(word);
            __wordMapsPtTaskIDGroup__.setInnerTask((counter++), ____wordMaps_1__PtTaskID__);
        }
        pt.runtime.TaskInfoOneArg<java.lang.Void, pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.lang.Integer>>> __printResultsPtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Void, pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.lang.Integer>>>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgNoReturn<pt.runtime.TaskIDGroup<java.util.Map<java.lang.String, java.lang.Integer>>>) (( __wordMapsPtLambdaArg__) -> printAllMaps(__wordMapsPtLambdaArg__.getResultsAsArray(new java.util.HashMap[1])))))));
        pt.runtime.ParaTask.registerSlotToNotify(__printResultsPtTask__, ()->updateGui());
        pt.runtime.ParaTask.registerSlotToNotify(__printResultsPtTask__, ()->updateDB());
        pt.runtime.ParaTask.registerDependences(__printResultsPtTask__, __wordMapsPtTaskIDGroup__);
        pt.runtime.TaskID<java.lang.Void> __printResultsPtTaskID__ = __printResultsPtTask__.start(__wordMapsPtTaskIDGroup__);
        try {
            __wordMapsPtTaskIDGroup__.waitTillFinished();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        for (int __wordMapsLoopIndex1__ = 0; __wordMapsLoopIndex1__ < (wordMaps.length); __wordMapsLoopIndex1__++)
            wordMaps[__wordMapsLoopIndex1__] = __wordMapsPtTaskIDGroup__.getInnerTaskResult(__wordMapsLoopIndex1__);
        
        java.util.Map<java.lang.String, java.lang.Integer> map = wordMaps[0];
    }
}

