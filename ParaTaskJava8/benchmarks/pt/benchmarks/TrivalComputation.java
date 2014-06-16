package pt.benchmarks;

import static pt.runtime.Task.asTask;

import java.util.concurrent.atomic.AtomicInteger;

import pt.runtime.TaskID;

public class TrivalComputation {
	private static AtomicInteger index = new AtomicInteger(0);

	public static void trivalComputationByParaTaskWithLambda() {
		TaskID<Void> id = asTask(() -> trivalComputation()).start();
		Void v = id.getResult();
	}

	public static int getCurrentValue() {
		return index.get();
	}
	
	private static void trivalComputation() {
		index.incrementAndGet();
	}
}
