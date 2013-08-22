package paratask.test;//####[1]####
//####[1]####
//-- ParaTask related imports//####[1]####
import paratask.runtime.*;//####[1]####
import java.util.concurrent.ExecutionException;//####[1]####
import java.util.concurrent.locks.*;//####[1]####
import java.lang.reflect.*;//####[1]####
import paratask.runtime.GuiThread;//####[1]####
import java.util.concurrent.BlockingQueue;//####[1]####
import java.util.ArrayList;//####[1]####
import java.util.List;//####[1]####
//####[1]####
public class TestCancel {//####[3]####
    static{ParaTask.init();}//####[3]####
    /*  ParaTask helper method to access private/protected slots *///####[3]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[3]####
        if (m.getParameterTypes().length == 0)//####[3]####
            m.invoke(instance);//####[3]####
        else if ((m.getParameterTypes().length == 1))//####[3]####
            m.invoke(instance, arg);//####[3]####
        else //####[3]####
            m.invoke(instance, arg, interResult);//####[3]####
    }//####[3]####
//####[4]####
    private static volatile Method __pt__compute__method = null;//####[4]####
    private synchronized static void __pt__compute__ensureMethodVarSet() {//####[4]####
        if (__pt__compute__method == null) {//####[4]####
            try {//####[4]####
                __pt__compute__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__compute", new Class[] {//####[4]####
                    //####[4]####
                });//####[4]####
            } catch (Exception e) {//####[4]####
                e.printStackTrace();//####[4]####
            }//####[4]####
        }//####[4]####
    }//####[4]####
    public static TaskID<Void> compute() {//####[4]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[4]####
        return compute(new TaskInfo());//####[4]####
    }//####[4]####
    public static TaskID<Void> compute(TaskInfo taskinfo) {//####[4]####
        // ensure Method variable is set//####[4]####
        if (__pt__compute__method == null) {//####[4]####
            __pt__compute__ensureMethodVarSet();//####[4]####
        }//####[4]####
        taskinfo.setParameters();//####[4]####
        taskinfo.setMethod(__pt__compute__method);//####[4]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[4]####
    }//####[4]####
    public static void __pt__compute() {//####[4]####
        boolean requestCancel = false;//####[5]####
        for (int i = 0; i < Integer.MAX_VALUE && !(requestCancel = CurrentTask.cancelRequested()); i++) //####[6]####
        {//####[6]####
            System.out.println(i + " " + requestCancel);//####[7]####
            try {//####[8]####
                Thread.sleep(1000);//####[9]####
            } catch (InterruptedException e) {//####[10]####
                e.printStackTrace();//####[11]####
            }//####[12]####
        }//####[13]####
        System.out.println(requestCancel);//####[14]####
    }//####[15]####
//####[15]####
//####[17]####
    public static void main(String[] args) {//####[17]####
        TaskID tid = compute();//####[18]####
        TaskIDGroup tig = new TaskIDGroup(1);//####[19]####
        tig.add(tid);//####[20]####
        try {//####[22]####
            Thread.sleep(1000 * 5);//####[23]####
            tid.cancelAttempt();//####[24]####
            tig.waitTillFinished();//####[25]####
        } catch (ExecutionException e) {//####[26]####
            e.printStackTrace();//####[27]####
        } catch (InterruptedException e) {//####[28]####
            e.printStackTrace();//####[29]####
        }//####[30]####
    }//####[31]####
}//####[31]####
