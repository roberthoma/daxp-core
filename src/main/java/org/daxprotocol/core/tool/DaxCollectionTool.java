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

package org.daxprotocol.core.tool;

import org.daxprotocol.core.dictionary.DaxBaseDictionary;
import org.daxprotocol.core.model.pair.DaxPair;

import java.util.Map;
import java.util.Set;

public class DaxCollectionTool {
    public static  <K,V> Map<K,V> putAndReturnMap(Map<K,V> map , K k,V v){
        map.put(k,v);
        return map;
    }


    public static <V> Set<V> addAndReturnSet(Set<V> set, V v){
        if (!set.contains(v)){
            set.add(v);
        };
        return  set;
    }

    public static DaxBaseDictionary<String> putAndReturnMap(DaxBaseDictionary<String> map , String k, DaxPair<?> pair){
        map.putAttribute(k,pair);
        return map;
    }

}
