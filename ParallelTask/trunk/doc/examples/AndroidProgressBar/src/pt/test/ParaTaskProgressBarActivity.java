package pt.test;//####[1]####
//####[1]####
import pt.runtime.TaskID;//####[3]####
import android.os.Bundle;//####[4]####
import android.os.Handler;//####[5]####
import android.app.Activity;//####[6]####
import android.text.Editable;//####[7]####
import android.util.Log;//####[8]####
import android.view.View;//####[9]####
import android.view.View.OnClickListener;//####[10]####
import android.widget.*;//####[11]####
//####[11]####
//-- ParaTask related imports//####[11]####
import pt.runtime.*;//####[11]####
import java.util.concurrent.ExecutionException;//####[11]####
import java.util.concurrent.locks.*;//####[11]####
import java.lang.reflect.*;//####[11]####
import pt.runtime.GuiThread;//####[11]####
import java.util.concurrent.BlockingQueue;//####[11]####
import java.util.ArrayList;//####[11]####
import java.util.List;//####[11]####
//####[11]####
public class ParaTaskProgressBarActivity extends Activity {//####[13]####
    static{ParaTask.init();}//####[13]####
    /*  ParaTask helper method to access private/protected slots *///####[13]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[13]####
        if (m.getParameterTypes().length == 0)//####[13]####
            m.invoke(instance);//####[13]####
        else if ((m.getParameterTypes().length == 1))//####[13]####
            m.invoke(instance, arg);//####[13]####
        else //####[13]####
            m.invoke(instance, arg, interResult);//####[13]####
    }//####[13]####
//####[14]####
    ProgressBar myBarHorizontal;//####[14]####
//####[15]####
    ProgressBar myBarCircular;//####[15]####
//####[16]####
    TextView lblTopCaption;//####[16]####
//####[17]####
    EditText txtDataBox;//####[17]####
//####[18]####
    Button btnDoSomething;//####[18]####
//####[19]####
    Button btnDoItAgain;//####[19]####
//####[21]####
    int globalVar = 0;//####[21]####
//####[23]####
    boolean isRunning = false;//####[23]####
//####[24]####
    String PATIENCE = "Background task is running." + "\nPlease be patient...";//####[24]####
//####[28]####
    @Override//####[28]####
    public void onCreate(Bundle savedInstanceState) {//####[28]####
        super.onCreate(savedInstanceState);//####[29]####
        setContentView(R.layout.activity_para_task_progress_bar);//####[30]####
        lblTopCaption = (TextView) findViewById(R.id.lblTopCaption);//####[31]####
        myBarHorizontal = (ProgressBar) findViewById(R.id.myBarHor);//####[32]####
        myBarCircular = (ProgressBar) findViewById(R.id.myBarCir);//####[33]####
        txtDataBox = (EditText) findViewById(R.id.txtBox1);//####[34]####
        txtDataBox.setHint("Foreground interaction\nEnter some words here...");//####[35]####
        btnDoItAgain = (Button) findViewById(R.id.btnDoItAgain);//####[37]####
        btnDoItAgain.setOnClickListener(new OnClickListener() {//####[37]####
//####[40]####
            @Override//####[40]####
            public void onClick(View v) {//####[40]####
                onStart();//####[41]####
            }//####[42]####
        });//####[42]####
        btnDoSomething = (Button) findViewById(R.id.btnDoSomething);//####[44]####
        btnDoSomething.setOnClickListener(new OnClickListener() {//####[44]####
//####[47]####
            @Override//####[47]####
            public void onClick(View v) {//####[47]####
                Editable text = txtDataBox.getText();//####[48]####
                Toast.makeText(getBaseContext(), "You said >> \n" + text, 1).show();//####[51]####
            }//####[52]####
        });//####[52]####
    }//####[54]####
//####[57]####
    @Override//####[57]####
    protected void onStart() {//####[57]####
        super.onStart();//####[58]####
        lblTopCaption.setText("Background task starts ...");//####[60]####
        txtDataBox.setText("");//####[61]####
        btnDoItAgain.setEnabled(false);//####[62]####
        myBarHorizontal.setMax(100);//####[65]####
        myBarHorizontal.setProgress(0);//####[66]####
        myBarHorizontal.setVisibility(View.VISIBLE);//####[67]####
        myBarCircular.setVisibility(View.VISIBLE);//####[68]####
        TaskInfo __pt__taskid = new TaskInfo();//####[70]####
//####[70]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[70]####
//####[70]####
//####[70]####
        /*  -- ParaTask notify clause for 'taskid' -- *///####[70]####
        try {//####[70]####
            Method __pt__taskid_slot_0 = null;//####[70]####
            __pt__taskid_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "onTaskComplete", new Class[] {});//####[72]####
            if (false) onTaskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[72]####
            __pt__taskid.addSlotToNotify(new Slot(__pt__taskid_slot_0, this, false));//####[72]####
//####[72]####
        } catch(Exception __pt__e) { //####[72]####
            System.err.println("Problem registering method in clause:");//####[72]####
            __pt__e.printStackTrace();//####[72]####
        }//####[72]####
//####[72]####
        /*  -- ParaTask notify-intermediate clause for 'taskid' -- *///####[72]####
        try {//####[72]####
            Method __pt__taskid_inter_slot_0 = null;//####[72]####
            __pt__taskid_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveProgress", new Class[] {TaskID.class, String.class});//####[72]####
            TaskID __pt__taskid_inter_slot_0_dummy_0 = null;//####[72]####
            String __pt__taskid_inter_slot_0_dummy_1 = null;//####[72]####
            if (false) receiveProgress(__pt__taskid_inter_slot_0_dummy_0, __pt__taskid_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[72]####
            __pt__taskid.addInterSlotToNotify(new Slot(__pt__taskid_inter_slot_0, this, true));//####[72]####
//####[72]####
        } catch(Exception __pt__e) { //####[72]####
            System.err.println("Problem registering method in clause:");//####[72]####
            __pt__e.printStackTrace();//####[72]####
        }//####[72]####
//####[72]####
        /*  -- ParaTask trycatch clause for 'taskid' -- *///####[72]####
        try {//####[72]####
            Method __pt__taskid_handler_0 = null;//####[72]####
            __pt__taskid_handler_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleInterruptedException", new Class[] { TaskID.class });//####[73]####
            TaskID __pt__taskid_handler_0_dummy_0 = null;//####[73]####
            if (false) handleInterruptedException(__pt__taskid_handler_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[73]####
            __pt__taskid.addExceptionHandler(InterruptedException.class, new Slot(__pt__taskid_handler_0, this, false));//####[73]####
//####[73]####
            Method __pt__taskid_handler_1 = null;//####[73]####
            __pt__taskid_handler_1 = ParaTaskHelper.getDeclaredMethod(getClass(), "handleAllThrowables", new Class[] { TaskID.class });//####[74]####
            TaskID __pt__taskid_handler_1_dummy_0 = null;//####[74]####
            if (false) handleAllThrowables(__pt__taskid_handler_1_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[74]####
            __pt__taskid.addExceptionHandler(Throwable.class, new Slot(__pt__taskid_handler_1, this, false));//####[74]####
//####[74]####
        } catch(Exception __pt__e) { //####[74]####
            System.err.println("Problem registering method in clause:");//####[74]####
            __pt__e.printStackTrace();//####[74]####
        }//####[74]####
        TaskID taskid = null;//####[74]####
        try {//####[74]####
            taskid = backgroundTask(__pt__taskid);//####[74]####
        } catch(InterruptedException __pt__e) { //####[74]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'InterruptedException' //####[74]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[74]####
        } catch(Throwable __pt__e) { //####[74]####
            /*   This is a dummy try/catch to quiet the Java compiler. If 'Throwable' //####[74]####
             *   occurs, this is properly handled by the ParaTask runtime. *///####[74]####
        }//####[74]####
        ;//####[74]####
        ;//####[74]####
    }//####[75]####
//####[77]####
    private void receiveProgress(TaskID id, String info) {//####[77]####
        lblTopCaption.setText(PATIENCE + "\nCurrent progress: " + id.getProgress() + "%  globalVar: " + globalVar + "\nInterim info: " + info);//####[79]####
        myBarHorizontal.setProgress(id.getProgress());//####[82]####
    }//####[83]####
//####[85]####
    private void onTaskComplete() {//####[85]####
        lblTopCaption.setText("Background task done!");//####[86]####
        myBarHorizontal.setVisibility(View.INVISIBLE);//####[87]####
        myBarCircular.setVisibility(View.INVISIBLE);//####[88]####
        btnDoItAgain.setEnabled(true);//####[89]####
    }//####[90]####
//####[92]####
    private void handleInterruptedException(TaskID id) {//####[92]####
        Log.e("exception in background task: ", id.getException().getMessage());//####[93]####
    }//####[94]####
//####[96]####
    private void handleAllThrowables(TaskID id) {//####[96]####
        Toast.makeText(getApplication(), "Exception caught in background task:\n" + id.getException().getMessage(), 1).show();//####[98]####
    }//####[99]####
//####[101]####
    private static volatile Method __pt__backgroundTask__method = null;//####[101]####
    private synchronized static void __pt__backgroundTask__ensureMethodVarSet() {//####[101]####
        if (__pt__backgroundTask__method == null) {//####[101]####
            try {//####[101]####
                __pt__backgroundTask__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__backgroundTask", new Class[] {//####[101]####
                    //####[101]####
                });//####[101]####
            } catch (Exception e) {//####[101]####
                e.printStackTrace();//####[101]####
            }//####[101]####
        }//####[101]####
    }//####[101]####
    private TaskID<Void> backgroundTask() throws InterruptedException {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return backgroundTask(new TaskInfo());//####[101]####
    }//####[101]####
    private TaskID<Void> backgroundTask(TaskInfo taskinfo) throws InterruptedException {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__backgroundTask__method == null) {//####[101]####
            __pt__backgroundTask__ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setParameters();//####[101]####
        taskinfo.setMethod(__pt__backgroundTask__method);//####[101]####
        taskinfo.setInstance(this);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[101]####
    }//####[101]####
    public void __pt__backgroundTask() throws InterruptedException {//####[101]####
        int max = 20;//####[103]####
        for (int n = 0; n < max; n++) //####[104]####
        {//####[104]####
            Thread.sleep(500);//####[106]####
            globalVar++;//####[108]####
            int progress = (int) ((n + 1.0) / max * 100);//####[113]####
            CurrentTask.setProgress(progress);//####[114]####
            CurrentTask.publishInterim("Task Thread ID " + CurrentTask.currentThreadID());//####[115]####
            if (globalVar % 35 == 0) //####[117]####
            throw new RuntimeException("Catch me if you can! globalVar: " + globalVar);//####[118]####
        }//####[119]####
    }//####[120]####
//####[120]####
}//####[120]####
