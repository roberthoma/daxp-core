/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 Robert Homa
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
import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxStringPair;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.group.DaxpGroupItf;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.field.DaxMsgType;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.trailer.DaxTrailer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.daxprotocol.core.codec.DaxTag.*;

public class DaxMessageFactory {



    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxMsgType.DIC_REQ);
    }


    private void putBodyBlock(DaxBody body, int fieldId, Map<Integer, DaxPair<?>> map){
        body.nextBlock(DaxBlockType.BLOCK_FIELD);
        body.putPair(FIELD_ID,String.valueOf( fieldId));
        map.forEach((i, pair) -> body.putPair(pair));

    }


    private void putDicValueToBody(DaxBody body, int fieldId  ,String kValue, String vDesc){
        body.nextBlock(DaxBlockType.BLOCK_ENUM_VALUE);
        body.putPair(FIELD_ID,String.valueOf( fieldId));
        body.putPair(FIELD_VALUE,kValue);
        body.putPair(FIELD_VALUE_DESCRIPTION,vDesc);
    }

    private void putGroupToBody(DaxBody body, DaxpFieldGroup group){
        body.nextBlock(DaxBlockType.BLOCK_GROUP);
        body.putPair(GROUP_ID, String.valueOf(group.id()));
        body.putPair(GROUP_NAME, String.valueOf(group.name()));
        if (group.masterId() != 0 ){
            body.putPair(GROUP_MASTER_ID, String.valueOf(group.masterId()));
        }
        if (!group.description().isBlank() ){
            body.putPair(GROUP_DESCRIPTION, group.description());
        }

    }

    //TODO Create message with dictionary using context
   // TODO BLOCK_TYPE use
    public DaxMessage createDictionaryMsg(DaxDictionary dictionary) {
        DaxMessage message = new DaxMessage(DaxMsgType.DATA_DIC);

        dictionary.getAttributMap().forEach((fieldId, atrMap) ->
                        putBodyBlock(message.getBody(),fieldId,  atrMap)
                );

        dictionary.getValueDicMap().forEach((fieldId, valeMap) ->
                valeMap.forEach((v, vDesc) -> putDicValueToBody(message.getBody(), fieldId, v, vDesc ))
                );

        dictionary.getGroupMap().forEach((integer, group) ->
                putGroupToBody(message.getBody(), group));

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
                            body.putPair(new DaxPair<>(daxp.tag(), field.get(entry)));
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

    public DaxMessage errorInvalidMessageType() {
        DaxMessage message = new DaxMessage(DaxMsgType.ERR_RES);
        message.getBody().nextBlock();
        message.getBody().putPair(new DaxStringPair(ERR_DESCRIPTION,"Invalid Message Type"));
        return message;
    }
}
