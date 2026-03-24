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
package org.daxprotocol.core.dictionary.populator;

import org.daxprotocol.core.annotation.*;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxEnum;
import org.daxprotocol.core.dictionary.DaxEnumValue;
import org.daxprotocol.core.dictionary.DaxMessageItem;
import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.group.DaxGroup;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.rules.DaxParserService;
import org.daxprotocol.core.tool.DaxLangTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


//TODO create  service  DaxValidationAttributeManager
//TODO refactoring all populator class
public class DaxPopulator {

    DaxpConfig               config;
    DaxStringReferenceMapper contextMapper;
    DaxParserService         parserService;

    DaxPopulatorEnumType enumPopulator;

    DaxPopulatorMessage messagePopulator;

    DaxPopulatorAnnotation annotationPopulator;
    public DaxPopulator(DaxpConfig config,
                                  DaxStringReferenceMapper contextMapper,
                                  DaxParserService parserService
    ){
        this.config        = config;
        this.contextMapper = contextMapper;
        this.parserService = parserService;
        enumPopulator = new DaxPopulatorEnumType(config, contextMapper, parserService);
        annotationPopulator = new DaxPopulatorAnnotation(
                                        parserService ,
                                        enumPopulator,
                                        config,
                                        contextMapper);
        messagePopulator = new DaxPopulatorMessage(parserService);
    }








//            for (Method m : clazz.getDeclaredMethods()) {
//        DaxpValue methodAnn = m.getAnnotation(DaxpValue.class);
//        if (methodAnn == null) continue;
//
//        Class<?> returnType = m.getReturnType();
//        // Object value =  m.invoke(clazz);
//        System.out.println(methodAnn.tagId());
//
//    }






//TODO create hendler method
//       for (Method method : clazz.getDeclaredMethods()) {
//        if (method.isAnnotationPresent(DaxpMsg.class)){
//            populateDaxpMsg(daxDic, method);
//        }
//    }


//TODO throw Runtim exception of tags, group etc are duplicated
    public void populateFromAnnotations(DaxDictionary daxDic, Class<?> clazz){

        annotationPopulator.populate(daxDic, clazz);
    }
//------------------------------------------------



    public void populateFromMessage(DaxDictionary daxDic, DaxMessage message) {

        messagePopulator.populate(daxDic, message);
        //TODO get context from head , if not exist default ctx is obligatory

    }



}
