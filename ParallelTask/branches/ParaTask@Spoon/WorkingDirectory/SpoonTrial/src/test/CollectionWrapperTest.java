package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.runtime.ParaTask;
import sp.annotations.Future;

public class CollectionWrapperTest {
	
	public static int simulateWork(int i){
		Random rand = new Random();
		int random = rand.nextInt(10);
		try{
			System.out.println("Thread " + Thread.currentThread().getId() + " is going to sleep for " + random + " seconds.");
			Thread.sleep(random*1000);
		}catch(Exception e){e.printStackTrace();}
		System.out.println("Thread " + Thread.currentThread().getId() + " is returning " + (i * random));
		return (i * random);
	}
	
	public static void main(String[] args){
		
		@Future
		int specialNum = simulateWork(-2);
		
		@Future
		List<Integer> myList = ParaTask.getPtWrapper(new ArrayList<Integer>());
		
		for(int i = 0; i < 20; i++){
			@Future
			int num = simulateWork(i);
			
			if( i%3 == 0 )
				myList.add(i);
			else
				myList.add(num);
		}
		
		myList.add(specialNum);
		
		
		for(int counter = 0; counter < myList.size(); counter++){
			int num = myList.get(counter);
			System.out.println("get(Index): " + myList.get(counter) + ", and num: " + num);
		}
	}
}
