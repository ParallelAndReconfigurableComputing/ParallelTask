package paratask.test;//####[1]####
//####[1]####
import java.util.Date;//####[3]####
import paratask.moldyn.Param;//####[5]####
//####[5]####
//-- ParaTask related imports//####[5]####
import paratask.runtime.*;//####[5]####
import java.util.concurrent.ExecutionException;//####[5]####
import java.util.concurrent.locks.*;//####[5]####
import java.lang.reflect.*;//####[5]####
import paratask.runtime.GuiThread;//####[5]####
import java.util.concurrent.BlockingQueue;//####[5]####
import java.util.ArrayList;//####[5]####
import java.util.List;//####[5]####
//####[5]####
public class TestNestedTask2 {//####[7]####
    static{ParaTask.init();}//####[7]####
    /*  ParaTask helper method to access private/protected slots *///####[7]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[7]####
        if (m.getParameterTypes().length == 0)//####[7]####
            m.invoke(instance);//####[7]####
        else if ((m.getParameterTypes().length == 1))//####[7]####
            m.invoke(instance, arg);//####[7]####
        else //####[7]####
            m.invoke(instance, arg, interResult);//####[7]####
    }//####[7]####
//####[12]####
    /**
	 * @param args
	 *///####[12]####
    public static void main(String[] args) {//####[12]####
        TestNestedTask2 task = new TestNestedTask2();//####[14]####
        TaskID tid = task.task_1();//####[15]####
        TaskIDGroup tig = new TaskIDGroup(1);//####[16]####
        tig.add(tid);//####[17]####
        try {//####[21]####
            tig.waitTillFinished();//####[22]####
        } catch (ExecutionException e) {//####[23]####
            e.printStackTrace();//####[24]####
        } catch (InterruptedException e) {//####[25]####
            e.printStackTrace();//####[26]####
        }//####[27]####
    }//####[29]####
//####[31]####
    private static volatile Method __pt__task_1__method = null;//####[31]####
    private synchronized static void __pt__task_1__ensureMethodVarSet() {//####[31]####
        if (__pt__task_1__method == null) {//####[31]####
            try {//####[31]####
                __pt__task_1__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__task_1", new Class[] {//####[31]####
                    //####[31]####
                });//####[31]####
            } catch (Exception e) {//####[31]####
                e.printStackTrace();//####[31]####
            }//####[31]####
        }//####[31]####
    }//####[31]####
    private TaskID<Void> task_1() {//####[31]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[31]####
        return task_1(new TaskInfo());//####[31]####
    }//####[31]####
    private TaskID<Void> task_1(TaskInfo taskinfo) {//####[31]####
        // ensure Method variable is set//####[31]####
        if (__pt__task_1__method == null) {//####[31]####
            __pt__task_1__ensureMethodVarSet();//####[31]####
        }//####[31]####
        taskinfo.setParameters();//####[31]####
        taskinfo.setMethod(__pt__task_1__method);//####[31]####
        taskinfo.setInstance(this);//####[31]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[31]####
    }//####[31]####
    public void __pt__task_1() {//####[31]####
        System.out.println("start running task 1 " + new Date() + " on thread " + Thread.currentThread().getId());//####[32]####
        try {//####[35]####
            Thread.sleep(1000 * 5);//####[36]####
        } catch (InterruptedException e) {//####[37]####
            e.printStackTrace();//####[38]####
        }//####[39]####
        TaskID tid = task_2();//####[41]####
        TaskIDGroup tig = new TaskIDGroup(1);//####[42]####
        tig.add(tid);//####[43]####
        try {//####[45]####
            Thread.sleep(1000 * 5);//####[46]####
        } catch (InterruptedException e) {//####[47]####
            e.printStackTrace();//####[48]####
        }//####[49]####
        try {//####[51]####
            tig.waitTillFinished();//####[52]####
        } catch (ExecutionException e) {//####[53]####
            e.printStackTrace();//####[54]####
        } catch (InterruptedException e) {//####[55]####
            e.printStackTrace();//####[56]####
        }//####[57]####
        System.out.println("end running task 1 " + new Date() + " on thread " + Thread.currentThread().getId());//####[61]####
    }//####[63]####
//####[63]####
//####[65]####
    private static volatile Method __pt__task_2__method = null;//####[65]####
    private synchronized static void __pt__task_2__ensureMethodVarSet() {//####[65]####
        if (__pt__task_2__method == null) {//####[65]####
            try {//####[65]####
                __pt__task_2__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__task_2", new Class[] {//####[65]####
                    //####[65]####
                });//####[65]####
            } catch (Exception e) {//####[65]####
                e.printStackTrace();//####[65]####
            }//####[65]####
        }//####[65]####
    }//####[65]####
    private TaskID<Void> task_2() {//####[65]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[65]####
        return task_2(new TaskInfo());//####[65]####
    }//####[65]####
    private TaskID<Void> task_2(TaskInfo taskinfo) {//####[65]####
        // ensure Method variable is set//####[65]####
        if (__pt__task_2__method == null) {//####[65]####
            __pt__task_2__ensureMethodVarSet();//####[65]####
        }//####[65]####
        taskinfo.setParameters();//####[65]####
        taskinfo.setMethod(__pt__task_2__method);//####[65]####
        taskinfo.setInstance(this);//####[65]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[65]####
    }//####[65]####
    public void __pt__task_2() {//####[65]####
        System.out.println("start running task 2 " + new Date() + " on thread " + Thread.currentThread().getId());//####[66]####
        try {//####[69]####
            Thread.sleep(1000 * 5);//####[70]####
        } catch (InterruptedException e) {//####[71]####
            e.printStackTrace();//####[72]####
        }//####[73]####
        TaskID tid = task_3();//####[75]####
        TaskIDGroup tig = new TaskIDGroup(1);//####[76]####
        tig.add(tid);//####[77]####
        try {//####[79]####
            Thread.sleep(1000 * 5);//####[80]####
        } catch (InterruptedException e) {//####[81]####
            e.printStackTrace();//####[82]####
        }//####[83]####
        try {//####[85]####
            tig.waitTillFinished();//####[86]####
        } catch (ExecutionException e) {//####[87]####
            e.printStackTrace();//####[88]####
        } catch (InterruptedException e) {//####[89]####
            e.printStackTrace();//####[90]####
        }//####[91]####
        System.out.println("end running task 2 " + new Date() + " on thread " + Thread.currentThread().getId());//####[93]####
    }//####[94]####
//####[94]####
//####[96]####
    private static volatile Method __pt__task_3__method = null;//####[96]####
    private synchronized static void __pt__task_3__ensureMethodVarSet() {//####[96]####
        if (__pt__task_3__method == null) {//####[96]####
            try {//####[96]####
                __pt__task_3__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__task_3", new Class[] {//####[96]####
                    //####[96]####
                });//####[96]####
            } catch (Exception e) {//####[96]####
                e.printStackTrace();//####[96]####
            }//####[96]####
        }//####[96]####
    }//####[96]####
    private TaskID<Void> task_3() {//####[96]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[96]####
        return task_3(new TaskInfo());//####[96]####
    }//####[96]####
    private TaskID<Void> task_3(TaskInfo taskinfo) {//####[96]####
        // ensure Method variable is set//####[96]####
        if (__pt__task_3__method == null) {//####[96]####
            __pt__task_3__ensureMethodVarSet();//####[96]####
        }//####[96]####
        taskinfo.setParameters();//####[96]####
        taskinfo.setMethod(__pt__task_3__method);//####[96]####
        taskinfo.setInstance(this);//####[96]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[96]####
    }//####[96]####
    public void __pt__task_3() {//####[96]####
        System.out.println("start running task 3 " + new Date() + " on thread " + Thread.currentThread().getId());//####[97]####
        try {//####[100]####
            Thread.sleep(1000 * 5);//####[101]####
        } catch (InterruptedException e) {//####[102]####
            e.printStackTrace();//####[103]####
        }//####[104]####
        System.out.println("end running task 3 " + new Date() + " on thread " + Thread.currentThread().getId());//####[106]####
    }//####[107]####
//####[107]####
}//####[107]####
