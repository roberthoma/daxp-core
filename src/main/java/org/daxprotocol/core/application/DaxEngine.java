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
import org.daxprotocol.core.dispatcher.DaxDispatcher;
import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.register.DaxSchemaBuilder;
import org.daxprotocol.core.register.DaxPopulatorEnumType;
import org.daxprotocol.core.register.DaxPopulatorMessage;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.register.DaxMessageConverter;
import org.daxprotocol.core.schema.DaxSchemaRegister;
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

    private final DaxSchemaRegister schema;

    private final DaxPreambleFactory preambleFactory;

    private final DaxMessageFactory messageFactory;

    private final DaxContextMapper contextMapper;

    private final DaxMessageMapper messageMapper;

    private final DaxTagCodec tagCodec;

    private final DaxPairCodec pairCodec;

    private final  DaxTagParser tagParser ;

    private final  DaxFrameParser frameParser ;

    private DaxHandlerRegistry handlerRegistry;

    private DaxPopulatorEnumType  enumPopulator;


    DaxPopulatorMessage messagePopulator;

    DaxSchemaBuilder annotationRegister;

    DaxDispatcher dispatcher;

    //TODO move tagParser to tagCodec


    public DaxEngine(DaxConfig config){
        this.config = config;

        DaxContext appContext = DaxContextFactory.createAppContext(config);
        DaxContext sysContext = DaxContextFactory.createSysContext();

        contextMapper = new DaxContextMapper();
        messageMapper = new DaxMessageMapper();

        contextMapper.registerPredefined(sysContext);
        contextMapper.registerPredefined(appContext);

        schema = new DaxSchemaRegister(config, contextMapper, messageMapper);
        schema.putContext(sysContext);
        schema.putContext(appContext);
        DaxCoreTags.init(schema);

        tagParser  = new DaxTagParser(contextMapper);

        handlerRegistry = new DaxHandlerRegistry();

        tagCodec      = new DaxTagCodec     (config, contextMapper, tagParser );
        pairCodec     = new DaxPairCodec    (config, contextMapper, tagCodec);
        preambleCodec = new DaxPreambleCodec(config, contextMapper);

        DaxHeadCodec    headCodec    = new DaxHeadCodec(pairCodec);
        DaxBodyCodec    bodyCodec    = new DaxBodyCodec(pairCodec);
        DaxTrailerCodec trailerCodec = new DaxTrailerCodec(pairCodec);;

        messageCodec = new DaxMessageCodec(config, pairCodec,
                                           headCodec, bodyCodec, trailerCodec
        );

        frameCodec = new DaxFrameCodec(config, preambleCodec, messageCodec);


        messagePopulator    = new DaxPopulatorMessage( tagParser, schema);
        enumPopulator       = new DaxPopulatorEnumType(config, contextMapper, schema);
        annotationRegister = new DaxSchemaBuilder(tagParser ,
                                                        enumPopulator,
                                                        config,
                                                        contextMapper,
                schema,
                                                        handlerRegistry,
                tagCodec);

        messageConverter     = new DaxMessageConverter(config,contextMapper , schema, tagCodec);




        preambleFactory = new DaxPreambleFactory(config, preambleCodec);


        messageFactory       = new DaxMessageFactory(config, contextMapper, tagCodec,  messageCodec,
                                                     headCodec, bodyCodec, trailerCodec, schema, tagParser
        );


        frameParser =  new DaxFrameParser( config,
                                                 contextMapper,
                                                 tagParser,
                schema,
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

    public DaxSchemaRegister getSchema() {
        if(schema == null){
            throw new RuntimeException("Dictionary is NOT READY !!!!");
        }
        return schema;
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

}
