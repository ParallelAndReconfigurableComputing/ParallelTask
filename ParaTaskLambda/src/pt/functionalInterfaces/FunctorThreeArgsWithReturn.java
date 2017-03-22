package pt.functionalInterfaces;

@FunctionalInterface
public interface FunctorThreeArgsWithReturn<R, T1, T2, T3> {
	R exec (T1 arg1, T2 arg2, T3 arg3) throws Throwable;
}
