package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;


@State(Scope.Thread)
public class MeanRecursiveBenchmarkStealing {

	@Setup
	public void init() {
		BenchmarksWrapper.initMeanCalc();
	}
		
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads01_stealing() {
		BenchmarksWrapper.computeMeanRecursively(1, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads02_stealing() {
		BenchmarksWrapper.computeMeanRecursively(2, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads03_stealing() {
		BenchmarksWrapper.computeMeanRecursively(3, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads04_stealing() {
		BenchmarksWrapper.computeMeanRecursively(4, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads05_stealing() {
		BenchmarksWrapper.computeMeanRecursively(5, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads06_stealing() {
		BenchmarksWrapper.computeMeanRecursively(6, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads07_stealing() {
		BenchmarksWrapper.computeMeanRecursively(7, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads08_stealing() {
		BenchmarksWrapper.computeMeanRecursively(8, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads09_stealing() {
		BenchmarksWrapper.computeMeanRecursively(9, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads10_stealing() {
		BenchmarksWrapper.computeMeanRecursively(10, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads11_stealing() {
		BenchmarksWrapper.computeMeanRecursively(11, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads12_stealing() {
		BenchmarksWrapper.computeMeanRecursively(12, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads13_stealing() {
		BenchmarksWrapper.computeMeanRecursively(13, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads14_stealing() {
		BenchmarksWrapper.computeMeanRecursively(14, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads15_stealing() {
		BenchmarksWrapper.computeMeanRecursively(15, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads16_stealing() {
		BenchmarksWrapper.computeMeanRecursively(16, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads17_stealing() {
		BenchmarksWrapper.computeMeanRecursively(17, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads18_stealing() {
		BenchmarksWrapper.computeMeanRecursively(18, ScheduleType.WorkStealing);
	}
}
