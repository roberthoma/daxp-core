package org.daxprotocol.core.model.pair;

import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.value.DaxValue;

public class DaxPair {
    DaxTag tag;
    DaxValue<?> value;

    public DaxTag getTag() {
        return tag;
    }

    public DaxValue<?> getValue() {
        return value;
    }

    public DaxPair(DaxTag tag, DaxValue<?> value) {
        this.tag = tag;
        this.value = value;
    }
}
