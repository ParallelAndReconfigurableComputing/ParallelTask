package pt.benchmarks.wrapper;

import pt.benchmarks.TrivialComputation;
import pt.benchmarks.Variance;
import pt.benchmarks.queens.Queens;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;

public class BenchmarksWrapper {
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
		TrivialComputation.trivalComputationByParaTaskWithLambda();
	}
	
	public static int getTrivalComputationCurrentValue() {
		return TrivialComputation.getCurrentValue();
	}
	
	public static void solveQueensPuzzleWithThreshold(int nQueen, int numThreads, 
    		ScheduleType schedule, int threshold) {
		Queens.solveQueensPuzzleWithThreshold(nQueen, numThreads, 
				ParaTaskWrapper.getSchedulingType(schedule), threshold);
	}
	
	public static void solveQueensPuzzleSequentially(int nQueen, int nNewton) {
		Queens.solveQueensPuzzleSequentially(nQueen, nNewton);
	}
	
	public static void solveQueensPuzzleWithNewtonChaos(int nQueen, int numThreads, 
    		ScheduleType schedule, int nNewton) {
		Queens.solveQueensPuzzleWithNewtonChaos(nQueen, numThreads, 
				ParaTaskWrapper.getSchedulingType(schedule), nNewton);
	}
	
	public static void computeMeanParaTask(double[] population, int numThreads, 
			ScheduleType schedule) {
		Variance.computeMeanParaTask(population, numThreads, 
				ParaTaskWrapper.getSchedulingType(schedule));
	}

}
