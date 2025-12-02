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

package org.daxprotocol.core.config;

public final class DaxpConfig {
    private char pairSeparator;
    private int applicationContextId;
    private String tagFormat;
    private String encoding; //TODO
    public void setApplicationContextId(int applicationContextId) {
        this.applicationContextId = applicationContextId;
    }

    public void setTagFormat(String tagFormat) {
        this.tagFormat = tagFormat;
    }

    public  int getApplicationContextId() {
        return applicationContextId;
    }

    public String getTagFormat() {
        return tagFormat;
    }


    public DaxpConfig() {
    }

    public char getPairSeparator() {
        return pairSeparator;
    }

    public void setPairSeparator(char pairSeparator) {
        this.pairSeparator = pairSeparator;
    }

}