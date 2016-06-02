package sp.processors;

import sp.annotations.Future;
import spoon.reflect.declaration.CtVariable;

public class CollectionWrapperProcessor {
	public CollectionWrapperProcessor(Future future, CtVariable<?> annotatedElement){
		/*
		 *In the collection wrapper, taskID objects can be added to the collection wrapper.
		 *So first, before every annotated collection wrapper invocation call:
		 *     ParaTask.processInParallel(true)
		 *Then check if any of the usages of the collection wrapper object is using a 
		 *taskID object as it argument using the "isTaskIDReplacement()" method. 
		 *In this case, remove the ".getReturnResult()" from the syntax. 
		 */
	}
}
