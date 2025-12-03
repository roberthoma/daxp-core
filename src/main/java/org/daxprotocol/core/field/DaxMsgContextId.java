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
package org.daxprotocol.core.field;

import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.model.pair.DaxPair;

public class DaxMsgContextId extends DaxPair<Integer> {
    public DaxMsgContextId( Integer value) {
        super(DaxTagConst.MSG_CONTEXT, value);
    }
    @Override
    public String getStrValue() {
        return String.valueOf(value);
    };
}
