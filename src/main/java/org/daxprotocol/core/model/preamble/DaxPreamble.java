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

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;

/**
 * Represents the preamble of a DAXP message — defines
 * how the rest of the message is encoded and parsed.
 */
public class DaxPreamble {

    private char pairSeparator = DaxCoreConstants.DEFAULT_PAIR_SEPARATOR;
    private String protocolVersion =  DaxConfig.PROTOCOL_VERSION;
    private int msgCnt = -1;  //quantity                 //CNT  Number of item messages following preamble. Default 1
    private DaxCharacterEncoding encoding;
    private int namespaceId = -1;
    //TODO Add Token !!!

    public DaxPreamble(){
    }

    public void setPairSeparator(char pairSeparator) {
        this.pairSeparator = pairSeparator;
    }

    public char getPairSeparator() {
        return pairSeparator;
    }

    public DaxPreamble(DaxCharacterEncoding encoding ){
        this.encoding = encoding;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion.toUpperCase().trim();
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

    public int getnamespaceId(){
        return namespaceId;
    }

    public void setnamespaceId(int namespaceId){
        this.namespaceId = namespaceId;
    }


}