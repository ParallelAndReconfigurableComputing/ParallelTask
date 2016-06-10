package sp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sp.annotations.Future;
import sp.processors.SpoonUtils.ExpressionRole;
import spoon.reflect.Factory;
import spoon.reflect.code.CtArrayAccess;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.support.reflect.code.CtInvocationImpl;

public class CollectionWrapperProcessor extends PtAnnotationProcessor {
	
	private List<CtVariableAccess<?>> variableAccessExpressions = null;
	private boolean insideCollection = false;
	private CtTypeReference<?> thisCollectionGenericType = null;
	private CtTypeReference<?> thisCollectionType = null;
	
	public CollectionWrapperProcessor(Factory factory, Future future, CtLocalVariable<?> annotatedElement){
		thisAnnotatedElement = annotatedElement;
		thisFactory = factory;
		thisFutureAnnotation = future;
		thisElementName = thisAnnotatedElement.getSimpleName();
	}

	@Override
	public void process() {
		inspectElement();
		findAllVariableAccessExpressions();
		modifySourceCode();
	}
	
	private void inspectElement(){
		String thisStatement = thisAnnotatedElement.toString();
		String collectionType = thisStatement.substring(thisStatement.indexOf("<")+1, thisStatement.indexOf(">"));
		collectionType = SpoonUtils.getType(collectionType);
		thisCollectionGenericType = thisFactory.Core().createTypeReference();
		thisCollectionGenericType.setSimpleName(collectionType);
		thisCollectionType = thisAnnotatedElement.getType();
	}

	@Override
	protected void modifySourceCode() {
		modifyCollectionDeclaration();
		for(CtVariableAccess<?> varAccess : variableAccessExpressions){
			String varName = varAccess.toString();
			System.out.println("inspecting: " + varName);
			boolean expressionModified = false;
			if(SpoonUtils.isTaskIDReplacement(thisAnnotatedElement, varName)){
				System.out.println(varName + " is a taskID replacement.");
				modifyWithTaskIDReplacement(varAccess);
				expressionModified = true;
			}
			else{
				CtStatement declarationStatement = SpoonUtils.getDeclarationStatement(varAccess.getParent(CtStatement.class)
													, varName);
				if(declarationStatement != null){
					System.out.println("The declaration for " + varName + " is: " + declarationStatement.toString());
					Future future = hasFutureAnnotation(declarationStatement);
					if(future != null){
						modifyWithFutuerObject(future, declarationStatement, varAccess);
						expressionModified = true;
					}
				}
			}			
		}
	}
	
	private void modifyCollectionDeclaration(){
		CtInvocation invokePT = thisFactory.Core().createInvocation();
		CtExecutableReference executable = thisFactory.Core().createExecutableReference();
		CtCodeSnippetExpression invocationTarget = thisFactory.Core().createCodeSnippetExpression();
		CtCodeSnippetExpression invocationArgument = thisFactory.Core().createCodeSnippetExpression();
		
		executable.setSimpleName("processingInParallel");
		invocationTarget.setValue(SpoonUtils.getParaTaskSyntax());
		invocationArgument.setValue("true");
		List<CtExpression<?>> arguments = new ArrayList<>();
		arguments.add(invocationArgument);
		
		invokePT.setExecutable(executable); //processingInParallel
		invokePT.setTarget(invocationTarget);//pt.runtime.ParaTask
		invokePT.setArguments(arguments);//true
	}

	
	private void modifyWithFutuerObject(Future future, CtStatement declarationStatement, CtVariableAccess<?> varAccess){
		
	}
	
	private void modifyWithTaskIDReplacement(CtVariableAccess<?> varAccess){
		String varName = varAccess.toString();
		varName = SpoonUtils.getOrigName(varName);
		varName = varName.trim();
		varName = SpoonUtils.getTaskIDName(varName);
		CtVariableReference varRef = thisFactory.Core().createFieldReference();
		varRef.setSimpleName(varName);
		varAccess.setVariable(varRef);
	}
	
	private void findAllVariableAccessExpressions(){
		List<CtStatement> containingStmts = SpoonUtils.findVarAccessOtherThanFutureDefinition(thisAnnotatedElement.getParent(CtBlock.class), thisAnnotatedElement);
		mapOfContainingStatements = SpoonUtils.listAllExpressionsOfStatements(containingStmts);
		variableAccessExpressions = new ArrayList<>();
		
		Set<CtStatement> statements = mapOfContainingStatements.keySet();
		for(CtStatement statement : statements){
			insideCollection = false;
		
			if(statement instanceof CtInvocationImpl<?>){
				CtInvocationImpl<?> invocation = (CtInvocationImpl<?>) statement;
				CtExpression<?> target = invocation.getTarget();
				if(target != null){
					String invocTarget = invocation.getTarget().toString();
					if(invocTarget.contains(thisElementName)){
						insideCollection = true;
						List<CtExpression<?>> arguments = invocation.getArguments();
						for(CtExpression<?> argument : arguments)
							listVariableAccessExpressions(argument);
					}					
				}
			}
			
			if(!insideCollection){
				Set<CtExpression<?>> statementExpressions = mapOfContainingStatements.get(statement).keySet();
				for (CtExpression<?> expression : statementExpressions){
					listVariableAccessExpressions(expression);
				}
			}
		}
	}
	
	private void listVariableAccessExpressions(CtExpression<?> expression){
		if(expression instanceof CtVariableAccess<?>){
			if(insideCollection){
				CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) expression;
				variableAccessExpressions.add(variableAccess);
			}
		}
		else if (expression instanceof CtBinaryOperator<?>){
			CtBinaryOperator<?> binaryOperator = (CtBinaryOperator<?>) expression;
			listVariableAccessExpressions(binaryOperator.getLeftHandOperand());
			listVariableAccessExpressions(binaryOperator.getRightHandOperand());
		}
		else if(expression instanceof CtInvocationImpl<?>){
			CtInvocationImpl<?> invocation = (CtInvocationImpl<?>) expression;
			CtExpression<?> target = invocation.getTarget();
			if(target != null){
				String invocTarget = invocation.getTarget().toString();
				if(invocTarget.contains(thisElementName))
					insideCollection = true;
			}
			List<CtExpression<?>> arguments = invocation.getArguments();
			for(CtExpression<?> argument : arguments){
				listVariableAccessExpressions(argument);
			}
			insideCollection = false;
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
	
//----------------------------------------------------HELPER METHODS---------------------------------------------------
	private Future hasFutureAnnotation(CtStatement declarationStatement){
		return null;
	}
	
	private CtTypeReference<?> collectionCast(){
		return null;
	}
//----------------------------------------------------HELPER METHODS---------------------------------------------------
}
