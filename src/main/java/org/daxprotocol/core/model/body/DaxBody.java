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
import java.util.HashMap;
import java.util.Map;

public class DaxBody {

    Map<Integer,Map<Integer, DaxPair<?>>> blocksMap = new HashMap<>();

    int blockIdx = 0;

    public int getBlockCount() {
        return blockIdx + 1;
    }

    public Map<Integer, DaxPair<?>> getBlock(int blockIdx){
        return blocksMap.get(blockIdx);
    }

    public void putPair(DaxPair<?> pair){
        blocksMap.get(blockIdx).put(pair.getTag(),pair);
    }
    public void putPair(int tag, String value){
        blocksMap.get(blockIdx).put(tag,new DaxStringPair(tag, value));
    }

    public void nextBlock(){
        blockIdx = blocksMap.size();
        blocksMap.put(blockIdx,new HashMap<>());
    }

}
