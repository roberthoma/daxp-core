package org.daxprotocol.core.register;

import org.daxprotocol.core.annotation.*;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageItem;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.exceptions.DaxAnnotationException;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.dto.DaxDTO;
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

public class DaxAnnotationRegister {
    private static final Logger logger = LoggerFactory.getLogger(DaxAnnotationRegister.class);
    DaxPopulatorJakartaValidation jakartaPopulator;
    DaxTagParser tagParser;
    DaxPopulatorEnumType enumPopulator;
    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxDictionary daxDic;
    DaxHandlerRegistry handlerRegistry;
    public DaxAnnotationRegister(
            DaxTagParser tagParser ,
            DaxPopulatorEnumType  enumPopulator,
            DaxConfig config,
            DaxContextMapper contextMapper,
            DaxDictionary daxDic,
            DaxHandlerRegistry handlerRegistry

    ){
        this.tagParser = tagParser;
        this.jakartaPopulator = new DaxPopulatorJakartaValidation();
        this.config = config;
        this.contextMapper = contextMapper;
        this.enumPopulator = enumPopulator;
        this.daxDic = daxDic;
        this.handlerRegistry = handlerRegistry;
    }

    private DaxTag createTagFromAnn(
                            String value,
                            String context,
                            int tagId
    ){
        DaxTag tag;
        int contextId = context.isBlank() ?
                config.getAppContextId():
                contextMapper.getReferenceId(context);

        if (!value.isBlank()){
            tag = tagParser.parseDaxTag(value,config.getAppContextId());
        }
        else {
            tag = DaxTag.of(contextId ,tagId);
        }


        //TODO check and throw exception


        return tag;
    }

    private void registerDaxpField(Field field,
            DaxTag dtoTag
    ){
        DaxTag tag = DaxCoreTags.UNKNOW_TAG;
//        int contextId = -1;
        Class<?> fType = field.getType();
        DaxDataType dataType = DaxDataType.fromClass(fType);
        DaxTag dataTypeTag = DaxCoreTags.UNKNOW_TAG;

        if (field.isAnnotationPresent(DaxpField.class)) {
            DaxpField daxField = field.getAnnotation(DaxpField.class);

            field.setAccessible(true);

            tag =  createTagFromAnn(daxField.value(),daxField.context(),  daxField.tagId());


        }

        if (field.isAnnotationPresent(DaxpValue.class)) {
            DaxpValue daxpValue = field.getAnnotation(DaxpValue.class);

            field.setAccessible(true);
            tag =  createTagFromAnn(daxpValue.value(),daxpValue.context(),  daxpValue.tagId());


        }




//        if(contextId == -1 || tag.equals(DaxCoreTags.UNKNOW_TAG)){
//            throw new DaxAnnotationException("RegisterDaxpFieldException "+field.getName()) ;
//        }




        //Class  change type to char

        if (field.getType().isEnum()){

            if (field.getType().isAnnotationPresent(DaxpEnum.class)) {
                DaxpEnum enumAtn = field.getType().getAnnotation(DaxpEnum.class);

    //                        String typeName = !typeAtn.name().isBlank() ? typeAtn.name() :
    //                                field.getClass().getSimpleName();

                DaxTag typeTag = DaxTag.of(config.getAppContextId(),enumAtn.tagId());
                daxDic.putAtrEnumTypeTag(tag, typeTag);
                System.out.println("is Enum ");

            }
            else {
                //TODO
                System.out.println("No annotation ");
            }

            //populateEnumFromFieldAnnotation(field,daxDic);
        }


        daxDic.putTag(tag);


//        daxDic.putAtrDataType(tag,dataType.getCode());


        if (dataType.getCode() == DaxDataType.DTO.getCode()){
            DaxpDTO dto = field.getType(). getAnnotation(DaxpDTO.class);

            //            int contextId2 = dto.context().isBlank() ?
//                    config.getAppContextId():
//                    contextMapper.getReferenceId(dto.context());


            tag =  createTagFromAnn(dto.value(),dto.context(),  dto.tagId());

            dataTypeTag = DaxTag.of( tag.getContextId() ,dto.tagId());

            daxDic.putAtrDtoDataTypeId(tag,dataTypeTag);
        }
        else {
            daxDic.putAtrDataType(tag,dataType.getCode());
        }


        daxDic.putDtoField( dtoTag,tag);

      //  daxDic.putAtrReadOnly( ????);

        jakartaPopulator.populate(daxDic, field, tag );

    }

    private void registerDaxpValueFromDTO(DaxTag dtoTag , Class<?> clazz){


        for (Method method : clazz.getDeclaredMethods()) {
            DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
            if (methodAnn == null) continue;

            DaxTag tag;

            int contextId = methodAnn.context().isBlank() ?
                    config.getAppContextId():
                    contextMapper.getReferenceId(methodAnn.context());

            if (!methodAnn.value().isBlank()){
                tag = tagParser.parseDaxTag(methodAnn.value(),config.getAppContextId());
            }
            else {
                tag = DaxTag.of(contextId ,methodAnn.tagId());
            }

            daxDic.putTag(tag);

            Class<?> returnType = method.getReturnType();
            // Object value =  m.invoke(clazz);
            System.out.println(methodAnn.value());
            //   putFieldIntoGroup(field, daxDic, groupId);

            //TODO  DaxpValue methodAnn = field.getAnnotation(DaxpValue.class);
            //TODO DaxpValue as readonly
            daxDic.putAtrDataType( tag, returnType);

            daxDic.putDtoField( dtoTag,tag);
        }

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

            daxDic.putMsgItem(mgs);

        } catch (IllegalAccessException e) {
            throw new DaxAnnotationException("IllegalAccessException "+field.getName()) ;
        }
    }
    private void registerDaxpSchema(Class<?> clazz){

        for (Field field : DaxLangTool.allFields(clazz)) {
//            DaxDictionaryDecoratorService.printDaxFieldInfo(field);

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

        DaxpTag daxTag = field.getAnnotation(DaxpTag.class);
        field.setAccessible(true);

        Class<?> tagClazz =  field.getType();
        DaxTag tag;

        System.out.println(tagClazz.toString());




        try {
            if (field.getType() == String.class) {
               String value = (String)(field.get(null));
               tag = tagParser.parseDaxTag(value,config.getAppContextId());
            }
            else {
                int tagId = -1;
                tagId = field.getInt(null);
                //TODO Create DaxContextCodec
                int contextId = daxTag.context().isBlank() ? config.getAppContextId():
                        contextMapper.getReferenceId(daxTag.context());

                tag = DaxTag.of(contextId ,tagId);


            }
        }
        catch (Exception e){
            throw new DaxAnnotationException("RegisterDaxpTagException "+field.getName()) ;
        }




         daxDic.putTag(tag );

//        if (daxTag.uiLabel()!=null) {
//            daxDic.putAtrUiLabel(tag, daxTag.uiLabel());
//        }

//        if (daxTag.uiLabel()!=null) {
//            daxDic.putAtrUiLabel(tag, daxTag.uiLabel());
//        }

        //TODO  check tah DataType is exist

        logger.info("UiLabel : {}",daxTag.description());

        if (daxTag.dataType().equals("S")) {
            daxDic.putAtrDataType(tag,String.class);
        }

        if (daxTag.clazz() != null) {
            daxDic.putAtrDataType(tag,daxTag.clazz());
        }



        if (daxTag.readOnly()) {
            daxDic.putAtrReadOnly(tag,Boolean.TRUE);
        }

        jakartaPopulator.populate(daxDic, field, tag );

    }
    private void registerDTO(Class<?> clazz){

        DaxpDTO typeAnn = clazz.getAnnotation(DaxpDTO.class);
        String dtoName = !typeAnn.name().isBlank() ? typeAnn.name() :
                                                        clazz.getSimpleName();


        DaxTag dtoTag = DaxTag.of(config.getAppContextId(), typeAnn.tagId());
        daxDic.putDTO(new DaxDTO(dtoTag, dtoName));

        daxDic.putAtrDataType(dtoTag, DaxDataType.DTO.getCode());

        for (Field field : DaxLangTool.allFields(clazz)) {
            if (field.isAnnotationPresent(DaxpField.class)
               ||field.isAnnotationPresent(DaxpValue.class))
            {
                registerDaxpField(field, dtoTag);
            }

        }


        registerDaxpValueFromDTO(dtoTag, clazz);

    }


    private void registerController(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            DaxpHandler methodAnn = method.getAnnotation(DaxpHandler.class);
            if (methodAnn == null) continue;
            handlerRegistry.putHandler(methodAnn.value(), method, clazz);
        }


    }


    public void register(Class<?> clazz) {


        try {
            if (clazz.isAnnotationPresent(DaxpSchema.class)) {
                registerDaxpSchema( clazz);
//                return;
            }

            if (clazz.isAnnotationPresent(DaxpDTO.class)) {
                registerDTO(clazz);
//                return;
            }

            if (clazz.isEnum() ||
                clazz.isAnnotationPresent(DaxpEnum.class))
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
