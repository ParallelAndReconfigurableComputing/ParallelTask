package test;
import apt.annotations.AsyncCatch;
import apt.annotations.Future;
import apt.annotations.InitParaTask;
import apt.annotations.TaskInfoType;

class SimpleAnnotation{
	public void foo1(int x){
		System.out.println((x * 5));
	}
}

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
	
	@InitParaTask
	public static void main(String[] args) {
		@Future(taskType=TaskInfoType.MULTI)
		@AsyncCatch(throwables={InterruptedException.class, IllegalArgumentException.class}, handlers={"foo(1)", "foo1(1, 2)"})
		int Var = foo3(10);		
		try{
			SimpleAnnotation simp = new SimpleAnnotation();
			simp.foo1(5);
			
			int[] array = new int[5];
			for (int i = 0; i < 5; i++){
				@Future
				int Var1 = foo2(i);
				array[i] = Var1;
			}
			
			int VarX = foo1(8, 5);

			
			@Future(depends="Var1", notifies="simp.foo1(7)")
			int Var2 = foo1(VarX, Var);

			@Future(depends="Var1, Var")
			int Var3 = foo2(Var2);
		
			System.out.println("The result of Var2 + Var3 is: " + (Var2 + Var3));
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

}
