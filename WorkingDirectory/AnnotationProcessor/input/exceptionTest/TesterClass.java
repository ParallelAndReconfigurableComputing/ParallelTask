package exceptionTest;

import java.io.IOException;
import java.util.Random;

import apt.annotations.AsyncCatch;
import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.InitParaTask;

public class TesterClass {
	public int foo(int x) throws InterruptedException{
		Random rand = new Random();
		int randomNo = rand.nextInt(x);
		Thread.sleep(randomNo*1000);
		return randomNo;
	}
	
	public int foox(int x) throws IOException{
		return x * 10;
	}
	
	public void handleExcep(Exception e) throws Exception{
		e.printStackTrace();
	}
	
	public void handleThrowable(Exception e){
		e.printStackTrace();
	}
	
	public void handleException(){
		System.out.println("Exception occurred");
	}
	
	public Void updateGui(){
		System.out.println("GUI updated");
		return null;
	}
	
	@InitParaTask
	public void taskRun(){
		try{
			for(int i = 0; i < 10; i++){
				@Future
				@AsyncCatch(throwables={Exception.class}, handlers={"handleException()"})
				int a = foo(i);
				@Gui(notifiedBy={"a"})
				Void handler = updateGui();	
			}
			@Future
			int b = foox(10);
			@Gui(notifiedBy={"b"})
			Void handler = updateGui();	
//			int num = foo(10) + foox(10);
			System.out.println("result: " + foo(10));
		}catch(IllegalArgumentException|IOException e){
			try {
				handleExcep(e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}catch(RuntimeException e2){
			e2.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
