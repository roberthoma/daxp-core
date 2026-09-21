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

import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.namespace.*;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.mapper.DaxMessageMapper;
import org.daxprotocol.core.mapper.DaxSchemaMapper;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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

    DaxConfig           config;
    DaxNamespaceMapper  namespaceMapper;
    DaxMessageMapper    messageMapper;
    DaxSchemaMapper     schemaMapper;

    public DaxBaseRegistry<DaxTag> getTagAttributes() {
        return tagAttributes;
    }

    /*****************************************************
     * Main SET of tags
     */

    DaxBaseRegistry<DaxTag> tagAttributes = new DaxBaseRegistry<>();



    /*****************************************************
     *  Map of namespace referenced by integer
     */
    //TODO refactor DaxNamespace to DaxBaseDictionary
    Map<Integer, DaxNamespace> namespaceMap = new ConcurrentHashMap<>();


    /*****************************************************
     *  Map of schema referenced by integer
     */
    DaxBaseRegistry<Integer> namespaceRegistry = new DaxBaseRegistry<>();



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


    public Set<DaxTag> getEntityFields(DaxTag tag) {
        return entityFieldsMap.getOrDefault(tag, Collections.emptySet());
    }



    //TODO chek exist of fields in group,
    //TODO check recursions
    public void putEntityEntry(DaxTag entityTag, DaxTag tag) {
        entityFieldsMap.merge(entityTag,  new HashSet<>(Set.of(tag)),(daxTags, daxTags2) ->
                DaxLangTool.addAndReturnSet(daxTags, tag) );
    }

    //**********************************************************************



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


    public void putCollectionValue(DaxTag colTag, String key ,DaxPair<?> atrPair){


        collectionValues.merge(colTag,
                DaxLangTool.putAndReturnMap(
                new DaxBaseRegistry<>(),key,atrPair),
                        (eM, nM) ->
                                DaxLangTool.putAndReturnMap(eM, key,atrPair));


    }


    public DaxBaseRegistry<String> getCollectionValues(DaxTag colTag) {
        return collectionValues.get(colTag);
    }


    //------------------------
    public void putNamespace(String symbol,String name, String description){
        int refId = schemaMapper.getReferenceId(name);
        namespaceRegistry.putAttribute(refId,new DaxPairString(ENTRY_SYMBOL,symbol));
        namespaceRegistry.putAttribute(refId,new DaxPairString(ENTRY_NAME,name));
        namespaceRegistry.putAttribute(refId,new DaxPairString(ENTRY_DESCRIPTION,description));
    }
    ///---------------------------------------------------------------------------------------

    public DaxBaseRegistry<Integer> getSchemaDictionary (){
        return namespaceRegistry;
    }

    ///---------------------------------------------------------------------------------------
    /// TMP. move to DaxDataTypeService
    public boolean isPrimitiveType(DaxTag tag){
        boolean isPrimitiveType = false;
        try {

        var atrMap = tagAttributes.getAttributMap().get(tag);

        if (atrMap.containsKey(ATR_DATA_TYPE))
        {
            return atrMap.get(ATR_DATA_TYPE).getDataTypeValue().isPrimitiveType();
        }

        if (atrMap.containsKey(ATR_REF_TAG_ID))
        {
            var refTag= atrMap.get(ATR_REF_TAG_ID).getValue();

            var atrRefMap = tagAttributes.getAttributMap()
                    .get(refTag);



            return isPrimitiveType;
        }

            return isPrimitiveType;
        } catch (Exception e) {
            System.out.println("ROHO>"+e.getMessage());
        }
        return false;
    }
    ///---------------------------------------------------------------------------------------

    public List<DaxTag> getTagListByDataType(DaxDataType targetType) {
        return tagAttributes.getAttributMap().entrySet().stream()
                .filter(entry -> {
                    DaxPair<?> typePair = entry.getValue().get(DaxCoreTags.ATR_DATA_TYPE);
                    return typePair != null && typePair.getDataTypeValue() == targetType;
                })
                .map(Map.Entry::getKey)
                .toList();
    }

//    public List<DaxTag> getTagsByDataType(DaxDataType targetType) {
//        return tagAttributes.getKeysByDataType(targetType);
//    }
    ///---------------------------------------------------------------------------------------


    public boolean isTagRegistered(DaxTag tag) {
        return tagAttributes.containsKey(tag);
    }
    ///---------------------------------------------------------------------------------------

    public DaxDataType getDataType(DaxTag tag) {
        var attributes = tagAttributes.getAttributMap().get(tag);

        if (attributes == null) {
            return DaxDataType.NONE;
        }

        var attribute = attributes.get(ATR_DATA_TYPE);

        if (attribute == null || attribute.getDataTypeValue() == null) {
            return DaxDataType.NONE;
        }

        return attribute.getDataTypeValue();
    }

    ///---------------------------------------------------------------------------------------
    public boolean isCollectionDictionary(DaxTag tag){
        if(tagAttributes.containsKey(tag)){
            if(tagAttributes.getAttributMap().get(tag).containsKey(DaxCoreTags.COLLECTION_IS_DICTIONARY)){
                return tagAttributes.getAttributMap()
                                     .get(tag)
                                      .get(DaxCoreTags.COLLECTION_IS_DICTIONARY).getBooleanValue();
            }
        }

      return false;
    }
    ///---------------------

}
