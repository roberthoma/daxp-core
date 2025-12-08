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
import org.daxprotocol.core.dictionary.DaxContextDictionary;
import org.daxprotocol.core.model.context.DaxContext;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageDicItem;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumName;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.group.DaxpGroupItf;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.field.DaxMsgType;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.trailer.DaxTrailer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.daxprotocol.core.codec.DaxTagConst.*;

public class DaxMessageFactory {



    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxMsgType.DIC_REQ);
    }


    private void putBodyBlock(DaxBody body, int fieldId, Map<Integer, DaxPair<?>> map){
        body.nextBlock(DaxBlockType.BLOCK_FIELD);
//        String tagStr = "X:"+String.valueOf( fieldId);
//        body.putPair(FIELD_ID,tagStr);
        body.putPair(FIELD_ID,String.valueOf( fieldId));
        map.forEach((i, pair) -> body.putPair(pair));

    }


    private void putDicValueToBody(DaxBody body, String enumName ,String kValue, String vDesc){
        body.nextBlock(DaxBlockType.BLOCK_ENUM_VALUE);
        body.putPair(ENUM_NAME,enumName);
        body.putPair(FIELD_VALUE,kValue);
        body.putPair(FIELD_VALUE_DESCRIPTION,vDesc);
    }

    private void putGroupToBody(DaxBody body, DaxpGroupItf group){
        body.nextBlock(DaxBlockType.BLOCK_GROUP_NAME);
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
        body.putPair(FIELD_ID, String.valueOf(daxContext.id));
        body.putPair(FIELD_VALUE_SYMBOL, daxContext.symbol);
        body.putPair(FIELD_VALUE_PREFIX, daxContext.tagPrefix);
        body.putPair(FIELD_VALUE_DESCRIPTION, daxContext.description);

    }


    //TODO Create message with dictionary using context, or group, or field/(list of field)
    //create multi message with context dictionary values
//    public DaxMessage createDictionaryMsg(DaxDictionary dictionary) {
    private void dictionaryToMsg(DaxMessage message,
                                           DaxContextDictionary dictionary)
    {
        dictionary.getMsgMap().forEach((s, messageDicItem) ->
                putMsgItem(message.getBody(),messageDicItem)
                );

        dictionary.getEnumMap().forEach((s, enumName) ->
                putEnumToBlock(message.getBody(), enumName)
                );

        dictionary.getEnumValueMap().forEach((enumName, valeMap) ->
                valeMap.forEach((v, enumValue)
                        -> putDicValueToBody(message.getBody(), enumName, v, enumValue.getDesc() ))
                );

        dictionary.getGroupMap().forEach((integer, group) ->
                putGroupToBody(message.getBody(), group));


        dictionary.getAttributMap().forEach((fieldId, atrMap) ->
                putBodyBlock(message.getBody(),fieldId,  atrMap)
        );
    }


    public DaxMessage dictionaryToMsg(DaxDictionary dictionary) {
        DaxMessage message = new DaxMessage(DaxMsgType.DATA_DIC);

//        DaxStringPair ctxPair = new DaxStringPair(MSG_CONTEXT, String.valueOf(dictionary.getContextId()));
//        message.getHead().putPair(ctxPair);


        dictionary.getContextMap().forEach((idCtx, context) ->
                        putContextToBody(message.getBody(), context)
                );


        dictionary.getContextDicMap()
                  .forEach((id, contextDic) ->
                            dictionaryToMsg(message, contextDic)
        //putDictionaryToBody(message, dictionary.getApplicationDictionary())
        );

        message.finish();
        return message;
    }


    @SuppressWarnings("unchecked")
    public DaxMessage toDaxMessage(String messageType, Object daxDataEntry ) {
        return  daxDataEntry instanceof List<?> ?
            toDaxMessageFromList( messageType, (List<Object>) daxDataEntry )
          : toDaxMessageFromList( messageType, List.of(daxDataEntry) );
    }

    private DaxMessage toDaxMessageFromList(String messageType, List<Object> daxDataEntry ){
        DaxPreamble preamble = new DaxPreamble();
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();
        daxDataEntry.forEach(entry -> {
            body.nextBlock();
            try {
                for (Field field : entry.getClass().getDeclaredFields()) {
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
