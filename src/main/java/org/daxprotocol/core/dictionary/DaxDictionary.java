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
import org.daxprotocol.core.exceptions.DaxTagParserException;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.mapper.DaxMessageMapper;
import org.daxprotocol.core.mapper.DaxSchemaMapper;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.tag.DaxTagDestiny;
import org.daxprotocol.core.tool.DaxCollectionTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import static org.daxprotocol.core.application.DaxCoreTags.*;


/******************************************************
 Registration Rules
 * 1) A tag Destination must be a specific type (e.g., FIELDS, ENTITY).

 * 2) A tag can be used multiple times within the same destination type.
 * However, each unique tag can only be used once per entity.

 * 3) Each entry name used in Annotation must be single world
 */


public class DaxDictionary {
    private static final Logger logger = LoggerFactory.getLogger(DaxDictionary.class);

    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxMessageMapper messageMapper;
    DaxSchemaMapper  schemaMapper;

    /*****************************************************
     * Main SET of tags
     */
//    Set<DaxTag> tagSet = new HashSet<>();
    Map<DaxTag,DaxRegisterSource> tagMap         = new ConcurrentHashMap<>();
    Map<DaxTag,DaxTagDestiny>     tagDestinyMap  = new ConcurrentHashMap<>();

    DaxBaseDictionary<DaxTag> tagAttributes = new DaxBaseDictionary<>();




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
     *  Entity Map
     */
    Map<DaxTag, Set<DaxTag>> entityFieldsMap = new ConcurrentHashMap<>();

    /*****************************************************
     * Map of entity fields attributes
     * Key : tagId
     * Value : map of attributes
     * */
    Map<DaxTag,DaxBaseDictionary<DaxTag>> entityEntryAttributes = new HashMap<>();

    /******************************************************/

    public DaxDictionary(DaxConfig config,
            DaxContextMapper contextMapper ,
            DaxMessageMapper messageMapper ,
            DaxSchemaMapper schemaMapper
    )
    {
        logger.info("Init DaxDictionary ...");
        this.config = config;

        this.contextMapper = contextMapper;
        this.messageMapper = messageMapper;
        this.schemaMapper  = schemaMapper;

        collectionRegMap.put(config.getAppContextId(), new DaxCollectionRegister(config.getAppContextId()) );

        msgMap = new DaxMessageRegister(config.getAppContextId());
        messageDicMap.put(config.getAppContextId(),msgMap);


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
        tagAttributes.putAttribute(tag, new DaxPairTag(COLLECTION_ID,typeTag));
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

//    public void putAtrEnumTypeTag(DaxTag tag, DaxTag enumTag) {
//        putAttribute(tag, new DaxAtrEnumTag(enumTag));
//    }


    public Map<DaxTag, Map<String, DaxEnumValue>> getEnumValueMap(DaxTag tag) {
        return collectionRegMap.get(tag.getContextId()).getValueMap();
    }


    //TODO getters and setter for other context enumDic;

    /*******************************************************************************
     * Entity  registration
     */


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

    //*********************
    // Attributes of tags



    private void putTagAttribute(DaxTag tag, DaxPair<?> atrPair){
        tagAttributes.putAttribute(tag, atrPair);
    }

    public void putTagAttributes(DaxTag tag, Set< DaxPair<?>> pairMap) {
        pairMap.forEach(( atrPair) -> putTagAttribute(tag,atrPair));
    };


    public void putAtrDataType  (DaxTag tag, DaxDataType dataType) { putTagAttribute(tag, new DaxPairDataType(ATR_DATA_TYPE,dataType));}
    public void putAtrSizeMax   (DaxTag tag,  Integer max )        { putTagAttribute(tag, new DaxPairInteger(ATR_SIZE_MAX,max));}
    public void putAtrSizeMin   (DaxTag tag,  Integer min )        { putTagAttribute(tag, new DaxPairInteger(ATR_SIZE_MIN,min));}
    public void putAtrNullable  (DaxTag tag,  Boolean able)        { putTagAttribute(tag, new DaxPairBoolean(ATR_NULLABLE ,able));}
    public void putAtrEntryName (DaxTag tag,  String name)         { if(name!= null && !name.isBlank()){ putTagAttribute(tag, new DaxPairString(ENTRY_NAME,name));}}
    public void putAtrDescription(DaxTag tag, String desc)         { if(desc!= null && !desc.isBlank()){ putTagAttribute(tag, new DaxPairString(ENTRY_DESCRIPTION,desc));}}
    public void putAtrReadOnly  (DaxTag tag, Boolean able)         { putTagAttribute(tag, new DaxPairBoolean(ATR_READONLY ,able));}
    public void putAtrDeprecated(DaxTag tag)                       { putTagAttribute(tag, new DaxPairBoolean(ATR_IS_DEPRECATED,true));}


    //*********************
    // Attributes of fields and values derived from the entity.
    private void putEntityEntryAttribute(DaxTag entityTag, DaxTag tag, DaxPair<?> atrPair){

        if(!entityEntryAttributes.containsKey(entityTag)){
            entityEntryAttributes.put(entityTag, new DaxBaseDictionary<>());
        }
        DaxBaseDictionary<DaxTag> dic = entityEntryAttributes.get(entityTag);

        dic.putAttribute(tag, atrPair);

    }
    public void putAtrDataType  (DaxTag entityTag, DaxTag tag, DaxDataType dataType) { putTagAttribute(tag, new DaxPairDataType(ATR_DATA_TYPE,dataType));}
    public void putAtrSizeMax   (DaxTag entityTag, DaxTag tag,  Integer max )        { putTagAttribute(tag, new DaxPairInteger(ATR_SIZE_MAX,max));}
    public void putAtrSizeMin   (DaxTag entityTag, DaxTag tag,  Integer min )        { putTagAttribute(tag, new DaxPairInteger(ATR_SIZE_MIN,min));}
    public void putAtrNullable  (DaxTag entityTag, DaxTag tag,  Boolean able)        { putTagAttribute(tag, new DaxPairBoolean(ATR_NULLABLE ,able));}
    public void putAtrEntryName (DaxTag entityTag, DaxTag tag,  String name)         { if(name!= null && !name.isBlank()){ putEntityEntryAttribute(entityTag, tag, new DaxPairString(ENTRY_NAME,name));}}
    public void putAtrDescription(DaxTag entityTag, DaxTag tag, String desc)         { if(desc!= null && !desc.isBlank()){ putEntityEntryAttribute(entityTag, tag, new DaxPairString(ENTRY_DESCRIPTION,desc));}}
    public void putAtrReadOnly  (DaxTag entityTag, DaxTag tag, Boolean able)         { putTagAttribute(tag, new DaxPairBoolean(ATR_READONLY ,able));}
    public void putAtrDeprecated(DaxTag entityTag, DaxTag tag)                       { putTagAttribute(tag, new DaxPairBoolean(ATR_IS_DEPRECATED,true));}



    public Map<DaxTag,DaxBaseDictionary<DaxTag>> getEntityEntryAttributes(){
        return entityEntryAttributes;
    }


    public Map<DaxTag, Map<DaxTag, DaxPair<?>>> getTagAttributeMap(){
        return tagAttributes.getAttributMap();
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
