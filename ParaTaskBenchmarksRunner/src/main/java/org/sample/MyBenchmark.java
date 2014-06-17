package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import pt.benchmarks.wrapper.BenchmarksWrapper;

@State(Scope.Thread)
public class MyBenchmark {

	@Setup
	public void init() {
		VarianceConfig.init();
	}
	
	@TearDown
	public void tearDown() {
		System.out.println("\nTrivialComputation iterations (warmup included) run: " + 
				BenchmarksWrapper.getTrivalComputationCurrentValue());
	}

	@GenerateMicroBenchmark
	public double testVarianceImperative() {
		return BenchmarksWrapper.varianceImperative(VarianceConfig.population);
	}

	@GenerateMicroBenchmark
	public double testVarianceStreams() {
		return BenchmarksWrapper.varianceStreams(VarianceConfig.population);
	}

	@GenerateMicroBenchmark
	public double testVarianceForkJoin() {
		return BenchmarksWrapper.varianceForkJoin(VarianceConfig.population,
				VarianceConfig.THRESHOLD);
	}

	@GenerateMicroBenchmark
	public double testVarianceParaTaskWithLambda() {
		return BenchmarksWrapper.varianceParaTaskWithLambda(
				VarianceConfig.population, VarianceConfig.THRESHOLD);
	}

	@GenerateMicroBenchmark
	public void testTrivialComputation() {
		BenchmarksWrapper
				.trivalComputationByParaTaskWithLambda(VarianceConfig.dataForTrivalComputation);
	}
}
