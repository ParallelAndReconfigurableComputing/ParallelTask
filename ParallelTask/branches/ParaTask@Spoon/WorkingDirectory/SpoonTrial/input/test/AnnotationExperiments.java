package test;

import pt.annotations.AsyncCatch;
import pt.annotations.Future;
import pt.annotations.Task;

public class AnnotationExperiments {

    // use pt.annotations.Void instead of void
    @Task
    public Void taskReturnsVoid() {
        // do something
        return null;
    }

    @Task
    private int taskReturnsInt() {
        // do something
        return 0;
    }

    @Task
    final public static String taskReturnsStr() {
        return "";
    }

    
    @Task
    String taskReturnsStr(int arg) {
        return String.valueOf(arg);
    }
    
    @Task
    String taskReturnsStr2() {
        return String.valueOf(3);
    }
    
    void notifiesHandler() {

    }
    
    void notifiesHandler2() {

    }

    void handleException(RuntimeException ex) {

    }

    void strHandler(String str) {
    	
    }
    
    int test() {
        AnnotationExperiments exp = new AnnotationExperiments();


        @Future
        final Void retVoid = taskReturnsVoid();

        @Future
        int retInt = exp.taskReturnsInt() + 3 - 1;
        
        //FunctorWithOneArg<String, Integer> fun = (__retInt__arg__) -> exp.taskReturnsStr(__retInt__arg__);
        //pt.runtime.Task.asTask((FunctorWithOneArg<String, Integer>) (__retInt__arg__) -> exp.taskReturnsStr(__retInt__arg__)).start();
                
        @Future
        String bar = exp.taskReturnsStr(retInt);

        int foo = 3 + (retInt);


        AnnotationExperiments anotherObj = new AnnotationExperiments();
        @Future(depends = "retVoid", 
        		notifies = "strHandler(?); notifiesHandler(); notifiesHandler2; anotherObj::notifiesHandler; anotherObj::notifiesHandler2()")
        //@AsyncCatch(throwable = RuntimeException.class, handler = "handleException")
        String retStr = exp.taskReturnsStr2();

        return 1;
    }

    public static void main(String[] args) {
        AnnotationExperiments exp = new AnnotationExperiments();

        System.out.println(exp.taskReturnsInt());
    }
}
