package pt.runtime;

import pt.functionalInterfaces.FunctorThreeArgsNoReturn;
import pt.functionalInterfaces.FunctorThreeArgsWithReturn;

class SlotThreeArgs<R, T1, T2, T3> extends Slot<R>{

	private FunctorThreeArgsNoReturn<T1, T2, T3> functorNoReturn = null;
	private FunctorThreeArgsWithReturn<R, T1, T2, T3> functorWithReturn = null;
	
	SlotThreeArgs(FunctorThreeArgsNoReturn<T1, T2, T3> functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotThreeArgs(FunctorThreeArgsWithReturn<R, T1, T2, T3> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
	
	R execute(T1 arg1, T2 arg2, T3 arg3){
		if (this.functorWithReturn!=null)
			return this.functorWithReturn.exec(arg1, arg2, arg3);
		this.functorNoReturn.exec(arg1, arg2, arg3);
		return null;
	}
}
