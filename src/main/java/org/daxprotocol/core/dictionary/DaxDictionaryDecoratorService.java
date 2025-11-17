package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.annotation.DaxpFieldGroup;

import java.lang.reflect.Field;

public class DaxDictionaryDecoratorService {


    public static  void printDaxGroupInfo(DaxpFieldGroup group ){
        System.out.println(" >> DaxpGroup ");

        System.out.println("GRP name : " +group.name());
        System.out.println("GRP id : " +group.id());
        System.out.println("GRP master id : " +group.masterId());
        System.out.println("GRP desc : " +group.description());
        System.out.println("GRP namespace : " +group.namespace());
    }

    public static void  printDaxFieldInfo(Field field){
        System.out.println("\n> POP FIELD Name : "+field.getName());
        System.out.println("___> POP FIELD Type Name      : "+field.getType().getTypeName());
        System.out.println("___> POP FIELD Component Type : "+field.getType().getComponentType());
    }

    public static void  printDaxEnumInfo(Field field){
        if (!field.getType().isEnum()){
            throw new RuntimeException("It ["+field.getName()+"] is NOT ENUM field !!!");
        }
        System.out.println("___> POP FIELD is ENUM");
        Object[] constants = field.getType().getEnumConstants();
        for (Object c : constants) {
            System.out.println(c);
        }
    }

    public static  void printDaxScanClass(Class<?> clazz){
        System.out.println("====================================");
        System.out.println("->  POP CLASS :"+clazz.getSimpleName());

    }

}
