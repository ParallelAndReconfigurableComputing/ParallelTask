package sp.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

	public static enum ExpressionRole{Assigned, Assignment, ThrownExpression, ReturnExpression, 
		WhileLoopExpression, DoLoopExpression, ForLoopExpression, ForEachExpression, IfConditionExpression,
		AssertEvaluationExpression, AssertActualExpression, CaseExpression, LocalVarDefaultExpression,
		SwitchSelectorExpression, InvocationArgumentExpression, InvocationTargetExpression, SynchronizedMonitoringExpression};
		
	private static Map<String, String> primitiveMap = new HashMap<String, String>();
	private static Map<CtStatement, Map<CtExpression<?>, ExpressionRole>> mapOfExpressions = null;

	static {
		primitiveMap.put("int", "Integer");
		primitiveMap.put("short", "Short");
		primitiveMap.put("long", "Long");
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
//		String statement = null;
//		Pattern pattern = Pattern.compile(regex);
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
		mapOfExpressions = new LinkedHashMap<>();
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
				if(statementsToInspect != null)			
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
				
				boolean fetched = false;
				if(assignmentImpl.getAssigned() != null){
					statementExpressions.put(assignmentImpl.getAssigned(), ExpressionRole.Assigned);
					fetched = true;
				}
				if(assignmentImpl.getAssignment() != null){
					statementExpressions.put(assignmentImpl.getAssignment(), ExpressionRole.Assignment);
					fetched = true;
				}
				if(fetched)
					mapOfExpressions.put(assignmentImpl, statementExpressions);
			}
			
			else if (statement instanceof CtThrowImpl){
				CtThrowImpl throwImpl = (CtThrowImpl) statement;
				if(throwImpl.getThrownExpression() != null){
					statementExpressions.put(throwImpl.getThrownExpression(), ExpressionRole.ThrownExpression);
					mapOfExpressions.put(throwImpl, statementExpressions);
				}
			}
			
			else if (statement instanceof CtReturnImpl<?>){
				CtReturnImpl<?> returnImp = (CtReturnImpl<?>) statement;
				if(returnImp.getReturnedExpression() != null){
					statementExpressions.put(returnImp.getReturnedExpression(), ExpressionRole.ReturnExpression);
					mapOfExpressions.put(returnImp, statementExpressions);
				}
			}
			
			else if (statement instanceof CtLoopImpl){
			
				if (statement instanceof CtWhileImpl){
					
					CtWhileImpl whileImpl = (CtWhileImpl) statement;
					
					if(whileImpl.getLoopingExpression() != null){
						statementExpressions.put(whileImpl.getLoopingExpression(), ExpressionRole.WhileLoopExpression);
						mapOfExpressions.put(whileImpl, statementExpressions);
					}
					
					if(whileImpl.getBody() != null){
						statementsToInspect.add(whileImpl.getBody());
						listAllExpressions(statementsToInspect);
					}
				}
				
				else if (statement instanceof CtDoImpl){
					CtDoImpl doImpl = (CtDoImpl) statement;
					if(doImpl.getLoopingExpression() != null){
						statementExpressions.put(doImpl.getLoopingExpression(), ExpressionRole.DoLoopExpression);
						mapOfExpressions.put(doImpl, statementExpressions);
					}
					
					if(doImpl.getBody() != null){
						statementsToInspect.add(doImpl.getBody());
						listAllExpressions(statementsToInspect);
					}
				}
				
				else if (statement instanceof CtForImpl){
					CtForImpl forImpl = (CtForImpl) statement;
					if(forImpl.getExpression() != null){
						statementExpressions.put(forImpl.getExpression(), ExpressionRole.ForLoopExpression);
						mapOfExpressions.put(forImpl, statementExpressions);
					}
					boolean fetched = false;
					if(forImpl.getBody() != null){
						statementsToInspect.add(forImpl.getBody());
						fetched = true;
					}
					if(forImpl.getForInit() != null){
						statementsToInspect.addAll(forImpl.getForInit());
						fetched = true;
					}
					if(forImpl.getForUpdate() != null){
						statementsToInspect.addAll(forImpl.getForUpdate());
						fetched = true;
					}
					
					if (fetched)
						listAllExpressions(statementsToInspect);
				}
				
				else if (statement instanceof CtForEachImpl){
					CtForEachImpl forEachImpl = (CtForEachImpl) statement;
					if(forEachImpl.getExpression() != null){
						statementExpressions.put(forEachImpl.getExpression(), ExpressionRole.ForEachExpression);
						mapOfExpressions.put(forEachImpl, statementExpressions);
					}
					if(forEachImpl.getBody() != null){
						statementsToInspect.add(forEachImpl.getBody());
						listAllExpressions(statementsToInspect);
					}
				}
			}
			
			else if (statement instanceof CtIfImpl){
				CtIfImpl ifImpl = (CtIfImpl) statement;
				if(ifImpl.getCondition() != null){
					statementExpressions.put(ifImpl.getCondition(), ExpressionRole.IfConditionExpression);
					mapOfExpressions.put(ifImpl, statementExpressions);
				}
				boolean fetched = false;
				if(ifImpl.getThenStatement() != null){
					statementsToInspect.add(ifImpl.getThenStatement());
					fetched = true;
				}
				if(ifImpl.getElseStatement() != null){
					statementsToInspect.add(ifImpl.getElseStatement());
					fetched = true;
				}
				if(fetched)
					listAllExpressions(statementsToInspect);
			}
			
			else if (statement instanceof CtAssertImpl<?>){
				CtAssertImpl<?> assertImpl = (CtAssertImpl<?>) statement;
				boolean fetched = false;
				if(assertImpl.getAssertExpression() != null){
					statementExpressions.put(assertImpl.getAssertExpression(), ExpressionRole.AssertEvaluationExpression);
					fetched = true;
				}
				if(assertImpl.getExpression() != null){
					statementExpressions.put(assertImpl.getExpression(), ExpressionRole.AssertActualExpression);
					fetched = true;
				}
				if(fetched)
					mapOfExpressions.put(assertImpl, statementExpressions);
			}
			
			else if (statement instanceof CtCaseImpl<?>){
				CtCaseImpl<?> caseImpl = (CtCaseImpl<?>) statement;
				if(caseImpl.getCaseExpression() != null){
					statementExpressions.put(caseImpl.getCaseExpression(), ExpressionRole.CaseExpression);
					mapOfExpressions.put(caseImpl, statementExpressions);
				}
				if(caseImpl.getStatements() != null){
					statementsToInspect.addAll(caseImpl.getStatements());
					listAllExpressions(statementsToInspect);
				}
			}
			
			else if (statement instanceof CtContinueImpl){
				//AT THIS STAGE DO NOTHING FOR A CONTINUE STATEMENT
			}
			
			else if (statement instanceof CtLocalVariableImpl<?>){
				CtLocalVariableImpl<?> varImpl = (CtLocalVariableImpl<?>) statement;
				if(varImpl.getDefaultExpression() != null){
					statementExpressions.put(varImpl.getDefaultExpression(), ExpressionRole.LocalVarDefaultExpression);
					mapOfExpressions.put(varImpl, statementExpressions);
				}
			}
			
			else if (statement instanceof CtSynchronizedImpl){
				CtSynchronizedImpl syncImpl = (CtSynchronizedImpl) statement;
				if(syncImpl.getExpression() != null){
					statementExpressions.put(syncImpl.getExpression(), ExpressionRole.SynchronizedMonitoringExpression);
					mapOfExpressions.put(syncImpl, statementExpressions);
				}
				
				if(syncImpl.getBlock() != null){
					statementsToInspect.add(syncImpl.getBlock());
					listAllExpressions(statementsToInspect);
				}
			}
			
			else if (statement instanceof CtTryImpl){
				//if (statement instanceof CtTryWithResource){} for new version of spoon!
				CtTryImpl tryImpl = (CtTryImpl) statement;
				boolean fetched = false;
				if(tryImpl.getBody() != null){
					statementsToInspect.add(tryImpl.getBody());
					fetched = true;
				}
				if(tryImpl.getFinalizer() != null){
					statementsToInspect.add(tryImpl.getFinalizer());
					fetched = true;
				}
				if(tryImpl.getCatchers() != null){
					List<CtCatch> catchers = tryImpl.getCatchers();
					for (CtCatch catcher : catchers){
						statementsToInspect.add(catcher.getBody());
					}
					fetched = true;
				}
				if(fetched)
					listAllExpressions(statementsToInspect);
			}
			else if (statement instanceof CtBreakImpl){
				//AT THIS STAGE DO NOTHING FOR A BREAK STATEMENT
			}
			
			else if (statement instanceof CtSwitchImpl<?>){
				CtSwitchImpl<?> switchImpl = (CtSwitchImpl<?>) statement;
				if(switchImpl.getSelector() != null){
					statementExpressions.put(switchImpl.getSelector(), ExpressionRole.SwitchSelectorExpression);
					mapOfExpressions.put(switchImpl, statementExpressions);
				}
				if(switchImpl.getCases() != null){
					statementsToInspect.addAll(switchImpl.getCases());
					listAllExpressions(statementsToInspect);
				}
			}
			
			else if (statement instanceof CtInvocationImpl<?>){
				CtInvocationImpl<?> invocImpl = (CtInvocationImpl<?>) statement;
				boolean fetched = false;
				if(invocImpl.getArguments() != null){
					List<CtExpression<?>> arguments = invocImpl.getArguments();
					for(CtExpression<?> argument : arguments){
						statementExpressions.put(argument, ExpressionRole.InvocationArgumentExpression);
					}
					fetched = true;
				}
				if(invocImpl.getTarget() != null){
					statementExpressions.put(invocImpl.getTarget(), ExpressionRole.InvocationTargetExpression);
					fetched = true;
				}
				if(fetched)
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
		List<CtAnnotation<? extends Annotation>> ats = s.getAnnotations();
		for(CtAnnotation<? extends Annotation> a : ats) {
			if(clazz.isInstance(a.getActualAnnotation()))
				return true;
		}
		return false;
	}
	
	/**
	 * Finds and returns the declaration statement of an argument that has
	 * been used within another statement. 
	 * 
	 * @param currentStatement : CtStatement, the statement, in which the variable is used.
	 * @param argName : String, the name of the variable, for which we are looking for the declaration. 
	 * 
	 * @return CtStatement, declaring statement
	 */
	public static CtStatement getDeclarationStatement(CtStatement currentStatement, String argName) {
		if (!argName.matches("[a-zA-Z0-9_]+")) {
			return null;
		}
		
		/*
		 * If the argument does not have the same name as the current declared element, which is being
		 * inspected, then its names is changed into a TaskName format iff it is a future variable. 
		 */		
		String argTaskName = getTaskName(argName);
		
		CtBlock<?> block = currentStatement.getParent(CtBlock.class);
		while(block != null){
			List<CtStatement> blockStatements = block.getStatements();
			
			for(CtStatement statement : blockStatements) {
				
				if(statement == currentStatement){
					if(currentStatement instanceof CtLocalVariableImpl<?>){
						CtLocalVariableImpl<?> currentDeclaration = (CtLocalVariableImpl<?>) currentStatement;
						if(currentDeclaration.getSimpleName().equals(argTaskName) || currentDeclaration.getSimpleName().equals(argName))
							return statement;
					}
					else
						break;
				}
				
				if (statement instanceof CtLocalVariable<?>){
					if(statement instanceof CtVariable<?>){
						CtVariable<?> variableDeclaration = (CtVariable<?>) statement;
						if(variableDeclaration.getSimpleName().equals(argTaskName) || variableDeclaration.getSimpleName().equals(argName)){
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
	 * is itself a future variable or not. If yes, then its name is replaced by its <code>taskInfo</code>
	 * equivalent. 
	 * 
	 * @param currentDeclaredElement : CtLocalVariable, the annotated element that is currently being 
	 * processed by spoon processor.
	 * 
	 * @param argName : String, the name of the element being investigated.
	 * 
	 * @return <code>true</code>, if the argument is a future variable, <code>false</code> otherwise. 
	 */
	public static boolean isFutureVariable(CtLocalVariable<?> currentDeclaredElement, String argName){
		/*
		 * If an object is a future variable, then its name at its declaration statement has definitely
		 * changed to the TaskInfo format. When we transfer the name into its TaskInfo format here,
		 * the 'getDeclarationStatement()' method will search for two String literals. 
		 * First, the TaskInfo format created here. Second, the TaskInfo format of the TaskInfo format. 
		 * (i.e., TaskInfo(TaskInfo)). Considering that the second String literal will not be found, a
		 * statement is returned if and only if the statement name is in TaskInfo format (i.e., first
		 * string literal). 
		 */
		argName = getTaskName(argName);
		
		CtLocalVariable<?> declaration = (CtLocalVariable<?>)getDeclarationStatement(currentDeclaredElement, argName);
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
	
	public static String getTaskIDGroupName(String name){
		return "__" + name + "PtTaskIDGroup__";
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
		
		if(elementName.startsWith("__") && elementName.endsWith("PtLambdaArg__"+getResultSyntax()))
			return elementName.substring("__".length(), (elementName.length() - ("PtLambdaArg__"+getResultSyntax()).length()));
		
		else if (elementName.startsWith("__") && elementName.endsWith("PtTaskID__"+getResultSyntax()))
			return elementName.substring("__".length(), (elementName.length() - ("PtTaskID__"+getResultSyntax()).length()));
		
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
	
	/**
	 * Indicates if an object represents the taskID object of a task that has been declared
	 * before the statement that is currently being processed by spoon processor.
	 * Considering that spoon processes elements in order of appearance, if a taskInfo has
	 * been created, and its values replaced by <code>taskID.getReturnResult()</code>, then
	 * it must be declared before, and hence processed before the current element being processed.
	 *  
	 * @param element : CtLocalVariable, the local variable declaration that is annotated, and is currently
	 * being processed by spoon.
	 * 
	 * @param name : String, the name of the variable that is being inspected. 
	 * 
	 * @return <code>true</code> if the variable is replace by a taskID object, <code>false</code> otherwise.
	 */
	public static boolean isTaskIDReplacement(CtLocalVariable<?> element, String name){
		if(name.startsWith("__") && name.endsWith("PtTaskID__"+getResultSyntax())){
			String originalName = getOrigName(name);
			if(isFutureVariable(element, originalName)){
				return true;
			}
		}
		return false;
	}
	
	public static String getResultSyntax(){
		return ".getReturnResult()";
	}
	
	public static String getResultSyntax(int index){
		return ".getReturnResult(" + index + ")";
	}
	
	public static String getDependsOnSyntax(){
		return "depndsOn";
	}
	
	public static String getNotifiesSyntax(){
		return "registerSlotToNotify";
	}
	
	public static String getAsyncExceptionSyntax(){
		return "registerAsyncCatch";
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
	
	public static String getTaskIDGroupSyntax()	{
		return "pt.runtime.TaskIDGroup";
	}
	public static String getAsTaskSyntax(){
		return "pt.runtime.ParaTask.asTask";
	}
	
	public static String getCollecitonWrapperSyntax(){
		return "pt.wrappers.PtCollectionWrapper";
	}
	
	public static String getListWrapperSyntax(){
		return "pt.wrappers.PtListWrapper";
	}
	
	public static String getMapWrapperSyntax(){
		return "pt.wrappers.PtMapWrapper";
	}
	
	public static String getSetWrapperSyntax(){
		return "pt.wrappers.PtSetWrapper";
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
