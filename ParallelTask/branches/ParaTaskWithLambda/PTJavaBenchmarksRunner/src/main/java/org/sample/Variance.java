package org.sample;//####[1]####
//####[1]####
import java.util.Random;//####[3]####
import pt.runtime.TaskIDGroup;//####[5]####
import pt.runtime.ParaTask.ScheduleType;//####[6]####
import pt.runtime.ParaTask.ThreadPoolType;//####[7]####
//####[7]####
//-- ParaTask related imports//####[7]####
import pt.runtime.*;//####[7]####
import java.util.concurrent.ExecutionException;//####[7]####
import java.util.concurrent.locks.*;//####[7]####
import java.lang.reflect.*;//####[7]####
import pt.runtime.GuiThread;//####[7]####
import java.util.concurrent.BlockingQueue;//####[7]####
import java.util.ArrayList;//####[7]####
import java.util.List;//####[7]####
//####[7]####
public class Variance {//####[9]####
    static{ParaTask.init();}//####[9]####
    /*  ParaTask helper method to access private/protected slots *///####[9]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[9]####
        if (m.getParameterTypes().length == 0)//####[9]####
            m.invoke(instance);//####[9]####
        else if ((m.getParameterTypes().length == 1))//####[9]####
            m.invoke(instance, arg);//####[9]####
        else //####[9]####
            m.invoke(instance, arg, interResult);//####[9]####
    }//####[9]####
//####[11]####
    private static final Random rand = new Random();//####[11]####
//####[12]####
    private static final int MIN = 1;//####[12]####
//####[13]####
    private static final int MAX = 140;//####[13]####
//####[14]####
    private static final int POPULATION_SIZE = 300000000;//####[14]####
//####[16]####
    public static long THRESHOLD = 1000;//####[16]####
//####[18]####
    public static void init() {//####[18]####
        population = generatePopulation(POPULATION_SIZE);//####[19]####
    }//####[20]####
//####[22]####
    public static double[] generatePopulation(int populationSize) {//####[22]####
        ParaTask.init();//####[23]####
        double[] data = new double[populationSize];//####[25]####
        for (int i = 0; i < populationSize; ++i) //####[26]####
        {//####[26]####
            data[i] = randInt();//####[27]####
        }//####[28]####
        return data;//####[29]####
    }//####[30]####
//####[32]####
    private static double[] population;//####[32]####
//####[34]####
    public static void main(String... args) {//####[34]####
        population = generatePopulation(POPULATION_SIZE);//####[36]####
        computeMeanParaTask(2, ScheduleType.WorkSharing);//####[38]####
        computeMeanParaTask(7, ScheduleType.WorkSharing);//####[39]####
    }//####[47]####
//####[49]####
    public static int randInt() {//####[49]####
        return rand.nextInt((MAX - MIN) + 1) + MIN;//####[50]####
    }//####[51]####
//####[53]####
    public static double varianceImperative() {//####[53]####
        double average = 0.0;//####[54]####
        for (double p : population) //####[55]####
        {//####[55]####
            average += p;//####[56]####
        }//####[57]####
        average /= population.length;//####[58]####
        double variance = 0.0;//####[60]####
        for (double p : population) //####[61]####
        {//####[61]####
            variance += (p - average) * (p - average);//####[62]####
        }//####[63]####
        return variance / population.length;//####[64]####
    }//####[65]####
//####[67]####
    private static double getResult(TaskID<Double> t) {//####[67]####
        Double result = null;//####[68]####
        try {//####[69]####
            result = t.getReturnResult();//####[70]####
        } catch (Exception e) {//####[71]####
            throw new ParaTaskRuntimeException(e.getMessage());//####[72]####
        }//####[73]####
        return result;//####[74]####
    }//####[75]####
//####[77]####
    public static double varianceParaTask() {//####[77]####
        double total = computeSum(0, population.length);//####[78]####
        double mean = total / population.length;//####[79]####
        double varSum = computeVarianceSum(0, population.length, mean);//####[80]####
        return varSum / population.length;//####[81]####
    }//####[82]####
//####[84]####
    public static void varianceWithThreshold(int numThreads, ScheduleType schedule, int threshold) {//####[85]####
        ParaTask.setScheduling(schedule);//####[86]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[87]####
        THRESHOLD = threshold;//####[88]####
        varianceParaTask();//####[89]####
    }//####[90]####
//####[92]####
    public static double meanRecusive(int numThreads, ScheduleType schedule, int threshold) {//####[93]####
        ParaTask.setScheduling(schedule);//####[94]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ONEOFF, numThreads);//####[95]####
        if (numThreads == 1) //####[97]####
        {//####[97]####
            double total = 0;//####[98]####
            for (int i = 0; i < population.length; i++) //####[99]####
            {//####[99]####
                total += population[i];//####[100]####
            }//####[101]####
            return total / population.length;//####[102]####
        }//####[103]####
        THRESHOLD = threshold;//####[105]####
        double total = computeSum(0, population.length);//####[106]####
        return total / population.length;//####[107]####
    }//####[108]####
//####[110]####
    private static double computeSum(int start, int end) {//####[110]####
        int length = end - start;//####[111]####
        if (length <= THRESHOLD) //####[112]####
        {//####[112]####
            double total = 0;//####[113]####
            for (int i = start; i < end; i++) //####[114]####
            {//####[114]####
                total += population[i];//####[115]####
            }//####[116]####
            return total;//####[117]####
        }//####[118]####
        TaskID<Double> left = computeSumTask(start, start + length / 2);//####[120]####
        double rightSum = computeSum(start + length / 2, end);//####[122]####
        return getResult(left) + rightSum;//####[124]####
    }//####[125]####
//####[127]####
    private static volatile Method __pt__computeSumTask_int_int_method = null;//####[127]####
    private synchronized static void __pt__computeSumTask_int_int_ensureMethodVarSet() {//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            try {//####[127]####
                __pt__computeSumTask_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeSumTask", new Class[] {//####[127]####
                    int.class, int.class//####[127]####
                });//####[127]####
            } catch (Exception e) {//####[127]####
                e.printStackTrace();//####[127]####
            }//####[127]####
        }//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(int start, int end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(int start, int end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, int end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, int end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setTaskIdArgIndexes(0);//####[127]####
        taskinfo.addDependsOn(start);//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, int end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, int end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setQueueArgIndexes(0);//####[127]####
        taskinfo.setIsPipeline(true);//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(int start, TaskID<Integer> end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(int start, TaskID<Integer> end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setTaskIdArgIndexes(1);//####[127]####
        taskinfo.addDependsOn(end);//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, TaskID<Integer> end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[127]####
        taskinfo.addDependsOn(start);//####[127]####
        taskinfo.addDependsOn(end);//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, TaskID<Integer> end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setQueueArgIndexes(0);//####[127]####
        taskinfo.setIsPipeline(true);//####[127]####
        taskinfo.setTaskIdArgIndexes(1);//####[127]####
        taskinfo.addDependsOn(end);//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(int start, BlockingQueue<Integer> end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(int start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setQueueArgIndexes(1);//####[127]####
        taskinfo.setIsPipeline(true);//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, BlockingQueue<Integer> end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setQueueArgIndexes(1);//####[127]####
        taskinfo.setIsPipeline(true);//####[127]####
        taskinfo.setTaskIdArgIndexes(0);//####[127]####
        taskinfo.addDependsOn(start);//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end) {//####[127]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[127]####
        return computeSumTask(start, end, new TaskInfo());//####[127]####
    }//####[127]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[127]####
        // ensure Method variable is set//####[127]####
        if (__pt__computeSumTask_int_int_method == null) {//####[127]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[127]####
        }//####[127]####
        taskinfo.setQueueArgIndexes(0, 1);//####[127]####
        taskinfo.setIsPipeline(true);//####[127]####
        taskinfo.setParameters(start, end);//####[127]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[127]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[127]####
    }//####[127]####
    public static double __pt__computeSumTask(int start, int end) {//####[127]####
        return computeSum(start, end);//####[128]####
    }//####[129]####
//####[129]####
//####[131]####
    private static volatile Method __pt__computeVarianceSumTask_int_int_double_method = null;//####[131]####
    private synchronized static void __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet() {//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            try {//####[131]####
                __pt__computeVarianceSumTask_int_int_double_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeVarianceSumTask", new Class[] {//####[131]####
                    int.class, int.class, double.class//####[131]####
                });//####[131]####
            } catch (Exception e) {//####[131]####
                e.printStackTrace();//####[131]####
            }//####[131]####
        }//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(0);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(1);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(1);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(1);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(1);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(0);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, double mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0, 1);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(2);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(2);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setTaskIdArgIndexes(0, 1, 2);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(1);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(2);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(1);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0, 1);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(2);//####[131]####
        taskinfo.addDependsOn(mean);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(0);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0, 2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(1);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0, 2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(1);//####[131]####
        taskinfo.addDependsOn(end);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(1, 2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(1, 2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setTaskIdArgIndexes(0);//####[131]####
        taskinfo.addDependsOn(start);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[131]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[131]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[131]####
    }//####[131]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[131]####
        // ensure Method variable is set//####[131]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[131]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[131]####
        }//####[131]####
        taskinfo.setQueueArgIndexes(0, 1, 2);//####[131]####
        taskinfo.setIsPipeline(true);//####[131]####
        taskinfo.setParameters(start, end, mean);//####[131]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[131]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[131]####
    }//####[131]####
    public static double __pt__computeVarianceSumTask(int start, int end, double mean) {//####[131]####
        return computeVarianceSum(start, end, mean);//####[132]####
    }//####[133]####
//####[133]####
//####[135]####
    private static double computeVarianceSum(int start, int end, double mean) {//####[135]####
        int length = end - start;//####[136]####
        if (length <= THRESHOLD) //####[137]####
        {//####[137]####
            double variance = 0;//####[138]####
            for (int i = start; i < end; i++) //####[139]####
            {//####[139]####
                variance += (population[i] - mean) * (population[i] - mean);//####[140]####
            }//####[141]####
            return variance;//####[142]####
        }//####[143]####
        TaskID<Double> left = computeVarianceSumTask(start, start + length / 2, mean);//####[145]####
        double rightSum = computeVarianceSum(start + length / 2, end, mean);//####[148]####
        return getResult(left) + rightSum;//####[150]####
    }//####[151]####
//####[153]####
    private static volatile Method __pt__computeNewSumTask_int_int_method = null;//####[153]####
    private synchronized static void __pt__computeNewSumTask_int_int_ensureMethodVarSet() {//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            try {//####[153]####
                __pt__computeNewSumTask_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeNewSumTask", new Class[] {//####[153]####
                    int.class, int.class//####[153]####
                });//####[153]####
            } catch (Exception e) {//####[153]####
                e.printStackTrace();//####[153]####
            }//####[153]####
        }//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(int start, int end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(int start, int end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, int end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, int end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setTaskIdArgIndexes(0);//####[153]####
        taskinfo.addDependsOn(start);//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, int end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, int end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setQueueArgIndexes(0);//####[153]####
        taskinfo.setIsPipeline(true);//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(int start, TaskID<Integer> end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(int start, TaskID<Integer> end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setTaskIdArgIndexes(1);//####[153]####
        taskinfo.addDependsOn(end);//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, TaskID<Integer> end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[153]####
        taskinfo.addDependsOn(start);//####[153]####
        taskinfo.addDependsOn(end);//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, TaskID<Integer> end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setQueueArgIndexes(0);//####[153]####
        taskinfo.setIsPipeline(true);//####[153]####
        taskinfo.setTaskIdArgIndexes(1);//####[153]####
        taskinfo.addDependsOn(end);//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(int start, BlockingQueue<Integer> end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(int start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setQueueArgIndexes(1);//####[153]####
        taskinfo.setIsPipeline(true);//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, BlockingQueue<Integer> end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setQueueArgIndexes(1);//####[153]####
        taskinfo.setIsPipeline(true);//####[153]####
        taskinfo.setTaskIdArgIndexes(0);//####[153]####
        taskinfo.addDependsOn(start);//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end) {//####[153]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[153]####
        return computeNewSumTask(start, end, new TaskInfo());//####[153]####
    }//####[153]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[153]####
        // ensure Method variable is set//####[153]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[153]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[153]####
        }//####[153]####
        taskinfo.setQueueArgIndexes(0, 1);//####[153]####
        taskinfo.setIsPipeline(true);//####[153]####
        taskinfo.setParameters(start, end);//####[153]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[153]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[153]####
    }//####[153]####
    public static double __pt__computeNewSumTask(int start, int end) {//####[153]####
        double total = 0;//####[154]####
        for (int i = start; i < end; i++) //####[155]####
        {//####[155]####
            total += population[i];//####[156]####
        }//####[157]####
        return total;//####[158]####
    }//####[159]####
//####[159]####
//####[161]####
    public static double computeMeanParaTask(int numThreads, ScheduleType schedule) {//####[161]####
        ParaTask.setScheduling(schedule);//####[162]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[163]####
        if (numThreads == 1) //####[164]####
        {//####[164]####
            double total = 0;//####[165]####
            for (int i = 0; i < population.length; i++) //####[166]####
            {//####[166]####
                total += population[i];//####[167]####
            }//####[168]####
            return total / population.length;//####[169]####
        }//####[170]####
        int lenPerTask = population.length / numThreads;//####[172]####
        TaskIDGroup<Double> group = new TaskIDGroup<Double>(numThreads - 1);//####[173]####
        int start = 0;//####[175]####
        for (int i = 0; i < numThreads - 1; i++) //####[176]####
        {//####[176]####
            TaskID<Double> sum = computeNewSumTask(start, start + lenPerTask);//####[177]####
            start += lenPerTask;//####[178]####
            group.add(sum);//####[179]####
        }//####[180]####
        double sum = 0;//####[182]####
        for (int i = start; i < population.length; ++i) //####[183]####
        {//####[183]####
            sum += population[i];//####[184]####
        }//####[185]####
        try {//####[186]####
            group.waitTillFinished();//####[187]####
            Reduction<Double> red = new Reduction<Double>() {//####[187]####
//####[191]####
                @Override//####[191]####
                public Double combine(Double a, Double b) {//####[191]####
                    return a + b;//####[192]####
                }//####[193]####
            };//####[193]####
            sum += group.reduce(red);//####[196]####
            return sum / population.length;//####[198]####
        } catch (Exception e) {//####[200]####
            throw new RuntimeException(e.getMessage());//####[201]####
        }//####[202]####
    }//####[203]####
}//####[203]####
