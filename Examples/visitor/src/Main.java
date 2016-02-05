package spoon.examples.visitor.src;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import spoon.examples.visitor.src.expression.Expression;
import spoon.examples.visitor.src.expression.IntegerExpression;
import spoon.examples.visitor.src.expression.MinusOperatorExpression;
import spoon.examples.visitor.src.expression.PlusOperatorExpression;

public class Main {

	public static void main(String[] args) {
		ByteArrayOutputStream fos = new ByteArrayOutputStream(100);
		PrintStream ps = new PrintStream(fos);
		System.setErr(ps);
		Expression e = new PlusOperatorExpression(new IntegerExpression(1),
				new MinusOperatorExpression(new IntegerExpression(2),
						new IntegerExpression(3)));
		System.setOut(ps);
		new PrintExpressionVisitor().visitExpression(e);
		System.out.print("\n");
		System.err.println(fos.toString());
	}

}