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

import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.body.DaxBodyCodec;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.head.DaxHeadCodec;
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

        sb.append(preambleCodec.encode(message.getPreamble()))
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

    public List<DaxMessage> decodeAll(String msgStr) {
        List<DaxMessage> messageList = new ArrayList<>();
        DaxPreamble preamble;
        DaxHead head;
        DaxBody body ;
        Map<String, String> preambleMap = new HashMap<>();

        Pattern pairPattern = DaxPreambleCodec.getPairPattern(msgStr);

        String preamblePart = msgStr.split(String.valueOf(DaxTag.MSG_TYPE)+DaxCodecSymbols.EQUAL)[0];

        Matcher m = pairPattern.matcher(preamblePart);

        while (m.find()) {
            preambleMap.put(m.group(1), m.group(2));
        }

        preamble = DaxPreambleCodec.fromMap(preambleMap);

        List<DaxStringPair> listOfPair = DaxDecodeService.parsePairs(msgStr,pairPattern);

        head = DaxHeadCodec.createHead(listOfPair);

        body = head.getBlockCount() > 0 ?
                DaxBodyCodec.createBody(head.getBlockCount(), listOfPair) : null;

        messageList.add(new DaxMessage(preamble,head,body,null));

        return messageList;
    }

    @Override public DaxMessage decode(String msg) {


        return decodeAll(msg).get(0);

//        return DaxDecodeService.parseAndDecode(msg).get(0);
    }


    public int getMessageCount(String msg){
      return 1; //TODO fix it
    }

}
