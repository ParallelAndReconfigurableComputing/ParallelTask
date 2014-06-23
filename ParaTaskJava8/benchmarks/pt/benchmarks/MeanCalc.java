package pt.benchmarks;

import java.util.Random;

public class MeanCalc {
    private static final Random rand = new Random();
    
    private static final int MIN = 1;
    
    private static final int MAX = 140;
    
    public static final int POPULATION_SIZE = 90000000;
    
    private static long THRESHOLD = 1000;
    
    public static void init() {
        MeanCalc.population = MeanCalc.generatePopulation(POPULATION_SIZE);
    }
    
    public static double[] generatePopulation(int populationSize) {
        double[] data = new double[populationSize];
        for (int i = 0 ; i < populationSize ; ++i) {
            data[i] = MeanCalc.randInt();
        }
        return data;
    }
    
    private static double[] population;
    
    public static void main(String... args) {
        MeanCalc.population = MeanCalc.generatePopulation(POPULATION_SIZE);
        System.out.println(("meanRecursive 1 thread: " + (MeanCalc.meanRecursive(1 ,3000))));
        System.out.println(("meanRecursive 2 thread: " + (MeanCalc.meanRecursive(2 ,3000))));
    }
    
    private static int randInt() {
        return (rand.nextInt((((MAX) - (MIN)) + 1))) + (MIN);
    }
    
    public static double varianceImperative() {
        double average = 0.0;
        for (double p : MeanCalc.population) {
            average += p;
        }
        average /= MeanCalc.population.length;
        double variance = 0.0;
        for (double p : MeanCalc.population) {
            variance += (p - average) * (p - average);
        }
        return variance / (MeanCalc.population.length);
    }
    
    public static double varianceParaTask() {
        double total = MeanCalc.computeSum(0 ,MeanCalc.population.length);
        double mean = total / (MeanCalc.population.length);
        double varSum = MeanCalc.computeVarianceSum(0 ,MeanCalc.population.length ,mean);
        return varSum / (MeanCalc.population.length);
    }
    
    public static void varianceWithThreshold(int threshold) {
        MeanCalc.THRESHOLD = threshold;
        MeanCalc.varianceParaTask();
    }
    
    public static double meanRecursive(int numThreads, int threshold) {
        if (numThreads == 1) {
            double total = 0;
            for (int i = 0 ; i < (MeanCalc.population.length) ; i++) {
                total += MeanCalc.population[i];
            }
            return total / (MeanCalc.population.length);
        } 
        MeanCalc.THRESHOLD = threshold;
        double total = MeanCalc.computeSum(0 ,MeanCalc.population.length);
        return total / (MeanCalc.population.length);
    }
    
    private static double computeSum(int start, int end) {
        int length = end - start;
        if (length <= (MeanCalc.THRESHOLD)) {
            double total = 0;
            for (int i = start ; i < end ; i++) {
                total += MeanCalc.population[i];
            }
            return total;
        } 
        pt.runtime.TaskID<Double> __leftSum__ = pt.runtime.Task.asTask(() -> computeSum(start ,(start + (length / 2)))).start();
        double rightSum = MeanCalc.computeSum((start + (length / 2)) ,end);
        final double leftSum = __leftSum__.getResult();
        return leftSum + rightSum;
    }
    
    private static double computeVarianceSum(int start, int end, double mean) {
        int length = end - start;
        if (length <= (MeanCalc.THRESHOLD)) {
            double variance = 0;
            for (int i = start ; i < end ; i++) {
                variance += ((MeanCalc.population[i]) - mean) * ((MeanCalc.population[i]) - mean);
            }
            return variance;
        } 
        pt.runtime.TaskID<Double> __leftSum__ = pt.runtime.Task.asTask(() -> computeVarianceSum(start ,(start + (length / 2)) ,mean)).start();
        double rightSum = MeanCalc.computeVarianceSum((start + (length / 2)) ,end ,mean);
        final double leftSum = __leftSum__.getResult();
        return leftSum + rightSum;
    }
    
}

