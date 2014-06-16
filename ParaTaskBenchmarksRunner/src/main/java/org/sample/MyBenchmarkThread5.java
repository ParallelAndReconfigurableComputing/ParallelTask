package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.ParaTaskWrapper;
import pt.benchmarks.wrapper.VarianceWrapper;


@State(Scope.Thread)
public class MyBenchmarkThread5 {

    double[] population;

    @Setup
    public void init() {
        population = VarianceWrapper.generatePopulation(VarianceConfig.POPULATION_SIZE);
        ParaTaskWrapper.setThreadPoolSize(5);
    }
    
    @GenerateMicroBenchmark
    public double testVarianceParaTaskWithLambda() {
        return VarianceWrapper.varianceParaTaskWithLambda(population, VarianceConfig.THRESHOLD);
    }
    

}
