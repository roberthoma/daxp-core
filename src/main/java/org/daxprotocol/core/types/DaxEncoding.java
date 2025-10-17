package org.daxprotocol.core.types;

public enum DaxEncoding {
    ASCII("ASCII"), UTF8("UTF8"), UTF16("UTF16");



    private final String value;

    DaxEncoding(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DaxEncoding fromVale(String value) {
        for (DaxEncoding t : values()) {
            if (t.value.equalsIgnoreCase(value)) return t;
        }
        return null;
    }
}
