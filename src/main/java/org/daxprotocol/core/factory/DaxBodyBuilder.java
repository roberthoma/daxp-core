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
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.codec.DaxValueCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.registries.AnnotatedField;
import org.daxprotocol.core.registries.AnnotatedMethod;
import org.daxprotocol.core.registries.ClassMetadata;
import org.daxprotocol.core.registries.DaxSemanticRegistry;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.strategy.DaxBulkCollectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxBodyBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DaxBodyBuilder.class);
    private final DaxTagCodec tagCodec;
    private final DaxDataTypeCodec dataTypeCodec;
    private final DaxValueCodec valueCodec;
    private final DaxSemanticRegistry semanticRegistry;
    private final DaxDataTypeService dataTypeService;
    private final DaxBulkCollectionEncoder bulkCollectionBuilder;
    private final DaxBulkCollectionStrategy bulkCollectionStrategy;
    ///----------------------------------------------------------------------------------------
    public DaxBodyBuilder(DaxBulkCollectionStrategy  bulkCollectionStrategy ,DaxTagCodec tagCodec,
            DaxDataTypeCodec dataTypeCodec, DaxValueCodec valueCodec
    ,  DaxSemanticRegistry semanticRegistry, DaxDataTypeService dataTypeService,
       DaxBulkCollectionEncoder bulkCollectionBuilder)
    {
        this.bulkCollectionStrategy = bulkCollectionStrategy;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        this.valueCodec = valueCodec;
        this.semanticRegistry = semanticRegistry;
        this.dataTypeService  = dataTypeService;
        this.bulkCollectionBuilder = bulkCollectionBuilder;
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
    public  DaxBody  toDaxMessageFromObject( Object daxDataEntries,
                                             Set<DaxTag> reqTagSet)
    {
        DaxBody body = new DaxBody();

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

        return body;
    }
    ///----------------------------------------------------------------------------------------
    private void valueToBlock(int blockIdx,DaxBody body, DaxTag tag,Object value, Set<DaxTag> reqTagSet,
                              DaxTag ownerTag)
    {
         logger.trace("valueToBlock > tag {} ", tagCodec.encode(tag));

        if (value == null) {
            body.putPair(blockIdx, new DaxPairString(tag, DaxCoreConstants.OPERATION_NULL,
                                                          DaxCoreConstants.OPERATOR_ACTION));
            return;
        }

        /// check metadata by tag !!!!!!!!!!!
//         if (semanticRegistry.isPrimitiveType(tag)){
//             System.out.println("PRIMITIVE ");
//         }

        if (dataTypeService.isPrimitiveType(value.getClass())) {
            body.putPair(blockIdx, valueCodec.encodeToPairs(tag, value));
            return;
        }

        if (dataTypeService.isDaxpCollection(value)) {
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

        ClassMetadata metadata = semanticRegistry.getClassMetadata(entry.getClass());

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

        body.nextBlock(DaxBlockType.BLOCK_BULK_COLLECTION);
        int nestedIdx = body.getCurrentIdx();
        body.putTagBlockReference(blockIdx, tag, nestedIdx + 1);

        appendCollectionItemToBlock(nestedIdx, tag, body, value, COLLECTION_BULK_DATA, null, ownerTag);
    }



    ///----------------------------------------------------------------------------------------
    private void processCollection( int blockIdx,DaxTag tag,DaxBody body,Object object,
                                   Set<DaxTag> reqTagSet,DaxTag ownerTag)
    {
        logger.trace("begin processCollection block {}, tag {}", blockIdx, tag.getTagId());

        if (bulkCollectionStrategy.isBulkCompose(object)) {
           bulkCollectionToBlock(blockIdx, tag, body, object, ownerTag);
  //          body.putPair(blockIdx, new DaxPair<>(COLLECTION_BULK_DATA,object));
            return;
        }

        if (dataTypeService.isMap(object)) {
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
        if (bulkCollectionStrategy.isBulkCompose(map)) {
            bulkCollectionToBlock(blockIdx, tag, body, bulkCollectionBuilder.mapEncode(map), ownerTag);
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
        if (bulkCollectionStrategy.isBulkCompose(collection)) {
            bulkCollectionToBlock(blockIdx, tag, body, bulkCollectionBuilder.iterableCollectionEncode(collection), ownerTag);
        } else {
            collection.forEach(objVal ->
                    collectionElementToBlock(blockIdx, tag, body, null, objVal, reqTagSet, ownerTag)
            );
        }
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
        if (  dataTypeService.isPrimitiveType(element.getClass())
           || collectionTag.equals(DaxCoreTags.COLLECTION_BULK_DATA)) // ??????????
            {
            body.putPair(nestedIdx, valueCodec.encodeToPairs(collectionTag, element));
            body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID, ownerTag));
            body.putPair(nestedIdx, new DaxPairTag(ENTRY_TAG, tag));
        } else {
            objectToMsgBlock(nestedIdx, tag, element, body, reqTagSet, ownerTag);
        }
    }


   ///----------------------------------------------------------------------------------------


   //todo refactor. move to head codec
   private  final Set<DaxTag> headSet = Set.of(MSG_TYPE,
           MSG_BLOCK_QUANTITY
           //new DaxTag(MSG_namespace)
   );


    public boolean isHeadTag(DaxTag tag){
        return headSet.contains(tag);
    }


    public  DaxBody createBody(int blockCount , List<DaxPair<?>> listOfPair){
        DaxBody body = new DaxBody();

//        if (blockCount==0) {
        body.nextBlock();
//        }
        for(DaxPair pair : listOfPair){
            if(pair.getTag().equals(DaxCoreTags.CHECKSUM)){
                break;
            }
            if (isHeadTag(pair.getTag())){
                continue;
            }
            if (pair.getTag().equals(DaxCoreTags.BLOCK_INDEX)) {
//                if (blockCount==0){
//                    throw new RuntimeException("Body creation Exception : block index and blockCount==0 !!!");
//                }
                body.nextBlock();
            }
            body.putPair(pair);
        }
        return body;
    }



}