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

import java.util.*;
import java.util.function.Function;

//TODO join with DaxAtrDataType
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



}