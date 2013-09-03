//-- ParaTask related imports//####[-1]####
import pt.runtime.*;//####[-1]####
import java.util.concurrent.ExecutionException;//####[-1]####
import java.util.concurrent.locks.*;//####[-1]####
import java.lang.reflect.*;//####[-1]####
import pt.runtime.GuiThread;//####[-1]####
import java.util.concurrent.BlockingQueue;//####[-1]####
import java.util.ArrayList;//####[-1]####
import java.util.List;//####[-1]####
//####[-1]####
public class HelloWorld {//####[1]####
    static{ParaTask.init();}//####[1]####
    /*  ParaTask helper method to access private/protected slots *///####[1]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[1]####
        if (m.getParameterTypes().length == 0)//####[1]####
            m.invoke(instance);//####[1]####
        else if ((m.getParameterTypes().length == 1))//####[1]####
            m.invoke(instance, arg);//####[1]####
        else //####[1]####
            m.invoke(instance, arg, interResult);//####[1]####
    }//####[1]####
//####[3]####
    public static void main(String[] args) {//####[3]####
        System.out.println("(1)");//####[5]####
        hello("Sequential");//####[7]####
        System.out.println("(2)");//####[9]####
        TaskID id1 = oneoff_hello();//####[11]####
        System.out.println("(3)");//####[13]####
        TaskIDGroup id2 = multi_hello();//####[15]####
        System.out.println("(4)");//####[17]####
        TaskID id3 = interactive_hello();//####[19]####
        System.out.println("(5)");//####[21]####
        TaskID id4 = new HelloWorld().oneoff_hello2();//####[23]####
        System.out.println("(6)");//####[25]####
        TaskIDGroup g = new TaskIDGroup(4);//####[27]####
        g.add(id1);//####[28]####
        g.add(id2);//####[29]####
        g.add(id3);//####[30]####
        g.add(id4);//####[31]####
        System.out.println("** Going to wait for the tasks to execute... ");//####[32]####
        try {//####[33]####
            g.waitTillFinished();//####[34]####
        } catch (ExecutionException e) {//####[35]####
            e.printStackTrace();//####[36]####
        } catch (InterruptedException e) {//####[37]####
            e.printStackTrace();//####[38]####
        }//####[39]####
        System.out.println("** Done! All tasks have now completed.");//####[40]####
    }//####[41]####
//####[43]####
    private static void hello(String name) {//####[43]####
        System.out.println("Hello from " + name);//####[44]####
    }//####[45]####
//####[47]####
    private static volatile Method __pt__oneoff_hello__method = null;//####[47]####
    private synchronized static void __pt__oneoff_hello__ensureMethodVarSet() {//####[47]####
        if (__pt__oneoff_hello__method == null) {//####[47]####
            try {//####[47]####
                __pt__oneoff_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__oneoff_hello", new Class[] {//####[47]####
                    //####[47]####
                });//####[47]####
            } catch (Exception e) {//####[47]####
                e.printStackTrace();//####[47]####
            }//####[47]####
        }//####[47]####
    }//####[47]####
    private static TaskID<Void> oneoff_hello() {//####[47]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[47]####
        return oneoff_hello(new TaskInfo());//####[47]####
    }//####[47]####
    private static TaskID<Void> oneoff_hello(TaskInfo taskinfo) {//####[47]####
        // ensure Method variable is set//####[47]####
        if (__pt__oneoff_hello__method == null) {//####[47]####
            __pt__oneoff_hello__ensureMethodVarSet();//####[47]####
        }//####[47]####
        taskinfo.setParameters();//####[47]####
        taskinfo.setMethod(__pt__oneoff_hello__method);//####[47]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[47]####
    }//####[47]####
    public static void __pt__oneoff_hello() {//####[47]####
        hello("One-off Task");//####[48]####
    }//####[49]####
//####[49]####
//####[51]####
    private static volatile Method __pt__oneoff_hello2__method = null;//####[51]####
    private synchronized static void __pt__oneoff_hello2__ensureMethodVarSet() {//####[51]####
        if (__pt__oneoff_hello2__method == null) {//####[51]####
            try {//####[51]####
                __pt__oneoff_hello2__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__oneoff_hello2", new Class[] {//####[51]####
                    //####[51]####
                });//####[51]####
            } catch (Exception e) {//####[51]####
                e.printStackTrace();//####[51]####
            }//####[51]####
        }//####[51]####
    }//####[51]####
    private TaskID<Void> oneoff_hello2() {//####[51]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[51]####
        return oneoff_hello2(new TaskInfo());//####[51]####
    }//####[51]####
    private TaskID<Void> oneoff_hello2(TaskInfo taskinfo) {//####[51]####
        // ensure Method variable is set//####[51]####
        if (__pt__oneoff_hello2__method == null) {//####[51]####
            __pt__oneoff_hello2__ensureMethodVarSet();//####[51]####
        }//####[51]####
        taskinfo.setParameters();//####[51]####
        taskinfo.setMethod(__pt__oneoff_hello2__method);//####[51]####
        taskinfo.setInstance(this);//####[51]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[51]####
    }//####[51]####
    public void __pt__oneoff_hello2() {//####[51]####
        System.out.println("Hello from oneoff_hello2");//####[52]####
    }//####[53]####
//####[53]####
//####[55]####
    private static volatile Method __pt__multi_hello__method = null;//####[55]####
    private synchronized static void __pt__multi_hello__ensureMethodVarSet() {//####[55]####
        if (__pt__multi_hello__method == null) {//####[55]####
            try {//####[55]####
                __pt__multi_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__multi_hello", new Class[] {//####[55]####
                    //####[55]####
                });//####[55]####
            } catch (Exception e) {//####[55]####
                e.printStackTrace();//####[55]####
            }//####[55]####
        }//####[55]####
    }//####[55]####
    private static TaskIDGroup<Void> multi_hello() {//####[55]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[55]####
        return multi_hello(new TaskInfo());//####[55]####
    }//####[55]####
    private static TaskIDGroup<Void> multi_hello(TaskInfo taskinfo) {//####[55]####
        // ensure Method variable is set//####[55]####
        if (__pt__multi_hello__method == null) {//####[55]####
            __pt__multi_hello__ensureMethodVarSet();//####[55]####
        }//####[55]####
        taskinfo.setParameters();//####[55]####
        taskinfo.setMethod(__pt__multi_hello__method);//####[55]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[55]####
    }//####[55]####
    public static void __pt__multi_hello() {//####[55]####
        hello("Multi-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[56]####
    }//####[57]####
//####[57]####
//####[59]####
    private static volatile Method __pt__interactive_hello__method = null;//####[59]####
    private synchronized static void __pt__interactive_hello__ensureMethodVarSet() {//####[59]####
        if (__pt__interactive_hello__method == null) {//####[59]####
            try {//####[59]####
                __pt__interactive_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__interactive_hello", new Class[] {//####[59]####
                    //####[59]####
                });//####[59]####
            } catch (Exception e) {//####[59]####
                e.printStackTrace();//####[59]####
            }//####[59]####
        }//####[59]####
    }//####[59]####
    public static TaskID<Void> interactive_hello() {//####[59]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[59]####
        return interactive_hello(new TaskInfo());//####[59]####
    }//####[59]####
    public static TaskID<Void> interactive_hello(TaskInfo taskinfo) {//####[59]####
        // ensure Method variable is set//####[59]####
        if (__pt__interactive_hello__method == null) {//####[59]####
            __pt__interactive_hello__ensureMethodVarSet();//####[59]####
        }//####[59]####
        taskinfo.setParameters();//####[59]####
        taskinfo.setMethod(__pt__interactive_hello__method);//####[59]####
        taskinfo.setInteractive(true);//####[59]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[59]####
    }//####[59]####
    public static void __pt__interactive_hello() {//####[59]####
        hello("Interactive Task");//####[60]####
    }//####[61]####
//####[61]####
}//####[61]####
