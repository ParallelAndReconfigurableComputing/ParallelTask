package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.benchmarks.wrapper.BenchmarksWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper;
import pt.benchmarks.wrapper.ParaTaskWrapper.ScheduleType;


@State(Scope.Thread)
public class QueensBenchmarkWithNewtonChaos {

	@Setup
	public void init() {
		ParaTaskWrapper.init();
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads01_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 1, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads02_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 2, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads03_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 3, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads04_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 4, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads05_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 5, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads06_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 6, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads07_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 7, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads08_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 8, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads09_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 9, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads10_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 10, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads11_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 11, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads12_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 12, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads13_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 13, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads14_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 14, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads15_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 15, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads16_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 16, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads17_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 17, ScheduleType.WorkSharing, 2);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n7_threads18_sharing_newton2() {
		BenchmarksWrapper.solveQueensPuzzleWithNewtonChaos(7, 18, ScheduleType.WorkSharing, 2);
	}
}
