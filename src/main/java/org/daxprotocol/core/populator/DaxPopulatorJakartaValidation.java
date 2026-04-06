package org.daxprotocol.core.populator;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.reflect.Field;
import java.util.Arrays;

public class DaxPopulatorJakartaValidation {

    public void populate(DaxDictionary daxDic, Field field , DaxTag tag){
        boolean isJakartaValidation = Arrays.stream(field.getAnnotations())
                .anyMatch(a -> a.annotationType().getPackageName()
                        .startsWith("jakarta.validation"));

        if (!isJakartaValidation){
            return;
        }

        if (field.isAnnotationPresent(NotNull.class)) {
            daxDic.putAtrNullable(tag, false);
        }

        if (field.isAnnotationPresent(Size.class)) {
            Size size = field.getAnnotation(Size.class);
            if (size.min() > 0){
                daxDic.putAtrSizeMin(tag, size.min());
            }
            if (size.max() < Integer.MAX_VALUE){
                daxDic.putAtrSizeMax(tag, size.max());
            }
        }

    }
}
