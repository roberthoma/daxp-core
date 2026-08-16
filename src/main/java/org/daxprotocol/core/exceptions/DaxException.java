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

package org.daxprotocol.core.exceptions;

/**
 * Base class for all DAXP protocol related issues.
 * Uses RuntimeException for flexible error handling in stream processing.
 */
public  class DaxException extends RuntimeException {
    private final String daxErrorCode;

    // 1. Basic: Code + Message
    public DaxException(String daxErrorCode, String message) {
        super(String.format("[%s] %s", daxErrorCode, message));
        this.daxErrorCode = daxErrorCode;
    }

    // 2. Chaining: Code + Message + Original Cause (e.g., IOException)
    public DaxException(String daxErrorCode, String message, Throwable cause) {
        super(String.format("[%s] %s", daxErrorCode, message), cause);
        this.daxErrorCode = daxErrorCode;
    }

    // 3. Wrapping: Code + Original Cause only
    public DaxException(String daxErrorCode, Throwable cause) {
        super(cause != null ? String.format("[%s] %s", daxErrorCode, cause.getMessage()) : "[" + daxErrorCode + "]", cause);
        this.daxErrorCode = daxErrorCode;
    }

    // 4. Advanced: Control over suppression and stack trace
    protected DaxException(String daxErrorCode, String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(String.format("[%s] %s", daxErrorCode, message), cause, enableSuppression, writableStackTrace);
        this.daxErrorCode = daxErrorCode;
    }

    public String getDaxErrorCode   () {
        return daxErrorCode;
    }
}