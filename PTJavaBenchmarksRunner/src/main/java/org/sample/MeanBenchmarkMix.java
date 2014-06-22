package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class MeanBenchmarkMix {

	@Setup
	public void init() {
		Variance.init();
	}
	
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread01() {
		Variance.computeMeanParaTask(1, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread02() {
		Variance.computeMeanParaTask(2, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread03() {
		Variance.computeMeanParaTask(3, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread04() {
		Variance.computeMeanParaTask(4, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread05() {
		Variance.computeMeanParaTask(5, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread06() {
		Variance.computeMeanParaTask(6, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread07() {
		Variance.computeMeanParaTask(7, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread08() {
		Variance.computeMeanParaTask(8, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread09() {
		Variance.computeMeanParaTask(9, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread10() {
		Variance.computeMeanParaTask(10, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread11() {
		Variance.computeMeanParaTask(11, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread12() {
		Variance.computeMeanParaTask(12, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread13() {
		Variance.computeMeanParaTask(13, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread14() {
		Variance.computeMeanParaTask(14, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread15() {
		Variance.computeMeanParaTask(15, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread16() {
		Variance.computeMeanParaTask(16, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread17() {
		Variance.computeMeanParaTask(17, ScheduleType.MixedSchedule);
	}
	
	@GenerateMicroBenchmark
	public void testMean_mix_thread18() {
		Variance.computeMeanParaTask(18, ScheduleType.MixedSchedule);
	}
}
