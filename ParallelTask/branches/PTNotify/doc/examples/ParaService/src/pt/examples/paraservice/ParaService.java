package pt.examples.paraservice;//####[1]####
//####[1]####
import pt.runtime.ParaTask;//####[3]####
import android.app.Service;//####[4]####
import android.content.Intent;//####[5]####
import android.os.IBinder;//####[6]####
import android.util.Log;//####[7]####
import android.widget.Toast;//####[8]####
//####[8]####
//-- ParaTask related imports//####[8]####
import pt.runtime.*;//####[8]####
import java.util.concurrent.ExecutionException;//####[8]####
import java.util.concurrent.locks.*;//####[8]####
import java.lang.reflect.*;//####[8]####
import pt.runtime.GuiThread;//####[8]####
import java.util.concurrent.BlockingQueue;//####[8]####
import java.util.ArrayList;//####[8]####
import java.util.List;//####[8]####
//####[8]####
public class ParaService extends Service {//####[10]####
    static{ParaTask.init();}//####[10]####
    /*  ParaTask helper method to access private/protected slots *///####[10]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[10]####
        if (m.getParameterTypes().length == 0)//####[10]####
            m.invoke(instance);//####[10]####
        else if ((m.getParameterTypes().length == 1))//####[10]####
            m.invoke(instance, arg);//####[10]####
        else //####[10]####
            m.invoke(instance, arg, interResult);//####[10]####
    }//####[10]####
//####[12]####
    int globalVar = 0;//####[12]####
//####[15]####
    @Override//####[15]####
    public IBinder onBind(Intent intent) {//####[15]####
        throw new UnsupportedOperationException("Not yet implemented");//####[17]####
    }//####[18]####
//####[22]####
    @Override//####[22]####
    public void onCreate() {//####[22]####
        Thread t = Thread.currentThread();//####[23]####
        Toast.makeText(this, "onCreate Current thread id: " + t.getId() + " name: " + t.getName() + "\n ParaTask EDT id: " + ParaTask.getEDT().getId() + " name: " + ParaTask.getEDT().getName(), Toast.LENGTH_LONG).show();//####[27]####
    }//####[29]####
//####[32]####
    @Override//####[32]####
    public void onStart(Intent intent, int startId) {//####[32]####
        Toast.makeText(this, "ParaService Started", Toast.LENGTH_LONG).show();//####[34]####
        TaskInfo __pt__taskid = new TaskInfo();//####[36]####
//####[36]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[36]####
//####[36]####
//####[36]####
        /*  -- ParaTask notify clause for 'taskid' -- *///####[36]####
        try {//####[36]####
            Method __pt__taskid_slot_0 = null;//####[36]####
            __pt__taskid_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "onTaskComplete", new Class[] {});//####[38]####
            if (false) onTaskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[38]####
            __pt__taskid.addSlotToNotify(new Slot(__pt__taskid_slot_0, this, false));//####[38]####
//####[38]####
        } catch(Exception __pt__e) { //####[38]####
            System.err.println("Problem registering method in clause:");//####[38]####
            __pt__e.printStackTrace();//####[38]####
        }//####[38]####
//####[38]####
        /*  -- ParaTask notify-intermediate clause for 'taskid' -- *///####[38]####
        try {//####[38]####
            Method __pt__taskid_inter_slot_0 = null;//####[38]####
            __pt__taskid_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveProgress", new Class[] {TaskID.class, String.class});//####[38]####
            TaskID __pt__taskid_inter_slot_0_dummy_0 = null;//####[38]####
            String __pt__taskid_inter_slot_0_dummy_1 = null;//####[38]####
            if (false) receiveProgress(__pt__taskid_inter_slot_0_dummy_0, __pt__taskid_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[38]####
            __pt__taskid.addInterSlotToNotify(new Slot(__pt__taskid_inter_slot_0, this, true));//####[38]####
//####[38]####
        } catch(Exception __pt__e) { //####[38]####
            System.err.println("Problem registering method in clause:");//####[38]####
            __pt__e.printStackTrace();//####[38]####
        }//####[38]####
//####[38]####
        /*  -- ParaTask trycatch clause for 'taskid' -- *///####[38]####
        try {//####[38]####
            Method __pt__taskid_handler_0 = null;//####[38]####
            __pt__taskid_handler_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleInterruptedException", new Class[] { TaskID.class });//####[39]####
            TaskID __pt__taskid_handler_0_dummy_0 = null;//####[39]####
            if (false) handleInterruptedException(__pt__taskid_handler_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[39]####
            __pt__taskid.addExceptionHandler(InterruptedException.class, new Slot(__pt__taskid_handler_0, this, false));//####[39]####
//####[39]####
            Method __pt__taskid_handler_1 = null;//####[39]####
            __pt__taskid_handler_1 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleAllThrowables", new Class[] { TaskID.class });//####[40]####
            TaskID __pt__taskid_handler_1_dummy_0 = null;//####[40]####
            if (false) handleAllThrowables(__pt__taskid_handler_1_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[40]####
            __pt__taskid.addExceptionHandler(Throwable.class, new Slot(__pt__taskid_handler_1, this, false));//####[40]####
//####[40]####
        } catch(Exception __pt__e) { //####[40]####
            System.err.println("Problem registering method in clause:");//####[40]####
            __pt__e.printStackTrace();//####[40]####
        }//####[40]####
        TaskID taskid = null;//####[40]####
        try {//####[40]####
            taskid = countingTask(__pt__taskid);//####[40]####
        } catch(InterruptedException __pt__e) { //####[40]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'InterruptedException' //####[40]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[40]####
        } catch(Throwable __pt__e) { //####[40]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'Throwable' //####[40]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[40]####
        }//####[40]####
        ;//####[40]####
        ;//####[40]####
    }//####[41]####
//####[43]####
    private void handleInterruptedException(TaskID id) {//####[43]####
        Log.e("exception in background task: ", id.getException().getMessage());//####[44]####
    }//####[45]####
//####[47]####
    private void handleAllThrowables(TaskID id) {//####[47]####
        Toast.makeText(getApplication(), "Exception caught in background task:\n" + id.getException().getMessage(), 1).show();//####[49]####
    }//####[50]####
//####[52]####
    private void receiveProgress(TaskID id, String info) {//####[52]####
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();//####[53]####
    }//####[54]####
//####[56]####
    private void onTaskComplete() {//####[56]####
        Toast.makeText(this, "Complete!", Toast.LENGTH_LONG).show();//####[57]####
    }//####[58]####
//####[61]####
    @Override//####[61]####
    public void onDestroy() {//####[61]####
        Toast.makeText(this, "ParaService Destroyed", Toast.LENGTH_LONG).show();//####[62]####
    }//####[63]####
//####[65]####
    private static volatile Method __pt__countingTask__method = null;//####[65]####
    private synchronized static void __pt__countingTask__ensureMethodVarSet() {//####[65]####
        if (__pt__countingTask__method == null) {//####[65]####
            try {//####[65]####
                __pt__countingTask__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__countingTask", new Class[] {//####[65]####
                    //####[65]####
                });//####[65]####
            } catch (Exception e) {//####[65]####
                e.printStackTrace();//####[65]####
            }//####[65]####
        }//####[65]####
    }//####[65]####
    private TaskID<Void> countingTask() throws InterruptedException {//####[65]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[65]####
        return countingTask(new TaskInfo());//####[65]####
    }//####[65]####
    private TaskID<Void> countingTask(TaskInfo taskinfo) throws InterruptedException {//####[65]####
        // ensure Method variable is set//####[65]####
        if (__pt__countingTask__method == null) {//####[65]####
            __pt__countingTask__ensureMethodVarSet();//####[65]####
        }//####[65]####
        taskinfo.setParameters();//####[65]####
        taskinfo.setMethod(__pt__countingTask__method);//####[65]####
        taskinfo.setInstance(this);//####[65]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[65]####
    }//####[65]####
    public void __pt__countingTask() throws InterruptedException {//####[65]####
        int max = 20;//####[66]####
        for (int n = 0; n < max; n++) //####[67]####
        {//####[67]####
            Thread.sleep(1000);//####[68]####
            globalVar++;//####[69]####
            int progress = (int) ((n + 1.0) / max * 100);//####[71]####
            if (globalVar % 5 == 0) //####[73]####
            {//####[73]####
                CurrentTask.setProgress(progress);//####[74]####
                CurrentTask.publishInterim("Current number: " + globalVar);//####[75]####
            }//####[76]####
        }//####[77]####
    }//####[78]####
//####[78]####
}//####[78]####
