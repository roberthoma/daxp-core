package org.daxprotocol.core.model.value;

import org.daxprotocol.core.datatype.DaxDataType;

public class DaxValueInteger extends DaxValue<Integer> {
    public DaxValueInteger(Integer value){
        super(DaxDataType.INTEGER, value);
    }
}
