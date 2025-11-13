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
package org.daxprotocol.core.codec;

import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.daxprotocol.core.model.trailer.DaxTrailerCodec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaxMessageCodec implements DaxCodec<DaxMessage>{
    DaxPreambleCodec preambleCodec = new DaxPreambleCodec();
    DaxHeadCodec headCodec = new DaxHeadCodec();
    DaxBodyCodec bodyCodec = new DaxBodyCodec();
    DaxTrailerCodec trailerCodec = new DaxTrailerCodec();

    @Override public String encode(DaxMessage message) {
        StringBuilder sb = new StringBuilder();
        DaxPreamble preamble = new DaxPreamble();
        sb.append(preambleCodec.encode(preamble))
          .append(headCodec.encode(message.getHead(), message.getBody().getBlockCount()))
          .append(bodyCodec.encode(message.getBody()))
          .append(trailerCodec.encode(message.getTrailer()));

        return sb.toString();
    }

    public static Map<String, String> parseMap(String msgPart, Pattern pairPattern  ) {
        Map<String, String> map = new HashMap<>();

        Matcher m = pairPattern.matcher(msgPart);

        while (m.find()) {
            map.put(m.group(1), m.group(2));
        }
        return map;
    }

    //    public List<DaxMessage> decodeFisrt(String msgStr, int nfirs) {
    //    }


    public List<DaxMessage> decodeAll(String msgStr) {
        return decodeAll(msgStr,null);
    }

   private DaxMessage createMsg(List<DaxStringPair> listOfPair){
       DaxHead head;
       DaxBody body ;

       head = DaxHeadCodec.createHead(listOfPair);
       body = DaxBodyCodec.createBody(head.getBlockCount(), listOfPair) ;
       //todo trailer with check

       return new DaxMessage(head,body,null);
   }

   private List<List<DaxStringPair>> splitMessages(List<DaxStringPair> allPairs) {
       List<List<DaxStringPair>> result = new ArrayList<>();
       List<DaxStringPair> current = new ArrayList<>();

       for (DaxStringPair pair : allPairs) {
           if (pair.getTag() == DaxTag.MSG_TYPE) {
               // start of a new message
               if (!current.isEmpty()) {
                   // if previous message wasn't closed correctly, save it anyway
                   result.add(new ArrayList<>(current));
                   current.clear();
               }
           }

           current.add(pair);

           if (pair.getTag() == DaxTag.CHECKSUM) {
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


    public List<DaxMessage> decodeAll(String msgStr, DaxDictionary dic) {
        List<DaxMessage> messageList = new ArrayList<>();
        DaxPreamble preamble = preambleCodec.decode(msgStr);

        int fistMsgIdx = msgStr.indexOf(String.valueOf(DaxTag.MSG_TYPE)+DaxCodecSymbols.EQUAL);

        String msgPairsStr = msgStr.substring(fistMsgIdx);

        List<DaxStringPair> listOfPair = DaxDecodeService.parsePairs(msgPairsStr, preamble.getPairPattern());

        List<List<DaxStringPair>> msgPairList =  splitMessages(listOfPair);

        msgPairList.forEach(pairs ->
            messageList.add(createMsg(pairs))
        );

        return messageList;
    }

    public DaxMessage decode(String msg, DaxDictionary dic) {
        return decodeAll(msg,dic).get(0);
    }

    @Override
    public DaxMessage decode(String msg) {
        return decodeAll(msg).get(0);
    }


    public  int getMessageCount(String msgStr){
        DaxPreamble preamble = preambleCodec.decode(msgStr);
      return preamble.getMsgCnt() ;
    }

}
