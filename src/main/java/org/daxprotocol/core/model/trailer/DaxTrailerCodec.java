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

package org.daxprotocol.core.model.trailer;

import org.daxprotocol.core.codec.DaxPairCodec;
import org.daxprotocol.core.config.DaxpConfig;

import static org.daxprotocol.core.codec.DaxTagConst.*;

//public class DaxTrailerCodec implements DaxCodec<DaxTrailer> {
public class DaxTrailerCodec {
    DaxPairCodec pairCodec;

    public DaxTrailerCodec (DaxPairCodec pairCodec) {
        this.pairCodec = pairCodec;
    }

    //@Override
    public String encode(DaxTrailer message) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n"); //TODO only for test profile
        sb.append(CHECKSUM).append(DaxpConfig.EQUAL)
                .append("123")
                .append(DaxpConfig.PAIR_SEPARATOR);

        return sb.toString();
    }

    //@Override
    public DaxTrailer decode(String wire) {
        return null;
    }
}
