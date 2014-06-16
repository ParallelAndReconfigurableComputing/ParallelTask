package pt.benchmarks.wrapper;

import pt.variance.Variance;

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
	
	public static double varianceForkJoin(double[] population) {
		return Variance.varianceForkJoin(population);
	}
}
