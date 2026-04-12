package org.daxprotocol.core.populator;

import org.daxprotocol.core.annotation.*;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageItem;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.exceptions.DaxAnnotationException;
import org.daxprotocol.core.field.DaxDataType;
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
    //DaxParser parserService;
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

    private void registerDaxpField(Field field,
            DaxTag dtoTag
    ){
        String uiLabel="";
        DaxTag tag = DaxCoreTags.UNKNOW_TAG;
        int contextId = -1;
        Class<?> fType = field.getType();
        DaxDataType dataType = DaxDataType.fromClass(fType);
        DaxTag dataTypeTag = DaxCoreTags.UNKNOW_TAG;

        if (field.isAnnotationPresent(DaxpField.class)) {
            DaxpField daxField = field.getAnnotation(DaxpField.class);
            uiLabel = daxField.uiLabel();

            field.setAccessible(true);
            //TODO move to tool  class
             contextId = daxField.context().isBlank() ?
                    config.getAppContextId():
                    contextMapper.getReferenceId(daxField.context());
            tag = new DaxTag(contextId ,daxField.value());

        }

        if (field.isAnnotationPresent(DaxpValue.class)) {
            DaxpValue daxpValue = field.getAnnotation(DaxpValue.class);
            uiLabel = daxpValue.uiLabel();

            field.setAccessible(true);
            //TODO move to tool  class
            contextId = daxpValue.context().isBlank() ?
                    config.getAppContextId():
                    contextMapper.getReferenceId(daxpValue.context());
            tag = new DaxTag(contextId ,daxpValue.value());
        }




        if(contextId == -1 || tag.equals(DaxCoreTags.UNKNOW_TAG)){
            System.out.println("ERRRRRRRRRRRRRRRRRR>>>>>");
            return;
        }




        //Class  change type to char

        if (field.getType().isEnum()){

            if (field.getType().isAnnotationPresent(DaxpEnum.class)) {
                DaxpEnum enumAtn = field.getType().getAnnotation(DaxpEnum.class);

    //                        String typeName = !typeAtn.name().isBlank() ? typeAtn.name() :
    //                                field.getClass().getSimpleName();

                DaxTag typeTag = new DaxTag(config.getAppContextId(),enumAtn.tagId());
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
            int contextId2 = dto.context().isBlank() ?
                    config.getAppContextId():
                    contextMapper.getReferenceId(dto.context());

            dataTypeTag = new DaxTag(contextId ,dto.value());
            daxDic.putAtrDtoDataTypeId(tag,dataTypeTag);
        }
        else {
            daxDic.putAtrDataType(tag,dataType.getCode());
        }

        daxDic.putAtrUiLabel(tag, uiLabel);

        daxDic.putDtoField( dtoTag,tag);

      //  daxDic.putAtrReadOnly( ????);

        jakartaPopulator.populate(daxDic, field, tag );

    }

    private void registerDaxpValueFromDTO(DaxTag dtoTag , Class<?> clazz){


        for (Method method : clazz.getDeclaredMethods()) {
            DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
            if (methodAnn == null) continue;


            int contextId = methodAnn.context().isBlank() ?
                    config.getAppContextId():
                    contextMapper.getReferenceId(methodAnn.context());

            DaxTag tag = new DaxTag(contextId ,methodAnn.value());

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

//        if (daxTag.uiLabel()!=null) {
//            daxDic.putAtrUiLabel(tag, daxTag.uiLabel());
//        }

        //TODO  check tah DataType is exist

        logger.info("UiLabel : {}",daxTag.uiLabel());

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


        DaxTag dtoTag = new DaxTag(config.getAppContextId(), typeAnn.value());
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
        //        DaxDictionaryDecoratorService.printDaxScanClass(clazz);

        try {
            if (clazz.isAnnotationPresent(DaxpSchema.class)) {
                registerDaxpSchema( clazz);
                return;
            }

            if (clazz.isAnnotationPresent(DaxpDTO.class)) {
                registerDTO(clazz);
                return;
            }

            if (clazz.isEnum() ||
                clazz.isAnnotationPresent(DaxpEnum.class))
            {
                enumPopulator.populate( clazz);
                return;
            }

            if (clazz.isAnnotationPresent(DaxpController.class)) {
               registerController(clazz);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
