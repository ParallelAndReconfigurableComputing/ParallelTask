package threadInheritHandler;

public class Main {

	public static void main(String[] args){
		ExceptionThrowers et = new ExceptionThrowers();
		ExceptionHandlers eh = new ExceptionHandlers();
		
		try {
			try{
				Thread t1 = new Thread(()->et.parentMethod());
				t1.start();
			}catch(NullPointerException e){
				e.printStackTrace();
			}			
			Thread t2 = new Thread(()->et.runtimeExceptionThrower());
			t2.start();
		}catch (RuntimeException e){
			eh.handleRuntimeException(e);
		}		
	}
}

 