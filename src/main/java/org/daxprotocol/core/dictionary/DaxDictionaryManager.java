package org.daxprotocol.core.dictionary;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpTag;
import org.daxprotocol.core.attributes.DaxAtrNullable;

import java.lang.reflect.Field;

public class DaxDictionaryManager {
    public void populateFromAnnotations(DaxDictionary daxDic, Class<?> clazz){
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(DaxpTag.class)) {
                    DaxpTag daxp = field.getAnnotation(DaxpTag.class);
                    field.setAccessible(true);

                    //Class will change to char
                    daxDic.putAtrDataType(daxp.tag(),field.getType());

                    if (field.isAnnotationPresent(NotNull.class)) {
                        daxDic.putAtrNullable(daxp.tag(), DaxAtrNullable.NULLABLE_FALSE);
                    }

                    if (field.isAnnotationPresent(Size.class)) {
                        Size size = field.getAnnotation(Size.class);
                        if (size.min() > 0){
                            daxDic.putAtrSizeMin(daxp.tag(), size.min());
                        }
                        if (size.max() < Integer.MAX_VALUE){
                            daxDic.putAtrSizeMax(daxp.tag(), size.max());
                        }
                    }

                    if (daxp.uiLabel()!=null) {
                        daxDic.putAtrUiLabel(daxp.tag(), daxp.uiLabel());
                    }

                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
