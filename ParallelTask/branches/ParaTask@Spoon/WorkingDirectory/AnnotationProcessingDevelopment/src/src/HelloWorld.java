package src;

import java.util.concurrent.ConcurrentLinkedQueue;

import annotations.Future;
import annotations.Task;


public class HelloWorld {

	@Task
	public static Void HelloOne(){
		System.out.println("Hello world from hello one! Thread No. " + Thread.currentThread());
		return null;
	}
	
	@Task(isMultiple=true, numOfMultiTask=3)
	public static int HelloTwo(ConcurrentLinkedQueue<String> names){
		for (String name : names){
			System.out.println("Hello " + name + " from thread " + Thread.currentThread());
		}
		return names.size();
	}
	
	@Task
	public static int Addition(int a, int b){
		System.out.println("Thread " + Thread.currentThread() + " is in addition.");
		return a + b;
	}
	
	public static void main(String[] args){
		@Future
		Void hello1 = HelloOne();
		
		ConcurrentLinkedQueue<String> names = new ConcurrentLinkedQueue<>();
		names.add("Ellie"); names.add("Bob"); names.add("Samuel"); names.add("Chris");
		names.add("George"); names.add("Sally"); names.add("Sophia"); names.add("Sina");
		
		@Future
		int var = HelloTwo(names); 
		
		@Future
		int addition = Addition(1, var);
		
		System.out.println("Processing finished with the following values:");
		System.out.println("var: " + var);
		System.out.println("addition: " + addition);
	}
}
