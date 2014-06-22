package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class MeanBenchmarkStealing {

	@Setup
	public void init() {
		Variance.init();
	}
	
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread01() {
		Variance.computeMeanParaTask(1, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread02() {
		Variance.computeMeanParaTask(2, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread03() {
		Variance.computeMeanParaTask(3, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread04() {
		Variance.computeMeanParaTask(4, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread05() {
		Variance.computeMeanParaTask(5, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread06() {
		Variance.computeMeanParaTask(6, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread07() {
		Variance.computeMeanParaTask(7, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread08() {
		Variance.computeMeanParaTask(8, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread09() {
		Variance.computeMeanParaTask(9, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread10() {
		Variance.computeMeanParaTask(10, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread11() {
		Variance.computeMeanParaTask(11, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread12() {
		Variance.computeMeanParaTask(12, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread13() {
		Variance.computeMeanParaTask(13, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread14() {
		Variance.computeMeanParaTask(14, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread15() {
		Variance.computeMeanParaTask(15, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread16() {
		Variance.computeMeanParaTask(16, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread17() {
		Variance.computeMeanParaTask(17, ScheduleType.WorkStealing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_stealing_thread18() {
		Variance.computeMeanParaTask(18, ScheduleType.WorkStealing);
	}
}
