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
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleTag_OLD;
import org.daxprotocol.core.parsers.DaxPatternFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//        //Check message is a DAXP
////        if(!msg.startsWith(DaxpConfig.DAXP_PREAMBLE_PREFIX)){
//        //TODO Add to protocol rules
//        if(!msg.startsWith(DaxPreambleTag_OLD.DAXP)){
//            throw new RuntimeException("It is NOT DAXP message !!!");
//        }
//
//        //TODO Add to protocol rules
//        int fistMsgIdx = msg.indexOf(String.valueOf(DaxTagConst.MSG_TYPE.getTagId())+ DaxConfig.EQUAL);
//
//       // String pream = msg.substring(0,fistMsgIdx);
//        Matcher m = fistMsgIdx > 0 ? pairPattern.matcher(msg.substring(0,fistMsgIdx)) :
//                    pairPattern.matcher(msg);
//
//        while (m.find()) {
//            map.put(m.group(1), m.group(2));
//        }
//        return map;
//    }

    /** Decode msgStr format → Preamble object. */
 //   @Override
    public DaxPreamble decode(String msgStr) {
        DaxPreamble preamble = new DaxPreamble();

//        //TODO  Refactoring with new parser
//
//        Pattern pairPattern =  DaxPatternFactory.compilePreamblePairPattern(DaxConfig.PAIR_SEPARATOR);
//
//        Map<String, String> map = parsePreamble(msgStr, pairPattern);
//
//        preamble.setProtocolVersion(map.getOrDefault(DaxPreambleTag_OLD.DAXP, DaxConfig.PROTOCOL_VERSION));
//
//        Optional<DaxCharacterEncoding>  encodingOpt = DaxCharacterEncoding.fromName(
//                map.getOrDefault(DaxPreambleTag_OLD.ENCODING,config.getDefaultEncoding().getCanonicalName()));
//
//        encodingOpt.ifPresent(preamble::setEncoding);
//
//        preamble.setMsgCnt(Integer.parseInt(map.getOrDefault(DaxPreambleTag_OLD.MSG_COUNT,"1")));
//
//        String context = map.getOrDefault(DaxPreambleTag_OLD.MSG_CONTEXT,
//                                          map.getOrDefault(DaxPreambleTag_OLD.MSG_SENDER,
//                                                  contextMapper.getReference(config.getAppContextId())
//                                          )
//        );
//
//        preamble.setMsgContextId(contextMapper.getReferenceId(context));
        return preamble;

    }


}
