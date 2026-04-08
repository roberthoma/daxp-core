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
package org.daxprotocol.core.codec;

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.trailer.DaxTrailer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//public class DaxMessageCodec implements DaxCodec<DaxMessage>{
public class DaxMessageCodec {
    DaxConfig config;
    DaxPairCodec     pairCodec;
    DaxPreambleCodec preambleCodec;
    DaxHeadCodec     headCodec;
    DaxBodyCodec     bodyCodec;
    DaxTrailerCodec  trailerCodec;

    public DaxMessageCodec(
            DaxConfig config,
            DaxPairCodec pairCodec,
            DaxPreambleCodec preambleCodec,
            DaxHeadCodec headCodec,
            DaxBodyCodec bodyCodec,
            DaxTrailerCodec trailerCodec
            ) {
      this.pairCodec = pairCodec;
      this.preambleCodec = preambleCodec;
      this.headCodec = headCodec;
      this.bodyCodec = bodyCodec;
      this.trailerCodec = trailerCodec;
      this.config = config;


    }

//TODO move to any service

    public String encodeAll(List<DaxMessage> messageList) {
    return " no messss !!!";
    }

    public String encode(DaxPreamble preamble  ,DaxMessage message) {
        return null;
    }
        //@Override
    /*
    public String encode(DaxMessage message) {
        StringBuilder sb = new StringBuilder();
        DaxPreamble preamble = new DaxPreamble();
        preamble.setEncoding(config.getDefaultEncoding());
        preamble.setMsgContextId(config.getAppContextId());

        StringBuilder msgSb = new StringBuilder();

        msgSb.append(headCodec.encode(message.getHead(), message.getBody().getBlockCount()))
             .append(bodyCodec.encode(message.getBody()));

        DaxTrailer trailer = new DaxTrailer();

        trailer.setChecksum(DaxCodecService.calculateChecksum(msgSb.toString()));

        sb.append(preambleCodec.encode(preamble))
                .append(msgSb)
                .append(trailerCodec.encode(trailer));

        //TODO create statistics counter
        //TODO  System.out.println("TODO Counter statistics message length = "+sb.length());

        return sb.toString();
    }
*/
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
    public DaxMessage createMsg(String msgType,List<DaxPair<?>> listOfPair){
        DaxHead head;
        DaxBody body;
        DaxTrailer trailer;

        head = headCodec.createHead(listOfPair);
        body = bodyCodec.createBody(head.getBlockCount(), listOfPair) ;
        trailer = trailerCodec.createTrailer(listOfPair);
        //todo trailer with check

        return new DaxMessage(head,body,trailer);
    }


//TODO Add validation after creation of DaxMessage. for example message with blocks, without BLOCK_TYPE !!!

    public DaxPreambleCodec getPreambleCodec(){
       return preambleCodec;
    }



//    public List<DaxMessage> decodeAll(String msgStr) {
//        List<DaxMessage> messageList = new ArrayList<>();
//
//
//        return messageList;
//    }
//
//    public DaxMessage decode(String msg) {
//        return decodeAll(msg).get(0);
//    }


//    public  int getMessageCount(String msgStr){
//        DaxPreamble preamble = preambleCodec.decode(msgStr);
//      return preamble.getMsgCnt() ;
//    }

//    public DaxPreamble decodePreamble(String body) {
//        return parser.parsePreamble(body);
//    }
//
//    public List<DaxMessage> decodeMessageList(String body) {
//        List<DaxMessage> messageList = new ArrayList<>();
//        List<DaxPair<?>> pairList = parser.parsePairList(body);
//
//        //>>>>> todo pairList.forEach(daxPair -> );
//
//        return messageList;
//
//    }
//
//    public DaxPreamble decodePreambleFromMap(Map<String, String> params) {
//
//        return null;
//    }
//
//    public List<DaxMessage> decodeMessageFromMap(Map<String, String> params) {
//        return null;
//    }
}
