package exceptionTest;

import java.io.IOException;
import java.util.Random;

import apt.annotations.AsyncCatch;
import apt.annotations.Future;
import apt.annotations.Gui;
import apt.annotations.InitParaTask;

public class TesterClass {
	public int foo(int x){
		Random rand = new Random();
		int randomNo = rand.nextInt(x);
		try {
			Thread.sleep(randomNo*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return randomNo;
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
			}
			@Future
			int b = foo(10);
			@Gui(notifiedBy={"b"})
			Void handler = updateGui();			
		}catch(IllegalArgumentException e){
			try {
				handleExcep(e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}catch(RuntimeException e2){
			e2.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
