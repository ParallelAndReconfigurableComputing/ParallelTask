package pt.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pt.annotations.FutureGroup;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtVariable;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.code.CtLoopImpl;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtPackageImpl;

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
	private boolean importDone;
	private CtClassImpl<?> outerClass;
	
	@Override
	public void process(FutureGroup futureGroupAnnotation, CtVariable<?> annotatedElement) {
			System.out.println("FutureGroup found");
		
			if (!(annotatedElement instanceof CtLocalVariableImpl<?>))
				throw new RuntimeException("@FutureGroup can only be used for local array definitions");
			
			thisElementAnnotation = futureGroupAnnotation;
			thisArrayElement = (CtLocalVariableImpl<?>) annotatedElement;
			importDone = false;
			
			if(!setupArray())
				throw new RuntimeException("@FutureGroup annotation can only be used for declaring arrays!");

			printParents();
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
	
	private void printContainingStatements(){
		//Thankfully, if there is a nested loop statement, spoon returns the outermost statement!
		List<CtStatement> containingStatements = SpoonUtils.findVarAccessOtherThanFutureDefinition((CtBlock<?>)thisArrayElement.getParent(), thisArrayElement);
		for(CtStatement statement : containingStatements){
			System.out.println("Containing statement: \n" + statement);
			System.out.println("-------------------------------------------");
			System.out.println("statement type: " + statement.getClass());
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
		}
	}
	
	private void printParents(){
		List<CtElement> parents = SpoonUtils.getParents(thisArrayElement);
		for(CtElement parent : parents){
			if((parent instanceof CtClassImpl<?>)){
				outerClass = (CtClassImpl<?>) parent;
			}
			System.out.println("Element parent: \n" + parent);
			System.out.println("-------------------------------------------");
			System.out.println("Parent type: " + parent.getClass());
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
		}
		System.out.println("outerclass: \n" + outerClass);
	}
	
	/*This should be done in a recursive way!*/
	private List<CtStatement> getBodyOfLoop(CtStatement loopBody){
		List<CtStatement> loopStatements = new ArrayList<CtStatement>();
		System.out.println("body type: " + loopBody.getClass());
		
		if(loopBody instanceof CtBlockImpl<?>){
			
			CtBlockImpl<?> block = (CtBlockImpl<?>) loopBody;
			List<CtStatement> statements = block.getStatements();
			
			for(CtStatement statement : statements){
				if (statement instanceof CtLoopImpl){
					loopStatements.addAll(getBodyOfLoop(((CtLoopImpl)statement).getBody()));
				}
				
				//also consider other possible statements
				loopStatements.add(statement);
			}
		}
		
		while(loopBody instanceof CtLoopImpl){
			CtLoopImpl lp = (CtLoopImpl) loopBody;
			loopBody = lp.getBody();
		}
		
		System.out.println(loopBody);
		return null;
	}
}
