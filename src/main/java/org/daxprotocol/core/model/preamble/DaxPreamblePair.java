package org.daxprotocol.core.model.preamble;

public class DaxPreamblePair {
    String tag;
    String value;

    public DaxPreamblePair(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }
    @Override
    public String toString() {
        return tag + "=" + value;
    }

}
