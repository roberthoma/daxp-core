/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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

package org.daxprotocol.core.registries;

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.namespace.*;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
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


/**********************************************************************************************
 *                  Registration Rules
 * 1) A tag destination must be a specific type (e.g., FIELDS, ENTITY, COLLECTION).

 * 2) A tag can be used multiple times within the same destination type.
 *    However, each unique tag can only be used once per entity, collection ...

 * 3) Each entry name used in an annotation must be a single word.

 * 4) All fields annotated with the same tag must be of the same data type.
 **********************************************************************************************/

public class DaxSemanticRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DaxSemanticRegistry.class);

    DaxConfig        config;
    DaxNamespaceMapper namespaceMapper;
    DaxMessageMapper messageMapper;
    DaxSchemaMapper  schemaMapper;

    /*****************************************************
     * Main SET of tags
     */
//    Set<DaxTag> tagSet = new HashSet<>();
    Map<DaxTag,DaxTagDestiny>     tagDestinyMap  = new ConcurrentHashMap<>();

    Map<DaxTag,DaxRegisterSource> tagMap         = new ConcurrentHashMap<>();

    DaxBaseRegistry<DaxTag> tagAttributes = new DaxBaseRegistry<>();



    /*****************************************************
     *  Map of namespace referenced by integer
     */
    //TODO refactor DaxNamespace to DaxBaseDictionary
    Map<Integer, DaxNamespace> namespaceMap = new ConcurrentHashMap<>();


    /*****************************************************
     *  Map of schema referenced by integer
     */
    DaxBaseRegistry<Integer> schemaDic = new DaxBaseRegistry<>();



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
    Map<DaxTag, DaxBaseRegistry<DaxTag>> entityEntryAttributes = new HashMap<>();


    /******************************************************/


    DaxBaseRegistry<DaxTag> collectionAttributes         = new DaxBaseRegistry<>();


    Map<DaxTag, DaxBaseRegistry<String>> collectionValues = new ConcurrentHashMap<>();





    /******************************************************/

    public DaxSemanticRegistry(DaxConfig config,
            DaxNamespaceMapper namespaceMapper ,
            DaxMessageMapper messageMapper ,
            DaxSchemaMapper schemaMapper
    )
    {
        logger.info("Init DaxDictionary ...");
        this.config = config;

        this.namespaceMapper = namespaceMapper;
        this.messageMapper = messageMapper;
        this.schemaMapper  = schemaMapper;


        msgMap = new DaxMessageRegister(config.getAppNamespaceId());
        messageDicMap.put(config.getAppNamespaceId(),msgMap);


    }

    //**********************************************************************
    // namespace

    public Map<Integer, DaxNamespace> getNamespaceMap() {
        return namespaceMap;
    }


    public void putNamespace(DaxNamespace namespace){
        namespaceMap.put(namespaceMapper.getReferenceId(namespace.getTagPrefix()),namespace);
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
     * Entity  registration
     */


    public Map<DaxTag, Set<DaxTag>> getEntityFieldsMap(){
        return entityFieldsMap;
    }


    public Map<DaxTag,DaxTagDestiny> getTagDestinyMap(){
        return tagDestinyMap;
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


    public void putTagAtrDataType(DaxTag tag, DaxDataType dataType) { putTagAttribute(tag, new DaxPairDataType(ATR_DATA_TYPE,dataType));}
    public void putTagAtrSizeMax(DaxTag tag,  Integer max )         { putTagAttribute(tag, new DaxPairInteger(ATR_SIZE_MAX,max));}
    public void putTagAtrSizeMin(DaxTag tag,  Integer min )         { putTagAttribute(tag, new DaxPairInteger(ATR_SIZE_MIN,min));}
    public void putTagAtrNullable(DaxTag tag,  Boolean able)        { putTagAttribute(tag, new DaxPairBoolean(ATR_NULLABLE ,able));}
    public void putTagAtrName(DaxTag tag,  String name)             { if(name!= null && !name.isBlank()){ putTagAttribute(tag, new DaxPairString(ENTRY_NAME,name));}}
    public void putTagAtrDescription(DaxTag tag, String desc)       { if(desc!= null && !desc.isBlank()){ putTagAttribute(tag, new DaxPairString(ENTRY_DESCRIPTION,desc));}}
    public void putTagAtrReadOnly(DaxTag tag, Boolean able)         { putTagAttribute(tag, new DaxPairBoolean(ATR_READONLY ,able));}
    public void putTagAtrDeprecated(DaxTag tag)                     { putTagAttribute(tag, new DaxPairBoolean(ATR_IS_DEPRECATED,true));}


    //*********************
    // Attributes of fields and values derived from the entity.
    private void putEntityEntryAttribute(DaxTag entityTag, DaxTag tag, DaxPair<?> atrPair){

        if(!entityEntryAttributes.containsKey(entityTag)){
            entityEntryAttributes.put(entityTag, new DaxBaseRegistry<>());
        }
        DaxBaseRegistry<DaxTag> dic = entityEntryAttributes.get(entityTag);

        dic.putAttribute(tag, atrPair);

    }
    public void putEntityTagAttributes(DaxTag entityTag, DaxTag tag, Set< DaxPair<?>> pairMap) {
        pairMap.forEach(( atrPair) -> putEntityEntryAttribute(entityTag,tag,atrPair));
    };

    public void putEntityEntryAtrDataType(DaxTag entityTag, DaxTag tag, DaxDataType dataType) { putEntityEntryAttribute(entityTag, tag, new DaxPairDataType(ATR_DATA_TYPE,dataType));}
    public void putEntityEntryAtrSizeMax(DaxTag entityTag, DaxTag tag,  Integer max )         { putEntityEntryAttribute(entityTag, tag, new DaxPairInteger(ATR_SIZE_MAX,max));}
    public void putEntityEntryAtrSizeMin(DaxTag entityTag, DaxTag tag,  Integer min )         { putEntityEntryAttribute(entityTag, tag, new DaxPairInteger(ATR_SIZE_MIN,min));}
    public void putEntityEntryAtrNullable(DaxTag entityTag, DaxTag tag,  Boolean able)        { putEntityEntryAttribute(entityTag, tag, new DaxPairBoolean(ATR_NULLABLE ,able));}
    public void putEntityEntryAtrName(DaxTag entityTag, DaxTag tag,  String name)             { if(name!= null && !name.isBlank()){ putEntityEntryAttribute(entityTag, tag, new DaxPairString(ENTRY_NAME,name));}}
    public void putEntityEntryAtrDescription(DaxTag entityTag, DaxTag tag, String desc)       { if(desc!= null && !desc.isBlank()){ putEntityEntryAttribute(entityTag, tag, new DaxPairString(ENTRY_DESCRIPTION,desc));}}
    public void putEntityEntryAtrReadOnly(DaxTag entityTag, DaxTag tag, Boolean able)         { putEntityEntryAttribute(entityTag, tag, new DaxPairBoolean(ATR_READONLY ,able));}
    public void putEntityEntryAtrDeprecated(DaxTag entityTag, DaxTag tag)                     { putEntityEntryAttribute(entityTag, tag, new DaxPairBoolean(ATR_IS_DEPRECATED,true));}



    public Map<DaxTag, DaxBaseRegistry<DaxTag>> getEntityEntryAttributes(){
        return entityEntryAttributes;
    }

    public DaxBaseRegistry<DaxTag> getEntityBaseDic(DaxTag entityTag){
        return entityEntryAttributes.get(entityTag);
    }


    public Map<DaxTag, Map<DaxTag, DaxPair<?>>> getTagAttributeMap(){
        return tagAttributes.getAttributMap();
    }

    /*******************************************************************************
     * Collection  registration
     */
    public void putCollectionType(DaxTag tag, DaxTag typeTag) {
        tagAttributes.putAttribute(tag, new DaxPairTag(COLLECTION_ID,typeTag));
    }

    //TODO getters and setter for other namespace enumDic;

    public void putCollectionAttributes(DaxTag colTag, DaxPair<?> atrPair ){
        collectionAttributes.putAttribute(colTag, atrPair);
    }

    public void putCollectionAttributes(DaxTag tag, Set< DaxPair<?>> pairMap) {
        pairMap.forEach(( atrPair) -> putCollectionAttributes(tag,atrPair));
    };


    public void putCollectionAtrValueDataType(DaxTag colTag,  DaxDataType dataType) { putCollectionAttributes(colTag, new DaxPairDataType(ATR_DATA_TYPE,dataType));}
    public void putCollectionAtrKeyDataType  (DaxTag colTag,  DaxDataType dataType) { putCollectionAttributes(colTag, new DaxPairDataType(ATR_DATA_TYPE,dataType));}
    //    public void putCollectionAtrSizeMax(DaxTag colTag,   Integer max )         { putCollectionAttributes(colTag, new DaxPairInteger(ATR_SIZE_MAX,max));}
//    public void putCollectionAtrSizeMin(DaxTag colTag,   Integer min )         { putCollectionAttributes(colTag,  new DaxPairInteger(ATR_SIZE_MIN,min));}
//    public void putCollectionAtrNullable(DaxTag colTag,   Boolean able)        { putCollectionAttributes(colTag,  new DaxPairBoolean(ATR_NULLABLE ,able));}
    public void putCollectionAtrName(DaxTag colTag,   String name)             { if(name!= null && !name.isBlank()){ putCollectionAttributes(colTag,  new DaxPairString(ENTRY_NAME,name));}}
    public void putCollectionAtrDescription(DaxTag colTag,  String desc)       { if(desc!= null && !desc.isBlank()){ putCollectionAttributes(colTag,  new DaxPairString(ENTRY_DESCRIPTION,desc));}}
    //    public void putCollectionAtrReadOnly  (DaxTag colTag,  Boolean able)       { putCollectionAttributes(colTag, tag, new DaxPairBoolean(ATR_READONLY ,able));}
    public void putCollectionAtrDeprecated(DaxTag colTag)                      { putCollectionAttributes(colTag,  new DaxPairBoolean(ATR_IS_DEPRECATED,true));}


    public DaxBaseRegistry<DaxTag> getCollectionAttributes() {
        return collectionAttributes;
    }


    public void putCollectionValue(DaxTag colTag, String key ,DaxPair<?> atrPair){


        collectionValues.merge(colTag,
                DaxCollectionTool.putAndReturnMap(
                new DaxBaseRegistry<>(),key,atrPair),
                        (eM, nM) ->
                                DaxCollectionTool.putAndReturnMap(eM, key,atrPair));


    }


    public DaxBaseRegistry<String> getCollectionValues(DaxTag colTag) {
        return collectionValues.get(colTag);
    }

    /*********************
     * Tag registration
    */

    public void putTag(DaxTag tag, DaxRegisterSource source, DaxTagDestiny destiny){

        if (tagDestinyMap.containsKey(tag))
        {
            if( tagDestinyMap.get(tag) == DaxTagDestiny.TAG){
               tagDestinyMap.put(tag,destiny);
            }

            //TODO Refactor controling
//            if(tagDestinyMap.get(tag) != destiny )
//            {
//                throw new DaxTagParserException("Bad tag destination, tag is use as : " + tagDestinyMap.get(tag).toString()
//                        );
//            }
        }
        else {
           tagDestinyMap.put(tag,destiny);
        }


        if (tagMap.containsKey(tag)){
            logger.warn("TAG {} EXIST in dictionary , source {}  ", tag.getTagId(), tagMap.get(tag));
            //throw new RuntimeException("Tag "+tag.getTagId()+" exist !!!");
            return;
        }

        tagMap.put(tag,source);


    }

    //------------------------
    public void putSchema(String symbol,String name, String description){
        int refId = schemaMapper.getReferenceId(name);
        schemaDic.putAttribute(refId,new DaxPairString(ENTRY_SYMBOL,symbol));
        schemaDic.putAttribute(refId,new DaxPairString(ENTRY_NAME,name));
        schemaDic.putAttribute(refId,new DaxPairString(ENTRY_DESCRIPTION,description));
    }

    public DaxBaseRegistry<Integer> getSchemaDictionary (){
        return schemaDic;
    }
    ///--------------------------------------
    /// TMP. move to service
    public boolean isPrimitiveType(DaxTag tag){
        boolean isPrimitiveType = false;
        try {

        System.out.println("DATA MODEL:"+tag.getTagId());
        var atrMap = tagAttributes.getAttributMap()
                 .get(tag);
        if (atrMap.containsKey(ATR_DATA_TYPE))
        {
            isPrimitiveType = atrMap.get(ATR_DATA_TYPE).getDataTypeValue().isPrimitiveType();
            return isPrimitiveType;
        }

        if (atrMap.containsKey(ATR_REF_DATA_TYPE))
        {
            var refTag= atrMap.get(ATR_REF_DATA_TYPE).getValue();

            var atrRefMap = tagAttributes.getAttributMap()
                    .get(refTag);



            return isPrimitiveType;
        }


             System.out.println("DATA MODEL  is primitive="+ isPrimitiveType);
            return isPrimitiveType;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


}
