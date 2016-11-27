package sp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sp.annotations.Future;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtBlock;
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
		
	public FieldTaskIDGroupProcessor(Factory factory, Future future, CtField<?> annotatedElement){
		super(factory, future);
		thisAnnotatedField = annotatedElement;
		thisElementType = thisAnnotatedField.getType();
		thisElementName = thisAnnotatedField.getSimpleName();
		thisTaskIDGroupName = APTUtils.getTaskIDGroupName(thisElementName);
		elasticTaskGroup = true;
	}
	
	@Override
	protected void inspectArrayDeclaration(){
		fieldModifiers = thisAnnotatedField.getModifiers();
		inspectArrayDeclaration(thisAnnotatedField.getDefaultExpression().toString());
	}
	
	@Override
	protected List<CtStatement> findOccurrences(){
		List<CtStatement> occurrences = new ArrayList<>();
		CtClass	parentClass = thisAnnotatedField.getParent(CtClass.class);
		Set<CtConstructor<?>> constructors = parentClass.getConstructors();
		Set<CtMethod<?>> methods = parentClass.getAllMethods();
			
		for(CtConstructor<?> constructor : constructors){
			List<CtStatement> containingStatements = APTUtils.findVarAccessOtherThanFutureDefinition(constructor.getBody(), thisAnnotatedField);
			occurrences.addAll(containingStatements);
		}
		
		for(CtMethod<?> method : methods){
			List<CtStatement> containingStatements = APTUtils.findVarAccessOtherThanFutureDefinition(method.getBody(), thisAnnotatedField);
			occurrences.addAll(containingStatements);
		}
		return occurrences;
	}
	
	@Override
	protected void insertNewStatements(CtLocalVariable<?> taskIDGroupDeclarationStatement, List<CtStatement> reductionStatements){
		insertField(taskIDGroupDeclarationStatement);
		insertReductionStatements(reductionStatements);
	}
	
	private void insertField(CtLocalVariable<?> taskIDGroupDeclarationStatement){
		CtClassImpl<?> parentClass = (CtClassImpl<?>) thisAnnotatedField.getParent(CtClass.class);
		CtField taskIDGroupField = thisFactory.Core().createField();
		taskIDGroupField.setType(taskIDGroupDeclarationStatement.getType());
		taskIDGroupField.setSimpleName(taskIDGroupDeclarationStatement.getSimpleName());
		taskIDGroupField.setDefaultExpression(taskIDGroupDeclarationStatement.getDefaultExpression());
		taskIDGroupField.setModifiers(fieldModifiers);
		parentClass.addField(taskIDGroupField);
	}
	
	private void insertReductionStatements(List<CtStatement> reductionStatemetnts){
		CtClassImpl<?> parentClass = (CtClassImpl<?>) thisAnnotatedField.getParent(CtClass.class);
		CtConstructor constructor = parentClass.getConstructor();
		
		if(constructor == null){
			constructor = thisFactory.Core().createConstructor();
		}
		
		CtStatementList statements = thisFactory.Core().createStatementList();
		statements.setStatements(reductionStatemetnts);
		CtBlock<?> constructorBlock = (CtBlock<?>) constructor.getBody();
		constructorBlock.insertBegin(statements);
		parentClass.addConstructor(constructor);
	}
}

