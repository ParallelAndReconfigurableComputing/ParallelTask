package spoon.examples.visitor.src;

import spoon.examples.visitor.src.expression.BinaryOperatorExpression;
import spoon.examples.visitor.src.expression.Expression;
import spoon.examples.visitor.src.expression.IntegerExpression;
import spoon.examples.visitor.src.expression.MinusOperatorExpression;
import spoon.examples.visitor.src.expression.PlusOperatorExpression;

/**
 * This simple visitor prints out an expression.
 */
public class PrintExpressionVisitor {

    public void visitExpression(Expression e) {
        ((VisitedElement)e).accept(this);
    }
    
    public void visitIntegerExpression(IntegerExpression e) {
        System.out.print(e.getValue());
    }

    public void visitPlusOperatorExpression(PlusOperatorExpression e) {
        System.out.print(" ( ");
        visitExpression(e.getLeft());
        System.out.print(" + ");
        visitExpression(e.getRight());
        System.out.print(" ) ");
    }

    public void visitMinusOperatorExpression(MinusOperatorExpression e) {
        System.out.print(" ( ");
        visitExpression(e.getLeft());
        System.out.print(" - ");
        visitExpression(e.getRight());
        System.out.print(" ) ");
    }

    public void visitBinaryOperatorExpression(BinaryOperatorExpression e) {
    }

}