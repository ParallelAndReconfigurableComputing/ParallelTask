package sp.processors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sp.annotations.Future;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.support.reflect.declaration.CtClassImpl;

public class FieldTaskIDGroupProcessor extends TaskIDGroupProcessor {
	private CtField<?> thisAnnotatedField = null;
	private Set<ModifierKind> fieldModifiers = null;
		
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
				
		CtAnonymousExecutable anonymousBlock = thisFactory.Core().createAnonymousExecutable();
		CtBlock anonymousBody = thisFactory.Core().createBlock();
		anonymousBody.setStatements(reductionStatemetnts);
		anonymousBlock.setBody(anonymousBody);
		parentClass.addAnonymousExecutable(anonymousBlock);
	}
}

