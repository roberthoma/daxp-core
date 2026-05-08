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

import org.daxprotocol.core.annotation.*;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.exceptions.DaxAnnotationException;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.tag.DaxTagDestiny;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;


public class DaxDictionaryRegister {
    private static final Logger logger = LoggerFactory.getLogger(DaxDictionaryRegister.class);
    DaxPopulatorJakartaValidation jakartaPopulator;
    DaxTagParser tagParser;
    DaxPopulatorEnumType enumPopulator;
    DaxConfig config;
//    DaxContextMapper contextMapper;
    DaxDictionary dictionary;
    DaxHandlerRegistry handlerRegistry;
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    public DaxDictionaryRegister(
            DaxTagParser tagParser ,
            DaxPopulatorEnumType  enumPopulator,
            DaxConfig config,
//            DaxContextMapper contextMapper,
            DaxDictionary dictionary,
            DaxHandlerRegistry handlerRegistry,
            DaxTagCodec tagCodec,
            DaxDataTypeCodec dataTypeCodec

    ){
        this.tagParser = tagParser;
        this.jakartaPopulator = new DaxPopulatorJakartaValidation();
        this.config = config;
//        this.contextMapper = contextMapper;
        this.enumPopulator = enumPopulator;
        this.dictionary = dictionary;
        this.handlerRegistry = handlerRegistry;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
    }

//    private void  putAtrDeprecated(DaxTag tag, DaxpDeprecated daxpDeprecated){
//        dictionary.putAtrDeprecated(tag);
//    }
//
//    private void  putAtrDeprecated(DaxTag tag, Deprecated deprecated){
//        dictionary.putAtrDeprecated(tag);
//    }


    private void registerDaxpField(Field field,
            DaxTag entityTag,
            DaxRegisterSource source

    ){
        DaxTag tag = DaxCoreTags.UNKNOW_TAG;
        Class<?> fType = field.getType();
        //DaxDataType dataType = DaxDataType.fromClass(fType);
        DaxTag dataTypeTag = DaxCoreTags.UNKNOW_TAG;
        String fieldName = "";
        DaxTagDestiny tagDestiny = DaxTagDestiny.UNKNOW;
         String fieldDesc = "";  //todo refator to optional

        ;

        logger.trace("registerDaxpField > field name:"+ field.getName());




        if (field.isAnnotationPresent(DaxpField.class)) {
            DaxpField daxField = field.getAnnotation(DaxpField.class);

            field.setAccessible(true);

            tag =  tagCodec.decode(daxField);
            fieldName = daxField.name();
            tagDestiny = DaxTagDestiny.FIELD_OR_VALUE;
            fieldDesc = daxField.description();

        }

        if (field.isAnnotationPresent(DaxpValue.class)) {
            DaxpValue daxpValue = field.getAnnotation(DaxpValue.class);

           // field.setAccessible(true);
            tag =  tagCodec.decode(daxpValue);
            fieldName = daxpValue.name();
            tagDestiny = DaxTagDestiny.FIELD_OR_VALUE;
            fieldDesc = daxpValue.description();

        }
        if (fieldName.isBlank()){
           fieldName = field.getName();
        }

//        if(contextId == -1 || tag.equals(DaxCoreTags.UNKNOW_TAG)){
//            throw new DaxAnnotationException("RegisterDaxpFieldException "+field.getName()) ;
//        }



        //Class  change type to char

        if (field.getType().isEnum()){

            if (field.getType().isAnnotationPresent(DaxpCollection.class)) {
                DaxpCollection dicAnn = field.getType().getAnnotation(DaxpCollection.class);

                String typeName = !dicAnn.name().isBlank() ? dicAnn.name() :
                        field.getClass().getSimpleName();

                DaxTag typeTag = tagCodec.decode(dicAnn.value(),dicAnn.context(),  dicAnn.tagId());


                dictionary.putCollectionType(tag, typeTag);
                System.out.println("is Enum >>>>>>>>>>  TO DEVELOP ");
                tagDestiny = DaxTagDestiny.COLLECTION;

            }
            else {
                //TODO
                System.out.println("No annotation ");
            }

            //populateEnumFromFieldAnnotation(field,daxDic);
        }


        dictionary.putTag(tag, source , tagDestiny);
        dictionary.putAtrEntryName(tag, fieldName);

        Type generitType =  field.getGenericType();

        dictionary.putTagAttributes(tag, dataTypeCodec.encode(field.getType() , generitType));

        dictionary.putAtrDescription(entityTag,tag, fieldDesc);
        dictionary.putEntityField( entityTag,tag);


        //TODO develop AtrDeprecated for fields
        if (field.isAnnotationPresent(Deprecated.class)) {
            //putAtrDeprecated(tag, field.getAnnotation(Deprecated.class));
            dictionary.putAtrDeprecated(entityTag,tag);
        }

        if (field.isAnnotationPresent(DaxpDeprecated.class)) {
            //putAtrDeprecated(tag, field.getAnnotation(DaxpDeprecated.class));
            dictionary.putAtrDeprecated(entityTag,tag);
        }


      //  daxDic.putAtrReadOnly( ????);

        jakartaPopulator.populate(dictionary, field, tag );



    }

    private void registerDaxpValueFromEntity(DaxTag entityTag , Method method, DaxRegisterSource source){



            DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
            if (methodAnn == null) return;

            DaxTag tag  = tagCodec.decode(methodAnn.value(),methodAnn.context(),methodAnn.tagId());
            dictionary.putTag(tag,source, DaxTagDestiny.FIELD_OR_VALUE);

            Class<?> returnClass = method.getReturnType();

            dictionary.putTagAttributes( tag, dataTypeCodec.encode( returnClass ));
            dictionary.putAtrReadOnly(entityTag,tag,true);
            dictionary.putAtrDescription(entityTag,tag, "Testowy opis ");//  methodAnn.description());

            dictionary.putEntityField( entityTag,tag);

    }



    private void registerDaxpMsg(Field field, DaxRegisterSource source) {
        try {
            DaxpMsg msgAnn = field.getAnnotation(DaxpMsg.class);
            String msgValue = (String) field.get(null);

            DaxMessageItem mgs = new DaxMessageItem(msgValue, msgAnn.description());
            logger.info("Register MSG: {} ( {} )" , msgValue ,msgAnn.description());

            Arrays.stream(msgAnn.respMsg()).forEach(mgs::addRelatedMsgType);

            Arrays.stream(msgAnn.reqTag()).forEach(tagStr ->
                    mgs.addReqTag(tagParser.parseDaxTag(tagStr, config.getAppContextId())));

            dictionary.putMsgItem(mgs);

        } catch (IllegalAccessException e) {
            throw new DaxAnnotationException("IllegalAccessException "+field.getName()) ;
        }
    }
    private void registerDaxpSchema(Class<?> clazz){

        for (Field field : DaxLangTool.allFields(clazz)) {

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTag(field, DaxRegisterSource.SCHEMA_ANNOTATION);

            }
            if (field.isAnnotationPresent(DaxpMsg.class)){
                registerDaxpMsg(field,DaxRegisterSource.SCHEMA_ANNOTATION);
            }

            if (field.isAnnotationPresent(DaxpField.class)){
                throw new DaxAnnotationException("Can't use annotation DaxpField in Schame class :"+clazz.getName());
            }

            if (field.isAnnotationPresent(DaxpValue.class)){
                throw new DaxAnnotationException("Can't use annotation DaxpValue in Schame class :"+clazz.getName());
            }

        }
    }

    private void registerDaxpTag(Field field,DaxRegisterSource source) {
        // (optional but recommended) only accept static int constants
        if (!Modifier.isStatic(field.getModifiers())) return;

        if (!Modifier.isFinal(field.getModifiers())) return;  //TODO check or set read only

        DaxpTag tagAnn = field.getAnnotation(DaxpTag.class);
        field.setAccessible(true);

        Class<?> tagClazz =  field.getType();
        DaxTag tag;

//        System.out.println(tagClazz.toString());


        String value = "";
        int tagId = -1 ;
        try {
            if (field.getType() == String.class) {
               value = (String)(field.get(null));
            }
            else {
               tagId = field.getInt(null);
            }
        }
        catch (Exception e){
            throw new DaxAnnotationException("RegisterDaxpTagException "+field.getName()) ;
        }


          tag =  tagCodec.decode(value,tagAnn.context(),tagId);


        dictionary.putTag(tag, source, DaxTagDestiny.TAG);

            logger.info("Tag {} description : {}",tagCodec.encode(tag) ,tagAnn.description());

        if (! tagAnn.clazz().equals(Void.class)) {
            dictionary.putTagAttributes(tag, dataTypeCodec.encode(tagAnn.clazz()));
        }

        if (tagAnn.daxDataType() != DaxDataType.UNKNOWN){
            dictionary.putAtrDataType(tag,tagAnn.daxDataType());
        }

        if (tagAnn.readOnly()) {
            dictionary.putAtrReadOnly(tag,Boolean.TRUE);
        }



        jakartaPopulator.populate(dictionary, field, tag );

    }

//    private String getName(Class<?> clazz, Class<A extends Annotation> ann  ){
//
//        String entityName = !entityAnn.name().isBlank() ? entityAnn.name() :
//                clazz.getSimpleName();
//
//    }


    private void registerEntity(Class<?> clazz){

        DaxpEntity entityAnn = clazz.getAnnotation(DaxpEntity.class);


        String entityName = !entityAnn.name().isBlank() ? entityAnn.name() :
                                                        clazz.getSimpleName();


        DaxTag entityTag = tagCodec.decode(entityAnn.value(),entityAnn.context(),entityAnn.tagId());

        dictionary.putTag(entityTag,DaxRegisterSource.ENTITY_ANNOTATION, DaxTagDestiny.ENTITY);
        dictionary.putAtrDataType(entityTag,DaxDataType.ENTITY);
        dictionary.putAtrEntryName(entityTag,entityName);

        if (clazz.isAnnotationPresent(Deprecated.class)) {
            //putAtrDeprecated(entityTag, clazz.getAnnotation(Deprecated.class));
            dictionary.putAtrDeprecated(entityTag);
        }

        if (clazz.isAnnotationPresent(DaxpDeprecated.class)) {
            //putAtrDeprecated(entityTag, clazz.getAnnotation(DaxpDeprecated.class));
            dictionary.putAtrDeprecated(entityTag);
        }


        List<Field> allFields = DaxLangTool.allFields(clazz);

        for (Field field : allFields) {
            if (field.isAnnotationPresent(DaxpField.class)
               ||field.isAnnotationPresent(DaxpValue.class))
            {
                registerDaxpField(field, entityTag, DaxRegisterSource.ENTITY_ANNOTATION);
            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTag(field, DaxRegisterSource.ENTITY_ANNOTATION);

            }


            if (field.isAnnotationPresent(DaxpMsg.class)){
                registerDaxpMsg(field, DaxRegisterSource.ENTITY_ANNOTATION);
            }



        }

        for (Method method : clazz.getDeclaredMethods()) {
             registerDaxpValueFromEntity(entityTag, method,DaxRegisterSource.ENTITY_ANNOTATION);
        }

    }

   //todo
    private void registerController(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            DaxpHandler methodAnn = method.getAnnotation(DaxpHandler.class);
            if (methodAnn == null) continue;
            handlerRegistry.putHandler(methodAnn.value(), method, clazz);
        }


    }


    public void scanAndRegister(Class<?> clazz) {


        try {
            if (clazz.isAnnotationPresent(DaxpSchema.class)) {
                DaxpSchema ann = clazz.getAnnotation(DaxpSchema.class);
                logger.info("Scanning SCEMA : {}", ann.name());
                registerDaxpSchema( clazz);
            }

            if (clazz.isAnnotationPresent(DaxpEntity.class)) {
                DaxpEntity ann = clazz.getAnnotation(DaxpEntity.class);
                logger.info("Scanning ENTITY : {}", ann.name());

                registerEntity(clazz);
            }

            if (clazz.isEnum() ||
                clazz.isAnnotationPresent(DaxpCollection.class))
            {
                logger.info("Scanning Collection : {}", clazz.getName());
                enumPopulator.populate( clazz);
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
