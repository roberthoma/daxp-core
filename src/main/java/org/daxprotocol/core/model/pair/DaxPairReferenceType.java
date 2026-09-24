package org.daxprotocol.core.model.pair;

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxReferenceType;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPairReferenceType extends DaxPair<DaxReferenceType> {
    public DaxPairReferenceType(DaxTag tag, DaxReferenceType value){
        super(tag,value);
    }

}