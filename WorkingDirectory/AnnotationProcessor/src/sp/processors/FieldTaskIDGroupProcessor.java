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
		parentClass = thisAnnotatedField.getParent(CtClass.class);
		System.out.println("field default expression: " + thisAnnotatedField.getDefaultExpression());
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
		System.out.println("occurrences: " + occurrences);
		return occurrences;
	}
	
	@Override
	protected void insertNewStatements(CtLocalVariable<?> taskIDGroupDeclarationStatement, List<CtStatement> reductionStatements){
		insertField(taskIDGroupDeclarationStatement);
		insertReductionStatements(reductionStatements);
	}
	
	@Override
	protected CtBlock<?> getParentBlockForWaitStatement(CtStatement containingStatement){
		CtMethod<?> parentMethod = containingStatement.getParent(CtMethod.class);
		return parentMethod.getBody();
	}
	
	private void insertField(CtLocalVariable<?> taskIDGroupDeclarationStatement){
		CtClassImpl<?> parentClass = (CtClassImpl<?>) thisAnnotatedField.getParent(CtClass.class);
		CtField taskIDGroupField = thisFactory.Core().createField();
		taskIDGroupField.setType(taskIDGroupDeclarationStatement.getType());
		taskIDGroupField.setSimpleName(taskIDGroupDeclarationStatement.getSimpleName());
		taskIDGroupField.setDefaultExpression(taskIDGroupDeclarationStatement.getDefaultExpression());
		taskIDGroupField.setModifiers(fieldModifiers);
		parentClass.addFieldAtTop(taskIDGroupField);
	}
	
	private void insertReductionStatements(List<CtStatement> reductionStatemetnts){
		CtClassImpl parentClass = (CtClassImpl<?>) thisAnnotatedField.getParent(CtClass.class);
		CtConstructor constructor = parentClass.getConstructor();
				
		/*
		 * When a new constructor is created, it does not have a body! Therefore, a new body
		 * (i.e., CtBlock) must be created and assigned to it. Otherwise, there will be no
		 * constructors added to the actual code. 
		 * 
		 * In this case, statements are inserted in the reverse order of the original statement
		 * list, when insertBegin is used for the new block. Therefore, the list is inverted before
		 * insertion.
		 */		 
		if(constructor == null){
			System.out.println("default constrcutor is null");
			constructor = thisFactory.Core().createConstructor();
			CtBlock<?> block = thisFactory.Core().createBlock();
			constructor.setBody(block);
			
			List<CtStatement> constructorStatements = new ArrayList<>();
			int index = reductionStatemetnts.size() - 1;
			for(; index >= 0; index--){
				constructorStatements.add(reductionStatemetnts.get(index));
			}
			
			reductionStatemetnts = constructorStatements;			
		}	
		
		System.out.println("constructor's size: " + parentClass.getConstructors().size());
		CtStatementList statements = thisFactory.Core().createStatementList();
		statements.setStatements(reductionStatemetnts);
		CtBlock<?> constructorBlock = (CtBlock<?>) constructor.getBody();
		constructorBlock.insertBegin(statements);
		
		parentClass.addConstructor(constructor);

//		else{
//			System.out.println("constructors are null");
//			Set<CtConstructor> constructors = new HashSet<>();
//			constructors.add(constructor);
//			parentClass.setConstructors(constructors);
//		}
	}
}

