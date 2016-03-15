


class SimpleAnnotation {
    public void foo1(int x) {
        System.out.println((x * 5));
    }
    
}

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
        TaskInfoNoArgs<Integer> __VarTask__ = ((TaskInfoNoArgs<Integer>)(ParaTask.asTask(
			(FunctorNoArgsWithReturn<Integer>)() -> OldAnnotation.foo3(10))));
        TaskID<Integer> __VarTaskID__ = __VarTask__.start();
        try {
            SimpleAnnotation simp = new SimpleAnnotation();
            simp.foo1(5);
            TaskInfoNoArgs<Void> __Var1Task__ = ((TaskInfoNoArgs<Void>)(ParaTask.asTask(
			() -> { try
 				 {  
						OldAnnotation.foo(5);
 				 }catch(Exception e){
						e.printStackTrace();
  				 }
			})));
            TaskID<Void> __Var1TaskID__ = __Var1Task__.start();
            int VarX = OldAnnotation.foo1(8 ,5);
            TaskInfoTwoArgs<Integer, Integer, TaskID<Integer>> __Var2Task__ = ((TaskInfoTwoArgs<Integer, Integer, TaskID<Integer>>)(ParaTask.asTask(
			(FunctorTwoArgsWithReturn<Integer, Integer, TaskID<Integer>>)(__VarXNonLambdaArg__, __VarLambdaArg__) -> { try
 				 {  
						return OldAnnotation.foo1(__VarXNonLambdaArg__ ,__VarLambdaArg__.getReturnResult());
 				 }catch(Exception e){
						e.printStackTrace();
						return null; 
  				 }
			})));
            __Var2Task__.dependsOn(__VarTaskID__, __Var1TaskID__);
            ParaTask.registerSlotToNotify(__Var2Task__, ()->simp.foo1(7));
            TaskID<Integer> __Var2TaskID__ = __Var2Task__.start(VarX, __VarTaskID__);
            TaskInfoOneArg<Integer, TaskID<Integer>> __Var3Task__ = ((TaskInfoOneArg<Integer, TaskID<Integer>>)(ParaTask.asTask(
			(FunctorOneArgWithReturn<Integer, TaskID<Integer>>)(__Var2LambdaArg__) -> { try
 				 {  
						return OldAnnotation.foo2(__Var2LambdaArg__.getReturnResult());
 				 }catch(Exception e){
						e.printStackTrace();
						return null; 
  				 }
			})));
            __Var3Task__.dependsOn(__Var1TaskID__, __VarTaskID__, __Var2TaskID__);
            TaskID<Integer> __Var3TaskID__ = __Var3Task__.start(__Var2TaskID__);
            System.out.println(("The result of Var2 + Var3 is: " + (__Var2TaskID__.getReturnResult() + __Var3TaskID__.getReturnResult())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}

