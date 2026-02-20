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
package org.daxprotocol.core.codec;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.Set;

public class DaxTagConst {

//    /**
//     * Preamble tags
//     * */
//    public static final String DAXP = "DAXP"; // protocol identifier and version
//    public static final String TF   = "TF";   // tag format: DEC | HEX
//    public static final String EN   = "EN";   // encoding: ASCII | UTF8 | UTF16
//    public static final String CTX  = "CX";   // context: optional  - list FIX, CRM

    /**
     * Head TAGS
     * */
    public static final DaxTag MSG_TYPE  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,9);;
    public static final DaxTag MSG_BLOCK_COUNT = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,6);;
    public static final DaxTag MSG_ROLE = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,18);;
    public static final DaxTag MSG_CONTEXT = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,20);; //Default context

    //TODO Create predefine sys DaxTag
    //DaxTag.newPredefineTag(DaxTagConst.ENUM_DESCRIPTION)

    static Set<DaxTag> headSet = Set.of(MSG_TYPE,
            MSG_BLOCK_COUNT
            //new DaxTag(MSG_CONTEXT)
    );


    public static boolean isHeadTag(DaxTag tag){
        return headSet.contains(tag);
    }

    /**********************************
     * Body tags
     */
    public static final DaxTag BLOCK_INDEX             = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,7);;
    public static final DaxTag BLOCK_TYPE              = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,5);;

    // Set of universal tags
//    F_NAME
//    DESCRIPTION
//    SYMBOL
//    STATUS


    public static final DaxTag FIELD_ID                = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,100); ; //  FieldId     Integer
    public static final DaxTag FIELD_NAME              = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,101); ; //   FieldName   String
    public static final DaxTag FIELD_STATUS            = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,102); ; //  FieldStatus     Char    Indicates the current life-cycle state of a field  in the DAXP dictionary.
    public static final DaxTag FIELD_VALUE             = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,103); ; //  FieldValue  <atr_data_type>
    public static final DaxTag FIELD_VALUE_DEFAULT     = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,104); ; //  FieldDefaultValue   <atr_data_type>
    public static final DaxTag FIELD_VALUE_DESCRIPTION = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,105); ; //  Field Value Description use in Value list
    public static final DaxTag FIELD_VALUE_SYMBOL      = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,106); ; //  Field Value Symbol use in Value list
    public static final DaxTag FIELD_VALUE_PREFIX      = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,107); ; //  Field Value Symbol use in Value list

    public static final DaxTag FIELD_ID_LIST           = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,115); ; //  FieldIdList     List<Integer>
    public static final DaxTag FIELD_DATA_TYPE         = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,110); ; //
    public static final DaxTag FIELD_DATA_TYPE_TAG         = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID, 110) ; //
//    public static final DaxTag FIELD_ENUM_NAME         = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,111); ; //
//    public static final DaxTag FIELD_GROUP_ID          = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,120); ; //

    //------
    public static final DaxTag FIELD_ROLE         = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,122); ; //



    public static final DaxTag ENUM_NAME               = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,130); ; //
    public static final DaxTag ENUM_DESCRIPTION        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,131); ; //
    public static final DaxTag ENUM_VALUE              = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,132); ; //
    public static final DaxTag ENUM_VALUE_ID           = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,133); ; //
    public static final DaxTag ENUM_VALUE_SYMBOL       = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,134); ; //
    public static final DaxTag ENUM_VALUE_DESCRIPTION  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,135); ; //
    //----
    public static final DaxTag NAMESPACE          = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,144); ; //
    public static final DaxTag NAMESPACE_DESC     = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,144); ; //


    //----
    public static final DaxTag GROUP_ID                = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,141); ; //
    public static final DaxTag GROUP_MASTER_ID         = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,142); ; //
    public static final DaxTag GROUP_NAME              = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,143); ; //
    public static final DaxTag GROUP_NAMESPACE         = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,144); ; //
    public static final DaxTag GROUP_DESCRIPTION       = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,145); ; //

    //    Character[1]

    public static final DaxTag ATR_RANGE_MIN_VALUE = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,161); ; //          <atr_data_type>     Minimum value
    public static final DaxTag ATR_RANGE_MAX_VALUE = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,162); ; //          <atr_data_type>     Maximum value
    public static final DaxTag ATR_PRECISION       = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,163); ; //        Integer     Double precision
    public static final DaxTag ATR_STEP_SIZE       = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,164); ; //        <atr_data_type>
    public static final DaxTag ATR_UNIT_ID         = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,166); ; //
    public static final DaxTag ATR_NULLABLE        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,165); ; //
    public static final DaxTag ATR_SIZE_MAX        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,166);;
    public static final DaxTag ATR_SIZE_MIN        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,167);;

    /*****************************
     * Attributes of UI
     */
    public static final DaxTag ATR_UI_ITEM_TYPE    = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,208); ; //
    public static final DaxTag ATR_UI_LABEL        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,209); ; //     UiLabel     String
    public static final DaxTag ATR_UI_LABEL_TAG        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID, 209) ; //     UiLabel     String
    public static final DaxTag ATR_UI_DESCRIPTION  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,210); ; //   UiDescription   String
    public static final DaxTag ATR_UI_IS_EDITABLE  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,220); ; //       Boolean

    /*****************************
     * Errors of UI
     */
    public static final DaxTag ERR_FIELD_NR        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,224); ; //
    public static final DaxTag ERR_FIELD_ID        = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,225); ; //
    public static final DaxTag ERR_DESCRIPTION     = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,226); ; //

    /******************************
     * Trailer tag
     * */
    public static final DaxTag CHECKSUM                = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,99); ; //     Checksum    Integer


}
