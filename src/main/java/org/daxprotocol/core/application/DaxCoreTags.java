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
package org.daxprotocol.core.application;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * Predefined DAXP Core tags reserved in range 1..999.
 * These tags are owned by the DAXP protocol and must not be reused
 * by application/domain dictionaries.
 */
public class DaxCoreTags {

    private static final Map<Integer, DaxTag> SYS_TAG_CACHE = new HashMap<>();

    private static DaxTag daxpSysTag(int tagId) {
        if (SYS_TAG_CACHE.containsKey(tagId)) {
            throw new IllegalStateException("Duplicate System Tag ID detected: " + tagId);
        }
        DaxTag tag = new DaxTag(DaxConfig.DAXP_CONTEXT_ID, tagId);
        SYS_TAG_CACHE.put(tagId, tag);
        return tag;
    }
    /**
     * Head TAGS
     * */
    public static final DaxTag MSG_TYPE  = daxpSysTag(9);
    public static final DaxTag MSG_BLOCK_COUNT = daxpSysTag(6);
    public static final DaxTag MSG_ROLE = daxpSysTag(18);
    public static final DaxTag MSG_CONTEXT = daxpSysTag(20); //Default context

    //TODO Create predefine sys DaxTag
    //DaxTag.newPredefineTag(DaxTagConst.ENUM_DESCRIPTION)

    private static final Set<DaxTag> headSet = Set.of(MSG_TYPE,
            MSG_BLOCK_COUNT
            //new DaxTag(MSG_CONTEXT)
    );


    public static boolean isHeadTag(DaxTag tag){
        return headSet.contains(tag);
    }

    /**********************************
     * UNKNOW_TAG
     */
     public static final DaxTag UNKNOW_TAG             = daxpSysTag(-1);

    /**********************************
     * Body tags
     */
    public static final DaxTag BLOCK_INDEX             = daxpSysTag(7);
    public static final DaxTag BLOCK_TYPE              = daxpSysTag(5);

    // Set of universal tags
//    F_NAME
//    DESCRIPTION
//    NAME
//    SYMBOL
//    STATUS


    public static final DaxTag FIELD_ID                = daxpSysTag(100);
    public static final DaxTag FIELD_NAME              = daxpSysTag(101);
    public static final DaxTag FIELD_STATUS            = daxpSysTag(102);
    public static final DaxTag FIELD_VALUE             = daxpSysTag(103);
    public static final DaxTag FIELD_VALUE_DEFAULT     = daxpSysTag(104);
    public static final DaxTag FIELD_VALUE_DESCRIPTION = daxpSysTag(105);
    public static final DaxTag FIELD_VALUE_SYMBOL      = daxpSysTag(106);
    public static final DaxTag FIELD_VALUE_PREFIX      = daxpSysTag(107);
    public static final DaxTag REQ_FIELD_LIST          = daxpSysTag(108);



    public static final DaxTag FIELD_ID_LIST           = daxpSysTag(115);
    public static final DaxTag FIELD_DATA_TYPE         = daxpSysTag(110);
    public static final DaxTag DTO_DATA_TYPE_ID        = daxpSysTag(118);
//    public static final DaxTag FIELD_ENUM_NAME         = daxpSysTag(111);
//    public static final DaxTag FIELD_GROUP_ID          = daxpSysTag(120);

    //------
    public static final DaxTag FIELD_ROLE         = daxpSysTag(122);



    public static final DaxTag ENUM_ID                 = daxpSysTag(129);
    public static final DaxTag ENUM_NAME               = daxpSysTag(130);
    public static final DaxTag ENUM_DESCRIPTION        = daxpSysTag(131);
    public static final DaxTag ENUM_VALUE              = daxpSysTag(132);
    public static final DaxTag ENUM_VALUE_DESCRIPTION  = daxpSysTag(133);
    public static final DaxTag ENUM_VALUE_SYMBOL       = daxpSysTag(134);
    public static final DaxTag ENUM_VALUE_LIST         = daxpSysTag(135);
    public static final DaxTag ENUM_VALUE_TAG          = daxpSysTag(136);
    //----
//    public static final DaxTag NAMESPACE          = daxpSysTag(144);
//    public static final DaxTag NAMESPACE_DESC     = daxpSysTag(144);


    //----
    public static final DaxTag GROUP_ID                = daxpSysTag(141);
    //??? public static final DaxTag DTO_MASTER_ID = daxpSysTag(142);
    public static final DaxTag DTO_NAME = daxpSysTag(143);
    public static final DaxTag DTO_NAMESPACE         = daxpSysTag(144);
    public static final DaxTag DTO_DESCRIPTION = daxpSysTag(145);


    /*****************************
     * Attributes
     */
    public static final DaxTag MESSAGE_TAGS             = daxpSysTag(151);
    public static final DaxTag MESSAGE_RELATED_MSGS      = daxpSysTag(152);
    //    public static final DaxTag MSG_REQ_IN_RESPOND_TAGS  = daxpSysTag(155);



    /*****************************
     * Attributes
     */
    public static final DaxTag ATR_RANGE_MIN_VALUE = daxpSysTag(161);
    public static final DaxTag ATR_RANGE_MAX_VALUE = daxpSysTag(162);
    public static final DaxTag ATR_PRECISION       = daxpSysTag(163);
    public static final DaxTag ATR_STEP_SIZE       = daxpSysTag(164);
    public static final DaxTag ATR_UNIT_ID         = daxpSysTag(165);
    public static final DaxTag ATR_NULLABLE        = daxpSysTag(166);
    public static final DaxTag ATR_SIZE_MAX        = daxpSysTag(167);
    public static final DaxTag ATR_SIZE_MIN        = daxpSysTag(168);
    public static final DaxTag ATR_READONLY        = daxpSysTag(169);
    /*****************************
     * Attributes of UI
     */
    public static final DaxTag ATR_UI_ITEM_TYPE    = daxpSysTag(208);
    public static final DaxTag ATR_UI_LABEL        = daxpSysTag(209);
    public static final DaxTag ATR_UI_DESCRIPTION  = daxpSysTag(210);
    public static final DaxTag ATR_UI_IS_EDITABLE  = daxpSysTag(220);

    /*****************************
     * Errors
     */
    //TODO create list of exception - errors like DAX-34444 or . context CRN-020202
    public static final DaxTag ERR_FIELD_NR        = daxpSysTag(224);
    public static final DaxTag ERR_FIELD_ID        = daxpSysTag(225);
    public static final DaxTag ERR_DESCRIPTION     = daxpSysTag(226);

    /******************************
     * Trailer tag
     * */
    public static final DaxTag CHECKSUM                = daxpSysTag(99);


    public static void init(DaxDictionary dic){
        dic.putAtrDataType(ATR_NULLABLE,Boolean.class);
        dic.putAtrDataType(BLOCK_INDEX,Integer.class);
    }


}
