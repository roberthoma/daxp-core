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
package org.daxprotocol.core.dictionary;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpFieldGroup;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.decorator.DaxDictionaryDecoratorService;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumPopulator;
import org.daxprotocol.core.field.DaxAtrNullable;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;


public class DaxDictionaryPopulator {

    DaxpConfig config;
    //TODO dictionary validation method after populateFromAnnotations
    // error  example :
    // 1) if any group refer to no existed master group


    //TODO create  service  DaxValidationAttributeManager


    public DaxDictionaryPopulator(DaxpConfig config){
        this.config = config;
    }

    private void popJakartaValidationAttribute(DaxDictionary daxDic,Field field ,DaxTag tag){
        boolean isJakartaValidation = Arrays.stream(field.getAnnotations())
                .anyMatch(a -> a.annotationType().getPackageName()
                        .startsWith("jakarta.validation"));

        if (!isJakartaValidation){
            return;
        }

        if (field.isAnnotationPresent(NotNull.class)) {
            daxDic.putAtrNullable(tag, DaxAtrNullable.NULLABLE_FALSE);
        }

        if (field.isAnnotationPresent(Size.class)) {
            Size size = field.getAnnotation(Size.class);
            if (size.min() > 0){
                daxDic.putAtrSizeMin(tag, size.min());
            }
            if (size.max() < Integer.MAX_VALUE){
                daxDic.putAtrSizeMax(tag, size.max());
            }
        }

    }

    public void populateEnumFromAnnotations(Field field , DaxDictionary daxDic){
        DaxDictionaryDecoratorService.printDaxEnumInfo(field);

        DaxpField daxp = field.getAnnotation(DaxpField.class);
        field.setAccessible(true);
        DaxTag tag = new DaxTag(config.getApplicationContextId(),daxp.tagId());
        String enumName = field.getType().getSimpleName();
        daxDic.putAtrEnumName(tag, enumName);

        //TODO check exist
        daxDic.putEnum(enumName,enumName);  // to improve

        Object[] constants = field.getType().getEnumConstants();

        for (Object c : constants) {
            daxDic.putEnumValue(enumName, c.toString(),"");
        }

    }

    public void populateFromAnnotations(DaxDictionary daxDic, Class<?> clazz){
        DaxEnumPopulator enumManager = new DaxEnumPopulator();

        try {
            int groupId = 0;

            if (clazz.isAnnotationPresent(DaxpFieldGroup.class)){
                DaxDictionaryDecoratorService.printDaxScanClass(clazz);
                DaxpFieldGroup group =  clazz.getAnnotation(DaxpFieldGroup.class);

                DaxDictionaryDecoratorService.printDaxGroupInfo(group);
                groupId = group.groupId();
                group.masterId();

                daxDic.putGroup(groupId, group.name() );
            }

            for (Field field : clazz.getDeclaredFields()) {
                DaxDictionaryDecoratorService.printDaxFieldInfo(field);
                if (!field.isAnnotationPresent(DaxpField.class)) {
                   continue;
                }

                DaxpField daxp = field.getAnnotation(DaxpField.class);
                field.setAccessible(true);

                int contextId = daxp.context().isBlank() ? config.getApplicationContextId():
                        DaxContextMapper.getContextId(daxp.context());


                DaxTag tag = new DaxTag(contextId ,daxp.tagId());
                //Class  change type to char
                daxDic.putAtrDataType(tag,field.getType());

                if (field.getType().isEnum()){
                    populateEnumFromAnnotations(field,daxDic);
                }

                popJakartaValidationAttribute(daxDic, field, tag );

                if (daxp.uiLabel()!=null) {
                    daxDic.putAtrUiLabel(tag, daxp.uiLabel());
                }

                daxDic.putAtrGroupId(tag, groupId);


            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void populateFromMsgBlock(DaxDictionary daxDic, Map<DaxTag, DaxPair<?>> blockPairMap) {

       String blockType =   blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.BLOCK_TYPE)).getStrValue();

        if(blockType.equals(DaxBlockType.BLOCK_MESSAGE)){
            DaxMessageDicItem item = new DaxMessageDicItem(
                    blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.FIELD_VALUE)).getStrValue(),
                    blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.FIELD_VALUE_DESCRIPTION)).getStrValue());

            daxDic.putMsgItem(item);
            return;
        }

        if(blockType.equals(DaxBlockType.BLOCK_ENUM)){
            String name = blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.ENUM_NAME)).getStrValue();
            String desc = "";
            if (blockPairMap.containsKey(DaxTag.newPredefineTag(DaxTagConst.ENUM_DESCRIPTION))){
                desc = blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.ENUM_DESCRIPTION)).getStrValue();
            }
            daxDic.putEnum(name, desc );
        }

        if(blockType.equals(DaxBlockType.BLOCK_ENUM_VALUE)){
            String name  = blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.ENUM_NAME)).getStrValue();
            String value = blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.FIELD_VALUE)).getStrValue();

            daxDic.putEnumValue(name,value,"");
        }

        if(blockType.equals(DaxBlockType.BLOCK_GROUP_NAME)){
            daxDic.putGroup( Integer.parseInt(blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.GROUP_ID)).getStrValue()),
                    blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.GROUP_NAME)).getStrValue());
        }


        if(blockType.equals(DaxBlockType.BLOCK_FIELD)){

            int fieldId = Integer.parseInt (blockPairMap.get(DaxTag.newPredefineTag(DaxTagConst.FIELD_ID)).getStrValue());

            blockPairMap.forEach((integer, daxPair) ->
                    daxDic.putAttribute(fieldId,daxPair));

        }


    }

    public void populateFromMessage(DaxDictionary daxDic, DaxMessage message) {

        //TODO get context from head , if not exist default ctx is obligatory

        //message.getHead().

        message.getBody().getBlockMap().forEach((integer, integerDaxPairMap) ->
                  populateFromMsgBlock(daxDic, integerDaxPairMap)
                );
    }



}
