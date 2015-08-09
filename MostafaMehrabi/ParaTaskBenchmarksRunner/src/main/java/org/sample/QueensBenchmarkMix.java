package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;


@State(Scope.Thread)
public class QueensBenchmarkMix {

	@Setup
	public void init() {
		ParaTaskWrapper.init();
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_sequential() {
		BenchmarksWrapper.solveQueensPuzzleSequentially(12, 0);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads01_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 1, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads02_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 2, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads03_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 3, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads04_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 4, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads05_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 5, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads06_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 6, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads07_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 7, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads08_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 8, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads09_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 9, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads10_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 10, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads11_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 11, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads12_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 12, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads13_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 13, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads14_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 14, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads15_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 15, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads16_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 16, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads17_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 17, ScheduleType.MixedSchedule, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads18_mix_threshold8() {
		BenchmarksWrapper.solveQueensPuzzleWithThreshold(12, 18, ScheduleType.MixedSchedule, 8);
	}
}
