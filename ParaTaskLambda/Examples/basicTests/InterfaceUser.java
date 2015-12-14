package benchmarks.basicTests;

import pt.functionalInterfaces.FunctorNoArgsNoReturn;
import pt.functionalInterfaces.FunctorOneArgWithReturn;

public class InterfaceUser<R, T1> {
	
	FunctorOneArgWithReturn<R, T1> functor;
	FunctorNoArgsNoReturn functor1;
	public InterfaceUser(FunctorOneArgWithReturn<R, T1> functor){
		this.functor = functor;
	}
	
	public InterfaceUser(FunctorNoArgsNoReturn functor){
		functor1 = functor;
	}
	
	public R execute (T1 arg){
		return this.functor.exec(arg);
	}
	
	public void execute(){
		this.functor1.exec();
	}
}
