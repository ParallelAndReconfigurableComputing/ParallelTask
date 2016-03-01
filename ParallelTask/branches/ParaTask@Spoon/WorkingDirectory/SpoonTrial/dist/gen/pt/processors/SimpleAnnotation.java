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
        int Var1 = (foo1(3)) + (foo(foo1(2) ,Var));
        System.out.println(("The result is: " + Var1));
        boolean Var2 = foo2(true);
        boolean Var3 = foo2(!Var2);
        Var2 = Var2 && false;
        boolean Var4 = Var2 || Var3;
        foo4(Var2);
    }
    
}

