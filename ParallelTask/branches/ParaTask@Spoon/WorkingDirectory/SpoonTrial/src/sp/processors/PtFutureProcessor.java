package sp.processors;

import sp.annotations.Future;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtVariable;
import spoon.support.reflect.code.CtInvocationImpl;

public class PtFutureProcessor extends AbstractAnnotationProcessor<Future, CtLocalVariable<?>> {

	@Override
	public void process(Future annotation, CtLocalVariable<?> annotatedElement) {
		/*
		 * Also, initialize ParaTask at the beginning of each method. 
		 * A common step for every annotated element with @Future is to look into
		 * its parent loop statement and change the name of the loop variable (the one
		 * inside the loop) to _PTLoop<Name>_ and assign that to 
		 * int <Name> = _PTLoop<Name>_  as the first statement of the loop! This is
		 * because Lambda expressions do not accept loop index as argument.
		 * This operation is done within this main processor! 
		 * */
		
		if(elementIsInvocation(annotatedElement)){
			InvocationProcessor processor = new InvocationProcessor(getFactory(), annotation, annotatedElement);
			processor.process();
		}
		
		else if (elementIsArrayDeclaration(annotatedElement)){
			TaskIDGroupProcessor processor = new TaskIDGroupProcessor(getFactory(), annotation, annotatedElement);
			processor.process();
		}
	}
	
	private boolean elementIsInvocation(CtLocalVariable<?> annotatedElement){
		CtExpression<?> defaultExpression = annotatedElement.getDefaultExpression();
		if (defaultExpression instanceof CtInvocationImpl<?>)
			return true;
		return false;
	}
	
	private boolean elementIsArrayDeclaration(CtLocalVariable<?> annotatedElement){
		String elementType = annotatedElement.getType().toString();
		if(elementType.contains("[]"))
			return true;
		return false;
	}
	
	private boolean elementIsCollectionDeclaration(){
		return false;
	}
}
