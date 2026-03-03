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
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.field.*;
import org.daxprotocol.core.group.DaxGroup;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxSetTool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DaxDictionary {

    DaxpConfig config;
    DaxStringReferenceMapper contextMapper;

    Map<Integer, DaxContext> contextMap = new HashMap<>();

    DaxEnumDictionary enumDictionary = new DaxEnumDictionary();

    /*****************************************************
     * DescriptiveMap : it is main dic of tag attributes
     * Key : tagId
     * Value : map of attributes
     * */
    Map<DaxTag, Map<DaxTag, DaxPair<?>>> attributMap = new HashMap<>();


    /*****************************************************
     *  Group Map
     */
    Map<Integer, DaxGroup> groupMap = new HashMap<>();


    Map<Integer, Set<DaxTag>> groupFieldsMap = new HashMap<>();
    Set<DaxTag> tagSet = new HashSet<>();

    DaxMessageDic messageDic = new DaxMessageDic();


    public DaxDictionary(DaxpConfig config, DaxStringReferenceMapper contextMapper) {
        System.out.println("Init DaxDictionary...");
        this.config = config;
        this.contextMapper = contextMapper;
    }


    public Map<Integer, DaxContext> getContextMap() {
        return contextMap;
    }


    public void putContext(DaxContext context){
        contextMap.put(contextMapper.getReferenceId(context.getSymbol()),context);
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

    public void putEnum(DaxEnum enumName){
        enumDictionary.putEnum(enumName.getName(), enumName.getDesc());
    }


    public Map<String, Map<String, DaxEnumValue>> getEnumValueMap() {
        return  enumDictionary.getValueMap();
    }

    public Map<String, DaxEnum>  getEnumMap() {
        return  enumDictionary.getEnumMap();
    }


    //**********************************************************************
    // Groups

    public void putGroup(DaxGroup group){

        groupMap.put(group.getId(),group);

    }

    public Map<Integer, DaxGroup> getGroupMap() {
        return groupMap;
    }


    public Map<Integer, Set<DaxTag>> getGroupFieldsMap(){
        return groupFieldsMap;
    }


    public Set<DaxTag> getTagSet(){
        return tagSet;
    }

    //**********************************************************************
    // Attributes

    private void putAttribute(int contextId, int tagId, DaxPair<?> atrPair){

      DaxTag tag = new DaxTag(contextId, tagId);

      attributMap.merge(tag, new HashMap<>(Map.of(atrPair.getTag(), atrPair)),
                (eM, nM) ->
                        DaxSetTool.putAndReturnMap(eM, atrPair.getTag(), atrPair));

    }

    public void putAttribute(int tagId, DaxPair<?> atrPair){
        putAttribute(config.getAppContextId(), tagId, atrPair);
    }

    public void putAttribute(DaxTag tag, DaxPair<?> atrPair){
        putAttribute(tag.getContextId(), tag.getTagId(), atrPair);
    }

    //**********************************************************************
    // Dedicated attributes

    public Map<DaxTag, DaxPair<?>> getFieldAttributeMap(int tagId) {
        DaxTag tag = new DaxTag(config.getAppContextId(), tagId);
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
   /**
    *
    * */
   //TODO chek exist of fields in group
    public void putFieldIntoGroup(DaxTag tag, int groupId) {
        groupFieldsMap.merge(groupId,  new HashSet<>(Set.of(tag)),(daxTags, daxTags2) ->
                DaxSetTool.addAndReturnSet(daxTags, tag) );
    }


    public void putTag(DaxTag tag){

        if (tagSet.contains(tag)){
            System.out.println("TAG > "+tag + " ...........  EXIST ............ ");
            //throw new RuntimeException("Tag "+tag.getTagId()+" exist !!!");
        }
        tagSet.add(tag);

    }



}
