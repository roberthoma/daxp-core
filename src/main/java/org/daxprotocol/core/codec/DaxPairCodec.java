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

import static org.daxprotocol.core.codec.DaxCodecSymbols.EQUAL;
import static org.daxprotocol.core.codec.DaxCodecSymbols.PAIR_SEPARATOR;

public class DaxPairCodec implements DaxCodec<DaxPair<?>> {

    public static String encode(StringBuilder sb,int tag, String value ) {
        sb.append(tag)
                .append(EQUAL)
                .append(value)
                .append(PAIR_SEPARATOR);
        return sb.toString() ;
    }

    public static String encode(StringBuilder sb,String tag, String value ) {
        sb.append(tag)
                .append(EQUAL)
                .append(value)
                .append(PAIR_SEPARATOR);
        return sb.toString() ;
    }

    @Override
    public String encode(DaxPair<?> pair) {
        StringBuilder sb = new StringBuilder();
        sb.append(pair.tag)
                .append(EQUAL)
                .append(pair.getStrValue())
                .append(PAIR_SEPARATOR);
        return sb.toString() ;
    }

    @Override
    public DaxPair<?> decode(String wire) {
        return null;
    }
}
