package sp.processors;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sp.annotations.Future;
import sp.processors.APTUtils.ExpressionRole;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtAnnotation;
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
	protected Factory thisFactory = null;
	protected Map<CtStatement, Map<CtExpression<?>, ExpressionRole>> mapOfContainingStatements = null;
	
	
	
	/**
	 * each sub-class implements this method, as a starting
	 * point for processing the annotated elements
	 */
	public abstract void process();
		
	/*
	 * each sub-class modifies statements in its own way!
	 */
	protected abstract void modifySourceCode(); 
	
//----------------------------------------------------HELPER METHODS---------------------------------------------------

	protected Future hasFutureAnnotation(CtStatement declarationStatement){
		//CtLocalVariable<?> declarationStatement = (CtLocalVariable<?>) statement;
		List<CtAnnotation<? extends Annotation>> annotations = declarationStatement.getAnnotations();
		for(CtAnnotation<? extends Annotation> annotation : annotations){
			Annotation actualAnnotation = annotation.getActualAnnotation();
			if(actualAnnotation instanceof Future){
				Future future = (Future) actualAnnotation;
				return future;
			}
		}
		return null;
	}
	
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
	
	protected void printVarAccessComponents(CtVariableAccess<?> variableAccess){
		System.out.println("Signature: " + variableAccess.getSignature().toString());
		System.out.println("Variable: " + variableAccess.getVariable().toString());
	}
	
	protected void printInvocationComponents(CtInvocation<?> invocation){
		System.out.println("Signature: " + invocation.getSignature());
		System.out.println("label: " + invocation.getLabel());
		System.out.println("Arguments: " + invocation.getArguments());
		System.out.println("Executable: " + invocation.getExecutable());
		System.out.println("Target: " + invocation.getTarget());
		System.out.println("Type: " + invocation.getType());
	}
	
	/*
	 * each sub-class can implement this method,
	 * that allows printing the components of an annotated
	 * element, in order to help with identifying the components
	 */
	protected void printLocalVariableComponents(CtLocalVariable<?> localVariable){
		System.out.println("Signature: " + localVariable.getSignature());
		System.out.println("SimpleName: " + localVariable.getSimpleName());
		System.out.println("Class: " + localVariable.getClass().toString());
		System.out.println("Default Expression: " + localVariable.getDefaultExpression());
		System.out.println("Position: " + localVariable.getPosition().toString());
		System.out.println("Reference: " + localVariable.getReference().toString());
		System.out.println("Reference Type: " + localVariable.getReferencedTypes().toString());
		System.out.println("Type: " + localVariable.getType().toString());
	}		
}
