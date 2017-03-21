

package test;


public class OldAnnotation {
    public static java.lang.Void foo(int x) throws java.lang.InterruptedException {
        java.lang.Thread.sleep(5000);
        java.lang.System.out.println(("Argument value: " + x));
        return null;
    }

    public static int foo1(int x, int y) throws java.lang.InterruptedException {
        java.lang.Thread.sleep(2000);
        return x + y;
    }

    public static int foo2(int x) throws java.lang.InterruptedException {
        java.lang.Thread.sleep(1000);
        return x * 10;
    }

    public static int foo3(int x) {
        return x * 100;
    }

    public static void main(java.lang.String[] args) {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        pt.runtime.TaskInfoNoArgs<java.lang.Integer> __VarPtTask__ = ((pt.runtime.TaskInfoNoArgs<java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, ((pt.functionalInterfaces.FunctorNoArgsWithReturn<java.lang.Integer>) (() -> test.OldAnnotation.foo3(10))))));
        pt.runtime.ParaTask.registerAsyncCatch(__VarPtTask__, java.lang.InterruptedException.class, () -> {
            try {
                test.OldAnnotation.foo(1);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        });
        pt.runtime.ParaTask.registerAsyncCatch(__VarPtTask__, java.lang.IllegalArgumentException.class, () -> {
            try {
                test.OldAnnotation.foo1(1, 2);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        });
        pt.runtime.TaskIDGroup<java.lang.Integer> __VarPtTaskID__ = ((pt.runtime.TaskIDGroup<java.lang.Integer>) (__VarPtTask__.start()));
        try {
            test.SimpleAnnotation simp = new test.SimpleAnnotation();
            simp.foo1(5);
            int[] array = new int[5];
            for (int i = 0; i < 5; i++) {
                pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer> __Var1PtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, java.lang.Integer>) (( __iPtNonLambdaArg__) -> {
                    try {
                        return test.OldAnnotation.foo2(__iPtNonLambdaArg__);
                    } catch (java.lang.Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })))));
                pt.runtime.ParaTask.registerAsyncCatch(__Var1PtTask__, java.lang.InterruptedException.class, ( e) -> {
                    e.printStackTrace();
                });
                pt.runtime.TaskID<java.lang.Integer> __Var1PtTaskID__ = __Var1PtTask__.start(i);
                array[i] = __Var1PtTaskID__.getReturnResult();
            }
            int VarX = test.OldAnnotation.foo1(8, 5);
            pt.runtime.TaskInfoTwoArgs<java.lang.Integer, java.lang.Integer, pt.runtime.TaskID<java.lang.Integer>> __Var2PtTask__ = ((pt.runtime.TaskInfoTwoArgs<java.lang.Integer, java.lang.Integer, pt.runtime.TaskID<java.lang.Integer>>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorTwoArgsWithReturn<java.lang.Integer, java.lang.Integer, pt.runtime.TaskID<java.lang.Integer>>) (( __VarXPtNonLambdaArg__, __VarPtLambdaArg__) -> {
                try {
                    return test.OldAnnotation.foo1(__VarXPtNonLambdaArg__, __VarPtLambdaArg__.getReturnResult());
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    return null;
                }
            })))));
            pt.runtime.ParaTask.registerDependences(__Var2PtTask__, __VarPtTaskID__);
            pt.runtime.ParaTask.registerSlotToNotify(__Var2PtTask__, () -> simp.foo1(7));
            pt.runtime.ParaTask.registerAsyncCatch(__Var2PtTask__, java.lang.InterruptedException.class, ( e) -> {
                e.printStackTrace();
            });
            pt.runtime.TaskID<java.lang.Integer> __Var2PtTaskID__ = __Var2PtTask__.start(VarX, __VarPtTaskID__);
            pt.runtime.TaskInfoOneArg<java.lang.Integer, pt.runtime.TaskID<java.lang.Integer>> __Var3PtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, pt.runtime.TaskID<java.lang.Integer>>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, pt.runtime.TaskID<java.lang.Integer>>) (( __Var2PtLambdaArg__) -> {
                try {
                    return test.OldAnnotation.foo2(__Var2PtLambdaArg__.getReturnResult());
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    return null;
                }
            })))));
            pt.runtime.ParaTask.registerDependences(__Var3PtTask__, __VarPtTaskID__, __Var2PtTaskID__);
            pt.runtime.ParaTask.registerAsyncCatch(__Var3PtTask__, java.lang.InterruptedException.class, ( e) -> {
                e.printStackTrace();
            });
            pt.runtime.TaskID<java.lang.Integer> __Var3PtTaskID__ = __Var3PtTask__.start(__Var2PtTaskID__);
            java.lang.System.out.println(("The result of Var2 + Var3 is: " + ((__Var2PtTaskID__.getReturnResult()) + (__Var3PtTaskID__.getReturnResult()))));
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
    }
}

