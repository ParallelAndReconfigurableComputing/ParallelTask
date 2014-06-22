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
    private static final int POPULATION_SIZE = 60000000;//####[16]####
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
        System.out.println("varianceImperative: " + varianceImperative());//####[40]####
        System.out.println("varianceParaTaskWithLambda: " + varianceParaTask());//####[41]####
    }//####[46]####
//####[48]####
    public static int randInt() {//####[48]####
        return rand.nextInt((MAX - MIN) + 1) + MIN;//####[49]####
    }//####[50]####
//####[52]####
    public static double varianceImperative() {//####[52]####
        double average = 0.0;//####[53]####
        for (double p : population) //####[54]####
        {//####[54]####
            average += p;//####[55]####
        }//####[56]####
        average /= population.length;//####[57]####
        double variance = 0.0;//####[59]####
        for (double p : population) //####[60]####
        {//####[60]####
            variance += (p - average) * (p - average);//####[61]####
        }//####[62]####
        return variance / population.length;//####[63]####
    }//####[64]####
//####[66]####
    private static double getResult(TaskID<Double> t) {//####[66]####
        Double result = null;//####[67]####
        try {//####[68]####
            result = t.getReturnResult();//####[69]####
        } catch (Exception e) {//####[70]####
            throw new ParaTaskRuntimeException(e.getMessage());//####[71]####
        }//####[72]####
        return result;//####[73]####
    }//####[74]####
//####[76]####
    public static double varianceParaTask() {//####[76]####
        double total = computeSum(0, population.length);//####[77]####
        double mean = total / population.length;//####[78]####
        double varSum = computeVarianceSum(0, population.length, mean);//####[79]####
        return varSum / population.length;//####[80]####
    }//####[81]####
//####[83]####
    public static void varianceWithThreshold(int numThreads, ScheduleType schedule, int threshold) {//####[84]####
        ParaTask.setScheduling(schedule);//####[85]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[86]####
        THRESHOLD = threshold;//####[87]####
        varianceParaTask();//####[88]####
    }//####[89]####
//####[91]####
    private static double computeSum(int start, int end) {//####[91]####
        int length = end - start;//####[92]####
        if (length <= THRESHOLD) //####[93]####
        {//####[93]####
            double total = 0;//####[94]####
            for (int i = start; i < end; i++) //####[95]####
            {//####[95]####
                total += population[i];//####[96]####
            }//####[97]####
            return total;//####[98]####
        }//####[99]####
        TaskID<Double> left = computeSumTask(start, start + length / 2);//####[101]####
        double rightSum = computeSum(start + length / 2, end);//####[103]####
        return getResult(left) + rightSum;//####[105]####
    }//####[106]####
//####[108]####
    private static volatile Method __pt__computeSumTask_int_int_method = null;//####[108]####
    private synchronized static void __pt__computeSumTask_int_int_ensureMethodVarSet() {//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            try {//####[108]####
                __pt__computeSumTask_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeSumTask", new Class[] {//####[108]####
                    int.class, int.class//####[108]####
                });//####[108]####
            } catch (Exception e) {//####[108]####
                e.printStackTrace();//####[108]####
            }//####[108]####
        }//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(int start, int end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(int start, int end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, int end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, int end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setTaskIdArgIndexes(0);//####[108]####
        taskinfo.addDependsOn(start);//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, int end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, int end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setQueueArgIndexes(0);//####[108]####
        taskinfo.setIsPipeline(true);//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(int start, TaskID<Integer> end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(int start, TaskID<Integer> end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setTaskIdArgIndexes(1);//####[108]####
        taskinfo.addDependsOn(end);//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, TaskID<Integer> end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[108]####
        taskinfo.addDependsOn(start);//####[108]####
        taskinfo.addDependsOn(end);//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, TaskID<Integer> end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setQueueArgIndexes(0);//####[108]####
        taskinfo.setIsPipeline(true);//####[108]####
        taskinfo.setTaskIdArgIndexes(1);//####[108]####
        taskinfo.addDependsOn(end);//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(int start, BlockingQueue<Integer> end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(int start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setQueueArgIndexes(1);//####[108]####
        taskinfo.setIsPipeline(true);//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, BlockingQueue<Integer> end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setQueueArgIndexes(1);//####[108]####
        taskinfo.setIsPipeline(true);//####[108]####
        taskinfo.setTaskIdArgIndexes(0);//####[108]####
        taskinfo.addDependsOn(start);//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end) {//####[108]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[108]####
        return computeSumTask(start, end, new TaskInfo());//####[108]####
    }//####[108]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[108]####
        // ensure Method variable is set//####[108]####
        if (__pt__computeSumTask_int_int_method == null) {//####[108]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[108]####
        }//####[108]####
        taskinfo.setQueueArgIndexes(0, 1);//####[108]####
        taskinfo.setIsPipeline(true);//####[108]####
        taskinfo.setParameters(start, end);//####[108]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[108]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[108]####
    }//####[108]####
    public static double __pt__computeSumTask(int start, int end) {//####[108]####
        return computeSum(start, end);//####[109]####
    }//####[110]####
//####[110]####
//####[112]####
    private static volatile Method __pt__computeVarianceSumTask_int_int_double_method = null;//####[112]####
    private synchronized static void __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet() {//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            try {//####[112]####
                __pt__computeVarianceSumTask_int_int_double_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeVarianceSumTask", new Class[] {//####[112]####
                    int.class, int.class, double.class//####[112]####
                });//####[112]####
            } catch (Exception e) {//####[112]####
                e.printStackTrace();//####[112]####
            }//####[112]####
        }//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setTaskIdArgIndexes(0);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setTaskIdArgIndexes(1);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(1);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(1);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(1);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(0);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, double mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0, 1);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setTaskIdArgIndexes(2);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(2);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setTaskIdArgIndexes(0, 1, 2);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(1);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(2);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(1);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0, 1);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(2);//####[112]####
        taskinfo.addDependsOn(mean);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(0);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0, 2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(1);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0, 2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(1);//####[112]####
        taskinfo.addDependsOn(end);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(1, 2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(1, 2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setTaskIdArgIndexes(0);//####[112]####
        taskinfo.addDependsOn(start);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[112]####
    }//####[112]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[112]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0, 1, 2);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setParameters(start, end, mean);//####[112]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    public static double __pt__computeVarianceSumTask(int start, int end, double mean) {//####[112]####
        return computeVarianceSum(start, end, mean);//####[113]####
    }//####[114]####
//####[114]####
//####[116]####
    private static double computeVarianceSum(int start, int end, double mean) {//####[116]####
        int length = end - start;//####[117]####
        if (length <= THRESHOLD) //####[118]####
        {//####[118]####
            double variance = 0;//####[119]####
            for (int i = start; i < end; i++) //####[120]####
            {//####[120]####
                variance += (population[i] - mean) * (population[i] - mean);//####[121]####
            }//####[122]####
            return variance;//####[123]####
        }//####[124]####
        TaskID<Double> left = computeVarianceSumTask(start, start + length / 2, mean);//####[126]####
        double rightSum = computeVarianceSum(start + length / 2, end, mean);//####[129]####
        return getResult(left) + rightSum;//####[131]####
    }//####[132]####
//####[134]####
    private static volatile Method __pt__computeNewSumTask_int_int_method = null;//####[134]####
    private synchronized static void __pt__computeNewSumTask_int_int_ensureMethodVarSet() {//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            try {//####[134]####
                __pt__computeNewSumTask_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeNewSumTask", new Class[] {//####[134]####
                    int.class, int.class//####[134]####
                });//####[134]####
            } catch (Exception e) {//####[134]####
                e.printStackTrace();//####[134]####
            }//####[134]####
        }//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(int start, int end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(int start, int end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, int end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, int end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setTaskIdArgIndexes(0);//####[134]####
        taskinfo.addDependsOn(start);//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, int end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, int end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setQueueArgIndexes(0);//####[134]####
        taskinfo.setIsPipeline(true);//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(int start, TaskID<Integer> end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(int start, TaskID<Integer> end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setTaskIdArgIndexes(1);//####[134]####
        taskinfo.addDependsOn(end);//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, TaskID<Integer> end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[134]####
        taskinfo.addDependsOn(start);//####[134]####
        taskinfo.addDependsOn(end);//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, TaskID<Integer> end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setQueueArgIndexes(0);//####[134]####
        taskinfo.setIsPipeline(true);//####[134]####
        taskinfo.setTaskIdArgIndexes(1);//####[134]####
        taskinfo.addDependsOn(end);//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(int start, BlockingQueue<Integer> end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(int start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setQueueArgIndexes(1);//####[134]####
        taskinfo.setIsPipeline(true);//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, BlockingQueue<Integer> end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setQueueArgIndexes(1);//####[134]####
        taskinfo.setIsPipeline(true);//####[134]####
        taskinfo.setTaskIdArgIndexes(0);//####[134]####
        taskinfo.addDependsOn(start);//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end) {//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return computeNewSumTask(start, end, new TaskInfo());//####[134]####
    }//####[134]####
    private static TaskID<Double> computeNewSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[134]####
        // ensure Method variable is set//####[134]####
        if (__pt__computeNewSumTask_int_int_method == null) {//####[134]####
            __pt__computeNewSumTask_int_int_ensureMethodVarSet();//####[134]####
        }//####[134]####
        taskinfo.setQueueArgIndexes(0, 1);//####[134]####
        taskinfo.setIsPipeline(true);//####[134]####
        taskinfo.setParameters(start, end);//####[134]####
        taskinfo.setMethod(__pt__computeNewSumTask_int_int_method);//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    public static double __pt__computeNewSumTask(int start, int end) {//####[134]####
        double total = 0;//####[135]####
        for (int i = start; i < end; i++) //####[136]####
        {//####[136]####
            total += population[i];//####[137]####
        }//####[138]####
        return total;//####[139]####
    }//####[140]####
//####[140]####
//####[142]####
    public static double computeMeanParaTask(int numThreads, ScheduleType schedule) {//####[142]####
        ParaTask.setScheduling(schedule);//####[143]####
        ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);//####[144]####
        if (numThreads == 1) //####[145]####
        {//####[145]####
            double total = 0;//####[146]####
            for (int i = 0; i < population.length; i++) //####[147]####
            {//####[147]####
                total += population[i];//####[148]####
            }//####[149]####
            return total;//####[150]####
        }//####[151]####
        int lenPerTask = population.length / numThreads;//####[153]####
        TaskIDGroup<Double> group = new TaskIDGroup<Double>(numThreads - 1);//####[154]####
        int start = 0;//####[156]####
        for (int i = 0; i < numThreads - 1; i++) //####[157]####
        {//####[157]####
            TaskID<Double> sum = computeNewSumTask(start, start + lenPerTask);//####[158]####
            start += lenPerTask;//####[159]####
            group.add(sum);//####[160]####
        }//####[161]####
        double sum = 0;//####[163]####
        for (int i = start; i < population.length; ++i) //####[164]####
        {//####[164]####
            sum += population[i];//####[165]####
        }//####[166]####
        try {//####[167]####
            group.waitTillFinished();//####[168]####
            Reduction<Double> red = new Reduction<Double>() {//####[168]####
//####[172]####
                @Override//####[172]####
                public Double combine(Double a, Double b) {//####[172]####
                    return a + b;//####[173]####
                }//####[174]####
            };//####[174]####
            sum += group.reduce(red);//####[177]####
            return sum / population.length;//####[178]####
        } catch (Exception e) {//####[180]####
            throw new RuntimeException(e.getMessage());//####[181]####
        }//####[182]####
    }//####[183]####
}//####[183]####
