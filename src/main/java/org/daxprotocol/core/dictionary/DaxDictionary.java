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

package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumDictionary;
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

    DaxpConfig config;

    Map<Integer, DaxContext> contextMap = new HashMap<>();

  //  Map<Integer, DaxContextDictionary> contextDicMap = new HashMap<>();
  DaxEnumDictionary enumDictionary = new DaxEnumDictionary();

    /*****************************************************
     * DescriptiveMap : it is main dic of field attributes
     * Key : tagId
     * Value : map of attributes
     * */
    Map<DaxTag, Map<DaxTag, DaxPair<?>>> attributMap = new HashMap<>();


    /*****************************************************
     *  Group Map
     */
    Map<Integer, DaxpGroupItf> groupMap = new HashMap<>();
//     Map<Integer, DaxpFieldGroup> groupMap = new HashMap<>();


    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx

    //TODO add group of values
//            7=14|5=L|141=35|100=2001,FIX.T3:67,2005,2074|
//            7=15|5=D|141=35|100=2001,2002,2005,2074,FIX:34

    // TODo Dictionary od fields define without identification of group

    DaxMessageDic messageDic = new DaxMessageDic();


    public DaxDictionary(DaxpConfig config) {
        System.out.println("Init DaxDictionary...");
        this.config = config;
    }


    public Map<Integer, DaxContext> getContextMap() {
        return contextMap;
    }


    public void putContext(DaxContext context){
        contextMap.put(DaxContextMapper.getContextId(context.symbol),context);
    }


    //**********************************************************************
    // Messages


    public void putMsgItem(DaxMessageDicItem messageDicItem){
          messageDic.putMsgItem(messageDicItem);
    }

    public Map<String, DaxMessageDicItem> getMsgMap() {
        return messageDic.getMsgMap();
    }



    //**********************************************************************
    // Enums


    public void putEnumValue(String enumName, String value, String desc){
        enumDictionary.putEnumValue(enumName, value, desc);
    }

    public void putEnum(String enumName, String desc){
          enumDictionary.putEnum(enumName, desc);
    }

    public void putEnum(DaxEnumName enumName){
        enumDictionary.putEnum(enumName.getName(), enumName.getDesc());
    }


    public Map<String, Map<String, DaxEnumValue>> getEnumValueMap() {
        return  enumDictionary.getValueMap();
    }

    public Map<String, DaxEnumName>  getEnumMap() {
        return  enumDictionary.getEnumMap();
    }


    //**********************************************************************
    // Groups

    public void putGroup(int idGroup, String grpName){
        DaxGroup grp =  new DaxGroup(idGroup,0,grpName);
        groupMap.put(grp.getId(),grp);

    }

    public Map<Integer, DaxpGroupItf> getGroupMap() {
        return groupMap;
    }

    //**********************************************************************
    // Attributes

    private void putAttribute(int contextId, int tagId, DaxPair<?> atrPair){

      DaxTag tag = new DaxTag(contextId, tagId);

      attributMap.merge(tag, new HashMap<>(Map.of(atrPair.getTag(), atrPair)),
                (eM, nM) ->
                        DaxTool.putAndReturn(eM, atrPair.getTag(), atrPair));

    }

    public void putAttribute(int tagId, DaxPair<?> atrPair){
        putAttribute(config.getApplicationContextId(), tagId, atrPair);
    }

    public void putAttribute(DaxTag tag, DaxPair<?> atrPair){
        putAttribute(tag.getContextId(), tag.getTagId(), atrPair);
    }

    //**********************************************************************
    // Dedicated attributes

    public Map<DaxTag, DaxPair<?>> getFieldAttributeMap(int tagId) {
        DaxTag tag = new DaxTag(config.getApplicationContextId(), tagId);
        return attributMap.get( tag);
    }

    public Map<DaxTag, Map<DaxTag, DaxPair<?>>> getAttributMap(){
        return attributMap;
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
