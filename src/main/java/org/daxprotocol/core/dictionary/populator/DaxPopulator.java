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

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.parsers.DaxParser;


//TODO create  service  DaxValidationAttributeManager
//TODO throw Runtime exception of tags, group etc are duplicated

public class DaxPopulator {

    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxParser parserService;
    DaxDictionary daxDic;
    DaxPopulatorEnumType enumPopulator;

    DaxPopulatorMessage messagePopulator;

    DaxPopulatorAnnotation annotationPopulator;
    public DaxPopulator(DaxConfig config,
                                  DaxContextMapper contextMapper,
                                  DaxParser parserService,
                                  DaxDictionary daxDic
    ){
        this.config        = config;
        this.contextMapper = contextMapper;
        this.parserService = parserService;
        this.daxDic = daxDic;
        enumPopulator = new DaxPopulatorEnumType(config, contextMapper, daxDic);
        annotationPopulator = new DaxPopulatorAnnotation(
                                        parserService ,
                                        enumPopulator,
                                        config,
                                        contextMapper,
                                        daxDic
                );

        messagePopulator = new DaxPopulatorMessage(parserService, daxDic);
    }


    public void populateFromAnnotations( Class<?> clazz){

        annotationPopulator.populate(clazz);
    }

//    public void populateFromMessage(DaxDictionary daxDic, DaxMessage message) {
    public void populateFromMessage(DaxPreamble preamble,  DaxMessage message) {

        messagePopulator.populate( preamble ,message);


    }



}
