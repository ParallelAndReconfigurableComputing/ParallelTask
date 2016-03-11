package conv;

import java.util.concurrent.ExecutionException;

import pt.functionalInterfaces.FunctorNoArgsNoReturn;
import pt.functionalInterfaces.FunctorNoArgsWithReturn;
import pt.functionalInterfaces.FunctorOneArgWithReturn;
import pt.runtime.ParaTask;
import pt.runtime.ParaTask.ScheduleType;
import pt.runtime.TaskID;
import pt.runtime.TaskInfoNoArgs;
import pt.runtime.TaskInfoOneArg;


//Arguments that are sent to a lambda expression must be used as "x", "y", "z" etc. and then in the
//.start() phrase the actual argument (e.g., TaskID) is sent!

//methods that throw exceptions, their exceptions needs to be handled in the lambda expression

//taskInfo<R, T1, ...> return type needs to be included for taskInfo as well

//FunctorNoArgsNoReturn MUST not have a generic type in front of it!

//Modify ParaTask such that if Scheduling task is not set, it can set it by itself!

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
    
    public static void main(String[] args) throws InterruptedException, IllegalAccessException{
    	ParaTask.setSchedulingType(ScheduleType.MixedSchedule);
        TaskInfoNoArgs<Integer> __VarTask__ = ((TaskInfoNoArgs<Integer>)(ParaTask.asTask((FunctorNoArgsWithReturn<Integer>)() -> OldAnnotation.foo3(10))));
        TaskID<Integer> __VarTaskID__ = __VarTask__.start();
        try {
            TaskInfoNoArgs<java.lang.Void> __Var1Task__ = ((TaskInfoNoArgs<java.lang.Void>)(ParaTask.asTask((FunctorNoArgsNoReturn)() -> {try{OldAnnotation.foo(5);}catch(Exception e){e.printStackTrace();}})));
            TaskID<java.lang.Void> __Var1TaskID__ = __Var1Task__.start();
            TaskInfoOneArg<Integer, TaskID<Integer>> __Var2Task__ = ((TaskInfoOneArg<Integer, TaskID<Integer>>)(ParaTask.asTask((FunctorOneArgWithReturn<Integer, TaskID<Integer>>)
            		(x) -> {try{return OldAnnotation.foo1(6 ,x.getReturnResult());}catch(Exception e){e.printStackTrace();}
					return null;})));
            TaskID<Integer> __Var2TaskID__ = __Var2Task__.start(__VarTaskID__);
            TaskInfoOneArg<Integer, TaskID<Integer>> __Var3Task__ = ((TaskInfoOneArg<Integer, TaskID<Integer>>)(ParaTask.asTask((FunctorOneArgWithReturn<Integer, TaskID<Integer>>)
            		(x) -> {try{ return OldAnnotation.foo2(x.getReturnResult());}catch(Exception e){e.addSuppressed(e);} return null;})));
            TaskID<Integer> __Var3TaskID__ = __Var3Task__.start(__Var2TaskID__);
            System.out.println(("The result of Var2 + Var3 is: " + (__Var2TaskID__.getReturnResult() + __Var3TaskID__.getReturnResult())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}

