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
            //TMP
            System.out.println("====================================");
            System.out.println("->  POP CLASS :"+clazz.getSimpleName());

            int groupId = 0;

            if (clazz.isAnnotationPresent(DaxpFieldGroup.class)){
                System.out.println(" >> DaxpGroup ");
                DaxpFieldGroup group =  clazz.getAnnotation(DaxpFieldGroup.class);

                System.out.println("GRP name : " +group.name());
                System.out.println("GRP id : " +group.id());
                System.out.println("GRP master id : " +group.masterId());
                System.out.println("GRP desc : " +group.description());
                System.out.println("GRP namespace : " +group.namespace());

                groupId = group.id();
                daxDic.addGroup( group );

            }

            for (Field field : clazz.getDeclaredFields()) {

                //TMP
                System.out.println("\n> POP FIELD Name : "+field.getName());
                System.out.println("___> POP FIELD Type Name      : "+field.getType().getTypeName());
                System.out.println("___> POP FIELD Component Type : "+field.getType().getComponentType());

                if (field.getType().isEnum()){
                   System.out.println("___> POP FIELD is ENUM");

                   Object[] constants = field.getType().getEnumConstants();

                    for (Object c : constants) {

                        System.out.println(c);
                    }

                }

                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField daxp = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);

                    //Class  change type to char
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

                    daxDic.putAtrGroupId(daxp.tag(), groupId);

                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
