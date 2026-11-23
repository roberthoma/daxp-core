package org.daxprotocol.core.model.pair;

import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPairBoolean extends DaxPair<Boolean> {
    public DaxPairBoolean(DaxTag tag, Boolean value){
        super(tag, value);
    }

    public Boolean getBooleanValue() {
//        if( value instanceof Character) {
//            return (Character) value == 'Y' ? Boolean.TRUE : Boolean.FALSE;
//        }
//        if( value instanceof String) {
//            return value.equals("Y") ? Boolean.TRUE : Boolean.FALSE;
//        }
        return (Boolean) value;

    }

}
