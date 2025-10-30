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
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaxParser {
    private static final
    Pattern FIELD = Pattern.compile("(\\w+)"+DaxCodecSymbols.EQUAL+"([^"+DaxCodecSymbols.PAIR_SEPARATOR+"]*)");

    public static Map<String, String> parsePreamble(String msg) {
        Map<String, String> map = new HashMap<>();

        // Everything before tag 9=
        String preamblePart = msg.split(String.valueOf(DaxTag.MSG_TYPE)+DaxCodecSymbols.EQUAL)[0];
        Matcher m = FIELD.matcher(preamblePart);

        while (m.find()) {
            map.put(m.group(1), m.group(2));
        }
        return map;
    }

    public static List<DaxStringPair> parsePairs(String msg) {
        List<DaxStringPair> list = new ArrayList<>();
        Matcher m = FIELD.matcher(msg);
        while (m.find()) {
            String tagStr = m.group(1);
            if (tagStr.matches("\\d+")) {
                int tag = Integer.parseInt(tagStr);
                list.add(new DaxStringPair(tag, m.group(2)));
            }
        }
        return list;
    }

    private static DaxHead createHead(List<DaxStringPair> listOfPair) {
        String msgType = listOfPair.get(0).value;
        DaxHead head = new DaxHead(msgType);

        Optional<DaxStringPair> optBlockCount = listOfPair.stream()
                                                 .filter(p -> p.tag == DaxTag.MSG_BLOCK_COUNT )
                                                 .findFirst();

        optBlockCount.ifPresent(pair -> head.setBlockCount(Integer.parseInt(pair.getValue())));

        return head;
    }


    private static DaxBody createBody(int blockCount ,List<DaxStringPair> listOfPair){
        DaxBody body = new DaxBody();
        boolean isBlock = false;

        for(DaxPair<?> pair : listOfPair){
            if(pair.tag == DaxTag.CHECKSUM){
                break;
            }

            if(pair.tag == DaxTag.BLOCK_INDEX
                    && Integer.valueOf((String) pair.value)>0
            )
            {
                isBlock = true;
            }

            if (isBlock) {
                if (pair.tag == DaxTag.BLOCK_INDEX) {
                    body.nextBlock();
                }
                body.putPair(pair);
            }

        }

        return body;
    }


    public static DaxMessage parse(String msgStr){
        DaxMessage message;
        DaxPreamble preamble;
        DaxHead head;
        DaxBody body ;

        Map<String, String> preambleMap = parsePreamble(msgStr);
        preamble = DaxPreamble.fromMap(preambleMap);

        List<DaxStringPair> listOfPair = parsePairs(msgStr);

        head = createHead(listOfPair);

        body = head.getBlockCount() > 0 ?
             createBody(head.getBlockCount(), listOfPair) : null;

        message = new DaxMessage(preamble,head,body,null);


        return message;
    }

}
