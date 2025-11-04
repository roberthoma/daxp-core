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
package org.daxprotocol.core.model.preamble;

import org.daxprotocol.core.codec.DaxDecodeService;

import java.util.regex.Pattern;

/**
 * Represents the preamble of a DAXP message — defines
 * how the rest of the message is encoded and parsed.
 */
public class DaxPreamble {
    Pattern pairPattern;
    private String protocolVersion = "1";       // DAXP=1
    //    private String context;               // CTX=RxModeler or GUI, optional
    private int cnt;                      //CNT  Number of item lines following preamble. Useful for validation.


    private Character pairSeparator = 0x001;
    private DaxTagFormat tagFormat;       // TF=DEC
    private DaxEncoding encoding;         // EN=UTF8

    public int getMsgCnt() {
        return cnt;
    }


    public DaxPreamble(){
        this.tagFormat = DaxTagFormat.DEC;
        this.encoding = DaxEncoding.UTF8;
    }




    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setTagFormat(DaxTagFormat tagFormat) {
        this.tagFormat = tagFormat;
    }

    public void setEncoding(DaxEncoding encoding) {
        this.encoding = encoding;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getProtocolVersion() { return protocolVersion; }
    public DaxTagFormat getTagFormat() { return tagFormat; }
    public DaxEncoding getEncoding() { return encoding; }
//    public String getContext() { return context; }

    public Character getPairSeparator() {
        return pairSeparator;
    }

    public void setPairSeparator(Character pairSeparator) {
        this.pairSeparator = pairSeparator;
        this.setPairPattern(DaxDecodeService.getPairPattern(pairSeparator));
    }

    public Pattern getPairPattern() {
        return pairPattern;
    }

    public void setPairPattern(Pattern pairPattern) {
        this.pairPattern = pairPattern;
    }
}