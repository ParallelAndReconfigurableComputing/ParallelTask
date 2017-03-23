package guiHandlerTest;

import java.util.Random;
import java.io.IOException;
import apt.annotations.AsyncCatch;
import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.InitParaTask;
import apt.annotations.TaskInfoType;

public class GuiHandlerTest {

	@Future
	int[] array1 = new int[5];
	
	private int foo(int x) throws InterruptedException{
		Random rand = new Random();
		int randNo = rand.nextInt(x);
		Thread.sleep(randNo*1000);
		return randNo;
	}
	
	private Void updateGui(int x, int y) throws Exception{
		return null;
	}
	
	private Void updateDB(int x, int y){
		return null;
	}
	
	public void handleException(){
		System.out.println("Exception occurred");
	}
	
	@InitParaTask()
	public void taskRun(){
		@Future
		int[] array2 = new int[5];
		
		try{
			int x = 25;
			int y = 13;
	
			@Future()
			@AsyncCatch(throwables={IOException.class}, handlers={"handleException()"})
			int a = foo(x);
//			@Gui(notifiedBy={"a"})
//			Void handler1 = updateGui(a, y);
			@Future()
			int b = foo(y);
			@Gui(notifiedBy={"b"})
			Void handler2 = updateGui(b, x);		
			@Gui(notifiedBy={"a", "b"})
			Void handler3 = updateDB(x, y);
			int j = a;
			int k = b;
			@Future(taskType=TaskInfoType.MULTI)
			int c = foo(24);
			@Gui(notifiedBy={"c", "array1", "array2"})
			Void handler4 = updateDB(j, k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
