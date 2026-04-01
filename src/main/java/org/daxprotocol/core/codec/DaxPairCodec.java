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

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPairCodec {
    DaxConfig config;
    DaxStringReferenceMapper contextMapper;
    DaxTagCodec tagCodec;
    public DaxPairCodec(DaxConfig config, DaxStringReferenceMapper contextMapper, DaxTagCodec tagCodec) {
        this.config = config;
        this.contextMapper = contextMapper;
        this.tagCodec = tagCodec;
    }

    public   String encode(StringBuilder sb, DaxTag tag, String value ) {
        if (value.isBlank()){
            return sb.toString();
        }
        sb.append(tagCodec.encode(tag))
                .append(DaxConfig.EQUAL)
                .append(value)
                .append(DaxConfig.PAIR_SEPARATOR);
        return sb.toString() ;
    }

    public   String encode(StringBuilder sb, DaxTag tag, DaxPair<?> pair ) {
        String value;
        if (pair.getValue() instanceof DaxTag){
            value = tagCodec.encode((DaxTag)pair.getValue());
        }
        else {
            value = pair.getStrValue();
        }
        return encode(sb,tag,value );
    }
}
