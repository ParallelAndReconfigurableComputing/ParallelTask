package pt.variance;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import static pt.runtime.Task.*;
import pt.runtime.*;

public class Variance {

    private static final Random rand = new Random();
    private static final int MIN = 1;
    private static final int MAX = 140;
    private static final int POPULATION_SIZE = 30_000_000;
    public static final int NUMBER_OF_RUNS = 20;
    
    public static final long THRESHOLD = 1_000_000;
    
    public static double[] generatePopulation(int populationSize) {
    	ParaTask.init();
        return DoubleStream.generate(Variance::randInt).limit(populationSize).toArray();
    }
    
    public static void main(String... args) {
// generate a population with different ages
        double[] population = generatePopulation(POPULATION_SIZE);
        
        System.out.println("varianceStreams: " + varianceStreams(population));
        System.out.println("varianceImperative: " + varianceImperative(population));
        System.out.println("varianceForkJoin: " + varianceForkJoin(population));
        System.out.println("varianceParaTaskWithLambda: " + varianceParaTaskWithLambda(population));
        
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
    
    public static double varianceParaTaskWithLambda(double[] population) {
    	double total = computeSumByParaTaskWithLambda(population, 0, population.length);
    	double mean = total / population.length;
    	double varSum = computeVarianceSumByParaTaskWithLambda(population, 0, population.length, mean);
    	return varSum / population.length;
    }
    
    private static double computeSumByParaTaskWithLambda(double[] numbers, int start, int end) {
    	int length = end - start;
        if (length <= THRESHOLD) {
            return computeSumSequentially(numbers, start, end);
        }

        TaskID<Double> left = asTask(() -> computeSumByParaTaskWithLambda(numbers, 
        		start, start + length/2)).start();
        
        double rightSum = computeSumByParaTaskWithLambda(numbers, start + length/2, end);
        
    	return left.getResult() + rightSum;
    }
    
    private static double computeVarianceSumByParaTaskWithLambda(double[] numbers, int start, int end, double mean) {
    	int length = end - start;
        if (length <= THRESHOLD) {
            return computeVarianceSumSequentially(numbers, start, end, mean);
        }

        TaskID<Double> left = asTask(() -> computeVarianceSumByParaTaskWithLambda(numbers, 
        		start, start + length/2, mean)).start();
        
        double rightSum = computeVarianceSumByParaTaskWithLambda(numbers, start + length/2, end, mean);
        
    	return left.getResult() + rightSum;
    }

    private static double computeSumSequentially(double[] numbers, int start, int end) {
    	double total = 0;
        for (int i = start; i < end; i++) {
            total += numbers[i];
        }
        return total;
    }
    
    private static double computeVarianceSumSequentially(double[] numbers, int start, int end, double average) {
    	double variance = 0;
        for (int i = start; i < end; i++) {
            variance += (numbers[i] - average) * (numbers[i] - average);
        }
        return variance;
    }
    
    public static double varianceForkJoin(double[] population){
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
        }));
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
        }));
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

        public ForkJoinCalculator(double[] numbers, SequentialCalculator sequentialCalculator) {
            this(numbers, 0, numbers.length, sequentialCalculator);
        }

        private ForkJoinCalculator(double[] numbers, int start, int end, SequentialCalculator sequentialCalculator) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
            this.sequentialCalculator = sequentialCalculator;
        }

        @Override
        protected Double compute() {
            int length = end - start;
            if (length <= THRESHOLD) {
                return sequentialCalculator.computeSequentially(numbers, start, end);
            }
            ForkJoinCalculator leftTask = new ForkJoinCalculator(numbers, start, start + length/2, sequentialCalculator);
            leftTask.fork();
            ForkJoinCalculator rightTask = new ForkJoinCalculator(numbers, start + length/2, end, sequentialCalculator);
            Double rightResult = rightTask.compute();
            Double leftResult = leftTask.join();
            return leftResult + rightResult;
        }
    }
}
