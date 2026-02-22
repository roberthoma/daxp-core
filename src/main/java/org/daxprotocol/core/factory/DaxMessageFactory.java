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
import org.daxprotocol.core.annotation.DaxpFieldGroup;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.decorator.DaxDictionaryDecoratorService;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageDicItem;
import org.daxprotocol.core.dictionary.DaxEnumName;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.group.DaxpGroupItf;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.field.DaxMsgType;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.daxprotocol.core.tool.DaxLangTool;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.daxprotocol.core.codec.DaxTagConst.*;

public class DaxMessageFactory {

    DaxpConfig config;
    DaxContextMapper contextMapper;

    public DaxMessageFactory(DaxpConfig config, DaxContextMapper contextMapper) {
        this.config = config;
        this.contextMapper = contextMapper;
    }

    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxMsgType.DIC_REQ);
    }


    private void putAttributesToTagBlock(DaxBody body, DaxTag tag, Map<DaxTag, DaxPair<?>> map){
        String fieldId;
        body.nextBlock(DaxBlockType.BLOCK_TAG);

        if ( tag.getContextId() != config.getAppContextId()
          && tag.getContextId() != DaxpConfig.DAXP_CONTEXT_ID
        ){
            fieldId = contextMapper.getContextSymbol(tag.getContextId())+DaxpConfig.CONTEXT_TAG_SEPARATOR+
                    tag.getTagId();
        }
        else {
            fieldId = String.valueOf(tag.getTagId());
        }

        body.putPair(FIELD_ID,fieldId);

        map.forEach((i, pair) -> body.putPair(pair));

    }


    private void putEnumDicToEnumBody(DaxBody body, String enumName ,String kValue, String vDesc){
        body.nextBlock(DaxBlockType.BLOCK_ENUM_VALUE);
        body.putPair(ENUM_NAME,enumName);
        body.putPair(FIELD_VALUE,kValue);
        body.putPair(FIELD_VALUE_DESCRIPTION,vDesc);
    }

    private void putGroupToBody(DaxBody body, DaxpGroupItf group){
        body.nextBlock(DaxBlockType.BLOCK_GROUP);
        body.putPair(GROUP_ID, String.valueOf(group.getId()));
        body.putPair(GROUP_NAME, String.valueOf(group.getName()));
        if (group.getMasterId() != 0 ){
            body.putPair(GROUP_MASTER_ID, String.valueOf(group.getMasterId()));
        }
        if (!group.getDescription().isBlank() ){
            body.putPair(GROUP_DESCRIPTION, group.getDescription());
        }

    }

    private void putEnumToBlock(DaxBody body, DaxEnumName enumName){
        body.nextBlock(DaxBlockType.BLOCK_ENUM);
        body.putPair(ENUM_NAME, enumName.getName());
        body.putPair(ENUM_DESCRIPTION, enumName.getDesc());
    }
    private void putMsgItem(DaxBody body,  DaxMessageDicItem msgItem){
        body.nextBlock(DaxBlockType.BLOCK_MESSAGE);
        body.putPair(FIELD_VALUE, msgItem.getMsgType());
        body.putPair(FIELD_VALUE_DESCRIPTION, msgItem.getMsgDesc());
    }

    private void putContextToBody(DaxBody body, DaxContext daxContext) {
        body.nextBlock(DaxBlockType.BLOCK_CONTEXT);
       // body.putPair(FIELD_ID, String.valueOf(daxContext.id));
        body.putPair(FIELD_VALUE_SYMBOL, daxContext.getSymbol());
        body.putPair(FIELD_VALUE_PREFIX, daxContext.getTagPrefix());
        body.putPair(FIELD_VALUE_DESCRIPTION, daxContext.getDescription());

    }


    private String tagEncode(DaxTag tag){
        if (tag.getContextId() == config.getAppContextId()){
            return String.valueOf(tag.getTagId());
        }
        return contextMapper.getContextSymbol(tag.getContextId())+
               DaxpConfig.CONTEXT_TAG_SEPARATOR+
               tag.getTagId();

    }

    private void putFieldsGroup(DaxBody body, Integer groupId, Set<DaxTag> daxTags) {
        body.nextBlock(DaxBlockType.BLOCK_FIELD_LIST);
        body.putPair(GROUP_ID, String.valueOf(groupId));
        String tagListStr = daxTags.stream()
                .map(this::tagEncode)
                .collect(Collectors.joining(DaxpConfig.TAG_LIST_SEPARATOR));
        body.putPair(FIELD_ID_LIST, tagListStr);
    }


    //TODO Create message with dictionary using context, or group, or field/(list of field)
    //TODO  create multi message with context dictionary values

    private void dictionaryToMsg(DaxMessage message,
                                           DaxDictionary dictionary)
    {
        dictionary.getMsgMap().forEach((s, messageDicItem) ->
                putMsgItem(message.getBody(),messageDicItem)
                );

        dictionary.getEnumMap().forEach((s, enumName) ->
                putEnumToBlock(message.getBody(), enumName)
                );

        dictionary.getEnumValueMap().forEach((enumName, valeMap) ->
                valeMap.forEach((v, enumValue)
                        -> putEnumDicToEnumBody(message.getBody(), enumName, v, enumValue.getDesc() ))
                );

        dictionary.getGroupMap().forEach((integer, group) ->
                putGroupToBody(message.getBody(), group));


        dictionary.getAttributMap().forEach((tag, atrMap) ->
                putAttributesToTagBlock(message.getBody(),tag,  atrMap)
        );

        dictionary.getFieldsGroupMap().forEach((groupId, daxFields) ->
                putFieldsGroup(message.getBody(),groupId,daxFields)

        );

   //     dictionary.getTagSet().forEach(daxTag -> putTagsBlock(message.getBody(),daxTag));

    }



    public DaxMessage dictionaryToMsg(DaxDictionary dictionary) {
        DaxMessage message = new DaxMessage(DaxMsgType.DATA_DIC);

//        DaxStringPair ctxPair = new DaxStringPair(MSG_CONTEXT, String.valueOf(dictionary.getContextId()));
//        message.getHead().putPair(ctxPair);


        dictionary.getContextMap().forEach((idCtx, context) ->
                        putContextToBody(message.getBody(), context)
                );

        dictionaryToMsg(message , dictionary);

//        dictionary.getContextDicMap()
//                  .forEach((id, contextDic) ->
//                            dictionaryToMsg(message, contextDic)
        //putDictionaryToBody(message, dictionary.getApplicationDictionary())
 //       );

        message.finish();
        return message;
    }



    @SuppressWarnings("unchecked")
    public DaxMessage toDaxMessage(String messageType, Object daxDataEntry ) {

//        if (daxDataEntry instanceof Map<?,?>) { ????
//
//            System.out.println("JEST Map<DaxTag,DaxPair<?>>");
//        }

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
            if (entry.getClass().isAnnotationPresent(DaxpFieldGroup.class)) {
                DaxDictionaryDecoratorService.printDaxScanClass(entry.getClass());
                DaxpFieldGroup group = entry.getClass().getAnnotation(DaxpFieldGroup.class);

            body.nextBlock();
            body.putPair(BLOCK_TYPE, DaxBlockType.BLOCK_INSTANCE);
            body.putPair(GROUP_ID, String.valueOf(group.groupId()));
            }




            try {
//                for (Field field : entry.getClass().getDeclaredFields()) {
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
