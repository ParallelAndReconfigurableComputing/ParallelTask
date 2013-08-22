package paratask.test;//####[1]####
//####[1]####
import java.lang.reflect.InvocationTargetException;//####[3]####
import java.lang.reflect.Method;//####[4]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[5]####
import java.util.Random;//####[6]####
import paratask.moldyn.Param;//####[8]####
import paratask.runtime.ParaTask.ThreadPoolType;//####[9]####
//####[9]####
//-- ParaTask related imports//####[9]####
import paratask.runtime.*;//####[9]####
import java.util.concurrent.ExecutionException;//####[9]####
import java.util.concurrent.locks.*;//####[9]####
import java.lang.reflect.*;//####[9]####
import paratask.runtime.GuiThread;//####[9]####
import java.util.concurrent.BlockingQueue;//####[9]####
import java.util.ArrayList;//####[9]####
import java.util.List;//####[9]####
//####[9]####
public class TestNestedTask4 {//####[11]####
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
    private static final int N_DATASIZE = 0;//####[12]####
//####[14]####
    private static final String BM_METHOD = "execute";//####[14]####
//####[16]####
    private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = { int.class };//####[16]####
//####[18]####
    private static final String MOL = "MOL";//####[18]####
//####[20]####
    private static final String MOL_CLASS = "core.moldyn.Molcore";//####[20]####
//####[22]####
    private static final String MON = "MON";//####[22]####
//####[24]####
    private static final String MON_CLASS = "core.montecarlo.Moncore";//####[24]####
//####[26]####
    private static final String RAY = "RAY";//####[26]####
//####[28]####
    private static final String RAY_CLASS = "core.raytracer.Raycore";//####[28]####
//####[30]####
    private static ConcurrentLinkedQueue<Benchmark> concurrentLinkedQueue = null;//####[30]####
//####[32]####
    private static int totalNum;//####[32]####
//####[34]####
    private static String benchmarkName;//####[34]####
//####[39]####
    public static void main(String[] args) {//####[39]####
        if (null == args || args.length != 2) //####[40]####
        {//####[40]####
            try {//####[41]####
                throw new Exception("Wrong arguemnts setting");//####[42]####
            } catch (Exception e) {//####[43]####
                e.printStackTrace();//####[44]####
            }//####[45]####
        }//####[46]####
        totalNum = Integer.valueOf(args[0]);//####[47]####
        benchmarkName = args[1];//####[48]####
        TaskID[] taskIDs = new TaskID[totalNum];//####[50]####
        TaskIDGroup tig = new TaskIDGroup(totalNum);//####[51]####
        long startTime = System.currentTimeMillis();//####[53]####
        for (int i = 0; i < totalNum; i++) //####[55]####
        {//####[55]####
            taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(benchmarkName)));//####[56]####
        }//####[57]####
        for (int i = 0; i < totalNum; i++) //####[59]####
        {//####[59]####
            tig.add(taskIDs[i]);//####[60]####
        }//####[61]####
        try {//####[63]####
            Thread.sleep(1000 * 10);//####[64]####
        } catch (InterruptedException e1) {//####[65]####
            e1.printStackTrace();//####[66]####
        }//####[67]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 8);//####[69]####
        try {//####[71]####
            Thread.sleep(1000 * 15);//####[72]####
        } catch (InterruptedException e1) {//####[73]####
            e1.printStackTrace();//####[74]####
        }//####[75]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 4);//####[77]####
        try {//####[79]####
            Thread.sleep(1000);//####[80]####
        } catch (InterruptedException e1) {//####[81]####
            e1.printStackTrace();//####[82]####
        }//####[83]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 6);//####[85]####
        try {//####[87]####
            tig.waitTillFinished();//####[88]####
        } catch (ExecutionException e) {//####[89]####
            e.printStackTrace();//####[90]####
        } catch (InterruptedException e) {//####[91]####
            e.printStackTrace();//####[92]####
        }//####[93]####
        long endTime = System.currentTimeMillis();//####[95]####
    }//####[96]####
//####[99]####
    private static volatile Method __pt__runBM_Benchmark_method = null;//####[99]####
    private synchronized static void __pt__runBM_Benchmark_ensureMethodVarSet() {//####[99]####
        if (__pt__runBM_Benchmark_method == null) {//####[99]####
            try {//####[99]####
                __pt__runBM_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM", new Class[] {//####[99]####
                    Benchmark.class//####[99]####
                });//####[99]####
            } catch (Exception e) {//####[99]####
                e.printStackTrace();//####[99]####
            }//####[99]####
        }//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM(Benchmark benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM(Benchmark benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_Benchmark_method == null) {//####[99]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_Benchmark_method == null) {//####[99]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setTaskIdArgIndexes(0);//####[99]####
        taskinfo.addDependsOn(benchmark);//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_Benchmark_method == null) {//####[99]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setQueueArgIndexes(0);//####[99]####
        taskinfo.setIsPipeline(true);//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    public static void __pt__runBM(Benchmark benchmark) {//####[99]####
        try {//####[100]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[101]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[102]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[104]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[105]####
            for (int i = 0; i < totalNum; i++) //####[107]####
            {//####[107]####
                taskIDs[i] = runBM_1(createBenchmark(getBenchmarkClass(benchmarkName)));//####[108]####
            }//####[109]####
            for (int i = 0; i < totalNum; i++) //####[111]####
            {//####[111]####
                tig.add(taskIDs[i]);//####[112]####
            }//####[113]####
            try {//####[115]####
                tig.waitTillFinished();//####[116]####
            } catch (ExecutionException e) {//####[117]####
                e.printStackTrace();//####[118]####
            } catch (InterruptedException e) {//####[119]####
                e.printStackTrace();//####[120]####
            }//####[121]####
        } catch (IllegalAccessException e) {//####[124]####
            e.printStackTrace();//####[125]####
        } catch (IllegalArgumentException e) {//####[126]####
            e.printStackTrace();//####[127]####
        } catch (InvocationTargetException e) {//####[128]####
            e.printStackTrace();//####[129]####
        }//####[130]####
    }//####[131]####
//####[131]####
//####[133]####
    private static volatile Method __pt__runBM_1_Benchmark_method = null;//####[133]####
    private synchronized static void __pt__runBM_1_Benchmark_ensureMethodVarSet() {//####[133]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[133]####
            try {//####[133]####
                __pt__runBM_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1", new Class[] {//####[133]####
                    Benchmark.class//####[133]####
                });//####[133]####
            } catch (Exception e) {//####[133]####
                e.printStackTrace();//####[133]####
            }//####[133]####
        }//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_1(Benchmark benchmark) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return runBM_1(benchmark, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_1(Benchmark benchmark, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[133]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setParameters(benchmark);//####[133]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return runBM_1(benchmark, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[133]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(0);//####[133]####
        taskinfo.addDependsOn(benchmark);//####[133]####
        taskinfo.setParameters(benchmark);//####[133]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return runBM_1(benchmark, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[133]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(benchmark);//####[133]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    public static void __pt__runBM_1(Benchmark benchmark) {//####[133]####
        try {//####[134]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[135]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[136]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[138]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[139]####
            for (int i = 0; i < totalNum; i++) //####[141]####
            {//####[141]####
                taskIDs[i] = runBM_2(createBenchmark(getBenchmarkClass(benchmarkName)));//####[142]####
            }//####[143]####
            for (int i = 0; i < totalNum; i++) //####[145]####
            {//####[145]####
                tig.add(taskIDs[i]);//####[146]####
            }//####[147]####
            try {//####[149]####
                tig.waitTillFinished();//####[150]####
            } catch (ExecutionException e) {//####[151]####
                e.printStackTrace();//####[152]####
            } catch (InterruptedException e) {//####[153]####
                e.printStackTrace();//####[154]####
            }//####[155]####
        } catch (IllegalAccessException e) {//####[158]####
            e.printStackTrace();//####[159]####
        } catch (IllegalArgumentException e) {//####[160]####
            e.printStackTrace();//####[161]####
        } catch (InvocationTargetException e) {//####[162]####
            e.printStackTrace();//####[163]####
        }//####[164]####
    }//####[165]####
//####[165]####
//####[167]####
    private static volatile Method __pt__runBM_2_Benchmark_method = null;//####[167]####
    private synchronized static void __pt__runBM_2_Benchmark_ensureMethodVarSet() {//####[167]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[167]####
            try {//####[167]####
                __pt__runBM_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[167]####
                    Benchmark.class//####[167]####
                });//####[167]####
            } catch (Exception e) {//####[167]####
                e.printStackTrace();//####[167]####
            }//####[167]####
        }//####[167]####
    }//####[167]####
    private static TaskID<Void> runBM_2(Benchmark benchmark) {//####[167]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[167]####
        return runBM_2(benchmark, new TaskInfo());//####[167]####
    }//####[167]####
    private static TaskID<Void> runBM_2(Benchmark benchmark, TaskInfo taskinfo) {//####[167]####
        // ensure Method variable is set//####[167]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[167]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[167]####
        }//####[167]####
        taskinfo.setParameters(benchmark);//####[167]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[167]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[167]####
    }//####[167]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark) {//####[167]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[167]####
        return runBM_2(benchmark, new TaskInfo());//####[167]####
    }//####[167]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[167]####
        // ensure Method variable is set//####[167]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[167]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[167]####
        }//####[167]####
        taskinfo.setTaskIdArgIndexes(0);//####[167]####
        taskinfo.addDependsOn(benchmark);//####[167]####
        taskinfo.setParameters(benchmark);//####[167]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[167]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[167]####
    }//####[167]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark) {//####[167]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[167]####
        return runBM_2(benchmark, new TaskInfo());//####[167]####
    }//####[167]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[167]####
        // ensure Method variable is set//####[167]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[167]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[167]####
        }//####[167]####
        taskinfo.setQueueArgIndexes(0);//####[167]####
        taskinfo.setIsPipeline(true);//####[167]####
        taskinfo.setParameters(benchmark);//####[167]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[167]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[167]####
    }//####[167]####
    public static void __pt__runBM_2(Benchmark benchmark) {//####[167]####
        try {//####[168]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[169]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[170]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[172]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[173]####
            for (int i = 0; i < totalNum; i++) //####[175]####
            {//####[175]####
                taskIDs[i] = runBM_4(createBenchmark(getBenchmarkClass(benchmarkName)));//####[176]####
            }//####[177]####
            for (int i = 0; i < totalNum; i++) //####[179]####
            {//####[179]####
                tig.add(taskIDs[i]);//####[180]####
            }//####[181]####
            try {//####[183]####
                tig.waitTillFinished();//####[184]####
            } catch (ExecutionException e) {//####[185]####
                e.printStackTrace();//####[186]####
            } catch (InterruptedException e) {//####[187]####
                e.printStackTrace();//####[188]####
            }//####[189]####
        } catch (IllegalAccessException e) {//####[192]####
            e.printStackTrace();//####[193]####
        } catch (IllegalArgumentException e) {//####[194]####
            e.printStackTrace();//####[195]####
        } catch (InvocationTargetException e) {//####[196]####
            e.printStackTrace();//####[197]####
        }//####[198]####
    }//####[199]####
//####[199]####
//####[201]####
    private static volatile Method __pt__runBM_3_Benchmark_method = null;//####[201]####
    private synchronized static void __pt__runBM_3_Benchmark_ensureMethodVarSet() {//####[201]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[201]####
            try {//####[201]####
                __pt__runBM_3_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_3", new Class[] {//####[201]####
                    Benchmark.class//####[201]####
                });//####[201]####
            } catch (Exception e) {//####[201]####
                e.printStackTrace();//####[201]####
            }//####[201]####
        }//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_3(Benchmark benchmark) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBM_3(benchmark, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_3(Benchmark benchmark, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[201]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setParameters(benchmark);//####[201]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_3(TaskID<Benchmark> benchmark) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBM_3(benchmark, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_3(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[201]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setTaskIdArgIndexes(0);//####[201]####
        taskinfo.addDependsOn(benchmark);//####[201]####
        taskinfo.setParameters(benchmark);//####[201]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_3(BlockingQueue<Benchmark> benchmark) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBM_3(benchmark, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_3(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[201]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setQueueArgIndexes(0);//####[201]####
        taskinfo.setIsPipeline(true);//####[201]####
        taskinfo.setParameters(benchmark);//####[201]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[201]####
    }//####[201]####
    public static void __pt__runBM_3(Benchmark benchmark) {//####[201]####
        try {//####[202]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[203]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[204]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[206]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[207]####
            for (int i = 0; i < totalNum; i++) //####[209]####
            {//####[209]####
                taskIDs[i] = runBM_4(createBenchmark(getBenchmarkClass(benchmarkName)));//####[210]####
            }//####[211]####
            for (int i = 0; i < totalNum; i++) //####[213]####
            {//####[213]####
                tig.add(taskIDs[i]);//####[214]####
            }//####[215]####
            try {//####[217]####
                tig.waitTillFinished();//####[218]####
            } catch (ExecutionException e) {//####[219]####
                e.printStackTrace();//####[220]####
            } catch (InterruptedException e) {//####[221]####
                e.printStackTrace();//####[222]####
            }//####[223]####
        } catch (IllegalAccessException e) {//####[226]####
            e.printStackTrace();//####[227]####
        } catch (IllegalArgumentException e) {//####[228]####
            e.printStackTrace();//####[229]####
        } catch (InvocationTargetException e) {//####[230]####
            e.printStackTrace();//####[231]####
        }//####[232]####
    }//####[233]####
//####[233]####
//####[235]####
    private static volatile Method __pt__runBM_4_Benchmark_method = null;//####[235]####
    private synchronized static void __pt__runBM_4_Benchmark_ensureMethodVarSet() {//####[235]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[235]####
            try {//####[235]####
                __pt__runBM_4_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_4", new Class[] {//####[235]####
                    Benchmark.class//####[235]####
                });//####[235]####
            } catch (Exception e) {//####[235]####
                e.printStackTrace();//####[235]####
            }//####[235]####
        }//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_4(Benchmark benchmark) {//####[235]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[235]####
        return runBM_4(benchmark, new TaskInfo());//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_4(Benchmark benchmark, TaskInfo taskinfo) {//####[235]####
        // ensure Method variable is set//####[235]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[235]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[235]####
        }//####[235]####
        taskinfo.setParameters(benchmark);//####[235]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[235]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_4(TaskID<Benchmark> benchmark) {//####[235]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[235]####
        return runBM_4(benchmark, new TaskInfo());//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_4(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[235]####
        // ensure Method variable is set//####[235]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[235]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[235]####
        }//####[235]####
        taskinfo.setTaskIdArgIndexes(0);//####[235]####
        taskinfo.addDependsOn(benchmark);//####[235]####
        taskinfo.setParameters(benchmark);//####[235]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[235]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_4(BlockingQueue<Benchmark> benchmark) {//####[235]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[235]####
        return runBM_4(benchmark, new TaskInfo());//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_4(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[235]####
        // ensure Method variable is set//####[235]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[235]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[235]####
        }//####[235]####
        taskinfo.setQueueArgIndexes(0);//####[235]####
        taskinfo.setIsPipeline(true);//####[235]####
        taskinfo.setParameters(benchmark);//####[235]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[235]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[235]####
    }//####[235]####
    public static void __pt__runBM_4(Benchmark benchmark) {//####[235]####
        try {//####[236]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[237]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[238]####
        } catch (IllegalAccessException e) {//####[239]####
            e.printStackTrace();//####[240]####
        } catch (IllegalArgumentException e) {//####[241]####
            e.printStackTrace();//####[242]####
        } catch (InvocationTargetException e) {//####[243]####
            e.printStackTrace();//####[244]####
        }//####[245]####
    }//####[246]####
//####[246]####
//####[248]####
    private static void runSubTask(Benchmark benchmark) {//####[248]####
        try {//####[249]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[250]####
        } catch (IllegalAccessException e) {//####[251]####
            e.printStackTrace();//####[252]####
        } catch (IllegalArgumentException e) {//####[253]####
            e.printStackTrace();//####[254]####
        } catch (InvocationTargetException e) {//####[255]####
            e.printStackTrace();//####[256]####
        }//####[257]####
    }//####[258]####
//####[260]####
    private static int[] random_serial(int limit) {//####[260]####
        int[] result = new int[limit];//####[261]####
        for (int i = 0; i < limit; i++) //####[262]####
        result[i] = i;//####[263]####
        int w;//####[264]####
        Random rand = new Random();//####[265]####
        for (int i = limit - 1; i > 0; i--) //####[266]####
        {//####[266]####
            w = rand.nextInt(i);//####[267]####
            int t = result[i];//####[268]####
            result[i] = result[w];//####[269]####
            result[w] = t;//####[270]####
        }//####[271]####
        return result;//####[272]####
    }//####[273]####
//####[275]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[275]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[276]####
        for (int i = 0; i < setLen; i++) //####[277]####
        {//####[277]####
            Object benchmark = null;//####[278]####
            Method method = null;//####[279]####
            try {//####[280]####
                benchmark = bmClass.newInstance();//####[281]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[282]####
            } catch (InstantiationException e) {//####[283]####
                e.printStackTrace();//####[284]####
            } catch (IllegalAccessException e) {//####[285]####
                e.printStackTrace();//####[286]####
            } catch (NoSuchMethodException e) {//####[287]####
                e.printStackTrace();//####[288]####
            } catch (SecurityException e) {//####[289]####
                e.printStackTrace();//####[290]####
            } catch (IllegalArgumentException e) {//####[291]####
                e.printStackTrace();//####[292]####
            }//####[293]####
            Object[] arguments = new Object[1];//####[294]####
            arguments[0] = N_DATASIZE;//####[295]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[297]####
        }//####[299]####
        return concurrentLinkedQueue;//####[300]####
    }//####[301]####
//####[303]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[303]####
        Object benchmark = null;//####[304]####
        Method method = null;//####[305]####
        try {//####[306]####
            benchmark = bmClass.newInstance();//####[307]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[308]####
        } catch (InstantiationException e) {//####[309]####
            e.printStackTrace();//####[310]####
        } catch (IllegalAccessException e) {//####[311]####
            e.printStackTrace();//####[312]####
        } catch (NoSuchMethodException e) {//####[313]####
            e.printStackTrace();//####[314]####
        } catch (SecurityException e) {//####[315]####
            e.printStackTrace();//####[316]####
        } catch (IllegalArgumentException e) {//####[317]####
            e.printStackTrace();//####[318]####
        }//####[319]####
        Object[] arguments = new Object[1];//####[320]####
        arguments[0] = N_DATASIZE;//####[321]####
        return new Benchmark(benchmark, method, arguments);//####[323]####
    }//####[324]####
//####[326]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[326]####
        Class<?> bmClass = null;//####[328]####
        try {//####[330]####
            if (bmName.equalsIgnoreCase(MOL)) //####[331]####
            {//####[331]####
                bmClass = Class.forName(MOL_CLASS);//####[332]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[333]####
            {//####[333]####
                bmClass = Class.forName(MON_CLASS);//####[334]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[335]####
            {//####[335]####
                bmClass = Class.forName(RAY_CLASS);//####[336]####
            } else {//####[337]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[338]####
            }//####[339]####
        } catch (ClassNotFoundException e) {//####[340]####
            e.printStackTrace();//####[341]####
        } catch (Exception e) {//####[342]####
            e.printStackTrace();//####[343]####
        }//####[344]####
        return bmClass;//####[346]####
    }//####[347]####
}//####[347]####
