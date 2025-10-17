package org.daxprotocol.core.types;

public enum DaxTagFormat {
    ASCII("ASCII"), UTF8("UTF8"), UTF16("UTF16");

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
