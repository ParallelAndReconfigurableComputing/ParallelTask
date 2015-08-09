package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class VarianceBenchmark {

	private static int threshold = 1000000;
	
	@Setup
	public void init() {
		Variance.init();
	}
	
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread01() {
		Variance.varianceWithThreshold(1, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread02() {
		Variance.varianceWithThreshold(2, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread03() {
		Variance.varianceWithThreshold(3, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread04() {
		Variance.varianceWithThreshold(4, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread05() {
		Variance.varianceWithThreshold(5, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread06() {
		Variance.varianceWithThreshold(6, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread07() {
		Variance.varianceWithThreshold(7, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread08() {
		Variance.varianceWithThreshold(8, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread09() {
		Variance.varianceWithThreshold(9, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread10() {
		Variance.varianceWithThreshold(10, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread11() {
		Variance.varianceWithThreshold(11, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread12() {
		Variance.varianceWithThreshold(12, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread13() {
		Variance.varianceWithThreshold(13, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread14() {
		Variance.varianceWithThreshold(14, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread15() {
		Variance.varianceWithThreshold(15, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread16() {
		Variance.varianceWithThreshold(16, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread17() {
		Variance.varianceWithThreshold(17, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testVariance_sharing_thread18() {
		Variance.varianceWithThreshold(18, ScheduleType.WorkSharing, threshold);
	}
}
