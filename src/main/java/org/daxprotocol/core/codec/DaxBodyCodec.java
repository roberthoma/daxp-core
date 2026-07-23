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
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.pair.DaxPairString;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.*;

//public class DaxBodyCodec implements DaxCodec<DaxBody> {
public class DaxBodyCodec {

    DaxPairCodec pairCodec;
    DaxTagCodec tagCodec;
    DaxValueCodec valueCodec;

    public DaxBodyCodec(DaxPairCodec pairCodec, DaxTagCodec tagCodec, DaxValueCodec valueCodec) {
        this.pairCodec = pairCodec;
        this.tagCodec = tagCodec;
        this.valueCodec = valueCodec;
    }

    private void encodeBodyBlock(StringBuilder sb, boolean isBlogIdx ,
                                      int blockIdx ,Map<DaxTag, DaxPair<?>> blockMap,
                                      Map<DaxTag, Set<Integer>> tagBlockRefMap,
                                     char pairSeparator)
    {
        if (isBlogIdx) {
           // sb.append("\n");  only for debug mode
            pairCodec.encode(sb, DaxCoreTags.BLOCK_INDEX, String.valueOf(blockIdx+1), pairSeparator);
       }

        if (!blockMap.containsKey(DaxCoreTags.BLOCK_TYPE) ){
            StringBuilder blockStr  = new StringBuilder();
            blockMap.forEach((daxTag, value) -> blockStr.append(value.toString()));
            int excBlockIdx = blockIdx+1;
            throw new RuntimeException("Block Exception : block without BLOCK_TYPE field !!!+ blockIdx"+excBlockIdx
                    +" block:"+blockStr);
        }

        DaxPair<?> blockType =  blockMap.get(DaxCoreTags.BLOCK_TYPE);
        pairCodec.encode(sb, DaxCoreTags.BLOCK_TYPE, blockType.getStrValue(), pairSeparator);

        if (blockMap.containsKey(DaxCoreTags.ENTRY_TAG) ){
            pairCodec.encode(sb, blockMap.get(DaxCoreTags.ENTRY_TAG), pairSeparator);
        }

        blockMap.forEach((tag, pair) ->
        {
            if (!tag.equals(DaxCoreTags.BLOCK_INDEX) &&
                !tag.equals(DaxCoreTags.BLOCK_TYPE) &&
                !tag.equals(DaxCoreTags.ENTRY_TAG)
            )
            {
                pairCodec.encode(sb, pair, pairSeparator);
            }
        });

        //TODO Refactor blockSet.toString to own method
        tagBlockRefMap.forEach((tag, blockSet)
                -> pairCodec.encode(sb, new DaxPairString(tag,valueCodec.toBlockRefString(blockSet),
                                                           DaxCoreConstants.OPERATOR_BLOCK_REFERENCE)
                                    ,pairSeparator )
        );



/*
        if (nullTags!=null && !nullTags.isEmpty()){

            nullTags.forEach(daxTag ->
                  pairCodec.encode(sb, new DaxPairString(daxTag,"N", DaxCoreConstants.OPERATOR_ACTION), pairSeparator)
            );
        }
        */
    }

    //@Override
    public String encode(DaxBody body,char pairSeparator) {
        boolean isBlockPair = body.getBlockCount() > 1;

        StringBuilder sb = new StringBuilder();

        body.getAllBlockMap()
            .forEach((idx, map) ->
                encodeBodyBlock(sb,isBlockPair,idx, map, body.getTagBlockRefMap(idx),
                                                         //body.getBlockNullTags(idx),
                        pairSeparator)
                        );



        return sb.toString();
    }

    //todo refactor. move to head codec
    private  final Set<DaxTag> headSet = Set.of(MSG_TYPE,
            MSG_BLOCK_QUANTITY
            //new DaxTag(MSG_namespace)
    );


    public boolean isHeadTag(DaxTag tag){
        return headSet.contains(tag);
    }


    public  DaxBody createBody(int blockCount , List<DaxPair<?>> listOfPair){
        DaxBody body = new DaxBody();

//        if (blockCount==0) {
            body.nextBlock();
//        }
        for(DaxPair pair : listOfPair){
            if(pair.getTag().equals(DaxCoreTags.CHECKSUM)){
                break;
            }
            if (isHeadTag(pair.getTag())){
                continue;
            }
            if (pair.getTag().equals(DaxCoreTags.BLOCK_INDEX)) {
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
