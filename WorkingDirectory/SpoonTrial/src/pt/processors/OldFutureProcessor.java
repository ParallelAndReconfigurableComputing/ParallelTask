package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.annotations.Future;
import pt.runtime.ParaTask;
import pt.runtime.TaskID;
import pt.runtime.TaskInfo;
import pt.runtime.TaskInfoThreeArgs;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtCodeSnippetExpressionImpl;

public class OldFutureProcessor extends AbstractAnnotationProcessor<Future, CtVariable<?>>{

	Future thisFuture = null;
	CtVariable<?> thisAnnotatedElement = null;
	String thisElementName = null;
	String thisTaskIDName = null;
	String thisElementReturnType = null;
	Set<String> dependencies = null;
	Set<String> notifiers = null;
	Set<CtExpression<?>> listOfArguments = null;
	Map<String, String> argumentsAndTypes = null;
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
		argumentsAndTypes = new HashMap<>();
		listOfArguments = new HashSet<>();
		
		processInvocation();
		processDependencies();
		processNotifications();
		processNewStatement();
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
		
		for (CtExpression<?> argument : arguments){
			if(SpoonUtils.isTaskIDReplacement(thisAnnotatedElement, argument.toString())){
				String originalArgumentName = SpoonUtils.getOrigName(argument.toString());
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
		System.out.println("element " + thisElementName + " depends on " + dependencies.toString());
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
	
	public void processNewStatement(){
		
		CtInvocation<?> invocation = (CtInvocation<?>) thisAnnotatedElement.getDefaultExpression();
		List<CtExpression<?>> arguments = invocation.getArguments();
		
		for (CtExpression<?> argument : arguments){
			if (!(argument instanceof CtLiteral<?>)){
				String argName = argument.toString();
				if(SpoonUtils.isTaskIDReplacement(thisAnnotatedElement, argName)){
					String origName = SpoonUtils.getOrigName(argName);
					CtVariable<?> declaration = SpoonUtils.getDeclarationStatement(thisAnnotatedElement, origName);
					CtTypeReference taskIDType = getTaskIDType(declaration);
					argument.setType(taskIDType);
					argumentsAndTypes.put(argument.getType().toString(), SpoonUtils.getTaskIDName(origName));
				}
				else{
					argumentsAndTypes.put(SpoonUtils.getType(argument.getType().toString()), argName);
				}
				listOfArguments.add(argument);
			}
		}
		
		System.out.println("map of arguments: " + argumentsAndTypes.toString());
		
		modifyThisStatement();
	}
	
	public String getNumArgs(){
		int numOfArgs = listOfArguments.size();
		String args = null;
		
		switch(numOfArgs){
		case 0:
			args = "NoArgs";
			break;
		case 1:
			args = "OneArg";
			break;
		case 2:
			args = "TwoArgs";
			break;
		case 3:
			args = "ThreeArgs";
			break;
		case 4:
			args = "FourArgs";
			break;
		case 5:
			args = "FiveArgs";
			break;
		case 6:
			args = "SixArgs";
			break;
		case 7:
			args = "SevenArgs";
			break;
		case 8:
			args = "EightArgs";
			break;
		case 9:
			args = "NineArgs";
			break;
		case 10:
			args = "TenArgs";
			break;
		case 11:
			args = "ElevenArgs";
			break;
		case 12:
			args = "TwelveArgs";
			break;
		default:
			args = "";	
		}
		return args;
	}

	public String getLambdaName(){
		String taskLambdaName = "TaskInfo";
		taskLambdaName += getNumArgs() + "<" + SpoonUtils.getType(thisElementReturnType)+">";
		return taskLambdaName;
	}

	public String getFunctorName(){
		String functorName = "Functor";
		String returnPhrase = (thisElementReturnType.contains("Void")) ? "NoReturn<Void" : ("WithReturn<"+SpoonUtils.getType(thisElementReturnType));
		functorName = functorName + getNumArgs() + returnPhrase;
		return functorName;
	}
	
	public CtTypeReference<?> getTaskIDType(CtVariable<?> declaration){
		
		String declarationType = declaration.getType().toString();
		System.out.println("declaration type: " + declarationType);
		String taskType = SpoonUtils.getType(declarationType);
		taskType = "TaskID<" + taskType + ">";
		CtTypeReference<?> taskIDType = getFactory().Core().createTypeReference();
		taskIDType.setSimpleName(taskType);
		return taskIDType;
	}

	public void modifyThisStatement(){
		CtInvocation<?> invocation = (CtInvocation<?>) thisAnnotatedElement.getDefaultExpression();
		CtTypeReference newType = getFactory().Core().createTypeReference();
		newType.setSimpleName(getLambdaName());
		thisAnnotatedElement.setType(newType);
		List<CtTypeReference<?>> typeCasts = new ArrayList<>();
		typeCasts.add(newType);
		invocation.setTypeCasts(typeCasts);
		thisAnnotatedElement.setSimpleName(SpoonUtils.getTaskName(thisElementName));
		System.out.println(thisAnnotatedElement.toString());
	}
}
