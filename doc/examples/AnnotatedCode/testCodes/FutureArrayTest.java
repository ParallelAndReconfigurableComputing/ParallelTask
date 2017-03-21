package code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.runtime.ParaTask;
import sp.annotations.AsyncCatch;
import sp.annotations.Future;

public class FutureArrayTest {
	
	@Future(reduction="sum")
	static int[] myArray = new int[10];
	
//	public FutureArrayTest(int i){
//    	myArray[1] = foo(i);
//    }
//	
//	public FutureArrayTest(){
//	   	System.out.println("Hello, default constructor");
//	}
	
	public static int foo(int i){
		return i * 10;
	}
	
	@sp.annotations.InitParaTask()
	public static void main(String[] args) {
		ObjectTest tester = new ObjectTest(3);
		
		@Future(taskCount = 4)
		int bb = foo(34);
		
		int n = 10;
		@Future(elasticGroup=true, reduction="sum")
		int[] array = new int[n];
		for (int i = 0; i < 5; i++){
			@Future(taskCount=2)
			@AsyncCatch(throwables={InterruptedException.class}, handlers={"foo(1)"})
			@AsyncCatch(throwables={IOException.class}, handlers = { "foo(2)" })
			@AsyncCatch(throwables={RuntimeException.class}, handlers={"foo(3)"})
			int a = foo(i); 
			array [i] = a;
		}
		array[5] = tester.get();
		array[6] = bb;
		for(int i = 7; i < 10; i++){
			array[i] = foo(i * 15);
		}
		myArray[0] = foo(3);
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
