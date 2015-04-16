package pt.runtime;

@FunctionalInterface
public interface FunctorWithOneArg<T> {
	T exec(Object arg);
}
