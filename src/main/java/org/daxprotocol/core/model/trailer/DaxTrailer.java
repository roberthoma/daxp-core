package org.daxprotocol.core.model.trailer;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

import java.util.LinkedHashMap;
import java.util.Map;

public class DaxTrailer {

    Map<Integer, DaxPair<?>> map = new LinkedHashMap<>();

    public DaxTrailer(){
        map.put(DaxTag.CHECKSUM,new DaxPair<Integer>(DaxTag.CHECKSUM,321));
    }


    public Integer getChecksum() {
        return (Integer)(map.get(DaxTag.CHECKSUM).getValue());
    }
}
