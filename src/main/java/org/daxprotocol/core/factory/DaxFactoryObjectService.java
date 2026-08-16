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
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpValue;
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.codec.DaxValueCodec;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxFactoryObjectService {

    private static final Logger logger = LoggerFactory.getLogger(DaxFactoryObjectService.class);

    private final DaxTagCodec tagCodec;
    private final DaxDataTypeCodec dataTypeCodec;
    private final DaxValueCodec valueCodec;

    // Cache reflected fields and methods per class to prevent expensive introspection overhead
    private final Map<Class<?>, ClassMetadata> metadataCache = new ConcurrentHashMap<>();

    public DaxFactoryObjectService(DaxTagCodec tagCodec, DaxDataTypeCodec dataTypeCodec, DaxValueCodec valueCodec) {
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        this.valueCodec = valueCodec;
    }

    public void objectToMsgBlock(
            int blockIdx,
            DaxTag blockTag,
            Object entry,
            DaxBody body,
            Set<DaxTag> reqTagSet,
            DaxTag ownerTag
    ) {
        if (entry == null) return;

        body.putPair(blockIdx, ENTRY_TAG, blockTag);
        ClassMetadata metadata = getClassMetadata(entry.getClass());

        // Process fields
        for (AnnotatedField annotatedField : metadata.fields()) {
            DaxTag tag = annotatedField.tag();
            if (reqTagSet != null && !reqTagSet.contains(tag)) {
                continue;
            }

            try {
                Object fieldValue = annotatedField.field().get(entry);
                if (fieldValue == null) {
                    body.putPair(blockIdx, new DaxPairString(tag, "N", DaxCoreConstants.OPERATOR_ACTION));
                } else {
                    putValueToBlock(blockIdx, tag, body, fieldValue, reqTagSet, ownerTag);
                }
            } catch (IllegalAccessException e) {
                logger.error("Failed to access field {} on {}", annotatedField.field().getName(), entry.getClass().getName(), e);
                throw new DaxException("Field access security exception", e);
            }
        }

        // Process methods
        for (AnnotatedMethod annotatedMethod : metadata.methods()) {
            DaxTag tag = annotatedMethod.tag();
            if (reqTagSet != null && !reqTagSet.contains(tag)) {
                continue;
            }

            try {
                Object methodValue = annotatedMethod.method().invoke(entry);
                if (methodValue == null) {
                    body.putPair(blockIdx, new DaxPairString(tag, "N", DaxCoreConstants.OPERATOR_ACTION));
                } else {
                    putValueToBlock(blockIdx, tag, body, methodValue, reqTagSet, ownerTag);
                }
            } catch (Exception e) {
                logger.error("Failed to invoke method {} on {}", annotatedMethod.method().getName(), entry.getClass().getName(), e);
                throw new DaxException("Method invocation failure", e);
            }
        }
    }

    private void putValueToBlock(
            int blockIdx,
            DaxTag tag,
            DaxBody body,
            Object object,
            Set<DaxTag> reqTagSet,
            DaxTag ownerTag
    ) {
        if (object.getClass().isAnnotationPresent(DaxpEntity.class)) {
            body.nextBlock(DaxBlockType.BLOCK_VALUE);
            int nestedIdx = body.getCurrentIdx();
            body.putTagBlockReference(blockIdx, tag, nestedIdx + 1);
            body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID, ownerTag));
            objectToMsgBlock(nestedIdx, tag, object, body, reqTagSet, ownerTag);
        } else if (dataTypeCodec.isCollection(object)) {
            processCollection(blockIdx, tag, body, object, reqTagSet, ownerTag);
        } else {
            body.putPair(blockIdx, valueCodec.encodeToPairs(tag, object));
        }
    }

    private void processCollection(
            int blockIdx,
            DaxTag tag,
            DaxBody body,
            Object object,
            Set<DaxTag> reqTagSet,
            DaxTag ownerTag
    ) {
        if (dataTypeCodec.isMap(object)) {
            Map<?, ?> map = (Map<?, ?>) object;
            map.forEach((key, value) -> {
                body.nextBlock(DaxBlockType.BLOCK_VALUE);
                int nestedIdx = body.getCurrentIdx();
                body.putTagBlockReference(blockIdx, tag, nestedIdx + 1);

                appendElementToBlock(nestedIdx, tag, body, value, COLLECTION_VALUE, reqTagSet, ownerTag);
                appendElementToBlock(nestedIdx, tag, body, key, COLLECTION_KEY, reqTagSet, ownerTag);
            });
        } else if (object instanceof Iterable<?>) {
            Iterable<?> collection = (Iterable<?>) object;
            for (Object objVal : collection) {
                body.nextBlock(DaxBlockType.BLOCK_VALUE);
                int nestedIdx = body.getCurrentIdx();
                body.putTagBlockReference(blockIdx, tag, nestedIdx + 1);
                appendElementToBlock(nestedIdx, tag, body, objVal, COLLECTION_VALUE, reqTagSet, ownerTag);
            }
        }
    }

    private void appendElementToBlock(
            int nestedIdx,
            DaxTag tag,
            DaxBody body,
            Object element,
            DaxTag collectionTag,
            Set<DaxTag> reqTagSet,
            DaxTag ownerTag
    ) {
        if (dataTypeCodec.isPrimitiveType(element)) {
            body.putPair(nestedIdx, valueCodec.encodeToPairs(collectionTag, element));
            body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID, ownerTag));
            body.putPair(nestedIdx, new DaxPairTag(ENTRY_TAG, tag));
        } else {
            objectToMsgBlock(nestedIdx, tag, element, body, reqTagSet, ownerTag);
        }
    }

    private ClassMetadata getClassMetadata(Class<?> clazz) {
        return metadataCache.computeIfAbsent(clazz, clz -> {
            List<AnnotatedField> fields = new ArrayList<>();
            for (Field field : DaxLangTool.allFields(clz)) {
                DaxTag tag = null;
                if (field.isAnnotationPresent(DaxpField.class)) {
                    tag = tagCodec.decode(field.getAnnotation(DaxpField.class));
                } else if (field.isAnnotationPresent(DaxpValue.class)) {
                    tag = tagCodec.decode(field.getAnnotation(DaxpValue.class));
                }

                if (tag != null) {
                    field.setAccessible(true);
                    fields.add(new AnnotatedField(field, tag));
                }
            }

            List<AnnotatedMethod> methods = new ArrayList<>();
            for (Method method : clz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(DaxpValue.class)) {
                    method.setAccessible(true);
                    methods.add(new AnnotatedMethod(method, tagCodec.decode(method.getAnnotation(DaxpValue.class))));
                }
            }

            return new ClassMetadata(fields, methods);
        });
    }

    private record AnnotatedField(Field field, DaxTag tag) {}
    private record AnnotatedMethod(Method method, DaxTag tag) {}
    private record ClassMetadata(List<AnnotatedField> fields, List<AnnotatedMethod> methods) {}
}