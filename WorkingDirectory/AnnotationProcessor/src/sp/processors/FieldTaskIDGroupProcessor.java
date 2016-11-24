package sp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sp.annotations.Future;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;

public class FieldTaskIDGroupProcessor extends TaskIDGroupProcessor {
	private CtField<?> thisAnnotatedField = null;
		
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
		System.out.println(occurrences);
		return occurrences;
	}
}

