package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.VarianceWrapper;

@State(Scope.Thread)
public class MyBenchmark {

    private static final int POPULATION_SIZE = 30000000;
    double[] population;

    @Setup
    public void init() {
        population = VarianceWrapper.generatePopulation(POPULATION_SIZE);
    }

    @GenerateMicroBenchmark
    public double testVarianceImperative() {
        return VarianceWrapper.varianceImperative(population);
    }

    @GenerateMicroBenchmark
    public double testVarianceStreams() {
        return VarianceWrapper.varianceStreams(population);
    }

    @GenerateMicroBenchmark
    public double testVarianceForkJoin() {
        return VarianceWrapper.varianceForkJoin(population);
    }

}
