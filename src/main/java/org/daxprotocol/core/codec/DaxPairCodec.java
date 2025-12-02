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
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;

import static org.daxprotocol.core.codec.DaxCodecSymbol.*;

public class DaxPairCodec implements DaxCodec<DaxPair<?>> {
    DaxpConfig config;

    public DaxPairCodec(DaxpConfig config) {
        this.config = config;
    }

    private  String encode(StringBuilder sb, int contextId ,int tagId, String value ) {
        if (value.isBlank()){
            return sb.toString();
        }
        if(contextId!=0){ //TODO add sys context
            sb.append(contextId)
              .append(CONTEXT_TAG_SEPARATOR);
        }


        sb.append(tagId)
                .append(EQUAL)
                .append(value)
                .append(PAIR_SEPARATOR);
        return sb.toString() ;
    }

    public  String encode(StringBuilder sb, DaxTag tag, String value ) {
        return  encode(sb, tag.getContextId() ,tag.getTagId(), value );
    }

    public  String encode(StringBuilder sb, int tagId, String value ) {
        return  encode(sb, 0 ,tagId, value );

    }

    @Override public String encode(DaxPair<?> object) {
        return "";
    }

    @Override
    public DaxPair<?> decode(String wire) {
        return null;
    }
}
