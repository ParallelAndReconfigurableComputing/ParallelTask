package org.sample;

import pt.benchmarks.wrapper.BenchmarksWrapper;

public class VarianceConfig {

    public static final int POPULATION_SIZE = 90000000;
    public static final int THRESHOLD = 10;
    

	public static double[] population;

	public static double[] dataForTrivalComputation;
	
	public static void init() {
		population = BenchmarksWrapper
				.generatePopulation(VarianceConfig.POPULATION_SIZE);
		dataForTrivalComputation = BenchmarksWrapper
				.generatePopulation(VarianceConfig.POPULATION_SIZE);
	}
}
