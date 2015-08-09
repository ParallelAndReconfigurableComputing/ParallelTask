package pt.runtime;

@FunctionalInterface
public interface FunctorVoidWithOneArg<T> {
	void exec(T arg);
}
