package test;

import sp.annotations.AsyncCatch;
import sp.annotations.Future;

public class FutureArrayTest {
	
	public static int foo(int i){
		return i * 10;
	}
	
	public static void main(String[] args) {
		ObjectTest tester = new ObjectTest(3);
		
		@Future(taskCount = 4)
		int bb = foo(34);
		
		int n = 10;
		@Future
		int[] array = new int[n];
		for (int i = 0; i < 5; i++){
			@Future(taskCount=2)
			@AsyncCatch(throwables={InterruptedException.class}, handlers={"foo(1)"})
			int a = foo(i); 
			array [i] = a;
		}
		array[5] = tester.get();
		array[6] = bb;
		for(int i = 7; i < 10; i++){
			array[i] = foo(i * 15);
		}
		
		int[] myarray = new int[2];
		myarray[0] = tester.get();
		
		System.out.println(array.length);
		
		for (int i = 0; i < array.length; i++){
			System.out.print(array[i]);
			if(i != array.length-1)
				System.out.print(", ");
		}	
		
		for (int i = 0; i < array.length; i++){
			foo(array[i]);
		}
	}
}
