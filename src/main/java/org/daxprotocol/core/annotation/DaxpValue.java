package org.daxprotocol.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface DaxpValue {
    String context() default "";   // Empty mean  DaxpConfig.APP_CONTEXT_SYMBOL;
    int    tagId();                // It can be define by @DaxpTag
    String uiLabel() default "";
}

