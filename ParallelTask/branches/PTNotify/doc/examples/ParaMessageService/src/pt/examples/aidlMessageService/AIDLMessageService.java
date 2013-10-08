package pt.examples.aidlMessageService;//####[1]####
//####[1]####
import java.text.SimpleDateFormat;//####[3]####
import pt.runtime.TaskID;//####[5]####
import android.app.Service;//####[7]####
import android.content.Intent;//####[8]####
import android.os.IBinder;//####[9]####
import android.util.Log;//####[10]####
//####[10]####
//-- ParaTask related imports//####[10]####
import pt.runtime.*;//####[10]####
import java.util.concurrent.ExecutionException;//####[10]####
import java.util.concurrent.locks.*;//####[10]####
import java.lang.reflect.*;//####[10]####
import pt.runtime.GuiThread;//####[10]####
import java.util.concurrent.BlockingQueue;//####[10]####
import java.util.ArrayList;//####[10]####
import java.util.List;//####[10]####
//####[10]####
public class AIDLMessageService extends Service {//####[12]####
    static{ParaTask.init();}//####[12]####
    /*  ParaTask helper method to access private/protected slots *///####[12]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[12]####
        if (m.getParameterTypes().length == 0)//####[12]####
            m.invoke(instance);//####[12]####
        else if ((m.getParameterTypes().length == 1))//####[12]####
            m.invoke(instance, arg);//####[12]####
        else //####[12]####
            m.invoke(instance, arg, interResult);//####[12]####
    }//####[12]####
//####[13]####
    private static final String PT_INTENT_ACTION_BIND_MESSAGE_SERVICE = "pt.intent.action.bindMessageService";//####[13]####
//####[14]####
    private static final String LOG_TAG = AIDLMessageService.class.getCanonicalName();//####[14]####
//####[16]####
    int globalVar = 0;//####[16]####
//####[19]####
    @Override//####[19]####
    public void onCreate() {//####[19]####
        super.onCreate();//####[20]####
        Log.i(LOG_TAG, "ParaTask: The AIDLMessageService was created.");//####[21]####
        TaskInfo __pt__taskid = new TaskInfo();//####[23]####
//####[23]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[23]####
//####[23]####
//####[23]####
        /*  -- ParaTask notify clause for 'taskid' -- *///####[23]####
        try {//####[23]####
            Method __pt__taskid_slot_0 = null;//####[23]####
            __pt__taskid_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "onTaskComplete", new Class[] {});//####[25]####
            if (false) onTaskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[25]####
            __pt__taskid.addSlotToNotify(new Slot(__pt__taskid_slot_0, this, false));//####[25]####
//####[25]####
        } catch(Exception __pt__e) { //####[25]####
            System.err.println("Problem registering method in clause:");//####[25]####
            __pt__e.printStackTrace();//####[25]####
        }//####[25]####
//####[25]####
        /*  -- ParaTask notify-intermediate clause for 'taskid' -- *///####[25]####
        try {//####[25]####
            Method __pt__taskid_inter_slot_0 = null;//####[25]####
            __pt__taskid_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveProgress", new Class[] {TaskID.class, String.class});//####[25]####
            TaskID __pt__taskid_inter_slot_0_dummy_0 = null;//####[25]####
            String __pt__taskid_inter_slot_0_dummy_1 = null;//####[25]####
            if (false) receiveProgress(__pt__taskid_inter_slot_0_dummy_0, __pt__taskid_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[25]####
            __pt__taskid.addInterSlotToNotify(new Slot(__pt__taskid_inter_slot_0, this, true));//####[25]####
//####[25]####
        } catch(Exception __pt__e) { //####[25]####
            System.err.println("Problem registering method in clause:");//####[25]####
            __pt__e.printStackTrace();//####[25]####
        }//####[25]####
//####[25]####
        /*  -- ParaTask trycatch clause for 'taskid' -- *///####[25]####
        try {//####[25]####
            Method __pt__taskid_handler_0 = null;//####[25]####
            __pt__taskid_handler_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleInterruptedException", new Class[] { TaskID.class });//####[26]####
            TaskID __pt__taskid_handler_0_dummy_0 = null;//####[26]####
            if (false) handleInterruptedException(__pt__taskid_handler_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[26]####
            __pt__taskid.addExceptionHandler(InterruptedException.class, new Slot(__pt__taskid_handler_0, this, false));//####[26]####
//####[26]####
            Method __pt__taskid_handler_1 = null;//####[26]####
            __pt__taskid_handler_1 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleAllThrowables", new Class[] { TaskID.class });//####[27]####
            TaskID __pt__taskid_handler_1_dummy_0 = null;//####[27]####
            if (false) handleAllThrowables(__pt__taskid_handler_1_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[27]####
            __pt__taskid.addExceptionHandler(Throwable.class, new Slot(__pt__taskid_handler_1, this, false));//####[27]####
//####[27]####
        } catch(Exception __pt__e) { //####[27]####
            System.err.println("Problem registering method in clause:");//####[27]####
            __pt__e.printStackTrace();//####[27]####
        }//####[27]####
        TaskID taskid = null;//####[27]####
        try {//####[27]####
            taskid = countingTask(__pt__taskid);//####[27]####
        } catch(InterruptedException __pt__e) { //####[27]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'InterruptedException' //####[27]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[27]####
        } catch(Throwable __pt__e) { //####[27]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'Throwable' //####[27]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[27]####
        }//####[27]####
        ;//####[27]####
        ;//####[27]####
    }//####[28]####
//####[31]####
    @Override//####[31]####
    public IBinder onBind(Intent intent) {//####[31]####
        if (PT_INTENT_ACTION_BIND_MESSAGE_SERVICE.equals(intent.getAction())) //####[32]####
        {//####[32]####
            Log.i(LOG_TAG, "ParaTask: The AIDLMessageService was binded.");//####[33]####
            return new TimeMessageService(this);//####[34]####
        }//####[35]####
        return null;//####[36]####
    }//####[37]####
//####[39]####
    String getStringForRemoteService() {//####[39]####
        return "Current count: " + this.globalVar + "\n" + getString(R.string.time_message) + (new SimpleDateFormat(" hh:mm:ss").format(System.currentTimeMillis()));//####[40]####
    }//####[41]####
//####[44]####
    private void handleInterruptedException(TaskID id) {//####[44]####
        Log.e(LOG_TAG, "ParaTask: exception in background task: ", id.getException());//####[45]####
    }//####[46]####
//####[48]####
    private void handleAllThrowables(TaskID id) {//####[48]####
        Log.e(LOG_TAG, "ParaTask: exception caught in background task", id.getException());//####[49]####
    }//####[50]####
//####[52]####
    private void receiveProgress(TaskID id, String info) {//####[52]####
        Log.e(LOG_TAG, "ParaTask: receiveProgress: " + id.getProgress() + " " + info);//####[53]####
    }//####[54]####
//####[56]####
    private void onTaskComplete() {//####[56]####
        Log.e(LOG_TAG, "ParaTask: onTaskComplete");//####[57]####
    }//####[58]####
//####[61]####
    @Override//####[61]####
    public void onDestroy() {//####[61]####
        Log.e(LOG_TAG, "ParaTask: onDestroy");//####[62]####
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
        int max = 100;//####[66]####
        for (int n = 0; n < max; n++) //####[67]####
        {//####[67]####
            Thread.sleep(500);//####[68]####
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
