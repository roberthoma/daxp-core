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
package org.daxprotocol.core.model.preamble;

import java.util.*;

public enum DaxPreambleTag {
    DAXP("DAXP"),
    VERSION("V"),  // DAXP rules Version
    IMPLEMENTATION("I"),  //Implementation Version
    ENCODING("EN"),       //Encoding
    MSG_NAMESPACE("NS"),
    MSG_QUANTITY("MQ"),
    MSG_SENDER("SN");


    private final String tag;

    // Map provides O(1) lookup to return the actual Enum object
    private static final Map<String, DaxPreambleTag> BY_TAG = new HashMap<>();

    static {
        for (DaxPreambleTag t : values()) {
            BY_TAG.put(t.tag, t);
        }
    }

    DaxPreambleTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    /**
     * Checks if a string is a valid tag.
     */
    public static boolean contains(String value) {
        if (value == null) return false;
        return BY_TAG.containsKey(value.toUpperCase().trim());
    }

    /**
     * Returns the Enum constant for the given tag string.
     * * @param value The tag string (e.g., "MC")
     * @return The matching DaxPreambleTag, or null if not found.
     */
    public static DaxPreambleTag fromTag(String value) {
        if (value == null) return null;
        return BY_TAG.get(value.toUpperCase().trim());
    }
}