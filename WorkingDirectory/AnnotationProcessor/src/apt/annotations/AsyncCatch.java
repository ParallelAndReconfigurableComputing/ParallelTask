package apt.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;

@Repeatable(AsyncCatches.class)
@Target(ElementType.LOCAL_VARIABLE)
public @interface AsyncCatch {
	/**
	 */
    Class<? extends Exception>[] throwables();
    String[] handlers();
}
