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


package org.daxprotocol.core.datatype;
/**
 * Defines the semantic type of a DAXP reference.
 *
 * A reference connects the current tag with another tag or definition
 * and describes the role of that relationship.
 *
 * <p>The referenced tag is typically provided by the DAXP reference
 * attribute, while this enum specifies how that reference should be
 * interpreted.</p>
 *
 * <ul>
 *     <li>{@link #TYPE} - the referenced tag defines the concrete type
 *         or structure of the current tag.</li>
 *     <li>{@link #VALUE} - the current value belongs to, or is defined by,
 *         the referenced domain or collection.</li>
 *     <li>{@link #KEY} - the current value represents a key defined by
 *         the referenced collection or structure.</li>
 *     <li>{@link #NONE} - the current tag does not use a semantic reference.</li>
 * </ul>
 */
public enum DaxReferenceType {

    /**
     * The referenced tag defines the concrete type or structure
     * of the current tag.
     *
     * Example:
     * A field of type ENT may reference the entity definition
     * that describes the carried entity instance.
     */
    TYPE(
            "TYPE",
            "Reference to the definition of the current tag type"
    ),

    /**
     * The current value is defined by or belongs to
     * the referenced domain or collection.
     *
     * Example:
     * An enum value represented as STR may reference the collection
     * containing all allowed enum values.
     */
    VALUE(
            "VALUE",
            "Reference to a domain or collection defining the current value"
    ),

    /**
     * The current value represents a key associated with
     * the referenced collection or structure.
     */
    KEY(
            "KEY",
            "Reference to a key defined by the referenced collection or structure"
    ),

    /**
     * The current tag does not use a semantic reference.
     * Its value can be interpreted directly from its DAXP data type.
     */
    NONE(
            "NONE",
            "No semantic reference; the value is defined directly by the tag"
    );

    private final String code;
    private final String description;

    DaxReferenceType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}