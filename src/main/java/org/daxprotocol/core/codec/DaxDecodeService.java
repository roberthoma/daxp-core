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



import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaxDecodeService {
    private static final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<>();
    static {
        CONVERTERS.put(String.class, s -> s);
        CONVERTERS.put(int.class,    Integer::parseInt);
        CONVERTERS.put(Integer.class,Integer::valueOf);
        CONVERTERS.put(long.class,   Long::parseLong);
        CONVERTERS.put(Long.class,   Long::valueOf);
        CONVERTERS.put(boolean.class,s -> Boolean.parseBoolean(s));
        CONVERTERS.put(Boolean.class,Boolean::valueOf);
        CONVERTERS.put(double.class, Double::parseDouble);
        CONVERTERS.put(Double.class, Double::valueOf);
        // add more as needed (char, BigDecimal, enums, etc.)
    }

    public static Pattern getPairPattern(char pairSeparator) {
        return Pattern.compile("(\\w+)"+ DaxCodecSymbols.EQUAL+"([^"+pairSeparator+"]*)");
    }


    public static Object convert(String raw, Class<?> type) {
        Function<String, Object> fn = CONVERTERS.get(type);
        if (fn == null) {
            throw new IllegalArgumentException("No converter for type: " + type.getName());
        }
        return fn.apply(raw);
    }

    /** Parses key=value pairs separated by the given delimiter. */
    static Map<String, String> parseKv(String section, char sep) {
        Map<String, String> map = new LinkedHashMap<>();
        if (section == null || section.isEmpty()) return map;

        String[] parts = section.split(Pattern.quote(String.valueOf(sep)));
        for (String part : parts) {
            if (part.isEmpty()) continue;
            int eq = part.indexOf(DaxCodecSymbols.EQUAL);
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
