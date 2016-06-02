package sp.annotations;

import spoon.reflect.code.CtStatement;
import spoon.reflect.visitor.Filter;

public class StatementMatcherFilter<T extends CtStatement> implements Filter<T> {

	CtStatement targetElement = null;
	public StatementMatcherFilter(T element) {
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
