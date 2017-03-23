package apt.processors;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apt.annotations.TaskInfoType;
import apt.annotations.TaskScheduingPolicy;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
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
import spoon.support.reflect.declaration.CtFieldImpl;

/**
 * Offers additional utilities for parsing the AST, finding specific 
 * statements, and modifying statements/expressions. 
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 */
public class APTUtils {

	public static enum ExpressionRole{Assigned, Assignment, ThrownExpression, ReturnExpression, 
		WhileLoopExpression, DoLoopExpression, ForLoopExpression, ForEachExpression, IfConditionExpression,
		AssertEvaluationExpression, AssertActualExpression, CaseExpression, LocalVarDefaultExpression,
		SwitchSelectorExpression, InvocationArgumentExpression, InvocationTargetExpression, SynchronizedMonitoringExpression};
		
	private static Map<String, String> primitiveTypesMap = new HashMap<String, String>();
	private static List<ASTNode> listOfASTNodes = null;

	static {
		primitiveTypesMap.put("int", "Integer");
		primitiveTypesMap.put("short", "Short");
		primitiveTypesMap.put("long", "Long");
		primitiveTypesMap.put("double", "Double");
		primitiveTypesMap.put("boolean", "Boolean");
		primitiveTypesMap.put("float", "Float");
		primitiveTypesMap.put("byte", "Byte");
	}

	public static String getType(String type) {
		String newType = primitiveTypesMap.get(type);
		if (newType != null)
			return newType;
		return type;
	}
	
	public static String getReturnType(String type){
		if (type.contains(",")){
			//this is a series of generic types from
			//TaskInfo or TaskID. The first one is return type.
			String[] types = type.split(",");
			
			if(types[0].contains("Map"))
				type = types[0] + "," + types[1];
			else
				type = types[0];
			
			type = type.trim();
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
					expressionsToModify.add(invocation.getTarget());
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
	
	public static List<ASTNode> listAllExpressionsOfStatements(List<CtStatement> statements){
		listOfASTNodes = new ArrayList<>();
		listAllExpressions(statements);
		return listOfASTNodes;
	}
	
	private static void listAllExpressions(List<CtStatement> statements){
		List<CtStatement> statementsToInspect = null;
		//Map<CtExpression<?>, ExpressionRole> statementExpressions = null;
		
		for (CtStatement statement : statements){
			
			statementsToInspect = new ArrayList<>();
			//statementExpressions = new HashMap<>();
			
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
				
				ASTNode node = new ASTNode(assignmentImpl);
				boolean fetched = false;
		
				if(assignmentImpl.getAssigned() != null){
					node.registerExpression(assignmentImpl.getAssigned(), ExpressionRole.Assigned);
					fetched = true;
				}
				if(assignmentImpl.getAssignment() != null){
					node.registerExpression(assignmentImpl.getAssignment(), ExpressionRole.Assignment);
					fetched = true;
				}
				
				if(fetched)
					listOfASTNodes.add(node);				
			}
			
			else if (statement instanceof CtThrowImpl){
				CtThrowImpl throwImpl = (CtThrowImpl) statement;
				ASTNode node = new ASTNode(throwImpl);
				
				if(throwImpl.getThrownExpression() != null){
					node.registerExpression(throwImpl.getThrownExpression(), ExpressionRole.ThrownExpression);
					listOfASTNodes.add(node);
				}
			}
			
			else if (statement instanceof CtReturnImpl<?>){
				CtReturnImpl<?> returnImp = (CtReturnImpl<?>) statement;
				ASTNode node = new ASTNode(returnImp);
				if(returnImp.getReturnedExpression() != null){
					node.registerExpression(returnImp.getReturnedExpression(), ExpressionRole.ReturnExpression);
					listOfASTNodes.add(node);
				}
			}
			
			else if (statement instanceof CtLoopImpl){
			
				if (statement instanceof CtWhileImpl){
					
					CtWhileImpl whileImpl = (CtWhileImpl) statement;
					ASTNode node = new ASTNode(whileImpl);
					
					if(whileImpl.getLoopingExpression() != null){
						node.registerExpression(whileImpl.getLoopingExpression(), ExpressionRole.WhileLoopExpression);
						listOfASTNodes.add(node);
					}
					
					if(whileImpl.getBody() != null){
						statementsToInspect.add(whileImpl.getBody());
						listAllExpressions(statementsToInspect);
					}
				}
				
				else if (statement instanceof CtDoImpl){
					CtDoImpl doImpl = (CtDoImpl) statement;
					ASTNode node = new ASTNode(doImpl);
					
					if(doImpl.getLoopingExpression() != null){
						node.registerExpression(doImpl.getLoopingExpression(), ExpressionRole.DoLoopExpression);
						listOfASTNodes.add(node);
					}
					
					if(doImpl.getBody() != null){
						statementsToInspect.add(doImpl.getBody());
						listAllExpressions(statementsToInspect);
					}
				}
				
				else if (statement instanceof CtForImpl){
					CtForImpl forImpl = (CtForImpl) statement;
					ASTNode node = new ASTNode(forImpl);
					
					if(forImpl.getExpression() != null){
						node.registerExpression(forImpl.getExpression(), ExpressionRole.ForLoopExpression);
						listOfASTNodes.add(node);
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
					ASTNode node = new ASTNode(forEachImpl);
					if(forEachImpl.getExpression() != null){
						node.registerExpression(forEachImpl.getExpression(), ExpressionRole.ForEachExpression);
						listOfASTNodes.add(node);
					}
					if(forEachImpl.getBody() != null){
						statementsToInspect.add(forEachImpl.getBody());
						listAllExpressions(statementsToInspect);
					}
				}
			}
			
			else if (statement instanceof CtIfImpl){
				CtIfImpl ifImpl = (CtIfImpl) statement;
				ASTNode node = new ASTNode(ifImpl);
				if(ifImpl.getCondition() != null){
					node.registerExpression(ifImpl.getCondition(), ExpressionRole.IfConditionExpression);
					listOfASTNodes.add(node);
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
				ASTNode node = new ASTNode(assertImpl);
				boolean fetched = false;
				if(assertImpl.getAssertExpression() != null){
					node.registerExpression(assertImpl.getAssertExpression(), ExpressionRole.AssertEvaluationExpression);
					fetched = true;
				}
				if(assertImpl.getExpression() != null){
					node.registerExpression(assertImpl.getExpression(), ExpressionRole.AssertActualExpression);
					fetched = true;
				}
				if(fetched)
					listOfASTNodes.add(node);
			}
			
			else if (statement instanceof CtCaseImpl<?>){
				CtCaseImpl<?> caseImpl = (CtCaseImpl<?>) statement;
				ASTNode node = new ASTNode(caseImpl);
				
				if(caseImpl.getCaseExpression() != null){
					node.registerExpression(caseImpl.getCaseExpression(), ExpressionRole.CaseExpression);
					listOfASTNodes.add(node);
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
				ASTNode node = new ASTNode(varImpl);
						
				if(varImpl.getDefaultExpression() != null){
					node.registerExpression(varImpl.getDefaultExpression(), ExpressionRole.LocalVarDefaultExpression);
					listOfASTNodes.add(node);
				}
			}
			
			else if (statement instanceof CtSynchronizedImpl){
				CtSynchronizedImpl syncImpl = (CtSynchronizedImpl) statement;
				ASTNode node = new ASTNode(syncImpl);
				if(syncImpl.getExpression() != null){
					node.registerExpression(syncImpl.getExpression(), ExpressionRole.SynchronizedMonitoringExpression);
					listOfASTNodes.add(node);
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
				ASTNode node = new ASTNode(switchImpl);
				if(switchImpl.getSelector() != null){
					node.registerExpression(switchImpl.getSelector(), ExpressionRole.SwitchSelectorExpression);
					listOfASTNodes.add(node);
				}
				if(switchImpl.getCases() != null){
					statementsToInspect.addAll(switchImpl.getCases());
					listAllExpressions(statementsToInspect);
				}
			}
			
			else if (statement instanceof CtInvocationImpl<?>){
				CtInvocationImpl<?> invocImpl = (CtInvocationImpl<?>) statement;
				ASTNode node = new ASTNode(invocImpl);
				boolean fetched = false;
				
				if(invocImpl.getArguments() != null){
					List<CtExpression<?>> arguments = invocImpl.getArguments();
					for(CtExpression<?> argument : arguments){
						node.registerExpression(argument, ExpressionRole.InvocationArgumentExpression);
					}
					fetched = true;
				}
				
				if(invocImpl.getTarget() != null){
					node.registerExpression(invocImpl.getTarget(), ExpressionRole.InvocationTargetExpression);
					fetched = true;
				}
				if(fetched)
				listOfASTNodes.add(node);
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
	 * of that future variable. 
	 * @param block
	 * @param element
	 * @return
	 */
	public static List<CtStatement> findVarAccessOtherThanFutureDefinition(CtBlock<?> block, CtVariable<?> element) {
		List<CtStatement> blockStatements = block.getStatements();
		List<CtStatement> statementsWithThisFutureVariable = new ArrayList<>();
		boolean search = false; //search for statements that access a variable can start after 
								//the declaration statement of that variable is found.
		
		/*
		 * foundDef is a flag that indicates that the declaration of a variable is found, and then after that the 
		 * search for other statements in which a variable is used can be performed. For fields, the situation is
		 * different, as the declaration of a filed is before every block statement, so the flag is already set to
		 * true. However, cases should be considered in which fields are overridden in inner blocks.  
		 */
		if(element instanceof CtField<?>)
			search = true;
				
		String varName = element.getSimpleName();
				
		for (CtStatement statement : blockStatements) {
			
			if (statement instanceof CtBlock<?>){
				CtBlock<?> blockStatement = (CtBlock<?>) statement;
				List<CtStatement> tempList = findVarAccessOtherThanFutureDefinition(blockStatement, element);
				if(tempList.size() != 0)
					statementsWithThisFutureVariable.addAll(tempList);
			}
			
			else{
				/*
				 * If we encounter a variable declaration statement that overrides 
				 * a field, then searching for that field through that statement block
				 * must be stopped. 
				 */
				if(statement instanceof CtLocalVariable<?>){
					CtLocalVariable<?> variable = (CtLocalVariable<?>) statement;
					if(element instanceof CtField<?>){
						if(variable.getSimpleName() == element.getSimpleName()){
							search = false;
						}
					}
				}
				
				String statementString = statement.toString();
								
				if (!search){
					//this will never become true for a field variable.
					//otherwise, the declaration of the local variable has 
					//been reached, and from now on, we can search for the 
					//access statements. 
					if (statement.equals(element)){
						search = true;
					}
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
	 * been used within another statement. This method expects the argument to be 
	 * declared before the statement that is passed as <i>currentStatement</i>
	 * 
	 * @param currentStatement : CtStatement, the statement, in which the variable is used.
	 * @param argName : String, the name of the variable, for which we are looking for the declaration. 
	 * 
	 * @return CtStatement, declaring statement
	 */
	public static CtVariable<?> getDeclarationStatement(CtStatement currentStatement, String argName) {
		if (!argName.matches("[a-zA-Z0-9_]+")) {
			return null;
		}		
		CtBlock<?> block = currentStatement.getParent(CtBlock.class);
		while(block != null){
			List<CtStatement> blockStatements = block.getStatements();
			
			for(CtStatement statement : blockStatements) {
							
				if(statement == currentStatement){
					if(statement instanceof CtLocalVariable<?>){
						CtLocalVariable<?> localVariable = (CtLocalVariable<?>) statement;
						if(isTheWantedDeclaration(localVariable, argName))
							return localVariable;
						else
							break;
					}
					else
						break;
				}
				
				if (statement instanceof CtLocalVariable<?>){
					CtLocalVariable<?> localVariable = (CtLocalVariable<?>) statement;
					if(isTheWantedDeclaration(localVariable, argName))
						return localVariable;
				}				
			}
			
			block = block.getParent(CtBlock.class);
		}
		//if not found yet, it might be among class fields
		CtClass<?> parentClass = currentStatement.getParent(CtClass.class);
		List<CtField<?>> classFields = parentClass.getFields();
		for(CtField<?> classField : classFields){
			if(isTheWantedDeclaration(classField, argName))
				return classField;
		}
		return null;
	}
	
	/**
	 * This method searches the entire body of a class for the declaration of a member named as
	 * <code>argName</code>. 
	 * 
	 * @param field
	 * @param argName
	 * @return
	 */
	public static CtVariable<?> getDeclarationStatement(CtField<?> field, String argName){
		if (!argName.matches("[a-zA-Z0-9_]+")) {
			return null;
		}
		
		CtClass<?> parentClass = field.getParent(CtClass.class);
		List<CtField<?>> classFields = parentClass.getFields();
		
		for(CtField<?> classField : classFields){
			if(isTheWantedDeclaration(classField, argName))
				return classField;
		}
		
		Set<CtMethod<?>> classMethods = parentClass.getMethods();
		for(CtMethod<?> classMethod : classMethods){
			CtBlock<?> methodBlock = classMethod.getBody();
			CtVariable<?> declaration = searchForVarInBlock(methodBlock, argName);
			if(declaration != null)
				return declaration;
		}
		
		return null;
	}
	
	public static CtVariable<?> searchForVarInBlock(CtBlock<?> block, String argName){
		List<CtStatement> blockStatements = block.getStatements();
		for(CtStatement blockStatement : blockStatements){
			if(blockStatement instanceof CtBlock<?>)
				searchForVarInBlock((CtBlock<?>)blockStatement, argName);
			
			else if(blockStatement instanceof CtLocalVariable<?>){
				CtLocalVariable<?> localVariable = (CtLocalVariable<?>) blockStatement;
				if(isTheWantedDeclaration(localVariable, argName))
					return localVariable;
			}
		}
		return null;
	}
	
	/**
	 * Checks if the statement that is passed as an argument, is the declaration statement of the
	 * variable name that is also sent as an argument. 
	 */
	public static boolean isTheWantedDeclaration(CtVariable<?> statement, String argName){
		/*
		 * If the argument does not have the same name as the current declared element, which is being
		 * inspected, then its names is changed into a TaskName format iff it is a future variable. 
		 */		
		String argTaskName = getTaskName(argName);
		
		if(statement instanceof CtLocalVariableImpl<?>){
			CtLocalVariableImpl<?> currentDeclaration = (CtLocalVariableImpl<?>) statement;
			if(currentDeclaration.getSimpleName().equals(argTaskName) || currentDeclaration.getSimpleName().equals(argName))
				return true;
		}
		
		else if(statement instanceof CtFieldImpl<?>){
			CtFieldImpl<?> currentDeclaration = (CtFieldImpl<?>) statement;
			if(currentDeclaration.getSimpleName().equals(argTaskName) || currentDeclaration.getSimpleName().equals(argName))
				return true;
		}
		
		return false;
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
	public static boolean isFutureVariable(CtVariable<?> currentDeclaredElement, String argName){
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
		CtVariable<?> declaration = null;
		
		if(currentDeclaredElement instanceof CtLocalVariable<?>){
			CtLocalVariable<?> localVariable = (CtLocalVariable<?>) currentDeclaredElement;
			declaration = getDeclarationStatement(localVariable, argName);
		}
		
		else if(currentDeclaredElement instanceof CtFieldImpl<?>){
			CtField<?> field = (CtField<?>) currentDeclaredElement;
			declaration = getDeclarationStatement(field, argName);
		}
		
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
	
	public static String getArrayLengthSyntax(){
		return ".length";
	}
	
	public static String getTaskReductionName(String name){
		return "__" + name + "PtTaskReductionObject__";
	}
	
	public static String getTaskIDName(String name) {
		return "__" + name + "PtTaskID__";
	}
	
	public static String getTaskIDGroupName(String name){
		return "__" + name + "PtTaskIDGroup__";
	}
	
	public static String getTaskIDGroupSizeSyntax(String name){
		return "__" + name + "PtTaskIDGroupSize__";
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
		return (name.startsWith("__") && name.endsWith("NonLambdaArg__"));
	}
	
	public static boolean isLambdaArg(String name){
		return (name.startsWith("__") && name.endsWith("LambdaArg__"));
	}
	
	public static boolean isPtTaskName(String name){
		return (name.startsWith("__") && name.endsWith("PtTask__"));
	}
	
	public static String getOriginalName(String elementName) {
		
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
	public static boolean isTaskIDReplacement(CtVariable<?> element, String name){
		if(name.startsWith("__") && name.endsWith("PtTaskID__"+getResultSyntax())){
			String originalName = getOriginalName(name);
			if(isFutureVariable(element, originalName)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFutureGroup(CtVariable<?> element, String name){
		String taskIDGroupName = getTaskIDGroupName(name);
		CtVariable<?> futureGroupDeclaration = null;
		CtVariable<?> taskIDGroupDeclaration = null;
		if(element instanceof CtLocalVariable<?>){
			CtLocalVariable<?> localVariable = (CtLocalVariable<?>) element;
			futureGroupDeclaration = getDeclarationStatement(localVariable, name);
			taskIDGroupDeclaration = getDeclarationStatement(localVariable, taskIDGroupName);
		}
		else if (element instanceof CtField<?>){
			CtField<?> field = (CtField<?>) element;
			futureGroupDeclaration = getDeclarationStatement(field, name);
			taskIDGroupDeclaration = getDeclarationStatement(field, taskIDGroupName);
		}
		if((futureGroupDeclaration == null) || (taskIDGroupDeclaration == null))
			return false;
		
		String taskIDGroupType = taskIDGroupDeclaration.getType().toString();
		String futureGroupType = futureGroupDeclaration.getType().toString();
		if(!taskIDGroupType.contains(getTaskIDGroupSyntax()))
			return false;
		if(!(futureGroupType.contains("[]")))
			return false;
		//check if it is a one dimensional array
		futureGroupType = futureGroupType.substring(futureGroupType.indexOf("]")+1); 
		if(futureGroupType.contains("]"))
			return false;
		return true;
	}
	
	public static String getLockQualifiedSyntax(){
		return "java.util.concurrent.locks.Lock";
	}
	
	public static String getReentrantLockQualifiedSyntax(){
		return "java.util.concurrent.locks.ReentrantLock";
	}
	
	public static String getRedLibPackageSyntax(){
		return "pu.RedLib.";
	}
	
	public static String getGetWrapperSyntax(){
		//to make sure that definitely method "getPtWrapper()" is called
		//and not something that has similar name e.g., getPtWrapperEx()
		//the left parenthesis is involved in the syntax!
		return "ParaTask.getPtHybridWrapper(";
	}
	
	public static String getExecuteInterimHandlerSyntax(){
		return "executeInterimHandler";
	}
	
	public static String getResultSyntax(){
		return ".getReturnResult()";
	}
	
	public static String getResultSyntax(int index){
		return ".getReturnResult(" + index + ")";
	}
	
	public static String getRegisterDependencesSyntax(){
		return "registerDependences";
	}
	
	public static String getRegisterSlotToNotifySyntax(){
		return "registerSlotToNotify";
	}
	
	public static String getRegisterAsyncCatchSyntax(){
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
		return "asTask";
	}
	
	public static String getSetReductionSyntax(){
		return "pt.runtime.ParaTask.registerReduction";
	}
	
	public static String getHybridCollectionSyntax(){
		return "pt.hybridWrappers.PtHybridCollection";
	}
	
	public static String getHybridListSyntax(){
		return "pt.hybridWrappers.PtHybridList";
	}
	
	public static String getHybridMapSyntax(){
		return "pt.hybridWrappers.PtHybridMap";
	}
	
	public static String getHybridSetSyntax(){
		return "pt.hybridWrappers.PtHybridSet";
	}
	
	public static String getPtTaskTypeSyntax(){
		return getParaTaskSyntax() + ".TaskType";
	}
	
	public static String getTaskType(TaskInfoType taskType){
		return getPtTaskTypeSyntax()+"."+taskType.toString();
	}
	
	public static String getPtScheduleTypeSynatx(){
		return getParaTaskSyntax() + ".PTSchedulingPolicy";
	}
	
	public static String getScheduleType(TaskScheduingPolicy schedulingPolicy){
		return getPtScheduleTypeSynatx() + "." + schedulingPolicy.toString();
	}
	
	public static String getDefaultNumberOfThreads(){
		return "Runtime.getRuntime().availableProcessors()";
	}
	
	public static String getDependsOnDelimiter(){
		return ",";
	}
	
	public static String getNotifyDelimiter(){
		return ";";
	}	
}
