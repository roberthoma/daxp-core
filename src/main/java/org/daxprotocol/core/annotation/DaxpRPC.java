package org.daxprotocol.core.annotation;

import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })

public @interface  DaxpRPC {
    int    tagId();
    String uiLabel() default "";

}
