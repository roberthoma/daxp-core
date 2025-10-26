package org.daxprotocol.core.attributes;

import org.daxprotocol.core.codec.DaxTag;

public class DaxAtrDataType extends DaxAttribute<Character>{
    public DaxAtrDataType(Character value) {
        super(DaxTag.FIELD_DATA_TYPE, value);
    }

    public static Character classToChar(Class<?> clazz){

        if (clazz == null) return null;

        String key = clazz.isPrimitive() ? clazz.getName() : clazz.getSimpleName();

        switch (key) {
            case "String":
                return 'S';
            case "Integer":
            case "int":
                return 'I';
            case "Character":
            case "char":
                return 'C';
            case "Boolean":
            case "boolean":
                return 'B';
            default:
                return '?';
        }

    }

}
