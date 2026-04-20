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

import java.util.Objects;

public final class DaxTag {
    int contextId;
    int tagId;

    public int getContextId() {
        return contextId;
    }

    public int getTagId() {
        return tagId;
    }

    public DaxTag(int contextId, int tagId) {
        this.contextId = contextId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DaxTag daxTag = (DaxTag) o;
        return contextId == daxTag.contextId && tagId == daxTag.tagId;
    }

    @Override public int hashCode() {
        return Objects.hash(contextId, tagId);
    }

}


/* TODO create refactoring DaxTag with protection
public final class DaxTag {
    private final int contextId; // Dobra praktyka: final, jeśli tagi są niemutowalne
    private final int tagId;

    // Prywatny konstruktor - nikt z zewnątrz nie użyje 'new'
    private DaxTag(int contextId, int tagId) {
        this.contextId = contextId;
        this.tagId = tagId;
    }

    // Publiczna metoda dla użytkowników
    public static DaxTag of(int contextId, int tagId) {
        if (contextId <= 0) {
            throw new IllegalArgumentException("User tags must have contextId > 0");
        }
        return new DaxTag(contextId, tagId);
    }

    // Wewnętrzne tagi systemowe dostępne tylko dla protokołu (np. w tym samym pakiece)
    static DaxTag createSystemTag(int tagId) {
        return new DaxTag(0, tagId);
    }

    // Gettery...
}
 */

