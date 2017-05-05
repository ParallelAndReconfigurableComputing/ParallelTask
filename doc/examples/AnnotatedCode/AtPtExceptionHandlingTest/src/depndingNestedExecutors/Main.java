package depndingNestedExecutors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args){
		ExceptionThrowers et = new ExceptionThrowers();
		ExceptionHandlers eh = new ExceptionHandlers();
		ExecutorService ex = Executors.newFixedThreadPool
				(Runtime.getRuntime().availableProcessors());
		
		try {
			Future<Void> f1 = ex.submit(() -> et.interruptedExceptionThrower());
			Void barrier = f1.get();
			try{
				Future<Void> f2 = ex.submit(() -> et.runtimeExceptionThrower());
				
				Future<Void> f3 = ex.submit(() -> et.nullExceptionThrower());
			}catch(NullPointerException e){
				eh.handleNullException(e);
			}
			
			Future<Void> f4 = ex.submit(() -> et.ioExceptionThrower());
		}catch (RuntimeException e){
			eh.handleRuntimeException(e);
		}catch (InterruptedException e) {
			eh.handleInterruptedException();
		} catch (ExecutionException e1) {
			System.out.println("ExecutionException occurred");
		}finally{
			ex.shutdown();
		}		
	}
}

 

 