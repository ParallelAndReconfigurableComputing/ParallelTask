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
//####[28]####
    @Override//####[28]####
    public IBinder onBind(Intent intent) {//####[28]####
        throw new UnsupportedOperationException("Not yet implemented");//####[30]####
    }//####[31]####
//####[35]####
    @Override//####[35]####
    public void onCreate() {//####[35]####
        Log.i(LOG_TAG, "ParaTask onCreate");//####[36]####
        Thread t = Thread.currentThread();//####[37]####
        Toast.makeText(this, "onCreate Current thread id: " + t.getId() + " name: " + t.getName() + "\n ParaTask EDT id: " + ParaTask.getEDT().getId() + " name: " + ParaTask.getEDT().getName(), Toast.LENGTH_LONG).show();//####[41]####
    }//####[42]####
//####[45]####
    @Override//####[45]####
    public void onStart(Intent intent, int startId) {//####[45]####
        Log.i(LOG_TAG, "ParaTask onStart");//####[46]####
        Toast.makeText(this, "ParaService Started", Toast.LENGTH_LONG).show();//####[48]####
        TaskInfo __pt__taskid = new TaskInfo();//####[50]####
//####[50]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[50]####
//####[50]####
//####[50]####
        /*  -- ParaTask notify clause for 'taskid' -- *///####[50]####
        try {//####[50]####
            Method __pt__taskid_slot_0 = null;//####[50]####
            __pt__taskid_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "onTaskComplete", new Class[] {});//####[52]####
            if (false) onTaskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[52]####
            __pt__taskid.addSlotToNotify(new Slot(__pt__taskid_slot_0, this, false));//####[52]####
//####[52]####
        } catch(Exception __pt__e) { //####[52]####
            System.err.println("Problem registering method in clause:");//####[52]####
            __pt__e.printStackTrace();//####[52]####
        }//####[52]####
//####[52]####
        /*  -- ParaTask notify-intermediate clause for 'taskid' -- *///####[52]####
        try {//####[52]####
            Method __pt__taskid_inter_slot_0 = null;//####[52]####
            __pt__taskid_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveProgress", new Class[] {TaskID.class, String.class});//####[52]####
            TaskID __pt__taskid_inter_slot_0_dummy_0 = null;//####[52]####
            String __pt__taskid_inter_slot_0_dummy_1 = null;//####[52]####
            if (false) receiveProgress(__pt__taskid_inter_slot_0_dummy_0, __pt__taskid_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[52]####
            __pt__taskid.addInterSlotToNotify(new Slot(__pt__taskid_inter_slot_0, this, true));//####[52]####
//####[52]####
        } catch(Exception __pt__e) { //####[52]####
            System.err.println("Problem registering method in clause:");//####[52]####
            __pt__e.printStackTrace();//####[52]####
        }//####[52]####
//####[52]####
        /*  -- ParaTask trycatch clause for 'taskid' -- *///####[52]####
        try {//####[52]####
            Method __pt__taskid_handler_0 = null;//####[52]####
            __pt__taskid_handler_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleInterruptedException", new Class[] { TaskID.class });//####[53]####
            TaskID __pt__taskid_handler_0_dummy_0 = null;//####[53]####
            if (false) handleInterruptedException(__pt__taskid_handler_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[53]####
            __pt__taskid.addExceptionHandler(InterruptedException.class, new Slot(__pt__taskid_handler_0, this, false));//####[53]####
//####[53]####
            Method __pt__taskid_handler_1 = null;//####[53]####
            __pt__taskid_handler_1 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleAllThrowables", new Class[] { TaskID.class });//####[54]####
            TaskID __pt__taskid_handler_1_dummy_0 = null;//####[54]####
            if (false) handleAllThrowables(__pt__taskid_handler_1_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[54]####
            __pt__taskid.addExceptionHandler(Throwable.class, new Slot(__pt__taskid_handler_1, this, false));//####[54]####
//####[54]####
        } catch(Exception __pt__e) { //####[54]####
            System.err.println("Problem registering method in clause:");//####[54]####
            __pt__e.printStackTrace();//####[54]####
        }//####[54]####
        TaskID taskid = null;//####[54]####
        try {//####[54]####
            taskid = countingTask(__pt__taskid);//####[54]####
        } catch(InterruptedException __pt__e) { //####[54]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'InterruptedException' //####[54]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[54]####
        } catch(Throwable __pt__e) { //####[54]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'Throwable' //####[54]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[54]####
        }//####[54]####
        ;//####[54]####
        ;//####[54]####
    }//####[55]####
//####[57]####
    private void handleInterruptedException(TaskID id) {//####[57]####
        Log.e(LOG_TAG, "ParaTask exception in background task: ", id.getException());//####[58]####
    }//####[59]####
//####[61]####
    private void handleAllThrowables(TaskID id) {//####[61]####
        Toast.makeText(getApplication(), "Exception caught in background task:\n" + id.getException().getMessage(), 1).show();//####[63]####
    }//####[64]####
//####[66]####
    private void receiveProgress(TaskID id, String info) {//####[66]####
        Log.i(LOG_TAG, "ParaTask receiveProgress: " + id.getProgress() + " " + info);//####[67]####
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();//####[68]####
    }//####[69]####
//####[71]####
    private void onTaskComplete() {//####[71]####
        Log.i(LOG_TAG, "ParaTask onTaskComplete");//####[72]####
        Toast.makeText(this, "Complete!", Toast.LENGTH_LONG).show();//####[73]####
    }//####[74]####
//####[77]####
    @Override//####[77]####
    public void onDestroy() {//####[77]####
        Log.i(LOG_TAG, "ParaTask onDestroy");//####[78]####
        Toast.makeText(this, "ParaService Destroyed", Toast.LENGTH_LONG).show();//####[79]####
    }//####[80]####
//####[82]####
    private static volatile Method __pt__countingTask__method = null;//####[82]####
    private synchronized static void __pt__countingTask__ensureMethodVarSet() {//####[82]####
        if (__pt__countingTask__method == null) {//####[82]####
            try {//####[82]####
                __pt__countingTask__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__countingTask", new Class[] {//####[82]####
                    //####[82]####
                });//####[82]####
            } catch (Exception e) {//####[82]####
                e.printStackTrace();//####[82]####
            }//####[82]####
        }//####[82]####
    }//####[82]####
    private TaskID<Void> countingTask() throws InterruptedException {//####[82]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[82]####
        return countingTask(new TaskInfo());//####[82]####
    }//####[82]####
    private TaskID<Void> countingTask(TaskInfo taskinfo) throws InterruptedException {//####[82]####
        // ensure Method variable is set//####[82]####
        if (__pt__countingTask__method == null) {//####[82]####
            __pt__countingTask__ensureMethodVarSet();//####[82]####
        }//####[82]####
        taskinfo.setParameters();//####[82]####
        taskinfo.setMethod(__pt__countingTask__method);//####[82]####
        taskinfo.setInstance(this);//####[82]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[82]####
    }//####[82]####
    public void __pt__countingTask() throws InterruptedException {//####[82]####
        Log.i(LOG_TAG, "ParaTask countingTask");//####[83]####
        int max = 20;//####[84]####
        for (int n = 0; n < max; n++) //####[85]####
        {//####[85]####
            Thread.sleep(1000);//####[86]####
            globalVar++;//####[87]####
            int progress = (int) ((n + 1.0) / max * 100);//####[89]####
            if (globalVar % 5 == 0) //####[91]####
            {//####[91]####
                CurrentTask.setProgress(progress);//####[92]####
                CurrentTask.publishInterim("Current number: " + globalVar);//####[93]####
                Log.i(LOG_TAG, "ParaTask countingTask publishInterim");//####[94]####
            }//####[95]####
        }//####[96]####
    }//####[97]####
//####[97]####
}//####[97]####
