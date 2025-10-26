package org.daxprotocol.core.attributes;

import org.daxprotocol.core.codec.DaxTag;

public class DaxAtrUiLabel extends DaxAttribute<String> {
    public DaxAtrUiLabel(String value) {
        super(DaxTag.ATR_UI_LABEL, value);
    }
}
