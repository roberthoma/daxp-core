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


    private void registerDaxEntry( Field           field,
                                    DaxTag            tag,
                                    DaxTag             entityTag,
                                    DaxRegisterSource  source,
                                    DaxTagDestiny destiny,
            String annName,
            String annDescription
    ){
        logger.trace("RegisterDaxEntry > field name:{}", field.getName());

        DaxAnnotationNote annNote = new DaxAnnotationNote();

        annNote.setEntityTag(entityTag);
        field.setAccessible(true);

        annNote.setName(annName.isBlank() ? field.getName(): annName );
        annNote.setDescription(annDescription);
        annNote.setTag(tag);
        annNote.setGenericType(field.getGenericType());
        annNote.setClazz(field.getType());

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

        jakartaRegister.register(dictionary, field, tag );



    }

    private void registerDaxpField( Field           field,
            DaxTag             entityTag,
            DaxRegisterSource  source
    ){
        DaxpField fieldAnn = field.getAnnotation(DaxpField.class);
        registerDaxEntry( field,
                tagCodec.decode(fieldAnn),
                entityTag,
                source,
                DaxTagDestiny.ENTITY_FIELD,
                fieldAnn.name(),
                fieldAnn.description()
        );
    }

    private void registerDaxpValue( Field           field,
            DaxTag             entityTag,
            DaxRegisterSource  source
    ){
        DaxpValue fieldAnn = field.getAnnotation(DaxpValue.class);
        registerDaxEntry( field,
                tagCodec.decode(fieldAnn),
                entityTag,
                source,
                DaxTagDestiny.ENTITY_VALUE,
                fieldAnn.name(),
                fieldAnn.description()
        );

    }

    private void registerMethodDaxpValue(DaxTag entityTag , Method method, DaxRegisterSource source){



            DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
            if (methodAnn == null) return;

            DaxTag tag  = tagCodec.decode(methodAnn);
            dictionary.putTag(tag,source, DaxTagDestiny.ENTITY_FIELD);

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
                    mgs.addReqTag(tagParser.parseDaxTag(tagStr, config.getAppnamespaceId())));

            dictionary.putMsgItem(mgs);

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

        DaxpSchema ann = field.getAnnotation(DaxpSchema.class);

        dictionary.putSchema(symbol , ann.name(), ann.description());
    }

    private void registerDaxpManifest(Class<?> clazz){
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

            if (field.isAnnotationPresent(DaxpSchema.class)){
                registerDaxpSchema(field,DaxRegisterSource.SCHEMA);
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

        service.registerByNote(annNote, source, DaxTagDestiny.TAG);


        jakartaRegister.register(dictionary, field, annNote.getTag() );

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
             registerMethodDaxpValue(entityNote.getTag(), method,DaxRegisterSource.ENTITY);
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

        DaxTag colTag =  tagCodec.decode(colAtn.value(),colAtn.namespace(),colAtn.tagId());


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

        //todo do not join DaxpSchema and DaxpEntity


        try {
            if (clazz.isAnnotationPresent(DaxpRegister.class)) {
                DaxpRegister ann = clazz.getAnnotation(DaxpRegister.class);

                registerDaxpManifest( clazz);
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
