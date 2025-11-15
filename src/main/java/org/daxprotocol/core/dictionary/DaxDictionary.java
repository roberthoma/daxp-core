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

import org.daxprotocol.core.annotation.DaxpFieldGroup;
import org.daxprotocol.core.field.*;
import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.group.DaxpGroupItf;

import java.util.*;

public class DaxDictionary {
    /*****************************************************
     * Dictionary of messages type, roles
     * Key: Message type
     * */
    Map<String, DaxMessageDicItem> msgMap = new HashMap<>();


    /*****************************************************
     *  Standard valueNamesMap
     * Map of string values and description ; enums others dictionary
     * Key : idField
     * */

    Map<Integer, Map<String,String>> valueDicMap = new HashMap<>();


    /*****************************************************
     * DescriptiveMap : it is main dic of field attributes
     * Key : idField
     * Value : map of attributes
     * */
    Map<Integer, Map<Integer, DaxPair<?>>> attributMap = new HashMap<>();

    /*****************************************************
     *  Group Map
     */
     Map<Integer, DaxpFieldGroup> groupMap = new HashMap<>();



 public DaxDictionary(){
        //TMP
        System.out.println("Init DaxDictionary...");

    }



    private <K,V>  Map<K,V> putAndReturn(Map<K,V> map , K k,V v){
        map.put(k,v);
        return map;
    }


    public void putMsgItem(DaxMessageDicItem messageDicItem){
        if (msgMap.containsKey(messageDicItem.getMsgType())){
            throw new RuntimeException( "Message "+messageDicItem.getMsgType()
                                       +" exists in DAXP dictionary !!!");
        }
        msgMap.put(messageDicItem.getMsgType(),messageDicItem);
    }


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

    public void addGroup(DaxpFieldGroup group){
        groupMap.put(group.id(), group);
    }


    public Map<Integer, DaxpFieldGroup> getGroupMap() {
       return groupMap;
    }

    public void putAtrGroupId(int fieldId, int groupId) {
       if(groupId==0) {
           return;
       }
       putAttribute(fieldId, new DaxAtrGroupId(groupId));
    }


    //----------------------------------------------------
    public void join (DaxDictionary dic){

        //TODO Validation for double idField in joined dictionary
        valueDicMap.putAll(dic.getValueDicMap());
    }

}
