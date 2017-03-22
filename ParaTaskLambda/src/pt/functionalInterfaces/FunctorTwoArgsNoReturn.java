package pt.functionalInterfaces;

@FunctionalInterface
public interface FunctorTwoArgsNoReturn<T1, T2> {
	void exec(T1 arg1, T2 arg2) throws Throwable;
}
