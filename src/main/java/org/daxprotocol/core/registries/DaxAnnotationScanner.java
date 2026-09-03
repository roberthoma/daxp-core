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

import static org.daxprotocol.core.application.DaxCoreTags.COLLECTION_VALUE;


public class DaxAnnotationScanner {
    private static final Logger logger = LoggerFactory.getLogger(DaxAnnotationScanner.class);
    DaxJakartaValidationRegistrar jakartaRegister;
    DaxTagParser tagParser;
    DaxConfig config;
    DaxSemanticRegistry semanticRegistry;
    DaxHandlerRegistry handlerRegistry;
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    DaxClassRegisterService service;
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
        this.jakartaRegister = new DaxJakartaValidationRegistrar();
        this.config = config;
        this.semanticRegistry = semanticRegistry;
        this.handlerRegistry = handlerRegistry;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        this.dataTypeService = dataTypeService;
        service = new DaxClassRegisterService(semanticRegistry, tagCodec, dataTypeCodec);
    }



    //--------------------------------------------


    private void registerDaxEntry( Field           field,
                                    DaxTag            tag,
                                    DaxTag             entityTag,
                                    DaxRegisterSource  source,
                                    DaxTagDestiny destiny,
            String annName,
            String annDescription
    ){
        logger.trace("RegisterDaxEntry > tag:{} field name:{}", tagCodec.encode(tag), field.getName());
        DaxAnnotationNote annNote = new DaxAnnotationNote();



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



        annNote.setEntityTag(entityTag);
        field.setAccessible(true);

        annNote.setName(annName.isBlank() ? field.getName(): annName );
        annNote.setDescription(annDescription);
        annNote.setTag(tag);
        annNote.setGenericType(field.getGenericType());

        annNote.setClazz(field.getType());

        if (!dataTypeCodec.isPrimitiveType(field.getType())){
            if ( semanticRegistry.isClassRegistered( field.getType())) {
                annNote.setReferenceTypeTag( semanticRegistry.getClassTag(field.getType()));
            }
//            else {
//                scanAndRegister(field.getType());
//                if ( semanticRegistry.isClassRegistered( field.getType())) {
//                    annNote.setReferenceTypeTag( semanticRegistry.getClassTag(field.getType()));
//                }
//            }


        }


        annNote.setDaxDataType(dataTypeService.decodeClass(field.getType()));

        if (destiny.equals(DaxTagDestiny.ENTITY_VALUE)){
            annNote.setReadOnly(true);

        }


        //-----------------------------------------------------------------
        //TODO develop AtrDeprecated for fields
        if (field.isAnnotationPresent(Deprecated.class)) {
            annNote.setDeprecated(true);
        }
        if (field.isAnnotationPresent(DaxpDeprecated.class)) {
            annNote.setDeprecated(true);
        }
        //-----------------------------------------------------------------


        service.registerByNote(annNote, source, destiny);

        jakartaRegister.register(semanticRegistry, field, tag );



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

    private void registerDaxpSchema(Field field, DaxRegisterSource source)  {
        String symbol = "";
        try {
          symbol = (String)( field.get(null));
        }
        catch (IllegalAccessException e){
            e.printStackTrace();
        }

        DaxpNamespace ann = field.getAnnotation(DaxpNamespace.class);

        semanticRegistry.putSchema(symbol , ann.name(), ann.description());
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
                registerDaxpTag(field, DaxRegisterSource.REGISTRY);

            }
            if (field.isAnnotationPresent(DaxpMessage.class)){
                registerDaxpMsg(field,DaxRegisterSource.REGISTRY);
            }

            if (field.isAnnotationPresent(DaxpNamespace.class)){
                registerDaxpSchema(field,DaxRegisterSource.REGISTRY);
            }

        }
    }


    private void registerDaxpTag(Field field,DaxRegisterSource source) {
// (optional but recommended) only accept static int constants
//        if (!Modifier.isStatic(field.getModifiers())) return;
//        if (!Modifier.isFinal(field.getModifiers())) return;

        DaxpTag tagAnn            = field.getAnnotation(DaxpTag.class);




        DaxAnnotationNote annNote = new DaxAnnotationNote();
        field.setAccessible(true);

        annNote.setTag(tagCodec.decode(tagAnn, field));

        if(annNote.getTag().getTagId() == 5032){
            System.out.println("TTTT");

        }

        annNote.setClazz(tagAnn.clazz());
        annNote.setDescription(tagAnn.description());
        annNote.setDaxDataType(tagAnn.daxDataType());
        annNote.setReadOnly(tagAnn.readOnly());


        if (!dataTypeCodec.isPrimitiveType(tagAnn.clazz())){
            if ( semanticRegistry.isClassRegistered( tagAnn.clazz())) {
                annNote.setReferenceTypeTag( semanticRegistry.getClassTag(tagAnn.clazz()));
            }
//            else {
//                scanAndRegister(field.getType());
//                if ( semanticRegistry.isClassRegistered( field.getType())) {
//                    annNote.setReferenceTypeTag( semanticRegistry.getClassTag(field.getType()));
//                }
//            }


        }

        service.registerByNote(annNote, source, DaxTagDestiny.TAG);


        jakartaRegister.register(semanticRegistry, field, annNote.getTag() );

    }




    private void registerEntity(Class<?> clazz){
        logger.trace("registerEntity class : {}",clazz.getName());

        DaxpEntity entityAnn = clazz.getAnnotation(DaxpEntity.class);

        DaxAnnotationNote entityNote = new DaxAnnotationNote();
        entityNote.setName(!entityAnn.name().isBlank() ? entityAnn.name() : clazz.getSimpleName());
        entityNote.setTag(tagCodec.decode(entityAnn));
        entityNote.setDaxDataType(DaxDataType.ENTITY);
        entityNote.setSchemaOwner(entityAnn.schema());

        //-----------------------------------------------------------------
        //TODO develop AtrDeprecated for fields
        if (clazz.isAnnotationPresent(Deprecated.class)) {
            entityNote.setDeprecated(true);
        }
        if (clazz.isAnnotationPresent(DaxpDeprecated.class)) {
            entityNote.setDeprecated(true);
        }
        //-----------------------------------------------------------------


        service.registerByNote(entityNote, DaxRegisterSource.ENTITY, DaxTagDestiny.ENTITY);

       //----------------------------
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
                        entityNote.getTag(),
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
                        entityNote.getTag(),
                        DaxRegisterSource.ENTITY,
                        DaxTagDestiny.ENTITY_VALUE,
                        fieldAnn.name(),
                        fieldAnn.description()
                );





            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTag(field, DaxRegisterSource.ENTITY);

            }


            if (field.isAnnotationPresent(DaxpMessage.class)){
                registerDaxpMsg(field, DaxRegisterSource.ENTITY);
            }



        }

        for (Method method : clazz.getDeclaredMethods()) {
             registerMethodDaxpValue(entityNote.getTag(), method,DaxRegisterSource.ENTITY);
        }

        semanticRegistry.registerClass(clazz, entityNote.getTag());

    }

   //todo
    private void registerController(Class<?> clazz) {
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

    //TODO develop uniformity checking of class tags with fields
//        if (! annNote.getClazz().equals(Void.class)) {
//
//            //---------
//            //TODO check is reference datatype
//
//            if( annNote.getClazz().isAnnotationPresent(DaxpCollection.class)){
//
//                DaxpCollection dicAnn = annNote.getClazz().getAnnotation(DaxpCollection.class);
//                DaxTag tagTT =  tagCodec.decode(dicAnn);
//                semanticRegistry.putTagAttributes(annNote.getTag()
//                        , Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_DATA_TYPE, tagTT)));
//
//            }
//            else {
//
//               semanticRegistry.putTagAttributes(annNote.getTag()
//                                      , dataTypeCodec.encode(annNote.getClazz(), annNote.getGenericType()));
//            }
//        }

    //-----------------------------------------------------------------
    public void scanAndRegister(Class<?> clazz) {

        //todo do not join DaxpSchema and DaxpEntity


        try {
            if (clazz.isAnnotationPresent(DaxpRegistry.class)) {
                DaxpRegistry ann = clazz.getAnnotation(DaxpRegistry.class);

                registerDaxpRegistry( clazz);
            }

            if (clazz.isAnnotationPresent(DaxpEntity.class)) {

                DaxpEntity ann = clazz.getAnnotation(DaxpEntity.class);
                logger.info("Scanning ENTITY : {}", ann.name());

                registerEntity(clazz);
            }

            if (//clazz.isEnum() || clazz.equals(Enum.class) ||

                    clazz.isAnnotationPresent(DaxpCollection.class))
            {
                logger.info("Scanning Collection : {}", clazz.getName());
                registerCollection( clazz);
            }

            if (clazz.isAnnotationPresent(DaxpController.class)) {
                DaxpController ann = clazz.getAnnotation(DaxpController.class);
                logger.info("Scanning Daxp Controller : {}", clazz.getName());

               registerController(clazz);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
