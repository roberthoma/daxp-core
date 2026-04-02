package org.daxprotocol.core.exceptions;

public class DaxMsgParserException extends DaxException {

    private static final String CODE = "DAXP-0005";
    private static final String PREFIX = "Message Parser : ";

    // 1. Basic constructor
    public DaxMsgParserException(String details) {
        super(CODE, PREFIX + details);
    }

    // 2. Chaining constructor (e.g., wrapping a NumberFormatException)
    public DaxMsgParserException(String details, Throwable cause) {
        super(CODE, PREFIX + details, cause);
    }

    // 3. Simple wrapping constructor
    public DaxMsgParserException(Throwable cause) {
        super(CODE, cause);
    }

    // 4. Advanced constructor for internal control
    protected DaxMsgParserException(String details, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(CODE, PREFIX + details, cause, enableSuppression, writableStackTrace);
    }
}