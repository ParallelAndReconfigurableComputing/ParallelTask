package depndingNestedExecutors;

import java.io.IOException;

public class ExceptionThrowers {
	public Void ioExceptionThrower() throws IOException{
		System.out.println("thread " + Thread.currentThread().getId()
				+ " throwing IO exception.");
		throw new IOException();
	}
	
	public Void interruptedExceptionThrower() throws InterruptedException{
		System.out.println("thread " + Thread.currentThread().getId()
				+ " throwing interrupted exception.");
		throw new InterruptedException();
	}
	
	public Void runtimeExceptionThrower() throws RuntimeException{
		System.out.println("thread " + Thread.currentThread().getId()
				+ " throwing runtime exception.");
		throw new RuntimeException();
	}
	
	public Void nullExceptionThrower() throws NullPointerException{
		System.out.println("thread " + Thread.currentThread().getId()
				+ " throwing null pointer exception.");
		throw new NullPointerException();
	}	
}


