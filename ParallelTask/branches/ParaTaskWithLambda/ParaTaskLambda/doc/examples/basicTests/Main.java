package benchmarks.basicTests;

import pt.runtime.*;
import pt.runtime.ParaTask.ScheduleType;

import java.lang.Thread;

public class Main {
	public static void main(String[] args){
		try{
			//ParaTask.init(ScheduleType.MixedSchedule);
			ParaTask.setSchedulingType(ScheduleType.WorkSharing);
			//ParaTask.init();
			TaskInfoTwoArgs<Void, Integer, Integer> taskInfo_1 = (TaskInfoTwoArgs<Void, Integer, Integer>) ParaTask.asTask((Integer x, Integer y)->
																					{	try {Thread.sleep(200);} 
																							catch (Exception e) {
																							e.printStackTrace();
																						}
																						System.out.println("The sum is: " + (x+y));});
			TaskInfoTwoArgs<Void, Integer, Integer> taskInfo_2 = (TaskInfoTwoArgs<Void, Integer, Integer>) ParaTask.asTask((Integer x, Integer y)->
																					{
																						try{Thread.sleep(400);;
																						}catch(Exception e){
																							e.printStackTrace();																					
																						}
																						System.out.println("The sub is: " + (x-y));});
			TaskInfoTwoArgs<Void, Integer, Integer> taskInfo_3 = 
					(TaskInfoTwoArgs<Void, Integer, Integer>)ParaTask.asTask((Integer x, Integer y)->{try {
																							Thread.sleep(200);
																						} catch (Exception e) {
																							// TODO Auto-generated catch block
																							e.printStackTrace();
																						}
																						 System.out.println("This functor is pretending to calculate " + x + "+" + y);});
			taskInfo_2.start(7, 5);
			TaskID<Void> id = taskInfo_3.start(10, 11);		
			taskInfo_1.dependsOn(id);
			taskInfo_1.start(1, 3);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
