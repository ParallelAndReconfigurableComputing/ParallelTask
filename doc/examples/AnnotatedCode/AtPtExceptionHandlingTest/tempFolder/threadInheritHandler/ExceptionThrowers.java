package threadInheritHandler;

public class ExceptionThrowers {
    public Void parentMethod() {
        System.out.println("thread " + Thread.currentThread().getId()
        	+ " calling the parent method.");
        Thread t = new Thread(()->runtimeExceptionThrower());
        t.start();
        try {
           Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Void runtimeExceptionThrower() throws RuntimeException {
        System.out.println("thread " + Thread.currentThread().getId()
        		+ " throwing runtime exception.");
        throw new RuntimeException();
    }
}


