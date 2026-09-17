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
import org.daxprotocol.core.model.pair.DaxPairDataType;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.daxprotocol.core.application.DaxCoreTags.COLLECTION_VALUE;

/**
 * Scans Java classes for DAXP annotations and registers metadata, entities,
 * fields, handlers, and validation rules into internal semantic registries.
 */
public class DaxAnnotationScanner {

    private static final Logger logger = LoggerFactory.getLogger(DaxAnnotationScanner.class);

    private final DaxTagParser tagParser;
    private final DaxConfig config;
    private final DaxSemanticRegistry semanticRegistry;
    private final DaxHandlerRegistry handlerRegistry;
    private final DaxTagCodec tagCodec;
    private final DaxDataTypeCodec dataTypeCodec;
    private final DaxDataTypeService dataTypeService;
    private final DaxSemanticCollector semanticCollector;
    // TODO move to Annotation scanner
    private Map<Class<?>,DaxTag> classDaxTagMap = new HashMap<>();


    /**
     * Constructs a new scanner with necessary protocol services and registries.
     */
    public DaxAnnotationScanner(
            DaxTagParser tagParser,
            DaxConfig config,
            DaxSemanticRegistry semanticRegistry,
            DaxHandlerRegistry handlerRegistry,
            DaxTagCodec tagCodec,
            DaxDataTypeCodec dataTypeCodec,
            DaxDataTypeService dataTypeService,
            DaxSemanticCollector semanticCollector
    ) {
        this.tagParser = tagParser;
        this.config = config;
        this.semanticRegistry = semanticRegistry;
        this.handlerRegistry = handlerRegistry;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
        this.dataTypeService = dataTypeService;
        this.semanticCollector = semanticCollector;
    }

    // =========================================================================
    // PUBLIC API
    // =========================================================================

    /**
     * Main entry point for scanning target classes. Inspects annotations on the class
     * and delegates registration to specific internal methods.
     *
     * @param clazz Target class to scan.
     */
    public void scanAndRegister(Class<?> clazz) {
     //   try {

            if (classDaxTagMap.containsKey(clazz)) {
              return;
            }

            if (clazz.isAnnotationPresent(DaxpManifest.class)) {
                registerManifest(clazz);
            }

            if (clazz.isAnnotationPresent(DaxpRegistry.class)) {
                registerDaxpRegistry(clazz);
            }

            if (clazz.isAnnotationPresent(DaxpEntity.class)) {
                registerEntity(clazz);
            }

            if (clazz.isAnnotationPresent(DaxpCollection.class)) {
                registerCollection(clazz);
            }

            if (clazz.isAnnotationPresent(DaxpController.class)) {
                registerController(clazz);
            }

//        } catch (Exception e) {
//            throw new RuntimeException("Failed to scan and register class: " + clazz.getName(), e);
//        }
    }

    /**
     * Inspects standard Jakarta Bean Validation annotations on a field and reflects
     * constraints (such as nullability and sizes) into the DAXP semantic registry.
     *
     * @param field            Class field being inspected.
     * @param tag              DAXP Tag mapped to the field.
     */
    //todo add Entity information to validation
    private void validationRegister( Field field, DaxTag tag, DaxTag entityTag) {
        boolean isJakartaValidation = Arrays.stream(field.getAnnotations())
                .anyMatch(a -> a.annotationType().getPackageName().startsWith("jakarta.validation"));

        if (!isJakartaValidation) {
            return;
        }

        if (field.isAnnotationPresent(NotNull.class)) {
            semanticCollector.putTagAtrNullable(entityTag,tag, false);
        }

        if (field.isAnnotationPresent(Size.class)) {
            Size size = field.getAnnotation(Size.class);
            if (size.min() > 0) {
                semanticCollector.putTagAtrSizeMin(entityTag,tag, size.min());
            }
            if (size.max() < Integer.MAX_VALUE) {
                semanticCollector.putTagAtrSizeMax(entityTag,tag, size.max());
            }
        }
    }

    // =========================================================================
    // PRIVATE REGISTRATION HELPERS
    // =========================================================================

    /**
     * Registers a single field entry associated with an entity.
     */
    private void registerEntityEntry(
            Field field,
            DaxTag tag,
            DaxTag entityTag,
      //      DaxTagDestiny destiny,
            String annName,
            String annDescription,
            DaxDataType daxDataType)
    {
        logger.trace("RegisterDaxEntry > tag:{} field name:{}", tagCodec.encode(tag), field.getName());

        if (field.getType().equals(Void.class)) {
            logger.error("FIELD IS VOID tag={}", tagCodec.encode(tag));
            return;
        }

   //     semanticRegistry.putTag(tag,  destiny);
        field.setAccessible(true);

        String name = annName.isBlank() ? field.getName() : annName;

        semanticRegistry.putEntityEntry(entityTag, tag);

        semanticCollector.registerName( entityTag,tag ,name);
        semanticCollector.registerDescription( entityTag,tag,  annDescription);
        //------------------------------------
/*
        if(daxDataType!= DaxDataType.NONE){
          semanticCollector.registerDataType(entityTag,tag,daxDataType);
        }
        else {
          semanticCollector.registerDataType(entityTag,tag,dataTypeService.decodeClass(field.getType()));
        }
*/
if(tag.getTagId()==5032){
    System.out.println("jeste");

}

        if(daxDataType == DaxDataType.NONE){
            daxDataType = dataTypeService.decodeClass(field.getType());
        }

        if ( daxDataType.isPrimitiveType() ){
           semanticCollector.registerDataType(tag,entityTag,daxDataType);
        }else {

            System.out.println("DO OPROGRAMOWANI >>"+field.getType().getName()+" tag="+tagCodec.encode(tag)
            +" entity="+tagCodec.encode(  entityTag)
            );

        }


        // Map reference or complex data types

        //////////   poniżej do przebudowy



            if (dataTypeService.isPrimitiveType(field.getType())) {
                semanticCollector.registerDataType(tag, dataTypeService.decodeClass(field.getType()));
            }
            else {
                if (classDaxTagMap.containsKey(field.getType())) {
                    DaxTag refTag = classDaxTagMap.get(field.getType());

                    System.out.println("JEST JEST 11111");

                    semanticCollector.putTagAttributes(tag,
                            Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_TAG_ID, refTag),
                                   new DaxPairDataType(DaxCoreTags.ATR_REF_DATA_TYPE, DaxDataType.ENTITY))

                    );
                }

                else {
                    scanAndRegister(field.getType());  //stackoverflow

                    if (classDaxTagMap.containsKey(field.getType())) {
                        DaxTag refTag =  classDaxTagMap.get(field.getType());

                        System.out.println("JEST JEST 22222");

                        semanticCollector.putTagAttributes(tag,
                                Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_TAG_ID, refTag),
                                        new DaxPairDataType(DaxCoreTags.ATR_REF_DATA_TYPE, DaxDataType.ENTITY))

                        );
                    }else {

/*
 all collection

                        Not registered class= java.util.List
                        DO OPROGRAMOWANI >>java.util.Map tag=5083 entity=5000
                        Not registered class= java.util.Map
                        DO OPROGRAMOWANI >>java.util.Set tag=5089 entity=5000
                        Not registered class= java.util.Set
                        DO OPROGRAMOWANI >>java.util.Set tag=5090 entity=5000
                        Not registered class= java.util.Set
                        DO OPROGRAMOWANI >>java.util.Queue tag=5091 entity=5000
                        Not registered class= java.util.Queue
                        DO OPROGRAMOWANI >>java.util.LinkedList tag=5092 entity=5000
                        Not registered class= java.util.LinkedList
                        DO OPROGRAMOWANI >>org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxTestEnumNoAnnotation tag=5099 entity=5000
                        Not registered class= org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxTestEnumNoAnnotation
                        DO OPROGRAMOWANI >>org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxSubNoTagEntity tag=5100 entity=5000
                        Not registered class= org.daxprotocol.core.unit_test.dax_10_00_annotation.DaxSubNoTagEntity
                        DO OPROGRAMOWANI >>java.util.Map tag=5115 entity=5000
                        Not registered class= java.util.Map
                        DO OPROGRAMOWANI >>java.util.Map tag=5116 entity=5000
                        Not registered class= java.util.Map
                        DO OPROGRAMOWANI >>java.util.Map tag=5117 entity=5000
                        Not registered class= java.util.Map
                        Nazwa: org.daxprotocol.core.unit_test.dax
*/
                    System.out.println("Not registered class= "+field.getType().getName());
//                    semanticRegistry.putTagAttributes(tag,
//                            dataTypeCodec.encode(field.getType(), field.getGenericType()));
                    }
                }



           }




//        else {
//            System.out.println("FIELD READ ONLY");
//        }
        //------------------------------------


//???            semanticRegistry.putTagAtrReadOnly(tag, true);

        // Handle deprecation annotations
        if (field.isAnnotationPresent(Deprecated.class) || field.isAnnotationPresent(DaxpDeprecated.class)) {
            semanticCollector.putEntityEntryAtrDeprecated(entityTag, tag);
        }

        // Check for Jakarta constraints
        validationRegister( field, tag, entityTag);
    }

    /**
     * Registers methods annotated with {@link DaxpValue}.
     */
    private void registerDaxpValue(DaxTag entityTag, Method method) {
        DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
        if (methodAnn == null) {
            return;
        }

        DaxTag tag = tagCodec.decode(methodAnn);
//        semanticRegistry.putTag(tag, DaxTagDestiny.ENTITY_FIELD);

        Class<?> returnClass = method.getReturnType();
        semanticCollector.putTagAttributes(tag, dataTypeCodec.encode(returnClass));
        semanticCollector.putEntityEntryAtrReadOnly(entityTag, tag, true);
        semanticCollector.putEntityEntryAtrDescription(entityTag, tag, methodAnn.description());
        semanticRegistry.putEntityEntry(entityTag, tag);
    }

    /**
     * Registers protocol static message items defined on fields.
     */
    private void registerDaxpMsg(Field field) {
        try {
            DaxpMessage msgAnn = field.getAnnotation(DaxpMessage.class);
            String msgValue = (String) field.get(null);

            DaxMessageItem msg = new DaxMessageItem(msgValue, msgAnn.description());
            logger.info("Register MSG: {} ({})", msgValue, msgAnn.description());

            Arrays.stream(msgAnn.respMsg()).forEach(msg::addRelatedMsgType);
            Arrays.stream(msgAnn.reqTag()).forEach(tagStr ->
                    msg.addReqTag(tagParser.parseDaxTag(tagStr, config.getAppNamespaceId())));

            semanticRegistry.putMsgItem(msg);

        } catch (IllegalAccessException e) {
            throw new DaxAnnotationException("Illegal access while reading message field: " + field.getName(), e);
        }
    }

    /**
     * Registers protocol namespaces.
     */
    private void registerDaxpNamespace(Field field) {
        String symbol = "";
        try {
            symbol = (String) field.get(null);
        } catch (IllegalAccessException e) {
            logger.error("Failed to extract namespace symbol from field: {}", field.getName(), e);
        }

        DaxpNamespace ann = field.getAnnotation(DaxpNamespace.class);
        semanticRegistry.putNamespace(symbol, ann.name(), ann.description());
    }

    /**
     * Processes schema registry classes.
     */
    private void registerDaxpRegistry(Class<?> clazz) {
        //todo getNamespace

        for (Field field : DaxLangTool.allFields(clazz)) {

            if (field.isAnnotationPresent(DaxpField.class)) {
                throw new DaxAnnotationException("Cannot use annotation @DaxpField in Registry class: " + clazz.getName());
            }

            if (field.isAnnotationPresent(DaxpValue.class)) {
                throw new DaxAnnotationException("Cannot use annotation @DaxpValue in Registry class: " + clazz.getName());
            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerTag(field,null);  //todo przekazanć namespace
            }

            if (field.isAnnotationPresent(DaxpMessage.class)) {
                registerDaxpMsg(field);
            }

            if (field.isAnnotationPresent(DaxpNamespace.class)) {
                registerDaxpNamespace(field);
            }

         //   DaxpManifest

        }
    }

    /**
     * Registers manifest information.
     */
    private void registerManifest(Class<?> clazz) {
        for (Field field : DaxLangTool.allFields(clazz)) {
            if (field.isAnnotationPresent(DaxpNamespace.class)) {
                registerDaxpNamespace(field);
            }
        }
    }

    /**
     * Registers individual tags configured directly via {@link DaxpTag}.

     @Target({ ElementType.FIELD })
     public @interface  DaxpTag {
     String namespace() default ""; //>>>> empty mean  DaxpConfig.APP_namespace_SYMBOL;
     DaxDataType daxDataType()  default DaxDataType.UNKNOWN;
     boolean readOnly() default false;
     String description() default "";
     Class<?> clazz() default Void.class;
     }


     */




    private void registerTag(Field field, DaxTag entityTag) {
        DaxpTag tagAnn = field.getAnnotation(DaxpTag.class);
        field.setAccessible(true);
        DaxTag tag = tagCodec.decode(tagAnn, field);

        //DAXPTypeMismatchException rejestracja tagu , jeżeli bez typu . to zapisz NONE , jeże
        // użycie taga w encji jest na jakiś typ wówczas zapisz typ danych przy tagu (jeżeli był NONE)
        // jęzeli w innym miejscu użycie taga o innym type to wyjątek
        //sprawdzić typ danych w semanticRegistry jeżeli nie istanie to wrowadzić uncnow


        if ( !tagAnn.daxDataType().equals(DaxDataType.NONE)
           &&!tagAnn.clazz().equals(Void.class)){
            throw new RuntimeException("DAXPTypeMismatchException  tag registration " + tag.getTagId());
        }

        if (!tagAnn.clazz().equals(Void.class)){
            semanticCollector.registerDataType(tag, dataTypeService.decodeClass( tagAnn.clazz()) );
        }
        else {
            semanticCollector.registerDataType(tag, tagAnn.daxDataType());
        }

        semanticCollector.registerDescription(tag, tagAnn.description());


        validationRegister( field, tag , entityTag);

    }

    /**
     * Processes entity classes annotated with {@link DaxpEntity}.
     */
    private void registerEntity(Class<?> clazz) {
        DaxpEntity entityAnn = clazz.getAnnotation(DaxpEntity.class);

        logger.info("Scanning ENTITY, name: {}, class: {}", entityAnn.name(), clazz.getName());
        DaxTag entityTag = tagCodec.decode(entityAnn);

        String entityName = !entityAnn.name().isBlank() ? entityAnn.name() : clazz.getSimpleName();
        classDaxTagMap.put(clazz, entityTag);

//        semanticRegistry.putTag(entityTag, DaxTagDestiny.ENTITY);
        semanticCollector.putTagAtrName(entityTag, entityName);
        semanticCollector.putTagAtrDescription(entityTag, entityAnn.description());
        semanticCollector.putTagAtrDataType(entityTag, DaxDataType.ENTITY);

        if (clazz.isAnnotationPresent(Deprecated.class) || clazz.isAnnotationPresent(DaxpDeprecated.class)) {
            semanticCollector.putTagAtrDeprecated(entityTag);
        }

        List<Field> allFields = DaxLangTool.allFields(clazz);

        for (Field field : allFields) {
            // Validate conflicting annotations
            if (field.isAnnotationPresent(DaxpField.class) && field.isAnnotationPresent(DaxpValue.class)) {
                throw new DaxAnnotationException("Cannot combine @DaxpField and @DaxpValue annotations on field: " + field.getName());
            }

            if (field.isAnnotationPresent(DaxpField.class) && field.isAnnotationPresent(DaxpTag.class)) {
                throw new DaxAnnotationException("Cannot combine @DaxpField and @DaxpTag annotations on field: " + field.getName());
            }
/*
*
* public @interface DaxpField {
    DaxDataType daxDataType()  default DaxDataType.UNKNOWN;
    String value() default "";      //namespace plus tagId "FIX:53"
    int tagId() default -1;                   // It can be define by @DaxpTag
    String namespace() default "";   // Empty mean  DaxpConfig.APP_namespace_SYMBOL;
    String name() default "";      //Use for rename field name , example :used for JSON cast.
    String description() default "";
}

* */
            if (field.isAnnotationPresent(DaxpField.class)) {
                DaxpField fieldAnn = field.getAnnotation(DaxpField.class);
                DaxTag tag = tagCodec.decode(fieldAnn);
                registerEntityEntry(
                        field,
                        tag,
                        entityTag,
                     //   DaxTagDestiny.ENTITY_FIELD,
                        fieldAnn.name(), // annName.isBlank() ? field.getName() : annName;
                        fieldAnn.description(),
                        fieldAnn.daxDataType()
                );
            }
/*
* public @interface DaxpValue {
    DaxDataType daxDataType()  default DaxDataType.UNKNOWN;
    String value() default "";      //namespace plus tagId "FIX:53"
    int tagId() default -1;                   // It can be define by @DaxpTag
    String namespace() default "";   // Empty mean  DaxpConfig.APP_namespace_SYMBOL;
    String name() default "";      //Use for rename field name , example :used for JSON cast.
    String description() default "";
}
* */
            if (field.isAnnotationPresent(DaxpValue.class)) {
                DaxpValue fieldAnn = field.getAnnotation(DaxpValue.class);
                DaxTag tag = tagCodec.decode(fieldAnn);
                registerEntityEntry(
                        field,
                        tag,
                        entityTag,
                        fieldAnn.name(),
                        fieldAnn.description(),
                        fieldAnn.daxDataType()
                );
            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerTag(field, entityTag);
            }

            if (field.isAnnotationPresent(DaxpMessage.class)) {
                registerDaxpMsg(field);
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            registerDaxpValue(entityTag, method);
        }





       // semanticRegistry.registerClass(clazz, entityTag);
        //---------------
        Class<?>[] declaredClasses = clazz.getDeclaredClasses();

        for (Class<?> innerClass : declaredClasses) {
            if (innerClass.getSimpleName().equals("TestInsideEnum")) {

                // 1. Sprawdzenie typu (czy to Enum)
                boolean isEnum = innerClass.isEnum();
                System.out.println("Nazwa: " + innerClass.getName());
                System.out.println("Czy to enum? " + isEnum);

                // 2. Pobranie adnotacji @DaxpCollection
                if (innerClass.isAnnotationPresent(DaxpCollection.class)) {
                    DaxpCollection annotation = innerClass.getAnnotation(DaxpCollection.class);
                    System.out.println("Tag ID z adnotacji: " + annotation.tagId());
                }

                // 3. Pobranie wartości enuma
                Object[] enumConstants = innerClass.getEnumConstants();
                System.out.println("Wartości enuma:");
                for (Object enumConstant : enumConstants) {
                    System.out.println(" - " + enumConstant);
                }
            }
        }


    }

    /**
     * Scans and registers controller handlers annotated with {@link DaxpController}.
     */
    private void registerController(Class<?> clazz) {
        logger.info("Scanning Daxp Controller: {}", clazz.getName());

        for (Method method : clazz.getDeclaredMethods()) {
            DaxpHandler methodAnn = method.getAnnotation(DaxpHandler.class);
            if (methodAnn == null) {
                continue;
            }
            handlerRegistry.putHandler(methodAnn.value(), method, clazz);
        }
    }

    /**
     * Registers collections or dictionary enums annotated with {@link DaxpCollection}.
     */
    private void registerCollection(Class<?> clazz) {
        DaxpCollection colAtn = clazz.getAnnotation(DaxpCollection.class);
        String name = !colAtn.name().isBlank() ? colAtn.name() : clazz.getSimpleName();
        DaxTag tag = tagCodec.decode(colAtn);

        semanticCollector.putTagAtrName(tag, name);
        semanticCollector.putTagAtrDescription(tag, colAtn.description());
        semanticCollector.putTagAttributes(tag, dataTypeCodec.encode(clazz));

        if (semanticRegistry.isCollectionDictionary(tag)){
                Object[] constants = clazz.getEnumConstants();
                if (constants != null) {
                    for (Object c : constants) {
                        String key = c.toString();
                        semanticRegistry.putCollectionValue(tag, key, new DaxPairString(COLLECTION_VALUE, key));
                    }
                }
            }
        //}
        classDaxTagMap.put(clazz, tag);
    }
}