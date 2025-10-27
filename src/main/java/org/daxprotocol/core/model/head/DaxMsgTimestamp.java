package org.daxprotocol.core.model.head;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxMsgTimestamp extends DaxPair<String> {
    public DaxMsgTimestamp(String value) {
        super(DaxTag.MSG_TIMESTAMP, value);
    }
}
