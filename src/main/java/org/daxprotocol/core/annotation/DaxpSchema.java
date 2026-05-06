package org.daxprotocol.core.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
@Documented
public @interface DaxpSchema {
    String value() default "";      // Shor name
    String name() default "";      //Use for rename field name , example :used for JSON cast.
    String description() default "";
}
