package org.daxprotocol.core.exceptions;

/**
 * DAXP-0011: Thrown when the parser encounters structural issues
 * within the tag=value pairs (e.g., missing '=', invalid tag ID format).
 */
public class DaxTagParserException extends DaxException {

    private static final String CODE = "DAXP-0011";
    private static final String PREFIX = "Tag Parser : ";

    // 1. Basic constructor
    public DaxTagParserException(String details) {
        super(CODE, PREFIX + details);
    }

    // 2. Chaining constructor (e.g., wrapping a NumberFormatException)
    public DaxTagParserException(String details, Throwable cause) {
        super(CODE, PREFIX + details, cause);
    }

    // 3. Simple wrapping constructor
    public DaxTagParserException(Throwable cause) {
        super(CODE, cause);
    }

    // 4. Advanced constructor for internal control
    protected DaxTagParserException(String details, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(CODE, PREFIX + details, cause, enableSuppression, writableStackTrace);
    }
}