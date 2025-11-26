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

package org.daxprotocol.core.dictionary.daxenum;

import org.daxprotocol.core.tool.DaxTool;

import java.util.HashMap;
import java.util.Map;

public class DaxDictionaryEnum {

    /*****************************************************
     *  Standard EnumMap
     * Map of string values and description ; enums others dictionary
     * Key : idField
     * */

    Map<String, DaxEnumName> enumMap = new HashMap<>();


    /*****************************************************
     *  Standard valueNamesMap
     * Map of string values and description ; enums others dictionary
     * Key : idField
     * */

    Map<String, Map<String, DaxEnumValue>> enumValueMap = new HashMap<>();



    public void putEnum(String name,  String desc){
        enumMap.computeIfAbsent(name,nameS -> new DaxEnumName(nameS, desc));
    }


    public void putEnumValue(String enumName, String value, String desc){
        enumValueMap.merge(enumName,new HashMap<>(Map.of(value, new DaxEnumValue(value , desc))),
                (svMap, svMapN)
                        ->  DaxTool.putAndReturn(svMap,value, svMapN.get(value)));
    }

    public Map<String, Map<String, DaxEnumValue>> getValueMap() {
        return  enumValueMap;
    }

    public  Map<String, DaxEnumName> getEnumMap(){
        return enumMap;
    }


}
