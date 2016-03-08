package pt.annotations;

import org.eclipse.jdt.core.dom.ThisExpression;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;

public class AnnotationProcessingFilter<T extends CtStatement> implements Filter<T> {

	CtStatement targetElement = null;
	public AnnotationProcessingFilter(T element) {
		this.targetElement = element;
	}
	@Override
	public boolean matches(T element) {
		if (element.equals(targetElement))
			return true;
		return false;
	}

	@Override
	public Class<T> getType() {
		return (Class<T>) targetElement.getClass(); 
	}
}
