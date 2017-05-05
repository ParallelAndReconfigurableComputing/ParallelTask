package dependingNestedTryCatch;

import java.io.IOException;

import pt.runtime.ParaTask;
import pt.runtime.TaskID;
import pt.runtime.TaskInfoNoArgs;

public class Main {
	public static void main(String[] args) {
        ParaTask.init(ParaTask.PTSchedulingPolicy.MixedSchedule, 
        		Runtime.getRuntime().availableProcessors());
        ExceptionThrowers et = new ExceptionThrowers();
        ExceptionHandlers eh = new ExceptionHandlers();
        try {
            TaskInfoNoArgs<Void> __aPtTask__ = (TaskInfoNoArgs<Void>)
            		(ParaTask.asTask(ParaTask.TaskType.ONEOFF,()->et.interruptedExceptionThrower()));
            ParaTask.registerAsyncCatch(__aPtTask__, InterruptedException.class,(e)->{
                eh.handleInterruptedException();
            });
            ParaTask.registerAsyncCatch(__aPtTask__, RuntimeException.class,(e)->{
                eh.handleRuntimeException(e);
            });
            TaskID<Void> __aPtTaskID__ = __aPtTask__.start();
            Void barrier = __aPtTaskID__.getReturnResult();
            try {
                TaskInfoNoArgs<Void> __cPtTask__ = (TaskInfoNoArgs<Void>)
                		(ParaTask.asTask(ParaTask.TaskType.ONEOFF,()->et.runtimeExceptionThrower()));
                ParaTask.registerAsyncCatch(__cPtTask__, RuntimeException.class,(e)->{
                    eh.handleRuntimeException(e);
                });
                ParaTask.registerAsyncCatch(__cPtTask__, NullPointerException.class,(e)->{
                    eh.handleNullException(e);
                });
                TaskID<Void> __cPtTaskID__ = __cPtTask__.start();
                TaskInfoNoArgs<Void> __dPtTask__ = (TaskInfoNoArgs<Void>)
                		(ParaTask.asTask(ParaTask.TaskType.ONEOFF, ()->et.nullExceptionThrower()));
                ParaTask.registerAsyncCatch(__dPtTask__, RuntimeException.class,(e)->{
                    eh.handleRuntimeException(e);
                });
                ParaTask.registerAsyncCatch(__dPtTask__, NullPointerException.class,(e)->{
                    eh.handleNullException(e);
                });
                TaskID<Void> __dPtTaskID__ = __dPtTask__.start();
            } catch (NullPointerException e) {
                eh.handleNullException(e);
            }
            TaskInfoNoArgs<Void> __bPtTask__ = (TaskInfoNoArgs<Void>)
            		(ParaTask.asTask(ParaTask.TaskType.ONEOFF, ()->et.ioExceptionThrower()));
            ParaTask.registerAsyncCatch(__bPtTask__, IOException.class,(e)->{
                eh.handleIOException(e);
            });
            ParaTask.registerAsyncCatch(__bPtTask__, RuntimeException.class, (e)->{
                eh.handleRuntimeException(e);
            });
            TaskID<Void> __bPtTaskID__ = __bPtTask__.start();
        } catch (RuntimeException e) {
            eh.handleRuntimeException(e);
        }
    }
}

