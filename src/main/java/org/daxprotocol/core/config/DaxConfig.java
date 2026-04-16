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

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;

public final class DaxConfig {

    /*****************************************************
     *   DAXP Version
     */
    public static final String PROTOCOL_VERSION = "v0.1.0";


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

    public DaxConfig() {
    }

//    public CharSequence getContextTafSeparator() {
//        return DaxConfig.CONTEXT_TAG_SEPARATOR;
//    }

    public CharSequence getTagListSeparator(){
        return DaxCoreConstants.TAG_LIST_SEPARATOR;
    }

    //TODO get About  STRING
    //TODO get configuration String

}