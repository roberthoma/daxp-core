/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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
import org.daxprotocol.core.exceptions.DaxException;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.namespace.DaxNamespaceFactory;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.dispatcher.DaxDispatcher;
import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.mapper.DaxSchemaMapper;
import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.registries.DaxAnnotationScanner;
import org.daxprotocol.core.registries.DaxMessagePopulator;
import org.daxprotocol.core.registries.DaxHandlerRegistry;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.registries.DaxMessageConverter;
import org.daxprotocol.core.registries.DaxSemanticRegistry;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.namespace.DaxNamespace;
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

    private final DaxSemanticRegistry semanticRegistry;

    private final DaxPreambleFactory preambleFactory;

    private final DaxMessageFactory messageFactory;

    private final DaxNamespaceMapper namespaceMapper;

    private final DaxMessageMapper messageMapper;

    private final DaxSchemaMapper schemaMapper;

    private final DaxTagCodec tagCodec;

    private final DaxPairCodec pairCodec;

    private final  DaxTagParser tagParser ;

    private final  DaxFrameParser frameParser ;

    private final DaxHandlerRegistry handlerRegistry;

    private final DaxMessagePopulator messagePopulator;

    private final DaxAnnotationScanner annotationRegister;

    private final DaxDispatcher dispatcher;


    private final DaxDataTypeService dataTypeService;
    private final DaxDataTypeCodec dataTypeCodec;

    private  final DaxValueCodec valueCodec;

    //TODO move tagParser to tagCodec


//    public DaxEngine(DaxConfig config){
    public DaxEngine(Properties properties){

        this.config = DaxpConfigFactory.createConfig(properties);

        DaxNamespace appNamespace = DaxNamespaceFactory.createAppNamespace(config);
        DaxNamespace sysNamespace = DaxNamespaceFactory.createSysNamespace();

        namespaceMapper = new DaxNamespaceMapper();
        messageMapper = new DaxMessageMapper();
        schemaMapper  = new DaxSchemaMapper();

        namespaceMapper.registerPredefined(sysNamespace);
        namespaceMapper.registerPredefined(appNamespace);

//        config.setAppnamespaceId( namespaceMapper.getReferenceId(appNamespace.getTagPrefix()));
//        appNamespace.setId(config.getAppnamespaceId());

        tagParser  = new DaxTagParser(namespaceMapper);
        tagCodec   = new DaxTagCodec(config, namespaceMapper, tagParser );

        dataTypeService = new DaxDataTypeService();
        dataTypeCodec = new DaxDataTypeCodec(dataTypeService, tagCodec);

        valueCodec = new DaxValueCodec(dataTypeCodec);

        semanticRegistry = new DaxSemanticRegistry(config, namespaceMapper, messageMapper,schemaMapper);
        semanticRegistry.putNamespace(sysNamespace);
        semanticRegistry.putNamespace(appNamespace);
        DaxCoreTags.init(semanticRegistry);


        handlerRegistry = new DaxHandlerRegistry();


        pairCodec     = new DaxPairCodec    (tagCodec);
        preambleCodec = new DaxPreambleCodec( namespaceMapper);

        DaxHeadCodec    headCodec    = new DaxHeadCodec(pairCodec);
        DaxBodyCodec    bodyCodec    = new DaxBodyCodec(pairCodec, tagCodec, valueCodec);
        DaxTrailerCodec trailerCodec = new DaxTrailerCodec(pairCodec);;

        messageCodec = new DaxMessageCodec(config, pairCodec,
                                           headCodec, bodyCodec, trailerCodec
        );

        frameCodec = new DaxFrameCodec(config, preambleCodec, messageCodec);


        messagePopulator    = new DaxMessagePopulator( tagParser, semanticRegistry);
        annotationRegister = new DaxAnnotationScanner(tagParser ,
                                                        config,
//                                                        namespaceMapper,
                semanticRegistry,
                                                        handlerRegistry,
                tagCodec, dataTypeCodec,
                dataTypeService
        );

        messageConverter     = new DaxMessageConverter(config,//namespaceMapper ,
                semanticRegistry, tagCodec, dataTypeCodec, valueCodec, dataTypeService);




        preambleFactory = new DaxPreambleFactory(config, preambleCodec);


        messageFactory       = new  DaxMessageFactory(
                semanticRegistry,
                                            tagCodec,
                                            headCodec,
                                            bodyCodec,
                                            trailerCodec,
                                            valueCodec,
                                            dataTypeCodec
                                    );




        frameParser =  new DaxFrameParser( config,tagParser,
                                          messageFactory,preambleCodec,
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

    public DaxSemanticRegistry getSemanticRegistry() {
        if(semanticRegistry == null){
            throw new RuntimeException("Dictionary is NOT READY !!!!");
        }
        return semanticRegistry;
    }

    public DaxMessageFactory getMessageFactory() {
        return messageFactory;
    }

    public DaxNamespaceMapper getnamespaceMapper() {
        return namespaceMapper;
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

    public DaxDataTypeService getDataTypeService() {
        return dataTypeService;
    }

    public int getAppNamespaceId(){
      return config.getAppNamespaceId();
    }



    public void execute(DaxFrame reqFrame, DaxFrame respFrame ){
        if (respFrame == null){
            respFrame = new DaxFrame();
        }
        respFrame.setPreamble(preambleFactory.createRespPreamble(reqFrame));
        handlerRegistry.executor(reqFrame,respFrame);
    }

    //TODO refactoring respondFrame and within preamble
    public void execute(String  reqFrameString, DaxFrame respFrame ){
        logger.trace("Engine Executor reqFrameString= {}",reqFrameString);
        // check parsing if is bad  create maessage with error
        try {

        DaxFrame reqFrame = frameParser.parseFrame(reqFrameString);

        execute(reqFrame,respFrame);


        }
        catch (DaxException e) {
            logger.error(e.getMessage());
            respFrame.setPreamble(preambleFactory.createPreamble());
            respFrame.addMessage(messageFactory.daxExceptionMessage(e));
        }

        catch (Exception e) {
            logger.error(e.getMessage());
            respFrame.setPreamble(preambleFactory.createPreamble());
            respFrame.addMessage(messageFactory.logMessage(3,e.getMessage()));
        }
    }



    public void checkRegister() {

        //TODO Develop all references checking
        semanticRegistry.getTagAttributeMap() .forEach((daxTag, tagDaxPairMap) ->{
                if (tagDaxPairMap.containsKey(ATR_DATA_TYPE)){
                    if (tagDaxPairMap.get(ATR_DATA_TYPE)
                            .getDataTypeValue().equals(DaxDataType.UNKNOWN))
                          {logger.error(" TAG : {}  UNKNOWN Type", tagCodec.encode( daxTag));
                    }
                }
            });

        }


}
