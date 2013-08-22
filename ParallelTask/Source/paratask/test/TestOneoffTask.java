package paratask.test;//####[1]####
//####[1]####
import java.lang.reflect.InvocationTargetException;//####[3]####
import java.lang.reflect.Method;//####[4]####
//####[4]####
//-- ParaTask related imports//####[4]####
import paratask.runtime.*;//####[4]####
import java.util.concurrent.ExecutionException;//####[4]####
import java.util.concurrent.locks.*;//####[4]####
import java.lang.reflect.*;//####[4]####
import paratask.runtime.GuiThread;//####[4]####
import java.util.concurrent.BlockingQueue;//####[4]####
import java.util.ArrayList;//####[4]####
import java.util.List;//####[4]####
//####[4]####
public class TestOneoffTask {//####[6]####
    static{ParaTask.init();}//####[6]####
    /*  ParaTask helper method to access private/protected slots *///####[6]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[6]####
        if (m.getParameterTypes().length == 0)//####[6]####
            m.invoke(instance);//####[6]####
        else if ((m.getParameterTypes().length == 1))//####[6]####
            m.invoke(instance, arg);//####[6]####
        else //####[6]####
            m.invoke(instance, arg, interResult);//####[6]####
    }//####[6]####
//####[7]####
    private static final int N_DATASIZE = 0;//####[7]####
//####[9]####
    private static final String BM_METHOD = "execute";//####[9]####
//####[11]####
    private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = { int.class };//####[11]####
//####[13]####
    private static final String MOL = "MOL";//####[13]####
//####[15]####
    private static final String MOL_CLASS = "core.moldyn.Molcore";//####[15]####
//####[17]####
    private static final String MON = "MON";//####[17]####
//####[19]####
    private static final String MON_CLASS = "core.montecarlo.Moncore";//####[19]####
//####[21]####
    private static final String RAY = "RAY";//####[21]####
//####[23]####
    private static final String RAY_CLASS = "core.raytracer.Raycore";//####[23]####
//####[25]####
    public static void main(String[] args) {//####[25]####
        if (null == args || args.length != 1) //####[26]####
        {//####[26]####
            try {//####[27]####
                throw new Exception("Wrong arguemnts setting");//####[28]####
            } catch (Exception e) {//####[29]####
                e.printStackTrace();//####[30]####
            }//####[31]####
        }//####[32]####
        TaskID tid_0 = runBM_0(createBenchmark(getBenchmarkClass(args[0])));//####[34]####
        TaskID tid_1 = runBM_1(createBenchmark(getBenchmarkClass(args[0])));//####[35]####
        TaskID tid_2 = runBM_2(createBenchmark(getBenchmarkClass(args[0])));//####[36]####
        TaskID tid_3 = runBM_3(createBenchmark(getBenchmarkClass(args[0])));//####[37]####
        TaskID tid_4 = runBM_4(createBenchmark(getBenchmarkClass(args[0])));//####[38]####
        TaskID tid_5 = runBM_5(createBenchmark(getBenchmarkClass(args[0])));//####[39]####
        TaskID tid_6 = runBM_6(createBenchmark(getBenchmarkClass(args[0])));//####[40]####
        TaskID tid_7 = runBM_7(createBenchmark(getBenchmarkClass(args[0])));//####[41]####
        TaskID tid_8 = runBM_8(createBenchmark(getBenchmarkClass(args[0])));//####[42]####
        TaskID tid_9 = runBM_9(createBenchmark(getBenchmarkClass(args[0])));//####[43]####
        try {//####[45]####
            Thread.sleep(1000 * 30);//####[46]####
        } catch (InterruptedException e1) {//####[47]####
            e1.printStackTrace();//####[48]####
        }//####[49]####
        TaskID tid_10 = runBM_10(createBenchmark(getBenchmarkClass(args[0])));//####[51]####
        TaskID tid_11 = runBM_11(createBenchmark(getBenchmarkClass(args[0])));//####[52]####
        TaskID tid_12 = runBM_12(createBenchmark(getBenchmarkClass(args[0])));//####[53]####
        TaskID tid_13 = runBM_13(createBenchmark(getBenchmarkClass(args[0])));//####[54]####
        TaskID tid_14 = runBM_14(createBenchmark(getBenchmarkClass(args[0])));//####[55]####
        TaskID tid_15 = runBM_15(createBenchmark(getBenchmarkClass(args[0])));//####[56]####
        TaskID tid_16 = runBM_16(createBenchmark(getBenchmarkClass(args[0])));//####[57]####
        TaskID tid_17 = runBM_17(createBenchmark(getBenchmarkClass(args[0])));//####[58]####
        TaskID tid_18 = runBM_18(createBenchmark(getBenchmarkClass(args[0])));//####[59]####
        TaskID tid_19 = runBM_19(createBenchmark(getBenchmarkClass(args[0])));//####[60]####
        TaskIDGroup tig = new TaskIDGroup(20);//####[64]####
        tig.add(tid_0);//####[66]####
        tig.add(tid_1);//####[67]####
        tig.add(tid_2);//####[68]####
        tig.add(tid_3);//####[69]####
        tig.add(tid_4);//####[70]####
        tig.add(tid_5);//####[71]####
        tig.add(tid_6);//####[72]####
        tig.add(tid_7);//####[73]####
        tig.add(tid_8);//####[74]####
        tig.add(tid_9);//####[75]####
        tig.add(tid_10);//####[76]####
        tig.add(tid_11);//####[77]####
        tig.add(tid_12);//####[78]####
        tig.add(tid_13);//####[79]####
        tig.add(tid_14);//####[80]####
        tig.add(tid_15);//####[81]####
        tig.add(tid_16);//####[82]####
        tig.add(tid_17);//####[83]####
        tig.add(tid_18);//####[84]####
        tig.add(tid_19);//####[85]####
        try {//####[87]####
            tig.waitTillFinished();//####[88]####
        } catch (ExecutionException e) {//####[89]####
            e.printStackTrace();//####[90]####
        } catch (InterruptedException e) {//####[91]####
            e.printStackTrace();//####[92]####
        }//####[93]####
    }//####[94]####
//####[95]####
    private static volatile Method __pt__runBM_0_Benchmark_method = null;//####[95]####
    private synchronized static void __pt__runBM_0_Benchmark_ensureMethodVarSet() {//####[95]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[95]####
            try {//####[95]####
                __pt__runBM_0_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_0", new Class[] {//####[95]####
                    Benchmark.class//####[95]####
                });//####[95]####
            } catch (Exception e) {//####[95]####
                e.printStackTrace();//####[95]####
            }//####[95]####
        }//####[95]####
    }//####[95]####
    private static TaskID<Void> runBM_0(Benchmark benchmark) {//####[95]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[95]####
        return runBM_0(benchmark, new TaskInfo());//####[95]####
    }//####[95]####
    private static TaskID<Void> runBM_0(Benchmark benchmark, TaskInfo taskinfo) {//####[95]####
        // ensure Method variable is set//####[95]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[95]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[95]####
        }//####[95]####
        taskinfo.setParameters(benchmark);//####[95]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[95]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[95]####
    }//####[95]####
    private static TaskID<Void> runBM_0(TaskID<Benchmark> benchmark) {//####[95]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[95]####
        return runBM_0(benchmark, new TaskInfo());//####[95]####
    }//####[95]####
    private static TaskID<Void> runBM_0(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[95]####
        // ensure Method variable is set//####[95]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[95]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[95]####
        }//####[95]####
        taskinfo.setTaskIdArgIndexes(0);//####[95]####
        taskinfo.addDependsOn(benchmark);//####[95]####
        taskinfo.setParameters(benchmark);//####[95]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[95]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[95]####
    }//####[95]####
    private static TaskID<Void> runBM_0(BlockingQueue<Benchmark> benchmark) {//####[95]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[95]####
        return runBM_0(benchmark, new TaskInfo());//####[95]####
    }//####[95]####
    private static TaskID<Void> runBM_0(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[95]####
        // ensure Method variable is set//####[95]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[95]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[95]####
        }//####[95]####
        taskinfo.setQueueArgIndexes(0);//####[95]####
        taskinfo.setIsPipeline(true);//####[95]####
        taskinfo.setParameters(benchmark);//####[95]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[95]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[95]####
    }//####[95]####
    public static void __pt__runBM_0(Benchmark benchmark) {//####[95]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[95]####
        try {//####[96]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[96]####
        } catch (IllegalAccessException e) {//####[96]####
            e.printStackTrace();//####[96]####
        } catch (IllegalArgumentException e) {//####[96]####
            e.printStackTrace();//####[96]####
        } catch (InvocationTargetException e) {//####[96]####
            e.printStackTrace();//####[96]####
        }//####[96]####
    }//####[96]####
//####[96]####
//####[97]####
    private static volatile Method __pt__runBM_1_Benchmark_method = null;//####[97]####
    private synchronized static void __pt__runBM_1_Benchmark_ensureMethodVarSet() {//####[97]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[97]####
            try {//####[97]####
                __pt__runBM_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1", new Class[] {//####[97]####
                    Benchmark.class//####[97]####
                });//####[97]####
            } catch (Exception e) {//####[97]####
                e.printStackTrace();//####[97]####
            }//####[97]####
        }//####[97]####
    }//####[97]####
    private static TaskID<Void> runBM_1(Benchmark benchmark) {//####[97]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[97]####
        return runBM_1(benchmark, new TaskInfo());//####[97]####
    }//####[97]####
    private static TaskID<Void> runBM_1(Benchmark benchmark, TaskInfo taskinfo) {//####[97]####
        // ensure Method variable is set//####[97]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[97]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[97]####
        }//####[97]####
        taskinfo.setParameters(benchmark);//####[97]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[97]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[97]####
    }//####[97]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark) {//####[97]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[97]####
        return runBM_1(benchmark, new TaskInfo());//####[97]####
    }//####[97]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[97]####
        // ensure Method variable is set//####[97]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[97]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[97]####
        }//####[97]####
        taskinfo.setTaskIdArgIndexes(0);//####[97]####
        taskinfo.addDependsOn(benchmark);//####[97]####
        taskinfo.setParameters(benchmark);//####[97]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[97]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[97]####
    }//####[97]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark) {//####[97]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[97]####
        return runBM_1(benchmark, new TaskInfo());//####[97]####
    }//####[97]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[97]####
        // ensure Method variable is set//####[97]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[97]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[97]####
        }//####[97]####
        taskinfo.setQueueArgIndexes(0);//####[97]####
        taskinfo.setIsPipeline(true);//####[97]####
        taskinfo.setParameters(benchmark);//####[97]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[97]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[97]####
    }//####[97]####
    public static void __pt__runBM_1(Benchmark benchmark) {//####[97]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[97]####
        try {//####[98]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[98]####
        } catch (IllegalAccessException e) {//####[98]####
            e.printStackTrace();//####[98]####
        } catch (IllegalArgumentException e) {//####[98]####
            e.printStackTrace();//####[98]####
        } catch (InvocationTargetException e) {//####[98]####
            e.printStackTrace();//####[98]####
        }//####[98]####
    }//####[98]####
//####[98]####
//####[99]####
    private static volatile Method __pt__runBM_2_Benchmark_method = null;//####[99]####
    private synchronized static void __pt__runBM_2_Benchmark_ensureMethodVarSet() {//####[99]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[99]####
            try {//####[99]####
                __pt__runBM_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[99]####
                    Benchmark.class//####[99]####
                });//####[99]####
            } catch (Exception e) {//####[99]####
                e.printStackTrace();//####[99]####
            }//####[99]####
        }//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_2(Benchmark benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM_2(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_2(Benchmark benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[99]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM_2(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[99]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setTaskIdArgIndexes(0);//####[99]####
        taskinfo.addDependsOn(benchmark);//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM_2(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[99]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setQueueArgIndexes(0);//####[99]####
        taskinfo.setIsPipeline(true);//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    public static void __pt__runBM_2(Benchmark benchmark) {//####[99]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[99]####
        try {//####[100]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[100]####
        } catch (IllegalAccessException e) {//####[100]####
            e.printStackTrace();//####[100]####
        } catch (IllegalArgumentException e) {//####[100]####
            e.printStackTrace();//####[100]####
        } catch (InvocationTargetException e) {//####[100]####
            e.printStackTrace();//####[100]####
        }//####[100]####
    }//####[100]####
//####[100]####
//####[101]####
    private static volatile Method __pt__runBM_3_Benchmark_method = null;//####[101]####
    private synchronized static void __pt__runBM_3_Benchmark_ensureMethodVarSet() {//####[101]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[101]####
            try {//####[101]####
                __pt__runBM_3_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_3", new Class[] {//####[101]####
                    Benchmark.class//####[101]####
                });//####[101]####
            } catch (Exception e) {//####[101]####
                e.printStackTrace();//####[101]####
            }//####[101]####
        }//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_3(Benchmark benchmark) {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return runBM_3(benchmark, new TaskInfo());//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_3(Benchmark benchmark, TaskInfo taskinfo) {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[101]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setParameters(benchmark);//####[101]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_3(TaskID<Benchmark> benchmark) {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return runBM_3(benchmark, new TaskInfo());//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_3(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[101]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setTaskIdArgIndexes(0);//####[101]####
        taskinfo.addDependsOn(benchmark);//####[101]####
        taskinfo.setParameters(benchmark);//####[101]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_3(BlockingQueue<Benchmark> benchmark) {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return runBM_3(benchmark, new TaskInfo());//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_3(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[101]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setQueueArgIndexes(0);//####[101]####
        taskinfo.setIsPipeline(true);//####[101]####
        taskinfo.setParameters(benchmark);//####[101]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[101]####
    }//####[101]####
    public static void __pt__runBM_3(Benchmark benchmark) {//####[101]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[101]####
        try {//####[102]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[102]####
        } catch (IllegalAccessException e) {//####[102]####
            e.printStackTrace();//####[102]####
        } catch (IllegalArgumentException e) {//####[102]####
            e.printStackTrace();//####[102]####
        } catch (InvocationTargetException e) {//####[102]####
            e.printStackTrace();//####[102]####
        }//####[102]####
    }//####[102]####
//####[102]####
//####[103]####
    private static volatile Method __pt__runBM_4_Benchmark_method = null;//####[103]####
    private synchronized static void __pt__runBM_4_Benchmark_ensureMethodVarSet() {//####[103]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[103]####
            try {//####[103]####
                __pt__runBM_4_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_4", new Class[] {//####[103]####
                    Benchmark.class//####[103]####
                });//####[103]####
            } catch (Exception e) {//####[103]####
                e.printStackTrace();//####[103]####
            }//####[103]####
        }//####[103]####
    }//####[103]####
    private static TaskID<Void> runBM_4(Benchmark benchmark) {//####[103]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[103]####
        return runBM_4(benchmark, new TaskInfo());//####[103]####
    }//####[103]####
    private static TaskID<Void> runBM_4(Benchmark benchmark, TaskInfo taskinfo) {//####[103]####
        // ensure Method variable is set//####[103]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[103]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[103]####
        }//####[103]####
        taskinfo.setParameters(benchmark);//####[103]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[103]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[103]####
    }//####[103]####
    private static TaskID<Void> runBM_4(TaskID<Benchmark> benchmark) {//####[103]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[103]####
        return runBM_4(benchmark, new TaskInfo());//####[103]####
    }//####[103]####
    private static TaskID<Void> runBM_4(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[103]####
        // ensure Method variable is set//####[103]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[103]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[103]####
        }//####[103]####
        taskinfo.setTaskIdArgIndexes(0);//####[103]####
        taskinfo.addDependsOn(benchmark);//####[103]####
        taskinfo.setParameters(benchmark);//####[103]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[103]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[103]####
    }//####[103]####
    private static TaskID<Void> runBM_4(BlockingQueue<Benchmark> benchmark) {//####[103]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[103]####
        return runBM_4(benchmark, new TaskInfo());//####[103]####
    }//####[103]####
    private static TaskID<Void> runBM_4(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[103]####
        // ensure Method variable is set//####[103]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[103]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[103]####
        }//####[103]####
        taskinfo.setQueueArgIndexes(0);//####[103]####
        taskinfo.setIsPipeline(true);//####[103]####
        taskinfo.setParameters(benchmark);//####[103]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[103]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[103]####
    }//####[103]####
    public static void __pt__runBM_4(Benchmark benchmark) {//####[103]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[103]####
        try {//####[104]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[104]####
        } catch (IllegalAccessException e) {//####[104]####
            e.printStackTrace();//####[104]####
        } catch (IllegalArgumentException e) {//####[104]####
            e.printStackTrace();//####[104]####
        } catch (InvocationTargetException e) {//####[104]####
            e.printStackTrace();//####[104]####
        }//####[104]####
    }//####[104]####
//####[104]####
//####[105]####
    private static volatile Method __pt__runBM_5_Benchmark_method = null;//####[105]####
    private synchronized static void __pt__runBM_5_Benchmark_ensureMethodVarSet() {//####[105]####
        if (__pt__runBM_5_Benchmark_method == null) {//####[105]####
            try {//####[105]####
                __pt__runBM_5_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_5", new Class[] {//####[105]####
                    Benchmark.class//####[105]####
                });//####[105]####
            } catch (Exception e) {//####[105]####
                e.printStackTrace();//####[105]####
            }//####[105]####
        }//####[105]####
    }//####[105]####
    private static TaskID<Void> runBM_5(Benchmark benchmark) {//####[105]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[105]####
        return runBM_5(benchmark, new TaskInfo());//####[105]####
    }//####[105]####
    private static TaskID<Void> runBM_5(Benchmark benchmark, TaskInfo taskinfo) {//####[105]####
        // ensure Method variable is set//####[105]####
        if (__pt__runBM_5_Benchmark_method == null) {//####[105]####
            __pt__runBM_5_Benchmark_ensureMethodVarSet();//####[105]####
        }//####[105]####
        taskinfo.setParameters(benchmark);//####[105]####
        taskinfo.setMethod(__pt__runBM_5_Benchmark_method);//####[105]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[105]####
    }//####[105]####
    private static TaskID<Void> runBM_5(TaskID<Benchmark> benchmark) {//####[105]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[105]####
        return runBM_5(benchmark, new TaskInfo());//####[105]####
    }//####[105]####
    private static TaskID<Void> runBM_5(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[105]####
        // ensure Method variable is set//####[105]####
        if (__pt__runBM_5_Benchmark_method == null) {//####[105]####
            __pt__runBM_5_Benchmark_ensureMethodVarSet();//####[105]####
        }//####[105]####
        taskinfo.setTaskIdArgIndexes(0);//####[105]####
        taskinfo.addDependsOn(benchmark);//####[105]####
        taskinfo.setParameters(benchmark);//####[105]####
        taskinfo.setMethod(__pt__runBM_5_Benchmark_method);//####[105]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[105]####
    }//####[105]####
    private static TaskID<Void> runBM_5(BlockingQueue<Benchmark> benchmark) {//####[105]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[105]####
        return runBM_5(benchmark, new TaskInfo());//####[105]####
    }//####[105]####
    private static TaskID<Void> runBM_5(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[105]####
        // ensure Method variable is set//####[105]####
        if (__pt__runBM_5_Benchmark_method == null) {//####[105]####
            __pt__runBM_5_Benchmark_ensureMethodVarSet();//####[105]####
        }//####[105]####
        taskinfo.setQueueArgIndexes(0);//####[105]####
        taskinfo.setIsPipeline(true);//####[105]####
        taskinfo.setParameters(benchmark);//####[105]####
        taskinfo.setMethod(__pt__runBM_5_Benchmark_method);//####[105]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[105]####
    }//####[105]####
    public static void __pt__runBM_5(Benchmark benchmark) {//####[105]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[105]####
        try {//####[106]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[106]####
        } catch (IllegalAccessException e) {//####[106]####
            e.printStackTrace();//####[106]####
        } catch (IllegalArgumentException e) {//####[106]####
            e.printStackTrace();//####[106]####
        } catch (InvocationTargetException e) {//####[106]####
            e.printStackTrace();//####[106]####
        }//####[106]####
    }//####[106]####
//####[106]####
//####[107]####
    private static volatile Method __pt__runBM_6_Benchmark_method = null;//####[107]####
    private synchronized static void __pt__runBM_6_Benchmark_ensureMethodVarSet() {//####[107]####
        if (__pt__runBM_6_Benchmark_method == null) {//####[107]####
            try {//####[107]####
                __pt__runBM_6_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_6", new Class[] {//####[107]####
                    Benchmark.class//####[107]####
                });//####[107]####
            } catch (Exception e) {//####[107]####
                e.printStackTrace();//####[107]####
            }//####[107]####
        }//####[107]####
    }//####[107]####
    private static TaskID<Void> runBM_6(Benchmark benchmark) {//####[107]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[107]####
        return runBM_6(benchmark, new TaskInfo());//####[107]####
    }//####[107]####
    private static TaskID<Void> runBM_6(Benchmark benchmark, TaskInfo taskinfo) {//####[107]####
        // ensure Method variable is set//####[107]####
        if (__pt__runBM_6_Benchmark_method == null) {//####[107]####
            __pt__runBM_6_Benchmark_ensureMethodVarSet();//####[107]####
        }//####[107]####
        taskinfo.setParameters(benchmark);//####[107]####
        taskinfo.setMethod(__pt__runBM_6_Benchmark_method);//####[107]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[107]####
    }//####[107]####
    private static TaskID<Void> runBM_6(TaskID<Benchmark> benchmark) {//####[107]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[107]####
        return runBM_6(benchmark, new TaskInfo());//####[107]####
    }//####[107]####
    private static TaskID<Void> runBM_6(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[107]####
        // ensure Method variable is set//####[107]####
        if (__pt__runBM_6_Benchmark_method == null) {//####[107]####
            __pt__runBM_6_Benchmark_ensureMethodVarSet();//####[107]####
        }//####[107]####
        taskinfo.setTaskIdArgIndexes(0);//####[107]####
        taskinfo.addDependsOn(benchmark);//####[107]####
        taskinfo.setParameters(benchmark);//####[107]####
        taskinfo.setMethod(__pt__runBM_6_Benchmark_method);//####[107]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[107]####
    }//####[107]####
    private static TaskID<Void> runBM_6(BlockingQueue<Benchmark> benchmark) {//####[107]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[107]####
        return runBM_6(benchmark, new TaskInfo());//####[107]####
    }//####[107]####
    private static TaskID<Void> runBM_6(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[107]####
        // ensure Method variable is set//####[107]####
        if (__pt__runBM_6_Benchmark_method == null) {//####[107]####
            __pt__runBM_6_Benchmark_ensureMethodVarSet();//####[107]####
        }//####[107]####
        taskinfo.setQueueArgIndexes(0);//####[107]####
        taskinfo.setIsPipeline(true);//####[107]####
        taskinfo.setParameters(benchmark);//####[107]####
        taskinfo.setMethod(__pt__runBM_6_Benchmark_method);//####[107]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[107]####
    }//####[107]####
    public static void __pt__runBM_6(Benchmark benchmark) {//####[107]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[107]####
        try {//####[108]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[108]####
        } catch (IllegalAccessException e) {//####[108]####
            e.printStackTrace();//####[108]####
        } catch (IllegalArgumentException e) {//####[108]####
            e.printStackTrace();//####[108]####
        } catch (InvocationTargetException e) {//####[108]####
            e.printStackTrace();//####[108]####
        }//####[108]####
    }//####[108]####
//####[108]####
//####[109]####
    private static volatile Method __pt__runBM_7_Benchmark_method = null;//####[109]####
    private synchronized static void __pt__runBM_7_Benchmark_ensureMethodVarSet() {//####[109]####
        if (__pt__runBM_7_Benchmark_method == null) {//####[109]####
            try {//####[109]####
                __pt__runBM_7_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_7", new Class[] {//####[109]####
                    Benchmark.class//####[109]####
                });//####[109]####
            } catch (Exception e) {//####[109]####
                e.printStackTrace();//####[109]####
            }//####[109]####
        }//####[109]####
    }//####[109]####
    private static TaskID<Void> runBM_7(Benchmark benchmark) {//####[109]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[109]####
        return runBM_7(benchmark, new TaskInfo());//####[109]####
    }//####[109]####
    private static TaskID<Void> runBM_7(Benchmark benchmark, TaskInfo taskinfo) {//####[109]####
        // ensure Method variable is set//####[109]####
        if (__pt__runBM_7_Benchmark_method == null) {//####[109]####
            __pt__runBM_7_Benchmark_ensureMethodVarSet();//####[109]####
        }//####[109]####
        taskinfo.setParameters(benchmark);//####[109]####
        taskinfo.setMethod(__pt__runBM_7_Benchmark_method);//####[109]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[109]####
    }//####[109]####
    private static TaskID<Void> runBM_7(TaskID<Benchmark> benchmark) {//####[109]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[109]####
        return runBM_7(benchmark, new TaskInfo());//####[109]####
    }//####[109]####
    private static TaskID<Void> runBM_7(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[109]####
        // ensure Method variable is set//####[109]####
        if (__pt__runBM_7_Benchmark_method == null) {//####[109]####
            __pt__runBM_7_Benchmark_ensureMethodVarSet();//####[109]####
        }//####[109]####
        taskinfo.setTaskIdArgIndexes(0);//####[109]####
        taskinfo.addDependsOn(benchmark);//####[109]####
        taskinfo.setParameters(benchmark);//####[109]####
        taskinfo.setMethod(__pt__runBM_7_Benchmark_method);//####[109]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[109]####
    }//####[109]####
    private static TaskID<Void> runBM_7(BlockingQueue<Benchmark> benchmark) {//####[109]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[109]####
        return runBM_7(benchmark, new TaskInfo());//####[109]####
    }//####[109]####
    private static TaskID<Void> runBM_7(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[109]####
        // ensure Method variable is set//####[109]####
        if (__pt__runBM_7_Benchmark_method == null) {//####[109]####
            __pt__runBM_7_Benchmark_ensureMethodVarSet();//####[109]####
        }//####[109]####
        taskinfo.setQueueArgIndexes(0);//####[109]####
        taskinfo.setIsPipeline(true);//####[109]####
        taskinfo.setParameters(benchmark);//####[109]####
        taskinfo.setMethod(__pt__runBM_7_Benchmark_method);//####[109]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[109]####
    }//####[109]####
    public static void __pt__runBM_7(Benchmark benchmark) {//####[109]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[109]####
        try {//####[110]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[110]####
        } catch (IllegalAccessException e) {//####[110]####
            e.printStackTrace();//####[110]####
        } catch (IllegalArgumentException e) {//####[110]####
            e.printStackTrace();//####[110]####
        } catch (InvocationTargetException e) {//####[110]####
            e.printStackTrace();//####[110]####
        }//####[110]####
    }//####[110]####
//####[110]####
//####[111]####
    private static volatile Method __pt__runBM_8_Benchmark_method = null;//####[111]####
    private synchronized static void __pt__runBM_8_Benchmark_ensureMethodVarSet() {//####[111]####
        if (__pt__runBM_8_Benchmark_method == null) {//####[111]####
            try {//####[111]####
                __pt__runBM_8_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_8", new Class[] {//####[111]####
                    Benchmark.class//####[111]####
                });//####[111]####
            } catch (Exception e) {//####[111]####
                e.printStackTrace();//####[111]####
            }//####[111]####
        }//####[111]####
    }//####[111]####
    private static TaskID<Void> runBM_8(Benchmark benchmark) {//####[111]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[111]####
        return runBM_8(benchmark, new TaskInfo());//####[111]####
    }//####[111]####
    private static TaskID<Void> runBM_8(Benchmark benchmark, TaskInfo taskinfo) {//####[111]####
        // ensure Method variable is set//####[111]####
        if (__pt__runBM_8_Benchmark_method == null) {//####[111]####
            __pt__runBM_8_Benchmark_ensureMethodVarSet();//####[111]####
        }//####[111]####
        taskinfo.setParameters(benchmark);//####[111]####
        taskinfo.setMethod(__pt__runBM_8_Benchmark_method);//####[111]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[111]####
    }//####[111]####
    private static TaskID<Void> runBM_8(TaskID<Benchmark> benchmark) {//####[111]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[111]####
        return runBM_8(benchmark, new TaskInfo());//####[111]####
    }//####[111]####
    private static TaskID<Void> runBM_8(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[111]####
        // ensure Method variable is set//####[111]####
        if (__pt__runBM_8_Benchmark_method == null) {//####[111]####
            __pt__runBM_8_Benchmark_ensureMethodVarSet();//####[111]####
        }//####[111]####
        taskinfo.setTaskIdArgIndexes(0);//####[111]####
        taskinfo.addDependsOn(benchmark);//####[111]####
        taskinfo.setParameters(benchmark);//####[111]####
        taskinfo.setMethod(__pt__runBM_8_Benchmark_method);//####[111]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[111]####
    }//####[111]####
    private static TaskID<Void> runBM_8(BlockingQueue<Benchmark> benchmark) {//####[111]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[111]####
        return runBM_8(benchmark, new TaskInfo());//####[111]####
    }//####[111]####
    private static TaskID<Void> runBM_8(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[111]####
        // ensure Method variable is set//####[111]####
        if (__pt__runBM_8_Benchmark_method == null) {//####[111]####
            __pt__runBM_8_Benchmark_ensureMethodVarSet();//####[111]####
        }//####[111]####
        taskinfo.setQueueArgIndexes(0);//####[111]####
        taskinfo.setIsPipeline(true);//####[111]####
        taskinfo.setParameters(benchmark);//####[111]####
        taskinfo.setMethod(__pt__runBM_8_Benchmark_method);//####[111]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[111]####
    }//####[111]####
    public static void __pt__runBM_8(Benchmark benchmark) {//####[111]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[111]####
        try {//####[112]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[112]####
        } catch (IllegalAccessException e) {//####[112]####
            e.printStackTrace();//####[112]####
        } catch (IllegalArgumentException e) {//####[112]####
            e.printStackTrace();//####[112]####
        } catch (InvocationTargetException e) {//####[112]####
            e.printStackTrace();//####[112]####
        }//####[112]####
    }//####[112]####
//####[112]####
//####[113]####
    private static volatile Method __pt__runBM_9_Benchmark_method = null;//####[113]####
    private synchronized static void __pt__runBM_9_Benchmark_ensureMethodVarSet() {//####[113]####
        if (__pt__runBM_9_Benchmark_method == null) {//####[113]####
            try {//####[113]####
                __pt__runBM_9_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_9", new Class[] {//####[113]####
                    Benchmark.class//####[113]####
                });//####[113]####
            } catch (Exception e) {//####[113]####
                e.printStackTrace();//####[113]####
            }//####[113]####
        }//####[113]####
    }//####[113]####
    private static TaskID<Void> runBM_9(Benchmark benchmark) {//####[113]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[113]####
        return runBM_9(benchmark, new TaskInfo());//####[113]####
    }//####[113]####
    private static TaskID<Void> runBM_9(Benchmark benchmark, TaskInfo taskinfo) {//####[113]####
        // ensure Method variable is set//####[113]####
        if (__pt__runBM_9_Benchmark_method == null) {//####[113]####
            __pt__runBM_9_Benchmark_ensureMethodVarSet();//####[113]####
        }//####[113]####
        taskinfo.setParameters(benchmark);//####[113]####
        taskinfo.setMethod(__pt__runBM_9_Benchmark_method);//####[113]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[113]####
    }//####[113]####
    private static TaskID<Void> runBM_9(TaskID<Benchmark> benchmark) {//####[113]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[113]####
        return runBM_9(benchmark, new TaskInfo());//####[113]####
    }//####[113]####
    private static TaskID<Void> runBM_9(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[113]####
        // ensure Method variable is set//####[113]####
        if (__pt__runBM_9_Benchmark_method == null) {//####[113]####
            __pt__runBM_9_Benchmark_ensureMethodVarSet();//####[113]####
        }//####[113]####
        taskinfo.setTaskIdArgIndexes(0);//####[113]####
        taskinfo.addDependsOn(benchmark);//####[113]####
        taskinfo.setParameters(benchmark);//####[113]####
        taskinfo.setMethod(__pt__runBM_9_Benchmark_method);//####[113]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[113]####
    }//####[113]####
    private static TaskID<Void> runBM_9(BlockingQueue<Benchmark> benchmark) {//####[113]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[113]####
        return runBM_9(benchmark, new TaskInfo());//####[113]####
    }//####[113]####
    private static TaskID<Void> runBM_9(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[113]####
        // ensure Method variable is set//####[113]####
        if (__pt__runBM_9_Benchmark_method == null) {//####[113]####
            __pt__runBM_9_Benchmark_ensureMethodVarSet();//####[113]####
        }//####[113]####
        taskinfo.setQueueArgIndexes(0);//####[113]####
        taskinfo.setIsPipeline(true);//####[113]####
        taskinfo.setParameters(benchmark);//####[113]####
        taskinfo.setMethod(__pt__runBM_9_Benchmark_method);//####[113]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[113]####
    }//####[113]####
    public static void __pt__runBM_9(Benchmark benchmark) {//####[113]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[113]####
        try {//####[114]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[114]####
        } catch (IllegalAccessException e) {//####[114]####
            e.printStackTrace();//####[114]####
        } catch (IllegalArgumentException e) {//####[114]####
            e.printStackTrace();//####[114]####
        } catch (InvocationTargetException e) {//####[114]####
            e.printStackTrace();//####[114]####
        }//####[114]####
    }//####[114]####
//####[114]####
//####[115]####
    private static volatile Method __pt__runBM_10_Benchmark_method = null;//####[115]####
    private synchronized static void __pt__runBM_10_Benchmark_ensureMethodVarSet() {//####[115]####
        if (__pt__runBM_10_Benchmark_method == null) {//####[115]####
            try {//####[115]####
                __pt__runBM_10_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_10", new Class[] {//####[115]####
                    Benchmark.class//####[115]####
                });//####[115]####
            } catch (Exception e) {//####[115]####
                e.printStackTrace();//####[115]####
            }//####[115]####
        }//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM_10(Benchmark benchmark) {//####[115]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[115]####
        return runBM_10(benchmark, new TaskInfo());//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM_10(Benchmark benchmark, TaskInfo taskinfo) {//####[115]####
        // ensure Method variable is set//####[115]####
        if (__pt__runBM_10_Benchmark_method == null) {//####[115]####
            __pt__runBM_10_Benchmark_ensureMethodVarSet();//####[115]####
        }//####[115]####
        taskinfo.setParameters(benchmark);//####[115]####
        taskinfo.setMethod(__pt__runBM_10_Benchmark_method);//####[115]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM_10(TaskID<Benchmark> benchmark) {//####[115]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[115]####
        return runBM_10(benchmark, new TaskInfo());//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM_10(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[115]####
        // ensure Method variable is set//####[115]####
        if (__pt__runBM_10_Benchmark_method == null) {//####[115]####
            __pt__runBM_10_Benchmark_ensureMethodVarSet();//####[115]####
        }//####[115]####
        taskinfo.setTaskIdArgIndexes(0);//####[115]####
        taskinfo.addDependsOn(benchmark);//####[115]####
        taskinfo.setParameters(benchmark);//####[115]####
        taskinfo.setMethod(__pt__runBM_10_Benchmark_method);//####[115]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM_10(BlockingQueue<Benchmark> benchmark) {//####[115]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[115]####
        return runBM_10(benchmark, new TaskInfo());//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM_10(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[115]####
        // ensure Method variable is set//####[115]####
        if (__pt__runBM_10_Benchmark_method == null) {//####[115]####
            __pt__runBM_10_Benchmark_ensureMethodVarSet();//####[115]####
        }//####[115]####
        taskinfo.setQueueArgIndexes(0);//####[115]####
        taskinfo.setIsPipeline(true);//####[115]####
        taskinfo.setParameters(benchmark);//####[115]####
        taskinfo.setMethod(__pt__runBM_10_Benchmark_method);//####[115]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[115]####
    }//####[115]####
    public static void __pt__runBM_10(Benchmark benchmark) {//####[115]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[115]####
        try {//####[116]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[116]####
        } catch (IllegalAccessException e) {//####[116]####
            e.printStackTrace();//####[116]####
        } catch (IllegalArgumentException e) {//####[116]####
            e.printStackTrace();//####[116]####
        } catch (InvocationTargetException e) {//####[116]####
            e.printStackTrace();//####[116]####
        }//####[116]####
    }//####[116]####
//####[116]####
//####[117]####
    private static volatile Method __pt__runBM_11_Benchmark_method = null;//####[117]####
    private synchronized static void __pt__runBM_11_Benchmark_ensureMethodVarSet() {//####[117]####
        if (__pt__runBM_11_Benchmark_method == null) {//####[117]####
            try {//####[117]####
                __pt__runBM_11_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_11", new Class[] {//####[117]####
                    Benchmark.class//####[117]####
                });//####[117]####
            } catch (Exception e) {//####[117]####
                e.printStackTrace();//####[117]####
            }//####[117]####
        }//####[117]####
    }//####[117]####
    private static TaskID<Void> runBM_11(Benchmark benchmark) {//####[117]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[117]####
        return runBM_11(benchmark, new TaskInfo());//####[117]####
    }//####[117]####
    private static TaskID<Void> runBM_11(Benchmark benchmark, TaskInfo taskinfo) {//####[117]####
        // ensure Method variable is set//####[117]####
        if (__pt__runBM_11_Benchmark_method == null) {//####[117]####
            __pt__runBM_11_Benchmark_ensureMethodVarSet();//####[117]####
        }//####[117]####
        taskinfo.setParameters(benchmark);//####[117]####
        taskinfo.setMethod(__pt__runBM_11_Benchmark_method);//####[117]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[117]####
    }//####[117]####
    private static TaskID<Void> runBM_11(TaskID<Benchmark> benchmark) {//####[117]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[117]####
        return runBM_11(benchmark, new TaskInfo());//####[117]####
    }//####[117]####
    private static TaskID<Void> runBM_11(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[117]####
        // ensure Method variable is set//####[117]####
        if (__pt__runBM_11_Benchmark_method == null) {//####[117]####
            __pt__runBM_11_Benchmark_ensureMethodVarSet();//####[117]####
        }//####[117]####
        taskinfo.setTaskIdArgIndexes(0);//####[117]####
        taskinfo.addDependsOn(benchmark);//####[117]####
        taskinfo.setParameters(benchmark);//####[117]####
        taskinfo.setMethod(__pt__runBM_11_Benchmark_method);//####[117]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[117]####
    }//####[117]####
    private static TaskID<Void> runBM_11(BlockingQueue<Benchmark> benchmark) {//####[117]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[117]####
        return runBM_11(benchmark, new TaskInfo());//####[117]####
    }//####[117]####
    private static TaskID<Void> runBM_11(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[117]####
        // ensure Method variable is set//####[117]####
        if (__pt__runBM_11_Benchmark_method == null) {//####[117]####
            __pt__runBM_11_Benchmark_ensureMethodVarSet();//####[117]####
        }//####[117]####
        taskinfo.setQueueArgIndexes(0);//####[117]####
        taskinfo.setIsPipeline(true);//####[117]####
        taskinfo.setParameters(benchmark);//####[117]####
        taskinfo.setMethod(__pt__runBM_11_Benchmark_method);//####[117]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[117]####
    }//####[117]####
    public static void __pt__runBM_11(Benchmark benchmark) {//####[117]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[117]####
        try {//####[118]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[118]####
        } catch (IllegalAccessException e) {//####[118]####
            e.printStackTrace();//####[118]####
        } catch (IllegalArgumentException e) {//####[118]####
            e.printStackTrace();//####[118]####
        } catch (InvocationTargetException e) {//####[118]####
            e.printStackTrace();//####[118]####
        }//####[118]####
    }//####[118]####
//####[118]####
//####[119]####
    private static volatile Method __pt__runBM_12_Benchmark_method = null;//####[119]####
    private synchronized static void __pt__runBM_12_Benchmark_ensureMethodVarSet() {//####[119]####
        if (__pt__runBM_12_Benchmark_method == null) {//####[119]####
            try {//####[119]####
                __pt__runBM_12_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_12", new Class[] {//####[119]####
                    Benchmark.class//####[119]####
                });//####[119]####
            } catch (Exception e) {//####[119]####
                e.printStackTrace();//####[119]####
            }//####[119]####
        }//####[119]####
    }//####[119]####
    private static TaskID<Void> runBM_12(Benchmark benchmark) {//####[119]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[119]####
        return runBM_12(benchmark, new TaskInfo());//####[119]####
    }//####[119]####
    private static TaskID<Void> runBM_12(Benchmark benchmark, TaskInfo taskinfo) {//####[119]####
        // ensure Method variable is set//####[119]####
        if (__pt__runBM_12_Benchmark_method == null) {//####[119]####
            __pt__runBM_12_Benchmark_ensureMethodVarSet();//####[119]####
        }//####[119]####
        taskinfo.setParameters(benchmark);//####[119]####
        taskinfo.setMethod(__pt__runBM_12_Benchmark_method);//####[119]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[119]####
    }//####[119]####
    private static TaskID<Void> runBM_12(TaskID<Benchmark> benchmark) {//####[119]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[119]####
        return runBM_12(benchmark, new TaskInfo());//####[119]####
    }//####[119]####
    private static TaskID<Void> runBM_12(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[119]####
        // ensure Method variable is set//####[119]####
        if (__pt__runBM_12_Benchmark_method == null) {//####[119]####
            __pt__runBM_12_Benchmark_ensureMethodVarSet();//####[119]####
        }//####[119]####
        taskinfo.setTaskIdArgIndexes(0);//####[119]####
        taskinfo.addDependsOn(benchmark);//####[119]####
        taskinfo.setParameters(benchmark);//####[119]####
        taskinfo.setMethod(__pt__runBM_12_Benchmark_method);//####[119]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[119]####
    }//####[119]####
    private static TaskID<Void> runBM_12(BlockingQueue<Benchmark> benchmark) {//####[119]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[119]####
        return runBM_12(benchmark, new TaskInfo());//####[119]####
    }//####[119]####
    private static TaskID<Void> runBM_12(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[119]####
        // ensure Method variable is set//####[119]####
        if (__pt__runBM_12_Benchmark_method == null) {//####[119]####
            __pt__runBM_12_Benchmark_ensureMethodVarSet();//####[119]####
        }//####[119]####
        taskinfo.setQueueArgIndexes(0);//####[119]####
        taskinfo.setIsPipeline(true);//####[119]####
        taskinfo.setParameters(benchmark);//####[119]####
        taskinfo.setMethod(__pt__runBM_12_Benchmark_method);//####[119]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[119]####
    }//####[119]####
    public static void __pt__runBM_12(Benchmark benchmark) {//####[119]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[119]####
        try {//####[120]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[120]####
        } catch (IllegalAccessException e) {//####[120]####
            e.printStackTrace();//####[120]####
        } catch (IllegalArgumentException e) {//####[120]####
            e.printStackTrace();//####[120]####
        } catch (InvocationTargetException e) {//####[120]####
            e.printStackTrace();//####[120]####
        }//####[120]####
    }//####[120]####
//####[120]####
//####[121]####
    private static volatile Method __pt__runBM_13_Benchmark_method = null;//####[121]####
    private synchronized static void __pt__runBM_13_Benchmark_ensureMethodVarSet() {//####[121]####
        if (__pt__runBM_13_Benchmark_method == null) {//####[121]####
            try {//####[121]####
                __pt__runBM_13_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_13", new Class[] {//####[121]####
                    Benchmark.class//####[121]####
                });//####[121]####
            } catch (Exception e) {//####[121]####
                e.printStackTrace();//####[121]####
            }//####[121]####
        }//####[121]####
    }//####[121]####
    private static TaskID<Void> runBM_13(Benchmark benchmark) {//####[121]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[121]####
        return runBM_13(benchmark, new TaskInfo());//####[121]####
    }//####[121]####
    private static TaskID<Void> runBM_13(Benchmark benchmark, TaskInfo taskinfo) {//####[121]####
        // ensure Method variable is set//####[121]####
        if (__pt__runBM_13_Benchmark_method == null) {//####[121]####
            __pt__runBM_13_Benchmark_ensureMethodVarSet();//####[121]####
        }//####[121]####
        taskinfo.setParameters(benchmark);//####[121]####
        taskinfo.setMethod(__pt__runBM_13_Benchmark_method);//####[121]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[121]####
    }//####[121]####
    private static TaskID<Void> runBM_13(TaskID<Benchmark> benchmark) {//####[121]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[121]####
        return runBM_13(benchmark, new TaskInfo());//####[121]####
    }//####[121]####
    private static TaskID<Void> runBM_13(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[121]####
        // ensure Method variable is set//####[121]####
        if (__pt__runBM_13_Benchmark_method == null) {//####[121]####
            __pt__runBM_13_Benchmark_ensureMethodVarSet();//####[121]####
        }//####[121]####
        taskinfo.setTaskIdArgIndexes(0);//####[121]####
        taskinfo.addDependsOn(benchmark);//####[121]####
        taskinfo.setParameters(benchmark);//####[121]####
        taskinfo.setMethod(__pt__runBM_13_Benchmark_method);//####[121]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[121]####
    }//####[121]####
    private static TaskID<Void> runBM_13(BlockingQueue<Benchmark> benchmark) {//####[121]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[121]####
        return runBM_13(benchmark, new TaskInfo());//####[121]####
    }//####[121]####
    private static TaskID<Void> runBM_13(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[121]####
        // ensure Method variable is set//####[121]####
        if (__pt__runBM_13_Benchmark_method == null) {//####[121]####
            __pt__runBM_13_Benchmark_ensureMethodVarSet();//####[121]####
        }//####[121]####
        taskinfo.setQueueArgIndexes(0);//####[121]####
        taskinfo.setIsPipeline(true);//####[121]####
        taskinfo.setParameters(benchmark);//####[121]####
        taskinfo.setMethod(__pt__runBM_13_Benchmark_method);//####[121]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[121]####
    }//####[121]####
    public static void __pt__runBM_13(Benchmark benchmark) {//####[121]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[121]####
        try {//####[122]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[122]####
        } catch (IllegalAccessException e) {//####[122]####
            e.printStackTrace();//####[122]####
        } catch (IllegalArgumentException e) {//####[122]####
            e.printStackTrace();//####[122]####
        } catch (InvocationTargetException e) {//####[122]####
            e.printStackTrace();//####[122]####
        }//####[122]####
    }//####[122]####
//####[122]####
//####[123]####
    private static volatile Method __pt__runBM_14_Benchmark_method = null;//####[123]####
    private synchronized static void __pt__runBM_14_Benchmark_ensureMethodVarSet() {//####[123]####
        if (__pt__runBM_14_Benchmark_method == null) {//####[123]####
            try {//####[123]####
                __pt__runBM_14_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_14", new Class[] {//####[123]####
                    Benchmark.class//####[123]####
                });//####[123]####
            } catch (Exception e) {//####[123]####
                e.printStackTrace();//####[123]####
            }//####[123]####
        }//####[123]####
    }//####[123]####
    private static TaskID<Void> runBM_14(Benchmark benchmark) {//####[123]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[123]####
        return runBM_14(benchmark, new TaskInfo());//####[123]####
    }//####[123]####
    private static TaskID<Void> runBM_14(Benchmark benchmark, TaskInfo taskinfo) {//####[123]####
        // ensure Method variable is set//####[123]####
        if (__pt__runBM_14_Benchmark_method == null) {//####[123]####
            __pt__runBM_14_Benchmark_ensureMethodVarSet();//####[123]####
        }//####[123]####
        taskinfo.setParameters(benchmark);//####[123]####
        taskinfo.setMethod(__pt__runBM_14_Benchmark_method);//####[123]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[123]####
    }//####[123]####
    private static TaskID<Void> runBM_14(TaskID<Benchmark> benchmark) {//####[123]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[123]####
        return runBM_14(benchmark, new TaskInfo());//####[123]####
    }//####[123]####
    private static TaskID<Void> runBM_14(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[123]####
        // ensure Method variable is set//####[123]####
        if (__pt__runBM_14_Benchmark_method == null) {//####[123]####
            __pt__runBM_14_Benchmark_ensureMethodVarSet();//####[123]####
        }//####[123]####
        taskinfo.setTaskIdArgIndexes(0);//####[123]####
        taskinfo.addDependsOn(benchmark);//####[123]####
        taskinfo.setParameters(benchmark);//####[123]####
        taskinfo.setMethod(__pt__runBM_14_Benchmark_method);//####[123]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[123]####
    }//####[123]####
    private static TaskID<Void> runBM_14(BlockingQueue<Benchmark> benchmark) {//####[123]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[123]####
        return runBM_14(benchmark, new TaskInfo());//####[123]####
    }//####[123]####
    private static TaskID<Void> runBM_14(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[123]####
        // ensure Method variable is set//####[123]####
        if (__pt__runBM_14_Benchmark_method == null) {//####[123]####
            __pt__runBM_14_Benchmark_ensureMethodVarSet();//####[123]####
        }//####[123]####
        taskinfo.setQueueArgIndexes(0);//####[123]####
        taskinfo.setIsPipeline(true);//####[123]####
        taskinfo.setParameters(benchmark);//####[123]####
        taskinfo.setMethod(__pt__runBM_14_Benchmark_method);//####[123]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[123]####
    }//####[123]####
    public static void __pt__runBM_14(Benchmark benchmark) {//####[123]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[123]####
        try {//####[124]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[124]####
        } catch (IllegalAccessException e) {//####[124]####
            e.printStackTrace();//####[124]####
        } catch (IllegalArgumentException e) {//####[124]####
            e.printStackTrace();//####[124]####
        } catch (InvocationTargetException e) {//####[124]####
            e.printStackTrace();//####[124]####
        }//####[124]####
    }//####[124]####
//####[124]####
//####[125]####
    private static volatile Method __pt__runBM_15_Benchmark_method = null;//####[125]####
    private synchronized static void __pt__runBM_15_Benchmark_ensureMethodVarSet() {//####[125]####
        if (__pt__runBM_15_Benchmark_method == null) {//####[125]####
            try {//####[125]####
                __pt__runBM_15_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_15", new Class[] {//####[125]####
                    Benchmark.class//####[125]####
                });//####[125]####
            } catch (Exception e) {//####[125]####
                e.printStackTrace();//####[125]####
            }//####[125]####
        }//####[125]####
    }//####[125]####
    private static TaskID<Void> runBM_15(Benchmark benchmark) {//####[125]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[125]####
        return runBM_15(benchmark, new TaskInfo());//####[125]####
    }//####[125]####
    private static TaskID<Void> runBM_15(Benchmark benchmark, TaskInfo taskinfo) {//####[125]####
        // ensure Method variable is set//####[125]####
        if (__pt__runBM_15_Benchmark_method == null) {//####[125]####
            __pt__runBM_15_Benchmark_ensureMethodVarSet();//####[125]####
        }//####[125]####
        taskinfo.setParameters(benchmark);//####[125]####
        taskinfo.setMethod(__pt__runBM_15_Benchmark_method);//####[125]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[125]####
    }//####[125]####
    private static TaskID<Void> runBM_15(TaskID<Benchmark> benchmark) {//####[125]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[125]####
        return runBM_15(benchmark, new TaskInfo());//####[125]####
    }//####[125]####
    private static TaskID<Void> runBM_15(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[125]####
        // ensure Method variable is set//####[125]####
        if (__pt__runBM_15_Benchmark_method == null) {//####[125]####
            __pt__runBM_15_Benchmark_ensureMethodVarSet();//####[125]####
        }//####[125]####
        taskinfo.setTaskIdArgIndexes(0);//####[125]####
        taskinfo.addDependsOn(benchmark);//####[125]####
        taskinfo.setParameters(benchmark);//####[125]####
        taskinfo.setMethod(__pt__runBM_15_Benchmark_method);//####[125]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[125]####
    }//####[125]####
    private static TaskID<Void> runBM_15(BlockingQueue<Benchmark> benchmark) {//####[125]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[125]####
        return runBM_15(benchmark, new TaskInfo());//####[125]####
    }//####[125]####
    private static TaskID<Void> runBM_15(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[125]####
        // ensure Method variable is set//####[125]####
        if (__pt__runBM_15_Benchmark_method == null) {//####[125]####
            __pt__runBM_15_Benchmark_ensureMethodVarSet();//####[125]####
        }//####[125]####
        taskinfo.setQueueArgIndexes(0);//####[125]####
        taskinfo.setIsPipeline(true);//####[125]####
        taskinfo.setParameters(benchmark);//####[125]####
        taskinfo.setMethod(__pt__runBM_15_Benchmark_method);//####[125]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[125]####
    }//####[125]####
    public static void __pt__runBM_15(Benchmark benchmark) {//####[125]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[125]####
        try {//####[126]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[126]####
        } catch (IllegalAccessException e) {//####[126]####
            e.printStackTrace();//####[126]####
        } catch (IllegalArgumentException e) {//####[126]####
            e.printStackTrace();//####[126]####
        } catch (InvocationTargetException e) {//####[126]####
            e.printStackTrace();//####[126]####
        }//####[126]####
    }//####[126]####
//####[126]####
//####[127]####
    private static volatile Method __pt__runBM_16_Benchmark_method = null;//####[127]####
    private synchronized static void __pt__runBM_16_Benchmark_ensureMethodVarSet() {//####[127]####
        if (__pt__runBM_16_Benchmark_method == null) {//####[127]####
            try {//####[127]####
                __pt__runBM_16_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_16", new Class[] {//####[127]####
                    Benchmark.class//####[127]####
                });//####[127]####
            } catch (Exception e) {//####[127]####
                e.printStackTrace();//####[127]####
            }//####[127]####
        }//####[127]####
    }//####[127]####
    private static TaskID<Void> runBM_16(Benchmark benchmark) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return runBM_16(benchmark, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Void> runBM_16(Benchmark benchmark, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__runBM_16_Benchmark_method == null) {//####[127]####
            __pt__runBM_16_Benchmark_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setParameters(benchmark);//####[127]####
        taskinfo.setMethod(__pt__runBM_16_Benchmark_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Void> runBM_16(TaskID<Benchmark> benchmark) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return runBM_16(benchmark, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Void> runBM_16(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__runBM_16_Benchmark_method == null) {//####[127]####
            __pt__runBM_16_Benchmark_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setTaskIdArgIndexes(0);//####[127]####
        taskinfo.addDependsOn(benchmark);//####[127]####
        taskinfo.setParameters(benchmark);//####[127]####
        taskinfo.setMethod(__pt__runBM_16_Benchmark_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Void> runBM_16(BlockingQueue<Benchmark> benchmark) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return runBM_16(benchmark, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Void> runBM_16(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__runBM_16_Benchmark_method == null) {//####[127]####
            __pt__runBM_16_Benchmark_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setQueueArgIndexes(0);//####[127]####
        taskinfo.setIsPipeline(true);//####[127]####
        taskinfo.setParameters(benchmark);//####[127]####
        taskinfo.setMethod(__pt__runBM_16_Benchmark_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    public static void __pt__runBM_16(Benchmark benchmark) {//####[127]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[127]####
        try {//####[128]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[128]####
        } catch (IllegalAccessException e) {//####[128]####
            e.printStackTrace();//####[128]####
        } catch (IllegalArgumentException e) {//####[128]####
            e.printStackTrace();//####[128]####
        } catch (InvocationTargetException e) {//####[128]####
            e.printStackTrace();//####[128]####
        }//####[128]####
    }//####[128]####
//####[128]####
//####[129]####
    private static volatile Method __pt__runBM_17_Benchmark_method = null;//####[129]####
    private synchronized static void __pt__runBM_17_Benchmark_ensureMethodVarSet() {//####[129]####
        if (__pt__runBM_17_Benchmark_method == null) {//####[129]####
            try {//####[129]####
                __pt__runBM_17_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_17", new Class[] {//####[129]####
                    Benchmark.class//####[129]####
                });//####[129]####
            } catch (Exception e) {//####[129]####
                e.printStackTrace();//####[129]####
            }//####[129]####
        }//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_17(Benchmark benchmark) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return runBM_17(benchmark, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_17(Benchmark benchmark, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__runBM_17_Benchmark_method == null) {//####[129]####
            __pt__runBM_17_Benchmark_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setParameters(benchmark);//####[129]####
        taskinfo.setMethod(__pt__runBM_17_Benchmark_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_17(TaskID<Benchmark> benchmark) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return runBM_17(benchmark, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_17(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__runBM_17_Benchmark_method == null) {//####[129]####
            __pt__runBM_17_Benchmark_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setTaskIdArgIndexes(0);//####[129]####
        taskinfo.addDependsOn(benchmark);//####[129]####
        taskinfo.setParameters(benchmark);//####[129]####
        taskinfo.setMethod(__pt__runBM_17_Benchmark_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_17(BlockingQueue<Benchmark> benchmark) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return runBM_17(benchmark, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_17(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__runBM_17_Benchmark_method == null) {//####[129]####
            __pt__runBM_17_Benchmark_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setQueueArgIndexes(0);//####[129]####
        taskinfo.setIsPipeline(true);//####[129]####
        taskinfo.setParameters(benchmark);//####[129]####
        taskinfo.setMethod(__pt__runBM_17_Benchmark_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    public static void __pt__runBM_17(Benchmark benchmark) {//####[129]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[129]####
        try {//####[130]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[130]####
        } catch (IllegalAccessException e) {//####[130]####
            e.printStackTrace();//####[130]####
        } catch (IllegalArgumentException e) {//####[130]####
            e.printStackTrace();//####[130]####
        } catch (InvocationTargetException e) {//####[130]####
            e.printStackTrace();//####[130]####
        }//####[130]####
    }//####[130]####
//####[130]####
//####[131]####
    private static volatile Method __pt__runBM_18_Benchmark_method = null;//####[131]####
    private synchronized static void __pt__runBM_18_Benchmark_ensureMethodVarSet() {//####[131]####
        if (__pt__runBM_18_Benchmark_method == null) {//####[131]####
            try {//####[131]####
                __pt__runBM_18_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_18", new Class[] {//####[131]####
                    Benchmark.class//####[131]####
                });//####[131]####
            } catch (Exception e) {//####[131]####
                e.printStackTrace();//####[131]####
            }//####[131]####
        }//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_18(Benchmark benchmark) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return runBM_18(benchmark, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_18(Benchmark benchmark, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__runBM_18_Benchmark_method == null) {//####[131]####
            __pt__runBM_18_Benchmark_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setParameters(benchmark);//####[131]####
        taskinfo.setMethod(__pt__runBM_18_Benchmark_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_18(TaskID<Benchmark> benchmark) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return runBM_18(benchmark, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_18(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__runBM_18_Benchmark_method == null) {//####[131]####
            __pt__runBM_18_Benchmark_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(0);//####[131]####
        taskinfo.addDependsOn(benchmark);//####[131]####
        taskinfo.setParameters(benchmark);//####[131]####
        taskinfo.setMethod(__pt__runBM_18_Benchmark_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_18(BlockingQueue<Benchmark> benchmark) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return runBM_18(benchmark, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_18(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__runBM_18_Benchmark_method == null) {//####[131]####
            __pt__runBM_18_Benchmark_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(benchmark);//####[131]####
        taskinfo.setMethod(__pt__runBM_18_Benchmark_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    public static void __pt__runBM_18(Benchmark benchmark) {//####[131]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[131]####
        try {//####[132]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[132]####
        } catch (IllegalAccessException e) {//####[132]####
            e.printStackTrace();//####[132]####
        } catch (IllegalArgumentException e) {//####[132]####
            e.printStackTrace();//####[132]####
        } catch (InvocationTargetException e) {//####[132]####
            e.printStackTrace();//####[132]####
        }//####[132]####
    }//####[132]####
//####[132]####
//####[133]####
    private static volatile Method __pt__runBM_19_Benchmark_method = null;//####[133]####
    private synchronized static void __pt__runBM_19_Benchmark_ensureMethodVarSet() {//####[133]####
        if (__pt__runBM_19_Benchmark_method == null) {//####[133]####
            try {//####[133]####
                __pt__runBM_19_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_19", new Class[] {//####[133]####
                    Benchmark.class//####[133]####
                });//####[133]####
            } catch (Exception e) {//####[133]####
                e.printStackTrace();//####[133]####
            }//####[133]####
        }//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_19(Benchmark benchmark) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return runBM_19(benchmark, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_19(Benchmark benchmark, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__runBM_19_Benchmark_method == null) {//####[133]####
            __pt__runBM_19_Benchmark_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setParameters(benchmark);//####[133]####
        taskinfo.setMethod(__pt__runBM_19_Benchmark_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_19(TaskID<Benchmark> benchmark) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return runBM_19(benchmark, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_19(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__runBM_19_Benchmark_method == null) {//####[133]####
            __pt__runBM_19_Benchmark_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(0);//####[133]####
        taskinfo.addDependsOn(benchmark);//####[133]####
        taskinfo.setParameters(benchmark);//####[133]####
        taskinfo.setMethod(__pt__runBM_19_Benchmark_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_19(BlockingQueue<Benchmark> benchmark) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return runBM_19(benchmark, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_19(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__runBM_19_Benchmark_method == null) {//####[133]####
            __pt__runBM_19_Benchmark_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(benchmark);//####[133]####
        taskinfo.setMethod(__pt__runBM_19_Benchmark_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    public static void __pt__runBM_19(Benchmark benchmark) {//####[133]####
        System.out.println("Oneoff-Task [subtask " + CurrentTask.relativeID() + "]  [thread " + CurrentTask.currentThreadID() + "]  [globalID " + CurrentTask.globalID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "]  [TaskID " + CurrentTask.currentTaskID() + "]  [ISinside? " + CurrentTask.insideTask() + "]  [progress " + CurrentTask.getProgress() + "]");//####[133]####
        try {//####[134]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[134]####
        } catch (IllegalAccessException e) {//####[134]####
            e.printStackTrace();//####[134]####
        } catch (IllegalArgumentException e) {//####[134]####
            e.printStackTrace();//####[134]####
        } catch (InvocationTargetException e) {//####[134]####
            e.printStackTrace();//####[134]####
        }//####[134]####
    }//####[134]####
//####[134]####
//####[136]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[136]####
        Object benchmark = null;//####[137]####
        Method method = null;//####[138]####
        try {//####[139]####
            benchmark = bmClass.newInstance();//####[140]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[141]####
        } catch (InstantiationException e) {//####[142]####
            e.printStackTrace();//####[143]####
        } catch (IllegalAccessException e) {//####[144]####
            e.printStackTrace();//####[145]####
        } catch (NoSuchMethodException e) {//####[146]####
            e.printStackTrace();//####[147]####
        } catch (SecurityException e) {//####[148]####
            e.printStackTrace();//####[149]####
        } catch (IllegalArgumentException e) {//####[150]####
            e.printStackTrace();//####[151]####
        }//####[152]####
        Object[] arguments = new Object[1];//####[153]####
        arguments[0] = N_DATASIZE;//####[154]####
        return new Benchmark(benchmark, method, arguments);//####[156]####
    }//####[157]####
//####[159]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[159]####
        Class<?> bmClass = null;//####[161]####
        try {//####[163]####
            if (bmName.equalsIgnoreCase(MOL)) //####[164]####
            {//####[164]####
                bmClass = Class.forName(MOL_CLASS);//####[165]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[166]####
            {//####[166]####
                bmClass = Class.forName(MON_CLASS);//####[167]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[168]####
            {//####[168]####
                bmClass = Class.forName(RAY_CLASS);//####[169]####
            } else {//####[170]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[171]####
            }//####[172]####
        } catch (ClassNotFoundException e) {//####[173]####
            e.printStackTrace();//####[174]####
        } catch (Exception e) {//####[175]####
            e.printStackTrace();//####[176]####
        }//####[177]####
        return bmClass;//####[179]####
    }//####[180]####
}//####[180]####
