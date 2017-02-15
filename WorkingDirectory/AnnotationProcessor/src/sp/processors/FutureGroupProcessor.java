package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sp.annotations.Future;
import sp.processors.APTUtils.ExpressionRole;
import spoon.reflect.factory.Factory;
import spoon.reflect.code.CtArrayRead;
import spoon.reflect.code.CtArrayWrite;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtCatchVariable;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewArray;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtAssignmentImpl;

/**
 * This annotation processor processes <code>Future</code> annotations that appear at the declaration
 * of a local array. For example:
 * </br></br>
 * (a)Future</br>
 * Obj[] futureGroup = new Obj[num];
 * </br></br>
 * The processor only allows one dimensional future groups. This type is specifically used for grouping
 * TaskID objects that return the same type (e.g., Integer), and allows users to set a barrier in their
 * program to wait for the tasks until they are all finished. The waiting occurs at the first access to
 * an element of the array. 
 * </br></br>
 * For example:
 * </br></br>
 * Obj element = futureGroup[0];
 * </br></br>
 * gets the following statements added before it by the compiler.
 * </br></br>
 * try{</br>
 * 	&nbsp;&nbsp;&nbsp;_futureGroupTaskID_.wiatTillFinished(); </br>
 * }catch(Exception e){</br>
 * 	&nbsp;&nbsp;&nbsp;e.printStackTrace();</br>
 * }</br>
 * </br>
 * for(int i = 0; i < futureGroup.size(); i++){</br>
 * 	&nbsp;&nbsp;&nbsp;futureGroup[i] = _futureGroupTaskID_.getReturnResult(i);</br>
 * }</br></br>
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class FutureGroupProcessor extends AptAbstractFutureProcessor{
	
	protected CtTypeReference<?> thisGroupType = null;
	protected CtExpression<?> thisGroupDeclarationExpression = null;
	protected Future thisFutureAnnotation = null;
	protected int thisArrayDimension = 0;
	protected String thisTaskIDGroupName = null;
	protected String thisGroupSize = null;
	protected String thisGroupSizeName = null;
	protected int ptLoopIndexCounter = 0;
	protected int ptAsyncTaskCounter = 0;
	protected boolean elasticTaskGroup = false;
	protected boolean instantiatedLater = false;
	protected boolean waitStatementEntered = false;
	protected CtClass parentClass = null;
	protected List<CtMethod<?>> synchronizedMethods = null;	

	
	protected FutureGroupProcessor(Factory factory, Future future){
		thisFutureAnnotation = future;
		thisFactory = factory;
		thisGroupType = thisFactory.Core().createTypeReference();
		thisGroupSize = "";
		thisGroupSizeName = "";
		synchronizedMethods = new ArrayList<>();
	}
	
	public FutureGroupProcessor(Factory factory, Future future, CtLocalVariable<?> annotatedElement){
		this(factory, future);
		thisAnnotatedLocalElement = annotatedElement;		
		thisElementType = thisAnnotatedLocalElement.getType();	
		thisElementName = thisAnnotatedLocalElement.getSimpleName();
		thisTaskIDGroupName = APTUtils.getTaskIDGroupName(thisElementName);
		thisGroupSizeName = APTUtils.getTaskIDGroupSizeSyntax(thisElementName);
		parentClass = thisAnnotatedLocalElement.getParent(CtClass.class);
	}	
	
	@Override
	public void process(){
		inspectFutureAnnotation();
		inspectArrayDeclaration();
		modifySourceCode();		
	}	
	
	protected void inspectFutureAnnotation(){
		thisElementReductionString = thisFutureAnnotation.reduction();
		if(!elasticTaskGroup){	//for field task groups this attribute is automatically set to true, 
			 					//so don't inspect further. 
			elasticTaskGroup = thisFutureAnnotation.elasticGroup();
		}
	}
	
	protected void inspectArrayDeclaration(){
		getInstantiationExpression(thisAnnotatedLocalElement);
		inspectArrayDeclaration(thisGroupDeclarationExpression.toString());
	}
	
	protected void inspectArrayDeclaration(String defaultExpression){
		int counter = 0;
		String elementType = thisElementType.toString();
		String type = elementType.substring(0, elementType.indexOf('['));
		type = APTUtils.getType(type.trim());
		thisGroupType.setSimpleName(type);
		while(elementType.indexOf(']') != -1){
			elementType = elementType.substring(elementType.indexOf(']')+1);
			counter++;
		}
		
		thisArrayDimension = counter;
		if(thisArrayDimension != 1)
			throw new IllegalArgumentException("ONLY ONE DIMENSIONAL ARRAYS CAN BE DECLARED AS FUTURE ARRAYS!\n"
					+ "ERROR DETECTED IN: " + thisAnnotatedLocalElement);
		
		thisGroupSize = defaultExpression.substring(defaultExpression.lastIndexOf('[')+1, defaultExpression.indexOf(']'));		

	}
		
	/*
	 * ElasticTaskGroups where there is no size-boundary for a task group. This is especifically the case where a task group
	 * is defined as a field. We should allow field declarations of future groups and hybrid collections. A field future group
	 * can only be an elastic task group, because the size of the array maybe unknown at the time of declaring the array. 
	 * @see sp.processors.PtAnnotationProcessor#modifySourceCode()
	 */
	@Override
	protected void modifySourceCode() {		
		listOfContainingNodes = APTUtils.listAllExpressionsOfStatements(findVarAccessStatements());
		insertNewStatements();
		modifyArrayAccessStatements();
	}
	
	protected List<CtStatement> findVarAccessStatements(){
		List<CtStatement> varAccessStatements = null;
		varAccessStatements = APTUtils.findVarAccessOtherThanFutureDefinition(thisAnnotatedLocalElement.getParent(CtBlock.class), thisAnnotatedLocalElement);
		return varAccessStatements;
	}
	
	private void insertNewStatements(){
		if(!(thisGroupType.toString().contains("Void"))){
			if(instantiatedLater)
				insertTaskIDSizeStatements();
		}
		List<CtStatement> reductionStatements = getReductionStatements(thisGroupType.toString(), APTUtils.getTaskIDGroupName(thisElementName));
		insertNewStatements(declareTaskIDGroup(), reductionStatements);
	}	
	
	/*
	 * Modifies the statements in which this future array is assigned a future variables. This future
	 * variable can be either declared before the declaration of this future array, or after that. 
	 * Both cases are considered when processing. In another case, an invocation can be assigned to an
	 * element of this future array, which will get a customized declaration by the compiler. 
	 * Moreover, once it encounters the first statement in which the value for an element of the future
	 * array is accessed, the compiler inserts the barrier phrase for waiting for all tasks of the 
	 * future array, until they are processed and finished.   
	 * 
	 * Local task groups conceptually expect the access point of an array element to be the moment, from
	 * which the programmer does not assign any other future variables to the task group, so the compiler
	 * stops processing after the first time that an element of the array is accessed. 
	 */
	protected void modifyArrayAccessStatements(){
		CtStatement currentStatement = null;
		try{
			for(ASTNode node : listOfContainingNodes){
				currentStatement = node.getStatement();
				if(containsFutureGroupSyntax(2, currentStatement.toString())){
					/*
					 * When the statement has the syntax of the future group in it, if the statement is 
					 * an "Assignment" statement, and the syntax of the future group only appears in the
					 * assigned part of the statement (i.e., left side of the assignment), and the left 
					 * side of the statement is an "arrayWrite" expression, and the syntax of the future group
					 * does not exist in the right side of the assignment statement, then an asynchronous task
					 * must be added to the future group  
					 */
					if(currentStatement instanceof CtAssignment<?, ?>){
						CtAssignment<?, ?> assignment = (CtAssignment<?, ?>) currentStatement;
						CtExpression<?> assignedExpression = assignment.getAssigned();
						CtExpression<?> assignmentExpression = assignment.getAssignment();
						//Only if future group does not appear in the assignment side, we proceed
						if(!containsFutureGroupSyntax(2, assignmentExpression.toString())){
							if(assignedExpression instanceof CtArrayWrite<?>){
								//if the future group is the main array (i.e., not bigArray[futureGroup[i]])
								if(containsFutureGroupSyntax(1, assignedExpression.toString())){
									modifyAssignmentStatement(assignment);
								}
								//otherwise, future group is not the main array
								else{									
									insertWaitStatement(assignment);
								}
							}
						}
						//Otherwise, if the future group appears in the assignment side as well,
						//the future group is referenced.
						else{
							insertWaitStatement(currentStatement);
						}
					}	
					
					/*
					 * If the statement contains the syntax of the future group and is not an assignment 
					 * statement, then the future group has been referenced. But, the wait statement must
					 * be inserted only if the syntax appears in one of the expressions of the statement,
					 * for the case of conditional statements and blocks, the inner statements must be
					 * inspected.
					 */
					else{
						for (int expIndex = 0; expIndex < node.numberOfExpressions(); expIndex++){
							CtExpression<?> expression = node.getExpression(expIndex);
							if(containsFutureGroupSyntax(2, expression.toString())){
								insertWaitStatement(currentStatement);
								break;
							}
						}
					}
				}	
			}
		}catch(Exception e){
			System.out.println("\nEXCEPTION WHILE ATTEMPTING TO MODIFY: " + currentStatement.toString() + " IN FUTURE-GROUP PROCESSOR\n");
			e.printStackTrace();
		}
	}
	
	
	/*
	 * The taskIdSize statement will initialize a new variable call taskIDGroupSize that indicates the 
	 * size of an array. These statements are only entered if the instantiation of an array happens anywhere
	 * other than its declaration point. In this case, the value that is used by the programmer to specify
	 * the size of the array may be limited to a specific scope, and not recognized in other scopes. Therefore,
	 * the compiler declares a variable in the same scope as the declared TaskGroup, for later looping over the
	 * elements of the taskIDGroup. 
	 */
	private void insertTaskIDSizeStatements(){
		CtLocalVariable<?> taskGroupSizeVarDeclaration = declareTaskIDGroupSizeVariable();
		CtAssignment<?, ?> instantiateTaskGroupSizeVariable = instantiateTaskIDGroupVariable();
		
		insertTaskIDSizeDeclaration(taskGroupSizeVarDeclaration);
		insertTaskIDSizeAssignment(instantiateTaskGroupSizeVariable);
	}
	
	protected void insertTaskIDSizeDeclaration(CtLocalVariable<?> taskGroupSizeVarDeclaration){
		thisAnnotatedLocalElement.insertBefore(taskGroupSizeVarDeclaration);
	}
	
	protected void insertTaskIDSizeAssignment(CtAssignment<?, ?> instantiateTaskGroupSizeVariable){
		CtStatement parentStatement = thisGroupDeclarationExpression.getParent(CtStatement.class);
		parentStatement.insertAfter(instantiateTaskGroupSizeVariable);
	}
	
	protected void insertNewStatements(CtLocalVariable<?> taskIDGroupDeclarationStatement, List<CtStatement> reductionStatements){
		CtStatementList statementList = thisFactory.Core().createStatementList();
		List<CtStatement> statements = new ArrayList<>();
		statements.add(taskIDGroupDeclarationStatement);
		if(reductionStatements != null)
			statements.addAll(reductionStatements);
		statementList.setStatements(statements);
		thisAnnotatedLocalElement.insertAfter(statementList);
	}
	
	
	/*
	 * Insert a declaration statement for the TaskIDGroup before the declaration for the array. 
	 */
	private CtLocalVariable<?> declareTaskIDGroup(){
		CtLocalVariable<?> taskIDGroupDeclartion = thisFactory.Core().createLocalVariable();
		String type = APTUtils.getTaskIDGroupSyntax() + "<" + thisGroupType.toString() + ">";
		
		CtTypeReference taskIDGroupType = thisFactory.Core().createTypeReference();
		taskIDGroupType.setSimpleName(type);
		taskIDGroupDeclartion.setType(taskIDGroupType);
		taskIDGroupDeclartion.setSimpleName(thisTaskIDGroupName);
		
		String taskSize = (elasticTaskGroup) ? "" : thisGroupSize;
		String defaultExpressionString = "new " + APTUtils.getTaskIDGroupSyntax() + "<>(" + taskSize + ")";
		CtCodeSnippetExpression defaultExpression = thisFactory.Core().createCodeSnippetExpression();
		defaultExpression.setValue(defaultExpressionString);
		taskIDGroupDeclartion.setDefaultExpression(defaultExpression);
		return taskIDGroupDeclartion;
	}
	
	protected CtVariable<?> getCurrentAnnotatedElement(){
		return thisAnnotatedLocalElement;
	}
	
	/*
	 * ArrayWrite statements are turned into asynchronous tasks only if the synchronization phrase
	 * is not entered in their enclosing method earlier.
	 * 
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
	protected void modifyAssignmentStatement(CtAssignment<?, ?> accessStatement){
		CtMethod<?> parentMethod = accessStatement.getParent(CtMethod.class);
		if(synchronizedMethods.contains(parentMethod))
			return;
		
		CtExpression<?> assignment = accessStatement.getAssignment();
		String assignmentString = assignment.toString();
		boolean statementModified = false;
		
		CtVariable<?> currentAnnotatedElement = getCurrentAnnotatedElement();
		
		if(APTUtils.isTaskIDReplacement(currentAnnotatedElement, assignmentString)){
			modifyWithTaskIDReplacement(accessStatement);
			statementModified = true;
		}
		
		else if(hasInvocationExpression(assignment)){
			modifyWithInvocation(accessStatement);
			statementModified = true;
		}
		
		else{
			CtStatement declarationStatement = APTUtils.getDeclarationStatement(accessStatement, assignmentString);
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
					+ " IS NOT SUPPORTED BY PARATASK FUTURE ARRAYS YET!\nPARATASK FUTURE ARRAYS ARE MEANT TO GROUP ASYNCHRONOUS TASKS ONLY!"
					+ "\nTHE EXPRESSION " + assignmentString + " IS PROBABLY COMBINING TASKID WITH OTHER TYPES OF EXPRESSIONS!");
	}
	
	protected CtBlock<?> getParentBlockForWaitStatement(CtStatement containingStatement){
		return thisAnnotatedLocalElement.getParent(CtBlock.class);
	}
	
	protected void insertWaitStatement(CtStatement statement){
		CtMethod<?> parentMethod = statement.getParent(CtMethod.class);
		if(synchronizedMethods.contains(parentMethod)){
			return;
		}
		else{
			synchronizedMethods.add(parentMethod);
			insertWaitForTaskGroupBlock(statement);
		}
	}
	
	protected void insertWaitForTaskGroupBlock(CtStatement containingStatement){
		try{
			CtBlock<?> parentBlockOfAnnotatedElement = getParentBlockForWaitStatement(containingStatement);
			CtElement parentStatement = containingStatement;
					
			//go to the same scope in which the task group is defined. 
			while(!parentBlockOfAnnotatedElement.equals(parentStatement.getParent())){
				parentStatement = parentStatement.getParent();
			}
			
			List<CtStatement> waitForStatements = getWaitForTaskGroupStatements();
			StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>((CtStatement)parentStatement);

			for(CtStatement waitStatement : waitForStatements){
				parentBlockOfAnnotatedElement.insertBefore(filter, waitStatement);
			}			
		}catch(Exception e){
			System.out.println("EXCEPTION WAS THROWN WHEN INSERTING WAIT BLOCK FOR STATEMENT: " + containingStatement);
		}
	}
	
	protected List<CtStatement> getWaitForTaskGroupStatements(){
		return getTryAndForBlocks();
	}	
	
	protected List<CtStatement> getTryAndForBlocks(){
		List<CtStatement> waitForStatements = new ArrayList<>();
		CtTry tryBlock = createTryBlock();
		CtCatch catchBlock = createCatchBlock();
		
		List<CtCatch> catchers = new ArrayList<>();
		catchers.add(catchBlock);
		
		tryBlock.setCatchers(catchers);
		waitForStatements.add(tryBlock);
		
		if(!(thisGroupType.toString().contains("Void"))){
			waitForStatements.add(createForLoopForRetrievingArrayValues());
		}
		
		return waitForStatements;
	}
		
	private void modifyWithTaskIDReplacement(CtAssignment<?, ?> accessStatement){
		String assignedString   = accessStatement.getAssigned().toString();
		String assignmentString = accessStatement.getAssignment().toString();
		String taskIDName = APTUtils.getTaskIDName(APTUtils.getOrigName(assignmentString));
		
		String index = assignedString.substring(assignedString.indexOf('[')+1, assignedString.indexOf(']'));
		
		CtCodeSnippetStatement newStatement = thisFactory.Core().createCodeSnippetStatement();
		String newStatementString = thisTaskIDGroupName + ".setInnerTask(" + index + ", " + taskIDName + ")";
		newStatement.setValue(newStatementString);
		accessStatement.replace(newStatement);		
	}	
	
	private void modifyWithInvocation(CtAssignment<?, ?> accessStatement){
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
		
		String newAssignment = APTUtils.getTaskIDName(asyncTaskName) + ".getReturnResult()";
		CtCodeSnippetExpression newAssignmentExpression = thisFactory.Core().createCodeSnippetExpression();
		newAssignmentExpression.setValue(newAssignment);
		accessStatement.setAssignment(newAssignmentExpression);
		modifyWithTaskIDReplacement(accessStatement);
	}
	
	private void modifyWithFutuerObject(Future future, CtStatement ObjDeclaration, CtAssignment<?, ?> accessStatement){
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
	
	/*
	 * If the size of the array is not specified at the time of declaring the array, then 
	 * the taskIDGroup will be an elastic one. 
	 */
	protected void getInstantiationExpression(CtVariable<?> variable){
		try{
			thisGroupDeclarationExpression = variable.getDefaultExpression();
			if(thisGroupDeclarationExpression == null){
				instantiatedLater = true;
				elasticTaskGroup = true;
				thisGroupDeclarationExpression = findDefaultExpression(variable);
			}
		}catch(Exception e){
			System.err.println("There was no \"new\" statement found for future group: " + thisElementName);
			e.printStackTrace();
		}
	}
	
	/*
	 * Returns the first instantiation statement that is found for a future array. 
	 * If a future array is instantiated more than once through the user-code, the
	 * behavior of the compiler will be undefined, as there is no guarantee on which
	 * instantiation statement will be found first by the compiler. 
	 */
	private CtExpression<?> findDefaultExpression(CtVariable<?> variable) throws Exception{
		List<CtStatement> varAccessStatements = findVarAccessStatements();
		List<ASTNode> varAccessNodes = null;
		boolean assignmentFound = false;
		
		if(varAccessStatements != null){
			varAccessNodes = APTUtils.listAllExpressionsOfStatements(varAccessStatements);
		}
		
		for(ASTNode varAccessNode : varAccessNodes){
			assignmentFound = false;
			CtStatement nodeStatement = varAccessNode.getStatement();
			
			if(nodeStatement instanceof CtAssignment<?, ?>){
				
				int numOfExpressions = varAccessNode.numberOfExpressions();
				
				for(int index = 0; index < numOfExpressions; index++){
					CtExpression<?> expression = varAccessNode.getExpression(index);
					ExpressionRole role = varAccessNode.getExpressionRole(index);
					if(role.equals(ExpressionRole.Assigned)){
						String expressionName = expression.toString();
						if(expressionName.equals(variable.getSimpleName()))
							assignmentFound = true;
					}
					else if(role.equals(ExpressionRole.Assignment)){
						if(expression instanceof CtNewArray<?>){
							if(assignmentFound)
								return expression;
						}
					}
				}
			}
			
		}
		
		throw new Exception("FUTURE GROUP " + variable.getSimpleName() + " HAS BEEN DECLARED, BUT IS NOT INSTANTIATED WITH A NEW ARRAY STATEMENT!");
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
		return createCatchBlock("Exception", "");
	}	
		
	private CtLocalVariable<?> declareTaskIDGroupSizeVariable(){
		CtLocalVariable<?> taskIDGroupSizeVariable = thisFactory.Core().createLocalVariable();
		CtTypeReference taskIDGroupSizeType = thisFactory.Core().createTypeReference();
		taskIDGroupSizeType.setSimpleName("int");
		taskIDGroupSizeVariable.setType(taskIDGroupSizeType);
		taskIDGroupSizeVariable.setSimpleName(thisGroupSizeName);
		taskIDGroupSizeVariable.setDefaultExpression(null);
		return taskIDGroupSizeVariable;
	}
	
	private CtAssignment<?, ?> instantiateTaskIDGroupVariable(){
		CtAssignment<?, ?> instantiateTaskIDGroup = thisFactory.Core().createAssignment();
		CtCodeSnippetExpression assigned = thisFactory.Core().createCodeSnippetExpression();
		CtCodeSnippetExpression assignment = thisFactory.Core().createCodeSnippetExpression();
		
		assigned.setValue(thisGroupSizeName);
		assignment.setValue(thisGroupSize);
		
		instantiateTaskIDGroup.setAssigned(assigned);
		instantiateTaskIDGroup.setAssignment(assignment);
		
		return instantiateTaskIDGroup;
	}
	
	private CtCatch createCatchBlock(String exceptionClass, String message){
		CtCatch catchBlock = thisFactory.Core().createCatch();
		
		CtTypeReference exceptionType = thisFactory.Core().createTypeReference();
		exceptionType.setSimpleName(exceptionClass);
		
		String exceptionHandlingCommand = "";
		if(message.isEmpty())
			exceptionHandlingCommand = "e.printStackTrace()";
		else
			exceptionHandlingCommand = "e.printStackTrace(\"" + message +"\")";
		
		CtCatchVariable<? extends Throwable> catchParameter = thisFactory.Core().createCatchVariable();
		catchParameter.setType(exceptionType);
		catchParameter.setSimpleName("e");
		catchBlock.setParameter(catchParameter);
		
		List<CtStatement> catchStatements = new ArrayList<>();
		CtCodeSnippetStatement catchStatement = thisFactory.Core().createCodeSnippetStatement();
		
		catchStatement.setValue(exceptionHandlingCommand);
		catchStatements.add(catchStatement);
		
		
		CtBlock<?> catchBody = thisFactory.Core().createBlock();
		catchBody.setStatements(catchStatements);
		catchBlock.setBody(catchBody);
		
		return catchBlock;
	}
	
	private CtFor createForLoopForRetrievingArrayValues(){
		ptLoopIndexCounter++;
		
		CtFor forLoop = thisFactory.Core().createFor();
		CtCodeSnippetExpression loopCondition = thisFactory.Core().createCodeSnippetExpression();
		CtCodeSnippetStatement  initStmt = thisFactory.Core().createCodeSnippetStatement();
		CtCodeSnippetStatement  updateStmt = thisFactory.Core().createCodeSnippetStatement();
		CtCodeSnippetStatement  forBody = thisFactory.Core().createCodeSnippetStatement();
		String loopIndexName = "__" + thisElementName + "LoopIndex" + ptLoopIndexCounter + "__";
		
		String loopSize = (instantiatedLater) ? thisGroupSizeName : thisGroupSize;
		
		initStmt.setValue("int " + loopIndexName + " = 0");
		updateStmt.setValue(loopIndexName + "++");
		loopCondition.setValue(loopIndexName + " < " + loopSize);
		
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
	
	protected boolean containsFutureGroupSyntax(int mode, String component){
		if (mode != 1 && mode != 2){
			throw new IllegalArgumentException("Error in [FutureGroupProcessor::containsFutureGroupSyntax] method!\n"
					+ "The number passed for [mode] can only be either 1 or 2");
		}
		//mode 1 checks if the component is exactly the name of the future group and does 
		//not contain any other syntax
		if(mode == 1){
			String arrayName = component.substring(0, component.indexOf("["));
			if(arrayName.equals(thisElementName))
				return true;
		}
		//mode 2 checks if the component contains the name of the future group. That means,
		//the component may have other syntaxes as well (e.g., BigArray[futureGroup[i]])
		else if (mode == 2){
			//remove every occurrence of the future group on which methods are called. 
			String redundant = thisElementName+".";
			String tempStr = component.replace(redundant, "");
			if(tempStr.contains(thisElementName))
				return true;
		}
		return false;
	}	
	
	//-------------------------------------------------------HELPER METHODS------------------------------------------------------
	 
}
