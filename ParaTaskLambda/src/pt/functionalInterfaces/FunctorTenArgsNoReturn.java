package pt.functionalInterfaces;

@FunctionalInterface
public interface FunctorTenArgsNoReturn<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
	void exec(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10) throws Throwable;
}
