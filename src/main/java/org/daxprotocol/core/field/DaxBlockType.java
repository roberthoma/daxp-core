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

import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;

//Type of block application
public class DaxBlockType extends DaxPair<String> {

//    public static final DaxTag  TAG = new DaxTag(DaxTagConst.BLOCK_TYPE);
//    public static final String BLOCK_FIELD_LIST  = "F";
    public static final String BLOCK_DTO = "DTO";  //TODO change to Entity
 //   public static final String BLOCK_LIST        = "L";   // List of tags
    public static final String BLOCK_MESSAGE     = "MSG";
    public static final String BLOCK_CONTEXT     = "CTX";
    public static final String BLOCK_INSTANCE    = "INST";  // Group Instance (one record/row of values matching a Group Definition)
    public static final String BLOCK_TAG         = "TAG";
    public static final String BLOCK_ENUM        = "ENUM";  // N
    public static final String BLOCK_ENUM_VALUE  = "VAL";



    //TODO join !!!
    // 7=13|5=G|143=Customer|141=35|  and >>>
    //  7=23|5=F|115=2001,2002,FIX:53,2005,2073,2074,2075,2076,2077|141=35|
    // replace 141 by 144 GROUP_NAMESPACE
    // 7=13|5=G|143=Customer|115=2001,2002,FIX:53,2005,2073,2074,2075,2076,2077|144=Any.Name.Space|
    // it mean Any.Name.Space::Customer

    public DaxBlockType(String value) {
        super(DaxTagConst.BLOCK_TYPE, value);
    }
}
