package pt.runtime;

/**
 * <code>StandardFunctor</code> is a functional interface that receives any number of 
 * parameters of type <b>P</b>, and returns a value of type <b>R</b>.
 * 
 * @param <R> The type of return value
 * @param <P> The type of parameters received by the function
 *
 * @author Mostafa Mehrabi
 * @since  21/7/2015
 */

@FunctionalInterface
public interface StandardFunctor<R, P>{
	public R exec(P...params);
}
