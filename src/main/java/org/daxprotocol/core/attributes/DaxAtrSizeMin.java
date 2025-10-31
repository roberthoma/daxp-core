package org.daxprotocol.core.attributes;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxAtrSizeMin extends DaxPair<Integer> {
    public DaxAtrSizeMin(Integer value) {
        super(DaxTag.ATR_SIZE_MIN, value);
    }
}
