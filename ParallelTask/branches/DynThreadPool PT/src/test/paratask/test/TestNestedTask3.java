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
public class TestNestedTask3 {//####[11]####
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
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 4);//####[69]####
        try {//####[71]####
            Thread.sleep(1000 * 10);//####[72]####
        } catch (InterruptedException e1) {//####[73]####
            e1.printStackTrace();//####[74]####
        }//####[75]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 1);//####[77]####
        try {//####[78]####
            tig.waitTillFinished();//####[79]####
        } catch (ExecutionException e) {//####[80]####
            e.printStackTrace();//####[81]####
        } catch (InterruptedException e) {//####[82]####
            e.printStackTrace();//####[83]####
        }//####[84]####
        long endTime = System.currentTimeMillis();//####[86]####
    }//####[87]####
//####[90]####
    private static volatile Method __pt__runBM_Benchmark_method = null;//####[90]####
    private synchronized static void __pt__runBM_Benchmark_ensureMethodVarSet() {//####[90]####
        if (__pt__runBM_Benchmark_method == null) {//####[90]####
            try {//####[90]####
                __pt__runBM_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM", new Class[] {//####[90]####
                    Benchmark.class//####[90]####
                });//####[90]####
            } catch (Exception e) {//####[90]####
                e.printStackTrace();//####[90]####
            }//####[90]####
        }//####[90]####
    }//####[90]####
    private static TaskID<Void> runBM(Benchmark benchmark) {//####[90]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[90]####
        return runBM(benchmark, new TaskInfo());//####[90]####
    }//####[90]####
    private static TaskID<Void> runBM(Benchmark benchmark, TaskInfo taskinfo) {//####[90]####
        // ensure Method variable is set//####[90]####
        if (__pt__runBM_Benchmark_method == null) {//####[90]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[90]####
        }//####[90]####
        taskinfo.setParameters(benchmark);//####[90]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[90]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[90]####
    }//####[90]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark) {//####[90]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[90]####
        return runBM(benchmark, new TaskInfo());//####[90]####
    }//####[90]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[90]####
        // ensure Method variable is set//####[90]####
        if (__pt__runBM_Benchmark_method == null) {//####[90]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[90]####
        }//####[90]####
        taskinfo.setTaskIdArgIndexes(0);//####[90]####
        taskinfo.addDependsOn(benchmark);//####[90]####
        taskinfo.setParameters(benchmark);//####[90]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[90]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[90]####
    }//####[90]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark) {//####[90]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[90]####
        return runBM(benchmark, new TaskInfo());//####[90]####
    }//####[90]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[90]####
        // ensure Method variable is set//####[90]####
        if (__pt__runBM_Benchmark_method == null) {//####[90]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[90]####
        }//####[90]####
        taskinfo.setQueueArgIndexes(0);//####[90]####
        taskinfo.setIsPipeline(true);//####[90]####
        taskinfo.setParameters(benchmark);//####[90]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[90]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[90]####
    }//####[90]####
    public static void __pt__runBM(Benchmark benchmark) {//####[90]####
        try {//####[91]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[92]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[93]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[95]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[96]####
            for (int i = 0; i < totalNum; i++) //####[98]####
            {//####[98]####
                taskIDs[i] = runBM_1(createBenchmark(getBenchmarkClass(benchmarkName)));//####[99]####
            }//####[100]####
            for (int i = 0; i < totalNum; i++) //####[102]####
            {//####[102]####
                tig.add(taskIDs[i]);//####[103]####
            }//####[104]####
            try {//####[106]####
                tig.waitTillFinished();//####[107]####
            } catch (ExecutionException e) {//####[108]####
                e.printStackTrace();//####[109]####
            } catch (InterruptedException e) {//####[110]####
                e.printStackTrace();//####[111]####
            }//####[112]####
        } catch (IllegalAccessException e) {//####[115]####
            e.printStackTrace();//####[116]####
        } catch (IllegalArgumentException e) {//####[117]####
            e.printStackTrace();//####[118]####
        } catch (InvocationTargetException e) {//####[119]####
            e.printStackTrace();//####[120]####
        }//####[121]####
    }//####[122]####
//####[122]####
//####[124]####
    private static volatile Method __pt__runBM_1_Benchmark_method = null;//####[124]####
    private synchronized static void __pt__runBM_1_Benchmark_ensureMethodVarSet() {//####[124]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[124]####
            try {//####[124]####
                __pt__runBM_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1", new Class[] {//####[124]####
                    Benchmark.class//####[124]####
                });//####[124]####
            } catch (Exception e) {//####[124]####
                e.printStackTrace();//####[124]####
            }//####[124]####
        }//####[124]####
    }//####[124]####
    private static TaskID<Void> runBM_1(Benchmark benchmark) {//####[124]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[124]####
        return runBM_1(benchmark, new TaskInfo());//####[124]####
    }//####[124]####
    private static TaskID<Void> runBM_1(Benchmark benchmark, TaskInfo taskinfo) {//####[124]####
        // ensure Method variable is set//####[124]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[124]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[124]####
        }//####[124]####
        taskinfo.setParameters(benchmark);//####[124]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[124]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[124]####
    }//####[124]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark) {//####[124]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[124]####
        return runBM_1(benchmark, new TaskInfo());//####[124]####
    }//####[124]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[124]####
        // ensure Method variable is set//####[124]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[124]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[124]####
        }//####[124]####
        taskinfo.setTaskIdArgIndexes(0);//####[124]####
        taskinfo.addDependsOn(benchmark);//####[124]####
        taskinfo.setParameters(benchmark);//####[124]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[124]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[124]####
    }//####[124]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark) {//####[124]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[124]####
        return runBM_1(benchmark, new TaskInfo());//####[124]####
    }//####[124]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[124]####
        // ensure Method variable is set//####[124]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[124]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[124]####
        }//####[124]####
        taskinfo.setQueueArgIndexes(0);//####[124]####
        taskinfo.setIsPipeline(true);//####[124]####
        taskinfo.setParameters(benchmark);//####[124]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[124]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[124]####
    }//####[124]####
    public static void __pt__runBM_1(Benchmark benchmark) {//####[124]####
        try {//####[125]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[126]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[127]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[129]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[130]####
            for (int i = 0; i < totalNum; i++) //####[132]####
            {//####[132]####
                taskIDs[i] = runBM_4(createBenchmark(getBenchmarkClass(benchmarkName)));//####[133]####
            }//####[134]####
            for (int i = 0; i < totalNum; i++) //####[136]####
            {//####[136]####
                tig.add(taskIDs[i]);//####[137]####
            }//####[138]####
            try {//####[140]####
                tig.waitTillFinished();//####[141]####
            } catch (ExecutionException e) {//####[142]####
                e.printStackTrace();//####[143]####
            } catch (InterruptedException e) {//####[144]####
                e.printStackTrace();//####[145]####
            }//####[146]####
        } catch (IllegalAccessException e) {//####[149]####
            e.printStackTrace();//####[150]####
        } catch (IllegalArgumentException e) {//####[151]####
            e.printStackTrace();//####[152]####
        } catch (InvocationTargetException e) {//####[153]####
            e.printStackTrace();//####[154]####
        }//####[155]####
    }//####[156]####
//####[156]####
//####[158]####
    private static volatile Method __pt__runBM_2_Benchmark_method = null;//####[158]####
    private synchronized static void __pt__runBM_2_Benchmark_ensureMethodVarSet() {//####[158]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[158]####
            try {//####[158]####
                __pt__runBM_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[158]####
                    Benchmark.class//####[158]####
                });//####[158]####
            } catch (Exception e) {//####[158]####
                e.printStackTrace();//####[158]####
            }//####[158]####
        }//####[158]####
    }//####[158]####
    private static TaskID<Void> runBM_2(Benchmark benchmark) {//####[158]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[158]####
        return runBM_2(benchmark, new TaskInfo());//####[158]####
    }//####[158]####
    private static TaskID<Void> runBM_2(Benchmark benchmark, TaskInfo taskinfo) {//####[158]####
        // ensure Method variable is set//####[158]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[158]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[158]####
        }//####[158]####
        taskinfo.setParameters(benchmark);//####[158]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[158]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[158]####
    }//####[158]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark) {//####[158]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[158]####
        return runBM_2(benchmark, new TaskInfo());//####[158]####
    }//####[158]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[158]####
        // ensure Method variable is set//####[158]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[158]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[158]####
        }//####[158]####
        taskinfo.setTaskIdArgIndexes(0);//####[158]####
        taskinfo.addDependsOn(benchmark);//####[158]####
        taskinfo.setParameters(benchmark);//####[158]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[158]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[158]####
    }//####[158]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark) {//####[158]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[158]####
        return runBM_2(benchmark, new TaskInfo());//####[158]####
    }//####[158]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[158]####
        // ensure Method variable is set//####[158]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[158]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[158]####
        }//####[158]####
        taskinfo.setQueueArgIndexes(0);//####[158]####
        taskinfo.setIsPipeline(true);//####[158]####
        taskinfo.setParameters(benchmark);//####[158]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[158]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[158]####
    }//####[158]####
    public static void __pt__runBM_2(Benchmark benchmark) {//####[158]####
        try {//####[159]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[160]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[161]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[163]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[164]####
            for (int i = 0; i < totalNum; i++) //####[166]####
            {//####[166]####
                taskIDs[i] = runBM_3(createBenchmark(getBenchmarkClass(benchmarkName)));//####[167]####
            }//####[168]####
            for (int i = 0; i < totalNum; i++) //####[170]####
            {//####[170]####
                tig.add(taskIDs[i]);//####[171]####
            }//####[172]####
            try {//####[174]####
                tig.waitTillFinished();//####[175]####
            } catch (ExecutionException e) {//####[176]####
                e.printStackTrace();//####[177]####
            } catch (InterruptedException e) {//####[178]####
                e.printStackTrace();//####[179]####
            }//####[180]####
        } catch (IllegalAccessException e) {//####[183]####
            e.printStackTrace();//####[184]####
        } catch (IllegalArgumentException e) {//####[185]####
            e.printStackTrace();//####[186]####
        } catch (InvocationTargetException e) {//####[187]####
            e.printStackTrace();//####[188]####
        }//####[189]####
    }//####[190]####
//####[190]####
//####[192]####
    private static volatile Method __pt__runBM_3_Benchmark_method = null;//####[192]####
    private synchronized static void __pt__runBM_3_Benchmark_ensureMethodVarSet() {//####[192]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[192]####
            try {//####[192]####
                __pt__runBM_3_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_3", new Class[] {//####[192]####
                    Benchmark.class//####[192]####
                });//####[192]####
            } catch (Exception e) {//####[192]####
                e.printStackTrace();//####[192]####
            }//####[192]####
        }//####[192]####
    }//####[192]####
    private static TaskID<Void> runBM_3(Benchmark benchmark) {//####[192]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[192]####
        return runBM_3(benchmark, new TaskInfo());//####[192]####
    }//####[192]####
    private static TaskID<Void> runBM_3(Benchmark benchmark, TaskInfo taskinfo) {//####[192]####
        // ensure Method variable is set//####[192]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[192]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[192]####
        }//####[192]####
        taskinfo.setParameters(benchmark);//####[192]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[192]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[192]####
    }//####[192]####
    private static TaskID<Void> runBM_3(TaskID<Benchmark> benchmark) {//####[192]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[192]####
        return runBM_3(benchmark, new TaskInfo());//####[192]####
    }//####[192]####
    private static TaskID<Void> runBM_3(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[192]####
        // ensure Method variable is set//####[192]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[192]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[192]####
        }//####[192]####
        taskinfo.setTaskIdArgIndexes(0);//####[192]####
        taskinfo.addDependsOn(benchmark);//####[192]####
        taskinfo.setParameters(benchmark);//####[192]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[192]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[192]####
    }//####[192]####
    private static TaskID<Void> runBM_3(BlockingQueue<Benchmark> benchmark) {//####[192]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[192]####
        return runBM_3(benchmark, new TaskInfo());//####[192]####
    }//####[192]####
    private static TaskID<Void> runBM_3(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[192]####
        // ensure Method variable is set//####[192]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[192]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[192]####
        }//####[192]####
        taskinfo.setQueueArgIndexes(0);//####[192]####
        taskinfo.setIsPipeline(true);//####[192]####
        taskinfo.setParameters(benchmark);//####[192]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[192]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[192]####
    }//####[192]####
    public static void __pt__runBM_3(Benchmark benchmark) {//####[192]####
        try {//####[193]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[194]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[195]####
            TaskIDGroup tig = new TaskIDGroup(totalNum);//####[197]####
            TaskID[] taskIDs = new TaskID[totalNum];//####[198]####
            for (int i = 0; i < totalNum; i++) //####[200]####
            {//####[200]####
                taskIDs[i] = runBM_4(createBenchmark(getBenchmarkClass(benchmarkName)));//####[201]####
            }//####[202]####
            for (int i = 0; i < totalNum; i++) //####[204]####
            {//####[204]####
                tig.add(taskIDs[i]);//####[205]####
            }//####[206]####
            try {//####[208]####
                tig.waitTillFinished();//####[209]####
            } catch (ExecutionException e) {//####[210]####
                e.printStackTrace();//####[211]####
            } catch (InterruptedException e) {//####[212]####
                e.printStackTrace();//####[213]####
            }//####[214]####
        } catch (IllegalAccessException e) {//####[217]####
            e.printStackTrace();//####[218]####
        } catch (IllegalArgumentException e) {//####[219]####
            e.printStackTrace();//####[220]####
        } catch (InvocationTargetException e) {//####[221]####
            e.printStackTrace();//####[222]####
        }//####[223]####
    }//####[224]####
//####[224]####
//####[226]####
    private static volatile Method __pt__runBM_4_Benchmark_method = null;//####[226]####
    private synchronized static void __pt__runBM_4_Benchmark_ensureMethodVarSet() {//####[226]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[226]####
            try {//####[226]####
                __pt__runBM_4_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_4", new Class[] {//####[226]####
                    Benchmark.class//####[226]####
                });//####[226]####
            } catch (Exception e) {//####[226]####
                e.printStackTrace();//####[226]####
            }//####[226]####
        }//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_4(Benchmark benchmark) {//####[226]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[226]####
        return runBM_4(benchmark, new TaskInfo());//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_4(Benchmark benchmark, TaskInfo taskinfo) {//####[226]####
        // ensure Method variable is set//####[226]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[226]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[226]####
        }//####[226]####
        taskinfo.setParameters(benchmark);//####[226]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[226]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_4(TaskID<Benchmark> benchmark) {//####[226]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[226]####
        return runBM_4(benchmark, new TaskInfo());//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_4(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[226]####
        // ensure Method variable is set//####[226]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[226]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[226]####
        }//####[226]####
        taskinfo.setTaskIdArgIndexes(0);//####[226]####
        taskinfo.addDependsOn(benchmark);//####[226]####
        taskinfo.setParameters(benchmark);//####[226]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[226]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_4(BlockingQueue<Benchmark> benchmark) {//####[226]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[226]####
        return runBM_4(benchmark, new TaskInfo());//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_4(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[226]####
        // ensure Method variable is set//####[226]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[226]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[226]####
        }//####[226]####
        taskinfo.setQueueArgIndexes(0);//####[226]####
        taskinfo.setIsPipeline(true);//####[226]####
        taskinfo.setParameters(benchmark);//####[226]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[226]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[226]####
    }//####[226]####
    public static void __pt__runBM_4(Benchmark benchmark) {//####[226]####
        try {//####[227]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[228]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[229]####
        } catch (IllegalAccessException e) {//####[230]####
            e.printStackTrace();//####[231]####
        } catch (IllegalArgumentException e) {//####[232]####
            e.printStackTrace();//####[233]####
        } catch (InvocationTargetException e) {//####[234]####
            e.printStackTrace();//####[235]####
        }//####[236]####
    }//####[237]####
//####[237]####
//####[239]####
    private static void runSubTask(Benchmark benchmark) {//####[239]####
        try {//####[240]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[241]####
        } catch (IllegalAccessException e) {//####[242]####
            e.printStackTrace();//####[243]####
        } catch (IllegalArgumentException e) {//####[244]####
            e.printStackTrace();//####[245]####
        } catch (InvocationTargetException e) {//####[246]####
            e.printStackTrace();//####[247]####
        }//####[248]####
    }//####[249]####
//####[251]####
    private static int[] random_serial(int limit) {//####[251]####
        int[] result = new int[limit];//####[252]####
        for (int i = 0; i < limit; i++) //####[253]####
        result[i] = i;//####[254]####
        int w;//####[255]####
        Random rand = new Random();//####[256]####
        for (int i = limit - 1; i > 0; i--) //####[257]####
        {//####[257]####
            w = rand.nextInt(i);//####[258]####
            int t = result[i];//####[259]####
            result[i] = result[w];//####[260]####
            result[w] = t;//####[261]####
        }//####[262]####
        return result;//####[263]####
    }//####[264]####
//####[266]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[266]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[267]####
        for (int i = 0; i < setLen; i++) //####[268]####
        {//####[268]####
            Object benchmark = null;//####[269]####
            Method method = null;//####[270]####
            try {//####[271]####
                benchmark = bmClass.newInstance();//####[272]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[273]####
            } catch (InstantiationException e) {//####[274]####
                e.printStackTrace();//####[275]####
            } catch (IllegalAccessException e) {//####[276]####
                e.printStackTrace();//####[277]####
            } catch (NoSuchMethodException e) {//####[278]####
                e.printStackTrace();//####[279]####
            } catch (SecurityException e) {//####[280]####
                e.printStackTrace();//####[281]####
            } catch (IllegalArgumentException e) {//####[282]####
                e.printStackTrace();//####[283]####
            }//####[284]####
            Object[] arguments = new Object[1];//####[285]####
            arguments[0] = N_DATASIZE;//####[286]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[288]####
        }//####[290]####
        return concurrentLinkedQueue;//####[291]####
    }//####[292]####
//####[294]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[294]####
        Object benchmark = null;//####[295]####
        Method method = null;//####[296]####
        try {//####[297]####
            benchmark = bmClass.newInstance();//####[298]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[299]####
        } catch (InstantiationException e) {//####[300]####
            e.printStackTrace();//####[301]####
        } catch (IllegalAccessException e) {//####[302]####
            e.printStackTrace();//####[303]####
        } catch (NoSuchMethodException e) {//####[304]####
            e.printStackTrace();//####[305]####
        } catch (SecurityException e) {//####[306]####
            e.printStackTrace();//####[307]####
        } catch (IllegalArgumentException e) {//####[308]####
            e.printStackTrace();//####[309]####
        }//####[310]####
        Object[] arguments = new Object[1];//####[311]####
        arguments[0] = N_DATASIZE;//####[312]####
        return new Benchmark(benchmark, method, arguments);//####[314]####
    }//####[315]####
//####[317]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[317]####
        Class<?> bmClass = null;//####[319]####
        try {//####[321]####
            if (bmName.equalsIgnoreCase(MOL)) //####[322]####
            {//####[322]####
                bmClass = Class.forName(MOL_CLASS);//####[323]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[324]####
            {//####[324]####
                bmClass = Class.forName(MON_CLASS);//####[325]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[326]####
            {//####[326]####
                bmClass = Class.forName(RAY_CLASS);//####[327]####
            } else {//####[328]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[329]####
            }//####[330]####
        } catch (ClassNotFoundException e) {//####[331]####
            e.printStackTrace();//####[332]####
        } catch (Exception e) {//####[333]####
            e.printStackTrace();//####[334]####
        }//####[335]####
        return bmClass;//####[337]####
    }//####[338]####
}//####[338]####
