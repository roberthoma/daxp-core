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

import org.daxprotocol.core.annotation.DaxpTag;
import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.head.DaxMsgType;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.trailer.DaxTrailer;

import java.lang.reflect.Field;
import java.util.Map;

import static org.daxprotocol.core.codec.DaxTag.*;

public class DaxMessageFactory {



    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxMsgType.DIC_REQ);
    }


    private void putBodyBlock(DaxBody body, int fieldId, Map<Integer, DaxPair<?>> map){
        body.nextBlock();
        body.putPair(FIELD_ID,String.valueOf( fieldId));
        map.forEach((i, pair) -> body.putPair(pair));

    }

    public DaxMessage createDictionaryMsg(DaxDictionary dictionary) {
        DaxMessage message = new DaxMessage(DaxMsgType.DATA_DIC);
        dictionary.getAttributMap().forEach((fieldId, atrMap) ->
                        putBodyBlock(message.getBody(),fieldId,  atrMap)
                );
        return message;

    }


    public DaxMessage toDaxMessage(String messageType, Object daxDataEntry ){
        DaxPreamble preamble = new DaxPreamble();
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        body.nextBlock();
        DaxTrailer trailer = new DaxTrailer();
        try {
            for (Field field : daxDataEntry.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(DaxpTag.class)) {
                    DaxpTag daxp = field.getAnnotation(DaxpTag.class);
                    field.setAccessible(true);
                    if (field.get(daxDataEntry) != null) { //TODO For String check is empty
                        body.putPair(new DaxPair<>(daxp.tag(), field.get(daxDataEntry)));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new DaxMessage(head,body,trailer);
//        return new DaxMessage(preamble,head,body,trailer);

    }

}
