package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;


@State(Scope.Thread)
public class MeanBenchmarkMix {

	@Setup
	public void init() {
		VarianceConfig.init();
	}
		
	@GenerateMicroBenchmark
	public void testMean_threads01_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 1, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads02_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 2, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads03_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 3, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads04_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 4, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads05_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 5, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads06_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 6, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads07_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 7, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads08_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 8, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads09_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 9, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads10_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 10, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads11_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 11, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads12_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 12, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads13_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 13, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads14_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 14, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads15_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 15, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads16_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 16, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads17_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 17, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_threads18_mix() {
		BenchmarksWrapper.computeMeanParaTask(VarianceConfig.population, 18, ScheduleType.MixedSchedule);
	}
}
