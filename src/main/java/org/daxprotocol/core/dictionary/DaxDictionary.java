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

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.context.DaxContext;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumName;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumValue;
import org.daxprotocol.core.field.*;
import org.daxprotocol.core.group.DaxGroup;
import org.daxprotocol.core.group.DaxpGroupItf;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxTool;

import java.util.HashMap;
import java.util.Map;


public class DaxDictionary {



    Map<Integer, DaxContext> contextMap = new HashMap<>();

    Map<Integer, DaxContextDictionary> dictionaryMap = new HashMap<>();


    public DaxDictionary() {
        System.out.println("Init DaxDictionary...");
    }


    public DaxContextDictionary getDictionary(int contextId){
        if(!dictionaryMap.containsKey(contextId)){
            dictionaryMap.put(contextId, new DaxContextDictionary(contextId));
        }
        return dictionaryMap.get(contextId);
    }

    public DaxContextDictionary getDefaultDictionary(){

        return getDictionary(DaxpConfig.getApplicationContextId());
    }


    public void putDictionary(DaxContextDictionary dictionary){

        dictionaryMap.put(dictionary.contextId,dictionary);
    }
    //**********************************************************************
    // Context

    public void setContextMap(Map<Integer, DaxContext> contextMap) {
        this.contextMap = contextMap;
    }

    public Map<Integer, DaxContext> getContextMap() {
        return contextMap;
    }

    //**********************************************************************
    // Messages


    public void putMsgItem(DaxMessageDicItem messageDicItem){
        DaxContextDictionary dic =  getDefaultDictionary();
        dic.messageDic.putMsgItem(messageDicItem);
    }

    public Map<String, DaxMessageDicItem> getMsgMap() {
        DaxContextDictionary dic =  getDefaultDictionary();
        return dic.messageDic.getMsgMap();
    }



    //**********************************************************************
    // Enums


    public void putEnumValue(String enumName, String value, String desc){
        DaxContextDictionary dic =  getDefaultDictionary();
        dic.enumDictionary.putEnumValue(enumName, value, desc);
    }

    public void putEnum(String enumName, String desc){
        DaxContextDictionary dic =  getDefaultDictionary();
        dic.enumDictionary.putEnum(enumName, desc);
    }

    public void putEnum(DaxEnumName enumName){
        DaxContextDictionary dic =  getDefaultDictionary();
        dic.enumDictionary.putEnum(enumName.getName(), enumName.getDesc());
    }


    public Map<String, Map<String, DaxEnumValue>> getEnumValueMap() {
        DaxContextDictionary dic =  getDefaultDictionary();
        return  dic.enumDictionary.getValueMap();
    }

    public Map<String, DaxEnumName>  getEnumMap() {
        DaxContextDictionary dic =  getDefaultDictionary();
        return  dic.enumDictionary.getEnumMap();
    }


    //**********************************************************************
    // Groups

    public void putGroup(int idGroup, String grpName){
        DaxContextDictionary dic =  getDefaultDictionary();

        DaxGroup grp =  new DaxGroup(idGroup,0,grpName);
        dic.groupMap.put(grp.getId(),grp);

    }

    public Map<Integer, DaxpGroupItf> getGroupMap() {
        DaxContextDictionary dic =  getDefaultDictionary();
        return dic.groupMap;
    }

    //**********************************************************************
    // Attributes

    private void putAttribute(int contextId, int tagId, DaxPair<?> atrPair){

        DaxContextDictionary dic =  getDictionary(contextId);

        dic.attributMap.merge(tagId, new HashMap<>(Map.of(atrPair.getTag().getTagId(), atrPair)),
                (eM, nM) ->
                        DaxTool.putAndReturn(eM, atrPair.getTag().getTagId(), atrPair));

    }

    public void putAttribute(int tagId, DaxPair<?> atrPair){
        putAttribute(DaxpConfig.getApplicationContextId(), tagId, atrPair);
    }

    public void putAttribute(DaxTag tag, DaxPair<?> atrPair){
        putAttribute(tag.getContextId(), tag.getTagId(), atrPair);
    }

    //-----------------

    public Map<Integer, DaxPair<?>> getFieldAttributeMap(int tagId) {
        DaxContextDictionary dic =  getDefaultDictionary();
        return dic.attributMap.get(tagId);
    }

    public Map<Integer, Map<Integer, DaxPair<?>>> getAttributMap(){
        DaxContextDictionary dic =  getDefaultDictionary();
        return dic.attributMap;
    }


    public void put(int tagId,  Class<?> clazz){
        putAttribute(tagId, new DaxAtrDataType(clazz));
    };

    public void putAtrDataType(int tagId,  Class<?> clazz){
        putAttribute(tagId, new DaxAtrDataType(clazz));
    };


    public void putAtrDataType(DaxTag tag,  Class<?> clazz){
        putAttribute(tag, new DaxAtrDataType(clazz));
    };


    public void putAtrDataType(int tagId,  Character c){
        putAttribute(tagId, new DaxAtrDataType(c));
    };


    public void putAtrUiLabel(int tagId,  String uiLabel){
        putAttribute(tagId, new DaxAtrUiLabel(uiLabel));
    }
    public void putAtrUiLabel(DaxTag tag,  String uiLabel){
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrUiLabel(uiLabel));
    }

    public void putAtrSizeMax(int tagId,  Integer max){
        putAttribute(tagId, new DaxAtrSizeMax(max));
    }

    public void putAtrSizeMax(DaxTag tag,  Integer max){
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrSizeMax(max));
    }

    public void putAtrSizeMin(int tagId,  Integer min){
        putAttribute(tagId, new DaxAtrSizeMin(min));
    }
    public void putAtrSizeMin(DaxTag tag,  Integer min){
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrSizeMin(min));
    }


    public void putAtrNullable(int tadId,  Character able){
        putAttribute(tadId, new DaxAtrNullable(able));
    }
    public void putAtrNullable(DaxTag tag,  Character able){
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrNullable(able));
    }


    public void putAtrEnumName(int tagId, String enumName) {
        putAttribute(tagId, new DaxAtrEnumName(enumName));
    }
    public void putAtrEnumName(DaxTag tag, String enumName) {
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrEnumName(enumName));
    }

    public void putAtrGroupId(DaxTag tag, int groupId) {
        if(groupId==0) {
            return;
        }
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrGroupId(groupId));
    }
    public void putAtrGroupId(int tagId, int groupId) {
        if(groupId==0) {
            return;
        }
        putAttribute(tagId, new DaxAtrGroupId(groupId));
    }


}
