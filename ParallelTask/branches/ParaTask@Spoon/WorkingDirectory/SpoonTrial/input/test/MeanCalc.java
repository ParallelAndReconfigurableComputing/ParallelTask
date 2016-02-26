package test;

import java.util.Random;

import pt.annotations.Future;
import pt.annotations.Task;


public class MeanCalc {

    private static final Random rand = new Random();
    private static final int MIN = 1;
    private static final int MAX = 140;
    private static final int POPULATION_SIZE = 90000000;

    public static long THRESHOLD = 1000;
    
    public static void init() {
    	population = generatePopulation(POPULATION_SIZE);
    }
    
    public static double[] generatePopulation(int populationSize) {

    	
    	double[] data = new double[populationSize];
    	for(int i = 0; i < populationSize; ++i) {
    		data[i] = randInt();
    	}
    	return data;
    }
    
    private static double[] population;
    
    public static void main(String... args) {
    	// generate a population with different ages
        population = generatePopulation(POPULATION_SIZE);
        
        
        System.out.println("meanRecursive 1 thread: " + meanRecursive(1, 3000));
        System.out.println("meanRecursive 2 threads: " + meanRecursive(2, 3000));
        
    }

    private static int randInt() {
        return rand.nextInt((MAX - MIN) + 1) + MIN;
    }

    public static double varianceImperative(){
        double average = 0.0;
        for(double p: population){
            average += p;
        }
        average /= population.length;

        double variance = 0.0;
        for(double p: population){
            variance += (p - average) * (p - average);
        }
        return variance / population.length;
    }

    
    public static double varianceParaTask() {
    	double total = computeSum(0, population.length);
    	double mean = total / population.length;
    	double varSum = computeVarianceSum(0, population.length, mean);
    	return varSum / population.length;
    }
    
    public static void varianceWithThreshold(int threshold) {
    	THRESHOLD = threshold;
    	varianceParaTask();
    }

    public static double meanRecursive(int numThreads, int threshold) {
    	
    	if(numThreads == 1) {
    		double total = 0;
            for (int i = 0; i < population.length; i++) {
                total += population[i];
            }
            return total / population.length;
    	}
    	    	
    	THRESHOLD = threshold;
    	double total = computeSum(0, population.length);
    	return total / population.length;
    }
    
    @Task
    private static double computeSum(int start, int end) {
    	int length = end - start;
        if (length <= THRESHOLD) {
        	double total = 0;
            for (int i = start; i < end; i++) {
                total += population[i];
            }
            return total;
        }

        @Future
        double leftSum = computeSum(start, start + length/2);
        
        double rightSum = computeSum(start + length/2, end);
        
    	return leftSum + rightSum;
    }    
        
    @Task
    private static double computeVarianceSum(int start, int end, double mean) {
    	int length = end - start;
        if (length <= THRESHOLD) {
        	double variance = 0;
            for (int i = start; i < end; i++) {
                variance += (population[i] - mean) * (population[i] - mean);
            }
            return variance;
        }

        @Future
        double leftSum = computeVarianceSum(start, start + length/2, mean);
        
        double rightSum = computeVarianceSum(start + length/2, end, mean);
        
    	return leftSum + rightSum;
    }
        
    
}
