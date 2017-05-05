package aptInheritHandler;

import apt.annotations.Future;
import apt.annotations.InitParaTask;

public class Main {

	@InitParaTask	
	public static void main(String[] args){
		ExceptionThrowers et = new ExceptionThrowers();
		ExceptionHandlers eh = new ExceptionHandlers();
		
		try {
			try{
				@Future
				Void b = et.parentMethod();
			}catch(NullPointerException e){
				e.printStackTrace();
			}			
			@Future
			Void c = et.runtimeExceptionThrower();
		}catch (RuntimeException e){
			eh.handleRuntimeException(e);
		}		
	}
}

 