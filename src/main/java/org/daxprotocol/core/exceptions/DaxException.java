package org.daxprotocol.core.exceptions;

/**
 * Base class for all DAXP protocol related issues.
 * Uses RuntimeException for flexible error handling in stream processing.
 */
public abstract class DaxException extends RuntimeException {
    private final String daxErrorCode;

    // 1. Basic: Code + Message
    public DaxException(String daxErrorCode, String message) {
        super(String.format("[%s] %s", daxErrorCode, message));
        this.daxErrorCode = daxErrorCode;
    }

    // 2. Chaining: Code + Message + Original Cause (e.g., IOException)
    public DaxException(String daxErrorCode, String message, Throwable cause) {
        super(String.format("[%s] %s", daxErrorCode, message), cause);
        this.daxErrorCode = daxErrorCode;
    }

    // 3. Wrapping: Code + Original Cause only
    public DaxException(String daxErrorCode, Throwable cause) {
        super(cause != null ? String.format("[%s] %s", daxErrorCode, cause.getMessage()) : "[" + daxErrorCode + "]", cause);
        this.daxErrorCode = daxErrorCode;
    }

    // 4. Advanced: Control over suppression and stack trace
    protected DaxException(String daxErrorCode, String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(String.format("[%s] %s", daxErrorCode, message), cause, enableSuppression, writableStackTrace);
        this.daxErrorCode = daxErrorCode;
    }

    public String getDaxErrorCode() {
        return daxErrorCode;
    }
}