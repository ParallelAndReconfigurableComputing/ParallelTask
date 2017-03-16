package apt.processors;

import spoon.reflect.code.CtStatement;
import spoon.reflect.visitor.Filter;

/**
 * This customized filter is used by Spoon for finding a specific statement within
 * user code, in order to insert statements before or after that. 
 * Latest versions of Spoon allow direct insertion of code elements via the CtStatement
 * objects per se, therefore try inserting without the filter first. If not possible, 
 * then get the parent CtBlock object and then insert after the corresponding statement
 * using this filter. 
 * 
 * @author Mostafa Mehrabi
 * @since  2016
 * @param <T>
 */
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

	@SuppressWarnings("unchecked")
	public Class<T> getType() {
		return (Class<T>) targetElement.getClass(); 
	}
}
