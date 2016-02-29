package pt.processors;

import pt.annotations.Future;

public class SimpleAnnotation {
	public int foo(int a, int b){
		return a + b;
	}
	
	public int foo1(int a){
		return a * 5;
	}
	
	public void testAnno(){
		@Future
		int Var = foo(5, 8);
		
		@Future
		int var1 = foo1(3) + foo(foo1(2), Var);
		System.out.println("The result is: " + var1);
	}
}
