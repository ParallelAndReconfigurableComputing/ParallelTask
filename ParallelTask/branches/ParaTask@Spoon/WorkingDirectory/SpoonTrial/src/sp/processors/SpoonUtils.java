package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sp.annotations.TaskInfoType;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtVariableReference;
import spoon.support.reflect.code.CtArrayAccessImpl;
import spoon.support.reflect.code.CtAssertImpl;
import spoon.support.reflect.code.CtAssignmentImpl;
import spoon.support.reflect.code.CtBinaryOperatorImpl;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtBreakImpl;
import spoon.support.reflect.code.CtCaseImpl;
import spoon.support.reflect.code.CtConditionalImpl;
import spoon.support.reflect.code.CtContinueImpl;
import spoon.support.reflect.code.CtDoImpl;
import spoon.support.reflect.code.CtForEachImpl;
import spoon.support.reflect.code.CtForImpl;
import spoon.support.reflect.code.CtIfImpl;
import spoon.support.reflect.code.CtInvocationImpl;
import spoon.support.reflect.code.CtLiteralImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.code.CtLoopImpl;
import spoon.support.reflect.code.CtNewArrayImpl;
import spoon.support.reflect.code.CtOperatorAssignmentImpl;
import spoon.support.reflect.code.CtReturnImpl;
import spoon.support.reflect.code.CtSwitchImpl;
import spoon.support.reflect.code.CtSynchronizedImpl;
import spoon.support.reflect.code.CtTargetedExpressionImpl;
import spoon.support.reflect.code.CtThrowImpl;
import spoon.support.reflect.code.CtTryImpl;
import spoon.support.reflect.code.CtUnaryOperatorImpl;
import spoon.support.reflect.code.CtVariableAccessImpl;
import spoon.support.reflect.code.CtWhileImpl;

public class SpoonUtils {

	public static enum ExpressionRole{Assigned, Assignment, ThrowExpression, ReturnExpression, 
		WhileLoopExpression, DoLoopExpression, ForLoopExpression, ForEachExpression, IfConditionExpression,
		AssertEvaluationExpression, AssertActualExpression, CaseExpression, LocalVarDefaultExpression,
		SwitchSelectorExpression, InvocationArgumentExpression, InvocationTargetExpression};
		
	private static Map<String, String> primitiveMap = new HashMap<String, String>();
	private static Map<CtStatement, Map<CtExpression<?>, ExpressionRole>> mapOfExpressions = null;

	static {
		primitiveMap.put("int", "Integer");
		primitiveMap.put("double", "Double");
		primitiveMap.put("boolean", "Boolean");
		primitiveMap.put("float", "Float");
		primitiveMap.put("byte", "Byte");
	}

	public static String getType(String type) {
		String newType = primitiveMap.get(type);
		if (newType != null)
			return newType;
		return type;
	}
	
	public static String getReturnType(String type){
		if (type.contains(",")){
			//this is a series of generic types from
			//TaskInfo or TaskID. The first one is return type.
			String[] types = type.split(",");
			type = types[0].trim();
		}
		else
			type = getType(type);
		return type;
	}
	
	public static void modifyStatements(List<CtStatement> statements, String regex, String replacement){
		List<CtExpression<?>> expressionsToModify = null;
		List<CtStatement> statementsToModify = null;
		
		for (CtStatement statement : statements){
			
			expressionsToModify = new ArrayList<>();
			statementsToModify = new ArrayList<>();
			
			if (statement instanceof CtBlockImpl<?>){
				CtBlockImpl<?> blockStatement = (CtBlockImpl<?>) statement;
				statementsToModify = blockStatement.getStatements();
				modifyStatements(statementsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtAssignmentImpl<?, ?>){
				
					CtAssignmentImpl<?, ?> assignmentImpl = null;
					
				if (statement instanceof CtOperatorAssignmentImpl<?, ?>){
					assignmentImpl = (CtOperatorAssignmentImpl<?, ?>) statement;
				}
				else{
					assignmentImpl = (CtAssignmentImpl<?, ?>) statement;
				}
				expressionsToModify.add(assignmentImpl.getAssignment());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtThrowImpl){
				CtThrowImpl throwImpl = (CtThrowImpl) statement;
				expressionsToModify.add(throwImpl.getThrownExpression());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtReturnImpl<?>){
				CtReturnImpl<?> returnImp = (CtReturnImpl<?>) statement;
				expressionsToModify.add(returnImp.getReturnedExpression());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtLoopImpl){
			
				if (statement instanceof CtWhileImpl){
					CtWhileImpl whileImpl = (CtWhileImpl) statement;
					expressionsToModify.add(whileImpl.getLoopingExpression());
					modifyExpressions(expressionsToModify, regex, replacement);
					statementsToModify.add(whileImpl.getBody());
					modifyStatements(statementsToModify, regex, replacement);
				}
				
				else if (statement instanceof CtDoImpl){
					CtDoImpl doImpl = (CtDoImpl) statement;
					expressionsToModify.add(doImpl.getLoopingExpression());
					modifyExpressions(expressionsToModify, regex, replacement);
					statementsToModify.add(doImpl.getBody());
					modifyStatements(statementsToModify, regex, replacement);
				}
				
				else if (statement instanceof CtForImpl){
					CtForImpl forImpl = (CtForImpl) statement;
					expressionsToModify.add(forImpl.getExpression());
					modifyExpressions(expressionsToModify, regex, replacement);
					statementsToModify.add(forImpl.getBody());
					statementsToModify.addAll(forImpl.getForInit());
					statementsToModify.addAll(forImpl.getForUpdate());
					modifyStatements(statementsToModify, regex, replacement);
				}
				
				else if (statement instanceof CtForEachImpl){
					CtForEachImpl forEachImpl = (CtForEachImpl) statement;
					expressionsToModify.add(forEachImpl.getExpression());
					modifyExpressions(expressionsToModify, regex, replacement);
					statementsToModify.add(forEachImpl.getBody());
					modifyStatements(statementsToModify, regex, replacement);
				}
			}
			
			else if (statement instanceof CtIfImpl){
				CtIfImpl ifImpl = (CtIfImpl) statement;
				expressionsToModify.add(ifImpl.getCondition());
				modifyExpressions(expressionsToModify, regex, replacement);
				statementsToModify.add(ifImpl.getThenStatement());
				statementsToModify.add(ifImpl.getElseStatement());
				modifyStatements(statementsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtAssertImpl<?>){
				CtAssertImpl<?> assertImpl = (CtAssertImpl<?>) statement;
				expressionsToModify.add(assertImpl.getAssertExpression());
				expressionsToModify.add(assertImpl.getExpression());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtCaseImpl<?>){
				CtCaseImpl<?> caseImpl = (CtCaseImpl<?>) statement;
				expressionsToModify.add(caseImpl.getCaseExpression());
				modifyExpressions(expressionsToModify, regex, replacement);
				statementsToModify.addAll(caseImpl.getStatements());
				modifyStatements(statementsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtContinueImpl){
				//AT THIS STAGE DO NOTHING FOR A CONTINUE STATEMENT
			}
			
			else if (statement instanceof CtLocalVariableImpl<?>){
				CtLocalVariableImpl<?> varImpl = (CtLocalVariableImpl<?>) statement;
				expressionsToModify.add(varImpl.getDefaultExpression());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtSynchronizedImpl){
				CtSynchronizedImpl syncImpl = (CtSynchronizedImpl) statement;
				/*because the expressions in synchronized statements specify the monitoring
				  object, there is no need to change the object!*/
				statementsToModify.add(syncImpl.getBlock());
				modifyStatements(statementsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtTryImpl){
				//if (statement instanceof CtTryWithResource){} for new version of spoon!
				CtTryImpl tryImpl = (CtTryImpl) statement;
				statementsToModify.add(tryImpl.getBody());
				statementsToModify.add(tryImpl.getFinalizer());
				List<CtCatch> catchers = tryImpl.getCatchers();
				for (CtCatch catcher : catchers){
					statementsToModify.add(catcher.getBody());
				}
				modifyStatements(statementsToModify, regex, replacement);
			}
			else if (statement instanceof CtBreakImpl){
				//AT THIS STAGE DO NOTHING FOR A BREAK STATEMENT
			}
			
			else if (statement instanceof CtSwitchImpl<?>){
				CtSwitchImpl<?> switchImpl = (CtSwitchImpl<?>) statement;
				expressionsToModify.add(switchImpl.getSelector());
				modifyExpressions(expressionsToModify, regex, replacement);
				statementsToModify.addAll(switchImpl.getCases());
				modifyStatements(statementsToModify, regex, replacement);
			}
			
			else if (statement instanceof CtInvocationImpl<?>){
				CtInvocationImpl<?> invocImpl = (CtInvocationImpl<?>) statement;
				expressionsToModify.addAll(invocImpl.getArguments());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
		}
	}

	public static void modifyExpressions(List<CtExpression<?>> expressions, String regex, String replacement){
		String statement = null;
		Pattern pattern = Pattern.compile(regex);
		List<CtExpression<?>> expressionsToModify = null;
		
		for (CtExpression<?> expression : expressions){
			
			expressionsToModify = new ArrayList<>();
			
			if(expression instanceof CtTargetedExpressionImpl<?, ?>){
				
				if (expression instanceof CtArrayAccessImpl<?, ?>){
//					if (expression instanceof CtArrayReadImple<?>){} 		supported for new version of spoon!
//					else if (expression instanceof CtArrayWriteImpl<?>){}
					CtArrayAccessImpl<?, ?> arrayAccess = (CtArrayAccessImpl<?, ?>) expression;
					expressionsToModify.add(arrayAccess.getIndexExpression());
					modifyExpressions(expressionsToModify, regex, replacement);
				}
				
//				else if (expression instanceof CtConstructorCallImpl<?>){
//					if (expression instanceof CtNewClassImpl<?>){
//					
//					}	
//				} supported for new spoon!
				
//				else if (expression instanceof CtExecutableReferenceExpressionImpl){
//					
//				} supported for new spoon!
				
				else if (expression instanceof CtInvocationImpl<?>){
					CtInvocationImpl<?> invocation = (CtInvocationImpl<?>) expression;
					expressionsToModify.addAll(invocation.getArguments());
					modifyExpressions(expressionsToModify, regex, replacement);
				}
				
//				else if (expression instanceof CtThisAccessImple<?>){}		 supported for the new version of sppon!
			}
			
			else if (expression instanceof CtUnaryOperatorImpl<?>){
				CtUnaryOperatorImpl<?> unaryOp = (CtUnaryOperatorImpl<?>) expression;
				expressionsToModify.add(unaryOp.getOperand());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
			
			else if (expression instanceof CtBinaryOperatorImpl<?>){
				CtBinaryOperatorImpl<?> binaryOp = (CtBinaryOperatorImpl<?>) expression;
				expressionsToModify.add(binaryOp.getLeftHandOperand());
				expressionsToModify.add(binaryOp.getRightHandOperand());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
			
			else if (expression instanceof CtVariableAccessImpl<?>){
//				if (expression instanceof CtVariableReadImpl<?>){
//					if (expression instanceof CtSuperAccessImpl<?>){}	
//					else if (expression instanceof CtFieldAccessImpl<?>){
//						if (expression instanceof CtAnnotationFieldAccessImpl<?>){}			supported for new version of spoon!
//						else if (expression instanceof CtFieldReadImpl<?>){}
//				        else if (expression instanceof CtFieldWriteImpl<?>){}
//			
//					}
//				}           supported for the new version of spoon!
//				else if (expression instanceof CtVariableWriteImpl<?>){}
				CtVariableAccessImpl<?> varAccess = (CtVariableAccessImpl<?>) expression;
				CtVariableReference<?> varReference = varAccess.getVariable();
				String newName = replacePatternInSource(varReference.getSimpleName(), regex, replacement);
				varReference.setSimpleName(newName);
//				expression.replace(varAccess);
			}
			
			else if (expression instanceof CtConditionalImpl<?>){
				CtConditionalImpl<?> condImpl = (CtConditionalImpl<?>) expression;
				expressionsToModify.add(condImpl.getCondition());
				expressionsToModify.add(condImpl.getElseExpression());
				expressionsToModify.add(condImpl.getThenExpression());
				modifyExpressions(expressionsToModify, regex, replacement);
			}
			else if (expression instanceof CtNewArrayImpl<?>){
				
			}
			
//			else if (expression instanceof CtLambdaImpl<?>){
//			
//			}					supported for new version of spoon
			
			else if (expression instanceof CtLiteralImpl<?>){
				//DON'T DO ANYTHING FOR LITERALS AT THIS STAGE
			}
		}
	}
	
	public static Map<CtStatement, Map<CtExpression<?>, ExpressionRole>> listAllExpressionsOfStatements(List<CtStatement> statements){
		mapOfExpressions = new HashMap<>();
		listAllExpressions(statements);
		return mapOfExpressions;
	}
	
	private static void listAllExpressions(List<CtStatement> statements){
		List<CtStatement> statementsToInspect = null;
		Map<CtExpression<?>, ExpressionRole> statementExpressions = null;
		
		for (CtStatement statement : statements){
			
			statementsToInspect = new ArrayList<>();
			statementExpressions = new HashMap<>();
			
			if (statement instanceof CtBlockImpl<?>){
				CtBlockImpl<?> blockStatement = (CtBlockImpl<?>) statement;
				statementsToInspect = blockStatement.getStatements();
				listAllExpressions(statementsToInspect);
			}
			
			else if (statement instanceof CtAssignmentImpl<?, ?>){
				
					CtAssignmentImpl<?, ?> assignmentImpl = null;
					
				if (statement instanceof CtOperatorAssignmentImpl<?, ?>){
					assignmentImpl = (CtOperatorAssignmentImpl<?, ?>) statement;
				}
				else{
					assignmentImpl = (CtAssignmentImpl<?, ?>) statement;
				}
				statementExpressions.put(assignmentImpl.getAssigned(), ExpressionRole.Assigned);
				statementExpressions.put(assignmentImpl.getAssignment(), ExpressionRole.Assignment);
				mapOfExpressions.put(assignmentImpl, statementExpressions);
			}
			
			else if (statement instanceof CtThrowImpl){
				CtThrowImpl throwImpl = (CtThrowImpl) statement;
				statementExpressions.put(throwImpl.getThrownExpression(), ExpressionRole.ThrowExpression);
				mapOfExpressions.put(throwImpl, statementExpressions);
			}
			
			else if (statement instanceof CtReturnImpl<?>){
				CtReturnImpl<?> returnImp = (CtReturnImpl<?>) statement;
				statementExpressions.put(returnImp.getReturnedExpression(), ExpressionRole.ReturnExpression);
				mapOfExpressions.put(returnImp, statementExpressions);
			}
			
			else if (statement instanceof CtLoopImpl){
			
				if (statement instanceof CtWhileImpl){
					CtWhileImpl whileImpl = (CtWhileImpl) statement;
					statementExpressions.put(whileImpl.getLoopingExpression(), ExpressionRole.WhileLoopExpression);
					mapOfExpressions.put(whileImpl, statementExpressions);
					statementsToInspect.add(whileImpl.getBody());
					listAllExpressions(statementsToInspect);
				}
				
				else if (statement instanceof CtDoImpl){
					CtDoImpl doImpl = (CtDoImpl) statement;
					statementExpressions.put(doImpl.getLoopingExpression(), ExpressionRole.DoLoopExpression);
					mapOfExpressions.put(doImpl, statementExpressions);
					statementsToInspect.add(doImpl.getBody());
					listAllExpressions(statementsToInspect);
				}
				
				else if (statement instanceof CtForImpl){
					CtForImpl forImpl = (CtForImpl) statement;
					statementExpressions.put(forImpl.getExpression(), ExpressionRole.ForLoopExpression);
					mapOfExpressions.put(forImpl, statementExpressions);
					statementsToInspect.add(forImpl.getBody());
					statementsToInspect.addAll(forImpl.getForInit());
					statementsToInspect.addAll(forImpl.getForUpdate());
					listAllExpressions(statementsToInspect);
				}
				
				else if (statement instanceof CtForEachImpl){
					CtForEachImpl forEachImpl = (CtForEachImpl) statement;
					statementExpressions.put(forEachImpl.getExpression(), ExpressionRole.ForEachExpression);
					mapOfExpressions.put(forEachImpl, statementExpressions);
					statementsToInspect.add(forEachImpl.getBody());
					listAllExpressions(statementsToInspect);
				}
			}
			
			else if (statement instanceof CtIfImpl){
				CtIfImpl ifImpl = (CtIfImpl) statement;
				statementExpressions.put(ifImpl.getCondition(), ExpressionRole.IfConditionExpression);
				mapOfExpressions.put(ifImpl, statementExpressions);
				statementsToInspect.add(ifImpl.getThenStatement());
				statementsToInspect.add(ifImpl.getElseStatement());
				listAllExpressions(statementsToInspect);
			}
			
			else if (statement instanceof CtAssertImpl<?>){
				CtAssertImpl<?> assertImpl = (CtAssertImpl<?>) statement;
				statementExpressions.put(assertImpl.getAssertExpression(), ExpressionRole.AssertEvaluationExpression);
				statementExpressions.put(assertImpl.getExpression(), ExpressionRole.AssertActualExpression);
				mapOfExpressions.put(assertImpl, statementExpressions);
			}
			
			else if (statement instanceof CtCaseImpl<?>){
				CtCaseImpl<?> caseImpl = (CtCaseImpl<?>) statement;
				statementExpressions.put(caseImpl.getCaseExpression(), ExpressionRole.CaseExpression);
				mapOfExpressions.put(caseImpl, statementExpressions);
				statementsToInspect.addAll(caseImpl.getStatements());
				listAllExpressions(statementsToInspect);
			}
			
			else if (statement instanceof CtContinueImpl){
				//AT THIS STAGE DO NOTHING FOR A CONTINUE STATEMENT
			}
			
			else if (statement instanceof CtLocalVariableImpl<?>){
				CtLocalVariableImpl<?> varImpl = (CtLocalVariableImpl<?>) statement;
				statementExpressions.put(varImpl.getDefaultExpression(), ExpressionRole.LocalVarDefaultExpression);
				mapOfExpressions.put(varImpl, statementExpressions);
			}
			
			else if (statement instanceof CtSynchronizedImpl){
				CtSynchronizedImpl syncImpl = (CtSynchronizedImpl) statement;
				/*because the expressions in synchronized statements specify the monitoring
				  object, there is no need to change the object!*/
				statementsToInspect.add(syncImpl.getBlock());
				listAllExpressions(statementsToInspect);
			}
			
			else if (statement instanceof CtTryImpl){
				//if (statement instanceof CtTryWithResource){} for new version of spoon!
				CtTryImpl tryImpl = (CtTryImpl) statement;
				statementsToInspect.add(tryImpl.getBody());
				statementsToInspect.add(tryImpl.getFinalizer());
				List<CtCatch> catchers = tryImpl.getCatchers();
				for (CtCatch catcher : catchers){
					statementsToInspect.add(catcher.getBody());
				}
				listAllExpressions(statementsToInspect);
			}
			else if (statement instanceof CtBreakImpl){
				//AT THIS STAGE DO NOTHING FOR A BREAK STATEMENT
			}
			
			else if (statement instanceof CtSwitchImpl<?>){
				CtSwitchImpl<?> switchImpl = (CtSwitchImpl<?>) statement;
				statementExpressions.put(switchImpl.getSelector(), ExpressionRole.SwitchSelectorExpression);
				statementsToInspect.addAll(switchImpl.getCases());
				listAllExpressions(statementsToInspect);
			}
			
			else if (statement instanceof CtInvocationImpl<?>){
				CtInvocationImpl<?> invocImpl = (CtInvocationImpl<?>) statement;
				List<CtExpression<?>> arguments = invocImpl.getArguments();
				for(CtExpression<?> argument : arguments){
					statementExpressions.put(argument, ExpressionRole.InvocationArgumentExpression);
				}
				statementExpressions.put(invocImpl.getTarget(), ExpressionRole.InvocationTargetExpression);
				mapOfExpressions.put(invocImpl, statementExpressions);
			}
		}
	}

	
	public static String replacePatternInSource(String source, String patternRegex, String replacement){
		Pattern pattern = Pattern.compile(patternRegex);
		Matcher matcher = pattern.matcher(source);
		source = matcher.replaceAll(replacement);
		return source;
	}
	
	public static void replace(CtBlock<?> block, CtStatement old,
			CtStatement newStatement) {
		List<CtStatement> ls = block.getStatements();
		List<CtStatement> newLs = new ArrayList<CtStatement>();
		boolean replaced = false;
		for (CtStatement s : ls) {
			if (s == old) {
				newLs.add(newStatement);
				replaced = true;
			} else
				newLs.add(s);
		}		
		if (!replaced)
			throw new IllegalArgumentException("cannot find " + old
					+ " in the given block!");

		block.setStatements(newLs);
	}

	/**
	 * Finds the occurrence of a future variable within the block
	 * of that future variable. However, this only finds the first
	 * occurrence of that future variable, and not the others.
	 * @param block
	 * @param element
	 * @return
	 */
	public static List<CtStatement> findVarAccessOtherThanFutureDefinition(CtBlock<?> block,
			CtVariable<?> element) {
		List<CtStatement> blockStatements = block.getStatements();
		List<CtStatement> statementsWithThisFutureVariable = new ArrayList<>();
		boolean foundDef = false;
		
		String varName = element.getSimpleName();
		
		for (CtStatement statement : blockStatements) {
			
			if (statement instanceof CtBlock<?>){
				CtBlock<?> blockStatement = (CtBlock<?>) statement;
				findVarAccessOtherThanFutureDefinition(blockStatement, element);
			}
			
			else{
				String statementString = statement.toString();
					
				if (!foundDef){
					if (statement.equals(element))
						foundDef = true;
				}
				
				else {
					String regex = "\\b" + varName + "\\b";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(statementString);
					if(matcher.find()){
						statementsWithThisFutureVariable.add(statement);
					}
				}
			}
		}

		return statementsWithThisFutureVariable;
	}
	
	public static boolean hasAnnotation(CtStatement s, Class<?> clazz) {
		Set<CtAnnotation<? extends Annotation>> ats = s.getAnnotations();
		for(CtAnnotation<? extends Annotation> a : ats) {
			if(clazz.isInstance(a.getActualAnnotation()))
				return true;
		}
		return false;
	}
	
	/**
	 * Finds and returns the declaration statement of an argument that has
	 * been used within another variable declaration. 
	 * 
	 * @param element
	 * @param argName
	 * @return CtStatement declaring statement
	 */
	public static CtStatement getDeclarationStatement(CtVariable<?> element, String argName) {
		if (!argName.matches("[a-zA-Z0-9_]+")) {
			return null;
		}
		
		/*
		 * If the argument does not have the same name as the current declared element, which is being
		 * inspected, then its names is changed into a TaskName format iff it is a future variable. 
		 * */
		if(!element.getSimpleName().equals(argName))
			argName = getTaskName(argName);
		CtBlock<?> block = (CtBlock<?>)element.getParent();
		while(block != null){
			List<CtStatement> blockStatements = block.getStatements();
			
			for(CtStatement statement : blockStatements) {
				
				if(statement == element){
					if(element.getSimpleName().equals(argName))
						return statement;
					else
						break;
				}
				
				if (statement instanceof CtLocalVariable<?>){
					if(statement instanceof CtVariable<?>){
						CtVariable<?> variableDeclaration = (CtVariable<?>) statement;
						if(variableDeclaration.getSimpleName().equals(argName)){
							return statement;
						}
					}
				}
			}
			
			block = block.getParent(CtBlock.class);
		}
		return null;
	}
	
	
	/**
	 * Indicates if an argument that is used at the time of the declaration of another element
	 * is itself a future variable or not. 
	 * 
	 * @param element
	 * @param argName
	 * @return <code>true</code>, if the argument is a future variable, <code>false</code> otherwise. 
	 */
	public static boolean isFutureVariable(CtVariable<?> element, String argName){
		CtLocalVariable<?> declaration = (CtLocalVariable<?>)getDeclarationStatement(element, argName);
		if(declaration == null)
			return false;
		return true;
	}
	
	public static List<CtElement> getParents(CtElement element){
		List<CtElement> parents = new ArrayList<CtElement>();
		CtElement parent = element.getParent();
		while(parent != null){
			parents.add(parent);
			parent = parent.getParent();
		}
		return parents;
	}
	
	public static String getTaskIDName(String name) {
		return "__" + name + "PtTaskID__";
	}
	
	public static String getLambdaArgName(String name) {
		return "__" + name + "PtLambdaArg__";
	}
	
	public static String getTaskName(String name){
		return "__" + name + "PtTask__";
	}
	
	public static String getNonLambdaArgName(String name){
		return "__" + name + "PtNonLambdaArg__";
	}
	
	public static boolean isNonLambdaArg(String name){
		return (name.startsWith("__") && name.endsWith("NonLambdaArg__")) ? true : false;
	}
	
	public static boolean isLambdaArg(String name){
		return (name.startsWith("__") && name.endsWith("LambdaArg__")) ? true : false;
	}
	
	public static String getOrigName(String elementName) {
		
		if(elementName.startsWith("__") && elementName.endsWith("PtLambdaArg__"+getResultPhrase()))
			return elementName.substring("__".length(), (elementName.length() - ("PtLambdaArg__"+getResultPhrase()).length()));
		
		else if (elementName.startsWith("__") && elementName.endsWith("PtTaskID__"+getResultPhrase()))
			return elementName.substring("__".length(), (elementName.length() - ("PtTaskID__"+getResultPhrase()).length()));
		
		else if (elementName.startsWith("__") && elementName.endsWith("PtTask__"))
			return elementName.substring("__".length(), (elementName.length() - "PtTask__".length()));
		
		else if (elementName.startsWith("__") && elementName.endsWith("PtTaskID__"))
			return elementName.substring("__".length(), (elementName.length() - "PtTaskID__".length()));
		
		else if (elementName.startsWith("__") && elementName.endsWith("PtNonLambdaArg__"))
			return elementName.substring("__".length(), (elementName.length() - "PtNonLambdaArg__".length()));					
				
		else if (elementName.startsWith("__") && elementName.endsWith("PtLambdaArg__"))
			return elementName.substring("__".length(), (elementName.length() - "PtLambdaArg__".length()));
				
		else if (elementName.contains("<") && elementName.contains(">") && (elementName.indexOf("<") < elementName.lastIndexOf(">")))
			return elementName.substring(elementName.indexOf("<")+1, elementName.lastIndexOf(">"));
		
		return elementName;
	}
	
	public static boolean isTaskIDReplacement(CtVariable<?> element, String name){
		if(name.startsWith("__") && name.endsWith("__"+getResultPhrase())){
			String originalName = getOrigName(name);
			if(isFutureVariable(element, originalName)){
				return true;
			}
		}
		return false;
	}
	
	public static String getResultPhrase(){
		return ".getReturnResult()";
	}
	
	public static String getResultPhrase(int index){
		return ".getReturnResult(" + index + ")";
	}
	
	public static String getParaTaskSyntax(){
		return "pt.runtime.ParaTask";
	}
	
	public static String getFunctorSyntax(){
		return "pt.functionalInterfaces.Functor";
	}
	
	public static String getTaskInfoSyntax(){
		return "pt.runtime.TaskInfo";
	}
	
	public static String getTaskIdSyntax(){
		return "pt.runtime.TaskID";
	}
	
	public static String getAsTaskSyntax(){
		return "pt.runtime.ParaTask.asTask";
	}
	
	public static String getPtTaskTypeSyntax(){
		return getParaTaskSyntax() + ".TaskType";
	}
	
	public static String getTaskType(TaskInfoType taskType){
		return getPtTaskTypeSyntax()+"."+taskType.toString();
	}
	
	public static String getDependsOnDelimiter(){
		return ",";
	}
	
	public static String getNotifyDelimiter(){
		return ";";
	}
}
