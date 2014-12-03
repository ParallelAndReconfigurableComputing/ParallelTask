package pt.test;//####[1]####
//####[1]####
import java.util.Date;//####[3]####
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
public class TestNestedTask2 {//####[5]####
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
//####[10]####
    /**
	 * @param args
	 *///####[10]####
    public static void main(String[] args) {//####[10]####
        TestNestedTask2 task = new TestNestedTask2();//####[12]####
        TaskID tid = task.task_1();//####[13]####
        TaskIDGroup tig = new TaskIDGroup(1);//####[14]####
        tig.add(tid);//####[15]####
        try {//####[19]####
            tig.waitTillFinished();//####[20]####
        } catch (ExecutionException e) {//####[21]####
            e.printStackTrace();//####[22]####
        } catch (InterruptedException e) {//####[23]####
            e.printStackTrace();//####[24]####
        }//####[25]####
    }//####[27]####
//####[29]####
    private static volatile Method __pt__task_1__method = null;//####[29]####
    private synchronized static void __pt__task_1__ensureMethodVarSet() {//####[29]####
        if (__pt__task_1__method == null) {//####[29]####
            try {//####[29]####
                __pt__task_1__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__task_1", new Class[] {//####[29]####
                    //####[29]####
                });//####[29]####
            } catch (Exception e) {//####[29]####
                e.printStackTrace();//####[29]####
            }//####[29]####
        }//####[29]####
    }//####[29]####
    private TaskID<Void> task_1() {//####[29]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[29]####
        return task_1(new TaskInfo());//####[29]####
    }//####[29]####
    private TaskID<Void> task_1(TaskInfo taskinfo) {//####[29]####
        // ensure Method variable is set//####[29]####
        if (__pt__task_1__method == null) {//####[29]####
            __pt__task_1__ensureMethodVarSet();//####[29]####
        }//####[29]####
        taskinfo.setParameters();//####[29]####
        taskinfo.setMethod(__pt__task_1__method);//####[29]####
        taskinfo.setInstance(this);//####[29]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[29]####
    }//####[29]####
    public void __pt__task_1() {//####[29]####
        System.out.println("start running task 1 " + new Date() + " on thread " + Thread.currentThread().getId());//####[30]####
        try {//####[33]####
            Thread.sleep(1000 * 5);//####[34]####
        } catch (InterruptedException e) {//####[35]####
            e.printStackTrace();//####[36]####
        }//####[37]####
        TaskID tid = task_2();//####[39]####
        TaskIDGroup tig = new TaskIDGroup(1);//####[40]####
        tig.add(tid);//####[41]####
        try {//####[43]####
            Thread.sleep(1000 * 5);//####[44]####
        } catch (InterruptedException e) {//####[45]####
            e.printStackTrace();//####[46]####
        }//####[47]####
        try {//####[49]####
            tig.waitTillFinished();//####[50]####
        } catch (ExecutionException e) {//####[51]####
            e.printStackTrace();//####[52]####
        } catch (InterruptedException e) {//####[53]####
            e.printStackTrace();//####[54]####
        }//####[55]####
        System.out.println("end running task 1 " + new Date() + " on thread " + Thread.currentThread().getId());//####[59]####
    }//####[61]####
//####[61]####
//####[63]####
    private static volatile Method __pt__task_2__method = null;//####[63]####
    private synchronized static void __pt__task_2__ensureMethodVarSet() {//####[63]####
        if (__pt__task_2__method == null) {//####[63]####
            try {//####[63]####
                __pt__task_2__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__task_2", new Class[] {//####[63]####
                    //####[63]####
                });//####[63]####
            } catch (Exception e) {//####[63]####
                e.printStackTrace();//####[63]####
            }//####[63]####
        }//####[63]####
    }//####[63]####
    private TaskID<Void> task_2() {//####[63]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[63]####
        return task_2(new TaskInfo());//####[63]####
    }//####[63]####
    private TaskID<Void> task_2(TaskInfo taskinfo) {//####[63]####
        // ensure Method variable is set//####[63]####
        if (__pt__task_2__method == null) {//####[63]####
            __pt__task_2__ensureMethodVarSet();//####[63]####
        }//####[63]####
        taskinfo.setParameters();//####[63]####
        taskinfo.setMethod(__pt__task_2__method);//####[63]####
        taskinfo.setInstance(this);//####[63]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[63]####
    }//####[63]####
    public void __pt__task_2() {//####[63]####
        System.out.println("start running task 2 " + new Date() + " on thread " + Thread.currentThread().getId());//####[64]####
        try {//####[67]####
            Thread.sleep(1000 * 5);//####[68]####
        } catch (InterruptedException e) {//####[69]####
            e.printStackTrace();//####[70]####
        }//####[71]####
        TaskID tid = task_3();//####[73]####
        TaskIDGroup tig = new TaskIDGroup(1);//####[74]####
        tig.add(tid);//####[75]####
        try {//####[77]####
            Thread.sleep(1000 * 5);//####[78]####
        } catch (InterruptedException e) {//####[79]####
            e.printStackTrace();//####[80]####
        }//####[81]####
        try {//####[83]####
            tig.waitTillFinished();//####[84]####
        } catch (ExecutionException e) {//####[85]####
            e.printStackTrace();//####[86]####
        } catch (InterruptedException e) {//####[87]####
            e.printStackTrace();//####[88]####
        }//####[89]####
        System.out.println("end running task 2 " + new Date() + " on thread " + Thread.currentThread().getId());//####[91]####
    }//####[92]####
//####[92]####
//####[94]####
    private static volatile Method __pt__task_3__method = null;//####[94]####
    private synchronized static void __pt__task_3__ensureMethodVarSet() {//####[94]####
        if (__pt__task_3__method == null) {//####[94]####
            try {//####[94]####
                __pt__task_3__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__task_3", new Class[] {//####[94]####
                    //####[94]####
                });//####[94]####
            } catch (Exception e) {//####[94]####
                e.printStackTrace();//####[94]####
            }//####[94]####
        }//####[94]####
    }//####[94]####
    private TaskID<Void> task_3() {//####[94]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[94]####
        return task_3(new TaskInfo());//####[94]####
    }//####[94]####
    private TaskID<Void> task_3(TaskInfo taskinfo) {//####[94]####
        // ensure Method variable is set//####[94]####
        if (__pt__task_3__method == null) {//####[94]####
            __pt__task_3__ensureMethodVarSet();//####[94]####
        }//####[94]####
        taskinfo.setParameters();//####[94]####
        taskinfo.setMethod(__pt__task_3__method);//####[94]####
        taskinfo.setInstance(this);//####[94]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[94]####
    }//####[94]####
    public void __pt__task_3() {//####[94]####
        System.out.println("start running task 3 " + new Date() + " on thread " + Thread.currentThread().getId());//####[95]####
        try {//####[98]####
            Thread.sleep(1000 * 5);//####[99]####
        } catch (InterruptedException e) {//####[100]####
            e.printStackTrace();//####[101]####
        }//####[102]####
        System.out.println("end running task 3 " + new Date() + " on thread " + Thread.currentThread().getId());//####[104]####
    }//####[105]####
//####[105]####
}//####[105]####
