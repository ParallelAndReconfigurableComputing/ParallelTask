package pt.processors;


public class SimpleAnnotation {
    public int foo(int a, int b) {
        return a + b;
    }
    
    public int foo1(int a) {
        return a * 5;
    }
    
    public boolean foo2(boolean bool) {
        return !bool;
    }
    
    public void foo4(boolean bool) {
        boolean temp = !bool;
    }
    
    public void testAnno() {
        int Var = foo(5 ,8);
        int VarX = foo(__VarTaskID__.getResult() ,6);
        boolean Var1 = foo2(true);
        int Var2 = (foo1(__VarXTaskID__.getResult())) + (foo(foo1(2) ,__VarTaskID__.getResult()));
        System.out.println(("The result of __Var2TaskID__.getResult() is: " + (__Var2TaskID__.getResult() * __VarTaskID__.getResult())));
        boolean Var3 = foo2(!__Var1TaskID__.getResult());
        foo4((__Var1TaskID__.getResult() && __Var3TaskID__.getResult()));
        boolean Var4 = __Var1TaskID__.getResult() || __Var3TaskID__.getResult();
    }
    
}

