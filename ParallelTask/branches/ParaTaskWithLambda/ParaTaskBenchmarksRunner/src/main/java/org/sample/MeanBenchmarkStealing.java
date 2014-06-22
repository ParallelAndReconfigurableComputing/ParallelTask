package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;


@State(Scope.Thread)
public class MeanBenchmarkStealing {

	@Setup
	public void init() {
		VarianceConfig.init();
	}
		
	@GenerateMicroBenchmark
	public void testMean_threads01_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 1, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads02_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 2, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads03_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 3, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads04_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 4, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads05_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 5, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads06_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 6, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads07_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 7, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads08_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 8, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads09_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 9, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads10_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 10, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads11_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 11, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads12_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 12, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads13_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 13, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads14_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 14, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads15_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 15, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads16_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 16, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads17_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 17, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads18_stealing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 18, ScheduleType.WorkStealing);
	}
}
