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

package org.daxprotocol.core.model.head;

import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;

public class DaxMsgType extends DaxPair<String> {

    public static final String  DIC_REQ     =  "DR"; // 	REQ 	Request for a dictionary
    public static final String  DATA_DIC    =  "DD";  // 	RES 	Message containing a full dictionary of data types and their attributes
    public static final String  ERR_RES     =  "ER";  // 	RES 	Error request
    public static final String  DIC_RELOAD  =  "RL";   //	EVN 	Dictionary or attributes change, dictionary reload recommended

    public DaxMsgType(String value) {
        super(DaxTag.MSG_TYPE, value);
    }
}
