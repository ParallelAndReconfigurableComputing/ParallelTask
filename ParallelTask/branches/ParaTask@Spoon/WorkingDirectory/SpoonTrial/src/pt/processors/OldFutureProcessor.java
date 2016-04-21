package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.annotations.AnnotationProcessingFilter;
import pt.annotations.Future;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.reference.CtExecutableReferenceImpl;

public class OldFutureProcessor extends AbstractAnnotationProcessor<Future, CtVariable<?>>{

	Future thisFuture = null;
	CtVariable<?> thisAnnotatedElement = null;
	String thisElementName = null;
	String thisTaskIDName = null;
	String thisArgName = null;
	String thisElementReturnType = null;
	Set<String> dependencies = null;
	Set<String> notifiers = null;
	Map<String, String> argumentsAndTypes = null;
	Map<Class<? extends Throwable>, String> exceptions = null;
	StringBuilder stringBuilder = null;
	boolean throwsExceptions = false;
	
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
		thisArgName = SpoonUtils.getLambdaArgName(thisElementName);
		argumentsAndTypes = new HashMap<>();
				
		processInvocation();
		processDependencies();
		processNotifications();
		processInvocationArguments(thisAnnotatedElement.getDefaultExpression());
		modifyThisStatement();
		addNewStatements();
	}

	public void processInvocation(){
		
		CtExpression<?> defaultExpression = thisAnnotatedElement.getDefaultExpression();
		if (!(defaultExpression instanceof CtInvocation<?>)){
			throw new RuntimeException("The variable declaration must contain "
					+ "one and only one method invocation!");
		}
		
		CtInvocation<?> invocation = (CtInvocation<?>) defaultExpression;
		Set<CtTypeReference<? extends Throwable>> exceptions = null;
		exceptions = invocation.getExecutable().getDeclaration().getThrownTypes();
		if (!exceptions.isEmpty())
			throwsExceptions = true;
		
		String regex = "\\b" + thisElementName + "\\b";
		List<CtStatement> statements = SpoonUtils.findVarAccessOtherThanFutureDefinition
				((CtBlock<?>)thisAnnotatedElement.getParent(), thisAnnotatedElement);
		SpoonUtils.modifyStatements(statements, regex, (thisTaskIDName+".getReturnResult()"));
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
					for (String depends : dependsArray){
						//if not trimmed, duplicates with invisible white-space
						//get added
						depends = depends.trim(); 
						
						//in case user feeds taskID name etc. Small possibility but...
						depends = SpoonUtils.getOrigName(depends); 
						if(SpoonUtils.isFutureArgument(thisAnnotatedElement, depends))
							dependencies.add(SpoonUtils.getTaskIDName(depends));
					}
					/*if more than one future annotation is assigned to a variable
					 * (theoretically shouldn't be allowed), we only accept the first!*/
					break;
				}
			}
		}
	}
	
	public void processNotifications(){
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
						notifiers.add(notifySlot);
					}
				}
				//only the first future annotation
				break;
			}
		}
	}
	
	public void processInvocationArguments(CtExpression<?> expression){
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
		CtTypeReference<?> taskIDType = getFactory().Core().createTypeReference();
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
			
//			CtMethod<?> parentMethod = thisAnnotatedElement.getParent(CtMethod.class);
//			Set<CtTypeReference<? extends Throwable>> thrownTypes = new HashSet<>();
//			//this is not a safe approach, as if the method is called from another class, adding throwable
//			//to the method can cause error in the subsequent classes that call the method. instead add a
//			//catch statement!
//			CtTypeReference<? extends Throwable> exceptionType = getFactory().Core().createTypeReference();
//			exceptionType.setSimpleName("Exception");
//			thrownTypes.add(exceptionType);
//			parentMethod.setThrownTypes(thrownTypes);
		}
		
		String newArgumentPhrase = "\n\t\t\t" + functorTypeCast + getLambdaArgs() + " -> " + invocationPhrase;
		return newArgumentPhrase;
	}
	
	public void modifyThisStatement(){
		CtInvocation<?> invocation = (CtInvocation<?>) thisAnnotatedElement.getDefaultExpression();
		CtTypeReference thisElementNewType = getFactory().Core().createTypeReference();
		thisElementNewType.setSimpleName(getLambdaName());
		thisAnnotatedElement.setType(thisElementNewType);
		thisAnnotatedElement.setSimpleName(SpoonUtils.getTaskName(thisElementName));
				
				
		CtCodeSnippetExpression<?> newArgument = getFactory().Core().createCodeSnippetExpression();
			
		newArgument.setValue(newLambdaPhrase(invocation));
		CtExpression<?> newArgExp = newArgument;
				
		List<CtExpression<?>> arguments = new ArrayList<>();
		arguments.add(newArgExp);
		invocation.setArguments(arguments);
				
		List<CtTypeReference<?>> typeCasts = new ArrayList<>();
		typeCasts.add(thisElementNewType);
		invocation.setTypeCasts(typeCasts);
		
		CtExecutableReference executable = getFactory().Core().createExecutableReference();
		executable.setSimpleName("ParaTask.asTask");
		invocation.setExecutable(executable);
	}
	
	public void addNewStatements(){
		CtBlock<?> thisBlock = (CtBlock<?>) thisAnnotatedElement.getParent();
		AnnotationProcessingFilter<CtStatement> filter = new AnnotationProcessingFilter<CtStatement>
								(SpoonUtils.getDeclarationStatement(thisAnnotatedElement, thisElementName));
		//thisBlock.insertAfter(filter, newStatements());
		thisBlock.insertAfter(filter, newStatements());
	}
	
	public CtStatementList<?> newStatements(){
		CtStatementList<?> statements = getFactory().Core().createStatementList();
		List<CtStatement> sts = new ArrayList<>();
		
		CtInvocationImpl<?> dependsOnStatement = getDependsOnStatement();
		if (dependsOnStatement != null)
			sts.add(dependsOnStatement);
			
		/*for the cases where notification handlers use TaskID, ParaTask must check if the task is 
		 * already finished when registering the handler. Sometimes task finished before the 
		 * notifier is registered!*/
		List<CtInvocationImpl<?>> notifiers = getNotifyStatements();
		if (notifiers != null){
			sts.addAll(notifiers);
		}
		
		sts.add(getStartStatement());	
		
		statements.setStatements(sts);
		return statements;
	}
	
	public CtInvocationImpl<?> getDependsOnStatement(){
		/*create the dependsOn statement*/
		if(dependencies.isEmpty())
			return null;
		
		CtInvocationImpl<?> dependsOnInvoc = (CtInvocationImpl<?>) getFactory().Core().createInvocation();
		//dependsOnInvoc.s
		String dependsOnExecutable = SpoonUtils.getTaskName(thisElementName) + ".dependsOn";
		String dependsOnArguments = "";
		int counter = 0;
		for (String dependency : dependencies){
			dependsOnArguments += dependency;
			counter++;
			if (counter < dependencies.size())
				dependsOnArguments += ", ";
		}
		
		
		CtExecutableReference dependsOnExec = getFactory().Core().createExecutableReference();
		dependsOnExec.setSimpleName(dependsOnExecutable);
		dependsOnInvoc.setExecutable(dependsOnExec);
		
		List<CtExpression<?>> dependsOnArgs = new ArrayList<>();
		CtCodeSnippetExpression argsExp = getFactory().Core().createCodeSnippetExpression();
		argsExp.setValue(dependsOnArguments);
		dependsOnArgs.add(argsExp);
		dependsOnInvoc.setArguments(dependsOnArgs);
		
		return dependsOnInvoc;
	}
	
	public CtLocalVariableImpl<?> getStartStatement(){
		/*create the start statement*/
		CtLocalVariableImpl<?> localVar = (CtLocalVariableImpl<?>) getFactory().Core().createLocalVariable();
		String startPhrase = ".start(";
		
		Set<String> argTypes = argumentsAndTypes.keySet();	
		int counter = 0;
		for(String argType : argTypes){
			
			String arg = argumentsAndTypes.get(argType);
			
			if (SpoonUtils.isNonLambdaArg(arg))				
				startPhrase += SpoonUtils.getOrigName(arg);
			
			else if (SpoonUtils.isLambdaArg(arg))
				startPhrase += SpoonUtils.getTaskIDName(SpoonUtils.getOrigName(arg));
			 
			counter++;
			if(counter != argTypes.size())
				startPhrase += ", ";
		}
		startPhrase += ")";
		startPhrase = SpoonUtils.getTaskName(thisElementName) + startPhrase;
				
		CtCodeSnippetExpression defaultExp = getFactory().Core().createCodeSnippetExpression();
		defaultExp.setValue(startPhrase);
		
		CtTypeReference taskIDType = getTaskIDType(thisAnnotatedElement);
		localVar.setType(taskIDType);
		localVar.setSimpleName(SpoonUtils.getTaskIDName(thisElementName));
		localVar.setDefaultExpression(defaultExp);
		
		return localVar;
	}
	
	public List<CtInvocationImpl<?>> getNotifyStatements(){
		if (notifiers.isEmpty())
			return null;
		
		List<CtInvocationImpl<?>> notifyStatements = new ArrayList<>();
		
		CtInvocationImpl<?> notifyStatement = (CtInvocationImpl<?>) getFactory().Core().createInvocation();
		CtExecutableReferenceImpl executablePhrase = (CtExecutableReferenceImpl<?>) getFactory().Core().createExecutableReference();
		executablePhrase.setSimpleName("ParaTask.registerSlotToNotify");
		notifyStatement.setExecutable(executablePhrase);
		
				
		for (String notifier : notifiers){
			String execArgument = SpoonUtils.getTaskName(thisElementName) + ", " + notifier;
			CtCodeSnippetExpression<?> notifierExp = getFactory().Core().createCodeSnippetExpression();
			notifierExp.setValue(execArgument);
			List<CtExpression<?>> notifyStatemtnArgument = new ArrayList<>();
			notifyStatemtnArgument.add(notifierExp);
			notifyStatement.setArguments(notifyStatemtnArgument);
			notifyStatements.add(notifyStatement);
		}
		
		return notifyStatements;
	}
}
