package org.daxprotocol.core.register;

import org.daxprotocol.core.annotation.*;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.exceptions.DaxAnnotationException;
import org.daxprotocol.core.entity.DaxEntity;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;



//TODO Controlling reusing double tag in this same entity

public class DaxRegisterBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DaxRegisterBuilder.class);
    DaxPopulatorJakartaValidation jakartaPopulator;
    DaxTagParser tagParser;
    DaxPopulatorEnumType enumPopulator;
    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxRegister register;
    DaxHandlerRegistry handlerRegistry;
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    public DaxRegisterBuilder(
            DaxTagParser tagParser ,
            DaxPopulatorEnumType  enumPopulator,
            DaxConfig config,
            DaxContextMapper contextMapper,
            DaxRegister register,
            DaxHandlerRegistry handlerRegistry,
            DaxTagCodec tagCodec,
            DaxDataTypeCodec dataTypeCodec

    ){
        this.tagParser = tagParser;
        this.jakartaPopulator = new DaxPopulatorJakartaValidation();
        this.config = config;
        this.contextMapper = contextMapper;
        this.enumPopulator = enumPopulator;
        this.register = register;
        this.handlerRegistry = handlerRegistry;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
    }




    private void registerDaxpField(Field field,
            DaxTag entityTag
    ){
        DaxTag tag = DaxCoreTags.UNKNOW_TAG;
        Class<?> fType = field.getType();
        //DaxDataType dataType = DaxDataType.fromClass(fType);
        DaxTag dataTypeTag = DaxCoreTags.UNKNOW_TAG;
        String fieldName = "";

        ;

        logger.trace("registerDaxpField > field name:"+ field.getName());




        if (field.isAnnotationPresent(DaxpField.class)) {
            DaxpField daxField = field.getAnnotation(DaxpField.class);

            field.setAccessible(true);

            tag =  tagCodec.decode(daxField);
            fieldName = daxField.name();

        }

        if (field.isAnnotationPresent(DaxpValue.class)) {
            DaxpValue daxpValue = field.getAnnotation(DaxpValue.class);

           // field.setAccessible(true);
            tag =  tagCodec.decode(daxpValue);
            fieldName = daxpValue.name();

        }
        if (fieldName.isBlank()){
           fieldName = field.getName();
        }

//        if(contextId == -1 || tag.equals(DaxCoreTags.UNKNOW_TAG)){
//            throw new DaxAnnotationException("RegisterDaxpFieldException "+field.getName()) ;
//        }



        //Class  change type to char

        if (field.getType().isEnum()){

            if (field.getType().isAnnotationPresent(DaxpDictionary.class)) {
                DaxpDictionary dicAnn = field.getType().getAnnotation(DaxpDictionary.class);

                String typeName = !dicAnn.name().isBlank() ? dicAnn.name() :
                        field.getClass().getSimpleName();

                DaxTag typeTag = tagCodec.decode(dicAnn.value(),dicAnn.context(),  dicAnn.tagId());


                register.putAtrEnumTypeTag(tag, typeTag);
                System.out.println("is Enum ");

            }
            else {
                //TODO
                System.out.println("No annotation ");
            }

            //populateEnumFromFieldAnnotation(field,daxDic);
        }


        register.putTag(tag);
        register.putAtrFieldName(tag, fieldName);
        register.putAtrDataType(tag, dataTypeCodec.decodeClass(field.getType() ));

        if (field.isAnnotationPresent(Deprecated.class)) {
            Deprecated daxpValue = field.getAnnotation(Deprecated.class);
            register.putAtrDeprecated(tag);
        }





//        daxDic.putAtrDataType(tag,dataType.getCode());

//
//        if (dataType.getCode() == DaxDataType.ENTITY.getCode()){
//            DaxpEntity entityAnn = field.getType(). getAnnotation(DaxpEntity.class);
//
//            DaxTag ennSubTag = tagCodec.decode(entityAnn.value(),entityAnn.context(),  entityAnn.tagId());
//
//            //tag =  tagCodec.decode(entityAnn.value(),entityAnn.context(),  entityAnn.tagId());
//
//            dataTypeTag = DaxTag.of( tag.getContextId() ,entityAnn.tagId());
//
//            daxDic.putAtrEntityDataTypeId(tag,dataTypeTag);
//        }
//        else {
//            daxDic.putAtrDataType(tag,dataType.getCode());
//        }


        register.putEntityField( entityTag,tag);


      //  daxDic.putAtrReadOnly( ????);

        jakartaPopulator.populate(register, field, tag );



    }

    private void registerDaxpValueFromEntity(DaxTag entityTag , Method method){



            DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
            if (methodAnn == null) return;

            DaxTag tag  = tagCodec.decode(methodAnn.value(),methodAnn.context(),methodAnn.tagId());
            register.putTag(tag);

            Class<?> returnType = method.getReturnType();

            register.putAtrDataType( tag, dataTypeCodec.encode( returnType));
            register.putAtrReadOnly(tag,true);
            register.putEntityField( entityTag,tag);


    }



    private void registerDaxpMsg(Field field) {
        try {
            DaxpMsg msgAnn = field.getAnnotation(DaxpMsg.class);
            String msgValue = (String) field.get(null);

            DaxMessageItem mgs = new DaxMessageItem(msgValue, msgAnn.description());
            logger.info("Register MSG: {} ( {} )" , msgValue ,msgAnn.description());

            Arrays.stream(msgAnn.respMsg()).forEach(mgs::addRelatedMsgType);

            Arrays.stream(msgAnn.reqTag()).forEach(tagStr ->
                    mgs.addReqTag(tagParser.parseDaxTag(tagStr, config.getAppContextId())));

            register.putMsgItem(mgs);

        } catch (IllegalAccessException e) {
            throw new DaxAnnotationException("IllegalAccessException "+field.getName()) ;
        }
    }
    private void registerDaxpSchema(Class<?> clazz){

        for (Field field : DaxLangTool.allFields(clazz)) {

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTag(field);

            }
            if (field.isAnnotationPresent(DaxpMsg.class)){
                registerDaxpMsg(field);
            }
        }
    }

    private void registerDaxpTag(Field field) {
        // (optional but recommended) only accept static int constants
        if (!Modifier.isStatic(field.getModifiers())) return;

        if (!Modifier.isFinal(field.getModifiers())) return;  //TODO check or set read only

        DaxpTag tagAnn = field.getAnnotation(DaxpTag.class);
        field.setAccessible(true);

        Class<?> tagClazz =  field.getType();
        DaxTag tag;

        System.out.println(tagClazz.toString());


        String value = "";
        int tagId = -1 ;
        try {
            if (field.getType() == String.class) {
               value = (String)(field.get(null));
               //tag = tagParser.parseDaxTag(value,config.getAppContextId());

            }
            else {
                tagId = field.getInt(null);
                //TODO Create DaxContextCodec
//                int contextId = daxTag.context().isBlank() ? config.getAppContextId():
//                        contextMapper.getReferenceId(daxTag.context());
//
//                tag = DaxTag.of(contextId ,tagId);

            }
        }
        catch (Exception e){
            throw new DaxAnnotationException("RegisterDaxpTagException "+field.getName()) ;
        }


          tag =  tagCodec.decode(value,tagAnn.context(),tagId);


         register.putTag(tag );

        //TODO  check tah DataType is exist

        logger.info("description : {}",tagAnn.description());

        if (! tagAnn.clazz().equals(Void.class)) {
            register.putAtrDataType(tag, dataTypeCodec.encode(tagAnn.clazz()));
        }

        if (tagAnn.daxDataType() != DaxDataType.UNKNOWN){
            register.putAtrDataType(tag,tagAnn.daxDataType());
        }

        if (tagAnn.readOnly()) {
            register.putAtrReadOnly(tag,Boolean.TRUE);
        }

        jakartaPopulator.populate(register, field, tag );

    }
    private void registerEntity(Class<?> clazz){

        DaxpEntity entityAnn = clazz.getAnnotation(DaxpEntity.class);


        String entityName = !entityAnn.name().isBlank() ? entityAnn.name() :
                                                        clazz.getSimpleName();


        DaxTag entityTag = tagCodec.decode(entityAnn.value(),entityAnn.context(),entityAnn.tagId());

        register.putEntity(new DaxEntity(entityTag, entityName));

//        register.putAtrDataType(entityTag, dataTypeCodec.encode( DaxDataType.ENTITY));
        register.putAtrDataType(entityTag,  DaxDataType.ENTITY);
        register.putAtrFieldName(entityTag,entityName);

        List<Field> allFields = DaxLangTool.allFields(clazz);

        for (Field field : allFields) {
            if (field.isAnnotationPresent(DaxpField.class)
               ||field.isAnnotationPresent(DaxpValue.class))
            {
                registerDaxpField(field, entityTag);
            }

            if (field.isAnnotationPresent(DaxpTag.class)) {
                registerDaxpTag(field);

            }


            if (field.isAnnotationPresent(DaxpMsg.class)){
                registerDaxpMsg(field);
            }


        }

        for (Method method : clazz.getDeclaredMethods()) {
             registerDaxpValueFromEntity(entityTag, method);
        }

    }


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
                registerDaxpSchema( clazz);
//                return;
            }

            if (clazz.isAnnotationPresent(DaxpEntity.class)) {
                registerEntity(clazz);
//                return;
            }

            if (clazz.isEnum() ||
                clazz.isAnnotationPresent(DaxpDictionary.class))
            {
                enumPopulator.populate( clazz);
//                return;
            }

            if (clazz.isAnnotationPresent(DaxpController.class)) {
               registerController(clazz);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
