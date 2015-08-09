package pt.runtime;

/**
 * <code>NoParamFunctor</code> is a functional interface that does not 
 * recieve any parameters, but returns a value of type <b>R</b>.
 * 
 * @param <R> The type of return value
 * 
 * @author Mostafa Mehrabi
 * @since  21/7/2015
 */

@FunctionalInterface
public interface NoParamFunctor <R> {
	public R exec();
}
