package org.daxprotocol.core.attributes;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxAtrUiLabel extends DaxPair<String> {
    public DaxAtrUiLabel(String value) {
        super(DaxTag.ATR_UI_LABEL, value);
    }
}
