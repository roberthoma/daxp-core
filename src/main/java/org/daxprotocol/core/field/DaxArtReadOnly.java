package org.daxprotocol.core.field;

import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.model.pair.DaxPair;

public class DaxArtReadOnly extends DaxPair<Boolean> {
    public DaxArtReadOnly(Boolean value) {
        super(DaxTagConst.ATR_READONLY, value);
    }
}

