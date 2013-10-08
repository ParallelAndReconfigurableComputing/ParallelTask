package pt.examples.paraservice;//####[1]####
//####[1]####
import android.app.Service;//####[3]####
import android.content.Intent;//####[4]####
import android.os.IBinder;//####[5]####
import android.widget.Toast;//####[6]####
//####[6]####
//-- ParaTask related imports//####[6]####
import pt.runtime.*;//####[6]####
import java.util.concurrent.ExecutionException;//####[6]####
import java.util.concurrent.locks.*;//####[6]####
import java.lang.reflect.*;//####[6]####
import pt.runtime.GuiThread;//####[6]####
import java.util.concurrent.BlockingQueue;//####[6]####
import java.util.ArrayList;//####[6]####
import java.util.List;//####[6]####
//####[6]####
public class ParaService extends Service {//####[8]####
    static{ParaTask.init();}//####[8]####
    /*  ParaTask helper method to access private/protected slots *///####[8]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[8]####
        if (m.getParameterTypes().length == 0)//####[8]####
            m.invoke(instance);//####[8]####
        else if ((m.getParameterTypes().length == 1))//####[8]####
            m.invoke(instance, arg);//####[8]####
        else //####[8]####
            m.invoke(instance, arg, interResult);//####[8]####
    }//####[8]####
//####[9]####
    public ParaService() {//####[9]####
    }//####[10]####
//####[13]####
    @Override//####[13]####
    public IBinder onBind(Intent intent) {//####[13]####
        throw new UnsupportedOperationException("Not yet implemented");//####[15]####
    }//####[16]####
//####[20]####
    @Override//####[20]####
    public void onCreate() {//####[20]####
        Toast.makeText(this, "The ParaService process was Created!", Toast.LENGTH_LONG).show();//####[21]####
    }//####[23]####
//####[26]####
    @Override//####[26]####
    public void onStart(Intent intent, int startId) {//####[26]####
        Toast.makeText(this, "ParaService Started", Toast.LENGTH_LONG).show();//####[28]####
    }//####[29]####
//####[32]####
    @Override//####[32]####
    public void onDestroy() {//####[32]####
        Toast.makeText(this, "ParaService Destroyed", Toast.LENGTH_LONG).show();//####[33]####
    }//####[34]####
}//####[34]####
