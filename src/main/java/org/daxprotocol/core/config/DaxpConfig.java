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

public final class DaxpConfig {
    public static final String PROTOCOL_VERSION = "1";

    /** DAXP| char after DAXP is default separator for current message*/
    public static final int SEPARATOR_IDX    = 4;

    public static final int MAX_DAXP_TAG_ID  = 255;
    public static final int DAXP_CONTEXT_ID = 0;
    public static final String DAX_CONTEXT_SYMBOL = "SYS";
    public static final String DAX_CONTEXT_DESCRIPTION = "Daxp Context";


    private   String  defaultEncoding  = "UTF8";

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
        return 1;  // todo put in config file
    }

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public DaxpConfig() {
    }

//    public char getPairSeparator() {
//        return pairSeparator;
//    }
//
//    public void setPairSeparator(char pairSeparator) {
//        this.pairSeparator = pairSeparator;
//    }
//    public char getContextTagSeparator() {
//        return contextTagSeparator;
//    }
//
//    public void setContextTagSeparator(char contextTagSeparator) {
//        this.contextTagSeparator = contextTagSeparator;
//    }

}