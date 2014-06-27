package test;

import java.util.concurrent.atomic.AtomicInteger;

import pt.annotations.Future;
import pt.annotations.Task;


public class HelloWorld {
	private String name;
	
	public HelloWorld(String name) {
		this.name = name;
	}
	
	private AtomicInteger counter = new AtomicInteger(0);

	@Task
	public int hello(String name) {
		int c = counter.incrementAndGet();
		System.out.println(this.name + ": 'Hello " + name + "!', counter: " + c);
		return c;
	}
	
	@Task
	public int plus(int num1, int num2) {
		int ret = num1 + num2;
		System.out.println(num1 + " + " + num2 + " = " + ret);
		return ret;
	}
	
	private void notifyFunc() {
		System.out.println(name + ": 'Bye!'");
	}
	
	public static void main(String[] args) {
		HelloWorld hw = new HelloWorld("Foo");
		
		System.out.println("(1)");
		
		hw.hello("Sequential");
		
		System.out.println("(2)");
		
		@Future
		int hello1 = hw.hello("hello1");
				
		System.out.println("(3)");
		
		HelloWorld hw2 = new HelloWorld("Bar");
		
		@Future(notifies = "hw2::notifyFunc")
		int hello2 = hw2.hello("hello2");

		System.out.println("(4)");
		
		int num1 = hello1 + 50;
		
		@Future
		int result = hw2.plus(num1, hello2);
		
		System.out.println("not finished yet...");
	}

}
