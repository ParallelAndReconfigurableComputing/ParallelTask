package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;


@State(Scope.Thread)
public class MyBenchmark {


    @Setup
    public void init() {
        Variance.init();
    }

    @GenerateMicroBenchmark
    public double testVarianceImperative() {
        return Variance.varianceImperative();
    }
    
    @GenerateMicroBenchmark
    public double testVarianceParaTask() {
        return Variance.varianceParaTask();
    }
    

}
