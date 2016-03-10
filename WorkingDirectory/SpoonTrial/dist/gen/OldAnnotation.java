


public class OldAnnotation {
    public static Void foo(int x) throws InterruptedException {
        java.lang.Thread.sleep(5000);
        System.out.println(("Argument value: " + x));
        return null;
    }
    
    public static int foo1(int x, int y) throws InterruptedException {
        java.lang.Thread.sleep(2000);
        return x + y;
    }
    
    public static int foo2(int x) throws InterruptedException {
        java.lang.Thread.sleep(1000);
        return x * 10;
    }
    
    public static int foo3(int x) {
        return x * 100;
    }
    
    public static void main(String[] args) {
        TaskInfoNoArgs<Integer> __VarTask__ = ((TaskInfoNoArgs<Integer>)(OldAnnotation.foo3(10)));
        TaskID<<Integer> __VarTaskID__ = __VarTask__.start();
        try {
            TaskInfoNoArgs<java.lang.Void> __Var1Task__ = ((TaskInfoNoArgs<java.lang.Void>)(OldAnnotation.foo(5)));
            TaskID<<java.lang.Void> __Var1TaskID__ = __Var1Task__.start();
            TaskInfoOneArg<Integer> __Var2Task__ = ((TaskInfoOneArg<Integer>)(OldAnnotation.foo1(6 ,__VarTaskID__.getResult())));
            TaskID<<Integer> __Var2TaskID__ = __Var2Task__.start(__VarTaskID__);
            TaskInfoOneArg<Integer> __Var3Task__ = ((TaskInfoOneArg<Integer>)(OldAnnotation.foo2(__Var2TaskID__.getResult())));
            TaskID<<Integer> __Var3TaskID__ = __Var3Task__.start(__Var2TaskID__);
            System.out.println(("The result of Var2 + Var3 is: " + (__Var2TaskID__.getResult() + __Var3TaskID__.getResult())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}

