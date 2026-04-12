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
package org.daxprotocol.core.codec;


import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.exceptions.DaxMsgParserException;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleTag;
import org.daxprotocol.core.model.preamble.DaxPreambleTag_OLD;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.daxprotocol.core.config.DaxConfig.EQUAL;
import static org.daxprotocol.core.config.DaxConfig.PAIR_SEPARATOR;

/**
 * Encodes and decodes the PREAMBLE section of a DAXP message.
 * Format example: DAXP=v0.0.1|E=UTF-8\n
 */
//public class DaxPreambleCodec implements DaxCodec<DaxPreamble> {
public class DaxPreambleCodec {

    DaxConfig config;
    DaxContextMapper contextMapper;

    public DaxPreambleCodec(DaxConfig config, DaxContextMapper contextMapper) {
        this.config = config;
        this.contextMapper = contextMapper;
    }

    private  void encode(StringBuilder sb, String tag, String value ) {
        if (value.isBlank()){
            return ;
        }
        sb.append(tag)
                .append(EQUAL)
                .append(value)
                .append(PAIR_SEPARATOR);
    }



    /** Encode Preamble object → wire format (string). */
    public String encode(DaxPreamble preamble) {
        Map<String,String> map = new LinkedHashMap<>();
        map.put(DaxPreambleTag_OLD.ENCODING, preamble.getEncoding().getCanonicalName());
        map.put(DaxPreambleTag_OLD.MSG_CONTEXT,contextMapper.getReference(preamble.getMsgContextId()));

        if (preamble.getMsgCnt() > 1){
            map.put(DaxPreambleTag_OLD.MSG_COUNT, String.valueOf(preamble.getMsgCnt()));
        }


        StringBuilder sb = new StringBuilder();
        encode(sb, DaxPreambleTag_OLD.DAXP, preamble.getProtocolVersion() ); //Always first

        map.forEach((k, v) -> encode(sb,k,v));
        return sb.toString();
    }


//    public  Map<String, String> parsePreamble(String msg) {
//        Pattern pattern = DaxPatternFactory.compilePreamblePairPattern(DaxConfig.PAIR_SEPARATOR);
//        return parsePreamble(msg, pattern);
//    }

//    public  Map<String, String> parsePreamble(String msg, Pattern pairPattern) {
//        Map<String, String> map = new HashMap<>();
//

//        return map;
//    }

    /** Decode msgStr format → Preamble object. */
 //   @Override
//    public DaxPreamble decode(String msgStr) {
//        DaxPreamble preamble = new DaxPreamble();
//
//        return preamble;
//
//    }
    public boolean isTagPreamble(String tagStr){
        return  DaxPreambleTag.contains(tagStr);
    }

    public void decodeTag(String tagStr, String valueStr ,DaxPreamble preamble) {
        if (!DaxPreambleTag.contains(tagStr)) {
           throw new DaxMsgParserException("It "+tagStr+ " NOT  preamble tag !!!");
        }
            DaxPreambleTag tag = DaxPreambleTag.fromTag(tagStr);
            switch (tag) {
                case DAXP        -> preamble.setProtocolVersion(valueStr);
                case ENCODING    -> DaxCharacterEncoding.fromName(valueStr).ifPresent(preamble::setEncoding);
                case MSG_COUNT   -> preamble.setMsgCnt(Integer.parseInt(valueStr));
                case MSG_CONTEXT -> preamble.setMsgContextId(contextMapper.getReferenceId(valueStr));
                //case MSG_SENDER  -> preamble.setSe System.out.println("Sender: " + value);
            }
        }


}
