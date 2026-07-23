package org.daxprotocol.core.annotation;

import org.daxprotocol.core.datatype.DaxDataType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE })
@Documented
public @interface DaxpCollection {
    String value()       default "";
    int    tagId()       default -1;
    String namespace()     default "";
    String name()        default "";
    String description() default "";
    DaxDataType keyDataType()  default DaxDataType.UNKNOWN;
    DaxDataType valueDataType()  default DaxDataType.UNKNOWN;
}


//It can be regular class but is necessary use DaxpEnumValue