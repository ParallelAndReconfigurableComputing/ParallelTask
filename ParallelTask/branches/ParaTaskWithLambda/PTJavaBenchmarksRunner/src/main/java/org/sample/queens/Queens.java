package org.sample.queens;//####[1]####
//####[1]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[3]####
import pt.runtime.ParaTask.ScheduleType;//####[4]####
import pt.runtime.ParaTask.ThreadPoolType;//####[5]####
//####[5]####
//-- ParaTask related imports//####[5]####
import pt.runtime.*;//####[5]####
import java.util.concurrent.ExecutionException;//####[5]####
import java.util.concurrent.locks.*;//####[5]####
import java.lang.reflect.*;//####[5]####
import pt.runtime.GuiThread;//####[5]####
import java.util.concurrent.BlockingQueue;//####[5]####
import java.util.ArrayList;//####[5]####
import java.util.List;//####[5]####
//####[5]####
public class Queens {//####[7]####
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
//####[9]####
    private final int n;//####[9]####
//####[12]####
    public Queens(int n) {//####[12]####
        this.n = n;//####[13]####
    }//####[14]####
//####[16]####
    private boolean safe(int i, int j, int[] config) {//####[16]####
        for (int r = 0; r < i; r++) //####[17]####
        {//####[17]####
            int s = config[r];//####[18]####
            if (j == s || i - r == j - s || i - r == s - j) //####[19]####
            {//####[19]####
                return false;//####[20]####
            }//####[21]####
        }//####[22]####
        return true;//####[23]####
    }//####[24]####
//####[27]####
    private int[] nqueensSeq(int[] config, int i) {//####[27]####
        new NewtonChaos().newtonPartial(newtonNValue);//####[29]####
        if (i == n) //####[31]####
        {//####[31]####
            return config;//####[33]####
        }//####[34]####
        for (int j = 0; j < n; j++) //####[35]####
        {//####[35]####
            int[] nconfig = new int[n];//####[36]####
            System.arraycopy(config, 0, nconfig, 0, n);//####[37]####
            nconfig[i] = j;//####[38]####
            if (safe(i, j, nconfig)) //####[40]####
            {//####[40]####
                nqueensSeq(nconfig, i + 1);//####[41]####
            }//####[42]####
        }//####[43]####
        return null;//####[44]####
    }//####[45]####
//####[48]####
    private static volatile Method __pt__nqueens_intAr_int_method = null;//####[48]####
    private synchronized static void __pt__nqueens_intAr_int_ensureMethodVarSet() {//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            try {//####[48]####
                __pt__nqueens_intAr_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__nqueens", new Class[] {//####[48]####
                    int[].class, int.class//####[48]####
                });//####[48]####
            } catch (Exception e) {//####[48]####
                e.printStackTrace();//####[48]####
            }//####[48]####
        }//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(int[] config, int i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(int[] config, int i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, int i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, int i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setTaskIdArgIndexes(0);//####[48]####
        taskinfo.addDependsOn(config);//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, int i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, int i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setQueueArgIndexes(0);//####[48]####
        taskinfo.setIsPipeline(true);//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(int[] config, TaskID<Integer> i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(int[] config, TaskID<Integer> i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setTaskIdArgIndexes(1);//####[48]####
        taskinfo.addDependsOn(i);//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, TaskID<Integer> i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, TaskID<Integer> i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[48]####
        taskinfo.addDependsOn(config);//####[48]####
        taskinfo.addDependsOn(i);//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, TaskID<Integer> i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, TaskID<Integer> i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setQueueArgIndexes(0);//####[48]####
        taskinfo.setIsPipeline(true);//####[48]####
        taskinfo.setTaskIdArgIndexes(1);//####[48]####
        taskinfo.addDependsOn(i);//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(int[] config, BlockingQueue<Integer> i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(int[] config, BlockingQueue<Integer> i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setQueueArgIndexes(1);//####[48]####
        taskinfo.setIsPipeline(true);//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, BlockingQueue<Integer> i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, BlockingQueue<Integer> i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setQueueArgIndexes(1);//####[48]####
        taskinfo.setIsPipeline(true);//####[48]####
        taskinfo.setTaskIdArgIndexes(0);//####[48]####
        taskinfo.addDependsOn(config);//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, BlockingQueue<Integer> i) {//####[48]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[48]####
        return nqueens(config, i, new TaskInfo());//####[48]####
    }//####[48]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, BlockingQueue<Integer> i, TaskInfo taskinfo) {//####[48]####
        // ensure Method variable is set//####[48]####
        if (__pt__nqueens_intAr_int_method == null) {//####[48]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[48]####
        }//####[48]####
        taskinfo.setQueueArgIndexes(0, 1);//####[48]####
        taskinfo.setIsPipeline(true);//####[48]####
        taskinfo.setParameters(config, i);//####[48]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[48]####
        taskinfo.setInstance(this);//####[48]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[48]####
    }//####[48]####
    public int[] __pt__nqueens(int[] config, int i) {//####[48]####
        new NewtonChaos().newtonPartial(newtonNValue);//####[50]####
        if (i == n) //####[52]####
        {//####[52]####
            return config;//####[54]####
        }//####[55]####
        int groupSize = 0;//####[57]####
        for (int j = 0; j < n; j++) //####[58]####
        {//####[58]####
            int[] nconfig = new int[n];//####[59]####
            System.arraycopy(config, 0, nconfig, 0, n);//####[60]####
            nconfig[i] = j;//####[61]####
            if (safe(i, j, nconfig)) //####[63]####
            {//####[63]####
                groupSize++;//####[64]####
            }//####[65]####
        }//####[66]####
        TaskIDGroup<int[]> group = new TaskIDGroup<int[]>(groupSize);//####[68]####
        for (int j = 0; j < n; j++) //####[70]####
        {//####[70]####
            int[] nconfig = new int[n];//####[71]####
            System.arraycopy(config, 0, nconfig, 0, n);//####[72]####
            nconfig[i] = j;//####[73]####
            if (safe(i, j, nconfig)) //####[75]####
            {//####[75]####
                TaskID<int[]> id = nqueens(nconfig, i + 1);//####[76]####
                group.add(id);//####[78]####
            }//####[79]####
        }//####[80]####
        try {//####[81]####
            group.waitTillFinished();//####[82]####
            if (shouldPrint) //####[84]####
            {//####[84]####
                Reduction<int[]> red = new Reduction<int[]>() {//####[84]####
//####[87]####
                    @Override//####[87]####
                    public int[] combine(int[] a, int[] b) {//####[87]####
                        return a != null ? a : b;//####[88]####
                    }//####[89]####
                };//####[89]####
                int[] finalRes = group.reduce(red);//####[91]####
                if (finalRes != null) //####[92]####
                {//####[92]####
                    System.out.println("GOT A RESULT: ");//####[93]####
                    for (int f = 0; f < n; f++) //####[94]####
                    {//####[94]####
                        System.out.print(finalRes[f] + " ");//####[95]####
                    }//####[96]####
                    return finalRes;//####[99]####
                }//####[100]####
            }//####[101]####
        } catch (ExecutionException e) {//####[103]####
            e.printStackTrace();//####[104]####
        } catch (InterruptedException e) {//####[105]####
            e.printStackTrace();//####[106]####
        }//####[107]####
        return null;//####[108]####
    }//####[109]####
//####[109]####
//####[112]####
    public static void mainss(String argv[]) {//####[112]####
        for (int i = 0; i < 30; i++) //####[113]####
        {//####[113]####
            System.out.println("--------------------------------------------------- " + i);//####[115]####
        }//####[116]####
    }//####[117]####
//####[123]####
    public static int newtonNValue = 2;//####[123]####
//####[125]####
    private static boolean shouldPrint = false;//####[125]####
//####[127]####
    public static void main(String[] args) {//####[127]####
        String appType = "pt-steal";//####[128]####
        int nValue = 11;//####[130]####
        int numThreads = 3;//####[131]####
        if (args.length == 0) //####[132]####
        {//####[132]####
        } else if (args.length != 4) //####[133]####
        {//####[133]####
            System.out.println("Usage: (pt-steal|pt-mix|seq|jv-max) (Nvalue) (numThreads) (newtonValue)");//####[134]####
            System.exit(0);//####[135]####
        } else {//####[136]####
            appType = args[0];//####[137]####
            nValue = Integer.parseInt(args[1]);//####[138]####
            numThreads = Integer.parseInt(args[2]);//####[139]####
            newtonNValue = Integer.parseInt(args[3]);//####[140]####
        }//####[141]####
        if (appType.equals("pt-share")) //####[142]####
        {//####[142]####
            ParaTask.setScheduling(ScheduleType.WorkSharing);//####[143]####
        } else if (appType.equals("pt-steal")) //####[144]####
        {//####[144]####
            ParaTask.setScheduling(ScheduleType.WorkStealing);//####[145]####
        }//####[146]####
        if (numThreads > 0) //####[147]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[147]####
        Queens queens = new Queens(nValue);//####[148]####
        System.gc();//####[149]####
        int[] config = new int[nValue];//####[151]####
        int[] result = null;//####[152]####
        System.out.print("CountQueens(" + nValue + ")  ");//####[153]####
        if (appType.startsWith("pt-")) //####[154]####
        {//####[154]####
            long start = System.currentTimeMillis();//####[155]####
            TaskID<int[]> id = queens.nqueens(config, 0);//####[156]####
            try {//####[157]####
                result = id.getReturnResult();//####[158]####
            } catch (ExecutionException e) {//####[159]####
                e.printStackTrace();//####[160]####
            } catch (InterruptedException e) {//####[161]####
                e.printStackTrace();//####[162]####
            }//####[163]####
            long end = System.currentTimeMillis();//####[164]####
            if (appType.equals("pt-share")) //####[165]####
            {//####[165]####
                System.out.print("PT (work-sharing) ");//####[166]####
            } else if (appType.equals("pt-steal")) //####[167]####
            {//####[167]####
                System.out.print("PT (work-stealing) ");//####[168]####
            } else {//####[169]####
                System.out.print("PT (mixed-schedule) ");//####[170]####
            }//####[171]####
            System.out.println(numThreads + " threads = " + (end - start));//####[172]####
        } else if (appType.equals("seq")) //####[173]####
        {//####[173]####
            long start = System.currentTimeMillis();//####[174]####
            result = queens.nqueensSeq(config, 0);//####[175]####
            long end = System.currentTimeMillis();//####[176]####
            System.out.println("seq = " + (end - start));//####[177]####
        } else if (appType.equals("jv-max")) //####[178]####
        {//####[178]####
            long start = System.currentTimeMillis();//####[179]####
            long end = System.currentTimeMillis();//####[180]####
            System.out.println("Java-max = " + (end - start));//####[181]####
        } else if (appType.equals("jv-min")) //####[182]####
        {//####[182]####
            long start = System.currentTimeMillis();//####[183]####
            long end = System.currentTimeMillis();//####[184]####
            System.out.println("Java-min " + numThreads + " threads = " + (end - start));//####[185]####
        } else if (appType.equals("sw")) //####[186]####
        {//####[186]####
            long start = System.currentTimeMillis();//####[187]####
            long end = System.currentTimeMillis();//####[188]####
            System.out.println("SW = " + (end - start));//####[189]####
        } else {//####[190]####
            System.err.println("Aborting performance testing, unknown application: " + appType);//####[191]####
            System.exit(-1);//####[192]####
        }//####[193]####
    }//####[194]####
}//####[194]####
