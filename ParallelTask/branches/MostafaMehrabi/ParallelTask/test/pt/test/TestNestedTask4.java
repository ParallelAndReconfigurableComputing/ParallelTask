package pt.test;//####[1]####
//####[1]####
import java.lang.reflect.InvocationTargetException;//####[3]####
import java.lang.reflect.Method;//####[4]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[5]####
import java.util.Random;//####[6]####
import pt.runtime.ParaTask.ThreadPoolType;//####[8]####
//####[8]####
//-- ParaTask related imports//####[8]####
import pt.runtime.*;//####[8]####
import java.util.concurrent.ExecutionException;//####[8]####
import java.util.concurrent.locks.*;//####[8]####
import java.lang.reflect.*;//####[8]####
import pt.runtime.GuiThread;//####[8]####
import java.util.concurrent.BlockingQueue;//####[8]####
import java.util.ArrayList;//####[8]####
import java.util.List;//####[8]####
//####[8]####
public class TestNestedTask4 {//####[10]####
    static{ParaTask.init();}//####[10]####
    /*  ParaTask helper method to access private/protected slots *///####[10]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[10]####
        if (m.getParameterTypes().length == 0)//####[10]####
            m.invoke(instance);//####[10]####
        else if ((m.getParameterTypes().length == 1))//####[10]####
            m.invoke(instance, arg);//####[10]####
        else //####[10]####
            m.invoke(instance, arg, interResult);//####[10]####
    }//####[10]####
//####[11]####
    private static final int N_DATASIZE = 0;//####[11]####
//####[13]####
    private static final String BM_METHOD = "execute";//####[13]####
//####[15]####
    private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = { int.class };//####[15]####
//####[17]####
    private static final String MOL = "MOL";//####[17]####
//####[19]####
    private static final String MOL_CLASS = "core.moldyn.Molcore";//####[19]####
//####[21]####
    private static final String MON = "MON";//####[21]####
//####[23]####
    private static final String MON_CLASS = "core.montecarlo.Moncore";//####[23]####
//####[25]####
    private static final String RAY = "RAY";//####[25]####
//####[27]####
    private static final String RAY_CLASS = "core.raytracer.Raycore";//####[27]####
//####[29]####
    private static ConcurrentLinkedQueue<Benchmark> concurrentLinkedQueue = null;//####[29]####
//####[31]####
    private static int totalNum;//####[31]####
//####[33]####
    private static String benchmarkName;//####[33]####
//####[38]####
    public static void main(String[] args) {//####[38]####
        if (null == args || args.length != 2) //####[39]####
        {//####[39]####
            try {//####[40]####
                throw new Exception("Wrong arguemnts setting");//####[41]####
            } catch (Exception e) {//####[42]####
                e.printStackTrace();//####[43]####
            }//####[44]####
        }//####[45]####
        totalNum = Integer.valueOf(args[0]);//####[46]####
        benchmarkName = args[1];//####[47]####
        TaskID[] taskIDs = new TaskID[totalNum];//####[49]####
        TaskIDGroup tig = new TaskIDGroup(totalNum);//####[50]####
        long startTime = System.currentTimeMillis();//####[52]####
        for (int i = 0; i < totalNum; i++) //####[54]####
        {//####[54]####
            taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(benchmarkName)));//####[55]####
        }//####[56]####
        for (int i = 0; i < totalNum; i++) //####[58]####
        {//####[58]####
            tig.add(taskIDs[i]);//####[59]####
        }//####[60]####
        try {//####[62]####
            Thread.sleep(1000 * 10);//####[63]####
        } catch (InterruptedException e1) {//####[64]####
            e1.printStackTrace();//####[65]####
        }//####[66]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 8);//####[68]####
        try {//####[70]####
            Thread.sleep(1000 * 15);//####[71]####
        } catch (InterruptedException e1) {//####[72]####
            e1.printStackTrace();//####[73]####
        }//####[74]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 4);//####[76]####
        try {//####[78]####
            Thread.sleep(1000);//####[79]####
        } catch (InterruptedException e1) {//####[80]####
            e1.printStackTrace();//####[81]####
        }//####[82]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 6);//####[84]####
        try {//####[86]####
            tig.waitTillFinished();//####[87]####
        } catch (ExecutionException e) {//####[88]####
            e.printStackTrace();//####[89]####
        } catch (InterruptedException e) {//####[90]####
            e.printStackTrace();//####[91]####
        }//####[92]####
        long endTime = System.currentTimeMillis();//####[94]####
    }//####[95]####
//####[98]####
    private static volatile Method __pt__runBM_Benchmark_method = null;//####[98]####
    private synchronized static void __pt__runBM_Benchmark_ensureMethodVarSet() {//####[98]####
        if (__pt__runBM_Benchmark_method == null) {//####[98]####
            try {//####[98]####
                __pt__runBM_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM", new Class[] {//####[98]####
                    Benchmark.class//####[98]####
                });//####[98]####
            } catch (Exception e) {//####[98]####
                e.printStackTrace();//####[98]####
            }//####[98]####
        }//####[98]####
    }//####[98]####
    private static TaskID<Void> runBM(Benchmark benchmark) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return runBM(benchmark, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Void> runBM(Benchmark benchmark, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__runBM_Benchmark_method == null) {//####[98]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setParameters(benchmark);//####[98]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return runBM(benchmark, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__runBM_Benchmark_method == null) {//####[98]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setTaskIdArgIndexes(0);//####[98]####
        taskinfo.addDependsOn(benchmark);//####[98]####
        taskinfo.setParameters(benchmark);//####[98]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return runBM(benchmark, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__runBM_Benchmark_method == null) {//####[98]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setQueueArgIndexes(0);//####[98]####
        taskinfo.setIsPipeline(true);//####[98]####
        taskinfo.setParameters(benchmark);//####[98]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    public static void __pt__runBM(Benchmark benchmark) {//####[98]####
        try {//####[99]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[100]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[101]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[103]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[104]####
            for (int i = 0; i < totalNum; i++) //####[106]####
            {//####[106]####
                taskIDs[i] = runBM_1(createBenchmark(getBenchmarkClass(benchmarkName)));//####[107]####
            }//####[108]####
            for (int i = 0; i < totalNum; i++) //####[110]####
            {//####[110]####
                tig.add(taskIDs[i]);//####[111]####
            }//####[112]####
            try {//####[114]####
                tig.waitTillFinished();//####[115]####
            } catch (ExecutionException e) {//####[116]####
                e.printStackTrace();//####[117]####
            } catch (InterruptedException e) {//####[118]####
                e.printStackTrace();//####[119]####
            }//####[120]####
        } catch (IllegalAccessException e) {//####[123]####
            e.printStackTrace();//####[124]####
        } catch (IllegalArgumentException e) {//####[125]####
            e.printStackTrace();//####[126]####
        } catch (InvocationTargetException e) {//####[127]####
            e.printStackTrace();//####[128]####
        }//####[129]####
    }//####[130]####
//####[130]####
//####[132]####
    private static volatile Method __pt__runBM_1_Benchmark_method = null;//####[132]####
    private synchronized static void __pt__runBM_1_Benchmark_ensureMethodVarSet() {//####[132]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[132]####
            try {//####[132]####
                __pt__runBM_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1", new Class[] {//####[132]####
                    Benchmark.class//####[132]####
                });//####[132]####
            } catch (Exception e) {//####[132]####
                e.printStackTrace();//####[132]####
            }//####[132]####
        }//####[132]####
    }//####[132]####
    private static TaskID<Void> runBM_1(Benchmark benchmark) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return runBM_1(benchmark, new TaskInfo());//####[132]####
    }//####[132]####
    private static TaskID<Void> runBM_1(Benchmark benchmark, TaskInfo taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[132]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setParameters(benchmark);//####[132]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return runBM_1(benchmark, new TaskInfo());//####[132]####
    }//####[132]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[132]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setTaskIdArgIndexes(0);//####[132]####
        taskinfo.addDependsOn(benchmark);//####[132]####
        taskinfo.setParameters(benchmark);//####[132]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return runBM_1(benchmark, new TaskInfo());//####[132]####
    }//####[132]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[132]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setQueueArgIndexes(0);//####[132]####
        taskinfo.setIsPipeline(true);//####[132]####
        taskinfo.setParameters(benchmark);//####[132]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    public static void __pt__runBM_1(Benchmark benchmark) {//####[132]####
        try {//####[133]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[134]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[135]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[137]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[138]####
            for (int i = 0; i < totalNum; i++) //####[140]####
            {//####[140]####
                taskIDs[i] = runBM_2(createBenchmark(getBenchmarkClass(benchmarkName)));//####[141]####
            }//####[142]####
            for (int i = 0; i < totalNum; i++) //####[144]####
            {//####[144]####
                tig.add(taskIDs[i]);//####[145]####
            }//####[146]####
            try {//####[148]####
                tig.waitTillFinished();//####[149]####
            } catch (ExecutionException e) {//####[150]####
                e.printStackTrace();//####[151]####
            } catch (InterruptedException e) {//####[152]####
                e.printStackTrace();//####[153]####
            }//####[154]####
        } catch (IllegalAccessException e) {//####[157]####
            e.printStackTrace();//####[158]####
        } catch (IllegalArgumentException e) {//####[159]####
            e.printStackTrace();//####[160]####
        } catch (InvocationTargetException e) {//####[161]####
            e.printStackTrace();//####[162]####
        }//####[163]####
    }//####[164]####
//####[164]####
//####[166]####
    private static volatile Method __pt__runBM_2_Benchmark_method = null;//####[166]####
    private synchronized static void __pt__runBM_2_Benchmark_ensureMethodVarSet() {//####[166]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[166]####
            try {//####[166]####
                __pt__runBM_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[166]####
                    Benchmark.class//####[166]####
                });//####[166]####
            } catch (Exception e) {//####[166]####
                e.printStackTrace();//####[166]####
            }//####[166]####
        }//####[166]####
    }//####[166]####
    private static TaskID<Void> runBM_2(Benchmark benchmark) {//####[166]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[166]####
        return runBM_2(benchmark, new TaskInfo());//####[166]####
    }//####[166]####
    private static TaskID<Void> runBM_2(Benchmark benchmark, TaskInfo taskinfo) {//####[166]####
        // ensure Method variable is set//####[166]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[166]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[166]####
        }//####[166]####
        taskinfo.setParameters(benchmark);//####[166]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[166]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[166]####
    }//####[166]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark) {//####[166]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[166]####
        return runBM_2(benchmark, new TaskInfo());//####[166]####
    }//####[166]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[166]####
        // ensure Method variable is set//####[166]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[166]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[166]####
        }//####[166]####
        taskinfo.setTaskIdArgIndexes(0);//####[166]####
        taskinfo.addDependsOn(benchmark);//####[166]####
        taskinfo.setParameters(benchmark);//####[166]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[166]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[166]####
    }//####[166]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark) {//####[166]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[166]####
        return runBM_2(benchmark, new TaskInfo());//####[166]####
    }//####[166]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[166]####
        // ensure Method variable is set//####[166]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[166]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[166]####
        }//####[166]####
        taskinfo.setQueueArgIndexes(0);//####[166]####
        taskinfo.setIsPipeline(true);//####[166]####
        taskinfo.setParameters(benchmark);//####[166]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[166]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[166]####
    }//####[166]####
    public static void __pt__runBM_2(Benchmark benchmark) {//####[166]####
        try {//####[167]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[168]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[169]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[171]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[172]####
            for (int i = 0; i < totalNum; i++) //####[174]####
            {//####[174]####
                taskIDs[i] = runBM_4(createBenchmark(getBenchmarkClass(benchmarkName)));//####[175]####
            }//####[176]####
            for (int i = 0; i < totalNum; i++) //####[178]####
            {//####[178]####
                tig.add(taskIDs[i]);//####[179]####
            }//####[180]####
            try {//####[182]####
                tig.waitTillFinished();//####[183]####
            } catch (ExecutionException e) {//####[184]####
                e.printStackTrace();//####[185]####
            } catch (InterruptedException e) {//####[186]####
                e.printStackTrace();//####[187]####
            }//####[188]####
        } catch (IllegalAccessException e) {//####[191]####
            e.printStackTrace();//####[192]####
        } catch (IllegalArgumentException e) {//####[193]####
            e.printStackTrace();//####[194]####
        } catch (InvocationTargetException e) {//####[195]####
            e.printStackTrace();//####[196]####
        }//####[197]####
    }//####[198]####
//####[198]####
//####[200]####
    private static volatile Method __pt__runBM_3_Benchmark_method = null;//####[200]####
    private synchronized static void __pt__runBM_3_Benchmark_ensureMethodVarSet() {//####[200]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[200]####
            try {//####[200]####
                __pt__runBM_3_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_3", new Class[] {//####[200]####
                    Benchmark.class//####[200]####
                });//####[200]####
            } catch (Exception e) {//####[200]####
                e.printStackTrace();//####[200]####
            }//####[200]####
        }//####[200]####
    }//####[200]####
    private static TaskID<Void> runBM_3(Benchmark benchmark) {//####[200]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[200]####
        return runBM_3(benchmark, new TaskInfo());//####[200]####
    }//####[200]####
    private static TaskID<Void> runBM_3(Benchmark benchmark, TaskInfo taskinfo) {//####[200]####
        // ensure Method variable is set//####[200]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[200]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[200]####
        }//####[200]####
        taskinfo.setParameters(benchmark);//####[200]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[200]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[200]####
    }//####[200]####
    private static TaskID<Void> runBM_3(TaskID<Benchmark> benchmark) {//####[200]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[200]####
        return runBM_3(benchmark, new TaskInfo());//####[200]####
    }//####[200]####
    private static TaskID<Void> runBM_3(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[200]####
        // ensure Method variable is set//####[200]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[200]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[200]####
        }//####[200]####
        taskinfo.setTaskIdArgIndexes(0);//####[200]####
        taskinfo.addDependsOn(benchmark);//####[200]####
        taskinfo.setParameters(benchmark);//####[200]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[200]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[200]####
    }//####[200]####
    private static TaskID<Void> runBM_3(BlockingQueue<Benchmark> benchmark) {//####[200]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[200]####
        return runBM_3(benchmark, new TaskInfo());//####[200]####
    }//####[200]####
    private static TaskID<Void> runBM_3(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[200]####
        // ensure Method variable is set//####[200]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[200]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[200]####
        }//####[200]####
        taskinfo.setQueueArgIndexes(0);//####[200]####
        taskinfo.setIsPipeline(true);//####[200]####
        taskinfo.setParameters(benchmark);//####[200]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[200]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[200]####
    }//####[200]####
    public static void __pt__runBM_3(Benchmark benchmark) {//####[200]####
        try {//####[201]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[202]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[203]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[205]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[206]####
            for (int i = 0; i < totalNum; i++) //####[208]####
            {//####[208]####
                taskIDs[i] = runBM_4(createBenchmark(getBenchmarkClass(benchmarkName)));//####[209]####
            }//####[210]####
            for (int i = 0; i < totalNum; i++) //####[212]####
            {//####[212]####
                tig.add(taskIDs[i]);//####[213]####
            }//####[214]####
            try {//####[216]####
                tig.waitTillFinished();//####[217]####
            } catch (ExecutionException e) {//####[218]####
                e.printStackTrace();//####[219]####
            } catch (InterruptedException e) {//####[220]####
                e.printStackTrace();//####[221]####
            }//####[222]####
        } catch (IllegalAccessException e) {//####[225]####
            e.printStackTrace();//####[226]####
        } catch (IllegalArgumentException e) {//####[227]####
            e.printStackTrace();//####[228]####
        } catch (InvocationTargetException e) {//####[229]####
            e.printStackTrace();//####[230]####
        }//####[231]####
    }//####[232]####
//####[232]####
//####[234]####
    private static volatile Method __pt__runBM_4_Benchmark_method = null;//####[234]####
    private synchronized static void __pt__runBM_4_Benchmark_ensureMethodVarSet() {//####[234]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[234]####
            try {//####[234]####
                __pt__runBM_4_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_4", new Class[] {//####[234]####
                    Benchmark.class//####[234]####
                });//####[234]####
            } catch (Exception e) {//####[234]####
                e.printStackTrace();//####[234]####
            }//####[234]####
        }//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_4(Benchmark benchmark) {//####[234]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[234]####
        return runBM_4(benchmark, new TaskInfo());//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_4(Benchmark benchmark, TaskInfo taskinfo) {//####[234]####
        // ensure Method variable is set//####[234]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[234]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[234]####
        }//####[234]####
        taskinfo.setParameters(benchmark);//####[234]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[234]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_4(TaskID<Benchmark> benchmark) {//####[234]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[234]####
        return runBM_4(benchmark, new TaskInfo());//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_4(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[234]####
        // ensure Method variable is set//####[234]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[234]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[234]####
        }//####[234]####
        taskinfo.setTaskIdArgIndexes(0);//####[234]####
        taskinfo.addDependsOn(benchmark);//####[234]####
        taskinfo.setParameters(benchmark);//####[234]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[234]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_4(BlockingQueue<Benchmark> benchmark) {//####[234]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[234]####
        return runBM_4(benchmark, new TaskInfo());//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_4(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[234]####
        // ensure Method variable is set//####[234]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[234]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[234]####
        }//####[234]####
        taskinfo.setQueueArgIndexes(0);//####[234]####
        taskinfo.setIsPipeline(true);//####[234]####
        taskinfo.setParameters(benchmark);//####[234]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[234]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[234]####
    }//####[234]####
    public static void __pt__runBM_4(Benchmark benchmark) {//####[234]####
        try {//####[235]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[236]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[237]####
        } catch (IllegalAccessException e) {//####[238]####
            e.printStackTrace();//####[239]####
        } catch (IllegalArgumentException e) {//####[240]####
            e.printStackTrace();//####[241]####
        } catch (InvocationTargetException e) {//####[242]####
            e.printStackTrace();//####[243]####
        }//####[244]####
    }//####[245]####
//####[245]####
//####[247]####
    private static void runSubTask(Benchmark benchmark) {//####[247]####
        try {//####[248]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[249]####
        } catch (IllegalAccessException e) {//####[250]####
            e.printStackTrace();//####[251]####
        } catch (IllegalArgumentException e) {//####[252]####
            e.printStackTrace();//####[253]####
        } catch (InvocationTargetException e) {//####[254]####
            e.printStackTrace();//####[255]####
        }//####[256]####
    }//####[257]####
//####[259]####
    private static int[] random_serial(int limit) {//####[259]####
        int[] result = new int[limit];//####[260]####
        for (int i = 0; i < limit; i++) //####[261]####
        result[i] = i;//####[262]####
        int w;//####[263]####
        Random rand = new Random();//####[264]####
        for (int i = limit - 1; i > 0; i--) //####[265]####
        {//####[265]####
            w = rand.nextInt(i);//####[266]####
            int t = result[i];//####[267]####
            result[i] = result[w];//####[268]####
            result[w] = t;//####[269]####
        }//####[270]####
        return result;//####[271]####
    }//####[272]####
//####[274]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[274]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[275]####
        for (int i = 0; i < setLen; i++) //####[276]####
        {//####[276]####
            Object benchmark = null;//####[277]####
            Method method = null;//####[278]####
            try {//####[279]####
                benchmark = bmClass.newInstance();//####[280]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[281]####
            } catch (InstantiationException e) {//####[282]####
                e.printStackTrace();//####[283]####
            } catch (IllegalAccessException e) {//####[284]####
                e.printStackTrace();//####[285]####
            } catch (NoSuchMethodException e) {//####[286]####
                e.printStackTrace();//####[287]####
            } catch (SecurityException e) {//####[288]####
                e.printStackTrace();//####[289]####
            } catch (IllegalArgumentException e) {//####[290]####
                e.printStackTrace();//####[291]####
            }//####[292]####
            Object[] arguments = new Object[1];//####[293]####
            arguments[0] = N_DATASIZE;//####[294]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[296]####
        }//####[298]####
        return concurrentLinkedQueue;//####[299]####
    }//####[300]####
//####[302]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[302]####
        Object benchmark = null;//####[303]####
        Method method = null;//####[304]####
        try {//####[305]####
            benchmark = bmClass.newInstance();//####[306]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[307]####
        } catch (InstantiationException e) {//####[308]####
            e.printStackTrace();//####[309]####
        } catch (IllegalAccessException e) {//####[310]####
            e.printStackTrace();//####[311]####
        } catch (NoSuchMethodException e) {//####[312]####
            e.printStackTrace();//####[313]####
        } catch (SecurityException e) {//####[314]####
            e.printStackTrace();//####[315]####
        } catch (IllegalArgumentException e) {//####[316]####
            e.printStackTrace();//####[317]####
        }//####[318]####
        Object[] arguments = new Object[1];//####[319]####
        arguments[0] = N_DATASIZE;//####[320]####
        return new Benchmark(benchmark, method, arguments);//####[322]####
    }//####[323]####
//####[325]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[325]####
        Class<?> bmClass = null;//####[327]####
        try {//####[329]####
            if (bmName.equalsIgnoreCase(MOL)) //####[330]####
            {//####[330]####
                bmClass = Class.forName(MOL_CLASS);//####[331]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[332]####
            {//####[332]####
                bmClass = Class.forName(MON_CLASS);//####[333]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[334]####
            {//####[334]####
                bmClass = Class.forName(RAY_CLASS);//####[335]####
            } else {//####[336]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[337]####
            }//####[338]####
        } catch (ClassNotFoundException e) {//####[339]####
            e.printStackTrace();//####[340]####
        } catch (Exception e) {//####[341]####
            e.printStackTrace();//####[342]####
        }//####[343]####
        return bmClass;//####[345]####
    }//####[346]####
}//####[346]####
