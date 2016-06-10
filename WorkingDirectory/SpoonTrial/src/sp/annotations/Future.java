package sp.annotations;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

@Target({LOCAL_VARIABLE})
public @interface Future {
    String   depends()   default "";
    String   notifies()  default "";
    TaskInfoType taskType()  default TaskInfoType.ONEOFF;
    int      taskCount() default 0;
}
