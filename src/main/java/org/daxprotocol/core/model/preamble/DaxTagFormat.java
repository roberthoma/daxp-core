package org.daxprotocol.core.model.preamble;

public enum DaxTagFormat {
    DEC("DEC"), HEX("HEX");
    private final String value;

    DaxTagFormat(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DaxTagFormat fromVale(String value) {
        for (DaxTagFormat t : values()) {
            if (t.value.equalsIgnoreCase(value)) return t;
        }
        return null;
    }
}
