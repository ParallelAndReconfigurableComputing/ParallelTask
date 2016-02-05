package spoon.examples.nton.processing;

import spoon.examples.nton.annotation.Nton;
import spoon.examples.nton.template.NtonCodeTemplate;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.template.Substitution;

public class NtonProcessor extends
		AbstractAnnotationProcessor<Nton, CtClass<?>> {

	public void process(Nton nton, CtClass<?> cl) {
		NtonCodeTemplate template = new NtonCodeTemplate(cl.getReference(),
				nton.n());
		Substitution.insertAll(cl, template);
		for (CtConstructor<?> c : cl.getConstructors()) {
			c.getBody().insertEnd((CtStatement)
					Substitution.substituteMethodBody(cl, template,
							"initializer"));
		}
	}

}