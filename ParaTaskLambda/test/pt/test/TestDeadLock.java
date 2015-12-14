package pt.test;//####[1]####
//####[1]####
//-- ParaTask related imports//####[1]####
import pt.runtime.*;//####[1]####
import java.util.concurrent.ExecutionException;//####[1]####
import java.util.concurrent.locks.*;//####[1]####
import java.lang.reflect.*;//####[1]####
import pt.runtime.GuiThread;//####[1]####
import java.util.concurrent.BlockingQueue;//####[1]####
import java.util.ArrayList;//####[1]####
import java.util.List;//####[1]####
//####[1]####
public class TestDeadLock {//####[3]####
    static{ParaTask.init();}//####[3]####
    /*  ParaTask helper method to access private/protected slots *///####[3]####
    public void __pt__accessPrivateSlot(Method m, Object instance, Future arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[3]####
        if (m.getParameterTypes().length == 0)//####[3]####
            m.invoke(instance);//####[3]####
        else if ((m.getParameterTypes().length == 1))//####[3]####
            m.invoke(instance, arg);//####[3]####
        else //####[3]####
            m.invoke(instance, arg, interResult);//####[3]####
    }//####[3]####
//####[4]####
    public static void main(String[] args) {//####[4]####
        TestDeadLock testDeadLock = new TestDeadLock();//####[5]####
        Future tid_1 = null;//####[7]####
        Future tid_2 = null;//####[8]####
        Task __pt__tid_1 = new Task();//####[10]####
//####[10]####
        /*  -- ParaTask dependsOn clause for 'tid_1' -- *///####[10]####
        __pt__tid_1.addDependsOn(tid_2);//####[10]####
//####[10]####
        tid_1 = testDeadLock.task_1(__pt__tid_1);//####[10]####
        Task __pt__tid_2 = new Task();//####[11]####
//####[11]####
        /*  -- ParaTask dependsOn clause for 'tid_2' -- *///####[11]####
        __pt__tid_2.addDependsOn(tid_1);//####[11]####
//####[11]####
        tid_2 = testDeadLock.task_2(__pt__tid_2);//####[11]####
        FutureGroup tig = new FutureGroup(2);//####[12]####
        tig.add(tid_1);//####[13]####
        tig.add(tid_2);//####[14]####
        try {//####[16]####
            tig.waitTillFinished();//####[17]####
        } catch (ExecutionException e) {//####[18]####
            e.printStackTrace();//####[19]####
        } catch (InterruptedException e) {//####[20]####
            e.printStackTrace();//####[21]####
        }//####[22]####
    }//####[23]####
//####[25]####
    private static volatile Method __pt__task_1__method = null;//####[25]####
    private synchronized static void __pt__task_1__ensureMethodVarSet() {//####[25]####
        if (__pt__task_1__method == null) {//####[25]####
            try {//####[25]####
                __pt__task_1__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__task_1", new Class[] {//####[25]####
                    //####[25]####
                });//####[25]####
            } catch (Exception e) {//####[25]####
                e.printStackTrace();//####[25]####
            }//####[25]####
        }//####[25]####
    }//####[25]####
    private Future<Void> task_1() {//####[25]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[25]####
        return task_1(new Task());//####[25]####
    }//####[25]####
    private Future<Void> task_1(Task taskinfo) {//####[25]####
        // ensure Method variable is set//####[25]####
        if (__pt__task_1__method == null) {//####[25]####
            __pt__task_1__ensureMethodVarSet();//####[25]####
        }//####[25]####
        taskinfo.setParameters();//####[25]####
        taskinfo.setMethod(__pt__task_1__method);//####[25]####
        taskinfo.setInstance(this);//####[25]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[25]####
    }//####[25]####
    public void __pt__task_1() {//####[25]####
        System.out.println("i am task 1");//####[26]####
    }//####[27]####
//####[27]####
//####[29]####
    private static volatile Method __pt__task_2__method = null;//####[29]####
    private synchronized static void __pt__task_2__ensureMethodVarSet() {//####[29]####
        if (__pt__task_2__method == null) {//####[29]####
            try {//####[29]####
                __pt__task_2__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__task_2", new Class[] {//####[29]####
                    //####[29]####
                });//####[29]####
            } catch (Exception e) {//####[29]####
                e.printStackTrace();//####[29]####
            }//####[29]####
        }//####[29]####
    }//####[29]####
    private Future<Void> task_2() {//####[29]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[29]####
        return task_2(new Task());//####[29]####
    }//####[29]####
    private Future<Void> task_2(Task taskinfo) {//####[29]####
        // ensure Method variable is set//####[29]####
        if (__pt__task_2__method == null) {//####[29]####
            __pt__task_2__ensureMethodVarSet();//####[29]####
        }//####[29]####
        taskinfo.setParameters();//####[29]####
        taskinfo.setMethod(__pt__task_2__method);//####[29]####
        taskinfo.setInstance(this);//####[29]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[29]####
    }//####[29]####
    public void __pt__task_2() {//####[29]####
        System.out.println("i am task 2");//####[30]####
    }//####[31]####
//####[31]####
}//####[31]####
