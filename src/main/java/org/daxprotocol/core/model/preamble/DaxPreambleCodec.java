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


import org.daxprotocol.core.codec.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.daxprotocol.core.codec.DaxCodecSymbols.*;

/**
 * Encodes and decodes the PREAMBLE section of a DAXP message.
 * Format example: DAXP=1|TF=DEC|E=UTF8\n
 */
public class DaxPreambleCodec implements DaxCodec<DaxPreamble> {
    /** Encode Preamble object → wire format (string). */
    public String encode(DaxPreamble preamble) {
        Map<String,String> map = new LinkedHashMap<>();
        map.put(DaxPreambleTag.DAXP, preamble.getProtocolVersion());
        map.put(DaxPreambleTag.TF,  preamble.getTagFormat().value());
        map.put(DaxPreambleTag.EN, preamble.getEncoding().value());

        if (preamble.getCnt() > 1){
            map.put(DaxPreambleTag.CNT, String.valueOf(preamble.getCnt()));
        }

//        if (context != null && ! context.isEmpty())
//            map.put(DaxPreambleTag.CTX.tag(), context);


        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> DaxPairCodec.encode(sb,k,v));
        return sb.toString();
    }

    public static DaxPreamble fromMap(Map<String,String> map) {
        DaxPreamble p = new DaxPreamble();
        p.setProtocolVersion(map.getOrDefault(DaxPreambleTag.DAXP, "1"));
        p.setTagFormat(DaxTagFormat.valueOf(map.getOrDefault(DaxPreambleTag.TF, "DEC")));
        p.setEncoding(DaxEncoding.valueOf(map.getOrDefault(DaxPreambleTag.EN, "UTF8")));
//        p.context = map.get(DaxPreambleTag.CTX.tag());
        p.setCnt(Integer.parseInt(map.getOrDefault(DaxPreambleTag.CNT,"1")));
        return p;
    }

    public static Pattern getPairPattern(String msgStr){
        int  pairSeparatorIdx = msgStr.indexOf("TF=")-1;  // Example |TF= > |
        char pairSeparator = msgStr.charAt(pairSeparatorIdx);
        return Pattern.compile("(\\w+)"+ DaxCodecSymbols.EQUAL+"([^"+pairSeparator+"]*)");
    }

    public static Map<String, String> parsePreamble(String msg, Pattern pairPattern  ) {
        Map<String, String> map = new HashMap<>();

        // Everything before tag 9=
        String preamblePart = msg.split(String.valueOf(DaxTag.MSG_TYPE)+DaxCodecSymbols.EQUAL)[0];
        Matcher m = pairPattern.matcher(preamblePart);

        while (m.find()) {
            map.put(m.group(1), m.group(2));
        }
        return map;
    }

    /** Decode wire format → Preamble object. */
    public DaxPreamble decode(String msgStr) {

        Map<String, String> preambleMap = parsePreamble(msgStr, getPairPattern(msgStr));

        return  fromMap(preambleMap);
    }




}
