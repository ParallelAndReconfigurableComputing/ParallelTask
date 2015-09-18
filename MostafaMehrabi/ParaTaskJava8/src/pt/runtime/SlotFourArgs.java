package pt.runtime;

import pt.functionalInterfaces.FunctorFourArgsNoReturn;
import pt.functionalInterfaces.FunctorFourArgsWithReturn;

class SlotFourArgs<R, T1, T2, T3, T4> extends Slot<R>{

	private FunctorFourArgsNoReturn<T1, T2, T3, T4> functorNoReturn = null;
	private FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functorWithReturn = null;
	
	SlotFourArgs(FunctorFourArgsNoReturn<T1, T2, T3, T4> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotFourArgs(FunctorFourArgsWithReturn<R, T1, T2, T3, T4> functorWithReturn) {
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3, T4 arg4){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3, arg4);
		this.functorNoReturn.exec(arg1, arg2, arg3, arg4);
		return null;
	}
}
