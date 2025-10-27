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
package org.daxprotocol.core.model.head;
import org.daxprotocol.core.codec.DaxPair;

import java.util.LinkedHashMap;
import java.util.Map;

import static  org.daxprotocol.core.codec.DaxTag.*;

public final class DaxHead {

    Map<Integer, DaxPair<?>> map = new LinkedHashMap<>();

    public String getMsgType() {
        return map.get(MSG_TYPE).getStrValue();
    }

    public String getToken() {
        return map.getOrDefault(MSG_TOKEN, null).getStrValue();
    }

    public String getTimestamp() {
        return map.containsKey(MSG_TIMESTAMP)?  map.get(MSG_TIMESTAMP).getStrValue() : null;
    }

    public DaxHead(String msgType, String token, String timestamp) {
        map.put(MSG_TYPE,new DaxMsgType(msgType));
        map.put(MSG_TOKEN,new DaxMsgToken(token));
        map.put(MSG_TIMESTAMP,new DaxMsgTimestamp(timestamp));
        map.put(MSG_BLOCK_COUNT,new DaxMsgBlockCount(0));
    }
    public DaxHead(String msgType) {
        map.put(MSG_TYPE,new DaxMsgType(msgType));
    }

    public int getBlockCount() {
        return map.containsKey(MSG_BLOCK_COUNT) ? (Integer) (map.get(MSG_BLOCK_COUNT).getValue()) : 0;
    }

    public void setBlockCount(int blockCount) {
        map.put(MSG_BLOCK_COUNT,new DaxMsgBlockCount(blockCount));
    }

    public void setToken(String token) {
        map.put(MSG_TOKEN,new DaxMsgToken(token));
    }

    public void setTimestamp(String timestamp) {
        map.put(MSG_TIMESTAMP,new DaxMsgTimestamp(timestamp));
    }



}
