package processors;

import annotations.Future;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtVariable;

public class FutureProcessor<T> extends AbstractAnnotationProcessor<Future, CtVariable<T>>{

	@Override
	public void process(Future annotation, CtVariable<T> element) {
		CtExpression<T> expression = element.getDefaultExpression();
	}

}
