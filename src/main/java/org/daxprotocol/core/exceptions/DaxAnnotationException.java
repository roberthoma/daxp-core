package org.daxprotocol.core.exceptions;

public class DaxAnnotationException extends DaxException {

    private static final String CODE = "DAXP-0055";
    private static final String PREFIX = "Annotation scanner : ";

    // 1. Basic constructor
    public DaxAnnotationException(String details) {
        super(CODE, PREFIX + details);
    }

    // 2. Chaining constructor (e.g., wrapping a NumberFormatException)
    public DaxAnnotationException(String details, Throwable cause) {
        super(CODE, PREFIX + details, cause);
    }

    // 3. Simple wrapping constructor
    public DaxAnnotationException(Throwable cause) {
        super(CODE, cause);
    }

    // 4. Advanced constructor for internal control
    protected DaxAnnotationException(String details, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(CODE, PREFIX + details, cause, enableSuppression, writableStackTrace);
    }
}