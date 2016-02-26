package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Task {	
	boolean isInteractive() default false;
	boolean isMultiple() default false;
	int numOfMultiTask() default 0;
}
