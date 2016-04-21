package pt.annotations;


public class FutureGroupTest {
    private void myMethod() {
        FutureGroupTest.TestFuture tf = new FutureGroupTest.TestFuture();
        tf.myMethod();
    }
    
    public static void main(String[] args) {
        FutureGroupTest fgt = new FutureGroupTest();
        fgt.myMethod();
    }
    
}

