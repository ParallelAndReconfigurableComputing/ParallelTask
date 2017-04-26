

package exceptionHandling;


public class ExceptionHandlers {
    public void handleRuntimeException(java.lang.RuntimeException e) {
        java.lang.System.out.println(("handling runtime exception by " + (java.lang.Thread.currentThread().getId())));
    }

    public void handleInterruptedException() {
        java.lang.System.out.println(("handling interrupted exception by " + (java.lang.Thread.currentThread().getId())));
    }

    public void handleIOException(java.io.IOException e) {
        java.lang.System.out.println(("handling IO Exception by " + (java.lang.Thread.currentThread().getId())));
    }

    public void handleNullException(java.lang.NullPointerException e) {
        java.lang.System.out.println(("handling null pointer exception by " + (java.lang.Thread.currentThread().getId())));
    }
}

