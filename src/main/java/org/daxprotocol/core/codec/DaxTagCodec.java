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
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxParser;

public class DaxTagCodec {
    DaxConfig config;
    DaxStringReferenceMapper contextMapper;
    DaxParser parserService;
    public DaxTagCodec(DaxConfig config,
                       DaxStringReferenceMapper contextMapper,
                       DaxParser parserService){
      this.config = config;
      this.contextMapper = contextMapper;
      this.parserService = parserService;
    }

    public String encode( DaxTag tag){

        if(tag.getContextId() != DaxConfig.DAXP_CONTEXT_ID &&
                (tag.getContextId() != config.getAppContextId()
           || tag.getTagId() <= DaxConfig.DAXP_MAX_TAG_ID)
        )
        {
            return  contextMapper.getReference(tag.getContextId()) +
                    DaxConfig.CONTEXT_TAG_SEPARATOR + tag.getTagId();
        }


        return String.valueOf(tag.getTagId());

    }

    public DaxTag decode(String tagStr, int msgContextId){
        return parserService.parseDaxTag(tagStr, msgContextId);
    }

}
