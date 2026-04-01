package org.daxprotocol.core.model.preamble;

import java.util.*;

public enum DaxPreambleTag {
    DAXP("DAXP"),
    ENCODING("EN"),
    MSG_COUNT("MC"),
    MSG_CONTEXT("CX"),
    MSG_SENDER("SN");

    private final String tag;

    // Map provides O(1) lookup to return the actual Enum object
    private static final Map<String, DaxPreambleTag> BY_TAG = new HashMap<>();

    static {
        for (DaxPreambleTag t : values()) {
            BY_TAG.put(t.tag, t);
        }
    }

    DaxPreambleTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    /**
     * Checks if a string is a valid tag.
     */
    public static boolean contains(String value) {
        if (value == null) return false;
        return BY_TAG.containsKey(value.toUpperCase().trim());
    }

    /**
     * Returns the Enum constant for the given tag string.
     * * @param value The tag string (e.g., "MC")
     * @return The matching DaxPreambleTag, or null if not found.
     */
    public static DaxPreambleTag fromTag(String value) {
        if (value == null) return null;
        return BY_TAG.get(value.toUpperCase().trim());
    }
}