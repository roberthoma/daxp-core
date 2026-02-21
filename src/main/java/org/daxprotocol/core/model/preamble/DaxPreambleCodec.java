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


import org.daxprotocol.core.codec.*;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.daxprotocol.core.config.DaxpConfig.EQUAL;
import static org.daxprotocol.core.config.DaxpConfig.PAIR_SEPARATOR;

/**
 * Encodes and decodes the PREAMBLE section of a DAXP message.
 * Format example: DAXP|E=UTF-8\n
 */
public class DaxPreambleCodec implements DaxCodec<DaxPreamble> {

    DaxpConfig config;
    DaxContextMapper contextMapper;

    public DaxPreambleCodec(DaxpConfig config, DaxContextMapper contextMapper) {
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
        map.put(DaxPreambleTag.VERSION, preamble.getProtocolVersion());
        map.put(DaxPreambleTag.ENCODING, preamble.getEncoding().getCanonicalName());
        map.put(DaxPreambleTag.MSG_CONTEXT,contextMapper.getContextSymbol(preamble.getMsgContextId()));

        if (preamble.getMsgCnt() > 1){
            map.put(DaxPreambleTag.MSG_COUNT, String.valueOf(preamble.getMsgCnt()));
        }


        StringBuilder sb = new StringBuilder();
        sb.append(DaxPreambleTag.DAXP).append(PAIR_SEPARATOR);
        map.forEach((k, v) -> encode(sb,k,v));
        sb.append("\n"); // TODO configuration
        return sb.toString();
    }

    private char getPairSeparator(String msgStr){
        return msgStr.charAt(DaxpConfig.CHAR_SEPARATOR_IDX); // Example After DAXP is "|" separator
    }


    public  Pattern getPairPattern(String msgStr){
        return DaxDecodeService.getPreamblePairPattern(getPairSeparator(msgStr));
    }

//    public static Map<String, String> parsePreamble(String msg) {
    public  Map<String, String> parsePreamble(String msg) {
        return parsePreamble(msg, getPairPattern(msg));
    }

    public  Map<String, String> parsePreamble(String msg, Pattern pairPattern) {
        Map<String, String> map = new HashMap<>();

        //Check message is a DAXP
        if(!msg.startsWith(DaxpConfig.DAXP_PREAMBLE_PREFIX)){
            throw new RuntimeException("It is NOT DAXP message !!!");
        }

        // Everything after "DAXP|" and  before tag "9="
        String preamblePart = msg.substring(DaxpConfig.CHAR_SEPARATOR_IDX +1)
                             .split(String.valueOf(DaxTagConst.MSG_TYPE) + DaxpConfig.EQUAL)[0];

        Matcher m = pairPattern.matcher(preamblePart);

        while (m.find()) {
            map.put(m.group(1), m.group(2));
        }
        return map;
    }

    /** Decode msgStr format → Preamble object. */
    @Override
    public DaxPreamble decode(String msgStr) {
        DaxPreamble p = new DaxPreamble();

        //TODO fix this as no IDEA how to set fof test mode
        DaxpConfig.PAIR_SEPARATOR = getPairSeparator(msgStr);

        p.setPairSeparator(getPairSeparator(msgStr));

        Map<String, String> map = parsePreamble(msgStr, p.getPairPattern());

        p.setProtocolVersion(map.getOrDefault(DaxPreambleTag.VERSION, DaxpConfig.PROTOCOL_VERSION));

        Optional<DaxCharacterEncoding>  encodingOpt = DaxCharacterEncoding.fromName(
                map.getOrDefault(DaxPreambleTag.ENCODING,config.getDefaultEncoding().getCanonicalName()));

        encodingOpt.ifPresent(p::setEncoding);

        p.setMsgCnt(Integer.parseInt(map.getOrDefault(DaxPreambleTag.MSG_COUNT,"1")));

        String context = map.getOrDefault(DaxPreambleTag.MSG_CONTEXT,
                                          map.getOrDefault(DaxPreambleTag.MSG_SENDER,
                                                  contextMapper.getContextSymbol(config.getAppContextId())
                                          )
        );

        p.setMsgContextId(contextMapper.getContextId(context));
        return p;

    }


}
