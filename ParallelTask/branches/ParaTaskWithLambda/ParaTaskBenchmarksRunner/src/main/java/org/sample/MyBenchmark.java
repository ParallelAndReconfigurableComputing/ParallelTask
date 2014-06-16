package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import pt.benchmarks.wrapper.VarianceWrapper;

@State(Scope.Thread)
public class MyBenchmark {

	@Setup
	public void init() {
		VarianceConfig.init();
	}
	
	@TearDown
	public void tearDown() {
		System.out.println("\nTrivialComputation iterations (warmup included) run: " + 
				VarianceWrapper.getTrivalComputationCurrentValue());
	}

	@GenerateMicroBenchmark
	public double testVarianceImperative() {
		return VarianceWrapper.varianceImperative(VarianceConfig.population);
	}

	@GenerateMicroBenchmark
	public double testVarianceStreams() {
		return VarianceWrapper.varianceStreams(VarianceConfig.population);
	}

	@GenerateMicroBenchmark
	public double testVarianceForkJoin() {
		return VarianceWrapper.varianceForkJoin(VarianceConfig.population,
				VarianceConfig.THRESHOLD);
	}

	@GenerateMicroBenchmark
	public double testVarianceParaTaskWithLambda() {
		return VarianceWrapper.varianceParaTaskWithLambda(
				VarianceConfig.population, VarianceConfig.THRESHOLD);
	}

	@GenerateMicroBenchmark
	public void testTrivialComputation() {
		VarianceWrapper
				.trivalComputationByParaTaskWithLambda(VarianceConfig.dataForTrivalComputation);
	}
}
