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

import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;

import java.util.LinkedHashMap;
import java.util.Map;

public class DaxTrailer {

    Map<Integer, DaxPair<?>> map = new LinkedHashMap<>();

    public DaxTrailer(){
        map.put(DaxTagConst.CHECKSUM,new DaxPair<Integer>(DaxTagConst.CHECKSUM,321));
    }


    public Integer getChecksum() {
        return (Integer)(map.get(DaxTagConst.CHECKSUM).getValue());
    }
}
