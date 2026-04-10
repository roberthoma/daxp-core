/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
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
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.List;
import java.util.Map;

//public class DaxBodyCodec implements DaxCodec<DaxBody> {
public class DaxBodyCodec {

    DaxPairCodec pairCodec;


    public DaxBodyCodec(DaxPairCodec pairCodec) {
        this.pairCodec = pairCodec;
    }

    private void encodeBodyBlock(StringBuilder sb, boolean isBlogIdx ,
                                      int blockIdx ,Map<DaxTag, DaxPair<?>> blockMap)
    {
        if (isBlogIdx) {

            pairCodec.encode(sb, DaxTagConst.BLOCK_INDEX, String.valueOf(blockIdx+1));
       }

        if (!blockMap.containsKey(DaxTagConst.BLOCK_TYPE) ){
            StringBuilder blostr  = new StringBuilder();
            blockMap.forEach((daxTag, daxPair) -> blostr.append(daxPair.toString()));
            int excBlockIdx = blockIdx+1;
            throw new RuntimeException("Block Exception : block without BLOCK_TYPE field !!!+ blockIdx"+excBlockIdx
                    +" block:"+blostr);
        }

        DaxPair<?> blockType =  blockMap.get(DaxTagConst.BLOCK_TYPE);
        pairCodec.encode(sb, DaxTagConst.BLOCK_TYPE, blockType.getStrValue());

        if (blockMap.containsKey(DaxTagConst.FIELD_ID) ){
            pairCodec.encode(sb, DaxTagConst.FIELD_ID, blockMap.get(DaxTagConst.FIELD_ID));
        }

        blockMap.forEach((tag, pair) ->
        {
            if (!tag.equals(DaxTagConst.BLOCK_INDEX) &&
                !tag.equals(DaxTagConst.BLOCK_TYPE) &&
                !tag.equals(DaxTagConst.FIELD_ID)
            )
            {
                pairCodec.encode(sb, tag, pair);
            }
        });
    }

    //@Override
    public String encode(DaxBody body) {
        boolean isBlockPair = body.getBlockCount() > 1;

        StringBuilder sb = new StringBuilder();

        body.getBlockMap()
            .forEach((idx, map) ->
                encodeBodyBlock(sb,isBlockPair,idx, map)
        );

        return sb.toString();
    }


    public  DaxBody createBody(int blockCount , List<DaxPair<?>> listOfPair){
        DaxBody body = new DaxBody();

//        if (blockCount==0) {
            body.nextBlock();
//        }
        for(DaxPair<?> pair : listOfPair){
            if(pair.getTag().equals(DaxTagConst.CHECKSUM)){
                break;
            }
            if (DaxTagConst.isHeadTag(pair.getTag())){
                continue;
            }
            if (pair.getTag().equals(DaxTagConst.BLOCK_INDEX)) {
//                if (blockCount==0){
//                    throw new RuntimeException("Body creation Exception : block index and blockCount==0 !!!");
//                }
                body.nextBlock();
            }
            body.putPair(pair);
        }
        return body;
    }

}
