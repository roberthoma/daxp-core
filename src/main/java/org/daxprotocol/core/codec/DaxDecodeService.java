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
package org.daxprotocol.core.codec;

import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaxDecodeService {

    /** Parses key=value pairs separated by the given delimiter. */
    static Map<String, String> parseKv(String section, char sep) {
        Map<String, String> map = new LinkedHashMap<>();
        if (section == null || section.isEmpty()) return map;

        String[] parts = section.split(Pattern.quote(String.valueOf(sep)));
        for (String part : parts) {
            if (part.isEmpty()) continue;
            int eq = part.indexOf('=');
            if (eq <= 0) continue; // no key=value
            String key = part.substring(0, eq).trim();
            String val = part.substring(eq + 1).trim();
            if (!key.isEmpty()) map.put(key, val);
        }
        return map;
    }


    public static List<DaxStringPair> parsePairs(String msg, Pattern pairPattern) {
        List<DaxStringPair> list = new ArrayList<>();
        Matcher m = pairPattern.matcher(msg);
        while (m.find()) {
            String tagStr = m.group(1);
            if (tagStr.matches("\\d+")) {
                int tag = Integer.parseInt(tagStr);
                list.add(new DaxStringPair(tag, m.group(2)));
            }
        }
        return list;
    }
}
