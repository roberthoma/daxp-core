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

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.daxprotocol.core.model.trailer.DaxTrailerCodec;
import org.daxprotocol.core.parser.DaxPatternFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//public class DaxMessageCodec implements DaxCodec<DaxMessage>{
public class DaxMessageCodec {
    DaxpConfig       config;
    DaxPairCodec     pairCodec;
    DaxPreambleCodec preambleCodec;
    DaxHeadCodec     headCodec;
    DaxBodyCodec     bodyCodec;
    DaxTrailerCodec  trailerCodec;

    public DaxMessageCodec(
            DaxpConfig config,
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
        public  int calculateChecksum(String input) {
            byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);

            int sum = 0;

            for (byte b : bytes) {
//                if (b != DaxpConfig.PAIR_SEPARATOR
//                && b != '\n' // TODO move to DaxpConfig create array abandoned char
//                )

//                {   // ignore pipe
                    sum += b;
//                }
            }

            return sum % 256;
        }


        //@Override
    public String encode(DaxMessage message) {
        StringBuilder sb = new StringBuilder();
        DaxPreamble preamble = new DaxPreamble();
        preamble.setEncoding(config.getDefaultEncoding());
        preamble.setMsgContextId(config.getAppContextId());

        StringBuilder msgSb = new StringBuilder();

        msgSb.append(headCodec.encode(message.getHead(), message.getBody().getBlockCount()))
                .append(bodyCodec.encode(message.getBody()));

        DaxTrailer trailer = new DaxTrailer();

        trailer.setChecksum(calculateChecksum(msgSb.toString()));

        sb.append(preambleCodec.encode(preamble))
                .append(msgSb)
                .append(trailerCodec.encode(trailer));

//        sb.append(preambleCodec.encode(preamble))
//          .append(headCodec.encode(message.getHead(), message.getBody().getBlockCount()))
//          .append(bodyCodec.encode(message.getBody()))
//          .append(trailerCodec.encode(message.getTrailer()));

        return sb.toString();
    }

   private DaxMessage createMsg(List<DaxStringPair> listOfPair){
       DaxHead head;
       DaxBody body;
       DaxTrailer trailer;

//       head = DaxHeadCodec.createHead(listOfPair);
       head = headCodec.createHead(listOfPair);
       body = bodyCodec.createBody(head.getBlockCount(), listOfPair) ;
       trailer = trailerCodec.createTrailer(listOfPair);
       //todo trailer with check

       return new DaxMessage(head,body,trailer);
   }

   private List<List<DaxStringPair>> splitMessages(List<DaxStringPair> allPairs) {
       List<List<DaxStringPair>> result = new ArrayList<>();
       List<DaxStringPair> current = new ArrayList<>();

       for (DaxStringPair pair : allPairs) {
           if (pair.getTag().equals(DaxTagConst.MSG_TYPE)) {
               // start of a new message
               if (!current.isEmpty()) {
                   // if previous message wasn't closed correctly, save it anyway
                   result.add(new ArrayList<>(current));
                   current.clear();
               }
           }

           current.add(pair);

           if (pair.getTag().equals(DaxTagConst.CHECKSUM)) {
               // end of current message
               result.add(new ArrayList<>(current));
               current.clear();
           }
       }

       // handle last incomplete message (optional)
       if (!current.isEmpty()) {
           result.add(current);
       }

       return result;
   }

//TODO Add validation after creation of DaxMessage. for example message with blocks, without BLOCK_TYPE !!!

    public List<DaxMessage> decodeAll(String msgStr) {
        List<DaxMessage> messageList = new ArrayList<>();
        DaxPreamble preamble = preambleCodec.decode(msgStr);

        int fistMsgIdx = msgStr.indexOf(String.valueOf(DaxTagConst.MSG_TYPE)+ DaxpConfig.EQUAL);

        String msgPairsStr = msgStr.substring(fistMsgIdx);

        List<DaxStringPair> listOfPair = pairCodec.
                          parsePairs(msgPairsStr,
                                     DaxPatternFactory.compileMessagePairPattern(preamble.getMsgPairSeparator()),
                                     preamble.getMsgContextId());

        List<List<DaxStringPair>> msgPairList =  splitMessages(listOfPair);

        msgPairList.forEach(pairs ->
            messageList.add(createMsg(pairs))
        );

        return messageList;
    }

   // @Override
    public DaxMessage decode(String msg) {
        return decodeAll(msg).get(0);
    }


    public  int getMessageCount(String msgStr){
        DaxPreamble preamble = preambleCodec.decode(msgStr);
      return preamble.getMsgCnt() ;
    }

}
