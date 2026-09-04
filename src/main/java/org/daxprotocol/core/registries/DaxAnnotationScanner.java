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
package org.daxprotocol.core.registries;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.*;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.exceptions.DaxAnnotationException;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.tag.DaxTagDestiny;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.COLLECTION_VALUE;


public class DaxAnnotationScanner {
    private static final Logger logger = LoggerFactory.getLogger(DaxAnnotationScanner.class);
    DaxTagParser tagParser;
    DaxConfig config;
    DaxSemanticRegistry semanticRegistry;
    DaxHandlerRegistry handlerRegistry;
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    DaxDataTypeService dataTypeService;

    public DaxAnnotationScanner(
            DaxTagParser tagParser ,
            DaxConfig config,
            DaxSemanticRegistry semanticRegistry,
            DaxHandlerRegistry handlerRegistry,
            DaxTagCodec tagCodec,
            DaxDataTypeCodec dataTypeCodec,
            DaxDataTypeService dataTypeService

    ){
        this.tagParser = tagParser;
        this.config = config;
        this.semanticRegistry = semanticRegistry;
        this.handlerRegistry = handlerRegistry;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        this.dataTypeService = dataTypeService;

    }

    //--------------------------------------------

    public void jakartaRegister(DaxSemanticRegistry semanticRegistry, Field field , DaxTag tag){


        boolean isJakartaValidation = Arrays.stream(field.getAnnotations())
                .anyMatch(a -> a.annotationType().getPackageName()
                        .startsWith("jakarta.validation"));

        if (!isJakartaValidation){
            return;
        }

        if (field.isAnnotationPresent(NotNull.class)) {
            semanticRegistry.putTagAtrNullable(tag, false);
        }

        if (field.isAnnotationPresent(Size.class)) {
            Size size = field.getAnnotation(Size.class);
            if (size.min() > 0){
                semanticRegistry.putTagAtrSizeMin(tag, size.min());
            }
            if (size.max() < Integer.MAX_VALUE){
                semanticRegistry.putTagAtrSizeMax(tag, size.max());
            }
        }

    }




    private void registerDaxEntry( Field           field,
                                    DaxTag            tag,
                                    DaxTag             entityTag,
                                    DaxRegisterSource  source,
                                    DaxTagDestiny destiny,
            String annName,
            String annDescription
    ){
        logger.trace("RegisterDaxEntry > tag:{} field name:{}", tagCodec.encode(tag), field.getName());

        semanticRegistry.putTag(tag, source, destiny);

        if (tag.getTagId() == 5032){
            System.out.println("TEST :) 5032");
//            annNote.setReferenceTypeTag(
//        DaxTag refTag =  tagCodec.decode(colAtn);
//
//        semanticRegistry.putTagAttributes(tag
//                , Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_DATA_TYPE, refTag)));
//
//

        }

        field.setAccessible(true);


        String name = annName.isBlank() ? field.getName(): annName;
        semanticRegistry.putEntityField( entityTag,tag);
        semanticRegistry.putEntityEntryAtrName(entityTag, tag, name);
        semanticRegistry.putEntityEntryAtrDescription(entityTag,tag, annDescription);

        if (! field.getType().equals(Void.class)) {

            if (!dataTypeService.isPrimitiveType(field.getType())){
                if ( semanticRegistry.isClassRegistered( field.getType())) {
                DaxTag refTag =     semanticRegistry.getClassTag(field.getType());
                semanticRegistry.putTagAttributes(tag
                        , Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_DATA_TYPE, refTag)));

            }

            else {

                semanticRegistry.putTagAttributes(tag
                        , dataTypeCodec.encode(field.getType(),  field.getGenericType()));
            }
        }




        }


        semanticRegistry.putTagAtrDataType(tag, dataTypeService.decodeClass(field.getType()));

        if (destiny.equals(DaxTagDestiny.ENTITY_VALUE)){
            semanticRegistry.putTagAtrReadOnly(tag, true);
        }


        //-----------------------------------------------------------------
        //TODO develop AtrDeprecated for fields
        if (field.isAnnotationPresent(Deprecated.class)
                || field.isAnnotationPresent(DaxpDeprecated.class)
        )

        {
            semanticRegistry.putEntityEntryAtrDeprecated(entityTag,tag);
        }
        //-----------------------------------------------------------------


        jakartaRegister(semanticRegistry, field, tag );



    }



    private void registerMethodDaxpValue(DaxTag entityTag , Method method, DaxRegisterSource source){



            DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
            if (methodAnn == null) return;

            DaxTag tag  = tagCodec.decode(methodAnn);
            semanticRegistry.putTag(tag,source, DaxTagDestiny.ENTITY_FIELD);

            Class<?> returnClass = method.getReturnType();

            semanticRegistry.putTagAttributes( tag, dataTypeCodec.encode( returnClass ));
            semanticRegistry.putEntityEntryAtrReadOnly(entityTag,tag,true);
            semanticRegistry.putEntityEntryAtrDescription(entityTag,tag, "Testowy opis ");//  methodAnn.description());

            semanticRegistry.putEntityField( entityTag,tag);

    }



    private void registerDaxpMsg(Field field, DaxRegisterSource source) {
        try {
            DaxpMessage msgAnn = field.getAnnotation(DaxpMessage.class);
            String msgValue = (String) field.get(null);

            DaxMessageItem mgs = new DaxMessageItem(msgValue, msgAnn.description());
            logger.info("Register MSG: {} ( {} )" , msgValue ,msgAnn.description());

            Arrays.stream(msgAnn.respMsg()).forEach(mgs::addRelatedMsgType);

            Arrays.stream(msgAnn.reqTag()).forEach(tagStr ->
                    mgs.addReqTag(tagParser.parseDaxTag(tagStr, config.getAppNamespaceId())));

            semanticRegistry.putMsgItem(mgs);

        } catch (IllegalAccessException e) {
            throw new DaxAnnotationException("IllegalAccessException "+field.getName()) ;
        }
    }

    private void registerDaxpNamespace(Field field, DaxRegisterSource source)  {
        String symbol = "";
        try {
          symbol = (String)( field.get(null));
        }
        catch (IllegalAccessException e){
            e.printStackTrace();
        }

        DaxpNamespace ann = field.getAnnotation(DaxpNamespace.class);

        semanticRegistry.putNamespace(symbol , ann.name(), ann.description());
    }

    private void registerDaxpRegistry(Class<?> clazz){
        for (Field field : DaxLangTool.allFields(clazz)) {

            if (field.isAnnotationPresent(DaxpField.class)){
                throw new DaxAnnotationException("Can't use annotation DaxpField in Schame class :"+clazz.getName());
            }

            if (field.isAnnotationPresent(DaxpValue.class)){
                throw new DaxAnnotationException("Can't use annotation DaxpValue in Schame class :"+clazz.getName());
            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTagV2(field, DaxRegisterSource.REGISTRY);

            }
            if (field.isAnnotationPresent(DaxpMessage.class)){
                registerDaxpMsg(field,DaxRegisterSource.REGISTRY);
            }

            if (field.isAnnotationPresent(DaxpNamespace.class)){
                registerDaxpNamespace(field,DaxRegisterSource.REGISTRY);
            }

        }
    }

    private void registerManifest(Class<?> clazz){
        for (Field field : DaxLangTool.allFields(clazz)) {
            if (field.isAnnotationPresent(DaxpNamespace.class)){
                registerDaxpNamespace(field,DaxRegisterSource.REGISTRY);
            }
        }
    }
    //----------------------------------------------------
    private void registerDaxpTagV2(Field field,DaxRegisterSource source) {
        DaxpTag tagAnn            = field.getAnnotation(DaxpTag.class);
        field.setAccessible(true);
        DaxTag tag = tagCodec.decode(tagAnn, field);

        semanticRegistry.putTag(tag, source, DaxTagDestiny.TAG);

        semanticRegistry.putTagAtrDescription(tag, tagAnn.description() );
        if(tagAnn.readOnly()){
            semanticRegistry.putTagAtrReadOnly(tag, true);
        }

        semanticRegistry.putTagAtrDataType(tag,tagAnn.daxDataType());

        if (!dataTypeService.isPrimitiveType(tagAnn.clazz())){
            if ( semanticRegistry.isClassRegistered( tagAnn.clazz())) {

                semanticRegistry.putTagAttributes(tag
                        , Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_DATA_TYPE,
                                semanticRegistry.getClassTag(tagAnn.clazz()
                        ))));
            }

        }
        jakartaRegister(semanticRegistry, field, tag);
    }
   //----------------------------------------------------



    private void registerEntity(Class<?> clazz){

        DaxpEntity entityAnn = clazz.getAnnotation(DaxpEntity.class);

        logger.info("Scanning ENTITY, name: {}, class: {}", entityAnn.name(),clazz.getName());
        DaxTag entityTag =  tagCodec.decode(entityAnn);


        String entityName = !entityAnn.name().isBlank() ? entityAnn.name() : clazz.getSimpleName();

        semanticRegistry.putTag(entityTag, DaxRegisterSource.ENTITY, DaxTagDestiny.ENTITY);

        semanticRegistry.putTagAtrName(  entityTag,entityName);
        semanticRegistry.putTagAtrDataType(entityTag,DaxDataType.ENTITY);

        //-----------------------------------------------------------------
        //TODO develop AtrDeprecated for fields
        if (clazz.isAnnotationPresent(Deprecated.class)
        || clazz.isAnnotationPresent(DaxpDeprecated.class)
        )

        {
            semanticRegistry.putTagAtrDeprecated(entityTag);
        }

        List<Field> allFields = DaxLangTool.allFields(clazz);

        for (Field field : allFields) {

            if (   field.isAnnotationPresent(DaxpField.class)
                && field.isAnnotationPresent(DaxpValue.class))
            {
                throw new DaxAnnotationException("You can't join DaxpField && DaxpValue Annotation ");
            }


            if (   field.isAnnotationPresent(DaxpField.class)
                    && field.isAnnotationPresent(DaxpTag.class))
            {
                throw new DaxAnnotationException("You can't join DaxpField && DaxpTag Annotation ");
            }
            //TODO add other validation



            if (field.isAnnotationPresent(DaxpField.class))
            {


       //         registerDaxpField(field, entityNote.getTag(), DaxRegisterSource.ENTITY);

                DaxpField fieldAnn = field.getAnnotation(DaxpField.class);
                DaxTag tag = tagCodec.decode(fieldAnn);
                registerDaxEntry( field,
                        tag ,
                        entityTag , //entityNote.getTag(),
                        DaxRegisterSource.ENTITY,
                        DaxTagDestiny.ENTITY_FIELD,
                        fieldAnn.name(),
                        fieldAnn.description()
                );

            }

            if (field.isAnnotationPresent(DaxpValue.class))
            {

                DaxpValue fieldAnn = field.getAnnotation(DaxpValue.class);
                DaxTag tag = tagCodec.decode(fieldAnn);
                registerDaxEntry( field,
                        tag,
                        entityTag,//entityNote.getTag(),
                        DaxRegisterSource.ENTITY,
                        DaxTagDestiny.ENTITY_VALUE,
                        fieldAnn.name(),
                        fieldAnn.description()
                );





            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTagV2(field, DaxRegisterSource.ENTITY);

            }


            if (field.isAnnotationPresent(DaxpMessage.class)){
                registerDaxpMsg(field, DaxRegisterSource.ENTITY);
            }



        }

        for (Method method : clazz.getDeclaredMethods()) {
//             registerMethodDaxpValue(entityNote.getTag(), method,DaxRegisterSource.ENTITY);
             registerMethodDaxpValue(entityTag, method,DaxRegisterSource.ENTITY);
        }

//        semanticRegistry.registerClass(clazz, entityNote.getTag());
        semanticRegistry.registerClass(clazz, entityTag);

    }

   //todo
    private void registerController(Class<?> clazz) {
        logger.info("Scanning Daxp Controller : {}", clazz.getName());

        for (Method method : clazz.getDeclaredMethods()) {
            DaxpHandler methodAnn = method.getAnnotation(DaxpHandler.class);
            if (methodAnn == null) continue;
            handlerRegistry.putHandler(methodAnn.value(), method, clazz);
        }


    }


    private void registerCollection( Class<?> clazz ){
        DaxpCollection colAtn =  clazz.getAnnotation(DaxpCollection.class);
        String name = !colAtn.name().isBlank() ? colAtn.name() :
                clazz.getSimpleName();

        DaxTag tag =  tagCodec.decode(colAtn.value(),colAtn.namespace(),colAtn.tagId());

        semanticRegistry.putTagAtrName(tag, name);
        semanticRegistry.putTagAtrDescription(tag, colAtn.description());
        semanticRegistry.putTagAttributes(tag, dataTypeCodec.encode(clazz));

        Map<DaxTag, DaxPair<?>> atrMap = semanticRegistry.getTagAttributeMap().get(tag);

        if(atrMap.containsKey(DaxCoreTags.COLLECTION_IS_DICTIONARY)){
            if(atrMap.get(DaxCoreTags.COLLECTION_IS_DICTIONARY).getBooleanValue()){

                Object[] constants = clazz.getEnumConstants();

                if (constants != null){
                    for (Object c : constants) {
                        String key = c.toString();
                        semanticRegistry.putCollectionValue(tag, key, new DaxPairString(COLLECTION_VALUE,c.toString()));
                    }
                }


            }
        }

        semanticRegistry.registerClass(clazz, tag);


    }


    //-----------------------------------------------------------------
    public void scanAndRegister(Class<?> clazz) {
        try {

            if (clazz.isAnnotationPresent(DaxpManifest.class)) {
                registerManifest(clazz);
            }

            if (clazz.isAnnotationPresent(DaxpRegistry.class)) {
                registerDaxpRegistry( clazz);
            }

            if (clazz.isAnnotationPresent(DaxpEntity.class)) {
                registerEntity(clazz);
            }

            if (//clazz.isEnum() || clazz.equals(Enum.class) ||
                clazz.isAnnotationPresent(DaxpCollection.class))
            {
                registerCollection( clazz);
            }

            if (clazz.isAnnotationPresent(DaxpController.class)) {
               registerController(clazz);
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
