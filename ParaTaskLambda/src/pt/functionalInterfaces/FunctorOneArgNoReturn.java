package pt.functionalInterfaces;

/**
 * <code>VoidFunctor</code> is a functional interface that receives 
 * any number of parameters of type <b>T1</b>, but does not return 
 * any values.
 * 
 * @param <T1> The type of parameters
 * 
 * @author Mostafa Mehrabi
 * @since  21/7/2015
 */
@FunctionalInterface
public interface FunctorOneArgNoReturn<T1> {
	public void exec(T1 arg1) throws Throwable;
}
