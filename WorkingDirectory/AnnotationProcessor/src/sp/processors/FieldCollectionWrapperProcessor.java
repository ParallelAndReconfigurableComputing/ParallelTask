package sp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sp.annotations.Future;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

public class FieldCollectionWrapperProcessor extends CollectionWrapperProcessor {
	private Set<ModifierKind> fieldModifiers = null;
	private CtClass parentClass = null;

	public FieldCollectionWrapperProcessor(Factory factory, Future future, CtField<?> annotatedElement) {
		super(factory, future);
		thisAnnotatedField = annotatedElement;
		thisElementName = thisAnnotatedField.getSimpleName();
		thisElementType = thisAnnotatedField.getType();
		fieldModifiers = thisAnnotatedField.getModifiers();
		parentClass = thisAnnotatedField.getParent(CtClass.class);
	}	
	
	@Override
	protected String getDefaultExpression(){
		return thisAnnotatedField.getDefaultExpression().toString();
	}
	
	@Override
	protected String getElementString(){
		return thisAnnotatedField.toString();
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
	protected void modifyCollectionDeclaration(){
		changeDeclarationName(thisAnnotatedField);
		addWrapperDeclaration();
		
		CtAnonymousExecutable newAnonymousBlock = thisFactory.Core().createAnonymousExecutable();
		CtBlock anonymousBlockBody = thisFactory.Core().createBlock();
		
		List<CtStatement> statements = new ArrayList<>();
		CtAssignment<?, ?> newAssignmentStatement = createNewAssignmentStatement();
		statements.add(newAssignmentStatement);
		
		anonymousBlockBody.setStatements(statements);
		insertStatementBeforeDeclaration(newAssignmentStatement);
		insertStatementAfterDeclaration(newAssignmentStatement);
		
		newAnonymousBlock.setBody(anonymousBlockBody);
		parentClass.addAnonymousExecutable(newAnonymousBlock);
	}
	
	private CtAssignment<?, ?> createNewAssignmentStatement(){
		
		String newFieldName =  thisAnnotatedField.getSimpleName();
		CtExpression defaultExpression = thisAnnotatedField.getDefaultExpression();
		thisAnnotatedField.setDefaultExpression(null);
		
		CtCodeSnippetExpression assigned = thisFactory.Core().createCodeSnippetExpression();
		assigned.setValue(newFieldName);
		
		CtAssignment<?, ?> newAssignment = thisFactory.Core().createAssignment();
		newAssignment.setAssigned(assigned);
		newAssignment.setAssignment(defaultExpression);
		return newAssignment;
	}
	
	private void addWrapperDeclaration(){
		CtField<?> newWrapper = thisFactory.Core().createField();
		CtTypeReference wrapperType = thisFactory.Core().createTypeReference();
		String newTypeString = getCollectionType() + "<" + thisCollectionGenericType + ">";
		
		wrapperType.setSimpleName(newTypeString);
		
		newWrapper.setSimpleName(thisElementName);
		newWrapper.setType(wrapperType);
		newWrapper.setDefaultExpression(null);
		newWrapper.setModifiers(fieldModifiers);
		parentClass.addFieldAtTop(newWrapper);
	}
	
	protected void insertStatementAfterDeclaration(CtStatement statement){
		String fieldTaskName = thisAnnotatedField.getSimpleName();
		String newTypeString = getCollectionType() + "<" + thisCollectionGenericType + ">";
		String castingExpression = "(" + newTypeString + ") " + fieldTaskName;
		
		CtCodeSnippetExpression assigned = thisFactory.Core().createCodeSnippetExpression();
		assigned.setValue(thisElementName);
		
		CtCodeSnippetExpression assignment = thisFactory.Core().createCodeSnippetExpression();
		assignment.setValue(castingExpression);
		
		CtAssignment<?, ?> typeCastingStatement = thisFactory.Core().createAssignment();
		typeCastingStatement.setAssigned(assigned);
		typeCastingStatement.setAssignment(assignment);
		
		statement.insertAfter(typeCastingStatement);
	}
}
