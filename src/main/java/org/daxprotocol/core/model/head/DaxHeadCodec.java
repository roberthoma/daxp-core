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
package org.daxprotocol.core.model.head;

import org.daxprotocol.core.codec.DaxCodec;
import org.daxprotocol.core.codec.DaxPairCodec;
import org.daxprotocol.core.codec.DaxStringPair;
import org.daxprotocol.core.codec.DaxTag;

import java.util.List;
import java.util.Optional;

import static org.daxprotocol.core.codec.DaxTag.*;

public class DaxHeadCodec implements DaxCodec<DaxHead> {

    public String encode(DaxHead message,int blockCount) {

        StringBuilder sb = new StringBuilder();
        DaxPairCodec.encode(sb,MSG_TYPE,message.getMsgType());
        if (blockCount>1) {
            DaxPairCodec.encode(sb, MSG_BLOCK_COUNT, String.valueOf(blockCount));
        }

        return sb.toString();
    }

    @Override public String encode(DaxHead message) {

        StringBuilder sb = new StringBuilder();
        DaxPairCodec.encode(sb,MSG_TYPE,message.getMsgType());
        return sb.toString();
    }

    @Override public DaxHead decode(String wire) {
        return null;
    }

    public static DaxHead createHead(List<DaxStringPair> listOfPair) {
        String msgType = listOfPair.get(0).getValue();
        DaxHead head = new DaxHead(msgType);

        Optional<DaxStringPair> optBlockCount = listOfPair.stream()
                .filter(p -> p.getTag() == DaxTag.MSG_BLOCK_COUNT )
                .findFirst();

        optBlockCount.ifPresent(pair -> head.setBlockCount(Integer.parseInt(pair.getValue())));

        return head;
    }

}
