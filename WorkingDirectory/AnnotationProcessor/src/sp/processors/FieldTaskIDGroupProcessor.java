package sp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sp.annotations.Future;
import sp.processors.APTUtils.ExpressionRole;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.support.reflect.code.CtAssignmentImpl;

public class FieldTaskIDGroupProcessor extends TaskIDGroupProcessor {
	private Set<ModifierKind> fieldModifiers = null;
	private CtMethod<?>   arrayAccessMethod = null;
		
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
	public FieldTaskIDGroupProcessor(Factory factory, Future future, CtField<?> annotatedElement){
		super(factory, future);
		thisAnnotatedField = annotatedElement;
		thisElementType = thisAnnotatedField.getType();
		thisElementName = thisAnnotatedField.getSimpleName();
		thisTaskIDGroupName = APTUtils.getTaskIDGroupName(thisElementName);
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
		insertField(taskIDGroupDeclarationStatement);
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
				for(int index = 0; index < node.numberOfExpressions(); index++){
					CtExpression<?> expression = node.getExpression(index);
					ExpressionRole expressionRole = node.getExpressionRole(index);
					if(containsSyntaxOfAnArrayElement(expression.toString())){
						//Check if array element is on the left side of an assignment expression
						if(expressionRole.equals(ExpressionRole.Assigned)){ 
							//if yes, then change the expression into adding a taskID to a taskGroup if 
							//the array element is only on the left side of the assignment (i.e., a[] = ... )
							CtAssignmentImpl<?, ?> assignmentStmt = (CtAssignmentImpl<?, ?>) currentStatement;
							CtExpression<?> assignmentExp = assignmentStmt.getAssignment();
							if(!containsSyntaxOfAnArrayElement(assignmentExp.toString())){
								modifyAssignmentStatement(assignmentStmt);
								break;
							}
							else{
								//otherwise an array element has been referred to in this statement, so insert the wait block 
								if(!waitStatementEntered){
									insertWaitForTaskGroupBlock(currentStatement);
									arrayAccessMethod = currentStatement.getParent(CtMethod.class);
									waitStatementEntered = true;
								}else{
									CtMethod<?> method = currentStatement.getParent(CtMethod.class);
									if(!arrayAccessMethod.equals(method))
										throw new Exception("Wait statement for the task group is already entered. \n"
												+ "Currently, global task group elements can only be access in one method");
								}								
							}							
						}
						else{
							//if the expression is not an assignment expression, then array element is definitely
							//referred to, so insert the wait block.
							if(!waitStatementEntered){
								insertWaitForTaskGroupBlock(currentStatement);
								arrayAccessMethod = currentStatement.getParent(CtMethod.class);
								waitStatementEntered = true;
							}else{
								CtMethod<?> method = currentStatement.getParent(CtMethod.class);
								if(!arrayAccessMethod.equals(method))
									throw new Exception("Wait statement for the task group is already entered. \n"
											+ "Currently, global task group elements can only be access in one method");
							}						
						}
					}
				}
			}
		}catch(Exception e){
			System.out.println("\nEXCEPTION WHILE ATTEMPTING TO MODIFY: " + currentStatement.toString() + " IN TASKIDGROUP PROCESSOR\n");
			e.printStackTrace();
		}
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
		CtMethod<?> parentMethod = containingStatement.getParent(CtMethod.class);
		return parentMethod.getBody();
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
				
		CtAnonymousExecutable newAnonymousBlock = thisFactory.Core().createAnonymousExecutable();
		CtBlock newAnonymousBlockBody = thisFactory.Core().createBlock();
		newAnonymousBlockBody.setStatements(reductionStatemetnts);
		newAnonymousBlock.setBody(newAnonymousBlockBody);
		newAnonymousBlock.setModifiers(fieldModifiers);
		parentClass.addAnonymousExecutable(newAnonymousBlock);
	}
}

