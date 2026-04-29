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

package org.daxprotocol.core.model.trailer;

import org.daxprotocol.core.model.value.DaxValue;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.value.DaxValueInteger;

import java.util.LinkedHashMap;
import java.util.Map;

public class DaxTrailer {

    Map<DaxTag, DaxValue<?>> map = new LinkedHashMap<>();
    private final DaxValue<Integer> checksumPair;

    public DaxTrailer(){
        checksumPair = new DaxValueInteger(0);
        map.put(DaxCoreTags.CHECKSUM,checksumPair);
    }


    public Integer getChecksum() {
        return (Integer)(map.get(DaxCoreTags.CHECKSUM).getValue());
    }
    public void setChecksum(int checksum){
        checksumPair.setValue(checksum);
    }
}
