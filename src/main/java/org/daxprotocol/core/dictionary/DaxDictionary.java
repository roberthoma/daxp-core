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
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.field.*;
import org.daxprotocol.core.group.DaxDTO;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxCollectionTool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


//TODO
// dictionary od exception by context
// CRM-00234, DAX-23445, $:23455 , crm:33345

public class DaxDictionary {

    DaxpConfig config;
    DaxStringReferenceMapper contextMapper;
    DaxStringReferenceMapper messageMapper;

    /*****************************************************
     *  Map of context referenced by integer
     */
    Map<Integer, DaxContext> contextMap = new HashMap<>();


    /*****************************************************
     * Main SET of tags
     */
    Set<DaxTag> tagSet = new HashSet<>();



    Map<Integer, DaxEnumDictionary> enumDictionaryMap = new HashMap<>();
//    DaxEnumDictionary enumDictionary; //Application enumDic

    /*****************************************************
     * Dictionary of messages type, required and respond tags
     * Key: Message type
     * */

    Map<Integer, DaxMessageDictionary> messageDicMap = new HashMap<>();;
    DaxMessageDictionary msgMap;

    /*****************************************************
     *Map of tag attributes
     * Key : tagId
     * Value : map of attributes
     * */
    Map<DaxTag, Map<DaxTag, DaxPair<?>>> attributMap = new HashMap<>();


    /*****************************************************
     *  Group Map // TODO chage to type or schema
     */
    Map<DaxTag, DaxDTO> dtoMap = new HashMap<>();
    Map<DaxTag, Set<DaxTag>> dtoFieldsMap = new HashMap<>();


    /*****************************************************
     *  Handler And controller maps
     */

    Map<String, Method> handlerMap = new HashMap<>();
    Map<Class<?>, Object >  daxControllerMap = new HashMap<>();


    /******************************************************/
    int appContextId;

    public DaxDictionary(DaxpConfig config,
                         DaxStringReferenceMapper contextMapper ,
                         DaxStringReferenceMapper messageMapper
    )
    {
        System.out.println("Init DaxDictionary...");
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

    public void putDTO(DaxDTO group){

        dtoMap.put(group.getTag(),group);

    }

    public Map<DaxTag, DaxDTO> getDtoMap() {
        return dtoMap;
    }


    public Map<DaxTag, Set<DaxTag>> getDtoFieldsMap(){
        return dtoFieldsMap;
    }


    public Set<DaxTag> getTagSet(){
        return tagSet;
    }

    //TODO chek exist of fields in group,
    //TODO check recursions
    public void putDtoField(DaxTag dtoTag, DaxTag tag) {
        dtoFieldsMap.merge(dtoTag,  new HashSet<>(Set.of(tag)),(daxTags, daxTags2) ->
                DaxCollectionTool.addAndReturnSet(daxTags, tag) );
    }

    //**********************************************************************
    // Attributes

    private void putAttribute(int contextId, int tagId, DaxPair<?> atrPair){

      DaxTag tag = new DaxTag(contextId, tagId);

      attributMap.merge(tag, new HashMap<>(Map.of(atrPair.getTag(), atrPair)),
                (eM, nM) ->
                        DaxCollectionTool.putAndReturnMap(eM, atrPair.getTag(), atrPair));

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

    public void putAtrDataType(DaxTag tag,  Class<?> clazz){
        putAttribute(tag, new DaxAtrDataType(clazz));
    };

    public void putAtrDataType(DaxTag tag,  Character c){
        putAttribute(tag, new DaxAtrDataType(c));
    };


    public void putAtrUiLabel(DaxTag tag,  String uiLabel){
        if (uiLabel.isBlank()) return;
        putAttribute(tag.getContextId(),tag.getTagId(), new DaxAtrUiLabel(uiLabel));
    }

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

    // put DaxAtrDeprecated

    public void putTag(DaxTag tag){

        if (tagSet.contains(tag)){
            System.out.println("TAG > "+tag.getTagId() + " ...........  EXIST ............ ");
            //throw new RuntimeException("Tag "+tag.getTagId()+" exist !!!");
        }
        tagSet.add(tag);

    }


    public void putHandler(String msgType, Method method, Class<?> clazz) {
        handlerMap.put(msgType, method);
    }

    public void registerCtrl(Object daxpController) {
        daxControllerMap.put(daxpController.getClass(), daxpController);
    }

    public DaxMessage executor(DaxMessage reqMsg) {
        try {
            String msgType = reqMsg.getMsgType();
            Method method = handlerMap.get(msgType);

            Object obj = daxControllerMap.get(method.getDeclaringClass());

            Object respObj = method.invoke(obj, reqMsg);

            return (DaxMessage) respObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
