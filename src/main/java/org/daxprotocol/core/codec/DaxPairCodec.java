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
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaxPairCodec {
    DaxpConfig config;
    DaxStringReferenceMapper contextMapper;
    public DaxPairCodec(DaxpConfig config, DaxStringReferenceMapper contextMapper) {
        this.config = config;
        this.contextMapper = contextMapper;
    }

    //TODO Rebuild and use DaxProtocolRules
    private  String encode(StringBuilder sb, int contextId ,int tagId, String value ) {
        if (value.isBlank()){
            return sb.toString();
        }
        if(contextId!= DaxpConfig.DAXP_CONTEXT_ID &&
           contextId!= config.getAppContextId() )
        {
            sb.append(contextMapper.getReference(contextId))
              .append(DaxpConfig.CONTEXT_TAG_SEPARATOR);
        }


        sb.append(tagId)
                .append(DaxpConfig.EQUAL)
                .append(value)
                .append(DaxpConfig.PAIR_SEPARATOR);
        return sb.toString() ;
    }

    public List<DaxStringPair> parsePairs(String msg, Pattern pairPattern, int  msgContextId) {
        List<DaxStringPair> list = new ArrayList<>();
        Matcher m = pairPattern.matcher(msg);
        while (m.find()) {
            String contextStr = m.group(1);
            int tagId = Integer.parseInt(m.group(2));
            int contextId;
            if (contextStr == null) {
                if (tagId < DaxpConfig.DAXP_MAX_TAG_ID) {
                    contextId = DaxpConfig.DAXP_CONTEXT_ID;
                } else {
                    contextId = msgContextId;
                }
            } else {
                contextId = contextMapper.getReferenceId(contextStr);
            }

            list.add(new DaxStringPair(new DaxTag(contextId, tagId), m.group(3)));
        }
        return list;
    }

    public  String encode(StringBuilder sb, DaxTag tag, String value ) {
        return  encode(sb, tag.getContextId() ,tag.getTagId(), value );
    }

    public  String encode(StringBuilder sb, int tagId, String value ) {
        return  encode(sb, 0 ,tagId, value );

    }

//    @Override public String encode(DaxPair<?> object) {
//        return "";
//    }
//
//    @Override
//    public DaxPair<?> decode(String wire) {
//        return null;
//    }
}
