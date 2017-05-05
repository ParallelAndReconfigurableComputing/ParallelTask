package javaThreads;

import java.io.IOException;

public class ExceptionHandlers {
	public void handleRuntimeException(RuntimeException e){
		System.out.println("handling runtime exception by "
				+ Thread.currentThread().getId());
	}
	
	public void handleInterruptedException(){
		System.out.println("handling interrupted exception by "
				+ Thread.currentThread().getId());
	}
	
	public void handleIOException(IOException e){
		System.out.println("handling IO Exception by "
				+ Thread.currentThread().getId());
	}
	
	public void handleNullException(NullPointerException e){
		System.out.println("handling null pointer exception by "
				+ Thread.currentThread().getId());
	}
}

