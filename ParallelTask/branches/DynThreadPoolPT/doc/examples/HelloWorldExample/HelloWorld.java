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
    private void notifyFunc() {//####[3]####
        System.out.println("in notifyFunc");//####[4]####
    }//####[5]####
//####[7]####
    public static void main(String[] args) {//####[7]####
        System.out.println("(1)");//####[9]####
        hello("Sequential");//####[11]####
        System.out.println("(2)");//####[13]####
        TaskID id1 = oneoff_hello();//####[15]####
        System.out.println("(3)");//####[17]####
        HelloWorld hw = new HelloWorld();//####[19]####
        TaskInfo __pt__id2 = new TaskInfo();//####[21]####
//####[21]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[21]####
//####[21]####
//####[21]####
        /*  -- ParaTask notify clause for 'id2' -- *///####[21]####
        try {//####[21]####
            Method __pt__id2_slot_0 = null;//####[21]####
            __pt__id2_slot_0 = ParaTaskHelper.getDeclaredMethod(hw.getClass(), "notifyFunc", new Class[] {});//####[21]####
            if (false) hw.notifyFunc(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[21]####
            __pt__id2.addSlotToNotify(new Slot(__pt__id2_slot_0, hw, false));//####[21]####
//####[21]####
        } catch(Exception __pt__e) { //####[21]####
            System.err.println("Problem registering method in clause:");//####[21]####
            __pt__e.printStackTrace();//####[21]####
        }//####[21]####
        TaskIDGroup id2 = multi_hello(__pt__id2);//####[21]####
        System.out.println("(4)");//####[23]####
        TaskID id3 = interactive_hello();//####[25]####
        System.out.println("(5)");//####[27]####
        TaskID id4 = new HelloWorld().oneoff_hello2();//####[29]####
        System.out.println("(6)");//####[31]####
        TaskIDGroup g = new TaskIDGroup(4);//####[33]####
        g.add(id1);//####[34]####
        g.add(id2);//####[35]####
        g.add(id3);//####[36]####
        g.add(id4);//####[37]####
        System.out.println("** Going to wait for the tasks to execute... ");//####[38]####
        try {//####[39]####
            g.waitTillFinished();//####[40]####
        } catch (ExecutionException e) {//####[41]####
            e.printStackTrace();//####[42]####
        } catch (InterruptedException e) {//####[43]####
            e.printStackTrace();//####[44]####
        }//####[45]####
        System.out.println("** Done! All tasks have now completed.");//####[46]####
    }//####[47]####
//####[49]####
    private static void hello(String name) {//####[49]####
        System.out.println("Hello from " + name);//####[50]####
    }//####[51]####
//####[53]####
    private static volatile Method __pt__oneoff_hello__method = null;//####[53]####
    private synchronized static void __pt__oneoff_hello__ensureMethodVarSet() {//####[53]####
        if (__pt__oneoff_hello__method == null) {//####[53]####
            try {//####[53]####
                __pt__oneoff_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__oneoff_hello", new Class[] {//####[53]####
                    //####[53]####
                });//####[53]####
            } catch (Exception e) {//####[53]####
                e.printStackTrace();//####[53]####
            }//####[53]####
        }//####[53]####
    }//####[53]####
    private static TaskID<Void> oneoff_hello() {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return oneoff_hello(new TaskInfo());//####[53]####
    }//####[53]####
    private static TaskID<Void> oneoff_hello(TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__oneoff_hello__method == null) {//####[53]####
            __pt__oneoff_hello__ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setParameters();//####[53]####
        taskinfo.setMethod(__pt__oneoff_hello__method);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    public static void __pt__oneoff_hello() {//####[53]####
        hello("One-off Task");//####[54]####
    }//####[55]####
//####[55]####
//####[57]####
    private static volatile Method __pt__oneoff_hello2__method = null;//####[57]####
    private synchronized static void __pt__oneoff_hello2__ensureMethodVarSet() {//####[57]####
        if (__pt__oneoff_hello2__method == null) {//####[57]####
            try {//####[57]####
                __pt__oneoff_hello2__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__oneoff_hello2", new Class[] {//####[57]####
                    //####[57]####
                });//####[57]####
            } catch (Exception e) {//####[57]####
                e.printStackTrace();//####[57]####
            }//####[57]####
        }//####[57]####
    }//####[57]####
    private TaskID<Void> oneoff_hello2() {//####[57]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[57]####
        return oneoff_hello2(new TaskInfo());//####[57]####
    }//####[57]####
    private TaskID<Void> oneoff_hello2(TaskInfo taskinfo) {//####[57]####
        // ensure Method variable is set//####[57]####
        if (__pt__oneoff_hello2__method == null) {//####[57]####
            __pt__oneoff_hello2__ensureMethodVarSet();//####[57]####
        }//####[57]####
        taskinfo.setParameters();//####[57]####
        taskinfo.setMethod(__pt__oneoff_hello2__method);//####[57]####
        taskinfo.setInstance(this);//####[57]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[57]####
    }//####[57]####
    public void __pt__oneoff_hello2() {//####[57]####
        System.out.println("Hello from oneoff_hello2");//####[58]####
    }//####[59]####
//####[59]####
//####[61]####
    private static volatile Method __pt__multi_hello__method = null;//####[61]####
    private synchronized static void __pt__multi_hello__ensureMethodVarSet() {//####[61]####
        if (__pt__multi_hello__method == null) {//####[61]####
            try {//####[61]####
                __pt__multi_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__multi_hello", new Class[] {//####[61]####
                    //####[61]####
                });//####[61]####
            } catch (Exception e) {//####[61]####
                e.printStackTrace();//####[61]####
            }//####[61]####
        }//####[61]####
    }//####[61]####
    private static TaskIDGroup<Void> multi_hello() {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return multi_hello(new TaskInfo());//####[61]####
    }//####[61]####
    private static TaskIDGroup<Void> multi_hello(TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__multi_hello__method == null) {//####[61]####
            __pt__multi_hello__ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setParameters();//####[61]####
        taskinfo.setMethod(__pt__multi_hello__method);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[61]####
    }//####[61]####
    public static void __pt__multi_hello() {//####[61]####
        hello("Multi-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[62]####
    }//####[63]####
//####[63]####
//####[65]####
    private static volatile Method __pt__interactive_hello__method = null;//####[65]####
    private synchronized static void __pt__interactive_hello__ensureMethodVarSet() {//####[65]####
        if (__pt__interactive_hello__method == null) {//####[65]####
            try {//####[65]####
                __pt__interactive_hello__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__interactive_hello", new Class[] {//####[65]####
                    //####[65]####
                });//####[65]####
            } catch (Exception e) {//####[65]####
                e.printStackTrace();//####[65]####
            }//####[65]####
        }//####[65]####
    }//####[65]####
    public static TaskID<Void> interactive_hello() {//####[65]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[65]####
        return interactive_hello(new TaskInfo());//####[65]####
    }//####[65]####
    public static TaskID<Void> interactive_hello(TaskInfo taskinfo) {//####[65]####
        // ensure Method variable is set//####[65]####
        if (__pt__interactive_hello__method == null) {//####[65]####
            __pt__interactive_hello__ensureMethodVarSet();//####[65]####
        }//####[65]####
        taskinfo.setParameters();//####[65]####
        taskinfo.setMethod(__pt__interactive_hello__method);//####[65]####
        taskinfo.setInteractive(true);//####[65]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[65]####
    }//####[65]####
    public static void __pt__interactive_hello() {//####[65]####
        hello("Interactive Task");//####[66]####
    }//####[67]####
//####[67]####
}//####[67]####
