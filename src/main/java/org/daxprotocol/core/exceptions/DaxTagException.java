package org.daxprotocol.core.exceptions;

public class DaxTagException extends DaxException {

    private static final String CODE = "DAXP-0155";
    private static final String PREFIX = "Tag creation : ";

    // 1. Basic constructor
    public DaxTagException(String details) {
        super(CODE, PREFIX + details);
    }

    // 2. Chaining constructor (e.g., wrapping a NumberFormatException)
    public DaxTagException(String details, Throwable cause) {
        super(CODE, PREFIX + details, cause);
    }

    // 3. Simple wrapping constructor
    public DaxTagException(Throwable cause) {
        super(CODE, cause);
    }

    // 4. Advanced constructor for internal control
    protected DaxTagException(String details, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(CODE, PREFIX + details, cause, enableSuppression, writableStackTrace);
    }
}