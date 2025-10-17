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
package org.daxprotocol.core.model.head;

public enum DaxHeadTag {
    MSG_TYPE(9),
    TOKEN(15),
    BLOCK_COUNT(6);

    private final int tag;

    DaxHeadTag(int tag) {
        this.tag = tag;
    }

    public int tag() {
        return tag;
    }

    public static DaxHeadTag fromTag(int tag) {
        for (DaxHeadTag t : values()) {
            if (t.tag == tag) return t;
        }
        return null;
    }
}
