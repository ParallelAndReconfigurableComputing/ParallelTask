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
        @pt.annotations.Future
int Var1 = (foo1(3)) + (foo(foo1(2) ,__VarTaskID__.getResult()));
        System.out.println(("The result is: " + Var1));
        boolean Var2 = foo2(true);
        @pt.annotations.Future
boolean Var3 = foo2(!__Var2TaskID__.getResult());
        foo4(__Var2TaskID__.getResult());
        boolean Var4 = __Var2TaskID__.getResult() || Var3;
    }
    
}

