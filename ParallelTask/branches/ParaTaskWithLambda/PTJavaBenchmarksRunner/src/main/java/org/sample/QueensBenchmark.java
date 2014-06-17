package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.sample.queens.Queens;

import pt.runtime.ParaTask;
import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class QueensBenchmark {

	@Setup
	public void init() {
		ParaTask.init();
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads01_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 1, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads02_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 2, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads03_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 3, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads04_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 4, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads05_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 5, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads06_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 6, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads07_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 7, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads08_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 8, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads09_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 9, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads10_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 10, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads11_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 11, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads12_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 12, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads13_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 13, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads14_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 14, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads15_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 15, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads16_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 16, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads17_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 17, ScheduleType.WorkSharing, 8);
	}
	
	@GenerateMicroBenchmark
	public void testQueens_n12_threads18_sharing_threshold8() {
		Queens.solveQueensPuzzleWithThreshold(12, 18, ScheduleType.WorkSharing, 8);
	}
}
