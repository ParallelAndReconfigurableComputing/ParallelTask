package apt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import apt.annotations.AsyncCatch;
import apt.annotations.Future;

import java.util.List;

import spoon.reflect.factory.Factory;
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
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.reference.CtExecutableReferenceImpl;

/**
 * This annotation processor, processes <code>Future</code> annotations that specifically appear at
 * the declaration of a local variable. For example:</br>
 * (a)Future</br>
 * int a = foo(x);
 * </br>
 * It transforms the variable declaration into the declaration of a TaskInfo object. Using the attributes
 * of the <code>Future</code> annotation, this processor configures MULTI, ONEOFF, INTERACTIVE and MULTI_IO
 * tasks. Moreover, using the <code><b>depends</b></code> attribute, which is a comma separated string of future 
 * variables, a programmer can specify the future variables that a specific future variable needs to wait for
 * before it starts processing. 
 *  
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class InvocationProcessor extends AptAbstractFutureProcessor {
	
	private CtInvocation<?> thisInvocation = null;
	private Future thisFutureAnnotation = null;
	private String thisTaskIDName = null;
	private String thisTaskInfoName = null;
	private String thisTaskType = null;
	private int thisTaskCount = 0;
	private Set<String> dependencies = null;
	private Set<String> handlers = null;
	private Map<String, CtTypeReference<?>> argumentsAndTypes = null;
	private boolean throwsExceptions = false;
	private Map<String, String> asyncExceptions = null;
	private List<CtVariableAccess<?>> variableAccessExpressions = null;
	
	public InvocationProcessor(Factory factory, Future future, CtLocalVariable<?> annotatedElement){
		dependencies = new HashSet<>();
		handlers = new HashSet<>();
		asyncExceptions = new HashMap<>();
		argumentsAndTypes = new HashMap<>();
		variableAccessExpressions = new ArrayList<>();
		
		thisFutureAnnotation = future;
		thisAnnotatedLocalElement = annotatedElement;
		thisElementName = annotatedElement.getSimpleName();
		thisElementType = annotatedElement.getType();
		thisTaskIDName = APTUtils.getTaskIDName(thisElementName);
		thisTaskInfoName = APTUtils.getTaskName(thisElementName);
		thisFactory = factory;
	}
	
	@Override
	public void process(){
		getInvocations();
		checkIfThrowsException();
		replaceOccurrencesWithTaskIDName();
		inspectAnnotation();
		processInvocationArguments();
		modifySourceCode();		
	}		
	
	private void getInvocations(){
		CtExpression<?> expression = thisAnnotatedLocalElement.getDefaultExpression();
		if(!(expression instanceof CtInvocationImpl<?>))
			throw new IllegalArgumentException("ANNOTATED ELEMENT: ->" + thisAnnotatedLocalElement.toString()
					+ "<- IS EXPECTED TO BE AN INVOCATION STATEMENT!");
		thisInvocation = (CtInvocationImpl<?>) expression;
	}
	
	private void checkIfThrowsException(){
		try{
			Set<CtTypeReference<? extends Throwable>> exceptions = null;
			exceptions = thisInvocation.getExecutable().getDeclaration().getThrownTypes();
			if(exceptions != null && !exceptions.isEmpty())
				throwsExceptions = true;
		}catch(Exception e){
			System.out.println("\n EXCEPTION THROWN FOR INVOCATION: " + thisInvocation.toString() + " WHILE CHECKING FOR ITS THROWN TYPES. \n");
			e.printStackTrace();
		}
	}
	
	private void replaceOccurrencesWithTaskIDName(){
		String regex = "\\b" + thisElementName + "\\b";
		List<CtStatement> statementsAccessingThisVar = APTUtils.findVarAccessOtherThanFutureDefinition
				((CtBlockImpl<?>)thisAnnotatedLocalElement.getParent(), thisAnnotatedLocalElement);
		APTUtils.modifyStatements(statementsAccessingThisVar, regex, (thisTaskIDName+APTUtils.getResultSyntax()));
	}
	
	private void inspectAnnotation(){
		extractDependenciesFromStatement();
		extractTaskInfoFromAnnotation();
		extractReductionFromAnnotation();
		extractDependenciesFromAnnotation();
		extractHandlersFromAnnotation();
		extractAsyncExceptionsFromAnnotations();
	}
	
	/*
	 * This method parses the method invocation for this future variable declaration, and checks
	 * the arguments that are used within that invocation. If an argument is a taskIDReplacement, 
	 * that means its corresponding future variable has been declared before the declaration of 
	 * this future variable, so this future variable automatically depends on that task!   
	 */
	private void extractDependenciesFromStatement(){
		//this method could be subject to changes to extract from multiple invocations
		List<CtExpression<?>> arguments = thisInvocation.getArguments();
		for (CtExpression<?> argument : arguments){
			if(APTUtils.isTaskIDReplacement(thisAnnotatedLocalElement, argument.toString())){
				String originalArgumentName = APTUtils.getOrigName(argument.toString());
				dependencies.add(APTUtils.getTaskIDName(originalArgumentName));
			}
		}				
	}	
	
	/*
	 * This method inspect the corresponding annotation for this future variable, and figures out
	 * the task type (i.e., ONEOFF, MULTI, INTERACTIVE, MULTI_IO), and the number of tasks, if it
	 * is a multi-task.  
	 */
	private void extractTaskInfoFromAnnotation(){
		thisTaskType = APTUtils.getTaskType(thisFutureAnnotation.taskType()).trim();
		thisTaskCount = thisFutureAnnotation.taskCount();
		if(thisTaskCount < 0)
			thisTaskCount = 0;
	}
	
	/*
	 * This method extracts user-specified reduction from the corresponding annotation. One can 
	 * either use reserved keywords for RedLib reductions, or specify the name of a reduction 
	 * object that is already defined before declaring the future variable, or the name of a 
	 * class that is an implementation of RedLib Reduction interface. This reductin phrase is 
	 * only considered if the task is a MULTI task. 
	 */
	private void extractReductionFromAnnotation(){
		if(thisTaskType.contains("MULTI")){
			thisElementReductionString = thisFutureAnnotation.reduction();
		}else{
			thisElementReductionString = "";
		}			
	}
	
	/*
	 * Checks the values that are passed to the 'depends' attribute of the Future annotation, and 
	 * if a value is the identifier of a future variable that is declared before this future variable,
	 * its taskID is added as a dependency for this task. Since the data-structure is a Set, thus
	 * elements that already exist won't be added again. 
	 */
	private void extractDependenciesFromAnnotation(){
		String dependsOn = thisFutureAnnotation.depends();
		if (!dependsOn.isEmpty()){
			String[] dependsArray = dependsOn.split(APTUtils.getDependsOnDelimiter());
			for (String dependencyName : dependsArray){
				//if not trimmed, duplicates with invisible white-space
				//might get added
				dependencyName = dependencyName.trim(); 
						
				if(!dependencyName.isEmpty()){
					//in case user feeds taskID name etc. Small possibility but...
					dependencyName = APTUtils.getOrigName(dependencyName); 
					if(APTUtils.isFutureVariable(thisAnnotatedLocalElement, dependencyName))
						dependencies.add(APTUtils.getTaskIDName(dependencyName));
				}
			}					
		}		
	}
	
	/*
	 * Extracts the notification handlers that are specified to by notified once this task is done
	 * via the Future annotation.  
	 */
	private void extractHandlersFromAnnotation(){
		String notifyHandlers = thisFutureAnnotation.notifies();
		if(!notifyHandlers.isEmpty()){
			String[] handlersArray = notifyHandlers.split(APTUtils.getNotifyDelimiter());
			for (String handler : handlersArray){
				handler = handler.trim();
				if(!handler.isEmpty()){
					String notifySlot = "()->" + handler;
					handlers.add(notifySlot);
				}
			}
		}		
	}
	
	/*
	 * Extracts the exceptions that are specified to be handled asynchronously by this task via the 
	 * AsyncCatch annotation. 
	 */
	private void extractAsyncExceptionsFromAnnotations(){
		List<CtAnnotation<? extends Annotation>> annotations = thisAnnotatedLocalElement.getAnnotations();
		for(CtAnnotation<? extends Annotation> annotation : annotations){
			Annotation actualAnnotation = annotation.getActualAnnotation();
			if(actualAnnotation instanceof AsyncCatch){
				AsyncCatch asynCatch = (AsyncCatch) actualAnnotation;
				Class<? extends Exception>[] exceptions = asynCatch.throwables();
				String[] handlers = asynCatch.handlers();
				if (exceptions.length != handlers.length)
					throw new IllegalArgumentException("THE NUMBER OF EXCEPTION CLASSES SPECIFIED FOR " + thisAnnotatedLocalElement + 
							" IS NOT COMPLIANT WITH THE NUMBER OF HANDLERS SPECIFIED FOR THOSE EXCEPTIONS!");
				for(int i = 0; i < handlers.length; i++){
					asyncExceptions.put(exceptions[i].getName(), ("()->{try{" + handlers[i]) +";}catch(Exception e){e.printStackTrace();}}");
				}
			}
		}
	}	
	
	/*
	 * Considering that the name of the variables per-se cannot be used in lambda expressions, 
	 * they need representatives. This method checks if the argument is a taskIDReplacement, then
	 * it is replaced by a taskID representative, if not, it is replaced by a lambda representative.
	 * The name of the representative and its corresponding type is saved in a map to be used
	 * when creating the taskInfo declaration.   
	 */
	private void processInvocationArguments(){
		listVariableAccessExpressions();
		for (CtVariableAccess<?> varAccess : variableAccessExpressions){
			String argName = varAccess.toString();
			String origName = APTUtils.getOrigName(argName);
				
			if(APTUtils.isTaskIDReplacement(thisAnnotatedLocalElement, argName)){
				
				/*
				 * only LocalVariables can be future variables, and therefore, a taskID replacement can only be related to a 
				 * LocalVariable. 
				 */
				CtLocalVariable<?> declaration = (CtLocalVariable<?>)APTUtils.getDeclarationStatement(thisAnnotatedLocalElement, origName);
				CtTypeReference taskIDType = getTaskIDType(declaration, false);
				
				varAccess.getVariable().setSimpleName(APTUtils.getLambdaArgName(origName)+APTUtils.getResultSyntax());
				varAccess.getVariable().setType(taskIDType);					
				
				argumentsAndTypes.put(APTUtils.getLambdaArgName(origName), varAccess.getType());
			}
			else{
				varAccess.getVariable().setSimpleName(APTUtils.getNonLambdaArgName(argName));
				CtTypeReference<?> varType = thisFactory.Core().createTypeReference();
				varType.setSimpleName(APTUtils.getType(varAccess.getType().toString()));
				argumentsAndTypes.put(APTUtils.getNonLambdaArgName(argName), varType);
			}				
		}
	}
	
	/*
	 * Changes the current future variable declaration with the new TaskInfo declaration. 
	 * 1. Changes the type of declaration to its equivalent TaskInfo type. For example,
	 *    from 'int' to 'TaskInfo<Integer>'
	 *    
	 * 2. Changes the name to the taskInfo equivalent. For example from 'a' to '__aptTaskInfo__'
	 * 
	 * 3. Prepares the declaration arguments for the TaskInfo. That is, the taskType (e.g., ONEOFF),
	 *    task count, and the functional interface (functor) that are passed as arguments for declaring
	 *    a TaskInfo. 
	 *    
	 * 4. Casts the taskInfo declaration phrase to the type of this TaskInfo object. 
	 *	  For example, TaskInfoTwoArgs<Void, Integer, Integer>
	 *
	 * 5. Finally, generates the 'asTask' method and calls it on 'pt.runtime.ParaTask'. Note, that the
	 *    invocation target is set to 'null', otherwise, the method would be called on the parent class
	 *    of the declaration rather than 'pt.runtime.ParaTask'. 
	 * 
	 * (non-Javadoc)
	 * @see sp.processors.PtAnnotationProcessor#modifySourceCode()
	 */
	@Override
	protected void modifySourceCode() {
		//1
		CtTypeReference thisElementNewType = thisFactory.Core().createTypeReference();
		thisElementNewType.setSimpleName(getTaskInfoType());
		thisAnnotatedLocalElement.setType(thisElementNewType);
		
		//2
		thisAnnotatedLocalElement.setSimpleName(APTUtils.getTaskName(thisElementName));
		
		//3
		List<CtExpression<?>> arguments = new ArrayList<>();
		
		/* Figuring out the task type - e.g. ONEOFF */	
		CtCodeSnippetExpression<?> newArg1 = thisFactory.Core().createCodeSnippetExpression();
		newArg1.setValue(thisTaskType);
		CtExpression<?> firstArg = newArg1;
		arguments.add(firstArg);
	
		/* Figuring out task count. That is, the number of tasks that a multi-task is supposed to spawn. */
		if(thisTaskCount != 0){
			CtCodeSnippetExpression<?> newArg2 = thisFactory.Core().createCodeSnippetExpression();
			newArg2.setValue(Integer.toString(thisTaskCount));
			CtExpression<?> secondArg = newArg2;
			arguments.add(secondArg); 
		}
		
		/* Figuring out the functional interface! */
		CtCodeSnippetExpression<?> newArg3 = thisFactory.Core().createCodeSnippetExpression();
		newArg3.setValue(newFunctorPhrase(thisInvocation));
		CtExpression<?> thirdArg = newArg3;
		arguments.add(thirdArg);		
		 
		thisInvocation.setArguments(arguments);
		
		//4
		List<CtTypeReference<?>> typeCasts = new ArrayList<>();
		typeCasts.add(thisElementNewType);
		thisInvocation.setTypeCasts(typeCasts);
		
		//5
		CtExecutableReference executable = thisFactory.Core().createExecutableReference();
		executable.setSimpleName(APTUtils.getAsTaskSyntax());
		thisInvocation.setExecutable(executable);
		//if invocation target is not set to null, the 'asTask' method maybe called on an incorrect target. 
		thisInvocation.setTarget(null); 
		
		addNewStatements();	
	}

	/*
	 * Adds the statements for setting dependencies, exception handlers and triggering the taskInfo object
	 * after the declaration statement. 
	 */
	private void addNewStatements(){
		CtBlock<?> thisBlock = (CtBlock<?>) thisAnnotatedLocalElement.getParent();
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(thisAnnotatedLocalElement);
		//insert the new statements after the current invocation statement. 
		thisBlock.insertAfter(filter, newStatements());
	}
	
	private CtStatementList newStatements(){
		CtStatementList statements = thisFactory.Core().createStatementList();
		List<CtStatement> sts = new ArrayList<>();
		
		CtInvocationImpl<?> dependsOnStatement = getDependenceRegistrationStatement();
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
		
		List<CtStatement> reductionStatements = getReductionStatements(thisElementType.toString(), APTUtils.getTaskIDName(thisElementName));
		if(reductionStatements != null)
			sts.addAll(reductionStatements);
		
		statements.setStatements(sts);
		return statements;
	}
	
	/*
	 * Creates the dependsOn statement, which sets the TaskID objects, on which this task
	 * depends.  
	 */
	private CtInvocationImpl<?> getDependenceRegistrationStatement(){
		/*create the dependsOn statement*/
		if(dependencies.isEmpty())
			return null;
		
		CtInvocationImpl<?> dependsOnInvoc = (CtInvocationImpl<?>) thisFactory.Core().createInvocation();
		String dependsOnExecutable = APTUtils.getParaTaskSyntax() + "." + APTUtils.getRegisterDependencesSyntax();
		String taskInfoName = APTUtils.getTaskName(thisElementName);
		
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
		CtCodeSnippetExpression taskInfoExp = thisFactory.Core().createCodeSnippetExpression();
		CtCodeSnippetExpression argsExp = thisFactory.Core().createCodeSnippetExpression();
		
		taskInfoExp.setValue(taskInfoName);
		argsExp.setValue(dependsOnArguments);
		
		dependsOnArgs.add(taskInfoExp);
		dependsOnArgs.add(argsExp);
		dependsOnInvoc.setArguments(dependsOnArgs);
		
		return dependsOnInvoc;
	}
	
	/*
	 * creates the statement, which sets the notification handlers which are notified by this 
	 * task, once this task is finished. 
	 */
	private List<CtInvocationImpl<?>> getNotifyStatements(){
		if (handlers.isEmpty())
			return null;
		
		List<CtInvocationImpl<?>> notifyStatements = new ArrayList<>();
		
		CtInvocationImpl<?> notifyStatement = (CtInvocationImpl<?>) thisFactory.Core().createInvocation();
		CtExecutableReferenceImpl executablePhrase = (CtExecutableReferenceImpl<?>) thisFactory.Core().createExecutableReference();
		executablePhrase.setSimpleName(APTUtils.getParaTaskSyntax() + "." + APTUtils.getRegisterHandlerSlotSyntax());
		notifyStatement.setExecutable(executablePhrase);
		
				
		for (String handler : handlers){
			String execArgument = APTUtils.getTaskName(thisElementName) + ", " + handler;
			CtCodeSnippetExpression<?> notiesExpression = thisFactory.Core().createCodeSnippetExpression();
			notiesExpression.setValue(execArgument);
			List<CtExpression<?>> notifyStatemtnArgument = new ArrayList<>();
			notifyStatemtnArgument.add(notiesExpression);
			notifyStatement.setArguments(notifyStatemtnArgument);
			notifyStatements.add(notifyStatement);
		}
		
		return notifyStatements;
	}
	
	/*
	 * Creates the statement, which sets the asynchronous exceptions that are asynchronously handled by this
	 * tasks once they are thrown. 
	 */
	private List<CtInvocationImpl<?>> getAsyncExceptionStatements(){
	  List<CtInvocationImpl<?>> invocations = new ArrayList<>();
	  Set<String> exceptions = asyncExceptions.keySet();
	  
	  for(String exception : exceptions){
		  CtInvocationImpl<?> invocation = new CtInvocationImpl<>();
		  
		  CtCodeSnippetExpression<?> argument = thisFactory.Core().createCodeSnippetExpression();
		  argument.setValue(thisTaskInfoName + ", " + exception + ".class, " + asyncExceptions.get(exception));
		 
		  List<CtExpression<?>> arguments = new ArrayList<>();
		  arguments.add(argument);
		  
		  CtExecutableReference executable = thisFactory.Core().createExecutableReference();
		  executable.setSimpleName(APTUtils.getParaTaskSyntax() + "." + APTUtils.getRegisterAsyncCatchSyntax());
		  
		  invocation.setExecutable(executable);
		  invocation.setArguments(arguments);
		  invocations.add(invocation);
	  }
	  
	  /*
	   * AsynCatch has been processed, so remove it from the annotations. 
	   */
	  List<CtAnnotation<? extends Annotation>> newAnnotations = new ArrayList<>();
	  List<CtAnnotation<? extends Annotation>> annotations = thisAnnotatedLocalElement.getAnnotations();
	  for(CtAnnotation<? extends Annotation> annotation : annotations){
		  Annotation actualAnnotation = annotation.getActualAnnotation();
		  if(!(actualAnnotation instanceof AsyncCatch))	
			  newAnnotations.add(annotation);
	  }
	  
	  thisAnnotatedLocalElement.setAnnotations(newAnnotations);
	  return invocations;
	}
	
	/*
	 * Creates the 'start' statement that is called on the corresponding TaskInfo object, in order to 
	 * get it triggered. 
	 */
	private CtLocalVariableImpl<?> getStartStatement(){
		/*create the start statement*/
		CtLocalVariableImpl<?> taskIdDeclaration = (CtLocalVariableImpl<?>) thisFactory.Core().createLocalVariable();
		String startPhrase = ".start(";
		
		Set<String> argumentNames = argumentsAndTypes.keySet();	
		int counter = 0;
		for(String argumentName : argumentNames){
			
			CtTypeReference<?> argumentType = argumentsAndTypes.get(argumentName);
			
			if (APTUtils.isNonLambdaArg(argumentName))				
				startPhrase += APTUtils.getOrigName(argumentName);
			
			else if (APTUtils.isLambdaArg(argumentName))
				startPhrase += APTUtils.getTaskIDName(APTUtils.getOrigName(argumentName));
			 
			counter++;
			if(counter != argumentNames.size())
				startPhrase += ", ";
		}
		startPhrase += ")";
		
		boolean taskIDGroup = (thisTaskType.contains("MULTI")) ? true : false;
		CtTypeReference taskIDType = getTaskIDType(thisAnnotatedLocalElement, taskIDGroup);
		String castingPhrase = "";
		if(taskIDGroup)
			castingPhrase = "(" + taskIDType.toString() + ")";
		
		startPhrase = castingPhrase + thisTaskInfoName + startPhrase;
		CtCodeSnippetExpression defaultExp = thisFactory.Core().createCodeSnippetExpression();
		defaultExp.setValue(startPhrase);
				
		taskIdDeclaration.setType(taskIDType);
		taskIdDeclaration.setSimpleName(thisTaskIDName);
		taskIdDeclaration.setDefaultExpression(defaultExp);
		
		return taskIdDeclaration;
	}
			
	//---------------------------------------HELPER METHODS-----------------------------------------
	
	private String getNumArgs(){
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

	/*
	 * Figures out the generic type set for this TaskInfo object. 
	 */
	private String getTaskInfoType(){
		String returnPhase = (thisElementType.toString().contains("Void")) ? "Void" : APTUtils.getReturnType(thisElementType.toString());
		String taskInfoType = APTUtils.getTaskInfoSyntax() + getNumArgs() + "<" + returnPhase;
		Set<String> argumentNames = argumentsAndTypes.keySet();
		for(String argumentName : argumentNames){
			taskInfoType += ", " + argumentsAndTypes.get(argumentName).toString();
		}
		taskInfoType += ">";
		return taskInfoType;
	}
	
	/*
	 * Figures out the generic type set for the functional interface that corresponds to this 
	 * task object. 
	 * Note, that TaskInfo objects replace a 'Void' attribute when a task is not returning any
	 * value. However, PT functional interfaces do not specify the type in the generic type set,
	 * instead, the functor name is appended with "NoReturn" or "WithReturn". 
	 */
	private String getFunctorType(){
		String functorReturnPhrase = (thisElementType.toString().contains("Void")) ? "NoReturn<" : ("WithReturn<"+APTUtils.getReturnType(thisElementType.toString()));
		String functorName = APTUtils.getFunctorSyntax() + getNumArgs() + functorReturnPhrase;
		if(!(functorName.contains("NoReturn") || functorName.contains("NoArgsWithReturn")))
			functorName += ", ";
		return functorName;
	}
	
	/*
	 * Figures out the TaskID type of this task. For example: TaskID<Integer>, if the future variable
	 * is of type 'int/Integer'. 
	 * The boolean argument 'taskIDGroup' specifies if the created TaskID must be a TaskIDGroup. This
	 * boolean value is here, because TaskIDGroups are only meant to be created for the start statements,
	 * and not when TaskIDs are used as functor arguments. 
	 */
	private CtTypeReference getTaskIDType(CtVariable<?> declaration, boolean taskIDGroup){
		String declarationType = declaration.getType().toString();
		declarationType = APTUtils.getOrigName(declarationType);
		String taskType = APTUtils.getReturnType(declarationType);
		
		if(taskIDGroup)
			taskType = APTUtils.getTaskIDGroupSyntax() + "<" + taskType + ">";
		else
			taskType = APTUtils.getTaskIdSyntax() + "<" + taskType + ">";
		
		CtTypeReference<?> taskIDType = thisFactory.Core().createTypeReference();
		taskIDType.setSimpleName(taskType);
		return taskIDType;
	}

	/* Figures out the type of the functional interface that the functor for this
	 * lambda expression needs to be casted to.
	 */
	private String getFunctorCast(){
		String functorCast = getFunctorType();
		if(functorCast.contains("FunctorNoArgsNoReturn"))
			return "";
		
		Set<String> argumentNames = argumentsAndTypes.keySet();
		int counter = 0;
		for(String argumentName : argumentNames){
			counter++;
			functorCast += argumentsAndTypes.get(argumentName).toString();
			if(counter < argumentNames.size())
				functorCast += ", ";
		}
		functorCast += ">";
		return functorCast;
	}
	
	/* Creates the argument list that is going to be used for the lambda expression.
	 * That is the list of values that must be sent as arguments to the lambda expression.
	 */
	private String getLambdaArgs(){
		String lambdaArgs = "(";
		Set<String> argumentNames = argumentsAndTypes.keySet();
		int counter = 0;
		for (String argumentName : argumentNames){
			lambdaArgs += argumentName;
			counter++;
			if(counter < argumentNames.size())
				lambdaArgs += ", ";
		}
		lambdaArgs += ")";
		return lambdaArgs;
	}
	
	/* Creates the anonymous functor declaration that is going to be 
	 * used within the declaration of the corresponding TaskInfo for 
	 * this future variable. 
	 */
	private String newFunctorPhrase(CtInvocation<?> invocation){
		
		String returnStatement = "";
		String catchReturnStmt = "";
		
		if(!thisElementType.toString().contains("Void")){
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
		
		String newFunctorPhrase = "\n\t\t\t" + functorTypeCast + getLambdaArgs() + " -> " + invocationPhrase;
		return newFunctorPhrase;
	}
	
	/*
	 * Lists all the variable-access expressions used in this invocation.  
	 */
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

	//---------------------------------------HELPER METHODS-----------------------------------------
}