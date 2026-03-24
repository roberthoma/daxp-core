package org.daxprotocol.core.dictionary.populator;

import org.daxprotocol.core.annotation.*;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageItem;
import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.group.DaxDTO;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.rules.DaxParserService;
import org.daxprotocol.core.tool.DaxLangTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class DaxPopulatorAnnotation {
    DaxPopulatorJakartaValidation jakartaPopulator;
    DaxParserService parserService;
    DaxPopulatorEnumType enumPopulator;
    DaxpConfig config;
    DaxStringReferenceMapper contextMapper;
    public DaxPopulatorAnnotation(
            DaxParserService parserService ,
            DaxPopulatorEnumType  enumPopulator,
            DaxpConfig config,
            DaxStringReferenceMapper contextMapper

    ){
        this.parserService = parserService;
        jakartaPopulator = new DaxPopulatorJakartaValidation();
        this.config = config;
        this.contextMapper = contextMapper;


        this.enumPopulator = enumPopulator;

    }

    private void populateDaxpField(Field field, DaxDictionary daxDic , DaxTag groupTag){

        DaxpField daxField = field.getAnnotation(DaxpField.class);
        field.setAccessible(true);

        int contextId = daxField.context().isBlank() ?
                config.getAppContextId():
                contextMapper.getReferenceId(daxField.context());


        DaxTag tag = new DaxTag(contextId ,daxField.tagId());

        daxDic.putTag(tag);
        //Class  change type to char
        daxDic.putAtrDataType(tag,field.getType());

        if (field.getType().isEnum()){

            if (field.getType().isAnnotationPresent(DaxpDTO.class)) {
                DaxpDTO typeAtn = field.getType().getAnnotation(DaxpDTO.class);

                        //        DaxDictionaryDecoratorService.printDaxGroupInfo(group);

    //                        String typeName = !typeAtn.name().isBlank() ? typeAtn.name() :
    //                                field.getClass().getSimpleName();

                        DaxTag typeTag = new DaxTag(config.getAppContextId(),typeAtn.tagId());

                daxDic.putAtrEnumTypeTag(tag, typeTag);

                System.out.println("is aaaa ");

            }
            else {
                //TODO
                System.out.println("No annotation ");
            }

            //populateEnumFromFieldAnnotation(field,daxDic);
        }


        if (daxField.uiLabel()!=null) {
            daxDic.putAtrUiLabel(tag, daxField.uiLabel());
        }

        daxDic.putFieldIntoGroup(tag, groupTag);

        jakartaPopulator.populate(daxDic, field, tag );

    }
    private void populateDaxpFieldFromDTO(DaxTag groupTag ,DaxDictionary daxDic, Class<?> clazz){


        for (Field field : DaxLangTool.allFields(clazz)) {
//            DaxDictionaryDecoratorService.printDaxFieldInfo(field);
            if (field.isAnnotationPresent(DaxpField.class)) {
                populateDaxpField(field, daxDic, groupTag);
            }

        }


    }
    private void populateDaxpValueFromDTO(DaxTag groupTag ,DaxDictionary daxDic, Class<?> clazz){


        for (Method m : clazz.getDeclaredMethods()) {
            DaxpValue methodAnn = m.getAnnotation(DaxpValue.class);
            if (methodAnn == null) continue;

            Class<?> returnType = m.getReturnType();
            // Object value =  m.invoke(clazz);
            System.out.println(methodAnn.tagId());
            //   putFieldIntoGroup(field, daxDic, groupId);

            //TODO  DaxpValue methodAnn = field.getAnnotation(DaxpValue.class);
            //TODO DaxpValue as readonly

        }

    }
    private void populateDaxpMsg(DaxDictionary daxDic, Field field) {
        try {
            DaxpMsg msgAnn = field.getAnnotation(DaxpMsg.class);
            String msgValue = (String) field.get(null);

            DaxMessageItem mgs = new DaxMessageItem(msgValue, msgAnn.description());

            System.out.println("Zarejestrowano MSG: " + msgValue + " (" + msgAnn.description() + ")");

            Arrays.stream(msgAnn.respMsg()).forEach(mgs::addRelatedMsgType);
            Arrays.stream(msgAnn.reqTag()).forEach(tagStr ->
                    mgs.addReqTag(parserService.parseDaxTag(tagStr)));

            daxDic.putMsgItem(mgs);

        } catch (IllegalAccessException e) {
            // Obsłuż wyjątek, jeśli pole nie jest dostępne
            e.printStackTrace();
        }
    }
    private void populateDaxpSchema(DaxDictionary daxDic, Class<?> clazz){
        for (Field field : DaxLangTool.allFields(clazz)) {
//            DaxDictionaryDecoratorService.printDaxFieldInfo(field);

            if (field.isAnnotationPresent(DaxpTag.class)) {
                populateDaxpTag(daxDic, field);

            }
            if (field.isAnnotationPresent(DaxpMsg.class)){
                populateDaxpMsg(daxDic, field);
            }
        }
    }

    private void populateDaxpTag(DaxDictionary daxDic, Field field) {
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

        if (daxTag.uiLabel()!=null) {
            daxDic.putAtrUiLabel(tag, daxTag.uiLabel());
        }

        if (daxTag.dataType().equals("S")) {
            daxDic.putAtrDataType(tag,String.class);
        }

        if (daxTag.readOnly()) {
            daxDic.putAtrReadOnly(tag,Boolean.TRUE);
        }

        jakartaPopulator.populate(daxDic, field, tag );

    }
    private void populateDTO(DaxDictionary daxDic,Class<?> clazz){
        DaxpDTO typeAnn = clazz.getAnnotation(DaxpDTO.class);

        //        DaxDictionaryDecoratorService.printDaxGroupInfo(group); // change to logger

        String typeName = !typeAnn.name().isBlank() ? typeAnn.name() :
                                                        clazz.getSimpleName();


        DaxTag grpTag = new DaxTag(config.getAppContextId(), typeAnn.tagId());
        daxDic.putGroup(new DaxDTO(grpTag, typeName));

        daxDic.putAtrDataType(grpTag, DaxDataType.DTO.getCode());

//                groupId = groupMapper.getReferenceId(groupAtn.name());

        populateDaxpFieldFromDTO(grpTag, daxDic, clazz);
        populateDaxpValueFromDTO(grpTag, daxDic, clazz);

    }


    private void populateController(DaxDictionary daxDic,Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            DaxpHandler methodAnn = method.getAnnotation(DaxpHandler.class);
            if (methodAnn == null) continue;
            daxDic.putHandler(methodAnn.value(), method, clazz);
        }
    }


    public void populate(DaxDictionary daxDic,Class<?> clazz) {
        try {
            if (clazz.isAnnotationPresent(DaxpSchema.class)) {
                populateDaxpSchema(daxDic, clazz);
                return;
            }

            if (clazz.isAnnotationPresent(DaxpDTO.class)) {
                //        DaxDictionaryDecoratorService.printDaxScanClass(clazz);

                if (clazz.isEnum()) {
                    enumPopulator.populate(daxDic, clazz);
                    return;
                }
                populateDTO(daxDic,clazz);
                return;
            }

            if (clazz.isAnnotationPresent(DaxpController.class)) {
               populateController(daxDic, clazz);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
