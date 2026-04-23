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

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxTagParser;

public class DaxTagCodec {
    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxTagParser tagParser;
    public DaxTagCodec(DaxConfig config,
                       DaxContextMapper contextMapper,
                       DaxTagParser tagParser
    ){
      this.config = config;
      this.contextMapper = contextMapper;
      this.tagParser = tagParser;
    }

    public String encode( DaxTag tag){
        if(tag.getContextId() == DaxCoreConstants.DAXP_CONTEXT_ID){
            return  DaxCoreConstants.DAXP_CONTEXT_TAG_PREFIX+
                    DaxCoreConstants.CONTEXT_TAG_SEPARATOR + tag.getTagId();
        }

        if(tag.getContextId() != config.getAppContextId())
        {
            return  contextMapper.getReference(tag.getContextId()) +
                    DaxCoreConstants.CONTEXT_TAG_SEPARATOR + tag.getTagId();
        }
        return String.valueOf(tag.getTagId());
    }

    public DaxTag decode(
            String value,
            String context,
            int tagId
    ){
        DaxTag tag;
        int contextId = context.isBlank() ?
                config.getAppContextId():
                contextMapper.getReferenceId(context);

        if (!value.isBlank()){
            tag = tagParser.parseDaxTag(value,config.getAppContextId());
        }
        else {
            tag = DaxTag.of(contextId ,tagId);
        }

        return tag;
    }

}
