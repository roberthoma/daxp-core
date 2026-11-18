package org.daxprotocol.core.model.pair;

import org.daxprotocol.core.model.tag.DaxTag;


import java.util.Map;

public class DaxPairMap extends DaxPair<Map<?, ?>> {
    public DaxPairMap(DaxTag tag, Map<?, ?> value) {
        super(tag, value);
    }
}