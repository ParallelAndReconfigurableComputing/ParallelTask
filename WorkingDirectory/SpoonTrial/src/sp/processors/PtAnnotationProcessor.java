package sp.processors;

import java.util.Map;
import java.util.Set;

import sp.processors.SpoonUtils.ExpressionRole;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;
import spoon.reflect.reference.CtTypeReference;

public abstract class PtAnnotationProcessor {
	
	protected CtStatement thisAnnotatedElement = null;
	protected String thisElementName = null;
	protected CtTypeReference<?> thisElementType = null;
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
				
				System.out.println(", role: " + role);
			}					
		}
	}
	
	/*
	 * each sub-class implements this method, as a starting
	 * point for processing the annotated elements
	 */
	public abstract void process();
	
	/*
	 * each sub-class implements this method,
	 * that allows printing the components of an annotated
	 * element, in order to help with identifying the components
	 */
	protected abstract void printComponents();
	/*
	 * each sub-class modifies statements in its own way!
	 */
	protected abstract void modifySourceCode(); 
}
