package pt.benchmarks;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import static pt.runtime.Task.*;
import pt.runtime.*;
import pt.runtime.ParaTask.ScheduleType;
import pt.runtime.ParaTask.ThreadPoolType;

public class Variance {

    private static final Random rand = new Random();
    private static final int MIN = 1;
    private static final int MAX = 140;
    private static final int POPULATION_SIZE = 90_000_000;
    public static final int NUMBER_OF_RUNS = 20;
    
    //public static final long THRESHOLD = 1_000_000;
    
    
    public static double[] generatePopulation(int populationSize) {
    	ParaTask.init();
        return DoubleStream.generate(Variance::randInt).limit(populationSize).toArray();
    }
    
    public static void main(String... args) {
// generate a population with different ages
        double[] population = generatePopulation(POPULATION_SIZE);
        int threshold = 10;
        
        
        System.out.println("varianceStreams: " + varianceStreams(population));
        System.out.println("varianceImperative: " + varianceImperative(population));
        System.out.println("varianceForkJoin: " + varianceForkJoin(population, threshold));
        System.out.println("varianceParaTaskWithLambda: " + varianceParaTaskWithLambda(population, threshold));
        
        //System.out.println("Imperative version done in: " + measurePerf(Variance::varianceImperative, population) + " msecs" );
        //System.out.println("Parallel streams version done in : " + measurePerf(Variance::varianceStreams, population) + " msecs");
        //System.out.println("ForkJoin version done in : " + measurePerf(Variance::varianceForkJoin, population) + " msecs");
    }

    public static int randInt() {
        return rand.nextInt((MAX - MIN) + 1) + MIN;
    }

    public static double varianceImperative(double[] population){
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

    public static double varianceStreams(double[] population) {
        double average = Arrays.stream(population).parallel().average().orElse(0.0);
        double variance = Arrays.stream(population).parallel()
                                .map(p -> (p - average) * (p - average))
                                .sum() / population.length;
        return variance;
    }
    
    public static double varianceParaTaskWithLambda(double[] population, int threshold) {
    	double total = computeSumByParaTaskWithLambda(population, 0, population.length, threshold);
    	double mean = total / population.length;
    	double varSum = computeVarianceSumByParaTaskWithLambda(population, 0, population.length, mean, threshold);
    	return varSum / population.length;
    }
    
    private static double computeSumByParaTaskWithLambda(double[] numbers, int start, int end, int threshold) {
    	int length = end - start;
        if (length <= threshold) {
        	double total = 0;
            for (int i = start; i < end; i++) {
                total += numbers[i];
            }
            return total;
        }

        TaskID<Double> left = asTask(() -> computeSumByParaTaskWithLambda(numbers, 
        		start, start + length/2, threshold)).start();
        
        double rightSum = computeSumByParaTaskWithLambda(numbers, start + length/2, end, threshold);
        
    	return left.getResult() + rightSum;
    }
    
    private static double computeVarianceSumByParaTaskWithLambda(double[] numbers, int start, int end, double mean, int threshold) {
    	int length = end - start;
        if (length <= threshold) {
        	double variance = 0;
            for (int i = start; i < end; i++) {
                variance += (numbers[i] - mean) * (numbers[i] - mean);
            }
            return variance;
        }

        TaskID<Double> left = asTask(() -> computeVarianceSumByParaTaskWithLambda(numbers, 
        		start, start + length/2, mean, threshold)).start();
        
        double rightSum = computeVarianceSumByParaTaskWithLambda(numbers, start + length/2, end, mean, threshold);
        
    	return left.getResult() + rightSum;
    }
    
    public static double varianceForkJoin(double[] population, int threshold){
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        double total = forkJoinPool.invoke(new ForkJoinCalculator(population, new SequentialCalculator() {
            @Override
            public double computeSequentially(double[] numbers, int start, int end) {
                double total = 0;
                for (int i = start; i < end; i++) {
                    total += numbers[i];
                }
                return total;
            }
        }, threshold));
        final double average = total / population.length;
        double variance = forkJoinPool.invoke(new ForkJoinCalculator(population, new SequentialCalculator() {
            @Override
            public double computeSequentially(double[] numbers, int start, int end) {
                double variance = 0;
                for (int i = start; i < end; i++) {
                    variance += (numbers[i] - average) * (numbers[i] - average);
                }
                return variance;
            }
        }, threshold));
        return variance / population.length;
    }

    public static <T, R> long measurePerf(Function<T, R> f, T input) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            long start = System.nanoTime();
            R result = f.apply(input);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result: " + result);
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }

    public interface SequentialCalculator {
        double computeSequentially(double[] numbers, int start, int end);
    }

    public static class ForkJoinCalculator extends RecursiveTask<Double> {

        private final SequentialCalculator sequentialCalculator;
        private final double[] numbers;
        private final int start;
        private final int end;
        private final int threshold;

        public ForkJoinCalculator(double[] numbers, SequentialCalculator sequentialCalculator, int threshold) {
            this(numbers, 0, numbers.length, sequentialCalculator, threshold);
        }

        private ForkJoinCalculator(double[] numbers, int start, int end, SequentialCalculator sequentialCalculator, int threshold) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
            this.sequentialCalculator = sequentialCalculator;
            this.threshold = threshold;
        }

        @Override
        protected Double compute() {
            int length = end - start;
            if (length <= threshold) {
                return sequentialCalculator.computeSequentially(numbers, start, end);
            }
            ForkJoinCalculator leftTask = new ForkJoinCalculator(numbers, start, start + length/2, sequentialCalculator, threshold);
            leftTask.fork();
            ForkJoinCalculator rightTask = new ForkJoinCalculator(numbers, start + length/2, end, sequentialCalculator, threshold);
            Double rightResult = rightTask.compute();
            Double leftResult = leftTask.join();
            return leftResult + rightResult;
        }
    }
    
    private static double computeNewSumTask(double[] population, int start, int end) {
    	double total = 0;
        for (int i = start; i < end; i++) {
            total += population[i];
        }
        return total;
    }
    
    public static double computeMeanParaTask(double[] population, int numThreads, ScheduleType schedule) {

    	ParaTask.setScheduling(schedule);
    	ParaTask.setThreadPoolSize(ThreadPoolType.ALL, numThreads);
    	
    	if(numThreads == 1) {
    		double total = 0;
            for (int i = 0; i < population.length; i++) {
                total += population[i];
            }
            return total / population.length;
    	}
    	
    	int lenPerTask = population.length / numThreads;
    	TaskIDGroup<Double> group = new TaskIDGroup<Double>(numThreads - 1);
    	
    	int start = 0;
    	for(int i = 0; i < numThreads - 1; i++) {
    		final int s = start;
    		TaskID<Double> sum = asTask(() -> computeNewSumTask(population, s, s + lenPerTask))
    				.start();
    		start += lenPerTask;
    		group.add(sum);
    	}

    	double sum = 0;
    	for(int i = start; i < population.length; ++i) {
    		sum += population[i];
    	}
    	try {
    		group.waitTillFinished();
    		
    		Reduction<Double> red = new Reduction<Double>() {
				@Override
				public Double combine(Double a, Double b) {
					return a + b;
				}
            };    		    
            
            sum += group.reduce(red);
                        
            return sum / population.length;
            
    	} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
    }
}
