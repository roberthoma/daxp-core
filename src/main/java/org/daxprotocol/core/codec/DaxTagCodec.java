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

import org.daxprotocol.core.annotation.*;
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.exceptions.DaxAnnotationException;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxTagParser;

import java.lang.reflect.Field;

/**
 * Codec responsible for encoding {@link DaxTag} instances into string representations
 * and decoding various protocol annotations and reflective fields into {@link DaxTag} objects.
 */
public class DaxTagCodec {

    /** Core configuration settings for the DAX protocol. */
    DaxConfig config;

    /** Mapper used to resolve namespace identifiers and references. */
    DaxNamespaceMapper namespaceMapper;

    /** Parser responsible for converting string values into {@link DaxTag} models. */
    DaxTagParser tagParser;

    /**
     * Constructs a new {@code DaxTagCodec} with the required dependencies.
     *
     * @param config          the global DAX configuration
     * @param namespaceMapper the namespace reference mapper
     * @param tagParser       the parser for string-based DAX tags
     */
    public DaxTagCodec(DaxConfig config,
            DaxNamespaceMapper namespaceMapper,
            DaxTagParser tagParser
    ){
        this.config = config;
        this.namespaceMapper = namespaceMapper;
        this.tagParser = tagParser;
    }

    /**
     * Encodes a {@link DaxTag} into its formatted string representation based on its namespace.
     *
     * @param tag the {@link DaxTag} to encode
     * @return the string representation of the tag, formatted with namespace prefixes if applicable
     */
    public String encode(DaxTag tag){
        if(tag.getNamespaceId() == DaxCoreConstants.DAXP_NAMESPACE_ID){
            return  DaxCoreConstants.DAXP_NAMESPACE_TAG_PREFIX +
                    DaxCoreConstants.NAMESPACE_TAG_SEPARATOR + tag.getTagId();
        }

        if(tag.getNamespaceId() != config.getAppNamespaceId())
        {
            return  namespaceMapper.getReference(tag.getNamespaceId()) +
                    DaxCoreConstants.NAMESPACE_TAG_SEPARATOR + tag.getTagId();
        }
        return String.valueOf(tag.getTagId());
    }

    /**
     * Decodes a {@link DaxpField} annotation into a {@link DaxTag}.
     *
     * @param ann the field annotation containing tag information
     * @return the decoded {@link DaxTag}
     */
    public DaxTag decode(DaxpField ann){
        return decode(ann.value(), ann.namespace(), ann.tagId());
    }

    /**
     * Decodes a {@link DaxpCollection} annotation into a {@link DaxTag}.
     *
     * @param ann the collection annotation containing tag information
     * @return the decoded {@link DaxTag}
     */
    public DaxTag decode(DaxpCollection ann){
        return decode(ann.value(), ann.namespace(), ann.tagId());
    }

    /**
     * Decodes a {@link DaxpValue} annotation into a {@link DaxTag}.
     *
     * @param ann the value annotation containing tag information
     * @return the decoded {@link DaxTag}
     */
    public DaxTag decode(DaxpValue ann){
        return decode(ann.value(), ann.namespace(), ann.tagId());
    }

    /**
     * Decodes a {@link DaxpEntity} annotation into a {@link DaxTag}.
     *
     * @param ann the entity annotation containing tag information
     * @return the decoded {@link DaxTag}
     */
    public DaxTag decode(DaxpEntity ann){
        return decode(ann.value(), ann.namespace(), ann.tagId());
    }

    /**
     * Decodes a field annotated with {@link DaxpTag} by extracting its static value or ID reflectively.
     *
     * @param tagAnn the tag annotation on the field
     * @param field  the target reflective field
     * @return the decoded {@link DaxTag}
     * @throws DaxAnnotationException if the field value cannot be reflectively read
     */
    public DaxTag decode(DaxpTag tagAnn, Field field) {

        String value = "";
        int tagId = -1;

        try {
            if (field.getType() == String.class) {
                value = (String)(field.get(null));
            }
            else {
                tagId = field.getInt(null);
            }
        }
        catch (Exception e){
            throw new DaxAnnotationException("RegisterDaxpTagException " + field.getName());
        }

        return decode(value, tagAnn.namespace(), tagId);
    }

    /**
     * Internal helper method to resolve namespace IDs and construct a {@link DaxTag}
     * from raw annotation components.
     *
     * @param value     the raw string tag representation
     * @param namespace the target namespace string
     * @param tagId     the integer tag identifier
     * @return the constructed {@link DaxTag}
     */
    private DaxTag decode(
            String value,
            String namespace,
            int tagId
    ){
        DaxTag tag;
        int namespaceId = namespace.isBlank() ?
                config.getAppNamespaceId():
                namespaceMapper.getReferenceId(namespace);

        if (!value.isBlank()){
            tag = tagParser.parseDaxTag(value, config.getAppNamespaceId());
        }
        else {
            tag = DaxTag.of(namespaceId, tagId);
        }

        return tag;
    }

    /**
     * Decodes a string-based tag representation within a specific namespace ID context.
     *
     * @param value       the string tag value to parse
     * @param namespaceId the target namespace identifier
     * @return the parsed {@link DaxTag}
     */
    public DaxTag decode(
            String value,
            int namespaceId
    ){
        return tagParser.parseDaxTag(value, namespaceId);
    }
}