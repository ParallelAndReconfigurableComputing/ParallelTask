package pt.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.annotations.Future;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtVariable;

public class OldFutureProcessor extends AbstractAnnotationProcessor<Future, CtVariable<?>>{

	Future thisFuture = null;
	CtVariable<?> thisAnnotatedElement = null;
	String thisElementName = null;
	String thisTaskIDName = null;
	String thisElementReturnType = null;
	List<String> dependencies = null;
	List<String> notifiers = null;
	Map<Class<? extends Throwable>, String> exceptions = null;
	StringBuilder stringBuilder = null;
	
	@Override
	public void process(Future future, CtVariable<?> annotatedElement) {
		dependencies = new ArrayList<>();
		notifiers = new ArrayList<>();
		exceptions = new HashMap<>();
		stringBuilder = new StringBuilder();
		thisFuture = future;
		thisAnnotatedElement = annotatedElement;
		thisElementName = annotatedElement.getSimpleName();
		thisElementReturnType = annotatedElement.getType().toString();
		thisTaskIDName = SpoonUtils.getTaskIDName(thisElementName);
	}

	public void processInvocation(){
		CtExpression<?> defaultExpression = thisAnnotatedElement.getDefaultExpression();
		if (!(defaultExpression instanceof CtInvocation<?>)){
			throw new RuntimeException("The variable declaration must contain one and only one method invocation!");
		}
		CtInvocation<?> invocation = (CtInvocation<?>) defaultExpression;
		//invocation.
	}
	
	public void processDependencies(){
		
	}
	
	public void processNotifications(){
		
	}
	
	//public void process
}
