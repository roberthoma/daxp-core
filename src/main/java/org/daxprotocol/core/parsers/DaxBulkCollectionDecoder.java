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

package org.daxprotocol.core.parsers;

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

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Decodes DAXP bulk payload format back into entities, collections, and maps
 * using index-based stream traversal without string splitting.
 */
public class DaxBulkCollectionDecoder {
    private static final Logger logger = LoggerFactory.getLogger(DaxBulkCollectionDecoder.class);

    private final DaxTagCodec tagCodec;
    private final DaxDataTypeService dataTypeService;
    private final DaxSemanticRegistry semanticRegistry;

    public DaxBulkCollectionDecoder(DaxSemanticRegistry semanticRegistry, DaxTagCodec tagCodec, DaxDataTypeService dataTypeService) {
        this.tagCodec = tagCodec;
        this.dataTypeService = dataTypeService;
        this.semanticRegistry = semanticRegistry;
    }

    // =========================================================================================
    // MAP DECODING
    // =========================================================================================

    /**
     * Decodes a DAXP bulk payload string back into a Map without using split().
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> decodeMap(String payload, Class<K> keyClass, Class<V> valueClass, int namespaceId) {
        if (payload == null || payload.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<K, V> result = new LinkedHashMap<>();
        boolean isKeyEntity = keyClass.isAnnotationPresent(DaxpEntity.class);
        boolean isValueEntity = valueClass.isAnnotationPresent(DaxpEntity.class);

        String cleanPayload = stripPayloadBoundaries(payload);
        if (cleanPayload.isEmpty()) {
            return result;
        }

        List<String> records = parseRecords(cleanPayload);
        if (records.isEmpty()) {
            return result;
        }

        // 1. Parse Header (Record 0)
        String headerRecord = records.get(0);
        List<String> groupParts = extractGroupParts(headerRecord);

        List<Object> keyHeaders = isKeyEntity && !groupParts.isEmpty() ? parseHeaderTags(groupParts.get(0), namespaceId)
                                                                       : Collections.emptyList();
        List<Object> valueHeaders = isValueEntity && groupParts.size() > 1 ? parseHeaderTags(groupParts.get(1), namespaceId)
                                                                           : Collections.emptyList();

        ClassMetadata keyMetadata = isKeyEntity ? semanticRegistry.getClassMetadata(keyClass) : null;
        ClassMetadata valueMetadata = isValueEntity ? semanticRegistry.getClassMetadata(valueClass) : null;

        // 2. Parse Data Records (Record 1 to N)
        for (int i = 1; i < records.size(); i++) {
            String dataRecord = records.get(i);
            List<String> dataGroups = extractGroupParts(dataRecord);
            if (dataGroups.size() < 2) {
                continue;
            }

            K key = decodeKeyOrValue(dataGroups.get(0), keyClass, isKeyEntity, keyHeaders, keyMetadata);
            V val = decodeKeyOrValue(dataGroups.get(1), valueClass, isValueEntity, valueHeaders, valueMetadata);

            if (key != null) {
                result.put(key, val);
            }
        }

        return result;
    }

    // =========================================================================================
    // COLLECTION DECODING
    // =========================================================================================

    /**
     * Decodes a DAXP bulk payload string back into a List without using split().
     */
    public <T> List<T> decodeCollection(String payload, Class<T> itemClass, int namespaceId) {
        if (payload == null || payload.isEmpty()) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>();
        boolean isEntity = itemClass.isAnnotationPresent(DaxpEntity.class);

        String cleanPayload = stripPayloadBoundaries(payload);
        if (cleanPayload.isEmpty()) {
            return result;
        }

        List<String> records = parseRecords(cleanPayload);
        if (records.isEmpty()) {
            return result;
        }

        if (!isEntity) {
            // Primitive Collection Mode
            for (String record : records) {
                result.add(dataTypeService.convert(record, itemClass));
            }
        } else {
            // Complex Entity Collection Mode
            List<Object> headers = parseHeaderTags(records.get(0), namespaceId);
            ClassMetadata metadata = semanticRegistry.getClassMetadata(itemClass);

            for (int i = 1; i < records.size(); i++) {
                T entity = instantiateAndPopulateEntity(records.get(i), itemClass, metadata, headers);
                if (entity != null) {
                    result.add(entity);
                }
            }
        }

        return result;
    }

    // =========================================================================================
    // INDEX-BASED PARSING HELPERS (NO SPLIT)
    // =========================================================================================

    private String stripPayloadBoundaries(String payload) {
        String p = payload;
        char fs = DaxCoreConstants.SEPARATOR_FILE;
        char em = DaxCoreConstants.END_OF_MEDIUM;

        if (!p.isEmpty() && p.charAt(0) == fs) {
            p = p.substring(1);
        }
        if (!p.isEmpty() && p.charAt(p.length() - 1) == fs) {
            p = p.substring(0, p.length() - 1);
        }
        if (!p.isEmpty() && p.charAt(p.length() - 1) == em) {
            p = p.substring(0, p.length() - 1);
        }
        return p;
    }

    /**
     * Splits payload into records using SEPARATOR_RECORD index scanning.
     */
    private List<String> parseRecords(String payload) {
        List<String> records = new ArrayList<>();
        int start = 0;
        int len = payload.length();
        char rs = DaxCoreConstants.SEPARATOR_RECORD;

        while (start < len) {
            int idx = payload.indexOf(rs, start);
            if (idx == -1) {
                records.add(payload.substring(start));
                break;
            } else {
                records.add(payload.substring(start, idx));
                start = idx + 1;
            }
        }
        return records;
    }

    /**
     * Splits record segments into fields using SEPARATOR_UNIT index scanning.
     */
    private List<String> parseFields(String segment) {
        List<String> fields = new ArrayList<>();
        if (segment == null || segment.isEmpty()) {
            return fields;
        }

        int start = 0;
        int len = segment.length();
        char us = DaxCoreConstants.SEPARATOR_UNIT;

        while (start < len) {
            int idx = segment.indexOf(us, start);
            if (idx == -1) {
                fields.add(segment.substring(start));
                break;
            } else {
                fields.add(segment.substring(start, idx));
                start = idx + 1;
            }
        }
        return fields;
    }

    /**
     * Extracts text segments enclosed between <GS> ... <EM> using index scanning.
     */
    private List<String> extractGroupParts(String record) {
        List<String> groups = new ArrayList<>();
        int idx = 0;
        int len = record.length();
        char gs = DaxCoreConstants.SEPARATOR_GROUP;
        char em = DaxCoreConstants.END_OF_MEDIUM;

        while (idx < len) {
            int start = record.indexOf(gs, idx);
            if (start == -1) break;
            int end = record.indexOf(em, start);
            if (end == -1) break;

            groups.add(record.substring(start + 1, end));
            idx = end + 1;
            if (idx < len && record.charAt(idx) == gs) {
                idx++;
            }
        }
        return groups;
    }

    private List<Object> parseHeaderTags(String headerSegment, int namespaceId) {
        List<String> encodedTags = parseFields(headerSegment);
        List<Object> tags = new ArrayList<>();
        for (String t : encodedTags) {
            if (!t.isEmpty()) {
                tags.add(tagCodec.decode(t, namespaceId)); // TODO from mesg
            }
        }
        return tags;
    }

    @SuppressWarnings("unchecked")
    private <T> T decodeKeyOrValue(String segment, Class<T> clazz, boolean isEntity, List<Object> headers,
            ClassMetadata metadata) {
        if (!isEntity) {
            return dataTypeService.convert(segment, clazz);
        }
        return instantiateAndPopulateEntity(segment, clazz, metadata, headers);
    }

    private <T> T instantiateAndPopulateEntity(String dataSegment, Class<T> clazz, ClassMetadata metadata, List<Object> headers) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();

            List<String> values = parseFields(dataSegment);

            for (int i = 0; i < Math.min(values.size(), headers.size()); i++) {
                Object tagId = headers.get(i);
                String valStr = values.get(i);

                setValueToEntity(instance, metadata, tagId, valStr);
            }

            return instance;
        } catch (Exception e) {
            logger.error("Failed to decode entity of type {}", clazz.getName(), e);
            return null;
        }
    }

    private void setValueToEntity(Object instance, ClassMetadata metadata, Object tagId, String valStr) {
        for (AnnotatedField field : metadata.fields()) {
            if (String.valueOf(field.tag()).equals(String.valueOf(tagId))) {
                try {
                    Object converted = dataTypeService.convert(valStr, field.field().getType());
                    field.field().set(instance, converted);
                } catch (Exception e) {
                    logger.error("Failed to set field {}", field.field().getName(), e);
                }
                return;
            }
        }

        for (AnnotatedMethod method : metadata.methods()) {
            if (String.valueOf(method.tag()).equals(String.valueOf(tagId))) {
                try {
                    Class<?> paramType = method.method().getParameterTypes()[0];
                    Object converted = dataTypeService.convert(valStr, paramType);
                    method.method().invoke(instance, converted);
                } catch (Exception e) {
                    logger.error("Failed to invoke method {}", method.method().getName(), e);
                }
                return;
            }
        }
    }
}