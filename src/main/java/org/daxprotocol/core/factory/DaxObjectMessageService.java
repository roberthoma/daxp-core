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
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxObjectMessageService {
    private static final Logger logger = LoggerFactory.getLogger(DaxObjectMessageService.class);
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    DaxValueCodec valueCodec;
    public DaxObjectMessageService(    DaxTagCodec tagCodec,DaxDataTypeCodec dataTypeCodec, DaxValueCodec valueCodec){
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        this.valueCodec = valueCodec;
    }




    private void putValueToBlock(int blockIdx,DaxTag tag, DaxBody body, Object object,
                                 Set<DaxTag> reqTagSet, DaxTag ownerTag)
    {

        if (  object.getClass().isAnnotationPresent(DaxpEntity.class))
        {
            body.nextBlock(DaxBlockType.BLOCK_VALUE);
            int nestedIdx = body.getCurrentIdx();
            body.putTagBlockReference(blockIdx, tag, (nestedIdx+1));
            body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID,ownerTag));
            objectToMsgBlock(nestedIdx, tag,  object,  body , reqTagSet, ownerTag);
        }
        else {
            logger.trace("OBJECT TEST blockIdx={} objName={}",blockIdx,object.getClass().getName());

            if (dataTypeCodec.isCollection(object)){
                logger.trace("IS COLLECTION objName={}",object.getClass().getName());

                Iterator<?> iterator;
                if (dataTypeCodec.isMap_TMP(object) ){
                    iterator  = ((Map<?,?>)object).entrySet().iterator();

                    iterator.forEachRemaining(objVal ->
                            {
                                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) objVal;

                                Object key = entry.getKey();
                                Object value = entry.getValue();
                                body.nextBlock(DaxBlockType.BLOCK_VALUE);

                                int nestedIdx = body.getCurrentIdx();
                                body.putTagBlockReference(blockIdx, tag, (nestedIdx+1));

                                if(dataTypeCodec.isPrimitiveType (value))
                                {
                                    body.putPair(nestedIdx, valueCodec.encodeToPairs(COLLECTION_VALUE,value ));
                                    body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID,ownerTag));
                                    body.putPair(nestedIdx, new DaxPairTag(ENTRY_TAG,tag));
                                }
                                else {
                                    logger.trace("IS NOT primitive 1 {}",object.getClass().getName());
                                    objectToMsgBlock(nestedIdx, tag,  value,  body , reqTagSet, ownerTag);
                                }

                                if(dataTypeCodec.isPrimitiveType (key))
                                {
                                    body.putPair(nestedIdx, valueCodec.encodeToPairs(COLLECTION_KEY,key ));

                                }
                                else {
                                    logger.trace("IS NOT primitive 1.2 {}",object.getClass().getName());
                                    objectToMsgBlock(nestedIdx, tag,  key,  body , reqTagSet, ownerTag);
                                }

                            }
                    );

                }else {

                    iterator  = ((Collection<?>)object).iterator();

                    iterator.forEachRemaining(objVal ->
                        {

                            body.nextBlock(DaxBlockType.BLOCK_VALUE);
                            int nestedIdx = body.getCurrentIdx();
                            body.putTagBlockReference(blockIdx, tag, (nestedIdx+1));

                            if(dataTypeCodec.isPrimitiveType (objVal))
                            {
                                body.putPair(nestedIdx, valueCodec.encodeToPairs(COLLECTION_VALUE,objVal ));
                                body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID,ownerTag));
                                body.putPair(nestedIdx, new DaxPairTag(ENTRY_TAG,tag));
                            }
                            else {
                               logger.trace("IS NOT primitive 2 {}",object.getClass().getName());
                               objectToMsgBlock(nestedIdx, tag,  objVal,  body , reqTagSet, ownerTag);
                            }
                        }
                    );
                }

            }
            else {
               logger.trace("IS VALUE objName={}",object.getClass().getName());
               body.putPair(blockIdx, valueCodec.encodeToPairs(tag,object ));
            }
        }



    }



    public void objectToMsgBlock(int blockIdx,
                                DaxTag blockTag,
                                Object entry,
                                DaxBody body,
                                Set<DaxTag> reqTagSet,
                                DaxTag ownerTag
                               )
    {


        body.putPair(blockIdx, ENTRY_TAG, blockTag);

        try {
            for (Field field : DaxLangTool.allFields(entry.getClass())) {
                DaxTag tag;
                Object object;
                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField fieldAnn = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);
                    tag = tagCodec.decode( fieldAnn);
                }
                else if (field.isAnnotationPresent(DaxpValue.class)) {
                    DaxpValue valueAnn = field.getAnnotation(DaxpValue.class);
                    field.setAccessible(true);
                    tag = tagCodec.decode( valueAnn);
                }
                else {
                    continue;
                }
                ///Tag Selection : TODO develop, check first entry
                if(reqTagSet != null && !reqTagSet.contains(tag)){
                    continue;
                }

                object = field.get(entry);

                if(object == null){
//                    body.putNullTag(blockIdx,tag);
                    body.putPair(blockIdx,new DaxPairString(tag, "N", DaxCoreConstants.OPERATOR_ACTION));
                    continue;
                }

                putValueToBlock(blockIdx, tag,  body, object,reqTagSet, ownerTag);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String methodName = "?";
        try {
            for (Method method : entry.getClass().getDeclaredMethods()) {
                DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
                if (methodAnn == null) continue;

//                Class<?> returnType = method.getReturnType();

                methodName = method.getName();
                DaxTag tag = tagCodec.decode( methodAnn);

                if(reqTagSet != null && !reqTagSet.contains(tag)){
                    continue;
                }
                Object object = method.invoke(entry);

                if(object == null){
//                    body.putNullTag(blockIdx,tag);
                    body.putPair(blockIdx,new DaxPairString(tag, "N", DaxCoreConstants.OPERATOR_ACTION));
                    continue;
                }
                putValueToBlock(blockIdx, tag,  body,
                                object, reqTagSet,ownerTag
                                );
            }
        }
        catch (IllegalAccessException e){
            logger.error("Method {} is not Public ",methodName );
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

}
