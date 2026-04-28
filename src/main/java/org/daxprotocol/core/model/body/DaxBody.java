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
package org.daxprotocol.core.model.body;

import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.value.*;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.HashMap;
import java.util.Map;

public class DaxBody {

    Map<Integer,Map<DaxTag, DaxValue<?>>> blockMap = new HashMap<>();


    Map<Integer,Map<DaxTag, Integer>> blockRefMap = new HashMap<>();

    int blockIdx = -1;

    private void checkBlockCounterBeforePut(){
        if (blockIdx == -1) {
            throw new RuntimeException("No init block in message body !!! Please use nextBlock(). ");
        }
    }

    public int getBlockCount() {
        return blockMap.size()  ;
    }

    public Map<DaxTag, DaxValue<?>> getBlock(int blockIdx){
        return blockMap.get(blockIdx);
    }
    //------------------------------
    public void putPair(DaxTag tag, DaxValue<?> value){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(tag,value);
    }
    //------------------------------
    public void putPair(DaxPair pair){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(pair.getTag(),pair.getValue());
    }
    //------------------------------

    public void putPair(int blkIdx ,DaxTag tag, DaxValue<?> value){
        checkBlockCounterBeforePut();
        blockMap.get(blkIdx).put(tag,value);
    }
    //----------------------------
    public void putPair(DaxTag tag, String value){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(tag,new DaxValueString(value));
    }
    public void putPair(int blkIdx ,DaxTag tag, String value){
        checkBlockCounterBeforePut();
        blockMap.get(blkIdx).put(tag,new DaxValueString(value));
    }
    //----------------------------

    public void putPair(DaxTag tag, DaxTag tagValue){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(tag,new DaxValueTag(tagValue));
    }
    public void putPair(int blkIdx , DaxTag tag, DaxTag tagValue){
        checkBlockCounterBeforePut();
        blockMap.get(blkIdx).put(tag,new DaxValueTag(tagValue));
    }
    //----------------------------

    public void putPair(DaxTag tag, Integer value){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(tag,new DaxValueInteger(value));
    }
    public void putPair(int blkIdx,DaxTag tag, Integer value){
        checkBlockCounterBeforePut();
        blockMap.get(blkIdx).put(tag,new DaxValueInteger(value));
    }

    //-----------------------------

    public void nextBlock(){
        blockIdx = blockMap.size();
        blockMap.put(blockIdx,new HashMap<>());
        blockRefMap.put(blockIdx,new HashMap<>());

    }

    public void nextBlock(DaxBlockType blockType){
       nextBlock();
       putPair(DaxCoreTags.BLOCK_TYPE, new DaxValueCharacter(blockType.getCode()));
    }

    public DaxValue<?> getPair(int blockIdx, DaxTag tag){
        return blockMap.get(blockIdx).get(tag);
    }

    public Map<Integer,Map<DaxTag, DaxValue<?>>> getBlockMap() {
         return blockMap;
    }


    public int getCurrentIdx(){
        return blockIdx;
    }

    public void putTagBlockReference(int blogIdx, DaxTag tag, int refBlockIdx) {
        blockRefMap.get(blogIdx).put(tag,refBlockIdx);
    }

    public Map<DaxTag, Integer> getTagBlockRefMap(int blogIdx) {
        return blockRefMap.get(blogIdx);
    }

}
