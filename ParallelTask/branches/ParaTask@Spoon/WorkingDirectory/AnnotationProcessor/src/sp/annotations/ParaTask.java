package sp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface ParaTask {
	TaskScheduingPolicy schedulingPolicy() default TaskScheduingPolicy.MixedScheduling;
	int numberOfThreads() default 0;
}
