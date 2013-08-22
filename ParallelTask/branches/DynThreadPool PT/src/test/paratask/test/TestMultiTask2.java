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
public class TestMultiTask2 {//####[3]####
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
    public static void main(String[] args) {//####[4]####
        TaskID tid_0 = runBM_0();//####[6]####
        TaskID tid_1 = runBM_1();//####[7]####
        TaskID tid_2 = runBM_2();//####[8]####
        TaskID tid_3 = runBM_3();//####[9]####
        TaskID tid_4 = runBM_4();//####[10]####
        TaskIDGroup tig = new TaskIDGroup(5);//####[12]####
        tig.add(tid_0);//####[14]####
        tig.add(tid_1);//####[15]####
        tig.add(tid_2);//####[16]####
        tig.add(tid_3);//####[17]####
        tig.add(tid_4);//####[18]####
        try {//####[20]####
            tig.waitTillFinished();//####[21]####
        } catch (ExecutionException e) {//####[22]####
            e.printStackTrace();//####[23]####
        } catch (InterruptedException e) {//####[24]####
            e.printStackTrace();//####[25]####
        }//####[26]####
    }//####[27]####
//####[29]####
    private static volatile Method __pt__runBM_0__method = null;//####[29]####
    private synchronized static void __pt__runBM_0__ensureMethodVarSet() {//####[29]####
        if (__pt__runBM_0__method == null) {//####[29]####
            try {//####[29]####
                __pt__runBM_0__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_0", new Class[] {//####[29]####
                    //####[29]####
                });//####[29]####
            } catch (Exception e) {//####[29]####
                e.printStackTrace();//####[29]####
            }//####[29]####
        }//####[29]####
    }//####[29]####
    private static TaskIDGroup<Void> runBM_0() {//####[29]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[29]####
        return runBM_0(new TaskInfo());//####[29]####
    }//####[29]####
    private static TaskIDGroup<Void> runBM_0(TaskInfo taskinfo) {//####[29]####
        // ensure Method variable is set//####[29]####
        if (__pt__runBM_0__method == null) {//####[29]####
            __pt__runBM_0__ensureMethodVarSet();//####[29]####
        }//####[29]####
        taskinfo.setParameters();//####[29]####
        taskinfo.setMethod(__pt__runBM_0__method);//####[29]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[29]####
    }//####[29]####
    public static void __pt__runBM_0() {//####[29]####
        try {//####[29]####
            Thread.sleep(1000 * 5);//####[29]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihishes its work~~");//####[29]####
        } catch (InterruptedException e1) {//####[29]####
            e1.printStackTrace();//####[29]####
        }//####[29]####
    }//####[29]####
//####[29]####
//####[30]####
    private static volatile Method __pt__runBM_1__method = null;//####[30]####
    private synchronized static void __pt__runBM_1__ensureMethodVarSet() {//####[30]####
        if (__pt__runBM_1__method == null) {//####[30]####
            try {//####[30]####
                __pt__runBM_1__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1", new Class[] {//####[30]####
                    //####[30]####
                });//####[30]####
            } catch (Exception e) {//####[30]####
                e.printStackTrace();//####[30]####
            }//####[30]####
        }//####[30]####
    }//####[30]####
    private static TaskIDGroup<Void> runBM_1() {//####[30]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[30]####
        return runBM_1(new TaskInfo());//####[30]####
    }//####[30]####
    private static TaskIDGroup<Void> runBM_1(TaskInfo taskinfo) {//####[30]####
        // ensure Method variable is set//####[30]####
        if (__pt__runBM_1__method == null) {//####[30]####
            __pt__runBM_1__ensureMethodVarSet();//####[30]####
        }//####[30]####
        taskinfo.setParameters();//####[30]####
        taskinfo.setMethod(__pt__runBM_1__method);//####[30]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[30]####
    }//####[30]####
    public static void __pt__runBM_1() {//####[30]####
        try {//####[30]####
            Thread.sleep(1000 * 5);//####[30]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihishes its work~~");//####[30]####
        } catch (InterruptedException e1) {//####[30]####
            e1.printStackTrace();//####[30]####
        }//####[30]####
    }//####[30]####
//####[30]####
//####[31]####
    private static volatile Method __pt__runBM_2__method = null;//####[31]####
    private synchronized static void __pt__runBM_2__ensureMethodVarSet() {//####[31]####
        if (__pt__runBM_2__method == null) {//####[31]####
            try {//####[31]####
                __pt__runBM_2__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[31]####
                    //####[31]####
                });//####[31]####
            } catch (Exception e) {//####[31]####
                e.printStackTrace();//####[31]####
            }//####[31]####
        }//####[31]####
    }//####[31]####
    private static TaskIDGroup<Void> runBM_2() {//####[31]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[31]####
        return runBM_2(new TaskInfo());//####[31]####
    }//####[31]####
    private static TaskIDGroup<Void> runBM_2(TaskInfo taskinfo) {//####[31]####
        // ensure Method variable is set//####[31]####
        if (__pt__runBM_2__method == null) {//####[31]####
            __pt__runBM_2__ensureMethodVarSet();//####[31]####
        }//####[31]####
        taskinfo.setParameters();//####[31]####
        taskinfo.setMethod(__pt__runBM_2__method);//####[31]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[31]####
    }//####[31]####
    public static void __pt__runBM_2() {//####[31]####
        try {//####[31]####
            Thread.sleep(1000 * 5);//####[31]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihishes its work~~");//####[31]####
        } catch (InterruptedException e1) {//####[31]####
            e1.printStackTrace();//####[31]####
        }//####[31]####
    }//####[31]####
//####[31]####
//####[32]####
    private static volatile Method __pt__runBM_3__method = null;//####[32]####
    private synchronized static void __pt__runBM_3__ensureMethodVarSet() {//####[32]####
        if (__pt__runBM_3__method == null) {//####[32]####
            try {//####[32]####
                __pt__runBM_3__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_3", new Class[] {//####[32]####
                    //####[32]####
                });//####[32]####
            } catch (Exception e) {//####[32]####
                e.printStackTrace();//####[32]####
            }//####[32]####
        }//####[32]####
    }//####[32]####
    private static TaskIDGroup<Void> runBM_3() {//####[32]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[32]####
        return runBM_3(new TaskInfo());//####[32]####
    }//####[32]####
    private static TaskIDGroup<Void> runBM_3(TaskInfo taskinfo) {//####[32]####
        // ensure Method variable is set//####[32]####
        if (__pt__runBM_3__method == null) {//####[32]####
            __pt__runBM_3__ensureMethodVarSet();//####[32]####
        }//####[32]####
        taskinfo.setParameters();//####[32]####
        taskinfo.setMethod(__pt__runBM_3__method);//####[32]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[32]####
    }//####[32]####
    public static void __pt__runBM_3() {//####[32]####
        try {//####[32]####
            Thread.sleep(1000 * 5);//####[32]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihishes its work~~");//####[32]####
        } catch (InterruptedException e1) {//####[32]####
            e1.printStackTrace();//####[32]####
        }//####[32]####
    }//####[32]####
//####[32]####
//####[33]####
    private static volatile Method __pt__runBM_4__method = null;//####[33]####
    private synchronized static void __pt__runBM_4__ensureMethodVarSet() {//####[33]####
        if (__pt__runBM_4__method == null) {//####[33]####
            try {//####[33]####
                __pt__runBM_4__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_4", new Class[] {//####[33]####
                    //####[33]####
                });//####[33]####
            } catch (Exception e) {//####[33]####
                e.printStackTrace();//####[33]####
            }//####[33]####
        }//####[33]####
    }//####[33]####
    private static TaskIDGroup<Void> runBM_4() {//####[33]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[33]####
        return runBM_4(new TaskInfo());//####[33]####
    }//####[33]####
    private static TaskIDGroup<Void> runBM_4(TaskInfo taskinfo) {//####[33]####
        // ensure Method variable is set//####[33]####
        if (__pt__runBM_4__method == null) {//####[33]####
            __pt__runBM_4__ensureMethodVarSet();//####[33]####
        }//####[33]####
        taskinfo.setParameters();//####[33]####
        taskinfo.setMethod(__pt__runBM_4__method);//####[33]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[33]####
    }//####[33]####
    public static void __pt__runBM_4() {//####[33]####
        try {//####[33]####
            Thread.sleep(1000 * 5);//####[33]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihishes its work~~");//####[33]####
        } catch (InterruptedException e1) {//####[33]####
            e1.printStackTrace();//####[33]####
        }//####[33]####
    }//####[33]####
//####[33]####
}//####[33]####
