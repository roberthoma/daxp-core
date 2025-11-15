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

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxStringPair;
import org.daxprotocol.core.codec.DaxTag;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DaxBody {

    Map<Integer,Map<Integer, DaxPair<?>>> blockMap = new HashMap<>();

    int blockIdx = -1;

    private void checkBlockCounterBeforePut(){
        if (blockIdx == -1) {
            throw new RuntimeException("No init block in message body !!! Please use nextBlock(). ");
        }
    }

    public int getBlockCount() {
        return blockMap.size()  ;
    }

    public Map<Integer, DaxPair<?>> getBlock(int blockIdx){
        return blockMap.get(blockIdx);
    }

    public void putPair(DaxPair<?> pair){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(pair.getTag(),pair);
    }
    public void putPair(int tag, String value){
        checkBlockCounterBeforePut();
        blockMap.get(blockIdx).put(tag,new DaxStringPair(tag, value));
    }

    public void nextBlock(){
        blockIdx = blockMap.size();
        blockMap.put(blockIdx,new TreeMap<>());
    }

    public void nextBlock(String blockType){
       nextBlock();
       putPair(DaxTag.BLOCK_TYPE, blockType);
    }

    public DaxPair<?> getPair(int blockIdx, int tag){
        return blockMap.get(blockIdx).get(tag);
    }

    public Map<Integer,Map<Integer, DaxPair<?>>> getBlockMap() {
         return blockMap;
    }
}
