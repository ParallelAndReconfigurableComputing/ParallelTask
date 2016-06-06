package sp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.ParentBuilder;
import pt.wrappers.PtMapWrapper;
import sp.annotations.Future;
import sp.annotations.StatementMatcherFilter;
import sp.processors.SpoonUtils.ExpressionRole;
import spoon.reflect.Factory;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtTry;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtArrayAccessImpl;
import spoon.support.reflect.code.CtAssignmentImpl;
import spoon.support.reflect.code.CtTryImpl;

public class TaskIDGroupProcessor extends PtAnnotationProcessor{
	
	private CtVariable<?> thisAnnotatedElement = null;
	private Future thisFutureAnnotation = null;
	private CtTypeReference<?> thisElementType = null;
	private CtTypeReference<?> thisGroupType = null;
	private Factory thisFactory = null;
	private int thisArrayDimension = 0;
	private String thisTaskIDGroupName = null;
	private String thisGroupSize = null;
	
	public TaskIDGroupProcessor(Factory factory, Future future, CtVariable<?> annotatedElement){
		thisAnnotatedElement = annotatedElement;
		thisFutureAnnotation = future;
		thisFactory = factory;
		thisElementType = thisAnnotatedElement.getType();	
		thisGroupType = thisFactory.Core().createTypeReference();
		thisElementName = thisAnnotatedElement.getSimpleName();
		thisTaskIDGroupName = SpoonUtils.getTaskIDGroupName(thisElementName);
	}
	
	@Override
	public void process(){
		inspectArrayDeclaration();
		modifySourceCode();
	}	
	
	private void inspectArrayDeclaration(){
		int counter = 0;
		String elementType = thisElementType.toString();
		String type = elementType.substring(0, elementType.indexOf('['));
		type = SpoonUtils.getType(type.trim());
		thisGroupType.setSimpleName(type);
		while(elementType.indexOf(']') != -1){
			elementType = elementType.substring(elementType.indexOf(']')+1);
			counter++;
		}
		
		thisArrayDimension = counter;
		if(thisArrayDimension != 1)
			throw new IllegalArgumentException("ONLY ONE DIMENSIONAL ARRAYS CAN BE DECLARED AS FUTURE ARRAYS!\n"
					+ "ERROR DETECTED IN: " + thisAnnotatedElement);
		
		String defaultExpression = thisAnnotatedElement.getDefaultExpression().toString();
		thisGroupSize = defaultExpression.substring(defaultExpression.lastIndexOf('[')+1, defaultExpression.indexOf(']'));		
	}

	@Override
	protected void modifySourceCode() {
		List<CtStatement> occurrences = SpoonUtils.findVarAccessOtherThanFutureDefinition(thisAnnotatedElement.getParent(CtBlock.class), thisAnnotatedElement);
		mapOfContainingStatements = SpoonUtils.listAllExpressionsOfStatements(occurrences);
		declareTaskIDGroup();
		modifyArrayAccessStatements();
	}
	
	
	private void modifyArrayAccessStatements(){
		Set<CtStatement> statements = mapOfContainingStatements.keySet();
		for(CtStatement statement : statements){
			Set<CtExpression<?>> expressions = mapOfContainingStatements.get(statement).keySet();
			for(CtExpression<?> expression : expressions){
				ExpressionRole expressionRole = mapOfContainingStatements.get(statement).get(expression);
				if(containsArrayElementSyntax(expression.toString())){
					if(expressionRole.equals(ExpressionRole.Assigned)){
						CtAssignmentImpl<?, ?> assignmentStmt = (CtAssignmentImpl<?, ?>) statement;
						CtExpression<?> assignmentExp = assignmentStmt.getAssignment();
						if(!containsArrayElementSyntax(assignmentExp.toString()))
							modifyAssignmentStatement(assignmentStmt);
						else{
							insertWaitForTaskGroupBlock(statement);
							return;
						}							
					}
					else{
						insertWaitForTaskGroupBlock(statement);
						return;
					}
				}
			}
		}
	}
	
	
	private void declareTaskIDGroup(){
		CtLocalVariable<?> taskIDGroupDeclartion = thisFactory.Core().createLocalVariable();
		String type = SpoonUtils.getTaskIDGroupSyntax() + "<" + thisGroupType.toString() + ">";
		
		CtTypeReference taskIDGroupType = thisFactory.Core().createTypeReference();
		taskIDGroupType.setSimpleName(type);
		taskIDGroupDeclartion.setType(taskIDGroupType);
		taskIDGroupDeclartion.setSimpleName(thisTaskIDGroupName);
		
		String defaultExpressionString = "new " + SpoonUtils.getTaskIDGroupSyntax() + "<>(" + thisGroupSize + ")";
		CtCodeSnippetExpression defaultExpression = thisFactory.Core().createCodeSnippetExpression();
		defaultExpression.setValue(defaultExpressionString);
		taskIDGroupDeclartion.setDefaultExpression(defaultExpression);
		
		CtBlock<?> parentBlock = thisAnnotatedElement.getParent(CtBlock.class);
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(
				SpoonUtils.getDeclarationStatement(thisAnnotatedElement, thisElementName));
		
		parentBlock.insertAfter(filter, taskIDGroupDeclartion);
	}
	
	/*
	 * Three types of assignments must be considered by the compiler.
	 * 
	 * 1. array[i] = _taskIDReplacement_.getReturnResult(); 
	 *                    //__taskIDReplacement__ belongs to a future variable
	 *                    //that has been already processed by the compiler.
	 *                    //This means, that the future variable is declared
	 *                    //before 'array' the future TaskIDGroup
	 * 
	 * 2.	@Future
	 * 		Obj a = foo();
	 * 		array[i] = a; //where 'a' is a future variable that is defined
	 *                    //after the declaration of 'array' and before
	 *                    //this assignment statement.
	 * 
	 * 3. array[i] = foo(args); //where we have to define 'foo' as an 
	 *                          //asynchronous task within the code 
	 *                          //separately 
	 */
	private void modifyAssignmentStatement(CtAssignmentImpl<?, ?> accessStatement){
		
	}
	
	private void insertWaitForTaskGroupBlock(CtStatement statement){
		System.out.println("statement: " + statement.toString());
		CtElement containingBlock = thisAnnotatedElement.getParent(CtBlock.class);
		CtElement parent = statement.getParent(CtBlock.class);
		
		
		System.out.println("containing Block: \n" + containingBlock.toString());
		System.out.println("--------------------------------------------------");
		System.out.println("Parent Block: \n" + parent.toString()); 
		if(containingBlock.equals(parent))
			System.out.println("blocks are identical");
		
		while(!containingBlock.equals(parent.getParent())){
			parent = parent.getParent();
		}
		
		System.out.println("statement changed: " + parent.toString());

		CtTry tryBlock = createTryBlock();
		CtCatch catchBlock = createCatchBlock();
		
		List<CtCatch> catchers = new ArrayList<>();
		catchers.add(catchBlock);
		
		tryBlock.setCatchers(catchers);
		CtStatement parentStatemtn = (CtStatement) parent;
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(parentStatemtn);
		((CtBlock<?>)containingBlock).insertBefore(filter, tryBlock);
	}
		
	
	
	
	//-------------------------------------------------------HELPER METHODS------------------------------------------------------
	
	private CtTry createTryBlock(){
		CtTry tryBlock = thisFactory.Core().createTry();
		CtBlock<?> tryBody = thisFactory.Core().createBlock();
		
		List<CtStatement> statements = new ArrayList<>();
		CtCodeSnippetStatement statement = thisFactory.Core().createCodeSnippetStatement();
		
		String statementSyntax = thisTaskIDGroupName + ".waitTillFinished()";
		statement.setValue(statementSyntax);
		statements.add(statement);
		
		tryBody.setStatements(statements);
		tryBlock.setBody(tryBody);
		
		return tryBlock;
	}
	
	private CtCatch createCatchBlock(String exceptionType){
		return null;
	}
	
	private CtCatch createCatchBlock(String exceptionType, String message){
		return null;
	}
	
	private CtCatch createCatchBlock(){
		CtCatch catchBlock = thisFactory.Core().createCatch();
		
		CtTypeReference exceptionType = thisFactory.Core().createTypeReference();
		exceptionType.setSimpleName("Exception");
		
		CtLocalVariable<? extends Throwable> catchParameter = thisFactory.Core().createLocalVariable();
		catchParameter.setType(exceptionType);
		catchParameter.setSimpleName("e");
		catchBlock.setParameter(catchParameter);
		
		List<CtStatement> catchStatements = new ArrayList<>();
		CtCodeSnippetStatement catchStatement = thisFactory.Core().createCodeSnippetStatement();
		catchStatement.setValue("e.printStackTrace()");
		catchStatements.add(catchStatement);
		
		
		CtBlock<?> catchBody = thisFactory.Core().createBlock();
		catchBody.setStatements(catchStatements);
		catchBlock.setBody(catchBody);
		
		return catchBlock;
	}	
	
	private boolean containsArrayElementSyntax(String component){
		String regex = "\\b" + thisElementName + "\\b" + "\\[";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(component);
		if(matcher.find())
			return true;
		return false;
	}
	
	@Override
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
	
	//-------------------------------------------------------HELPER METHODS------------------------------------------------------
	 
}
