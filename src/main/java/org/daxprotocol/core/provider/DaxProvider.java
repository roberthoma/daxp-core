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

package org.daxprotocol.core.provider;

import org.daxprotocol.core.codec.*;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextFactory;
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryPopulator;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.model.trailer.DaxTrailerCodec;
import org.daxprotocol.core.rules.DaxParserService;
import org.daxprotocol.core.rules.DaxpRules;
import org.daxprotocol.core.strategy.DaxCoreStrategy;
import org.daxprotocol.core.strategy.DaxCoreStrategyImpl;

public class DaxProvider {

    private final DaxpConfig config;

    private final DaxPreambleCodec preambleCodec;

    private final DaxMessageCodec messageCodec;

    private final DaxMessageConverter messageConverter;

    private final DaxDictionary dictionary;

    private final DaxMessageFactory messageFactory;

    private final DaxDictionaryPopulator dictionaryPopulator;

    private final DaxCoreStrategy coreStrategy;

    private final DaxStringReferenceMapper contextMapper;
    private final DaxStringReferenceMapper messageMapper;

    private final DaxTagCodec tagCodec;
    private final DaxPairCodec pairCodec;

    private final DaxpRules daxpRules;

    private final DaxParserService parserService;

    public DaxProvider(DaxpConfig config){
        this.config = config;
        this.daxpRules = new DaxpRules();

        DaxContext appContext = DaxContextFactory.createAppContext(config);
        DaxContext sysContext = DaxContextFactory.createSysContext();

        contextMapper = new DaxStringReferenceMapper(config.getNextContextId());
        messageMapper = new DaxStringReferenceMapper(config.getNextMsgId());

        contextMapper.registerPredefined(sysContext);
        contextMapper.registerPredefined(appContext);

        parserService = new DaxParserService(config, contextMapper);

        dictionary = new DaxDictionary(config, contextMapper, messageMapper);
        dictionary.putContext(sysContext);
        dictionary.putContext(appContext);

        tagCodec       = new DaxTagCodec(config, contextMapper);
        pairCodec     = new DaxPairCodec(config, contextMapper, tagCodec);
        preambleCodec = new DaxPreambleCodec(config, contextMapper);
        DaxHeadCodec headCodec = new DaxHeadCodec(pairCodec);
        DaxBodyCodec bodyCodec = new DaxBodyCodec(pairCodec);
        DaxTrailerCodec trailerCodec = new DaxTrailerCodec(pairCodec);;

        messageCodec = new DaxMessageCodec(config, pairCodec, preambleCodec,
                                           headCodec, bodyCodec, trailerCodec,
                                           parserService);


        messageConverter     = new DaxMessageConverter(config,contextMapper );
        messageFactory       = new DaxMessageFactory(config, contextMapper, tagCodec);
        dictionaryPopulator  = new DaxDictionaryPopulator(config, contextMapper, parserService);
        coreStrategy         = new DaxCoreStrategyImpl(config, dictionary, dictionaryPopulator);

    }


    public DaxpConfig getConfig() {
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

    public DaxDictionaryPopulator getDictionaryPopulator (){
        return dictionaryPopulator;
    }

    public DaxCoreStrategy getCoreStrategy() {
        return coreStrategy;
    }

    public DaxStringReferenceMapper getContextMapper() {
        return contextMapper;
    }

    public DaxPairCodec getPairCodec() {
        return pairCodec;
    }

    public DaxParserService getParserService(){
        return parserService;
    }


}
