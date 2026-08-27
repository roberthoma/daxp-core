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

package org.daxprotocol.core.data;

import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.codec.DaxValueCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class DaxMessageConverter {
    private static final Logger logger = LoggerFactory.getLogger(DaxMessageConverter.class);

    private final DaxConfig config;
    private final DaxDataModel dictionary;
    private final DaxTagCodec tagCodec;
    private final DaxDataTypeCodec dataTypeCodec;
    private final DaxValueCodec valueCodec;
    private final DaxDataTypeService daxDataTypeService;

    public DaxMessageConverter(DaxConfig config,
            DaxDataModel dictionary,
            DaxTagCodec tagCodec,
            DaxDataTypeCodec dataTypeCodec,
            DaxValueCodec valueCodec,
            DaxDataTypeService daxDataTypeService) {
        this.config = config;
        this.dictionary = dictionary;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        this.valueCodec = valueCodec;
        this.daxDataTypeService = daxDataTypeService;
    }

    public <T> T createFromMessage(DaxMessage message, Class<T> targetClass) {
        return createFromMessage(message, targetClass, 0);
    }

    public <T> T createFromMessage(DaxMessage message, Class<T> targetClass, int blockIdx) {
        T instance = instantiateClass(targetClass);

        for (Field field : targetClass.getDeclaredFields()) {
            DaxpField ann = field.getAnnotation(DaxpField.class);
            if (ann == null) {
                continue;
            }

            DaxTag tag = tagCodec.decode(ann);
            field.setAccessible(true);

            try {
                if (message.isNullAt(blockIdx, tag)) {
                    valueCodec.setNull(instance, field);
                    continue;
                }

                // 1. Direct scalar value mapping
                var pair = message.get(blockIdx, tag);
                if (pair != null) {
                    Object converted = valueCodec.decode(pair.getStrValue(), field.getType());
                    field.set(instance, converted);
                    continue;
                }

                // 2. Reference-based mapping (Collections, Maps, Entities)
                if (message.isAnyReference(blockIdx, tag)) {
                    Set<Integer> refBlocksIdx = message.getRefBlocksIdx(blockIdx, tag);

                    if (Collection.class.isAssignableFrom(field.getType())) {
                        populateCollectionField(message, instance, field, refBlocksIdx);
                    } else if (Map.class.isAssignableFrom(field.getType())) {
                        populateMapField(message, instance, field, refBlocksIdx, tag);
                    } else {
                        populateSingleReference(message, instance, field, refBlocksIdx);
                    }
                }
            } catch (Exception e) {
                throw new DaxException("DAXP-XXXX2 Failed to map field '" + field.getName() + "' on class " + targetClass.getSimpleName(), e);
            }
        }

        return instance;
    }

    public void updateFromMessage(DaxMessage message, Object obj) {
        Class<?> clazz = obj.getClass();
        try {
            int namespaceId = config.getAppNamespaceId();
            for (Field f : clazz.getDeclaredFields()) {
                DaxpField ann = f.getAnnotation(DaxpField.class);
                if (ann == null) continue;

                DaxTag tag = DaxTag.of(namespaceId, ann.tagId());
                if (!message.getBody().getBlock(0).containsKey(tag)) continue;

                var pair = message.get(0, tag);
                if (pair == null) continue;

                Object converted = valueCodec.decode(pair.getStrValue(), f.getType());
                f.setAccessible(true);
                f.set(obj, converted);
            }
        } catch (Exception e) {
            throw new DaxException("DAXP-XXXX1 Failed to update object of type " + clazz.getSimpleName() + " from message", e);
        }
    }

    // --- Private Helper Handlers ---

    private void populateCollectionField(DaxMessage message, Object instance, Field field, Set<Integer> refBlocksIdx) throws IllegalAccessException {
        Collection<Object> collection = createCollectionInstance(field.getType());
        Class<?> elementClass = getGenericTypeArgument(field, 0, String.class);

        for (Integer refIdx : refBlocksIdx) {
            int targetBlockIdx = validateAndGetBlockIndex(message, refIdx);
            Object elementValue;

            if (daxDataTypeService.isPrimitiveType(elementClass)) {
                String rawVal = message.getBody()
                        .getBlockMap(targetBlockIdx)
                        .get(DaxCoreTags.COLLECTION_VALUE)
                        .getStrValue();
                elementValue = valueCodec.decode(rawVal, elementClass);
            } else {
                elementValue = createFromMessage(message, elementClass, targetBlockIdx);
            }
            collection.add(elementValue);
        }
        field.set(instance, collection);
    }

    private void populateMapField(DaxMessage message, Object instance, Field field,
                Set<Integer> refBlocksIdx, DaxTag tag) throws IllegalAccessException
    {
        logger.trace("Tag: {} refBlocksIdx = {}", tagCodec.encode(tag), refBlocksIdx);
        if (refBlocksIdx.isEmpty()) return;

        Map<Object, Object> map = createMapInstance(field.getType());
        Class<?> keyClass = getGenericTypeArgument(field, 0, String.class);
        Class<?> valueClass = getGenericTypeArgument(field, 1, Object.class);

        for (Integer refIdx : refBlocksIdx) {
            int targetBlockIdx = validateAndGetBlockIndex(message, refIdx);
            var blockMap = message.getBody().getBlockMap(targetBlockIdx);

            // Extract Key
            Object key;
            if (daxDataTypeService.isPrimitiveType(keyClass)) {
                String rawKey = blockMap.get(DaxCoreTags.COLLECTION_KEY).getStrValue();
                key = valueCodec.decode(rawKey, keyClass);
            } else {
                key = createFromMessage(message, keyClass, targetBlockIdx);
            }

            // Extract Value
            Object val;
            if (daxDataTypeService.isPrimitiveType(valueClass)) {
                String rawValue = blockMap.get(DaxCoreTags.COLLECTION_VALUE).getStrValue();
                val = valueCodec.decode(rawValue, valueClass);
            } else {
                val = createFromMessage(message, valueClass, targetBlockIdx);
            }

            map.put(key, val);
        }
        field.set(instance, map);
    }

    private void populateSingleReference(DaxMessage message, Object instance, Field field, Set<Integer> refBlocksIdx) {
        if (refBlocksIdx.isEmpty()) return;

        Integer refIdx = refBlocksIdx.iterator().next();
        int targetBlockIdx = validateAndGetBlockIndex(message, refIdx);

        try {
            Object nestedEntity = createFromMessage(message, field.getType(), targetBlockIdx);
            field.set(instance, nestedEntity);
        } catch (IllegalAccessException e) {
            throw new DaxException("Unable to set field value for " + field.getName(), e);
        }
    }

    // --- Reflection & Factory Utilities ---

    private <T> T instantiateClass(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new DaxException("Class " + clazz.getName() + " lacks a default no-argument constructor", e);
        } catch (Exception e) {
            throw new DaxException("Failed to instantiate " + clazz.getName(), e);
        }
    }

    private int validateAndGetBlockIndex(DaxMessage message, Integer refIdx) {
        int targetBlockIdx = refIdx - 1;
        if (targetBlockIdx < 0 || targetBlockIdx >= message.getBody().getBlocksCount()) {
            throw new DaxException("DAXP-XXXXX","Referenced block index " + refIdx + " (target idx " + targetBlockIdx + ") does not exist in message body.");
        }
        return targetBlockIdx;
    }

    @SuppressWarnings("unchecked")
    private Collection<Object> createCollectionInstance(Class<?> fieldType) {
        if (!fieldType.isInterface()) {
            try {
                Constructor<?> ctor = fieldType.getDeclaredConstructor();
                ctor.setAccessible(true);
                return (Collection<Object>) ctor.newInstance();
            } catch (Exception ignored) { }
        }
        if (Set.class.isAssignableFrom(fieldType)) return new LinkedHashSet<>();
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private Map<Object, Object> createMapInstance(Class<?> fieldType) {
        if (!fieldType.isInterface()) {
            try {
                Constructor<?> ctor = fieldType.getDeclaredConstructor();
                ctor.setAccessible(true);
                return (Map<Object, Object>) ctor.newInstance();
            } catch (Exception ignored) { }
        }
        if (SortedMap.class.isAssignableFrom(fieldType)) return new TreeMap<>();
        return new LinkedHashMap<>();
    }

    private Class<?> getGenericTypeArgument(Field field, int index, Class<?> fallback) {
        Type genericFieldType = field.getGenericType();
        if (genericFieldType instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (index < actualTypeArguments.length && actualTypeArguments[index] instanceof Class<?> clazz) {
                return clazz;
            }
        }
        return fallback;
    }
}