package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;


@State(Scope.Thread)
public class MeanBenchmark {

	@Setup
	public void init() {
		VarianceConfig.init();
	}
		
	@GenerateMicroBenchmark
	public void testMean_threads01_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 1, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads02_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 2, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads03_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 3, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads04_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 4, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads05_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 5, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads06_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 6, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads07_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 7, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads08_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 8, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads09_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 9, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads10_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 10, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads11_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 11, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads12_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 12, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads13_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 13, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads14_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 14, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads15_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 15, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads16_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 16, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads17_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 17, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads18_sharing() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 18, ScheduleType.WorkSharing);
	}
}
