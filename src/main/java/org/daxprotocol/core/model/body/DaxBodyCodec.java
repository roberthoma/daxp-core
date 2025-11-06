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
package org.daxprotocol.core.model.body;

import org.daxprotocol.core.codec.*;

import java.util.List;
import java.util.Map;

public class DaxBodyCodec implements DaxCodec<DaxBody> {


    protected void encodeBodyBlock(StringBuilder sb, boolean isBlogIdx ,
                                      int blockIdx ,Map<Integer, DaxPair<?>> blockMap)
    {
        if (isBlogIdx) {
            sb.append("\n"); //TODO Debug mode or optional
            DaxPairCodec.encode(sb, DaxTag.BLOCK_INDEX, String.valueOf(blockIdx+1));
        }

        blockMap.forEach((tag, s) ->
        {
            if (tag != DaxTag.BLOCK_INDEX) {
                DaxPairCodec.encode(sb, tag, s.getStrValue());
            }
        });
    }

    @Override
    public String encode(DaxBody body) {
        boolean isBlogPair = body.getBlockCount() > 1;

        StringBuilder sb = new StringBuilder();
        body.blocksMap
            .forEach((idx, map) ->
                encodeBodyBlock(sb,isBlogPair,idx, map)
        );
        return sb.toString();
    }

    @Override
    public DaxBody decode(String wire) {
        return null;
    }

    public static DaxBody createBody(int blockCount , List<DaxStringPair> listOfPair){
        DaxBody body = new DaxBody();
        body.nextBlock();
        for(DaxPair<?> pair : listOfPair){
            if(pair.getTag() == DaxTag.CHECKSUM){
                break;
            }
            if (DaxTag.isHeadTag(pair.getTag())){
                continue;
            }
            if (pair.getTag() == DaxTag.BLOCK_INDEX) {
                body.nextBlock();
            }
            body.putPair(pair);
        }
        return body;
    }

}
