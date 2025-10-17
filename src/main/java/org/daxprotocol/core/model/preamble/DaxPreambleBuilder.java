package org.daxprotocol.core.model.preamble;

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

import org.daxprotocol.core.types.DaxEncoding;
import org.daxprotocol.core.types.DaxTagFormat;

/**
 * Fluent builder for constructing a DaxPreamble instance.
 */
public final class DaxPreambleBuilder {
    private String protocolVersion = "1";
    private DaxTagFormat tagFormat = DaxTagFormat.DEC;
    private DaxEncoding encoding = DaxEncoding.UTF8;
    private String context = null;

    public DaxPreambleBuilder version(String version) {
        this.protocolVersion = version;
        return this;
    }

    public DaxPreambleBuilder tagFormat(DaxTagFormat format) {
        this.tagFormat = format;
        return this;
    }

    public DaxPreambleBuilder encoding(DaxEncoding encoding) {
        this.encoding = encoding;
        return this;
    }

    public DaxPreambleBuilder context(String context) {
        this.context = context;
        return this;
    }

    public DaxPreamble build() {
        DaxPreamble preamble = new DaxPreamble();
        preamble.setProtocolVersion(protocolVersion)
                .setTagFormat(tagFormat)
                .setEncoding(encoding)
                .setContext(context);
        return preamble;
    }
}