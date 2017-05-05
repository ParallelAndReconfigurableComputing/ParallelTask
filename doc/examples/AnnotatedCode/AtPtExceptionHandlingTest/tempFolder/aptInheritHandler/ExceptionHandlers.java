package aptInheritHandler;

public class ExceptionHandlers {
	public void handleRuntimeException(RuntimeException e){
		System.out.println("handling runtime exception by "
				+ Thread.currentThread().getId());
	}	
}

