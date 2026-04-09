package org.daxprotocol.core.exceptions;

public class DaxExecutorException extends DaxException {

    private static final String CODE = "DAXP-0035";
    private static final String PREFIX = "Message Executor : ";

    // 1. Basic constructor
    public DaxExecutorException(String details) {
        super(CODE, PREFIX + details);
    }

    // 2. Chaining constructor (e.g., wrapping a NumberFormatException)
    public DaxExecutorException(String details, Throwable cause) {
        super(CODE, PREFIX + details, cause);
    }

    // 3. Simple wrapping constructor
    public DaxExecutorException(Throwable cause) {
        super(CODE, cause);
    }

    // 4. Advanced constructor for internal control
    protected DaxExecutorException(String details, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(CODE, PREFIX + details, cause, enableSuppression, writableStackTrace);
    }
}