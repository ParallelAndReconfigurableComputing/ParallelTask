package sp.processors;

import java.util.List;

import sp.annotations.Future;
import spoon.reflect.Factory;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;

public class TaskIDGroupProcessor {
	
	private CtVariable<?> thisAnnotatedElement = null;
	private Future thisFutureAnnotation = null;
	private CtTypeReference<?> thisElementType = null;
	private CtTypeReference<?> thisGroupType = null;
	private Factory thisFactory = null;
	private int thisArrayDimension = 0;
	private String thisTaskIDGroupName = null;
	private String thisElementName = null;
	
	public TaskIDGroupProcessor(Factory factory, Future future, CtVariable<?> annotatedElement){
		thisAnnotatedElement = annotatedElement;
		thisFutureAnnotation = future;
		thisFactory = factory;
		thisElementType = thisAnnotatedElement.getType();	
		thisGroupType = thisFactory.Core().createTypeReference();
		thisElementName = thisAnnotatedElement.getSimpleName();
		thisTaskIDGroupName = SpoonUtils.getTaskIDGroupName(thisElementName);
	}
	
	public void process(){
		thisArrayDimension = detectArrayTypeAndDimension();
		if(thisArrayDimension != 1)
			throw new IllegalArgumentException("ONLY ONE DIMENSIONAL ARRAYS CAN BE DECLARED AS FUTURE ARRAYS!\n"
					+ "ERROR DETECTED IN: " + thisAnnotatedElement);
		List<CtStatement> occurrences = SpoonUtils.findVarAccessOtherThanFutureDefinition((CtBlock<?>)thisAnnotatedElement.getParent(), thisAnnotatedElement);
		for(CtStatement occurrence : occurrences){
			System.out.println("statement: " + occurrence + ", statement type: " + occurrence.getClass()); 
		}
	}
	
	private void printComponents(){
		System.out.println("Signature: " + thisAnnotatedElement.getSignature());
		System.out.println("SimpleName: " + thisAnnotatedElement.getSimpleName());
		System.out.println("Class: " + thisAnnotatedElement.getClass().toString());
		System.out.println("Default Expression: " + thisAnnotatedElement.getDefaultExpression());
		System.out.println("Position: " + thisAnnotatedElement.getPosition().toString());
		System.out.println("Reference: " + thisAnnotatedElement.getReference().toString());
		System.out.println("Reference Type: " + thisAnnotatedElement.getReferencedTypes().toString());
		System.out.println("Type: " + thisAnnotatedElement.getType().toString());
	}
	
	private int detectArrayTypeAndDimension(){
		int counter = 0;
		String elementType = thisElementType.toString();
		String type = elementType.substring(0, elementType.indexOf('['));
		type = SpoonUtils.getType(type.trim());
		thisGroupType.setSimpleName(type);
		while(elementType.indexOf(']') != -1){
			elementType = elementType.substring(elementType.indexOf(']')+1);
			counter++;
		}
		return counter;
	}
}
