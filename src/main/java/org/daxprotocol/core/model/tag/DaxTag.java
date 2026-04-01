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

import org.daxprotocol.core.config.DaxConfig;
import java.util.Objects;

public final class DaxTag {
    int contextId;
    int tagId;

    public int getContextId() {
        return contextId;
    }

    public void setContextId(int contextId) {
        this.contextId = contextId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public DaxTag(int contextId, int tagId) {
        this.contextId = contextId;
        this.tagId = tagId;
    }
    public static DaxTag newPredefineTag(Integer tag) {
        return new DaxTag(DaxConfig.DAXP_CONTEXT_ID,tag);
    }

    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DaxTag daxTag = (DaxTag) o;
        return contextId == daxTag.contextId && tagId == daxTag.tagId;
    }

    @Override public int hashCode() {
        return Objects.hash(contextId, tagId);
    }

}
