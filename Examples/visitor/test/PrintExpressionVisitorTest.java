package spoon.examples.visitor.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;
import spoon.examples.stack.src.Stack;
import spoon.examples.visitor.src.PrintExpressionVisitor;
import spoon.examples.visitor.src.expression.Expression;
import spoon.examples.visitor.src.expression.IntegerExpression;
import spoon.examples.visitor.src.expression.MinusOperatorExpression;
import spoon.examples.visitor.src.expression.PlusOperatorExpression;

public class PrintExpressionVisitorTest extends TestCase {

	Stack<Integer> stack;

	PrintStream oldOut;

	ByteArrayOutputStream fos;

	@Override
	protected void setUp() throws Exception {
		oldOut = System.out;
		fos = new ByteArrayOutputStream(100);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
	}

	public void testPrint() throws Exception {
		try {
			Expression e = new PlusOperatorExpression(new IntegerExpression(1),
					new MinusOperatorExpression(new IntegerExpression(2),
							new IntegerExpression(3)));
			new PrintExpressionVisitor().visitExpression(e);
			assertEquals(fos.toString(), " ( 1 +  ( 2 - 3 )  ) ");
		} catch (Exception e) {
			fail();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		System.setOut(oldOut);
		fos.close();
	}

}