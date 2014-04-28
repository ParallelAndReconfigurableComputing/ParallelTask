package pt.runtime;

@FunctionalInterface
public interface FunctionInterExceptionHandler {
	void doWork(Throwable e);
}
