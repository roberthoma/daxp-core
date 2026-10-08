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

package org.daxprotocol.core.factory;


import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.registries.AnnotatedField;
import org.daxprotocol.core.registries.AnnotatedMethod;
import org.daxprotocol.core.registries.ClassMetadata;
import org.daxprotocol.core.registries.DaxSemanticRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

import static org.daxprotocol.core.application.DaxCoreTags.*;
public class DaxBulkCollectionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DaxBulkCollectionBuilder.class);


    private final DaxTagCodec tagCodec;
    private final DaxDataTypeService dataTypeService;
    private final DaxSemanticRegistry semanticRegistry;
    ///----------------------------------------------------------------------------------------

    public DaxBulkCollectionBuilder(DaxSemanticRegistry semanticRegistry,DaxTagCodec tagCodec
            ,DaxDataTypeService dataTypeService

    ){
        this.tagCodec = tagCodec;
        this.dataTypeService = dataTypeService;
        this.semanticRegistry = semanticRegistry;
    }

    ///----------------------------------------------------------------------------------------
    private void writeHeaderFields(StringBuilder sb, ClassMetadata metadata) {
        boolean firstEntry = true;

        for (AnnotatedField f : metadata.fields()) {
            if (!firstEntry) sb.append(DaxCoreConstants.SEPARATOR_UNIT);
            sb.append(tagCodec.encode(f.tag()));
            firstEntry = false;
        }

        for (AnnotatedMethod m : metadata.methods()) {
            if (!firstEntry) sb.append(DaxCoreConstants.SEPARATOR_UNIT);
            sb.append(tagCodec.encode(m.tag()));
            firstEntry = false;
        }
    }
    ///----------------------------------------------------------------------------------------
    private void writeEntityRecordValues(StringBuilder sb, Object entity, ClassMetadata metadata) {
        boolean firstEntry = true;

        for (AnnotatedField annotatedField : metadata.fields()) {
            if (!firstEntry) sb.append(DaxCoreConstants.SEPARATOR_UNIT);
            try {
                Object val = annotatedField.field().get(entity);
                sb.append(val != null ? val.toString() : "");
            } catch (IllegalAccessException e) {
                logger.error("Bulk access error on field {}", annotatedField.field().getName(), e);
                sb.append("");
            }
            firstEntry = false;
        }

        for (AnnotatedMethod annotatedMethod : metadata.methods()) {
            if (!firstEntry) sb.append(DaxCoreConstants.SEPARATOR_UNIT);
            try {
                Object val = annotatedMethod.method().invoke(entity);
                sb.append(val != null ? val.toString() : "");
            } catch (Exception e) {
                logger.error("Bulk invocation error on method {}", annotatedMethod.method().getName(), e);
                sb.append("");
            }
            firstEntry = false;
        }
    }


    ///----------------------------------------------------------------------------------------

    private void appendPrimitiveRecord(StringBuilder sb, Object item) {
        sb.append(item != null ? item.toString() : "");
    }
    ///----------------------------------------------------------------------------------------
    private void writeHeader(StringBuilder sb, ClassMetadata metadata) {
        writeHeaderFields(sb, metadata);
        sb.append(DaxCoreConstants.SEPARATOR_RECORD);
    }
    ///----------------------------------------------------------------------------------------
    private void writeEntityRecord(StringBuilder sb, Object entity, ClassMetadata metadata) {
        if (entity == null) return;
        writeEntityRecordValues(sb, entity, metadata);
    }


    ///----------------------------------------------------------------------------------------
    public String collectionToBulk(Iterable<?> collection) {
        Iterator<?> iterator = collection.iterator();
        if (!iterator.hasNext()) {
            return "";
        }

        Object first = iterator.next();
        StringBuilder sb = new StringBuilder();
        sb.append(DaxCoreConstants.SEPARATOR_FILE);

        if (dataTypeService.isPrimitiveType(first.getClass())) {
            // Primitive Collection Mode: Single column without header
            appendPrimitiveRecord(sb, first);
            while (iterator.hasNext()) {
                sb.append(DaxCoreConstants.SEPARATOR_RECORD);
                appendPrimitiveRecord(sb, iterator.next());
            }
        } else {
            // Complex Entity Mode: Header row + Object values
            Class<?> clazz = first.getClass();
            ClassMetadata metadata = semanticRegistry.getClassMetadata(clazz);

            // 1. Write Header (Tag IDs)
            writeHeader(sb, metadata);

            // 2. Write Records
            writeEntityRecord(sb, first, metadata);
            while (iterator.hasNext()) {
                sb.append(DaxCoreConstants.SEPARATOR_RECORD);
                writeEntityRecord(sb, iterator.next(), metadata);
            }
        }
        sb.append(DaxCoreConstants.END_OF_MEDIUM);
        sb.append(DaxCoreConstants.SEPARATOR_FILE);

        return sb.toString();
    }
    ///----------------------------------------------------------------------------------------


    public String mapToBulk(Map<?, ?> map) {
        if (map.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(DaxCoreConstants.SEPARATOR_FILE);

        // Fetch the first element to inspect the value type in the map
        Map.Entry<?, ?> firstEntry = map.entrySet().iterator().next();
        Object sampleKey = firstEntry.getKey();
        Object sampleValue = firstEntry.getValue();

        boolean isValueEntity = sampleValue != null &&
                sampleValue.getClass().isAnnotationPresent(DaxpEntity.class);

        // 1. Building the Header
        sb.append(tagCodec.encode(COLLECTION_KEY)).append(DaxCoreConstants.SEPARATOR_UNIT);

        if (isValueEntity) {
            ClassMetadata metadata = semanticRegistry.getClassMetadata(sampleValue.getClass());
            writeHeaderFields(sb, metadata);
        } else {
            sb.append(tagCodec.encode(COLLECTION_VALUE));
        }

        // 2. Building Data Records
        map.forEach((key, val) -> {
            sb.append(DaxCoreConstants.SEPARATOR_RECORD);
            sb.append(key != null ? key.toString() : "")
                    .append(DaxCoreConstants.SEPARATOR_UNIT);

            if (isValueEntity && val != null) {
                ClassMetadata metadata = semanticRegistry.getClassMetadata(val.getClass());
                writeEntityRecordValues(sb, val, metadata);
            } else {
                sb.append(val != null ? val.toString() : "");
            }
        });
        sb.append(DaxCoreConstants.END_OF_MEDIUM);
        sb.append(DaxCoreConstants.SEPARATOR_FILE);
        return sb.toString();
    }
}

