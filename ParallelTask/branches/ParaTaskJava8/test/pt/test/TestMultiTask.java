package pt.test;//####[1]####
//####[1]####
import java.lang.reflect.InvocationTargetException;//####[3]####
import java.lang.reflect.Method;//####[4]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[5]####
//####[5]####
//-- ParaTask related imports//####[5]####
import pt.runtime.*;//####[5]####
import java.util.concurrent.ExecutionException;//####[5]####
import java.util.concurrent.locks.*;//####[5]####
import java.lang.reflect.*;//####[5]####
import pt.runtime.GuiThread;//####[5]####
import java.util.concurrent.BlockingQueue;//####[5]####
import java.util.ArrayList;//####[5]####
import java.util.List;//####[5]####
//####[5]####
public class TestMultiTask {//####[7]####
    static{ParaTask.init();}//####[7]####
    /*  ParaTask helper method to access private/protected slots *///####[7]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[7]####
        if (m.getParameterTypes().length == 0)//####[7]####
            m.invoke(instance);//####[7]####
        else if ((m.getParameterTypes().length == 1))//####[7]####
            m.invoke(instance, arg);//####[7]####
        else //####[7]####
            m.invoke(instance, arg, interResult);//####[7]####
    }//####[7]####
//####[8]####
    private static final int N_DATASIZE = 0;//####[8]####
//####[10]####
    private static final String BM_METHOD = "execute";//####[10]####
//####[12]####
    private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = { int.class };//####[12]####
//####[14]####
    private static final String MOL = "MOL";//####[14]####
//####[16]####
    private static final String MOL_CLASS = "core.moldyn.Molcore";//####[16]####
//####[18]####
    private static final String MON = "MON";//####[18]####
//####[20]####
    private static final String MON_CLASS = "core.montecarlo.Moncore";//####[20]####
//####[22]####
    private static final String RAY = "RAY";//####[22]####
//####[24]####
    private static final String RAY_CLASS = "core.raytracer.Raycore";//####[24]####
//####[26]####
    private static ConcurrentLinkedQueue<Benchmark> concurrentLinkedQueue = null;//####[26]####
//####[28]####
    public static void main(String[] args) {//####[28]####
        if (null == args || args.length != 2) //####[29]####
        {//####[29]####
            try {//####[30]####
                throw new Exception("Wrong arguemnts setting");//####[31]####
            } catch (Exception e) {//####[32]####
                e.printStackTrace();//####[33]####
            }//####[34]####
        }//####[35]####
        TaskID tid_0 = runBMS_0(createBenchmarkSet(getBenchmarkClass(args[0]), Integer.valueOf(args[1])));//####[39]####
        TaskID tid_1 = runBMS_1(createBenchmarkSet(getBenchmarkClass(args[0]), Integer.valueOf(args[1])));//####[40]####
        TaskID tid_2 = runBMS_2(createBenchmarkSet(getBenchmarkClass(args[0]), Integer.valueOf(args[1])));//####[41]####
        TaskID tid_3 = runBMS_3(createBenchmarkSet(getBenchmarkClass(args[0]), Integer.valueOf(args[1])));//####[42]####
        TaskID tid_4 = runBMS_4(createBenchmarkSet(getBenchmarkClass(args[0]), Integer.valueOf(args[1])));//####[43]####
        TaskIDGroup tig = new TaskIDGroup(5);//####[45]####
        tig.add(tid_0);//####[47]####
        tig.add(tid_1);//####[48]####
        tig.add(tid_2);//####[49]####
        tig.add(tid_3);//####[50]####
        tig.add(tid_4);//####[51]####
        try {//####[53]####
            tig.waitTillFinished();//####[54]####
        } catch (ExecutionException e) {//####[55]####
            e.printStackTrace();//####[56]####
        } catch (InterruptedException e) {//####[57]####
            e.printStackTrace();//####[58]####
        }//####[59]####
    }//####[60]####
//####[61]####
    private static volatile Method __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method = null;//####[61]####
    private synchronized static void __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[61]####
        if (__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method == null) {//####[61]####
            try {//####[61]####
                __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_0", new Class[] {//####[61]####
                    ConcurrentLinkedQueue.class//####[61]####
                });//####[61]####
            } catch (Exception e) {//####[61]####
                e.printStackTrace();//####[61]####
            }//####[61]####
        }//####[61]####
    }//####[61]####
    private static TaskIDGroup<Void> runBMS_0(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return runBMS_0(benchmarkQueue, new TaskInfo());//####[61]####
    }//####[61]####
    private static TaskIDGroup<Void> runBMS_0(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method == null) {//####[61]####
            __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setParameters(benchmarkQueue);//####[61]####
        taskinfo.setMethod(__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[61]####
    }//####[61]####
    private static TaskIDGroup<Void> runBMS_0(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return runBMS_0(benchmarkQueue, new TaskInfo());//####[61]####
    }//####[61]####
    private static TaskIDGroup<Void> runBMS_0(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method == null) {//####[61]####
            __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setTaskIdArgIndexes(0);//####[61]####
        taskinfo.addDependsOn(benchmarkQueue);//####[61]####
        taskinfo.setParameters(benchmarkQueue);//####[61]####
        taskinfo.setMethod(__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[61]####
    }//####[61]####
    private static TaskIDGroup<Void> runBMS_0(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return runBMS_0(benchmarkQueue, new TaskInfo());//####[61]####
    }//####[61]####
    private static TaskIDGroup<Void> runBMS_0(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method == null) {//####[61]####
            __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setQueueArgIndexes(0);//####[61]####
        taskinfo.setIsPipeline(true);//####[61]####
        taskinfo.setParameters(benchmarkQueue);//####[61]####
        taskinfo.setMethod(__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[61]####
    }//####[61]####
    public static void __pt__runBMS_0(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[61]####
        System.out.println("Multi Task 0 [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[62]####
        Benchmark benchmark = null;//####[63]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[64]####
        {//####[64]####
            runBM(benchmark);//####[65]####
        }//####[66]####
    }//####[67]####
//####[67]####
//####[69]####
    private static volatile Method __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method = null;//####[69]####
    private synchronized static void __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[69]####
        if (__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method == null) {//####[69]####
            try {//####[69]####
                __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_1", new Class[] {//####[69]####
                    ConcurrentLinkedQueue.class//####[69]####
                });//####[69]####
            } catch (Exception e) {//####[69]####
                e.printStackTrace();//####[69]####
            }//####[69]####
        }//####[69]####
    }//####[69]####
    private static TaskIDGroup<Void> runBMS_1(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[69]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[69]####
        return runBMS_1(benchmarkQueue, new TaskInfo());//####[69]####
    }//####[69]####
    private static TaskIDGroup<Void> runBMS_1(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[69]####
        // ensure Method variable is set//####[69]####
        if (__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method == null) {//####[69]####
            __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[69]####
        }//####[69]####
        taskinfo.setParameters(benchmarkQueue);//####[69]####
        taskinfo.setMethod(__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method);//####[69]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[69]####
    }//####[69]####
    private static TaskIDGroup<Void> runBMS_1(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[69]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[69]####
        return runBMS_1(benchmarkQueue, new TaskInfo());//####[69]####
    }//####[69]####
    private static TaskIDGroup<Void> runBMS_1(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[69]####
        // ensure Method variable is set//####[69]####
        if (__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method == null) {//####[69]####
            __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[69]####
        }//####[69]####
        taskinfo.setTaskIdArgIndexes(0);//####[69]####
        taskinfo.addDependsOn(benchmarkQueue);//####[69]####
        taskinfo.setParameters(benchmarkQueue);//####[69]####
        taskinfo.setMethod(__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method);//####[69]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[69]####
    }//####[69]####
    private static TaskIDGroup<Void> runBMS_1(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[69]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[69]####
        return runBMS_1(benchmarkQueue, new TaskInfo());//####[69]####
    }//####[69]####
    private static TaskIDGroup<Void> runBMS_1(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[69]####
        // ensure Method variable is set//####[69]####
        if (__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method == null) {//####[69]####
            __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[69]####
        }//####[69]####
        taskinfo.setQueueArgIndexes(0);//####[69]####
        taskinfo.setIsPipeline(true);//####[69]####
        taskinfo.setParameters(benchmarkQueue);//####[69]####
        taskinfo.setMethod(__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method);//####[69]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[69]####
    }//####[69]####
    public static void __pt__runBMS_1(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[69]####
        System.out.println("Multi Task 1 [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[70]####
        Benchmark benchmark = null;//####[71]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[72]####
        {//####[72]####
            runBM(benchmark);//####[73]####
        }//####[74]####
    }//####[75]####
//####[75]####
//####[77]####
    private static volatile Method __pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method = null;//####[77]####
    private synchronized static void __pt__runBMS_2_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[77]####
        if (__pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method == null) {//####[77]####
            try {//####[77]####
                __pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_2", new Class[] {//####[77]####
                    ConcurrentLinkedQueue.class//####[77]####
                });//####[77]####
            } catch (Exception e) {//####[77]####
                e.printStackTrace();//####[77]####
            }//####[77]####
        }//####[77]####
    }//####[77]####
    private static TaskIDGroup<Void> runBMS_2(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[77]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[77]####
        return runBMS_2(benchmarkQueue, new TaskInfo());//####[77]####
    }//####[77]####
    private static TaskIDGroup<Void> runBMS_2(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[77]####
        // ensure Method variable is set//####[77]####
        if (__pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method == null) {//####[77]####
            __pt__runBMS_2_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[77]####
        }//####[77]####
        taskinfo.setParameters(benchmarkQueue);//####[77]####
        taskinfo.setMethod(__pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method);//####[77]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[77]####
    }//####[77]####
    private static TaskIDGroup<Void> runBMS_2(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[77]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[77]####
        return runBMS_2(benchmarkQueue, new TaskInfo());//####[77]####
    }//####[77]####
    private static TaskIDGroup<Void> runBMS_2(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[77]####
        // ensure Method variable is set//####[77]####
        if (__pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method == null) {//####[77]####
            __pt__runBMS_2_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[77]####
        }//####[77]####
        taskinfo.setTaskIdArgIndexes(0);//####[77]####
        taskinfo.addDependsOn(benchmarkQueue);//####[77]####
        taskinfo.setParameters(benchmarkQueue);//####[77]####
        taskinfo.setMethod(__pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method);//####[77]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[77]####
    }//####[77]####
    private static TaskIDGroup<Void> runBMS_2(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[77]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[77]####
        return runBMS_2(benchmarkQueue, new TaskInfo());//####[77]####
    }//####[77]####
    private static TaskIDGroup<Void> runBMS_2(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[77]####
        // ensure Method variable is set//####[77]####
        if (__pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method == null) {//####[77]####
            __pt__runBMS_2_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[77]####
        }//####[77]####
        taskinfo.setQueueArgIndexes(0);//####[77]####
        taskinfo.setIsPipeline(true);//####[77]####
        taskinfo.setParameters(benchmarkQueue);//####[77]####
        taskinfo.setMethod(__pt__runBMS_2_ConcurrentLinkedQueueBenchmark_method);//####[77]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[77]####
    }//####[77]####
    public static void __pt__runBMS_2(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[77]####
        System.out.println("Multi Task 2 [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[78]####
        Benchmark benchmark = null;//####[79]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[80]####
        {//####[80]####
            runBM(benchmark);//####[81]####
        }//####[82]####
    }//####[83]####
//####[83]####
//####[85]####
    private static volatile Method __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method = null;//####[85]####
    private synchronized static void __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[85]####
        if (__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method == null) {//####[85]####
            try {//####[85]####
                __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_3", new Class[] {//####[85]####
                    ConcurrentLinkedQueue.class//####[85]####
                });//####[85]####
            } catch (Exception e) {//####[85]####
                e.printStackTrace();//####[85]####
            }//####[85]####
        }//####[85]####
    }//####[85]####
    private static TaskIDGroup<Void> runBMS_3(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return runBMS_3(benchmarkQueue, new TaskInfo());//####[85]####
    }//####[85]####
    private static TaskIDGroup<Void> runBMS_3(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method == null) {//####[85]####
            __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setParameters(benchmarkQueue);//####[85]####
        taskinfo.setMethod(__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[85]####
    }//####[85]####
    private static TaskIDGroup<Void> runBMS_3(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return runBMS_3(benchmarkQueue, new TaskInfo());//####[85]####
    }//####[85]####
    private static TaskIDGroup<Void> runBMS_3(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method == null) {//####[85]####
            __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setTaskIdArgIndexes(0);//####[85]####
        taskinfo.addDependsOn(benchmarkQueue);//####[85]####
        taskinfo.setParameters(benchmarkQueue);//####[85]####
        taskinfo.setMethod(__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[85]####
    }//####[85]####
    private static TaskIDGroup<Void> runBMS_3(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return runBMS_3(benchmarkQueue, new TaskInfo());//####[85]####
    }//####[85]####
    private static TaskIDGroup<Void> runBMS_3(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method == null) {//####[85]####
            __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setQueueArgIndexes(0);//####[85]####
        taskinfo.setIsPipeline(true);//####[85]####
        taskinfo.setParameters(benchmarkQueue);//####[85]####
        taskinfo.setMethod(__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[85]####
    }//####[85]####
    public static void __pt__runBMS_3(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[85]####
        System.out.println("Multi Task 3 [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[86]####
        Benchmark benchmark = null;//####[87]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[88]####
        {//####[88]####
            runBM(benchmark);//####[89]####
        }//####[90]####
    }//####[91]####
//####[91]####
//####[93]####
    private static volatile Method __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method = null;//####[93]####
    private synchronized static void __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[93]####
        if (__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method == null) {//####[93]####
            try {//####[93]####
                __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_4", new Class[] {//####[93]####
                    ConcurrentLinkedQueue.class//####[93]####
                });//####[93]####
            } catch (Exception e) {//####[93]####
                e.printStackTrace();//####[93]####
            }//####[93]####
        }//####[93]####
    }//####[93]####
    private static TaskIDGroup<Void> runBMS_4(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[93]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[93]####
        return runBMS_4(benchmarkQueue, new TaskInfo());//####[93]####
    }//####[93]####
    private static TaskIDGroup<Void> runBMS_4(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[93]####
        // ensure Method variable is set//####[93]####
        if (__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method == null) {//####[93]####
            __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[93]####
        }//####[93]####
        taskinfo.setParameters(benchmarkQueue);//####[93]####
        taskinfo.setMethod(__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method);//####[93]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[93]####
    }//####[93]####
    private static TaskIDGroup<Void> runBMS_4(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[93]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[93]####
        return runBMS_4(benchmarkQueue, new TaskInfo());//####[93]####
    }//####[93]####
    private static TaskIDGroup<Void> runBMS_4(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[93]####
        // ensure Method variable is set//####[93]####
        if (__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method == null) {//####[93]####
            __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[93]####
        }//####[93]####
        taskinfo.setTaskIdArgIndexes(0);//####[93]####
        taskinfo.addDependsOn(benchmarkQueue);//####[93]####
        taskinfo.setParameters(benchmarkQueue);//####[93]####
        taskinfo.setMethod(__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method);//####[93]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[93]####
    }//####[93]####
    private static TaskIDGroup<Void> runBMS_4(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[93]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[93]####
        return runBMS_4(benchmarkQueue, new TaskInfo());//####[93]####
    }//####[93]####
    private static TaskIDGroup<Void> runBMS_4(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[93]####
        // ensure Method variable is set//####[93]####
        if (__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method == null) {//####[93]####
            __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[93]####
        }//####[93]####
        taskinfo.setQueueArgIndexes(0);//####[93]####
        taskinfo.setIsPipeline(true);//####[93]####
        taskinfo.setParameters(benchmarkQueue);//####[93]####
        taskinfo.setMethod(__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method);//####[93]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 8);//####[93]####
    }//####[93]####
    public static void __pt__runBMS_4(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[93]####
        System.out.println("Multi Task 4 [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[94]####
        Benchmark benchmark = null;//####[95]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[96]####
        {//####[96]####
            runBM(benchmark);//####[97]####
        }//####[98]####
    }//####[99]####
//####[99]####
//####[102]####
    private static void runBM(Benchmark benchmark) {//####[102]####
        try {//####[103]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[104]####
        } catch (IllegalAccessException e) {//####[105]####
            e.printStackTrace();//####[106]####
        } catch (IllegalArgumentException e) {//####[107]####
            e.printStackTrace();//####[108]####
        } catch (InvocationTargetException e) {//####[109]####
            e.printStackTrace();//####[110]####
        }//####[111]####
    }//####[112]####
//####[114]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[114]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[115]####
        for (int i = 0; i < setLen; i++) //####[116]####
        {//####[116]####
            Object benchmark = null;//####[117]####
            Method method = null;//####[118]####
            try {//####[119]####
                benchmark = bmClass.newInstance();//####[120]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[121]####
            } catch (InstantiationException e) {//####[122]####
                e.printStackTrace();//####[123]####
            } catch (IllegalAccessException e) {//####[124]####
                e.printStackTrace();//####[125]####
            } catch (NoSuchMethodException e) {//####[126]####
                e.printStackTrace();//####[127]####
            } catch (SecurityException e) {//####[128]####
                e.printStackTrace();//####[129]####
            } catch (IllegalArgumentException e) {//####[130]####
                e.printStackTrace();//####[131]####
            }//####[132]####
            Object[] arguments = new Object[1];//####[133]####
            arguments[0] = N_DATASIZE;//####[134]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[136]####
        }//####[138]####
        return concurrentLinkedQueue;//####[139]####
    }//####[140]####
//####[142]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[142]####
        Class<?> bmClass = null;//####[144]####
        try {//####[146]####
            if (bmName.equalsIgnoreCase(MOL)) //####[147]####
            {//####[147]####
                bmClass = Class.forName(MOL_CLASS);//####[148]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[149]####
            {//####[149]####
                bmClass = Class.forName(MON_CLASS);//####[150]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[151]####
            {//####[151]####
                bmClass = Class.forName(RAY_CLASS);//####[152]####
            } else {//####[153]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[154]####
            }//####[155]####
        } catch (ClassNotFoundException e) {//####[156]####
            e.printStackTrace();//####[157]####
        } catch (Exception e) {//####[158]####
            e.printStackTrace();//####[159]####
        }//####[160]####
        return bmClass;//####[162]####
    }//####[163]####
}//####[163]####
