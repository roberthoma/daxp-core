/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 Robert Homa
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***********************************************************************
 */
package org.daxprotocol.core.codec;

import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.List;
import java.util.Map;

public class DaxBodyCodec implements DaxCodec<DaxBody> {


    private void encodeBodyBlock(StringBuilder sb, boolean isBlogIdx ,
                                      int blockIdx ,Map<DaxTag, DaxPair<?>> blockMap)
    {
        if (isBlogIdx) {
            sb.append("\n"); //TODO Debug mode or optional
            DaxPairCodec.encode(sb, DaxTagConst.BLOCK_INDEX, String.valueOf(blockIdx+1));
        }

        blockMap.forEach((tag, s) ->
        {
            if (!tag.equals(DaxTagConst.BLOCK_INDEX)) {
                DaxPairCodec.encode(sb, tag, s.getStrValue());
            }
        });
    }

    @Override
    public String encode(DaxBody body) {
        boolean isBlockPair = body.getBlockCount() > 1;

        StringBuilder sb = new StringBuilder();
        body.getBlockMap()
            .forEach((idx, map) ->
                encodeBodyBlock(sb,isBlockPair,idx, map)
        );
        return sb.toString();
    }

    @Override
    public DaxBody decode(String wire) {
        return null;
    }

    public static DaxBody createBody(int blockCount , List<DaxStringPair> listOfPair){
        DaxBody body = new DaxBody();

        if (blockCount==0) {
            body.nextBlock();
        }
        for(DaxPair<?> pair : listOfPair){
            if(pair.getTag().equals(DaxTagConst.CHECKSUM)){
                break;
            }
            if (DaxTagConst.isHeadTag(pair.getTag())){
                continue;
            }
            if (pair.getTag().equals(DaxTagConst.BLOCK_INDEX)) {
                if (blockCount==0){
                    throw new RuntimeException("Body creation Exception : block index and blockCount==0 !!!");
                }
                body.nextBlock();
            }
            body.putPair(pair);
        }
        return body;
    }

}
