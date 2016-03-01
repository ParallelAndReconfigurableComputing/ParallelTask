package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import pt.annotations.AsyncCatch;
import pt.annotations.Future;
import pt.runtime.ParaTask;
import pt.runtime.TaskInfo;
import pt.runtime.TaskInfoNoArgs;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtAssignmentImpl;
import spoon.support.reflect.code.CtBinaryOperatorImpl;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtCodeSnippetExpressionImpl;
import spoon.support.reflect.code.CtCodeSnippetStatementImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.code.CtVariableAccessImpl;
import spoon.support.reflect.declaration.CtExecutableImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;


public class FutureProcessor extends
		AbstractAnnotationProcessor<Future, CtVariable<?>> {
	
	List<CtInvocationImpl<?>> invocations = null;
	List<String> lambdaExpressionArguments = null;
	List<String> notifyHandlers = null;
	List<String> dependencies = null;
	String taskIDName = null;
	String returnType = null;
	String variableName = null;
	StringBuilder paraTaskExpression = null;
	CtVariable<?> element = null;
	Map<Class<? extends Exception>, String> asynchExceptions = null;
	
	public void process(Future annotation, CtVariable<?> element) {
		
		System.out.println("Current element: \n" + element.toString());
		
		this.element = element;
		
		variableName = element.getSimpleName();
		
		taskIDName = SpoonUtils.getTaskIDName(variableName);
		
		returnType = element.getType().getSimpleName();
				
		CtExpression<?> defaultExpression = element.getDefaultExpression();
		
//		System.out.println("----------------------------------------------------------------------");
//		System.out.println("Element simple name: " + variableName);
//		System.out.println("getDefaultExpression.getClass: " + defaultExpression.getClass());
//		System.out.println("And the expression is (toString): " + defaultExpression.toString());
//		System.out.println("Return type is: " + returnType);
//		System.out.println("----------------------------------------------------------------------");
//		
		invocations = new ArrayList<>();
		paraTaskExpression = new StringBuilder();
		dependencies =  new ArrayList<String>();
		notifyHandlers = new ArrayList<String>();
		asynchExceptions = new HashMap<Class<? extends Exception>, String>();
		
		System.out.println("Listing invocations for: " + defaultExpression);
		listInvocationsOfThisExpression(defaultExpression);
		
		if(invocations.isEmpty()) {
			System.out.println("Invocation list is empty!");
			
			//throw new RuntimeException("FUTURE ANNOTATION HAS NOT BEEN USED FOR A VALID EXPRESSION!\n"
//					+ "THE EXPRESSION MUST EITHER BE AN INVOCATION, OR A BINARY EXPRESSINO THAT CONTAINS AN INVOCATION!\n"
//					+ "YOUR EXPRESSION: " + defaultExpression.toString());
			
		}
		
		processInvocations();
		processFutureAttributes();
		processAsyncCatches();	
		findVariableUsageInBlcok();
	}


	/**
	 * Lists all of the invocations that are in an expression.
	 * @param expression
	 */
	public void listInvocationsOfThisExpression(CtExpression<?> expression){
		
		if (expression instanceof CtInvocationImpl<?>){
			invocations.add((CtInvocationImpl<?>)expression);
			return;
		}
		else if (expression instanceof CtBinaryOperatorImpl<?>){
			listInvocationsOfThisExpression(((CtBinaryOperatorImpl<?>) expression).getLeftHandOperand());
			listInvocationsOfThisExpression(((CtBinaryOperatorImpl<?>) expression).getRightHandOperand());
		}
		else if (expression instanceof CtCodeSnippetExpressionImpl<?>){
			CtCodeSnippetExpressionImpl<?> expressionImpl = (CtCodeSnippetExpressionImpl<?>) expression;
			//expression.
		}
		else {
			System.out.println("Currently not supported: Expression type: " + expression.getClass());
		}
		
		return;
	}
	
	/**
	 * Finds out whether this argument of an invocation should be added as
	 * dependencies and/or argument for the lambda expression!
	 * @param argument
	 */
	public void processArgument (CtExpression<?> argument){
		
		if (argument instanceof CtInvocationImpl<?>){
			CtInvocationImpl<?> invocation = (CtInvocationImpl<?>) argument;
			List<CtExpression<?>> arguments = invocation.getArguments();
			for (CtExpression<?> arg : arguments){
				processArgument(arg);
			}				
		}
		
		else if (argument instanceof CtBinaryOperatorImpl<?>){
			processArgument(((CtBinaryOperatorImpl<?>) argument).getLeftHandOperand());
			processArgument(((CtBinaryOperatorImpl<?>) argument).getRightHandOperand());
		}
		
		else if (argument instanceof CtVariableAccessImpl<?>){
			CtVariableAccessImpl<?> variableAccess = (CtVariableAccessImpl<?>) argument;
			String argName = variableAccess.toString();
			String argTasIDName = SpoonUtils.getTaskIDName(argName);
			if(SpoonUtils.isFutureArgument(element, argName)){
				dependencies.add(argTasIDName);
				variableAccess.getVariable().setSimpleName(argTasIDName+".getResult()");
				argName = argTasIDName;
			}
			lambdaExpressionArguments.add(argName);
		}
	}
	
	
	public void processFutureAttributes(){
		Set<CtAnnotation<?>> annotations = element.getAnnotations();
		for (CtAnnotation<? extends Annotation> ctAnno : annotations){
			Annotation annotation = ctAnno.getActualAnnotation();
			if (annotation instanceof Future){
				Future future = (Future) annotation;
				String dependsOn = future.depends();
				String notifies = future.notifies();
				
				if (!dependsOn.isEmpty()){
					String[] depends = dependsOn.split(",");
					for (String depend : depends){
						depend = depend.trim();
						if(!SpoonUtils.isFutureArgument(element, depend))
							throw new RuntimeException("One or more dependencies specified for future "
									+ "variable " + variableName + " are not future variables!");
						dependencies.add(SpoonUtils.getTaskIDName(depend));
					}
				}
				
				if(!notifies.isEmpty()){
					String[] notifyHandlers = notifies.split(";");
							
					for (String notifyHandler : notifyHandlers){
						notifyHandler = notifyHandler.trim();
						notifyHandler = "( ) -> " + notifyHandler;
						this.notifyHandlers.add(notifyHandler);
					}
				}
				/*The first @Future for a variable is considered!*/
				break;
			}
		}
	}
	
	public void processAsyncCatches(){
		Set<CtAnnotation<?>> annotations = element.getAnnotations();
		for (CtAnnotation<? extends Annotation> ctAnno : annotations){
			Annotation annotation = ctAnno.getActualAnnotation();
			if (annotation instanceof AsyncCatch){
				AsyncCatch asyncCatch = (AsyncCatch) annotation;
				try{
					Class<? extends Exception> exceptionClass = asyncCatch.throwable();
					String handler = asyncCatch.handler();
					handler = "() -> " + handler;
					this.asynchExceptions.put(exceptionClass, handler);
				}catch(Exception e){
					//e.printStackTrace();
				}
			}
		}
	}

	public void findVariableUsageInBlcok(){
		
		CtBlock<?> block = (CtBlock<?>)element.getParent();
		String statement = null;
		List<CtExpression<?>> invocationArguments = null;
		List<CtExpression<?>> modifiedInvocationArguments = null;
		
		String regex = "\\b" + variableName + "\\b";
		Pattern pattern = Pattern.compile(regex);
				
		List<CtStatement> variableAccessStatements = SpoonUtils.findVarAccessOtherThanFutureDefinition(block, (CtLocalVariable<?>)element);
		
		for (CtStatement variableAccessStatement : variableAccessStatements) {
			boolean invocation = false; boolean localVariable = false; boolean cdSnippetSt = false;
			
			System.out.println("--------------------------------------------");
			System.out.println("statement: " + variableAccessStatement.toString() + ", and statement type: " + variableAccessStatement.getClass());
			
			if (variableAccessStatement instanceof CtLocalVariableImpl<?>){
				localVariable = true;
				CtLocalVariableImpl<?> variableImpl = (CtLocalVariableImpl<?>) variableAccessStatement;
				statement = variableImpl.getDefaultExpression().toString();
			}
			
			else if (variableAccessStatement instanceof CtCodeSnippetStatementImpl){
				cdSnippetSt = true;
				CtCodeSnippetStatementImpl codeSnippetStatement = (CtCodeSnippetStatementImpl) variableAccessStatement;
				statement = codeSnippetStatement.getValue();
			}
			else if (variableAccessStatement instanceof CtInvocationImpl<?>){
				invocation = true;
				invocationArguments = new ArrayList<>();
				modifiedInvocationArguments = new ArrayList<>();
				CtInvocationImpl<?> invocationImp = (CtInvocationImpl<?>) variableAccessStatement;
				statement = invocationImp.getExecutable().getSimpleName();
				invocationArguments = invocationImp.getArguments();				
			}
			
			if (invocation){
				for (CtExpression<?> argument : invocationArguments){
					Matcher matcher = pattern.matcher(argument.toString());
					String temp = matcher.replaceAll(taskIDName+".getResult()");
					CtCodeSnippetExpression<?> tempArg = getFactory().Core().createCodeSnippetExpression();
					tempArg.setValue(temp);
					modifiedInvocationArguments.add(tempArg);
				}
			}
			
			else {
				Matcher matcher = pattern.matcher(statement);
				statement = matcher.replaceAll(taskIDName+".getResult()");
			}
			
			if (variableAccessStatement instanceof CtLocalVariableImpl<?>){
				CtLocalVariableImpl<?> variableImpl = (CtLocalVariableImpl<?>) variableAccessStatement;
				CtCodeSnippetExpression<?> codeSnippet = getFactory().Core().createCodeSnippetExpression();
				codeSnippet.setValue(statement);
				variableImpl.setDefaultExpression((CtExpression)codeSnippet);
			}
			else if (variableAccessStatement instanceof CtCodeSnippetStatementImpl){
				CtCodeSnippetStatementImpl codeSnippetStatement = (CtCodeSnippetStatementImpl) variableAccessStatement;
				codeSnippetStatement.setValue(statement);
			}
			else if (variableAccessStatement instanceof CtInvocationImpl<?>){
				
				CtInvocationImpl<?> invocationImp = (CtInvocationImpl<?>) variableAccessStatement;
				invocationImp.setArguments(modifiedInvocationArguments);
			}
		}
				
//		CtCodeSnippetStatement newStatement = new CtFutureDefCodeSnippetStatement(variableName, getFactory().Core().getMainFactory());
//		newStatement.setValue(paraTaskExpression.toString());
//		SpoonUtils.replace(block, (CtStatement)element, newStatement);
//		
//		System.out.println("\n\n\n");
	
	}
	
	/**
	 * This method finds the parameters that are sent as
	 * arguments, to the invocations that exist in an 
	 * expression.
	 * 
	 */
	public void processInvocations(){
		for(CtInvocationImpl<?> invocation : invocations)
			processInvocation(invocation);
	}
	
	/**
	 * This method finds the parameters that are sent
	 * as arguments, to one invocation!
	 * @param invocation
	 */
	public void processInvocation(CtInvocationImpl<?> invocation){
		
		
		List<CtExpression<?>> arguments = invocation.getArguments();
		String invocationExp = invocation.toString();
		String invocationSignature = invocation.getSignature();
		
		System.out.println("********************PROCESS INVOCATION*************************");
		System.out.println("getExecutable: " + invocation.getExecutable());
		System.out.println("invocation.toString: " + invocationExp);
		System.out.println("invocation.getSignature: " + invocationSignature);
		System.out.println("********************PROCESS INVOCATION*************************");
		
		lambdaExpressionArguments = new ArrayList<>();
		
		/* Figure out if there are any arguments that we need to remember
		 * as dependencies, or arguments to our lambda expression.*/
		for(CtExpression<?> argument : arguments) {
			processArgument(argument);
		}
			
	}
	
	
	
	/**
	 * This method finds the parameters that are sent
	 * as arguments, to one invocation!
	 * @param invocation
	 */
	public void _processThisInvocation(CtInvocationImpl<?> invocation){
		
		
		System.out.println("getExecutable: " + invocation.getExecutable());
		
		List<CtExpression<?>> callArgs = invocation.getArguments();
		String invocationExp = invocation.toString();
		
		//First that needs to be changed is that the taskID type depends on the number of arguments. 
		if (callArgs == null || callArgs.isEmpty()) {
			paraTaskExpression.append("pt.runtime.TaskID<" + SpoonUtils.getType(returnType) + "> ")
				.append(taskIDName).append(" = ")
				.append("pt.runtime.Task.asTask(() -> ").append(invocationExp).append(")");
		} else {
			String lambdaArgs = new String();
			List<String> argLs = new ArrayList<String>();
			for(CtExpression<?> e : callArgs) {
				System.out.println("arg type: " + e.getClass());
				
				if(!(e instanceof CtVariableAccessImpl))
					continue;
				
				CtVariableAccessImpl<?> argVarAccess = (CtVariableAccessImpl<?>) e;
				
				System.out.println("argVarAccess: " + argVarAccess + " type:" + argVarAccess.getType());
				
				String argName = argVarAccess.toString();
				
				System.out.println("%%%%% isFutureArgument " + SpoonUtils.isFutureArgument(element, argName));
				
				if(SpoonUtils.isFutureArgument(element, argName)) {
					argLs.add(argName);
				}
				
				if(!lambdaArgs.isEmpty())
					lambdaArgs += ", ";
					
				lambdaArgs += SpoonUtils.getLambdaArgName(argName);
			
				invocationExp = invocationExp.replace(argName, "(" + argVarAccess.getType() + ")" + SpoonUtils.getLambdaArgName(argName));
				
			}
			
			paraTaskExpression.append("pt.runtime.TaskID<" + SpoonUtils.getType(returnType) + "> ")
			.append(taskIDName).append(" = ")
			.append("pt.runtime.Task.asTask((" + lambdaArgs
					+ ") -> ").append(invocationExp).append(")");
			
			for(String fArg : argLs) {
				paraTaskExpression.append(".dependsOnAsArg(" + SpoonUtils.getTaskIDName(fArg) + ")");
			}
		}
		
		Set<CtAnnotation<? extends Annotation>> ats = element.getAnnotations();
		
		for(CtAnnotation<? extends Annotation> a : ats) {
			//System.out.println("@@ Got " + a.toString());
			Annotation anno = a.getActualAnnotation();
			//System.out.println("@@ getActualAnnotation: " + anno);
			
			if(anno instanceof Future) {
				Future f = (Future)anno;
				//System.out.println("it is a Future: " + f);
				
				String depends = f.depends();
				//System.out.println("depends: " + depends);
				
				String notifies = f.notifies();
				//System.out.println("notifies: " + notifies);
				
				if (!depends.isEmpty()) {
					String[] deps = depends.split(",");
					for(String dep : deps) {
						paraTaskExpression.append(".dependsOn(" + SpoonUtils.getTaskIDName(dep) + ")");
					}
				}
				
				/*
				 * It is better not to permit method references for notifies, rather we can
				 * ordinary method calls for the lambda expression that is sent to a handler.
				 * Unless we want to force that all handler methods have no arguments at all!
				 */
				if (!notifies.isEmpty()) {
					String[] handlers = notifies.split("(\\s)*;(\\s)*");
					for (String handler : handlers) {
						//System.out.println("handler: " + handler);
						//just define a slot as: "()->handler" - print this out, to see what we get!
						Pattern voidHandler = Pattern.compile("([^:]+::)?([a-zA-Z0-9_]+)(\\(\\s*\\))?");
						
						Pattern handlerWithArg = Pattern.compile("([^:]+::)?([a-zA-Z0-9_]+)(\\(\\s*\\?\\s*\\))");
												
						Matcher mVoidHandler = voidHandler.matcher(handler);
						Matcher mHandlerWithArg = handlerWithArg.matcher(handler);
						
						if(mVoidHandler.matches()) {
							String obj = mVoidHandler.group(1);
							String method = mVoidHandler.group(2);
							if(obj == null)
								obj = "this::";
							
							//System.out.println("handler method: " + method + "  obj: " + obj);
							paraTaskExpression.append(".withHandler(" + obj + method + ")");
						} else if(mHandlerWithArg.matches()) {
							String obj = mHandlerWithArg.group(1);
							String method = mHandlerWithArg.group(2);
							if(obj == null)
								obj = "this::";
							obj = obj.substring(0, obj.length() - 2);
							
							//System.out.println("handler method: " + method + "  obj: " + obj);
							
							//.withHandler(future -> panel.setImageTask((TaskID<ImageCombo>)future))
							paraTaskExpression.append(".withPipelineHandler(future -> " + obj + "." +
									method + "(future))");
						}
						
					}
				}
			}
			
			paraTaskExpression.append(".start()");
		}
		
		CtBlock<?> block = (CtBlock<?>)element.getParent();
		
//		CtStatement access = SpoonUtils.findVarAccessOtherThanFutureDefinition(block, (CtLocalVariable<?>)element);
//		if (access != null) {
//			String finalResult = "";
//			Set<ModifierKind> mods = element.getModifiers();
//			if (!mods.contains(ModifierKind.FINAL)) {
//				finalResult += "final ";
//			}
//			
//			finalResult += returnType + " " + variableName + " = "
//					+ SpoonUtils.getTaskIDName(variableName) + ".getResult()";
//			
//			CtCodeSnippetStatement finalResultStatement = getFactory().Core()
//					.createCodeSnippetStatement();
//			finalResultStatement.setValue(finalResult);
//			
//			access.insertBefore(finalResultStatement);
//		}
				
		CtCodeSnippetStatement newStatement = new CtFutureDefCodeSnippetStatement(variableName, getFactory().Core().getMainFactory());
		newStatement.setValue(paraTaskExpression.toString());
		SpoonUtils.replace(block, (CtStatement)element, newStatement);
		
		System.out.println("\n\n\n");
	}
	
}	