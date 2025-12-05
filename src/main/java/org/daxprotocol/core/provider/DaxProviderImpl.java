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

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.config.DaxpPropertiesLoader;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryPopulator;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.context.DaxContext;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;

public class DaxProviderImpl implements DaxProvider {

    private DaxpConfig config;

    private DaxPreambleCodec preambleCodec;

    private DaxMessageCodec messageCodec;

    private DaxMessageConverter messageConverter;

    private DaxDictionary dictionary;

    private DaxpPropertiesLoader propertiesLoader;

    private DaxMessageFactory messageFactory;

    private DaxDictionaryPopulator dictionaryPopulator;

    public DaxProviderImpl(String propertiesFile){
        propertiesLoader = new DaxpPropertiesLoader(propertiesFile);
        propertiesLoader.load();

        DaxContext appContext =  propertiesLoader.getApplicationContext();

        config = new DaxpConfig();
        dictionary = new DaxDictionary(config);
        config.setApplicationContextId( DaxContextMapper
                                       .getContextId( appContext.symbol ));

        dictionary.putContext(appContext);

    }


    @Override public DaxpConfig getConfig() {
        if (config == null) {
            throw new RuntimeException("Config is NOT READY !!!!");
        }
        return config;
    }

    @Override public DaxPreambleCodec getPreambleCodec() {
        if(preambleCodec == null){
            preambleCodec = new DaxPreambleCodec(getConfig());
        }
        return preambleCodec;
    }

    @Override public DaxMessageCodec getMessageCodec() {
        if(messageCodec == null) {
            messageCodec = new DaxMessageCodec(getConfig());
        }
        return messageCodec;
    }

    @Override public DaxMessageConverter getMessageConverter() {
        if(messageConverter == null) {
            messageConverter = new DaxMessageConverter(getConfig());
        }
        return messageConverter;
    }

    @Override public DaxDictionary getDictionary() {
        if(dictionary == null){
            throw new RuntimeException("Dictionary is NOT READY !!!!");
        }
        return dictionary;
    }

    @Override public DaxMessageFactory getMessageFactory() {
        if (messageFactory == null){
            messageFactory = new DaxMessageFactory();
        }
        return messageFactory;
    }
    @Override public DaxDictionaryPopulator getDictionaryPopulator (){
        if(dictionaryPopulator == null){
            dictionaryPopulator = new DaxDictionaryPopulator(getConfig());
        }
        return dictionaryPopulator;
    }

}
