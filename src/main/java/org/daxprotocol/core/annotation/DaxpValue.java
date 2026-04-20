package org.daxprotocol.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface DaxpValue {
    String value() default "";      //context plus tagId "FIX:53"
    int tagId() default -1;                   // It can be define by @DaxpTag
    String context() default "";   // Empty mean  DaxpConfig.APP_CONTEXT_SYMBOL;
    String uiLabel() default "";
    String name() default "";      //Use for rename field name , example :used for JSON cast.
}