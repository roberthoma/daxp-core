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

package org.daxprotocol.core.factory;

import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.annotation.DaxpValue;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.codec.*;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.*;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.dto.DaxDTO;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.daxprotocol.core.tool.DaxLangTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxMessageFactory {

    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxTagCodec tagCodec;
    DaxMessageCodec messageCodec;
    DaxHeadCodec     headCodec;
    DaxBodyCodec     bodyCodec;
    DaxTrailerCodec  trailerCodec;
    DaxDictionary dictionary;
    public DaxMessageFactory(DaxConfig config,
            DaxContextMapper contextMapper,
            DaxTagCodec tagCodec,
            DaxMessageCodec messageCodec,
            DaxHeadCodec headCodec,
            DaxBodyCodec bodyCodec,
            DaxTrailerCodec trailerCodec,
            DaxDictionary dictionary

            ) {
        this.config = config;
        this.contextMapper = contextMapper;
        this.tagCodec = tagCodec;
        this.messageCodec = messageCodec;
        this.headCodec = headCodec;
        this.bodyCodec = bodyCodec;
        this.trailerCodec = trailerCodec;
        this.dictionary = dictionary;

    }

    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxCoreMessages.DIC_REQ);
    }

    private void putAttributesToTagBlock(DaxBody body, DaxTag tag, Map<DaxTag, DaxPair<?>> map){
        body.nextBlock(DaxBlockType.BLOCK_TAG);
        body.putPair(FIELD_ID,tagCodec.encode(tag));

        map.forEach((i, pair) -> body.putPair(pair));
    }


    private String tagEncode(DaxTag tag){
        if (tag.getContextId() == config.getAppContextId()){
            return String.valueOf(tag.getTagId());
        }

        return contextMapper.getReference(tag.getContextId())+
                DaxConfig.CONTEXT_TAG_SEPARATOR+
                tag.getTagId();

    }

    private   String createTagListStr(Set<DaxTag> daxFields){

        return daxFields.stream()
                .map(this::tagEncode)
                .collect(Collectors.joining(DaxConfig.TAG_LIST_SEPARATOR));
    }

    private   String createStringValueListStr(Set<String> stringSet){

        return String.join(DaxConfig.TAG_LIST_SEPARATOR, stringSet);
    }


//    private void putEnumDicToEnumBody(DaxBody body, String enumName ,String kValue, String vDesc){
//        body.nextBlock(DaxBlockType.BLOCK_ENUM_VALUE);
//        body.putPair(ENUM_NAME,enumName);
//        body.putPair(FIELD_VALUE,kValue);
//        body.putPair(FIELD_VALUE_DESCRIPTION,vDesc);
//    }

//
//            dictionary.getFieldsGroupMap().forEach((groupId, daxFields) ->
//    putFieldsGroup(message.getBody(),groupId,daxFields)
//
//            );


    private void putDtoToBody(DaxBody body, DaxDTO dto, Set<DaxTag> daxFields){
        body.nextBlock(DaxBlockType.BLOCK_DTO);
        body.putPair(FIELD_ID, tagEncode(dto.getTag()));
        body.putPair(DTO_NAME, dto.getName());
        if (!dto.getDescription().isBlank() ){
            body.putPair(DTO_DESCRIPTION, dto.getDescription());
        }

        body.putPair(FIELD_ID_LIST, createTagListStr(daxFields));



    }

//    private void purEnumValueToBlock(DaxBody body,
//            DaxTag tag,
//            DaxEnumValue enumValueMap)
////                                Map<String, DaxEnumValue> enumValueMap)
//    {
//        body.nextBlock(DaxBlockType.BLOCK_ENUM_VALUE);
//        body.putPair(ENUM_ID, tagEncode(tag));
//        body.putPair(ENUM_VALUE, enumValueMap.getValue());
//        body.putPair(ENUM_VALUE_DESCRIPTION, enumValueMap.getDesc());
//
//
//    }

    private void putEnumToBlock(DaxBody body,
                                        DaxTag tag,
                                DaxEnum daxEnum){
        body.nextBlock(DaxBlockType.BLOCK_ENUM);
        body.putPair(ENUM_ID, tagEncode(tag));
        body.putPair(ENUM_NAME, daxEnum.getName());
        body.putPair(ENUM_DESCRIPTION, daxEnum.getDesc());

//TODO put list o enum value if description is empty
//        if (enumValueMap != null) {
//            body.putPair(ENUM_VALUE_LIST, String.join(DaxpConfig.VALUE_LIST_SEPARATOR, enumValueMap.keySet()));
//        }

    }
    private void putEnumValuesToBody(DaxBody body, DaxTag tag,Map<String, DaxEnumValue> enumValueMap ){
        enumValueMap.forEach( (s, value) -> {
            body.nextBlock(DaxBlockType.BLOCK_ENUM_VALUE);
            body.putPair(ENUM_ID, tagEncode(tag));
            body.putPair(ENUM_VALUE, value.getValue());

            if (!value.getDesc().isBlank()) {
                body.putPair(ENUM_VALUE_DESCRIPTION, value.getDesc());
            }
        }
        );

    }

    private void enumDictionaryToMsg(DaxBody body,DaxEnumDictionary enumDictionary){

        enumDictionary.getEnumMap().forEach((daxTag, daxEnum) ->
        { putEnumToBlock(body,daxTag,daxEnum);
          putEnumValuesToBody(body,daxTag,enumDictionary.getEnumValueMap(daxTag));
        }
        );
    }



    private void putMsgItem(DaxBody body,  DaxMessageItem msgItem){
        body.nextBlock(DaxBlockType.BLOCK_MESSAGE);
        body.putPair(FIELD_VALUE, msgItem.getMsgType());
        body.putPair(FIELD_VALUE_DESCRIPTION, msgItem.getMsgDesc());

        body.putPair(MESSAGE_TAGS, createTagListStr(msgItem.getMsgFields()));
        body.putPair(MESSAGE_RELATED_MSGS, createStringValueListStr(msgItem.getRelatedMsgType()));

    }

    private void putContextToBody(DaxBody body, DaxContext daxContext) {
        body.nextBlock(DaxBlockType.BLOCK_CONTEXT);
        body.putPair(FIELD_VALUE_SYMBOL, daxContext.getSymbol());
        body.putPair(FIELD_VALUE_PREFIX, daxContext.getTagPrefix());
        body.putPair(FIELD_VALUE_DESCRIPTION, daxContext.getDescription());

    }


    //TODO  create multi message with context dictionary values


    private void dictionaryToMsg(DaxDictionary dictionary, DaxMessage message)
    {
        dictionary.getMsgMap().forEach((s, messageDicItem) ->
                putMsgItem(message.getBody(),messageDicItem)
                );


//        dictionary.getContextMap().forEach((i, context) ->
//                enumDictionaryToMsg(message.getBody(),dictionary.getEnumDictionary(i)));

        enumDictionaryToMsg(message.getBody(),dictionary.getEnumDictionary(1));

        //----------------------------------------------------------------------------------
        // TODO create attributes by tags !!!!!
        dictionary.getAttributMap().forEach((tag, atrMap) ->
                putAttributesToTagBlock(message.getBody(),tag,  atrMap)
        );

        //     dictionary.getTagSet().forEach(daxTag -> putTagsBlock(message.getBody(),daxTag));
        //-------------------------------------------------------------------------------

        dictionary.getDtoMap().forEach((integer, group) ->
                putDtoToBody(message.getBody(), group, dictionary.getDtoFieldsMap().get(group.getTag())));



    }



    public DaxMessage dictionaryToMsg() {
        DaxMessage message = new DaxMessage(DaxCoreMessages.DATA_DIC);

        dictionary.getContextMap().forEach((idCtx, context) ->
                        putContextToBody(message.getBody(), context)
                );

        dictionaryToMsg(dictionary,message );

        message.finish();
        return message;
    }



    //TODO Check message atributes if  any massage is resoint type then will need message reqwuest
    // and throw exception
    @SuppressWarnings("unchecked")
    public DaxMessage toDaxMessage(String messageType, Object daxDataEntry ) {

        return  daxDataEntry instanceof List<?> ?
            toDaxMessageFromList( messageType, (List<Object>) daxDataEntry , null)
          : toDaxMessageFromList( messageType, List.of(daxDataEntry), null );
    }

    //TODO reate message with token
    public DaxMessage toDaxRespondMessage( DaxFrame reqFrame , String messageType, Object daxDataEntry ) {
        Set<DaxTag> tagSet;

        if (reqFrame.getFirstMessage().containsField(REQ_FIELD_LIST)) {
            tagSet = (Set<DaxTag>) (reqFrame.getFirstMessage().get(REQ_FIELD_LIST).getValue());

            return  daxDataEntry instanceof List<?> ?
                    toDaxMessageFromList( messageType, (List<Object>) daxDataEntry , tagSet)
                    : toDaxMessageFromList( messageType, List.of(daxDataEntry), tagSet );

        }
        return  daxDataEntry instanceof List<?> ?
                  toDaxMessageFromList( messageType, (List<Object>) daxDataEntry , null)
                : toDaxMessageFromList( messageType, List.of(daxDataEntry), null );
    }

    //TODO move to tool class
    private DaxTag creatTag(String context, int tagId){

        int contextId = context.isBlank() ?
                config.getAppContextId():
                contextMapper.getReferenceId(context);
        return new DaxTag(contextId ,tagId);
    }


//todo add required tagCollection reqTagSet
    private void objectToMsgBlock(int blogIdx, DaxTag blockTag, Object entry,
            DaxBody body,
            Set<DaxTag> reqTagSet ){
        body.putPair(blogIdx,FIELD_ID, blockTag);
        try {
            for (Field field : DaxLangTool.allFields(entry.getClass())) {
                //----------------------------------------
                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField fieldAnn = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);

                    if (field.get(entry) != null){
                        DaxTag tag = creatTag(fieldAnn.context(), fieldAnn.value());

                        if(reqTagSet != null && !reqTagSet.contains(tag)){
                            continue;
                        }


                        if (field.get(entry).getClass().isAnnotationPresent(DaxpDTO.class)){
                            body.nextBlock(DaxBlockType.BLOCK_INSTANCE);
                            int nestedIdx = body.getCurrentIdx();
                            body.putPair(blogIdx, new DaxPair<>(tag,nestedIdx+1));
                            objectToMsgBlock(nestedIdx, tag,  field.get(entry),  body , reqTagSet);
                        }
                        else {
                            body.putPair(blogIdx,new DaxPair<>(tag, field.get(entry)));
                        }
                    }
                    continue;
                }
                if (field.isAnnotationPresent(DaxpValue.class)) {
                    DaxpValue daxValue = field.getAnnotation(DaxpValue.class);
                    //   field.setAccessible(true);
                    if (field.get(entry) != null) {
                        DaxTag tag = creatTag(daxValue.context(), daxValue.value());

                        if(reqTagSet != null && !reqTagSet.contains(tag)){
                            continue;
                        }



                        body.putPair(blogIdx,new DaxPair<>(tag, field.get(entry)));
                    }
                }
                //----------------------------------------

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            for (Method method : entry.getClass().getDeclaredMethods()) {
                DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
                if (methodAnn == null) continue;
                Class<?> returnType = method.getReturnType();

                DaxTag tag = new DaxTag(config.getAppContextId(),methodAnn.value());
                if(reqTagSet != null && !reqTagSet.contains(tag)){
                    continue;
                }
                Object o = method.invoke(entry);
                body.putPair(blogIdx,new DaxPair<>(tag, o.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }


    private DaxMessage toDaxMessageFromList(String messageType, List<Object> daxDataEntry , Set<DaxTag> reqTagSet){
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        daxDataEntry.forEach(entry -> {
            if (entry.getClass().isAnnotationPresent(DaxpDTO.class)) {
                DaxpDTO dtoAnn = entry.getClass().getAnnotation(DaxpDTO.class);
                DaxTag tag = creatTag("",dtoAnn.value());
                body.nextBlock(DaxBlockType.BLOCK_INSTANCE);
                objectToMsgBlock(body.getCurrentIdx(),tag, entry, body, reqTagSet);
            }

        });

        return new DaxMessage(head,body,trailer);
    }

    @SuppressWarnings("unchecked")
//    public DaxMessage toDaxMessageFromPairMap( String messageType, Object pairMap)
//    {
//        return  pairMap instanceof List<?> ?
//                 toDaxMessageFromList( messageType, List.of(pairMap) )
//        : toDaxMessageFromListOfPairMap( messageType, (List<Map<DaxTag,DaxPair<?>>>) pairMap );
//
//    }

    public DaxMessage toDaxMessageFromPairMap( String messageType, Map<DaxTag,DaxPair<?>> pairMap){
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        body.nextBlock();
        pairMap.forEach((daxTag, daxPair) -> body.putPair(daxPair));

        return new DaxMessage(head,body,trailer);

    }


    public DaxMessage toDaxMessageFromListOfPairMap( String messageType,
                                          List<Map<DaxTag,DaxPair<?>>> pairMapList){


        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        pairMapList.forEach(pairMap ->{
                    body.nextBlock();
                    pairMap.forEach((daxTag, daxPair) -> body.putPair(daxPair));
                }
                );


        return new DaxMessage(head,body,trailer);
    }


    public DaxMessage errorResourceNotFound() {
        DaxMessage message = new DaxMessage(DaxCoreMessages.ERR_RES);
        message.getBody().nextBlock();
        message.getBody().putPair(new DaxPairString(ERR_DESCRIPTION,"Resource not found"));
        return message;
    }

    public DaxMessage okMessageType() {
        return new DaxMessage(DaxCoreMessages.OK_RES);

    }


    public DaxMessage errorInvalidMessageType() {
        DaxMessage message = new DaxMessage(DaxCoreMessages.ERR_RES);
        message.getBody().nextBlock();
        message.getBody().putPair(new DaxPairString(ERR_DESCRIPTION,"Invalid Message Type"));
        return message;
    }

    public DaxMessage createMsg(String messageType,List<DaxPair<?>> listOfPair){

        return messageCodec.createMsg(messageType, listOfPair);
    }

}
