package sp.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sp.annotations.Future;
import sp.annotations.StatementMatcherFilter;
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
import sun.net.NetworkServer;

public class CollectionWrapperProcessor extends PtAnnotationProcessor {
	
	private List<CtVariableAccess<?>> variableAccessArguments = null;
	private List<CtInvocation<?>> invocationArguments = null;
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
		findCollectionInvocationArguments();
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
		modifyVarAccessExpressions();
	}
	
	private void modifyCollectionDeclaration(){
		insertStatementBeforeDeclaration();
		changeDeclarationName();
		insertStatementAfterDeclaration();
	}
	
	private void modifyVarAccessExpressions(){
		for(CtVariableAccess<?> varAccess : variableAccessArguments){
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
	
	
	
//----------------------------------------------------HELPER METHODS---------------------------------------------------
	private Future hasFutureAnnotation(CtStatement declarationStatement){
		return null;
	}
	
	private boolean hasTaskAnnotation(CtInvocation<?> methodInovcation){
		return false;
	}
	
	private void insertStatementBeforeDeclaration(){
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
		
		CtBlock<?> parentBlock = thisAnnotatedElement.getParent(CtBlock.class);
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(thisAnnotatedElement);
		parentBlock.insertBefore(filter, invokePT);
	}
	
	private void changeDeclarationName(){
		String newName = SpoonUtils.getTaskName(thisElementName);
		thisAnnotatedElement.setSimpleName(newName);
	}
	
	private void insertStatementAfterDeclaration(){
		String newTypeString = getCollectionType() + "<" + thisCollectionGenericType + ">";
		CtTypeReference newType = thisFactory.Core().createTypeReference();
		newType.setSimpleName(newTypeString);
		List<CtTypeReference> typeCast = new ArrayList<>();
		typeCast.add(newType);
		
		CtLocalVariable<?> castedColleciton = thisFactory.Core().createLocalVariable();
	
		CtVariableAccess varAccess = thisFactory.Core().createVariableAccess();
		
		CtVariableReference varRef = thisFactory.Core().createFieldReference();
		varRef.setSimpleName(SpoonUtils.getTaskName(thisElementName));
		varAccess.setVariable(varRef);
		varAccess.setTypeCasts(typeCast);
		
		castedColleciton.setType(newType);
		castedColleciton.setSimpleName(thisElementName);
		castedColleciton.setDefaultExpression(varAccess);
		
		CtBlock<?> parentBlock = thisAnnotatedElement.getParent(CtBlock.class);
		StatementMatcherFilter<CtStatement> filter = new StatementMatcherFilter<CtStatement>(thisAnnotatedElement);
		parentBlock.insertAfter(filter, castedColleciton);
	}	
	
	private String getCollectionType(){
		String currentType = thisCollectionType.toString();
		if(currentType.contains("List"))
			return SpoonUtils.getListWrapperSyntax();
		else if (currentType.contains("Set"))
			return SpoonUtils.getSetWrapperSyntax();
		else if (currentType.contains("Map"))
			return SpoonUtils.getMapWrapperSyntax();
		else
			return SpoonUtils.getCollecitonWrapperSyntax();
	}	
	
	private void findCollectionInvocationArguments(){
		List<CtStatement> containingStmts = SpoonUtils.findVarAccessOtherThanFutureDefinition(thisAnnotatedElement.getParent(CtBlock.class), thisAnnotatedElement);
		mapOfContainingStatements = SpoonUtils.listAllExpressionsOfStatements(containingStmts);
		variableAccessArguments = new ArrayList<>();
		invocationArguments = new ArrayList<>();
		
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
							listArgumentsToProcess(argument);
					}					
				}
			}

			if(!insideCollection){
				Set<CtExpression<?>> statementExpressions = mapOfContainingStatements.get(statement).keySet();
				for (CtExpression<?> expression : statementExpressions){
					listArgumentsToProcess(expression);
				}
			}
		}		
	}
	

	private void listArgumentsToProcess(CtExpression<?> expression){
		if(expression instanceof CtVariableAccess<?>){
			if(insideCollection){
				CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) expression;
				variableAccessArguments.add(variableAccess);
			}
		}
		else if (expression instanceof CtBinaryOperator<?>){
			CtBinaryOperator<?> binaryOperator = (CtBinaryOperator<?>) expression;
			listArgumentsToProcess(binaryOperator.getLeftHandOperand());
			listArgumentsToProcess(binaryOperator.getRightHandOperand());
		}
		else if(expression instanceof CtInvocationImpl<?>){
			CtInvocationImpl<?> invocation = (CtInvocationImpl<?>) expression;
			if(insideCollection){
				invocationArguments.add(invocation);
			}
			CtExpression<?> target = invocation.getTarget();
			if(target != null){
				String invocTarget = invocation.getTarget().toString();
				if(invocTarget.contains(thisElementName))
					insideCollection = true;
			}
			List<CtExpression<?>> arguments = invocation.getArguments();
			for(CtExpression<?> argument : arguments){
				listArgumentsToProcess(argument);
			}
			insideCollection = false;
		}
		else if (expression instanceof CtUnaryOperator<?>){
			CtUnaryOperator<?> unaryOperator = (CtUnaryOperator<?>) expression;
			listArgumentsToProcess(unaryOperator.getOperand());
		}
		else if (expression instanceof CtArrayAccess<?, ?>){
			CtArrayAccess<?, ?> arrayAccess = (CtArrayAccess<?, ?>) expression;
			listArgumentsToProcess(arrayAccess.getIndexExpression());
		}		
	}
//----------------------------------------------------HELPER METHODS---------------------------------------------------
}
