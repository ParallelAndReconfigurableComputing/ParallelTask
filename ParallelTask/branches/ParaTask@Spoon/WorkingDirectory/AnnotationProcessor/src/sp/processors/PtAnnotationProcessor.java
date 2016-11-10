package sp.processors;

import java.util.Map;
import java.util.Set;

import sp.annotations.Future;
import sp.processors.APTUtils.ExpressionRole;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.reference.CtTypeReference;

/**
 * This abstract class must be implemented by every annotation processor. 
 * 
 * @author Mostafa Mehrabi
 *
 */
public abstract class PtAnnotationProcessor {
	
	protected CtLocalVariable<?> thisAnnotatedElement = null;
	protected String thisElementName = null;
	protected CtTypeReference<?> thisElementType = null;
	protected Future thisFutureAnnotation = null;
	protected Factory thisFactory = null;
	protected Map<CtStatement, Map<CtExpression<?>, ExpressionRole>> mapOfContainingStatements = null;
	
	protected void printIncludingExpressions(){
		Set<CtStatement> statements = mapOfContainingStatements.keySet();
		for(CtStatement statement : statements){
			System.out.println("--------------------------------------------------------");
			System.out.println("Inspecting statement: " + statement + ", type: " + statement.getClass());
			Map<CtExpression<?>, ExpressionRole> mapOfExp = mapOfContainingStatements.get(statement);
			Set<CtExpression<?>> expressions = mapOfExp.keySet();
			
			for(CtExpression<?> expression : expressions){
				System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
				System.out.print("Expression: " + expression.toString());
				
				String role = "";
				role = mapOfExp.get(expression).toString();
				
				System.out.println(", role: " + role + ", class: " + expression.getClass());
			}					
		}
	}
	
	/**
	 * each sub-class implements this method, as a starting
	 * point for processing the annotated elements
	 */
	public abstract void process();
	
	/*
	 * each sub-class can implement this method,
	 * that allows printing the components of an annotated
	 * element, in order to help with identifying the components
	 */
	protected void printComponents(){
		System.out.println("Signature: " + thisAnnotatedElement.getSignature());
		System.out.println("SimpleName: " + thisAnnotatedElement.getSimpleName());
		System.out.println("Class: " + thisAnnotatedElement.getClass().toString());
		System.out.println("Default Expression: " + thisAnnotatedElement.getDefaultExpression());
		System.out.println("Position: " + thisAnnotatedElement.getPosition().toString());
		System.out.println("Reference: " + thisAnnotatedElement.getReference().toString());
		System.out.println("Reference Type: " + thisAnnotatedElement.getReferencedTypes().toString());
		System.out.println("Type: " + thisAnnotatedElement.getType().toString());
	}	
	
	/*
	 * each sub-class modifies statements in its own way!
	 */
	protected abstract void modifySourceCode(); 
}
