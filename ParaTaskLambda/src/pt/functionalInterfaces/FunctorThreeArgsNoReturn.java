package pt.functionalInterfaces;

@FunctionalInterface
public interface FunctorThreeArgsNoReturn<T1, T2, T3> {
	void exec(T1 arg1, T2 arg2, T3 arg3) throws Throwable;
}
