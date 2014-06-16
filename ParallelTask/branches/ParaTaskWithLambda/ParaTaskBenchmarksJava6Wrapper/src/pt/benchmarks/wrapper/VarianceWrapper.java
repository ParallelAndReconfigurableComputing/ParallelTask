package pt.benchmarks.wrapper;

import pt.benchmarks.TrivalComputation;
import pt.benchmarks.Variance;

public class VarianceWrapper {
	public static double[] generatePopulation(int populationSize) {
		return Variance.generatePopulation(populationSize);
	}
	
	public static double varianceImperative(double[] population) {
		return Variance.varianceImperative(population);
	}
	
	public static double varianceStreams(double[] population) {
		return Variance.varianceStreams(population);
	}
	
	public static double varianceForkJoin(double[] population, int threshold) {
		return Variance.varianceForkJoin(population, threshold);
	}
	
	public static double varianceParaTaskWithLambda(double[] population, int threshold) {
		return Variance.varianceParaTaskWithLambda(population, threshold);
	}
	
	public static void trivalComputationByParaTaskWithLambda(double[] population) {
		TrivalComputation.trivalComputationByParaTaskWithLambda();
	}
	
	public static int getTrivalComputationCurrentValue() {
		return TrivalComputation.getCurrentValue();
	}
}
