package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class MeanRecusiveBenchmarkStealing {

	private static int threshold = 90000000 / 32;
	
	@Setup
	public void init() {
		Variance.init();
	}
	
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread01() {
		Variance.meanRecusive(1, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread02() {
		Variance.meanRecusive(2, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread03() {
		Variance.meanRecusive(3, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread04() {
		Variance.meanRecusive(4, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread05() {
		Variance.meanRecusive(5, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread06() {
		Variance.meanRecusive(6, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread07() {
		Variance.meanRecusive(7, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread08() {
		Variance.meanRecusive(8, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread09() {
		Variance.meanRecusive(9, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread10() {
		Variance.meanRecusive(10, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread11() {
		Variance.meanRecusive(11, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread12() {
		Variance.meanRecusive(12, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread13() {
		Variance.meanRecusive(13, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread14() {
		Variance.meanRecusive(14, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread15() {
		Variance.meanRecusive(15, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread16() {
		Variance.meanRecusive(16, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread17() {
		Variance.meanRecusive(17, ScheduleType.WorkStealing, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_stealing_thread18() {
		Variance.meanRecusive(18, ScheduleType.WorkStealing, threshold);
	}
}
