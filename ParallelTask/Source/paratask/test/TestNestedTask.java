package paratask.test;//####[1]####
//####[1]####
import java.util.Date;//####[3]####
import paratask.moldyn.Param;//####[5]####
//####[5]####
//-- ParaTask related imports//####[5]####
import paratask.runtime.*;//####[5]####
import java.util.concurrent.ExecutionException;//####[5]####
import java.util.concurrent.locks.*;//####[5]####
import java.lang.reflect.*;//####[5]####
import paratask.runtime.GuiThread;//####[5]####
import java.util.concurrent.BlockingQueue;//####[5]####
import java.util.ArrayList;//####[5]####
import java.util.List;//####[5]####
//####[5]####
public class TestNestedTask {//####[7]####
    static{ParaTask.init();}//####[7]####
    /*  ParaTask helper method to access private/protected slots *///####[7]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[7]####
        if (m.getParameterTypes().length == 0)//####[7]####
            m.invoke(instance);//####[7]####
        else if ((m.getParameterTypes().length == 1))//####[7]####
            m.invoke(instance, arg);//####[7]####
        else //####[7]####
            m.invoke(instance, arg, interResult);//####[7]####
    }//####[7]####
//####[8]####
    private static final int N_DATASIZE = 0;//####[8]####
//####[10]####
    private static final String BM_METHOD = "execute";//####[10]####
//####[12]####
    private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = { int.class };//####[12]####
//####[14]####
    private static final String MOL = "MOL";//####[14]####
//####[16]####
    private static final String MOL_CLASS = "core.moldyn.Molcore";//####[16]####
//####[18]####
    private static final String MON = "MON";//####[18]####
//####[20]####
    private static final String MON_CLASS = "core.montecarlo.Moncore";//####[20]####
//####[22]####
    private static final String RAY = "RAY";//####[22]####
//####[24]####
    private static final String RAY_CLASS = "core.raytracer.Raycore";//####[24]####
//####[26]####
    private static Class<?> bmClass;//####[26]####
//####[29]####
    public static void main(String[] args) {//####[29]####
        if (null == args || args.length != 1) //####[30]####
        {//####[30]####
            try {//####[31]####
                throw new Exception("Wrong arguemnts setting");//####[32]####
            } catch (Exception e) {//####[33]####
                e.printStackTrace();//####[34]####
            }//####[35]####
        }//####[36]####
        getBenchmarkClass(args[0]);//####[38]####
        TaskID tid_0 = runBM_0(createBenchmark());//####[40]####
        TaskID tid_1 = runBM_1(createBenchmark());//####[41]####
        TaskID tid_2 = runBM_2(createBenchmark());//####[42]####
        TaskIDGroup tig = new TaskIDGroup(3);//####[45]####
        tig.add(tid_0);//####[47]####
        tig.add(tid_1);//####[48]####
        tig.add(tid_2);//####[49]####
        try {//####[52]####
            tig.waitTillFinished();//####[53]####
        } catch (ExecutionException e) {//####[54]####
            e.printStackTrace();//####[55]####
        } catch (InterruptedException e) {//####[56]####
            e.printStackTrace();//####[57]####
        }//####[58]####
    }//####[59]####
//####[61]####
    private static volatile Method __pt__runBM_0_Benchmark_method = null;//####[61]####
    private synchronized static void __pt__runBM_0_Benchmark_ensureMethodVarSet() {//####[61]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[61]####
            try {//####[61]####
                __pt__runBM_0_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_0", new Class[] {//####[61]####
                    Benchmark.class//####[61]####
                });//####[61]####
            } catch (Exception e) {//####[61]####
                e.printStackTrace();//####[61]####
            }//####[61]####
        }//####[61]####
    }//####[61]####
    private static TaskID<Void> runBM_0(Benchmark benchmark) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return runBM_0(benchmark, new TaskInfo());//####[61]####
    }//####[61]####
    private static TaskID<Void> runBM_0(Benchmark benchmark, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[61]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setParameters(benchmark);//####[61]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private static TaskID<Void> runBM_0(TaskID<Benchmark> benchmark) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return runBM_0(benchmark, new TaskInfo());//####[61]####
    }//####[61]####
    private static TaskID<Void> runBM_0(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[61]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setTaskIdArgIndexes(0);//####[61]####
        taskinfo.addDependsOn(benchmark);//####[61]####
        taskinfo.setParameters(benchmark);//####[61]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private static TaskID<Void> runBM_0(BlockingQueue<Benchmark> benchmark) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return runBM_0(benchmark, new TaskInfo());//####[61]####
    }//####[61]####
    private static TaskID<Void> runBM_0(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[61]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setQueueArgIndexes(0);//####[61]####
        taskinfo.setIsPipeline(true);//####[61]####
        taskinfo.setParameters(benchmark);//####[61]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    public static void __pt__runBM_0(Benchmark benchmark) {//####[61]####
        try {//####[62]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[63]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_0 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[64]####
        } catch (IllegalAccessException e) {//####[67]####
            e.printStackTrace();//####[68]####
        } catch (IllegalArgumentException e) {//####[69]####
            e.printStackTrace();//####[70]####
        } catch (InvocationTargetException e) {//####[71]####
            e.printStackTrace();//####[72]####
        }//####[73]####
        TaskID tid_0_1 = runBM_0_1(createBenchmark());//####[75]####
        TaskIDGroup tig_0_1 = new TaskIDGroup(1);//####[76]####
        tig_0_1.add(tid_0_1);//####[77]####
        try {//####[79]####
            tig_0_1.waitTillFinished();//####[80]####
        } catch (ExecutionException e) {//####[81]####
            e.printStackTrace();//####[82]####
        } catch (InterruptedException e) {//####[83]####
            e.printStackTrace();//####[84]####
        }//####[85]####
        TaskID tid_0_2 = runBM_0_2(createBenchmark());//####[87]####
        TaskIDGroup tig_0_2 = new TaskIDGroup(1);//####[88]####
        tig_0_2.add(tid_0_2);//####[89]####
        try {//####[91]####
            tig_0_2.waitTillFinished();//####[92]####
        } catch (ExecutionException e) {//####[93]####
            e.printStackTrace();//####[94]####
        } catch (InterruptedException e) {//####[95]####
            e.printStackTrace();//####[96]####
        }//####[97]####
    }//####[99]####
//####[99]####
//####[101]####
    private static volatile Method __pt__runBM_0_1_Benchmark_method = null;//####[101]####
    private synchronized static void __pt__runBM_0_1_Benchmark_ensureMethodVarSet() {//####[101]####
        if (__pt__runBM_0_1_Benchmark_method == null) {//####[101]####
            try {//####[101]####
                __pt__runBM_0_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_0_1", new Class[] {//####[101]####
                    Benchmark.class//####[101]####
                });//####[101]####
            } catch (Exception e) {//####[101]####
                e.printStackTrace();//####[101]####
            }//####[101]####
        }//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_0_1(Benchmark benchmark) {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return runBM_0_1(benchmark, new TaskInfo());//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_0_1(Benchmark benchmark, TaskInfo taskinfo) {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__runBM_0_1_Benchmark_method == null) {//####[101]####
            __pt__runBM_0_1_Benchmark_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setParameters(benchmark);//####[101]####
        taskinfo.setMethod(__pt__runBM_0_1_Benchmark_method);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_0_1(TaskID<Benchmark> benchmark) {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return runBM_0_1(benchmark, new TaskInfo());//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_0_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__runBM_0_1_Benchmark_method == null) {//####[101]####
            __pt__runBM_0_1_Benchmark_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setTaskIdArgIndexes(0);//####[101]####
        taskinfo.addDependsOn(benchmark);//####[101]####
        taskinfo.setParameters(benchmark);//####[101]####
        taskinfo.setMethod(__pt__runBM_0_1_Benchmark_method);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_0_1(BlockingQueue<Benchmark> benchmark) {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return runBM_0_1(benchmark, new TaskInfo());//####[101]####
    }//####[101]####
    private static TaskID<Void> runBM_0_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__runBM_0_1_Benchmark_method == null) {//####[101]####
            __pt__runBM_0_1_Benchmark_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setQueueArgIndexes(0);//####[101]####
        taskinfo.setIsPipeline(true);//####[101]####
        taskinfo.setParameters(benchmark);//####[101]####
        taskinfo.setMethod(__pt__runBM_0_1_Benchmark_method);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[101]####
    }//####[101]####
    public static void __pt__runBM_0_1(Benchmark benchmark) {//####[101]####
        try {//####[102]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[103]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_0_1 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[104]####
        } catch (IllegalAccessException e) {//####[107]####
            e.printStackTrace();//####[108]####
        } catch (IllegalArgumentException e) {//####[109]####
            e.printStackTrace();//####[110]####
        } catch (InvocationTargetException e) {//####[111]####
            e.printStackTrace();//####[112]####
        }//####[113]####
    }//####[114]####
//####[114]####
//####[116]####
    private static volatile Method __pt__runBM_0_2_Benchmark_method = null;//####[116]####
    private synchronized static void __pt__runBM_0_2_Benchmark_ensureMethodVarSet() {//####[116]####
        if (__pt__runBM_0_2_Benchmark_method == null) {//####[116]####
            try {//####[116]####
                __pt__runBM_0_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_0_2", new Class[] {//####[116]####
                    Benchmark.class//####[116]####
                });//####[116]####
            } catch (Exception e) {//####[116]####
                e.printStackTrace();//####[116]####
            }//####[116]####
        }//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM_0_2(Benchmark benchmark) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return runBM_0_2(benchmark, new TaskInfo());//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM_0_2(Benchmark benchmark, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__runBM_0_2_Benchmark_method == null) {//####[116]####
            __pt__runBM_0_2_Benchmark_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setParameters(benchmark);//####[116]####
        taskinfo.setMethod(__pt__runBM_0_2_Benchmark_method);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM_0_2(TaskID<Benchmark> benchmark) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return runBM_0_2(benchmark, new TaskInfo());//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM_0_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__runBM_0_2_Benchmark_method == null) {//####[116]####
            __pt__runBM_0_2_Benchmark_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setTaskIdArgIndexes(0);//####[116]####
        taskinfo.addDependsOn(benchmark);//####[116]####
        taskinfo.setParameters(benchmark);//####[116]####
        taskinfo.setMethod(__pt__runBM_0_2_Benchmark_method);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM_0_2(BlockingQueue<Benchmark> benchmark) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return runBM_0_2(benchmark, new TaskInfo());//####[116]####
    }//####[116]####
    private static TaskID<Void> runBM_0_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__runBM_0_2_Benchmark_method == null) {//####[116]####
            __pt__runBM_0_2_Benchmark_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setQueueArgIndexes(0);//####[116]####
        taskinfo.setIsPipeline(true);//####[116]####
        taskinfo.setParameters(benchmark);//####[116]####
        taskinfo.setMethod(__pt__runBM_0_2_Benchmark_method);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    public static void __pt__runBM_0_2(Benchmark benchmark) {//####[116]####
        try {//####[117]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[118]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_0_2 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[119]####
        } catch (IllegalAccessException e) {//####[122]####
            e.printStackTrace();//####[123]####
        } catch (IllegalArgumentException e) {//####[124]####
            e.printStackTrace();//####[125]####
        } catch (InvocationTargetException e) {//####[126]####
            e.printStackTrace();//####[127]####
        }//####[128]####
    }//####[129]####
//####[129]####
//####[131]####
    private static volatile Method __pt__runBM_1_Benchmark_method = null;//####[131]####
    private synchronized static void __pt__runBM_1_Benchmark_ensureMethodVarSet() {//####[131]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[131]####
            try {//####[131]####
                __pt__runBM_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1", new Class[] {//####[131]####
                    Benchmark.class//####[131]####
                });//####[131]####
            } catch (Exception e) {//####[131]####
                e.printStackTrace();//####[131]####
            }//####[131]####
        }//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_1(Benchmark benchmark) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return runBM_1(benchmark, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_1(Benchmark benchmark, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[131]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setParameters(benchmark);//####[131]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return runBM_1(benchmark, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[131]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(0);//####[131]####
        taskinfo.addDependsOn(benchmark);//####[131]####
        taskinfo.setParameters(benchmark);//####[131]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return runBM_1(benchmark, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[131]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(benchmark);//####[131]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    public static void __pt__runBM_1(Benchmark benchmark) {//####[131]####
        try {//####[132]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[133]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_1 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[134]####
        } catch (IllegalAccessException e) {//####[137]####
            e.printStackTrace();//####[138]####
        } catch (IllegalArgumentException e) {//####[139]####
            e.printStackTrace();//####[140]####
        } catch (InvocationTargetException e) {//####[141]####
            e.printStackTrace();//####[142]####
        }//####[143]####
        TaskID tid_1_1 = runBM_1_1(createBenchmark());//####[145]####
        TaskIDGroup tig_1_1 = new TaskIDGroup(1);//####[146]####
        tig_1_1.add(tid_1_1);//####[147]####
        try {//####[149]####
            tig_1_1.waitTillFinished();//####[150]####
        } catch (ExecutionException e) {//####[151]####
            e.printStackTrace();//####[152]####
        } catch (InterruptedException e) {//####[153]####
            e.printStackTrace();//####[154]####
        }//####[155]####
        TaskID tid_1_2 = runBM_1_2(createBenchmark());//####[157]####
        TaskIDGroup tig_1_2 = new TaskIDGroup(1);//####[158]####
        tig_1_2.add(tid_1_2);//####[159]####
        try {//####[161]####
            tig_1_2.waitTillFinished();//####[162]####
        } catch (ExecutionException e) {//####[163]####
            e.printStackTrace();//####[164]####
        } catch (InterruptedException e) {//####[165]####
            e.printStackTrace();//####[166]####
        }//####[167]####
    }//####[169]####
//####[169]####
//####[171]####
    private static volatile Method __pt__runBM_1_1_Benchmark_method = null;//####[171]####
    private synchronized static void __pt__runBM_1_1_Benchmark_ensureMethodVarSet() {//####[171]####
        if (__pt__runBM_1_1_Benchmark_method == null) {//####[171]####
            try {//####[171]####
                __pt__runBM_1_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1_1", new Class[] {//####[171]####
                    Benchmark.class//####[171]####
                });//####[171]####
            } catch (Exception e) {//####[171]####
                e.printStackTrace();//####[171]####
            }//####[171]####
        }//####[171]####
    }//####[171]####
    private static TaskID<Void> runBM_1_1(Benchmark benchmark) {//####[171]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[171]####
        return runBM_1_1(benchmark, new TaskInfo());//####[171]####
    }//####[171]####
    private static TaskID<Void> runBM_1_1(Benchmark benchmark, TaskInfo taskinfo) {//####[171]####
        // ensure Method variable is set//####[171]####
        if (__pt__runBM_1_1_Benchmark_method == null) {//####[171]####
            __pt__runBM_1_1_Benchmark_ensureMethodVarSet();//####[171]####
        }//####[171]####
        taskinfo.setParameters(benchmark);//####[171]####
        taskinfo.setMethod(__pt__runBM_1_1_Benchmark_method);//####[171]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[171]####
    }//####[171]####
    private static TaskID<Void> runBM_1_1(TaskID<Benchmark> benchmark) {//####[171]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[171]####
        return runBM_1_1(benchmark, new TaskInfo());//####[171]####
    }//####[171]####
    private static TaskID<Void> runBM_1_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[171]####
        // ensure Method variable is set//####[171]####
        if (__pt__runBM_1_1_Benchmark_method == null) {//####[171]####
            __pt__runBM_1_1_Benchmark_ensureMethodVarSet();//####[171]####
        }//####[171]####
        taskinfo.setTaskIdArgIndexes(0);//####[171]####
        taskinfo.addDependsOn(benchmark);//####[171]####
        taskinfo.setParameters(benchmark);//####[171]####
        taskinfo.setMethod(__pt__runBM_1_1_Benchmark_method);//####[171]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[171]####
    }//####[171]####
    private static TaskID<Void> runBM_1_1(BlockingQueue<Benchmark> benchmark) {//####[171]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[171]####
        return runBM_1_1(benchmark, new TaskInfo());//####[171]####
    }//####[171]####
    private static TaskID<Void> runBM_1_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[171]####
        // ensure Method variable is set//####[171]####
        if (__pt__runBM_1_1_Benchmark_method == null) {//####[171]####
            __pt__runBM_1_1_Benchmark_ensureMethodVarSet();//####[171]####
        }//####[171]####
        taskinfo.setQueueArgIndexes(0);//####[171]####
        taskinfo.setIsPipeline(true);//####[171]####
        taskinfo.setParameters(benchmark);//####[171]####
        taskinfo.setMethod(__pt__runBM_1_1_Benchmark_method);//####[171]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[171]####
    }//####[171]####
    public static void __pt__runBM_1_1(Benchmark benchmark) {//####[171]####
        try {//####[172]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[173]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_1_1 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[174]####
        } catch (IllegalAccessException e) {//####[177]####
            e.printStackTrace();//####[178]####
        } catch (IllegalArgumentException e) {//####[179]####
            e.printStackTrace();//####[180]####
        } catch (InvocationTargetException e) {//####[181]####
            e.printStackTrace();//####[182]####
        }//####[183]####
    }//####[184]####
//####[184]####
//####[186]####
    private static volatile Method __pt__runBM_1_2_Benchmark_method = null;//####[186]####
    private synchronized static void __pt__runBM_1_2_Benchmark_ensureMethodVarSet() {//####[186]####
        if (__pt__runBM_1_2_Benchmark_method == null) {//####[186]####
            try {//####[186]####
                __pt__runBM_1_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1_2", new Class[] {//####[186]####
                    Benchmark.class//####[186]####
                });//####[186]####
            } catch (Exception e) {//####[186]####
                e.printStackTrace();//####[186]####
            }//####[186]####
        }//####[186]####
    }//####[186]####
    private static TaskID<Void> runBM_1_2(Benchmark benchmark) {//####[186]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[186]####
        return runBM_1_2(benchmark, new TaskInfo());//####[186]####
    }//####[186]####
    private static TaskID<Void> runBM_1_2(Benchmark benchmark, TaskInfo taskinfo) {//####[186]####
        // ensure Method variable is set//####[186]####
        if (__pt__runBM_1_2_Benchmark_method == null) {//####[186]####
            __pt__runBM_1_2_Benchmark_ensureMethodVarSet();//####[186]####
        }//####[186]####
        taskinfo.setParameters(benchmark);//####[186]####
        taskinfo.setMethod(__pt__runBM_1_2_Benchmark_method);//####[186]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[186]####
    }//####[186]####
    private static TaskID<Void> runBM_1_2(TaskID<Benchmark> benchmark) {//####[186]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[186]####
        return runBM_1_2(benchmark, new TaskInfo());//####[186]####
    }//####[186]####
    private static TaskID<Void> runBM_1_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[186]####
        // ensure Method variable is set//####[186]####
        if (__pt__runBM_1_2_Benchmark_method == null) {//####[186]####
            __pt__runBM_1_2_Benchmark_ensureMethodVarSet();//####[186]####
        }//####[186]####
        taskinfo.setTaskIdArgIndexes(0);//####[186]####
        taskinfo.addDependsOn(benchmark);//####[186]####
        taskinfo.setParameters(benchmark);//####[186]####
        taskinfo.setMethod(__pt__runBM_1_2_Benchmark_method);//####[186]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[186]####
    }//####[186]####
    private static TaskID<Void> runBM_1_2(BlockingQueue<Benchmark> benchmark) {//####[186]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[186]####
        return runBM_1_2(benchmark, new TaskInfo());//####[186]####
    }//####[186]####
    private static TaskID<Void> runBM_1_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[186]####
        // ensure Method variable is set//####[186]####
        if (__pt__runBM_1_2_Benchmark_method == null) {//####[186]####
            __pt__runBM_1_2_Benchmark_ensureMethodVarSet();//####[186]####
        }//####[186]####
        taskinfo.setQueueArgIndexes(0);//####[186]####
        taskinfo.setIsPipeline(true);//####[186]####
        taskinfo.setParameters(benchmark);//####[186]####
        taskinfo.setMethod(__pt__runBM_1_2_Benchmark_method);//####[186]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[186]####
    }//####[186]####
    public static void __pt__runBM_1_2(Benchmark benchmark) {//####[186]####
        try {//####[187]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[188]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_1_2 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[189]####
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
    private static volatile Method __pt__runBM_2_Benchmark_method = null;//####[201]####
    private synchronized static void __pt__runBM_2_Benchmark_ensureMethodVarSet() {//####[201]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[201]####
            try {//####[201]####
                __pt__runBM_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[201]####
                    Benchmark.class//####[201]####
                });//####[201]####
            } catch (Exception e) {//####[201]####
                e.printStackTrace();//####[201]####
            }//####[201]####
        }//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_2(Benchmark benchmark) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBM_2(benchmark, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_2(Benchmark benchmark, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[201]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setParameters(benchmark);//####[201]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBM_2(benchmark, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[201]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setTaskIdArgIndexes(0);//####[201]####
        taskinfo.addDependsOn(benchmark);//####[201]####
        taskinfo.setParameters(benchmark);//####[201]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBM_2(benchmark, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[201]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setQueueArgIndexes(0);//####[201]####
        taskinfo.setIsPipeline(true);//####[201]####
        taskinfo.setParameters(benchmark);//####[201]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[201]####
    }//####[201]####
    public static void __pt__runBM_2(Benchmark benchmark) {//####[201]####
        try {//####[202]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[203]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_2 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[204]####
        } catch (IllegalAccessException e) {//####[207]####
            e.printStackTrace();//####[208]####
        } catch (IllegalArgumentException e) {//####[209]####
            e.printStackTrace();//####[210]####
        } catch (InvocationTargetException e) {//####[211]####
            e.printStackTrace();//####[212]####
        }//####[213]####
        TaskID tid_2_1 = runBM_2_1(createBenchmark());//####[215]####
        TaskIDGroup tig_2_1 = new TaskIDGroup(1);//####[216]####
        tig_2_1.add(tid_2_1);//####[217]####
        try {//####[219]####
            Thread.sleep(1000 * 5);//####[220]####
        } catch (InterruptedException e) {//####[221]####
            e.printStackTrace();//####[222]####
        }//####[223]####
        try {//####[225]####
            tig_2_1.waitTillFinished();//####[226]####
        } catch (ExecutionException e) {//####[227]####
            e.printStackTrace();//####[228]####
        } catch (InterruptedException e) {//####[229]####
            e.printStackTrace();//####[230]####
        }//####[231]####
        TaskID tid_2_2 = runBM_2_2(createBenchmark());//####[233]####
        TaskIDGroup tig_2_2 = new TaskIDGroup(1);//####[234]####
        tig_2_2.add(tid_2_2);//####[235]####
        try {//####[237]####
            Thread.sleep(1000 * 5);//####[238]####
        } catch (InterruptedException e) {//####[239]####
            e.printStackTrace();//####[240]####
        }//####[241]####
        try {//####[243]####
            tig_2_2.waitTillFinished();//####[244]####
        } catch (ExecutionException e) {//####[245]####
            e.printStackTrace();//####[246]####
        } catch (InterruptedException e) {//####[247]####
            e.printStackTrace();//####[248]####
        }//####[249]####
    }//####[251]####
//####[251]####
//####[253]####
    private static volatile Method __pt__runBM_2_1_Benchmark_method = null;//####[253]####
    private synchronized static void __pt__runBM_2_1_Benchmark_ensureMethodVarSet() {//####[253]####
        if (__pt__runBM_2_1_Benchmark_method == null) {//####[253]####
            try {//####[253]####
                __pt__runBM_2_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2_1", new Class[] {//####[253]####
                    Benchmark.class//####[253]####
                });//####[253]####
            } catch (Exception e) {//####[253]####
                e.printStackTrace();//####[253]####
            }//####[253]####
        }//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_2_1(Benchmark benchmark) {//####[253]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[253]####
        return runBM_2_1(benchmark, new TaskInfo());//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_2_1(Benchmark benchmark, TaskInfo taskinfo) {//####[253]####
        // ensure Method variable is set//####[253]####
        if (__pt__runBM_2_1_Benchmark_method == null) {//####[253]####
            __pt__runBM_2_1_Benchmark_ensureMethodVarSet();//####[253]####
        }//####[253]####
        taskinfo.setParameters(benchmark);//####[253]####
        taskinfo.setMethod(__pt__runBM_2_1_Benchmark_method);//####[253]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_2_1(TaskID<Benchmark> benchmark) {//####[253]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[253]####
        return runBM_2_1(benchmark, new TaskInfo());//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_2_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[253]####
        // ensure Method variable is set//####[253]####
        if (__pt__runBM_2_1_Benchmark_method == null) {//####[253]####
            __pt__runBM_2_1_Benchmark_ensureMethodVarSet();//####[253]####
        }//####[253]####
        taskinfo.setTaskIdArgIndexes(0);//####[253]####
        taskinfo.addDependsOn(benchmark);//####[253]####
        taskinfo.setParameters(benchmark);//####[253]####
        taskinfo.setMethod(__pt__runBM_2_1_Benchmark_method);//####[253]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_2_1(BlockingQueue<Benchmark> benchmark) {//####[253]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[253]####
        return runBM_2_1(benchmark, new TaskInfo());//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_2_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[253]####
        // ensure Method variable is set//####[253]####
        if (__pt__runBM_2_1_Benchmark_method == null) {//####[253]####
            __pt__runBM_2_1_Benchmark_ensureMethodVarSet();//####[253]####
        }//####[253]####
        taskinfo.setQueueArgIndexes(0);//####[253]####
        taskinfo.setIsPipeline(true);//####[253]####
        taskinfo.setParameters(benchmark);//####[253]####
        taskinfo.setMethod(__pt__runBM_2_1_Benchmark_method);//####[253]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[253]####
    }//####[253]####
    public static void __pt__runBM_2_1(Benchmark benchmark) {//####[253]####
        try {//####[254]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[255]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_2_1 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[256]####
        } catch (IllegalAccessException e) {//####[259]####
            e.printStackTrace();//####[260]####
        } catch (IllegalArgumentException e) {//####[261]####
            e.printStackTrace();//####[262]####
        } catch (InvocationTargetException e) {//####[263]####
            e.printStackTrace();//####[264]####
        }//####[265]####
    }//####[266]####
//####[266]####
//####[268]####
    private static volatile Method __pt__runBM_2_2_Benchmark_method = null;//####[268]####
    private synchronized static void __pt__runBM_2_2_Benchmark_ensureMethodVarSet() {//####[268]####
        if (__pt__runBM_2_2_Benchmark_method == null) {//####[268]####
            try {//####[268]####
                __pt__runBM_2_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2_2", new Class[] {//####[268]####
                    Benchmark.class//####[268]####
                });//####[268]####
            } catch (Exception e) {//####[268]####
                e.printStackTrace();//####[268]####
            }//####[268]####
        }//####[268]####
    }//####[268]####
    private static TaskID<Void> runBM_2_2(Benchmark benchmark) {//####[268]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[268]####
        return runBM_2_2(benchmark, new TaskInfo());//####[268]####
    }//####[268]####
    private static TaskID<Void> runBM_2_2(Benchmark benchmark, TaskInfo taskinfo) {//####[268]####
        // ensure Method variable is set//####[268]####
        if (__pt__runBM_2_2_Benchmark_method == null) {//####[268]####
            __pt__runBM_2_2_Benchmark_ensureMethodVarSet();//####[268]####
        }//####[268]####
        taskinfo.setParameters(benchmark);//####[268]####
        taskinfo.setMethod(__pt__runBM_2_2_Benchmark_method);//####[268]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[268]####
    }//####[268]####
    private static TaskID<Void> runBM_2_2(TaskID<Benchmark> benchmark) {//####[268]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[268]####
        return runBM_2_2(benchmark, new TaskInfo());//####[268]####
    }//####[268]####
    private static TaskID<Void> runBM_2_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[268]####
        // ensure Method variable is set//####[268]####
        if (__pt__runBM_2_2_Benchmark_method == null) {//####[268]####
            __pt__runBM_2_2_Benchmark_ensureMethodVarSet();//####[268]####
        }//####[268]####
        taskinfo.setTaskIdArgIndexes(0);//####[268]####
        taskinfo.addDependsOn(benchmark);//####[268]####
        taskinfo.setParameters(benchmark);//####[268]####
        taskinfo.setMethod(__pt__runBM_2_2_Benchmark_method);//####[268]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[268]####
    }//####[268]####
    private static TaskID<Void> runBM_2_2(BlockingQueue<Benchmark> benchmark) {//####[268]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[268]####
        return runBM_2_2(benchmark, new TaskInfo());//####[268]####
    }//####[268]####
    private static TaskID<Void> runBM_2_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[268]####
        // ensure Method variable is set//####[268]####
        if (__pt__runBM_2_2_Benchmark_method == null) {//####[268]####
            __pt__runBM_2_2_Benchmark_ensureMethodVarSet();//####[268]####
        }//####[268]####
        taskinfo.setQueueArgIndexes(0);//####[268]####
        taskinfo.setIsPipeline(true);//####[268]####
        taskinfo.setParameters(benchmark);//####[268]####
        taskinfo.setMethod(__pt__runBM_2_2_Benchmark_method);//####[268]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[268]####
    }//####[268]####
    public static void __pt__runBM_2_2(Benchmark benchmark) {//####[268]####
        try {//####[269]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[270]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_2_2 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[271]####
        } catch (IllegalAccessException e) {//####[274]####
            e.printStackTrace();//####[275]####
        } catch (IllegalArgumentException e) {//####[276]####
            e.printStackTrace();//####[277]####
        } catch (InvocationTargetException e) {//####[278]####
            e.printStackTrace();//####[279]####
        }//####[280]####
    }//####[281]####
//####[281]####
//####[347]####
    private static Benchmark createBenchmark() {//####[347]####
        Object benchmark = null;//####[348]####
        Method method = null;//####[349]####
        try {//####[350]####
            benchmark = bmClass.newInstance();//####[351]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[352]####
        } catch (InstantiationException e) {//####[353]####
            e.printStackTrace();//####[354]####
        } catch (IllegalAccessException e) {//####[355]####
            e.printStackTrace();//####[356]####
        } catch (NoSuchMethodException e) {//####[357]####
            e.printStackTrace();//####[358]####
        } catch (SecurityException e) {//####[359]####
            e.printStackTrace();//####[360]####
        } catch (IllegalArgumentException e) {//####[361]####
            e.printStackTrace();//####[362]####
        }//####[363]####
        Object[] arguments = new Object[1];//####[364]####
        arguments[0] = N_DATASIZE;//####[365]####
        return new Benchmark(benchmark, method, arguments);//####[367]####
    }//####[368]####
//####[370]####
    private static void getBenchmarkClass(String bmName) {//####[370]####
        try {//####[371]####
            if (bmName.equalsIgnoreCase(MOL)) //####[372]####
            {//####[372]####
                bmClass = Class.forName(MOL_CLASS);//####[373]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[374]####
            {//####[374]####
                bmClass = Class.forName(MON_CLASS);//####[375]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[376]####
            {//####[376]####
                bmClass = Class.forName(RAY_CLASS);//####[377]####
            } else {//####[378]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[379]####
            }//####[380]####
        } catch (ClassNotFoundException e) {//####[381]####
            e.printStackTrace();//####[382]####
        } catch (Exception e) {//####[383]####
            e.printStackTrace();//####[384]####
        }//####[385]####
    }//####[386]####
}//####[386]####
