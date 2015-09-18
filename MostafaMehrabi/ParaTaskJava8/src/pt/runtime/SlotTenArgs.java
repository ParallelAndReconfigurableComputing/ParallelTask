package pt.runtime;

import pt.functionalInterfaces.FunctorTenArgsNoReturn;
import pt.functionalInterfaces.FunctorTenArgsWithReturn;

class SlotTenArgs<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends Slot<R> {

	private FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorNoReturn = null;
	private FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorWithReturn = null;
	
	SlotTenArgs(FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotTenArgs(FunctorTenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
		
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return null;
	}
}
