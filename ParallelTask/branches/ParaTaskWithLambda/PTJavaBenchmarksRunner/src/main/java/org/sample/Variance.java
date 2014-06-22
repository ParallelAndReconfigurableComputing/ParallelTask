package org.sample;//####[1]####
//####[1]####
import java.util.Arrays;//####[3]####
import java.util.Random;//####[4]####
import java.util.concurrent.*;//####[5]####
import pt.runtime.TaskIDGroup;//####[7]####
import pt.runtime.ParaTask.ScheduleType;//####[8]####
import pt.runtime.ParaTask.ThreadPoolType;//####[9]####
//####[9]####
//-- ParaTask related imports//####[9]####
import pt.runtime.*;//####[9]####
import java.util.concurrent.ExecutionException;//####[9]####
import java.util.concurrent.locks.*;//####[9]####
import java.lang.reflect.*;//####[9]####
import pt.runtime.GuiThread;//####[9]####
import java.util.concurrent.BlockingQueue;//####[9]####
import java.util.ArrayList;//####[9]####
import java.util.List;//####[9]####
//####[9]####
public class Variance {//####[11]####
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
//####[13]####
    private static final Random rand = new Random();//####[13]####
//####[14]####
    private static final int MIN = 1;//####[14]####
//####[15]####
    private static final int MAX = 140;//####[15]####
//####[16]####
    private static final int POPULATION_SIZE = 90000000;//####[16]####
//####[18]####
    public static long THRESHOLD = 1000;//####[18]####
//####[20]####
    public static void init() {//####[20]####
        population = generatePopulation(POPULATION_SIZE);//####[21]####
    }//####[22]####
//####[24]####
    public static double[] generatePopulation(int populationSize) {//####[24]####
        ParaTask.init();//####[25]####
        double[] data = new double[populationSize];//####[27]####
        for (int i = 0; i < populationSize; ++i) //####[28]####
        {//####[28]####
            data[i] = randInt();//####[29]####
        }//####[30]####
        return data;//####[31]####
    }//####[32]####
//####[34]####
    private static double[] population;//####[34]####
//####[36]####
    public static void main(String... args) {//####[36]####
        population = generatePopulation(POPULATION_SIZE);//####[38]####
        computeMeanParaTask(2, ScheduleType.WorkSharing);//####[40]####
        computeMeanParaTask(7, ScheduleType.WorkSharing);//####[41]####
    }//####[49]####
//####[51]####
    public static int randInt() {//####[51]####
        return rand.nextInt((MAX - MIN) + 1) + MIN;//####[52]####
    }//####[53]####
//####[55]####
    public static double varianceImperative() {//####[55]####
        double average = 0.0;//####[56]####
        for (double p : population) //####[57]####
        {//####[57]####
            average += p;//####[58]####
        }//####[59]####
        average /= population.length;//####[60]####
        double variance = 0.0;//####[62]####
        for (double p : population) //####[63]####
        {//####[63]####
            variance += (p - average) * (p - average);//####[64]####
        }//####[65]####
        return variance / population.length;//####[66]####
    }//####[67]####
//####[69]####
    private static double getResult(TaskID<Double> t) {//####[69]####
        Double result = null;//####[70]####
        try {//####[71]####
            result = t.getReturnResult();//####[72]####
        } catch (Exception e) {//####[73]####
            throw new ParaTaskRuntimeException(e.getMessage());//####[74]####
        }//####[75]####
        return result;//####[76]####
    }//####[77]####
//####[79]####
    public static double varianceParaTask() {//####[79]####
        double total = computeSum(0, population.length);//####[80]####
        double mean = total / population.length;//####[81]####
        double varSum = computeVarianceSum(0, population.length, mean);//####[82]####
        return varSum / population.length;//####[83]####
    }//####[84]####
//####[86]####
    public static void varianceWithThreshold(int numThreads, ScheduleType schedule, int threshold) {//####[87]####
        ParaTask.setScheduling(schedule);//####[88]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[89]####
        THRESHOLD = threshold;//####[90]####
        varianceParaTask();//####[91]####
    }//####[92]####
//####[94]####
    public static double meanRecusive(int numThreads, ScheduleType schedule, int threshold) {//####[95]####
        ParaTask.setScheduling(schedule);//####[96]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, numThreads);//####[97]####
        if (numThreads == 1) //####[99]####
        {//####[99]####
            double total = 0;//####[100]####
            for (int i = 0; i < population.length; i++) //####[101]####
            {//####[101]####
                total += population[i];//####[102]####
            }//####[103]####
            return total / population.length;//####[104]####
        }//####[105]####
        THRESHOLD = threshold;//####[107]####
        double total = computeSum(0, population.length);//####[108]####
        return total / population.length;//####[109]####
    }//####[110]####
//####[112]####
    private static double computeSum(int start, int end) {//####[112]####
        int length = end - start;//####[113]####
        if (length <= THRESHOLD) //####[114]####
        {//####[114]####
            double total = 0;//####[115]####
            for (int i = start; i < end; i++) //####[116]####
            {//####[116]####
                total += population[i];//####[117]####
            }//####[118]####
            return total;//####[119]####
        }//####[120]####
        TaskID<Double> left = computeSumTask(start, start + length / 2);//####[122]####
        double rightSum = computeSum(start + length / 2, end);//####[124]####
        return getResult(left) + rightSum;//####[126]####
    }//####[127]####
//####[129]####
    private static volatile Method __pt__computeSumTask_int_int_method = null;//####[129]####
    private synchronized static void __pt__computeSumTask_int_int_ensureMethodVarSet() {//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            try {//####[129]####
                __pt__computeSumTask_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeSumTask", new Class[] {//####[129]####
                    int.class, int.class//####[129]####
                });//####[129]####
            } catch (Exception e) {//####[129]####
                e.printStackTrace();//####[129]####
            }//####[129]####
        }//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(int start, int end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(int start, int end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, int end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, int end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setTaskIdArgIndexes(0);//####[129]####
        taskinfo.addDependsOn(start);//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, int end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, int end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setQueueArgIndexes(0);//####[129]####
        taskinfo.setIsPipeline(true);//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(int start, TaskID<Integer> end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(int start, TaskID<Integer> end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setTaskIdArgIndexes(1);//####[129]####
        taskinfo.addDependsOn(end);//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, TaskID<Integer> end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[129]####
        taskinfo.addDependsOn(start);//####[129]####
        taskinfo.addDependsOn(end);//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, TaskID<Integer> end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setQueueArgIndexes(0);//####[129]####
        taskinfo.setIsPipeline(true);//####[129]####
        taskinfo.setTaskIdArgIndexes(1);//####[129]####
        taskinfo.addDependsOn(end);//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(int start, BlockingQueue<Integer> end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(int start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setQueueArgIndexes(1);//####[129]####
        taskinfo.setIsPipeline(true);//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, BlockingQueue<Integer> end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setQueueArgIndexes(1);//####[129]####
        taskinfo.setIsPipeline(true);//####[129]####
        taskinfo.setTaskIdArgIndexes(0);//####[129]####
        taskinfo.addDependsOn(start);//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end) {//####[129]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[129]####
        return computeSumTask(start, end, new TaskInfo());//####[129]####
    }//####[129]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[129]####
        // ensure Method variable is set//####[129]####
        if (__pt__computeSumTask_int_int_method == null) {//####[129]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[129]####
        }//####[129]####
        taskinfo.setQueueArgIndexes(0, 1);//####[129]####
        taskinfo.setIsPipeline(true);//####[129]####
        taskinfo.setParameters(start, end);//####[129]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[129]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[129]####
    }//####[129]####
    public static double __pt__computeSumTask(int start, int end) {//####[129]####
        return computeSum(start, end);//####[130]####
    }//####[131]####
//####[131]####
//####[133]####
    private static volatile Method __pt__computeVarianceSumTask_int_int_double_method = null;//####[133]####
    private synchronized static void __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet() {//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            try {//####[133]####
                __pt__computeVarianceSumTask_int_int_double_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeVarianceSumTask", new Class[] {//####[133]####
                    int.class, int.class, double.class//####[133]####
                });//####[133]####
            } catch (Exception e) {//####[133]####
                e.printStackTrace();//####[133]####
            }//####[133]####
        }//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(0);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(1);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(1);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(1);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(1);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(0);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, double mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0, 1);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(2);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(2);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setTaskIdArgIndexes(0, 1, 2);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(1);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(2);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(1);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0, 1);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(2);//####[133]####
        taskinfo.addDependsOn(mean);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(0);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0, 2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(1);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0, 2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(1);//####[133]####
        taskinfo.addDependsOn(end);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(1, 2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(1, 2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setTaskIdArgIndexes(0);//####[133]####
        taskinfo.addDependsOn(start);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[133]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[133]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[133]####
    }//####[133]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[133]####
        // ensure Method variable is set//####[133]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[133]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[133]####
        }//####[133]####
        taskinfo.setQueueArgIndexes(0, 1, 2);//####[133]####
        taskinfo.setIsPipeline(true);//####[133]####
        taskinfo.setParameters(start, end, mean);//####[133]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[133]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[133]####
    }//####[133]####
    public static double __pt__computeVarianceSumTask(int start, int end, double mean) {//####[133]####
        return computeVarianceSum(start, end, mean);//####[134]####
    }//####[135]####
//####[135]####
//####[137]####
    private static double computeVarianceSum(int start, int end, double mean) {//####[137]####
        int length = end - start;//####[138]####
        if (length <= THRESHOLD) //####[139]####
        {//####[139]####
            double variance = 0;//####[140]####
            for (int i = start; i < end; i++) //####[141]####
            {//####[141]####
                variance += (population[i] - mean) * (population[i] - mean);//####[142]####
            }//####[143]####
            return variance;//####[144]####
        }//####[145]####
        TaskID<Double> left = computeVarianceSumTask(start, start + length / 2, mean);//####[147]####
        double rightSum = computeVarianceSum(start + length / 2, end, mean);//####[150]####
        return getResult(left) + rightSum;//####[152]####
    }//####[153]####
//####[155]####
    private static volatile Method __pt__computeNewSumTask_int_int_method = null;//####[155]####
    private synchronized static void __pt__computeNewSumTask_int_int_ensureMethodVarSet() {//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            try {//####[155]####
                __pt__computeNewSumTask_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeNewSumTask", new Class[] {//####[155]####
                    int.class, int.class//####[155]####
                });//####[155]####
            } catch (Exception e) {//####[155]####
                e.printStackTrace();//####[155]####
            }//####[155]####
        }//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(int start, int end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(int start, int end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, int end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, int end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setTaskIdArgIndexes(0);//####[155]####
        taskinfo.addDependsOn(start);//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, int end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, int end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setQueueArgIndexes(0);//####[155]####
        taskinfo.setIsPipeline(true);//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(int start, TaskID<Integer> end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(int start, TaskID<Integer> end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setTaskIdArgIndexes(1);//####[155]####
        taskinfo.addDependsOn(end);//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, TaskID<Integer> end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[155]####
        taskinfo.addDependsOn(start);//####[155]####
        taskinfo.addDependsOn(end);//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, TaskID<Integer> end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setQueueArgIndexes(0);//####[155]####
        taskinfo.setIsPipeline(true);//####[155]####
        taskinfo.setTaskIdArgIndexes(1);//####[155]####
        taskinfo.addDependsOn(end);//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(int start, BlockingQueue<Integer> end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(int start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setQueueArgIndexes(1);//####[155]####
        taskinfo.setIsPipeline(true);//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, BlockingQueue<Integer> end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setQueueArgIndexes(1);//####[155]####
        taskinfo.setIsPipeline(true);//####[155]####
        taskinfo.setTaskIdArgIndexes(0);//####[155]####
        taskinfo.addDependsOn(start);//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end) {//####[155]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[155]####
        return computeNewSumTask(start, end, new TaskInfo());//####[155]####
    }//####[155]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[155]####
        // ensure Method variable is set//####[155]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[155]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[155]####
        }//####[155]####
        taskinfo.setQueueArgIndexes(0, 1);//####[155]####
        taskinfo.setIsPipeline(true);//####[155]####
        taskinfo.setParameters(start, end);//####[155]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[155]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[155]####
    }//####[155]####
    public static double __pt__computeNewSumTask(int start, int end) {//####[155]####
        double total = 0;//####[156]####
        for (int i = start; i < end; i++) //####[157]####
        {//####[157]####
            total += population[i];//####[158]####
        }//####[159]####
        return total;//####[160]####
    }//####[161]####
//####[161]####
//####[163]####
    public static double computeMeanParaTask(int numThreads, ScheduleType schedule) {//####[163]####
        ParaTask.setScheduling(schedule);//####[164]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[165]####
        if (numThreads == 1) //####[166]####
        {//####[166]####
            double total = 0;//####[167]####
            for (int i = 0; i < population.length; i++) //####[168]####
            {//####[168]####
                total += population[i];//####[169]####
            }//####[170]####
            return total / population.length;//####[171]####
        }//####[172]####
        int lenPerTask = population.length / numThreads;//####[174]####
        TaskIDGroup<Double> group = new TaskIDGroup<Double>(numThreads - 1);//####[175]####
        int start = 0;//####[177]####
        for (int i = 0; i < numThreads - 1; i++) //####[178]####
        {//####[178]####
            TaskID<Double> sum = computeNewSumTask(start, start + lenPerTask);//####[179]####
            start += lenPerTask;//####[180]####
            group.add(sum);//####[181]####
        }//####[182]####
        double sum = 0;//####[184]####
        for (int i = start; i < population.length; ++i) //####[185]####
        {//####[185]####
            sum += population[i];//####[186]####
        }//####[187]####
        try {//####[188]####
            group.waitTillFinished();//####[189]####
            Reduction<Double> red = new Reduction<Double>() {//####[189]####
//####[193]####
                @Override//####[193]####
                public Double combine(Double a, Double b) {//####[193]####
                    return a + b;//####[194]####
                }//####[195]####
            };//####[195]####
            sum += group.reduce(red);//####[198]####
            return sum / population.length;//####[200]####
        } catch (Exception e) {//####[202]####
            throw new RuntimeException(e.getMessage());//####[203]####
        }//####[204]####
    }//####[205]####
}//####[205]####
