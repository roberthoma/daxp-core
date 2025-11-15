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

package org.daxprotocol.core.field;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxBlockType extends DaxPair<String> {

    public static final String  BLOCK_FIELD       =  "F";
    public static final String  BLOCK_GROUP       =  "G";
    public static final String  BLOCK_ENUM        =  "E";
    public static final String  BLOCK_ENUM_VALUE  =  "V";

    public DaxBlockType(String value) {
        super(DaxTag.BLOCK_TYPE, value);
    }
}
