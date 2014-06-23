package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;


@State(Scope.Thread)
public class MeanRecursiveBenchmark {

	@Setup
	public void init() {
		BenchmarksWrapper.initMeanCalc();
	}
		
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads01_sharing() {
		BenchmarksWrapper.computeMeanRecursively(1, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads02_sharing() {
		BenchmarksWrapper.computeMeanRecursively(2, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads03_sharing() {
		BenchmarksWrapper.computeMeanRecursively(3, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads04_sharing() {
		BenchmarksWrapper.computeMeanRecursively(4, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads05_sharing() {
		BenchmarksWrapper.computeMeanRecursively(5, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads06_sharing() {
		BenchmarksWrapper.computeMeanRecursively(6, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads07_sharing() {
		BenchmarksWrapper.computeMeanRecursively(7, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads08_sharing() {
		BenchmarksWrapper.computeMeanRecursively(8, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads09_sharing() {
		BenchmarksWrapper.computeMeanRecursively(9, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads10_sharing() {
		BenchmarksWrapper.computeMeanRecursively(10, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads11_sharing() {
		BenchmarksWrapper.computeMeanRecursively(11, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads12_sharing() {
		BenchmarksWrapper.computeMeanRecursively(12, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads13_sharing() {
		BenchmarksWrapper.computeMeanRecursively(13, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads14_sharing() {
		BenchmarksWrapper.computeMeanRecursively(14, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads15_sharing() {
		BenchmarksWrapper.computeMeanRecursively(15, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads16_sharing() {
		BenchmarksWrapper.computeMeanRecursively(16, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads17_sharing() {
		BenchmarksWrapper.computeMeanRecursively(17, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMeanRecursive_threads18_sharing() {
		BenchmarksWrapper.computeMeanRecursively(18, ScheduleType.WorkSharing);
	}
}
