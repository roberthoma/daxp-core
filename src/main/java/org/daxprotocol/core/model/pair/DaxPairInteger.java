package org.daxprotocol.core.model.pair;

import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPairInteger extends DaxPair<Integer> {
    public DaxPairInteger(DaxTag tag, Integer value) {
        super(tag, value);
    }

    @Override
    public String toString(){
        return tag+"="+value;
    }

}
