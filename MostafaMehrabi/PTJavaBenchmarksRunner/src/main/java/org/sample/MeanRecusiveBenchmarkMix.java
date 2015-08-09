package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class MeanRecusiveBenchmarkMix {

	private static int threshold = 90000000 / 32;
	
	@Setup
	public void init() {
		Variance.init();
	}
		
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread01() {
		Variance.meanRecusive(1, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread02() {
		Variance.meanRecusive(2, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread03() {
		Variance.meanRecusive(3, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread04() {
		Variance.meanRecusive(4, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread05() {
		Variance.meanRecusive(5, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread06() {
		Variance.meanRecusive(6, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread07() {
		Variance.meanRecusive(7, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread08() {
		Variance.meanRecusive(8, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread09() {
		Variance.meanRecusive(9, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread10() {
		Variance.meanRecusive(10, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread11() {
		Variance.meanRecusive(11, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread12() {
		Variance.meanRecusive(12, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread13() {
		Variance.meanRecusive(13, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread14() {
		Variance.meanRecusive(14, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread15() {
		Variance.meanRecusive(15, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread16() {
		Variance.meanRecusive(16, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread17() {
		Variance.meanRecusive(17, ScheduleType.MixedSchedule, threshold);
	}
	
	@GenerateMicroBenchmark
	public void testRecusiveMean_mix_thread18() {
		Variance.meanRecusive(18, ScheduleType.MixedSchedule, threshold);
	}
}
