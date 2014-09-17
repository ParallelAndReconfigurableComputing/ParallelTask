package pt.runtime;

@FunctionalInterface
public interface FunctorVoidWithTwoArgs<T1, T2> {
	void exec(T1 arg1, T2 arg2);
}
