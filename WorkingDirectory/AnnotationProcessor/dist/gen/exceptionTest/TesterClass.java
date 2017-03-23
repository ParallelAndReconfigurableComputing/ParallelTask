

package exceptionTest;


public class TesterClass {
    public int foo(int x) throws java.lang.InterruptedException {
        java.util.Random rand = new java.util.Random();
        int randomNo = rand.nextInt(x);
        java.lang.Thread.sleep((randomNo * 1000));
        return randomNo;
    }

    public int foox(int x) throws java.io.IOException {
        return x * 10;
    }

    public void handleExcep(java.lang.Exception e) throws java.lang.Exception {
        e.printStackTrace();
    }

    public void handleThrowable(java.lang.Exception e) {
        e.printStackTrace();
    }

    public void handleException() {
        java.lang.System.out.println("Exception occurred");
    }

    public java.lang.Void updateGui() {
        java.lang.System.out.println("GUI updated");
        return null;
    }

    public void taskRun() {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        try {
            for (int i = 0; i < 10; i++) {
                pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer> __aPtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Integer, java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorOneArgWithReturn<java.lang.Integer, java.lang.Integer>) (( __iPtNonLambdaArg__) -> foo(__iPtNonLambdaArg__))))));
                pt.runtime.ParaTask.registerSlotToNotify(__aPtTask__, ()->updateGui());
                pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, java.lang.InterruptedException.class, ( e) -> {
                    e.printStackTrace();
                });
                pt.runtime.ParaTask.registerAsyncCatch(__aPtTask__, java.lang.Exception.class, () -> handleException());
                pt.runtime.TaskID<java.lang.Integer> __aPtTaskID__ = __aPtTask__.start(i);
            }
            pt.runtime.TaskInfoNoArgs<java.lang.Integer> __bPtTask__ = ((pt.runtime.TaskInfoNoArgs<java.lang.Integer>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorNoArgsWithReturn<java.lang.Integer>) (() -> foox(10))))));
            pt.runtime.ParaTask.registerSlotToNotify(__bPtTask__, ()->updateGui());
            pt.runtime.ParaTask.registerAsyncCatch(__bPtTask__, java.io.IOException.class, ( e) -> {
                try {
                    handleExcep(e);
                } catch (java.lang.Exception e1) {
                    e1.printStackTrace();
                }
            });
            pt.runtime.TaskID<java.lang.Integer> __bPtTaskID__ = __bPtTask__.start();
            java.lang.System.out.println(("result: " + (foo(10))));
        } catch (java.lang.IllegalArgumentException e) {
            try {
                handleExcep(e);
            } catch (java.lang.Exception e1) {
                e1.printStackTrace();
            }
        } catch (java.lang.RuntimeException e2) {
            e2.printStackTrace();
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
    }
}

