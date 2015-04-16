package pt.runtime;
//couldn't we just use one functor that receives a list of arguments,
//and returns a list of return types!
@FunctionalInterface
public interface Functor<T> {
	T exec();
}
