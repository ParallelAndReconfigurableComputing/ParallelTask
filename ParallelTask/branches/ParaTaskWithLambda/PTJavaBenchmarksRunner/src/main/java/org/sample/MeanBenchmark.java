package org.sample;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import pt.runtime.ParaTask.ScheduleType;

@State(Scope.Thread)
public class MeanBenchmark {

	@Setup
	public void init() {
		Variance.init();
	}
	
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread01() {
		Variance.computeMeanParaTask(1, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread02() {
		Variance.computeMeanParaTask(2, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread03() {
		Variance.computeMeanParaTask(3, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread04() {
		Variance.computeMeanParaTask(4, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread05() {
		Variance.computeMeanParaTask(5, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread06() {
		Variance.computeMeanParaTask(6, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread07() {
		Variance.computeMeanParaTask(7, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread08() {
		Variance.computeMeanParaTask(8, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread09() {
		Variance.computeMeanParaTask(9, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread10() {
		Variance.computeMeanParaTask(10, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread11() {
		Variance.computeMeanParaTask(11, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread12() {
		Variance.computeMeanParaTask(12, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread13() {
		Variance.computeMeanParaTask(13, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread14() {
		Variance.computeMeanParaTask(14, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread15() {
		Variance.computeMeanParaTask(15, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread16() {
		Variance.computeMeanParaTask(16, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread17() {
		Variance.computeMeanParaTask(17, ScheduleType.WorkSharing);
	}
	
	@GenerateMicroBenchmark
	public void testMean_sharing_thread18() {
		Variance.computeMeanParaTask(18, ScheduleType.WorkSharing);
	}
}
