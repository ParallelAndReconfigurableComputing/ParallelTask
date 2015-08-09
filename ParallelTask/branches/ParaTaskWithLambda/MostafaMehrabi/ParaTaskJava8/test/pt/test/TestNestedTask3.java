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
public class TestNestedTask3 {//####[10]####
    static{ParaTask.init();}//####[10]####
    /*  ParaTask helper method to access private/protected slots *///####[10]####
    public void __pt__accessPrivateSlot(Method m, Object instance, Future arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[10]####
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
        Future[] taskIDs = new Future[totalNum];//####[49]####
        FutureGroup tig = new FutureGroup(totalNum);//####[50]####
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
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 4);//####[68]####
        try {//####[70]####
            Thread.sleep(1000 * 10);//####[71]####
        } catch (InterruptedException e1) {//####[72]####
            e1.printStackTrace();//####[73]####
        }//####[74]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 1);//####[76]####
        try {//####[77]####
            tig.waitTillFinished();//####[78]####
        } catch (ExecutionException e) {//####[79]####
            e.printStackTrace();//####[80]####
        } catch (InterruptedException e) {//####[81]####
            e.printStackTrace();//####[82]####
        }//####[83]####
        long endTime = System.currentTimeMillis();//####[85]####
    }//####[86]####
//####[89]####
    private static volatile Method __pt__runBM_Benchmark_method = null;//####[89]####
    private synchronized static void __pt__runBM_Benchmark_ensureMethodVarSet() {//####[89]####
        if (__pt__runBM_Benchmark_method == null) {//####[89]####
            try {//####[89]####
                __pt__runBM_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM", new Class[] {//####[89]####
                    Benchmark.class//####[89]####
                });//####[89]####
            } catch (Exception e) {//####[89]####
                e.printStackTrace();//####[89]####
            }//####[89]####
        }//####[89]####
    }//####[89]####
    private static Future<Void> runBM(Benchmark benchmark) {//####[89]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[89]####
        return runBM(benchmark, new Task());//####[89]####
    }//####[89]####
    private static Future<Void> runBM(Benchmark benchmark, Task taskinfo) {//####[89]####
        // ensure Method variable is set//####[89]####
        if (__pt__runBM_Benchmark_method == null) {//####[89]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[89]####
        }//####[89]####
        taskinfo.setParameters(benchmark);//####[89]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[89]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[89]####
    }//####[89]####
    private static Future<Void> runBM(Future<Benchmark> benchmark) {//####[89]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[89]####
        return runBM(benchmark, new Task());//####[89]####
    }//####[89]####
    private static Future<Void> runBM(Future<Benchmark> benchmark, Task taskinfo) {//####[89]####
        // ensure Method variable is set//####[89]####
        if (__pt__runBM_Benchmark_method == null) {//####[89]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[89]####
        }//####[89]####
        taskinfo.setTaskIdArgIndexes(0);//####[89]####
        taskinfo.addDependsOn(benchmark);//####[89]####
        taskinfo.setParameters(benchmark);//####[89]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[89]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[89]####
    }//####[89]####
    private static Future<Void> runBM(BlockingQueue<Benchmark> benchmark) {//####[89]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[89]####
        return runBM(benchmark, new Task());//####[89]####
    }//####[89]####
    private static Future<Void> runBM(BlockingQueue<Benchmark> benchmark, Task taskinfo) {//####[89]####
        // ensure Method variable is set//####[89]####
        if (__pt__runBM_Benchmark_method == null) {//####[89]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[89]####
        }//####[89]####
        taskinfo.setQueueArgIndexes(0);//####[89]####
        taskinfo.setIsPipeline(true);//####[89]####
        taskinfo.setParameters(benchmark);//####[89]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[89]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[89]####
    }//####[89]####
    public static void __pt__runBM(Benchmark benchmark) {//####[89]####
        try {//####[90]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[91]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[92]####
            FutureGroup tig = new FutureGroup(totalNum);//####[94]####
            Future[] taskIDs = new Future[totalNum];//####[95]####
            for (int i = 0; i < totalNum; i++) //####[97]####
            {//####[97]####
                taskIDs[i] = runBM_1(createBenchmark(getBenchmarkClass(benchmarkName)));//####[98]####
            }//####[99]####
            for (int i = 0; i < totalNum; i++) //####[101]####
            {//####[101]####
                tig.add(taskIDs[i]);//####[102]####
            }//####[103]####
            try {//####[105]####
                tig.waitTillFinished();//####[106]####
            } catch (ExecutionException e) {//####[107]####
                e.printStackTrace();//####[108]####
            } catch (InterruptedException e) {//####[109]####
                e.printStackTrace();//####[110]####
            }//####[111]####
        } catch (IllegalAccessException e) {//####[114]####
            e.printStackTrace();//####[115]####
        } catch (IllegalArgumentException e) {//####[116]####
            e.printStackTrace();//####[117]####
        } catch (InvocationTargetException e) {//####[118]####
            e.printStackTrace();//####[119]####
        }//####[120]####
    }//####[121]####
//####[121]####
//####[123]####
    private static volatile Method __pt__runBM_1_Benchmark_method = null;//####[123]####
    private synchronized static void __pt__runBM_1_Benchmark_ensureMethodVarSet() {//####[123]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[123]####
            try {//####[123]####
                __pt__runBM_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1", new Class[] {//####[123]####
                    Benchmark.class//####[123]####
                });//####[123]####
            } catch (Exception e) {//####[123]####
                e.printStackTrace();//####[123]####
            }//####[123]####
        }//####[123]####
    }//####[123]####
    private static Future<Void> runBM_1(Benchmark benchmark) {//####[123]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[123]####
        return runBM_1(benchmark, new Task());//####[123]####
    }//####[123]####
    private static Future<Void> runBM_1(Benchmark benchmark, Task taskinfo) {//####[123]####
        // ensure Method variable is set//####[123]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[123]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[123]####
        }//####[123]####
        taskinfo.setParameters(benchmark);//####[123]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[123]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[123]####
    }//####[123]####
    private static Future<Void> runBM_1(Future<Benchmark> benchmark) {//####[123]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[123]####
        return runBM_1(benchmark, new Task());//####[123]####
    }//####[123]####
    private static Future<Void> runBM_1(Future<Benchmark> benchmark, Task taskinfo) {//####[123]####
        // ensure Method variable is set//####[123]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[123]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[123]####
        }//####[123]####
        taskinfo.setTaskIdArgIndexes(0);//####[123]####
        taskinfo.addDependsOn(benchmark);//####[123]####
        taskinfo.setParameters(benchmark);//####[123]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[123]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[123]####
    }//####[123]####
    private static Future<Void> runBM_1(BlockingQueue<Benchmark> benchmark) {//####[123]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[123]####
        return runBM_1(benchmark, new Task());//####[123]####
    }//####[123]####
    private static Future<Void> runBM_1(BlockingQueue<Benchmark> benchmark, Task taskinfo) {//####[123]####
        // ensure Method variable is set//####[123]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[123]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[123]####
        }//####[123]####
        taskinfo.setQueueArgIndexes(0);//####[123]####
        taskinfo.setIsPipeline(true);//####[123]####
        taskinfo.setParameters(benchmark);//####[123]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[123]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[123]####
    }//####[123]####
    public static void __pt__runBM_1(Benchmark benchmark) {//####[123]####
        try {//####[124]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[125]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[126]####
            FutureGroup tig = new FutureGroup(totalNum);//####[128]####
            Future[] taskIDs = new Future[totalNum];//####[129]####
            for (int i = 0; i < totalNum; i++) //####[131]####
            {//####[131]####
                taskIDs[i] = runBM_4(createBenchmark(getBenchmarkClass(benchmarkName)));//####[132]####
            }//####[133]####
            for (int i = 0; i < totalNum; i++) //####[135]####
            {//####[135]####
                tig.add(taskIDs[i]);//####[136]####
            }//####[137]####
            try {//####[139]####
                tig.waitTillFinished();//####[140]####
            } catch (ExecutionException e) {//####[141]####
                e.printStackTrace();//####[142]####
            } catch (InterruptedException e) {//####[143]####
                e.printStackTrace();//####[144]####
            }//####[145]####
        } catch (IllegalAccessException e) {//####[148]####
            e.printStackTrace();//####[149]####
        } catch (IllegalArgumentException e) {//####[150]####
            e.printStackTrace();//####[151]####
        } catch (InvocationTargetException e) {//####[152]####
            e.printStackTrace();//####[153]####
        }//####[154]####
    }//####[155]####
//####[155]####
//####[157]####
    private static volatile Method __pt__runBM_2_Benchmark_method = null;//####[157]####
    private synchronized static void __pt__runBM_2_Benchmark_ensureMethodVarSet() {//####[157]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[157]####
            try {//####[157]####
                __pt__runBM_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[157]####
                    Benchmark.class//####[157]####
                });//####[157]####
            } catch (Exception e) {//####[157]####
                e.printStackTrace();//####[157]####
            }//####[157]####
        }//####[157]####
    }//####[157]####
    private static Future<Void> runBM_2(Benchmark benchmark) {//####[157]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[157]####
        return runBM_2(benchmark, new Task());//####[157]####
    }//####[157]####
    private static Future<Void> runBM_2(Benchmark benchmark, Task taskinfo) {//####[157]####
        // ensure Method variable is set//####[157]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[157]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[157]####
        }//####[157]####
        taskinfo.setParameters(benchmark);//####[157]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[157]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[157]####
    }//####[157]####
    private static Future<Void> runBM_2(Future<Benchmark> benchmark) {//####[157]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[157]####
        return runBM_2(benchmark, new Task());//####[157]####
    }//####[157]####
    private static Future<Void> runBM_2(Future<Benchmark> benchmark, Task taskinfo) {//####[157]####
        // ensure Method variable is set//####[157]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[157]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[157]####
        }//####[157]####
        taskinfo.setTaskIdArgIndexes(0);//####[157]####
        taskinfo.addDependsOn(benchmark);//####[157]####
        taskinfo.setParameters(benchmark);//####[157]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[157]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[157]####
    }//####[157]####
    private static Future<Void> runBM_2(BlockingQueue<Benchmark> benchmark) {//####[157]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[157]####
        return runBM_2(benchmark, new Task());//####[157]####
    }//####[157]####
    private static Future<Void> runBM_2(BlockingQueue<Benchmark> benchmark, Task taskinfo) {//####[157]####
        // ensure Method variable is set//####[157]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[157]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[157]####
        }//####[157]####
        taskinfo.setQueueArgIndexes(0);//####[157]####
        taskinfo.setIsPipeline(true);//####[157]####
        taskinfo.setParameters(benchmark);//####[157]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[157]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[157]####
    }//####[157]####
    public static void __pt__runBM_2(Benchmark benchmark) {//####[157]####
        try {//####[158]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[159]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[160]####
            FutureGroup tig = new FutureGroup(totalNum);//####[162]####
            Future[] taskIDs = new Future[totalNum];//####[163]####
            for (int i = 0; i < totalNum; i++) //####[165]####
            {//####[165]####
                taskIDs[i] = runBM_3(createBenchmark(getBenchmarkClass(benchmarkName)));//####[166]####
            }//####[167]####
            for (int i = 0; i < totalNum; i++) //####[169]####
            {//####[169]####
                tig.add(taskIDs[i]);//####[170]####
            }//####[171]####
            try {//####[173]####
                tig.waitTillFinished();//####[174]####
            } catch (ExecutionException e) {//####[175]####
                e.printStackTrace();//####[176]####
            } catch (InterruptedException e) {//####[177]####
                e.printStackTrace();//####[178]####
            }//####[179]####
        } catch (IllegalAccessException e) {//####[182]####
            e.printStackTrace();//####[183]####
        } catch (IllegalArgumentException e) {//####[184]####
            e.printStackTrace();//####[185]####
        } catch (InvocationTargetException e) {//####[186]####
            e.printStackTrace();//####[187]####
        }//####[188]####
    }//####[189]####
//####[189]####
//####[191]####
    private static volatile Method __pt__runBM_3_Benchmark_method = null;//####[191]####
    private synchronized static void __pt__runBM_3_Benchmark_ensureMethodVarSet() {//####[191]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[191]####
            try {//####[191]####
                __pt__runBM_3_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_3", new Class[] {//####[191]####
                    Benchmark.class//####[191]####
                });//####[191]####
            } catch (Exception e) {//####[191]####
                e.printStackTrace();//####[191]####
            }//####[191]####
        }//####[191]####
    }//####[191]####
    private static Future<Void> runBM_3(Benchmark benchmark) {//####[191]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[191]####
        return runBM_3(benchmark, new Task());//####[191]####
    }//####[191]####
    private static Future<Void> runBM_3(Benchmark benchmark, Task taskinfo) {//####[191]####
        // ensure Method variable is set//####[191]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[191]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[191]####
        }//####[191]####
        taskinfo.setParameters(benchmark);//####[191]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[191]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[191]####
    }//####[191]####
    private static Future<Void> runBM_3(Future<Benchmark> benchmark) {//####[191]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[191]####
        return runBM_3(benchmark, new Task());//####[191]####
    }//####[191]####
    private static Future<Void> runBM_3(Future<Benchmark> benchmark, Task taskinfo) {//####[191]####
        // ensure Method variable is set//####[191]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[191]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[191]####
        }//####[191]####
        taskinfo.setTaskIdArgIndexes(0);//####[191]####
        taskinfo.addDependsOn(benchmark);//####[191]####
        taskinfo.setParameters(benchmark);//####[191]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[191]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[191]####
    }//####[191]####
    private static Future<Void> runBM_3(BlockingQueue<Benchmark> benchmark) {//####[191]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[191]####
        return runBM_3(benchmark, new Task());//####[191]####
    }//####[191]####
    private static Future<Void> runBM_3(BlockingQueue<Benchmark> benchmark, Task taskinfo) {//####[191]####
        // ensure Method variable is set//####[191]####
        if (__pt__runBM_3_Benchmark_method == null) {//####[191]####
            __pt__runBM_3_Benchmark_ensureMethodVarSet();//####[191]####
        }//####[191]####
        taskinfo.setQueueArgIndexes(0);//####[191]####
        taskinfo.setIsPipeline(true);//####[191]####
        taskinfo.setParameters(benchmark);//####[191]####
        taskinfo.setMethod(__pt__runBM_3_Benchmark_method);//####[191]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[191]####
    }//####[191]####
    public static void __pt__runBM_3(Benchmark benchmark) {//####[191]####
        try {//####[192]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[193]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[194]####
            FutureGroup tig = new FutureGroup(totalNum);//####[196]####
            Future[] taskIDs = new Future[totalNum];//####[197]####
            for (int i = 0; i < totalNum; i++) //####[199]####
            {//####[199]####
                taskIDs[i] = runBM_4(createBenchmark(getBenchmarkClass(benchmarkName)));//####[200]####
            }//####[201]####
            for (int i = 0; i < totalNum; i++) //####[203]####
            {//####[203]####
                tig.add(taskIDs[i]);//####[204]####
            }//####[205]####
            try {//####[207]####
                tig.waitTillFinished();//####[208]####
            } catch (ExecutionException e) {//####[209]####
                e.printStackTrace();//####[210]####
            } catch (InterruptedException e) {//####[211]####
                e.printStackTrace();//####[212]####
            }//####[213]####
        } catch (IllegalAccessException e) {//####[216]####
            e.printStackTrace();//####[217]####
        } catch (IllegalArgumentException e) {//####[218]####
            e.printStackTrace();//####[219]####
        } catch (InvocationTargetException e) {//####[220]####
            e.printStackTrace();//####[221]####
        }//####[222]####
    }//####[223]####
//####[223]####
//####[225]####
    private static volatile Method __pt__runBM_4_Benchmark_method = null;//####[225]####
    private synchronized static void __pt__runBM_4_Benchmark_ensureMethodVarSet() {//####[225]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[225]####
            try {//####[225]####
                __pt__runBM_4_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_4", new Class[] {//####[225]####
                    Benchmark.class//####[225]####
                });//####[225]####
            } catch (Exception e) {//####[225]####
                e.printStackTrace();//####[225]####
            }//####[225]####
        }//####[225]####
    }//####[225]####
    private static Future<Void> runBM_4(Benchmark benchmark) {//####[225]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[225]####
        return runBM_4(benchmark, new Task());//####[225]####
    }//####[225]####
    private static Future<Void> runBM_4(Benchmark benchmark, Task taskinfo) {//####[225]####
        // ensure Method variable is set//####[225]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[225]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[225]####
        }//####[225]####
        taskinfo.setParameters(benchmark);//####[225]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[225]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[225]####
    }//####[225]####
    private static Future<Void> runBM_4(Future<Benchmark> benchmark) {//####[225]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[225]####
        return runBM_4(benchmark, new Task());//####[225]####
    }//####[225]####
    private static Future<Void> runBM_4(Future<Benchmark> benchmark, Task taskinfo) {//####[225]####
        // ensure Method variable is set//####[225]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[225]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[225]####
        }//####[225]####
        taskinfo.setTaskIdArgIndexes(0);//####[225]####
        taskinfo.addDependsOn(benchmark);//####[225]####
        taskinfo.setParameters(benchmark);//####[225]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[225]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[225]####
    }//####[225]####
    private static Future<Void> runBM_4(BlockingQueue<Benchmark> benchmark) {//####[225]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[225]####
        return runBM_4(benchmark, new Task());//####[225]####
    }//####[225]####
    private static Future<Void> runBM_4(BlockingQueue<Benchmark> benchmark, Task taskinfo) {//####[225]####
        // ensure Method variable is set//####[225]####
        if (__pt__runBM_4_Benchmark_method == null) {//####[225]####
            __pt__runBM_4_Benchmark_ensureMethodVarSet();//####[225]####
        }//####[225]####
        taskinfo.setQueueArgIndexes(0);//####[225]####
        taskinfo.setIsPipeline(true);//####[225]####
        taskinfo.setParameters(benchmark);//####[225]####
        taskinfo.setMethod(__pt__runBM_4_Benchmark_method);//####[225]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[225]####
    }//####[225]####
    public static void __pt__runBM_4(Benchmark benchmark) {//####[225]####
        try {//####[226]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[227]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[228]####
        } catch (IllegalAccessException e) {//####[229]####
            e.printStackTrace();//####[230]####
        } catch (IllegalArgumentException e) {//####[231]####
            e.printStackTrace();//####[232]####
        } catch (InvocationTargetException e) {//####[233]####
            e.printStackTrace();//####[234]####
        }//####[235]####
    }//####[236]####
//####[236]####
//####[238]####
    private static void runSubTask(Benchmark benchmark) {//####[238]####
        try {//####[239]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[240]####
        } catch (IllegalAccessException e) {//####[241]####
            e.printStackTrace();//####[242]####
        } catch (IllegalArgumentException e) {//####[243]####
            e.printStackTrace();//####[244]####
        } catch (InvocationTargetException e) {//####[245]####
            e.printStackTrace();//####[246]####
        }//####[247]####
    }//####[248]####
//####[250]####
    private static int[] random_serial(int limit) {//####[250]####
        int[] result = new int[limit];//####[251]####
        for (int i = 0; i < limit; i++) //####[252]####
        result[i] = i;//####[253]####
        int w;//####[254]####
        Random rand = new Random();//####[255]####
        for (int i = limit - 1; i > 0; i--) //####[256]####
        {//####[256]####
            w = rand.nextInt(i);//####[257]####
            int t = result[i];//####[258]####
            result[i] = result[w];//####[259]####
            result[w] = t;//####[260]####
        }//####[261]####
        return result;//####[262]####
    }//####[263]####
//####[265]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[265]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[266]####
        for (int i = 0; i < setLen; i++) //####[267]####
        {//####[267]####
            Object benchmark = null;//####[268]####
            Method method = null;//####[269]####
            try {//####[270]####
                benchmark = bmClass.newInstance();//####[271]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[272]####
            } catch (InstantiationException e) {//####[273]####
                e.printStackTrace();//####[274]####
            } catch (IllegalAccessException e) {//####[275]####
                e.printStackTrace();//####[276]####
            } catch (NoSuchMethodException e) {//####[277]####
                e.printStackTrace();//####[278]####
            } catch (SecurityException e) {//####[279]####
                e.printStackTrace();//####[280]####
            } catch (IllegalArgumentException e) {//####[281]####
                e.printStackTrace();//####[282]####
            }//####[283]####
            Object[] arguments = new Object[1];//####[284]####
            arguments[0] = N_DATASIZE;//####[285]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[287]####
        }//####[289]####
        return concurrentLinkedQueue;//####[290]####
    }//####[291]####
//####[293]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[293]####
        Object benchmark = null;//####[294]####
        Method method = null;//####[295]####
        try {//####[296]####
            benchmark = bmClass.newInstance();//####[297]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[298]####
        } catch (InstantiationException e) {//####[299]####
            e.printStackTrace();//####[300]####
        } catch (IllegalAccessException e) {//####[301]####
            e.printStackTrace();//####[302]####
        } catch (NoSuchMethodException e) {//####[303]####
            e.printStackTrace();//####[304]####
        } catch (SecurityException e) {//####[305]####
            e.printStackTrace();//####[306]####
        } catch (IllegalArgumentException e) {//####[307]####
            e.printStackTrace();//####[308]####
        }//####[309]####
        Object[] arguments = new Object[1];//####[310]####
        arguments[0] = N_DATASIZE;//####[311]####
        return new Benchmark(benchmark, method, arguments);//####[313]####
    }//####[314]####
//####[316]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[316]####
        Class<?> bmClass = null;//####[318]####
        try {//####[320]####
            if (bmName.equalsIgnoreCase(MOL)) //####[321]####
            {//####[321]####
                bmClass = Class.forName(MOL_CLASS);//####[322]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[323]####
            {//####[323]####
                bmClass = Class.forName(MON_CLASS);//####[324]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[325]####
            {//####[325]####
                bmClass = Class.forName(RAY_CLASS);//####[326]####
            } else {//####[327]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[328]####
            }//####[329]####
        } catch (ClassNotFoundException e) {//####[330]####
            e.printStackTrace();//####[331]####
        } catch (Exception e) {//####[332]####
            e.printStackTrace();//####[333]####
        }//####[334]####
        return bmClass;//####[336]####
    }//####[337]####
}//####[337]####
