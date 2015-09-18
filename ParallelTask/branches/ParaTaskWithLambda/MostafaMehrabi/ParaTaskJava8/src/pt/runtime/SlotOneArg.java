package pt.runtime;


import pt.functionalInterfaces.FunctorOneArgNoReturn;
import pt.functionalInterfaces.FunctorOneArgWithReturn;

class SlotOneArg<R, T1> extends Slot<R>{
	
	private FunctorOneArgNoReturn<T1> functorNoReturn = null;
	private FunctorOneArgWithReturn<R, T1> functorWithReturn = null;
	
	SlotOneArg(FunctorOneArgNoReturn<T1> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotOneArg(FunctorOneArgWithReturn<R, T1> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1){
		if (functorWithReturn!=null)
			return functorWithReturn.exec(arg1);
		functorNoReturn.exec(arg1);
		return null;
	}
}
