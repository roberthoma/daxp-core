package org.daxprotocol.core.annotation;



//use lie daxpField but only tag definition
 // like
 //TODO @DaxpTag(.......) // definition of attributes Only on one DaxpTag or DaxpField not both
 //public static final int  RX_INSTRUMENT_ID   = 4012;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface  DaxpTag {
    String context() default ""; //>>>> empty mean  DaxpConfig.APP_CONTEXT_SYMBOL;
    String uiLabel() default "";
    String dataType()  default "";
    boolean readOnly() default false;
}

