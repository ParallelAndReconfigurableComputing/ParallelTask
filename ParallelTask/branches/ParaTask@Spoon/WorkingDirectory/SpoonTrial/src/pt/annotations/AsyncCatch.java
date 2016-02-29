package pt.annotations;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.LOCAL_VARIABLE;


@Target(LOCAL_VARIABLE)
public @interface AsyncCatch {
    Class<? extends Exception> throwable();
    String handler();
}
