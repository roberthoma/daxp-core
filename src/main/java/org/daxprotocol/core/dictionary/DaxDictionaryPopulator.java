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
import org.daxprotocol.core.annotation.DaxpDictionary;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpFieldGroup;
import org.daxprotocol.core.annotation.DaxpTag;
import org.daxprotocol.core.codec.DaxDecodeService;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.field.DaxAtrNullable;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxLangTool;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

//TODO dictionary validation method after populateFromAnnotations
// error  example :
// 1) if any group refer to no existed master group
//TODO create  service  DaxValidationAttributeManager

public class DaxDictionaryPopulator {

    DaxpConfig config;
    DaxContextMapper contextMapper;


    public DaxDictionaryPopulator(DaxpConfig config, DaxContextMapper contextMapper){
        this.config = config;
        this.contextMapper = contextMapper;
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
//        DaxDictionaryDecoratorService.printDaxEnumInfo(field);

        DaxpField daxp = field.getAnnotation(DaxpField.class);
        field.setAccessible(true);
        DaxTag tag = new DaxTag(config.getAppContextId(),daxp.tagId());
        String enumName = field.getType().getSimpleName();
        daxDic.putAtrEnumName(tag, enumName);

        //TODO check exist
        daxDic.putEnum(enumName,enumName);  // to improve

        Object[] constants = field.getType().getEnumConstants();

        for (Object c : constants) {
            daxDic.putEnumValue(enumName, c.toString(),"");
        }

    }

    private void populateDaxpFieldGroup(DaxDictionary daxDic, Class<?> clazz){

        int groupId = 0;

//        DaxDictionaryDecoratorService.printDaxScanClass(clazz);
        DaxpFieldGroup group =  clazz.getAnnotation(DaxpFieldGroup.class);

//        DaxDictionaryDecoratorService.printDaxGroupInfo(group);
        groupId = group.groupId();
        group.masterId();

        daxDic.putGroup(groupId, group.name() );


        for (Field field : DaxLangTool.allFields(clazz)) {
//            DaxDictionaryDecoratorService.printDaxFieldInfo(field);
            if (!field.isAnnotationPresent(DaxpField.class)) {
                continue;
            }

            DaxpField daxField = field.getAnnotation(DaxpField.class);
            field.setAccessible(true);

            int contextId = daxField.context().isBlank() ?
                    config.getAppContextId():
                    contextMapper.getContextId(daxField.context());


            DaxTag tag = new DaxTag(contextId ,daxField.tagId());
            //Class  change type to char
            daxDic.putAtrDataType(tag,field.getType());

            if (field.getType().isEnum()){
                populateEnumFromAnnotations(field,daxDic);
            }

            popJakartaValidationAttribute(daxDic, field, tag );

            if (daxField.uiLabel()!=null) {
                daxDic.putAtrUiLabel(tag, daxField.uiLabel());
            }

            daxDic.putFieldIntoGroup(tag, groupId);


        }

    }

    private void populateDaxpDictionary(DaxDictionary daxDic, Class<?> clazz){
        for (Field field : DaxLangTool.allFields(clazz)) {
//            DaxDictionaryDecoratorService.printDaxFieldInfo(field);

            if (!field.isAnnotationPresent(DaxpTag.class)) continue;

            // (optional but recommended) only accept static int constants
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!Modifier.isFinal(field.getModifiers())) continue;

            int tagId = -1;
            try {
                tagId = field.getInt(null);
            }
            catch (Exception e){
                e.printStackTrace();
            }


            DaxpTag daxTag = field.getAnnotation(DaxpTag.class);
            field.setAccessible(true);

            int contextId = daxTag.context().isBlank() ? config.getAppContextId():
                    contextMapper.getContextId(daxTag.context());

            daxDic.putTag( new DaxTag(contextId ,tagId));

        }

    }


    public void populateFromAnnotations(DaxDictionary daxDic, Class<?> clazz){
        // DaxEnumPopulator enumManager = new DaxEnumPopulator();

        try {

            if (clazz.isAnnotationPresent(DaxpFieldGroup.class)){
                populateDaxpFieldGroup(daxDic,clazz);
            }

            if (clazz.isAnnotationPresent(DaxpDictionary.class)){
                populateDaxpDictionary(daxDic,clazz);
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void populateFromMsgBlock(DaxDictionary daxDic, Map<DaxTag, DaxPair<?>> blockPairMap) {

        String blockType =   blockPairMap.get(DaxTagConst.BLOCK_TYPE).getStrValue();

        if(blockType.equals(DaxBlockType.BLOCK_MESSAGE)){
            DaxMessageDicItem item = new DaxMessageDicItem(
                    blockPairMap.get(DaxTagConst.FIELD_VALUE).getStrValue(),
                    blockPairMap.get(DaxTagConst.FIELD_VALUE_DESCRIPTION).getStrValue());

            daxDic.putMsgItem(item);
            return;
        }

        if(blockType.equals(DaxBlockType.BLOCK_ENUM)){
            String name = blockPairMap.get(DaxTagConst.ENUM_NAME).getStrValue();
            String desc = "";
            if (blockPairMap.containsKey(DaxTagConst.ENUM_DESCRIPTION)){
                desc = blockPairMap.get(DaxTagConst.ENUM_DESCRIPTION).getStrValue();
            }
            daxDic.putEnum(name, desc );
        }

        if(blockType.equals(DaxBlockType.BLOCK_ENUM_VALUE)){
            String name  = blockPairMap.get(DaxTagConst.ENUM_NAME).getStrValue();
            String value = blockPairMap.get(DaxTagConst.FIELD_VALUE).getStrValue();

            daxDic.putEnumValue(name,value,"");
        }

        if(blockType.equals(DaxBlockType.BLOCK_GROUP)){
            daxDic.putGroup( Integer.parseInt(blockPairMap.get(DaxTagConst.GROUP_ID).getStrValue()),
                    blockPairMap.get(DaxTagConst.GROUP_NAME).getStrValue());
        }


        if(blockType.equals(DaxBlockType.BLOCK_FIELD)){

            String fieldId = blockPairMap.get(DaxTagConst.FIELD_ID).getStrValue();

            DaxTag tag = DaxDecodeService.parseDaxTag(config.getAppContextId(), fieldId);


            blockPairMap.forEach((integer, daxPair) ->
                    daxDic.putAttribute(tag,daxPair));

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
