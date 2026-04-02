package org.daxprotocol.core.exceptions;

public class DaxPreambleException extends DaxException {
    public DaxPreambleException(String details) {
        super("DAXP-0001", "Preamble Malformed: " + details);
    }
}