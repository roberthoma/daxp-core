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

package org.daxprotocol.core.schema;

import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.mapper.DaxMessageMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.attributes.*;
import org.daxprotocol.core.entity.DaxEntity;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxCollectionTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


//TODO
// dictionary od exception by context
// CRM-00234, DAX-23445, $:23455 , crm:33345

public class DaxSchemaRegister {
    private static final Logger logger = LoggerFactory.getLogger(DaxSchemaRegister.class);

    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxMessageMapper messageMapper;

    /*****************************************************
     *  Map of context referenced by integer
     */
    Map<Integer, DaxContext> contextMap = new HashMap<>();


    /*****************************************************
     * Main SET of tags
     */
    Set<DaxTag> tagSet = new HashSet<>();



    Map<Integer, DaxEnumDictionary> enumDictionaryMap = new ConcurrentHashMap<>();
//    DaxEnumDictionary enumDictionary; //Application enumDic

    /*****************************************************
     * Dictionary of messages type, required and respond tags
     * Key: Message type
     * */

    Map<Integer, DaxMessageDictionary> messageDicMap = new ConcurrentHashMap<>();;
    DaxMessageDictionary msgMap;

    /*****************************************************
     *Map of tag attributes
     * Key : tagId
     * Value : map of attributes
     * */
    Map<DaxTag, Map<DaxTag, DaxPair<?>>> attributMap = new ConcurrentHashMap<>();


    /*****************************************************
     *  Entity Map //
     */
    Map<DaxTag, Set<DaxTag>> entityFieldsMap = new ConcurrentHashMap<>();
    Map<DaxTag, DaxEntity> entityMap = new ConcurrentHashMap<>();


    /******************************************************/
    int appContextId;

    public DaxSchemaRegister(DaxConfig config,
            DaxContextMapper contextMapper ,
            DaxMessageMapper messageMapper
    )
    {
        logger.info("Init DaxSchemaRegister...");
        appContextId = config.getAppContextId();
        this.config = config;

        this.contextMapper = contextMapper;
        this.messageMapper = messageMapper;

        enumDictionaryMap.put(appContextId, new DaxEnumDictionary(appContextId) );

        msgMap = new DaxMessageDictionary(appContextId);
        messageDicMap.put(appContextId,msgMap);


    }

    //**********************************************************************
    // Context

    public Map<Integer, DaxContext> getContextMap() {
        return contextMap;
    }


    public void putContext(DaxContext context){
        contextMap.put(contextMapper.getReferenceId(context.getTagPrefix()),context);
//        contextMap.put(contextMapper.getReferenceId(context.getSymbol()),context);
    }


    //**********************************************************************
    // Messages
    public void putMsgItem(DaxMessageItem messageDicItem){
        msgMap.putMsgItem(messageDicItem);
    }

    public Map<String, DaxMessageItem> getMsgMap() {
        return msgMap.getMsgMap();
    }


    //**********************************************************************
    // Enums

    public DaxEnumDictionary getEnumDictionary(int contextId){
        return enumDictionaryMap.get(contextId);
    }

    public void putEnum(DaxTag tag, DaxEnum daxEnum){
        enumDictionaryMap.get(tag.getContextId()).putEnum(tag, daxEnum);

    }


    public Map<DaxTag, DaxEnum>  getEnumMap(int contextId) {
        return enumDictionaryMap.get(contextId).getEnumMap();
    }

    public void putEnumValue(DaxTag tag, DaxEnumValue value){
        enumDictionaryMap.get(tag.getContextId()).putEnumValue (tag, value);
    }



    public Map<DaxTag, Map<String, DaxEnumValue>> getEnumValueMap(DaxTag tag) {
        return enumDictionaryMap.get(tag.getContextId()).getValueMap();
    }


    //TODO getters and setter for other context enumDic;

    //**********************************************************************
    // Groups

    public void putEntity(DaxEntity entity){

        entityMap.put(entity.getTag(),entity);

    }

    public Map<DaxTag, DaxEntity> getEntityMap() {
        return entityMap;
    }


    public Map<DaxTag, Set<DaxTag>> getEntityFieldsMap(){
        return entityFieldsMap;
    }


    public Set<DaxTag> getTagSet(){
        return tagSet;
    }

    //TODO chek exist of fields in group,
    //TODO check recursions
    public void putEntityField(DaxTag entityTag, DaxTag tag) {
        entityFieldsMap.merge(entityTag,  new HashSet<>(Set.of(tag)),(daxTags, daxTags2) ->
                DaxCollectionTool.addAndReturnSet(daxTags, tag) );
    }

    //**********************************************************************
    // Attributes

    private void putAttribute(int contextId, int tagId, DaxPair<?> atrPair){

      DaxTag tag = DaxTag.of (contextId, tagId);

      attributMap.merge(tag, new ConcurrentHashMap<>(Map.of(atrPair.getTag(), atrPair)),
                (eM, nM) ->
                        DaxCollectionTool.putAndReturnMap(eM, atrPair.getTag(), atrPair));

    }


    public void putAttribute(DaxTag tag, DaxPair<?> atrPair){
        putAttribute(tag.getContextId(), tag.getTagId(), atrPair);
    }

    //**********************************************************************
    // Dedicated attributes

    public Map<DaxTag, DaxPair<?>> getFieldAttributeMap(int tagId) {
        DaxTag tag = DaxTag.of(config.getAppContextId(), tagId);
        return attributMap.get( tag);
    }

    public Map<DaxTag, Map<DaxTag, DaxPair<?>>> getAttributMap(){
        return attributMap;
    }


//    public void put(int tagId,  Class<?> clazz){
//        putAttribute(tagId, new DaxAtrDataType(clazz));
//    };

    public void putAtrDataType(DaxTag tag,  Class<?> clazz){
        putAttribute(tag, new DaxAtrDataType(clazz));
    };


    public void putAtrDataType(DaxTag tag,  String c){
        putAttribute(tag, new DaxAtrDataType(c));
    };

//    public void putAtrDataType(DaxTag tag,  DaxAtrDataType dataType){
//        putAttribute(tag, dataType);
//    };



    public void putAtrSizeMax(DaxTag tag,  Integer max){
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrSizeMax(max));
    }


    public void putAtrSizeMin(DaxTag tag,  Integer min){
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrSizeMin(min));
    }



    public void putAtrNullable(DaxTag tag,  Boolean able){
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrNullable(able));
    }

    public void putAtrReadOnly(DaxTag tag, Boolean able) {
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxArtReadOnly(able));
    }

    public void putAtrReadOnly(DaxTag tag, char able) {
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxArtReadOnly(able=='Y'? Boolean.TRUE:
                Boolean.FALSE));
    }

    public void putAtrEnumTypeTag(DaxTag tag, DaxTag enumTag) {
        putAttribute(tag, new DaxAtrEnumTag(enumTag));
    }


    public void putAtrEntityDataTypeId(DaxTag tag, DaxTag dataTypeTag) {
        putAttribute(tag, new DaxAtrEntityDataTypeId(dataTypeTag));
    }

    // put DaxAtrDeprecated

    public void putTag(DaxTag tag){

        if (tagSet.contains(tag)){
            logger.warn("TAG {} EXIST in dictionary ", tag.getTagId());
            //throw new RuntimeException("Tag "+tag.getTagId()+" exist !!!");
        }
        tagSet.add(tag);

    }
    //------------------------------------
    public DaxDataType getAtrDataType(DaxTag tag){
        if (attributMap.containsKey(tag) && attributMap.get(tag).containsKey(DaxCoreTags.DATA_TYPE)) {

            return DaxDataType.fromCode(attributMap.get(tag).get(DaxCoreTags.DATA_TYPE).getStrValue());
        }
        return DaxDataType.UNKNOWN;

    }




}
