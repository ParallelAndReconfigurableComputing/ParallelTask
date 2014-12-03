package pt.test;//####[1]####
//####[1]####
import java.util.Date;//####[3]####
//####[3]####
//-- ParaTask related imports//####[3]####
import pt.runtime.*;//####[3]####
import java.util.concurrent.ExecutionException;//####[3]####
import java.util.concurrent.locks.*;//####[3]####
import java.lang.reflect.*;//####[3]####
import pt.runtime.GuiThread;//####[3]####
import java.util.concurrent.BlockingQueue;//####[3]####
import java.util.ArrayList;//####[3]####
import java.util.List;//####[3]####
//####[3]####
public class TestNestedTask {//####[5]####
    static{ParaTask.init();}//####[5]####
    /*  ParaTask helper method to access private/protected slots *///####[5]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[5]####
        if (m.getParameterTypes().length == 0)//####[5]####
            m.invoke(instance);//####[5]####
        else if ((m.getParameterTypes().length == 1))//####[5]####
            m.invoke(instance, arg);//####[5]####
        else //####[5]####
            m.invoke(instance, arg, interResult);//####[5]####
    }//####[5]####
//####[6]####
    private static final int N_DATASIZE = 0;//####[6]####
//####[8]####
    private static final String BM_METHOD = "execute";//####[8]####
//####[10]####
    private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = { int.class };//####[10]####
//####[12]####
    private static final String MOL = "MOL";//####[12]####
//####[14]####
    private static final String MOL_CLASS = "core.moldyn.Molcore";//####[14]####
//####[16]####
    private static final String MON = "MON";//####[16]####
//####[18]####
    private static final String MON_CLASS = "core.montecarlo.Moncore";//####[18]####
//####[20]####
    private static final String RAY = "RAY";//####[20]####
//####[22]####
    private static final String RAY_CLASS = "core.raytracer.Raycore";//####[22]####
//####[24]####
    private static Class<?> bmClass;//####[24]####
//####[27]####
    public static void main(String[] args) {//####[27]####
        if (null == args || args.length != 1) //####[28]####
        {//####[28]####
            try {//####[29]####
                throw new Exception("Wrong arguemnts setting");//####[30]####
            } catch (Exception e) {//####[31]####
                e.printStackTrace();//####[32]####
            }//####[33]####
        }//####[34]####
        getBenchmarkClass(args[0]);//####[36]####
        TaskID tid_0 = runBM_0(createBenchmark());//####[38]####
        TaskID tid_1 = runBM_1(createBenchmark());//####[39]####
        TaskID tid_2 = runBM_2(createBenchmark());//####[40]####
        TaskIDGroup tig = new TaskIDGroup(3);//####[43]####
        tig.add(tid_0);//####[45]####
        tig.add(tid_1);//####[46]####
        tig.add(tid_2);//####[47]####
        try {//####[50]####
            tig.waitTillFinished();//####[51]####
        } catch (ExecutionException e) {//####[52]####
            e.printStackTrace();//####[53]####
        } catch (InterruptedException e) {//####[54]####
            e.printStackTrace();//####[55]####
        }//####[56]####
    }//####[57]####
//####[59]####
    private static volatile Method __pt__runBM_0_Benchmark_method = null;//####[59]####
    private synchronized static void __pt__runBM_0_Benchmark_ensureMethodVarSet() {//####[59]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[59]####
            try {//####[59]####
                __pt__runBM_0_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_0", new Class[] {//####[59]####
                    Benchmark.class//####[59]####
                });//####[59]####
            } catch (Exception e) {//####[59]####
                e.printStackTrace();//####[59]####
            }//####[59]####
        }//####[59]####
    }//####[59]####
    private static TaskID<Void> runBM_0(Benchmark benchmark) {//####[59]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[59]####
        return runBM_0(benchmark, new TaskInfo());//####[59]####
    }//####[59]####
    private static TaskID<Void> runBM_0(Benchmark benchmark, TaskInfo taskinfo) {//####[59]####
        // ensure Method variable is set//####[59]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[59]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[59]####
        }//####[59]####
        taskinfo.setParameters(benchmark);//####[59]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[59]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[59]####
    }//####[59]####
    private static TaskID<Void> runBM_0(TaskID<Benchmark> benchmark) {//####[59]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[59]####
        return runBM_0(benchmark, new TaskInfo());//####[59]####
    }//####[59]####
    private static TaskID<Void> runBM_0(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[59]####
        // ensure Method variable is set//####[59]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[59]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[59]####
        }//####[59]####
        taskinfo.setTaskIdArgIndexes(0);//####[59]####
        taskinfo.addDependsOn(benchmark);//####[59]####
        taskinfo.setParameters(benchmark);//####[59]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[59]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[59]####
    }//####[59]####
    private static TaskID<Void> runBM_0(BlockingQueue<Benchmark> benchmark) {//####[59]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[59]####
        return runBM_0(benchmark, new TaskInfo());//####[59]####
    }//####[59]####
    private static TaskID<Void> runBM_0(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[59]####
        // ensure Method variable is set//####[59]####
        if (__pt__runBM_0_Benchmark_method == null) {//####[59]####
            __pt__runBM_0_Benchmark_ensureMethodVarSet();//####[59]####
        }//####[59]####
        taskinfo.setQueueArgIndexes(0);//####[59]####
        taskinfo.setIsPipeline(true);//####[59]####
        taskinfo.setParameters(benchmark);//####[59]####
        taskinfo.setMethod(__pt__runBM_0_Benchmark_method);//####[59]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[59]####
    }//####[59]####
    public static void __pt__runBM_0(Benchmark benchmark) {//####[59]####
        try {//####[60]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[61]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_0 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[62]####
        } catch (IllegalAccessException e) {//####[65]####
            e.printStackTrace();//####[66]####
        } catch (IllegalArgumentException e) {//####[67]####
            e.printStackTrace();//####[68]####
        } catch (InvocationTargetException e) {//####[69]####
            e.printStackTrace();//####[70]####
        }//####[71]####
        TaskID tid_0_1 = runBM_0_1(createBenchmark());//####[73]####
        TaskIDGroup tig_0_1 = new TaskIDGroup(1);//####[74]####
        tig_0_1.add(tid_0_1);//####[75]####
        try {//####[77]####
            tig_0_1.waitTillFinished();//####[78]####
        } catch (ExecutionException e) {//####[79]####
            e.printStackTrace();//####[80]####
        } catch (InterruptedException e) {//####[81]####
            e.printStackTrace();//####[82]####
        }//####[83]####
        TaskID tid_0_2 = runBM_0_2(createBenchmark());//####[85]####
        TaskIDGroup tig_0_2 = new TaskIDGroup(1);//####[86]####
        tig_0_2.add(tid_0_2);//####[87]####
        try {//####[89]####
            tig_0_2.waitTillFinished();//####[90]####
        } catch (ExecutionException e) {//####[91]####
            e.printStackTrace();//####[92]####
        } catch (InterruptedException e) {//####[93]####
            e.printStackTrace();//####[94]####
        }//####[95]####
    }//####[97]####
//####[97]####
//####[99]####
    private static volatile Method __pt__runBM_0_1_Benchmark_method = null;//####[99]####
    private synchronized static void __pt__runBM_0_1_Benchmark_ensureMethodVarSet() {//####[99]####
        if (__pt__runBM_0_1_Benchmark_method == null) {//####[99]####
            try {//####[99]####
                __pt__runBM_0_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_0_1", new Class[] {//####[99]####
                    Benchmark.class//####[99]####
                });//####[99]####
            } catch (Exception e) {//####[99]####
                e.printStackTrace();//####[99]####
            }//####[99]####
        }//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_0_1(Benchmark benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM_0_1(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_0_1(Benchmark benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_0_1_Benchmark_method == null) {//####[99]####
            __pt__runBM_0_1_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_0_1_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_0_1(TaskID<Benchmark> benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM_0_1(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_0_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_0_1_Benchmark_method == null) {//####[99]####
            __pt__runBM_0_1_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setTaskIdArgIndexes(0);//####[99]####
        taskinfo.addDependsOn(benchmark);//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_0_1_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_0_1(BlockingQueue<Benchmark> benchmark) {//####[99]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[99]####
        return runBM_0_1(benchmark, new TaskInfo());//####[99]####
    }//####[99]####
    private static TaskID<Void> runBM_0_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[99]####
        // ensure Method variable is set//####[99]####
        if (__pt__runBM_0_1_Benchmark_method == null) {//####[99]####
            __pt__runBM_0_1_Benchmark_ensureMethodVarSet();//####[99]####
        }//####[99]####
        taskinfo.setQueueArgIndexes(0);//####[99]####
        taskinfo.setIsPipeline(true);//####[99]####
        taskinfo.setParameters(benchmark);//####[99]####
        taskinfo.setMethod(__pt__runBM_0_1_Benchmark_method);//####[99]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[99]####
    }//####[99]####
    public static void __pt__runBM_0_1(Benchmark benchmark) {//####[99]####
        try {//####[100]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[101]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_0_1 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[102]####
        } catch (IllegalAccessException e) {//####[105]####
            e.printStackTrace();//####[106]####
        } catch (IllegalArgumentException e) {//####[107]####
            e.printStackTrace();//####[108]####
        } catch (InvocationTargetException e) {//####[109]####
            e.printStackTrace();//####[110]####
        }//####[111]####
    }//####[112]####
//####[112]####
//####[114]####
    private static volatile Method __pt__runBM_0_2_Benchmark_method = null;//####[114]####
    private synchronized static void __pt__runBM_0_2_Benchmark_ensureMethodVarSet() {//####[114]####
        if (__pt__runBM_0_2_Benchmark_method == null) {//####[114]####
            try {//####[114]####
                __pt__runBM_0_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_0_2", new Class[] {//####[114]####
                    Benchmark.class//####[114]####
                });//####[114]####
            } catch (Exception e) {//####[114]####
                e.printStackTrace();//####[114]####
            }//####[114]####
        }//####[114]####
    }//####[114]####
    private static TaskID<Void> runBM_0_2(Benchmark benchmark) {//####[114]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[114]####
        return runBM_0_2(benchmark, new TaskInfo());//####[114]####
    }//####[114]####
    private static TaskID<Void> runBM_0_2(Benchmark benchmark, TaskInfo taskinfo) {//####[114]####
        // ensure Method variable is set//####[114]####
        if (__pt__runBM_0_2_Benchmark_method == null) {//####[114]####
            __pt__runBM_0_2_Benchmark_ensureMethodVarSet();//####[114]####
        }//####[114]####
        taskinfo.setParameters(benchmark);//####[114]####
        taskinfo.setMethod(__pt__runBM_0_2_Benchmark_method);//####[114]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[114]####
    }//####[114]####
    private static TaskID<Void> runBM_0_2(TaskID<Benchmark> benchmark) {//####[114]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[114]####
        return runBM_0_2(benchmark, new TaskInfo());//####[114]####
    }//####[114]####
    private static TaskID<Void> runBM_0_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[114]####
        // ensure Method variable is set//####[114]####
        if (__pt__runBM_0_2_Benchmark_method == null) {//####[114]####
            __pt__runBM_0_2_Benchmark_ensureMethodVarSet();//####[114]####
        }//####[114]####
        taskinfo.setTaskIdArgIndexes(0);//####[114]####
        taskinfo.addDependsOn(benchmark);//####[114]####
        taskinfo.setParameters(benchmark);//####[114]####
        taskinfo.setMethod(__pt__runBM_0_2_Benchmark_method);//####[114]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[114]####
    }//####[114]####
    private static TaskID<Void> runBM_0_2(BlockingQueue<Benchmark> benchmark) {//####[114]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[114]####
        return runBM_0_2(benchmark, new TaskInfo());//####[114]####
    }//####[114]####
    private static TaskID<Void> runBM_0_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[114]####
        // ensure Method variable is set//####[114]####
        if (__pt__runBM_0_2_Benchmark_method == null) {//####[114]####
            __pt__runBM_0_2_Benchmark_ensureMethodVarSet();//####[114]####
        }//####[114]####
        taskinfo.setQueueArgIndexes(0);//####[114]####
        taskinfo.setIsPipeline(true);//####[114]####
        taskinfo.setParameters(benchmark);//####[114]####
        taskinfo.setMethod(__pt__runBM_0_2_Benchmark_method);//####[114]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[114]####
    }//####[114]####
    public static void __pt__runBM_0_2(Benchmark benchmark) {//####[114]####
        try {//####[115]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[116]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_0_2 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[117]####
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
    private static volatile Method __pt__runBM_1_Benchmark_method = null;//####[129]####
    private synchronized static void __pt__runBM_1_Benchmark_ensureMethodVarSet() {//####[129]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[129]####
            try {//####[129]####
                __pt__runBM_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1", new Class[] {//####[129]####
                    Benchmark.class//####[129]####
                });//####[129]####
            } catch (Exception e) {//####[129]####
                e.printStackTrace();//####[129]####
            }//####[129]####
        }//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_1(Benchmark benchmark) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return runBM_1(benchmark, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_1(Benchmark benchmark, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[129]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setParameters(benchmark);//####[129]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return runBM_1(benchmark, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[129]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setTaskIdArgIndexes(0);//####[129]####
        taskinfo.addDependsOn(benchmark);//####[129]####
        taskinfo.setParameters(benchmark);//####[129]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return runBM_1(benchmark, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Void> runBM_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__runBM_1_Benchmark_method == null) {//####[129]####
            __pt__runBM_1_Benchmark_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setQueueArgIndexes(0);//####[129]####
        taskinfo.setIsPipeline(true);//####[129]####
        taskinfo.setParameters(benchmark);//####[129]####
        taskinfo.setMethod(__pt__runBM_1_Benchmark_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    public static void __pt__runBM_1(Benchmark benchmark) {//####[129]####
        try {//####[130]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[131]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_1 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[132]####
        } catch (IllegalAccessException e) {//####[135]####
            e.printStackTrace();//####[136]####
        } catch (IllegalArgumentException e) {//####[137]####
            e.printStackTrace();//####[138]####
        } catch (InvocationTargetException e) {//####[139]####
            e.printStackTrace();//####[140]####
        }//####[141]####
        TaskID tid_1_1 = runBM_1_1(createBenchmark());//####[143]####
        TaskIDGroup tig_1_1 = new TaskIDGroup(1);//####[144]####
        tig_1_1.add(tid_1_1);//####[145]####
        try {//####[147]####
            tig_1_1.waitTillFinished();//####[148]####
        } catch (ExecutionException e) {//####[149]####
            e.printStackTrace();//####[150]####
        } catch (InterruptedException e) {//####[151]####
            e.printStackTrace();//####[152]####
        }//####[153]####
        TaskID tid_1_2 = runBM_1_2(createBenchmark());//####[155]####
        TaskIDGroup tig_1_2 = new TaskIDGroup(1);//####[156]####
        tig_1_2.add(tid_1_2);//####[157]####
        try {//####[159]####
            tig_1_2.waitTillFinished();//####[160]####
        } catch (ExecutionException e) {//####[161]####
            e.printStackTrace();//####[162]####
        } catch (InterruptedException e) {//####[163]####
            e.printStackTrace();//####[164]####
        }//####[165]####
    }//####[167]####
//####[167]####
//####[169]####
    private static volatile Method __pt__runBM_1_1_Benchmark_method = null;//####[169]####
    private synchronized static void __pt__runBM_1_1_Benchmark_ensureMethodVarSet() {//####[169]####
        if (__pt__runBM_1_1_Benchmark_method == null) {//####[169]####
            try {//####[169]####
                __pt__runBM_1_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1_1", new Class[] {//####[169]####
                    Benchmark.class//####[169]####
                });//####[169]####
            } catch (Exception e) {//####[169]####
                e.printStackTrace();//####[169]####
            }//####[169]####
        }//####[169]####
    }//####[169]####
    private static TaskID<Void> runBM_1_1(Benchmark benchmark) {//####[169]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[169]####
        return runBM_1_1(benchmark, new TaskInfo());//####[169]####
    }//####[169]####
    private static TaskID<Void> runBM_1_1(Benchmark benchmark, TaskInfo taskinfo) {//####[169]####
        // ensure Method variable is set//####[169]####
        if (__pt__runBM_1_1_Benchmark_method == null) {//####[169]####
            __pt__runBM_1_1_Benchmark_ensureMethodVarSet();//####[169]####
        }//####[169]####
        taskinfo.setParameters(benchmark);//####[169]####
        taskinfo.setMethod(__pt__runBM_1_1_Benchmark_method);//####[169]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[169]####
    }//####[169]####
    private static TaskID<Void> runBM_1_1(TaskID<Benchmark> benchmark) {//####[169]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[169]####
        return runBM_1_1(benchmark, new TaskInfo());//####[169]####
    }//####[169]####
    private static TaskID<Void> runBM_1_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[169]####
        // ensure Method variable is set//####[169]####
        if (__pt__runBM_1_1_Benchmark_method == null) {//####[169]####
            __pt__runBM_1_1_Benchmark_ensureMethodVarSet();//####[169]####
        }//####[169]####
        taskinfo.setTaskIdArgIndexes(0);//####[169]####
        taskinfo.addDependsOn(benchmark);//####[169]####
        taskinfo.setParameters(benchmark);//####[169]####
        taskinfo.setMethod(__pt__runBM_1_1_Benchmark_method);//####[169]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[169]####
    }//####[169]####
    private static TaskID<Void> runBM_1_1(BlockingQueue<Benchmark> benchmark) {//####[169]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[169]####
        return runBM_1_1(benchmark, new TaskInfo());//####[169]####
    }//####[169]####
    private static TaskID<Void> runBM_1_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[169]####
        // ensure Method variable is set//####[169]####
        if (__pt__runBM_1_1_Benchmark_method == null) {//####[169]####
            __pt__runBM_1_1_Benchmark_ensureMethodVarSet();//####[169]####
        }//####[169]####
        taskinfo.setQueueArgIndexes(0);//####[169]####
        taskinfo.setIsPipeline(true);//####[169]####
        taskinfo.setParameters(benchmark);//####[169]####
        taskinfo.setMethod(__pt__runBM_1_1_Benchmark_method);//####[169]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[169]####
    }//####[169]####
    public static void __pt__runBM_1_1(Benchmark benchmark) {//####[169]####
        try {//####[170]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[171]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_1_1 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[172]####
        } catch (IllegalAccessException e) {//####[175]####
            e.printStackTrace();//####[176]####
        } catch (IllegalArgumentException e) {//####[177]####
            e.printStackTrace();//####[178]####
        } catch (InvocationTargetException e) {//####[179]####
            e.printStackTrace();//####[180]####
        }//####[181]####
    }//####[182]####
//####[182]####
//####[184]####
    private static volatile Method __pt__runBM_1_2_Benchmark_method = null;//####[184]####
    private synchronized static void __pt__runBM_1_2_Benchmark_ensureMethodVarSet() {//####[184]####
        if (__pt__runBM_1_2_Benchmark_method == null) {//####[184]####
            try {//####[184]####
                __pt__runBM_1_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_1_2", new Class[] {//####[184]####
                    Benchmark.class//####[184]####
                });//####[184]####
            } catch (Exception e) {//####[184]####
                e.printStackTrace();//####[184]####
            }//####[184]####
        }//####[184]####
    }//####[184]####
    private static TaskID<Void> runBM_1_2(Benchmark benchmark) {//####[184]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[184]####
        return runBM_1_2(benchmark, new TaskInfo());//####[184]####
    }//####[184]####
    private static TaskID<Void> runBM_1_2(Benchmark benchmark, TaskInfo taskinfo) {//####[184]####
        // ensure Method variable is set//####[184]####
        if (__pt__runBM_1_2_Benchmark_method == null) {//####[184]####
            __pt__runBM_1_2_Benchmark_ensureMethodVarSet();//####[184]####
        }//####[184]####
        taskinfo.setParameters(benchmark);//####[184]####
        taskinfo.setMethod(__pt__runBM_1_2_Benchmark_method);//####[184]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[184]####
    }//####[184]####
    private static TaskID<Void> runBM_1_2(TaskID<Benchmark> benchmark) {//####[184]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[184]####
        return runBM_1_2(benchmark, new TaskInfo());//####[184]####
    }//####[184]####
    private static TaskID<Void> runBM_1_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[184]####
        // ensure Method variable is set//####[184]####
        if (__pt__runBM_1_2_Benchmark_method == null) {//####[184]####
            __pt__runBM_1_2_Benchmark_ensureMethodVarSet();//####[184]####
        }//####[184]####
        taskinfo.setTaskIdArgIndexes(0);//####[184]####
        taskinfo.addDependsOn(benchmark);//####[184]####
        taskinfo.setParameters(benchmark);//####[184]####
        taskinfo.setMethod(__pt__runBM_1_2_Benchmark_method);//####[184]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[184]####
    }//####[184]####
    private static TaskID<Void> runBM_1_2(BlockingQueue<Benchmark> benchmark) {//####[184]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[184]####
        return runBM_1_2(benchmark, new TaskInfo());//####[184]####
    }//####[184]####
    private static TaskID<Void> runBM_1_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[184]####
        // ensure Method variable is set//####[184]####
        if (__pt__runBM_1_2_Benchmark_method == null) {//####[184]####
            __pt__runBM_1_2_Benchmark_ensureMethodVarSet();//####[184]####
        }//####[184]####
        taskinfo.setQueueArgIndexes(0);//####[184]####
        taskinfo.setIsPipeline(true);//####[184]####
        taskinfo.setParameters(benchmark);//####[184]####
        taskinfo.setMethod(__pt__runBM_1_2_Benchmark_method);//####[184]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[184]####
    }//####[184]####
    public static void __pt__runBM_1_2(Benchmark benchmark) {//####[184]####
        try {//####[185]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[186]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_1_2 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[187]####
        } catch (IllegalAccessException e) {//####[190]####
            e.printStackTrace();//####[191]####
        } catch (IllegalArgumentException e) {//####[192]####
            e.printStackTrace();//####[193]####
        } catch (InvocationTargetException e) {//####[194]####
            e.printStackTrace();//####[195]####
        }//####[196]####
    }//####[197]####
//####[197]####
//####[199]####
    private static volatile Method __pt__runBM_2_Benchmark_method = null;//####[199]####
    private synchronized static void __pt__runBM_2_Benchmark_ensureMethodVarSet() {//####[199]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[199]####
            try {//####[199]####
                __pt__runBM_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[199]####
                    Benchmark.class//####[199]####
                });//####[199]####
            } catch (Exception e) {//####[199]####
                e.printStackTrace();//####[199]####
            }//####[199]####
        }//####[199]####
    }//####[199]####
    private static TaskID<Void> runBM_2(Benchmark benchmark) {//####[199]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[199]####
        return runBM_2(benchmark, new TaskInfo());//####[199]####
    }//####[199]####
    private static TaskID<Void> runBM_2(Benchmark benchmark, TaskInfo taskinfo) {//####[199]####
        // ensure Method variable is set//####[199]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[199]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[199]####
        }//####[199]####
        taskinfo.setParameters(benchmark);//####[199]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[199]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[199]####
    }//####[199]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark) {//####[199]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[199]####
        return runBM_2(benchmark, new TaskInfo());//####[199]####
    }//####[199]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[199]####
        // ensure Method variable is set//####[199]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[199]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[199]####
        }//####[199]####
        taskinfo.setTaskIdArgIndexes(0);//####[199]####
        taskinfo.addDependsOn(benchmark);//####[199]####
        taskinfo.setParameters(benchmark);//####[199]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[199]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[199]####
    }//####[199]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark) {//####[199]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[199]####
        return runBM_2(benchmark, new TaskInfo());//####[199]####
    }//####[199]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[199]####
        // ensure Method variable is set//####[199]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[199]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[199]####
        }//####[199]####
        taskinfo.setQueueArgIndexes(0);//####[199]####
        taskinfo.setIsPipeline(true);//####[199]####
        taskinfo.setParameters(benchmark);//####[199]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[199]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[199]####
    }//####[199]####
    public static void __pt__runBM_2(Benchmark benchmark) {//####[199]####
        try {//####[200]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[201]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_2 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[202]####
        } catch (IllegalAccessException e) {//####[205]####
            e.printStackTrace();//####[206]####
        } catch (IllegalArgumentException e) {//####[207]####
            e.printStackTrace();//####[208]####
        } catch (InvocationTargetException e) {//####[209]####
            e.printStackTrace();//####[210]####
        }//####[211]####
        TaskID tid_2_1 = runBM_2_1(createBenchmark());//####[213]####
        TaskIDGroup tig_2_1 = new TaskIDGroup(1);//####[214]####
        tig_2_1.add(tid_2_1);//####[215]####
        try {//####[217]####
            Thread.sleep(1000 * 5);//####[218]####
        } catch (InterruptedException e) {//####[219]####
            e.printStackTrace();//####[220]####
        }//####[221]####
        try {//####[223]####
            tig_2_1.waitTillFinished();//####[224]####
        } catch (ExecutionException e) {//####[225]####
            e.printStackTrace();//####[226]####
        } catch (InterruptedException e) {//####[227]####
            e.printStackTrace();//####[228]####
        }//####[229]####
        TaskID tid_2_2 = runBM_2_2(createBenchmark());//####[231]####
        TaskIDGroup tig_2_2 = new TaskIDGroup(1);//####[232]####
        tig_2_2.add(tid_2_2);//####[233]####
        try {//####[235]####
            Thread.sleep(1000 * 5);//####[236]####
        } catch (InterruptedException e) {//####[237]####
            e.printStackTrace();//####[238]####
        }//####[239]####
        try {//####[241]####
            tig_2_2.waitTillFinished();//####[242]####
        } catch (ExecutionException e) {//####[243]####
            e.printStackTrace();//####[244]####
        } catch (InterruptedException e) {//####[245]####
            e.printStackTrace();//####[246]####
        }//####[247]####
    }//####[249]####
//####[249]####
//####[251]####
    private static volatile Method __pt__runBM_2_1_Benchmark_method = null;//####[251]####
    private synchronized static void __pt__runBM_2_1_Benchmark_ensureMethodVarSet() {//####[251]####
        if (__pt__runBM_2_1_Benchmark_method == null) {//####[251]####
            try {//####[251]####
                __pt__runBM_2_1_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2_1", new Class[] {//####[251]####
                    Benchmark.class//####[251]####
                });//####[251]####
            } catch (Exception e) {//####[251]####
                e.printStackTrace();//####[251]####
            }//####[251]####
        }//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_2_1(Benchmark benchmark) {//####[251]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[251]####
        return runBM_2_1(benchmark, new TaskInfo());//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_2_1(Benchmark benchmark, TaskInfo taskinfo) {//####[251]####
        // ensure Method variable is set//####[251]####
        if (__pt__runBM_2_1_Benchmark_method == null) {//####[251]####
            __pt__runBM_2_1_Benchmark_ensureMethodVarSet();//####[251]####
        }//####[251]####
        taskinfo.setParameters(benchmark);//####[251]####
        taskinfo.setMethod(__pt__runBM_2_1_Benchmark_method);//####[251]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_2_1(TaskID<Benchmark> benchmark) {//####[251]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[251]####
        return runBM_2_1(benchmark, new TaskInfo());//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_2_1(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[251]####
        // ensure Method variable is set//####[251]####
        if (__pt__runBM_2_1_Benchmark_method == null) {//####[251]####
            __pt__runBM_2_1_Benchmark_ensureMethodVarSet();//####[251]####
        }//####[251]####
        taskinfo.setTaskIdArgIndexes(0);//####[251]####
        taskinfo.addDependsOn(benchmark);//####[251]####
        taskinfo.setParameters(benchmark);//####[251]####
        taskinfo.setMethod(__pt__runBM_2_1_Benchmark_method);//####[251]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_2_1(BlockingQueue<Benchmark> benchmark) {//####[251]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[251]####
        return runBM_2_1(benchmark, new TaskInfo());//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_2_1(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[251]####
        // ensure Method variable is set//####[251]####
        if (__pt__runBM_2_1_Benchmark_method == null) {//####[251]####
            __pt__runBM_2_1_Benchmark_ensureMethodVarSet();//####[251]####
        }//####[251]####
        taskinfo.setQueueArgIndexes(0);//####[251]####
        taskinfo.setIsPipeline(true);//####[251]####
        taskinfo.setParameters(benchmark);//####[251]####
        taskinfo.setMethod(__pt__runBM_2_1_Benchmark_method);//####[251]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[251]####
    }//####[251]####
    public static void __pt__runBM_2_1(Benchmark benchmark) {//####[251]####
        try {//####[252]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[253]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_2_1 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[254]####
        } catch (IllegalAccessException e) {//####[257]####
            e.printStackTrace();//####[258]####
        } catch (IllegalArgumentException e) {//####[259]####
            e.printStackTrace();//####[260]####
        } catch (InvocationTargetException e) {//####[261]####
            e.printStackTrace();//####[262]####
        }//####[263]####
    }//####[264]####
//####[264]####
//####[266]####
    private static volatile Method __pt__runBM_2_2_Benchmark_method = null;//####[266]####
    private synchronized static void __pt__runBM_2_2_Benchmark_ensureMethodVarSet() {//####[266]####
        if (__pt__runBM_2_2_Benchmark_method == null) {//####[266]####
            try {//####[266]####
                __pt__runBM_2_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2_2", new Class[] {//####[266]####
                    Benchmark.class//####[266]####
                });//####[266]####
            } catch (Exception e) {//####[266]####
                e.printStackTrace();//####[266]####
            }//####[266]####
        }//####[266]####
    }//####[266]####
    private static TaskID<Void> runBM_2_2(Benchmark benchmark) {//####[266]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[266]####
        return runBM_2_2(benchmark, new TaskInfo());//####[266]####
    }//####[266]####
    private static TaskID<Void> runBM_2_2(Benchmark benchmark, TaskInfo taskinfo) {//####[266]####
        // ensure Method variable is set//####[266]####
        if (__pt__runBM_2_2_Benchmark_method == null) {//####[266]####
            __pt__runBM_2_2_Benchmark_ensureMethodVarSet();//####[266]####
        }//####[266]####
        taskinfo.setParameters(benchmark);//####[266]####
        taskinfo.setMethod(__pt__runBM_2_2_Benchmark_method);//####[266]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[266]####
    }//####[266]####
    private static TaskID<Void> runBM_2_2(TaskID<Benchmark> benchmark) {//####[266]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[266]####
        return runBM_2_2(benchmark, new TaskInfo());//####[266]####
    }//####[266]####
    private static TaskID<Void> runBM_2_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[266]####
        // ensure Method variable is set//####[266]####
        if (__pt__runBM_2_2_Benchmark_method == null) {//####[266]####
            __pt__runBM_2_2_Benchmark_ensureMethodVarSet();//####[266]####
        }//####[266]####
        taskinfo.setTaskIdArgIndexes(0);//####[266]####
        taskinfo.addDependsOn(benchmark);//####[266]####
        taskinfo.setParameters(benchmark);//####[266]####
        taskinfo.setMethod(__pt__runBM_2_2_Benchmark_method);//####[266]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[266]####
    }//####[266]####
    private static TaskID<Void> runBM_2_2(BlockingQueue<Benchmark> benchmark) {//####[266]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[266]####
        return runBM_2_2(benchmark, new TaskInfo());//####[266]####
    }//####[266]####
    private static TaskID<Void> runBM_2_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[266]####
        // ensure Method variable is set//####[266]####
        if (__pt__runBM_2_2_Benchmark_method == null) {//####[266]####
            __pt__runBM_2_2_Benchmark_ensureMethodVarSet();//####[266]####
        }//####[266]####
        taskinfo.setQueueArgIndexes(0);//####[266]####
        taskinfo.setIsPipeline(true);//####[266]####
        taskinfo.setParameters(benchmark);//####[266]####
        taskinfo.setMethod(__pt__runBM_2_2_Benchmark_method);//####[266]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[266]####
    }//####[266]####
    public static void __pt__runBM_2_2(Benchmark benchmark) {//####[266]####
        try {//####[267]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[268]####
            System.out.println("Thread " + Thread.currentThread().getId() + " fihish benchmark_2_2 " + "Multi Task [thread " + CurrentTask.currentThreadID() + "] [thread LocalID " + CurrentTask.currentThreadLocalID() + "] [globalID " + CurrentTask.globalID() + "]  [subtask " + CurrentTask.relativeID() + "]  [mulTaskSize " + CurrentTask.multiTaskSize() + "] ");//####[269]####
        } catch (IllegalAccessException e) {//####[272]####
            e.printStackTrace();//####[273]####
        } catch (IllegalArgumentException e) {//####[274]####
            e.printStackTrace();//####[275]####
        } catch (InvocationTargetException e) {//####[276]####
            e.printStackTrace();//####[277]####
        }//####[278]####
    }//####[279]####
//####[279]####
//####[345]####
    private static Benchmark createBenchmark() {//####[345]####
        Object benchmark = null;//####[346]####
        Method method = null;//####[347]####
        try {//####[348]####
            benchmark = bmClass.newInstance();//####[349]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[350]####
        } catch (InstantiationException e) {//####[351]####
            e.printStackTrace();//####[352]####
        } catch (IllegalAccessException e) {//####[353]####
            e.printStackTrace();//####[354]####
        } catch (NoSuchMethodException e) {//####[355]####
            e.printStackTrace();//####[356]####
        } catch (SecurityException e) {//####[357]####
            e.printStackTrace();//####[358]####
        } catch (IllegalArgumentException e) {//####[359]####
            e.printStackTrace();//####[360]####
        }//####[361]####
        Object[] arguments = new Object[1];//####[362]####
        arguments[0] = N_DATASIZE;//####[363]####
        return new Benchmark(benchmark, method, arguments);//####[365]####
    }//####[366]####
//####[368]####
    private static void getBenchmarkClass(String bmName) {//####[368]####
        try {//####[369]####
            if (bmName.equalsIgnoreCase(MOL)) //####[370]####
            {//####[370]####
                bmClass = Class.forName(MOL_CLASS);//####[371]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[372]####
            {//####[372]####
                bmClass = Class.forName(MON_CLASS);//####[373]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[374]####
            {//####[374]####
                bmClass = Class.forName(RAY_CLASS);//####[375]####
            } else {//####[376]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[377]####
            }//####[378]####
        } catch (ClassNotFoundException e) {//####[379]####
            e.printStackTrace();//####[380]####
        } catch (Exception e) {//####[381]####
            e.printStackTrace();//####[382]####
        }//####[383]####
    }//####[384]####
}//####[384]####
