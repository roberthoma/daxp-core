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

import org.daxprotocol.core.annotation.*;
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.exceptions.DaxAnnotationException;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxTagParser;

import java.lang.reflect.Field;

public class DaxTagCodec {
    DaxConfig config;
    DaxNamespaceMapper namespaceMapper;
    DaxTagParser tagParser;
    public DaxTagCodec(DaxConfig config,
                       DaxNamespaceMapper namespaceMapper,
                       DaxTagParser tagParser
    ){
      this.config = config;
      this.namespaceMapper = namespaceMapper;
      this.tagParser = tagParser;
    }

    public String encode( DaxTag tag){
        if(tag.getNamespaceId() == DaxCoreConstants.DAXP_NAMESPACE_ID){
            return  DaxCoreConstants.DAXP_NAMESPACE_TAG_PREFIX +
                    DaxCoreConstants.namespace_TAG_SEPARATOR + tag.getTagId();
        }

        if(tag.getNamespaceId() != config.getAppnamespaceId())
        {
            return  namespaceMapper.getReference(tag.getNamespaceId()) +
                    DaxCoreConstants.namespace_TAG_SEPARATOR + tag.getTagId();
        }
        return String.valueOf(tag.getTagId());
    }

//    public DaxTag decode(Annotation ann){
//        return decode(ann.value(),ann.namespace(), ann.tagId());
//    }
    public DaxTag decode(DaxpField ann){
        return decode(ann.value(),ann.namespace(), ann.tagId());
    }
    public DaxTag decode(DaxpCollection ann){
        return decode(ann.value(),ann.namespace(), ann.tagId());
    }
    public DaxTag decode(DaxpValue ann){
        return decode(ann.value(),ann.namespace(), ann.tagId());
    }
    public DaxTag decode(DaxpEntity ann){
        return decode(ann.value(),ann.namespace(), ann.tagId());
    }

    public DaxTag decode(DaxpTag tagAnn, Field field) {

        String value = "";
        int tagId = -1;

        ;
        try {
            if (field.getType() == String.class) {
                value  = (String)(field.get(null));
            }
            else {
                tagId = field.getInt(null);
            }
        }
        catch (Exception e){
            throw new DaxAnnotationException("RegisterDaxpTagException "+field.getName()) ;
        }

        return decode(value,tagAnn.namespace(), tagId);

    }

    public DaxTag decode(
            String value,
            String namespace,
            int tagId
    ){
        DaxTag tag;
        int namespaceId = namespace.isBlank() ?
                config.getAppnamespaceId():
                namespaceMapper.getReferenceId(namespace);

        if (!value.isBlank()){
            tag = tagParser.parseDaxTag(value,config.getAppnamespaceId());
        }
        else {
            tag = DaxTag.of(namespaceId ,tagId);
        }

        return tag;
    }

    public DaxTag decode(
            String value,
            int namespaceId
    ){
      return  tagParser.parseDaxTag(value,namespaceId);
    }
}
