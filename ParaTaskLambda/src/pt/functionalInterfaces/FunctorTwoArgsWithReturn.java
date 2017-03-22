package pt.functionalInterfaces;

@FunctionalInterface
public interface FunctorTwoArgsWithReturn<R, T1, T2> {
	R exec(T1 arg1, T2 arg2) throws Throwable;
}
