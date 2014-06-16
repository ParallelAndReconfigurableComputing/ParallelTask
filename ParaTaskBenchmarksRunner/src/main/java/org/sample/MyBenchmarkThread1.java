package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.ParaTaskWrapper;
import pt.benchmarks.wrapper.VarianceWrapper;


@State(Scope.Thread)
public class MyBenchmarkThread1 {

    @Setup
    public void init() {
    	VarianceConfig.init();
        ParaTaskWrapper.setThreadPoolSize(1);
    }
    
    @GenerateMicroBenchmark
    public double testVarianceParaTaskWithLambda() {
        return VarianceWrapper.varianceParaTaskWithLambda(VarianceConfig.population, VarianceConfig.THRESHOLD);
    }
    

}
