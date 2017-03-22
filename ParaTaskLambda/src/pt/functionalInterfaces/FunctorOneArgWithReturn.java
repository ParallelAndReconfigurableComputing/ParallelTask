package pt.functionalInterfaces;

/**
 * <code>StandardFunctor</code> is a functional interface that receives any number of 
 * parameters of type <b>T1</b>, and returns a value of type <b>R</b>.
 * 
 * @param <R> The type of return value
 * @param <T1> The type of parameters received by the function
 *
 * @author Mostafa Mehrabi
 * @since  21/7/2015
 */

@FunctionalInterface
public interface FunctorOneArgWithReturn<R, T1>{
	R exec(T1 arg) throws Throwable;
}
