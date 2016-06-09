package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import sp.annotations.AsyncCatch;
import sp.annotations.Future;
import sp.annotations.StatementMatcherFilter;
import spoon.reflect.Factory;
import spoon.reflect.code.CtArrayAccess;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.reference.CtExecutableReferenceImpl;

public class InvocationProcessor extends PtAnnotationProcessor {
	
	private Future thisFutureAnnotation = null;
	private CtInvocation<?> thisInvocation = null;
	private CtLocalVariable<?> thisAnnotatedElement = null;
	private String thisElementName = null;
	private String thisTaskIDName = null;
	private String thisTaskInfoName = null;
	private String thisTaskType = null;
	private int thisTaskCount = 0;
	private CtTypeReference<?> thisElementReturnType = null;
	private Set<String> dependencies = null;
	private Set<String> handlers = null;
	private Map<String, String> argumentsAndTypes = null;
	private boolean throwsExceptions = false;
	private Factory thisFactory = null;
	private Map<Class<? extends Exception>, String> asyncExceptions = null;
	private List<CtVariableAccess<?>> variableAccessExpressions = null;
	
	public InvocationProcessor(Factory factory, Future future, CtLocalVariable<?> annotatedElement){
		dependencies = new HashSet<>();
		handlers = new HashSet<>();
		asyncExceptions = new HashMap<>();
		argumentsAndTypes = new HashMap<>();
		variableAccessExpressions = new ArrayList<>();
		
		thisFutureAnnotation = future;
		thisAnnotatedElement = annotatedElement;
		thisElementName = annotatedElement.getSimpleName();
		thisElementReturnType = annotatedElement.getType();
		thisTaskIDName = SpoonUtils.getTaskIDName(thisElementName);
		thisTaskInfoName = SpoonUtils.getTaskName(thisElementName);
		thisFactory = factory;
	}
	
	public void process(){
		getInvocations();
		checkIfThrowsException();
		replaceOccurrencesWithTaskIDName();
		inspectAnnotation();
		processInvocationArguments();
		modifySourceCode();
		
	}
	

		
	
	private void getInvocations(){
		CtExpression<?> expression = thisAnnotatedElement.getDefaultExpression();
		if(!(expression instanceof CtInvocationImpl<?>))
			throw new IllegalArgumentException("ANNOTATED ELEMENT: ->" + thisAnnotatedElement.toString()
					+ "<- IS EXPECTED TO BE AN INVOCATION STATEMENT!");
		thisInvocation = (CtInvocationImpl<?>) expression;
	}
	
	private void checkIfThrowsException(){
		Set<CtTypeReference<? extends Throwable>> exceptions = null;
		exceptions = thisInvocation.getExecutable().getDeclaration().getThrownTypes();
		if(!exceptions.isEmpty())
			throwsExceptions = true;
	}
	
	private void replaceOccurrencesWithTaskIDName(){
		String regex = "\\b" + thisElementName + "\\b";
		List<CtStatement> statementsAccessingThisVar = SpoonUtils.findVarAccessOtherThanFutureDefinition
				((CtBlockImpl<?>)thisAnnotatedElement.getParent(), thisAnnotatedElement);
		SpoonUtils.modifyStatements(statementsAccessingThisVar, regex, (thisTaskIDName+SpoonUtils.getResultPhrase()));
	}
	
	private void inspectAnnotation(){
		extractDependenciesFromStatement();
		extractTaskInfoFromAnnotation();
		extractDependenciesFromAnnotation();
		extractHandlersFromAnnotation();
		extractAsyncExceptionsFromAnnotations();
	}
	
	private void extractDependenciesFromStatement(){
		//this method could be subject to changes to extract from multiple invocations
		List<CtExpression<?>> arguments = thisInvocation.getArguments();
		for (CtExpression<?> argument : arguments){
			if(SpoonUtils.isTaskIDReplacement(thisAnnotatedElement, argument.toString())){
				String originalArgumentName = SpoonUtils.getOrigName(argument.toString());
				dependencies.add(SpoonUtils.getTaskIDName(originalArgumentName));
			}
		}				
	}	
	
	private void extractTaskInfoFromAnnotation(){
		thisTaskType = SpoonUtils.getTaskType(thisFutureAnnotation.taskType()).trim();
		thisTaskCount = thisFutureAnnotation.taskCount();
		if(thisTaskCount < 0)
			thisTaskCount = 0;
	}
	
	private void extractDependenciesFromAnnotation(){
		String dependsOn = thisFutureAnnotation.depends();
		if (!dependsOn.isEmpty()){
			String[] dependsArray = dependsOn.split(SpoonUtils.getDependsOnDelimiter());
			for (String dependencyName : dependsArray){
				//if not trimmed, duplicates with invisible white-space
				//get added
				dependencyName = dependencyName.trim(); 
						
				if(!dependencyName.isEmpty()){
					//in case user feeds taskID name etc. Small possibility but...
					dependencyName = SpoonUtils.getOrigName(dependencyName); 
					if(SpoonUtils.isFutureVariable(thisAnnotatedElement, dependencyName))
						dependencies.add(SpoonUtils.getTaskIDName(dependencyName));
				}
			}					
		}		
	}
	
	private void extractHandlersFromAnnotation(){
		String notifyHandlers = thisFutureAnnotation.notifies();
		if(!notifyHandlers.isEmpty()){
			String[] handlersArray = notifyHandlers.split(SpoonUtils.getNotifyDelimiter());
			for (String handler : handlersArray){
				handler = handler.trim();
				if(!handler.isEmpty()){
					String notifySlot = "()->" + handler;
					handlers.add(notifySlot);
				}
			}
		}		
	}
	
	private void extractAsyncExceptionsFromAnnotations(){
		Set<CtAnnotation<? extends Annotation>> annotations = thisAnnotatedElement.getAnnotations();
		for(CtAnnotation<? extends Annotation> annotation : annotations){
			Annotation actualAnnotation = annotation.getActualAnnotation();
			if(actualAnnotation instanceof AsyncCatch){
				AsyncCatch asynCatch = (AsyncCatch) actualAnnotation;
				Class<? extends Exception>[] exceptions = asynCatch.throwables();
				String[] handlers = asynCatch.handlers();
				if (exceptions.length != handlers.length)
					throw new IllegalArgumentException("THE NUMBER OF EXCEPTION CLASSES SPECIFIED FOR " + thisAnnotatedElement + 
							" IS NOT COMPLIANT WITH THE NUMBER OF HANDLERS SPECIFIED FOR THOSE EXCEPTIONS!");
				for(int i = 0; i < handlers.length; i++){
					asyncExceptions.put(exceptions[i], ("()->{try{" + handlers[i]) +";}catch(Exception e){e.printStackTrace();}}");
				}
			}
		}
	}	
	
	private void processInvocationArguments(){
		listVariableAccessExpressions();
		for (CtVariableAccess<?> varAccess : variableAccessExpressions){
			String argName = varAccess.toString();
			String origName = SpoonUtils.getOrigName(argName);
				
			if(SpoonUtils.isTaskIDReplacement(thisAnnotatedElement, argName)){
					
				CtLocalVariable<?> declaration = (CtLocalVariable<?>)SpoonUtils.getDeclarationStatement(thisAnnotatedElement, origName);
				CtTypeReference taskIDType = getTaskIDType(declaration);
					
				varAccess.getVariable().setSimpleName(SpoonUtils.getLambdaArgName(origName)+SpoonUtils.getResultPhrase());
				varAccess.setType(taskIDType);					
				argumentsAndTypes.put(varAccess.getType().toString(), SpoonUtils.getLambdaArgName(origName));
			}
			else{
				varAccess.getVariable().setSimpleName(SpoonUtils.getNonLambdaArgName(argName));
				argumentsAndTypes.put(SpoonUtils.getType(varAccess.getType().toString()), SpoonUtils.getNonLambdaArgName(argName));
			}				
		}
	}
	
	@Override
	protected void modifySourceCode() {
		CtTypeReference thisElementNewType = thisFactory.Core().createTypeReference();
		thisElementNewType.setSimpleName(getTaskInfoType());
		thisAnnotatedElement.setType(thisElementNewType);
		thisAnnotatedElement.setSimpleName(SpoonUtils.getTaskName(thisElementName));
		List<CtExpression<?>> arguments = new ArrayList<>();
			
		CtCodeSnippetExpression<?> newArg1 = thisFactory.Core().createCodeSnippetExpression();
		newArg1.setValue(thisTaskType);
		CtExpression<?> firstArg = newArg1;
		arguments.add(firstArg);
		
		if(thisTaskCount != 0){
			CtCodeSnippetExpression<?> newArg2 = thisFactory.Core().createCodeSnippetExpression();
			newArg2.setValue(Integer.toString(thisTaskCount));
			CtExpression<?> secondArg = newArg2;
			arguments.add(secondArg); 
		}
		
		CtCodeSnippetExpression<?> newArg3 = thisFactory.Core().createCodeSnippetExpression();
		newArg3.setValue(newFunctorPhrase(thisInvocation));
		CtExpression<?> thirdArg = newArg3;
		arguments.add(thirdArg);		
		 
		thisInvocation.setArguments(arguments);
				
		List<CtTypeReference<?>> typeCasts = new ArrayList<>();
		typeCasts.add(thisElementNewType);
		thisInvocation.setTypeCasts(typeCasts);
		
		CtExecutableReference executable = thisFactory.Core().createExecutableReference();
		executable.setSimpleName(SpoonUtils.getAsTaskSyntax());
		thisInvocation.setExecutable(executable);
		
		addNewStatements();	
	}

	private void addNewStatements(){
		CtBlock<?> thisBlock = (CtBlock<?>) thisAnnotatedElement.getParent();
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(thisAnnotatedElement);
		//insert the new statements after the current invocation statement. 
		thisBlock.insertAfter(filter, newStatements());
	}
	
	private CtStatementList<?> newStatements(){
		CtStatementList<?> statements = thisFactory.Core().createStatementList();
		List<CtStatement> sts = new ArrayList<>();
		
		CtInvocationImpl<?> dependsOnStatement = getDependsOnStatement();
		if (dependsOnStatement != null)
			sts.add(dependsOnStatement);
			
		/*for the cases where notification handlers use TaskID, ParaTask must check if the task is 
		 * already finished when registering the handler. Sometimes task finished before the 
		 * notifier is registered!*/
		List<CtInvocationImpl<?>> handlers = getNotifyStatements();
		if (handlers != null){
			sts.addAll(handlers);
		}
		
		List<CtInvocationImpl<?>> asyncCatchStatements = getAsyncExceptionStatements();
		sts.addAll(asyncCatchStatements);
		
		sts.add(getStartStatement());	
		
		statements.setStatements(sts);
		return statements;
	}
	
	private CtInvocationImpl<?> getDependsOnStatement(){
		/*create the dependsOn statement*/
		if(dependencies.isEmpty())
			return null;
		
		CtInvocationImpl<?> dependsOnInvoc = (CtInvocationImpl<?>) thisFactory.Core().createInvocation();
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
		
		
		CtExecutableReference dependsOnExec = thisFactory.Core().createExecutableReference();
		dependsOnExec.setSimpleName(dependsOnExecutable);
		dependsOnInvoc.setExecutable(dependsOnExec);
		
		List<CtExpression<?>> dependsOnArgs = new ArrayList<>();
		CtCodeSnippetExpression argsExp = thisFactory.Core().createCodeSnippetExpression();
		argsExp.setValue(dependsOnArguments);
		dependsOnArgs.add(argsExp);
		dependsOnInvoc.setArguments(dependsOnArgs);
		
		return dependsOnInvoc;
	}
	
	private List<CtInvocationImpl<?>> getNotifyStatements(){
		if (handlers.isEmpty())
			return null;
		
		List<CtInvocationImpl<?>> notifyStatements = new ArrayList<>();
		
		CtInvocationImpl<?> notifyStatement = (CtInvocationImpl<?>) thisFactory.Core().createInvocation();
		CtExecutableReferenceImpl executablePhrase = (CtExecutableReferenceImpl<?>) thisFactory.Core().createExecutableReference();
		executablePhrase.setSimpleName(SpoonUtils.getParaTaskSyntax() + ".registerSlotToNotify");
		notifyStatement.setExecutable(executablePhrase);
		
				
		for (String handler : handlers){
			String execArgument = SpoonUtils.getTaskName(thisElementName) + ", " + handler;
			CtCodeSnippetExpression<?> notiesExpression = thisFactory.Core().createCodeSnippetExpression();
			notiesExpression.setValue(execArgument);
			List<CtExpression<?>> notifyStatemtnArgument = new ArrayList<>();
			notifyStatemtnArgument.add(notiesExpression);
			notifyStatement.setArguments(notifyStatemtnArgument);
			notifyStatements.add(notifyStatement);
		}
		
		return notifyStatements;
	}
	
	private List<CtInvocationImpl<?>> getAsyncExceptionStatements(){
	  List<CtInvocationImpl<?>> invocations = new ArrayList<>();
	  Set<Class<? extends Exception>> exceptions = asyncExceptions.keySet();
	  
	  for(Class<? extends Exception> exception : exceptions){
		  CtInvocationImpl<?> invocation = new CtInvocationImpl<>();
		  
		  CtCodeSnippetExpression<?> argument = thisFactory.Core().createCodeSnippetExpression();
		  argument.setValue(thisTaskInfoName + ", " + exception.getSimpleName() + ".class, " + asyncExceptions.get(exception));
		 
		  List<CtExpression<?>> arguments = new ArrayList<>();
		  arguments.add(argument);
		  
		  CtExecutableReference executable = thisFactory.Core().createExecutableReference();
		  executable.setSimpleName(SpoonUtils.getParaTaskSyntax()+".registerAsyncCatch");
		  
		  invocation.setExecutable(executable);
		  invocation.setArguments(arguments);
		  invocations.add(invocation);
	  }
	  
	  /*
	   * AsynCatch has been processed, so remove it from the annotations. 
	   */
	  Set<CtAnnotation<? extends Annotation>> newAnnotations = new LinkedHashSet<>();
	  Set<CtAnnotation<? extends Annotation>> annotations = thisAnnotatedElement.getAnnotations();
	  for(CtAnnotation<? extends Annotation> annotation : annotations){
		  Annotation actualAnnotation = annotation.getActualAnnotation();
		  if(!(actualAnnotation instanceof AsyncCatch))	
			  newAnnotations.add(annotation);
	  }
	  
	  thisAnnotatedElement.setAnnotations(newAnnotations);
	  return invocations;
	}
	
	private CtLocalVariableImpl<?> getStartStatement(){
		/*create the start statement*/
		CtLocalVariableImpl<?> taskIdDeclaration = (CtLocalVariableImpl<?>) thisFactory.Core().createLocalVariable();
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
		startPhrase = thisTaskInfoName + startPhrase;
				
		CtCodeSnippetExpression defaultExp = thisFactory.Core().createCodeSnippetExpression();
		defaultExp.setValue(startPhrase);
		
		CtTypeReference taskIDType = getTaskIDType(thisAnnotatedElement);
		taskIdDeclaration.setType(taskIDType);
		taskIdDeclaration.setSimpleName(thisTaskIDName);
		taskIdDeclaration.setDefaultExpression(defaultExp);
		
		return taskIdDeclaration;
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

	public String getTaskInfoType(){
		String returnPhase = (thisElementReturnType.toString().contains("Void")) ? "Void" : SpoonUtils.getReturnType(thisElementReturnType.toString());
		String taskInfoType = SpoonUtils.getTaskInfoSyntax() + getNumArgs() + "<" + returnPhase;
		Set<String> argTypes = argumentsAndTypes.keySet();
		for(String argType : argTypes){
			taskInfoType += ", " + argType;
		}
		taskInfoType += ">";
		return taskInfoType;
	}
	
	public String getFunctorType(){
		String functorReturnPhrase = (thisElementReturnType.toString().contains("Void")) ? "NoReturn<Void" : ("WithReturn<"+SpoonUtils.getReturnType(thisElementReturnType.toString()));
		String functorName = SpoonUtils.getFunctorSyntax() + getNumArgs() + functorReturnPhrase;
		return functorName;
	}
	
	public CtTypeReference getTaskIDType(CtVariable<?> declaration){
		String declarationType = declaration.getType().toString();
		declarationType = SpoonUtils.getOrigName(declarationType);
		String taskType = SpoonUtils.getReturnType(declarationType);		
		
		taskType = SpoonUtils.getTaskIdSyntax() + "<" + taskType + ">";
		CtTypeReference<?> taskIDType = thisFactory.Core().createTypeReference();
		taskIDType.setSimpleName(taskType);
		return taskIDType;
	}

	public String getFunctorCast(){
		String functorCast = getFunctorType();
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
	
	public String newFunctorPhrase(CtInvocation<?> invocation){
		
		String returnStatement = "";
		String catchReturnStmt = "";
		
		if(!thisElementReturnType.toString().contains("Void")){
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
	
	private void listVariableAccessExpressions(){
		variableAccessExpressions = new ArrayList<>();
		listVariableAccessExpressions(thisInvocation);
	}
	
	private void listVariableAccessExpressions(CtExpression<?> expression){
		
		if(expression instanceof CtVariableAccess<?>){
			CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) expression;
			variableAccessExpressions.add(variableAccess);
		}
		else if (expression instanceof CtBinaryOperator<?>){
			CtBinaryOperator<?> binaryOperator = (CtBinaryOperator<?>) expression;
			listVariableAccessExpressions(binaryOperator.getLeftHandOperand());
			listVariableAccessExpressions(binaryOperator.getRightHandOperand());
		}
		else if(expression instanceof CtInvocationImpl<?>){
			CtInvocationImpl<?> invocation = (CtInvocationImpl<?>) expression;
			List<CtExpression<?>> arguments = invocation.getArguments();
			for(CtExpression<?> argument : arguments){
				listVariableAccessExpressions(argument);
			}
		}
		else if (expression instanceof CtUnaryOperator<?>){
			CtUnaryOperator<?> unaryOperator = (CtUnaryOperator<?>) expression;
			listVariableAccessExpressions(unaryOperator.getOperand());
		}
		else if (expression instanceof CtArrayAccess<?, ?>){
			CtArrayAccess<?, ?> arrayAccess = (CtArrayAccess<?, ?>) expression;
			listVariableAccessExpressions(arrayAccess.getIndexExpression());
		}
		
	}

	@Override
	protected void printComponents() {
		// TODO Auto-generated method stub
		
	}

	//---------------------------------------HELPER METHODS-----------------------------------------
}
