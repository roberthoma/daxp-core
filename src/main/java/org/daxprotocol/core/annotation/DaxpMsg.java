package org.daxprotocol.core.annotation;

import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })

public @interface DaxpMsg {
    String description();
    String context() default "";
    String[] reqTag() default {};
    String[] respMsg() default {};

}
