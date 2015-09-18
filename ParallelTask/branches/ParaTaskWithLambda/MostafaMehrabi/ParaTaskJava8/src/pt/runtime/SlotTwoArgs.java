package pt.runtime;

import pt.functionalInterfaces.FunctorTwoArgsNoReturn;
import pt.functionalInterfaces.FunctorTwoArgsWithReturn;

class SlotTwoArgs<R, T1, T2> extends Slot<R> {
	
	private FunctorTwoArgsNoReturn<T1, T2> functorNoReturn = null;
	private FunctorTwoArgsWithReturn<R, T1, T2> functorWithReturn = null;
	
	SlotTwoArgs(FunctorTwoArgsNoReturn<T1, T2> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
		
	SlotTwoArgs(FunctorTwoArgsWithReturn<R, T1, T2> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1, T2 arg2){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2);
		this.functorNoReturn.exec(arg1, arg2);
		return null;
	}
}
