package paratest;//####[1]####
//####[1]####
import java.util.ArrayList;//####[3]####
import paratask.runtime.ParaTask;//####[5]####
import pi.ParIterator;//####[6]####
import pi.ParIteratorFactory;//####[7]####
//####[7]####
//-- ParaTask related imports//####[7]####
import paratask.runtime.*;//####[7]####
import java.util.concurrent.ExecutionException;//####[7]####
import java.util.concurrent.locks.*;//####[7]####
import java.lang.reflect.*;//####[7]####
import javax.swing.SwingUtilities;//####[7]####
//####[7]####
/**
 * The TestParaIterator class is a subclass of Test.
 * 
 * It initialises a ParIterator over an array of Bytes then 
 * runs through them in a parallel fashion. This is done by 
 * using multi tasks for processing the elements.
 * 
 * @param Size - the number of elements in the array
 * 
 * @author Peter Nicolau
 *///####[19]####
public class TestParaIterator extends Test {//####[20]####
//####[20]####
    /*  ParaTask helper method to access private/protected slots *///####[20]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[20]####
        if (m.getParameterTypes().length == 0)//####[20]####
            m.invoke(instance);//####[20]####
        else if ((m.getParameterTypes().length == 1))//####[20]####
            m.invoke(instance, arg);//####[20]####
        else //####[20]####
            m.invoke(instance, arg, interResult);//####[20]####
    }//####[20]####
//####[22]####
    private int fThreads;//####[22]####
//####[23]####
    private int fSize;//####[23]####
//####[24]####
    private ParIterator<Integer> fIterator;//####[24]####
//####[26]####
    public TestParaIterator(int threads, int size) {//####[26]####
        fThreads = threads;//####[27]####
        fSize = size;//####[28]####
    }//####[29]####
//####[31]####
    public void execute() {//####[31]####
        ArrayList<Integer> array = new ArrayList<Integer>();//####[32]####
        for (int i = 0; i < fSize; i++) //####[34]####
        {//####[34]####
            array.add(0);//####[35]####
        }//####[36]####
        fIterator = ParIteratorFactory.createParIterator(array, ParaTask.getThreadPoolSize());//####[38]####
        long start = System.nanoTime();//####[39]####
        TaskID id = process();//####[41]####
        try {//####[43]####
            id.waitTillFinished();//####[44]####
        } catch (ExecutionException e) {//####[45]####
            e.printStackTrace();//####[46]####
        } catch (InterruptedException e) {//####[47]####
            e.printStackTrace();//####[48]####
        }//####[49]####
        long finish = System.nanoTime();//####[51]####
        System.out.println(fSize + "\t" + (finish - start));//####[52]####
    }//####[53]####
//####[55]####
    private Method __pt__process_method = null;//####[55]####
    private Lock __pt__process_lock = new ReentrantLock();//####[55]####
    public TaskIDGroup<Void> process()  {//####[55]####
//####[55]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[55]####
        return process(new TaskInfo());//####[55]####
    }//####[55]####
    public TaskIDGroup<Void> process(TaskInfo taskinfo)  {//####[55]####
        if (__pt__process_method == null) {//####[55]####
            try {//####[55]####
                __pt__process_lock.lock();//####[55]####
                if (__pt__process_method == null) //####[55]####
                    __pt__process_method = ParaTaskHelper.getDeclaredMethod(getClass(), "__pt__process", new Class[] {});//####[55]####
            } catch (Exception e) {//####[55]####
                e.printStackTrace();//####[55]####
            } finally {//####[55]####
                __pt__process_lock.unlock();//####[55]####
            }//####[55]####
        }//####[55]####
//####[55]####
        taskinfo.setMethod(__pt__process_method);//####[55]####
        taskinfo.setInstance(this);//####[55]####
//####[55]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, fThreads);//####[55]####
    }//####[55]####
    public void __pt__process() {//####[55]####
        while (fIterator.hasNext()) //####[56]####
        {//####[56]####
            fIterator.next();//####[57]####
        }//####[58]####
    }//####[59]####
//####[59]####
}//####[59]####
