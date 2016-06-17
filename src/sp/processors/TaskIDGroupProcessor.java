package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sp.annotations.Future;
import sp.annotations.StatementMatcherFilter;
import sp.processors.SpoonUtils.ExpressionRole;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtCatchVariable;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtTry;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtAssignmentImpl;


public class TaskIDGroupProcessor extends PtAnnotationProcessor{
	
	
	private CtTypeReference<?> thisElementType = null;
	private CtTypeReference<?> thisGroupType = null;
	private int thisArrayDimension = 0;
	private String thisTaskIDGroupName = null;
	private String thisGroupSize = null;
	private int ptLoopIndexCounter = 0;
	private int ptAsyncTaskCounter = 0;
	
	public TaskIDGroupProcessor(Factory factory, Future future, CtLocalVariable<?> annotatedElement){
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
		CtStatement currentStatement = null;
		try{
			for(CtStatement statement : statements){
				currentStatement = statement;
				Set<CtExpression<?>> expressions = mapOfContainingStatements.get(statement).keySet();
				for(CtExpression<?> expression : expressions){
					ExpressionRole expressionRole = mapOfContainingStatements.get(statement).get(expression);
					if(containsArrayElementSyntax(expression.toString())){
						if(expressionRole.equals(ExpressionRole.Assigned)){
							CtAssignmentImpl<?, ?> assignmentStmt = (CtAssignmentImpl<?, ?>) statement;
							CtExpression<?> assignmentExp = assignmentStmt.getAssignment();
							if(!containsArrayElementSyntax(assignmentExp.toString())){
								modifyAssignmentStatement(assignmentStmt);
								break;
							}
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
		}catch(Exception e){
			System.out.println("\nEXCEPTION WHILE ATTEMPTING TO MODIFY: " + currentStatement.toString() + " IN TASKIDGROUP PROCESSOR\n");
			e.printStackTrace();
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
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(thisAnnotatedElement);
		
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
		CtExpression<?> assignment = accessStatement.getAssignment();
		String assignmentString = assignment.toString();
		boolean statementModified = false;
		
		
		
		if(SpoonUtils.isTaskIDReplacement(thisAnnotatedElement, assignmentString)){
			modifyWithTaskIDReplacement(accessStatement);
			statementModified = true;
		}
		
		else if(hasInvocationExpression(assignment)){
			modifyWithInvocation(accessStatement);
			statementModified = true;
		}
		
		else{
			CtStatement declarationStatement = SpoonUtils.getDeclarationStatement(accessStatement, assignmentString);
			if(declarationStatement != null){
				Future future = hasFutureAnnotation(declarationStatement);
				if(future != null){
					modifyWithFutuerObject(future, declarationStatement, accessStatement);
					statementModified = true;
				}
			}
		}
		
		if(!statementModified)
			throw new UnsupportedOperationException("\nTHE EXPRESSION " + assignmentString + " IN STATEMENT: " + accessStatement 
					+ " IS NOT SUPPORTED BY PARATASK FUTURE ARRAYS YET!\nPARATASK FUTURE ARRAYS ARE MEANT TO GROUP ASYNCHRONOUS TASKS!");
	}
	
	private void insertWaitForTaskGroupBlock(CtStatement statement){
		try{
		CtElement containingBlock = thisAnnotatedElement.getParent(CtBlock.class);
		//CtElement parent = statement.getParent(CtBlock.class);
		CtElement parent = statement;
				
		while(!containingBlock.equals(parent.getParent())){
			parent = parent.getParent();
		}
		
		CtTry tryBlock = createTryBlock();
		CtCatch catchBlock = createCatchBlock();
		
		List<CtCatch> catchers = new ArrayList<>();
		catchers.add(catchBlock);
		
		tryBlock.setCatchers(catchers);
		CtStatement parentStatemtn = (CtStatement) parent;
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(parentStatemtn);
		((CtBlock<?>)containingBlock).insertBefore(filter, tryBlock);
		
		if(!(thisGroupType.toString().contains("Void"))){
			CtFor forLoop = createForLoop();
			((CtBlock<?>)containingBlock).insertBefore(filter, forLoop);
		}
		}catch(Exception e){
			System.out.println("EXCEPTION WAS THROWN");
		}
	}
		
	private void modifyWithTaskIDReplacement(CtAssignmentImpl<?, ?> accessStatement){
		String assignedString   = accessStatement.getAssigned().toString();
		String assignmentString = accessStatement.getAssignment().toString();
		String taskIDName = SpoonUtils.getTaskIDName(SpoonUtils.getOrigName(assignmentString));
		
		String index = assignedString.substring(assignedString.indexOf('[')+1, assignedString.indexOf(']'));
		
		CtCodeSnippetStatement newStatement = thisFactory.Core().createCodeSnippetStatement();
		String newStatementString = thisTaskIDGroupName + ".setInnerTask(" + index + ", " + taskIDName + ")";
		newStatement.setValue(newStatementString);
		accessStatement.replace(newStatement);		
	}	
	
	private void modifyWithInvocation(CtAssignmentImpl<?, ?> accessStatement){
		ptAsyncTaskCounter++;
		
		String asyncTaskName = "__" + thisElementName + "_" + ptAsyncTaskCounter +"__";
				
		CtAnnotation<?> futureAnnotation = thisFactory.Core().createAnnotation();
		CtTypeReference<? extends Annotation> annotationType = thisFactory.Core().createTypeReference();
		annotationType.setSimpleName("sp.annotations.Future");
	
		CtExpression<?> asyncTaskDefaultExpression = accessStatement.getAssignment();
		futureAnnotation.setAnnotationType(annotationType);
		Set<CtAnnotation<? extends Annotation>> annotations = new HashSet<>();
		annotations.add(futureAnnotation);
		
		CtLocalVariable localAsyncTask = thisFactory.Core().createLocalVariable();
		localAsyncTask.setType(thisGroupType);
		localAsyncTask.setDefaultExpression(asyncTaskDefaultExpression);
		localAsyncTask.setSimpleName(asyncTaskName);
		//localAsyncTask.setAnnotations(annotations);
		
		CtBlock parentBlock = accessStatement.getParent(CtBlock.class);
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(accessStatement);
		parentBlock.insertBefore(filter, localAsyncTask);
		InvocationProcessor processor = new InvocationProcessor(thisFactory, thisFutureAnnotation, localAsyncTask);
		processor.process();
		
		String newAssignment = SpoonUtils.getTaskIDName(asyncTaskName) + ".getReturnResult()";
		CtCodeSnippetExpression newAssignmentExpression = thisFactory.Core().createCodeSnippetExpression();
		newAssignmentExpression.setValue(newAssignment);
		accessStatement.setAssignment(newAssignmentExpression);
		modifyWithTaskIDReplacement(accessStatement);
	}
	
	private void modifyWithFutuerObject(Future future, CtStatement ObjDeclaration, CtAssignmentImpl<?, ?> accessStatement){
		CtLocalVariable<?> futureObjDeclaration = (CtLocalVariable<?>) ObjDeclaration;
		InvocationProcessor processor = new InvocationProcessor(thisFactory, future, futureObjDeclaration);
		processor.process();
		
		//Annotation processed, so remove it!
		List<CtAnnotation<? extends Annotation>> annotations = new ArrayList<>();
		List<CtAnnotation<? extends Annotation>> actualAnnotations = futureObjDeclaration.getAnnotations();
		for(CtAnnotation<? extends Annotation> annotation : actualAnnotations){
			Annotation actualAnnotation = annotation.getActualAnnotation();
			if(!(actualAnnotation instanceof Future))
				annotations.add(annotation);
		}
		futureObjDeclaration.setAnnotations(annotations);
		modifyWithTaskIDReplacement(accessStatement);
	}
	
	//-------------------------------------------------------HELPER METHODS------------------------------------------------------
	
	private Future hasFutureAnnotation(CtStatement statement){
		CtLocalVariable<?> declarationStatement = (CtLocalVariable<?>) statement;
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
	
	private boolean hasInvocationExpression(CtExpression<?> assignment){
		if(assignment instanceof CtInvocation<?>)
			return true;
		return false;
	}
	
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
	
	private CtCatch createCatchBlock(){
		return createCatchBlock("Exception");
	}	
		
	private CtCatch createCatchBlock(String exceptionClass){
		return createCatchBlock(exceptionClass, "");
	}
	
	private CtCatch createCatchBlock(String exceptionClass, String message){
		CtCatch catchBlock = thisFactory.Core().createCatch();
		
		CtTypeReference exceptionType = thisFactory.Core().createTypeReference();
		exceptionType.setSimpleName(exceptionClass);
		
		String exceptionMessage = "";
		if(message.isEmpty())
			exceptionMessage = "e.printStackTrace()";
		else
			exceptionMessage = "e.printStackTrace(\"" + message +"\")";
		
		CtCatchVariable<? extends Throwable> catchParameter = thisFactory.Core().createCatchVariable();
		catchParameter.setType(exceptionType);
		catchParameter.setSimpleName("e");
		catchBlock.setParameter(catchParameter);
		
		List<CtStatement> catchStatements = new ArrayList<>();
		CtCodeSnippetStatement catchStatement = thisFactory.Core().createCodeSnippetStatement();
		
		catchStatement.setValue(exceptionMessage);
		catchStatements.add(catchStatement);
		
		
		CtBlock<?> catchBody = thisFactory.Core().createBlock();
		catchBody.setStatements(catchStatements);
		catchBlock.setBody(catchBody);
		
		return catchBlock;
	}
	
	private CtFor createForLoop(){
		ptLoopIndexCounter++;
		
		CtFor forLoop = thisFactory.Core().createFor();
		CtCodeSnippetExpression loopCondition = thisFactory.Core().createCodeSnippetExpression();
		CtCodeSnippetStatement  initStmt = thisFactory.Core().createCodeSnippetStatement();
		CtCodeSnippetStatement  updateStmt = thisFactory.Core().createCodeSnippetStatement();
		CtCodeSnippetStatement  forBody = thisFactory.Core().createCodeSnippetStatement();
		String loopIndexName = "__ptLoopIndex" + ptLoopIndexCounter + "__";
		
		initStmt.setValue("int " + loopIndexName + " = 0");
		updateStmt.setValue(loopIndexName + "++");
		loopCondition.setValue(loopIndexName + " < " + thisGroupSize);
		
		String bodyString = thisElementName + "[" + loopIndexName + "] = " + thisTaskIDGroupName + ".getInnerTaskResult(" + loopIndexName + ")";    
		forBody.setValue(bodyString);
		
		List<CtStatement> initStmts = new ArrayList<>();
		initStmts.add(initStmt);
		
		List<CtStatement> updateStmts = new ArrayList<>();
		updateStmts.add(updateStmt);
		
		forLoop.setExpression(loopCondition);
		forLoop.setForInit(initStmts);
		forLoop.setForUpdate(updateStmts);
		forLoop.setBody(forBody);
		return forLoop;
	}
	
	private boolean containsArrayElementSyntax(String component){
		String regex = "\\b" + thisElementName + "\\b" + "\\[";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(component);
		if(matcher.find())
			return true;
		return false;
	}	
	
	//-------------------------------------------------------HELPER METHODS------------------------------------------------------
	 
}
