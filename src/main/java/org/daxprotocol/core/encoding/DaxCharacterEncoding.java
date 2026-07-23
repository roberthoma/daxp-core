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
package org.daxprotocol.core.encoding;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

public enum DaxCharacterEncoding {

    // ASCII FAMILY
    US_ASCII("US-ASCII", DaxEncodingFamily.ASCII),

    // ISO-8859
    ISO_8859_1("ISO-8859-1", DaxEncodingFamily.ISO_8859),
    ISO_8859_2("ISO-8859-2", DaxEncodingFamily.ISO_8859),
    ISO_8859_5("ISO-8859-5", DaxEncodingFamily.ISO_8859),
    ISO_8859_6("ISO-8859-6", DaxEncodingFamily.ISO_8859),
    ISO_8859_7("ISO-8859-7", DaxEncodingFamily.ISO_8859),
    ISO_8859_9("ISO-8859-9", DaxEncodingFamily.ISO_8859),

    // WINDOWS CODE PAGES
    WINDOWS_1250("windows-1250", DaxEncodingFamily.WINDOWS),
    WINDOWS_1251("windows-1251", DaxEncodingFamily.WINDOWS),
    WINDOWS_1252("windows-1252", DaxEncodingFamily.WINDOWS),
    WINDOWS_1256("windows-1256", DaxEncodingFamily.WINDOWS),

    // UNICODE
    UTF_8("UTF-8", DaxEncodingFamily.UNICODE),
    UTF_16("UTF-16", DaxEncodingFamily.UNICODE),
    UTF_16LE("UTF-16LE", DaxEncodingFamily.UNICODE),
    UTF_16BE("UTF-16BE", DaxEncodingFamily.UNICODE),
    UTF_32("UTF-32", DaxEncodingFamily.UNICODE),
    UTF_32LE("UTF-32LE", DaxEncodingFamily.UNICODE),
    UTF_32BE("UTF-32BE", DaxEncodingFamily.UNICODE),

    // EAST ASIAN
    SHIFT_JIS("Shift_JIS", DaxEncodingFamily.EAST_ASIAN),
    EUC_JP("EUC-JP", DaxEncodingFamily.EAST_ASIAN),
    EUC_KR("EUC-KR", DaxEncodingFamily.EAST_ASIAN),
    BIG5("Big5", DaxEncodingFamily.EAST_ASIAN),
    GB2312("GB2312", DaxEncodingFamily.EAST_ASIAN),
    GBK("GBK", DaxEncodingFamily.EAST_ASIAN),

    // OTHER / LEGACY
    EBCDIC_CP037("IBM037", DaxEncodingFamily.EBCDIC),
    KOI8_R("KOI8-R", DaxEncodingFamily.LEGACY),
    MAC_ROMAN("x-MacRoman", DaxEncodingFamily.LEGACY),
    TIS_620("TIS-620", DaxEncodingFamily.LEGACY);

    private final String canonicalName;
    private final DaxEncodingFamily family;

    DaxCharacterEncoding(String canonicalName, DaxEncodingFamily family) {
        this.canonicalName = canonicalName;
        this.family = family;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public DaxEncodingFamily getFamily() {
        return family;
    }

    public Charset toCharset() {
        return Charset.forName(canonicalName);
    }
    //TODO add Dax Exception
    public static Optional<DaxCharacterEncoding> fromName(String name) {
        return Arrays.stream(values())
                .filter(e -> e.canonicalName.equalsIgnoreCase(name))
                .findFirst();
    }

    public boolean isUnicode() {
        return family == DaxEncodingFamily.UNICODE;
    }

    public boolean isSingleByte() {
        return family == DaxEncodingFamily.ASCII
                || family == DaxEncodingFamily.ISO_8859
                || family == DaxEncodingFamily.WINDOWS
                || family == DaxEncodingFamily.LEGACY;
    }
}