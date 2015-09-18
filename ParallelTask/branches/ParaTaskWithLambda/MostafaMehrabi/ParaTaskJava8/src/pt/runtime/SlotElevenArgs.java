package pt.runtime;

import pt.functionalInterfaces.FunctorElevenArgsNoReturn;
import pt.functionalInterfaces.FunctorElevenArgsWithReturn;

class SlotElevenArgs<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> extends Slot<R>{

	private FunctorElevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functorNoReturn = null;
	private FunctorElevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functorWithReturn = null;
	
	SlotElevenArgs(FunctorElevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotElevenArgs(FunctorElevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11){
		if(this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		return null;
	}
}
