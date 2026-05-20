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
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.daxprotocol.core.application.DaxCoreTags.COLLECTION_VALUE;


public class DaxDictionaryRegister {
    private static final Logger logger = LoggerFactory.getLogger(DaxDictionaryRegister.class);
    DaxJakartaValidationRegister jakartaRegister;
    DaxTagParser tagParser;
    DaxConfig config;
    DaxDictionary dictionary;
    DaxHandlerRegistry handlerRegistry;
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    DaxDictionaryRegisterService service;
    public DaxDictionaryRegister(
            DaxTagParser tagParser ,
            DaxConfig config,
            DaxDictionary dictionary,
            DaxHandlerRegistry handlerRegistry,
            DaxTagCodec tagCodec,
            DaxDataTypeCodec dataTypeCodec

    ){
        this.tagParser = tagParser;
        this.jakartaRegister = new DaxJakartaValidationRegister();
        this.config = config;
        this.dictionary = dictionary;
        this.handlerRegistry = handlerRegistry;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        service = new DaxDictionaryRegisterService(dictionary, tagCodec, dataTypeCodec);
    }



    //--------------------------------------------

    private void registerDaxpField( Field           field,
                                    DaxTag             entityTag,
                                    DaxRegisterSource  source
    ){
        logger.trace("registerDaxpField > field name:{}", field.getName());


        //DaxDataType dataType = DaxDataType.fromClass(fType);
        DaxTag dataTypeTag = DaxCoreTags.UNKNOW_TAG;
        String fieldName = "";
        DaxTagDestiny tagDestiny = DaxTagDestiny.UNKNOW;
        String fieldDesc = "";  //todo refator to optional



        DaxAnnotationNote annNote = new DaxAnnotationNote();

        DaxpField daxField = field.getAnnotation(DaxpField.class);

        field.setAccessible(true);

        DaxTag tag = tagCodec.decode(daxField);
        fieldName  = daxField.name().isBlank() ? field.getName(): daxField.name();
        tagDestiny = DaxTagDestiny.FIELD;
        fieldDesc  = daxField.description();

        annNote.setName(daxField.name().isBlank() ? field.getName(): daxField.name());
        annNote.setTag(tag);




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
        dictionary.putEntityEntryAtrName(entityTag, tag, fieldName);

        Type generitType =  field.getGenericType();
        dictionary.putTagAttributes( tag, dataTypeCodec.encode(field.getType() , generitType));
        //dictionary.putEntityTagAttributes(entityTag, tag, dataTypeCodec.encode(field.getType() , generitType));

        //annNote.setDaxDataType(DaxDataType.);????????

        dictionary.putEntityEntryAtrDescription(entityTag,tag, fieldDesc);
        dictionary.putEntityField( entityTag,tag);



        //-----------------------------------------------------------------
        //TODO develop AtrDeprecated for fields
        if (field.isAnnotationPresent(Deprecated.class)) {
            //putAtrDeprecated(tag, field.getAnnotation(DaxpDeprecated.class));
            dictionary.putEntityEntryAtrDeprecated(entityTag,tag);
        }

        if (field.isAnnotationPresent(DaxpDeprecated.class)) {
            //putAtrDeprecated(tag, field.getAnnotation(DaxpDeprecated.class));
            dictionary.putEntityEntryAtrDeprecated(entityTag,tag);
        }
        //-----------------------------------------------------------------

      //  daxDic.putAtrReadOnly( ????);

        service.regByNote(annNote, source, DaxTagDestiny.FIELD);


        jakartaRegister.register(dictionary, field, tag );



    }

    private void registerDaxpValue( Field           field,
                                    DaxTag             entityTag,
                                    DaxRegisterSource  source
    ){
        logger.trace("registerDaxpValue > field name:"+ field.getName());

        DaxTag tag = DaxCoreTags.UNKNOW_TAG;
        Class<?> fType = field.getType();
        //DaxDataType dataType = DaxDataType.fromClass(fType);
        DaxTag dataTypeTag = DaxCoreTags.UNKNOW_TAG;
        String fieldName = "";
        DaxTagDestiny tagDestiny = DaxTagDestiny.UNKNOW;
         String fieldDesc = "";  //todo refator to optional

        ;




        if (field.isAnnotationPresent(DaxpValue.class)) {
            DaxpValue daxpValue = field.getAnnotation(DaxpValue.class);

           // field.setAccessible(true);
            tag =  tagCodec.decode(daxpValue);
            fieldName = daxpValue.name();
            tagDestiny = DaxTagDestiny.FIELD;
            fieldDesc = daxpValue.description();

        }

        if (fieldName.isBlank()){
           fieldName = field.getName();
        }



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
        dictionary.putEntityEntryAtrName(entityTag, tag, fieldName);

        Type generitType =  field.getGenericType();
//        dictionary.putTagAttributes( tag, dataTypeCodec.encode(field.getType() , generitType));
//
        dictionary.putEntityTagAttributes(entityTag, tag, dataTypeCodec.encode(field.getType() , generitType));

        dictionary.putEntityEntryAtrDescription(entityTag,tag, fieldDesc);
        dictionary.putEntityField( entityTag,tag);



        //-----------------------------------------------------------------
        //TODO develop AtrDeprecated for fields
        if (field.isAnnotationPresent(Deprecated.class)) {
            //putAtrDeprecated(tag, field.getAnnotation(DaxpDeprecated.class));
            dictionary.putEntityEntryAtrDeprecated(entityTag,tag);
        }

        if (field.isAnnotationPresent(DaxpDeprecated.class)) {
            //putAtrDeprecated(tag, field.getAnnotation(DaxpDeprecated.class));
            dictionary.putEntityEntryAtrDeprecated(entityTag,tag);
        }
        //-----------------------------------------------------------------

      //  daxDic.putAtrReadOnly( ????);

        jakartaRegister.register(dictionary, field, tag );



    }

    private void registerDaxpValueFromEntity(DaxTag entityTag , Method method, DaxRegisterSource source){



            DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
            if (methodAnn == null) return;

            DaxTag tag  = tagCodec.decode(methodAnn.value(),methodAnn.context(),methodAnn.tagId());
            dictionary.putTag(tag,source, DaxTagDestiny.FIELD);

            Class<?> returnClass = method.getReturnType();

            dictionary.putTagAttributes( tag, dataTypeCodec.encode( returnClass ));
            dictionary.putEntityEntryAtrReadOnly(entityTag,tag,true);
            dictionary.putEntityEntryAtrDescription(entityTag,tag, "Testowy opis ");//  methodAnn.description());

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
        //Add schame

        for (Field field : DaxLangTool.allFields(clazz)) {


            if (field.isAnnotationPresent(DaxpField.class)){
                throw new DaxAnnotationException("Can't use annotation DaxpField in Schame class :"+clazz.getName());
            }

            if (field.isAnnotationPresent(DaxpValue.class)){
                throw new DaxAnnotationException("Can't use annotation DaxpValue in Schame class :"+clazz.getName());
            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTag(field, DaxRegisterSource.SCHEMA);

            }
            if (field.isAnnotationPresent(DaxpMsg.class)){
                registerDaxpMsg(field,DaxRegisterSource.SCHEMA);
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

        annNote.setClazz(tagAnn.clazz());
        annNote.setDescription(tagAnn.description());
        annNote.setDaxDataType(tagAnn.daxDataType());
        annNote.setReadOnly(tagAnn.readOnly());

        service.regByNote(annNote, source, DaxTagDestiny.TAG);


        jakartaRegister.register(dictionary, field, annNote.getTag() );

    }







//    private String getName(Class<?> clazz, Class<A extends Annotation> ann  ){
//
//        String entityName = !entityAnn.name().isBlank() ? entityAnn.name() :
//                clazz.getSimpleName();
//
//    }


    private void registerEntity(Class<?> clazz){
        logger.trace("registerEntity class : {}",clazz.getName());

        DaxpEntity entityAnn = clazz.getAnnotation(DaxpEntity.class);

        DaxAnnotationNote entityNote = new DaxAnnotationNote();
        entityNote.setName(!entityAnn.name().isBlank() ? entityAnn.name() : clazz.getSimpleName());
        entityNote.setTag(tagCodec.decode(entityAnn));
        entityNote.setDaxDataType(DaxDataType.ENTITY);

        service.regByNote(entityNote, DaxRegisterSource.ENTITY, DaxTagDestiny.ENTITY);

        if (clazz.isAnnotationPresent(Deprecated.class)) {
            //putAtrDeprecated(entityTag, clazz.getAnnotation(Deprecated.class));
            dictionary.putTagAtrDeprecated(entityNote.getTag());
        }

        if (clazz.isAnnotationPresent(DaxpDeprecated.class)) {
            //putAtrDeprecated(entityTag, clazz.getAnnotation(DaxpDeprecated.class));
            dictionary.putTagAtrDeprecated(entityNote.getTag());
        }

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
                registerDaxpField(field, entityNote.getTag(), DaxRegisterSource.ENTITY);
            }

            if (field.isAnnotationPresent(DaxpValue.class))
            {
                registerDaxpValue(field, entityNote.getTag(), DaxRegisterSource.ENTITY);
            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTag(field, DaxRegisterSource.ENTITY);

            }


            if (field.isAnnotationPresent(DaxpMsg.class)){
                registerDaxpMsg(field, DaxRegisterSource.ENTITY);
            }



        }

        for (Method method : clazz.getDeclaredMethods()) {
             registerDaxpValueFromEntity(entityNote.getTag(), method,DaxRegisterSource.ENTITY);
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


    private void registerCollection( Class<?> clazz ){
        DaxpCollection colAtn =  clazz.getAnnotation(DaxpCollection.class);
        String name = !colAtn.name().isBlank() ? colAtn.name() :
                clazz.getSimpleName();

        DaxTag colTag =  tagCodec.decode(colAtn.value(),colAtn.context(),colAtn.tagId());


        dictionary.putCollectionAtrName(colTag, name);
        dictionary.putCollectionAtrDescription(colTag, colAtn.description());


        dictionary.putCollectionAttributes(colTag, dataTypeCodec.encode(clazz));

        Map<DaxTag, DaxPair<?>> atrMap = dictionary.getCollectionAttributes().getAttributMap().get(colTag);
        if(atrMap.containsKey(DaxCoreTags.COLLECTION_IS_DICTIONARY)){
            if(atrMap.get(DaxCoreTags.COLLECTION_IS_DICTIONARY).getBooleanValue()){

                Object[] constants = clazz.getEnumConstants();

                if (constants != null){
                    for (Object c : constants) {
                        String key = c.toString();
                        dictionary.putCollectionValue(colTag, key, new DaxPairString(COLLECTION_VALUE,c.toString()));
                    }
                }


            }
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

            if (clazz.isEnum() || clazz.equals(Enum.class) ||
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
