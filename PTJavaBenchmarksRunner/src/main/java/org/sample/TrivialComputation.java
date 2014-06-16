package org.sample;//####[1]####
//####[1]####
import java.util.concurrent.atomic.AtomicInteger;//####[3]####
//####[3]####
//-- ParaTask related imports//####[3]####
import pt.runtime.*;//####[3]####
import java.util.concurrent.ExecutionException;//####[3]####
import java.util.concurrent.locks.*;//####[3]####
import java.lang.reflect.*;//####[3]####
import pt.runtime.GuiThread;//####[3]####
import java.util.concurrent.BlockingQueue;//####[3]####
import java.util.ArrayList;//####[3]####
import java.util.List;//####[3]####
//####[3]####
public class TrivialComputation {//####[5]####
    static{ParaTask.init();}//####[5]####
    /*  ParaTask helper method to access private/protected slots *///####[5]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[5]####
        if (m.getParameterTypes().length == 0)//####[5]####
            m.invoke(instance);//####[5]####
        else if ((m.getParameterTypes().length == 1))//####[5]####
            m.invoke(instance, arg);//####[5]####
        else //####[5]####
            m.invoke(instance, arg, interResult);//####[5]####
    }//####[5]####
//####[6]####
    private static AtomicInteger index = new AtomicInteger(0);//####[6]####
//####[8]####
    public static void trivalComputationByParaTaskWithLambda() {//####[8]####
        TaskID id = trivalComputation();//####[9]####
        try {//####[11]####
            id.getReturnResult();//####[12]####
        } catch (Exception e) {//####[13]####
            throw new ParaTaskRuntimeException(e.getMessage());//####[14]####
        }//####[15]####
    }//####[16]####
//####[18]####
    public static int getCurrentValue() {//####[18]####
        return index.get();//####[19]####
    }//####[20]####
//####[22]####
    private static volatile Method __pt__trivalComputation__method = null;//####[22]####
    private synchronized static void __pt__trivalComputation__ensureMethodVarSet() {//####[22]####
        if (__pt__trivalComputation__method == null) {//####[22]####
            try {//####[22]####
                __pt__trivalComputation__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__trivalComputation", new Class[] {//####[22]####
                    //####[22]####
                });//####[22]####
            } catch (Exception e) {//####[22]####
                e.printStackTrace();//####[22]####
            }//####[22]####
        }//####[22]####
    }//####[22]####
    private static TaskID<Void> trivalComputation() {//####[22]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[22]####
        return trivalComputation(new TaskInfo());//####[22]####
    }//####[22]####
    private static TaskID<Void> trivalComputation(TaskInfo taskinfo) {//####[22]####
        // ensure Method variable is set//####[22]####
        if (__pt__trivalComputation__method == null) {//####[22]####
            __pt__trivalComputation__ensureMethodVarSet();//####[22]####
        }//####[22]####
        taskinfo.setParameters();//####[22]####
        taskinfo.setMethod(__pt__trivalComputation__method);//####[22]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[22]####
    }//####[22]####
    public static void __pt__trivalComputation() {//####[22]####
        index.incrementAndGet();//####[23]####
    }//####[24]####
//####[24]####
}//####[24]####
