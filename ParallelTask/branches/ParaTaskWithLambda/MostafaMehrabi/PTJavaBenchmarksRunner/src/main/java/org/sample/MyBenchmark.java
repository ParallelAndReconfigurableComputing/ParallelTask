package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Thread)
public class MyBenchmark {

	@Setup
	public void init() {
		Variance.init();
	}

	@TearDown
	public void tearDown() {
		System.out
				.println("\nTrivialComputation iterations (warmup included) run: "
						+ TrivialComputation.getCurrentValue());
	}

	@GenerateMicroBenchmark
	public double testVarianceImperative() {
		return Variance.varianceImperative();
	}

	@GenerateMicroBenchmark
	public double testVarianceParaTask() {
		return Variance.varianceParaTask();
	}

	@GenerateMicroBenchmark
	public void testTrivialComputation() {
		TrivialComputation.trivalComputationByParaTask();
	}
}
