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

import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Fluent builder for {@link DaxHead}.
 */
public final class DaxHeadBuilder {

    private String msgType;     // required
    private String token;       // optional
    private String timestamp;   // optional, defaults to now() //TODO to should be date
    private int blockCount = 1; // starts from 1

    /** Sets required message type (tag 9). */
    public DaxHeadBuilder msgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    /** Sets token (tag 15). */
    public DaxHeadBuilder token(String token) {
        this.token = token;
        return this;
    }

    /** Sets timestamp (tag 16). */
    public DaxHeadBuilder timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /** Sets block count manually. */
    public DaxHeadBuilder blockCount(int blockCount) {
        this.blockCount = blockCount;
        return this;
    }

    /** Increments block counter (fluent). */
    public DaxHeadBuilder incrementBlock() {
        this.blockCount++;
        return this;
    }

    /** Builds immutable DaxHead instance. */
    public DaxHead build() {
        if (msgType == null || msgType.isEmpty()) {
            throw new IllegalStateException("msgType (tag 9) must be provided");
        }
        if (timestamp == null) {
            timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        }
        return new DaxHead(msgType, token, timestamp);
    }
}