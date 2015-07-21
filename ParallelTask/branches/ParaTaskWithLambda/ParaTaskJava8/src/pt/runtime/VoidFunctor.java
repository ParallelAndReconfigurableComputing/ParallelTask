package pt.runtime;

/**
 * <code>VoidFunctor</code> is a functional interface that receives 
 * any number of parameters of type <b>P</b>, but does not return 
 * any values.
 * 
 * @param <P> The type of parameters
 * 
 * @author Mostafa Mehrabi
 * @since  21/7/2015
 */
@FunctionalInterface
public interface VoidFunctor <P> {
	public void exec(P...params);
}
