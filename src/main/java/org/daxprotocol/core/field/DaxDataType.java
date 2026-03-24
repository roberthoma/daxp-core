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

package org.daxprotocol.core.field;

import org.daxprotocol.core.annotation.DaxpDTO;

import java.util.Date;
import java.util.Map;
/*
TODO Extend for
- email
- BigDecimal
- LocalDate
- LocalDateTime
- JSON
- XML
- DayOfMonth
- Country 	-  String field (see definition of "String" above) representing a country using ISO 3166
- Currency - String field (see definition of "String" above) representing a currency type using ISO 4217 Currency <15> code (3 character) values.
like FIX protocol
-Regexp

- String
- Integer
- Double
- Float
- boolean

From java
primitive type → int, long, boolean
class type → String, Customer
interface type → List, Serializable
enum type → OrderStatus
record type → CustomerDto
annotation type → @Override
array type → String[]


S = String
I = Integer
B = Boolean
D = LocalDate
T = LocalDateTime
F = Float
R = Double
N = Decimal
P = BigDecimal
J = JSON
X = XML
Q = Regexp
e = Email
M = DayOfMonth
C = Country
U = Currency
Y = Enum
K = Tag
O = DTO


* */
public enum DaxDataType {
    INTEGER('I'),
    LONG('L'),
    STRING('S'),
    BOOLEAN('B'),
    CHAR('C'),
    ENUM('E'),
    DATE('D'),
    DTO('O'),
    UNKNOWN('?');

    private final char code;

    private static final Map<Class<?>, DaxDataType> TYPE_MAP = Map.of(
            String.class, STRING,
            Integer.class, INTEGER,
            int.class, INTEGER,
            Long.class, LONG,
            long.class, LONG,
            Boolean.class, BOOLEAN,
            boolean.class, BOOLEAN,
            Character.class, CHAR,
            char.class, CHAR,
            Enum.class, ENUM
    );

    DaxDataType(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static DaxDataType fromCode(Character code) {
        if (code == null) {
            return null;
        }

        for (DaxDataType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        return UNKNOWN;
    }

    public static DaxDataType fromClass(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        if (clazz.isEnum()) {
            return ENUM;
        }

        if (clazz.isAnnotationPresent(DaxpDTO.class)) {
            return DTO;
        }

        if (Date.class.isAssignableFrom(clazz)) {
            return DATE;
        }

        return TYPE_MAP.getOrDefault(clazz, UNKNOWN);
    }
}