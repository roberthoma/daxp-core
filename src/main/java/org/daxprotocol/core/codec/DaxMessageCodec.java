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
import org.daxprotocol.core.model.body.DaxBodyCodec;
import org.daxprotocol.core.model.head.DaxHeadCodec;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.daxprotocol.core.model.trailer.DaxTrailerCodec;

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

    @Override public DaxMessage decode(String msg) {
        return DaxDecodeService.parse(msg);
    }
}
