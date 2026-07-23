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

package org.daxprotocol.core.config;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;

public final class DaxConfig {

    /*****************************************************
     *   DAXP Version
     */
    public static final String PROTOCOL_VERSION = "0.7beta";


    private DaxCharacterEncoding defaultEncoding = DaxCharacterEncoding.UTF_8; //TODO from config file

    private int appnamespaceId = 1;

    private String appnamespaceSymbol;
    private String appnamespaceTagPrefix;
    private String appnamespaceDescription;




    public String getAppnamespaceTagPrefix() {
        return appnamespaceTagPrefix;
    }

    public void setAppnamespaceTagPrefix(String appnamespaceTagPrefix) {
        this.appnamespaceTagPrefix = appnamespaceTagPrefix;
    }

    public void setAppnamespaceSymbol(String appnamespaceSymbol) {
        this.appnamespaceSymbol = appnamespaceSymbol;
    }

    public String getAppnamespaceSymbol() {
        return appnamespaceSymbol;
    }
    public String getAppnamespaceDescription() {
        return appnamespaceDescription;
    }

    public void setAppnamespaceDescription(String appnamespaceDescription) {
        this.appnamespaceDescription = appnamespaceDescription;
    }

    public int getAppnamespaceId(){
        if(appnamespaceId == -1){
            throw new RuntimeException("NOT INIT APPLICATION namespace");
        }

        return appnamespaceId;  // todo put in config file
    }
    public void setAppnamespaceId(int appnamespaceId) {
        this.appnamespaceId = appnamespaceId;
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


    //TODO get About  STRING
    //TODO get configuration String

}