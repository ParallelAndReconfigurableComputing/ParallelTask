package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;


@State(Scope.Thread)
public class MeanRecursiveBenchmarkMix {

	@Setup
	public void init() {
		BenchmarksWrapper.initMeanCalc();
	}
		
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads01_mix() {
		BenchmarksWrapper.computeMeanRecursively(1, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads02_mix() {
		BenchmarksWrapper.computeMeanRecursively(2, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads03_mix() {
		BenchmarksWrapper.computeMeanRecursively(3, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads04_mix() {
		BenchmarksWrapper.computeMeanRecursively(4, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads05_mix() {
		BenchmarksWrapper.computeMeanRecursively(5, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads06_mix() {
		BenchmarksWrapper.computeMeanRecursively(6, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads07_mix() {
		BenchmarksWrapper.computeMeanRecursively(7, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads08_mix() {
		BenchmarksWrapper.computeMeanRecursively(8, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads09_mix() {
		BenchmarksWrapper.computeMeanRecursively(9, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads10_mix() {
		BenchmarksWrapper.computeMeanRecursively(10, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads11_mix() {
		BenchmarksWrapper.computeMeanRecursively(11, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads12_mix() {
		BenchmarksWrapper.computeMeanRecursively(12, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads13_mix() {
		BenchmarksWrapper.computeMeanRecursively(13, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads14_mix() {
		BenchmarksWrapper.computeMeanRecursively(14, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads15_mix() {
		BenchmarksWrapper.computeMeanRecursively(15, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads16_mix() {
		BenchmarksWrapper.computeMeanRecursively(16, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads17_mix() {
		BenchmarksWrapper.computeMeanRecursively(17, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads18_mix() {
		BenchmarksWrapper.computeMeanRecursively(18, ScheduleType.MixedSchedule);
	}
}
