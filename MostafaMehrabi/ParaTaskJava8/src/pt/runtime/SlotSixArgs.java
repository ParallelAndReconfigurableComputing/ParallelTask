package pt.runtime;

import pt.functionalInterfaces.FunctorSixArgsNoReturn;
import pt.functionalInterfaces.FunctorSixArgsWithReturn;

class SlotSixArgs<R, T1, T2, T3, T4, T5, T6> extends Slot<R> {

	private FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functorNoReturn = null;
	private FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functorWithReturn = null;
	
	SlotSixArgs(FunctorSixArgsNoReturn<T1, T2, T3, T4, T5, T6> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotSixArgs(FunctorSixArgsWithReturn<R, T1, T2, T3, T4, T5, T6> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6){
		if(this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6);
		return null;  
	}
}
