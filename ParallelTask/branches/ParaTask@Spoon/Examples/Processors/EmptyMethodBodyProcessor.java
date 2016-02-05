package spoon.examples.analysis.processing;

import spoon.processing.AbstractProcessor;
import spoon.processing.Severity;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.ModifierKind;

/**
 * Reports warnings when empty methods are found.
 */
public class EmptyMethodBodyProcessor extends
		AbstractProcessor<CtExecutable<?>> {

	public void process(CtExecutable<?> element) {
		if (element.getParent(CtClass.class) != null
				&& !element.getModifiers().contains(ModifierKind.ABSTRACT)
				&& element.getBody().getStatements().size() == 0) {
			getFactory().getEnvironment().report(this, Severity.ERROR, element,
					"!!! This is not an compilation or execution error this is just for testing!!!! Empty block");
		}
	}

}