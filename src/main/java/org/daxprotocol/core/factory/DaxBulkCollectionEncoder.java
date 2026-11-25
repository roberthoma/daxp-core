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
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import org.daxprotocol.core.model.preamble.DaxPreamble;
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
public class DaxBulkCollectionEncoder {
    private static final Logger logger = LoggerFactory.getLogger(DaxBulkCollectionEncoder.class);

    private final DaxTagCodec tagCodec;
    private final DaxDataTypeService dataTypeService;
    private final DaxSemanticRegistry semanticRegistry;

    public DaxBulkCollectionEncoder(DaxSemanticRegistry semanticRegistry,
                                    DaxTagCodec tagCodec, DaxDataTypeService dataTypeService)
    {
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
    private void writeHeaderFields(StringBuilder sb, ClassMetadata metadata, DaxPreamble preamble) {
        boolean firstEntry = true;

        for (AnnotatedField f : metadata.fields()) {
            if (!firstEntry) sb.append(preamble.getUnitSeparator());
            sb.append(tagCodec.encode(f.tag()));
            firstEntry = false;
        }

        for (AnnotatedMethod m : metadata.methods()) {
            if (!firstEntry) sb.append(preamble.getUnitSeparator());
            sb.append(tagCodec.encode(m.tag()));
            firstEntry = false;
        }
    }

    /**
     * Extracts and appends values of an entity's fields and methods separated by SEPARATOR_UNIT.
     */
    private void writeEntityRecordValues(StringBuilder sb, Object entity,
                                         ClassMetadata metadata, DaxPreamble preamble)
    {
        boolean firstEntry = true;

        for (AnnotatedField annotatedField : metadata.fields()) {
            if (!firstEntry) sb.append(preamble.getUnitSeparator());
            try {
                Object val = annotatedField.field().get(entity);
                formatAndAppendValue(sb, val, preamble);
            } catch (IllegalAccessException e) {
                logger.error("Bulk access error on field {}", annotatedField.field().getName(), e);
                sb.append("");
            }
            firstEntry = false;
        }

        for (AnnotatedMethod annotatedMethod : metadata.methods()) {
            if (!firstEntry) sb.append(preamble.getUnitSeparator());
            try {
                Object val = annotatedMethod.method().invoke(entity);
                formatAndAppendValue(sb, val, preamble);
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
    private void formatAndAppendValue(StringBuilder sb, Object val, DaxPreamble preamble) {
        if (val == null) {
            return;
        }

        Class<?> clazz = val.getClass();

        if (clazz.isAnnotationPresent(DaxpEntity.class)) {
            sb.append(entityToBulk(val, preamble));
        } else if (val instanceof Iterable<?>) {
            sb.append(iterableCollectionEncode((Iterable<?>) val, preamble));
        } else if (val instanceof Map<?, ?>) {
            sb.append(mapEncode((Map<?, ?>) val, preamble));
        } else {
            sb.append(val.toString());
        }
    }

    /**
     * Converts a single entity object into a bulk representation wrapped as a single-element list.
     */
    private String entityToBulk(Object entity, DaxPreamble preamble) {
        if (entity == null) {
            return "";
        }
        return iterableCollectionEncode(Collections.singletonList(entity), preamble);
    }

    // =========================================================================================
    // LIST / SET HANDLING (Iterable)
    // =========================================================================================

    /**
     * Serializes an Iterable (List or Set) into DAXP bulk payload representation.
     */
    public String iterableCollectionEncode(Iterable<?> collection, DaxPreamble preamble) {
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
                sb.append(preamble.getRecordSeparator());
                Object item = iterator.next();
                sb.append(item != null ? item.toString() : "");
            }
        } else {
            // Complex Entity Collection Mode: Field Tag Headers + Data Rows
            Class<?> clazz = first.getClass();
            ClassMetadata metadata = semanticRegistry.getClassMetadata(clazz);

            // 1. Header row containing Tag IDs
            writeHeaderFields(sb, metadata, preamble);
            sb.append(preamble.getRecordSeparator());

            // 2. Data rows
            writeEntityRecordValues(sb, first, metadata, preamble);
            while (iterator.hasNext()) {
                sb.append(preamble.getRecordSeparator());
                Object entity = iterator.next();
                if (entity != null) {
                    writeEntityRecordValues(sb, entity, metadata, preamble);
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
     * Always wraps both Key and Value groups in SEPARATOR_GROUP (...) END_OF_MEDIUM + SEPARATOR_GROUP boundaries.
     */

    // =========================================================================================
    // MAP HANDLING (Key -> Value)
    // =========================================================================================

    /**
     * Serializes a Map into DAXP bulk payload representation.
     * Wraps both Key and Value groups in SEPARATOR_GROUP (...) END_OF_MEDIUM + SEPARATOR_GROUP boundaries
     * without intermediate unit separators.
     */
    public String mapEncode(Map<?, ?> map, DaxPreamble preamble) {
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
        sb.append(preamble.getGroupSeparator());
        if (isKeyEntity) {
            ClassMetadata keyMetadata = semanticRegistry.getClassMetadata(sampleKey.getClass());
            writeHeaderFields(sb, keyMetadata, preamble);
        } else {
            sb.append(tagCodec.encode(COLLECTION_KEY));
        }
        sb.append(DaxCoreConstants.END_OF_MEDIUM);
        sb.append(preamble.getGroupSeparator());

        // Header: VALUE (Directly follows Key group, no SEPARATOR_UNIT)
        sb.append(preamble.getGroupSeparator());
        if (isValueEntity) {
            ClassMetadata valueMetadata = semanticRegistry.getClassMetadata(sampleValue.getClass());
            writeHeaderFields(sb, valueMetadata, preamble);
        } else {
            sb.append(tagCodec.encode(COLLECTION_VALUE));
        }
        sb.append(DaxCoreConstants.END_OF_MEDIUM);
        sb.append(preamble.getGroupSeparator());

        // -------------------------------------------------------------------------------------
        // 2. BUILD MAP DATA RECORDS
        // -------------------------------------------------------------------------------------
        map.forEach((key, val) -> {
            sb.append(preamble.getRecordSeparator());

            // Write Key
            sb.append(preamble.getGroupSeparator());
            if (isKeyEntity && key != null) {
                ClassMetadata keyMetadata = semanticRegistry.getClassMetadata(key.getClass());
                writeEntityRecordValues(sb, key, keyMetadata, preamble);
            } else {
                sb.append(key != null ? key.toString() : "");
            }
            sb.append(DaxCoreConstants.END_OF_MEDIUM);
            sb.append(preamble.getGroupSeparator());

            // Write Value (Directly follows Key group, no SEPARATOR_UNIT)
            sb.append(preamble.getGroupSeparator());
            if (isValueEntity && val != null) {
                ClassMetadata valueMetadata = semanticRegistry.getClassMetadata(val.getClass());
                writeEntityRecordValues(sb, val, valueMetadata, preamble);
            } else {
                formatAndAppendValue(sb, val, preamble);
            }
            sb.append(DaxCoreConstants.END_OF_MEDIUM);
            sb.append(preamble.getGroupSeparator());
        });

        sb.append(DaxCoreConstants.END_OF_MEDIUM);
        sb.append(DaxCoreConstants.SEPARATOR_FILE);
        return sb.toString();
    }

    /**
     * Serializes a collection into DAXP bulk payload format, delegating maps to
     * {@link #mapEncode(Map, DaxPreamble)} and iterables to {@link #iterableCollectionEncode(Iterable, DaxPreamble)}.
     *
     * @param collection the map or iterable to encode
     * @return the encoded bulk payload, or an empty string if the input is null,
     *         unsupported, or empty
     */
    public String collectionEncode(Object collection, DaxPreamble preamble) {
        if (collection instanceof Map<?, ?> map) {
            return mapEncode(map, preamble);
        }
        if (collection instanceof Iterable<?> iterable) {
            return iterableCollectionEncode(iterable, preamble);
        }
        return "";
    }
}
