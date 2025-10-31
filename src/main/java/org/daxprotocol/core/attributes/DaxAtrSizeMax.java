package org.daxprotocol.core.attributes;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxAtrSizeMax extends DaxPair<Integer> {
    public DaxAtrSizeMax(Integer value) {
        super(DaxTag.ATR_SIZE_MAX, value);
    }
}
