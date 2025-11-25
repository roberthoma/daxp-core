/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 Robert Homa
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
package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.dictionary.daxenum.DaxDictionaryEnum;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumName;
import org.daxprotocol.core.dictionary.daxenum.DaxEnumValue;
import org.daxprotocol.core.field.*;
import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.group.DaxGroup;
import org.daxprotocol.core.group.DaxpGroupItf;

import java.util.*;
//TODO create context dictionary
public class DaxDictionary {

    String defaultContext;


   // Map<String , DaxContext>  contextMap = new HashMap<>();


    Map<String,DaxContextDic> dictionaryMap =  new HashMap<>();



//    public DaxContext getContext(String symbol) {
//        return contextMap.get(symbol);
//    }

    public DaxContextDic getDictionary(String contextSymbol) {
        return dictionaryMap.get(contextSymbol);
    }


    public DaxContextDic getDefaultDic() {
        return dictionaryMap.get(defaultContext);
    }




}
