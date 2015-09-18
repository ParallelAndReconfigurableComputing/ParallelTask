package pt.runtime;

import pt.functionalInterfaces.FunctorSevenArgsNoReturn;
import pt.functionalInterfaces.FunctorSevenArgsWithReturn;

class SlotSevenArgs<R, T1, T2, T3, T4, T5, T6, T7> extends Slot<R>{

	private FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functorNoReturn = null;
	private FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functorWithReturn = null;
	
	SlotSevenArgs(FunctorSevenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7> functorNoReturn){		
		this.functorNoReturn = functorNoReturn;
	}
		
	SlotSevenArgs(FunctorSevenArgsWithReturn<R, T1, T2, T3, T4, T5, T6, T7> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
		
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return null;
	}
	
}
