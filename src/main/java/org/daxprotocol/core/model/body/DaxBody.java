/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.*;

public class DaxBody {

    Map<Integer,Map<DaxTag, DaxPair<?>>>    blockMap      = new HashMap<>();
    Map<Integer,Map<DaxTag, Set<Integer>>>  blockRefMap   = new HashMap<>();

    int blockIdx = -1;

    private void checkBlockCounterBeforePut(){
        if (blockIdx == -1) {
            throw new RuntimeException("No init block in message body !!! Please use nextBlock(). ");
        }
    }

    public int getBlockCount() {
        return blockMap.size()  ;
    }

    public Map<DaxTag, DaxPair<?>> getBlock(int blockIdx){
        return blockMap.get(blockIdx);
    }
    //------------------------------
    public void putPair(DaxPair<?> pair){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(pair.getTag(),pair);
    }
    //------------------------------

    public void putPair(int blkIdx , DaxPair<?> pair){
        checkBlockCounterBeforePut();
        blockMap.get(blkIdx).put(pair.getTag(),pair);
    }
    //------------------------------

    public void putPair(int blkIdx , Set<DaxPair<?>> pairSet){
        checkBlockCounterBeforePut();
        pairSet.forEach(pair ->
        blockMap.get(blkIdx).put(pair.getTag(),pair));
    }

    //----------------------------
    public void putPair(DaxTag tag, String value){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(tag,new DaxPairString(tag,value));
    }
    public void putPair(int blkIdx ,DaxTag tag, String value){
        checkBlockCounterBeforePut();
        blockMap.get(blkIdx).put(tag,new DaxPairString(tag,value));
    }
    //----------------------------

    public void putPair(DaxTag tag, DaxTag tagValue){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(tag,new DaxPairTag(tag,tagValue));
    }
    public void putPair(int blkIdx , DaxTag tag, DaxTag tagValue){
        checkBlockCounterBeforePut();
        blockMap.get(blkIdx).put(tag,new DaxPairTag(tag,tagValue));
    }
    //----------------------------

    public void putPair(DaxTag tag, Integer value){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(tag,new DaxPairInteger(tag,value));
    }
    public void putPair(int blkIdx,DaxTag tag, Integer value){
        checkBlockCounterBeforePut();
        blockMap.get(blkIdx).put(tag,new DaxPairInteger(tag,value));
    }

    //-----------------------------

    public void nextBlock(){
        blockIdx = blockMap.size();
        blockMap.put(blockIdx,new HashMap<>());
        blockRefMap.put(blockIdx,new HashMap<>());
   //     blockNullTags.put(blockIdx, new HashSet<>());
    }

    public void nextBlock(DaxBlockType blockType){
       nextBlock();
       putPair( new DaxPairCharacter(DaxCoreTags.BLOCK_TYPE,blockType.getCode()));
    }

    public DaxPair<?> getPair(int blockIdx, DaxTag tag){
        return blockMap.get(blockIdx).get(tag);
    }

    public Map<Integer,Map<DaxTag, DaxPair<?>>> getAllBlockMap() {
         return blockMap;
    }

    public Map<DaxTag, DaxPair<?>> getBlockMap(int blockIdx) {
         return blockMap.get(blockIdx);
    }



    public int getCurrentIdx(){
        return blockIdx;
    }

    //TODO Refactor to merge
    public void putTagBlockReference(int blogIdx, DaxTag tag, int refBlockIdx) {
        if(!blockRefMap.get(blogIdx).containsKey(tag)){
            blockRefMap.get(blogIdx).put(tag, new HashSet<>());
        }
        blockRefMap.get(blogIdx).get(tag).add( refBlockIdx);

    }

    public Map<DaxTag, Set<Integer>> getTagBlockRefMap(int blogIdx) {
        return blockRefMap.get(blogIdx);
    }


    public boolean isNullAt(int blockIdx, DaxTag tag) {
        //TODO check idea .
        DaxPair<?> pair;
        pair = blockMap.get(blockIdx).get(tag);
        if (pair == null){
            Set<Integer>  sset = blockRefMap.get(blockIdx).get(tag);
            return  (sset == null || sset.isEmpty());
        }

        return pair.getOperator() == '^' && pair.getStrValue().trim().compareTo("N")== 0;


    }

    public boolean isAnyReference(int blockIdx,DaxTag tag) {
        if (!blockRefMap.get(blockIdx).containsKey(tag)){
            return false;
        }
       return !( (blockRefMap.get(0).get(tag))).isEmpty();


    }

    public Set<Integer> getRefBlocksIdx(int blockIdx, DaxTag tag) {
      return         blockRefMap.get(blockIdx).get(tag);
    }
}
