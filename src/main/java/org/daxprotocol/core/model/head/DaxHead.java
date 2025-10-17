package org.daxprotocol.core.model.head;
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
public class DaxHead {
    private final String msgType;     // tag 9

    private String token;       // tag 15 (optional)
    private String timestamp;   // tag 16 (ISO-8601 with millis)
    private int blockCount = 1;  //TODO add increment function

    public String getMsgType() {
        return msgType;
    }

    public String getToken() {
        return token;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public DaxHead(String msgType, String token, String timestamp) {
        this.msgType = msgType;
        this.token = token;
        this.timestamp = timestamp;
    }
    public DaxHead(String msgType) {
        this.msgType = msgType;
        this.token = "";
        this.timestamp = "";
    }

    public int getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



}
