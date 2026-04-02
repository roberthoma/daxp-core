package org.daxprotocol.core.exceptions;

/**
 * Base class for all DAXP protocol related issues.
 * Uses RuntimeException for flexible error handling in stream processing.
 */
public abstract class DaxException extends RuntimeException {
    private final String errorCode;

    // 1. Basic: Code + Message
    public DaxException(String errorCode, String message) {
        super(String.format("[%s] %s", errorCode, message));
        this.errorCode = errorCode;
    }

    // 2. Chaining: Code + Message + Original Cause (e.g., IOException)
    public DaxException(String errorCode, String message, Throwable cause) {
        super(String.format("[%s] %s", errorCode, message), cause);
        this.errorCode = errorCode;
    }

    // 3. Wrapping: Code + Original Cause only
    public DaxException(String errorCode, Throwable cause) {
        super(cause != null ? String.format("[%s] %s", errorCode, cause.getMessage()) : "[" + errorCode + "]", cause);
        this.errorCode = errorCode;
    }

    // 4. Advanced: Control over suppression and stack trace
    protected DaxException(String errorCode, String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(String.format("[%s] %s", errorCode, message), cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}