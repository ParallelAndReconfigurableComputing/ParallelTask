package pt.java8;

import java.io.IOException;

import pt.runtime.Future;
import static pt.runtime.Task.*;

public class ExperimentJava8 {

	public int task() {
		//do computation
		return 0;
	}
	
	public int taskInt(int i) {
		//do computation
		return 0;
	}
	
	public int handlerInt() {
		return 0;
	}
	
	public int handlerInt2() {
		return 0;
	}
	
	public void handlerVoid() {
		
	}
	
	public void handleException(Throwable exception) {
	}
	
	public void handleException2(Throwable ex) {
	}

    public void handleIOException(Throwable ex) {
    }
	
	public void test() {
        // 1. "task" is a common method public int task(), and
        //   this::task is a Method Reference in Java 8, like a lambda.
        // 2. asTask, asIOTask are are static function in Task.java,
        //   and they are imported by "import static pt.experiments.Task.*;",
        //   then we can call them in other classes directly as follows:
		Future id1 = asTask(this::task).run();

        // 1. "() -> taskInt(3)" is a lambda in Java 8.
        // 2. asIOTask can be removed, if we use annotations like @Task(type="IOTask"):
        //  @Task(type="IOTask")
        //  public int taskInt(int i) { ... }
		Future id2 = asIOTask(() -> taskInt(3))
				.dependsOn(id1)     // set up a dependency this way
				.run();
		
		Future id3 = asTask(this::task)
				.dependsOn(id1, id2) // multiple dependencies
				.run();
		
		ExperimentJava8 anotherObj = new ExperimentJava8();
		Future id4 = asTask(() -> anotherObj.task())
				.dependsOn(id2)
				.run();

        // we can also specify a Method Reference of other object:
        // anotherObj::task, which is exact the same as the notify clause of ParaTask
		Future id5 = asMultiTask(anotherObj::task, 8)
			.dependsOn(id1)
			.withHandler(this::handlerInt)      // set up a "notify" handler this way
			.withHandler(() -> {
                return this.handlerInt2();
            })
			.withHandler(this::handlerVoid)
			.asyncCatch(RuntimeException.class, e -> handleException(e)) //handle an exception
            .asyncCatch(IOException.class, ex -> handleIOException(ex)) // handle another exception
            .asyncCatch(IOException.class, this::handleException2)
			.run();
	}
}
