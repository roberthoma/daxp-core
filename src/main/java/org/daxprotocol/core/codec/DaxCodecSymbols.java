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

package org.daxprotocol.core.codec;

public final class DaxCodecSymbols {
    private DaxCodecSymbols() {}

    /** key=value */
    public static final char EQUAL = '=';

    /** Pair separator on the WIRE (binary, non-printable). */
    public static char PAIR_SEPARATOR = 0x0001; // SOH  // Default // todo


//    /** Pair separator for logs/tests (printable). */
//    public static final char FIELD_SEP_DEBUG = '|';

//    /** Separator between the preamble pairs. */
//    public static final char PREAMBLE_PAR_SEPARATOR = ;

//    /** End of message. */
//    public static final String MSG_END = "\r\n";
}