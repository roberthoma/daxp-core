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

import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.trailer.DaxTrailer;

import java.util.List;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxTrailerCodec {
    DaxPairCodec pairCodec;

    public DaxTrailerCodec (DaxPairCodec pairCodec) {
        this.pairCodec = pairCodec;
    }

    public String encode(DaxTrailer trailer, char pairSeparator) {
        StringBuilder sb = new StringBuilder();
        pairCodec.encode(sb,CHECKSUM,String.valueOf(trailer.getChecksum()),pairSeparator);
        return sb.toString();
    }


    public DaxTrailer createTrailer(List<DaxPair<?>> listOfPair) {
        DaxTrailer trailer = new DaxTrailer();
        for(DaxPair<?> pair : listOfPair) {
            if (pair.getTag().equals(DaxCoreTags.CHECKSUM)) {
                trailer.setChecksum(pair.getIntegerValue());
                break;
            }
        }
       return trailer;
    }
}
