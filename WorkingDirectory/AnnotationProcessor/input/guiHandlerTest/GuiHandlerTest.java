package guiHandlerTest;

import java.util.Random;

import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.InitParaTask;
import apt.annotations.TaskInfoType;

public class GuiHandlerTest {

	private int foo(int x){
		Random rand = new Random();
		int randNo = rand.nextInt(x);
		try{
			Thread.sleep(randNo*1000);
		}catch(Exception e){}
		return randNo;
	}
	
	private Void updateGui(int x, int y) throws Exception{
		return null;
	}
	
	private Void updateDB(int x, int y){
		return null;
	}
	
	@InitParaTask()
	public void taskRun(){
		try{
			int x = 25;
			int y = 13;
	
			@Future()
			int a = foo(x);
			@Gui(notifiedBy={"a"})
			Void handler1 = updateGui(a, y);
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
			@Gui(notifiedBy={"c"})
			Void handler4 = updateDB(j, k);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
