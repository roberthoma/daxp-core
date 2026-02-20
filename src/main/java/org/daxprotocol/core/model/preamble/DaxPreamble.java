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
package org.daxprotocol.core.model.preamble;

import org.daxprotocol.core.codec.DaxDecodeService;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;

import java.util.regex.Pattern;

/**
 * Represents the preamble of a DAXP message — defines
 * how the rest of the message is encoded and parsed.
 */
public class DaxPreamble {
    Pattern pairPattern;
    private char msgPairSeparator;
    private String protocolVersion = DaxpConfig.PROTOCOL_VERSION;       // V=1
    private int msgCnt;                   //CNT  Number of item messages following preamble. Default 1
    private DaxCharacterEncoding encoding;
    private int msgContextId;

    public DaxPreamble(){
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }


    public void setEncoding(DaxCharacterEncoding encoding) {
        this.encoding = encoding;
    }

    public int getMsgCnt() {
        return msgCnt;
    }

    public void setMsgCnt(int msgCnt) {
        this.msgCnt = msgCnt;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public DaxCharacterEncoding getEncoding() {
        return encoding;
    }

    public void setPairSeparator(Character pairSeparator) {
        this.msgPairSeparator = pairSeparator;
        this.setPairPattern(DaxDecodeService.getPreamblePairPattern(this.msgPairSeparator));
    }

    public Pattern getPairPattern() {
        return pairPattern;
    }

    public void setPairPattern(Pattern pairPattern) {
        this.pairPattern = pairPattern;
    }
    public int getMsgContextId(){
        return msgContextId;
    }

    public void  setMsgContextId(int contextId){
        this.msgContextId = contextId;
    }

    public char getMsgPairSeparator() {
        return msgPairSeparator;
    }

}