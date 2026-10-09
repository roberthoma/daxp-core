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
package org.daxprotocol.core.parsers;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.registries.AnnotatedField;
import org.daxprotocol.core.registries.ClassMetadata;
import org.daxprotocol.core.registries.DaxSemanticRegistry;

import java.util.*;

/**
 * High-performance, zero-split decoder for DAXP Bulk Collection format.
 * Parses hierarchical ASCII control characters (0x1C to 0x1F) using index cursors.
 */
public class DaxBulkCollectionDecoder {

//    private final DaxTagCodec tagCodec;
//    private final DaxDataTypeService dataTypeService;
//    private final DaxSemanticRegistry semanticRegistry;
//
//    public DaxBulkCollectionDecoder(DaxSemanticRegistry semanticRegistry, DaxTagCodec tagCodec, DaxDataTypeService dataTypeService) {
//        this.tagCodec = tagCodec;
//        this.dataTypeService = dataTypeService;
//        this.semanticRegistry = semanticRegistry;
//    }
//
//    /**
//     * Decodes a DAXP bulk string into a target Java Map.
//     */
//    @SuppressWarnings("unchecked")
//    public <K, V> Map<K, V> decodeMap(String bulkData, Class<K> keyClass, Class<V> valueClass) {
//        Map<K, V> resultMap = new HashMap<>();
//
//        if (bulkData == null || bulkData.isEmpty()) {
//            return resultMap;
//        }
//
//        int len = bulkData.length();
//        int cursor = 0;
//
//        // Strip leading/trailing FILE_SEPARATOR (0x1C) if present
//        if (bulkData.charAt(cursor) == DaxCoreConstants.SEPARATOR_FILE) cursor++;
//        int endBoundary = len;
//        if (bulkData.charAt(endBoundary - 1) == DaxCoreConstants.SEPARATOR_FILE) endBoundary--;
//
//        // ---------------------------------------------------------------------
//        // 1. PARSE HEADER ROW (Up to first RECORD_SEPARATOR 0x1E)
//        // ---------------------------------------------------------------------
//        int headerEnd = findNextSeparator(bulkData, cursor, endBoundary, DaxCoreConstants.SEPARATOR_RECORD);
//        if (headerEnd == -1) {
//            headerEnd = endBoundary; // Single header, no records
//        }
//
//        List<DaxTag> keyTags = new ArrayList<>();
//        List<DaxTag> valueTags = new ArrayList<>();
//
//        parseHeaderRow(bulkData, cursor, headerEnd, keyTags, valueTags);
//
//        // Move cursor past header and the RECORD_SEPARATOR
//        cursor = headerEnd + 1;
//
//        // ---------------------------------------------------------------------
//        // 2. PARSE DATA RECORDS (Iterating row by row using RECORD_SEPARATOR)
//        // ---------------------------------------------------------------------
//        ClassMetadata keyMetadata = semanticRegistry.getClassMetadata(keyClass);
//        ClassMetadata valueMetadata = semanticRegistry.getClassMetadata(valueClass);
//
//        while (cursor < endBoundary) {
//            int recordEnd = findNextSeparator(bulkData, cursor, endBoundary, DaxCoreConstants.SEPARATOR_RECORD);
//            if (recordEnd == -1) {
//                recordEnd = endBoundary;
//            }
//
//            // Read line content
//            if (cursor < recordEnd) {
//                MapEntryHolder entry = parseDataRecord(
//                        bulkData, cursor, recordEnd,
//                        keyClass, valueClass,
//                        keyTags, valueTags,
//                        keyMetadata, valueMetadata
//                );
//
//                if (entry != null) {
//                    resultMap.put((K) entry.key, (V) entry.value);
//                }
//            }
//
//            cursor = recordEnd + 1;
//        }
//
//        return resultMap;
//    }
//
//    // =========================================================================================
//    // PARSING HELPERS (INDEX-BASED)
//    // =========================================================================================
//
//    private void parseHeaderRow(String data, int start, int end, List<DaxTag> keyTags, List<DaxTag> valueTags) {
//        // Find the unit separator between Key Header and Value Header
//        int unitSep = findNextSeparator(data, start, end, DaxCoreConstants.SEPARATOR_UNIT);
//
//        int keyEnd = (unitSep != -1) ? unitSep : end;
//        int valueStart = (unitSep != -1) ? unitSep + 1 : end;
//
//        // Extract Key Tags
//        extractTagsFromSegment(data, start, keyEnd, keyTags);
//
//        // Extract Value Tags
//        if (valueStart < end) {
//            extractTagsFromSegment(data, valueStart, end, valueTags);
//        }
//    }
//
//    private void extractTagsFromSegment(String data, int start, int end, List<DaxTag> tagList) {
//        int pos = start;
//
//        // Skip GROUP_SEPARATOR if wrapped
//        if (pos < end && data.charAt(pos) == DaxCoreConstants.SEPARATOR_GROUP) pos++;
//        int actualEnd = end;
//        if (actualEnd > pos && data.charAt(actualEnd - 1) == DaxCoreConstants.SEPARATOR_GROUP) actualEnd--;
//
//        int tokenStart = pos;
//        while (pos <= actualEnd) {
//            if (pos == actualEnd || data.charAt(pos) == DaxCoreConstants.SEPARATOR_UNIT) {
//                if (tokenStart < pos) {
//                    String tagStr = data.substring(tokenStart, pos);
//                    tagList.add(tagCodec.decode(tagStr));
//                }
//                tokenStart = pos + 1;
//            }
//            pos++;
//        }
//    }
//
//    private MapEntryHolder parseDataRecord(
//            String data, int start, int end,
//            Class<?> keyClass, Class<?> valueClass,
//            List<DaxTag> keyTags, List<DaxTag> valueTags,
//            ClassMetadata keyMeta, ClassMetadata valMeta) {
//
//        int unitSep = findNextSeparator(data, start, end, DaxCoreConstants.SEPARATOR_UNIT);
//        if (unitSep == -1) return null;
//
//        String keySegment = extractSegment(data, start, unitSep);
//        String valueSegment = extractSegment(data, unitSep + 1, end);
//
//        Object keyObj = instantiateAndHydrate(keySegment, keyClass, keyTags, keyMeta);
//        Object valObj = instantiateAndHydrate(valueSegment, valueClass, valueTags, valMeta);
//
//        return new MapEntryHolder(keyObj, valObj);
//    }
//
//    private Object instantiateAndHydrate(String rawValue, Class<?> targetClass, List<DaxTag> tags, ClassMetadata metadata) {
//        if (rawValue.isEmpty()) return null;
//
//        // 1. Primitive / Simple Type
//        if (dataTypeService.isPrimitiveType(targetClass) || tags.isEmpty()) {
//            return dataTypeService.convert(rawValue, targetClass);
//        }
//
//        // 2. Complex Entity Object
//        try {
//            Object instance = targetClass.getDeclaredConstructor().newInstance();
//            int pos = 0;
//            int len = rawValue.length();
//            int tagIndex = 0;
//
//            int tokenStart = 0;
//            while (pos <= len) {
//                if (pos == len || rawValue.charAt(pos) == DaxCoreConstants.SEPARATOR_UNIT) {
//                    if (tagIndex < tags.size()) {
//                        String fieldValStr = rawValue.substring(tokenStart, pos);
//                        if (!fieldValStr.isEmpty()) {
//                            DaxTag tag = tags.get(tagIndex);
//                            injectFieldValue(instance, tag, fieldValStr, metadata);
//                        }
//                    }
//                    tagIndex++;
//                    tokenStart = pos + 1;
//                }
//                pos++;
//            }
//            return instance;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to hydrate object of type: " + targetClass.getName(), e);
//        }
//    }
//
//    private void injectFieldValue(Object instance, DaxTag tag, String valueStr, ClassMetadata metadata) {
//        AnnotatedField field = metadata.getFieldByTag(tag);
//        if (field != null) {
//            try {
//                Object convertedValue = dataTypeService.convert(valueStr, field.field().getType());
//                field.field().setAccessible(true);
//                field.field().set(instance, convertedValue);
//            } catch (Exception e) {
//                // Log or handle field set error
//            }
//        }
//    }
//
//    private int findNextSeparator(String data, int start, int end, char separator) {
//        for (int i = start; i < end; i++) {
//            if (data.charAt(i) == separator) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private String extractSegment(String data, int start, int end) {
//        if (start >= end) return "";
//
//        // Strip GROUP_SEPARATOR if wrapped
//        if (data.charAt(start) == DaxCoreConstants.SEPARATOR_GROUP) start++;
//        if (end > start && data.charAt(end - 1) == DaxCoreConstants.SEPARATOR_GROUP) end--;
//
//        return data.substring(start, end);
//    }
//
//    private static class MapEntryHolder {
//        final Object key;
//        final Object value;
//
//        MapEntryHolder(Object key, Object value) {
//            this.key = key;
//            this.value = value;
//        }
//    }
}