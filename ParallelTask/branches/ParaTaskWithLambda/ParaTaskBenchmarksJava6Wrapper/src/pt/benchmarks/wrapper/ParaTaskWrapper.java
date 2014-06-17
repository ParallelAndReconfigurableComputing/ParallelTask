package pt.benchmarks.wrapper;

import pt.runtime.ParaTask;
import pt.runtime.ParaTask.ThreadPoolType;

public class ParaTaskWrapper {
	
	public static enum ScheduleType { 
		WorkSharing, 
		WorkStealing,
		MixedSchedule 
	};
	
	public static void init() {
		ParaTask.init();
	}
	
	public static void setThreadPoolSize(int size) {
		ParaTask.setThreadPoolSize(ThreadPoolType.ALL, size);
	}
	
	
	public static ParaTask.ScheduleType getSchedulingType(ScheduleType type) {
		ParaTask.ScheduleType t = ParaTask.ScheduleType.WorkSharing;
		if(type == ScheduleType.WorkStealing)
			t = ParaTask.ScheduleType.WorkStealing;
		if(type == ScheduleType.MixedSchedule)
			t = ParaTask.ScheduleType.MixedSchedule;
		return t;
	}
	
	public static void setScheduling(ScheduleType type) {
		ParaTask.setScheduling(getSchedulingType(type));
	}
}
