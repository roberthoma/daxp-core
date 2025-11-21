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

import org.daxprotocol.core.dictionary.daxenum.DaxDictionaryEnum;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumName;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumValue;
import org.daxprotocol.core.field.*;
import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.group.DaxGroup;
import org.daxprotocol.core.group.DaxpGroupItf;

import java.util.*;
//TODO create context dictionary
public class DaxDictionary {


    //TODO add group of values
//            7=12|5=L|141=35|100=2001,2002,2005,2074|
//            7=12|5=D|141=35|100=2001,2002,2005,2074

    // TODo Dictionary od fields define without identification of group


    /*****************************************************
     * Dictionary of messages type, roles
     * Key: Message type
     * */
    Map<String, DaxMessageDicItem> msgMap = new HashMap<>();


    /*****************************************************
     * DescriptiveMap : it is main dic of field attributes
     * Key : idField
     * Value : map of attributes
     * */
    Map<Integer, Map<Integer, DaxPair<?>>> attributMap = new HashMap<>();

    /*****************************************************
     *  Group Map
     */
     Map<Integer, DaxpGroupItf> groupMap = new HashMap<>();
//     Map<Integer, DaxpFieldGroup> groupMap = new HashMap<>();


    DaxDictionaryEnum enumDictionary = new DaxDictionaryEnum();

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


    public Map<String, Map<String, DaxEnumValue>> getEnumValueMap() {
        return  enumDictionary.getValueMap();
    }

    public Map<String, DaxEnumName>  getEnumMap() {
        return  enumDictionary.getEnumMap();
    }

    public void put(int fieldId,  Class<?> clazz){
        putAttribute(fieldId, new DaxAtrDataType(clazz));
    };

    public void putAtrDataType(int fieldId,  Class<?> clazz){
        putAttribute(fieldId, new DaxAtrDataType(clazz));
    };

    public void putAtrDataType(int fieldId,  Character c){
        putAttribute(fieldId, new DaxAtrDataType(c));
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

    public void putEnumValue(String enumName, String value, String desc){
        enumDictionary.putEnumValue(enumName, value, desc);
    }

    public void putEnum(String enumName, String desc){
        enumDictionary.putEnum(enumName, desc);
    }

    public void putEnum(DaxEnumName enumName){
        enumDictionary.putEnum(enumName.getName(), enumName.getDesc());
    }

    public void putGroup(int idGroup, String grpName){

        DaxGroup grp =  new DaxGroup(idGroup,0,grpName);


       groupMap.put(grp.getId(),grp);

   }

    public Map<Integer, DaxpGroupItf> getGroupMap() {
       return groupMap;
    }

    public void putAtrGroupId(int fieldId, int groupId) {
       if(groupId==0) {
           return;
       }
       putAttribute(fieldId, new DaxAtrGroupId(groupId));
    }



    public void putAtrEnumName(int fieldId, String enumName) {
        putAttribute(fieldId, new DaxAtrEnumName(enumName));
    }

    public Map<String, DaxMessageDicItem> getMsgMap() {
        return msgMap;
    }

    //----------------------------------------------------
    public void join (DaxDictionary dic){
        //TODO Validation for double idField in joined dictionary
//        enumValueMap.putAll(dic.getEnumValueMap());
    }

}
