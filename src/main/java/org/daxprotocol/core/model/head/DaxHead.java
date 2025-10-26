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
import org.daxprotocol.core.types.DaxInteger;
import org.daxprotocol.core.types.DaxString;
import org.daxprotocol.core.types.DaxValue;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static  org.daxprotocol.core.codec.DaxTag.*;

public final class DaxHead {

    Map<Integer, DaxValue<?>> map = new LinkedHashMap<>();

    public String getMsgType() {
        return (String) (map.get(MSG_TYPE).getValue());
    }

    public String getToken() {
        return map.containsKey(MSG_TOKEN) ? (String) (map.get(MSG_TOKEN).getValue()) : null;
    }

    public String getTimestamp() {
        return map.containsKey(MSG_TIMESTAMP) ? (String) (map.get(MSG_TIMESTAMP).getValue()) : null;
    }

    public DaxHead(String msgType, String token, String timestamp) {
        map.put(MSG_TYPE,new DaxString(MSG_TYPE,msgType));
        map.put(MSG_TOKEN,new DaxString(MSG_TOKEN,token));
        map.put(MSG_TIMESTAMP,new DaxString(MSG_TIMESTAMP,timestamp));
        map.put(MSG_BLOCK_COUNT,new DaxInteger(MSG_BLOCK_COUNT,0));
    }
    public DaxHead(String msgType) {
        map.put(MSG_TYPE,new DaxString(MSG_TYPE,msgType));
    }

    public int getBlockCount() {
        return map.containsKey(MSG_BLOCK_COUNT) ? (Integer) (map.get(MSG_BLOCK_COUNT).getValue()) : 0;
    }

    public void setBlockCount(int blockCount) {
        map.put(MSG_BLOCK_COUNT,new DaxInteger(MSG_BLOCK_COUNT,blockCount));
    }

    public void setToken(String token) {
        map.put(MSG_TOKEN,new DaxString(MSG_TOKEN,token));
    }

    public void setTimestamp(String timestamp) {
        map.put(MSG_TIMESTAMP,new DaxString(MSG_TIMESTAMP,timestamp));
    }



}
