package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.annotations.Future;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtVariable;

public class OldFutureProcessor extends AbstractAnnotationProcessor<Future, CtVariable<?>>{

	Future thisFuture = null;
	CtVariable<?> thisAnnotatedElement = null;
	String thisElementName = null;
	String thisTaskIDName = null;
	String thisElementReturnType = null;
	Set<String> dependencies = null;
	Set<String> notifiers = null;
	Map<Class<? extends Throwable>, String> exceptions = null;
	StringBuilder stringBuilder = null;
	
	@Override
	public void process(Future future, CtVariable<?> annotatedElement) {
		dependencies = new HashSet<>();
		notifiers = new HashSet<>();
		exceptions = new HashMap<>();
		stringBuilder = new StringBuilder();
		thisFuture = future;
		thisAnnotatedElement = annotatedElement;
		thisElementName = annotatedElement.getSimpleName();
		thisElementReturnType = annotatedElement.getType().toString();
		thisTaskIDName = SpoonUtils.getTaskIDName(thisElementName);
		processInvocation();
		processDependencies();
		processNotifications();
	}

	public void processInvocation(){
		
		CtExpression<?> defaultExpression = thisAnnotatedElement.getDefaultExpression();
		if (!(defaultExpression instanceof CtInvocation<?>)){
			throw new RuntimeException("The variable declaration must contain "
					+ "one and only one method invocation!");
		}
		
		String regex = "\\b" + thisElementName + "\\b";
		List<CtStatement> statements = SpoonUtils.findVarAccessOtherThanFutureDefinition
				((CtBlock<?>)thisAnnotatedElement.getParent(), thisAnnotatedElement);
		SpoonUtils.modifyStatements(statements, regex, (thisTaskIDName+".getResult()"));
	}
	
	public void processDependencies(){
		CtInvocation<?> invocation = (CtInvocation<?>) thisAnnotatedElement.getDefaultExpression();
		List<CtExpression<?>> arguments = invocation.getArguments();
		System.out.println("Arguments for " + thisElementName + " are: " + arguments.toString());
		
		for (CtExpression<?> argument : arguments){
			if(SpoonUtils.isTaskIDReplacement(thisAnnotatedElement, argument.toString())){
				String originalArgumentName = SpoonUtils.getOrigName(argument.toString());
				System.out.println("original name: " + originalArgumentName);
				dependencies.add(SpoonUtils.getTaskIDName(originalArgumentName));
			}
		}
		
		Set<CtAnnotation<?>> annotations = thisAnnotatedElement.getAnnotations();
		for(CtAnnotation<?> ctAnno : annotations){
			Annotation annotation = ctAnno.getActualAnnotation();
			if (annotation instanceof Future){
				Future future = (Future) annotation;
				String dependsOn = future.depends();
				if (!dependsOn.isEmpty()){
					String[] dependsArray = dependsOn.split(",");
					for (String depends : dependsArray)							
							dependencies.add(SpoonUtils.getTaskIDName(depends));
					
					/*if more than one future annotation is assigned to a variable
					 * (theoretically shouldn't be allowed), we only accept the first!*/
					break;
				}
			}
		}
		System.out.println("----------------------------------------------------------------------");
		System.out.println("Variable " + thisElementName + " depends on " + dependencies.toString());
		System.out.println("----------------------------------------------------------------------");
	}
	
	public void processNotifications(){
		Set<CtAnnotation<?>> annotations = thisAnnotatedElement.getAnnotations();
		for(CtAnnotation<?> ctAnno : annotations){
			Annotation annotation = ctAnno.getActualAnnotation();
			if (annotation instanceof Future){
				Future future = (Future) annotation;
				String notifies = future.notifies();
				String[] notifyArray = notifies.split(";");
				for (String notify : notifyArray){
					String notifySlot = "()->" + notify;
					notifiers.add(notifySlot);
				}
			}
		}
	}
	
	//public void process
}
