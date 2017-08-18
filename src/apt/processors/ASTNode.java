package apt.processors;

import java.util.ArrayList;
import java.util.List;

import apt.processors.APTUtils.ExpressionRole;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;

/**
 * This class represents an AST node, with its corresponding 
 * statement, parent statement, the expressions within the 
 * statement and the role of each statement. 
 * This class is initially intended to facilitate processing 
 * the expressions and statements that hold a specific object
 * (e.g., future object).
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 * @see    {@link APTUtils#listAllExpressionsOfStatements(List)}
 */

public class ASTNode {
	private class Expression{
		CtExpression<?> expression;
		ExpressionRole  expressionRole;
	}
	
	private CtStatement parentStatement = null;
	private CtStatement thisStatement = null;
	private List<Expression> statementExpressions = null;
	
	public ASTNode(CtStatement statement){
		thisStatement = statement;
		parentStatement = statement.getParent(CtStatement.class);
		statementExpressions = new ArrayList<>();
	}
	
	public void registerExpression (CtExpression<?> expression, ExpressionRole role){
		Expression newExpression = new Expression();
		newExpression.expression = expression;
		newExpression.expressionRole = role;
		statementExpressions.add(newExpression);
	}
	
	public int getNumberOfExpressions(){
		return statementExpressions.size();
	}
	
	public CtExpression<?> getExpression (int index){
		return statementExpressions.get(index).expression;
	}
	
	public ExpressionRole getExpressionRole (int index){
		return statementExpressions.get(index).expressionRole;
	}
	
	public CtStatement getStatement(){
		return thisStatement;
	}
	
	public CtStatement getParentStatement(){
		return parentStatement;
	}
}
