package org.daxprotocol.core.exceptions;


public abstract class DaxAppException extends DaxException {
    public DaxAppException(int appCode, String message) {
        super("DAXP-" + appCode, message);

        // Safety check for the reserved range
        if (appCode < 5000 || appCode > 8999) {
            throw new IllegalArgumentException("App codes must be between 5000 and 8999");
        }
    }
}