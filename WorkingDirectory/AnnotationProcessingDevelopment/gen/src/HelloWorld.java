package src;


public class HelloWorld {
    @annotations.Task
    public static java.lang.Void HelloOne() {
        java.lang.System.out.println(("Hello world from hello one! Thread No. " + (java.lang.Thread.currentThread())));
        return null;
    }

    @annotations.Task(isMultiple = true, numOfMultiTask = 3)
    public static int HelloTwo(java.util.concurrent.ConcurrentLinkedQueue<java.lang.String> names) {
        for (java.lang.String name : names) {
            java.lang.System.out.println(((("Hello " + name) + " from thread ") + (java.lang.Thread.currentThread())));
        }
        return names.size();
    }

    @annotations.Task
    public static int Addition(int a, int b) {
        java.lang.System.out.println((("Thread " + (java.lang.Thread.currentThread())) + " is in addition."));
        return a + b;
    }

    public static void main(java.lang.String[] args) {
        java.lang.Void hello1 = src.HelloWorld.HelloOne();
        java.util.concurrent.ConcurrentLinkedQueue<java.lang.String> names = new java.util.concurrent.ConcurrentLinkedQueue<>();
        names.add("Ellie");
        names.add("Bob");
        names.add("Samuel");
        names.add("Chris");
        names.add("George");
        names.add("Sally");
        names.add("Sophia");
        names.add("Sina");
        int var = src.HelloWorld.HelloTwo(names);
        int addition = src.HelloWorld.Addition(1, var);
        java.lang.System.out.println("Processing finished with the following values:");
        java.lang.System.out.println(("var: " + var));
        java.lang.System.out.println(("addition: " + addition));
    }
}

