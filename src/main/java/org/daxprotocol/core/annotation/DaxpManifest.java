package org.daxprotocol.core.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD, TYPE })
@Documented
public @interface DaxpManifest {
//    String value() default "";      // Schema symbol
//    String name() default "";       // Use for rename schema name
//    String description() default "";
}

