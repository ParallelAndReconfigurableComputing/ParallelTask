package spoon.examples.factory.processing;

import java.util.ArrayList;
import java.util.List;

import spoon.examples.factory.src.Factory;
import spoon.processing.AbstractProcessor;
import spoon.processing.Severity;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.CtSimpleType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

public class FactoryProcessor extends AbstractProcessor<CtNewClass<?>> {

	public void process(CtNewClass<?> newClass) {
		// skip factory creation
		if (getFactoryType().isAssignableFrom(
				newClass.getExecutable().getDeclaringType()))
			return;
		// skip creations in factories
		if (getFactoryType().isAssignableFrom(
				((CtSimpleType<?>) newClass.getParent(CtSimpleType.class))
						.getReference()))
			return;
		// only report for types created by the factory
		for (CtTypeReference<?> t : getCreatedTypes()) {
			if (t.isAssignableFrom(newClass.getType())) {
				getFactory().getEnvironment().report(this, Severity.WARNING,
						newClass, "wrong use of factory pattern");
			}
		}

	}

	CtTypeReference<?> factoryType = null;

	protected CtTypeReference<?> getFactoryType() {
		if (factoryType == null) {
			factoryType = getFactory().Type().createReference(Factory.class);
		}
		return factoryType;
	}

	List<CtTypeReference<?>> createdTypes;

	private List<CtTypeReference<?>> getCreatedTypes() {
		if (createdTypes == null) {
			createdTypes = new ArrayList<CtTypeReference<?>>();
			for (CtExecutableReference<?> m : getFactoryType().getDeclaredExecutables()) {
				createdTypes.add(m.getType());
			}
		}
		return createdTypes;
	}

}