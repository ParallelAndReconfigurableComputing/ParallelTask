package sp.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface Future {
    String   depends()   default "";
    String   notifies()  default "";
    String   reduction() default "";
    boolean elasticTaskGroup() default false;
    TaskInfoType taskType()  default TaskInfoType.ONEOFF;
    int      taskCount() default 0;
}
