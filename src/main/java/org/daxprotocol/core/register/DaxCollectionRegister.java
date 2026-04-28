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

package org.daxprotocol.core.register;

import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxCollectionTool;

import java.util.HashMap;
import java.util.Map;


//TODO move back to dictionary
public class DaxCollectionRegister {
    Integer contextId;

    /*****************************************************
     *  Standard EnumMap
     * Map of string values and description ; enums others dictionary
     * Key : idField
     * */

    Map<DaxTag, DaxEnum> enumMap      = new HashMap<>();
//    Map<DaxTag, Enum<?> > enumMap2      = new HashMap<>();

    /*****************************************************
     *  Standard valueNamesMap
     * Map of string values and description ; enums others dictionary
     * Key : idField
     * */
    //TODO create reference mapper
    //
    Map<DaxTag, Map<String, DaxEnumValue>> enumValueMap = new HashMap<>();


    public DaxCollectionRegister(Integer contextId){
        this.contextId = contextId;
    }


    public void putEnum(DaxTag tag, DaxEnum daxEnum){
        enumMap.computeIfAbsent(tag,nameS -> daxEnum);
    }

//    public void putEnum2(DaxTag tag, Enum<?> daxEnum){
//        enumMap2.computeIfAbsent(tag,nameS -> daxEnum);
//    }


//    public void putEnumValue(DaxTag tag, String value, String desc){
    public void putEnumValue(DaxTag tag, DaxEnumValue enumValue){

        enumValueMap.merge(tag,new HashMap<>(Map.of(enumValue.getValue(), enumValue)),
                (svMap, svMapN)
                        ->  DaxCollectionTool.putAndReturnMap(svMap,enumValue.getValue(), enumValue));
    }

    public Map<DaxTag, Map<String, DaxEnumValue>> getValueMap() {
        return  enumValueMap;
    }

    public  Map<DaxTag, DaxEnum> getEnumMap(){
        return enumMap;
    }


    public Map<String, DaxEnumValue> getEnumValueMap(DaxTag daxTag) {
        return enumValueMap.get(daxTag);
    }
}
