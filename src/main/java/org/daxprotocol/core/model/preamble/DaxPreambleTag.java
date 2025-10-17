package org.daxprotocol.core.model.preamble;

public enum DaxPreambleTag {
    DAXP("DAXP"),   // protocol identifier and version
    TF("TF"),       // tag format: DEC | HEX
    EN("EN"),       // encoding: ASCII | UTF8 | UTF16
    CTX("CTX");     // context: optional

    private final String tag;

    DaxPreambleTag(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }

    public static DaxPreambleTag fromTag(String tag) {
        for (DaxPreambleTag t : values()) {
            if (t.tag.equalsIgnoreCase(tag)) return t;
        }
        return null;
    }
}
