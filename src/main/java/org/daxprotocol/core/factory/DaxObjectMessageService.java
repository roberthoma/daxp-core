/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
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
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxObjectMessageService {
    private static final Logger logger = LoggerFactory.getLogger(DaxObjectMessageService.class);
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    public DaxObjectMessageService(    DaxTagCodec tagCodec,DaxDataTypeCodec dataTypeCodec){
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
    }

    private void putValueToBlock(int blockIdx,DaxTag tag, DaxBody body, Object object,
                                 Set<DaxTag> reqTagSet, DaxTag ownerTag)
    {
        if (  object.getClass().isAnnotationPresent(DaxpEntity.class))
        {
            body.nextBlock(DaxBlockType.BLOCK_VALUE);
            int nestedIdx = body.getCurrentIdx();
            body.putTagBlockReference(blockIdx, tag, (nestedIdx+1));
           // body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID,ownerTag));
            objectToMsgBlock(nestedIdx, tag,  object,  body , reqTagSet, ownerTag);
        }
        else {
            logger.trace("COL blockIdx={} objName={}",blockIdx,object.getClass().getName());
            if (dataTypeCodec.decodeFromObject(object).equals(DaxDataType.COLLECTION) ){

                Iterator<?> iterator  = ((Collection<?>)object).iterator();

                iterator.forEachRemaining(objVal ->
                    {
                        body.nextBlock(DaxBlockType.BLOCK_VALUE);
                        int nestedIdx = body.getCurrentIdx();
                        body.putTagBlockReference(blockIdx, tag, (nestedIdx+1));


                        if(dataTypeCodec.decodeFromObject(objVal).equals(DaxDataType.STRING)){
                            body.putPair(nestedIdx, dataTypeCodec.convertToValue(COLLECTION_VALUE,objVal ));
                         //   body.putPair(nestedIdx, new DaxPairTag(ENTRY_OWNER_ID,ownerTag));
                            body.putPair(nestedIdx, new DaxPairTag(ENTRY_TAG,tag));
                        }
                        else {
                           objectToMsgBlock(nestedIdx, tag,  objVal,  body , reqTagSet, ownerTag);
                        }
                    }
                );

            }
            else {
               body.putPair(blockIdx, dataTypeCodec.convertToValue(tag,object ));
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

                if(reqTagSet != null && !reqTagSet.contains(tag)){
                    continue;
                }

                object = field.get(entry);

                if(object == null){
                    body.putNullTag(blockIdx,tag);
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
                    body.putNullTag(blockIdx,tag);
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
