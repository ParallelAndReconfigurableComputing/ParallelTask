package dependingNestedTryCatch;

import java.io.IOException;

import apt.annotations.Future;
import apt.annotations.InitParaTask;

public class Main {

	@InitParaTask	
	public static void main(String[] args){
		ExceptionThrowers et = new ExceptionThrowers();
		ExceptionHandlers eh = new ExceptionHandlers();

		try {
			@Future
			Void a = et.interruptedExceptionThrower();
		
			Void barrier = a;
			try{
				@Future
				Void c = et.runtimeExceptionThrower();
					
				@Future
				Void d = et.nullExceptionThrower();
			}catch(NullPointerException e){
				eh.handleNullException(e);
			}
			
			@Future
			Void b = et.ioExceptionThrower();
		} catch (InterruptedException e) {
			eh.handleInterruptedException();
		} catch (IOException e) {
			eh.handleIOException(e);
		} catch (RuntimeException e){
			eh.handleRuntimeException(e);
		}
		
	}
}

 