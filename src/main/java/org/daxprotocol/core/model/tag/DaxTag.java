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

package org.daxprotocol.core.model.tag;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.exceptions.DaxTagException;

import java.util.Objects;

public final class DaxTag {
    private final int contextId;
    private final int tagId;

    // Private constructor to prevent external instantiation via 'new'
    private DaxTag(int contextId, int tagId) {
        this.contextId = contextId;
        this.tagId = tagId;
    }

    // Public factory method for user-defined tags
    public static DaxTag of(int contextId, int tagId) {
        if (contextId <= DaxCoreConstants.DAXP_CONTEXT_ID) {
            throw new DaxTagException("User tags must have contextId > 0");
        }
        return new DaxTag(contextId, tagId);
    }

    // Internal system tags reserved for the protocol (e.g., used within the same package)
    public static DaxTag createCoreTag(int tagId) {
        return new DaxTag(DaxCoreConstants.DAXP_CONTEXT_ID, tagId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaxTag daxTag = (DaxTag) o;
        return contextId == daxTag.contextId && tagId == daxTag.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contextId, tagId);
    }

    public int getContextId() {
        return contextId;
    }

    public int getTagId() {
        return tagId;
    }
}