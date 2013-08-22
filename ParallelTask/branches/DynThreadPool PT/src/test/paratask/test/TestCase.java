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
public class TestCase {//####[10]####
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
    private static final String DATE_FORMART = "yyyyMMddHHmmss";//####[29]####
//####[31]####
    private static ConcurrentLinkedQueue<Benchmark> concurrentLinkedQueue = null;//####[31]####
//####[33]####
    private static List<Float> resultList = new ArrayList<Float>();//####[33]####
//####[51]####
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
	 * *///####[51]####
    private String benchmarkName;//####[51]####
//####[53]####
    private String queueLength;//####[53]####
//####[55]####
    private String startTime;//####[55]####
//####[57]####
    public TestCase(String benchmarkName, String queueLength, String startTime) {//####[57]####
        super();//####[58]####
        this.benchmarkName = benchmarkName;//####[59]####
        this.queueLength = queueLength;//####[60]####
        this.startTime = startTime;//####[61]####
    }//####[62]####
//####[63]####
    public void startTest() {//####[63]####
        Class<?> bmClass = getBenchmarkClass(benchmarkName);//####[64]####
        long sTime = 0L;//####[65]####
        long eTimer = 0L;//####[66]####
        try {//####[67]####
            concurrentLinkedQueue = createBenchmarkSet(bmClass, Integer.valueOf(queueLength));//####[68]####
            Date startDate = new SimpleDateFormat(DATE_FORMART).parse(startTime);//####[70]####
            while (startDate.after(new Date())) //####[71]####
            {//####[71]####
                try {//####[72]####
                    Thread.sleep(1000);//####[73]####
                } catch (InterruptedException e) {//####[74]####
                    e.printStackTrace();//####[75]####
                }//####[76]####
            }//####[77]####
            sTime = System.currentTimeMillis();//####[79]####
            TaskID tid = execute(concurrentLinkedQueue);//####[81]####
            TaskIDGroup tig = new TaskIDGroup(1);//####[82]####
            tig.add(tid);//####[83]####
            tig.waitTillFinished();//####[84]####
            eTimer = System.currentTimeMillis();//####[86]####
        } catch (NumberFormatException e) {//####[88]####
            e.printStackTrace();//####[89]####
        } catch (ParseException e) {//####[90]####
            e.printStackTrace();//####[91]####
        } catch (ExecutionException e) {//####[92]####
            e.printStackTrace();//####[93]####
        } catch (InterruptedException e) {//####[94]####
            e.printStackTrace();//####[95]####
        }//####[96]####
        float sum = 0.0f;//####[98]####
        for (float result : resultList) //####[99]####
        {//####[99]####
            sum += result;//####[100]####
        }//####[101]####
        System.out.println(sum / resultList.size() + " " + (eTimer - sTime) / 1000000);//####[102]####
    }//####[104]####
//####[106]####
    private static volatile Method __pt__execute_ConcurrentLinkedQueueBenchmark_method = null;//####[106]####
    private synchronized static void __pt__execute_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[106]####
        if (__pt__execute_ConcurrentLinkedQueueBenchmark_method == null) {//####[106]####
            try {//####[106]####
                __pt__execute_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__execute", new Class[] {//####[106]####
                    ConcurrentLinkedQueue.class//####[106]####
                });//####[106]####
            } catch (Exception e) {//####[106]####
                e.printStackTrace();//####[106]####
            }//####[106]####
        }//####[106]####
    }//####[106]####
    private TaskIDGroup<Void> execute(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[106]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[106]####
        return execute(benchmarkQueue, new TaskInfo());//####[106]####
    }//####[106]####
    private TaskIDGroup<Void> execute(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[106]####
        // ensure Method variable is set//####[106]####
        if (__pt__execute_ConcurrentLinkedQueueBenchmark_method == null) {//####[106]####
            __pt__execute_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[106]####
        }//####[106]####
        taskinfo.setParameters(benchmarkQueue);//####[106]####
        taskinfo.setMethod(__pt__execute_ConcurrentLinkedQueueBenchmark_method);//####[106]####
        taskinfo.setInstance(this);//####[106]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[106]####
    }//####[106]####
    private TaskIDGroup<Void> execute(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[106]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[106]####
        return execute(benchmarkQueue, new TaskInfo());//####[106]####
    }//####[106]####
    private TaskIDGroup<Void> execute(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[106]####
        // ensure Method variable is set//####[106]####
        if (__pt__execute_ConcurrentLinkedQueueBenchmark_method == null) {//####[106]####
            __pt__execute_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[106]####
        }//####[106]####
        taskinfo.setTaskIdArgIndexes(0);//####[106]####
        taskinfo.addDependsOn(benchmarkQueue);//####[106]####
        taskinfo.setParameters(benchmarkQueue);//####[106]####
        taskinfo.setMethod(__pt__execute_ConcurrentLinkedQueueBenchmark_method);//####[106]####
        taskinfo.setInstance(this);//####[106]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[106]####
    }//####[106]####
    private TaskIDGroup<Void> execute(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[106]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[106]####
        return execute(benchmarkQueue, new TaskInfo());//####[106]####
    }//####[106]####
    private TaskIDGroup<Void> execute(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[106]####
        // ensure Method variable is set//####[106]####
        if (__pt__execute_ConcurrentLinkedQueueBenchmark_method == null) {//####[106]####
            __pt__execute_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[106]####
        }//####[106]####
        taskinfo.setQueueArgIndexes(0);//####[106]####
        taskinfo.setIsPipeline(true);//####[106]####
        taskinfo.setParameters(benchmarkQueue);//####[106]####
        taskinfo.setMethod(__pt__execute_ConcurrentLinkedQueueBenchmark_method);//####[106]####
        taskinfo.setInstance(this);//####[106]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[106]####
    }//####[106]####
    public void __pt__execute(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[106]####
        Benchmark benchmark = null;//####[108]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[109]####
        {//####[109]####
            resultList.add(runBM(benchmark));//####[110]####
        }//####[111]####
    }//####[113]####
//####[113]####
//####[115]####
    private Float runBM(Benchmark benchmark) {//####[115]####
        Object returnObj = null;//####[116]####
        try {//####[117]####
            returnObj = benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[118]####
        } catch (IllegalAccessException e) {//####[119]####
            e.printStackTrace();//####[120]####
        } catch (IllegalArgumentException e) {//####[121]####
            e.printStackTrace();//####[122]####
        } catch (InvocationTargetException e) {//####[123]####
            e.printStackTrace();//####[124]####
        }//####[125]####
        return (Float) returnObj;//####[126]####
    }//####[127]####
//####[129]####
    private ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[129]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[130]####
        for (int i = 0; i < setLen; i++) //####[131]####
        {//####[131]####
            Object benchmark = null;//####[132]####
            Method method = null;//####[133]####
            try {//####[134]####
                benchmark = bmClass.newInstance();//####[135]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[136]####
            } catch (InstantiationException e) {//####[137]####
                e.printStackTrace();//####[138]####
            } catch (IllegalAccessException e) {//####[139]####
                e.printStackTrace();//####[140]####
            } catch (NoSuchMethodException e) {//####[141]####
                e.printStackTrace();//####[142]####
            } catch (SecurityException e) {//####[143]####
                e.printStackTrace();//####[144]####
            } catch (IllegalArgumentException e) {//####[145]####
                e.printStackTrace();//####[146]####
            }//####[147]####
            Object[] arguments = new Object[1];//####[148]####
            arguments[0] = N_DATASIZE;//####[149]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[151]####
        }//####[153]####
        return concurrentLinkedQueue;//####[154]####
    }//####[155]####
//####[157]####
    private Class<?> getBenchmarkClass(String bmName) {//####[157]####
        Class<?> bmClass = null;//####[159]####
        try {//####[161]####
            if (bmName.equalsIgnoreCase(MOL)) //####[162]####
            {//####[162]####
                bmClass = Class.forName(MOL_CLASS);//####[163]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[164]####
            {//####[164]####
                bmClass = Class.forName(MON_CLASS);//####[165]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[166]####
            {//####[166]####
                bmClass = Class.forName(RAY_CLASS);//####[167]####
            } else {//####[168]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[169]####
            }//####[170]####
        } catch (ClassNotFoundException e) {//####[171]####
            e.printStackTrace();//####[172]####
        } catch (Exception e) {//####[173]####
            e.printStackTrace();//####[174]####
        }//####[175]####
        return bmClass;//####[177]####
    }//####[178]####
}//####[178]####
