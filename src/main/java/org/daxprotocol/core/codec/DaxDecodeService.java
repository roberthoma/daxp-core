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

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaxDecodeService {
    private static final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(String.class, s -> s);
        CONVERTERS.put(int.class, Integer::parseInt);
        CONVERTERS.put(Integer.class, Integer::valueOf);
        CONVERTERS.put(long.class, Long::parseLong);
        CONVERTERS.put(Long.class, Long::valueOf);
        CONVERTERS.put(boolean.class, s -> Boolean.parseBoolean(s));
        CONVERTERS.put(Boolean.class, Boolean::valueOf);
        CONVERTERS.put(double.class, Double::parseDouble);
        CONVERTERS.put(Double.class, Double::valueOf);

        // add more as needed (char, BigDecimal, enums, etc.)
    }

    public static Pattern getPreamblePairPattern(char pairSeparator) {
        return Pattern.compile("(\\w+)" + DaxCodecSymbol.EQUAL + "([^" + pairSeparator + "]*)");
    }

    public static Pattern getMessagePairPattern(char pairSeparator) {
        String sep = Pattern.quote(String.valueOf(pairSeparator));
        return Pattern.compile(
                "(?:([A-Za-z0-3]{0,3}):)?(\\d+)"
                        + DaxCodecSymbol.EQUAL +
                        "([^" + sep + "]*)" +
                        sep
        );
    }


    public static Object convert(String value, Class<?> type) {
        Function<String, Object> fn = CONVERTERS.get(type);
        if (fn != null) {
            return fn.apply(value);
        }
        //Enum support
        if (type.isEnum()) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            Object enumValue = Enum.valueOf((Class<Enum>) type, value);
            return enumValue;
        }
        throw new IllegalArgumentException("No converter for type: " + type.getName());

    }


    public static List<DaxStringPair> parsePairs(String msg, Pattern pairPattern, String dftContext) {
        List<DaxStringPair> list = new ArrayList<>();
        Matcher m = pairPattern.matcher(msg);
        while (m.find()) {
            String contextSymbol;
            String contextStr = m.group(1);
            int tagId = Integer.parseInt(m.group(2));
            int contextId;
            if (contextStr == null) {
                if (tagId < DaxpConfig.MAX_DAXP_TAG_ID) {
                    contextSymbol = DaxpConfig.DAX_CONTEXT_SYMBOL;
                } else {
                    contextSymbol = dftContext;
                }
            } else {
                contextSymbol = contextStr;
            }
            contextId = DaxContextMapper.getContextId(contextSymbol);
            list.add(new DaxStringPair(new DaxTag(contextId, tagId), m.group(3)));
        }
        return list;
    }

    public static DaxTag parseDaxTag(String tagStr) {
        int tagId;
        int contextId = 0;
        Pattern pattern = Pattern.compile("^(?:([A-Za-z]+):)?([0-9]+)$");

        Matcher m = pattern.matcher(tagStr);

        if (m.matches()) {
            String contextSymbol = m.group(1); // null if no context
            tagId = Integer.parseInt(m.group(2));

            if (contextSymbol == null){
                if (tagId > DaxpConfig.MAX_DAXP_TAG_ID) {
                    contextId = DaxpConfig.APP_CONTEXT_ID;
                }
            }
            return new DaxTag(contextId, tagId);
        }



        throw new RuntimeException("NOT correct DaxTag "+tagStr);
    }
}