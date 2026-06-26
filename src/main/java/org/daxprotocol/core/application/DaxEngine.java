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
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.context.DaxContextFactory;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.dispatcher.DaxDispatcher;
import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.mapper.DaxSchemaMapper;
import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.dictionary.DaxDictionaryRegister;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static org.daxprotocol.core.application.DaxCoreTags.ATR_DATA_TYPE;

public class DaxEngine {
    private static final Logger logger = LoggerFactory.getLogger(DaxEngine.class);
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

    private DaxMessagePopulator messagePopulator;

    private DaxDictionaryRegister annotationRegister;

    private DaxDispatcher dispatcher;


    private DaxDataTypeService daxDataTypeService;
    private DaxDataTypeCodec dataTypeCodec;

    private  DaxValueCodec valueCodec;

    //TODO move tagParser to tagCodec


//    public DaxEngine(DaxConfig config){
    public DaxEngine(Properties properties){

        this.config = DaxpConfigFactory.createConfig(properties);

        DaxContext appContext = DaxContextFactory.createAppContext(config);
        DaxContext sysContext = DaxContextFactory.createSysContext();

        contextMapper = new DaxContextMapper();
        messageMapper = new DaxMessageMapper();
        schemaMapper  = new DaxSchemaMapper();

        contextMapper.registerPredefined(sysContext);
        contextMapper.registerPredefined(appContext);

//        config.setAppContextId( contextMapper.getReferenceId(appContext.getTagPrefix()));
//        appContext.setId(config.getAppContextId());

        tagParser  = new DaxTagParser(contextMapper);
        tagCodec   = new DaxTagCodec(config, contextMapper, tagParser );

        daxDataTypeService = new DaxDataTypeService();
        dataTypeCodec = new DaxDataTypeCodec(daxDataTypeService, tagCodec);

        valueCodec = new DaxValueCodec(dataTypeCodec);

        dictionary = new DaxDictionary(config, contextMapper, messageMapper,schemaMapper);
        dictionary.putContext(sysContext);
        dictionary.putContext(appContext);
        DaxCoreTags.init(dictionary);


        handlerRegistry = new DaxHandlerRegistry();


        pairCodec     = new DaxPairCodec    (tagCodec);
        preambleCodec = new DaxPreambleCodec( contextMapper);

        DaxHeadCodec    headCodec    = new DaxHeadCodec(pairCodec);
        DaxBodyCodec    bodyCodec    = new DaxBodyCodec(pairCodec, tagCodec, valueCodec);
        DaxTrailerCodec trailerCodec = new DaxTrailerCodec(pairCodec);;

        messageCodec = new DaxMessageCodec(config, pairCodec,
                                           headCodec, bodyCodec, trailerCodec
        );

        frameCodec = new DaxFrameCodec(config, preambleCodec, messageCodec);


        messagePopulator    = new DaxMessagePopulator( tagParser, dictionary);
        annotationRegister = new DaxDictionaryRegister(tagParser ,
                                                        config,
//                                                        contextMapper,
                dictionary,
                                                        handlerRegistry,
                tagCodec, dataTypeCodec
        );

        messageConverter     = new DaxMessageConverter(config,//contextMapper ,
                dictionary, tagCodec, dataTypeCodec, valueCodec, daxDataTypeService);




        preambleFactory = new DaxPreambleFactory(config, preambleCodec);


        messageFactory       = new DaxMessageFactory(config,
                tagCodec,  messageCodec,
                headCodec, bodyCodec, trailerCodec, dictionary,
                dataTypeCodec,
                valueCodec
        );


        frameParser =  new DaxFrameParser( config,
                                             //    contextMapper,
                                                 tagParser,
                dictionary,
                                                 messageFactory,
                                                 preambleCodec,
                pairCodec) ;

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

    public DaxDataTypeService getDaxDataTypeService() {
        return daxDataTypeService;
    }

    public int getAppContextId(){
      return config.getAppContextId();
    }

    public void checkRegister() {

        //TODO Develop all references checking
        dictionary.getTagAttributeMap() .forEach((daxTag, tagDaxPairMap) ->{
                if (tagDaxPairMap.containsKey(ATR_DATA_TYPE)){
                    if (tagDaxPairMap.get(ATR_DATA_TYPE)
                            .getDataTypeValue().equals(DaxDataType.UNKNOWN))
                          {logger.error(" TAG : {}  UNKNOWN Type", tagCodec.encode( daxTag));
                    }
                }
            });

        }


}
