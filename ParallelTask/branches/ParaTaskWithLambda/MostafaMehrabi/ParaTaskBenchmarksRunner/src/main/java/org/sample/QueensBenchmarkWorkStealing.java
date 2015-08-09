package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;

@State(Scope.Thread)
public class QueensBenchmarkWorkStealing {

	@Setup
	public void init() {
		ParaTaskWrapper.init();
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_sequential() {
		BenchmarksWrapper.solveQueensPuzzleSequentially(12, 0);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads01_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 1, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads02_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 2, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads03_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 3, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads04_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 4, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads05_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 5, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads06_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 6, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads07_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 7, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads08_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 8, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads09_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 9, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads10_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 10, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads11_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 11, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads12_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 12, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads13_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 13, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads14_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 14, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads15_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 15, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads16_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 16, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads17_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 17, ScheduleType.WorkStealing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads18_stealingthreshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 18, ScheduleType.WorkStealing, 8);
	}
}
