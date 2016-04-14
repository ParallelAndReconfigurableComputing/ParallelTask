package pt.processors;

import java.util.concurrent.ExecutionException;

import pt.annotations.FutureGroup;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.declaration.CtVariable;
import spoon.support.reflect.code.CtLocalVariableImpl;

/**
 * This annotation allows programmers to define an array of
 * objects and assign future tasks to them later. This
 * functionality is meant to mimic the definition of TaskIDGroups,
 * and then submitting individual TaskIDs to them.
 * </br>
 * Local array definitions are interpreted as CtLocalVariableImpl<?>
 * objects by spoon. Therefore, for an array definition such as:
 * </br></br>
 * <b>int[][] array = new int[5][6];</b>
 * </br></br>
 * <i>signature</i> is: array</br>
 * <i>simple name</i> is: array</br>
 * <i>default expression</i> is: new int[5][6]</br>
 * <i>type</i> is: int[][]</br>
 * 
 * @author Mostafa Mehrabi
 */
public class FutureGroupProcessor extends 
	AbstractAnnotationProcessor<FutureGroup, CtVariable<?>> {

	private FutureGroup thisElementAnnotation = null;
	private CtLocalVariableImpl<?> thisArrayElement;
	private String thisArrayType;	
	private int thisArrayDimension;
	
	@Override
	public void process(FutureGroup futureGroupAnnotation, CtVariable<?> annotatedElement) {
			if (!(annotatedElement instanceof CtLocalVariableImpl<?>))
				throw new RuntimeException("@FutureGroup can only be used for local array definitions");
			
			thisElementAnnotation = futureGroupAnnotation;
			thisArrayElement = (CtLocalVariableImpl<?>) annotatedElement;
			if(!setupArray())
				throw new RuntimeException("@FutureGroup annotation can only be used for declaring arrays!");
	}
	
	private boolean setupArray(){
		thisArrayType = thisArrayElement.getType().toString();
		thisArrayDimension = arrayDimension(thisArrayType);
		System.out.println("dimension: " + thisArrayDimension);
		if (thisArrayDimension != 0)
			return true;
		return false;
	}

	private int arrayDimension(String arrayType){
		int counter = 0;
		while(arrayType.contains("[]")){
			counter++;
			arrayType = arrayType.substring(0, (arrayType.lastIndexOf('[')));			
		}
		return counter;
	}
}
