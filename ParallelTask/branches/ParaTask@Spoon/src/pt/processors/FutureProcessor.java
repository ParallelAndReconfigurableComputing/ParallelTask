package pt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.annotations.Future;

import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.declaration.ModifierKind;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtVariableAccessImpl;

public class FutureProcessor extends
		AbstractAnnotationProcessor<Future, CtVariable<?>> {
	
	public void process(Future annotation, CtVariable<?> element) {
		System.out.println("Current element: \n" + element.toString());
		
		String name = element.getSimpleName();
		String futureName = SpoonUtils.getFutureName(name);
		
		String type = element.getType().getSimpleName();
		
		CtExpression<?> defaultExpression = element.getDefaultExpression();
		System.out.println("getDefaultExpression.getClass: " + defaultExpression.getClass());

		if(!(defaultExpression instanceof CtInvocationImpl)) {
			throw new RuntimeException("@Future can only be used to define a variable via method invocation");
		}
		
		StringBuilder newExp = new StringBuilder();
		
		CtInvocationImpl<?> call = (CtInvocationImpl<?>) defaultExpression;
		
		System.out.println("getExecutable: " + call.getExecutable());
		
		List<CtExpression<?>> callArgs = call.getArguments();
		String defaultExp = defaultExpression.toString();
		
		if (callArgs == null || callArgs.isEmpty()) {
			newExp.append("pt.runtime.TaskID<" + SpoonUtils.getType(type) + "> ")
				.append(futureName).append(" = ")
				.append("pt.runtime.Task.asTask(() -> ").append(defaultExp).append(")");
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
					
					if(!lambdaArgs.isEmpty())
						lambdaArgs += ", ";
					
					lambdaArgs += SpoonUtils.getLambdaArgName(argName);
			
					//TODO
					defaultExp = defaultExp.replace(argName, "(" + argVarAccess.getType() + ")" + SpoonUtils.getLambdaArgName(argName));
				}
				
			}
			
			newExp.append("pt.runtime.TaskID<" + SpoonUtils.getType(type) + "> ")
			.append(futureName).append(" = ")
			.append("pt.runtime.Task.asTask((" + lambdaArgs
					+ ") -> ").append(defaultExp).append(")");
			
			for(String fArg : argLs) {
				newExp.append(".dependsOnAsArg(" + SpoonUtils.getFutureName(fArg) + ")");
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
						newExp.append(".dependsOn(" + SpoonUtils.getFutureName(dep) + ")");
					}
				}
				
				if (!notifies.isEmpty()) {
					String[] handlers = notifies.split("(\\s)*;(\\s)*");
					for (String handler : handlers) {
						//System.out.println("handler: " + handler);
						
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
							newExp.append(".withHandler(" + obj + method + ")");
						} else if(mHandlerWithArg.matches()) {
							String obj = mHandlerWithArg.group(1);
							String method = mHandlerWithArg.group(2);
							if(obj == null)
								obj = "this::";
							obj = obj.substring(0, obj.length() - 2);
							
							//System.out.println("handler method: " + method + "  obj: " + obj);
							
							//.withHandler(future -> panel.setImageTask((TaskID<ImageCombo>)future))
							newExp.append(".withPipelineHandler(future -> " + obj + "." +
									method + "(future))");
						}
						
					}
				}
			}
			
			newExp.append(".start()");
		}
		
		CtBlock<?> block = (CtBlock<?>)element.getParent();
		
		CtStatement access = SpoonUtils.findVarAccessOtherThanFutureDefinition(block, (CtLocalVariable<?>)element);
		if (access != null) {
			String finalResult = "";
			Set<ModifierKind> mods = element.getModifiers();
			if (!mods.contains(ModifierKind.FINAL)) {
				finalResult += "final ";
			}
			
			finalResult += type + " " + name + " = "
					+ SpoonUtils.getFutureName(name) + ".getResult()";
			
			CtCodeSnippetStatement finalResultStatement = getFactory().Core()
					.createCodeSnippetStatement();
			finalResultStatement.setValue(finalResult);
			
			access.insertBefore(finalResultStatement);
		}
				
		CtCodeSnippetStatement newStatement = new CtFutureDefCodeSnippetStatement(name, getFactory().Core().getMainFactory());
		newStatement.setValue(newExp.toString());
		SpoonUtils.replace(block, (CtStatement)element, newStatement);
		
		System.out.println("\n\n\n");
	}
}