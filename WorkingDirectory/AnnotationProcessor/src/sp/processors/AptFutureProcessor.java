package sp.processors;

import sp.annotations.Future;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtVariable;
import spoon.support.reflect.code.CtInvocationImpl;

/**
 * This class is called by the annotation processor, for processing any code
 * element that is annotated with the Future annotation.
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class AptFutureProcessor extends AbstractAnnotationProcessor<Future, CtVariable<?>> {

	@Override
	public void process(Future annotation, CtVariable<?> annotatedElement) {
		/*
		 * Also, initialize ParaTask at the beginning of each method. 
		 * A common step for every annotated element with @Future is to look into
		 * its parent loop statement and change the name of the loop variable (the one
		 * inside the loop) to _PTLoop<Name>_ and assign that to 
		 * int <Name> = _PTLoop<Name>_  as the first statement of the loop! This is
		 * because Lambda expressions do not accept loop index as argument.
		 * This operation is done within this main processor! 
		 * */
		if(!(annotatedElement instanceof CtLocalVariable<?>) && !(annotatedElement instanceof CtField<?>)){
			System.err.println("ERROR: FUTURE ANNOTATIONS ARE ONLY PERMITTED FOR FIELDS AND LOCAL VARIABLES!");
			return;
		}
		
		if(elementIsCollectionDeclaration(annotatedElement)){
			HybridCollectionProcessor processor;
			if(annotatedElement instanceof CtLocalVariable<?>){
				CtLocalVariable<?> element = (CtLocalVariable<?>) annotatedElement;
				processor = new HybridCollectionProcessor(getFactory(), annotation, element);
				processor.process();
			}else{
				CtField<?> element = (CtField<?>) annotatedElement;
				processor = new FieldHybridCollectionProcessor(getFactory(), annotation, element);
				processor.process();
			}
		}
		
		else if (elementIsArrayDeclaration(annotatedElement)){
			FutureGroupProcessor processor;
			if(annotatedElement instanceof CtLocalVariable<?>){
				CtLocalVariable<?> element = (CtLocalVariable<?>) annotatedElement;
				processor = new FutureGroupProcessor(getFactory(), annotation, element);
			}else{
				CtField<?> element = (CtField<?>) annotatedElement;
				processor = new FieldFutureGroupProcessor(getFactory(), annotation, element);
			}
			processor.process();
		}
		
		/*
		 * It is important to keep this check the last, because other cases may also contain an invocation expression
		 * that returns an array, a collection, etc.
		 */
		else if(elementIsInvocation(annotatedElement)){
			if(annotatedElement instanceof CtField<?>){
				System.err.println("ERROR FOR ELEMENT: " + annotatedElement.toString());
				System.err.println("GLOBAL FUTURE VARIABLES ARE NOT PERMITTED. ONLY FUTURE ARRAYS AND HYBRID COLLECTIONS CAN BE"
						+ " DEFINED GLOBALLY! FUTURE VARIABLES ARE ONLY SUPPORTED FOR LOCAL SCOPES!");
				return;
			}			
			CtLocalVariable<?> element = (CtLocalVariable<?>) annotatedElement;
			InvocationProcessor processor = new InvocationProcessor(getFactory(), annotation, element);
			processor.process();
		}
		
		else{
			System.err.println("ERROR FOR ELEMENT: " + annotatedElement.toString());
			System.err.println("THE SPECIFIED ELEMENT IS NOT IN ANY OF THE SUPPORTED FORMATS FOR @Future");
			System.err.println("@Future CAN ONLY APPEAR ON ONE-DIMENSIONAL ARRAYS, SPECIFIC TYPES OF COLLECTIONS "
					+ "\nAND LOCAL VARIABLE DECLARATRIONS THT INVOLVE METHOD INVOCATIONS!");
			return;
		}
	}

	private boolean elementIsInvocation(CtVariable<?> annotatedElement){
		CtExpression<?> defaultExpression = annotatedElement.getDefaultExpression();
		if (defaultExpression instanceof CtInvocationImpl<?>)
			return true;
		return false;
	}
	
	private boolean elementIsArrayDeclaration(CtVariable<?> annotatedElement){
		String elementType = annotatedElement.getType().toString();
		if(elementType.contains("[]"))
			return true;
		return false;
	}
	
	private boolean elementIsCollectionDeclaration(CtVariable<?> annotatedElement){
		String elementType = annotatedElement.getType().toString();
		if(elementType.contains("List") || elementType.contains("Set") || elementType.contains("Map") || elementType.contains("Collection")) {
			String defaultExpression = annotatedElement.getDefaultExpression().toString();
			if(defaultExpression.contains(APTUtils.getGetWrapperSyntax()))
				return true;		
		}
		return false;
	}
}
