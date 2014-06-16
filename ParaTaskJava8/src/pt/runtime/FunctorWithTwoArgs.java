package pt.runtime;

@FunctionalInterface
public interface FunctorWithTwoArgs<T> {
	T exec(Object arg1, Object arg2);
}
