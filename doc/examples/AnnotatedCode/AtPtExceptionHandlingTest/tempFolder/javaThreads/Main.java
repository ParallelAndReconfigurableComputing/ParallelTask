package javaThreads;

import java.io.IOException;

import apt.annotations.Future;
import apt.annotations.InitParaTask;

public class Main {

	@InitParaTask	
	public static void main(String[] args){
		ExceptionThrowers et = new ExceptionThrowers();
		ExceptionHandlers eh = new ExceptionHandlers();
		
		try {
			Thread t1 = new Thread(() -> 
				et.interruptedExceptionThrower());
			t1.start();
			
			try{
				Thread t2 = new Thread(() ->
					et.runtimeExceptionThrower());
				t2.start();
				
				Thread t3 = new Thread(() -> 
				et.nullExceptionThrower());
				t3.start();
			}catch(NullPointerException e){
				eh.handleNullException(e);
			}
			
			Thread t4 = new Thread(() -> et.ioExceptionThrower());
			t4.start();
		} catch (InterruptedException e) {
			eh.handleInterruptedException();
		} catch (IOException e) {
			eh.handleIOException(e);
		} catch (RuntimeException e){
			eh.handleRuntimeException(e);
		}
		
	}
}

 
