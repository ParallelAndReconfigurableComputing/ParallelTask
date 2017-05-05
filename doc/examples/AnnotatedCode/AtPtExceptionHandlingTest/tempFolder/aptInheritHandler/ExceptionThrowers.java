package aptInheritHandler;

import apt.annotations.Future;

public class ExceptionThrowers {
	public Void parentMethod(){
		System.out.println("thread " + Thread.currentThread().getId()
				+ " calling the parent method.");
		@Future
		Void a = runtimeExceptionThrower();
		
		try{
			Thread.sleep(5000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Void runtimeExceptionThrower() throws RuntimeException{
		System.out.println("thread " + Thread.currentThread().getId()
				+ " throwing runtime exception.");
		throw new RuntimeException();
	}	
}

