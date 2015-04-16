package pt.runtime;

@FunctionalInterface
public interface Functor<T> {
	T exec();
}
