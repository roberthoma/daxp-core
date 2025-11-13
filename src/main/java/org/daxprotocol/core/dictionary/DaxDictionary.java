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
package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.attributes.*;
import org.daxprotocol.core.codec.DaxPair;

import java.util.*;

public class DaxDictionary {

    private <K,V>  Map<K,V> putAndReturn(Map<K,V> map , K k,V v){
        map.put(k,v);
        return map;
    }

    /** Standard valueNamesMap
     * Map of string values and description
     * Key : idField
     * */

    Map<Integer, Map<String,String>> valueDicMap = new HashMap<>();


    /**
     * DescriptiveMap : it is main dic of field attributes
     * Key : idField
     * Value : map of attributes
     * */
    Map<Integer, Map<Integer, DaxPair<?>>> attributMap = new HashMap<>();

    public void putAttribute(int fieldId, DaxPair<?> atrPair){
        attributMap.merge(fieldId, new HashMap<>(Map.of(atrPair.getTag(), atrPair)),
        (eM, nM) -> putAndReturn(eM, atrPair.getTag(), atrPair));

    }


    public Map<Integer, DaxPair<?>> getFieldAttributeMap(int fieldId) {
        return attributMap.get(fieldId);
    }

    public Map<Integer, Map<Integer, DaxPair<?>>> getAttributMap(){
      return attributMap;
    }



    public Map<Integer, Map<String, String>> getValueDicMap() {
        return valueDicMap;
    }

    public void put(int fieldId,  Class<?> clazz){
        putAttribute(fieldId, new DaxAtrDataType(clazz));
    };

    public void putAtrDataType(int fieldId,  Class<?> clazz){
        putAttribute(fieldId, new DaxAtrDataType(clazz));
    };

   public void putAtrUiLabel(int fieldId,  String uiLabel){
       putAttribute(fieldId, new DaxAtrUiLabel(uiLabel));
   }

    public void putAtrSizeMax(int fieldId,  Integer max){
        putAttribute(fieldId, new DaxAtrSizeMax(max));
    }

    public void putAtrSizeMin(int fieldId,  Integer min){
        putAttribute(fieldId, new DaxAtrSizeMin(min));
    }


    public void putAtrNullable(int fieldId,  Character able){
        putAttribute(fieldId, new DaxAtrNullable(able));
    }

    public void putDicValue(int idField, String value, String desc){
        valueDicMap.merge(idField,new HashMap<>(Map.of(value,desc)),
                (svMap, svMapN) ->  putAndReturn(svMap, value,desc));
    }

    //----------------------------------------------------
    public void join (DaxDictionary dic){
//TODO walidacja czy istniejący już idField czasami się nie dubluje.

  //      attributeMap.putAll (dic.getAttributeMap());
        valueDicMap.putAll(dic.getValueDicMap());
    }


}
