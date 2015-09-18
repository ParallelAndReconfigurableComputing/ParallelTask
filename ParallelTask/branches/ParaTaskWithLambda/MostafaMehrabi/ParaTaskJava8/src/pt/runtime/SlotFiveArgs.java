package pt.runtime;

import pt.functionalInterfaces.FunctorFiveArgsNoReturn;
import pt.functionalInterfaces.FunctorFiveArgsWithReturn;

class SlotFiveArgs<R, T1, T2, T3, T4, T5> extends Slot<R> {

	private FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functorNoReturn = null;
	private FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functorWithReturn = null;
	
	SlotFiveArgs(FunctorFiveArgsNoReturn<T1, T2, T3, T4, T5> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotFiveArgs(FunctorFiveArgsWithReturn<R, T1, T2, T3, T4, T5> functorWithReturn) {
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5){
		if(this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5);
		return null;
	}
}
