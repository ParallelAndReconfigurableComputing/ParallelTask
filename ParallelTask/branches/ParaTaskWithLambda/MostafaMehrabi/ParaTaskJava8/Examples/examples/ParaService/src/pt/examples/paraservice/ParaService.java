package pt.examples.paraservice;//####[1]####
//####[1]####
import pt.runtime.ParaTask;//####[3]####
import pt.runtime.TaskID;//####[4]####
import android.app.Service;//####[5]####
import android.content.Intent;//####[6]####
import android.os.IBinder;//####[7]####
import android.util.Log;//####[8]####
import android.widget.Toast;//####[9]####
//####[9]####
//-- ParaTask related imports//####[9]####
import pt.runtime.*;//####[9]####
import java.util.concurrent.ExecutionException;//####[9]####
import java.util.concurrent.locks.*;//####[9]####
import java.lang.reflect.*;//####[9]####
import pt.runtime.GuiThread;//####[9]####
import java.util.concurrent.BlockingQueue;//####[9]####
import java.util.ArrayList;//####[9]####
import java.util.List;//####[9]####
//####[9]####
public class ParaService extends Service {//####[11]####
    static{ParaTask.init();}//####[11]####
    /*  ParaTask helper method to access private/protected slots *///####[11]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[11]####
        if (m.getParameterTypes().length == 0)//####[11]####
            m.invoke(instance);//####[11]####
        else if ((m.getParameterTypes().length == 1))//####[11]####
            m.invoke(instance, arg);//####[11]####
        else //####[11]####
            m.invoke(instance, arg, interResult);//####[11]####
    }//####[11]####
//####[12]####
    private static final String LOG_TAG = ParaService.class.getCanonicalName();//####[12]####
//####[14]####
    int globalVar = 0;//####[14]####
//####[17]####
    @Override//####[17]####
    public IBinder onBind(Intent intent) {//####[17]####
        throw new UnsupportedOperationException("Not yet implemented");//####[19]####
    }//####[20]####
//####[24]####
    @Override//####[24]####
    public void onCreate() {//####[24]####
        Log.i(LOG_TAG, "ParaTask onCreate");//####[25]####
        Thread t = Thread.currentThread();//####[26]####
        Toast.makeText(this, "onCreate Current thread id: " + t.getId() + " name: " + t.getName() + "\n ParaTask EDT id: " + ParaTask.getEDT().getId() + " name: " + ParaTask.getEDT().getName(), Toast.LENGTH_LONG).show();//####[30]####
    }//####[31]####
//####[34]####
    @Override//####[34]####
    public void onStart(Intent intent, int startId) {//####[34]####
        Log.i(LOG_TAG, "ParaTask onStart");//####[35]####
        Toast.makeText(this, "ParaService Started", Toast.LENGTH_LONG).show();//####[37]####
        TaskInfo __pt__taskid = new TaskInfo();//####[39]####
//####[39]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[39]####
//####[39]####
//####[39]####
        /*  -- ParaTask notify clause for 'taskid' -- *///####[39]####
        try {//####[39]####
            Method __pt__taskid_slot_0 = null;//####[39]####
            __pt__taskid_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "onTaskComplete", new Class[] {});//####[41]####
            if (false) onTaskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[41]####
            __pt__taskid.addSlotToNotify(new Slot(__pt__taskid_slot_0, this, false));//####[41]####
//####[41]####
        } catch(Exception __pt__e) { //####[41]####
            System.err.println("Problem registering method in clause:");//####[41]####
            __pt__e.printStackTrace();//####[41]####
        }//####[41]####
//####[41]####
        /*  -- ParaTask notify-intermediate clause for 'taskid' -- *///####[41]####
        try {//####[41]####
            Method __pt__taskid_inter_slot_0 = null;//####[41]####
            __pt__taskid_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveProgress", new Class[] {TaskID.class, String.class});//####[41]####
            TaskID __pt__taskid_inter_slot_0_dummy_0 = null;//####[41]####
            String __pt__taskid_inter_slot_0_dummy_1 = null;//####[41]####
            if (false) receiveProgress(__pt__taskid_inter_slot_0_dummy_0, __pt__taskid_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[41]####
            __pt__taskid.addInterSlotToNotify(new Slot(__pt__taskid_inter_slot_0, this, true));//####[41]####
//####[41]####
        } catch(Exception __pt__e) { //####[41]####
            System.err.println("Problem registering method in clause:");//####[41]####
            __pt__e.printStackTrace();//####[41]####
        }//####[41]####
//####[41]####
        /*  -- ParaTask trycatch clause for 'taskid' -- *///####[41]####
        try {//####[41]####
            Method __pt__taskid_handler_0 = null;//####[41]####
            __pt__taskid_handler_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleInterruptedException", new Class[] { TaskID.class });//####[42]####
            TaskID __pt__taskid_handler_0_dummy_0 = null;//####[42]####
            if (false) handleInterruptedException(__pt__taskid_handler_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[42]####
            __pt__taskid.addExceptionHandler(InterruptedException.class, new Slot(__pt__taskid_handler_0, this, false));//####[42]####
//####[42]####
            Method __pt__taskid_handler_1 = null;//####[42]####
            __pt__taskid_handler_1 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleAllThrowables", new Class[] { TaskID.class });//####[43]####
            TaskID __pt__taskid_handler_1_dummy_0 = null;//####[43]####
            if (false) handleAllThrowables(__pt__taskid_handler_1_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[43]####
            __pt__taskid.addExceptionHandler(Throwable.class, new Slot(__pt__taskid_handler_1, this, false));//####[43]####
//####[43]####
        } catch(Exception __pt__e) { //####[43]####
            System.err.println("Problem registering method in clause:");//####[43]####
            __pt__e.printStackTrace();//####[43]####
        }//####[43]####
        TaskID taskid = null;//####[43]####
        try {//####[43]####
            taskid = countingTask(__pt__taskid);//####[43]####
        } catch(InterruptedException __pt__e) { //####[43]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'InterruptedException' //####[43]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[43]####
        } catch(Throwable __pt__e) { //####[43]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'Throwable' //####[43]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[43]####
        }//####[43]####
        ;//####[43]####
        ;//####[43]####
    }//####[44]####
//####[46]####
    private void handleInterruptedException(TaskID id) {//####[46]####
        Log.e(LOG_TAG, "ParaTask exception in background task: ", id.getException());//####[47]####
    }//####[48]####
//####[50]####
    private void handleAllThrowables(TaskID id) {//####[50]####
        Toast.makeText(getApplication(), "Exception caught in background task:\n" + id.getException().getMessage(), 1).show();//####[52]####
    }//####[53]####
//####[55]####
    private void receiveProgress(TaskID id, String info) {//####[55]####
        Log.i(LOG_TAG, "ParaTask receiveProgress: " + id.getProgress() + " " + info);//####[56]####
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();//####[57]####
    }//####[58]####
//####[60]####
    private void onTaskComplete() {//####[60]####
        Log.i(LOG_TAG, "ParaTask onTaskComplete");//####[61]####
        Toast.makeText(this, "Complete!", Toast.LENGTH_LONG).show();//####[62]####
    }//####[63]####
//####[66]####
    @Override//####[66]####
    public void onDestroy() {//####[66]####
        Log.i(LOG_TAG, "ParaTask onDestroy");//####[67]####
        Toast.makeText(this, "ParaService Destroyed", Toast.LENGTH_LONG).show();//####[68]####
    }//####[69]####
//####[71]####
    private static volatile Method __pt__countingTask__method = null;//####[71]####
    private synchronized static void __pt__countingTask__ensureMethodVarSet() {//####[71]####
        if (__pt__countingTask__method == null) {//####[71]####
            try {//####[71]####
                __pt__countingTask__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__countingTask", new Class[] {//####[71]####
                    //####[71]####
                });//####[71]####
            } catch (Exception e) {//####[71]####
                e.printStackTrace();//####[71]####
            }//####[71]####
        }//####[71]####
    }//####[71]####
    private TaskID<Void> countingTask() throws InterruptedException {//####[71]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[71]####
        return countingTask(new TaskInfo());//####[71]####
    }//####[71]####
    private TaskID<Void> countingTask(TaskInfo taskinfo) throws InterruptedException {//####[71]####
        // ensure Method variable is set//####[71]####
        if (__pt__countingTask__method == null) {//####[71]####
            __pt__countingTask__ensureMethodVarSet();//####[71]####
        }//####[71]####
        taskinfo.setParameters();//####[71]####
        taskinfo.setMethod(__pt__countingTask__method);//####[71]####
        taskinfo.setInstance(this);//####[71]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[71]####
    }//####[71]####
    public void __pt__countingTask() throws InterruptedException {//####[71]####
        Log.i(LOG_TAG, "ParaTask countingTask");//####[72]####
        int max = 20;//####[73]####
        for (int n = 0; n < max; n++) //####[74]####
        {//####[74]####
            Thread.sleep(1000);//####[75]####
            globalVar++;//####[76]####
            int progress = (int) ((n + 1.0) / max * 100);//####[78]####
            if (globalVar % 5 == 0) //####[80]####
            {//####[80]####
                CurrentTask.setProgress(progress);//####[81]####
                CurrentTask.publishInterim("Current number: " + globalVar);//####[82]####
                Log.i(LOG_TAG, "ParaTask countingTask publishInterim");//####[83]####
            }//####[84]####
        }//####[85]####
    }//####[86]####
//####[86]####
}//####[86]####
