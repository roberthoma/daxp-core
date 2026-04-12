package org.daxprotocol.core.field;

import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.model.pair.DaxPair;

public class DaxArtReadOnly extends DaxPair<Boolean> {
    public DaxArtReadOnly(Boolean value) {
        super(DaxCoreTags.ATR_READONLY, value);
    }
}

