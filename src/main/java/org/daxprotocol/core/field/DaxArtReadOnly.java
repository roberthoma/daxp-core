package org.daxprotocol.core.field;

import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.model.pair.DaxPair;

public class DaxArtReadOnly extends DaxPair<Boolean> {

//    public static Character EDITABLE_TRUE   = 'Y'; // Default
//    public static Character EDITABLE_FALSE  = 'N';

    public DaxArtReadOnly(Boolean value) {
        super(DaxTagConst.ATR_READONLY, value);
    }
}

