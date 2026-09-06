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

            if (semanticRegistry.isClassRegistered(clazz)) {
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
     * @param semanticRegistry Target semantic registry to store validation rules.
     * @param field            Class field being inspected.
     * @param tag              DAXP Tag mapped to the field.
     */
    //todo add Entity information to validation
    private void validationRegister(DaxSemanticRegistry semanticRegistry, Field field, DaxTag tag) {
        boolean isJakartaValidation = Arrays.stream(field.getAnnotations())
                .anyMatch(a -> a.annotationType().getPackageName().startsWith("jakarta.validation"));

        if (!isJakartaValidation) {
            return;
        }

        if (field.isAnnotationPresent(NotNull.class)) {
            semanticRegistry.putTagAtrNullable(tag, false);
        }

        if (field.isAnnotationPresent(Size.class)) {
            Size size = field.getAnnotation(Size.class);
            if (size.min() > 0) {
                semanticRegistry.putTagAtrSizeMin(tag, size.min());
            }
            if (size.max() < Integer.MAX_VALUE) {
                semanticRegistry.putTagAtrSizeMax(tag, size.max());
            }
        }
    }

    // =========================================================================
    // PRIVATE REGISTRATION HELPERS
    // =========================================================================

    /**
     * Registers a single field entry associated with an entity.
     */
    private void registerDaxEntry(
            Field field,
            DaxTag tag,
            DaxTag entityTag,
            DaxTagDestiny destiny,
            String annName,
            String annDescription,
            DaxDataType daxDataType)
    {
        logger.trace("RegisterDaxEntry > tag:{} field name:{}", tagCodec.encode(tag), field.getName());

        semanticRegistry.putTag(tag,  destiny);
        field.setAccessible(true);

        String name = annName.isBlank() ? field.getName() : annName;
        semanticRegistry.putEntityField(entityTag, tag);
        semanticRegistry.putEntityEntryAtrName(entityTag, tag, name);
        semanticRegistry.putEntityEntryAtrDescription(entityTag, tag, annDescription);

        // Map reference or complex data types
        if (!field.getType().equals(Void.class)) {

            if (!dataTypeService.isPrimitiveType(field.getType())) {

                if (semanticRegistry.isClassRegistered(field.getType())) {
                    DaxTag refTag = semanticRegistry.getClassTag(field.getType());

//                    throw new RuntimeException("i co  ? ");

                    semanticRegistry.putTagAttributes(tag,
                            Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_DATA_TYPE, refTag)));
                } else {
                    semanticRegistry.putTagAttributes(tag,
                            dataTypeCodec.encode(field.getType(), field.getGenericType()));
                }

            }
            else {
                semanticRegistry.putTagAtrDataType(tag, dataTypeService.decodeClass(field.getType()));
           }

        }



        if (destiny.equals(DaxTagDestiny.ENTITY_VALUE)) {
            semanticRegistry.putTagAtrReadOnly(tag, true);
        }

        // Handle deprecation annotations
        if (field.isAnnotationPresent(Deprecated.class) || field.isAnnotationPresent(DaxpDeprecated.class)) {
            semanticRegistry.putEntityEntryAtrDeprecated(entityTag, tag);
        }

        // Check for Jakarta constraints
        validationRegister(semanticRegistry, field, tag);
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
        semanticRegistry.putTag(tag, DaxTagDestiny.ENTITY_FIELD);

        Class<?> returnClass = method.getReturnType();
        semanticRegistry.putTagAttributes(tag, dataTypeCodec.encode(returnClass));
        semanticRegistry.putEntityEntryAtrReadOnly(entityTag, tag, true);
        semanticRegistry.putEntityEntryAtrDescription(entityTag, tag, methodAnn.description());
        semanticRegistry.putEntityField(entityTag, tag);
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
                registerDaxpTag(field);  //todo przekazanć namespace
            }

            if (field.isAnnotationPresent(DaxpMessage.class)) {
                registerDaxpMsg(field);
            }

            if (field.isAnnotationPresent(DaxpNamespace.class)) {
                registerDaxpNamespace(field);
            }
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




    private void registerDaxpTag(Field field) {
        DaxpTag tagAnn = field.getAnnotation(DaxpTag.class);
        field.setAccessible(true);
        DaxTag tag = tagCodec.decode(tagAnn, field);

        //DAXPTypeMismatchException rejestracja tagu , jeżeli bez typu . to zapisz NONE , jeże
        // użycie taga w encji jest na jakiś typ wówczas zapisz typ danych przy tagu (jeżeli był NONE)
        // jęzeli w innym miejscu użycie taga o innym type to wyjątek

        //sprawdzić typ danych w semanticRegistry jeżeli nie istanie to wrowadzić uncnow





//        semanticRegistry.putTag(tag, DaxTagDestiny.TAG);
//        semanticRegistry.putTagAtrDescription(tag, tagAnn.description());

        if (tagAnn.readOnly()) {
            semanticRegistry.putTagAtrReadOnly(tag, true);
        }


/*
        semanticRegistry.putTagAtrDataType(tag, tagAnn.daxDataType());

        if (!dataTypeService.isPrimitiveType(tagAnn.clazz())) {


            if (semanticRegistry.isClassRegistered(tagAnn.clazz())) {


                semanticRegistry.putTagAttributes(tag, Set.of(new DaxPairTag(
                        DaxCoreTags.ATR_REF_DATA_TYPE,
                        semanticRegistry.getClassTag(tagAnn.clazz())
                )));
            }
        }
*/

        if ( !tagAnn.daxDataType().equals(DaxDataType.UNKNOWN)
           &&!tagAnn.clazz().equals(Void.class)){
            throw new RuntimeException("MISHMASH  tag registration " + tag.getTagId());
        }

//        if (!tagAnn.daxDataType().equals(DaxDataType.UNKNOWN)){
//
//        }
        if (!tagAnn.clazz().equals(Void.class)){
            semanticCollector.registerTag(tag, tagAnn.clazz() ,tagAnn.description());
        }
        else {
            semanticCollector.registerTag(tag, tagAnn.daxDataType() ,tagAnn.description());
        }

        validationRegister(semanticRegistry, field, tag);

    }

    /**
     * Processes entity classes annotated with {@link DaxpEntity}.
     */
    private void registerEntity(Class<?> clazz) {
        DaxpEntity entityAnn = clazz.getAnnotation(DaxpEntity.class);

        logger.info("Scanning ENTITY, name: {}, class: {}", entityAnn.name(), clazz.getName());
        DaxTag entityTag = tagCodec.decode(entityAnn);

        String entityName = !entityAnn.name().isBlank() ? entityAnn.name() : clazz.getSimpleName();

        semanticRegistry.putTag(entityTag, DaxTagDestiny.ENTITY);
        semanticRegistry.putTagAtrName(entityTag, entityName);
        semanticRegistry.putTagAtrDataType(entityTag, DaxDataType.ENTITY);

        if (clazz.isAnnotationPresent(Deprecated.class) || clazz.isAnnotationPresent(DaxpDeprecated.class)) {
            semanticRegistry.putTagAtrDeprecated(entityTag);
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
                registerDaxEntry(
                        field,
                        tag,
                        entityTag,
                        DaxTagDestiny.ENTITY_FIELD,
                        fieldAnn.name(),
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
                registerDaxEntry(
                        field,
                        tag,
                        entityTag,
                        DaxTagDestiny.ENTITY_VALUE,
                        fieldAnn.name(),
                        fieldAnn.description(),
                        fieldAnn.daxDataType()
                );
            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTag(field);
            }

            if (field.isAnnotationPresent(DaxpMessage.class)) {
                registerDaxpMsg(field);
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            registerDaxpValue(entityTag, method);
        }

        semanticRegistry.registerClass(clazz, entityTag);
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

        DaxTag tag = tagCodec.decode(colAtn.value(), colAtn.namespace(), colAtn.tagId());

        semanticRegistry.putTagAtrName(tag, name);
        semanticRegistry.putTagAtrDescription(tag, colAtn.description());
        semanticRegistry.putTagAttributes(tag, dataTypeCodec.encode(clazz));

        Map<DaxTag, DaxPair<?>> atrMap = semanticRegistry.getTagAttributeMap().get(tag);

        if (atrMap != null && atrMap.containsKey(DaxCoreTags.COLLECTION_IS_DICTIONARY)) {
            if (atrMap.get(DaxCoreTags.COLLECTION_IS_DICTIONARY).getBooleanValue()) {

                Object[] constants = clazz.getEnumConstants();
                if (constants != null) {
                    for (Object c : constants) {
                        String key = c.toString();
                        semanticRegistry.putCollectionValue(tag, key, new DaxPairString(COLLECTION_VALUE, key));
                    }
                }
            }
        }

        semanticRegistry.registerClass(clazz, tag);
    }
}