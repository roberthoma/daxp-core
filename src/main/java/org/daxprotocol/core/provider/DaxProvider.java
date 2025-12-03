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
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryPopulator;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;

public interface DaxProvider {
     DaxpConfig getConfig();

    DaxPreambleCodec getPreambleCodec();

    DaxMessageCodec getMessageCodec();

    DaxMessageConverter getMessageConverter();

    DaxDictionary getDictionary();

    DaxMessageFactory getMessageFactory();

    DaxDictionaryPopulator getDictionaryPopulator();

}
