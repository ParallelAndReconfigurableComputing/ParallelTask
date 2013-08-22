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
public class HelloWorld {//####[3]####
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
//####[5]####
    public static void main(String[] args) {//####[5]####
        System.out.println("(1)");//####[6]####
        hello("Sequential");//####[8]####
        System.out.println("(2)");//####[10]####
        TaskID id1 = oneoff_hello();//####[12]####
        System.out.println("(3)");//####[14]####
        TaskIDGroup id2 = multi_hello();//####[16]####
        System.out.println("(4)");//####[18]####
        TaskID id3 = interactive_hello();//####[20]####
        System.out.println("(5)");//####[22]####
        TaskID id4 = new HelloWorld().oneoff_hello2();//####[24]####
        System.out.println("(6)");//####[26]####
        TaskIDGroup g = new TaskIDGroup(4);//####[28]####
        g.add(id1);//####[29]####
        g.add(id2);//####[30]####
        g.add(id3);//####[31]####
        g.add(id4);//####[32]####
        System.out.println("** Going to wait for the tasks to execute... ");//####[33]####
        try {//####[34]####
            g.waitTillFinished();//####[35]####
        } catch (ExecutionException e) {//####[36]####
            e.printStackTrace();//####[37]####
        } catch (InterruptedException e) {//####[38]####
            e.printStackTrace();//####[39]####
        }//####[40]####
        System.out.println("** Done! All tasks have now completed.");//####[41]####
    }//####[42]####
//####[44]####
    private static void hello(String name) {//####[44]####
        System.out.println("Hello from " + name);//####[45]####
    }//####[46]####
//####[48]####
    private static volatile Method __pt__oneoff_hello__method = null;//####[48]####
    private synchronized static void __pt__oneoff_hello__ensureMethodVarSet() {//####[48]####
        if (__pt__oneoff_hello__method == null) {//####[48]####
            try {//####[48]####
                __pt__oneoff_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__oneoff_hello", new Class[] {//####[48]####
                    //####[48]####
                });//####[48]####
            } catch (Exception e) {//####[48]####
                e.printStackTrace();//####[48]####
            }//####[48]####
        }//####[48]####
    }//####[48]####
    private static TaskID<Void> oneoff_hello() {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return oneoff_hello(new TaskInfo());//####[48]####
    }//####[48]####
    private static TaskID<Void> oneoff_hello(TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__oneoff_hello__method == null) {//####[48]####
            __pt__oneoff_hello__ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setParameters();//####[48]####
        taskinfo.setMethod(__pt__oneoff_hello__method);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    public static void __pt__oneoff_hello() {//####[48]####
        hello("One-off Task");//####[49]####
    }//####[50]####
//####[50]####
//####[52]####
    private static volatile Method __pt__oneoff_hello2__method = null;//####[52]####
    private synchronized static void __pt__oneoff_hello2__ensureMethodVarSet() {//####[52]####
        if (__pt__oneoff_hello2__method == null) {//####[52]####
            try {//####[52]####
                __pt__oneoff_hello2__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__oneoff_hello2", new Class[] {//####[52]####
                    //####[52]####
                });//####[52]####
            } catch (Exception e) {//####[52]####
                e.printStackTrace();//####[52]####
            }//####[52]####
        }//####[52]####
    }//####[52]####
    private TaskID<Void> oneoff_hello2() {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return oneoff_hello2(new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> oneoff_hello2(TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__oneoff_hello2__method == null) {//####[52]####
            __pt__oneoff_hello2__ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setParameters();//####[52]####
        taskinfo.setMethod(__pt__oneoff_hello2__method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    public void __pt__oneoff_hello2() {//####[52]####
        System.out.println("Hello from oneoff_hello2");//####[53]####
    }//####[54]####
//####[54]####
//####[56]####
    private static volatile Method __pt__multi_hello__method = null;//####[56]####
    private synchronized static void __pt__multi_hello__ensureMethodVarSet() {//####[56]####
        if (__pt__multi_hello__method == null) {//####[56]####
            try {//####[56]####
                __pt__multi_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__multi_hello", new Class[] {//####[56]####
                    //####[56]####
                });//####[56]####
            } catch (Exception e) {//####[56]####
                e.printStackTrace();//####[56]####
            }//####[56]####
        }//####[56]####
    }//####[56]####
    private static TaskIDGroup<Void> multi_hello() {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return multi_hello(new TaskInfo());//####[56]####
    }//####[56]####
    private static TaskIDGroup<Void> multi_hello(TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__multi_hello__method == null) {//####[56]####
            __pt__multi_hello__ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setParameters();//####[56]####
        taskinfo.setMethod(__pt__multi_hello__method);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[56]####
    }//####[56]####
    public static void __pt__multi_hello() {//####[56]####
        hello("Multi-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[57]####
    }//####[58]####
//####[58]####
//####[60]####
    private static volatile Method __pt__interactive_hello__method = null;//####[60]####
    private synchronized static void __pt__interactive_hello__ensureMethodVarSet() {//####[60]####
        if (__pt__interactive_hello__method == null) {//####[60]####
            try {//####[60]####
                __pt__interactive_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__interactive_hello", new Class[] {//####[60]####
                    //####[60]####
                });//####[60]####
            } catch (Exception e) {//####[60]####
                e.printStackTrace();//####[60]####
            }//####[60]####
        }//####[60]####
    }//####[60]####
    public static TaskID<Void> interactive_hello() {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return interactive_hello(new TaskInfo());//####[60]####
    }//####[60]####
    public static TaskID<Void> interactive_hello(TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__interactive_hello__method == null) {//####[60]####
            __pt__interactive_hello__ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setParameters();//####[60]####
        taskinfo.setMethod(__pt__interactive_hello__method);//####[60]####
        taskinfo.setInteractive(true);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public static void __pt__interactive_hello() {//####[60]####
        hello("Interactive Task");//####[61]####
    }//####[62]####
//####[62]####
}//####[62]####
