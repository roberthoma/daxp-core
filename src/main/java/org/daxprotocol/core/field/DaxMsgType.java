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

import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;

public class DaxMsgType extends DaxPair<String> {

    //request : introduce yourself
    //DEPENDENCY from required tags/fields
    //TODO Add to preferences
    private static final String daxPrefix = "SYS.";

    public static final String  DIC_REQ     =  daxPrefix+"DR"; // 	REQ 	Request for a dictionary
    public static final String  DATA_DIC    =  daxPrefix+"DD";  // 	RES 	Dictionary of data types and their attributes
    public static final String  CONTEXT_DIC =  daxPrefix+"XD";  // 	RES 	Dictionary of data types and their attributes

    public static final String  OK_RES      =  daxPrefix+"OK";  // 	RES 	Error request
    public static final String  ERR_RES     =  daxPrefix+"ER";  // 	RES 	Error request
    public static final String  DIC_RELOAD  =  daxPrefix+"RL";   //	EVN 	Dictionary or attributes change, dictionary reload recommended

    // new sys message .. daxp configuration  : set pairSeparator ..

    public DaxMsgType(String value) {
        super(DaxTagConst.MSG_TYPE, value);
    }
}

/*

GET	Read (retrieve data)	/customers/123
POST	Create (new resource)	/orders
PUT	Replace entire resource	/customers/123
PATCH	Partially update	/customers/123 (only name)
DELETE	Delete resource	/customers/123
CALL - Remote Procedure Call
   or

CRUD
- Create
- Read
- Update
- Delete


*/

/*
Description ,
* */

/*
$DR
#HB   – Heartbeat
@LOG  – Log or trace message

Use the hybrid format:
SYS.DR, SYS.DD, SYS.ER, SYS.RA for predefined
and
CRM.DR, CNT.DD, ORD.ER for user messages.
*/