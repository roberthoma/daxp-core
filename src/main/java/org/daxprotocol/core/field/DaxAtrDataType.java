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

import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;

import java.util.Date;

//TODO data type to has to be developed
public class DaxAtrDataType extends DaxPair<Character> {
    public final static Character DATA_TYPE_INTEGER = 'I';
    public final static Character DATA_TYPE_LONG    = 'L';
    public final static Character DATA_TYPE_STRING  = 'S';
    public final static Character DATA_TYPE_BOOLEAN = 'B';
    public final static Character DATA_TYPE_CHAR    = 'C';
    public final static Character DATA_TYPE_ENUM    = 'E';
    public final static Character DATA_TYPE_DATE    = 'D';


    public DaxAtrDataType(Character c) {
        super(DaxTagConst.FIELD_DATA_TYPE, c);
    }


    public DaxAtrDataType(Class<?> clazz) {
        super(DaxTagConst.FIELD_DATA_TYPE, classToChar(clazz));
    }


//TODO for refactoring
        public static Character classToChar(Class<?> clazz){

        if (clazz == null) {
            return null;
        }

        String key = clazz.isPrimitive() ? clazz.getName() : clazz.getSimpleName();

        if(clazz.isEnum()){
            return DATA_TYPE_ENUM;
        }

            return switch (key) {
                case "String" -> DATA_TYPE_STRING;
                case "Integer", "int" -> DATA_TYPE_INTEGER;
                case "Character", "char" -> DATA_TYPE_CHAR;
                case "Boolean", "boolean" -> DATA_TYPE_BOOLEAN;
                case "Long" -> DATA_TYPE_LONG;
                case "Date" -> DATA_TYPE_DATE;
                case "Enum" -> DATA_TYPE_ENUM;
                default -> '?';
            };

    }
    public static Class<?>  charToClass(Character c){

        return switch (c) {
            case 'S' -> String.class;
            case 'L' -> Long.class;
            case 'I' -> Integer.class;
            case 'C' -> Character.class;
            case 'B' -> Boolean.class;
            case 'D' -> Date.class;
            case 'E' -> Enum.class;
            default -> Object.class;
        };


    }



}
