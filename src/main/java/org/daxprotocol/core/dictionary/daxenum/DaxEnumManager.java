package org.daxprotocol.core.dictionary.daxenum;

import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.decorator.DaxDictionaryDecoratorService;
import org.daxprotocol.core.dictionary.DaxContextDic;

import java.lang.reflect.Field;

public class DaxEnumManager {


    public void populateEnumFromAnnotations(Field field , DaxContextDic daxDic){
        DaxDictionaryDecoratorService.printDaxEnumInfo(field);

        DaxpField daxp = field.getAnnotation(DaxpField.class);
        field.setAccessible(true);

        String enumName = field.getType().getSimpleName();
        daxDic.putAtrEnumName(daxp.tag(), enumName);

        //TODO check exist
        daxDic.putEnum(enumName,enumName);  // to improve

        Object[] constants = field.getType().getEnumConstants();

        for (Object c : constants) {
            daxDic.putEnumValue(enumName, c.toString(),"");
        }

    }
}
