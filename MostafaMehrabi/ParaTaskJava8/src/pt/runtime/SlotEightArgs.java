package pt.runtime;

import pt.functionalInterfaces.FunctorEightArgsNoReturn;
import pt.functionalInterfaces.FunctorEightArgsWithReturn;

class SlotEightArgs<R, T1, T2, T3, T4, T5, T6, T7, T8> extends Slot<R>{
	
	private FunctorEightArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8> functorNoReturn = null;
	private FunctorEightArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8> functorWithReturn = null;
	
	SlotEightArgs(FunctorEightArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotEightArgs(FunctorEightArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8){
		if(this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return null;
	}
}
