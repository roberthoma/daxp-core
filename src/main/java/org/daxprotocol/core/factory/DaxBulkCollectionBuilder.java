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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import static org.daxprotocol.core.application.DaxCoreTags.*;

/**
 * Serializes entities, collections, and maps into DAXP bulk payload format.
 * Utilizes standard ASCII control separators (0x1C to 0x1F) for hierarchical boundaries.
 */
public class DaxBulkCollectionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DaxBulkCollectionBuilder.class);

    private final DaxTagCodec tagCodec;
    private final DaxDataTypeService dataTypeService;
    private final DaxSemanticRegistry semanticRegistry;

    public DaxBulkCollectionBuilder(DaxSemanticRegistry semanticRegistry, DaxTagCodec tagCodec, DaxDataTypeService dataTypeService) {
        this.tagCodec = tagCodec;
        this.dataTypeService = dataTypeService;
        this.semanticRegistry = semanticRegistry;
    }

    // =========================================================================================
    // FIELD & VALUE HELPER METHODS
    // =========================================================================================

    /**
     * Encodes and appends metadata tag identifiers for all entity fields and methods.
     */
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

    /**
     * Extracts and appends values of an entity's fields and methods separated by SEPARATOR_UNIT.
     */
    private void writeEntityRecordValues(StringBuilder sb, Object entity, ClassMetadata metadata) {
        boolean firstEntry = true;

        for (AnnotatedField annotatedField : metadata.fields()) {
            if (!firstEntry) sb.append(DaxCoreConstants.SEPARATOR_UNIT);
            try {
                Object val = annotatedField.field().get(entity);
                formatAndAppendValue(sb, val);
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
                formatAndAppendValue(sb, val);
            } catch (Exception e) {
                logger.error("Bulk invocation error on method {}", annotatedMethod.method().getName(), e);
                sb.append("");
            }
            firstEntry = false;
        }
    }

    /**
     * Formats an arbitrary value depending on whether it is an Entity, Collection, Map, or Primitive.
     */
    private void formatAndAppendValue(StringBuilder sb, Object val) {
        if (val == null) {
            return;
        }

        Class<?> clazz = val.getClass();

        if (clazz.isAnnotationPresent(DaxpEntity.class)) {
            sb.append(entityToBulk(val));
        } else if (val instanceof Iterable<?>) {
            sb.append(collectionToBulk((Iterable<?>) val));
        } else if (val instanceof Map<?, ?>) {
            sb.append(mapToBulk((Map<?, ?>) val));
        } else {
            sb.append(val.toString());
        }
    }

    /**
     * Converts a single entity object into a bulk representation wrapped as a single-element list.
     */
    public String entityToBulk(Object entity) {
        if (entity == null) {
            return "";
        }
        return collectionToBulk(Collections.singletonList(entity));
    }

    // =========================================================================================
    // LIST / SET HANDLING (Iterable)
    // =========================================================================================

    /**
     * Serializes an Iterable (List or Set) into DAXP bulk payload representation.
     */
    public String collectionToBulk(Iterable<?> collection) {
        Iterator<?> iterator = collection.iterator();
        if (!iterator.hasNext()) {
            return "";
        }

        Object first = iterator.next();
        StringBuilder sb = new StringBuilder();
        sb.append(DaxCoreConstants.SEPARATOR_FILE);

        if (dataTypeService.isPrimitiveType(first.getClass())) {
            // Primitive Collection Mode: Single column without metadata headers
            sb.append(first);
            while (iterator.hasNext()) {
                sb.append(DaxCoreConstants.SEPARATOR_RECORD);
                Object item = iterator.next();
                sb.append(item != null ? item.toString() : "");
            }
        } else {
            // Complex Entity Collection Mode: Field Tag Headers + Data Rows
            Class<?> clazz = first.getClass();
            ClassMetadata metadata = semanticRegistry.getClassMetadata(clazz);

            // 1. Header row containing Tag IDs
            writeHeaderFields(sb, metadata);
            sb.append(DaxCoreConstants.SEPARATOR_RECORD);

            // 2. Data rows
            writeEntityRecordValues(sb, first, metadata);
            while (iterator.hasNext()) {
                sb.append(DaxCoreConstants.SEPARATOR_RECORD);
                Object entity = iterator.next();
                if (entity != null) {
                    writeEntityRecordValues(sb, entity, metadata);
                }
            }
        }

        sb.append(DaxCoreConstants.END_OF_MEDIUM);
        sb.append(DaxCoreConstants.SEPARATOR_FILE);
        return sb.toString();
    }

    // =========================================================================================
    // MAP HANDLING (Key -> Value)
    // =========================================================================================

    /**
     * Serializes a Map into DAXP bulk payload representation.
     * Handles primitive and complex Entity types for both Key and Value using SEPARATOR_GROUP boundaries.
     */
    public String mapToBulk(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(DaxCoreConstants.SEPARATOR_FILE);

        Map.Entry<?, ?> firstEntry = map.entrySet().iterator().next();
        Object sampleKey = firstEntry.getKey();
        Object sampleValue = firstEntry.getValue();

        boolean isKeyEntity = sampleKey != null && sampleKey.getClass().isAnnotationPresent(DaxpEntity.class);
        boolean isValueEntity = sampleValue != null && sampleValue.getClass().isAnnotationPresent(DaxpEntity.class);

        // -------------------------------------------------------------------------------------
        // 1. BUILD MAP HEADER
        // -------------------------------------------------------------------------------------

        // Header: KEY
        if (isKeyEntity) {
            ClassMetadata keyMetadata = semanticRegistry.getClassMetadata(sampleKey.getClass());
            sb.append(DaxCoreConstants.SEPARATOR_GROUP);
            writeHeaderFields(sb, keyMetadata);
            sb.append(DaxCoreConstants.SEPARATOR_GROUP);
        } else {
            sb.append(tagCodec.encode(COLLECTION_KEY));
        }

        sb.append(DaxCoreConstants.SEPARATOR_UNIT);

        // Header: VALUE
        if (isValueEntity) {
            ClassMetadata valueMetadata = semanticRegistry.getClassMetadata(sampleValue.getClass());
            sb.append(DaxCoreConstants.SEPARATOR_GROUP);
            writeHeaderFields(sb, valueMetadata);
            sb.append(DaxCoreConstants.SEPARATOR_GROUP);
        } else {
            sb.append(tagCodec.encode(COLLECTION_VALUE));
        }

        // -------------------------------------------------------------------------------------
        // 2. BUILD MAP DATA RECORDS
        // -------------------------------------------------------------------------------------
        map.forEach((key, val) -> {
            sb.append(DaxCoreConstants.SEPARATOR_RECORD);

            // Write Key
            if (isKeyEntity && key != null) {
                ClassMetadata keyMetadata = semanticRegistry.getClassMetadata(key.getClass());
                sb.append(DaxCoreConstants.SEPARATOR_GROUP);
                writeEntityRecordValues(sb, key, keyMetadata);
                sb.append(DaxCoreConstants.SEPARATOR_GROUP);
            } else {
                sb.append(key != null ? key.toString() : "");
            }

            sb.append(DaxCoreConstants.SEPARATOR_UNIT);

            // Write Value
            if (isValueEntity && val != null) {
                ClassMetadata valueMetadata = semanticRegistry.getClassMetadata(val.getClass());
                sb.append(DaxCoreConstants.SEPARATOR_GROUP);
                writeEntityRecordValues(sb, val, valueMetadata);
                sb.append(DaxCoreConstants.SEPARATOR_GROUP);
            } else {
                formatAndAppendValue(sb, val);
            }
        });

        sb.append(DaxCoreConstants.END_OF_MEDIUM);
        sb.append(DaxCoreConstants.SEPARATOR_FILE);
        return sb.toString();
    }
}