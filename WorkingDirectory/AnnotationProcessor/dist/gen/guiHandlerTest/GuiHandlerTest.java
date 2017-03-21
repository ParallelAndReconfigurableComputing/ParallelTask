

package guiHandlerTest;


public class GuiHandlerTest {
    private int foo(int x) throws java.lang.InterruptedException {
        java.util.Random rand = new java.util.Random();
        int randNo = rand.nextInt(x);
        java.lang.Thread.sleep((randNo * 1000));
        return randNo;
    }

    private java.lang.Void updateGui(int x, int y) throws java.lang.Exception {
        return null;
    }

    private java.lang.Void updateDB(int x, int y) {
        return null;
    }

    public void handleException() {
        java.lang.System.out.println("Exception occurred");
    }

    public void taskRun() {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        try {
            int x = 25;
            int y = 13;
            pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer> __aPtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, java.lang.Integer>) (( __xPtNonLambdaArg__) -> {
                try {
                    return foo(__xPtNonLambdaArg__);
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    return null;
                }
            })))));
            pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, java.io.IOException.class, () -> {
                try {
                    handleException();
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                }
            });
            pt.runtime.TaskID<java.lang.Integer> __aPtTaskID__ = __aPtTask__.start(x);
            int b = foo(y);
            java.lang.Void handler2 = updateGui(b, x);
            java.lang.Void handler3 = updateDB(x, y);
            int j = __aPtTaskID__.getReturnResult();
            int k = b;
            int c = foo(24);
            java.lang.Void handler4 = updateDB(j, k);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}

