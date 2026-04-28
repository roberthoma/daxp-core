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

package org.daxprotocol.core.datatype;

import org.daxprotocol.core.model.value.DaxValue;
import org.daxprotocol.core.application.DaxCoreTags;

//Type of block application
// TODO refactoring to ENUM
public class DaxBlockType extends DaxValue<String> {

    public static final char BLOCK_ENTITY      = 'E';
    public static final char BLOCK_MESSAGE     = 'M';
    public static final char BLOCK_CONTEXT     = 'X';
    public static final char BLOCK_INSTANCE    = 'I';  // Group Instance (one record/row of values matching a Group Definition)
    public static final char BLOCK_TAG         = 'T';
    public static final char BLOCK_FIELD       = 'F';
    public static final char BLOCK_COLLECTION  = 'C';   // Definition of collection : Name , Allow duplication , has key ....
    public static final char BLOCK_VALUE       = 'V';   // for example Collection value


//    public static final String BLOCK_LIST        = "L";   // List of tags
//    public static final DaxTag  TAG = new DaxTag(DaxTagConst.BLOCK_TYPE);


    public DaxBlockType(char value) {
        super(DaxCoreTags.BLOCK_TYPE, value);
    }
}
