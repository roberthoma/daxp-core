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

import static org.daxprotocol.core.codec.DaxTag.*;
public class DaxDictionary {
    /**
     * DescriptiveMap : it is main dic of describes fields / variables
     * Key : idField
     * */
    Map<Integer, Map<Integer, DaxPair<?>>> attributMap = new HashMap<>();

    public Map<Integer, DaxPair<?>> getFieldAttributeMap(int fieldId) {
        return attributMap.get(fieldId);
    }
    public Map<Integer, Map<Integer, DaxPair<?>>> getAttributMap(){
      return attributMap;
    }


    /** Standard valueNamesMap
     * Map of One char values
     * Key : idField
     * */

    Map<Integer, Map<Character,String>> valueNamesMap = new HashMap<>();
    public Map<Integer, Map<Character, String>> getCharacterValueNameMap() {
        return valueNamesMap;
    }


    //TODO Message type

    public void putAttribute(int fieldId, DaxPair<?> atrPair){
       Map<Integer, DaxPair<?>> fieldAtrMap;
       if (attributMap.containsKey(fieldId)){
           fieldAtrMap = attributMap.get(fieldId);
       }
       else {
           fieldAtrMap = new HashMap<>();
           attributMap.put(fieldId,fieldAtrMap);
       }
        fieldAtrMap.put(atrPair.getTag(), atrPair) ;
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



   //isUiEditable


    // ---------------------
    public void putValue(int idField, char charValue, String desc){

        if (!valueNamesMap.containsKey(idField)){
            Map<Character,String> valMap = new HashMap<>();
            valMap.put(charValue,desc);
            valueNamesMap.put(idField, valMap);
            return;
        }

        valueNamesMap.get(idField).put(charValue, desc);

    }
    //----------------------------------------------------
    public void join (DaxDictionary dic){
//TODO walidacja czy istniejący już idField czasami się nie dubluje.

  //      attributeMap.putAll (dic.getAttributeMap());
        valueNamesMap.putAll(dic.getCharacterValueNameMap());
    }


}
