import static pt.runtime.Task.*;

import java.util.concurrent.ExecutionException;

import pt.runtime.CurrentTask;
import pt.runtime.FunctorVoid;
import pt.runtime.TaskID;
import pt.runtime.TaskIDGroup;

public class HelloWorld {

	private void notifyFunc() {
		System.out.println("in notifyFunc");
	}

	public static void main(String[] args) {

		System.out.println("(1)");

		hello("Sequential");

		System.out.println("(2)");

		TaskID<Void> id1 = asTask(HelloWorld::oneoff_hello).start();

		System.out.println("(3)");

		HelloWorld hw = new HelloWorld();

		TaskID<Void> id2 = asMultiTask(HelloWorld::multi_hello, 8).withHandler(
				hw::notifyFunc).start();

		System.out.println("(4)");

		TaskID<Void> id3 = asIOTask(HelloWorld::interactive_hello).start();

		System.out.println("(5)");

		TaskID<Void> id4 = asTask(new HelloWorld()::oneoff_hello2).start();

		System.out.println("(6)");

		TaskIDGroup<Void> g = new TaskIDGroup<>(4);
		g.add(id1);
		g.add(id2);
		g.add(id3);
		g.add(id4);
		System.out.println("** Going to wait for the tasks to execute... ");
		try {
			g.waitTillFinished();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("** Done! All tasks have now completed.");
	}

	private static void hello(String name) {
		System.out.println("Hello from " + name);
	}

	private static void oneoff_hello() {
		hello("One-off Task");
	}

	private void oneoff_hello2() {
		System.out.println("Hello from oneoff_hello2");
	}

	private static void multi_hello() {
		hello("Multi-Task [subtask " + CurrentTask.relativeID() + "]  [thread "
				+ CurrentTask.currentThreadID() + "]  [globalID "
				+ CurrentTask.globalID() + "]  [mulTaskSize "
				+ CurrentTask.multiTaskSize() + "]  [TaskID "
				+ CurrentTask.currentTaskID() + "]  [ISinside? "
				+ CurrentTask.insideTask() + "]  [progress "
				+ CurrentTask.getProgress() + "]");
	}

	public static void interactive_hello() {
		hello("Interactive Task");
	}
}
