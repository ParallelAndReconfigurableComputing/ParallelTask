package smallProject;
import java.util.Random;

import pt.runtime.TaskIDGroup;
import pt.runtime.ParaTask;
import pu.RedLib.IntegerSum;
import apt.annotations.Future;

public class FutureArray 
{
	
	int range = 5;
	
	@Future(reduction="newSum")
	private int[] myArray;
	IntegerSum newSum = new IntegerSum();
	Integer newInt = new Integer(0);
	

	
	public FutureArray(int num){
		myArray = new int[num];
		range = num;		
	}
	
	private int task(int i){
		try{
			Random rand = new Random();
			int sleepTime = rand.nextInt(10);
			int multiplier = rand.nextInt(i);
			Thread.sleep(sleepTime*1000);
			System.out.println("Thread " + Thread.currentThread().getId() + " slept for " 
																+ sleepTime + " seconds");
			return (i*multiplier);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public void processTasks(){
		for(int i = 0; i < range; i++){
			myArray   [ i ] = task(i+3);
		}
		
		int[] bigArray = new int[2];
		for(int i = 0; i < 2; i++){
			bigArray [myArray [i ] ] = task(i+1);
		}		
		Void dd = finishTask(myArray, 7);
	}
	
	public void processInnerTasks(){
		boolean setArray = true;
		@Future()
		int[] newArray; 
		if(setArray){
			newArray = new int[7];
		}
		else
			newArray = new int[8];
		for (int i = 0; i < newArray.length; i++){
			newArray[i] = task(i);
		}
		
		System.out.println(newArray   [ 0 ] + ", " + newArray  [2]);
		
		newArray[4] = task(100);
		newArray[6] = task(200);
	}
	
	private Void finishTask(int array[], int index){
		for(int i = 0; i < range; i++){
			System.out.println("The result of " + i + "th task: " + array[i]);
		}
		return null;
	}	
}
