package smallProject;

import java.util.Random;

import sp.annotations.Future;

public class FutureArray {
	@Future(reduction="sum")
	int[] myArray = new int[10];
	int range = 0;
	public FutureArray(int num){
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
			myArray[i] = task(i+3);
		}
		finishTask();
	}
	
	private void finishTask(){
		for(int i = 0; i < range; i++){
			System.out.println("The result of " + i + "th task: " + myArray[i]);
		}
	}
}
