package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import pt.annotations.AnnotationProcessingFilter;
import pt.annotations.Future;
import spoon.processing.AbstractProcessor;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.Factory;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtStatementImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

public class InvocationProcessor {
	
	private Future thisFuture = null;
	private CtInvocation<?> thisInvocation = null;
	private CtVariable<?> thisAnnotatedElement = null;
	private String thisElementName = null;
	private String thisTaskIDName = null;
	private String thisArgName = null;
	private String thisElementReturnType = null;
	private Set<String> dependencies = null;
	private Set<String> handlers = null;
	private Map<String, String> argumentsAndTypes = null;
	private Map<Class<? extends Throwable>, String> exceptions = null;
	private StringBuilder stringBuilder = null;
	private boolean throwsExceptions = false;
	private Factory thisFactory = null;
	
	public InvocationProcessor(Factory factory, Future future, CtVariable<?> annotatedElement){
		dependencies = new HashSet<>();
		handlers = new HashSet<>();
		exceptions = new HashMap<>();
		stringBuilder = new StringBuilder();
		thisFuture = future;
		thisAnnotatedElement = annotatedElement;
		thisElementName = annotatedElement.getSimpleName();
		thisElementReturnType = annotatedElement.getType().toString();
		thisTaskIDName = SpoonUtils.getTaskIDName(thisElementName);
		thisArgName = SpoonUtils.getLambdaArgName(thisElementName);
		argumentsAndTypes = new HashMap<>();
		thisFactory = factory;
	}
	
	public void process(){
		getInvocations();
		checkIfThrowsException();
		replaceWithTaskIDName();
		extractDependencies();
		extractHandlers();
		processInvocationArguments(thisAnnotatedElement.getDefaultExpression());
		modifyThisStatement();
		addNewStatements();
	}
	
	private void getInvocations(){
		CtExpression<?> invocation = thisAnnotatedElement.getDefaultExpression();
		thisInvocation = (CtInvocationImpl<?>) invocation;
	}
	
	private void checkIfThrowsException(){
		Set<CtTypeReference<? extends Throwable>> exceptions = null;
		exceptions = thisInvocation.getExecutable().getDeclaration().getThrownTypes();
		if(!exceptions.isEmpty())
			throwsExceptions = true;
	}
	
	private void replaceWithTaskIDName(){
		String regex = "\\b" + thisElementName + "\\b";
		List<CtStatement> statements = SpoonUtils.findVarAccessOtherThanFutureDefinition
				((CtBlockImpl<?>)thisAnnotatedElement.getParent(), thisAnnotatedElement);
		SpoonUtils.modifyStatements(statements, regex, (thisTaskIDName+".getReturnResult()"));
	}
	
	private void extractDependencies(){
		List<CtExpression<?>> arguments = thisInvocation.getArguments();
		
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
					for (String depends : dependsArray){
						//if not trimmed, duplicates with invisible white-space
						//get added
						depends = depends.trim(); 
						
						//in case user feeds taskID name etc. Small possibility but...
						depends = SpoonUtils.getOrigName(depends); 
						if(SpoonUtils.isFutureArgument(thisAnnotatedElement, depends))
							dependencies.add(SpoonUtils.getTaskIDName(depends));
					}					
				}
				/*if more than one future annotation is assigned to a variable
				 * (theoretically shouldn't be allowed), we only accept the first!*/
				break;
			}
		}
	}	
	
	private void extractHandlers(){
		Set<CtAnnotation<?>> annotations = thisAnnotatedElement.getAnnotations();
		for(CtAnnotation<?> ctAnno : annotations){
			Annotation annotation = ctAnno.getActualAnnotation();
			if (annotation instanceof Future){
				Future future = (Future) annotation;
				String notifies = future.notifies();
				if(!notifies.isEmpty()){
					String[] notifyArray = notifies.split(";");
					for (String notify : notifyArray){
						notify = notify.trim();
						String notifySlot = "()->" + notify;
						handlers.add(notifySlot);
					}
				}
				//only the first future annotation
				break;
			}
		}
	}
	
	private void processInvocationArguments(CtExpression<?> expression){
		if (!(expression instanceof CtInvocation<?>))
			return;
		CtInvocation<?> invocation = (CtInvocation<?>) expression;
		List<CtExpression<?>> arguments = invocation.getArguments();
		
		for (CtExpression<?> argument : arguments){
			if (argument instanceof CtVariableAccess<?>){
				CtVariableAccess<?> varAccess = (CtVariableAccess<?>) argument;
				String argName = varAccess.toString();
				String origName = SpoonUtils.getOrigName(argName);
				
				if(SpoonUtils.isTaskIDReplacement(thisAnnotatedElement, argName)){
					
					CtLocalVariable<?> declaration = (CtLocalVariable<?>)SpoonUtils.getDeclarationStatement(thisAnnotatedElement, origName);
					CtTypeReference taskIDType = getTaskIDType(declaration);
					
					varAccess.getVariable().setSimpleName(SpoonUtils.getLambdaArgName(origName)+".getReturnResult()");
					varAccess.setType(taskIDType);					
					argumentsAndTypes.put(varAccess.getType().toString(), SpoonUtils.getLambdaArgName(origName));
				}
				else{
					varAccess.getVariable().setSimpleName(SpoonUtils.getNonLambdaArgName(argName));
					argumentsAndTypes.put(SpoonUtils.getType(varAccess.getType().toString()), SpoonUtils.getNonLambdaArgName(argName));
				}				
			}
		}
	}
	
	private void modifyThisStatement(){
		thisInvocation = (CtInvocation<?>) thisAnnotatedElement.getDefaultExpression();
		CtTypeReference thisElementNewType = thisFactory.Core().createTypeReference();
		thisElementNewType.setSimpleName(getLambdaName());
		thisAnnotatedElement.setType(thisElementNewType);
		thisAnnotatedElement.setSimpleName(SpoonUtils.getTaskName(thisElementName));
				
				
		CtCodeSnippetExpression<?> newArgument = thisFactory.Core().createCodeSnippetExpression();
			
		newArgument.setValue(newLambdaPhrase(thisInvocation));
		CtExpression<?> newArgExp = newArgument;
				
		List<CtExpression<?>> arguments = new ArrayList<>();
		arguments.add(newArgExp);
		thisInvocation.setArguments(arguments);
				
		List<CtTypeReference<?>> typeCasts = new ArrayList<>();
		typeCasts.add(thisElementNewType);
		thisInvocation.setTypeCasts(typeCasts);
		
		CtExecutableReference executable = thisFactory.Core().createExecutableReference();
		executable.setSimpleName("ParaTask.asTask");
		thisInvocation.setExecutable(executable);
	}
	
	private void addNewStatements(){
		CtBlock<?> thisBlock = (CtBlock<?>) thisAnnotatedElement.getParent();
		AnnotationProcessingFilter<CtStatement> filter = new AnnotationProcessingFilter<CtStatement>
								(SpoonUtils.getDeclarationStatement(thisAnnotatedElement, thisElementName));
		//thisBlock.insertAfter(filter, newStatements());
		thisBlock.insertAfter(filter, newStatements());
	}
	
	//---------------------------------------HELPER METHODS-----------------------------------------
	
	public String getNumArgs(){
		Set<String> argTypes = argumentsAndTypes.keySet();
		int numOfArgs = argTypes.size();
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
		String returnPhase = (thisElementReturnType.contains("Void")) ? "Void" : SpoonUtils.getType(thisElementReturnType);
		String taskInfo = "TaskInfo" + getNumArgs() + "<" + returnPhase;
		Set<String> argTypes = argumentsAndTypes.keySet();
		for(String argType : argTypes){
			taskInfo += ", " + argType;
		}
		taskInfo += ">";
		return taskInfo;
	}
	
	public String getFunctorName(){
		String returnPhrase = (thisElementReturnType.contains("Void")) ? "NoReturn<Void" : ("WithReturn<"+SpoonUtils.getType(thisElementReturnType));
		String functorName = "Functor" + getNumArgs() + returnPhrase;
		return functorName;
	}
	
	public CtTypeReference getTaskIDType(CtVariable<?> declaration){
		
		String declarationType = declaration.getType().toString();
		declarationType = SpoonUtils.getOrigName(declarationType);
		String taskType = SpoonUtils.getType(declarationType);		
		taskType = "TaskID<" + taskType + ">";
		CtTypeReference<?> taskIDType = thisFactory.Core().createTypeReference();
		taskIDType.setSimpleName(taskType);
		return taskIDType;
	}

	public String getFunctorCast(){
		String functorCast = getFunctorName();
		if(functorCast.contains("FunctorNoArgsNoReturn"))
			return "";
		Set<String> argTypes = argumentsAndTypes.keySet();
		for(String argType : argTypes){
			functorCast += (", " + argType);
		}
		functorCast += ">";
		return functorCast;
	}
	
	public String getLambdaArgs(){
		String lambdaArgs = "(";
		Set<String> argTypes = argumentsAndTypes.keySet();
		int counter = 0;
		for (String argType : argTypes){
			lambdaArgs += argumentsAndTypes.get(argType);
			counter++;
			if(counter < argTypes.size())
				lambdaArgs += ", ";
		}
		lambdaArgs += ")";
		return lambdaArgs;
	}
	
	public String newLambdaPhrase(CtInvocation<?> invocation){
		
		String returnStatement = "";
		String catchReturnStmt = "";
		
		if(!thisElementReturnType.contains("Void")){
			returnStatement = "return ";
			catchReturnStmt = "\t\t\t\t\t\treturn null; \n";
		}
			
		
		String functorTypeCast = getFunctorCast();
		if (!functorTypeCast.isEmpty())
			functorTypeCast = "(" + functorTypeCast + ")";
		
		String invocationPhrase = invocation.toString();
		if(throwsExceptions){
			invocationPhrase =  "{ try\n"+
								" \t\t\t\t {  \n"+
								"\t\t\t\t\t\t" + returnStatement + invocationPhrase + ";\n" +
								" \t\t\t\t }catch(Exception e){\n"+
								"\t\t\t\t\t\te.printStackTrace();\n"+
								catchReturnStmt + 
								"  \t\t\t\t }\n"+
								"\t\t\t}";
		}
		
		String newArgumentPhrase = "\n\t\t\t" + functorTypeCast + getLambdaArgs() + " -> " + invocationPhrase;
		return newArgumentPhrase;
	}
	
	//---------------------------------------HELPER METHODS-----------------------------------------
}
