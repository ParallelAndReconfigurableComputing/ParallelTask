

package exceptionHandling;


public class ExceptionThrowers {
    public java.lang.Void ioExceptionThrower() throws java.io.IOException {
        java.lang.System.out.println((("thread " + (java.lang.Thread.currentThread().getId())) + " throwing IO exception."));
        throw new java.io.IOException();
    }

    public java.lang.Void interruptedExceptionThrower() throws java.lang.InterruptedException {
        java.lang.System.out.println((("thread " + (java.lang.Thread.currentThread().getId())) + " throwing interrupted exception."));
        throw new java.lang.InterruptedException();
    }

    public java.lang.Void runtimeExceptionThrower() throws java.lang.RuntimeException {
        java.lang.System.out.println((("thread " + (java.lang.Thread.currentThread().getId())) + " throwing runtime exception."));
        throw new java.lang.RuntimeException();
    }

    public java.lang.Void nullExceptionThrower() throws java.lang.NullPointerException {
        java.lang.System.out.println((("thread " + (java.lang.Thread.currentThread().getId())) + " throwing null pointer exception."));
        throw new java.lang.NullPointerException();
    }
}

