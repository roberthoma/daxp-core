package org.daxprotocol.core.model.pair;

import org.daxprotocol.core.model.tag.DaxTag;

import java.util.Collection;

public class DaxPairCollection extends DaxPair<Collection<?>> {
    public DaxPairCollection(DaxTag tag, Collection<?>  value){
        super(tag, value);
    }
}
