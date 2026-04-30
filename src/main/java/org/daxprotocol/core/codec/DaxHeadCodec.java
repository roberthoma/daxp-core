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

import org.daxprotocol.core.exceptions.DaxTagParserException;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.pair.DaxPair;


import java.util.List;
import java.util.Optional;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxHeadCodec{

    DaxPairCodec pairCodec;

    public DaxHeadCodec(DaxPairCodec pairCodec) {
        this.pairCodec = pairCodec;
    }

    public String encode(DaxHead head,int blockCount, char pairSeparator) {

        StringBuilder sb = new StringBuilder();

        pairCodec.encode(sb,MSG_TYPE,head.getMsgType(), pairSeparator);


        if (blockCount>1) {
            pairCodec.encode(sb, MSG_BLOCK_QUANTITY, String.valueOf(blockCount), pairSeparator);
        }

        return sb.toString();
    }

//    @Override
//    public String encode(DaxHead message) {
//
//        StringBuilder sb = new StringBuilder();
//        pairCodec.encode(sb,MSG_TYPE,message.getMsgType());
//        return sb.toString();
//    }



    public  DaxHead createHead(List<DaxPair<?>> listOfPair) {
        if(!listOfPair.get(0).getTag().equals(MSG_TYPE)){
              throw new DaxTagParserException("First pair is not MESSAGE_TYPE");

        }

        String msgType = listOfPair.get(0).getStrValue();



        DaxHead head = new DaxHead(msgType);

        Optional<DaxPair<?>> optBlockCount = listOfPair.stream()
                .filter(p -> p.getTag().equals(MSG_BLOCK_QUANTITY) )
                .findFirst();

        optBlockCount.ifPresent(pair -> head.setBlockCount(pair.getIntegerValue()));

        return head;
    }
    public  DaxHead createHead(String msgType,List<DaxPair<?>> listOfPair) {
        DaxHead head = new DaxHead(msgType);

        Optional<DaxPair<?>> optBlockCount = listOfPair.stream()
                .filter(p -> p.getTag().equals(MSG_BLOCK_QUANTITY) )
                .findFirst();

        optBlockCount.ifPresent(pair -> head.setBlockCount(pair.getIntegerValue()));

        return head;
    }

}
