package org.daxprotocol.core.model.body;

import org.daxprotocol.core.codec.DaxCodec;
import org.daxprotocol.core.codec.DaxPairCodec;
import org.daxprotocol.core.codec.DaxTag;

import java.util.Map;

public class DaxBodyCodec implements DaxCodec<DaxBody> {


    protected void encodeBodyBlock(StringBuilder sb,int blockIdx ,Map<Integer, String> blockMap){
        DaxPairCodec.encode(sb, DaxTag.BLOCK_INDEX, String.valueOf(blockIdx));
        blockMap.forEach((fid, s) -> DaxPairCodec.encode(sb, fid, s));
    }

    @Override public String encode(DaxBody body) {
        boolean isBlogPair = body.getBlocks()>1;

        StringBuilder sb = new StringBuilder();
        body.blocksMap.forEach((idx, map) -> encodeBodyBlock(sb,idx, map));
        return sb.toString();
    }

    @Override public DaxBody decode(String wire) {
        return null;
    }
}
