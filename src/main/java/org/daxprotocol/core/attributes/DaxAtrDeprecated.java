package org.daxprotocol.core.attributes;

import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.model.pair.DaxPair;


public class DaxAtrDeprecated extends DaxPair<Boolean> {
    public DaxAtrDeprecated(Boolean value) {
        super(DaxCoreTags.ATR_IS_DEPRECATED, value);
    }
}
