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
package org.daxprotocol.core.factory;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.codec.*;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxMessageFactory {

    private static final Logger logger = LoggerFactory.getLogger(DaxMessageFactory.class);

    private final DaxTagCodec tagCodec;
    private final DaxHeadCodec headCodec;
    private final DaxBodyCodec bodyCodec;
    private final DaxTrailerCodec trailerCodec;
    private final DaxDictionaryMessageFactory dicMessageFactory;
    private final DaxFactoryObjectService objectService;



    public DaxMessageFactory(
            DaxDictionary dictionary,
            DaxTagCodec tagCodec,
            DaxHeadCodec headCodec,
            DaxBodyCodec bodyCodec,
            DaxTrailerCodec trailerCodec,
            DaxValueCodec valueCodec,
            DaxDataTypeCodec dataTypeCodec
    ) {
        this.tagCodec = tagCodec;
        this.headCodec = headCodec;
        this.bodyCodec = bodyCodec;
        this.trailerCodec = trailerCodec;
        this.dicMessageFactory =  new DaxDictionaryMessageFactory(tagCodec, dictionary);
        this.objectService = new DaxFactoryObjectService(tagCodec, dataTypeCodec, valueCodec);
    }

    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxCoreMessages.DIC_REQ);
    }

    public DaxMessage dictionaryToMsg() {
        return dicMessageFactory.dictionaryToMsg();
    }

    public DaxMessage toDaxMessage(String messageType, Object daxDataEntry) {
        return objectService.toDaxMessageFromObject(messageType, daxDataEntry, null);
    }

    @SuppressWarnings("unchecked")
    public DaxMessage toDaxMessage(DaxMessage reqMsg, String respMsgType, Object daxDataEntry) {
        Set<DaxTag> tagSet = null;

        if (reqMsg.containsField(0, REQ_FIELD_LIST))
        {
            tagSet = (Set<DaxTag>) reqMsg.get(0, REQ_FIELD_LIST).getValue();
        }

        return objectService.toDaxMessageFromObject(respMsgType, daxDataEntry, tagSet);
    }



    public DaxMessage toDaxMessageFromPairMap(String messageType, Map<DaxTag, DaxPair<?>> pairMap) {
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        body.nextBlock();
        pairMap.values().forEach(body::putPair);

        return new DaxMessage(head, body, trailer);
    }

    public DaxMessage toDaxMessageFromListOfPairMap(String messageType, List<Map<DaxTag, DaxPair<?>>> pairMapList) {
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        pairMapList.forEach(pairMap -> {
            body.nextBlock();
            pairMap.values().forEach(body::putPair);
        });

        return new DaxMessage(head, body, trailer);
    }

    public DaxMessage errorResourceNotFound() {
        DaxMessage message = new DaxMessage(DaxCoreMessages.ERR_RES);
        message.getBody().nextBlock();
        message.getBody().putPair(new DaxPairString(ERR_DESCRIPTION, "Resource not found"));
        return message;
    }

    public DaxMessage okMessage() {
        return new DaxMessage(DaxCoreMessages.OK_RES);
    }

    public DaxMessage errorInvalidMessageType() {
        DaxMessage message = new DaxMessage(DaxCoreMessages.ERR_RES);
        message.getBody().nextBlock(DaxBlockType.BLOCK_INSTANCE);
        message.getBody().putPair(new DaxPairString(ERR_DESCRIPTION, "Invalid Message Type"));
        return message;
    }

    public DaxMessage daxExceptionMessage(DaxException daxException) {
        DaxMessage message = new DaxMessage(DaxCoreMessages.ERR_RES);
        message.getBody().nextBlock(DaxBlockType.BLOCK_INSTANCE);
        message.getBody().putPair(new DaxPairString(ERR_FIELD_ID, daxException.getDaxErrorCode()));
        message.getBody().putPair(new DaxPairString(ERR_DESCRIPTION, daxException.getMessage()));
        return message;
    }

    public DaxMessage exceptionMessage(Exception exception) {
        DaxMessage message = new DaxMessage(DaxCoreMessages.ERR_RES);
        message.getBody().nextBlock(DaxBlockType.BLOCK_INSTANCE);
        message.getBody().putPair(new DaxPairString(ERR_DESCRIPTION, exception.getMessage()));
        return message;
    }


    public DaxMessage createMsg(String messageType, List<DaxPair<?>> listOfPair) {
        DaxHead head = headCodec.createHead(listOfPair);
        DaxBody body = bodyCodec.createBody(head.getBlockCount(), listOfPair);
        DaxTrailer trailer = trailerCodec.createTrailer(listOfPair);
        return new DaxMessage(head, body, trailer);
    }

    public DaxMessage createMsg(List<DaxPair<?>> listOfPair) {
        DaxHead head = headCodec.createHead(listOfPair);
        DaxBody body = bodyCodec.createBody(0, listOfPair);
        DaxTrailer trailer = trailerCodec.createTrailer(listOfPair);
        return new DaxMessage(head, body, trailer);
    }

    public DaxMessage aboutToMsg() {
        DaxMessage message = new DaxMessage(DaxCoreMessages.ABOUT_RESP);
        message.getBody().nextBlock(DaxBlockType.BLOCK_INSTANCE);
        message.getBody().putPair(new DaxPairString(ENTRY_DESCRIPTION, DaxCoreConstants.DAXP_NAMESPACE_DESCRIPTION));
        return message;
    }

    public DaxMessage logMessage(int level, String log) {
        DaxMessage message = new DaxMessage(DaxCoreMessages.LOG);
        message.getBody().nextBlock(DaxBlockType.BLOCK_INSTANCE);
        message.getBody().putPair(new DaxPairString(LOG_LEVEL, "TRACE"));
        message.getBody().putPair(new DaxPairString(ENTRY_DESCRIPTION, log));
        return message;
    }



}