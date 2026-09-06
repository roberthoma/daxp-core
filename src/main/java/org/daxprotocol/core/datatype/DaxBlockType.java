/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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

package org.daxprotocol.core.datatype;

///Type of block in message

public enum DaxBlockType {//extends DaxValue<String> {

    BLOCK_MESSAGE('M',"Message block"), /// Only in dictionary type message
    BLOCK_NAMESPACE('S',"Namespace block"), /// Only in dictionary type message
    BLOCK_ENTITY('E', "Entity block"),
    BLOCK_TYPES('T',"Type definition block"), /// Only in dictionary type message
    BLOCK_FIELD('F',"Field definition block"),
    BLOCK_VALUE('V',"Value  block"),   // for example Collection value
    BLOCK_INSTANCE('I',"Instance of entity block")
   ;
    private final Character code;
    private final String description;
    /**
     * Determines the DAXP type based on a Java Object.
     * Useful when serializing from Java to DAXP.
     */


    DaxBlockType(Character code, String description){ //, Class<?> javaType) {
        this.code = code;
        this.description = description;
    }


    public Character getCode() { return code; }
    public String getDescription() { return description; }


    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", code, name(), description);
    }

    /**
     * Finds DaxBlockType by its 3-letter code.
     * @param code e.g. M, X ...
     * @return DaxBlockType or null if not found
     */
    public static DaxBlockType fromCode(char code) {
        return switch (code){
            case 'E' -> BLOCK_ENTITY;
            case 'M' -> BLOCK_MESSAGE;
            case 'S' -> BLOCK_NAMESPACE;
            case 'I' -> BLOCK_INSTANCE;
            case 'D' -> BLOCK_TYPES;
            case 'F' -> BLOCK_FIELD;
//            case 'C' -> BLOCK_COLLECTION;
            case 'V' -> BLOCK_VALUE;
            default ->  throw new IllegalArgumentException("Unknown DAXP data type code: " + code);

        };
    }

}
