package sp.processors;

import sp.annotations.Future;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.declaration.CtVariable;

public class ArrayProcessor extends AbstractAnnotationProcessor<Future, CtVariable<?>> {

	@Override
	public void process(Future arg0, CtVariable<?> arg1) {
		// TODO Auto-generated method stub
		System.out.println("Array processor");
	}

}
