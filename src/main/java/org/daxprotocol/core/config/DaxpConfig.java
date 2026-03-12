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

package org.daxprotocol.core.config;

import org.daxprotocol.core.encoding.DaxCharacterEncoding;

public final class DaxpConfig {
    /*****************************************************
     *   DAXP Version
     */
    public static final String PROTOCOL_VERSION = "v0.1.0";


     /*****************************************************
     *  Preamble DAXP|V=v0.1.0|...
     *  DAXP| char after DAXP is default separator for current message
     */
    public static final String DAXP_PREAMBLE_PREFIX = "DAXP";
    public static final int    CHAR_SEPARATOR_IDX  = 4;

    public static final int    DAXP_MAX_TAG_ID = 255;

    /*****************************************************
     *  DAXP Context
     */
    public static final int    DAXP_CONTEXT_ID = 0;
    public static final String DAXP_CONTEXT_SYMBOL      = "DAXP";
    public static final String DAXP_CONTEXT_DESCRIPTION = "DAXP Context";


    /*****************************************************
     * Separators
     */
//    public static final char TAG_LIST_SEPARATOR    = ',';
//    public static final char CONTEXT_TAG_SEPARATOR = ',';

    public static final CharSequence TAG_LIST_SEPARATOR    = ";";
    public static final CharSequence VALUE_LIST_SEPARATOR    = ";";
    public static final CharSequence CONTEXT_TAG_SEPARATOR = ":";

    /** key=value */
    public static final char EQUAL = '=';

    /**  Pair separator on the WIRE (binary, non-printable). */
    //public static char PAIR_SEPARATOR = 0x0001;  //<<<< target
    public static char PAIR_SEPARATOR = '|';   // << ONLY for test


    private DaxCharacterEncoding defaultEncoding = DaxCharacterEncoding.UTF_8; //TODO from config file

    private int appContextId = 1;

    public int getNextContextId() {
        return appContextId + 1;
    }

//    private int   nextContextId = 2;
    private String appContextSymbol;
    private String appContextTagPrefix;
    private String appContextDescription;




    public String getAppContextTagPrefix() {
        return appContextTagPrefix;
    }

    public void setAppContextTagPrefix(String appContextTagPrefix) {
        this.appContextTagPrefix = appContextTagPrefix;
    }

    public void setAppContextSymbol(String appContextSymbol) {
        this.appContextSymbol = appContextSymbol;
    }

    public String getAppContextSymbol() {
        return appContextSymbol;
    }
    public String getAppContextDescription() {
        return appContextDescription;
    }

    public void setAppContextDescription(String appContextDescription) {
        this.appContextDescription = appContextDescription;
    }

    public int getAppContextId(){
        return appContextId;  // todo put in config file
    }
    public void setAppContextId(int appContextId) {
        this.appContextId = appContextId;
    }

    public DaxCharacterEncoding getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(DaxCharacterEncoding defaultEncoding)
    {
        this.defaultEncoding = defaultEncoding;
    }

    public DaxpConfig() {
    }

    public CharSequence getContextTafSeparator() {
        return DaxpConfig.CONTEXT_TAG_SEPARATOR;
    }

    public int getNextGroupId() {
        return 1;
    }

//    public char getPairSeparator() {
//        return pairSeparator;
//    }
//
//    public void setPairSeparator(char pairSeparator) {
//        this.pairSeparator = pairSeparator;
//    }
}