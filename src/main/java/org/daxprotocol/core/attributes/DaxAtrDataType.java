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

package org.daxprotocol.core.attributes;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxAtrDataType extends DaxPair<Character> {
    public static Character DATA_TYPE_INTEGER = 'I';
    public static Character DATA_TYPE_STRING  = 'S';
    public static Character DATA_TYPE_BOOLEAN = 'B';
    public static Character DATA_TYPE_CHAR    = 'C';



    public DaxAtrDataType(Class<?> clazz) {
        super(DaxTag.FIELD_DATA_TYPE, classToChar(clazz));
    }

//    public static Class<?> toClazz(Character cc){
//
//    }


        public static Character classToChar(Class<?> clazz){

        if (clazz == null) return null;

        String key = clazz.isPrimitive() ? clazz.getName() : clazz.getSimpleName();

        switch (key) {
            case "String":
                return DATA_TYPE_STRING;
            case "Integer":
            case "int":
                return DATA_TYPE_INTEGER;
            case "Character":
            case "char":
                return DATA_TYPE_CHAR;
            case "Boolean":
            case "boolean":
                return DATA_TYPE_BOOLEAN;
            default:
                return '?';
        }

    }

}
