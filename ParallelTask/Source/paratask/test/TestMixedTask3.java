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
public class TestMixedTask3 {//####[11]####
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
//####[36]####
    public static void main(String[] args) {//####[36]####
        if (null == args || args.length != 3) //####[37]####
        {//####[37]####
            try {//####[38]####
                throw new Exception("Wrong arguemnts setting");//####[39]####
            } catch (Exception e) {//####[40]####
                e.printStackTrace();//####[41]####
            }//####[42]####
        }//####[43]####
        int sectionNum = Integer.valueOf(args[0]);//####[45]####
        int totalNum = sectionNum * 3;//####[46]####
        TaskID[] taskIDs = new TaskID[totalNum];//####[48]####
        TaskIDGroup tig = new TaskIDGroup(totalNum);//####[49]####
        long startTime = System.currentTimeMillis();//####[51]####
        int[] randomSerial = random_serial(totalNum);//####[52]####
        int i = 0;//####[54]####
        for (; i < sectionNum; i++) //####[55]####
        {//####[55]####
            if (randomSerial[i] % 2 == 0) //####[56]####
            {//####[56]####
                taskIDs[i] = runBMS(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[57]####
            } else {//####[58]####
                taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(args[1])));//####[59]####
            }//####[60]####
        }//####[61]####
        try {//####[63]####
            Thread.sleep(1000 * 20);//####[64]####
        } catch (InterruptedException e1) {//####[65]####
            e1.printStackTrace();//####[66]####
        }//####[67]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 4);//####[68]####
        for (; i < sectionNum * 2; i++) //####[70]####
        {//####[70]####
            if (randomSerial[i] % 2 == 0) //####[71]####
            {//####[71]####
                taskIDs[i] = runBMS(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[72]####
            } else {//####[73]####
                taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(args[1])));//####[74]####
            }//####[75]####
        }//####[76]####
        try {//####[78]####
            Thread.sleep(1000 * 20);//####[79]####
        } catch (InterruptedException e1) {//####[80]####
            e1.printStackTrace();//####[81]####
        }//####[82]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, 1);//####[83]####
        for (; i < totalNum; i++) //####[85]####
        {//####[85]####
            if (randomSerial[i] % 2 == 0) //####[86]####
            {//####[86]####
                taskIDs[i] = runBMS(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[87]####
            } else {//####[88]####
                taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(args[1])));//####[89]####
            }//####[90]####
        }//####[91]####
        for (int j = 0; j < totalNum; j++) //####[93]####
        {//####[93]####
            tig.add(taskIDs[j]);//####[94]####
        }//####[95]####
        try {//####[97]####
            tig.waitTillFinished();//####[98]####
        } catch (ExecutionException e) {//####[99]####
            e.printStackTrace();//####[100]####
        } catch (InterruptedException e) {//####[101]####
            e.printStackTrace();//####[102]####
        }//####[103]####
        long endTime = System.currentTimeMillis();//####[105]####
    }//####[106]####
//####[108]####
    private static volatile Method __pt__runBMS_ConcurrentLinkedQueueBenchmark_method = null;//####[108]####
    private synchronized static void __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[108]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[108]####
            try {//####[108]####
                __pt__runBMS_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS", new Class[] {//####[108]####
                    ConcurrentLinkedQueue.class//####[108]####
                });//####[108]####
            } catch (Exception e) {//####[108]####
                e.printStackTrace();//####[108]####
            }//####[108]####
        }//####[108]####
    }//####[108]####
    private static TaskIDGroup<Void> runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskIDGroup<Void> runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[108]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setParameters(benchmarkQueue);//####[108]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[108]####
    }//####[108]####
    private static TaskIDGroup<Void> runBMS(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskIDGroup<Void> runBMS(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[108]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setTaskIdArgIndexes(0);//####[108]####
        taskinfo.addDependsOn(benchmarkQueue);//####[108]####
        taskinfo.setParameters(benchmarkQueue);//####[108]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[108]####
    }//####[108]####
    private static TaskIDGroup<Void> runBMS(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskIDGroup<Void> runBMS(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[108]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setQueueArgIndexes(0);//####[108]####
        taskinfo.setIsPipeline(true);//####[108]####
        taskinfo.setParameters(benchmarkQueue);//####[108]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[108]####
    }//####[108]####
    public static void __pt__runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[108]####
        Benchmark benchmark = null;//####[110]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[111]####
        {//####[111]####
            runSubTask(benchmark);//####[112]####
        }//####[113]####
    }//####[114]####
//####[114]####
//####[116]####
    private static volatile Method __pt__runBM_Benchmark_method = null;//####[116]####
    private synchronized static void __pt__runBM_Benchmark_ensureMethodVarSet() {//####[116]####
        if (__pt__runBM_Benchmark_method == null) {//####[116]####
            try {//####[116]####
                __pt__runBM_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM", new Class[] {//####[116]####
                    Benchmark.class//####[116]####
                });//####[116]####
            } catch (Exception e) {//####[116]####
                e.printStackTrace();//####[116]####
            }//####[116]####
        }//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM(Benchmark benchmark) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return runBM(benchmark, new TaskInfo());//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM(Benchmark benchmark, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__runBM_Benchmark_method == null) {//####[116]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setParameters(benchmark);//####[116]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return runBM(benchmark, new TaskInfo());//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__runBM_Benchmark_method == null) {//####[116]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setTaskIdArgIndexes(0);//####[116]####
        taskinfo.addDependsOn(benchmark);//####[116]####
        taskinfo.setParameters(benchmark);//####[116]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return runBM(benchmark, new TaskInfo());//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__runBM_Benchmark_method == null) {//####[116]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setQueueArgIndexes(0);//####[116]####
        taskinfo.setIsPipeline(true);//####[116]####
        taskinfo.setParameters(benchmark);//####[116]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    public static void __pt__runBM(Benchmark benchmark) {//####[116]####
        try {//####[117]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[118]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[119]####
        } catch (IllegalAccessException e) {//####[120]####
            e.printStackTrace();//####[121]####
        } catch (IllegalArgumentException e) {//####[122]####
            e.printStackTrace();//####[123]####
        } catch (InvocationTargetException e) {//####[124]####
            e.printStackTrace();//####[125]####
        }//####[126]####
    }//####[127]####
//####[127]####
//####[129]####
    private static void runSubTask(Benchmark benchmark) {//####[129]####
        try {//####[130]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[131]####
        } catch (IllegalAccessException e) {//####[132]####
            e.printStackTrace();//####[133]####
        } catch (IllegalArgumentException e) {//####[134]####
            e.printStackTrace();//####[135]####
        } catch (InvocationTargetException e) {//####[136]####
            e.printStackTrace();//####[137]####
        }//####[138]####
    }//####[139]####
//####[141]####
    private static int[] random_serial(int limit) {//####[141]####
        int[] result = new int[limit];//####[142]####
        for (int i = 0; i < limit; i++) //####[143]####
        result[i] = i;//####[144]####
        int w;//####[145]####
        Random rand = new Random();//####[146]####
        for (int i = limit - 1; i > 0; i--) //####[147]####
        {//####[147]####
            w = rand.nextInt(i);//####[148]####
            int t = result[i];//####[149]####
            result[i] = result[w];//####[150]####
            result[w] = t;//####[151]####
        }//####[152]####
        return result;//####[153]####
    }//####[154]####
//####[156]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[156]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[157]####
        for (int i = 0; i < setLen; i++) //####[158]####
        {//####[158]####
            Object benchmark = null;//####[159]####
            Method method = null;//####[160]####
            try {//####[161]####
                benchmark = bmClass.newInstance();//####[162]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[163]####
            } catch (InstantiationException e) {//####[164]####
                e.printStackTrace();//####[165]####
            } catch (IllegalAccessException e) {//####[166]####
                e.printStackTrace();//####[167]####
            } catch (NoSuchMethodException e) {//####[168]####
                e.printStackTrace();//####[169]####
            } catch (SecurityException e) {//####[170]####
                e.printStackTrace();//####[171]####
            } catch (IllegalArgumentException e) {//####[172]####
                e.printStackTrace();//####[173]####
            }//####[174]####
            Object[] arguments = new Object[1];//####[175]####
            arguments[0] = N_DATASIZE;//####[176]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[178]####
        }//####[180]####
        return concurrentLinkedQueue;//####[181]####
    }//####[182]####
//####[184]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[184]####
        Object benchmark = null;//####[185]####
        Method method = null;//####[186]####
        try {//####[187]####
            benchmark = bmClass.newInstance();//####[188]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[189]####
        } catch (InstantiationException e) {//####[190]####
            e.printStackTrace();//####[191]####
        } catch (IllegalAccessException e) {//####[192]####
            e.printStackTrace();//####[193]####
        } catch (NoSuchMethodException e) {//####[194]####
            e.printStackTrace();//####[195]####
        } catch (SecurityException e) {//####[196]####
            e.printStackTrace();//####[197]####
        } catch (IllegalArgumentException e) {//####[198]####
            e.printStackTrace();//####[199]####
        }//####[200]####
        Object[] arguments = new Object[1];//####[201]####
        arguments[0] = N_DATASIZE;//####[202]####
        return new Benchmark(benchmark, method, arguments);//####[204]####
    }//####[205]####
//####[207]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[207]####
        Class<?> bmClass = null;//####[209]####
        try {//####[211]####
            if (bmName.equalsIgnoreCase(MOL)) //####[212]####
            {//####[212]####
                bmClass = Class.forName(MOL_CLASS);//####[213]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[214]####
            {//####[214]####
                bmClass = Class.forName(MON_CLASS);//####[215]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[216]####
            {//####[216]####
                bmClass = Class.forName(RAY_CLASS);//####[217]####
            } else {//####[218]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[219]####
            }//####[220]####
        } catch (ClassNotFoundException e) {//####[221]####
            e.printStackTrace();//####[222]####
        } catch (Exception e) {//####[223]####
            e.printStackTrace();//####[224]####
        }//####[225]####
        return bmClass;//####[227]####
    }//####[228]####
}//####[228]####
