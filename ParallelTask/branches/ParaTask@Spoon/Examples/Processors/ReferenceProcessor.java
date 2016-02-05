package spoon.examples.analysis.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import spoon.compiler.Environment;
import spoon.processing.AbstractProcessor;
import spoon.processing.FactoryAccessor;
import spoon.processing.Severity;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtSimpleType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.ReferenceTypeFilter;

/**
 * Finds circular dependencies between packages and report warnings when found.
 */
public class ReferenceProcessor extends AbstractProcessor<CtPackage> {

	List<CtTypeReference<?>> ignoredTypes = new ArrayList<CtTypeReference<?>>();

	@Override
	public void init() {
		ignoredTypes
				.add(getFactory().Type().createReference(Environment.class));
		ignoredTypes.add(getFactory().Type().createReference(Factory.class));
		ignoredTypes.add(getFactory().Type().createReference(
				FactoryAccessor.class));
	}

	Map<CtPackageReference, Set<CtPackageReference>> packRefs = new HashMap<CtPackageReference, Set<CtPackageReference>>();

	public void process(CtPackage element) {
		CtPackageReference pack = element.getReference();
		Set<CtPackageReference> refs = new TreeSet<CtPackageReference>();
		for (CtSimpleType<?> t : element.getTypes()) {
			for (CtTypeReference<?> tref : Query.getReferences(t,
					new ReferenceTypeFilter<CtTypeReference<?>>(
							CtTypeReference.class))) {
				if (tref.getPackage() != null
						&& !tref.getPackage().equals(pack)) {
					if (ignoredTypes.contains(tref))
						continue;
					refs.add(tref.getPackage());
				}
			}
		}
		if (refs.size() > 0)
			packRefs.put(pack, refs);
		// if (getFactory().getEnvironment().isVerbose())
		// System.out.println(refs);
	}

	@Override
	public void processingDone() {
		// getFactory().getEnvironment().reportMessage(
		// "package dependencies: " + packRefs);
		// getFactory().getEnvironment().reportMessage(
		// "looking for circular package dependencies...");
		// looking for circular dependecies
		for (CtPackageReference p : packRefs.keySet()) {
			Stack<CtPackageReference> path = new Stack<CtPackageReference>();
			path.push(p);
			scanDependencies(path);
		}
	}

	Set<CtPackageReference> scanned = new TreeSet<CtPackageReference>();

	void scanDependencies(Stack<CtPackageReference> path) {
		// getFactory().getEnvironment().reportMessage("scanning " + path);
		CtPackageReference ref = path.peek();
		// return if already scanned
		if (scanned.contains(ref))
			return;
		scanned.add(ref);
		Set<CtPackageReference> refs = packRefs.get(ref);
		if (refs != null) {
			for (CtPackageReference p : refs) {
				if (path.contains(p)) {
					List<CtPackageReference> circularPath = new ArrayList<CtPackageReference>(
							path.subList(path.indexOf(p), path.size()));
					circularPath.add(p);
					getEnvironment().report(this,
							Severity.WARNING,
							"circular dependency " + circularPath);
					break;
				} else {
					path.push(p);
					scanDependencies(path);
					path.pop();
				}
			}
		}
	}

}