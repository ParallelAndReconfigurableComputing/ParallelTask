/*
 * This class contains all the static variables and methods which would
 * be required by the Task Execution Logger for ParaTask, in order to
 * capture relevant execution details during runtime.
 */

package paratask.logger;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TaskLogger {

	// List to store all the logs during exection
	public static List<StringBuffer> TaskDetailsArray = Collections
			.synchronizedList(new ArrayList<StringBuffer>());

	// debug flag used to facilitate starting and stopping of logging
	public static Boolean debugFlag = false;

	// debug flag to facilitate starting and immediate force stop of logging
	public static Boolean debugFlagMain = false;

	// not used at present
	public static int startIndex = -1;

	// map to enable determining parent-child relations between tasks
	public static ConcurrentHashMap<Long, Integer> threadTaskMap = new ConcurrentHashMap<Long, Integer>();

	// not used currently
	public static Boolean getDebugFlag() {
		return debugFlag;
	}

	// not used currently
	public static void setDebugFlag(Boolean debugFlag) {
		TaskLogger.debugFlag = debugFlag;
		TaskLogger.debugFlagMain = debugFlag;
	}

	// method called to start logging
	public static void startDebug() {
		TaskLogger.debugFlag = true;
		TaskLogger.debugFlagMain = true;
	}

	/*
	 * method called to stop logging Details of tasks already created will still
	 * be logged
	 */
	public static void stopDebug() {
		TaskLogger.debugFlag = false;
	}

	/*
	 * method called to stop logging immediately Details of tasks already
	 * created will not be logged
	 */
	public static void stopDebugNow() {
		TaskLogger.debugFlagMain = false;
		TaskLogger.debugFlag = false;
	}

	/*
	 * write all the information accumulated in the TaskDetailsArray to a log
	 * file
	 */
	public static void writeArrayToFile() {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("log" + System.currentTimeMillis()
							+ ".txt"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			writer.write("startLog\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (StringBuffer taskDets : paratask.logger.TaskLogger.TaskDetailsArray) {
			if (taskDets != null) {
				/*
				 * write the details of this task to the log file only if it is
				 * either a group or multitask group or if it is a task whole
				 * entire life-cycle has been recorded
				 */
				if ((taskDets.indexOf("TaskGroup=") >= 0)
						|| ((taskDets.indexOf("started") >= 0) && (taskDets
								.indexOf("ended") >= 0))) {
					System.out.println(taskDets);
					try {
						writer.write(taskDets + "\n");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}

		try {
			writer.write("endLog");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
