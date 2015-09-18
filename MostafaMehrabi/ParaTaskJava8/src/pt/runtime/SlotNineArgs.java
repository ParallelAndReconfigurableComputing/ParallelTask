package pt.runtime;

import pt.functionalInterfaces.FunctorNineArgsNoReturn;
import pt.functionalInterfaces.FunctorNineArgsWithReturn;

class SlotNineArgs<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Slot<R>{

	private FunctorNineArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9> functorNoReturn = null;
	private FunctorNineArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> functorWithReturn = null;
	
	SlotNineArgs(FunctorNineArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
		
	SlotNineArgs(FunctorNineArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return null;
	}
}
