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

package org.daxprotocol.core.application;

import org.daxprotocol.core.codec.*;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.context.DaxContextFactory;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.dispatcher.DaxDispatcher;
import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.mapper.DaxSchemaMapper;
import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.dictionary.DaxDictionaryRegister;
import org.daxprotocol.core.dictionary.DaxPopulatorEnumType;
import org.daxprotocol.core.dictionary.DaxMessagePopulator;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.dictionary.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.mapper.DaxMessageMapper;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTrailerCodec;

public class DaxEngine {

    private final DaxConfig config;

    private final DaxPreambleCodec preambleCodec;

    private final DaxMessageCodec messageCodec;
    private final DaxFrameCodec frameCodec;

    private final DaxMessageConverter messageConverter;

    private final DaxDictionary dictionary;

    private final DaxPreambleFactory preambleFactory;

    private final DaxMessageFactory messageFactory;

    private final DaxContextMapper contextMapper;

    private final DaxMessageMapper messageMapper;

    private final DaxSchemaMapper schemaMapper;

    private final DaxTagCodec tagCodec;

    private final DaxPairCodec pairCodec;

    private final  DaxTagParser tagParser ;

    private final  DaxFrameParser frameParser ;

    private DaxHandlerRegistry handlerRegistry;

    private DaxPopulatorEnumType  enumPopulator;


    private DaxMessagePopulator messagePopulator;

    private DaxDictionaryRegister annotationRegister;

    private DaxDispatcher dispatcher;

    private DaxDataTypeCodec dataTypeCodec;

    //TODO move tagParser to tagCodec


    public DaxEngine(DaxConfig config){
        this.config = config;

        DaxContext appContext = DaxContextFactory.createAppContext(config);
        DaxContext sysContext = DaxContextFactory.createSysContext();

        contextMapper = new DaxContextMapper();
        messageMapper = new DaxMessageMapper();
        schemaMapper  = new DaxSchemaMapper();

        contextMapper.registerPredefined(sysContext);
        contextMapper.registerPredefined(appContext);
        dataTypeCodec = new DaxDataTypeCodec();

        dictionary = new DaxDictionary(config, contextMapper, messageMapper,schemaMapper);
        dictionary.putContext(sysContext);
        dictionary.putContext(appContext);
        DaxCoreTags.init(dictionary);

        tagParser  = new DaxTagParser(contextMapper);

        handlerRegistry = new DaxHandlerRegistry();

        tagCodec      = new DaxTagCodec     (config, contextMapper, tagParser );
        pairCodec     = new DaxPairCodec    (tagCodec);
        preambleCodec = new DaxPreambleCodec( contextMapper);

        DaxHeadCodec    headCodec    = new DaxHeadCodec(pairCodec);
        DaxBodyCodec    bodyCodec    = new DaxBodyCodec(pairCodec, tagCodec);
        DaxTrailerCodec trailerCodec = new DaxTrailerCodec(pairCodec);;

        messageCodec = new DaxMessageCodec(config, pairCodec,
                                           headCodec, bodyCodec, trailerCodec
        );

        frameCodec = new DaxFrameCodec(config, preambleCodec, messageCodec);


        messagePopulator    = new DaxMessagePopulator( tagParser, dictionary);
        enumPopulator       = new DaxPopulatorEnumType(config, dictionary, dataTypeCodec);
        annotationRegister = new DaxDictionaryRegister(tagParser ,
                                                        enumPopulator,
                                                        config,
//                                                        contextMapper,
                dictionary,
                                                        handlerRegistry,
                tagCodec, dataTypeCodec
        );

        messageConverter     = new DaxMessageConverter(config,//contextMapper ,
                dictionary, tagCodec, dataTypeCodec);




        preambleFactory = new DaxPreambleFactory(config, preambleCodec);


        messageFactory       = new DaxMessageFactory(config,
                tagCodec,  messageCodec,
                                                     headCodec, bodyCodec, trailerCodec, dictionary, tagParser,
                dataTypeCodec
        );


        frameParser =  new DaxFrameParser( config,
                                             //    contextMapper,
                                                 tagParser,
                dictionary,
                                                 messageFactory,
                                                 preambleCodec) ;

        //-----------------
        //Registration
        annotationRegister.scanAndRegister(DaxCoreController.class);
        handlerRegistry.registerCtrl(new DaxCoreController(messageFactory));
        dispatcher = new DaxDispatcher(preambleFactory);




    }


    public DaxConfig getConfig() {
        if (config == null) {
            throw new RuntimeException("Config is NOT READY !!!!");
        }
        return config;
    }

    public DaxPreambleCodec getPreambleCodec() {

        return preambleCodec;
    }

    public DaxMessageCodec getMessageCodec() {
        return messageCodec;
    }

    public DaxMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public DaxDictionary getDictionary() {
        if(dictionary == null){
            throw new RuntimeException("Dictionary is NOT READY !!!!");
        }
        return dictionary;
    }

    public DaxMessageFactory getMessageFactory() {
        return messageFactory;
    }

    public DaxContextMapper getContextMapper() {
        return contextMapper;
    }

    public DaxPairCodec getPairCodec() {
        return pairCodec;
    }

    public DaxTagParser getTagParser(){
        return tagParser;
    }

    public DaxTagCodec getTagCodec(){
        return tagCodec;
    }

    public void register(Class<?> clazz) {
        annotationRegister.scanAndRegister(clazz);
    }

    public DaxHandlerRegistry getHandlerRegistry() {
        return handlerRegistry;
    }

    public DaxFrameCodec getFrameCodec(){
        return frameCodec;
    }

    public DaxFrameParser getFrameParser() {
        return frameParser;
    }


    public DaxPreambleFactory getPreambleFactory(){
        return preambleFactory;
    }

    public DaxDispatcher getDispatcher() {
        return dispatcher;
    }

    public DaxDataTypeCodec getDataTypeCodec() {
        return dataTypeCodec;
    }


}
