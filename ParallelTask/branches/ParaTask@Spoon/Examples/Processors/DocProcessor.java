package spoon.examples.analysis.processing;

import spoon.processing.AbstractProcessor;
import spoon.processing.Severity;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtSimpleType;
import spoon.reflect.declaration.ModifierKind;

/**
 * Reports warnings when undocumented elements are found.
 */
public class DocProcessor extends AbstractProcessor<CtElement> {

	public void process(CtElement element) {
		if (element instanceof CtSimpleType || element instanceof CtField
				|| element instanceof CtExecutable) {
			if (((CtModifiable) element).getModifiers().contains(
					ModifierKind.PUBLIC)
					|| ((CtModifiable) element).getModifiers().contains(
							ModifierKind.PROTECTED)) {
				// if(element instanceof CtMethod) {
				// ((CtMethod)element).getReference().getOverloadingExecutable()
				// }
				if (element.getDocComment() == null
						|| element.getDocComment().equals("")) {
					getFactory().getEnvironment().report(this,
							Severity.WARNING, element, "undocument element");
				}
			}
		}
	}

}