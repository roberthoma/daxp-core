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
import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.populator.DaxAnnotationRegister;
import org.daxprotocol.core.populator.DaxPopulatorEnumType;
import org.daxprotocol.core.populator.DaxPopulatorMessage;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.mapper.DaxMessageMapper;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTrailerCodec;
import org.daxprotocol.core.parsers.DaxpRules;

public class DaxEngine {

    private final DaxConfig config;

    private final DaxPreambleCodec preambleCodec;

    private final DaxMessageCodec messageCodec;
    private final DaxFrameCodec frameCodec;

    private final DaxMessageConverter messageConverter;

    private final DaxDictionary dictionary;

    private final DaxMessageFactory messageFactory;

 //   private final DaxPopulator populator;

    private final DaxContextMapper contextMapper;

    private final DaxMessageMapper messageMapper;

    private final DaxTagCodec tagCodec;

    private final DaxPairCodec pairCodec;

    private final DaxpRules daxpRules;

    private final  DaxTagParser tagParser ;
    private final  DaxFrameParser frameParser ;


    private DaxHandlerRegistry handlerRegistry;
    private DaxPopulatorEnumType  enumPopulator;


    DaxPopulatorMessage messagePopulator;

    DaxAnnotationRegister annotationPopulator;

    public DaxEngine(DaxConfig config){
        this.config = config;
        this.daxpRules = new DaxpRules();

        DaxContext appContext = DaxContextFactory.createAppContext(config);
        DaxContext sysContext = DaxContextFactory.createSysContext();

        contextMapper = new DaxContextMapper();
        messageMapper = new DaxMessageMapper();

        contextMapper.registerPredefined(sysContext);
        contextMapper.registerPredefined(appContext);

        dictionary = new DaxDictionary(config, contextMapper, messageMapper);
        dictionary.putContext(sysContext);
        dictionary.putContext(appContext);
        DaxCoreTags.init(dictionary);

        tagParser  = new DaxTagParser(contextMapper);

        handlerRegistry = new DaxHandlerRegistry();

        tagCodec      = new DaxTagCodec     (config, contextMapper); //, parser);
        pairCodec     = new DaxPairCodec    (config, contextMapper, tagCodec);
        preambleCodec = new DaxPreambleCodec(config, contextMapper);

        DaxHeadCodec    headCodec    = new DaxHeadCodec(pairCodec);
        DaxBodyCodec    bodyCodec    = new DaxBodyCodec(pairCodec);
        DaxTrailerCodec trailerCodec = new DaxTrailerCodec(pairCodec);;

        messageCodec = new DaxMessageCodec(config, pairCodec,
                                           headCodec, bodyCodec, trailerCodec
        );

        frameCodec = new DaxFrameCodec(config, preambleCodec, messageCodec);



        messagePopulator    = new DaxPopulatorMessage( tagParser, dictionary);
        enumPopulator       = new DaxPopulatorEnumType(config, contextMapper, dictionary);
        annotationPopulator = new DaxAnnotationRegister(tagParser ,
                                                        enumPopulator,
                                                        config,
                                                        contextMapper,
                                                        dictionary,
                                                        handlerRegistry);

        messageConverter     = new DaxMessageConverter(config,contextMapper );







        messageFactory       = new DaxMessageFactory(config, contextMapper, tagCodec,  messageCodec,
                                                     headCodec, bodyCodec, trailerCodec, dictionary
        );


        frameParser =  new DaxFrameParser( config,
                                                 contextMapper,
                                                 tagParser,
                                                 dictionary,
                                                messageCodec,
                                                 preambleCodec) ;

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

//    public DaxPopulator getPopulator(){
//        return populator;
//    }


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

    public void populate(Class<?> clazz) {
        annotationPopulator.register(clazz);
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

    //    public DaxPopulator getDaxPopulator() {
//        return populator;
//    }
    ////////////////////

//    daxEngine.register(clazz);

 //   daxEngine.getAllTypeAnnotationClass()
//            .forEach(annClass ->
//                scanner.addIncludeFilter(new AnnotationTypeFilter(annClass))
//        );



//        @PostMapping("/post") // GetMapping
//        public ResponseEntity<String> postMessage(@RequestParam(required = false) Map<String, String> params,
//                @RequestBody(required = false) String body)
//        {
//            return msgDisposeExe(params, body);
//        }


        ////////////////////////////
        // IN SPRINg boot application
//        public ResponseEntity<String> dispose(Map<String, String> params, String body) {
//            try {
//                // 1. Logic: Decide if we parse the Body (DAXP Message) or Params
//                DaxMessage incoming = null;
//
//                if (body != null && !body.isEmpty()) {
//                    incoming = this.parser.parse(body); // Handles <SOH> or |
//                } else {
//                    incoming = this.parser.fromMap(params);  <<<<<<<<<
//                }
//
//                // 2. Logic: Find the Handler (The "Registered" method)
//                // You mentioned: "registered method by handler"
//                DaxResponse response = this.handlerRegistry.execute(incoming);
//
//                // 3. Logic: Return the response in DAXP format
//                return ResponseEntity.ok()
//                        .header("Content-Type", "text/plain; charset=UTF-8")
//                        .body(response.toDaxString()); // Returns DAXP=v...|...99=...
//
//            } catch (DAXPException e) {
//                // Handle your DAXP-XXXX exceptions here!
//                return ResponseEntity.status(400)
//                        .body(this.errorGenerator.buildError(e));
//            }
//        }
//
        ////////////


}
