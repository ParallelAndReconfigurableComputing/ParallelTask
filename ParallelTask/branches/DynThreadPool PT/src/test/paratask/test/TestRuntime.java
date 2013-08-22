package paratask.test;//####[1]####
//####[1]####
import java.lang.reflect.InvocationTargetException;//####[3]####
import java.lang.reflect.Method;//####[4]####
import java.text.ParseException;//####[5]####
import java.text.SimpleDateFormat;//####[6]####
import java.util.Date;//####[7]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[8]####
//####[8]####
//-- ParaTask related imports//####[8]####
import paratask.runtime.*;//####[8]####
import java.util.concurrent.ExecutionException;//####[8]####
import java.util.concurrent.locks.*;//####[8]####
import java.lang.reflect.*;//####[8]####
import paratask.runtime.GuiThread;//####[8]####
import java.util.concurrent.BlockingQueue;//####[8]####
import java.util.ArrayList;//####[8]####
import java.util.List;//####[8]####
//####[8]####
public class TestRuntime {//####[10]####
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
    private static final int N_DATASIZE = 1;//####[11]####
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
    private static final String DATE_FORMART = "yyyyMMddHHmmss";//####[29]####
//####[31]####
    private static ConcurrentLinkedQueue<Benchmark> concurrentLinkedQueue = null;//####[31]####
//####[33]####
    private static List<Float> resultList = new ArrayList<Float>();//####[33]####
//####[50]####
    /**
	 * @param args
	 *            [0] The type of JGF benchmark. It should be a 3 character
	 *            length String. The allowed value only includes {MOL, MON,
	 *            RAY}, and it is not case sensitive.
	 * 
	 * @param args
	 *            [1] The length of the benchmark set. This value define how
	 *            many benchmark will be included during one test case.
	 * 
	 * @param args
	 *            [2] The benchmark start running time. The time format must be "yyyyMMddHHmmss"
	 * 
	 * 
	 * *///####[50]####
    public static void main(String[] args) {//####[50]####
        if (null == args || args.length != 3) //####[51]####
        {//####[51]####
            try {//####[52]####
                throw new Exception("Wrong arguemnts setting");//####[53]####
            } catch (Exception e) {//####[54]####
                e.printStackTrace();//####[55]####
            }//####[56]####
        }//####[57]####
        Class<?> bmClass = getBenchmarkClass(args[0]);//####[59]####
        try {//####[61]####
            concurrentLinkedQueue = createBenchmarkSet(bmClass, Integer.valueOf(args[1]));//####[62]####
            Date startDate = new SimpleDateFormat(DATE_FORMART).parse(args[2]);//####[64]####
            while (startDate.after(new Date())) //####[65]####
            {//####[65]####
                try {//####[66]####
                    Thread.sleep(1000);//####[67]####
                } catch (InterruptedException e) {//####[68]####
                    e.printStackTrace();//####[69]####
                }//####[70]####
            }//####[71]####
            TaskID tid = execute(concurrentLinkedQueue);//####[73]####
            TaskIDGroup tig = new TaskIDGroup(1);//####[74]####
            tig.add(tid);//####[75]####
            tig.waitTillFinished();//####[76]####
        } catch (NumberFormatException e) {//####[78]####
            e.printStackTrace();//####[79]####
        } catch (ParseException e) {//####[80]####
            e.printStackTrace();//####[81]####
        } catch (ExecutionException e) {//####[82]####
            e.printStackTrace();//####[83]####
        } catch (InterruptedException e) {//####[84]####
            e.printStackTrace();//####[85]####
        }//####[86]####
        float sum = 0.0f;//####[88]####
        for (float result : resultList) //####[89]####
        {//####[89]####
            sum += result;//####[90]####
        }//####[91]####
        System.out.println(sum / resultList.size());//####[92]####
    }//####[94]####
//####[96]####
    private static volatile Method __pt__execute_ConcurrentLinkedQueueBenchmark_method = null;//####[96]####
    private synchronized static void __pt__execute_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[96]####
        if (__pt__execute_ConcurrentLinkedQueueBenchmark_method == null) {//####[96]####
            try {//####[96]####
                __pt__execute_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__execute", new Class[] {//####[96]####
                    ConcurrentLinkedQueue.class//####[96]####
                });//####[96]####
            } catch (Exception e) {//####[96]####
                e.printStackTrace();//####[96]####
            }//####[96]####
        }//####[96]####
    }//####[96]####
    private static TaskIDGroup<Void> execute(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[96]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[96]####
        return execute(benchmarkQueue, new TaskInfo());//####[96]####
    }//####[96]####
    private static TaskIDGroup<Void> execute(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[96]####
        // ensure Method variable is set//####[96]####
        if (__pt__execute_ConcurrentLinkedQueueBenchmark_method == null) {//####[96]####
            __pt__execute_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[96]####
        }//####[96]####
        taskinfo.setParameters(benchmarkQueue);//####[96]####
        taskinfo.setMethod(__pt__execute_ConcurrentLinkedQueueBenchmark_method);//####[96]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[96]####
    }//####[96]####
    private static TaskIDGroup<Void> execute(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[96]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[96]####
        return execute(benchmarkQueue, new TaskInfo());//####[96]####
    }//####[96]####
    private static TaskIDGroup<Void> execute(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[96]####
        // ensure Method variable is set//####[96]####
        if (__pt__execute_ConcurrentLinkedQueueBenchmark_method == null) {//####[96]####
            __pt__execute_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[96]####
        }//####[96]####
        taskinfo.setTaskIdArgIndexes(0);//####[96]####
        taskinfo.addDependsOn(benchmarkQueue);//####[96]####
        taskinfo.setParameters(benchmarkQueue);//####[96]####
        taskinfo.setMethod(__pt__execute_ConcurrentLinkedQueueBenchmark_method);//####[96]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[96]####
    }//####[96]####
    private static TaskIDGroup<Void> execute(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[96]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[96]####
        return execute(benchmarkQueue, new TaskInfo());//####[96]####
    }//####[96]####
    private static TaskIDGroup<Void> execute(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[96]####
        // ensure Method variable is set//####[96]####
        if (__pt__execute_ConcurrentLinkedQueueBenchmark_method == null) {//####[96]####
            __pt__execute_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[96]####
        }//####[96]####
        taskinfo.setQueueArgIndexes(0);//####[96]####
        taskinfo.setIsPipeline(true);//####[96]####
        taskinfo.setParameters(benchmarkQueue);//####[96]####
        taskinfo.setMethod(__pt__execute_ConcurrentLinkedQueueBenchmark_method);//####[96]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[96]####
    }//####[96]####
    public static void __pt__execute(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[96]####
        Benchmark benchmark = null;//####[98]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[99]####
        {//####[99]####
            resultList.add(runBM(benchmark));//####[100]####
        }//####[101]####
    }//####[103]####
//####[103]####
//####[105]####
    private static Float runBM(Benchmark benchmark) {//####[105]####
        Object returnObj = null;//####[106]####
        try {//####[107]####
            returnObj = benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[108]####
        } catch (IllegalAccessException e) {//####[109]####
            e.printStackTrace();//####[110]####
        } catch (IllegalArgumentException e) {//####[111]####
            e.printStackTrace();//####[112]####
        } catch (InvocationTargetException e) {//####[113]####
            e.printStackTrace();//####[114]####
        }//####[115]####
        return (Float) returnObj;//####[116]####
    }//####[117]####
//####[119]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[119]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[120]####
        for (int i = 0; i < setLen; i++) //####[121]####
        {//####[121]####
            Object benchmark = null;//####[122]####
            Method method = null;//####[123]####
            try {//####[124]####
                benchmark = bmClass.newInstance();//####[125]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[126]####
            } catch (InstantiationException e) {//####[127]####
                e.printStackTrace();//####[128]####
            } catch (IllegalAccessException e) {//####[129]####
                e.printStackTrace();//####[130]####
            } catch (NoSuchMethodException e) {//####[131]####
                e.printStackTrace();//####[132]####
            } catch (SecurityException e) {//####[133]####
                e.printStackTrace();//####[134]####
            } catch (IllegalArgumentException e) {//####[135]####
                e.printStackTrace();//####[136]####
            }//####[137]####
            Object[] arguments = new Object[1];//####[138]####
            arguments[0] = N_DATASIZE;//####[139]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[141]####
        }//####[143]####
        return concurrentLinkedQueue;//####[144]####
    }//####[145]####
//####[147]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[147]####
        Class<?> bmClass = null;//####[149]####
        try {//####[151]####
            if (bmName.equalsIgnoreCase(MOL)) //####[152]####
            {//####[152]####
                bmClass = Class.forName(MOL_CLASS);//####[153]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[154]####
            {//####[154]####
                bmClass = Class.forName(MON_CLASS);//####[155]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[156]####
            {//####[156]####
                bmClass = Class.forName(RAY_CLASS);//####[157]####
            } else {//####[158]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[159]####
            }//####[160]####
        } catch (ClassNotFoundException e) {//####[161]####
            e.printStackTrace();//####[162]####
        } catch (Exception e) {//####[163]####
            e.printStackTrace();//####[164]####
        }//####[165]####
        return bmClass;//####[167]####
    }//####[168]####
}//####[168]####
