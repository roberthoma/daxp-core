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
import org.daxprotocol.core.registries.DaxSemanticRegistry;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.trailer.DaxTrailer;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxObjectMessageFactory {

    private static final Logger logger = LoggerFactory.getLogger(DaxObjectMessageFactory.class);
    private static final  int BULK_SIZE = 233; //tmp   <<<<<<<<<<<<<<<<<<<<

    private final DaxTagCodec tagCodec;
    private final DaxDataTypeCodec dataTypeCodec;
    private final DaxValueCodec valueCodec;
    private final DaxSemanticRegistry dataModel;

    private record AnnotatedField(Field field, DaxTag tag) {}
    private record AnnotatedMethod(Method method, DaxTag tag) {}
    private record ClassMetadata(List<AnnotatedField> fields, List<AnnotatedMethod> methods) {}

    // Cache reflected fields and methods per class to prevent expensive introspection overhead
    private final Map<Class<?>, ClassMetadata> metadataCache = new ConcurrentHashMap<>();

    ///----------------------------------------------------------------------------------------
    public DaxObjectMessageFactory(DaxTagCodec tagCodec, DaxDataTypeCodec dataTypeCodec, DaxValueCodec valueCodec
    ,  DaxSemanticRegistry dataModel) {
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        this.valueCodec = valueCodec;
        this.dataModel = dataModel;
    }
    ///----------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private List<Object> normalizeToList(Object daxDataEntry) {
        if (daxDataEntry instanceof List<?>) {
            return (List<Object>) daxDataEntry;
        }
        return daxDataEntry != null ? List.of(daxDataEntry) : List.of();
    }
    ///----------------------------------------------------------------------------------------
    public DaxMessage toDaxMessageFromObject(String messageType,
                                             Object daxDataEntries,
                                             Set<DaxTag> reqTagSet)
    {
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        DaxTrailer trailer = new DaxTrailer();

        for (Object entry : normalizeToList(daxDataEntries)) {
            if (entry != null && entry.getClass().isAnnotationPresent(DaxpEntity.class)) {
                var entityAnn = entry.getClass().getAnnotation(DaxpEntity.class);
                DaxTag tag = tagCodec.decode(entityAnn);
                body.nextBlock(DaxBlockType.BLOCK_INSTANCE);
                objectToMsgBlock(body.getCurrentIdx(), tag, entry, body, reqTagSet, tag);
            }
            else {
                logger.warn("TEST NO DAXP_ENTITY .........");
            }
        }

        return new DaxMessage(head, body, trailer);
    }
    ///----------------------------------------------------------------------------------------
    private void valueToBlock(int blockIdx,DaxBody body, DaxTag tag,Object value, Set<DaxTag> reqTagSet, DaxTag ownerTag){
         logger.trace("valueToBlock > tag {} ", tagCodec.encode(tag));
        if (value == null) {
            body.putPair(blockIdx, new DaxPairString(tag, DaxCoreConstants.OPERATION_NULL,
                                                          DaxCoreConstants.OPERATOR_ACTION));
            return;
        }

        /// check metadata by tag !!!!!!!!!!!
         if (dataModel.isPrimitiveType(tag)){
             System.out.println("PRIMITIVE ");
         }

        if (dataTypeCodec.isPrimitiveType(value)) {
            body.putPair(blockIdx, valueCodec.encodeToPairs(tag, value));
            return;
        }

        if (dataTypeCodec.isCollection(value)) {
            processCollection(blockIdx, tag, body, value, reqTagSet, ownerTag);
            return;
        }

        if (value.getClass().isAnnotationPresent(DaxpEntity.class)) {
            body.nextBlock(DaxBlockType.BLOCK_VALUE);
            int nestedIdx = body.getCurrentIdx();
            body.putTagBlockReference(blockIdx, tag, nestedIdx + 1);
            body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID, ownerTag));
            objectToMsgBlock(nestedIdx, tag, value, body, reqTagSet, ownerTag);
            return;
        }



        ///
        // In future throw should work
        //TMP  throw new DaxException("DAXP-SSS12 No Determinate data type", "valueToBlock");

    }

    ///----------------------------------------------------------------------------------------
    private void objectToMsgBlock(
            int blockIdx,
            DaxTag blockTag,
            Object entry,
            DaxBody body,
            Set<DaxTag> reqTagSet,
            DaxTag ownerTag
    ) {
        if (entry == null) {
             throw new DaxException("DAXP-SSSS1 Field access security exception", "ENTRY is null !!!");
        };  // NOT GOOD IDEA

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
                valueToBlock(blockIdx,body, tag,fieldValue, reqTagSet, ownerTag);

            } catch (IllegalAccessException e) {
                logger.error("Failed to access field {} on {}", annotatedField.field().getName(),
                                                                entry.getClass().getName(), e);
                throw new DaxException("DAXP-SSSS2 Field access security exception", e);
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
                valueToBlock(blockIdx,body, tag,methodValue, reqTagSet, ownerTag);
            } catch (Exception e) {
                logger.error("Failed to invoke method {} on {}", annotatedMethod.method().getName(), entry.getClass().getName(), e);
                throw new DaxException("Method invocation failure", e);
            }
        }
    }
    ///----------------------------------------------------------------------------------------
    private void collectionElementToBlock( int blockIdx, DaxTag tag,DaxBody body,Object key,Object value,
                                          Set<DaxTag> reqTagSet,DaxTag ownerTag)
    {

        body.nextBlock(DaxBlockType.BLOCK_VALUE);
        int nestedIdx = body.getCurrentIdx();
        body.putTagBlockReference(blockIdx, tag, nestedIdx + 1);

        logger.trace("append value tag={}",tag.getTagId());
        appendCollectionItemToBlock(nestedIdx, tag, body, value, COLLECTION_VALUE, reqTagSet, ownerTag);
        if(key != null){
            logger.trace("append key tag={}",tag.getTagId());

            appendCollectionItemToBlock(nestedIdx, tag, body, key,   COLLECTION_KEY,   reqTagSet, ownerTag);
        }
    }
    ///----------------------------------------------------------------------------------------

    private void bulkCollectionToBlock( int blockIdx, DaxTag tag,DaxBody body,Object value,
                                       DaxTag ownerTag)
    {

        body.nextBlock(DaxBlockType.BLOCK_VALUE);
        int nestedIdx = body.getCurrentIdx();
        body.putTagBlockReference(blockIdx, tag, nestedIdx + 1);

        appendCollectionItemToBlock(nestedIdx, tag, body, value, COLLECTION_BULK_VALUE, null, ownerTag);
    }

    ///----------------------------------------------------------------------------------------
    private void processCollection( int blockIdx,DaxTag tag,DaxBody body,Object object,
                                   Set<DaxTag> reqTagSet,DaxTag ownerTag)
    {
        logger.trace("begin processCollection block {}, tag {}", blockIdx, tag.getTagId());

        if (dataTypeCodec.isMap(object)) {
            processMapBlock(blockIdx, tag, body, (Map<?, ?>) object, reqTagSet, ownerTag);
        } else if (object instanceof Iterable<?> iterable) {
            processIterableBlock(blockIdx, tag, body, iterable, reqTagSet, ownerTag);
        } else if (object != null && object.getClass().isArray()) {
            processIterableBlock(blockIdx, tag, body, arrayToIterable(object), reqTagSet, ownerTag);
        } else {
            logger.error("processCollection standard failure for object type: {}",
                    object != null ? object.getClass().getName() : "null");
            throw new DaxException("DAXP-XX432", "Invalid collection payload provided to processCollection");
        }
    }
    ///----------------------------------------------------------------------------------------
    private void processMapBlock(int blockIdx, DaxTag tag, DaxBody body, Map<?, ?> map,
                                Set<DaxTag> reqTagSet, DaxTag ownerTag)
    {
        if (map.size() > BULK_SIZE) {
            bulkCollectionToBlock(blockIdx, tag, body, mapToBulk(map), ownerTag);
        } else {
            map.forEach((key, value) ->
                    collectionElementToBlock(blockIdx, tag, body, key, value, reqTagSet, ownerTag)
            );
        }
    }
    ///----------------------------------------------------------------------------------------
    private void processIterableBlock(int blockIdx, DaxTag tag, DaxBody body, Iterable<?> collection,
                                     Set<DaxTag> reqTagSet, DaxTag ownerTag)
    {
        int size = getIterableSize(collection);

        if (size > BULK_SIZE) {
            bulkCollectionToBlock(blockIdx, tag, body, collectionToBulk(collection), ownerTag);
        } else {
            collection.forEach(objVal ->
                    collectionElementToBlock(blockIdx, tag, body, null, objVal, reqTagSet, ownerTag)
            );
        }
    }
    ///----------------------------------------------------------------------------------------

    /**
     * Determines size without traversing the whole sequence if the instance implements Collection.
     */
    private int getIterableSize(Iterable<?> iterable) {
        if (iterable instanceof Collection<?> col) {
            return col.size();
        }
        int count = 0;
        for (Object ignored : iterable) {
            count++;
            if (count > BULK_SIZE) return count; // Short-circuit early to optimize performance
        }
        return count;
    }
    ///----------------------------------------------------------------------------------------

    private Iterable<?> arrayToIterable(Object array) {
        int length = java.lang.reflect.Array.getLength(array);
        List<Object> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(java.lang.reflect.Array.get(array, i));
        }
        return list;
    }
    ///----------------------------------------------------------------------------------------

    private void appendCollectionItemToBlock(
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
    ///----------------------------------------------------------------------------------------
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
    ///----------------------------------------------------------------------------------------
    private String collectionToBulk(Iterable<?> collection) {
        Iterator<?> iterator = collection.iterator();
        if (!iterator.hasNext()) {
            return "";
        }

        Object first = iterator.next();
        StringBuilder sb = new StringBuilder();
        sb.append(DaxCoreConstants.SEPARATOR_START_OF_TEXT);

        if (dataTypeCodec.isPrimitiveType(first)) {
            // Primitive Collection Mode: Single column without header
            appendPrimitiveRecord(sb, first);
            while (iterator.hasNext()) {
                sb.append(DaxCoreConstants.SEPARATOR_RECORD);
                appendPrimitiveRecord(sb, iterator.next());
            }
        } else {
            // Complex Entity Mode: Header row + Object values
            Class<?> clazz = first.getClass();
            ClassMetadata metadata = getClassMetadata(clazz);

            // 1. Write Header (Tag IDs)
            writeHeader(sb, metadata);

            // 2. Write Records
            writeEntityRecord(sb, first, metadata);
            while (iterator.hasNext()) {
                sb.append(DaxCoreConstants.SEPARATOR_RECORD);
                writeEntityRecord(sb, iterator.next(), metadata);
            }
        }

        sb.append(DaxCoreConstants.SEPARATOR_END_OF_TEXT);
        return sb.toString();
    }
    ///----------------------------------------------------------------------------------------
    private String mapToBulk(Map<?, ?> map) {
        if (map.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(DaxCoreConstants.SEPARATOR_START_OF_TEXT);

        // Pobieramy pierwszy element, aby zbadać typ wartości w mapie
        Map.Entry<?, ?> firstEntry = map.entrySet().iterator().next();
        Object sampleValue = firstEntry.getValue();

        boolean isValueEntity = sampleValue != null &&
                sampleValue.getClass().isAnnotationPresent(DaxpEntity.class);

        // 1. Budowanie Nagłówka
        sb.append(tagCodec.encode(COLLECTION_KEY)).append(DaxCoreConstants.SEPARATOR_UNIT);

        if (isValueEntity) {
            ClassMetadata metadata = getClassMetadata(sampleValue.getClass());
            writeHeaderFields(sb, metadata);
        } else {
            sb.append(tagCodec.encode(COLLECTION_VALUE));
        }

        // 2. Budowanie Rekordów Data
        map.forEach((key, val) -> {
            sb.append(DaxCoreConstants.SEPARATOR_RECORD);
            sb.append(key != null ? key.toString() : "")
                    .append(DaxCoreConstants.SEPARATOR_UNIT);

            if (isValueEntity && val != null) {
                ClassMetadata metadata = getClassMetadata(val.getClass());
                writeEntityRecordValues(sb, val, metadata);
            } else {
                sb.append(val != null ? val.toString() : "");
            }
        });

        sb.append(DaxCoreConstants.SEPARATOR_END_OF_TEXT);
        return sb.toString();
    }

    ///----------------------------------------------------------------------------------------
    private void writeHeader(StringBuilder sb, ClassMetadata metadata) {
        writeHeaderFields(sb, metadata);
        sb.append(DaxCoreConstants.SEPARATOR_RECORD);
    }
    ///----------------------------------------------------------------------------------------
    private void writeHeaderFields(StringBuilder sb, ClassMetadata metadata) {
        boolean firstEntry = true;

        for (AnnotatedField f : metadata.fields()) {
            if (!firstEntry) sb.append(DaxCoreConstants.SEPARATOR_UNIT);
            sb.append(f.tag().getTagId());
            firstEntry = false;
        }

        for (AnnotatedMethod m : metadata.methods()) {
            if (!firstEntry) sb.append(DaxCoreConstants.SEPARATOR_UNIT);
            sb.append(m.tag().getTagId());
            firstEntry = false;
        }
    }

    ///----------------------------------------------------------------------------------------
    private void writeEntityRecord(StringBuilder sb, Object entity, ClassMetadata metadata) {
        if (entity == null) return;
        writeEntityRecordValues(sb, entity, metadata);
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
}