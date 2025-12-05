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


import org.daxprotocol.core.context.DaxContextMapper;

import java.util.concurrent.atomic.AtomicInteger;

//TODO Add builder
public final class DaxpConfig {
    public static final String DAX_CONTEXT_SYMBOL = "SYS";
    public static final int    DAX_CONTEXT_ID = 0;
    public static  String      APP_CONTEXT_SYMBOL ;
    public static  int         APP_CONTEXT_ID ;


    /** Generator used for assigning IDs to unknown contexts :   AtomicInteger nextContextId */
    public static final int   CONTEXT_DYNAMIC_START_ID = 10;

    /** DAXP| char after DAXP is default separator for current message*/
    public static final int    SEPARATOR_IDX    = 4;

    public static final String PROTOCOL_VERSION = "1";
    public static final String DEFAULT_ENCODING = "UTF8";
    public static final int    MAX_DAXP_TAG_ID  = 255;

    private int applicationContextId;
    private String encoding;


    //TODO NewLine after block

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setApplicationContextId(int applicationContextId) {
        this.applicationContextId = applicationContextId;
    }


    public  int getApplicationContextId() {
        return applicationContextId;
    }
    public  String getApplicationContext() {
        return DaxContextMapper.getContextSymbol(applicationContextId);
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