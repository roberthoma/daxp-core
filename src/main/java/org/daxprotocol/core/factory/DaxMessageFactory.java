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
import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpValue;
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.codec.*;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.dictionary.*;
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
    DaxTagParser tagParser;
    DaxDataTypeCodec dataTypeCodec;
    public DaxMessageFactory(DaxConfig config,
            DaxContextMapper contextMapper,
            DaxTagCodec tagCodec,
            DaxMessageCodec messageCodec,
            DaxHeadCodec headCodec,
            DaxBodyCodec bodyCodec,
            DaxTrailerCodec trailerCodec,
            DaxDictionary dictionary,
            DaxTagParser tagParser,
            DaxDataTypeCodec dataTypeCodec

            ) {
        this.config = config;
        this.contextMapper = contextMapper;
        this.tagCodec = tagCodec;
        this.messageCodec = messageCodec;
        this.headCodec = headCodec;
        this.bodyCodec = bodyCodec;
        this.trailerCodec = trailerCodec;
        this.dictionary = dictionary;
        this.tagParser = tagParser;
        this.dataTypeCodec = dataTypeCodec;

    }

    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxCoreMessages.DIC_REQ);
    }

    private void putAttributesToTagBlock(DaxBody body, DaxTag tag, Map<DaxTag, DaxPair<?>> map){
        body.nextBlock(DaxBlockType.BLOCK_TAG);
        body.putPair(new DaxPairTag(ENTRY_TAG,tag));
        map.forEach((atrTag, pair) -> body.putPair(pair));

    }



    private   String createTagListStr(Set<DaxTag> daxFields){

        return daxFields.stream()
                .map(tag ->  tagCodec.encode(tag))
                .collect(Collectors.joining(DaxCoreConstants.TAG_LIST_SEPARATOR));
    }

    private   String createStringValueListStr(Set<String> stringSet){

        return String.join(DaxCoreConstants.TAG_LIST_SEPARATOR, stringSet);
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


//    private void putEntityToBody(DaxBody body, DaxEntity entity, Set<DaxTag> daxFields){
//        body.nextBlock(DaxBlockType.BLOCK_ENTITY);
//        body.putPair(ENTRY_TAG, tagCodec.encode(entity.getTag()) );
//        body.putPair(ENTRY_NAME, entity.getName());
//        body.putPair( ENTITY_NAME, entity.getName());
//        if (!entity.getDescription().isBlank() ){
//            body.putPair(ENTRY_DESCRIPTION, entity.getDescription());
//        }
//
//        body.putPair(TAG_LIST, createTagListStr(daxFields));
//
//
//
//    }



    private void putEnumToBlock(DaxBody body,
                                        DaxTag tag,
                                DaxCollection_TMP daxCollectionTMP){
        body.nextBlock(DaxBlockType.BLOCK_COLLECTION);
        body.putPair(COLLECTION_ID, tagCodec.encode(tag));
        body.putPair(ENTRY_NAME, daxCollectionTMP.getName());
        body.putPair(ENTRY_DESCRIPTION, daxCollectionTMP.getDesc());

//TODO put list o enum value if description is empty
//        if (enumValueMap != null) {
//            body.putPair(ENUM_VALUE_LIST, String.join(DaxpConfig.VALUE_LIST_SEPARATOR, enumValueMap.keySet()));
//        }

    }
    private void putCollectionValuesToBody(DaxBody body, DaxTag tag,Map<String, DaxEnumValue> enumValueMap ){
        enumValueMap.forEach( (s, value) -> {
            body.nextBlock(DaxBlockType.BLOCK_VALUE);
            body.putPair(COLLECTION_ID, tagCodec.encode(tag));
            body.putPair(COLLECTION_VALUE, value.getValue());

            if (!value.getDesc().isBlank()) {
                body.putPair(ENTRY_DESCRIPTION, value.getDesc());
            }
        }
        );

    }

    private void enumDictionaryToMsg(DaxBody body, DaxCollectionRegister enumDictionary){

        enumDictionary.getEnumMap().forEach((daxTag, daxEnum) ->
        { putEnumToBlock(body,daxTag,daxEnum);
          putCollectionValuesToBody(body,daxTag,enumDictionary.getEnumValueMap(daxTag));
        }
        );
    }



    private void putMsgItem(DaxBody body,  DaxMessageItem msgItem){
        body.nextBlock(DaxBlockType.BLOCK_MESSAGE);
        body.putPair(ENTRY_SYMBOL, msgItem.getMsgType());
        body.putPair(ENTRY_DESCRIPTION, msgItem.getMsgDesc());

        body.putPair(MESSAGE_TAGS, createTagListStr(msgItem.getMsgFields()));
        body.putPair(MESSAGE_RELATED_MSGS, createStringValueListStr(msgItem.getRelatedMsgType()));

    }

    private void putContextToBody(DaxBody body, DaxContext daxContext) {
        body.nextBlock(DaxBlockType.BLOCK_CONTEXT);
        body.putPair(ENTRY_SYMBOL, daxContext.getSymbol());
        body.putPair(FIELD_VALUE_PREFIX, daxContext.getTagPrefix());
        body.putPair(ENTRY_DESCRIPTION, daxContext.getDescription());

    }




    //TODO Develop selective tags
    public DaxMessage dictionaryToMsg() {
        DaxMessage message = new DaxMessage(DaxCoreMessages.DATA_DIC);

        dictionary.getContextMap().forEach((idCtx, context) ->
                        putContextToBody(message.getBody(), context)
                );

        dictionary.getMsgMap().forEach((s, messageDicItem) ->
                putMsgItem(message.getBody(),messageDicItem)
        );

        enumDictionaryToMsg(message.getBody(),dictionary.getEnumDictionary(1));

        dictionary.getAttributMap().forEach((tag, atrMap) ->
                putAttributesToTagBlock(message.getBody(),tag,  atrMap)
        );

//        dictionary.getEntityMap().forEach((tag, entity) ->
//                putEntityToBody(message.getBody(), entity, dictionary.getEntityFieldsMap().get(entity.getTag())));



        message.finish();
        return message;
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxxxxxxxxxxxxxxxxxxxxxxxxx

    //TODO Check message atributes if  any massage is resoint type then will need message request
    // and throw exception
    @SuppressWarnings("unchecked")
    public DaxMessage toDaxMessage(String messageType, Object daxDataEntry ) {

        return  daxDataEntry instanceof List<?> ?
            toDaxMessageFromList( messageType, (List<Object>) daxDataEntry , null)
          : toDaxMessageFromList( messageType, List.of(daxDataEntry), null );
    }

    //TODO reate message with token and implement  outFrame
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
        return DaxTag.of(contextId ,tagId);
    }


//todo add required tagCollection reqTagSet
    private void objectToMsgBlock(int blogIdx, DaxTag blockTag, Object entry,
            DaxBody body,
            Set<DaxTag> reqTagSet ){
        body.putPair(blogIdx, ENTRY_TAG, blockTag);
        try {
            for (Field field : DaxLangTool.allFields(entry.getClass())) {
                //----------------------------------------
                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField fieldAnn = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);

                    DaxTag tag;

                    if (field.get(entry) != null){
                        tag = tagCodec.decode( fieldAnn);

                        if(reqTagSet != null && !reqTagSet.contains(tag)){
                            continue;
                        }

                        //TODO add refenrens to other oblck using prefix like @ or #....
                        if (field.get(entry).getClass().isAnnotationPresent(DaxpEntity.class)){
                            body.nextBlock(DaxBlockType.BLOCK_INSTANCE);
                            int nestedIdx = body.getCurrentIdx();

                            body.putTagBlockReference(blogIdx, tag, (nestedIdx+1));

                            objectToMsgBlock(nestedIdx, tag,  field.get(entry),  body , reqTagSet);
                        }
                        else {
//                            body.putPair(blogIdx,tag, new DaxValue<>(field.get(entry)));
                            body.putPair(blogIdx, dataTypeCodec.convertToValue(tag,field.get(entry) ));
                        }
                    }
                    continue;
                }
                if (field.isAnnotationPresent(DaxpValue.class)) {
                    DaxpValue valueAnn = field.getAnnotation(DaxpValue.class);
                    //   field.setAccessible(true);
                    if (field.get(entry) != null) {
                        DaxTag tag = tagCodec.decode( valueAnn);

                        if(reqTagSet != null && !reqTagSet.contains(tag)){
                            continue;
                        }
                        body.putPair(blogIdx,dataTypeCodec.convertToValue(tag, field.get(entry) ));

//                        body.putPair(blogIdx,new DaxValue<>(tag, field.get(entry)));
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

                DaxTag tag = tagCodec.decode( methodAnn);
                if(reqTagSet != null && !reqTagSet.contains(tag)){
                    continue;
                }
                Object o = method.invoke(entry);
                body.putPair(blogIdx,new DaxPairString(tag,o.toString()));
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
            if (entry.getClass().isAnnotationPresent(DaxpEntity.class)) {
                DaxpEntity entityAnn = entry.getClass().getAnnotation(DaxpEntity.class);
                DaxTag tag =  tagCodec.decode( entityAnn);
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

    public DaxMessage toDaxMessageFromPairMap( String messageType, Map<DaxTag, DaxPair<?>> pairMap){
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        body.nextBlock();
        pairMap.forEach((daxTag, pair) -> body.putPair(pair));
//        pairMap.forEach(body::putPair);

        return new DaxMessage(head,body,trailer);

    }


    public DaxMessage toDaxMessageFromListOfPairMap( String messageType,
                                          List<Map<DaxTag, DaxPair<?>>> pairMapList){


        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        pairMapList.forEach(pairMap ->{
                    body.nextBlock();
                    pairMap.forEach((daxTag, pair) -> body.putPair(pair));
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
        DaxHead head;
        DaxBody body;
        DaxTrailer trailer;

        head = headCodec.createHead(listOfPair);
        body = bodyCodec.createBody(head.getBlockCount(), listOfPair) ;
        trailer = trailerCodec.createTrailer(listOfPair);
        //todo trailer with check

        return new DaxMessage(head,body,trailer);
        //return messageCodec.createMsg(messageType, listOfPair);
    }

    public DaxMessage createMsg(List<DaxPair<?>> listOfPair){
        DaxHead head;
        DaxBody body;
        DaxTrailer trailer;

        head = headCodec.createHead(listOfPair);
        body = bodyCodec.createBody(0, listOfPair) ;
        trailer = trailerCodec.createTrailer(listOfPair);
        //todo trailer with check

        return new DaxMessage(head,body,trailer);
    }



}
