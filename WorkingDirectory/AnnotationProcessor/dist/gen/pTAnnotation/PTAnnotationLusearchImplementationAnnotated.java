

package pTAnnotation;


public class PTAnnotationLusearchImplementationAnnotated {
    pt.runtime.TaskIDGroup<java.lang.Void> __futureGroupPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>();

    volatile boolean __futureGroupPtTaskIDGroup__Synchronized = false;

    volatile java.util.concurrent.locks.Lock __futureGroupPtTaskIDGroup__Lock = new java.util.concurrent.locks.ReentrantLock();

    int numOfThreads;

    int repeatFactor;

    int counter;

    java.lang.Void[] futureGroup;

    public PTAnnotationLusearchImplementationAnnotated(int type, int numOfThreads, int repeatFactor) {
        pTAnnotation.PTAnnotationLusearchImplementationAnnotated.this.numOfThreads = numOfThreads;
        pTAnnotation.PTAnnotationLusearchImplementationAnnotated.this.repeatFactor = repeatFactor;
        futureGroup = new java.lang.Void[repeatFactor];
        counter = 0;
        pt.runtime.ParaTask.PTSchedulingPolicy scheduleType = pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule;
        switch (type) {
            case 1 :
                scheduleType = pt.runtime.ParaTask.PTSchedulingPolicy.WorkStealing;
                break;
            case 2 :
                scheduleType = pt.runtime.ParaTask.PTSchedulingPolicy.WorkSharing;
                break;
        }
        pt.runtime.ParaTask.init(scheduleType, numOfThreads);
    }

    public void search() {
        pu.loopScheduler.LoopScheduler scheduler = pu.loopScheduler.LoopSchedulerFactory.createLoopScheduler(0, 10, 1, numOfThreads, pu.loopScheduler.AbstractLoopScheduler.LoopCondition.LessThan, pu.loopScheduler.LoopSchedulerFactory.LoopSchedulingType.Static);
        pt.runtime.TaskInfoOneArg<java.lang.Void, pu.loopScheduler.LoopScheduler> __multiTaskPtTask__ = ((pt.runtime.TaskInfoOneArg<java.lang.Void, pu.loopScheduler.LoopScheduler>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.MULTI, ((pt.functionalInterfaces.FunctorOneArgNoReturn<pu.loopScheduler.LoopScheduler>) (( __schedulerPtNonLambdaArg__) -> searchTask(__schedulerPtNonLambdaArg__))))));
        pt.runtime.TaskIDGroup<java.lang.Void> __multiTaskPtTaskID__ = ((pt.runtime.TaskIDGroup<java.lang.Void>) (__multiTaskPtTask__.start(scheduler)));
        if (!(__futureGroupPtTaskIDGroup__Synchronized)) {
            __futureGroupPtTaskIDGroup__.setInnerTask(((counter)++), __multiTaskPtTaskID__);
        }else {
            futureGroup[((counter)++)] = __multiTaskPtTaskID__.getReturnResult();
        }
    }

    public java.lang.Void searchTask(pu.loopScheduler.LoopScheduler scheduler) {
        return null;
    }

    public void waitTillFinished() {
        java.lang.Long start = java.lang.System.currentTimeMillis();
        if (!(__futureGroupPtTaskIDGroup__Synchronized)) {
            __futureGroupPtTaskIDGroup__Lock.lock();
            if (!(__futureGroupPtTaskIDGroup__Synchronized)) {
                try {
                    __futureGroupPtTaskIDGroup__.waitTillFinished();
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                }
                __futureGroupPtTaskIDGroup__Synchronized = true;
            }
            __futureGroupPtTaskIDGroup__Lock.unlock();
        }
        java.lang.Void temp = futureGroup[0];
        java.lang.Long end = java.lang.System.currentTimeMillis();
        java.lang.System.out.println((("barrier took: " + (end - start)) + " milliseconds."));
    }
}

