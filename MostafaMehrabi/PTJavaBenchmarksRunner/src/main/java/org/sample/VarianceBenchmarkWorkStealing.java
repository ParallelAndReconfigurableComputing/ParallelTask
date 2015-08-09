package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class VarianceBenchmarkWorkStealing {

	private static int threshold = 1000000;
	
	@Setup
	public void init() {
		Variance.init();
	}
	
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread01() {
		Variance.varianceWithThreshold(1, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread02() {
		Variance.varianceWithThreshold(2, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread03() {
		Variance.varianceWithThreshold(3, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread04() {
		Variance.varianceWithThreshold(4, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread05() {
		Variance.varianceWithThreshold(5, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread06() {
		Variance.varianceWithThreshold(6, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread07() {
		Variance.varianceWithThreshold(7, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread08() {
		Variance.varianceWithThreshold(8, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread09() {
		Variance.varianceWithThreshold(9, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread10() {
		Variance.varianceWithThreshold(10, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread11() {
		Variance.varianceWithThreshold(11, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread12() {
		Variance.varianceWithThreshold(12, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread13() {
		Variance.varianceWithThreshold(13, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread14() {
		Variance.varianceWithThreshold(14, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread15() {
		Variance.varianceWithThreshold(15, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread16() {
		Variance.varianceWithThreshold(16, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread17() {
		Variance.varianceWithThreshold(17, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_stealing_thread18() {
		Variance.varianceWithThreshold(18, ScheduleType.WorkStealing, threshold);
	}
}
