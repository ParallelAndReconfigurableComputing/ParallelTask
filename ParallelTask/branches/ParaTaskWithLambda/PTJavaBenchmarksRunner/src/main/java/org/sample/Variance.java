package org.sample;//####[1]####
//####[1]####
import java.util.Arrays;//####[3]####
import java.util.Random;//####[4]####
import java.util.concurrent.*;//####[5]####
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
public class Variance {//####[8]####
    static{ParaTask.init();}//####[8]####
    /*  ParaTask helper method to access private/protected slots *///####[8]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[8]####
        if (m.getParameterTypes().length == 0)//####[8]####
            m.invoke(instance);//####[8]####
        else if ((m.getParameterTypes().length == 1))//####[8]####
            m.invoke(instance, arg);//####[8]####
        else //####[8]####
            m.invoke(instance, arg, interResult);//####[8]####
    }//####[8]####
//####[10]####
    private static final Random rand = new Random();//####[10]####
//####[11]####
    private static final int MIN = 1;//####[11]####
//####[12]####
    private static final int MAX = 140;//####[12]####
//####[13]####
    private static final int POPULATION_SIZE = 30000000;//####[13]####
//####[14]####
    public static final int NUMBER_OF_RUNS = 20;//####[14]####
//####[16]####
    public static final long THRESHOLD = 10;//####[16]####
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
        System.out.println("varianceImperative: " + varianceImperative());//####[38]####
        System.out.println("varianceParaTaskWithLambda: " + varianceParaTask());//####[39]####
    }//####[44]####
//####[46]####
    public static int randInt() {//####[46]####
        return rand.nextInt((MAX - MIN) + 1) + MIN;//####[47]####
    }//####[48]####
//####[50]####
    public static double varianceImperative() {//####[50]####
        double average = 0.0;//####[51]####
        for (double p : population) //####[52]####
        {//####[52]####
            average += p;//####[53]####
        }//####[54]####
        average /= population.length;//####[55]####
        double variance = 0.0;//####[57]####
        for (double p : population) //####[58]####
        {//####[58]####
            variance += (p - average) * (p - average);//####[59]####
        }//####[60]####
        return variance / population.length;//####[61]####
    }//####[62]####
//####[64]####
    private static double getResult(TaskID<Double> t) {//####[64]####
        Double result = null;//####[65]####
        try {//####[66]####
            result = t.getReturnResult();//####[67]####
        } catch (Exception e) {//####[68]####
            throw new ParaTaskRuntimeException(e.getMessage());//####[69]####
        }//####[70]####
        return result;//####[71]####
    }//####[72]####
//####[74]####
    public static double varianceParaTask() {//####[74]####
        double total = computeSum(0, population.length);//####[75]####
        double mean = total / population.length;//####[76]####
        double varSum = computeVarianceSum(0, population.length, mean);//####[77]####
        return varSum / population.length;//####[78]####
    }//####[79]####
//####[81]####
    private static double computeSum(int start, int end) {//####[81]####
        int length = end - start;//####[82]####
        if (length <= THRESHOLD) //####[83]####
        {//####[83]####
            double total = 0;//####[84]####
            for (int i = start; i < end; i++) //####[85]####
            {//####[85]####
                total += population[i];//####[86]####
            }//####[87]####
            return total;//####[88]####
        }//####[89]####
        TaskID<Double> left = computeSumTask(start, start + length / 2);//####[91]####
        double rightSum = computeSum(start + length / 2, end);//####[93]####
        return getResult(left) + rightSum;//####[95]####
    }//####[96]####
//####[98]####
    private static volatile Method __pt__computeSumTask_int_int_method = null;//####[98]####
    private synchronized static void __pt__computeSumTask_int_int_ensureMethodVarSet() {//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            try {//####[98]####
                __pt__computeSumTask_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeSumTask", new Class[] {//####[98]####
                    int.class, int.class//####[98]####
                });//####[98]####
            } catch (Exception e) {//####[98]####
                e.printStackTrace();//####[98]####
            }//####[98]####
        }//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(int start, int end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(int start, int end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, int end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, int end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setTaskIdArgIndexes(0);//####[98]####
        taskinfo.addDependsOn(start);//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, int end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, int end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setQueueArgIndexes(0);//####[98]####
        taskinfo.setIsPipeline(true);//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(int start, TaskID<Integer> end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(int start, TaskID<Integer> end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setTaskIdArgIndexes(1);//####[98]####
        taskinfo.addDependsOn(end);//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, TaskID<Integer> end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[98]####
        taskinfo.addDependsOn(start);//####[98]####
        taskinfo.addDependsOn(end);//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, TaskID<Integer> end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setQueueArgIndexes(0);//####[98]####
        taskinfo.setIsPipeline(true);//####[98]####
        taskinfo.setTaskIdArgIndexes(1);//####[98]####
        taskinfo.addDependsOn(end);//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(int start, BlockingQueue<Integer> end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(int start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setQueueArgIndexes(1);//####[98]####
        taskinfo.setIsPipeline(true);//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, BlockingQueue<Integer> end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setQueueArgIndexes(1);//####[98]####
        taskinfo.setIsPipeline(true);//####[98]####
        taskinfo.setTaskIdArgIndexes(0);//####[98]####
        taskinfo.addDependsOn(start);//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end) {//####[98]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[98]####
        return computeSumTask(start, end, new TaskInfo());//####[98]####
    }//####[98]####
    private static TaskID<Double> computeSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskInfo taskinfo) {//####[98]####
        // ensure Method variable is set//####[98]####
        if (__pt__computeSumTask_int_int_method == null) {//####[98]####
            __pt__computeSumTask_int_int_ensureMethodVarSet();//####[98]####
        }//####[98]####
        taskinfo.setQueueArgIndexes(0, 1);//####[98]####
        taskinfo.setIsPipeline(true);//####[98]####
        taskinfo.setParameters(start, end);//####[98]####
        taskinfo.setMethod(__pt__computeSumTask_int_int_method);//####[98]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[98]####
    }//####[98]####
    public static double __pt__computeSumTask(int start, int end) {//####[98]####
        return computeSum(start, end);//####[99]####
    }//####[100]####
//####[100]####
//####[102]####
    private static volatile Method __pt__computeVarianceSumTask_int_int_double_method = null;//####[102]####
    private synchronized static void __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet() {//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            try {//####[102]####
                __pt__computeVarianceSumTask_int_int_double_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__computeVarianceSumTask", new Class[] {//####[102]####
                    int.class, int.class, double.class//####[102]####
                });//####[102]####
            } catch (Exception e) {//####[102]####
                e.printStackTrace();//####[102]####
            }//####[102]####
        }//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setTaskIdArgIndexes(0);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setTaskIdArgIndexes(1);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(1);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(1);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(1);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(0);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, double mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, double mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0, 1);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setTaskIdArgIndexes(2);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(2);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setTaskIdArgIndexes(0, 1, 2);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(1);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(2);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(1);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, TaskID<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0, 1);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(2);//####[102]####
        taskinfo.addDependsOn(mean);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(0);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, int end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0, 2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(1);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, TaskID<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0, 2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(1);//####[102]####
        taskinfo.addDependsOn(end);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(int start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(1, 2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(TaskID<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(1, 2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setTaskIdArgIndexes(0);//####[102]####
        taskinfo.addDependsOn(start);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean) {//####[102]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[102]####
        return computeVarianceSumTask(start, end, mean, new TaskInfo());//####[102]####
    }//####[102]####
    private static TaskID<Double> computeVarianceSumTask(BlockingQueue<Integer> start, BlockingQueue<Integer> end, BlockingQueue<Double> mean, TaskInfo taskinfo) {//####[102]####
        // ensure Method variable is set//####[102]####
        if (__pt__computeVarianceSumTask_int_int_double_method == null) {//####[102]####
            __pt__computeVarianceSumTask_int_int_double_ensureMethodVarSet();//####[102]####
        }//####[102]####
        taskinfo.setQueueArgIndexes(0, 1, 2);//####[102]####
        taskinfo.setIsPipeline(true);//####[102]####
        taskinfo.setParameters(start, end, mean);//####[102]####
        taskinfo.setMethod(__pt__computeVarianceSumTask_int_int_double_method);//####[102]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[102]####
    }//####[102]####
    public static double __pt__computeVarianceSumTask(int start, int end, double mean) {//####[102]####
        return computeVarianceSum(start, end, mean);//####[103]####
    }//####[104]####
//####[104]####
//####[106]####
    private static double computeVarianceSum(int start, int end, double mean) {//####[106]####
        int length = end - start;//####[107]####
        if (length <= THRESHOLD) //####[108]####
        {//####[108]####
            double variance = 0;//####[109]####
            for (int i = start; i < end; i++) //####[110]####
            {//####[110]####
                variance += (population[i] - mean) * (population[i] - mean);//####[111]####
            }//####[112]####
            return variance;//####[113]####
        }//####[114]####
        TaskID<Double> left = computeVarianceSumTask(start, start + length / 2, mean);//####[116]####
        double rightSum = computeVarianceSum(start + length / 2, end, mean);//####[119]####
        return getResult(left) + rightSum;//####[121]####
    }//####[122]####
}//####[122]####
