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

import org.daxprotocol.core.codec.DaxBodyCodec;
import org.daxprotocol.core.codec.DaxHeadCodec;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPairCodec;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextFactory;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryPopulator;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.daxprotocol.core.model.trailer.DaxTrailerCodec;
import org.daxprotocol.core.parser.DaxParserService;
import org.daxprotocol.core.strategy.DaxCoreStrategy;
import org.daxprotocol.core.strategy.DaxCoreStrategyImpl;

public class DaxProviderImpl implements DaxProvider {

    private final DaxpConfig config;

    private final DaxPreambleCodec preambleCodec;

    private final DaxMessageCodec messageCodec;

    private final DaxMessageConverter messageConverter;

    private final DaxDictionary dictionary;

    private final DaxMessageFactory messageFactory;

    private final DaxDictionaryPopulator dictionaryPopulator;

    private final DaxCoreStrategy coreStrategy;

    private final DaxContextMapper contextMapper;

    private final DaxPairCodec pairCodec;

    private final DaxParserService parserService;

    public DaxProviderImpl(DaxpConfig config){
        this.config = config;

        DaxContext appContext = DaxContextFactory.createAppContext(config);
        DaxContext sysContext = DaxContextFactory.createSysContext();

        contextMapper = new DaxContextMapper(config);
        contextMapper.registerPredefined(sysContext);
        contextMapper.registerPredefined(appContext);

        parserService = new DaxParserService(config, contextMapper);

        dictionary = new DaxDictionary(config, contextMapper);
        dictionary.putContext(sysContext);
        dictionary.putContext(appContext);

        pairCodec     = new DaxPairCodec(config, contextMapper);
        preambleCodec = new DaxPreambleCodec(config, contextMapper);

        DaxHeadCodec headCodec = new DaxHeadCodec(pairCodec);;
        DaxBodyCodec bodyCodec = new DaxBodyCodec(pairCodec);
        DaxTrailerCodec trailerCodec = new DaxTrailerCodec(pairCodec);;

        messageCodec = new DaxMessageCodec(config, pairCodec, preambleCodec, headCodec, bodyCodec, trailerCodec);


        messageConverter     = new DaxMessageConverter(config,contextMapper );
        messageFactory       = new DaxMessageFactory(config, contextMapper);
        dictionaryPopulator  = new DaxDictionaryPopulator(config, contextMapper, parserService);
        coreStrategy         = new DaxCoreStrategyImpl(config, dictionary, dictionaryPopulator);

    }


    @Override public DaxpConfig getConfig() {
        if (config == null) {
            throw new RuntimeException("Config is NOT READY !!!!");
        }
        return config;
    }

    @Override public DaxPreambleCodec getPreambleCodec() {

        return preambleCodec;
    }

    @Override public DaxMessageCodec getMessageCodec() {
        return messageCodec;
    }

    @Override public DaxMessageConverter getMessageConverter() {
        return messageConverter;
    }

    @Override public DaxDictionary getDictionary() {
        if(dictionary == null){
            throw new RuntimeException("Dictionary is NOT READY !!!!");
        }
        return dictionary;
    }

    @Override public DaxMessageFactory getMessageFactory() {
        return messageFactory;
    }
    @Override public DaxDictionaryPopulator getDictionaryPopulator (){
        return dictionaryPopulator;
    }

    @Override public DaxCoreStrategy getCoreStrategy() {
        return coreStrategy;
    }

    @Override public DaxContextMapper getContextMapper() {
        return contextMapper;
    }

    @Override public DaxPairCodec getPairCodec() {
        return pairCodec;
    }


}
