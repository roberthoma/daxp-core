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
package org.daxprotocol.core.codec;

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.daxprotocol.core.tool.DaxChecksumService;

import java.util.List;


public class DaxMessageCodec {
    DaxConfig        config;
    DaxPairCodec     pairCodec;
    DaxHeadCodec     headCodec;
    DaxBodyCodec     bodyCodec;
    DaxTrailerCodec  trailerCodec;

    public DaxMessageCodec(
            DaxConfig config,
            DaxPairCodec pairCodec,
            DaxHeadCodec headCodec,
            DaxBodyCodec bodyCodec,
            DaxTrailerCodec trailerCodec
            ) {
      this.pairCodec = pairCodec;
      this.headCodec = headCodec;
      this.bodyCodec = bodyCodec;
      this.trailerCodec = trailerCodec;
      this.config = config;


    }

    public String encodeAll(List<DaxMessage> messageList) {
        throw new RuntimeException("NOT implemented jet encodeAll");
    }

    public String encode(DaxMessage message, DaxPreamble preamble) {

        StringBuilder msgSb = new StringBuilder();
        char pS = preamble.getPairSeparator();

        msgSb.append(headCodec.encode(message.getHead(), message.getBody().getBlocksCount(), pS))
             .append(bodyCodec.encode(message.getBody(), pS));

        DaxTrailer trailer = new DaxTrailer();

        trailer.setChecksum(DaxChecksumService.calculateChecksum(msgSb.toString()));
        msgSb.append(trailerCodec.encode(trailer,pS));

        //TODO create statistics counter for example avg message size
        //TODO  System.out.println("TODO Counter statistics message length = "+sb.length());

        return msgSb.toString();
    }


//TODO Add validation after creation of DaxMessage. for example message with blocks, without BLOCK_TYPE !!!

}
