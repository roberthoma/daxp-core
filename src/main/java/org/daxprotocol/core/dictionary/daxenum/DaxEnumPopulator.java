/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
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
package org.daxprotocol.core.dictionary.daxenum;

import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.decorator.DaxDictionaryDecoratorService;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.reflect.Field;

public class DaxEnumPopulator {



    //moved to DaxDictionaryPopulator
    public void populateEnumFromAnnotations(Field field , DaxDictionary daxDic){
//        DaxDictionaryDecoratorService.printDaxEnumInfo(field);
//
//        DaxpField daxp = field.getAnnotation(DaxpField.class);
//        field.setAccessible(true);
//        DaxTag tag = new DaxTag(daxp.contextId(),daxp.tagId());
//        String enumName = field.getType().getSimpleName();
//        daxDic.putAtrEnumName(tag, enumName);
//
//        //TODO check exist
//        daxDic.putEnum(enumName,enumName);  // to improve
//
//        Object[] constants = field.getType().getEnumConstants();
//
//        for (Object c : constants) {
//            daxDic.putEnumValue(enumName, c.toString(),"");
//        }

    }
}
