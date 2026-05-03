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

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.context.*;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.exceptions.DaxTagParserException;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.mapper.DaxMessageMapper;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.entity.DaxEntity;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.tag.DaxTagDestiny;
import org.daxprotocol.core.tool.DaxCollectionTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import static org.daxprotocol.core.application.DaxCoreTags.*;


/******************************************************
 Registration Rules
 A tag Destination must be a specific type (e.g., FIELDS, ENTITY).
 A tag can be used multiple times within the same destination type.
 However, each unique tag can only be used once per entity.
 */


public class DaxDictionary {
    private static final Logger logger = LoggerFactory.getLogger(DaxDictionary.class);

    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxMessageMapper messageMapper;


    /*****************************************************
     * Main SET of tags
     */
//    Set<DaxTag> tagSet = new HashSet<>();
    Map<DaxTag,DaxRegisterSource> tagMap = new ConcurrentHashMap<>();
    Map<DaxTag,DaxTagDestiny>     tagDestinyMap  = new ConcurrentHashMap<>();

    /*****************************************************
     *  Map of context referenced by integer
     */
    Map<Integer, DaxContext> contextMap = new ConcurrentHashMap<>();


    Map<Integer, DaxCollectionRegister> collectionRegMap = new ConcurrentHashMap<>();
//    DaxEnumDictionary enumDictionary; //Application enumDic

    /*****************************************************
     * Dictionary of messages type, required and respond tags
     * Key: Message type
     * */

    Map<Integer, DaxMessageRegister> messageDicMap = new ConcurrentHashMap<>();
    DaxMessageRegister msgMap;

    /*****************************************************
     * Map of tag attributes
     * Key : tagId
     * Value : map of attributes
     * */
     Map<DaxTag, Map<DaxTag, DaxPair<?>>> attributMap = new ConcurrentHashMap<>();
//    Map<DaxTag, Map<DaxTag, DaxValue<?>>> attributMap = new ConcurrentHashMap<>();


    //TODO
    //Add dedicated attributes for field used by Entity

    /*****************************************************
     *  Entity Map
     */
    Map<DaxTag, Set<DaxTag>> entityFieldsMap = new ConcurrentHashMap<>();
    Map<DaxTag, DaxEntity>   entityMap = new ConcurrentHashMap<>();

//    Map <DaxTag, > ???? Atrybuty Fields dla encji .

    /******************************************************/
    int appContextId;

    public DaxDictionary(DaxConfig config,
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
    }


    //**********************************************************************
    // Messages
    public void putMsgItem(DaxMessageItem messageDicItem){
        msgMap.putMsgItem(messageDicItem);
    }

    public Map<String, DaxMessageItem> getMsgMap() {
        return msgMap.getMsgMap();
    }


    /*******************************************************************************
     * Collection  registration
     */
    public void putCollectionType(DaxTag tag, DaxTag typeTag) {
        putAttribute(tag, new DaxPairTag(COLLECTION_ID,typeTag));
    }


    public DaxCollectionRegister getEnumDictionary(int contextId){
        return collectionRegMap.get(contextId);
    }

    public void putEnum(DaxTag tag, DaxCollection_TMP daxCollectionTMP){
        collectionRegMap.get(tag.getContextId()).putEnum(tag, daxCollectionTMP);

    }


    public Map<DaxTag, DaxCollection_TMP>  getEnumMap(int contextId) {
        return collectionRegMap.get(contextId).getEnumMap();
    }

    public void putEnumValue(DaxTag tag, DaxEnumValue value){
        collectionRegMap.get(tag.getContextId()).putEnumValue (tag, value);
    }



    public Map<DaxTag, Map<String, DaxEnumValue>> getEnumValueMap(DaxTag tag) {
        return collectionRegMap.get(tag.getContextId()).getValueMap();
    }


    //TODO getters and setter for other context enumDic;

    /*******************************************************************************
     * Entity  registration
     */

    public void putEntity(DaxEntity entity){

        entityMap.put(entity.getTag(),entity);

    }

    public Map<DaxTag, DaxEntity> getEntityMap() {
        return entityMap;
    }


    public Map<DaxTag, Set<DaxTag>> getEntityFieldsMap(){
        return entityFieldsMap;
    }


    public Map<DaxTag,DaxRegisterSource> getTagSet(){
        return tagMap;
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

    //**********************************************************************
    // Dedicated attributes

//    public Map<DaxTag, DaxValue<?>> getFieldAttributeMap(DaxTag tagId) {
//        DaxTag tag = DaxTag.of(config.getAppContextId(), tagId);
//        return attributMap.get( tag);
//    }

    public Map<DaxTag, Map<DaxTag, DaxPair<?>>> getAttributMap(){
        return attributMap;
    }


//    public void put(int tagId,  Class<?> clazz){
//        putAttribute(tagId, new DaxAtrDataType(clazz));
//    };

    public void putAtrDataType(DaxTag tag, Set< DaxPair<?>> pairMap){
        pairMap.forEach(( atrPair) ->
        putAttribute(tag,atrPair));

    };


    public void putAtrDataType(DaxTag tag, DaxDataType dataType){
        putAttribute(tag, new DaxPairDataType(ATR_DATA_TYPE,dataType));
    };






    public void putAtrSizeMax(DaxTag tag,  Integer max){
        putAttribute(tag,  new DaxPairInteger(ATR_SIZE_MAX,max));
    }


    public void putAtrSizeMin(DaxTag tag,  Integer min){
        putAttribute(tag,  new DaxPairInteger(ATR_SIZE_MIN,min));
    }



    public void putAtrNullable(DaxTag tag,  Boolean able){
        putAttribute(tag, new DaxPairBoolean(ATR_NULLABLE ,able));
    }

    public void putAtrReadOnly(DaxTag tag, Boolean able) {
        putAttribute(tag, new DaxPairBoolean(ATR_READONLY ,able));
    }

//    public void putAtrReadOnly(DaxTag tag, char able) {
//        putAttribute(tag, new DaxArtReadOnly(able=='Y'? Boolean.TRUE:Boolean.FALSE));
//    }

//    public void putAtrEnumTypeTag(DaxTag tag, DaxTag enumTag) {
//        putAttribute(tag, new DaxAtrEnumTag(enumTag));
//    }


    public void putAtrFieldName(DaxTag tag, String name) {
        putAttribute(tag, new DaxPairString(ENTRY_NAME,name));
    }



    public void putAtrDeprecated(DaxTag tag) {
        putAttribute(tag, new DaxPairBoolean(ATR_IS_DEPRECATED,true));
    }


    /*********************
     * Tag registration
    */
    public void putTag(DaxTag tag, DaxRegisterSource source){

        if (tagMap.containsKey(tag)){
            logger.warn("TAG {} EXIST in dictionary , source {}  ", tag.getTagId(), tagMap.get(tag));
            //throw new RuntimeException("Tag "+tag.getTagId()+" exist !!!");
            return;
        }
        tagMap.put(tag,source);

    }
    public void putTagDestiny(DaxTag tag, DaxTagDestiny destiny){
        if (tagDestinyMap.containsKey(tag))
        {
           if(tagDestinyMap.get(tag) != destiny)
           {
              throw new DaxTagParserException("Bad tag destination, tag is use as : " + tagDestinyMap.get(tag).toString());
           }
           return;
        }
        tagDestinyMap.put(tag,destiny);
    }

    public void putTag(DaxTag tag, DaxRegisterSource source, DaxTagDestiny destiny){
        putTag(tag, source);
        putTagDestiny(tag,destiny); // Check

    }


}
