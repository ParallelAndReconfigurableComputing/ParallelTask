package sp.processors;

import java.util.ArrayList;
import java.util.List;

import sp.processors.APTUtils.ExpressionRole;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;

public class ASTNode {
	private class Expression{
		CtExpression<?> expression;
		ExpressionRole  expressionRole;
	}
	
	CtStatement parentStatement = null;
	CtStatement thisStatement = null;
	List<Expression> statementExpressions = null;
	
	public ASTNode(CtStatement statement){
		thisStatement = statement;
		parentStatement = statement.getParent(CtStatement.class);
		statementExpressions = new ArrayList<>();
	}
	
	public void registerExpression (CtExpression<?> expression, ExpressionRole role){
		Expression newExpression = new Expression();
		newExpression.expression = expression;
		newExpression.expressionRole = role;
	}
	
	public int numberOfExpressions(){
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
