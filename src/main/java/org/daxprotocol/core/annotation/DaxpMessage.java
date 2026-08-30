package org.daxprotocol.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })

public @interface DaxpMessage {
    String description();
    String namespace() default "";
    String[] reqTag() default {};
    String[] respMsg() default {};

}
