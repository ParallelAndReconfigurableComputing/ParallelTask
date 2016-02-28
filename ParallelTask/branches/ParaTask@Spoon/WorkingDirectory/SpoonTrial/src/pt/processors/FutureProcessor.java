package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.annotations.Future;
import pt.runtime.ParaTask;
import pt.runtime.TaskInfo;
import pt.runtime.TaskInfoNoArgs;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ModifierKind;
import spoon.support.reflect.code.CtBinaryOperatorImpl;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.code.CtVariableAccessImpl;


public class FutureProcessor extends
		AbstractAnnotationProcessor<Future, CtVariable<?>> {
	
	List<CtInvocationImpl<?>> invocations = null;
	List<String> arguments = null;
	String taskIDName = null;
	String returnType = null;
	String variableName = null;
	StringBuilder paraTaskExpression = null;
	CtVariable<?> element = null;
	
	
	public void process(Future annotation, CtVariable<?> element) {
		
		System.out.println("Current element: \n" + element.toString());
		
		this.element = element;
		
		variableName = element.getSimpleName();
		
		taskIDName = SpoonUtils.getFutureName(variableName);
		
		returnType = element.getType().getSimpleName();
				
		CtExpression<?> defaultExpression = element.getDefaultExpression();
		
		System.out.println("----------------------------------------------------------------------");
		System.out.println("Element simple name: " + variableName);
		System.out.println("getDefaultExpression.getClass: " + defaultExpression.getClass());
		System.out.println("And the expression is (toString): " + defaultExpression.toString());
		System.out.println("Return type is: " + returnType);
		System.out.println("----------------------------------------------------------------------");
		
		invocations = new ArrayList<>();
		listInvocationsOfThisExp(defaultExpression);
		
		if(invocations.isEmpty()) {
			throw new RuntimeException("FUTURE ANNOTATION HAS NOT BEEN USED FOR A VALID EXPRESSION!\n"
					+ "THE EXPRESSION MUST EITHER BE AN INVOCATION, OR A BINARY EXPRESSINO THAT CONTAINS AN INVOCATION!\n"
					+ "YOUR EXPRESSION: " + defaultExpression.toString());
			
		}
		
		paraTaskExpression = new StringBuilder();
		processInvocations();
	
	}


	public boolean isExpressionValid(CtExpression<?> expression){
		if (expression instanceof CtInvocationImpl<?>)
			return true;
		if (expression instanceof CtBinaryOperatorImpl<?>){
			if(isExpressionValid(((CtBinaryOperatorImpl<?>) expression).getLeftHandOperand()))
				return true;
			
			if(isExpressionValid(((CtBinaryOperatorImpl<?>) expression).getRightHandOperand()))
				return true;
		}
			
		return false;
	}
	
	public void listInvocationsOfThisExp(CtExpression<?> expression){
		if (expression instanceof CtInvocationImpl<?>){
			invocations.add((CtInvocationImpl<?>)expression);
			return;
		}
		else if (expression instanceof CtBinaryOperatorImpl<?>){
			listInvocationsOfThisExp(((CtBinaryOperatorImpl<?>) expression).getLeftHandOperand());
			listInvocationsOfThisExp(((CtBinaryOperatorImpl<?>) expression).getRightHandOperand());
		}
		return;
	}
	
	/**
	 * This method finds the parameters that are sent as
	 * arguments, to the invocations that exist in an 
	 * expression.
	 * 
	 */
	public void processInvocations(){
		for(CtInvocationImpl<?> invocation : invocations)
			processThisInvocation(invocation);
	}
	
	/**
	 * This method finds the parameters that are sent
	 * as arguments, to one invocation!
	 * @param invocation
	 */
	public void processThisInvocation(CtInvocationImpl<?> invocation){
		
		
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
				paraTaskExpression.append(".dependsOnAsArg(" + SpoonUtils.getFutureName(fArg) + ")");
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
						paraTaskExpression.append(".dependsOn(" + SpoonUtils.getFutureName(dep) + ")");
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
		
		CtStatement access = SpoonUtils.findVarAccessOtherThanFutureDefinition(block, (CtLocalVariable<?>)element);
		if (access != null) {
			String finalResult = "";
			Set<ModifierKind> mods = element.getModifiers();
			if (!mods.contains(ModifierKind.FINAL)) {
				finalResult += "final ";
			}
			
			finalResult += returnType + " " + variableName + " = "
					+ SpoonUtils.getFutureName(variableName) + ".getResult()";
			
			CtCodeSnippetStatement finalResultStatement = getFactory().Core()
					.createCodeSnippetStatement();
			finalResultStatement.setValue(finalResult);
			
			access.insertBefore(finalResultStatement);
		}
				
		CtCodeSnippetStatement newStatement = new CtFutureDefCodeSnippetStatement(variableName, getFactory().Core().getMainFactory());
		newStatement.setValue(paraTaskExpression.toString());
		SpoonUtils.replace(block, (CtStatement)element, newStatement);
		
		System.out.println("\n\n\n");
	}
	
}	