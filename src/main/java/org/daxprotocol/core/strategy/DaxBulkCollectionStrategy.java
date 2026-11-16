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

package org.daxprotocol.core.strategy;

import org.daxprotocol.core.application.DaxCoreConfig;

import java.util.Collection;
import java.util.Map;

public class DaxBulkCollectionStrategy {

    private final DaxCoreConfig coreConfig;

    public DaxBulkCollectionStrategy(DaxCoreConfig coreConfig) {
        this.coreConfig = coreConfig;
    }

    public boolean isBulkCompose(Object object) {
        if (object instanceof Iterable<?> iterable) {
            return isBulkCompose(iterable);
        }
        if (object instanceof Map<?, ?> map) {
            return isBulkCompose(map);
        }
        return false;
    }

    public boolean isBulkCompose(Iterable<?> iterable) {

        if (iterable instanceof Collection<?> col) {
            return   isBulkCompose(col.size());
        }

        int count = 0;
        for (Object ignored : iterable) {
            count++;
            return isBulkCompose(count);
        }
        return false;
    }

    ///----------------------------------------------------------------------------------------
    public boolean isBulkCompose(Map<?, ?> map){
        return isBulkCompose(map.size());
    }

    ///----------------------------------------------------------------------------------------
    private boolean isBulkCompose(int size){
    //todo for develop check by tag that any entity or collection is flat or not
    // if on flat return false

        if (size > coreConfig.getMinBulkSize()) {
            return true;
        }
        return false;
    }
}
