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
import org.daxprotocol.core.annotation.*;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.group.DaxGroup;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.rules.DaxParserService;
import org.daxprotocol.core.tool.DaxLangTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO dictionary validation method after populateFromAnnotations
// error  example :
 // 1) if any group refer to no existed master group
//TODO create  service  DaxValidationAttributeManager


public class DaxDictionaryPopulator {

    DaxpConfig               config;
    DaxStringReferenceMapper contextMapper;
    DaxParserService         parserService;


    public DaxDictionaryPopulator(DaxpConfig config,
                                  DaxStringReferenceMapper contextMapper,
                                  DaxParserService parserService
    ){
        this.config        = config;
        this.contextMapper = contextMapper;
        this.parserService = parserService;
    }

    private void popJakartaValidationAttribute(DaxDictionary daxDic,Field field ,DaxTag tag){
        boolean isJakartaValidation = Arrays.stream(field.getAnnotations())
                .anyMatch(a -> a.annotationType().getPackageName()
                        .startsWith("jakarta.validation"));

        if (!isJakartaValidation){
            return;
        }

        if (field.isAnnotationPresent(NotNull.class)) {
            daxDic.putAtrNullable(tag, false);
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

    private void populateEnumType(DaxDictionary daxDic, Class<?> clazz ){
        DaxpType groupAtn =  clazz.getAnnotation(DaxpType.class);


        //        DaxDictionaryDecoratorService.printDaxGroupInfo(group);

        String enumName = !groupAtn.name().isBlank() ? groupAtn.name() :
                clazz.getSimpleName();


        DaxTag enumTag = new DaxTag(config.getAppContextId(),groupAtn.tagId());

        daxDic.putEnum(enumTag, new DaxEnum(enumName,groupAtn.description()) );
//        daxDic.getEnumDictionary(). putEnum2(enumTag, DaxLangTool.asEnumClass(clazz)  );

        Object[] constants = clazz.getEnumConstants();

        for (Object c : constants) {
            daxDic.putEnumValue(enumTag, new DaxEnumValue(c.toString(),""));
        }


        System.out.println("Test 123");

//                groupId = groupMapper.getReferenceId(groupAtn.name());

    }


//    private void populateEnumFromTypeAnnotation(Field field , DaxDictionary daxDic){
//
//        for (Object c : constants) {
//            daxDic.putEnumValue(enumName, c.toString(),"");
//        }
//
//    }


    private void populateEnumFromFieldAnnotation(Field field , DaxDictionary daxDic){
//        DaxDictionaryDecoratorService.printDaxEnumInfo(field);

        DaxpField daxp = field.getAnnotation(DaxpField.class);
        field.setAccessible(true);

        DaxTag tag = new DaxTag(config.getAppContextId(),daxp.tagId());

        String enumName = field.getType().getSimpleName();

        daxDic.putAtrEnumName(tag, enumName);

//        String typeName = !groupAtn.name().isBlank() ? groupAtn.name() :
//                clazz.getSimpleName();

        //TODO check exist
        daxDic.putEnum(tag,new DaxEnum(enumName,""));  // to improve

        Object[] constants = field.getType().getEnumConstants();


        for (Object c : constants) {
            daxDic.putEnumValue(tag, new DaxEnumValue(c.toString(),""));
        }

        System.out.println("Test 123");

    }

    private void putFieldIntoGroup(Field field, DaxDictionary daxDic , DaxTag groupTag){

        DaxpField daxField = field.getAnnotation(DaxpField.class);
        field.setAccessible(true);

        int contextId = daxField.context().isBlank() ?
                config.getAppContextId():
                contextMapper.getReferenceId(daxField.context());


        DaxTag tag = new DaxTag(contextId ,daxField.tagId());

        daxDic.putTag(tag);
        //Class  change type to char
        daxDic.putAtrDataType(tag,field.getType());

        if (field.getType().isEnum()){
            /// ////////////////
            if (field.getClass().isAnnotationPresent(DaxpType.class)) {
                DaxpType typeAtn = field.getAnnotation(DaxpType.class);

                        //        DaxDictionaryDecoratorService.printDaxGroupInfo(group);

                        String typeName = !typeAtn.name().isBlank() ? typeAtn.name() :
                                field.getClass().getSimpleName();

                        DaxTag typeTag = new DaxTag(config.getAppContextId(),typeAtn.tagId());

              //  daxDic.putAtr DataType(tag,field.getType());


            }
        /// /////////
            //populateEnumFromFieldAnnotation(field,daxDic);
        }

        popJakartaValidationAttribute(daxDic, field, tag );

        if (daxField.uiLabel()!=null) {
            daxDic.putAtrUiLabel(tag, daxField.uiLabel());
        }

        daxDic.putFieldIntoGroup(tag, groupTag);


    }




//    private void putMethodIntoGroup(Field field, DaxDictionary daxDic , int groupId){
//
//    }


    private void populateDaxpFieldAtGroup(DaxTag groupTag ,DaxDictionary daxDic, Class<?> clazz){


        for (Field field : DaxLangTool.allFields(clazz)) {
//            DaxDictionaryDecoratorService.printDaxFieldInfo(field);
            if (field.isAnnotationPresent(DaxpField.class)) {
                putFieldIntoGroup(field, daxDic, groupTag);
            }
            //TODO  DaxpValue methodAnn = field.getAnnotation(DaxpValue.class);


        }


    }
    private void populateDaxpMethodAtGroup(DaxTag groupTag ,DaxDictionary daxDic, Class<?> clazz){


        for (Method m : clazz.getDeclaredMethods()) {
            DaxpValue methodAnn = m.getAnnotation(DaxpValue.class);
            if (methodAnn == null) continue;

            Class<?> returnType = m.getReturnType();
            // Object value =  m.invoke(clazz);
            System.out.println(methodAnn.tagId());
         //   putFieldIntoGroup(field, daxDic, groupId);

            //TODO DaxpRPC ????

        }

    }



    private void populateDaxpDictionary(DaxDictionary daxDic, Class<?> clazz){
        for (Field field : DaxLangTool.allFields(clazz)) {
//            DaxDictionaryDecoratorService.printDaxFieldInfo(field);

            if (!field.isAnnotationPresent(DaxpTag.class)
             //TODO || !field.isAnnotationPresent(DaxpType.class)
            ) continue;

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
                    contextMapper.getReferenceId(daxTag.context());

            DaxTag tag = new DaxTag(contextId ,tagId);
            daxDic.putTag(tag );

            if (daxTag.uiLabel()!=null) {
                daxDic.putAtrUiLabel(tag, daxTag.uiLabel());
            }

            if (daxTag.uiLabel()!=null) {
                daxDic.putAtrUiLabel(tag, daxTag.uiLabel());
            }

            if (daxTag.dataType().equals("S")) {
                daxDic.putAtrDataType(tag,String.class);
            }

            if (daxTag.readOnly()) {
                daxDic.putAtrReadOnly(tag,Boolean.TRUE);
            }

            popJakartaValidationAttribute(daxDic, field, tag );

        }

    }




//TODO throw Runtim exception of tags, group etc are duplicated
    public void populateFromAnnotations(DaxDictionary daxDic, Class<?> clazz){

        try {
            if (clazz.isAnnotationPresent(DaxpDictionary.class)){
                populateDaxpDictionary(daxDic,clazz);
            }

            if (clazz.isAnnotationPresent(DaxpType.class)){
      //        DaxDictionaryDecoratorService.printDaxScanClass(clazz);

                if (clazz.isEnum()){
                    populateEnumType(daxDic, clazz);
                    return;
                }

                DaxpType groupAtn =  clazz.getAnnotation(DaxpType.class);

      //        DaxDictionaryDecoratorService.printDaxGroupInfo(group);

                String typeName = !groupAtn.name().isBlank() ? groupAtn.name() :
                                   clazz.getSimpleName();


                DaxTag grpTag = new DaxTag(config.getAppContextId(),groupAtn.tagId());
                daxDic.putGroup(new DaxGroup(grpTag, typeName));

                daxDic.putAtrDataType(grpTag, DaxDataType.GROUP.getCode());

//                groupId = groupMapper.getReferenceId(groupAtn.name());

                populateDaxpFieldAtGroup(grpTag,daxDic,clazz);
                populateDaxpMethodAtGroup(grpTag, daxDic,clazz);
            }


        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void populateFromMsgBlock(DaxDictionary daxDic, Map<DaxTag, DaxPair<?>> blockPairMap) {

        String blockType =   blockPairMap.get(DaxTagConst.BLOCK_TYPE).getStrValue();

        if(blockType.equals(DaxBlockType.BLOCK_MESSAGE)){
            DaxMessageItem item = new DaxMessageItem(
                    blockPairMap.get(DaxTagConst.FIELD_VALUE).getStrValue(),
                    blockPairMap.get(DaxTagConst.FIELD_VALUE_DESCRIPTION).getStrValue());

            daxDic.putMsgItem(item);

//            public static final DaxTag MSG_REQUIRED_TAGS        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,151); ; //
//            public static final DaxTag MSG_RESPOND_TAGS         = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,152); ; //
//            public static final DaxTag MSG_REQ_IN_RESPOND_TAGS  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,155); ; //

            return;
        }

        if(blockType.equals(DaxBlockType.BLOCK_ENUM)){

            DaxTag enumTag = parserService.parseDaxTag(
                    blockPairMap.get(DaxTagConst.ENUM_ID).getStrValue()
            ) ;

            daxDic.putTag( enumTag);


            String name = blockPairMap.get(DaxTagConst.ENUM_NAME).getStrValue();
            String desc = "";
            if (blockPairMap.containsKey(DaxTagConst.ENUM_DESCRIPTION)){
                desc = blockPairMap.get(DaxTagConst.ENUM_DESCRIPTION).getStrValue();
            }
            daxDic.putEnum(enumTag, new DaxEnum(name , desc ));
//            String valuesStrList = blockPairMap.get(DaxTagConst.ENUM_VALUE_LIST).getStrValue();
//            List<String>  valueList =  Arrays.stream(valuesStrList
//                                                     .split(DaxpConfig.VALUE_LIST_SEPARATOR.toString()))
//                    .map(String::trim)
//                    .filter(s -> !s.isEmpty())
//                    .collect(Collectors.toList());
//             valueList.forEach(eValue -> daxDic.putEnumValue( enumTag, new DaxEnumValue(eValue , "")));

            return;
        }

        if(blockType.equals(DaxBlockType.BLOCK_ENUM_VALUE)){
            DaxTag enumTag = parserService.parseDaxTag(
                    blockPairMap.get(DaxTagConst.ENUM_ID).getStrValue()
            ) ;

            String name  = blockPairMap.get(DaxTagConst.ENUM_VALUE).getStrValue();

            if (blockPairMap.containsKey(DaxTagConst.ENUM_VALUE_DESCRIPTION)) {
                String value = blockPairMap.get(DaxTagConst.ENUM_VALUE_DESCRIPTION).getStrValue();
                daxDic.putEnumValue(enumTag, new DaxEnumValue(name, value));
            }

            return;
        }



        if(blockType.equals(DaxBlockType.BLOCK_TAG)){

            DaxTag tag = parserService.parseDaxTag(
                               blockPairMap.get(DaxTagConst.FIELD_ID).getStrValue()
                         ) ;

            daxDic.putTag( tag);

            //TODO check if not exist FIELD_DATA_TYPE keep as String with warring

            daxDic.putAtrDataType(tag, blockPairMap.get(DaxTagConst.FIELD_DATA_TYPE).getCharValue());


            if(blockPairMap.containsKey(DaxTagConst.ATR_UI_LABEL)) {
                daxDic.putAtrUiLabel(tag, blockPairMap.get(DaxTagConst.ATR_UI_LABEL).getStrValue());

            }

            if(blockPairMap.containsKey(DaxTagConst.ATR_NULLABLE)) {
                daxDic.putAtrNullable(tag,
                        blockPairMap.get(DaxTagConst.ATR_NULLABLE).getCharValue()=='Y'
                        );
            }


            if(blockPairMap.containsKey(DaxTagConst.ATR_SIZE_MAX)) {
                daxDic.putAtrSizeMax(tag,
                        blockPairMap.get(DaxTagConst.ATR_SIZE_MAX).getIntegerValue()
                );
            }

            if(blockPairMap.containsKey(DaxTagConst.ATR_SIZE_MIN)) {
                daxDic.putAtrSizeMin(tag,
                        blockPairMap.get(DaxTagConst.ATR_SIZE_MIN).getIntegerValue()
                );
            }
            if(blockPairMap.containsKey(DaxTagConst.ATR_READONLY)) {
                daxDic.putAtrReadOnly(tag,
                        blockPairMap.get(DaxTagConst.ATR_READONLY).getBooleanValue()
                );
            }

            if(blockPairMap.containsKey(DaxTagConst.ENUM_NAME)) {
                daxDic.putAtrEnumName(tag,
                        blockPairMap.get(DaxTagConst.ENUM_NAME).getStrValue()
                );
            }

            return;
        }



        if(blockType.equals(DaxBlockType.BLOCK_GROUP)){
            String groupName = blockPairMap.get(DaxTagConst.GROUP_NAME).getStrValue();

            //int groupId = groupMapper.getReferenceId(groupName);
            DaxTag groupTag = parserService.parseDaxTag(
                    blockPairMap.get(DaxTagConst.FIELD_ID).getStrValue()
            ) ;

            //blockPairMap.get(DaxTagConst.FIELD).getStrValue();

            DaxGroup group = new DaxGroup(groupTag,groupName);
            daxDic.putGroup(group);
            String fieldIdStrList = blockPairMap.get(DaxTagConst.FIELD_ID_LIST).getStrValue();
            List<DaxTag>  tagList = parserService.parseDaxTagList(fieldIdStrList);
            tagList.forEach(tag -> daxDic.putFieldIntoGroup(tag, groupTag));
            return;
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
