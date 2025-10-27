package org.daxprotocol.core.codec;

public class DaxStringPair extends DaxPair<String>{
    public DaxStringPair(int tag, String value) {
        super(tag, value);
    }

    @Override
    public String toString(){
        return tag+"="+value;
    }

}
