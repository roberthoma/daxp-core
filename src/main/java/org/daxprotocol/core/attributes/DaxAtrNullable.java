package org.daxprotocol.core.attributes;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxAtrNullable extends DaxPair<Character> {

    public static Character NULLABLE_TRUE = 'Y';
    public static Character NULLABLE_FALSE  = 'N';

    public DaxAtrNullable(Character value) {
        super(DaxTag.ATR_NULLABLE, value);
    }
}
