package pt.processors;

import pt.annotations.Future;

public class SimpleAnnotation {
	public int foo(int a, int b){
		return a + b;
	}
	
	public int foo1(int a){
		return a * 5;
	}
	
	public boolean foo2(boolean bool){
		return !bool;
	}
	
	public void foo4(boolean bool){
		boolean temp = !bool;
	}
	
	public void testAnno(){
		@Future
		int Var = foo(5, 8);
		
		@Future
		int VarX = foo(Var, 6);
		
		@Future
		boolean Var1 = foo2(true);
		
		@Future
		int Var2 = foo1(VarX) + foo(foo1(2), Var);
		
		System.out.println("The result of Var2 is: " + (Var2*Var));
		
		@Future
		boolean Var3 = foo2(!Var1);
		
		//Var2 = Var2 && false;
	
		foo4(Var1 && Var3);
		
		boolean Var4 = (Var1 || Var3);
		
		
	}
}
