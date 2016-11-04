package paratest;//####[1]####
//####[1]####
import paratask.runtime.CurrentTask;//####[3]####
//####[3]####
//-- ParaTask related imports//####[3]####
import paratask.runtime.*;//####[3]####
import java.util.concurrent.ExecutionException;//####[3]####
import java.util.concurrent.locks.*;//####[3]####
import java.lang.reflect.*;//####[3]####
import javax.swing.SwingUtilities;//####[3]####
//####[3]####
/**
 * The TestParaArray1 class is a subclass of Test.
 * 
 * It initialises a simple Array of Bytes then runs through 
 * incrementing them by one in a parallel fashion. This is 
 * done by using multi tasks for processing the elements.
 * 
 * This implementation alternates between worker threads so 
 * that for an example with two threads the first thread 
 * would increment odd numbered elements while the second 
 * thread would increment even numbered ones.
 * 
 * @param Size - the number of elements in the array
 * @param Stride - the number of elements between processing
 * 
 * @author Peter Nicolau
 *///####[21]####
public class TestParaArray1 extends Test {//####[22]####
//####[22]####
    /*  ParaTask helper method to access private/protected slots *///####[22]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[22]####
        if (m.getParameterTypes().length == 0)//####[22]####
            m.invoke(instance);//####[22]####
        else if ((m.getParameterTypes().length == 1))//####[22]####
            m.invoke(instance, arg);//####[22]####
        else //####[22]####
            m.invoke(instance, arg, interResult);//####[22]####
    }//####[22]####
//####[24]####
    private int fThreads;//####[24]####
//####[25]####
    private int fSize;//####[25]####
//####[26]####
    private int fStride;//####[26]####
//####[27]####
    private int[] fArray;//####[27]####
//####[29]####
    public TestParaArray1(int threads, int size, int stride) {//####[29]####
        fThreads = threads;//####[30]####
        fSize = size;//####[31]####
        fStride = stride;//####[32]####
    }//####[33]####
//####[35]####
    public void execute() {//####[35]####
        fArray = new int[fSize];//####[36]####
        long start = System.nanoTime();//####[37]####
        TaskID id = process();//####[39]####
        try {//####[41]####
            id.waitTillFinished();//####[42]####
        } catch (ExecutionException e) {//####[43]####
            e.printStackTrace();//####[44]####
        } catch (InterruptedException e) {//####[45]####
            e.printStackTrace();//####[46]####
        }//####[47]####
        long finish = System.nanoTime();//####[49]####
        System.out.println(fSize + "\t" + fStride + "\t" + (finish - start));//####[50]####
    }//####[51]####
//####[53]####
    private Method __pt__process_method = null;//####[53]####
    private Lock __pt__process_lock = new ReentrantLock();//####[53]####
    public TaskIDGroup<Void> process()  {//####[53]####
//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return process(new TaskInfo());//####[53]####
    }//####[53]####
    public TaskIDGroup<Void> process(TaskInfo taskinfo)  {//####[53]####
        if (__pt__process_method == null) {//####[53]####
            try {//####[53]####
                __pt__process_lock.lock();//####[53]####
                if (__pt__process_method == null) //####[53]####
                    __pt__process_method = ParaTaskHelper.getDeclaredMethod(getClass(), "__pt__process", new Class[] {});//####[53]####
            } catch (Exception e) {//####[53]####
                e.printStackTrace();//####[53]####
            } finally {//####[53]####
                __pt__process_lock.unlock();//####[53]####
            }//####[53]####
        }//####[53]####
//####[53]####
        taskinfo.setMethod(__pt__process_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
//####[53]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, fThreads);//####[53]####
    }//####[53]####
    public void __pt__process() {//####[53]####
        int taskID = CurrentTask.relativeID();//####[54]####
        for (int i = fStride * taskID; i < fSize; i += fStride * fThreads) //####[56]####
        {//####[56]####
            fArray[i]++;//####[57]####
        }//####[58]####
    }//####[59]####
//####[59]####
}//####[59]####
