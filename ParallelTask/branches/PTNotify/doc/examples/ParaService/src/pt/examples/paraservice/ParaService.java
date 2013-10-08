package pt.examples.paraservice;//####[1]####
//####[1]####
import android.app.Service;//####[3]####
import android.content.Intent;//####[4]####
import android.os.IBinder;//####[5]####
import android.util.Log;//####[6]####
import android.widget.Toast;//####[7]####
//####[7]####
//-- ParaTask related imports//####[7]####
import pt.runtime.*;//####[7]####
import java.util.concurrent.ExecutionException;//####[7]####
import java.util.concurrent.locks.*;//####[7]####
import java.lang.reflect.*;//####[7]####
import pt.runtime.GuiThread;//####[7]####
import java.util.concurrent.BlockingQueue;//####[7]####
import java.util.ArrayList;//####[7]####
import java.util.List;//####[7]####
//####[7]####
public class ParaService extends Service {//####[9]####
    static{ParaTask.init();}//####[9]####
    /*  ParaTask helper method to access private/protected slots *///####[9]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[9]####
        if (m.getParameterTypes().length == 0)//####[9]####
            m.invoke(instance);//####[9]####
        else if ((m.getParameterTypes().length == 1))//####[9]####
            m.invoke(instance, arg);//####[9]####
        else //####[9]####
            m.invoke(instance, arg, interResult);//####[9]####
    }//####[9]####
//####[11]####
    int globalVar = 0;//####[11]####
//####[14]####
    @Override//####[14]####
    public IBinder onBind(Intent intent) {//####[14]####
        throw new UnsupportedOperationException("Not yet implemented");//####[16]####
    }//####[17]####
//####[21]####
    @Override//####[21]####
    public void onCreate() {//####[21]####
        Toast.makeText(this, "The ParaService process was Created!", Toast.LENGTH_LONG).show();//####[22]####
    }//####[24]####
//####[27]####
    @Override//####[27]####
    public void onStart(Intent intent, int startId) {//####[27]####
        Toast.makeText(this, "ParaService Started", Toast.LENGTH_LONG).show();//####[29]####
        TaskInfo __pt__taskid = new TaskInfo();//####[31]####
//####[31]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[31]####
//####[31]####
//####[31]####
        /*  -- ParaTask notify clause for 'taskid' -- *///####[31]####
        try {//####[31]####
            Method __pt__taskid_slot_0 = null;//####[31]####
            __pt__taskid_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "onTaskComplete", new Class[] {});//####[33]####
            if (false) onTaskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[33]####
            __pt__taskid.addSlotToNotify(new Slot(__pt__taskid_slot_0, this, false));//####[33]####
//####[33]####
        } catch(Exception __pt__e) { //####[33]####
            System.err.println("Problem registering method in clause:");//####[33]####
            __pt__e.printStackTrace();//####[33]####
        }//####[33]####
//####[33]####
        /*  -- ParaTask notify-intermediate clause for 'taskid' -- *///####[33]####
        try {//####[33]####
            Method __pt__taskid_inter_slot_0 = null;//####[33]####
            __pt__taskid_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveProgress", new Class[] {TaskID.class, String.class});//####[33]####
            TaskID __pt__taskid_inter_slot_0_dummy_0 = null;//####[33]####
            String __pt__taskid_inter_slot_0_dummy_1 = null;//####[33]####
            if (false) receiveProgress(__pt__taskid_inter_slot_0_dummy_0, __pt__taskid_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[33]####
            __pt__taskid.addInterSlotToNotify(new Slot(__pt__taskid_inter_slot_0, this, true));//####[33]####
//####[33]####
        } catch(Exception __pt__e) { //####[33]####
            System.err.println("Problem registering method in clause:");//####[33]####
            __pt__e.printStackTrace();//####[33]####
        }//####[33]####
//####[33]####
        /*  -- ParaTask trycatch clause for 'taskid' -- *///####[33]####
        try {//####[33]####
            Method __pt__taskid_handler_0 = null;//####[33]####
            __pt__taskid_handler_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleInterruptedException", new Class[] { TaskID.class });//####[34]####
            TaskID __pt__taskid_handler_0_dummy_0 = null;//####[34]####
            if (false) handleInterruptedException(__pt__taskid_handler_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[34]####
            __pt__taskid.addExceptionHandler(InterruptedException.class, new Slot(__pt__taskid_handler_0, this, false));//####[34]####
//####[34]####
            Method __pt__taskid_handler_1 = null;//####[34]####
            __pt__taskid_handler_1 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleAllThrowables", new Class[] { TaskID.class });//####[35]####
            TaskID __pt__taskid_handler_1_dummy_0 = null;//####[35]####
            if (false) handleAllThrowables(__pt__taskid_handler_1_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[35]####
            __pt__taskid.addExceptionHandler(Throwable.class, new Slot(__pt__taskid_handler_1, this, false));//####[35]####
//####[35]####
        } catch(Exception __pt__e) { //####[35]####
            System.err.println("Problem registering method in clause:");//####[35]####
            __pt__e.printStackTrace();//####[35]####
        }//####[35]####
        TaskID taskid = null;//####[35]####
        try {//####[35]####
            taskid = countingTask(__pt__taskid);//####[35]####
        } catch(InterruptedException __pt__e) { //####[35]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'InterruptedException' //####[35]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[35]####
        } catch(Throwable __pt__e) { //####[35]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'Throwable' //####[35]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[35]####
        }//####[35]####
        ;//####[35]####
        ;//####[35]####
    }//####[36]####
//####[38]####
    private void handleInterruptedException(TaskID id) {//####[38]####
        Log.e("exception in background task: ", id.getException().getMessage());//####[39]####
    }//####[40]####
//####[42]####
    private void handleAllThrowables(TaskID id) {//####[42]####
        Toast.makeText(getApplication(), "Exception caught in background task:\n" + id.getException().getMessage(), 1).show();//####[44]####
    }//####[45]####
//####[47]####
    private void receiveProgress(TaskID id, String info) {//####[47]####
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();//####[48]####
    }//####[49]####
//####[51]####
    private void onTaskComplete() {//####[51]####
        Toast.makeText(this, "Complete!", Toast.LENGTH_LONG).show();//####[52]####
    }//####[53]####
//####[56]####
    @Override//####[56]####
    public void onDestroy() {//####[56]####
        Toast.makeText(this, "ParaService Destroyed", Toast.LENGTH_LONG).show();//####[57]####
    }//####[58]####
//####[60]####
    private static volatile Method __pt__countingTask__method = null;//####[60]####
    private synchronized static void __pt__countingTask__ensureMethodVarSet() {//####[60]####
        if (__pt__countingTask__method == null) {//####[60]####
            try {//####[60]####
                __pt__countingTask__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__countingTask", new Class[] {//####[60]####
                    //####[60]####
                });//####[60]####
            } catch (Exception e) {//####[60]####
                e.printStackTrace();//####[60]####
            }//####[60]####
        }//####[60]####
    }//####[60]####
    private TaskID<Void> countingTask() throws InterruptedException {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return countingTask(new TaskInfo());//####[60]####
    }//####[60]####
    private TaskID<Void> countingTask(TaskInfo taskinfo) throws InterruptedException {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__countingTask__method == null) {//####[60]####
            __pt__countingTask__ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setParameters();//####[60]####
        taskinfo.setMethod(__pt__countingTask__method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public void __pt__countingTask() throws InterruptedException {//####[60]####
        int max = 20;//####[61]####
        for (int n = 0; n < max; n++) //####[62]####
        {//####[62]####
            Thread.sleep(1000);//####[63]####
            globalVar++;//####[64]####
            int progress = (int) ((n + 1.0) / max * 100);//####[66]####
            if (globalVar % 5 == 0) //####[68]####
            {//####[68]####
                CurrentTask.setProgress(progress);//####[69]####
                CurrentTask.publishInterim("Current number: " + globalVar);//####[70]####
            }//####[71]####
        }//####[72]####
    }//####[73]####
//####[73]####
}//####[73]####
