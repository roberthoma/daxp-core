package org.daxprotocol.core.exceptions;

public class DaxFrameParserException extends DaxException {

    private static final String CODE = "DAXP-0005";
    private static final String PREFIX = "Frame Parser : ";

    // 1. Basic constructor
    public DaxFrameParserException(String details) {
        super(CODE, PREFIX + details);
    }

    // 2. Chaining constructor (e.g., wrapping a NumberFormatException)
    public DaxFrameParserException(String details, Throwable cause) {
        super(CODE, PREFIX + details, cause);
    }

    // 3. Simple wrapping constructor
    public DaxFrameParserException(Throwable cause) {
        super(CODE, cause);
    }

    // 4. Advanced constructor for internal control
    protected DaxFrameParserException(String details, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(CODE, PREFIX + details, cause, enableSuppression, writableStackTrace);
    }
}