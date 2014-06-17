package org.sample.queens;//####[1]####
//####[1]####
import pt.runtime.ParaTask.ScheduleType;//####[3]####
import pt.runtime.ParaTask.ThreadPoolType;//####[4]####
//####[4]####
//-- ParaTask related imports//####[4]####
import pt.runtime.*;//####[4]####
import java.util.concurrent.ExecutionException;//####[4]####
import java.util.concurrent.locks.*;//####[4]####
import java.lang.reflect.*;//####[4]####
import pt.runtime.GuiThread;//####[4]####
import java.util.concurrent.BlockingQueue;//####[4]####
import java.util.ArrayList;//####[4]####
import java.util.List;//####[4]####
//####[4]####
public class Queens {//####[6]####
    static{ParaTask.init();}//####[6]####
    /*  ParaTask helper method to access private/protected slots *///####[6]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[6]####
        if (m.getParameterTypes().length == 0)//####[6]####
            m.invoke(instance);//####[6]####
        else if ((m.getParameterTypes().length == 1))//####[6]####
            m.invoke(instance, arg);//####[6]####
        else //####[6]####
            m.invoke(instance, arg, interResult);//####[6]####
    }//####[6]####
//####[8]####
    private final int n;//####[8]####
//####[9]####
    private int newtonNValue = 2;//####[9]####
//####[11]####
    private int threshold = 4;//####[11]####
//####[13]####
    private static boolean shouldPrintResults = false;//####[13]####
//####[15]####
    public Queens(int n, int newtonValue, int threshold) {//####[15]####
        this.n = n;//####[16]####
        this.newtonNValue = newtonValue;//####[17]####
        this.threshold = threshold;//####[18]####
    }//####[19]####
//####[21]####
    private boolean safe(int i, int j, int[] config) {//####[21]####
        for (int r = 0; r < i; r++) //####[22]####
        {//####[22]####
            int s = config[r];//####[23]####
            if (j == s || i - r == j - s || i - r == s - j) //####[24]####
            {//####[24]####
                return false;//####[25]####
            }//####[26]####
        }//####[27]####
        return true;//####[28]####
    }//####[29]####
//####[32]####
    private int[] nqueensSeq(int[] config, int i) {//####[32]####
        if (this.newtonNValue > 0) //####[34]####
        new NewtonChaos().newtonPartial(newtonNValue);//####[35]####
        if (i == n) //####[37]####
        {//####[37]####
            if (shouldPrintResults) //####[38]####
            {//####[38]####
                System.out.print("Got a result (in nqueensSeq): ");//####[39]####
                for (int v : config) //####[40]####
                {//####[40]####
                    System.out.print(v + " ");//####[41]####
                }//####[42]####
                System.out.println();//####[43]####
            }//####[44]####
            return config;//####[46]####
        }//####[47]####
        for (int j = 0; j < n; j++) //####[48]####
        {//####[48]####
            int[] nconfig = new int[n];//####[49]####
            System.arraycopy(config, 0, nconfig, 0, n);//####[50]####
            nconfig[i] = j;//####[51]####
            if (safe(i, j, nconfig)) //####[53]####
            {//####[53]####
                nqueensSeq(nconfig, i + 1);//####[54]####
            }//####[55]####
        }//####[56]####
        return null;//####[57]####
    }//####[58]####
//####[61]####
    private static volatile Method __pt__nqueens_intAr_int_method = null;//####[61]####
    private synchronized static void __pt__nqueens_intAr_int_ensureMethodVarSet() {//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            try {//####[61]####
                __pt__nqueens_intAr_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__nqueens", new Class[] {//####[61]####
                    int[].class, int.class//####[61]####
                });//####[61]####
            } catch (Exception e) {//####[61]####
                e.printStackTrace();//####[61]####
            }//####[61]####
        }//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(int[] config, int i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(int[] config, int i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, int i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, int i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setTaskIdArgIndexes(0);//####[61]####
        taskinfo.addDependsOn(config);//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, int i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, int i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setQueueArgIndexes(0);//####[61]####
        taskinfo.setIsPipeline(true);//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(int[] config, TaskID<Integer> i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(int[] config, TaskID<Integer> i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setTaskIdArgIndexes(1);//####[61]####
        taskinfo.addDependsOn(i);//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, TaskID<Integer> i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, TaskID<Integer> i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[61]####
        taskinfo.addDependsOn(config);//####[61]####
        taskinfo.addDependsOn(i);//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, TaskID<Integer> i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, TaskID<Integer> i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setQueueArgIndexes(0);//####[61]####
        taskinfo.setIsPipeline(true);//####[61]####
        taskinfo.setTaskIdArgIndexes(1);//####[61]####
        taskinfo.addDependsOn(i);//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(int[] config, BlockingQueue<Integer> i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(int[] config, BlockingQueue<Integer> i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setQueueArgIndexes(1);//####[61]####
        taskinfo.setIsPipeline(true);//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, BlockingQueue<Integer> i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(TaskID<int[]> config, BlockingQueue<Integer> i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setQueueArgIndexes(1);//####[61]####
        taskinfo.setIsPipeline(true);//####[61]####
        taskinfo.setTaskIdArgIndexes(0);//####[61]####
        taskinfo.addDependsOn(config);//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, BlockingQueue<Integer> i) {//####[61]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[61]####
        return nqueens(config, i, new TaskInfo());//####[61]####
    }//####[61]####
    private TaskID<int[]> nqueens(BlockingQueue<int[]> config, BlockingQueue<Integer> i, TaskInfo taskinfo) {//####[61]####
        // ensure Method variable is set//####[61]####
        if (__pt__nqueens_intAr_int_method == null) {//####[61]####
            __pt__nqueens_intAr_int_ensureMethodVarSet();//####[61]####
        }//####[61]####
        taskinfo.setQueueArgIndexes(0, 1);//####[61]####
        taskinfo.setIsPipeline(true);//####[61]####
        taskinfo.setParameters(config, i);//####[61]####
        taskinfo.setMethod(__pt__nqueens_intAr_int_method);//####[61]####
        taskinfo.setInstance(this);//####[61]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[61]####
    }//####[61]####
    public int[] __pt__nqueens(int[] config, int i) {//####[61]####
        if (this.newtonNValue > 0) //####[63]####
        new NewtonChaos().newtonPartial(newtonNValue);//####[64]####
        if (i == n) //####[66]####
        {//####[66]####
            if (shouldPrintResults) //####[67]####
            {//####[67]####
                System.out.print("Got a result: ");//####[68]####
                for (int v : config) //####[69]####
                {//####[69]####
                    System.out.print(v + " ");//####[70]####
                }//####[71]####
                System.out.println();//####[72]####
            }//####[73]####
            return config;//####[74]####
        }//####[75]####
        int groupSize = 0;//####[77]####
        for (int j = 0; j < n; j++) //####[78]####
        {//####[78]####
            int[] nconfig = new int[n];//####[79]####
            System.arraycopy(config, 0, nconfig, 0, n);//####[80]####
            nconfig[i] = j;//####[81]####
            if (safe(i, j, nconfig)) //####[83]####
            {//####[83]####
                if (this.newtonNValue > 0) //####[84]####
                groupSize++; else {//####[86]####
                    if (n - i <= this.threshold) //####[87]####
                    {//####[87]####
                    } else groupSize++;//####[88]####
                }//####[90]####
            }//####[91]####
        }//####[92]####
        TaskIDGroup<int[]> group = new TaskIDGroup<int[]>(groupSize);//####[94]####
        for (int j = 0; j < n; j++) //####[96]####
        {//####[96]####
            int[] nconfig = new int[n];//####[97]####
            System.arraycopy(config, 0, nconfig, 0, n);//####[98]####
            nconfig[i] = j;//####[99]####
            if (safe(i, j, nconfig)) //####[101]####
            {//####[101]####
                if (this.newtonNValue > 0) //####[102]####
                {//####[102]####
                    TaskID<int[]> id = nqueens(nconfig, i + 1);//####[103]####
                    group.add(id);//####[104]####
                } else {//####[105]####
                    if (n - i <= this.threshold) //####[107]####
                    {//####[107]####
                        nqueensSeq(nconfig, i + 1);//####[108]####
                    } else {//####[109]####
                        TaskID<int[]> id = nqueens(nconfig, i + 1);//####[110]####
                        group.add(id);//####[111]####
                    }//####[112]####
                }//####[113]####
            }//####[114]####
        }//####[115]####
        try {//####[117]####
            group.waitTillFinished();//####[118]####
        } catch (ExecutionException e) {//####[119]####
            e.printStackTrace();//####[120]####
        } catch (InterruptedException e) {//####[121]####
            e.printStackTrace();//####[122]####
        }//####[123]####
        return null;//####[124]####
    }//####[125]####
//####[125]####
//####[127]####
    public static void solveQueensPuzzleWithNewtonChaos(int nQueen, int numThreads, ScheduleType schedule, int nNewton) {//####[128]####
        assert nNewton >= 0;//####[129]####
        solveQueensPuzzle(nQueen, numThreads, schedule, nNewton, 0);//####[130]####
    }//####[131]####
//####[133]####
    public static void solveQueensPuzzleWithThreshold(int nQueen, int numThreads, ScheduleType schedule, int threshold) {//####[134]####
        assert threshold >= 0;//####[135]####
        solveQueensPuzzle(nQueen, numThreads, schedule, 0, threshold);//####[136]####
    }//####[137]####
//####[139]####
    private static void solveQueensPuzzle(int nQueen, int numThreads, ScheduleType schedule, int nNewton, int threshold) {//####[140]####
        ParaTask.setScheduling(schedule);//####[141]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[142]####
        Queens queens = new Queens(nQueen, nNewton, threshold);//####[143]####
        int[] config = new int[nQueen];//####[145]####
        int[] result = null;//####[146]####
        TaskID<int[]> id = queens.nqueens(config, 0);//####[148]####
        try {//####[149]####
            result = id.getReturnResult();//####[150]####
        } catch (ExecutionException e) {//####[151]####
            e.printStackTrace();//####[152]####
        } catch (InterruptedException e) {//####[153]####
            e.printStackTrace();//####[154]####
        }//####[155]####
    }//####[156]####
//####[158]####
    public static void solveQueensPuzzleSequentially(int nQueen, int nNewton) {//####[158]####
        Queens queens = new Queens(nQueen, nNewton, 0);//####[159]####
        int[] config = new int[nQueen];//####[160]####
        queens.nqueensSeq(config, 0);//####[161]####
    }//####[162]####
//####[164]####
    public static void main2(String[] args) {//####[164]####
        shouldPrintResults = true;//####[165]####
        solveQueensPuzzleWithThreshold(6, 1, ScheduleType.WorkSharing, 4);//####[167]####
    }//####[170]####
//####[172]####
    public static void main(String[] args) {//####[172]####
        String appType = "pt-steal";//####[173]####
        int nValue = 11;//####[175]####
        int numThreads = 3;//####[176]####
        int newtonValue = 2;//####[177]####
        if (args.length == 0) //####[178]####
        {//####[178]####
        } else if (args.length != 4) //####[179]####
        {//####[179]####
            System.out.println("Usage: (pt-steal|pt-mix|seq|jv-max) (Nvalue) (numThreads) (newtonValue)");//####[180]####
            System.exit(0);//####[181]####
        } else {//####[182]####
            appType = args[0];//####[183]####
            nValue = Integer.parseInt(args[1]);//####[184]####
            numThreads = Integer.parseInt(args[2]);//####[185]####
            newtonValue = Integer.parseInt(args[3]);//####[186]####
        }//####[187]####
        if (appType.equals("pt-share")) //####[188]####
        {//####[188]####
            ParaTask.setScheduling(ScheduleType.WorkSharing);//####[189]####
        } else if (appType.equals("pt-steal")) //####[190]####
        {//####[190]####
            ParaTask.setScheduling(ScheduleType.WorkStealing);//####[191]####
        }//####[192]####
        if (numThreads > 0) //####[193]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[193]####
        Queens queens = new Queens(nValue, newtonValue, 0);//####[194]####
        System.gc();//####[195]####
        int[] config = new int[nValue];//####[197]####
        int[] result = null;//####[198]####
        System.out.print("CountQueens(" + nValue + ")  ");//####[199]####
        if (appType.startsWith("pt-")) //####[200]####
        {//####[200]####
            long start = System.currentTimeMillis();//####[201]####
            TaskID<int[]> id = queens.nqueens(config, 0);//####[202]####
            try {//####[203]####
                result = id.getReturnResult();//####[204]####
            } catch (ExecutionException e) {//####[205]####
                e.printStackTrace();//####[206]####
            } catch (InterruptedException e) {//####[207]####
                e.printStackTrace();//####[208]####
            }//####[209]####
            long end = System.currentTimeMillis();//####[210]####
            if (appType.equals("pt-share")) //####[211]####
            {//####[211]####
                System.out.print("PT (work-sharing) ");//####[212]####
            } else if (appType.equals("pt-steal")) //####[213]####
            {//####[213]####
                System.out.print("PT (work-stealing) ");//####[214]####
            } else {//####[215]####
                System.out.print("PT (mixed-schedule) ");//####[216]####
            }//####[217]####
            System.out.println(numThreads + " threads = " + (end - start));//####[218]####
        } else if (appType.equals("seq")) //####[219]####
        {//####[219]####
            long start = System.currentTimeMillis();//####[220]####
            result = queens.nqueensSeq(config, 0);//####[221]####
            long end = System.currentTimeMillis();//####[222]####
            System.out.println("seq = " + (end - start));//####[223]####
        } else if (appType.equals("jv-max")) //####[224]####
        {//####[224]####
            long start = System.currentTimeMillis();//####[225]####
            long end = System.currentTimeMillis();//####[226]####
            System.out.println("Java-max = " + (end - start));//####[227]####
        } else if (appType.equals("jv-min")) //####[228]####
        {//####[228]####
            long start = System.currentTimeMillis();//####[229]####
            long end = System.currentTimeMillis();//####[230]####
            System.out.println("Java-min " + numThreads + " threads = " + (end - start));//####[231]####
        } else if (appType.equals("sw")) //####[232]####
        {//####[232]####
            long start = System.currentTimeMillis();//####[233]####
            long end = System.currentTimeMillis();//####[234]####
            System.out.println("SW = " + (end - start));//####[235]####
        } else {//####[236]####
            System.err.println("Aborting performance testing, unknown application: " + appType);//####[237]####
            System.exit(-1);//####[238]####
        }//####[239]####
    }//####[240]####
}//####[240]####
