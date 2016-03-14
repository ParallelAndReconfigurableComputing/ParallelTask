import pt.annotations.Future;

public class OldAnnotation {
	
	
	public static Void foo(int x) throws InterruptedException{
		Thread.sleep(5000);
		System.out.println("Argument value: " + x);
		return null;
	}
	
	public static int foo1(int x, int y) throws InterruptedException{
		Thread.sleep(2000);
		return x + y;
	}
	
	public static int foo2(int x) throws InterruptedException{
		Thread.sleep(1000);
		return x * 10;
	}
	
	public static int foo3(int x){
		return x * 100;
	}
	
	public static void main(String[] args) {
		@Future
		int Var = foo3(10);		
		try{
			@Future
			Void Var1 = foo(5);
			
			int VarX = foo1(8, 5);
		
			@Future(depends="Var1")
			int Var2 = foo1(VarX, Var);
		
			@Future(depends="Var1, Var")
			int Var3 = foo2(Var2);
		
			System.out.println("The result of Var2 + Var3 is: " + (Var2 + Var3));
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

}
