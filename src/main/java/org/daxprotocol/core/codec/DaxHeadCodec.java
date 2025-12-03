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
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.pair.DaxStringPair;

import java.util.List;
import java.util.Optional;

import static org.daxprotocol.core.codec.DaxTagConst.*;
//TODO add logger
public class DaxHeadCodec implements DaxCodec<DaxHead> {

    DaxpConfig config;
    DaxPairCodec pairCodec;

    public DaxHeadCodec(DaxpConfig config) {
        this.config = config;
        this.pairCodec = new DaxPairCodec(config);
    }

    public String encode(DaxHead head,int blockCount) {

        StringBuilder sb = new StringBuilder();

        pairCodec.encode(sb,MSG_TYPE,head.getMsgType());

//        if (head.getContextId() != config.getApplicationContextId()
//            && head.getContextId() != 0 ) //TODO add to const
//        {
        int msgContextId = head.getContextId()!= -1 ? head.getContextId():
                config.getApplicationContextId();
            pairCodec.encode(sb, MSG_CONTEXT, String.valueOf(msgContextId));
//            pairCodec.encode(sb, MSG_CONTEXT, String.valueOf(head.getContextId()));
//        }

        if (blockCount>1) {
            pairCodec.encode(sb, MSG_BLOCK_COUNT, String.valueOf(blockCount));
        }

        return sb.toString();
    }

    @Override public String encode(DaxHead message) {

        StringBuilder sb = new StringBuilder();
        pairCodec.encode(sb,MSG_TYPE,message.getMsgType());
        return sb.toString();
    }

    @Override public DaxHead decode(String wire) {
        return null;
    }

    public static DaxHead createHead(List<DaxStringPair> listOfPair) {
        String msgType = listOfPair.get(0).getValue();
        DaxHead head = new DaxHead(msgType);

        Optional<DaxStringPair> optBlockCount = listOfPair.stream()
                .filter(p -> p.getTag().equals(DaxTagConst.MSG_BLOCK_COUNT) )
                .findFirst();

        optBlockCount.ifPresent(pair -> head.setBlockCount(Integer.parseInt(pair.getValue())));

        return head;
    }

}
