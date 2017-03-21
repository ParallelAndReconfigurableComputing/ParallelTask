

package fieldFutureGroupWithReduction;


public class FieldFutureGroupClass {
    pt.runtime.TaskIDGroup<java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>> __mapsPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>();

    volatile boolean __mapsPtTaskIDGroup__Synchronized = false;

    volatile java.util.concurrent.locks.Lock __mapsPtTaskIDGroup__Lock = new java.util.concurrent.locks.ReentrantLock();

    java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>[] maps;

    int reps = 2;

    int size = 0;

    public FieldFutureGroupClass(int size) {
        fieldFutureGroupWithReduction.FieldFutureGroupClass.this.size = size;
        maps = new java.util.HashMap[(reps) * size];
    }

    public java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> task(int i) {
        java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> result = new java.util.HashMap<>();
        java.util.List<java.lang.String> list = new java.util.ArrayList<>();
        java.util.Random rand = new java.util.Random();
        int randNo = rand.nextInt((1 + i));
        try {
            java.lang.Thread.sleep((randNo * 1000));
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        java.lang.String str = (((("This is random No. " + randNo) + " for number ") + i) + ", by thread: ") + (java.lang.Thread.currentThread().getId());
        list.add(str);
        result.put(i, list);
        return result;
    }

    @apt.annotations.ReductionMethod
    public java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> reduce() {
        return __mapsPtTaskIDGroup__.getReturnResult();
    }

    public void startTasks() {
        int index = 0;
        for (int i = 0; i < (reps); i++) {
            for (int j = 0; j < (size); j++) {
                if (!(__mapsPtTaskIDGroup__Synchronized)) {
                    pt.runtime.TaskInfoOneArg<java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>, java.lang.Integer> ____maps_1__PtTask__ = ((pt.runtime.TaskInfoOneArg<java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>, java.lang.Integer>) (( __jPtNonLambdaArg__) -> task(__jPtNonLambdaArg__))))));
                    pt.runtime.TaskID<java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>> ____maps_1__PtTaskID__ = ____maps_1__PtTask__.start(j);
                    __mapsPtTaskIDGroup__.setInnerTask((index++), ____maps_1__PtTaskID__);
                }else {
                    maps[(index++)] = task(j);
                }
            }
        }
        java.util.Map<java.lang.Integer, java.util.List<java.lang.String>> finalResult = new java.util.HashMap<>();
        finalResult = reduce();
        for (java.util.Map.Entry<java.lang.Integer, java.util.List<java.lang.String>> entry : finalResult.entrySet()) {
            java.lang.Integer key = entry.getKey();
            java.util.List<java.lang.String> value = entry.getValue();
            java.lang.System.out.println((("For key: " + key) + " --> "));
            java.lang.System.out.println(value);
        }
    }

    {
        fieldFutureGroupWithReduction.MapReduction __mapsPtTaskReductionObject__ = new fieldFutureGroupWithReduction.MapReduction(new pu.RedLib.ListUnion<java.lang.String>());
        pt.runtime.ParaTask.registerReduction(__mapsPtTaskIDGroup__, __mapsPtTaskReductionObject__);
    }
}

