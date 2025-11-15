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
package org.daxprotocol.core.codec;
import java.util.Set;

public class DaxTag {

    /**
     * Preamble tags
     * */
    public static final String DAXP = "DAXP"; // protocol identifier and version
    public static final String TF   = "TF";   // tag format: DEC | HEX
    public static final String EN   = "EN";   // encoding: ASCII | UTF8 | UTF16
    public static final String CTX  = "CX";   // context: optional  - list FIX, CRM

    /**
    * Head TAGS
    * */
    public static final int MSG_TYPE  = 9;
    public static final int MSG_BLOCK_COUNT = 6;
    public static final int MSG_ROLE = 18;

    static Set<Integer> headSet = Set.of(MSG_TYPE, MSG_BLOCK_COUNT );


    public static boolean isHeadTag(int tag){
        return headSet.contains(tag);
    }

    /**********************************
     * Body tags
     */
    public static final int BLOCK_INDEX             = 7;
    public static final int BLOCK_TYPE              = 5;


    public static final int FIELD_ID                = 100 ; //  FieldId     Integer
    public static final int FIELD_NAME              = 101 ; //   FieldName   String
    public static final int FIELD_STATUS            = 102 ; //  FieldStatus     Char    Indicates the current life-cycle state of a field  in the DAXP dictionary.
    public static final int FIELD_VALUE             = 103 ; //  FieldValue  <atr_data_type>
    public static final int FIELD_VALUE_DEFAULT     = 104 ; //  FieldDefaultValue   <atr_data_type>
    public static final int FIELD_VALUE_DESCRIPTION = 105 ; //  Field Value Description use in Value list
    public static final int FIELD_VALUE_SYMBOL      = 106 ; //  Field Value Symbol use in Value list
    public static final int FIELD_ID_LIST           = 115 ; //  FieldIdList     List<Integer>
    public static final int FIELD_DATA_TYPE         = 110 ; //
//    public static final int FIELD_ENUM_NAME         = 111 ; //
    public static final int FIELD_GROUP_ID          = 120 ; //

    //------

    public static final int ENUM_NAME               = 130 ; //
    public static final int ENUM_DESCRIPTION        = 131 ; //
    public static final int ENUM_VALUE              = 132 ; //
    public static final int ENUM_VALUE_ID           = 133 ; //
    public static final int ENUM_VALUE_SYMBOL       = 134 ; //
    public static final int ENUM_VALUE_DESCRIPTION  = 135 ; //
    //----
    public static final int NAMESPACE          = 144 ; //
    public static final int NAMESPACE_DESC     = 144 ; //


    //----
    public static final int GROUP_ID                = 141 ; //
    public static final int GROUP_MASTER_ID         = 142 ; //
    public static final int GROUP_NAME              = 143 ; //
    public static final int GROUP_NAMESPACE         = 144 ; //
    public static final int GROUP_DESCRIPTION       = 145 ; //

    //    Character[1]

    public static final int ATR_RANGE_MIN_VALUE = 161 ; //          <atr_data_type>     Minimum value
    public static final int ATR_RANGE_MAX_VALUE = 162 ; //          <atr_data_type>     Maximum value
    public static final int ATR_PRECISION       = 163 ; //        Integer     Double precision
    public static final int ATR_STEP_SIZE       = 164 ; //        <atr_data_type>
    public static final int ATR_UNIT_ID         = 166 ; //
    public static final int ATR_NULLABLE        = 165 ; //
    public static final int ATR_SIZE_MAX        = 166;
    public static final int ATR_SIZE_MIN        = 167;

    /*****************************
     * Attributes of UI
     */
    public static final int ATR_UI_ITEM_TYPE    = 208 ; //
    public static final int ATR_UI_LABEL        = 209 ; //     UiLabel     String
    public static final int ATR_UI_DESCRIPTION  = 210 ; //   UiDescription   String
    public static final int ATR_UI_IS_EDITABLE  = 220 ; //       Boolean

    public static final int ERR_FIELD_NR        = 224 ; //
    public static final int ERR_FIELD_ID        = 225 ; //
    public static final int ERR_DESCRIPTION     = 226 ; //

    /******************************
     * Trailer tag
    * */
    public static final int CHECKSUM                = 99 ; //     Checksum    Integer


}
