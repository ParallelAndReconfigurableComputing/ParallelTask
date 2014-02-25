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
public class TestMixedTask3 {//####[10]####
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
//####[35]####
    public static void main(String[] args) {//####[35]####
        if (null == args || args.length != 3) //####[36]####
        {//####[36]####
            try {//####[37]####
                throw new Exception("Wrong arguemnts setting");//####[38]####
            } catch (Exception e) {//####[39]####
                e.printStackTrace();//####[40]####
            }//####[41]####
        }//####[42]####
        int sectionNum = Integer.valueOf(args[0]);//####[44]####
        int totalNum = sectionNum * 3;//####[45]####
        TaskID[] taskIDs = new TaskID[totalNum];//####[47]####
        TaskIDGroup tig = new TaskIDGroup(totalNum);//####[48]####
        long startTime = System.currentTimeMillis();//####[50]####
        int[] randomSerial = random_serial(totalNum);//####[51]####
        int i = 0;//####[53]####
        for (; i < sectionNum; i++) //####[54]####
        {//####[54]####
            if (randomSerial[i] % 2 == 0) //####[55]####
            {//####[55]####
                taskIDs[i] = runBMS(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[56]####
            } else {//####[57]####
                taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(args[1])));//####[58]####
            }//####[59]####
        }//####[60]####
        try {//####[62]####
            Thread.sleep(1000 * 20);//####[63]####
        } catch (InterruptedException e1) {//####[64]####
            e1.printStackTrace();//####[65]####
        }//####[66]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 4);//####[67]####
        for (; i < sectionNum * 2; i++) //####[69]####
        {//####[69]####
            if (randomSerial[i] % 2 == 0) //####[70]####
            {//####[70]####
                taskIDs[i] = runBMS(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[71]####
            } else {//####[72]####
                taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(args[1])));//####[73]####
            }//####[74]####
        }//####[75]####
        try {//####[77]####
            Thread.sleep(1000 * 20);//####[78]####
        } catch (InterruptedException e1) {//####[79]####
            e1.printStackTrace();//####[80]####
        }//####[81]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 1);//####[82]####
        for (; i < totalNum; i++) //####[84]####
        {//####[84]####
            if (randomSerial[i] % 2 == 0) //####[85]####
            {//####[85]####
                taskIDs[i] = runBMS(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[86]####
            } else {//####[87]####
                taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(args[1])));//####[88]####
            }//####[89]####
        }//####[90]####
        for (int j = 0; j < totalNum; j++) //####[92]####
        {//####[92]####
            tig.add(taskIDs[j]);//####[93]####
        }//####[94]####
        try {//####[96]####
            tig.waitTillFinished();//####[97]####
        } catch (ExecutionException e) {//####[98]####
            e.printStackTrace();//####[99]####
        } catch (InterruptedException e) {//####[100]####
            e.printStackTrace();//####[101]####
        }//####[102]####
        long endTime = System.currentTimeMillis();//####[104]####
    }//####[105]####
//####[107]####
    private static volatile Method __pt__runBMS_ConcurrentLinkedQueueBenchmark_method = null;//####[107]####
    private synchronized static void __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[107]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[107]####
            try {//####[107]####
                __pt__runBMS_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS", new Class[] {//####[107]####
                    ConcurrentLinkedQueue.class//####[107]####
                });//####[107]####
            } catch (Exception e) {//####[107]####
                e.printStackTrace();//####[107]####
            }//####[107]####
        }//####[107]####
    }//####[107]####
    private static TaskIDGroup<Void> runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[107]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[107]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[107]####
    }//####[107]####
    private static TaskIDGroup<Void> runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[107]####
        // ensure Method variable is set//####[107]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[107]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[107]####
        }//####[107]####
        taskinfo.setParameters(benchmarkQueue);//####[107]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[107]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[107]####
    }//####[107]####
    private static TaskIDGroup<Void> runBMS(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[107]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[107]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[107]####
    }//####[107]####
    private static TaskIDGroup<Void> runBMS(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[107]####
        // ensure Method variable is set//####[107]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[107]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[107]####
        }//####[107]####
        taskinfo.setTaskIdArgIndexes(0);//####[107]####
        taskinfo.addDependsOn(benchmarkQueue);//####[107]####
        taskinfo.setParameters(benchmarkQueue);//####[107]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[107]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[107]####
    }//####[107]####
    private static TaskIDGroup<Void> runBMS(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[107]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[107]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[107]####
    }//####[107]####
    private static TaskIDGroup<Void> runBMS(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[107]####
        // ensure Method variable is set//####[107]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[107]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[107]####
        }//####[107]####
        taskinfo.setQueueArgIndexes(0);//####[107]####
        taskinfo.setIsPipeline(true);//####[107]####
        taskinfo.setParameters(benchmarkQueue);//####[107]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[107]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[107]####
    }//####[107]####
    public static void __pt__runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[107]####
        Benchmark benchmark = null;//####[109]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[110]####
        {//####[110]####
            runSubTask(benchmark);//####[111]####
        }//####[112]####
    }//####[113]####
//####[113]####
//####[115]####
    private static volatile Method __pt__runBM_Benchmark_method = null;//####[115]####
    private synchronized static void __pt__runBM_Benchmark_ensureMethodVarSet() {//####[115]####
        if (__pt__runBM_Benchmark_method == null) {//####[115]####
            try {//####[115]####
                __pt__runBM_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM", new Class[] {//####[115]####
                    Benchmark.class//####[115]####
                });//####[115]####
            } catch (Exception e) {//####[115]####
                e.printStackTrace();//####[115]####
            }//####[115]####
        }//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM(Benchmark benchmark) {//####[115]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[115]####
        return runBM(benchmark, new TaskInfo());//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM(Benchmark benchmark, TaskInfo taskinfo) {//####[115]####
        // ensure Method variable is set//####[115]####
        if (__pt__runBM_Benchmark_method == null) {//####[115]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[115]####
        }//####[115]####
        taskinfo.setParameters(benchmark);//####[115]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[115]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark) {//####[115]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[115]####
        return runBM(benchmark, new TaskInfo());//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[115]####
        // ensure Method variable is set//####[115]####
        if (__pt__runBM_Benchmark_method == null) {//####[115]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[115]####
        }//####[115]####
        taskinfo.setTaskIdArgIndexes(0);//####[115]####
        taskinfo.addDependsOn(benchmark);//####[115]####
        taskinfo.setParameters(benchmark);//####[115]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[115]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark) {//####[115]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[115]####
        return runBM(benchmark, new TaskInfo());//####[115]####
    }//####[115]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[115]####
        // ensure Method variable is set//####[115]####
        if (__pt__runBM_Benchmark_method == null) {//####[115]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[115]####
        }//####[115]####
        taskinfo.setQueueArgIndexes(0);//####[115]####
        taskinfo.setIsPipeline(true);//####[115]####
        taskinfo.setParameters(benchmark);//####[115]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[115]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[115]####
    }//####[115]####
    public static void __pt__runBM(Benchmark benchmark) {//####[115]####
        try {//####[116]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[117]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[118]####
        } catch (IllegalAccessException e) {//####[119]####
            e.printStackTrace();//####[120]####
        } catch (IllegalArgumentException e) {//####[121]####
            e.printStackTrace();//####[122]####
        } catch (InvocationTargetException e) {//####[123]####
            e.printStackTrace();//####[124]####
        }//####[125]####
    }//####[126]####
//####[126]####
//####[128]####
    private static void runSubTask(Benchmark benchmark) {//####[128]####
        try {//####[129]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[130]####
        } catch (IllegalAccessException e) {//####[131]####
            e.printStackTrace();//####[132]####
        } catch (IllegalArgumentException e) {//####[133]####
            e.printStackTrace();//####[134]####
        } catch (InvocationTargetException e) {//####[135]####
            e.printStackTrace();//####[136]####
        }//####[137]####
    }//####[138]####
//####[140]####
    private static int[] random_serial(int limit) {//####[140]####
        int[] result = new int[limit];//####[141]####
        for (int i = 0; i < limit; i++) //####[142]####
        result[i] = i;//####[143]####
        int w;//####[144]####
        Random rand = new Random();//####[145]####
        for (int i = limit - 1; i > 0; i--) //####[146]####
        {//####[146]####
            w = rand.nextInt(i);//####[147]####
            int t = result[i];//####[148]####
            result[i] = result[w];//####[149]####
            result[w] = t;//####[150]####
        }//####[151]####
        return result;//####[152]####
    }//####[153]####
//####[155]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[155]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[156]####
        for (int i = 0; i < setLen; i++) //####[157]####
        {//####[157]####
            Object benchmark = null;//####[158]####
            Method method = null;//####[159]####
            try {//####[160]####
                benchmark = bmClass.newInstance();//####[161]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[162]####
            } catch (InstantiationException e) {//####[163]####
                e.printStackTrace();//####[164]####
            } catch (IllegalAccessException e) {//####[165]####
                e.printStackTrace();//####[166]####
            } catch (NoSuchMethodException e) {//####[167]####
                e.printStackTrace();//####[168]####
            } catch (SecurityException e) {//####[169]####
                e.printStackTrace();//####[170]####
            } catch (IllegalArgumentException e) {//####[171]####
                e.printStackTrace();//####[172]####
            }//####[173]####
            Object[] arguments = new Object[1];//####[174]####
            arguments[0] = N_DATASIZE;//####[175]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[177]####
        }//####[179]####
        return concurrentLinkedQueue;//####[180]####
    }//####[181]####
//####[183]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[183]####
        Object benchmark = null;//####[184]####
        Method method = null;//####[185]####
        try {//####[186]####
            benchmark = bmClass.newInstance();//####[187]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[188]####
        } catch (InstantiationException e) {//####[189]####
            e.printStackTrace();//####[190]####
        } catch (IllegalAccessException e) {//####[191]####
            e.printStackTrace();//####[192]####
        } catch (NoSuchMethodException e) {//####[193]####
            e.printStackTrace();//####[194]####
        } catch (SecurityException e) {//####[195]####
            e.printStackTrace();//####[196]####
        } catch (IllegalArgumentException e) {//####[197]####
            e.printStackTrace();//####[198]####
        }//####[199]####
        Object[] arguments = new Object[1];//####[200]####
        arguments[0] = N_DATASIZE;//####[201]####
        return new Benchmark(benchmark, method, arguments);//####[203]####
    }//####[204]####
//####[206]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[206]####
        Class<?> bmClass = null;//####[208]####
        try {//####[210]####
            if (bmName.equalsIgnoreCase(MOL)) //####[211]####
            {//####[211]####
                bmClass = Class.forName(MOL_CLASS);//####[212]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[213]####
            {//####[213]####
                bmClass = Class.forName(MON_CLASS);//####[214]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[215]####
            {//####[215]####
                bmClass = Class.forName(RAY_CLASS);//####[216]####
            } else {//####[217]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[218]####
            }//####[219]####
        } catch (ClassNotFoundException e) {//####[220]####
            e.printStackTrace();//####[221]####
        } catch (Exception e) {//####[222]####
            e.printStackTrace();//####[223]####
        }//####[224]####
        return bmClass;//####[226]####
    }//####[227]####
}//####[227]####
