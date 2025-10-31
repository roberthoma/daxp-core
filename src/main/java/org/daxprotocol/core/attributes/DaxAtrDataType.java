package org.daxprotocol.core.attributes;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxAtrDataType extends DaxPair<Character> {
    public static Character DATA_TYPE_INTEGER = 'I';
    public static Character DATA_TYPE_STRING  = 'S';
    public static Character DATA_TYPE_BOOLEAN = 'B';
    public static Character DATA_TYPE_CHAR    = 'C';



    public DaxAtrDataType(Class<?> clazz) {
        super(DaxTag.FIELD_DATA_TYPE, classToChar(clazz));
    }

//    public static Class<?> toClazz(Character cc){
//
//    }


        public static Character classToChar(Class<?> clazz){

        if (clazz == null) return null;

        String key = clazz.isPrimitive() ? clazz.getName() : clazz.getSimpleName();

        switch (key) {
            case "String":
                return DATA_TYPE_STRING;
            case "Integer":
            case "int":
                return DATA_TYPE_INTEGER;
            case "Character":
            case "char":
                return DATA_TYPE_CHAR;
            case "Boolean":
            case "boolean":
                return DATA_TYPE_BOOLEAN;
            default:
                return '?';
        }

    }

}
