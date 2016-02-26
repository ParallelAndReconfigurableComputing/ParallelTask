package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface Future {
	
	/**
	 * This attribute specifies if a <code>Task</code> is supposed
	 * to be executed as a multi-task (i.e., SIMD).
	 * <p>
	 * <b>Warning</b>: The tasks of a multi-task share the same data
	 * structure. Therefore, it is necessary to ensure thread-safety
	 * of the container for multi-tasks.
	 * <p>
	 * <b>default</b>: false
	 * 
	 * @return <code>true</code> if the <code>Task</code> is multi-task,
	 * <code>false</code> otherwise.	 
	 */
	boolean	    isMultiple() 		default false;
	
	/**
	 * This attribute indicates if a <code>Task</code> is based on 
	 * <b>I/O</b> operations. Parallel Task uses a different thread
	 * pool for interactive tasks. 
	 * <p>
	 * <b>default</b>: false
	 * 
	 * @return <code>true</code> if a <code>Task</code> is interactive,
	 * <code>false</code> otherwise.
	 */
	boolean 	isInteractive() 	default false;
	
	/**
	 * This attribute indicates if a <code>Task</code> defined by a
	 * <b>lambda expression</b> should receive its corresponding parameters
	 * by reference (i.e., shallow copy), or copy them (i.e., deep copy).
	 * <p>
	 * <b>default</b>: false
	 * 
	 * @return <code>true</code> if the copy is a shallow copy, <code>false</code>
	 * otherwise.
	 */
	boolean     varByReference()    default false;
	
	/**
	 * This attribute indicates the number of sub-tasks of a multi-task. 
	 * This attribute is automatically ignored if the task is not a multi-task.
	 * <p>
	 * <b>default</b>: 1
	 * 
	 * @return int - the number of sub-tasks of a multi-task.
	 */
	int     	numOfMultiTask() 	default 1;
	
	/**
	 * This attribute is an array of strings, each of which is the name of another
	 * <b>future variable</b>.
	 * 
	 * @return String[] - the names of future variables.
	 */
	String[]    dependsOn()			default "";
}
