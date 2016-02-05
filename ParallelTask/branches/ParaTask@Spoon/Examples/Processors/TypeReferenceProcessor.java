package spoon.examples.analysis.processing;

import java.util.HashSet;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.processing.Severity;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.ReferenceTypeFilter;

/**
 * Reports messages to tell what types are used by a given type.
 */
public class TypeReferenceProcessor extends AbstractProcessor<CtType<?>> {

	public void process(CtType<?> type) {
		Set<CtTypeReference<?>> typeRefs = type.getUsedTypes(true);
/*		for (CtTypeReference<?> typeRef : Query
				.getReferences(type, new ReferenceTypeFilter<CtTypeReference>(
						CtTypeReference.class))) {
			if (!(typeRef.isPrimitive()
					|| (typeRef instanceof CtArrayTypeReference)
					|| typeRef.toString().equals(CtTypeReference.NULL_TYPE_NAME) || (typeRef
					.getPackage() != null && "java.lang".equals(typeRef
					.getPackage().toString())))
					&& !typeRef.getPackage().equals(
							type.getPackage().getReference())) {
				typeRefs.add(typeRef);
			}
		}
*/		if (typeRefs.size() > 0)
			getEnvironment().report(this, Severity.MESSAGE,
					"type " + type.getReference() + " uses " + typeRefs);
	}

	public static Set<CtTypeReference<?>> getTypeReferences(CtType<?> type) {
		System.out.print(type.getReference() + " => ");
		Set<CtTypeReference<?>> typeRefs = new HashSet<CtTypeReference<?>>();
		for (CtTypeReference<?> typeRef : Query
				.getReferences(type, new ReferenceTypeFilter<CtTypeReference<?>>(
						CtTypeReference.class))) {
			if (!(typeRef.isPrimitive()
					|| (typeRef instanceof CtArrayTypeReference)
					|| typeRef.toString().equals(CtTypeReference.NULL_TYPE_NAME) || (typeRef
					.getPackage() != null && "java.lang".equals(typeRef
					.getPackage().toString())))
					&& !typeRef.getPackage().equals(
							type.getPackage().getReference())) {
				typeRefs.add(typeRef);
			}
		}
		System.out.println(typeRefs);
		return typeRefs;
	}

}