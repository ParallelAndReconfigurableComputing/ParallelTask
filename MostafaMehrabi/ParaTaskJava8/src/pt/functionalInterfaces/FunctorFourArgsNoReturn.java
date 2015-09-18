package pt.functionalInterfaces;

@FunctionalInterface
public interface FunctorFourArgsNoReturn <T1, T2, T3, T4> {
	void exec(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
}
