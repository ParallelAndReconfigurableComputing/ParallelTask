package org.sample;

import pt.benchmarks.wrapper.VarianceWrapper;

public class VarianceConfig {

    public static final int POPULATION_SIZE = 30000000;
    public static final int THRESHOLD = 10;
    

	public static double[] population;

	public static double[] dataForTrivalComputation;
	
	public static void init() {
		population = VarianceWrapper
				.generatePopulation(VarianceConfig.POPULATION_SIZE);
		dataForTrivalComputation = VarianceWrapper
				.generatePopulation(VarianceConfig.POPULATION_SIZE);
	}
}
