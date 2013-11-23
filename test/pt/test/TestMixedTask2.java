package pt.test;//####[1]####
//####[1]####
import java.lang.reflect.InvocationTargetException;//####[3]####
import java.lang.reflect.Method;//####[4]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[5]####
import java.util.Random;//####[6]####
//####[6]####
//-- ParaTask related imports//####[6]####
import pt.runtime.*;//####[6]####
import pt.runtime.ParaTask.ThreadPoolType;

import java.util.concurrent.ExecutionException;//####[6]####
import java.util.concurrent.locks.*;//####[6]####
import java.lang.reflect.*;//####[6]####
import pt.runtime.GuiThread;//####[6]####
import java.util.concurrent.BlockingQueue;//####[6]####
import java.util.ArrayList;//####[6]####
import java.util.List;//####[6]####
//####[6]####
public class TestMixedTask2 {//####[8]####
    static{
    	ParaTask.init();
    }//####[8]####
    /*  ParaTask helper method to access private/protected slots *///####[8]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[8]####
        if (m.getParameterTypes().length == 0)//####[8]####
            m.invoke(instance);//####[8]####
        else if ((m.getParameterTypes().length == 1))//####[8]####
            m.invoke(instance, arg);//####[8]####
        else //####[8]####
            m.invoke(instance, arg, interResult);//####[8]####
    }//####[8]####
//####[9]####
    private static final int N_DATASIZE = 0;//####[9]####
//####[11]####
    private static final String BM_METHOD = "execute";//####[11]####
//####[13]####
    private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = { int.class };//####[13]####
//####[15]####
    private static final String MOL = "MOL";//####[15]####
//####[17]####
    private static final String MOL_CLASS = "core.moldyn.Molcore";//####[17]####
//####[19]####
    private static final String MON = "MON";//####[19]####
//####[21]####
    private static final String MON_CLASS = "core.montecarlo.Moncore";//####[21]####
//####[23]####
    private static final String RAY = "RAY";//####[23]####
//####[25]####
    private static final String RAY_CLASS = "core.raytracer.Raycore";//####[25]####
//####[27]####
    private static ConcurrentLinkedQueue<Benchmark> concurrentLinkedQueue = null;//####[27]####
//####[33]####
    public static void main(String[] args) {//####[33]####
        if (null == args || args.length != 3) //####[34]####
        {//####[34]####
            try {//####[35]####
                throw new Exception("Wrong arguemnts setting");//####[36]####
            } catch (Exception e) {//####[37]####
                e.printStackTrace();//####[38]####
            }//####[39]####
        }//####[40]####
        int totalNum = Integer.valueOf(args[0]);//####[42]####
        TaskID[] taskIDs = new TaskID[totalNum];//####[44]####
        TaskIDGroup tig = new TaskIDGroup(totalNum);//####[46]####
        long startTime = System.currentTimeMillis();//####[48]####
        int[] randomSerial = random_serial(totalNum);//####[50]####
        for (int i = 0; i < randomSerial.length; i++) //####[51]####
        {//####[51]####
            if (randomSerial[i] % 2 == 0) //####[52]####
            {//####[52]####
                taskIDs[i] = runBMS(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[53]####
            } else {//####[54]####
                taskIDs[i] = runBM(createBenchmark(getBenchmarkClass(args[1])));//####[55]####
            }//####[56]####
        }//####[57]####
        for (int i = 0; i < totalNum; i++) //####[59]####
        {//####[59]####
            tig.add(taskIDs[i]);//####[60]####
        }//####[61]####
        try {//####[63]####
            tig.waitTillFinished();//####[64]####
        } catch (ExecutionException e) {//####[65]####
            e.printStackTrace();//####[66]####
        } catch (InterruptedException e) {//####[67]####
            e.printStackTrace();//####[68]####
        }//####[69]####
        long endTime = System.currentTimeMillis();//####[71]####
        System.out.println(endTime - startTime);//####[73]####
    }//####[74]####
//####[76]####
    private static volatile Method __pt__runBMS_ConcurrentLinkedQueueBenchmark_method = null;//####[76]####
    private synchronized static void __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[76]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[76]####
            try {//####[76]####
                __pt__runBMS_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS", new Class[] {//####[76]####
                    ConcurrentLinkedQueue.class//####[76]####
                });//####[76]####
            } catch (Exception e) {//####[76]####
                e.printStackTrace();//####[76]####
            }//####[76]####
        }//####[76]####
    }//####[76]####
    private static TaskIDGroup<Void> runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[76]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[76]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[76]####
    }//####[76]####
    private static TaskIDGroup<Void> runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[76]####
        // ensure Method variable is set//####[76]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[76]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[76]####
        }//####[76]####
        taskinfo.setParameters(benchmarkQueue);//####[76]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[76]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[76]####
    }//####[76]####
    private static TaskIDGroup<Void> runBMS(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[76]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[76]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[76]####
    }//####[76]####
    private static TaskIDGroup<Void> runBMS(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[76]####
        // ensure Method variable is set//####[76]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[76]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[76]####
        }//####[76]####
        taskinfo.setTaskIdArgIndexes(0);//####[76]####
        taskinfo.addDependsOn(benchmarkQueue);//####[76]####
        taskinfo.setParameters(benchmarkQueue);//####[76]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[76]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[76]####
    }//####[76]####
    private static TaskIDGroup<Void> runBMS(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[76]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[76]####
        return runBMS(benchmarkQueue, new TaskInfo());//####[76]####
    }//####[76]####
    private static TaskIDGroup<Void> runBMS(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[76]####
        // ensure Method variable is set//####[76]####
        if (__pt__runBMS_ConcurrentLinkedQueueBenchmark_method == null) {//####[76]####
            __pt__runBMS_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[76]####
        }//####[76]####
        taskinfo.setQueueArgIndexes(0);//####[76]####
        taskinfo.setIsPipeline(true);//####[76]####
        taskinfo.setParameters(benchmarkQueue);//####[76]####
        taskinfo.setMethod(__pt__runBMS_ConcurrentLinkedQueueBenchmark_method);//####[76]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[76]####
    }//####[76]####
    public static void __pt__runBMS(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[76]####
        System.out.println("Multi Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[77]####
        Benchmark benchmark = null;//####[78]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[79]####
        {//####[79]####
            runSubTask(benchmark);//####[80]####
        }//####[81]####
    }//####[82]####
//####[82]####
//####[84]####
    private static volatile Method __pt__runBM_Benchmark_method = null;//####[84]####
    private synchronized static void __pt__runBM_Benchmark_ensureMethodVarSet() {//####[84]####
        if (__pt__runBM_Benchmark_method == null) {//####[84]####
            try {//####[84]####
                __pt__runBM_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM", new Class[] {//####[84]####
                    Benchmark.class//####[84]####
                });//####[84]####
            } catch (Exception e) {//####[84]####
                e.printStackTrace();//####[84]####
            }//####[84]####
        }//####[84]####
    }//####[84]####
    private static TaskID<Void> runBM(Benchmark benchmark) {//####[84]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[84]####
        return runBM(benchmark, new TaskInfo());//####[84]####
    }//####[84]####
    private static TaskID<Void> runBM(Benchmark benchmark, TaskInfo taskinfo) {//####[84]####
        // ensure Method variable is set//####[84]####
        if (__pt__runBM_Benchmark_method == null) {//####[84]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[84]####
        }//####[84]####
        taskinfo.setParameters(benchmark);//####[84]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[84]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[84]####
    }//####[84]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark) {//####[84]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[84]####
        return runBM(benchmark, new TaskInfo());//####[84]####
    }//####[84]####
    private static TaskID<Void> runBM(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[84]####
        // ensure Method variable is set//####[84]####
        if (__pt__runBM_Benchmark_method == null) {//####[84]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[84]####
        }//####[84]####
        taskinfo.setTaskIdArgIndexes(0);//####[84]####
        taskinfo.addDependsOn(benchmark);//####[84]####
        taskinfo.setParameters(benchmark);//####[84]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[84]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[84]####
    }//####[84]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark) {//####[84]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[84]####
        return runBM(benchmark, new TaskInfo());//####[84]####
    }//####[84]####
    private static TaskID<Void> runBM(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[84]####
        // ensure Method variable is set//####[84]####
        if (__pt__runBM_Benchmark_method == null) {//####[84]####
            __pt__runBM_Benchmark_ensureMethodVarSet();//####[84]####
        }//####[84]####
        taskinfo.setQueueArgIndexes(0);//####[84]####
        taskinfo.setIsPipeline(true);//####[84]####
        taskinfo.setParameters(benchmark);//####[84]####
        taskinfo.setMethod(__pt__runBM_Benchmark_method);//####[84]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[84]####
    }//####[84]####
    public static void __pt__runBM(Benchmark benchmark) {//####[84]####
        try {//####[85]####
            System.out.println("One-off Task [Thread ID " + CurrentTask.currentThreadID() + "] [Thread Local ID " + CurrentTask.currentThreadLocalID() + "] [Task Global ID " + CurrentTask.globalID() + "]  [Task Relative ID " + CurrentTask.relativeID() + "]  [Multi Task Size " + CurrentTask.multiTaskSize() + "] ");//####[86]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[87]####
        } catch (IllegalAccessException e) {//####[88]####
            e.printStackTrace();//####[89]####
        } catch (IllegalArgumentException e) {//####[90]####
            e.printStackTrace();//####[91]####
        } catch (InvocationTargetException e) {//####[92]####
            e.printStackTrace();//####[93]####
        }//####[94]####
    }//####[95]####
//####[95]####
//####[97]####
    private static void runSubTask(Benchmark benchmark) {//####[97]####
        try {//####[98]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[99]####
        } catch (IllegalAccessException e) {//####[100]####
            e.printStackTrace();//####[101]####
        } catch (IllegalArgumentException e) {//####[102]####
            e.printStackTrace();//####[103]####
        } catch (InvocationTargetException e) {//####[104]####
            e.printStackTrace();//####[105]####
        }//####[106]####
    }//####[107]####
//####[109]####
    private static int[] random_serial(int limit) {//####[109]####
        int[] result = new int[limit];//####[110]####
        for (int i = 0; i < limit; i++) //####[111]####
        result[i] = i;//####[112]####
        int w;//####[113]####
        Random rand = new Random();//####[114]####
        for (int i = limit - 1; i > 0; i--) //####[115]####
        {//####[115]####
            w = rand.nextInt(i);//####[116]####
            int t = result[i];//####[117]####
            result[i] = result[w];//####[118]####
            result[w] = t;//####[119]####
        }//####[120]####
        return result;//####[121]####
    }//####[122]####
//####[124]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[124]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[125]####
        for (int i = 0; i < setLen; i++) //####[126]####
        {//####[126]####
            Object benchmark = null;//####[127]####
            Method method = null;//####[128]####
            try {//####[129]####
                benchmark = bmClass.newInstance();//####[130]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[131]####
            } catch (InstantiationException e) {//####[132]####
                e.printStackTrace();//####[133]####
            } catch (IllegalAccessException e) {//####[134]####
                e.printStackTrace();//####[135]####
            } catch (NoSuchMethodException e) {//####[136]####
                e.printStackTrace();//####[137]####
            } catch (SecurityException e) {//####[138]####
                e.printStackTrace();//####[139]####
            } catch (IllegalArgumentException e) {//####[140]####
                e.printStackTrace();//####[141]####
            }//####[142]####
            Object[] arguments = new Object[1];//####[143]####
            arguments[0] = N_DATASIZE;//####[144]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[146]####
        }//####[148]####
        return concurrentLinkedQueue;//####[149]####
    }//####[150]####
//####[152]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[152]####
        Object benchmark = null;//####[153]####
        Method method = null;//####[154]####
        try {//####[155]####
            benchmark = bmClass.newInstance();//####[156]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[157]####
        } catch (InstantiationException e) {//####[158]####
            e.printStackTrace();//####[159]####
        } catch (IllegalAccessException e) {//####[160]####
            e.printStackTrace();//####[161]####
        } catch (NoSuchMethodException e) {//####[162]####
            e.printStackTrace();//####[163]####
        } catch (SecurityException e) {//####[164]####
            e.printStackTrace();//####[165]####
        } catch (IllegalArgumentException e) {//####[166]####
            e.printStackTrace();//####[167]####
        }//####[168]####
        Object[] arguments = new Object[1];//####[169]####
        arguments[0] = N_DATASIZE;//####[170]####
        return new Benchmark(benchmark, method, arguments);//####[172]####
    }//####[173]####
//####[175]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[175]####
        Class<?> bmClass = null;//####[177]####
        try {//####[179]####
            if (bmName.equalsIgnoreCase(MOL)) //####[180]####
            {//####[180]####
                bmClass = Class.forName(MOL_CLASS);//####[181]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[182]####
            {//####[182]####
                bmClass = Class.forName(MON_CLASS);//####[183]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[184]####
            {//####[184]####
                bmClass = Class.forName(RAY_CLASS);//####[185]####
            } else {//####[186]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[187]####
            }//####[188]####
        } catch (ClassNotFoundException e) {//####[189]####
            e.printStackTrace();//####[190]####
        } catch (Exception e) {//####[191]####
            e.printStackTrace();//####[192]####
        }//####[193]####
        return bmClass;//####[195]####
    }//####[196]####
}//####[196]####
