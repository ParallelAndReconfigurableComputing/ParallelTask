package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sp.annotations.Future;
import sp.annotations.ReductionMethod;
import sp.processors.APTUtils.ExpressionRole;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtArrayRead;
import spoon.reflect.code.CtArrayWrite;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtAssignmentImpl;

public class FieldFutureGroupProcessor extends FutureGroupProcessor {
	private Set<ModifierKind> fieldModifiers = new HashSet<>();
	/*
	 * syncBooleanFlagName is the name of a boolean flag that indicates
	 * whether the waiter method has been called on a future group or not.
	 * This will be inserted in the source code. 
	 */
	private String		syncBooleanFlagName = null;
	
	/*
	 * Both CountDownLatch and ReentrantLock use AbstractQueuedSynchronizer in their
	 * underlying implementation, therefore they provide identical synchronization 
	 * and visibility semantics. So, we will use a lock, because using it globally is
	 * more intuitive, and also a CountDownLatch can be released only once.  
	 */
	private String 		syncLockName = null;
	
	/*
	 * 1- Cases where the default expression for a declaration is after the declaration line, for example:
	 * int[] i;
	 * 
	 * i = new int[num]
	 * must be investigated. 
	 * 
	 * 
	 * 2- receiving the total result of a taskIdGroup when a reduction is specified must be supported as well. 
	 * Current best solution is that the programmer declares the name of the indicator via an attribute of future
	 * (e.g., String totalGroupResult() default ""; --> and the code replaces the declaration of that code with 
	 * taskIDGroup.getReturnResult();
	 * For this case, if the default expression is provided at a later point, it must be found and removed. 
	 */
	public FieldFutureGroupProcessor(Factory factory, Future future, CtField<?> annotatedElement){
		super(factory, future);
		thisAnnotatedField = annotatedElement;
		thisElementType = thisAnnotatedField.getType();
		thisElementName = thisAnnotatedField.getSimpleName();
		thisTaskIDGroupName = APTUtils.getTaskIDGroupName(thisElementName);
		syncLockName = thisTaskIDGroupName + "Lock";
		syncBooleanFlagName = thisTaskIDGroupName + "Synchronized";
		thisGroupSizeName = APTUtils.getTaskIDGroupSizeSyntax(thisElementName);
		elasticTaskGroup = true;
		parentClass = thisAnnotatedField.getParent(CtClass.class);
	}
	
	@Override
	protected void inspectArrayDeclaration(){
		fieldModifiers = thisAnnotatedField.getModifiers();
		getInstantiationExpression(thisAnnotatedField);
		inspectArrayDeclaration(thisGroupDeclarationExpression.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<CtStatement> findVarAccessStatements(){
		List<CtStatement> accessStatements = new ArrayList<>();
		Set<CtConstructor<?>> constructors = parentClass.getConstructors();
		Set<CtMethod<?>> methods = parentClass.getAllMethods();
			
		for(CtConstructor<?> constructor : constructors){
			List<CtStatement> containingStatements = APTUtils.findVarAccessOtherThanFutureDefinition(constructor.getBody(), thisAnnotatedField);
			accessStatements.addAll(containingStatements);
		}
		
		for(CtMethod<?> method : methods){
			List<CtStatement> containingStatements = APTUtils.findVarAccessOtherThanFutureDefinition(method.getBody(), thisAnnotatedField);
			accessStatements.addAll(containingStatements);
		}
		return accessStatements;
	}
	
	@Override
	protected void insertNewStatements(CtLocalVariable<?> taskIDGroupDeclarationStatement, List<CtStatement> reductionStatements){
		insertNewFields();
		insertWaitForMethod();
		insertField(taskIDGroupDeclarationStatement);
		if (reductionStatements != null)
			insertReductionStatements(reductionStatements);
	}
	
	
	@Override
	protected CtVariable<?> getCurrentAnnotatedElement(){
		return thisAnnotatedField;
	}
	
	/*
	 * Modifies the statements in which this future array is assigned a future variables. This future
	 * variable can be either declared before the declaration of this future array, or after that. 
	 * Both cases are considered when processing. In another case, an invocation can be assigned to an
	 * element of this future array, which will get a customized declaration by the compiler. 
	 * Moreover, once it encounters the first statement in which the value for an element of the future
	 * array is accessed, the compiler inserts the barrier phrase for waiting for all task of the 
	 * future array, until they are processed and finished.   
	 * 
	 * This case needs to be discussed within the group. Whether a method should be nominated by the user
	 * in which the wait statement is entered, and array access statements in other methods are assumed to 
	 * take place after the nominated method is called, therefore, wait statement is not entered there.
	 */
	@Override
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
									insertAssignmentBlock(assignment);
								}
								//otherwise, future group is not the main array
								else{									
									inspectReferencedElement(assignment);
								}
							}
						}
						//Otherwise, if the future group appears in the assignment side as well,
						//the future group is referenced.
						else{
							inspectReferencedElement(currentStatement);
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
								inspectReferencedElement(currentStatement);
								break;
							}
						}
					}
				}				
			}
		}catch(Exception e){
			System.out.println("\nEXCEPTION WHILE ATTEMPTING TO MODIFY: " + currentStatement.toString() + " IN FUTURE-IDGROUP PROCESSOR\n");
			e.printStackTrace();
		}
	}	
	
	private void inspectReferencedElement(CtStatement statement){
		CtMethod<?> parentMethod = statement.getParent(CtMethod.class);
		if(synchronizedMethods.contains(parentMethod))
			return;
		
		if(isReductionMethod(parentMethod))
			insertReturnResultStatement(parentMethod);
		else 
			insertWaitStatement(statement);
	}
	
	private void insertAssignmentBlock(CtAssignment<?, ?> assignment){
		CtMethod<?> parentMethod = assignment.getParent(CtMethod.class);
		if(synchronizedMethods.contains(parentMethod))
			return;
				
		CtIf ifStatement = createIfStatement(assignment);
		assignment.replace(ifStatement);
		CtBlock<?> ifBlock = (CtBlock<?>) ifStatement.getThenStatement();
		CtAssignment<?, ?> newAssignment = (CtAssignment<?, ?>) ifBlock.getStatements().get(0);
		modifyAssignmentStatement(newAssignment);
	}
	
	private CtIf createIfStatement(CtAssignment<?, ?> assignment){

		List<CtStatement> statementList = null;
		CtIf ifStatement = thisFactory.Core().createIf();

		//we don't want the elseBody to be affected by code modification, since both elseBody and
		//ifBody refer to the same assignment statement. So, we trick the compiler, we create a 
		//code snippet element that is not recognized as an assignment, and just has the syntax!
		CtCodeSnippetStatement elseBody = thisFactory.Core().createCodeSnippetStatement();
		elseBody.setValue(assignment.toString());

		CtAssignment<?, ?> ifBody = thisFactory.Core().createAssignment();
		ifBody.setAssigned((CtExpression) assignment.getAssigned());
		ifBody.setAssignment((CtExpression) assignment.getAssignment());
			
		CtUnaryOperator<Boolean> ifCondition = thisFactory.Core().createUnaryOperator();
		ifCondition.setKind(UnaryOperatorKind.NOT);
		
		CtCodeSnippetExpression operand = thisFactory.Core().createCodeSnippetExpression();
		operand.setValue(syncBooleanFlagName);
		ifCondition.setOperand(operand);

		CtBlock ifBlock = thisFactory.Core().createBlock();
		statementList = new ArrayList<>();
		statementList.add(ifBody);
		ifBlock.setStatements(statementList);
		
		CtBlock elseBlock = thisFactory.Core().createBlock();
		statementList = new ArrayList<>();
		statementList.add(elseBody);
		elseBlock.setStatements(statementList);
		
		ifStatement.setCondition(ifCondition);
		ifStatement.setThenStatement(ifBlock);
		ifStatement.setElseStatement(elseBlock);		
	
		return ifStatement;
	}
	
	@Override
	protected List<CtStatement> getWaitForTaskGroupStatements(){
		List<CtStatement> statementToAdd = new ArrayList<>();
		CtIf mainIfStatement = thisFactory.Core().createIf();
		CtIf innerIfStatement = thisFactory.Core().createIf();
		
		CtUnaryOperator<Boolean> ifCondition = thisFactory.Core().createUnaryOperator();
		CtCodeSnippetExpression ifOperand = thisFactory.Core().createCodeSnippetExpression();
		ifOperand.setValue(syncBooleanFlagName);
		ifCondition.setKind(UnaryOperatorKind.NOT);
		ifCondition.setOperand(ifOperand);
		
		CtInvocation<?> acquireLock = thisFactory.Core().createInvocation();
		CtExecutableReference acquireLockExecutable = thisFactory.Core().createExecutableReference();
		acquireLockExecutable.setSimpleName("lock");
		CtCodeSnippetExpression<?> acquireLockTarget = thisFactory.Core().createCodeSnippetExpression();
		acquireLockTarget.setValue(syncLockName);
		
		acquireLock.setTarget(acquireLockTarget);
		acquireLock.setExecutable(acquireLockExecutable);
		
		CtInvocation<?> releaseLock = thisFactory.Core().createInvocation();
		CtExecutableReference releaseLockExecutable = thisFactory.Core().createExecutableReference();
		releaseLockExecutable.setSimpleName("unlock");
		CtCodeSnippetExpression<?> releaseLockTarget = thisFactory.Core().createCodeSnippetExpression();
		releaseLockTarget.setValue(syncLockName);
		
		releaseLock.setTarget(releaseLockTarget);
		releaseLock.setExecutable(releaseLockExecutable);
		
		CtBlock<?> mainThenBlock = thisFactory.Core().createBlock();
		CtBlock<?> innerThenBlock = thisFactory.Core().createBlock();
		
		CtAssignment<?, ?> setSyncFlagToTrue = thisFactory.Core().createAssignment();
		CtCodeSnippetExpression assigned = thisFactory.Core().createCodeSnippetExpression();
		assigned.setValue(syncBooleanFlagName);
		CtCodeSnippetExpression assignment = thisFactory.Core().createCodeSnippetExpression();
		assignment.setValue("true");
		setSyncFlagToTrue.setAssigned(assigned);
		setSyncFlagToTrue.setAssignment(assignment);
		
		List<CtStatement> innerThenStatements = new ArrayList<>();
		
		innerThenStatements.addAll(getTryAndForBlocks());
		innerThenStatements.add(setSyncFlagToTrue);
		
		innerThenBlock.setStatements(innerThenStatements);
		innerIfStatement.setCondition(ifCondition);
		innerIfStatement.setThenStatement(innerThenBlock);
		
		List<CtStatement> mainThenStatements = new ArrayList<>();
		mainThenStatements.add(acquireLock);
		mainThenStatements.add(innerIfStatement);
		mainThenStatements.add(releaseLock);		
		mainThenBlock.setStatements(mainThenStatements);
		
		mainIfStatement.setCondition(ifCondition);
		mainIfStatement.setThenStatement(mainThenBlock);
		
		statementToAdd.add(mainIfStatement);
		return statementToAdd;
	}
	
	@Override
	protected void insertTaskIDSizeDeclaration(CtLocalVariable<?> taskGroupSizeVarDeclaration){
		CtField taskIDGroupSizeField = thisFactory.Core().createField();
		taskIDGroupSizeField.setSimpleName(taskGroupSizeVarDeclaration.getSimpleName());
		taskIDGroupSizeField.setType(taskGroupSizeVarDeclaration.getType());
		taskIDGroupSizeField.setDefaultExpression(taskGroupSizeVarDeclaration.getDefaultExpression());
		parentClass.addFieldAtTop(taskIDGroupSizeField);
	}
	
	@Override
	protected CtBlock<?> getParentBlockForWaitStatement(CtStatement containingStatement){
		//it has to be the method, in case the statement is found within a for loop,
		//we don't want the wait block to be inserted in a for loop, but before the for loop!
		CtMethod<?> parentMethod = containingStatement.getParent(CtMethod.class);
		return parentMethod.getBody();
	}
	
	/*
	 * in the generated code, the synchronization filed indicates if a future group
	 * has already been synchronized.
	 */
	private void insertNewFields(){
		CtField<?> syncBooleanFlag = thisFactory.Core().createField();
		CtTypeReference syncBooleanFlagType = thisFactory.Core().createTypeReference();
		syncBooleanFlagType.setSimpleName("boolean");

		CtCodeSnippetExpression booleanFlagDefaultExpression = thisFactory.Core().createCodeSnippetExpression();
		booleanFlagDefaultExpression.setValue("false");

		
		CtField<?> syncLock = thisFactory.Core().createField();
		CtTypeReference syncLockType = thisFactory.Core().createTypeReference();
		syncLockType.setSimpleName(APTUtils.getLockQualifiedSyntax());
		
		CtCodeSnippetExpression syncLockDefaultExpression = thisFactory.Core().createCodeSnippetExpression();
		syncLockDefaultExpression.setValue("new " + APTUtils.getReentrantLockQualifiedSyntax() + "()");
		
		syncBooleanFlag.setSimpleName(syncBooleanFlagName);
		syncBooleanFlag.setType(syncBooleanFlagType);
		syncBooleanFlag.setDefaultExpression(booleanFlagDefaultExpression);
		
		syncLock.setSimpleName(syncLockName);
		syncLock.setType(syncLockType);
		syncLock.setDefaultExpression(syncLockDefaultExpression);
		
		
		Set<ModifierKind> modifiers = new HashSet<>();
		modifiers.addAll(fieldModifiers);
		if(!modifiers.contains(ModifierKind.VOLATILE)){
			modifiers.add(ModifierKind.VOLATILE);
		}


		syncBooleanFlag.setModifiers(modifiers);
		syncLock.setModifiers(modifiers);		
			
		parentClass.addFieldAtTop(syncLock);
		parentClass.addFieldAtTop(syncBooleanFlag);
	}
	
	private void insertWaitForMethod(){
		
	}
	
	private void insertField(CtLocalVariable<?> taskIDGroupDeclarationStatement){
		CtField taskIDGroupField = thisFactory.Core().createField();
		taskIDGroupField.setType(taskIDGroupDeclarationStatement.getType());
		taskIDGroupField.setSimpleName(taskIDGroupDeclarationStatement.getSimpleName());
		taskIDGroupField.setDefaultExpression(taskIDGroupDeclarationStatement.getDefaultExpression());
		taskIDGroupField.setModifiers(fieldModifiers);

		parentClass.addFieldAtTop(taskIDGroupField);
	}
	
	/*
	 * Adds a static anonymous block to the class, in which the reduction statement is created
	 * and assigned to the corresponding TaskIDGroup object. 
	 */
	private void insertReductionStatements(List<CtStatement> reductionStatemetnts){
		Set<CtConstructor> constructors = parentClass.getConstructors();
		
		Set<ModifierKind> anonymousBlockModifiers = new HashSet<>();
		if(fieldModifiers.contains(ModifierKind.STATIC)){
			anonymousBlockModifiers.add(ModifierKind.STATIC);
		}
		
		CtAnonymousExecutable newAnonymousBlock = thisFactory.Core().createAnonymousExecutable();
		CtBlock newAnonymousBlockBody = thisFactory.Core().createBlock();
		newAnonymousBlockBody.setStatements(reductionStatemetnts);
		newAnonymousBlock.setBody(newAnonymousBlockBody);
		newAnonymousBlock.setModifiers(anonymousBlockModifiers);
		parentClass.addAnonymousExecutable(newAnonymousBlock);
	}
	
	private boolean isReductionMethod(CtMethod<?> method){
		List<CtAnnotation<?>> methodAnnotations = method.getAnnotations();
		for(CtAnnotation<? extends Annotation> annotation : methodAnnotations){
			Annotation actualAnnotation = annotation.getActualAnnotation();
			if(actualAnnotation instanceof ReductionMethod)
				return true;
		}
		return false;
	}
	
	private void insertReturnResultStatement(CtMethod<?> method){
		CtReturn<?> returnStatement = thisFactory.Core().createReturn();
		CtCodeSnippetExpression returnedExpression = thisFactory.Core().createCodeSnippetExpression();
		returnedExpression.setValue(thisTaskIDGroupName + APTUtils.getResultSyntax());
		returnStatement.setReturnedExpression(returnedExpression);
		
		List<CtStatement> methodStatements = new ArrayList<>();
		methodStatements.add(returnStatement);
		
		CtBlock methodBlock = thisFactory.Core().createBlock();
		methodBlock.setStatements(methodStatements);
		method.setBody(methodBlock);
		synchronizedMethods.add(method);
	}
}

