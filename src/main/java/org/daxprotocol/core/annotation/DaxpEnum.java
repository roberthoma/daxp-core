package org.daxprotocol.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
@Documented
public @interface DaxpEnum {
    int    tagId();
    String name()        default "";
    String description() default "";
}


//It can be regular class but is necessary use DaxpEnumValue