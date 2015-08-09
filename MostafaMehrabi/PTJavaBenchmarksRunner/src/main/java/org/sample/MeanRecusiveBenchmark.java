package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class MeanRecusiveBenchmark {

	private static int threshold = 90000000 / 32;
	
	@Setup
	public void init() {
		Variance.init();
	}
	
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread01() {
		Variance.meanRecusive(1, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread02() {
		Variance.meanRecusive(2, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread03() {
		Variance.meanRecusive(3, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread04() {
		Variance.meanRecusive(4, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread05() {
		Variance.meanRecusive(5, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread06() {
		Variance.meanRecusive(6, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread07() {
		Variance.meanRecusive(7, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread08() {
		Variance.meanRecusive(8, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread09() {
		Variance.meanRecusive(9, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread10() {
		Variance.meanRecusive(10, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread11() {
		Variance.meanRecusive(11, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread12() {
		Variance.meanRecusive(12, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread13() {
		Variance.meanRecusive(13, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread14() {
		Variance.meanRecusive(14, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread15() {
		Variance.meanRecusive(15, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread16() {
		Variance.meanRecusive(16, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread17() {
		Variance.meanRecusive(17, ScheduleType.WorkSharing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_sharing_thread18() {
		Variance.meanRecusive(18, ScheduleType.WorkSharing, threshold);
	}
}
