package org.daxprotocol.core.model.head;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxMsgBlockCount extends DaxPair<Integer> {
    public DaxMsgBlockCount( Integer value) {
        super(DaxTag.MSG_BLOCK_COUNT, value);
    }
    @Override
    public String getStrValue() {
        return String.valueOf(value);
    };
}
