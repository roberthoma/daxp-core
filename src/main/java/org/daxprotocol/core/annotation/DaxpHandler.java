package org.daxprotocol.core.annotation;

// TODO DaxpHandler
//(context = "CMR", operation = "findById")

//    String messageType();

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DaxpHandler {
    String value();
//    String context() default "";
}

    /*
    Tip: Dodaj w nim parametr async = true/false, aby obsłużyć kolejki

    // Metoda inicjująca wysyłkę dużej paczki przez Kafkę

    public DaxMessage initiateLargeDataTransfer(DaxMessage msg) {
        String token = generateToken();
        // ... logika wrzucenia zlecenia na kolejkę ...

        // Zwracamy tylko potwierdzenie z tokenem
        return provider.getMessageFactory()
                .toDaxMessage(CRM_ACK_TOKEN, token);
    }

*/

