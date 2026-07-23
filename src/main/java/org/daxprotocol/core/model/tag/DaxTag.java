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
    private final int namespaceId;
    private final int tagId;

    // Private constructor to prevent external instantiation via 'new'
    private DaxTag(int namespaceId, int tagId) {
        this.namespaceId = namespaceId;
        this.tagId = tagId;
    }

    // Public factory method for user-defined tags
    public static DaxTag of(int namespaceId, int tagId) {
        if (namespaceId <= DaxCoreConstants.DAXP_NAMESPACE_ID) {
            throw new DaxTagException("User tags must have namespaceId > 0");
        }
        return new DaxTag(namespaceId, tagId);
    }

    // Internal system tags reserved for the protocol (e.g., used within the same package)
    public static DaxTag createCoreTag(int tagId) {
        return new DaxTag(DaxCoreConstants.DAXP_NAMESPACE_ID, tagId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaxTag daxTag = (DaxTag) o;
        return namespaceId == daxTag.namespaceId && tagId == daxTag.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespaceId, tagId);
    }

    public int getNamespaceId() {
        return namespaceId;
    }

    public int getTagId() {
        return tagId;
    }
}