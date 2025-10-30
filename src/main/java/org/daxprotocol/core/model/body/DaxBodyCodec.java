package org.daxprotocol.core.model.body;

import org.daxprotocol.core.codec.DaxCodec;
import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxPairCodec;
import org.daxprotocol.core.codec.DaxTag;

import java.util.Map;

public class DaxBodyCodec implements DaxCodec<DaxBody> {


    protected void encodeBodyBlock(StringBuilder sb, boolean isBlogIdx ,
                                      int blockIdx ,Map<Integer, DaxPair<?>> blockMap)
    {
        if (isBlogIdx) {
            DaxPairCodec.encode(sb, DaxTag.BLOCK_INDEX, String.valueOf(blockIdx+1));
        }

//        blockMap.forEach((tag, s) -> DaxPairCodec.encode(sb, tag, s.getStrValue()));

        blockMap.forEach((tag, s) ->
        {
            if (tag != DaxTag.BLOCK_INDEX) {
                DaxPairCodec.encode(sb, tag, s.getStrValue());
            }
        });
    }

    @Override public String encode(DaxBody body) {
        boolean isBlogPair = body.getBlockCount() > 1;

        StringBuilder sb = new StringBuilder();
        body.blocksMap
            .forEach((idx, map) ->
                encodeBodyBlock(sb,isBlogPair,idx, map)
        );
        return sb.toString();
    }

    @Override public DaxBody decode(String wire) {
        return null;
    }
}
