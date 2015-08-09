package pt.runtime;
/**
 * <code>NoParamVoidFunctor</code> is a functional interface that neither receives any
 * parameters, nor returns any values.
 *
 * 
 * @author Mostafa Mehrabi
 * @since  21/7/2015
 */

@FunctionalInterface
public interface NoParamVoidFunctor {
	public void exec();
}
