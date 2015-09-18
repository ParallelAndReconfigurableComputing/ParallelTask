package pt.runtime;

import pt.functionalInterfaces.FunctorNoArgsNoReturn;
import pt.functionalInterfaces.FunctorNoArgsWithReturn;

class SlotNoArgs<R> extends Slot<R>{
	
	private FunctorNoArgsNoReturn functorNoReturn = null;
	private FunctorNoArgsWithReturn<R> functorWithReturn = null;
	
	SlotNoArgs(FunctorNoArgsNoReturn functorNoReturn){
		this.functorNoReturn = functorNoReturn;
	}
	
	SlotNoArgs(FunctorNoArgsWithReturn<R> functorWithReturn){
		this.functorWithReturn = functorWithReturn;
	}
		
	R execute(){
		if (this.functorWithReturn != null)
			return this.functorWithReturn.exec();
		this.functorNoReturn.exec();
		return null;
	}
}
