/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 Robert Homa
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***********************************************************************
 */
package org.daxprotocol.core.dictionary;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpFieldGroup;
import org.daxprotocol.core.field.DaxAtrNullable;

import java.lang.reflect.Field;

public class DaxDictionaryManager {

    //TODO dictionary validation method after populateFromAnnotations
    // error  example :
    // 1) if any group refer to no existed master group
    // 2) enum field refer to no exiting enum ...




    public void populateFromAnnotations(DaxDictionary daxDic, Class<?> clazz){
        try {
            int groupId = 0;

            if (clazz.isAnnotationPresent(DaxpFieldGroup.class)){
                DaxDictionaryDecoratorService.printDaxScanClass(clazz);
                DaxpFieldGroup group =  clazz.getAnnotation(DaxpFieldGroup.class);

                DaxDictionaryDecoratorService.printDaxGroupInfo(group);
                groupId = group.id();
                daxDic.addGroup( group );
            }

            for (Field field : clazz.getDeclaredFields()) {
                DaxDictionaryDecoratorService.printDaxFieldInfo(field);
                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField daxp = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);

                    //Class  change type to char
                    daxDic.putAtrDataType(daxp.tag(),field.getType());

                    if (field.getType().isEnum()){
                        DaxDictionaryDecoratorService.printDaxEnumInfo(field);

                        String enumName = field.getType().getSimpleName();
                        daxDic.putAtrEnumName(daxp.tag(), enumName);

                        daxDic.putEnum(enumName,enumName);  // to improve

                        //TODO Create external service for ENUM populate
                        Object[] constants = field.getType().getEnumConstants();

                        for (Object c : constants) {
                            daxDic.putEnumValue(enumName, c.toString(),"");
                       }


                    }


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

                    daxDic.putAtrGroupId(daxp.tag(), groupId);

                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
