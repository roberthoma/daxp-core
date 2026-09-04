/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.registries.DaxSemanticRegistry;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.HashMap;
import java.util.Map;

/**
 * Predefined DAXP Core tags reserved by namespace alias $:
 * These tags are owned by the DAXP protocol and must not be reused
 * by application/domain dictionaries.
 */

//TODO Add name for each tag
//TODO rebuid to DaxBaseDictionary
public class DaxCoreTags {

    private static final Map<Integer, DaxTag> SYS_TAG_CACHE = new HashMap<>();
    private static final Map<Integer, DaxDataType> SYS_TAG_DATATYPE = new HashMap<>();

    private static DaxTag daxpSysTag(int tagId) {
       return daxpSysTag(tagId, DaxDataType.UNKNOWN) ;
    }

    private static DaxDataType getDataType(DaxTag tag){
        return SYS_TAG_DATATYPE.get(tag.getTagId());
    }

    private static DaxTag daxpSysTag(int tagId, DaxDataType dataType) {
        if (SYS_TAG_CACHE.containsKey(tagId)) {
            throw new IllegalStateException("Duplicate DAXP TAG ID detected: " + tagId);
        }
        DaxTag tag =  DaxTag.createCoreTag (tagId) ;
        SYS_TAG_CACHE.put(tagId, tag);
        SYS_TAG_DATATYPE.put(tagId, dataType);
        return tag;
    }




    /**********************************
     * UNKNOW_TAG  ;(
     */
     public static final DaxTag UNKNOWN_TAG             = daxpSysTag(-1);

    /**
     * Head TAGS
     * */
    public static final DaxTag MSG_TYPE  = daxpSysTag(1,DaxDataType.MESSAGE_TYPE);
    public static final DaxTag MSG_ROLE = daxpSysTag(2);
    public static final DaxTag MSG_BLOCK_QUANTITY = daxpSysTag(3, DaxDataType.QUANTITY);

    /**********************************
     * Body tags
     */
    public static final DaxTag BLOCK_INDEX             = daxpSysTag(4, DaxDataType.QUANTITY);
    public static final DaxTag BLOCK_TYPE              = daxpSysTag(5);
  //  public static final DaxTag REFERENCE_BLOCK         = daxpSysTag(8);   is free :)

    /******************************
     * Trailer tag
     * */
    public static final DaxTag CHECKSUM                = daxpSysTag(9);


    ///Universal tag
    public static final DaxTag ENTRY_TAG               = daxpSysTag(6);
    public static final DaxTag ENTRY_OWNER_ID          = daxpSysTag(7);
    public static final DaxTag ENTRY_NAME              = daxpSysTag(11);
    public static final DaxTag ENTRY_DESCRIPTION       = daxpSysTag(12);
    public static final DaxTag ENTRY_SYMBOL            = daxpSysTag(15);

//    public static final DaxTag ENTRY_SCHEMA            = daxpSysTag(???);

    public static final DaxTag TAG_OWNER_ID          = daxpSysTag(111);


    public static final DaxTag ENTRY_VALUE             = daxpSysTag(103);
    public static final DaxTag ENTRY_VALUE_DEFAULT     = daxpSysTag(104);

    public static final DaxTag FIELD_VALUE_PREFIX      = daxpSysTag(107);
    public static final DaxTag REQ_FIELD_LIST          = daxpSysTag(108);


    public static final DaxTag TAG_LIST                = daxpSysTag(115);

    public static final DaxTag ENTITY_DATA_TYPE_ID     = daxpSysTag(118);

    public static final DaxTag FIELD_ROLE         = daxpSysTag(122);


    //----
    //    public static final DaxTag SCHEMA           = daxpSysTag(144);
    //    public static final DaxTag SCHEMA_DESC     = daxpSysTag(144);


    //----
    public static final DaxTag ENTITY_NAME = daxpSysTag(143);

    //----------
    //Use in multi message transaction , tag is head item
    public static final DaxTag MSG_TRANSACTION_ID          = daxpSysTag(150);

    /*****************************
     * Attributes
     */
    public static final DaxTag MESSAGE_TAGS             = daxpSysTag(151);
    public static final DaxTag MESSAGE_RELATED_MSGS      = daxpSysTag(152);
    //    public static final DaxTag MSG_REQ_IN_RESPOND_TAGS  = daxpSysTag(155);



    /*****************************
     * Attributes
     */
    public static final DaxTag ATR_DATA_TYPE           = daxpSysTag(20);

    /// Reference to collection or entity definition
    public static final DaxTag ATR_REF_DATA_TYPE       = daxpSysTag(21);

    public static final DaxTag ATR_RANGE_MIN_VALUE = daxpSysTag(161);
    public static final DaxTag ATR_RANGE_MAX_VALUE = daxpSysTag(162);
    public static final DaxTag ATR_PRECISION       = daxpSysTag(163);
    public static final DaxTag ATR_STEP_SIZE       = daxpSysTag(164);
    public static final DaxTag ATR_UNIT_ID         = daxpSysTag(165);
    public static final DaxTag ATR_NULLABLE        = daxpSysTag(166);
    public static final DaxTag ATR_SIZE_MAX        = daxpSysTag(167);
    public static final DaxTag ATR_SIZE_MIN        = daxpSysTag(168);
    public static final DaxTag ATR_READONLY        = daxpSysTag(169);

    //----------------------------------------------------------------------------------
    public static final DaxTag ATR_IS_DEPRECATED   = daxpSysTag(170);
/*
    public @interface DaxpDeprecated {
        String since() default ""; //This same as original Deprecated
        boolean forRemoval() default false; //This same as original  Deprecated
        String  removalVersion() default "";
        String  replacement() default "";
        String  reason() default "";
    }
*/
    //----------------------------------------------------------------------------------



    public static final DaxTag VALUE_IS_EDITABLE   = daxpSysTag(172);


    public static final DaxTag COLLECTION_ID = daxpSysTag(129);
    public static final DaxTag COLLECTION_NAME               = daxpSysTag(130);
    //    public static final DaxTag COL_DESCRIPTION        = daxpSysTag(131);
    public static final DaxTag COLLECTION_KEY = daxpSysTag(132);
    public static final DaxTag COLLECTION_VALUE = daxpSysTag(133);

//    public static final DaxTag COL_VALUE_DESCRIPTION  = daxpSysTag(133);
//    public static final DaxTag COL_VALUE_SYMBOL       = daxpSysTag(134);
//    public static final DaxTag COL_VALUE_LIST         = daxpSysTag(135);
//    public static final DaxTag COL_VALUE_TAG          = daxpSysTag(136);

    public static final DaxTag COLLECTION_ALLOW_DUPLICATES = daxpSysTag(210);
    public static final DaxTag COLLECTION_HAS_KEY = daxpSysTag(211);

    public static final DaxTag COLLECTION_IS_ORDERED = daxpSysTag(212);
    public static final DaxTag COLLECTION_IS_NAVIGABLE = daxpSysTag(213);
    public static final DaxTag COLLECTION_IS_DICTIONARY = daxpSysTag(214);
    public static final DaxTag COLLECTION_IS_CLOSED = daxpSysTag(215);

    public static final DaxTag COLLECTION_KEY_DATA_TYPE = daxpSysTag(225);
    public static final DaxTag COLLECTION_KEY_TYPE_REF_ID = daxpSysTag(226);

    public static final DaxTag COLLECTION_VALUE_DATA_TYPE = daxpSysTag(227);
    public static final DaxTag COLLECTION_VALUE_TYPE_REF_ID = daxpSysTag(228);

    public static final DaxTag COLLECTION_BULK_VALUE = daxpSysTag(230);



    // COLLECTION_PK,  ATTR_UNIQUE
    //Foreign Key	ATTR_REF_namespace	Points to a different namespace (e.g., CRM:ID) as a relational constraint.

    /*****************************
     * Attributes of UI
     */
    //    public static final DaxTag ATR_UI_ITEM_TYPE    = daxpSysTag(208);
    //    public static final DaxTag ATR_UI_LABEL        = daxpSysTag(209);
    //    public static final DaxTag ATR_UI_DESCRIPTION  = daxpSysTag(210);
    //    public static final DaxTag ATR_UI_IS_EDITABLE  = daxpSysTag(220);

    /*****************************
     * Errors
     */
    //TODO create list of exception - errors like DAX-34444 or . namespace CRN-020202
    public static final DaxTag ERR_FIELD_NR        = daxpSysTag(324);
    public static final DaxTag ERR_FIELD_ID        = daxpSysTag(325);
    public static final DaxTag ERR_DESCRIPTION     = daxpSysTag(326);


    //LOGs tags
    public static final DaxTag LOG_LEVEL        = daxpSysTag(250);
    //LOG_LEVEL
    //LOG_TXT



    public static void init(DaxSemanticRegistry dic){
//        dic.putAtrDataType(ATR_NULLABLE,Boolean.class);
//        dic.putAtrDataType(BLOCK_INDEX,Integer.class);
    }


}
