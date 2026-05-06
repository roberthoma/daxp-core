package org.daxprotocol.core.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE, ElementType.MODULE, ElementType.PARAMETER, ElementType.TYPE})
public @interface DaxpDeprecated {
    String since() default ""; //This same as original Deprecated
    boolean forRemoval() default false; //This same as original  Deprecated
    String  removalVersion() default "";
    String  replacement() default "";
    String  reason() default "";
}


// level = DaxDeprecationLevel.WARNING,
//

//public enum DaxDeprecationLevel {
//    WARNING,   // działa, ale parser/log zgłasza warning
//    ERROR,     // w strict mode błąd
//    HIDDEN,    // nie pokazuj w generated GUI/API docs
//    REMOVED    // już nieobsługiwane
//}