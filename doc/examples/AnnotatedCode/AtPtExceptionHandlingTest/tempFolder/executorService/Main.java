package executorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static void main(String[] args){
		ExceptionThrowers et = new ExceptionThrowers();
		ExceptionHandlers eh = new ExceptionHandlers();
		ExecutorService ex = Executors.newFixedThreadPool
				(Runtime.getRuntime().availableProcessors());
		
		try {
			ex.submit(() -> et.interruptedExceptionThrower());
			
			try{
				ex.submit(() -> et.runtimeExceptionThrower());
				
				ex.submit(() -> et.nullExceptionThrower());
			}catch(NullPointerException e){
				eh.handleNullException(e);
			}
			
			ex.submit(() -> et.ioExceptionThrower());
		}catch (RuntimeException e){
			eh.handleRuntimeException(e);
		}catch (Exception e) {
			eh.handleInterruptedException();
		}finally{
			ex.shutdown();
		}
		
	}
}

 

 