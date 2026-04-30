package org.daxprotocol.core.model.pair;

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPairDataType extends DaxPair<DaxDataType> {
    public DaxPairDataType(DaxTag tag, DaxDataType value){
        super(tag,value);
    }

}