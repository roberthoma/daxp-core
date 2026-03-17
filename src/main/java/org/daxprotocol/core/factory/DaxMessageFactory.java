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
import org.daxprotocol.core.annotation.DaxpType;
import org.daxprotocol.core.annotation.DaxpValue;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.dictionary.*;
import org.daxprotocol.core.group.DaxGroup;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.field.DaxMsgType;
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

import static org.daxprotocol.core.codec.DaxTagConst.*;

public class DaxMessageFactory {

    DaxpConfig config;
    DaxStringReferenceMapper contextMapper;
    DaxTagCodec tagCodec;

    public DaxMessageFactory(DaxpConfig config, DaxStringReferenceMapper contextMapper, DaxTagCodec tagCodec) {
        this.config = config;
        this.contextMapper = contextMapper;
        this.tagCodec = tagCodec;
    }

    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxMsgType.DIC_REQ);
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
                DaxpConfig.CONTEXT_TAG_SEPARATOR+
                tag.getTagId();

    }

    private   String createTagListStr(Set<DaxTag> daxFields){

        return daxFields.stream()
                .map(this::tagEncode)
                .collect(Collectors.joining(DaxpConfig.TAG_LIST_SEPARATOR));
    }

    private   String createStringValueListStr(Set<String> stringSet){

        return String.join(DaxpConfig.TAG_LIST_SEPARATOR, stringSet);
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


    private void putGroupToBody(DaxBody body, DaxGroup group, Set<DaxTag> daxFields){
        body.nextBlock(DaxBlockType.BLOCK_GROUP);
        body.putPair(FIELD_ID, tagEncode(group.getTag()));
        body.putPair(GROUP_NAME, group.getName());
        if (!group.getDescription().isBlank() ){
            body.putPair(GROUP_DESCRIPTION, group.getDescription());
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

        dictionary.getGroupMap().forEach((integer, group) ->
                putGroupToBody(message.getBody(), group, dictionary.getGroupFieldsMap().get(group.getTag())));



    }



    public DaxMessage dictionaryToMsg(DaxDictionary dictionary) {
        DaxMessage message = new DaxMessage(DaxMsgType.DATA_DIC);

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
            toDaxMessageFromList( messageType, (List<Object>) daxDataEntry )
          : toDaxMessageFromList( messageType, List.of(daxDataEntry) );
    }

    //TODO reate message with token
    public DaxMessage toDaxMessage( DaxMessage messageReq , String messageType, Object daxDataEntry ) {

        return  daxDataEntry instanceof List<?> ?
                toDaxMessageFromList( messageType, (List<Object>) daxDataEntry )
                : toDaxMessageFromList( messageType, List.of(daxDataEntry) );
    }




    private DaxMessage toDaxMessageFromList(String messageType, List<Object> daxDataEntry ){
      //  DaxPreamble preamble = new DaxPreamble();
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();
        daxDataEntry.forEach(entry -> {
            //todo REFACTORING
            if (entry.getClass().isAnnotationPresent(DaxpType.class)) {
//                DaxDictionaryDecoratorService.printDaxScanClass(entry.getClass());
                DaxpType group = entry.getClass().getAnnotation(DaxpType.class);

            body.nextBlock();
            body.putPair(BLOCK_TYPE, DaxBlockType.BLOCK_INSTANCE);
            body.putPair(GROUP_NAME, String.valueOf(group.name()));
            }

            try {
                for (Field field : DaxLangTool.allFields(entry.getClass())) {
                    if (field.isAnnotationPresent(DaxpField.class)) {
                        DaxpField daxp = field.getAnnotation(DaxpField.class);
                        field.setAccessible(true);
                        if (field.get(entry) != null) { //TODO For String check is empty
                            body.putPair(new DaxPair<>(daxp.tagId(), field.get(entry)));
                        }
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //>>>>>>>>>>>>....
            try {
                for (Method m : entry.getClass().getDeclaredMethods()) {
                    DaxpValue methodAnn = m.getAnnotation(DaxpValue.class);
                    if (methodAnn == null) continue;
                    Class<?> returnType = m.getReturnType();
                    Object o = m.invoke(entry);
                    DaxTag tag = new DaxTag(config.getAppContextId(),methodAnn.tagId());
                    body.putPair(new DaxPair<>(tag, o.toString()));
                }
            } catch (Exception e) {
                //throw new RuntimeException(e);
                e.printStackTrace();
            }

            //<<<<<<<<<




        });

        return new DaxMessage(head,body,trailer);
    }

    @SuppressWarnings("unchecked")
    public DaxMessage toDaxMessageFromPairMap( String messageType, Object pairMap)
    {
        return  pairMap instanceof List<?> ?
                toDaxMessageFromListOfPairMap( messageType, (List<Map<DaxTag,DaxPair<?>>>) pairMap )
                : toDaxMessageFromList( messageType, List.of(pairMap) );
    }

    private DaxMessage toDaxMessageFromListOfPairMap( String messageType,
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
        DaxMessage message = new DaxMessage(DaxMsgType.ERR_RES);
        message.getBody().nextBlock();
        message.getBody().putPair(new DaxStringPair(ERR_DESCRIPTION,"Resource not found"));
        return message;
    }

    public DaxMessage okMessageType() {
        DaxMessage message = new DaxMessage(DaxMsgType.OK_RES);
        return message;
    }


    public DaxMessage errorInvalidMessageType() {
        DaxMessage message = new DaxMessage(DaxMsgType.ERR_RES);
        message.getBody().nextBlock();
        message.getBody().putPair(new DaxStringPair(ERR_DESCRIPTION,"Invalid Message Type"));
        return message;
    }

}
