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

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.codec.*;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.daxprotocol.core.dictionary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxMessageFactory {
    private static final Logger logger = LoggerFactory.getLogger(DaxMessageFactory.class);
    DaxConfig config;
    DaxTagCodec tagCodec;
    DaxMessageCodec messageCodec;
    DaxHeadCodec     headCodec;
    DaxBodyCodec     bodyCodec;
    DaxTrailerCodec  trailerCodec;
    DaxDictionary dictionary;
//    DaxTagParser tagParser;
    DaxDataTypeCodec dataTypeCodec;

    DaxDictionaryMessageFactory dicMessageFactory;
    DaxObjectMessageService objectMessageService;

    public DaxMessageFactory(DaxConfig config,
            DaxTagCodec tagCodec,
            DaxMessageCodec messageCodec,
            DaxHeadCodec headCodec,
            DaxBodyCodec bodyCodec,
            DaxTrailerCodec trailerCodec,
            DaxDictionary dictionary,
//            DaxTagParser tagParser,
            DaxDataTypeCodec dataTypeCodec

            ) {
        this.config = config;
        this.tagCodec = tagCodec;
        this.messageCodec = messageCodec;
        this.headCodec = headCodec;
        this.bodyCodec = bodyCodec;
        this.trailerCodec = trailerCodec;
        this.dictionary = dictionary;
//        this.tagParser = tagParser;
        this.dataTypeCodec = dataTypeCodec;
        this.dicMessageFactory = new DaxDictionaryMessageFactory(tagCodec,dictionary);
        this.objectMessageService = new DaxObjectMessageService(tagCodec, dataTypeCodec);

    }

    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxCoreMessages.DIC_REQ);
    }


    public DaxMessage dictionaryToMsg() {
        return dicMessageFactory.dictionaryToMsg();
    }


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

        if (reqFrame.getFirstMessage().containsField(0,REQ_FIELD_LIST)) {
            tagSet = (Set<DaxTag>) (reqFrame.getFirstMessage().get(0,REQ_FIELD_LIST).getValue());

            return  daxDataEntry instanceof List<?> ?
                    toDaxMessageFromList( messageType, (List<Object>) daxDataEntry , tagSet)
                    : toDaxMessageFromList( messageType, List.of(daxDataEntry), tagSet );

        }
        return  daxDataEntry instanceof List<?> ?
                  toDaxMessageFromList( messageType, (List<Object>) daxDataEntry , null)
                : toDaxMessageFromList( messageType, List.of(daxDataEntry), null );
    }

    //-----------------------------------------------

    private DaxMessage toDaxMessageFromList(String messageType, List<Object> daxDataEntry , Set<DaxTag> reqTagSet){
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        daxDataEntry.forEach(entry -> {

            if (entry.getClass().isAnnotationPresent(DaxpEntity.class)) {
                DaxpEntity entityAnn = entry.getClass().getAnnotation(DaxpEntity.class);
                DaxTag tag =  tagCodec.decode( entityAnn);
                body.nextBlock(DaxBlockType.BLOCK_INSTANCE);
                objectMessageService.objectToMsgBlock(body.getCurrentIdx(),tag, entry, body, reqTagSet, tag);
            }

        });

        return new DaxMessage(head,body,trailer);
    }

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
