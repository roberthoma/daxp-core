package org.daxprotocol.core.model.head;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxMsgToken extends DaxPair<String> {
    public DaxMsgToken(String value) {
        super(DaxTag.MSG_TOKEN, value);
    }
}
