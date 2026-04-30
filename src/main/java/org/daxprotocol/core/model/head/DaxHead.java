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
package org.daxprotocol.core.model.head;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.pair.DaxPairInteger;
import org.daxprotocol.core.model.pair.DaxPairString;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public final class DaxHead {

    Map<DaxTag, DaxPair<?>> map = new LinkedHashMap<>();

    public String getMsgType() {
        return map.get(MSG_TYPE).getStrValue();
    }

    public DaxHead(String msgType) {
        map.put(MSG_TYPE,new DaxPairString(MSG_TYPE,msgType));
        map.put(MSG_BLOCK_QUANTITY,new DaxPairInteger(MSG_BLOCK_QUANTITY,0));
    }

    public int getBlockCount() {
        return map.containsKey(MSG_BLOCK_QUANTITY) ? (Integer) (map.get(MSG_BLOCK_QUANTITY).getValue()) : 0;
    }

    public void setBlockCount(int blockCount) {
         map.merge(MSG_BLOCK_QUANTITY,new DaxPairInteger(MSG_BLOCK_QUANTITY,blockCount),
                 (daxPair, daxPair2) -> daxPair2);
    }

}
