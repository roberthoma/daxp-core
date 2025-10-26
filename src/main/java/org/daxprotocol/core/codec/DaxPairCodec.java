package org.daxprotocol.core.codec;

import static org.daxprotocol.core.codec.DaxCodecSymbols.EQUAL;
import static org.daxprotocol.core.codec.DaxCodecSymbols.PAIR_SEPARATOR;

public class DaxPairCodec { //implements DaxCodec<DaxPair> {

    public static String encode(StringBuilder sb,int tag, String value ) {
        sb.append(tag)
                .append(EQUAL)
                .append(value)
                .append(PAIR_SEPARATOR);
        return sb.toString() ;
    }

    public static String encode(StringBuilder sb,String tag, String value ) {
        sb.append(tag)
                .append(EQUAL)
                .append(value)
                .append(PAIR_SEPARATOR);
        return sb.toString() ;
    }

    //@Override
    public String encode(DaxPair pair) {
        StringBuilder sb = new StringBuilder();
        sb.append(pair.tag)
                .append(EQUAL)
                .append(pair.value)
                .append(PAIR_SEPARATOR);
        return sb.toString() ;
    }

    //@Override
    public DaxPair decode(String wire) {
        return null;
    }
}
