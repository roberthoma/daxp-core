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

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.context.*;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
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
// CRM:00234, DAX:23445, $:23455 , crm:33345

public class DaxRegister {
    private static final Logger logger = LoggerFactory.getLogger(DaxRegister.class);

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

    /*****************************************************
     * Entity tags, key is tag of entity
     */
    Map<DaxTag,  Set<DaxTag>> entityTagSet = new HashMap<>();


    Map<Integer, DaxCollectionRegister> collectionRegMap = new ConcurrentHashMap<>();
//    DaxEnumDictionary enumDictionary; //Application enumDic

    /*****************************************************
     * Dictionary of messages type, required and respond tags
     * Key: Message type
     * */

    Map<Integer, DaxMessageRegister> messageDicMap = new ConcurrentHashMap<>();;
    DaxMessageRegister msgMap;

    /*****************************************************
     *Map of tag attributes
     * Key : tagId
     * Value : map of attributes
     * */
    Map<DaxTag, Map<DaxTag, DaxPair<?>>> attributMap = new ConcurrentHashMap<>();


    //TODO
    //Add dedicated attributes for field used by Entity

    /*****************************************************
     *  Entity Map //
     */
    Map<DaxTag, Set<DaxTag>> entityFieldsMap = new ConcurrentHashMap<>();
    Map<DaxTag, DaxEntity> entityMap = new ConcurrentHashMap<>();


    /******************************************************/
    int appContextId;

    public DaxRegister(DaxConfig config,
            DaxContextMapper contextMapper ,
            DaxMessageMapper messageMapper ,
            DaxDataTypeCodec dataTypeCodec
    )
    {
        logger.info("Init DaxSchemaRegister...");
        appContextId = config.getAppContextId();
        this.config = config;

        this.contextMapper = contextMapper;
        this.messageMapper = messageMapper;

        collectionRegMap.put(appContextId, new DaxCollectionRegister(appContextId) );

        msgMap = new DaxMessageRegister(appContextId);
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

    public DaxCollectionRegister getEnumDictionary(int contextId){
        return collectionRegMap.get(contextId);
    }

    public void putEnum(DaxTag tag, DaxEnum daxEnum){
        collectionRegMap.get(tag.getContextId()).putEnum(tag, daxEnum);

    }


    public Map<DaxTag, DaxEnum>  getEnumMap(int contextId) {
        return collectionRegMap.get(contextId).getEnumMap();
    }

    public void putEnumValue(DaxTag tag, DaxEnumValue value){
        collectionRegMap.get(tag.getContextId()).putEnumValue (tag, value);
    }



    public Map<DaxTag, Map<String, DaxEnumValue>> getEnumValueMap(DaxTag tag) {
        return collectionRegMap.get(tag.getContextId()).getValueMap();
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


    public void putAttribute(DaxTag tag, DaxPair<?> atrPair){
        attributMap.merge(tag, new ConcurrentHashMap<>(Map.of(atrPair.getTag(), atrPair)),
                (eM, nM) ->
                        DaxCollectionTool.putAndReturnMap(eM, atrPair.getTag(), atrPair));

    }
//    public void putAttribute(DaxTag tag, DaxTag atrTag,  DaxValue<?> atrValue){
//        attributMap.merge(tag, new ConcurrentHashMap<>(Map.of(atrTag, atrPair)),
//                (eM, nM) ->
//                        DaxCollectionTool.putAndReturnMap(eM, atrTag, atrPair));
//
//    }

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

    public void putAtrDataType(DaxTag tag, Map<DaxTag, DaxPair<?>> pairMap){
        pairMap.forEach((atrTag, atrPair) ->
        putAttribute(tag, atrPair));

    };

//
//    public void putAtrDataType(DaxTag tag, DaxDataType dataType){
//        putAttribute(tag, new DaxAtrDataType(dataType));
//    };






    public void putAtrSizeMax(DaxTag tag,  Integer max){
        putAttribute(tag, new DaxAtrSizeMax(max));
    }


    public void putAtrSizeMin(DaxTag tag,  Integer min){
        putAttribute(tag, new DaxAtrSizeMin(min));
    }



    public void putAtrNullable(DaxTag tag,  Boolean able){
        putAttribute(tag, new DaxAtrNullable(able));
    }

    public void putAtrReadOnly(DaxTag tag, Boolean able) {
        putAttribute(tag,  new DaxArtReadOnly(able));
    }

    public void putAtrReadOnly(DaxTag tag, char able) {
        putAttribute(tag, new DaxArtReadOnly(able=='Y'? Boolean.TRUE:Boolean.FALSE));
    }

    public void putAtrEnumTypeTag(DaxTag tag, DaxTag enumTag) {
        putAttribute(tag, new DaxAtrEnumTag(enumTag));
    }



    public void putAtrFieldName(DaxTag tag, String name) {
        putAttribute(tag, new DaxArtFieldName(name));
    }




    public void putAtrEntityDataTypeId(DaxTag tag, DaxTag dataTypeTag) {
        putAttribute(tag, new DaxAtrEntityDataTypeId(dataTypeTag));
    }


    public void putAtrDeprecated(DaxTag tag) {
        putAttribute(tag, new DaxAtrDeprecated(true));
    }

    public void putTag(DaxTag tag){

        if (tagSet.contains(tag)){
            logger.warn("TAG {} EXIST in dictionary ", tag.getTagId());
            //throw new RuntimeException("Tag "+tag.getTagId()+" exist !!!");
        }
        tagSet.add(tag);

    }
    //------------------------------------
//    public Class<?> getAtrDataType(DaxTag tag){
//        if (attributMap.containsKey(tag) && attributMap.get(tag).containsKey(DaxCoreTags.DATA_TYPE)) {
//
//            return DaxDataType_OLD.fromCode(attributMap.get(tag).get(DaxCoreTags.DATA_TYPE).getStrValue());
//        }
//        return DaxDataType_OLD.UNKNOWN;
//
//    }




}
